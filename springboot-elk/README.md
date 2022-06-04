
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

* 启动类main方法加入自动生成日志代码

  ```text
    @SpringBootApplication
  public class ElkApplication {
      public static final Logger log = LoggerFactory.getLogger(ElkApplication.class);
      Random random = new Random(10000);
  
      public static void main(String[] args) {
          SpringApplication.run(ElkApplication.class, args);
          new ElkApplication().initTask();
  
      }
  
      private void initTask() {
          Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
              @Override
              public void run() {
                  log.info("seed info msg :" + random.nextInt(999999));
              }
          }, 100, 100, TimeUnit.MILLISECONDS);
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





# 加入filebeat

* logstash  新建filebeat-logstash-log.conf

  ```yaml
  input{
  	beats {
          host => "192.168.123.166"
          port => 9600
  	}
  }
  
  output{
  	elasticsearch{
  	    hosts=>["192.168.123.166:9200"]
  	    index => "%{[@metadata][beat]}-%{[@metadata][version]}-%{+YYYY.MM.dd}"
      }
  }
  ```

  

* 启动

  ```text
  bin/logstash -f filebeat-logstash-log.conf
  ```

* Filebeat 修改配置文件,找到下方修改的地方修改即可，主要监听的日志文件和输出的logstash服务器地址

  ```yaml
  filebeat.inputs:
  
  - type: log
    enabled: true
    paths:
      - /cxt/codework/java/springboot-demo/logs/springboot-elk/2022-06-04/info.2022-06-04.0.log
  setup.kibana:
    Host: "192.168.123.166:5601"
  #----------------------------- Logstash output --------------------------------
  output.logstash:
    # The Logstash hosts
    hosts: ["192.168.123.166:9600"]
  ```

* springboot 应用程序配置生成日志文件的位置,resource下新建logback-spring-file.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <configuration debug="false" scan="false">
  
      <!-- Log file path -->
      <property name="log.path" value="logs/springboot-elk"/>
  
      <!-- Console log output -->
      <appender name="console"
                class="ch.qos.logback.core.ConsoleAppender">
          <encoder>
              <pattern>%d{MM-dd HH:mm:ss.SSS} %-5level [%logger{50}] - %msg%n
              </pattern>
          </encoder>
      </appender>
  
      <!-- Log file debug output -->
      <appender name="fileRolling_info" class="ch.qos.logback.core.rolling.RollingFileAppender">
          <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
              <fileNamePattern>${log.path}/%d{yyyy-MM-dd}/info.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
              <TimeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                  <maxFileSize>50MB</maxFileSize>
              </TimeBasedFileNamingAndTriggeringPolicy>
          </rollingPolicy>
          <encoder>
              <pattern>%date [%thread] %-5level [%logger{50}] %file:%line - %msg%n
              </pattern>
          </encoder>
          <!--<filter class="ch.qos.logback.classic.filter.LevelFilter"> <level>ERROR</level>
              <onMatch>DENY</onMatch> <onMismatch>NEUTRAL</onMismatch> </filter> -->
      </appender>
      <!-- Log file error output -->
      <appender name="fileRolling_error" class="ch.qos.logback.core.rolling.RollingFileAppender">
          <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
              <fileNamePattern>${log.path}/%d{yyyy-MM-dd}/error.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
              <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                  <maxFileSize>50MB</maxFileSize>
              </timeBasedFileNamingAndTriggeringPolicy>
          </rollingPolicy>
          <encoder>
              <pattern>%date [%thread] %-5level [%logger{50}] %file:%line - %msg%n
              </pattern>
          </encoder>
          <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
              <level>ERROR</level>
          </filter>
      </appender>
  
      <!-- Level: FATAL 0 ERROR 3 WARN 4 INFO 6 DEBUG 7 -->
      <root level="info">
          <!--{dev.start}-->
          <appender-ref ref="console"/>
          <!--{dev.end}-->
          <!--{alpha.start}
          <appender-ref ref="fileRolling_info" />
          {alpha.end}-->
          <!--        {release.start}-->
          <appender-ref ref="fileRolling_info"/>
          <!--        {release.end}-->
          <appender-ref ref="fileRolling_error"/>
      </root>
      <!-- Framework level setting -->
      <!--    <include resource="config/logger-core.xml" />-->
  
      <!-- Project level setting -->
      <!-- <logger name="your.package" level="DEBUG" /> -->
      <logger name="org.springframework" level="INFO"></logger>
      <logger name="org.mybatis" level="INFO"></logger>
  </configuration>
  
  ```

* application.yml文件中指定logback-spring-file.xml 

  ```text
  logging:
    # 默认logback-spring.xml 使用logstash传输到es；
    # 改为logback-spring-file.xml传输日志到归档日志文件，使用filebeat监听日志
    config: classpath:logback-spring-file.xml
  ```


