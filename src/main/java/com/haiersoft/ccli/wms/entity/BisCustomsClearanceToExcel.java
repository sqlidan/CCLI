package com.haiersoft.ccli.wms.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 */
public class BisCustomsClearanceToExcel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5388208904327056983L;



	@Excel(name = "客户名称",width =20)
	private String CLIENT_NAME; //

	@Excel(name = "服务项目",replace = { "报进_0","报出_1"," _null"})
	private String SERVICE_PROJECT; // 服务项目



	@JSONField(name = "DECLARE_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Excel(name = "申报日期",format = "yyyy-MM-dd",width =15 )
	private Date DECLARE_TIME; // 申报日期


	@Excel(name = "贸易方式", replace = { "保税_0","来料加工 _1","进料加工 _2","一般贸易 _3"," _null" })
	private String MODE_TRADE; // 贸易方式


	@Excel(name = "提单号")
	private String BILL_NUM; // 提单号

	@Excel(name = "报关单号")
	private String CUSTOMS_DECLARATION_NUMBER; // 报关单号

	@Excel(name = "原产国")
	private String CONTRY_ORAGIN; // 原产国


	@Excel(name = "启运国")
	private String CONTRY_DEPARTURE; // 启运国

	@Excel(name = "收货人")
	private String CONSIGNEE; // 收货人

	@Excel(name = "发货人 ")
	private String CONSIGNOR; // 发货人

	@Excel(name = "货权方名称",width =20)
	private String CARGO_CLIENT_NAME; //货权方名称

	@Excel(name = "消费者使用单位",width =15)
	private String USE_UNIT; // 消费者使用单位


	@Excel(name = "入境口岸")
	private String PORT_ENTRY; //入境口岸

	@Excel(name = "存放地点")
	private String STORAGE_PLACE; // 存放地点



	//@Excel(name = "客户ID")
	//private String CLIENT_ID; // 客户ID

	
	//@Excel(name = "货权方ID")
	//private String CARGO_CLIENT_ID; //货权方ID
	

	


	@Excel(name = "是否审核", replace = { "未审核_0","已提交 _1","已驳回 _2","已审核 _3"," _null" })
	private String AUDITING_STATE;// 审核状态 0：未审核 1：已审核

	@Excel(name = "备注 ")
	private String COMMENTS; //备注

	//@Excel(name = "操作人")
	private String OPERATOR;//操作人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	//@Excel(name = "创建时间")
	private Date OPERATE_TIME;//操作时间

	@Excel(name = "数量",type=10)
	private BigDecimal NUM;//操作时间

	@Excel(name = "净重",type=10)
	private BigDecimal NET_WEIGHT;//净重

	@Excel(name = "毛重",type=10)
	private BigDecimal GROSS_WEIGHT;//毛重

	@Excel(name = "金额",type=10)
	private BigDecimal MONEY;//金额

	@Excel(name = "币制", replace = { "人民币_0","美元 _1","日元 _2","欧元 _3","英镑 _4"," _null" })
	private String CURRENCY_VALUE;//币制


	@Excel(name = "木托编号")
	private String WOODEN_NO;//木托编号


	//@Excel(name = "审核人")
	private String EXAMINE_PERSON;//审核人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	//@Excel(name = "审核时间")
	private Date EXAMINE_TIME;//审核时间

	public Date getDECLARE_TIME() {
		return DECLARE_TIME;
	}

	public void setDECLARE_TIME(Date DECLARE_TIME) {
		this.DECLARE_TIME = DECLARE_TIME;
	}

	public String getBILL_NUM() {
		return BILL_NUM;
	}

	public void setBILL_NUM(String BILL_NUM) {
		this.BILL_NUM = BILL_NUM;
	}

	public String getSERVICE_PROJECT() {
		return SERVICE_PROJECT;
	}

	public void setSERVICE_PROJECT(String SERVICE_PROJECT) {
		this.SERVICE_PROJECT = SERVICE_PROJECT;
	}

	public String getCONSIGNEE() {
		return CONSIGNEE;
	}

	public void setCONSIGNEE(String CONSIGNEE) {
		this.CONSIGNEE = CONSIGNEE;
	}

	public String getCONSIGNOR() {
		return CONSIGNOR;
	}

	public void setCONSIGNOR(String CONSIGNOR) {
		this.CONSIGNOR = CONSIGNOR;
	}

	public String getUSE_UNIT() {
		return USE_UNIT;
	}

	public void setUSE_UNIT(String USE_UNIT) {
		this.USE_UNIT = USE_UNIT;
	}

	public String getMODE_TRADE() {
		return MODE_TRADE;
	}

	public void setMODE_TRADE(String MODE_TRADE) {
		this.MODE_TRADE = MODE_TRADE;
	}

	public String getSTORAGE_PLACE() {
		return STORAGE_PLACE;
	}

	public void setSTORAGE_PLACE(String STORAGE_PLACE) {
		this.STORAGE_PLACE = STORAGE_PLACE;
	}

	public String getPORT_ENTRY() {
		return PORT_ENTRY;
	}

	public void setPORT_ENTRY(String PORT_ENTRY) {
		this.PORT_ENTRY = PORT_ENTRY;
	}

	public String getCONTRY_DEPARTURE() {
		return CONTRY_DEPARTURE;
	}

	public void setCONTRY_DEPARTURE(String CONTRY_DEPARTURE) {
		this.CONTRY_DEPARTURE = CONTRY_DEPARTURE;
	}

	public String getCUSTOMS_DECLARATION_NUMBER() {
		return CUSTOMS_DECLARATION_NUMBER;
	}

	public void setCUSTOMS_DECLARATION_NUMBER(String CUSTOMS_DECLARATION_NUMBER) {
		this.CUSTOMS_DECLARATION_NUMBER = CUSTOMS_DECLARATION_NUMBER;
	}

	public String getCONTRY_ORAGIN() {
		return CONTRY_ORAGIN;
	}

	public void setCONTRY_ORAGIN(String CONTRY_ORAGIN) {
		this.CONTRY_ORAGIN = CONTRY_ORAGIN;
	}

//	public String getCLIENT_ID() {
//		return CLIENT_ID;
//	}
//
//	public void setCLIENT_ID(String CLIENT_ID) {
//		this.CLIENT_ID = CLIENT_ID;
//	}

	public String getCLIENT_NAME() {
		return CLIENT_NAME;
	}

	public void setCLIENT_NAME(String CLIENT_NAME) {
		this.CLIENT_NAME = CLIENT_NAME;
	}

//	public String getCARGO_CLIENT_ID() {
//		return CARGO_CLIENT_ID;
//	}
//
//	public void setCARGO_CLIENT_ID(String CARGO_CLIENT_ID) {
//		this.CARGO_CLIENT_ID = CARGO_CLIENT_ID;
//	}

	public String getCARGO_CLIENT_NAME() {
		return CARGO_CLIENT_NAME;
	}

	public void setCARGO_CLIENT_NAME(String CARGO_CLIENT_NAME) {
		this.CARGO_CLIENT_NAME = CARGO_CLIENT_NAME;
	}

	public String getCOMMENTS() {
		return COMMENTS;
	}

	public void setCOMMENTS(String COMMENTS) {
		this.COMMENTS = COMMENTS;
	}

	public String getAUDITING_STATE() {
		return AUDITING_STATE;
	}

	public void setAUDITING_STATE(String AUDITING_STATE) {
		this.AUDITING_STATE = AUDITING_STATE;
	}

	public String getOPERATOR() {
		return OPERATOR;
	}

	public void setOPERATOR(String OPERATOR) {
		this.OPERATOR = OPERATOR;
	}

	public Date getOPERATE_TIME() {
		return OPERATE_TIME;
	}

	public void setOPERATE_TIME(Date OPERATE_TIME) {
		this.OPERATE_TIME = OPERATE_TIME;
	}

	public String getEXAMINE_PERSON() {
		return EXAMINE_PERSON;
	}

	public void setEXAMINE_PERSON(String EXAMINE_PERSON) {
		this.EXAMINE_PERSON = EXAMINE_PERSON;
	}

	public Date getEXAMINE_TIME() {
		return EXAMINE_TIME;
	}

	public void setEXAMINE_TIME(Date EXAMINE_TIME) {
		this.EXAMINE_TIME = EXAMINE_TIME;
	}

	public BigDecimal getNUM() {
		return NUM;
	}

	public void setNUM(BigDecimal NUM) {
		this.NUM = NUM;
	}

	public BigDecimal getNET_WEIGHT() {
		return NET_WEIGHT;
	}

	public void setNET_WEIGHT(BigDecimal NET_WEIGHT) {
		this.NET_WEIGHT = NET_WEIGHT;
	}

	public BigDecimal getGROSS_WEIGHT() {
		return GROSS_WEIGHT;
	}

	public void setGROSS_WEIGHT(BigDecimal GROSS_WEIGHT) {
		this.GROSS_WEIGHT = GROSS_WEIGHT;
	}

	public BigDecimal getMONEY() {
		return MONEY;
	}

	public void setMONEY(BigDecimal MONEY) {
		this.MONEY = MONEY;
	}

	public String getCURRENCY_VALUE() {
		return CURRENCY_VALUE;
	}

	public void setCURRENCY_VALUE(String CURRENCY_VALUE) {
		this.CURRENCY_VALUE = CURRENCY_VALUE;
	}

	public String getWOODEN_NO() {
		return WOODEN_NO;
	}

	public void setWOODEN_NO(String WOODEN_NO) {
		this.WOODEN_NO = WOODEN_NO;
	}
}