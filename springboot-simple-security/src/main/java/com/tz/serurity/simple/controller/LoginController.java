package com.tz.serurity.simple.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tz
 * @Classname LoginController
 * @Description
 * @Date 2019-12-18 11:40
 */
@RestController
public class LoginController {
    @RequestMapping("/login-success")
    public String login(){
        return "login success";
    }
    @RequestMapping("/r/r1")
    public String r1(){
        return "r1";
    }
    @RequestMapping("/r/r2")
    public String r2(){
        return "r2";
    }
}
