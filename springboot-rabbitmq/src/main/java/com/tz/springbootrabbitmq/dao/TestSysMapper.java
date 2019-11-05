package com.tz.springbootrabbitmq.dao;


import com.tz.springbootrabbitmq.bean.TestSys;
import com.tz.springbootrabbitmq.tk.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author liBai
 */
@Mapper
@Repository
public interface TestSysMapper extends MyBaseMapper<TestSys> {
}