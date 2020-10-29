package com.tz.serurity.simple.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tz
 * @Classname LoginController
 * @Description
 * @Date 2019-12-18 11:40
 */
@Controller
public class LoginController {
    @RequestMapping("/r/r1")
    @ResponseBody
    public String r1() {
        return "r1";
    }

    @RequestMapping("/r/r2")
    @ResponseBody
    public String r2() {
        return "r2";
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
