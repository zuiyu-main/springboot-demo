package com.tz.security.bean;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "role")
public class Role  {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String name;

}