package com.zuiyu.transport.service;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName IndicesService
 * @Description 索引设置服务
 * @Date 20:40 2021/6/7
 **/
public interface IndicesService {
    /**
     * 创建索引不带mapping设置
     *
     * @param index    索引名
     * @param settings settings
     * @return
     */
    CreateIndexResponse createIndex(String index, Settings settings);

    /**
     * 单独创建索引
     *
     * @param index          索引名称
     * @param settings       索引setting
     * @param contentBuilder mapping
     * @return
     */
    CreateIndexResponse createIndex(String index, Settings settings, XContentBuilder contentBuilder);

    /**
     * 创建完索引之后设置mapping
     *
     * @param index          索引名
     * @param type           类型
     * @param contentBuilder mapping内容
     * @return
     */
    AcknowledgedResponse putMapping(String index, String type, XContentBuilder contentBuilder);

    /**
     * 更新索引settings
     *
     * @param index    索引名
     * @param settings 更新的setting
     * @return
     */
    AcknowledgedResponse updateSettings(String index, Settings settings);

    /**
     * 删除一个或多个索引
     *
     * @param index 索引名
     * @return
     */
    AcknowledgedResponse deleteIndices(String... index);
}
