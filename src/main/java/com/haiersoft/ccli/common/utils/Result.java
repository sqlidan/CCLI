package com.haiersoft.ccli.common.utils;

import java.util.List;
import java.util.Map;

import com.haiersoft.ccli.common.persistence.Page;

public class Result<T> {

	/**
	 * 消息代码,0表示成功，1表示默认失败。<br/>
	 * -1至-100为系统定义异常，不可占用
	 */
	private int code = 1;
	/**
	 * 消息
	 */
	private String msg = null;
	/**
	 * 异常堆栈
	 */
	private String trace = null;
	/**
	 * 调用的参数名称，顺序按照参数的下标
	 */
	private String[] keys;
	/**
	 * 调用的参数的值，顺序按照草书名称的下标
	 */
	private String[] values;

	/**
	 * map
	 */
	private Map<?, ?> map;

	/**
	 * list集合
	 */
	private List<?> list;
	
	/**
	 * 对象类
	 */
	private T obj;
	
	/**
	 * 分页对象
	 */
	private Page<T> page;
	
	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	/** 
	 * @return code
	 */
	public int getCode() {
		return code;
	}

	/** 
	 * @param: 给code注入值code 
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/** 
	 * @return msg
	 */
	public String getMsg() {
		return msg;
	}

	/** 
	 * @param: 给msg注入值msg 
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/** 
	 * @return trace
	 */
	public String getTrace() {
		return trace;
	}

	/** 
	 * @param: 给trace注入值trace 
	 */
	public void setTrace(String trace) {
		this.trace = trace;
	}

	/** 
	 * @return keys
	 */
	public String[] getKeys() {
		return keys;
	}

	/** 
	 * @param: 给keys注入值keys 
	 */
	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	/** 
	 * @return values
	 */
	public String[] getValues() {
		return values;
	}

	/** 
	 * @param: 给values注入值values 
	 */
	public void setValues(String[] values) {
		this.values = values;
	}

	/** 
	 * @return map
	 */
	public Map<?, ?> getMap() {
		return map;
	}

	/** 
	 * @param: 给map注入值map 
	 */
	public void setMap(Map<?, ?> map) {
		this.map = map;
	}

	/** 
	 * @return list
	 */
	public List<?> getList() {
		return list;
	}

	/** 
	 * @param: 给list注入值list 
	 */
	public void setList(List<?> list) {
		this.list = list;
	}

	
}
