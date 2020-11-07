package com.tz.fdfs.controller;

import com.tz.fdfs.bean.MultipartFileParam;
import com.tz.fdfs.util.FileUtils;
import com.tz.fdfs.util.Md5Utils;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FileControllerTest {

    @Autowired
    private FileController fileController;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void blockUpload() throws IOException {
        File file = new File("/Users/cxt/Downloads/test2/test.pdf");
        MultipartFile allFile = FileUtils.file2MultipartFile(file);
        String md5 = Md5Utils.getMD5(file);
        long allFileSize = allFile.getSize();

        File f1 = new File("/Users/cxt/Downloads/test2/xaa");
        MultipartFile multipartFile = FileUtils.file2MultipartFile(f1);
        MultipartFileParam param = MultipartFileParam.builder()
                .chunk(0)
                .chunkMd5(Md5Utils.getMD5(f1))
                .chunks(3)
                .contentType(multipartFile.getContentType())
                .chunkSize(4194304L)
                .file(multipartFile)
                .md5(md5)
                .name(f1.getName())
                .size(allFileSize)
                .build();


        File f2 = new File("/Users/cxt/Downloads/test2/xab");
        MultipartFile multipartFile2 = FileUtils.file2MultipartFile(f2);
        MultipartFileParam param1 = MultipartFileParam.builder()
                .chunk(1)
                .chunkMd5(Md5Utils.getMD5(f2))
                .chunks(3)
                .contentType(multipartFile2.getContentType())
                .chunkSize(4194304L)
                .file(multipartFile2)
                .md5(md5)
                .name(f2.getName())
                .size(allFileSize)
                .build();


        File f3 = new File("/Users/cxt/Downloads/test2/xac");
        MultipartFile multipartFile3 = FileUtils.file2MultipartFile(f3);
        MultipartFileParam param2 = MultipartFileParam.builder()
                .chunk(2)
                .chunkMd5(Md5Utils.getMD5(f3))
                .chunks(3)
                .contentType(multipartFile3.getContentType())
                .chunkSize(4194304L)
                .file(multipartFile3)
                .md5(Md5Utils.getMD5(file))
                .name(f3.getName())
                .size(allFile.getSize())
                .build();

        Object o = fileController.blockUpload(param);
        System.out.println("o " + o);
        Object o1 = fileController.blockUpload(param1);
        System.out.println("o1 " + o1);
        Object o2 = fileController.blockUpload(param2);
        System.out.println("o2 " + o2);
    }

    @Test
    public void mergeFiles() throws Exception {
        File file = new File("/Users/cxt/Downloads/test2/test.pdf");
        MultipartFile allFile = FileUtils.file2MultipartFile(file);

        MultipartFileParam param = MultipartFileParam.builder()
                //"application/pdf"
                .contentType(allFile.getContentType())
                .md5(Md5Utils.getMD5(file))
                .name(allFile.getOriginalFilename())
                .size(allFile.getSize())
                .build();
        Object o = fileController.mergeFiles(param);
        System.out.println("合并" + o);
    }

    @Test
    public void deleteFile() throws IOException {

        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:8888", String.class);
        Document document = Jsoup.parseBodyFragment(forEntity.getBody());
        Elements a = document.select("a");
        List<String> ids = new LinkedList<>();
        for (Element element : a) {
            String href = element.attr("href");
            char[] chars = href.toCharArray();
            for (char aChar : chars) {
                boolean digit = Character.isDigit(aChar);
                if (digit) {
                    ids.add(href.substring(1));
                    break;
                }
            }
        }
        for (String id : ids) {
            System.out.println(id);
            String url = "http://127.0.0.1:8780/delete/" + id;
            try {
                restTemplate.getForEntity(url, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getHTMLText() throws IOException {
        List<String> ids = new ArrayList<>(10);
        ids.add("1,0dda956822");
        ids.add("6,0fc35b46e9");
        ids.add("7,128b4968e8");
        ids.add("7,14e0b81d03");


        System.out.println(" ------------------------- \n");
//        String getText = fileController.getText("1,0dda956822");
//        String title = "福建:多措并举推进民法典深度培训";
//        String getText = fileController.getText("7,128b4968e8");
//        String title = "昆山市人民检察院依法以盗窃罪对张某甲提起公诉";
//        String getText = fileController.getText("7,14e0b81d03");
//        String title = "五大行公告：8月25日起对个人房贷统一转为LPR定价";
        String getText = fileController.getText("6,0fc35b46e9");
        String title = "福建高院传达学习贯彻省委十届十次全会精神";
        System.out.println(getText);
        String s = getText.replaceAll(title, "");
        int indexOf1 = s.indexOf("发布时间：");
        if (indexOf1 > -1) {
            s = s.substring(indexOf1 + 25);
        } else {
            int indexOf2 = s.indexOf("来源：");
            if (indexOf2 > -1) {
                s = s.substring(indexOf2 + 3);
                s = s.substring(s.indexOf(" ") + 1);
            }

        }
        System.out.println("\n");
        System.out.println(s);
    }

    @Test
    public void getHTMLText1() throws IOException {
        List<FileDesc> ids = new ArrayList<>(10);
        FileDesc f1 = new FileDesc();
        f1.setId("1,018375ed3927");
        f1.setTitle("第二届民营经济法治建设峰会召开 高云龙周强张军出席");
//
//        FileDesc f2 = new FileDesc();
//        f2.setId("5,017585e2afa4");
//        f2.setTitle("宁波法院：争当“最美模范生”");


        ids.add(f1);
//        ids.add(f2);
//        ids.add(f3);
        for (FileDesc fileDesc : ids) {
            StringBuilder text = new StringBuilder();
            int index = 0;
            System.err.println("直接读取------start");
            text = new StringBuilder(fileController.getText(fileDesc.getId()));

            if (StringUtils.isEmpty(text.toString().replace((char) 12288, ' ').trim())) {
                continue;
            }
            text = new StringBuilder(getDesc(text.toString(), fileDesc.getTitle()));
            boolean b1 = StringUtils.isEmpty(text.toString().replace((char) 12288, ' ').trim())
                    || text.toString().contains(fileDesc.getTitle())
                    || text.toString().contains("图") && text.toString().contains("摄")
                    || text.toString().contains("发布日期：")
                    || text.toString().contains("发布时间：")
                    || text.toString().contains("作者")
                    || text.toString().contains("作者") && text.toString().contains("发布")
                    || text.toString().contains("作者") && text.toString().contains("发表")
                    || text.toString().contains("来源") && text.toString().contains("作者")
                    || text.toString().contains("来源") && text.toString().contains("发布")
                    || text.toString().contains("来源") && text.toString().contains("发表");
            if (b1) {

                String getText = fileController.loadFileText(null, fileDesc.getId());
                Document document = Jsoup.parseBodyFragment(getText);
                Elements p = document.select("p");
                if (p.size() == 0) {
                    Document doc = Jsoup.parse(getText);
                    text = new StringBuilder(doc.text().replace(fileDesc.getTitle(), ""));
                } else {
                    text = new StringBuilder(p.get(index).text());
                    boolean b =
                            StringUtils.isEmpty(text.toString().replace((char) 12288, ' ').trim())
                                    || text.toString().contains(fileDesc.getTitle())
                                    || text.toString().contains("图") && text.toString().contains("摄")
                                    || text.toString().contains("作者") && text.toString().contains("发布")
                                    || text.toString().contains("发布日期：")
                                    || text.toString().contains("发布时间：")
                                    || text.toString().contains("作者") && text.toString().contains("发表")
                                    || text.toString().contains("来源") && text.toString().contains("作者")
                                    || text.toString().contains("来源") && text.toString().contains("发布")
                                    || text.toString().contains("来源") && text.toString().contains("发表");
                    while (b) {
                        index++;
                        if (index >= p.size()) {
                            index--;
                            b = false;
                            text = new StringBuilder();
                        } else {
                            text = new StringBuilder(p.get(index).text());
                            b = StringUtils.isEmpty(text.toString().replace((char) 12288, ' ').trim())
                                    || text.toString().contains(fileDesc.getTitle())
                                    || text.toString().contains("图") && text.toString().contains("摄")
                                    || text.toString().contains("发布日期：")
                                    || text.toString().contains("发布时间：")
                                    || text.toString().contains("作者") && text.toString().contains("发布")
                                    || text.toString().contains("作者") && text.toString().contains("发表")
                                    || text.toString().contains("来源") && text.toString().contains("作者")
                                    || text.toString().contains("来源") && text.toString().contains("发布")
                                    || text.toString().contains("来源") && text.toString().contains("发表");
                        }
                    }
                }
                boolean flag = true;
                while (text.length() <= 300 && flag) {
                    index++;
                    if (index >= p.size()) {
                        flag = false;
                        continue;
                    }
                    text.append(p.get(index).text());
                }
            }


            System.out.println(text);
            System.out.println(text.length());
        }


        System.out.println("\n");
    }

    public String getDesc(String str, String title) {
        String fileDesc = str.length() < 600 ? str : str.substring(0, 600);
        fileDesc = fileDesc.replaceAll("\\s{2,}", " ");
        if (!StringUtils.isEmpty(title)) {
            fileDesc = fileDesc.replace(title.replaceAll("\\s{2,}", " "), "");
        }
        if (fileDesc.length() > 300) {
            fileDesc = fileDesc.substring(0, 300);
        }
        if (fileDesc.contains("来源：")) {
            fileDesc = fileDesc.substring(fileDesc.indexOf("来源："));
        }
        if (fileDesc.contains("发布时间：")) {
            fileDesc = fileDesc.substring(fileDesc.indexOf("发布时间：") + 25);
        }

        return fileDesc;
    }

    @Data
    public static class FileDesc {
        private String title;
        private String id;
    }

    @Test
    public void upload() throws IOException {
        File file = new File("/Users/cxt/Downloads/test1/工作周报 2020-08-24.html");
        MultipartFile multipartFile = FileUtils.file2MultipartFile(file);
        Object upload = fileController.upload(multipartFile);
        System.out.println(upload.toString());
    }
}
