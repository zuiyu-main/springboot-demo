package com.zuiyu.transport.service.impl;

import com.zuiyu.transport.service.SearchService;
import org.elasticsearch.action.get.*;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName SearchServiceImpl
 * @Description 搜索服务
 * @Date 16:24 2021/4/13
 **/
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private TransportClient transportClient;

    @Override
    public GetResponse getById(String index, String type, String id) {
        return transportClient.prepareGet(index, type, id).get();
    }

    @Override
    public List<Map<String, Object>> multiGetById(List<MultiGetRequest.Item> items) {
        List<Map<String, Object>> res = new LinkedList<>();
        MultiGetRequestBuilder multiGetRequestBuilder = transportClient.prepareMultiGet();
        items.forEach(multiGetRequestBuilder::add);
        MultiGetResponse multiGetItemResponses = multiGetRequestBuilder.get();
        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                res.add(response.getSourceAsMap());
            }
        }
        return res;
    }
}
