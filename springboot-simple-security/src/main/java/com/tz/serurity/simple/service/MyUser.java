package com.tz.serurity.simple.service;

import lombok.Data;

import java.io.Serializable;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName MyUser
 * @Description
 * @Date 18:02 2020/10/28
 **/
@Data
public class MyUser implements Serializable {
    private static final long serialVersionUID = 3497935890426858541L;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号是否过期
     */
    private boolean accountNonExpired = true;

    /**
     * 账号是否未锁定
     */
    private boolean accountNonLocked = true;

    /**
     * 用户密码凭证是否未过期
     */
    private boolean credentialsNonExpired = true;

    /**
     * 用户账户是否可用
     */
    private boolean enabled = true;
}
