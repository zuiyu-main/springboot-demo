package com.tz.security.dao;

import com.tz.security.bean.Role;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface RoleMapper extends Mapper<Role> {
    List<Role> getRolesByUserId(Long id);
}