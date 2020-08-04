package com.tz.springbootrabbitmq.config;

import com.tz.springbootrabbitmq.rabbit.MsgHandlerService;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
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


    /**
     * 交换久初始化
     */
    public static final String DEFAULT_EXCHANGE = "defaultExchange";
    public static final String TEST_EXCHANGE = "test";
    public static final String PRIORITY_EXCHANGE = "priority";


    /**
     * 队列初始化
     */
    public static final String DEFAULT_QUEUE = "default";
    public static final String TEST_QUEUE_1 = "test.1";
    public static final String TEST_QUEUE_2 = "test.2";
    public static final String TEST_QUEUE_3 = "test.3";
    public static final String PRIORITY_QUEUE = "priority";

    /**
     * 路由key初始化
     */
    public static final String DEFAULT_ROUTE_KEY = "default.*";
    public static final String TEST_ROUTE_KEY = "test.*";
    public static final String PRIORITY_ROUTE_KEY = "priority.*";


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
        return new DirectExchange(DEFAULT_EXCHANGE);
    }

    @Bean
    public TopicExchange testExchange() {
        return new TopicExchange(TEST_EXCHANGE);
    }

    @Bean
    public TopicExchange priorityExchange() {
        return new TopicExchange(PRIORITY_EXCHANGE);
    }

    /**
     * durable 是否持久化
     *
     * @return
     */
    @Bean
    public Queue defaultQueue() {
        return new Queue(DEFAULT_QUEUE, true);
    }

    @Bean
    public Queue queue1() {
        return new Queue(TEST_QUEUE_1, true);
    }

    @Bean
    public Queue queue2() {
        return new Queue(TEST_QUEUE_2, true);
    }

    @Bean
    public Queue queue3() {
        return new Queue(TEST_QUEUE_3, true);
    }


    /**
     * 生命优先级队列
     * 参数 x-max-priority  最大优先级
     *
     * @return
     */
    @Bean
    public Queue priorityQueue() {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("x-max-priority", 10);
        return new Queue(PRIORITY_QUEUE, true, false, false, map);
    }


    /**
     * 初始化交换机与队列通过路由key绑定
     *
     * @return
     */
    @Bean
    public Binding defaultQueueBindingExchange() {
        return new Binding(DEFAULT_QUEUE, Binding.DestinationType.QUEUE, DEFAULT_EXCHANGE, DEFAULT_ROUTE_KEY, null);
    }

    @Bean
    public Binding priorityQueueBindingTestExchange() {
        return new Binding(PRIORITY_QUEUE, Binding.DestinationType.QUEUE, PRIORITY_EXCHANGE, PRIORITY_ROUTE_KEY, null);
    }

    /**
     * 另外一种队列与交换机的绑定方式
     *
     * @return
     */
    @Bean
    public Binding bindingTest1() {

        return BindingBuilder.bind(queue1()).to(testExchange()).with(RabbitConfig.TEST_ROUTE_KEY);
    }

    @Bean
    public Binding bindingTest2() {
        return BindingBuilder.bind(queue2()).to(testExchange()).with(RabbitConfig.TEST_ROUTE_KEY);
    }

    @Bean
    public Binding bindingTest3() {
        return BindingBuilder.bind(queue3()).to(testExchange()).with(RabbitConfig.TEST_ROUTE_KEY);
    }


    /**
     * 注册消费者,自定义消费者数量为5个
     */

    @Bean
    @Scope("prototype")
    public MsgHandlerService handleService() {
        return new MsgHandlerService();
    }

    @Bean
    public SimpleMessageListenerContainer mqMessageContainer(MsgHandlerService handleService) throws AmqpException, IOException {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueueNames(DEFAULT_QUEUE);
        container.setExposeListenerChannel(true);
        //设置每个消费者获取的最大的消息数量
        container.setPrefetchCount(100);
        //消费者个数
        container.setConcurrentConsumers(5);
        //设置确认模式为手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //监听处理类
        container.setMessageListener(handleService);
        return container;
    }
}
