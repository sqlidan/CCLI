package com.haiersoft.ccli.api.entity;

public class ExchangeRequestVo {

	String accountId; // 准入渠道号
	String id;// 库存明细主键,只有静态质押
	Integer pledgeNumber;// 质押监管数量
	Double pledgeWeight;// 质押监管重量
	String order;// 指令 质押监管/ZYJG
	
	String pledgeId;// 静态为原押品主键
	Integer relieveNumber;// 解压后仍被质押的数量
	Double relieveWeight;// 解压后仍被质押的重量
	String relieveOrder;// 解除质押监管/JCZYJG

	String sourceTrendId;// 解除质押唯一标识
	String relatedTrendId;// 解除质押时，原质押唯一标识
	String trendId;// 新质押唯一标识
	String signature;// 验证签名
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getPledgeNumber() {
		return pledgeNumber;
	}
	public void setPledgeNumber(Integer pledgeNumber) {
		this.pledgeNumber = pledgeNumber;
	}
	public Double getPledgeWeight() {
		return pledgeWeight;
	}
	public void setPledgeWeight(Double pledgeWeight) {
		this.pledgeWeight = pledgeWeight;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getPledgeId() {
		return pledgeId;
	}
	public void setPledgeId(String pledgeId) {
		this.pledgeId = pledgeId;
	}
	public Integer getRelieveNumber() {
		return relieveNumber;
	}
	public void setRelieveNumber(Integer relieveNumber) {
		this.relieveNumber = relieveNumber;
	}
	public Double getRelieveWeight() {
		return relieveWeight;
	}
	public void setRelieveWeight(Double relieveWeight) {
		this.relieveWeight = relieveWeight;
	}
	public String getRelieveOrder() {
		return relieveOrder;
	}
	public void setRelieveOrder(String relieveOrder) {
		this.relieveOrder = relieveOrder;
	}
	public String getSourceTrendId() {
		return sourceTrendId;
	}
	public void setSourceTrendId(String sourceTrendId) {
		this.sourceTrendId = sourceTrendId;
	}
	public String getRelatedTrendId() {
		return relatedTrendId;
	}
	public void setRelatedTrendId(String relatedTrendId) {
		this.relatedTrendId = relatedTrendId;
	}
	public String getTrendId() {
		return trendId;
	}
	public void setTrendId(String trendId) {
		this.trendId = trendId;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}


}
