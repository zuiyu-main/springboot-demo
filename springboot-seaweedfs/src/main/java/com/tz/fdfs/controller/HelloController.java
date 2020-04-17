package com.tz.fdfs.controller;

import com.tz.fdfs.config.SeaweedFileService;
import lombok.extern.slf4j.Slf4j;
import net.anumbrella.seaweedfs.core.file.FileHandleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author https://github.com/TianPuJun @无痕
 * @ClassName HelloController
 * @Description
 * @Date 09:43 2020/3/12
 **/
@Slf4j
@RestController
public class HelloController {
    @Autowired
    private SeaweedFileService seaweedFileService;

    @RequestMapping("/upload")
    public String upload() throws IOException {

        FileHandleStatus fileHandleStatus = seaweedFileService.saveFileByStream("JAVA核心知识点整理.pdf", new FileInputStream("/Users/cxt/Downloads/JAVA核心知识点整理.pdf"));
        log.info(fileHandleStatus.toString());
        return fileHandleStatus.toString();
    }

    @RequestMapping("/get")
    public String get(String fid) throws IOException {
        FileHandleStatus fileStatus = seaweedFileService.getFileStatus(fid, "JAVA核心知识点整理.pdf", "application/pdf");
        return fileStatus.toString();
    }

    @RequestMapping("/delete")
    public void delete(String fid) throws IOException {
        seaweedFileService.deleteFile(fid);
//        List<String> list = new ArrayList<>();
//        list.add("4,05b078a4e6");
//        list.add("4,0dde090d22");4,0dde090d22
//        list.add("4,13f12ae8b7");4,13f12ae8b7
//        list.add("4,15b7d92c45");
//        list.add("5,0b69d7b537");
//        list.add("5,0fe62a58ea");
//        list.add("test.pdf");
//        list.add("最高人民法院关于适用《中华人民共和国公司法》若干问题的规定（四）%20%28法释〔2017〕16号%29.pdf");
//        seaweedFileService.deleteFiles(list);

    }


}
