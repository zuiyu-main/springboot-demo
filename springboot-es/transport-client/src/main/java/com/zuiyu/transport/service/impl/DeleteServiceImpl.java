package com.zuiyu.transport.service.impl;

import com.zuiyu.transport.service.DeleteService;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName DeleteServiceImpl
 * @Description es 删除服务
 * @Date 20:28 2021/4/13
 **/
@Service
public class DeleteServiceImpl implements DeleteService {
    @Autowired
    private TransportClient transportClient;

    @Override
    public DeleteResponse deleteById(String index, String type, String id) {
        return transportClient.prepareDelete(index, type, id).get();
    }

    @Override
    public BulkByScrollResponse deleteByQuery(String index, QueryBuilder queryBuilder) {
        return new DeleteByQueryRequestBuilder(transportClient, DeleteByQueryAction.INSTANCE)
                .filter(queryBuilder)
                .source(index)
                .get();
    }
}
