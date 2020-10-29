package com.tz.fdfs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tz.fdfs.bean.ChunkInfo;
import com.tz.fdfs.bean.ChunkManifest;
import com.tz.fdfs.bean.FileStatus;
import com.tz.fdfs.bean.MultipartFileParam;
import com.tz.fdfs.config.SeaweedFileService;
import com.tz.fdfs.util.EncodingDetect;
import com.tz.fdfs.util.HtmlParse;
import com.tz.fdfs.util.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import net.anumbrella.seaweedfs.core.file.FileHandleStatus;
import net.anumbrella.seaweedfs.core.http.StreamResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author https://github.com/TianPuJun @无痕
 * @ClassName FileController
 * @Description
 * @Date 09:43 2020/3/12
 **/
@Slf4j
@RestController
@RequestMapping("/api")
public class FileController {
    @Autowired
    private SeaweedFileService seaweedFileService;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${file-upload.url}")
    private String fidUrl;

    @Value("${file-upload.submit}")
    private String submit;

    @Value("${file-upload.public-url}")
    private String publicUrl;

    @Autowired
    private RestTemplate restTemplate;
    /**
     * 文件分块信息保存
     */
    private static final String FILE_UPLOAD_CHUNK_KEY = "FILE:UPLOAD:CHUNK:";


    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Object upload(@RequestParam MultipartFile file) throws IOException {
        FileHandleStatus fileHandleStatus = seaweedFileService.saveFile(file);
        return fileHandleStatus.toString();
    }

    /**
     * 分块上传文件
     *
     * @param param
     * @return
     * @throws IOException
     */
    @PostMapping("/blockUpload")
    public Object blockUpload(MultipartFileParam param) throws IOException {

        /**
         * 对分块文件上传
         * seaweedfs返回的文件信息封装对象保存到redis
         * key= 总文件的md5，value为分块文件的信息
         * 返回
         */

        String key = FILE_UPLOAD_CHUNK_KEY + param.getMd5();
        // 上传文件
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        Resource resource = new InputStreamResource(param.getFile().getInputStream());
        //FileSystemResource fileSystemResource = new FileSystemResource();
        //设置请求体，注意是LinkedMultiValueMap
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", resource);
        form.add("filename", param.getName());

        //用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        FileStatus fileStatus = restTemplate.postForObject(submit, files, FileStatus.class);
        // 封装seaweedfs 需要的分块文件信息
        ChunkInfo chunkInfo = new ChunkInfo();
        chunkInfo.setFid(fileStatus.getFid());
        chunkInfo.setOffset(param.getChunkSize() * param.getChunk());
        chunkInfo.setSize(fileStatus.getSize());
        // 缓存在redis
        JedisUtils.hset(key, param.getChunkMd5(), objectMapper.writeValueAsString(chunkInfo), 0);
        return objectMapper.writeValueAsString(chunkInfo);
    }


    @PostMapping("/mergeFiles")
    public Object mergeFiles(MultipartFileParam param) throws Exception {
        /**
         * 根据文件md5获取redis中所有缓存的块文件信息
         * 封装seaweedfs需要的合并文件数据样式
         * 调用携带？cm=true的接口进行文件上传
         * 返回上传之后的文件信息
         */
        String key = FILE_UPLOAD_CHUNK_KEY + param.getMd5();
        Map<String, String> stringStringMap = JedisUtils.hgetAll(key, 0);
        Collection<String> values = stringStringMap.values();
        List<ChunkInfo> list = new LinkedList<>();
        for (String value : values) {
            ChunkInfo chunkInfo = objectMapper.readValue(value, ChunkInfo.class);
            list.add(chunkInfo);
        }
        long sum = list.stream().mapToLong(ChunkInfo::getSize).sum();
        ChunkManifest chunkManiFest = new ChunkManifest();
        chunkManiFest.setChunks(list);
        chunkManiFest.setMime(param.getContentType());
        chunkManiFest.setName(param.getName());
        chunkManiFest.setSize(sum);
        String text = objectMapper.writeValueAsString(chunkManiFest);
        log.info("mergeFiles 合并文件json [{}]", text);
        FileStatus fileStatus = restTemplate.getForEntity(fidUrl, FileStatus.class).getBody();
        log.info("mergeFiles getFileDetail fileStatus [{}] [{}]", publicUrl, fileStatus);
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        seaweedFileService.uploadFileWithCm(publicUrl, fileStatus.getFid(), param.getName() + ".json", inputStream, param.getContentType());
        log.info("mergeFiles 文件合并成功 fid [{}]", fileStatus.getFid());
        return objectMapper.writeValueAsString(fileStatus);
    }


    /**
     * 获取文件信息
     *
     * @param fid
     * @return
     * @throws IOException
     */
    @RequestMapping("/getFileStatus/{fid}")
    public FileHandleStatus getFileStatus(@PathVariable(value = "fid") String fid) throws IOException {
        FileHandleStatus fileStatus = seaweedFileService.getFileStatus(fid);
        return fileStatus;
    }

