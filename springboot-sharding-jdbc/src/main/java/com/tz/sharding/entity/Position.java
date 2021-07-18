package com.tz.sharding.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName position
 * @Description
 * @Date 10:22 2021/7/17
 **/
@Entity
@Table(name = "position")
public class Position implements Serializable {

    private static final long serialVersionUID = -5164155277567424586L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "salary")
    private String salary;
    @Column(name = "city")
    private String city;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
