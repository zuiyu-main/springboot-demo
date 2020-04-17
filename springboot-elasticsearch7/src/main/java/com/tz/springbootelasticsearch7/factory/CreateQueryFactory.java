package com.tz.springbootelasticsearch7.factory;

import com.tz.springbootelasticsearch7.bean.QueryTypeEnum;
import com.tz.springbootelasticsearch7.service.QueryService;

/**
 * @author https://github.com/TianPuJun @无痕
 * @ClassName CreateQueryFactory
 * @Description
 * @Date 10:05 2020/4/4
 **/
public class CreateQueryFactory {
    /**
     * 反射获取所有创建es语句的前缀
     */
    private static final String QUERY_SERVICE_PREFIX = "com.tz.springbootelasticsearch7.service.impl.";

    public static QueryService getAbstractQueryBuilder(String type) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        QueryTypeEnum queryTypeEnum = QueryTypeEnum.valueOf(type.toUpperCase());
        Class<?> aClass = Class.forName(QUERY_SERVICE_PREFIX + queryTypeEnum.getValue());
        return (QueryService) aClass.newInstance();
    }
}
