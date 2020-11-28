package com.tz.code.model;

import lombok.Data;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName MyUser
 * @Description
 * @Date 15:54 2020/11/23
 **/
@Data
public class MyUser {
    private String userName;

    private String password;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;
}
