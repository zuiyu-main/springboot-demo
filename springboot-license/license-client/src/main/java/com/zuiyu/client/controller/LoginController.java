package com.zuiyu.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zuiyu
 * @date 2022/8/15
 * @description
 * @link https://github.com/zuiyu-main
 */

@RestController
public class LoginController {

    public static Logger log = LoggerFactory.getLogger(LoginController.class);

    @GetMapping(value = "login")
    public Map<String, Object> test(@RequestParam(required = true) String loginName, @RequestParam(required = true) String password) {
        Map<String, Object> result = new HashMap<>(1);
        log.info(MessageFormat.format("登录名称：{0}，密码：{1}", loginName, password));
        //模拟登录
        log.info("模拟登录被拦截检查证书可用性");
        result.put("code", 200);
        return result;
    }
}
