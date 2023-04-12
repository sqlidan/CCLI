package com.haiersoft.ccli.report.entity;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Mazy
 * @ClassName: OutStock
 * @Description: 海关出库报表
 * @date 2016年6月24日 上午9:44:43
 */

public class OutStock implements Serializable{

    private static final long serialVersionUID = 2799111007612445959L;
	
	// Fields
	private String itemNum;//项号
	private String cargoName;//品名	
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date declareTime;//报关时间	
	private String cdNum;//报关单号	
	private Double netWeight;//净重	
	private Double totalPrices;//总价
	private Double duty;//关税
	private String zzs;//增值税
	private String consignee;//流向，收货单位
	private String tradeType;//贸易方式
	private String year;//报关年份
	private String month;//报关月份
	private String CurrencyType;//币种
	private String yearmonth;//年月
	
	private Date searchStrTime;//开始年月
	private Date searchEndTime;//结束年月
	
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
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
	public Double getDuty() {
		return duty;
	}
	public void setDuty(Double duty) {
		this.duty = duty;
	}
	public String getZzs() {
		return zzs;
	}
	public void setZzs(String zzs) {
		this.zzs = zzs;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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
	public String getCurrencyType() {
		return CurrencyType;
	}
	public void setCurrencyType(String currencyType) {
		CurrencyType = currencyType;
	}
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}


	

}
