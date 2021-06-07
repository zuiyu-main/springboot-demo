package com.zuiyu.transport.service.impl;

import com.zuiyu.transport.common.EsIndexConstant;
import com.zuiyu.transport.service.SearchService;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName SearchServiceImpl
 * @Description 搜索服务
 * @Date 16:24 2021/4/13
 **/
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private TransportClient transportClient;

    @Override
    public GetResponse getById(String index, String type, String id) {
        return transportClient.prepareGet(index, type, id).get();
    }

    @Override
    public List<Map<String, Object>> multiGetById(List<MultiGetRequest.Item> items) {
        List<Map<String, Object>> res = new LinkedList<>();
        MultiGetRequestBuilder multiGetRequestBuilder = transportClient.prepareMultiGet();
        items.forEach(multiGetRequestBuilder::add);
        MultiGetResponse multiGetItemResponses = multiGetRequestBuilder.get();
        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                res.add(response.getSourceAsMap());
            }
        }
        return res;
    }

    @Override
    public void usrScrollSearch(QueryBuilder queryBuilder, String... indices) {
        SearchResponse scrollResp = transportClient.prepareSearch(indices)
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(queryBuilder)
                .setSize(100).get();
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                //Handle the hit...
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                System.out.println("输出结果:" + sourceAsMap.toString());

            }

            scrollResp = transportClient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        } while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.

    }

    @Override
    public void multiSearch(List<SearchRequestBuilder> searchRequestBuilders) {
        SearchRequestBuilder srb1 = transportClient
                .prepareSearch().setQuery(QueryBuilders.queryStringQuery("elasticsearch")).setSize(1);
        SearchRequestBuilder srb2 = transportClient
                .prepareSearch().setQuery(QueryBuilders.matchQuery("title", "Upsert")).setSize(1);

        MultiSearchResponse items = transportClient.prepareMultiSearch().add(srb1).add(srb2).get();
        // You will get all individual responses from MultiSearchResponse#getResponses()
        long nbHits = 0;
        for (MultiSearchResponse.Item item : items.getResponses()) {
            SearchResponse response = item.getResponse();
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                System.out.println("命中结果:" + sourceAsMap.toString());

            }
            nbHits += response.getHits().getTotalHits().value;
        }
        System.out.println("总条数：" + nbHits);

    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_metrics_aggregations.html#java-aggs-metrics-stats
     */
    @Override
    public void useAggregations() {
        SearchResponse searchResponse = transportClient.prepareSearch(EsIndexConstant.TEST_INDEX)
                .setQuery(matchAllQuery())
                .addAggregation(AggregationBuilders.terms("agg1").field("title.keyword"))
                .addAggregation(AggregationBuilders.dateHistogram("agg2").field("birth").calendarInterval(DateHistogramInterval.YEAR))
                .addAggregation(AggregationBuilders.min("agg3").field("height"))
                .addAggregation(AggregationBuilders.max("agg4").field("height"))
                .addAggregation(AggregationBuilders.sum("agg5").field("height"))
                .addAggregation(AggregationBuilders.avg("agg6").field("height"))
                .addAggregation(AggregationBuilders.stats("agg7").field("height"))
                .addAggregation(AggregationBuilders.extendedStats("agg8").field("height"))
                .addAggregation(AggregationBuilders.count("agg9").field("height"))
                .addAggregation(AggregationBuilders.percentiles("agg10").field("height"))
                .get();
        Map<String, Aggregation> asMap = searchResponse.getAggregations().getAsMap();
//        doc_count_error_upper_bound 未知
//        有两个错误值会显示在项的聚合上，第一个doc_count_error_upper_bound ，给出个没有被算进最后的结果的最大可能的数字。这个表明在最坏情况下，有doc_count_error_upper_bound 这么多文档个数的一个值被遗漏了。 这就是doc_count_error_upper_bound （文档数错误上界）这个参数的意义
//        如果设置show_term_doc_count_error这个参数为ture，还会对每个 bucket都显示一个错误数，表示最大可能的误差情况。
//        sum_other_doc_count 当有大量不同值时，ES只返回数量最多的项。这个数字表示有多少文档的统计数量没有返回。
//        用order参数可以做排序，默认是按照doc_count倒序排列的。
//        可以改变默认情况
//          “order” : { “_count” : “asc” } 这是按照doc_count升序排列
//          “order” : { “_term” : “asc” } 这是按照字母表升序排列。
        System.out.println("agg1数据：" + asMap.get("agg1"));
        System.out.println("agg2数据：" + asMap.get("agg2"));
        System.out.println("agg3数据：" + asMap.get("agg3"));
        System.out.println("agg4数据：" + asMap.get("agg4"));
        System.out.println("agg5数据：" + asMap.get("agg5"));
        System.out.println("agg6数据：" + asMap.get("agg6"));
        System.out.println("agg7数据：" + asMap.get("agg7"));
        System.out.println("agg8数据：" + asMap.get("agg8"));
        System.out.println("agg9数据：" + asMap.get("agg9"));
        System.out.println("agg10数据：" + asMap.get("agg10"));


    }
}
