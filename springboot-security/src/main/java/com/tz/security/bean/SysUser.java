package com.tz.security.bean;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Table(name = "sys_user")
public class SysUser {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String username;

    private String password;
    private List<Role> authorities;


}