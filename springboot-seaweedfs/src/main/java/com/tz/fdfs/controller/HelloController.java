package com.tz.fdfs.controller;

import com.tz.fdfs.config.SeaweedFileService;
import lombok.extern.slf4j.Slf4j;
import net.anumbrella.seaweedfs.core.FileTemplate;
import net.anumbrella.seaweedfs.core.file.FileHandleStatus;
import net.anumbrella.seaweedfs.core.http.StreamResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

}
