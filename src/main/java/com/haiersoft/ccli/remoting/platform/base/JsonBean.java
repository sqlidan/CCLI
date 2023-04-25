package com.haiersoft.ccli.remoting.platform.base;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class JsonBean<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6301090774720402654L;

	public static final String SUCCESS = "0";

    public static final String ERROR = "1";

    /**
     * 0成功，1失败
     * 默认值为0，即成功
     */
    private String code = SUCCESS;

    /**
     * 格式类型
     */
    private String type;

    /**
     * 返回数据
     */
    private T result;

    public JsonBean() {

    }

    public JsonBean(String type) {
        this.type = type;
    }

    /**
     *
     * @param code 状态码
     * @param type 数据格式
     * @param result 返回值
     */
    public JsonBean(String code, String type, T result) {
        this.code = code;
        this.type = type;
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

}
