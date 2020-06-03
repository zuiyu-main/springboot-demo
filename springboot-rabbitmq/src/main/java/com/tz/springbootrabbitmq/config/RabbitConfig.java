package com.tz.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tz
 * @Classname RabbitConfig
 * @Description
 * @Date 2019-09-18 17:36
 */
@SpringBootConfiguration
public class RabbitConfig {
    /**
     Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
     Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
     Queue:消息的载体,每个消息都会被投到一个或多个队列。
     Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
     Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
     vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
     Producer:消息生产者,就是投递消息的程序.
     Consumer:消息消费者,就是接受消息的程序.
     Channel:消息通道,在客户端的每个连接里,可建立多个channel.
     */
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    public static final String EXCHANGE_A = "test_exchange";
    public static final String EXCHANGE_TEST = "test";
    public static final String MY_EXCHANGE = "myExchange";


    public static final String TEST_A = "test.1";
    public static final String TEST_B = "test.2";
    public static final String TEST_C = "test.3";

    public static final String TEST_ROUTE_KEY = "test.*";
    public static final String PRIORITY_ROUTE_KEY = "priority";
    public static final String PRIORITY_ROUTE_KEY_TEST = "priority.test";

    public static final String QUEUE_PRIORITY = "queue.priority.1";
    public static final String QUEUE_PRIORITY_2 = "queue.priority.2";


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("my_vhost");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }
    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     HeadersExchange ：通过添加属性key-value匹配
     DirectExchange:按照routingkey分发到指定队列
     TopicExchange:多关键字匹配 定义 route key 可以群发多个队列
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE_A);
    }

    @Bean
    public TopicExchange testExchange() {
        return new TopicExchange("test");
    }

    /**
     * 获取队列A
     *
     * @return
     */
    @Bean
    public Queue queueA() {
        //队列持久
        return new Queue(TEST_A, true);
    }

    @Bean
    public Queue queueB() {
        //队列持久
        return new Queue(TEST_B, true);
    }

    @Bean
    public Queue queueC() {
        //队列持久
        return new Queue(TEST_C, true);
    }


    @Bean
    public Queue priority_Queue() {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("x-max-priority", 10);
        return new Queue(QUEUE_PRIORITY, true, false, false, map);
    }

    @Bean
    public Queue priority_Queue_2() {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("x-max-priority", 10);
        return new Queue(QUEUE_PRIORITY_2, true, false, false, map);
    }

    @Bean
    public DirectExchange myExchange() {
        return new DirectExchange(MY_EXCHANGE);
    }

    @Bean
    public Binding myBindingPriority() {
        return new Binding("queue.priority.1", Binding.DestinationType.QUEUE, "myExchange", PRIORITY_ROUTE_KEY, null);
    }

    @Bean
    public Binding myBindingPriority2() {
        return new Binding("queue.priority.2", Binding.DestinationType.QUEUE, "test", PRIORITY_ROUTE_KEY_TEST, null);
    }

    @Bean
    public Binding binding() {

        return BindingBuilder.bind(queueA()).to(testExchange()).with(RabbitConfig.TEST_ROUTE_KEY);
    }

    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(queueB()).to(testExchange()).with(RabbitConfig.TEST_ROUTE_KEY);
    }

    @Bean
    public Binding bindingC() {
        return BindingBuilder.bind(queueC()).to(testExchange()).with(RabbitConfig.TEST_ROUTE_KEY);
    }

}
