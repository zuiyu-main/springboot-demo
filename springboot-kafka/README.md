# 本文配置
* kafka client version 2.11
* zk version 3.4.13
* spring kafka version 2.3.1.RELEASE
* springboot version 2.2.0.RELEASE
* [版本对应关系](https://spring.io/projects/spring-kafka#overview)

* [kafka-docker]( https://github.com/wurstmeister/kafka-docker)

* [apache kafka](https://kafka.apache.org/090/documentation.html#brokerconfigs)
## 推荐kafka-docker github clone下来进入文件夹修改配置启动即可
#### 启动文件内容请参考resources/docker 文件夹，摘抄自kafka-docker github
####  启动流程1
```shell script
git clone https://github.com/wurstmeister/kafka-docker
cd kafka-docker
# 启动命令
docker-compose up -d
# 关闭命令
docker-compose stop
或者
docker-compose down
```
#### 启动流程2
```shell script
cd src/main/resources/docker
docker-compose up -d
```
#### 备注： docker-compose怎么安装以及相关操作自行百度安装即可，不了解的在docker-kafka github README中也已经详细说明，如启动不成功，请提交issues
* docker-compose.yml 内容

```
    version: '2'
     services:
       zookeeper:
         image: wurstmeister/zookeeper
         ports:
           - "2181:2181"
       kafka:
         build: .
         ports:
           - "9092:9092"
         environment:
           KAFKA_LISTENERS: PLAINTEXT://:9092
           KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://192.168.1.123:9092
           KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
         volumes:
           - /var/run/docker.sock:/var/run/docker.sock
```

 启动失败原因分析

####  第一个坑
主要还是对kafka不熟悉，老版本配置host.name，port，本人在kafka官网拿到的docker-compose文件，然后没有配置，
每次启动都是默认接口，例如32805，所以每次链接使用127.0.0.1:9200 都连接不成功，改为127.0.0.1:32805就可以的
另一种解决就是配置一下连接端口
##### 解决
* 第一种配置方式配置
```shell script
   KAFKA_LISTENERS: PLAINTEXT://:9092
   KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://192.168.1.123:9092
```
此处是新版配置，ip最好不要写localhost和127.0.0.1
* 第二种就是配置host和port
```shell script
KAFKA_ADVERTISED_HOST_NAME: 192.168.1.123

```
advertised.port 指定也没有生效，后续有人测试生效可以提交pr，第二种方式指定hostname之后java连接使用配置文件中默认生成的端口
端口查看方式，一种是进入容器
```shell script
docker exec -it kafka容器id /bin/bash
cd /opt/kafka/config
vi server.properties
```

找到端口在java中根据ip+这个端口进行连接
另一种查看docker端口映射，前面的端口就是我们要连接的端口
* 建议使用第一种，简单省事，直接java中配置ip:9092即可连接，注意此处ip最好不要填写localhost或者127.0.0.1





