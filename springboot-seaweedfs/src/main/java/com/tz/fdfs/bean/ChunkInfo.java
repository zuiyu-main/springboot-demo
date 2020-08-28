package com.tz.fdfs.bean;

import lombok.Data;

/**
 * @author tz
 * @ClassName ChunkInfo
 * @Description
 * @Date 16:32 2020/1/14
 **/
@Data
public class ChunkInfo {
    private String fid;
    /**
     * 第几块
     */
    private Long offset;
    private Long size;
}
