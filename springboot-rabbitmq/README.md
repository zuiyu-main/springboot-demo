# springboot-demo
基于springboot后台框架，涉及技术，通用mapper，shiro，mysql，redis
## 测试sys表
-----
CREATE TABLE `test_sys` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
-----
## 集成rabbitmq
一个生产者，二个消费者，并行消费
参考连接
https://blog.csdn.net/qq_38455201/article/details/80308771
https://www.cnblogs.com/selwynHome/p/9609298.html