package com.tz.mybatisplus.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.mybatisplus.common.mapper.CommonMapper;
import com.tz.mybatisplus.sys.entity.User;
import com.tz.mybatisplus.sys.mapper.UserMapper;
import com.tz.mybatisplus.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 当前系统用户 服务实现类
 * </p>
 *
 * @author tz
 * @since 2019-11-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private CommonMapper commonMapper;

    @Override
    public List<Map<String, Object>> findUserList(String sql) {
        return commonMapper.executeSql(sql);
    }
}
