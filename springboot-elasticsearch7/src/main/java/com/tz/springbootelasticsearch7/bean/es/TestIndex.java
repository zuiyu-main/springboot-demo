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
@Document(indexName = "test_index", createIndex = true)
public class TestIndex {
    @Field(type = FieldType.Text)
    private String field1;
    @Field(type = FieldType.Keyword)
    private String field2;
    @Field(type = FieldType.Long)
    private String field3;
    private String field4;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }
}
