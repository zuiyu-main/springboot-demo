# sharding-jdbc分库分表

## 水平分表
### pom 设置

```text
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
            <version>2.2.5.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>2.2.5.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.25</version>
        </dependency>
        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
            <version>4.1.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
```
### sharding-datasource 设置
```text
# datasource

spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/sharding1
        username: root
        password: root
      ds1:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/sharding2
        username: root
        password: root
# sharding-datasource
    sharding:
      tables:
        # 表名
        position:
          # 切分策略：database-strategy,actual-data-nodes,table-strategy,key-generator,logic-table
          database-strategy:
            # 行表达式格式
            inline:
              # 按照哪个字段
              sharding-column: id
              algorithm-expression: ds${id % 2}

```

### 设置sharding 自增主键策略,id列使用雪花算法
```text
spring:
  shardingsphere:
    sharding:
      tables:
        # 表名
        position:
          key-generator:
            column: id
            type: SNOWFLAKE
```

### 自定义主键
* 自定义主键生成类
```java
package com.tz.sharding.id;

import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

import java.util.Properties;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName MyId
 * @Description
 * @Date 16:01 2021/7/17
 **/
public class MyId implements ShardingKeyGenerator {

    private SnowflakeShardingKeyGenerator snowflakeShardingKeyGenerator = new SnowflakeShardingKeyGenerator();

    @Override
    public Comparable<?> generateKey() {
        System.err.println("执行了自定义生成主键key ......");
        return snowflakeShardingKeyGenerator.generateKey();
    }

    @Override
    public String getType() {
        return "ZUIYU";
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

```
* resources 创建配置文件 META_INF/servies/org.apache.shardingsphere.spi.keygen.shardingKeyGenerator

```text
com.tz.sharding.id.MyId
```
## 垂直分表
```text
spring:
    sharding:
      tables:
        b_order:
          database-strategy:
            inline:
# 根据公司分库
              sharding-column: company_id
              algorithm-expression: ds$->{company_id % 2}
          table-strategy:
            inline:
# 根据id分表，生成b_order1/2
              sharding-column: id
              algorithm-expression: b_order${id % 2}
          actual-data-nodes: ds${0..1}.b_order${0..1}
          key-generator:
            column: id
            type: SNOWFLAKE
``` 
