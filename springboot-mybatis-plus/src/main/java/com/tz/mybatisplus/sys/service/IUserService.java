package com.tz.mybatisplus.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.mybatisplus.sys.entity.User;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 当前系统用户 服务类
 * </p>
 *
 * @author tz
 * @since 2019-11-22
 */
public interface IUserService extends IService<User> {

    List<Map<String, Object>> findUserList(String sql);

}
