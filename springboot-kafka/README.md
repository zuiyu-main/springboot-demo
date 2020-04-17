## 本文配置
* kafka client version 2.11
* zk version 3.4.13
* spring kafka version 2.3.1.RELEASE
* springboot version 2.2.0.RELEASE
* [版本对应关系](https://spring.io/projects/spring-kafka#overview)
* [kafka-docker]( https://github.com/wurstmeister/kafka-docker)
* [apache kafka](https://kafka.apache.org/090/documentation.html#brokerconfigs)

## 推荐kafka-docker github clone下来进入文件夹修改配置启动即可
 * 启动文件内容请参考resources/docker 文件夹，摘抄自kafka-docker github
 
###  启动流程1 （使用resource下docker目录文件）
```shell script
git clone https://github.com/wurstmeister/kafka-docker

cd kafka-docker

# 启动命令
docker-compose up -d

# 关闭命令
docker-compose stop

# 关闭并删除
docker-compose down
```
### 启动流程2 （使用下面docker-compose.yml）

* docker-compose.yml 内容

```
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka:2.11-0.11.0.3
    ports:
      - "9092:9092"
    environment:
      KAFKA_LISTENERS: PLAINTEXT://:9092
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://127.0.0.1:9092
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```
* 命令启动

```shell script
# 启动命令
docker-compose up -d

# 关闭命令
docker-compose stop

# 关闭并删除
docker-compose down
```

~~~  
备注： docker-compose怎么安装以及相关操作自行百度安装即可，不了解的在docker-kafka github README中也已经详细说明，如启动不成功，请提交issues
~~~



##  坑位
* springboot配置ip+端口是否正确配置
* kafka启动端口，监听ip是否正确

## kafka配置ip端口的地方

* 第一种配置方式配置

```shell script
   KAFKA_LISTENERS: PLAINTEXT://:9092
   KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://127.0.0.1:9092
```

* 第二种就是配置host和port

```shell script
KAFKA_ADVERTISED_HOST_NAME: 192.168.1.123
# advertised.port 指定也没有生效，后续有人测试生效可以提交pr，第二种方式指定hostname之后java连接使用配置文件中默认生成的端口9092
```

* 容器暴露端口在docker-compose.yml文件中指定

##  端口查看方式

* 进入容器
```shell script
docker exec -it kafka容器id /bin/bash
cd /opt/kafka/config
vi server.properties
```
找到端口在java中根据ip+这个端口进行连接

* docker-compose 日志查看
```shell script
docker-compose logs --tail="400" -f
```
* 直接使用docker ps 找到指定的kafka容器启动端口
```shell script
docker ps
```