    /**
     * 加载文件流
     *
     * @param request
     * @param response
     * @param fid      6,01788f2afc0f
     * @throws IOException
     */
    @GetMapping("/loadFileStream/{fid}")
    public void loadFileStream1(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable(value = "fid") String fid) throws IOException {
        log.info("服务器读取数据1 fid[{}]", fid);
        StreamResponse fileStream = seaweedFileService.getFileStream(fid);
        response.setStatus(fileStream.getHttpResponseStatusCode());
        getResponse(response, fileStream.getInputStream());
    }

    /**
     * 分块加载文件 版本注意自定义版本，查看README
     *
     * @param request
     * @param response
     * @param fid
     * @throws IOException
     */
    @GetMapping("/loadFileBlockStream/{fid}")
    public void loadFileBlockStream(HttpServletRequest request, HttpServletResponse response,
                                    @PathVariable(value = "fid") String fid) throws IOException {
        String range = request.getHeader("range");
        Map<String, String> map = new HashMap<>(16);
        map.put("range", range);
        StreamResponse fileStream = seaweedFileService.loadFileBlockStream(fid, map);
        response.setStatus(fileStream.getHttpResponseStatusCode());
        getResponse(response, fileStream.getInputStream());
    }

    /**
     * 图像的缩放读取 版本注意自定义版本，查看README https://github.com/chrislusf/seaweedfs
     *
     * @param request
     * @param response
     * @param fid
     * @throws IOException
     */
    @GetMapping("/readScaledPhoto/{fid}")
    public void readScaledPhoto(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable(value = "fid") String fid,
                                @RequestParam String fileSuffix,
                                @RequestParam String width,
                                @RequestParam String height,
                                @RequestParam String mode) throws IOException {

        String url = "http://localhost:8888/";
//        String url = "http://localhost:8888/WX20200905-150535@2x.png?height=400&width=400&mode=fill";
        StringBuilder str = new StringBuilder();
        str.append(url);
        str.append(fid)
//                .append(".").append(fileSuffix)
                .append("?width=").append(width)
                .append("&height=").append(height)
                .append("&mode=").append(mode);
        ResponseEntity<Resource> entity = restTemplate.getForEntity(str.toString(), Resource.class);
        InputStream inputStream = entity.getBody().getInputStream();
        ServletOutputStream out = response.getOutputStream();
        int len = 0;
        byte[] buffer = new byte[4096];
        while (len != -1) {
            len = inputStream.read(buffer);
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();

    }

    /**
     * 加载文件的纯文本
     *
     * @param request
     * @param fid
     * @return
     * @throws IOException
     */
    @GetMapping("/loadFileText/{fid}")
    public String loadFileText(HttpServletRequest request,
                               @PathVariable(value = "fid") String fid) throws IOException {
        StreamResponse stream = seaweedFileService.getFileStream(fid);
        String encode = EncodingDetect.getJavaEncode(stream.getInputStream(), (int) stream.getLength());
        BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(stream.getInputStream(), encode));
        StringBuffer sb = new StringBuffer();
        String sTempOneLine = new String();
        while ((sTempOneLine = tBufferedReader.readLine()) != null) {
            if (sb.length() > 0) {
                sb.append("\n" + sTempOneLine);
            } else {
                sb.append(sTempOneLine);
            }
        }
        return sb.toString();
    }

    /**
     * 根据文件id删除文件
     *
     * @param fid
     * @throws IOException
     */
    @RequestMapping("/delete/{fid}")
    public void delete(@PathVariable String fid) throws IOException {
        seaweedFileService.deleteFile(fid);
    }

    @GetMapping("/getHTMLText/{fid}")
    public String getText(@PathVariable(value = "fid") String fid) throws IOException {
        StreamResponse fileStream = seaweedFileService.getFileStream(fid);
        InputStream inputStream1 = fileStream.getInputStream();
        byte[] buffer1 = new byte[inputStream1.available()];
        inputStream1.read(buffer1);
        String path = System.getProperty("java.io.tmpdir") + fid + ".html";
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        OutputStream outStream1 = new FileOutputStream(targetFile);
        outStream1.write(buffer1);
        String encode = HtmlParse.getEncode(path);
        StringBuilder str = new StringBuilder();
        try {
            InputStreamReader reader1 = new InputStreamReader(new FileInputStream(targetFile), encode);
            int c1 = 0;
            while ((c1 = reader1.read()) != -1) {
                str.append((char) c1);
            }
            reader1.close();
        } catch (IOException ioe) {
            log.warn("html 文件读取编码失败 [{}]", ioe.getMessage());
        }
        Document doc = Jsoup.parse(str.toString());
        boolean delete1 = targetFile.delete();
        if (!delete1) {
            targetFile.deleteOnExit();
        }
        return doc.text();
    }

    public void getResponse(HttpServletResponse response, InputStream inputStream) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        int len = 0;
        byte[] buffer = new byte[4096];
        while (len != -1) {
            len = inputStream.read(buffer);
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
    }


}
