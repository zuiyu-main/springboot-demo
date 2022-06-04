# springboot+logStash+elasticsearch+kibana

## 版本
* elasticsearch 7.4.2
* logStash 7.4.2
* springboot 2.1.10

# 下载地址

选择下载的产品和版本，进行下载即可

```text
https://www.elastic.co/cn/downloads/past-releases
```



# 部署

## 启动Elasticsearch 

* 设置配置文件elasticsearch

  ```yaml
  cluster.name: my-application
  node.name: node-1
  path.data: /cxt/software/maces/7.4.2/elasticsearch-7.4.2/data
  path.logs: /cxt/software/maces/7.4.2/elasticsearch-7.4.2/logs
  network.host: 0.0.0.0
  http.port: 9200
  discovery.seed_hosts: ["127.0.0.1"]
  cluster.initial_master_nodes: ["node-1"]
  ```

* 启动

  ```bash
  bin/elasticsearch
  ```

  

## 启动Kibana

* 直接去bin目录下启动即可， 都是本机启动无需修改配置

  ```bash
  bin/kibana
  ```

  

## 启动LogStash

* config文件夹下创建springboot-log.conf,该配置文件的作用是启动在本机9600端口，后面springboot应用可以直接往9600发送日志。input为日志输入，output为日志输出到elasticsearch

  ```yaml
  input{
  # 启动在9600端口，输出在控制台
  	tcp {
          mode => "server"
          host => "0.0.0.0"
          port => 9600
          codec => json_lines
  	}
  }
  
  output{
  	elasticsearch{
  	    hosts=>["192.168.123.166:9200"]
  	    index => "springboot-logstash-%{+YYYY.MM.dd}"
      }
  #	stdout{
  #		codec => rubydebug
  #	}
  }
  ```

  

* 启动

  ```bash
  bin/logstash -f config/springboot-log.conf
  ```

## 启动SpringBoot应用

* pom

  ```text
  		<dependency>
  			<groupId>net.logstash.logback</groupId>
  			<artifactId>logstash-logback-encoder</artifactId>
  			<version>7.0</version>
  		</dependency>
  ```

  

* testController 方法

  ```text
  @RestController
  public class TestController {
      public static final Logger log = LoggerFactory.getLogger(TestController.class);
      @RequestMapping("/test")
      public String test(){
          log.info("this is a log from springboot");
          log.trace("this is a trace log ");
          return "success";
      }
  }
  
  ```

  

* resource新建logback-spring.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <configuration>
      <include resource="org/springframework/boot/logging/logback/base.xml" />
      <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
          <!--配置logStash 服务地址-->
          <destination>192.168.123.166:9600</destination>
          <!-- 日志输出编码 -->
          <encoder charset="UTF-8"
                   class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
              <providers>
                  <timestamp>
                      <timeZone>UTC</timeZone>
                  </timestamp>
                  <pattern>
                      <pattern>
                          {
                          "logLevel": "%level",
                          "serviceName": "${springAppName:-}",
                          "pid": "${PID:-}",
                          "thread": "%thread",
                          "class": "%logger{40}",
                          "detail": "%message"
                          }
                      </pattern>
                  </pattern>
              </providers>
          </encoder>
      </appender>
  
      <root level="INFO">
          <appender-ref ref="LOGSTASH" />
          <appender-ref ref="CONSOLE" />
      </root>
  </configuration>
  
  
  ```

## 验证

* 按顺序启动好了之后打开es-head插件即可查看索引信息，其中有一条调用接口的信息，还有应用启动的消息

  ![image-20220604110812146](https://s2.loli.net/2022/06/04/GFOsdLXK8QTWufk.png)

* Kibana数据展示

  * 设置索引规则

    ![image-20220604110933554](https://s2.loli.net/2022/06/04/XYHBkRytl9QE27i.png)

    输入之后设置时间戳匹配

  * 展示数据,选择Discover

    ![image-20220604111403630](https://s2.loli.net/2022/06/04/bLB2qv8i9cNOz36.png)

