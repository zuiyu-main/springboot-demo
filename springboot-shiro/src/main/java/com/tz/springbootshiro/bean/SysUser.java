package com.tz.springbootshiro.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "sys_user")
public class SysUser {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "full_name")
    private String fullName;

    private String password;

    private String salt;
}