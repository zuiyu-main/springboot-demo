package com.tz.security.dao;

import com.tz.security.bean.SysUser;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@org.apache.ibatis.annotations.Mapper
@Repository
public interface SysUserMapper extends Mapper<SysUser> {
}