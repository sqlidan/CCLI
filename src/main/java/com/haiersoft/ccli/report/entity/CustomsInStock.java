package com.haiersoft.ccli.report.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Connor.M
 * @ClassName: Stock
 * @Description: 库存信息表
 * @date 2016年3月9日 下午3:44:43
 */
public class CustomsInStock implements Serializable{

    private static final long serialVersionUID = 2799111007612445959L;
	
	private String itemNum;//项号
	private String CurrencyType;//币种
	private String cargoName;//品名
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date declareTime;//申报日期
	private String cdNum;//报关单号
	private Double netWeight;//净重（KG）
	private Double totalPrices;//货值（美元）
	private String verifinum;//核销单号
	private String tradetype;//贸易类型
	private String yearmonth;//年月
	private Date searchStrTime;//开始年月
	private Date searchEndTime;//结束年月
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	public String getCurrencyType() {
		return CurrencyType;
	}
	public void setCurrencyType(String currencyType) {
		CurrencyType = currencyType;
	}
	public String getCargoName() {
		return cargoName;
	}
	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	public Date getDeclareTime() {
		return declareTime;
	}
	public void setDeclareTime(Date declareTime) {
		this.declareTime = declareTime;
	}
	public String getCdNum() {
		return cdNum;
	}
	public void setCdNum(String cdNum) {
		this.cdNum = cdNum;
	}
	public Double getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}
	public Double getTotalPrices() {
		return totalPrices;
	}
	public void setTotalPrices(Double totalPrices) {
		this.totalPrices = totalPrices;
	}
	public String getVerifinum() {
		return verifinum;
	}
	public void setVerifinum(String verifinum) {
		this.verifinum = verifinum;
	}
	public String getTradetype() {
		return tradetype;
	}
	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}
	
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	public Date getSearchStrTime() {
		return searchStrTime;
	}
	public void setSearchStrTime(Date searchStrTime) {
		this.searchStrTime = searchStrTime;
	}
	public Date getSearchEndTime() {
		return searchEndTime;
	}
	public void setSearchEndTime(Date searchEndTime) {
		this.searchEndTime = searchEndTime;
	}

	
}
