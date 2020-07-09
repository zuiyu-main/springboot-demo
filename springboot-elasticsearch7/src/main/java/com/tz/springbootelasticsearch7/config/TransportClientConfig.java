//package com.tz.springbootelasticsearch7.config;
//
//
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.springframework.boot.SpringBootConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//
//import javax.annotation.PostConstruct;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
///**
// * @author tz
// * @Classname ElasticSearchConfig
// * @Description es-config
// * @Date 2019-07-18 09:40
// */
//@SpringBootConfiguration
//public class TransportClientConfig extends ElasticsearchConfigurationSupport {
////    @PostConstruct
////    void init() {
////        System.setProperty("es.set.netty.runtime.available.processors", "false");
////    }
//
//    /**
//     * 此处配置带xpack验证的es，需去除yml配置，二者选其一，
//     * yml 无密码
//     * 此处有密码
//     *
//     * @return
//     * @throws UnknownHostException
//     */
////    @Bean
////    public TransportClient transportClient() throws UnknownHostException {
////        TransportClient client = new PreBuiltXPackTransportClient(Settings.builder()
////                .put("cluster.name", "docker-cluster")
////    账号elastic 密码changeme
////                .put("xpack.security.user", "elastic:changeme")
////                .build())
////                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
////        return client;
////    }
//    @Bean
//    public Client elasticsearchClient() throws UnknownHostException {
//        Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
//        TransportClient client = new PreBuiltTransportClient(settings);
//        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
//        return client;
//    }
//
//    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
//    public ElasticsearchTemplate elasticsearchTemplate() throws UnknownHostException {
//        return new ElasticsearchTemplate(elasticsearchClient());
//    }
//
//}
