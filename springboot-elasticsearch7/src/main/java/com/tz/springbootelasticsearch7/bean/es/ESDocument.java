package com.tz.springbootelasticsearch7.bean.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @author tz
 * @Classname ESDocument
 * @Description es bean
 * @Date 2019-07-20 09:21
 */
@Data
@Document(indexName = "poms", type = "content")
public class ESDocument {
    @Id
    private String id;

    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String name;

    private String projectId;

    public ESDocument(String id, String name, String projectId) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
    }
    public ESDocument() {

    }
}
