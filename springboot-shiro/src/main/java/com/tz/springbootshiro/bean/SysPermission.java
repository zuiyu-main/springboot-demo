package com.tz.springbootshiro.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "sys_permission")
public class SysPermission {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "res_name")
    private String resName;

    @Column(name = "res_type")
    private String resType;

    private String permission;

    private String url;
}