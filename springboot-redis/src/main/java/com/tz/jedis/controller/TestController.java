package com.tz.jedis.controller;

import com.tz.jedis.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tz
 * @Classname TestControllr
 * @Description
 * @Date 2019-11-21 17:11
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private JedisUtil jedisUtil;
    @GetMapping("/set")
    public String test(){
        jedisUtil.set("key1","test msg",0);
        String key1 = jedisUtil.get("key1", 0);
        System.out.println(key1);
        return key1;
    }
}
