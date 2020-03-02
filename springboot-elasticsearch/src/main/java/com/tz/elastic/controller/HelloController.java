package com.tz.elastic.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tz.elastic.bean.es.TestDemo;
import com.tz.elastic.dao.es.TestDemoDao;
import com.unboundid.util.json.JSONObject;
import lombok.Data;
import org.bouncycastle.util.test.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

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
    @Autowired
    private TestDemoDao testDemoDao;
    @GetMapping("/get")
    public String getTest() throws JsonProcessingException {
        TestDemo testDemo = new TestDemo();
        testDemo.setId(UUID.randomUUID().toString());
        testDemo.setName("tz");
        testDemo.setUsername("admin");
        TestDemo save = testDemoDao.save(testDemo);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(save);
        return s;


    }

}
