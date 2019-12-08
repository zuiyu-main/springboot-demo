package com.tz.security.bean;

import javax.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
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