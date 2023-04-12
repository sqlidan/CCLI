package com.haiersoft.ccli.api.entity;

import java.util.HashMap;

public class InStockInfoResponseVo  extends HashMap<String, Object> {	
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
	
	public static InStockInfoResponseVo warn(String msg) {
		InStockInfoResponseVo responseVo = new InStockInfoResponseVo();
		responseVo.put("errorCode", WARN);
		responseVo.put("errorMsg", msg);
		responseVo.put("success", false);		
		return responseVo;
	}

	
	public static InStockInfoResponseVo ok(Object data,InStockInfoSort inStockInfoSort) {
		InStockInfoResponseVo responseVo = new InStockInfoResponseVo();
		responseVo.put("errorCode", SUCCESS);
		responseVo.put("errorMsg", "操作成功");
		responseVo.put("pageSize",inStockInfoSort.getPageSize());
		responseVo.put("pageNum",inStockInfoSort.getPageNum());
		responseVo.put("total",inStockInfoSort.getTotal());
		responseVo.put("result", data);
		responseVo.put("success", true);		
		return responseVo;
	}

	
	public static InStockInfoResponseVo error(Object msg) {
		InStockInfoResponseVo responseVo = new InStockInfoResponseVo();
		responseVo.put("errorCode", FAIL);
		responseVo.put("errorMsg", msg);
		responseVo.put("success", false);	
		return responseVo;
	}
	
	public static InStockInfoResponseVo pledgeOk() {
		InStockInfoResponseVo responseVo = new InStockInfoResponseVo();
		responseVo.put("errorCode", SUCCESS);
		responseVo.put("errorMsg", "操作成功");
		responseVo.put("success", true);		
		return responseVo;
	}

	public static InStockInfoResponseVo success() {
		InStockInfoResponseVo responseVo = new InStockInfoResponseVo();
		responseVo.put("errorCode", SUCCESS);
		responseVo.put("errorMsg", "操作成功");
		responseVo.put("success", true);		
		return responseVo;
	}
	
	
}
