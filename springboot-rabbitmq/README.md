# springboot-demo
基于springboot后台框架，涉及技术，通用mapper，shiro，mysql，redis
## 集成rabbitmq
一个生产者，二个消费者，并行消费
参考连接
https://blog.csdn.net/qq_38455201/article/details/80308771
https://www.cnblogs.com/selwynHome/p/9609298.html
sockjs 配置，stomp配置参考
[sockjs](https://docs.spring.io/spring-framework/docs/4.1.6.RELEASE/spring-framework-reference/html/websocket.html#websocket-fallback-sockjs-client)

https://www.cnblogs.com/myitroad/p/9334141.html

[构建](https://registry.hub.docker.com/_/rabbitmq/)

# rabbitmq监听关闭时间
[tcp 检测](https://www.rabbitmq.com/heartbeats.html#tcp-keepalives)
[channel](https://www.rabbitmq.com/channels.html)
[shutdown](https://www.rabbitmq.com/api-guide.html#shutdown)

# rabbitmq 消息优先级
* 前提

优先级高的消息可以被优先消费，这个也是有前提的
 如果在消费者的消费速度大于生产者的速度且Broke 中没有消息堆积的情况下 
 对发送的消息设置优先级也就没有什么实际意义。
 因为生产者刚发送完多条消息就被消费者消费了，
 那么就相当于 Broker 中至多只有一条消息，对于单条消息来说优先级是没有什么意义的。

* rabbitConfig 中初始化创建优先级队列（代码参考RabbitConfig）
```text
    public static final String EXCHANGE_TEST = "test";
    public static final String MY_EXCHANGE = "myExchange";

    @Bean
    public TopicExchange testExchange() {
        return new TopicExchange("test");
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
```
* 创建发送消息的模拟发送（代码参考msgProducer）

```text
    public void sendPriorityMsg(String exchange,String routeKey,Object msg,Integer priority){
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            //设置编码
            messageProperties.setContentEncoding("utf-8");
            //设置过期时间10*1000毫秒
            messageProperties.setPriority(priority);
            return message;
        };
        rabbitTemplate.convertAndSend(exchange,routeKey,msg,messagePostProcessor);
    }

```
* 发送消息测试,此时不要添加消费者订阅（代码参考HelloServiceImpl)

```text
        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE,RabbitConfig.PRIORITY_ROUTE_KEY,"测试优先级任务1",1);
        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE,RabbitConfig.PRIORITY_ROUTE_KEY,"测试优先级任务1-2",1);
        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE,RabbitConfig.PRIORITY_ROUTE_KEY,"测试优先级任务1-3",1);
        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE,RabbitConfig.PRIORITY_ROUTE_KEY,"测试优先级任务1-4",1);
        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE,RabbitConfig.PRIORITY_ROUTE_KEY,"测试优先级任务1-5",1);
        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE,RabbitConfig.PRIORITY_ROUTE_KEY,"测试优先级任务2-5",5);
        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE,RabbitConfig.PRIORITY_ROUTE_KEY,"测试优先级任务2-4",5);
        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE,RabbitConfig.PRIORITY_ROUTE_KEY,"测试优先级任务2-3",5);
        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE,RabbitConfig.PRIORITY_ROUTE_KEY,"测试优先级任务2-2",5);
        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE,RabbitConfig.PRIORITY_ROUTE_KEY,"测试优先级任务2-1",5);
```

* 看到队列中堆积的消息之后启动消费者（代码参考MsgReceiver）

```text
    @RabbitListener(queues = RabbitConfig.QUEUE_PRIORITY)
    @RabbitHandler
    public void priority(String content) {
        log.info("处理器myExchange的消息： " + content);
    }
```
