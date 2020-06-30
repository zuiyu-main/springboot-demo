package com.tz.springbootelasticsearch7.dao.es;

import com.tz.springbootelasticsearch7.bean.es.MyIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author https://github.com/TianPuJun @256g的胃
 * @ClassName MyIndexDao
 * @Description 测试索引dao
 * @Date 09:38 2020/6/30
 **/
@Repository
public interface MyIndexDao extends ElasticsearchRepository<MyIndex, String> {

    MyIndex findAllByContentLike(String content);
}
