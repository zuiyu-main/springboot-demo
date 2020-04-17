package com.tz.springbootshiro.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tz
 * @Classname CodeMsg
 * @Description 返回错误代码集合
 * @Date 2019-11-10 09:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeMsg {
    private int code;
    private String msg;

    /*----------------通用的错误码------------------*/

    public static CodeMsg SUCCESS = new CodeMsg(200, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");

    /*----------------登录模块 5002XX--------------------*/

    public static CodeMsg ACCOUNT_LOCK = new CodeMsg(500210, "账号已锁定");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500211, "密码不正确");
    public static CodeMsg ACCOUNT_NOT_FOUND = new CodeMsg(500212, "未知账号");
    public static CodeMsg ERROR_NUM_MORE = new CodeMsg(500213, "用户名密码错误次数过多");
    public static CodeMsg USERNAME_PASSWORD_ERROR = new CodeMsg(500214, "用户名或密码不正确");
    public static CodeMsg LOGIN_ERROR = new CodeMsg(500215, "登录失败");
    public static CodeMsg LOGIN_SUCCESS = new CodeMsg(500216, "登录成功");



}
