package com.tz.security.bean;

import javax.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Table(name = "role")
public class Role  {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String name;

}