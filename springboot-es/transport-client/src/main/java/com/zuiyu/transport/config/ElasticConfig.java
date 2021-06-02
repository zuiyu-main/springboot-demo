package com.zuiyu.transport.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName ElasticConfig
 * @Description es 配置
 * @Date 21:06 2021/4/12
 **/
@SpringBootConfiguration
public class ElasticConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticConfig.class);
    @Resource
    public ElasticsearchProperties elasticsearchProperties;

    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", elasticsearchProperties.getClusterName()).build();
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName(elasticsearchProperties.getHost()), elasticsearchProperties.getPort()));
        LOGGER.info("Elasticsearch connection success Host:[{}],Port:[{}]", elasticsearchProperties.getHost(), elasticsearchProperties.getPort());
        LOGGER.info("Elasticsearch connection success clusterName:[{}])", elasticsearchProperties.getClusterName());
        return client;
    }

}
