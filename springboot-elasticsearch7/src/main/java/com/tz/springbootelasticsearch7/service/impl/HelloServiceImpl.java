package com.tz.springbootelasticsearch7.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tz.springbootelasticsearch7.bean.es.MyIndex;
import com.tz.springbootelasticsearch7.dao.es.MyIndexDao;
import com.tz.springbootelasticsearch7.service.HelloService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author liBai
 * @Classname HelloServiceImpl
 * @Description hello
 * @Date 2019-06-02 10:50
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Autowired
    private MyIndexDao myIndexDao;


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public String sayHello() {
        MyIndex myIndex = new MyIndex();
        myIndex.setId("1");
        myIndex.setTitle("测试1");
        myIndex.setContent("curl -XPOST http://localhost:9200/index/_create/3 -H 'Content-Type:application/json' -d'\n" +
                "{\"content\":\"中韩渔警冲突调查：韩警平均每天扣1艘中国渔船\"}\n" +
                "'");
        myIndexDao.save(myIndex);
        return "";
    }

    @Override
    public String getRedisInfo() {


        MyIndex contentLike = myIndexDao.findAllByContentLike("中国");
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("content", "中国").analyzer("ik_max_word")).build();
        SearchHits<MyIndex> search = elasticsearchTemplate.search(searchQuery, MyIndex.class);
        List<SearchHit<MyIndex>> searchHits = search.getSearchHits();
        System.out.println(contentLike);
        return "";
    }

    public static void main(String[] args) {
        String json = "{\n" +
                "\t\"pageSize\":\"0\",\n" +
                "\t\"pageNum\": \"20\",\n" +
                "    \"condition\":[{\"operation\":\"and\",\"field\":\"k1\",\"value\":\"v1\",\"type\":\"like\"},\n" +
                "{\"operation\":\"or\",\"field\":\"k2\",\"value\":\"v2\",\"type\":\"eq\"},\n" +
                "{\"operation\":\"or\",\"field\":\"k3\",\"value\":\"v3\",\"type\":\"gt\"},\n" +
                "{\"operation\":\"or\",\"field\":\"k4\",\"value\":\"v4\",\"type\":\"lt\"}]\n" +
                "}";
        JSONObject object = JSONObject.parseObject(json);
//        log.info("pageSize=[{}]", object.getInteger("pageSize"));
//        log.info("pageNum=[{}]", object.getInteger("pageNum"));
        JSONArray array = object.getJSONArray("condition");
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        BoolQueryBuilder shouldQuery = boolQuery();
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String operation = jsonObject.getString("operation");
            String field = jsonObject.getString("field");
            String value = jsonObject.getString("value");
            String type = jsonObject.getString("type");
            if ("and".equals(operation)) {
                if ("like".equals(type)) {
                    wildcardQuery(field, value + "*");
                } else if ("eq".equals(type)) {
                    termQuery(field, value);
                } else {
                    RangeQueryBuilder queryBuilder = rangeQuery(field);

                }
                shouldQuery.must();

            } else {
                if ("like".equals(type)) {
//                    wildcardQuery()
                } else if ("eq".equals(type)) {
//                    termQuery()
                } else {
//                    rangeQuery()
                }
                shouldQuery.should();
            }
            boolQueryBuilder.must(shouldQuery);
//            SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).build();
//            log.info("operation=[{}]",jsonObject.getString("operation"));
//            log.info("field=[{}]",jsonObject.getString("field"));
//            log.info("value=[{}]",jsonObject.getString("value"));
//            log.info("type=[{}]",jsonObject.getString("type"));
        }
    }
}
