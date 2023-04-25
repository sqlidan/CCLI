package com.haiersoft.ccli.supervision.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.util.Date;


/**
 * SKU
 *  @author MyEclipse Persistence Tools
 */
@ExcelTarget("BaseAccountExcel")
public class BaseAccountExcel implements Serializable {
	
    private static final long serialVersionUID = 8270056740403940287L;

	@Excel(name = "提单号")
	private String billNo;

	@Excel(name = "箱号")
	private String containerNo;
	@Excel(name = "货类")
	private String productType;
	@Excel(name = "货物名称(品名)")
	private String productName;
	//原申请数量
	@Excel(name = "原申报数量")
	private String rSumnum;
	//原申请重量
	@Excel(name = "原申报重量")
	private String rSumweight;
	//剩余件数
	@Excel(name = "剩余件数")
	private String surplusNum;
	//剩余重量
	@Excel(name = "剩余重量")
	private String surplusWeight;

	private String areaNum;


	private Date appointDate;

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getrSumnum() {
		return rSumnum;
	}

	public void setrSumnum(String rSumnum) {
		this.rSumnum = rSumnum;
	}

	public String getrSumweight() {
		return rSumweight;
	}

	public void setrSumweight(String rSumweight) {
		this.rSumweight = rSumweight;
	}

	public String getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(String surplusNum) {
		this.surplusNum = surplusNum;
	}

	public String getSurplusWeight() {
		return surplusWeight;
	}

	public void setSurplusWeight(String surplusWeight) {
		this.surplusWeight = surplusWeight;
	}

	public String getAreaNum() {
		return areaNum;
	}

	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}

	public Date getAppointDate() {
		return appointDate;
	}

	public void setAppointDate(Date appointDate) {
		this.appointDate = appointDate;
	}
}