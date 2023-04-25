package com.haiersoft.ccli.outsidequery.entity;

import java.util.HashMap;

public class ResponseVo  extends HashMap<String, Object> {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4548885580133225171L;
	
	// 成功
	private static final Integer SUCCESS = 0;
	// 警告
	private static final Integer WARN = 1;
	// 异常 失败
	private static final Integer FAIL = 500;
	
	public static ResponseVo warn(String msg) {
		ResponseVo responseVo = new ResponseVo();
		responseVo.put("code", WARN);
		responseVo.put("msg", msg);
		responseVo.put("success", false);		
		return responseVo;
	}

	
	public static ResponseVo ok(Object data) {
		ResponseVo responseVo = new ResponseVo();
		responseVo.put("code", SUCCESS);
		responseVo.put("msg", "操作成功");
		responseVo.put("result", data);
		responseVo.put("success", true);		
		return responseVo;
	}

	
	public static ResponseVo error(Object msg) {
		ResponseVo responseVo = new ResponseVo();
		responseVo.put("code", FAIL);
		responseVo.put("msg", msg);
		responseVo.put("success", false);	
		return responseVo;
	}
	
	public static ResponseVo pledgeOk() {
		ResponseVo responseVo = new ResponseVo();
		responseVo.put("code", SUCCESS);
		responseVo.put("msg", "操作成功");
		responseVo.put("success", true);		
		return responseVo;
	}

	public static ResponseVo success() {
		ResponseVo responseVo = new ResponseVo();
		responseVo.put("code", SUCCESS);
		responseVo.put("msg", "操作成功");
		responseVo.put("success", true);		
		return responseVo;
	}
	
	
}
