package com.tz.springbootelasticsearch7.bean;

/**
 * @author cxt
 */

public enum QueryTypeEnum {
    /**
     * eq
     */
    EQ("term", "TermQueryImpl"),
    LIKE("wildcard", "WildcardQueryImpl"),

    /**
     * range
     */
    GT("gtRange", "RangeQueryImpl");
    private String key;
    private String value;

    QueryTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
