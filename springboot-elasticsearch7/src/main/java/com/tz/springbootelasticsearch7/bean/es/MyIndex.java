package com.tz.springbootelasticsearch7.bean.es;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author https://github.com/TianPuJun @256g的胃
 * @ClassName MyIndex
 * @Description 测试索引 分词
 * @Date 09:35 2020/6/30
 **/
@Document(indexName = "my_index", createIndex = true)
public class MyIndex {
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
    private String content;
    @Field(type = FieldType.Text)
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
