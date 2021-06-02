package com.zuiyu.transport.service.impl;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName BulkProcess
 * @Description
 * @Date 15:17 2021/4/21
 **/
@Service
public class BulkProcess implements Flushable, Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkProcess.class);

    @Autowired
    private TransportClient transportClient;

    private BulkProcessor bulkProcessor;

    @PostConstruct
    public void init() {
        bulkProcessor = BulkProcessor.builder(transportClient, new BulkProcessor.Listener() {
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
                // 10000个请求一次批量插入
                .setBulkActions(10000)
                // 设置并发请求书，值为0表示只允许执行单个请求
                .setConcurrentRequests(0)
                .build();
    }

    @Override
    public void close() throws IOException {
        bulkProcessor.close();
    }

    @Override
    public void flush() throws IOException {

        bulkProcessor.flush();
    }
}
