package com.tz.jedis;

import com.tz.jedis.util.JedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JedisApplicationTests {

    @Test
    public void contextLoads() {
        JedisUtils.set("key1", "test msg", 0);

        System.out.println(JedisUtils.get("key1", 0));
    }
    @Test
    public void hashTest(){
    }

}
