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

    private String userName;

    private String password;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;
}
