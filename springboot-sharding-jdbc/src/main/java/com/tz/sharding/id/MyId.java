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
