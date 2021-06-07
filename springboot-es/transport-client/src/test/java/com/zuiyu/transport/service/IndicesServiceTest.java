package com.zuiyu.transport.service;

import com.zuiyu.transport.common.EsIndexConstant;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@SpringBootTest
@RunWith(SpringRunner.class)
public class IndicesServiceTest {
    @Autowired
    private IndicesService indicesService;

    @Test
    public void createIndex() {
        Settings settings = Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
                .build();
        CreateIndexResponse index = indicesService.createIndex(EsIndexConstant.TEST_INDEX, settings);
        System.out.println("CreateIndexResponse:" + index.index());
    }

    @Test
    public void testCreateIndex() throws IOException {
        Settings settings = Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
                .build();
        XContentBuilder mapping = jsonBuilder()
                .startObject()
                .startObject(EsIndexConstant.INDEX_TYPE)
                .startObject("properties")
                .startObject("title").field("type", "keyword").endObject()
                .startObject("content").field("type", "text").endObject()
                .startObject("height").field("type", "integer").endObject()
                .startObject("birth").field("type", "date").endObject()
                .endObject()
                .endObject()
                .endObject();
        CreateIndexResponse index = indicesService.createIndex(EsIndexConstant.TEST_INDEX, settings, mapping);
        System.out.println("CreateIndexResponse:" + index.index());
    }

    @Test
    public void putMapping() throws IOException {
        XContentBuilder mapping = jsonBuilder()
                .startObject()
                .startObject(EsIndexConstant.INDEX_TYPE)
                .startObject("properties")
                .startObject("time").field("type", "date").endObject()
                .endObject()
                .endObject()
                .endObject();

        AcknowledgedResponse acknowledgedResponse = indicesService.putMapping(EsIndexConstant.TEST_INDEX, EsIndexConstant.INDEX_TYPE, mapping);
        System.out.println("acknowledgedResponse:" + acknowledgedResponse.isAcknowledged());
    }

    @Test
    public void updateSettings() {
        Settings settings = Settings.builder()
                // 副本数量修改
                .put("index.number_of_replicas", 0)
                .build();
        AcknowledgedResponse acknowledgedResponse = indicesService.updateSettings(EsIndexConstant.TEST_INDEX, settings);
        System.out.println("acknowledgedResponse:" + acknowledgedResponse.isAcknowledged());
    }

    @Test
    public void deleteIndices() {
        AcknowledgedResponse acknowledgedResponse = indicesService.deleteIndices(EsIndexConstant.TEST_INDEX);
        System.out.println("acknowledgedResponse:" + acknowledgedResponse.isAcknowledged());
    }
}
