package com.tz.elastic.dao.es;

import com.tz.elastic.bean.es.ESDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author tz
 * @Classname DocumentSearchRepository
 * @Description es dao
 * @Date 2019-07-20 09:23
 */
@Repository
public interface DocumentSearchRepository extends ElasticsearchRepository<ESDocument,String> {
}
