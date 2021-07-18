package com.tz.sharding.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName BOrder
 * @Description
 * @Date 21:31 2021/7/18
 **/
@Entity
@Table(name = "b_order")
public class BOrder implements Serializable {
    private static final long serialVersionUID = 5301229613413342378L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "is_del")
    private Boolean isDel;
    @Column(name = "company_id")
    private Integer companyId;
    @Column(name = "position_id")
    private long positionId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "publish_user_id")
    private Integer publishUserId;
    @Column(name = "resume_type")
    private Integer resumeType;
    @Column(name = "status")
    private String status;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "operate_time")
    private Date operateTime;
    @Column(name = "work_year")
    private String workYear;
    @Column(name = "name")
    private String name;
    @Column(name = "position_name")
    private String positionName;
    @Column(name = "resumed_id")
    private Integer resumedId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getDel() {
        return isDel;
    }

    public void setDel(Boolean del) {
        isDel = del;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(Integer publishUserId) {
        this.publishUserId = publishUserId;
    }

    public Integer getResumeType() {
        return resumeType;
    }

    public void setResumeType(Integer resumeType) {
        this.resumeType = resumeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getWorkYear() {
        return workYear;
    }

    public void setWorkYear(String workYear) {
        this.workYear = workYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Integer getResumedId() {
        return resumedId;
    }

    public void setResumeId(Integer resumedId) {
        this.resumedId = resumedId;
    }
}
