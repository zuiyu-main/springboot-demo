package com.tz.springbootshiro.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "sys_role")
public class SysRole {
    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name")
    private String roleName;
}