package com.tz.elastic.dao.es;

import com.tz.elastic.bean.es.Library;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author tz
 * @Classname LibraryRepository
 * @Description
 * @Date 2019-10-16 19:25
 */
@Repository
public interface LibraryRepository extends ElasticsearchRepository<Library, Long> {
}
