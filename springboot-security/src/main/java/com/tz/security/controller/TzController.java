package com.tz.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author tz
 * @Classname TzController
 * @Description
 * @Date 2019-12-08 10:08
 */
@Controller
public class TzController {
    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/index")
    @ResponseBody
    public Object index() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @RequestMapping("/hello1")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin')")
    public String hello1() {
        return "hello1";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
