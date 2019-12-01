package com.tz.jedis;

import com.tz.jedis.util.JedisUtil;
import io.swagger.models.auth.In;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JedisApplicationTests {
    @Autowired
    JedisUtil jedisUtil;

    @Autowired
    RedisTemplate redisTemplate;
    @Test
    public void contextLoads() {
        jedisUtil.set("key1","test msg",0);

        System.out.println(jedisUtil.get("key1",0));
    }
    @Test
    public void hashTest(){
    }

}
