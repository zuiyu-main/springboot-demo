package com.zuiyu.transport.service;

import com.zuiyu.transport.common.EsIndexConstant;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UpdateServiceTest {

    @Autowired
    private UpdateService updateService;
    @Autowired
    private TransportClient transportClient;

    @Test
    public void updateById() throws IOException, ExecutionException, InterruptedException {

        String id = "2";
        XContentBuilder xContentBuilder = jsonBuilder()
                .startObject()
                .field("title", "醉鱼")
                .endObject();
        UpdateResponse updateResponse = updateService.updateById(EsIndexConstant.TEST_INDEX, id, xContentBuilder);
        System.out.println("RESULT：" + updateResponse.getResult());
    }

    @Test
    public void upsertById() throws IOException, ExecutionException, InterruptedException {
        XContentBuilder source = jsonBuilder()
                .startObject()
                .field("title", "醉鱼")
                .field("time", LocalDateTime.now())
                .endObject();
        XContentBuilder doc = jsonBuilder()
                .startObject()
                .field("title", "Upsert")
                .field("content", "Upsert content")
                .endObject();
        UpdateResponse updateResponse = updateService.upsertById(EsIndexConstant.TEST_INDEX, "1", source, doc);
        System.out.println("更新结果：" + updateResponse.getResult());

    }

    @Test
    public void updateByQuery() {
        UpdateByQueryRequestBuilder updateByQuery =
                new UpdateByQueryRequestBuilder(transportClient, UpdateByQueryAction.INSTANCE);
        updateByQuery.source(EsIndexConstant.TEST_INDEX)
                .filter(QueryBuilders.termQuery("title.keyword", "kimchy"))
                .maxDocs(1000)
                .script(new Script(ScriptType.INLINE,
                        "painless",
                        // 修改title=kimchy的数据中content=content+:absolutely
                        "ctx._source.content = ctx._source.content+':absolutely'",
                        Collections.emptyMap()));
        BulkByScrollResponse response = updateService.updateByQuery(updateByQuery);
        System.out.println("status:" + response.getStatus());


    }
}
