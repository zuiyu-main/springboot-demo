package com.zuiyu.transport.service;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName DeleteService
 * @Date 20:27 2021/4/13
 **/
public interface DeleteService {
    /**
     * 根据id在索引index中删除type的文档
     *
     * @param index 索引
     * @param type  类型
     * @param id    id
     * @return
     */
    DeleteResponse deleteById(String index, String type, String id);

    /**
     * 根据条件在索引index中删除
     *
     * @param index        索引
     * @param queryBuilder 删除条件
     * @return
     */
    BulkByScrollResponse deleteByQuery(String index, QueryBuilder queryBuilder);
}
