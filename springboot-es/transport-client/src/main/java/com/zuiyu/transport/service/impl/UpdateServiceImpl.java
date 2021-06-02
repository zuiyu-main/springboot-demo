package com.zuiyu.transport.service.impl;

import com.zuiyu.transport.common.EsIndexConstant;
import com.zuiyu.transport.service.UpdateService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName UpdateServiceImpl
 * @Description es 更新服务
 * @Date 20:55 2021/4/13
 **/
@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private TransportClient transportClient;

    @Override
    public UpdateResponse updateById(String index, String id, XContentBuilder doc) throws ExecutionException, InterruptedException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index);
        updateRequest.id(id);
        updateRequest.doc(doc);
        return transportClient.update(updateRequest).get();
    }

    @Override
    public UpdateResponse upsertById(String index, String id, XContentBuilder source, XContentBuilder doc) throws ExecutionException, InterruptedException {
        IndexRequest indexRequest = new IndexRequest(index)
                .id(id)
                .source(source);
        UpdateRequest updateRequest = new UpdateRequest(index, id)
                .doc(doc)
                .upsert(indexRequest);
        return transportClient.update(updateRequest).get();
    }

    @Override
    public BulkByScrollResponse updateByQuery(UpdateByQueryRequestBuilder queryRequestBuilder) {
        queryRequestBuilder.source(EsIndexConstant.TEST_INDEX).abortOnVersionConflict(false);
        return queryRequestBuilder.get();
    }
}
