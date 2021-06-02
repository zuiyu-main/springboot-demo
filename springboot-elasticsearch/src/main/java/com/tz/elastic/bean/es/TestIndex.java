package com.tz.elastic.bean.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * @author 醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName TestIndex
 * @Description TestIndex
 * @Date 22:22 2021/3/30
 **/
@Document(indexName = EsIndexConstant.TEST_INDEX, type = EsIndexConstant.INDEX_TYPE)
/**
 * createIndex = false 设置不自动创建索引
 * 字段设置或者set方法  org.springframework.data.annotation.Transient,可以不再es中自动生成索引
 * @Document 作用在类，标记实体类为文档对象，一般有两个属性 indexName：对应索引库名称，type：对应在索引库中的类型,es7之后只有一个type _doc,此处自定义类型，shards：分片数量，默认5，replicas：副本数量，默认1
 * @Id 作用在成员变量，标记一个字段作为id主键
 * @Field 作用在成员变量，标记为文档的字段，并指定字段映射属性：type：字段类型，是枚举：FieldType，可以是text、long、short、date、integer、object等
 * text：存储数据时候，会自动分词，并生成索引；keyword：存储数据时候，不会分词建立索引
 * Numerical：数值类型，分两类；基本数据类型：long、integer、short、byte、double、float、half_float。浮点数的高精度类型：scaled_float；需要指定一个精度因子，比如10或100。elasticsearch会把真实值乘以这个因子后存储，取出时再还原
 * Date：日期类型；elasticsearch可以对日期格式化为字符串存储，但是建议我们存储为毫秒值，存储为long，节省空间。
 * index：是否索引，布尔类型，默认是true
 * store：是否存储，布尔类型，默认是false
 * analyzer：分词器名称，这里的ik_max_word即使用ik分词器
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestIndex {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Keyword)
    private String title;
    @Field(type = FieldType.Text)
    private String content;
    @Field(type = FieldType.Boolean)
    private Boolean status;
    @Field(type = FieldType.Date)
    private Date time;
    @Field(type = FieldType.Long)
    private Long gmtCreate;
    @Field(type = FieldType.Long)
    private Long gmtModified;
    @Field(type = FieldType.Integer)
    private Integer deleted;
    /**
     * 保存字符串数组，对应keyword 匹配，指定类型为list中对象类型
     */
    @Field(type = FieldType.Keyword)
    private List<String> permission;


}
