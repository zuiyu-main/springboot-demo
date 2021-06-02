package com.zuiyu.transport.service;

import com.zuiyu.transport.common.EsIndexConstant;
import org.elasticsearch.action.get.GetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void getById() {
        GetResponse response = searchService.getById(EsIndexConstant.TEST_INDEX, EsIndexConstant.INDEX_TYPE, "2");
        Map<String, Object> sourceAsMap = response.getSourceAsMap();
        sourceAsMap.forEach((k, v) -> {
            System.out.println(k + "=" + v);
        });
    }
}
