package com.tz.springbootshiro.service.impl;

import com.tz.springbootshiro.bean.SysPermission;
import com.tz.springbootshiro.bean.SysRolePermission;
import com.tz.springbootshiro.bean.SysUserRole;
import com.tz.springbootshiro.dao.SysPermissionMapper;
import com.tz.springbootshiro.dao.SysRolePermissionMapper;
import com.tz.springbootshiro.dao.SysUserRoleMapper;
import com.tz.springbootshiro.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.Assert;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author tz
 * @Classname SysPermissionServiceImpl
 * @Description
 * @Date 2019-11-10 09:04
 */
@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    /**
     * 查找用户权限
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> selectPermissionByUserId(Long userId) {
        Assert.isTrue(null != userId,"the parameter userId must not be null");
        List<String> res = new LinkedList<>();
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        List<SysUserRole> select = sysUserRoleMapper.select(sysUserRole);
        if(CollectionUtils.isEmpty(select)){
            return new ArrayList<>(1);
        }
        select.forEach(role->{
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setRoleId(role.getRoleId());
            List<SysRolePermission> permissions = sysRolePermissionMapper.select(sysRolePermission);
            if(!CollectionUtils.isEmpty(permissions)){
                permissions.forEach(e->{
                    SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(e.getPermissionId());
                    res.add(sysPermission.getPermission());
                });
            }
        });
        return res;
    }
}
