package com.tz.mybatisplus.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName CommonMapper
 * @Description
 * @Date 16:27 2022/3/30
 **/
@Repository
@Mapper
public interface CommonMapper {
    List<Map<String, Object>> executeSql(String sql);
}
