package com.tz.tkmapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TkmapperApplicationTests {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void cleanRedis(){
        redisTemplate.opsForValue().set("key","v11al",1, TimeUnit.MINUTES);
//        redisTemplate.delete("key");
    }
}
