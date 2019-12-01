package com.tz.springbootshiro.controller;

import com.tz.springbootshiro.common.CodeMsg;
import com.tz.springbootshiro.common.ResultBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author tz
 * @Classname LoginController
 * @Description 登录
 * @Date 2019-11-10 09:52
 */
@Controller
public class LoginController {
    /**
     * shiro 跳转登录页面使用
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String defaultLogin() {
        return "/login/login";
    }

    /**
     * 登录页面表单提交使用
     * @param username
     * @param password
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/login")
    public ResultBean login(@RequestParam("userName") String username, @RequestParam("password") String password) {
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 执行认证登陆
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            return new ResultBean(CodeMsg.ACCOUNT_NOT_FOUND);
        } catch (IncorrectCredentialsException ice) {
            return new ResultBean(CodeMsg.PASSWORD_ERROR);
        } catch (LockedAccountException lae) {
            return new ResultBean(CodeMsg.ACCOUNT_LOCK);
        } catch (ExcessiveAttemptsException eae) {
            return new ResultBean(CodeMsg.ERROR_NUM_MORE);
        } catch (AuthenticationException ae) {
            return new ResultBean(CodeMsg.USERNAME_PASSWORD_ERROR);
        }
        if (subject.isAuthenticated()) {
            return new ResultBean(CodeMsg.LOGIN_SUCCESS);
        } else {
            token.clear();
            return new ResultBean(CodeMsg.LOGIN_ERROR);
        }
    }
}
