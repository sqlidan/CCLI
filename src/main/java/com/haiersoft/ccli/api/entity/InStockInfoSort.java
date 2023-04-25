package com.haiersoft.ccli.api.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 在库信息返回
 * @author 
 *
 */
//@Entity
public class InStockInfoSort {
	
	private String sortField; //排序字段
	private String sortOrder; //排序顺序（降序 升序）
	private Integer pageNum; //查询页
	private Integer pageSize; //查询每页数量
	private Integer total; //总条数
	
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}

	
}
