package com.zuiyu.transport.service;

import com.zuiyu.transport.bean.TestIndex;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;

import java.util.List;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName IndexService
 * @Date 16:35 2021/4/13
 **/
public interface IndexService {
    /**
     * index 索引中type类型加入id的一个文档
     *
     * @param index           索引
     * @param type            类型
     * @param id              id
     * @param xContentBuilder 文档内容
     * @return
     */
    IndexResponse indexDoc(String index, String type, String id, XContentBuilder xContentBuilder);

    /**
     * 批量插入数据
     *
     * @param requestBuilders 单条indexRequest插入结构
     * @return
     */
    BulkResponse bulkIndex(List<IndexRequestBuilder> requestBuilders);

    /**
     * 批量处理数据
     *
     * @param requests 支持IndexRequest/UpdateRequest/DeleteRequest
     */
    void bulkProcessor(List<DocWriteRequest<TestIndex>> requests);

    /**
     * reindex 指定
     *
     * @param sourceIndex  源索引
     * @param targetIndex  目标索引
     * @param queryBuilder 源索引数据的查询条件
     * @return
     */
    BulkByScrollResponse reindex(String sourceIndex, String targetIndex, QueryBuilder queryBuilder);

}
