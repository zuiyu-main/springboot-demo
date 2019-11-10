package com.tz.springbootshiro.tk;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author liBai
 * @Classname MyBaseMapper
 * @Description TODO base mapper 不能被扫描
 * @Date 2019-06-02 10:41
 */
public interface MyBaseMapper<T>  extends Mapper<T>, MySqlMapper<T> , ExampleMapper<T> {
}
