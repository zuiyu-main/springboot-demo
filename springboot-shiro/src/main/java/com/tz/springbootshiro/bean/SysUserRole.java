package com.tz.springbootshiro.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "sys_user_role")
public class SysUserRole {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "role_id")
    private Long roleId;
}