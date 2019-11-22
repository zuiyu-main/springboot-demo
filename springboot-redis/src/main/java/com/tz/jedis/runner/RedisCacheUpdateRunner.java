package com.tz.jedis.runner;

import com.tz.jedis.util.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author tz
 * @Classname RedisCacheUpdateRunner
 * @Description redis 启动缓存更新
 * @Date 2019-11-21 16:31
 */
@Component
@Slf4j
@Order(10)
public class RedisCacheUpdateRunner implements CommandLineRunner {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JedisUtil jedisUtil;
    @Override
    public void run(String... args) throws Exception {
        // pattern 为要匹配等正则
        String pattern = "ke2y1";
        RedisConnection connection = redisTemplate
                .getConnectionFactory().getConnection();
        Set<byte[]> caches = connection.keys(pattern.getBytes());
        if(!caches.isEmpty()){
            Long del = connection.del(caches.toArray(new byte[][]{}));
            log.info("-------------->>>>>>>>redis server 启动清空redis 登录缓存,清除结果数量----->[{}]",del);
        }

        log.info("------------->>>>>>redis server 启动清空缓存结束");
        // 第二种清除key的方式
//        jedisUtil.delete(new String[]{"key1"},0);
    }
}
