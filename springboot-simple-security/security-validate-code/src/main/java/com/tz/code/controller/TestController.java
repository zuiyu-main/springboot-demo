package com.tz.code.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName TestController
 * @Description
 * @Date 15:44 2020/11/23
 **/
@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello() {
        return "hello spring security";
    }

    @GetMapping("/index")
    @ResponseBody
    public Object index() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/index2")
    @ResponseBody
    public Object index(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/index3")
    @ResponseBody
    public Object currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

}
