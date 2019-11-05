package com.tz.tkmapper.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "test_sys")
public class TestSys {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String name;
}