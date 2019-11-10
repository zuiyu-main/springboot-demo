package com.tz.springbootshiro.service.impl;

import com.tz.springbootshiro.bean.SysUser;
import com.tz.springbootshiro.dao.SysUserMapper;
import com.tz.springbootshiro.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tz
 * @Classname SysUserServiceImpl
 * @Description
 * @Date 2019-11-09 19:18
 */
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public SysUser findByUserName(String username) {
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        SysUser user = sysUserMapper.selectOne(sysUser);
        return user;
    }
}
