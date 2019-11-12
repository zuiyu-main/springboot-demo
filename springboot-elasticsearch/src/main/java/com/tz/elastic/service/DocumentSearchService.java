package com.tz.elastic.service;



import com.tz.elastic.bean.es.EsDocument;

import java.util.List;

/**
 * @author tz
 * @Classname DocumentSearchService
 * @Description es search
 * @Date 2019-07-20 09:24
 */
public interface DocumentSearchService {
    EsDocument getDocumentById(String id) ;


    void deleteDocumentById(String id);



    void saveDocument(List<EsDocument> ESDocuments);



    List<EsDocument> getDocumentsByNameOrderByCreateOn(String name,
                                                       String projectId,
                                                       String orderField);
    void save(EsDocument esDocument);
    void delete(String id);
    void getById(String id);
    void getByName(String name, String projectId);

}
