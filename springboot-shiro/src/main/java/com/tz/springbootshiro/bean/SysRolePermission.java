package com.tz.springbootshiro.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "sys_role_permission")
public class SysRolePermission {
    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Id
    @Column(name = "permission_id")
    private Long permissionId;
}