package com.tz.fdfs.bean;

import lombok.Data;

import java.util.List;

/**
 * @author tz
 * @ClassName ChunkManifest
 * @Description 批量文件合并集合
 * @Date 16:31 2020/1/14
 **/
@Data
public class ChunkManifest {
    private String name;
    private String mime;
    private Long size;
    private List<ChunkInfo> chunks;
}
