package com.haiersoft.ccli.cost.entity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

/**
 * 业务付款单明细EXCEL
 */
@ExcelTarget("BisPayInfoExcel") 
public class BisPayInfoExcel implements java.io.Serializable {
	 
	private static final long serialVersionUID = 4040788502963084202L;
	
	@Excel(name = "客户名称")
	private String clientName;  //客户名称
	@Excel(name = "提单号")
	private String billNum;  //提单号
	@Excel(name = "费目名称")
	private String feeName;  //费目名称
	@Excel(name = "单价")
	private Double unitPrice;  //单价
	@Excel(name = "数量")
	private Double num;  		//数量
	@Excel(name = "金额")
	private Double totelPrice;  //金额
	@Excel(name = "税率")
	private Double taxRate;  //税率
	@Excel(name = "备注")
	private String remark;  //税率
	@Excel(name = "是否垫付")
	private String helpPay;  //是否垫代垫付 0:否 1：是
	@Excel(name = "是否冲账")
	private String ifReverse;  //是否冲账 0:否 1：是
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getTotelPrice() {
		return totelPrice;
	}
	public void setTotelPrice(Double totelPrice) {
		this.totelPrice = totelPrice;
	}
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	public String getHelpPay() {
		return helpPay;
	}
	public void setHelpPay(String helpPay) {
		this.helpPay = helpPay;
	}
	public String getIfReverse() {
		return ifReverse;
	}
	public void setIfReverse(String ifReverse) {
		this.ifReverse = ifReverse;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	
	 
	
	
}