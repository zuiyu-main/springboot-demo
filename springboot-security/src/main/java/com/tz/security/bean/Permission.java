package com.tz.security.bean;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String url;

    private String name;

    private String description;

    private Long pid;
}