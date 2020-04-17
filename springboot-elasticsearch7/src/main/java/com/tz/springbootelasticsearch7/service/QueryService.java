package com.tz.springbootelasticsearch7.service;

import org.elasticsearch.index.query.AbstractQueryBuilder;

/**
 * @author tz
 */
public interface QueryService {
    /**
     * 创建默认query
     *
     * @param field 查询的字段名
     * @param value 查询的值
     * @return
     */
    AbstractQueryBuilder createQuery(String field, String value);
}
