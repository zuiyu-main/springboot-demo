package com.tz.fdfs.config;

import lombok.extern.slf4j.Slf4j;
import net.anumbrella.seaweedfs.core.file.FileHandleStatus;
import net.anumbrella.seaweedfs.core.http.StreamResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author tz
 * @ClassName SeaweedFileService
 * @Description 文件服务器链接
 * @Date 16:46 2020/1/16
 **/
@Slf4j
@Service
public class SeaweedFileService implements CommandLineRunner {
    @Value("${seaweedfs.host}")
    private String host;

    @Value("${seaweedfs.port}")
    private Integer port;

    SeaweedClient client;

    @Override
    public void run(String... args) {
        client = new SeaweedClient(host, port);
    }

    public long uploadFile(String saveFileUrl, String fid, String fileName, InputStream inputStream, String contentType) throws Exception {
        return client.uploadFile(saveFileUrl, fid, fileName, inputStream, contentType);
    }

    public FileHandleStatus saveFileByStream(String fileName, InputStream inputStream) throws IOException {
        return client.saveFileByStream(fileName, inputStream);
    }

    public void deleteFile(String fid) throws IOException {
        client.deleteFile(fid);
    }

    public void deleteFiles(List<String> fileIds) throws IOException {
        client.deleteFiles(fileIds);
    }

    public StreamResponse getFileStream(String fid) throws IOException {
        log.info("seaweedfsFileService getFileStream temple = [{}]", this.client);
        return client.getFileStream(fid);
    }

    public FileHandleStatus getFileStatus(String fileId, String fileName, String contentType) throws IOException {
        return client.getFileStatus(fileId, fileName, contentType);
    }

}
