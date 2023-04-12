package com.haiersoft.ccli.wms.entity;
/**
 * 拆托模型对象
 * @author LZG
 *
 */
public class SplitStockModel {
	private String ordId;//订单号
	private String ids;//库存明细id集合
	private Integer[] tId;//代拆分库存明细id
	private Integer[] spNum;//拆分数量
	private String[]  newTCode;//新托盘号
	public String getOrdId() {
		return ordId;
	}
	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}
	public Integer[] gettId() {
		return tId;
	}
	public void settId(Integer[] tId) {
		this.tId = tId;
	}
	public Integer[] getSpNum() {
		return spNum;
	}
	public void setSpNum(Integer[] spNum) {
		this.spNum = spNum;
	}
	public String[] getNewTCode() {
		return newTCode;
	}
	public void setNewTCode(String[] newTCode) {
		this.newTCode = newTCode;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}
