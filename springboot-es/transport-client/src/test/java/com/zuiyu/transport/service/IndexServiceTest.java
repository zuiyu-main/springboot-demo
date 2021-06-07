package com.zuiyu.transport.service;

import com.zuiyu.transport.common.EsIndexConstant;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.rest.RestStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@SpringBootTest
@RunWith(SpringRunner.class)
public class IndexServiceTest {

    @Autowired
    private TransportClient transportClient;
    @Autowired
    private IndexService indexService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void indexDoc() throws IOException {

        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("title", "kimchy")
                .field("time", new Date())
                .field("content", "trying out Elasticsearch")
                .endObject();
        IndexResponse response = indexService.indexDoc(EsIndexConstant.TEST_INDEX, EsIndexConstant.INDEX_TYPE, "2", builder);
        // Index name
        String _index = response.getIndex();
        // Type name
        String _type = response.getType();
        // Document ID (generated or not)
        String _id = response.getId();
        // Version (if it's the first time you index this document, you will get: 1)
        long _version = response.getVersion();
        // status has stored current instance statement.
        RestStatus status = response.status();
        System.out.println("索引：" + _index);
        System.out.println("type：" + _type);
        System.out.println("ID：" + _id);
        System.out.println("version：" + _version);
        System.out.println("status：" + status.toString());

    }

    @Test
    public void bulkIndex() throws IOException {
        XContentBuilder xContentBuilder = jsonBuilder()
                .startObject()
                .field("title", "kimchy")
                .field("time", new Date())
                .field("content", "trying out Elasticsearch")
                .field("birth", "1995-01-01")
                .field("height", "180")
                .endObject();
        IndexRequestBuilder indexRequestBuilder = transportClient.prepareIndex(EsIndexConstant.TEST_INDEX, EsIndexConstant.INDEX_TYPE, "1")
                .setSource(xContentBuilder);
        XContentBuilder xContentBuilder2 = jsonBuilder()
                .startObject()
                .field("title", "kimchy2")
                .field("time", new Date())
                .field("content", "kimchy2 trying out Elasticsearch2")
                .field("birth", "1996-01-01")
                .field("height", "190")
                .endObject();
        IndexRequestBuilder indexRequestBuilder2 = transportClient.prepareIndex(EsIndexConstant.TEST_INDEX, EsIndexConstant.INDEX_TYPE, "2")
                .setSource(xContentBuilder2);
        XContentBuilder xContentBuilder3 = jsonBuilder()
                .startObject()
                .field("title", "kimchy3")
                .field("time", new Date())
                .field("content", "kimchy3 trying out Elasticsearch3")
                .field("birth", "1997-01-01")
                .field("height", "185")
                .endObject();
        IndexRequestBuilder indexRequestBuilder3 = transportClient.prepareIndex(EsIndexConstant.TEST_INDEX, EsIndexConstant.INDEX_TYPE, "3")
                .setSource(xContentBuilder3);
        List<IndexRequestBuilder> indexRequestBuilders = new LinkedList<>();
        indexRequestBuilders.add(indexRequestBuilder);
        indexRequestBuilders.add(indexRequestBuilder2);
        indexRequestBuilders.add(indexRequestBuilder3);
        BulkResponse bulkItemResponses = indexService.bulkIndex(indexRequestBuilders);
        if (bulkItemResponses.hasFailures()) {
            System.out.println("批量插入有失败");
            BulkItemResponse[] items = bulkItemResponses.getItems();
            for (BulkItemResponse item : items) {
                System.out.println(item.getFailureMessage());
            }
        }

    }

    @Test
    public void reindex() {
        BulkByScrollResponse reindex = indexService.reindex(EsIndexConstant.TEST_INDEX, EsIndexConstant.TEST_REINDEX, matchAllQuery());
        System.out.println("reindex 结果:" + reindex.getStatus());
        System.out.println("reindex 条数:" + reindex.getTotal());
    }
}
