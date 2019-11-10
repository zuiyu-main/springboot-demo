package com.tz.springbootshiro.dao;

import com.tz.springbootshiro.bean.SysRolePermission;
import com.tz.springbootshiro.tk.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author tz
 */
@Mapper
@Repository
public interface SysRolePermissionMapper extends MyBaseMapper<SysRolePermission> {
}