package com.tz.elastic;

import com.tz.elastic.bean.es.TestDocument;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticApplicationTests {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void contextLoads() {
    }
    @Test
    public void arrayInsertTest(){
        List<IndexQuery> indexQueryList = new ArrayList<>(16);
        for(int i=0;i<10;i++){
            TestDocument testDocument = new TestDocument();
            testDocument.setId(UUID.randomUUID().toString());
            List<String> list = Arrays.asList("test"+i,"abc123","public");
            testDocument.setPermission(list);
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(testDocument.getId());
            indexQuery.setObject(testDocument);
            indexQueryList.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(indexQueryList);
        elasticsearchTemplate.refresh(TestDocument.class);
    }
    @Test
    public void selectArray(){
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(null, new String[]{"permission"});
        //查询结果不返回content
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery().must(termQuery("permission","abc123"))).withSourceFilter(fetchSourceFilter).build();
        List<TestDocument> testDocuments = elasticsearchTemplate.queryForList(searchQuery, TestDocument.class);
        testDocuments.forEach(e->{
            System.out.println(e.getId());
        });
    }

}
