package com.tz.elastic.controller;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

/**
 * @author liBai
 * @Classname HelloController
 * @Description TODO
 * @Date 2019-06-02 10:49
 */
@RestController
@RequestMapping("/test")
public class HelloController {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @GetMapping("/")
    public String test() {
        Client client = elasticsearchTemplate.getClient();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("f_info_4")
                .setTypes("ARCHIVE_FILED");
        SearchResponse searchResponse = searchRequestBuilder.setQuery(
                multiMatchQuery("中华", "FILE_NAME", "FILE").analyzer("ik_smart").boost(3.0f))
                .execute().actionGet();

        System.out.println(searchResponse.getHits());

        return "test";
    }

}
