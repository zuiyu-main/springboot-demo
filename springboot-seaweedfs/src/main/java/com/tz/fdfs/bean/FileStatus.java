package com.tz.fdfs.bean;

import lombok.Data;

/**
 * @author https://github.com/TianPuJun @256g的胃
 * @ClassName FileStatus
 * @Description seaweedfs 返回的bean
 * @Date 17:11 2020/8/21
 **/
@Data
public class FileStatus {
    private String fid;
    private String publicUrl;
    private String url;
    private int count;

    private String fileName;
    private Long size;
}
