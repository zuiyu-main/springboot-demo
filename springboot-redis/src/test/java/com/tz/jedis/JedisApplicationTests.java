package com.tz.jedis;

import com.tz.jedis.util.JedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JedisApplicationTests {
    @Autowired
    JedisUtil jedisUtil;

    @Test
    public void contextLoads() {
        jedisUtil.set("key1","test msg",0);
        System.out.println(jedisUtil.get("key1",0));
    }

}
