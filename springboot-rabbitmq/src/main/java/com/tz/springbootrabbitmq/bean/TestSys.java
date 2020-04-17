package com.tz.springbootrabbitmq.bean;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "test_sys")
public class TestSys {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String name;
}