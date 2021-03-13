package com.tz.springbootelasticsearch7;

import com.tz.springbootelasticsearch7.bean.es.MyIndex;
import com.tz.springbootelasticsearch7.dao.es.MyIndexDao;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringbootElasticsearch7ApplicationTests {

    @Autowired
    private MyIndexDao myIndexDao;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Test
    public void contextLoads() {
        // 添加测试数据
//        MyIndex m1 = new MyIndex();
//        m1.setId("1");
//        m1.setContent("content1");
//        m1.setTitle("title1");
//        myIndexDao.save(m1);
//        MyIndex m2 = new MyIndex();
//        m2.setId("2");
//        m2.setContent("content12");
//        m2.setTitle("title12");
//        myIndexDao.save(m2);
//        MyIndex myIndex = new MyIndex();
//        myIndex.setId("3");
//        myIndex.setContent("content123");
//        myIndex.setTitle("title123");
//        myIndexDao.save(myIndex);
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        /**
         * 测试must+should
         *  这个语句，预期：必须满足title=title12*,或者title=title1*
         *  结果：        boolQueryBuilder
         *                 .should(boolQuery().must(wildcardQuery("title", "title12*")))
         *                 .should(wildcardQuery("title", "title1*"));
         *  预期差：        boolQueryBuilder
         *                 .must(wildcardQuery("title", "title12*"))
         *                 .should(wildcardQuery("title", "title1*"));
         *
         *
         */
        boolQueryBuilder
                .should(boolQuery().must(wildcardQuery("title", "title12*")))
                .should(wildcardQuery("title", "title1*"));

        Iterable<MyIndex> search = myIndexDao.search(boolQueryBuilder);
        search.forEach(e -> {
            System.out.println(e.toString());
        });

    }

}
