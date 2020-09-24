package com.tz.jedis.controller;

import com.tz.jedis.util.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tz
 * @Classname TestController
 * @Description
 * @Date 2019-11-21 17:11
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/set")
    public String test() {
        JedisUtils.set("key1", "test msg", 0);
        String key1 = JedisUtils.get("key1", 0);
        System.out.println(key1);
        return key1;
    }

    @PostMapping("/upload")
    public String upload(HttpServletRequest request, MultipartFile FileData) {
        System.out.println(FileData.getName());
        return "success";
    }

}
