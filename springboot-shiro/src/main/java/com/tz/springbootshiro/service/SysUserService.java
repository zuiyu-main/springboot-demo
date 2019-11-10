package com.tz.springbootshiro.service;

import com.tz.springbootshiro.bean.SysUser;

/**
 * @author tz
 * @Classname SysUserService
 * @Description
 * @Date 2019-11-09 19:18
 */
public interface SysUserService {
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    SysUser findByUserName(String username);

}
