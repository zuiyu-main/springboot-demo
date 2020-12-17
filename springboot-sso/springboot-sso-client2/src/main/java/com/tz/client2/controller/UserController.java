package com.tz.client2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName UserController
 * @Description
 * @Date 12:05 2020/12/17
 **/
@RestController
public class UserController {
    @GetMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
