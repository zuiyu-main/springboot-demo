package com.tz.springbootshiro.controller;

import com.tz.springbootshiro.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tz
 * @Classname SysUserController
 * @Description 用户控制器
 * @Date 2019-11-09 19:17
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

}
