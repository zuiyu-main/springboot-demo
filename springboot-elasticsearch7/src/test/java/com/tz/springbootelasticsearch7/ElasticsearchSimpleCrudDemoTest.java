package com.tz.springbootelasticsearch7;

import com.tz.springbootelasticsearch7.bean.es.MyIndex;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.moreLikeThisQuery;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName ElasticsearchSimpleCrudDemoTest
 * @Description
 * @Date 19:24 2021/3/28
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchSimpleCrudDemoTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void indexTest() {
        MyIndex myIndex = new MyIndex();
        myIndex.setId(UUID.randomUUID().toString());
        myIndex.setTitle("标题 你好你好标题啊");
        myIndex.setContent("content3");

        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setObject(myIndex);
        indexQuery.setId(myIndex.getId());
        IndexCoordinates indexCoordinates = IndexCoordinates.of("my_index");
        elasticsearchRestTemplate.index(indexQuery, indexCoordinates);
        System.out.println("index success id = " + myIndex.getId());
    }

    @Test
    public void getByIdTest() {
        MyIndex myIndex = elasticsearchRestTemplate.get("a31022c3-ba30-46c1-931c-10d9ca230ac1", MyIndex.class);
        System.out.println("====== get index start ======");
        System.out.println(myIndex);
        System.out.println("====== get index end ======");
    }

    @Test
    public void deleteById() {
        IndexCoordinates indexCoordinates = IndexCoordinates.of("my_index");
        String delete = elasticsearchRestTemplate.delete("a31022c3-ba30-46c1-931c-10d9ca230ac1", indexCoordinates);
        System.out.println("根据id删除= " + delete);
    }

    @Test
    public void updateByIdTest() {

        String id = "a31022c3-ba30-46c1-931c-10d9ca230ac1";
        Document document = Document.create();
        document.put("title", "new title set");
        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withDocument(document)
                .withDocAsUpsert(true)

                .build();

        IndexCoordinates indexCoordinates = IndexCoordinates.of("my_index");
        UpdateResponse update = elasticsearchRestTemplate.update(updateQuery, indexCoordinates);
        UpdateResponse.Result result = update.getResult();
        System.out.println("根据主键更新结果= " + result.toString());
    }

    @Test
    public void dynamicCreateIndexTest() {
        IndexCoordinates test_index = IndexCoordinates.of("test_index");
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(test_index);
        Document setting = Document.create();
        Document index = Document.create();
        index.put("number_of_shards", 3);
        index.put("number_of_replicas", 2);
        setting.put("index", index);
        boolean b = indexOperations.create(setting);
        System.out.println("创建test_index =" + b);


    }


    @Test
    public void dynamicCreateMapping() {
        IndexCoordinates test_index = IndexCoordinates.of("test_index");
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(test_index);
        Document mappingObj = Document.create();
        Document properties = Document.create();
        Document field1 = Document.create();
        field1.put("type", "text");
        properties.put("field1", field1);

        Document field2 = Document.create();
        field2.put("type", "keyword");
        properties.put("field2", field2);

        Document field3 = Document.create();
        field3.put("type", "long");
        properties.put("field3", field3);

        Document field4 = Document.create();
        field4.put("type", "text");
        Document field4Child = Document.create();
        Document keyword = Document.create();
        keyword.put("ignore_above", 256);
        keyword.put("type", "keyword");
        field4Child.put("keyword", keyword);
        field4.put("fields", field4Child);

        properties.put("field4", field4);
        mappingObj.put("properties", properties);

        indexOperations.putMapping(mappingObj);
    }

    @Test
    public void matchSearchTest() {
        String[] fields = {"title.keyword"};
        String[] texts = {"title"};
        MoreLikeThisQueryBuilder moreLikeThisQueryBuilder = moreLikeThisQuery(fields, texts, null)
                .minTermFreq(1)
                .maxQueryTerms(12);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(moreLikeThisQueryBuilder).build();
        SearchHits<MyIndex> search = elasticsearchRestTemplate.search(query, MyIndex.class);
        System.out.println(search.getTotalHits());
    }
}
