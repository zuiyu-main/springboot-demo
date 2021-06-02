package com.tz.elastic;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticApplicationTests {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Test
    public void contextLoads() {
    }

    @Test
    public void testSelect() {
        Client client = elasticsearchTemplate.getClient();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("f_info_4", "j_info_5", "j_info_11", "f_info_37");
//                .setTypes("ARCHIVE_FILED");
        BoolQueryBuilder boolQueryBuilder = boolQuery();
//        boolQueryBuilder.should(wildcardQuery("FILE_NAME.keyword","中 华*"));
//        boolQueryBuilder.should(wildcardQuery("FILE.keyword","中 华*"));
        boolQueryBuilder.should(multiMatchQuery("中华", "FILE_NAME", "FILE").slop(2).analyzer("standard").boost(3.0f));
        boolQueryBuilder.should(multiMatchQuery("中华", "FILE_NAME", "FILE").slop(2).analyzer("ik_smart").boost(3.0f));
        SearchResponse searchResponse = searchRequestBuilder.setQuery(boolQueryBuilder)
                .setFetchSource(new String[]{"FILE_NAME"}, null)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setTypes()
                .execute().actionGet();

        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit searchHitFields : hits1) {
            Map<String, Object> sourceAsMap = searchHitFields.getSourceAsMap();
            System.out.println(sourceAsMap.toString());
        }
        System.out.println(searchResponse.getHits().totalHits);
    }

}
