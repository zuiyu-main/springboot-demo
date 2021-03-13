package com.tz.springbootelasticsearch7.runner;


import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tz
 * @Classname InitElasticIndexRunner
 * @Description
 * @Date 2019-11-15 14:13
 */
@Component
@Order(10)
public class InitElasticIndexRunner implements CommandLineRunner {


    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void run(String... args) throws Exception {

//        log.info("初始化创建索引");
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        // 创建索引
//        createIndex(client);
        // 创建索引模版，规则test*
//        createIndexTemp(client);
        // 创建索引模版结束，创建test_1,会使用模版的字段
//        elasticsearchTemplate.createIndex("test_1");


    }

    /**
     * 初始化创建索引
     */
    private void createIndex(RestHighLevelClient client) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("tweets_1");
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
        );
        // 索引方式1 start
//        request.mapping(
//                "{\n" +
//                        "  \"properties\": {\n" +
//                        "    \"message\": {\n" +
//                        "      \"type\": \"text\"\n" +
//                        "    }\n" +
//                        "  }\n" +
//                        "}",
//                XContentType.JSON);
        // 索引方式1 end
        // 索引方式2 start
        Map<String, Object> message = new HashMap<>();
        message.put("type", "text");
        Map<String, Object> content = new HashMap<>();
        content.put("type", "text");
        Map<String, Object> title = new HashMap<>();
        title.put("type", "keyword");
        Map<String, Object> properties = new HashMap<>();
        properties.put("message", message);
        properties.put("content", content);
        properties.put("title", title);
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        request.mapping(mapping);
        // 索引方式2 end
        // 索引方式3 start
//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.startObject("properties");
//            {
//                builder.startObject("message");
//                {
//                    builder.field("type", "text");
        // 增加测试子字段
//                    builder.startObject("fields");
//                    {
//                        builder.startObject("keyword");
//                        builder.field("ignore_above",256);
//                        builder.field("type","keyword");
//                        builder.endObject();
//                    }
//                    builder.endObject();
//                }
//                builder.endObject();
//            }
//            builder.endObject();
//        }
//        builder.endObject();
//        request.mapping(builder);

        // 索引方式3 end

        // 索引别名
        request.alias(new Alias("tweets_search").filter(QueryBuilders.termQuery("name", "tz")));
        request.setTimeout(TimeValue.timeValueMinutes(2));
        request.setMasterTimeout(TimeValue.timeValueMinutes(1));
        // 同步执行
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        // 异步执行
//        ActionListener<CreateIndexResponse> listener =
//                new ActionListener<CreateIndexResponse>() {
//
//                    @Override
//                    public void onResponse(CreateIndexResponse createIndexResponse) {
//                        log.info("异步执行创建索引成功返回");
//
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//                        log.error("异步执行创建索引失败返回");
//
//                    }
//                };
//
//        client.indices().createAsync(request, RequestOptions.DEFAULT, listener);


        // 响应
        boolean acknowledged = createIndexResponse.isAcknowledged();
//        log.info("创建索引响应1：[{}]", acknowledged);
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
//        log.info("创建索引响应2：[{}]", shardsAcknowledged);


    }

    /**
     * 创建索引模版
     *
     * @throws IOException
     */
    public static final void createIndexTemp(RestHighLevelClient client) throws IOException {

        PutIndexTemplateRequest request = new PutIndexTemplateRequest("test_index_template");
        List<String> indexPatterns = new ArrayList<String>();
        indexPatterns.add("test*");
        request.patterns(indexPatterns);

        /** mapping */
        XContentBuilder jsonBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("_source")
                .field("enabled", false)
                .endObject()

                .startObject("properties")
                .startObject("hostname")
                .field("type", "keyword")
                .endObject()
                .startObject("gmtCreate")
                .field("type", "date")
                .field("format", "yyyy-MM-dd HH:mm:ss")
                .endObject()

                .endObject()
                .endObject();


        request.mapping("_doc", jsonBuilder);

        Map<String, Object> settings = new HashMap<>();

        settings.put("number_of_shards", 1);
        settings.put("number_of_replicas", 0);
        request.settings(settings);
        AcknowledgedResponse acknowledgedResponse = client.indices().putTemplate(request, RequestOptions.DEFAULT);
//        log.info("创建索引模版返回:[{}]", acknowledgedResponse.isAcknowledged());

    }

}
