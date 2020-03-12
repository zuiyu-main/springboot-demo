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