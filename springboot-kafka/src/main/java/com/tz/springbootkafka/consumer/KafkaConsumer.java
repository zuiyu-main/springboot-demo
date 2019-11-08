package com.tz.springbootkafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

/**
 * @author tz
 * @Classname KafkaConsumer
 * @Description 消费者
 * @Date 2019-11-06 17:01
 */
@Component
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = "test",groupId = "group1")
    public void processMessage(String content) {
        log.info("消费者1监听消息,消息内容=[{}]",content);
    }
    /**
     *     id是消费者监听容器
     *     配置topic和分区：监听两个topic，分别为topic1、topic2，topic1只接收分区0，3的消息，
     *     topic2接收分区0和分区1的消息，但是分区1的消费者初始位置为5
     * @param record
     */
    @KafkaListener(id = "myContainer1",
            topicPartitions =
                    { @TopicPartition(topic = "topic1", partitions = { "0", "3" }),
                            @TopicPartition(topic = "topic2", partitions = "0",
                                    partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "4"))
                    })
    public void listen(ConsumerRecord<?, ?> record) {
        log.info("topic1消息监听，topic={},key={},value={}",record.topic(),record.key(),record.value());
    }


    @KafkaListener(id = "myContainer2",topics = {"foo","bar"})
    public void listen2(ConsumerRecord<?, ?> record){
        log.info("foo,bar 多主题消息监听，topic={},key={},value={}",record.topic(),record.key(),record.value());

    }
}
