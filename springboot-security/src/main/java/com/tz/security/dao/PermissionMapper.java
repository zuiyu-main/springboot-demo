package com.tz.security.dao;

import com.tz.security.bean.Permission;
import com.tz.security.bean.RolePermission;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Repository
public interface PermissionMapper extends Mapper<Permission> {
    List<RolePermission> getRolePermissions();
}