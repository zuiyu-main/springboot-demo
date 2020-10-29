package com.tz.fdfs.config;

import lombok.extern.slf4j.Slf4j;
import net.anumbrella.seaweedfs.core.FileSource;
import net.anumbrella.seaweedfs.core.FileTemplate;
import net.anumbrella.seaweedfs.core.VolumeWrapper;
import net.anumbrella.seaweedfs.core.file.FileHandleStatus;
import net.anumbrella.seaweedfs.core.http.StreamResponse;
import org.apache.http.entity.ContentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tz
 * @ClassName SeaweedClient
 * @Description 文件服务器链接配置
 * @Date 16:44 2020/1/16
 **/
@Slf4j
public class SeaweedClient {
    private String host;

    private int port;

    private FileTemplate template;

    private MyFileTemplate myFileTemplate;

    private VolumeWrapper volumeWrapper;

    private MyVolumeWrapper myVolumeWrapper;

    public SeaweedClient(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    public void init() {
        FileSource fileSource = new FileSource();
        fileSource.setHost(host);
        fileSource.setPort(port);
        try {
            fileSource.startup();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("SeaweedClient ---> init 文件服务器连接失败 ，msg: [{}]", e.getMessage());
        }
        template = new FileTemplate(fileSource.getConnection());
        myFileTemplate = new MyFileTemplate(fileSource.getConnection());
        myVolumeWrapper = new MyVolumeWrapper(fileSource.getConnection());
        volumeWrapper = new VolumeWrapper(fileSource.getConnection());
        log.info("seaweedfs startup success");
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public FileHandleStatus saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        FileHandleStatus status = template.saveFileByStream(fileName, file.getInputStream(), ContentType.create(file.getContentType()));
        return status;
    }

    public FileHandleStatus saveFileByStream(String fileName, InputStream inputStream) throws IOException {
        FileHandleStatus fileHandleStatus = template.saveFileByStream(fileName, inputStream, ContentType.APPLICATION_OCTET_STREAM);
        if (fileHandleStatus != null) {
            return fileHandleStatus;
        }
        return null;
    }

    /**
     * 根据文件id获取文件url
     *
     * @param fileId
     * @return
     * @throws IOException
     */
    public String getFile(String fileId) throws IOException {
        return template.getFileUrl(fileId);
    }

    /**
     * 获取文件流
     *
     * @param fileId
     * @return
     * @throws IOException
     */
    public StreamResponse getFileStream(String fileId) throws IOException {
        log.info("seaweedfs client getFileStream temple = [{}]", this.template);
        return template.getFileStream(fileId);
    }

    public FileHandleStatus getFileStatus(String fileId, String fileName, String contentType) throws IOException {
        return myFileTemplate.getFileStatus(fileId, fileName, contentType);
    }

    public FileHandleStatus getFileStatus(String fileId) throws IOException {
        return template.getFileStatus(fileId);
    }

    /**
     * 通过文件流保存文件，此处为合并文件json文本上传使用方法，修改了cm=true
     *
     * @param saveFileUrl
     * @param fid
     * @param fileName
     * @param inputStream
     * @param contentType
     * @return
     * @throws Exception
     */
    public long uploadFileWithCm(String saveFileUrl, String fid, String fileName, InputStream inputStream, String contentType) throws Exception {
        return myVolumeWrapper.uploadFileWithCm(saveFileUrl, fid, fileName, inputStream, null, ContentType.create(contentType));
    }

    public void deleteFile(String fid) throws IOException {
        template.deleteFile(fid);
    }

    public void deleteFiles(List<String> fileIds) throws IOException {
        template.deleteFiles(new ArrayList(fileIds));
    }

    public StreamResponse readScaledPhoto(String fid, Map<String, String> imgMap) throws IOException {
        return template.getFileStream(fid, null, imgMap);
    }

    public StreamResponse loadFileBlockStream(String fid, Map<String, String> map) throws IOException {
        return template.getFileStream(fid, map, null);
    }

}
