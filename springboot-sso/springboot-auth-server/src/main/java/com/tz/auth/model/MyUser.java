package com.tz.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName MyUser
 * @Description
 * @Date 15:53 2020/12/4
 **/
@Data
public class MyUser implements Serializable {


    private static final long serialVersionUID = -7360491624866891189L;

    private String userName;
    private String password;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

}
