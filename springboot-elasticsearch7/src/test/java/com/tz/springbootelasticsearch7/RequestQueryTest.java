package com.tz.springbootelasticsearch7;

import com.tz.springbootelasticsearch7.bean.es.TestIndex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName RequestQUeryTest
 * @Description
 * @Date 15:46 2021/3/30
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class RequestQueryTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void Test() {

        NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(matchQuery("field4", "中华")).build();
        IndexCoordinates test_index = IndexCoordinates.of("test_index");
        SearchHits<TestIndex> search = elasticsearchRestTemplate.search(build, TestIndex.class, test_index);
        System.out.println(search.getTotalHits());
    }
}
