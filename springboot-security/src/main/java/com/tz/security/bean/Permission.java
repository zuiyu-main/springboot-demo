package com.tz.security.bean;

import javax.persistence.*;
import lombok.Data;

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