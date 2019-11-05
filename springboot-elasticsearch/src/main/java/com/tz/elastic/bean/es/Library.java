package com.tz.elastic.bean.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author tz
 * @Classname Library
 * @Description
 * @Date 2019-10-16 19:24
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "local_library", type = "book")
public class Library {
    /**
     *     index：是否设置分词
     *     analyzer：存储时使用的分词器
     *          ik_max_word
     *          ik_word
     *     searchAnalyze：搜索时使用的分词器
     *     store：是否存储
     *     type: 数据类型
     */
    @Id
    private Integer book_id;
    private String book_code;
    private String book_name;
    private Integer book_price;
    private String book_author;
    private String book_desc;
}
