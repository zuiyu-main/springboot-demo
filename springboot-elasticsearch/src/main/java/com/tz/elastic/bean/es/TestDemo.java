package com.tz.elastic.bean.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author https://github.com/TianPuJun @无痕
 * @ClassName TestDemo
 * @Description
 * @Date 18:01 2020/2/25
 **/
@Data
@Document(indexName = "test_demo", type = "test", createIndex = true)
public class TestDemo {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Keyword)
    private String username;
    /**
     * transient
     * 可以不生成映射
     */
    @Transient
    private String name;
}
