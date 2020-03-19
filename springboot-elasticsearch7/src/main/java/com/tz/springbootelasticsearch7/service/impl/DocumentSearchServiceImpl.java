//package com.tz.springbootelasticsearch7.service.impl;
//
//
//import com.tz.springbootelasticsearch7.bean.es.ESDocument;
//import com.tz.springbootelasticsearch7.dao.es.DocumentSearchRepository;
//import com.tz.springbootelasticsearch7.service.DocumentSearchService;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.elasticsearch.index.query.QueryBuilders.*;
//
///**
// * @author tz
// * @Classname DocumentSearchServiceImpl
// * @Description es 实现
// * @Date 2019-07-20 09:25
// */
//@Service
//public class DocumentSearchServiceImpl implements DocumentSearchService {
//    @Autowired
//    private DocumentSearchRepository documentSearchRepository;
//
//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//    @Override
//    public ESDocument getDocumentById(String id) {
//        ESDocument country = documentSearchRepository.findById(id).orElse(new ESDocument("6","6","6"));
//        return country;
//    }
//
//
//    @Override
//    public void deleteDocumentById(String id) {
//        SearchQuery query = new NativeSearchQueryBuilder().withQuery(boolQuery()
//                .must(termQuery("id",id)).must(termQuery("name","name1"))).build();
//        List<ESDocument> esDocuments = elasticsearchTemplate.queryForList(query, ESDocument.class);
//        System.out.println(esDocuments.toString());
//        documentSearchRepository.deleteById(id);
//    }
//
//
//    @Override
//    public void saveDocument(List<ESDocument> ESDocuments) {
//        documentSearchRepository.saveAll(ESDocuments);
//    }
//
//
//
//    @Override
//    public List<ESDocument> getDocumentsByNameOrderByCreateOn(String name, String
//            projectId, String orderField)  {
//        List<ESDocument> ESDocuments = new ArrayList<>();
//        try {
//            SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                    .withQuery(matchQuery("name", name))
//                    .withFilter(matchPhraseQuery("projectId", projectId))
//                    .withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
////                    .withSort(new FieldSortBuilder(orderField).order(SortOrder.DESC))
//                    .build();
//            ESDocuments = elasticsearchTemplate.queryForList(searchQuery,
//                    ESDocument.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ESDocuments;
//    }
//
//    @Override
//    public void save(ESDocument esDocument) {
//        ESDocument save = documentSearchRepository.save(esDocument);
//        System.out.println(save.toString());
//    }
//
//    @Override
//    public void delete(String id) {
//        documentSearchRepository.deleteById(id);
//    }
//
//    @Override
//    public void getById(String id) {
//        ESDocument esDocument = documentSearchRepository.findById(id).orElse(new ESDocument());
//        System.out.println(esDocument.toString());
//
//    }
//
//    @Override
//    public void getByName(String name, String projectId) {
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("name",name)).withQuery(matchQuery("projectId",projectId)).build();
//        List<ESDocument> esDocuments = elasticsearchTemplate.queryForList(searchQuery, ESDocument.class);
//        esDocuments.forEach(e-> System.out.println(e.toString()));
//    }
//
//}
