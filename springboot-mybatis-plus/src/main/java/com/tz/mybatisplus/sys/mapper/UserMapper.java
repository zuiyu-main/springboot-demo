package com.tz.mybatisplus.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tz.mybatisplus.sys.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 当前系统用户 Mapper 接口
 * </p>
 *
 * @author tz
 * @since 2019-11-22
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
