package com.tz.tkmapper.dao;


import com.tz.tkmapper.bean.TestSys;
import com.tz.tkmapper.tk.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author liBai
 */
@Mapper
@Repository
public interface TestSysMapper extends MyBaseMapper<TestSys> {
}