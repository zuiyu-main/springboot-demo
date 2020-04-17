package com.tz.springbootelasticsearch7.service.impl;

import com.tz.springbootelasticsearch7.service.QueryService;
import org.elasticsearch.index.query.AbstractQueryBuilder;

import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

/**
 * @author https://github.com/TianPuJun @无痕
 * @ClassName WildcardQueryImpl
 * @Description 模糊查询语句生成
 * @Date 17:45 2020/4/4
 **/
public class WildcardQueryImpl implements QueryService {

    /**
     * 创建默认query
     *
     * @param field 查询的字段名
     * @param value 查询的值
     * @return
     */
    @Override
    public AbstractQueryBuilder createQuery(String field, String value) {
        return wildcardQuery(field, "*" + value + "*");
    }
}
