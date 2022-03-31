package com.tz.mybatisplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tz.mybatisplus.sys.entity.User;
import com.tz.mybatisplus.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        user.setSalt("salt-salt");
        user.setUserName("test");
        user.setDeleted(0);
        iUserService.save(user);
        return user.toString();
    }

    @GetMapping("/get")
    public String get() {

//        return String.valueOf(iUserService.count());

        QueryWrapper<User> qw = new QueryWrapper();
        qw.lambda().eq(User::getUserName, "test");
        List<User> list = iUserService.list(qw);
        return list.toString();
//
//        String sql = "SELECT Count(*) FROM sys_user WHERE USER_NAME = 'test1'";
//        List<Map<String, Object>> userList = iUserService.findUserList(sql);
//        return userList.toString();

//        boolean b = iUserService.removeById(10);
//        return "b";
    }


}
