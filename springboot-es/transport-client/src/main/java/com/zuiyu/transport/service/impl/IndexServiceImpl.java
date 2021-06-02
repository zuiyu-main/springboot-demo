package com.zuiyu.transport.service.impl;

import com.zuiyu.transport.bean.TestIndex;
import com.zuiyu.transport.service.IndexService;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.index.reindex.ReindexRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName IndexServiceImpl
 * @Description 索引加入文档
 * @Date 16:35 2021/4/13
 **/
@Service
public class IndexServiceImpl implements IndexService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexServiceImpl.class);
    @Autowired
    private TransportClient transportClient;

    @Override
    public IndexResponse indexDoc(String index, String type, String id, XContentBuilder xContentBuilder) {
        return transportClient.prepareIndex(index, type, id)
                .setSource(xContentBuilder)
                .get();
    }

    @Override
    public BulkResponse bulkIndex(List<IndexRequestBuilder> requestBuilders) {
        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        for (IndexRequestBuilder requestBuilder : requestBuilders) {
            bulkRequestBuilder.add(requestBuilder);
        }
        return bulkRequestBuilder.get();
    }

    @Override
    public void bulkProcessor(List<DocWriteRequest<TestIndex>> requests) {
        BulkProcessor build = BulkProcessor.builder(transportClient, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                LOGGER.info("bulkProcessor beforeBulk ...");
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                LOGGER.info("bulkProcessor afterBulk success ...");

            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                LOGGER.info("bulkProcessor afterBulk error ...");

            }
        })
                // 10个请求一次批量插入
                .setBulkActions(10)
                // 设置并发请求书，值为0表示只允许执行单个请求
                .setConcurrentRequests(0)
                .build();
        for (DocWriteRequest<?> request : requests) {
            build.add(request);
        }
        // Flush any remaining requests
        build.flush();
        // Or close the bulkProcessor if you don't need it anymore
        build.close();
        // Refresh your indices
        transportClient.admin().indices().prepareRefresh().get();

    }

    @Override
    public BulkByScrollResponse reindex(String sourceIndex, String targetIndex, QueryBuilder queryBuilder) {
        return
                new ReindexRequestBuilder(transportClient, ReindexAction.INSTANCE)
                        .source(sourceIndex)
                        .destination(targetIndex)
                        .filter(queryBuilder)
                        .get();
    }
}
