package com.tz.springbootkafka.config;

import com.tz.springbootkafka.constant.MyTopic;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tz
 * @Classname TopicConfig
 * @Description topic配置 自动创建的topic分区数是1，复制因子是0
 * @Date 2019-11-08 09:17
 */
@EnableKafka
@SpringBootConfiguration
public class TopicConfig {
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic foo() {
        // 第一个是参数是topic名字，第二个参数是分区个数
        // 第三个是topic的复制因子个数
        // ----------------->>>>>>>>>>>>>>>当broker个数为1个时会创建topic失败，
        //提示：replication factor: 2 larger than available brokers: 1
        //只有在集群中才能使用kafka的备份功能
        return new NewTopic(MyTopic.FOO, 5, (short) 1);
    }

    @Bean
    public NewTopic bar() {
        return new NewTopic(MyTopic.BAR, 5, (short) 1);
    }
    @Bean
    public NewTopic test(){
        return new NewTopic(MyTopic.TEST, 5, (short) 1);
    }

    @Bean
    public NewTopic topic1(){
        return new NewTopic(MyTopic.TOPIC1, 5, (short) 1);
    }

    @Bean
    public NewTopic topic2(){
        return new NewTopic(MyTopic.TOPIC2, 5, (short) 1);
    }
}
