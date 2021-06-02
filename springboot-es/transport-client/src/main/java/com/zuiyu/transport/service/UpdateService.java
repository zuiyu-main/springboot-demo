package com.zuiyu.transport.service;

import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;

import java.util.concurrent.ExecutionException;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName UpdateService
 * @Date 20:55 2021/4/13
 **/
public interface UpdateService {
    /**
     * 根据主键id更新内容
     *
     * @param index 索引
     * @param id    id
     * @param doc   更新文档内容
     * @return
     */
    UpdateResponse updateById(String index, String id, XContentBuilder doc) throws ExecutionException, InterruptedException;


    /**
     * 根据ID更新文档不存在时进行插入
     *
     * @param index
     * @param id
     * @param source 文档不存在时插入的数据
     * @param doc    要更新的文档内容
     * @return
     */
    UpdateResponse upsertById(String index, String id, XContentBuilder source, XContentBuilder doc) throws ExecutionException, InterruptedException;

    /**
     * 根据条件更新数据
     *
     * @param queryRequestBuilder
     * @return
     */
    BulkByScrollResponse updateByQuery(UpdateByQueryRequestBuilder queryRequestBuilder);
}
