package com.haiersoft.ccli.api.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 在库信息返回
 * @author 
 *
 */
//@Entity
public class InStockInfo {
	
	private String id;
	private String tdh; //提单号
	private String xh; //集装箱号
	private String item; //小类代码
	private String itemName; //货物描述(品名)
	private String location; //区位
	private String itemStatus; // 货物状态
	//@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private String instorageDate; //入库日期
	private Integer qty; //件数
	private Double netWeight; //单净重
	private Double grossWeight; //单毛重
	private Double totalNetWeight; //总净重
	private Double totalGrossWeight; //总毛重
	private String pledge; //质押状态(全部质押/部分质押/未质押)
	private Double pledgeWeight; //已质押重量(净重)
	private Integer pledgeAmount; //已质押数量
	

//	private Integer total; //总条数
//	private Integer pageNum; //查询页
//	private Integer pageSize; //查询每页数量
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTdh() {
		return tdh;
	}
	public void setTdh(String tdh) {
		this.tdh = tdh;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}
	public String getInstorageDate() {
		return instorageDate;
	}
	public void setInstorageDate(String instorageDate) {
		this.instorageDate = instorageDate;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Double getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}
	public Double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public Double getTotalNetWeight() {
		return totalNetWeight;
	}
	public void setTotalNetWeight(Double totalNetWeight) {
		this.totalNetWeight = totalNetWeight;
	}
	public Double getTotalGrossWeight() {
		return totalGrossWeight;
	}
	public void setTotalGrossWeight(Double totalGrossWeight) {
		this.totalGrossWeight = totalGrossWeight;
	}
	public String getPledge() {
		return pledge;
	}
	public void setPledge(String pledge) {
		this.pledge = pledge;
	}
	public Double getPledgeWeight() {
		return pledgeWeight;
	}
	public void setPledgeWeight(Double pledgeWeight) {
		this.pledgeWeight = pledgeWeight;
	}
	public Integer getPledgeAmount() {
		return pledgeAmount;
	}
	public void setPledgeAmount(Integer pledgeAmount) {
		this.pledgeAmount = pledgeAmount;
	}
	
	

	@Override
	public String toString() {
		return "InStockInfo [id=" + id + ", tdh=" + tdh + ", xh=" + xh + ", item=" + item + ", itemName=" + itemName
				+ ", location=" + location + ", itemStatus=" + itemStatus + ", instorageDate=" + instorageDate
				+ ", qty=" + qty + ", netWeight=" + netWeight + ", grossWeight=" + grossWeight + ", totalNetWeight="
				+ totalNetWeight + ", totalGrossWeight=" + totalGrossWeight + ", pledge=" + pledge + ", pledgeWeight="
				+ pledgeWeight + ", pledgeAmount=" + pledgeAmount + "]";
	}

	
}
