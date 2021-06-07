package com.zuiyu.transport.service.impl;

import com.zuiyu.transport.common.EsIndexConstant;
import com.zuiyu.transport.service.IndicesService;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName IndicesServiceImpl
 * @Description 索引基础设置服务
 * @Date 20:40 2021/6/7
 **/
@Service
public class IndicesServiceImpl implements IndicesService {
    @Autowired
    private TransportClient transportClient;

    @Override
    public CreateIndexResponse createIndex(String index, Settings settings) {
        return transportClient.admin()
                .indices()
                .prepareCreate(index)
                .setSettings(settings)
                .get();
    }

    @Override
    public CreateIndexResponse createIndex(String index, Settings settings, XContentBuilder contentBuilder) {
        return transportClient.admin()
                .indices()
                .prepareCreate(index)
                .setSettings(settings)
                .addMapping(EsIndexConstant.INDEX_TYPE, contentBuilder)
                .get();
    }

    @Override
    public AcknowledgedResponse putMapping(String index, String type, XContentBuilder contentBuilder) {
        return transportClient.admin().indices()
                .preparePutMapping(index)
                .setType(type)
                .setSource(contentBuilder)
                .get();
    }

    @Override
    public AcknowledgedResponse updateSettings(String index, Settings settings) {
        AcknowledgedResponse acknowledgedResponse = transportClient.admin().indices()
                .prepareUpdateSettings(index)
                .setSettings(settings)
                .get();
        return acknowledgedResponse;
    }

    @Override
    public AcknowledgedResponse deleteIndices(String... index) {
        return transportClient.admin()
                .indices()
                .prepareDelete(index)
                .get();
    }


}
