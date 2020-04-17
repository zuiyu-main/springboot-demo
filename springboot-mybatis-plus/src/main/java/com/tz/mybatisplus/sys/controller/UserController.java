package com.tz.mybatisplus.sys.controller;


import com.tz.mybatisplus.sys.entity.User;
import com.tz.mybatisplus.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 当前系统用户 前端控制器
 * </p>
 *
 * @author tz
 * @since 2019-11-22
 */
@RestController
@RequestMapping("/sys/user")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @GetMapping("/save")
    public String save(){
        User user = new User();
        user.setFullName("fullName");
        user.setPassword("12312313");
        user.setSalt("12312312111");
        user.setUserName("test");
        iUserService.save(user);
        return user.toString();
    }

    @GetMapping("/get")
    public String get(){

        return String.valueOf(iUserService.count());
    }
}
