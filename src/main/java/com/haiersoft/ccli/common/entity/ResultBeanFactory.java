package com.haiersoft.ccli.common.entity;

/**
 * Created by galaxy on 2017/6/30.
 */
public class ResultBeanFactory {

    public static <T> ResultBean<T> generate() {
        return new ResultBean<T>();
    }

}
