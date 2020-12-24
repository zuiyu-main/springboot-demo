package com.tz.springbootrabbitmq.util;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static com.tz.springbootrabbitmq.config.RabbitConfig.*;
import static com.tz.springbootrabbitmq.config.RabbitConfig.TEST_QUEUE_2;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName RabbitUtils
 * @Description
 * @Date 14:55 2020/12/24
 **/
@Configuration
public class RabbitUtils {
    private static final Logger logger = LoggerFactory.getLogger(RabbitUtils.class);

    private final RabbitAdmin rabbitAdmin;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitUtils(RabbitAdmin rabbitAdmin, RabbitTemplate rabbitTemplate) {
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 转换Message对象
     *
     * @param messageType 返回消息类型 MessageProperties类中常量
     * @param msg
     * @return
     */
    public Message getMessage(String messageType, Object msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(messageType);
        Message message = new Message(msg.toString().getBytes(), messageProperties);
        return message;
    }

    /**
     * 有绑定Key的Exchange发送
     *
     * @param routingKey
     * @param msg
     */
    public void sendMessageToExchange(TopicExchange topicExchange, String routingKey, Object msg) {
        Message message = getMessage(MessageProperties.CONTENT_TYPE_JSON, msg);
        rabbitTemplate.send(topicExchange.getName(), routingKey, message);
    }

    /**
     * 没有绑定KEY的Exchange发送
     *
     * @param exchange
     * @param msg
     */
    public void sendMessageToExchange(TopicExchange topicExchange, AbstractExchange exchange, String msg) {
        addExchange(exchange);
        logger.info("RabbitMQ send " + exchange.getName() + "->" + msg);
        rabbitTemplate.convertAndSend(topicExchange.getName(), msg);
    }

    /**
     * 给queue发送消息
     *
     * @param queueName
     * @param msg
     */
    public void sendToQueue(String queueName, String msg) {
        sendToQueue(DirectExchange.DEFAULT, queueName, msg);
    }

    /**
     * 给direct交换机指定queue发送消息
     *
     * @param directExchange
     * @param queueName
     * @param msg
     */
    public void sendToQueue(DirectExchange directExchange, String queueName, String msg) {
        Queue queue = new Queue(queueName);
        addQueue(queue);
        Binding binding = BindingBuilder.bind(queue).to(directExchange).withQueueName();
        rabbitAdmin.declareBinding(binding);
        //设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
        rabbitTemplate.convertAndSend(directExchange.getName(), queueName, msg);
    }

    /**
     * 给queue发送消息
     *
     * @param queueName
     * @param msg
     */
    public String receiveFromQueue(String queueName) {
        return receiveFromQueue(DirectExchange.DEFAULT, queueName);
    }

    /**
     * 给direct交换机指定queue发送消息
     *
     * @param directExchange
     * @param queueName
     * @param msg
     */
    public String receiveFromQueue(DirectExchange directExchange, String queueName) {
        Queue queue = new Queue(queueName);
        addQueue(queue);
        Binding binding = BindingBuilder.bind(queue).to(directExchange).withQueueName();
        rabbitAdmin.declareBinding(binding);
        String messages = (String) rabbitTemplate.receiveAndConvert(queueName);
        System.out.println("Receive:" + messages);
        return messages;
    }

    /**
     * 创建Exchange
     *
     * @param exchange
     */
    public void addExchange(AbstractExchange exchange) {
        rabbitAdmin.declareExchange(exchange);
    }

    /**
     * 删除一个Exchange
     *
     * @param exchangeName
     */
    public boolean deleteExchange(String exchangeName) {
        return rabbitAdmin.deleteExchange(exchangeName);
    }


    /**
     * Declare a queue whose name is automatically named. It is created with exclusive = true, autoDelete=true, and
     * durable = false.
     *
     * @return Queue
     */
    public Queue addQueue() {
        return rabbitAdmin.declareQueue();
    }

    /**
     * 创建一个指定的Queue
     *
     * @param queue
     * @return queueName
     */
    public String addQueue(Queue queue) {
        return rabbitAdmin.declareQueue(queue);
    }

    /**
     * Delete a queue.
     *
     * @param queueName the name of the queue.
     * @param unused    true if the queue should be deleted only if not in use.
     * @param empty     true if the queue should be deleted only if empty.
     */
    public void deleteQueue(String queueName, boolean unused, boolean empty) {
        rabbitAdmin.deleteQueue(queueName, unused, empty);
    }

    /**
     * 删除一个queue
     *
     * @param queueName
     * @return true if the queue existed and was deleted.
     */
    public boolean deleteQueue(String queueName) {
        return rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     *
     * @param queue
     * @param exchange
     * @param routingKey
     */
    public void addBinding(Queue queue, TopicExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 绑定一个Exchange到一个匹配型Exchange 使用一个routingKey
     *
     * @param exchange
     * @param topicExchange
     * @param routingKey
     */
    public void addBinding(Exchange exchange, TopicExchange topicExchange, String routingKey) {
        Binding binding = BindingBuilder.bind(exchange).to(topicExchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 去掉一个binding
     *
     * @param binding
     */
    public void removeBinding(Binding binding) {
        rabbitAdmin.removeBinding(binding);
    }

    /**
     * 获取队列消息数量
     *
     * @param queue
     * @return
     * @throws IOException
     */
    public int getMessageCount(String queue) throws IOException {
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        // 创建链接
        Connection connection = connectionFactory.createConnection();
        // 创建信道
        Channel channel = connection.createChannel(false);
        // 清空 指定队列
//        channel.queuePurge(TEST_QUEUE_2);

        // 删除队列
//        channel.queueDelete(TEST_QUEUE_2);
        // 创建队列
//        rabbitUtils.addQueue(new Queue(TEST_QUEUE_2, true));
        // 绑定队列
//        channel.queueBind(TEST_QUEUE_2,TEST_EXCHANGE,TEST_ROUTE_KEY);

        // 创建一个type=direct 持久化的 非自动删除的交换器
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queue);

        // 获取队列中的消息个数
        return declareOk.getMessageCount();

    }
}
