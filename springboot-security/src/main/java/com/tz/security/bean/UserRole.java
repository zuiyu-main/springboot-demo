package com.tz.security.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "user_role")
public class UserRole {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;
}