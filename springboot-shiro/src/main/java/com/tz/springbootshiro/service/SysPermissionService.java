package com.tz.springbootshiro.service;

import java.util.List;

/**
 * @author tz
 * @Classname SysPermissionService
 * @Description
 * @Date 2019-11-10 09:04
 */
public interface SysPermissionService {
    /**
     * 查找用户权限
     * @param userId
     * @return
     */
    List<String> selectPermissionByUserId(Long userId);
}
