package com.tz.elastic.bean.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @author tz
 * @Classname TestDocument
 * @Description
 * @Date 2019-11-12 16:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "test",type = "test")
public class TestDocument {
    @Id
    private String id;
    @Field(type = FieldType.Auto)
    private List<String> permission;
}
