package com.tz.elastic.dao.es;

import com.tz.elastic.bean.es.TestDemo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author https://github.com/TianPuJun @无痕
 * @ClassName TestDemoDao
 * @Description test数据
 * @Date 17:52 2020/2/25
 **/
@Repository
public interface TestDemoDao extends ElasticsearchRepository<TestDemo,String> {
}
