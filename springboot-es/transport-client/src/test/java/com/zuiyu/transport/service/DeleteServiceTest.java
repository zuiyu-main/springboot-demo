package com.zuiyu.transport.service;

import com.zuiyu.transport.common.EsIndexConstant;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DeleteServiceTest {

    @Autowired
    private DeleteService deleteService;

    @Test
    public void deleteById() {
        DeleteResponse response = deleteService.deleteById(EsIndexConstant.TEST_INDEX, EsIndexConstant.INDEX_TYPE, "1");
        System.out.println("删除结果:" + response.toString());
    }

    @Test
    public void deleteByQuery() {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("gender", "male");
        BulkByScrollResponse response = deleteService.deleteByQuery(EsIndexConstant.TEST_INDEX, matchQueryBuilder);
        long deleted = response.getDeleted();
        System.out.println("删除结果:" + deleted);
    }
}
