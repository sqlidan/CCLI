package com.haiersoft.ccli.common.entity;

/**
 * Created by galaxy on 2017/6/30.
 */
public class ResultBean<T> {

    public static final int SUCCESS_CODE = 0;

    public static final String SUCCESS_MSG = "成功";

    private int code; // 0.成功
    private String msg;
    private T obj;

    public ResultBean() {
        code = SUCCESS_CODE;
        msg = SUCCESS_MSG;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

}
