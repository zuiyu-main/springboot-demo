package com.tz.security.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "role_permission")
public class RolePermission {
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "permission_id")
    private Long permissionId;
}