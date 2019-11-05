package com.tz.elastic.bean.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "poms", type = "content")
public class ESDocument {
    @Id
    private String id;

    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String name;

    private String projectId;

}
