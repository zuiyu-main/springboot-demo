package com.tz.springbootshiro.common;

import lombok.Data;

/**
 * @author tz
 * @Classname ResultBean
 * @Description 统一返回结果
 * @Date 2019-11-10 09:48
 */
@Data
public class ResultBean<T> {
    private int code;
    private String msg;
    private T data;

    /**
     *  成功时候的调用
     * */
    public static  <T> ResultBean<T> success(T data){
        return new ResultBean<T>(data);
    }

    /**
     *  失败时候的调用
     * */
    public static  <T> ResultBean<T> error(CodeMsg codeMsg){
        return new ResultBean<T>(codeMsg);
    }

    private ResultBean(T data) {
        this.data = data;
    }

    private ResultBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResultBean(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }
    }
