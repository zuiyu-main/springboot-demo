package com.tz.sharding.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName PositionDetail
 * @Description
 * @Date 16:14 2021/7/17
 **/
@Entity
@Table(name = "position_detail")
public class PositionDetail implements Serializable {

    private static final long serialVersionUID = 5829366671507950253L;
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "pid")
    private long pid;
    @Column(name = "description")
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
