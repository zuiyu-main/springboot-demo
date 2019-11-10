package com.tz.springbootshiro.dao;

import com.tz.springbootshiro.bean.SysUserRole;
import com.tz.springbootshiro.tk.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author tz
 */
@Mapper
@Repository
public interface SysUserRoleMapper extends MyBaseMapper<SysUserRole> {
}