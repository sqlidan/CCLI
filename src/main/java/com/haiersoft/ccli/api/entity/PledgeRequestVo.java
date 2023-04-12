package com.haiersoft.ccli.api.entity;

public class PledgeRequestVo {

	String accountId;// 准入渠道号
	String id;// 静态质押时为库存明细主键，动态质押时为对方传来的业务主键
	Integer pledgeNumber;// 质押监管数量
	Double pledgeWeight;// 质押监管重量
	String order;// 指令
	String itemClass;// 货种名称
	String customerNumber;// 冷链客户号
	String trendId;// 质押/解除质押唯一标识
	String relatedTrendId;// 原质押唯一标识
	String signature;// 验证签名

	public String getTrendId() {
		return trendId;
	}

	public void setTrendId(String trendId) {
		this.trendId = trendId;
	}

	public String getRelatedTrendId() {
		return relatedTrendId;
	}

	public void setRelatedTrendId(String relatedTrendId) {
		this.relatedTrendId = relatedTrendId;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getItemClass() {
		return itemClass;
	}

	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
