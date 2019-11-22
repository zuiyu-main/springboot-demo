package com.tz.mybatisplus.sys.service.impl;

import com.tz.mybatisplus.sys.entity.User;
import com.tz.mybatisplus.sys.mapper.UserMapper;
import com.tz.mybatisplus.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
