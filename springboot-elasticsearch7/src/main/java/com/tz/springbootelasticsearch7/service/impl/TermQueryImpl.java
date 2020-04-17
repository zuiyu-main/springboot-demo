package com.tz.springbootelasticsearch7.service.impl;

import com.tz.springbootelasticsearch7.service.QueryService;
import org.elasticsearch.index.query.AbstractQueryBuilder;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * @author https://github.com/TianPuJun @无痕
 * @ClassName BaseQueryServiceImpl
 * @Description
 * @Date 10:18 2020/4/4
 **/
public class TermQueryImpl implements QueryService {

    /**
     * 创建默认query
     *
     * @param field 查询的字段名
     * @param value 查询的值
     * @return
     */
    @Override
    public AbstractQueryBuilder createQuery(String field, String value) {
        return termQuery(field, value);
    }
}
