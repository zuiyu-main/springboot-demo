package com.zuiyu.transport.service;

import com.zuiyu.transport.common.EsIndexConstant;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

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

    @Test
    public void multiGetById() {
        List<MultiGetRequest.Item> items = new LinkedList<>();
        MultiGetRequest.Item item = new MultiGetRequest.Item(EsIndexConstant.TEST_INDEX, "2");
        items.add(item);
        List<Map<String, Object>> maps = searchService.multiGetById(items);
        System.out.println(maps.toString());
    }

    @Test
    public void usrScrollSearch() {
        QueryBuilder qb = termQuery("title.keyword", "Upsert");
        searchService.usrScrollSearch(qb, EsIndexConstant.TEST_INDEX);

    }

    @Test
    public void multiSearch() {
        searchService.multiSearch(null);
    }

    @Test
    public void useAggregations() {
        searchService.useAggregations();
    }
}
