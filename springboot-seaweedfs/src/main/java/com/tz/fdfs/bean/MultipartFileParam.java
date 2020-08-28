package com.tz.fdfs.bean;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author https://github.com/TianPuJun @256g的胃
 * @ClassName MultipartFileParam
 * @Description 文件分块上传的参数
 * @Date 15:02 2020/8/15
 **/
@Data
@Builder
public class MultipartFileParam {
    /**
     * 总分片数量
     */
    private Integer chunks;
    /**
     * 当前为第几块分片
     */
    private Integer chunk;
    /**
     * 当前分片大小
     */
    private Long size = 0L;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件md5
     */
    private String md5;

    /**
     * 当前分块的md5
     */
    private String chunkMd5;
    /**
     * 分片大小,注意不是每一个分块的大小，是以多大拆分的。坑1
     */
    private Long chunkSize;

    /**
     * 文件
     */
    private MultipartFile file;

    /**
     * 文件类型
     */
    private String contentType;

}
