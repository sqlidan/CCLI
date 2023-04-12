package com.haiersoft.ccli.wms.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.jeecgframework.poi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * BisOutForecast entity. @author MyEclipse Persistence Tools
 * 出库预报单
 */
@Entity
@Table(name = "BIS_OUT_FORECAST")
public class BisOutForecast implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 29800484822355909L;

	@Id
	@Column(name = "FOR_ID")
	@Excel(name = "预报单ID")
	private String forId;//预报单ID
	
	@Column(name = "CLIENT_ID")
	private String clientId; //客户ID
	
	@Column(name = "CLIENT_NAME")
	@Excel(name = "客户名称")
	private String clientName; //客户名称
	
	@Column(name = "DECLARATION_UNIT_ID")
	private String declarationUnitId; //报关公司ID
	
	@Column(name = "DECLARATION_UNIT")
	@Excel(name = "报关公司")
	private String declarationUnit; //报关公司
	
	@Column(name = "CIQ_DECLARATION_UNIT_ID")
	private String ciqDeclarationUnitId;  //报检公司ID
	
	@Column(name = "CIQ_DECLARATION_UNIT")
	@Excel(name = "报检公司")
	private String ciqDeclarationUnit;  //报检公司
	
	@Column(name = "TRADE_MODE")
	@Excel(name = "贸易方式")
	private String tradeMode;  //贸易方式
	
	@Column(name = "CUSTOMER_SERVICE")
	private String customerService;  //客服
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	@Excel(name = "下单日期")
	private Date createTime;  //下单日期
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;  //修改日期
	
	@Column(name = "BILL_NUM")
	@Excel(name = "提单号")
	private String billNum;  //提单号
	
	@Column(name = "CTN_CONT")
	@Excel(name = "箱量")
	private Integer ctnCont;  //箱量
	
	@Column(name = "CD_SIGN")
	@Excel(name = "是否报关", replace = {"未通关_0", "已通关_1"})
	private Integer cdSign;    //(1已通关，0未通关)报关标志
	
	@Column(name = "CIQ_SIGN")
	@Excel(name = "是否报检", replace = {"未报检_0", "已报检_1"})
	private Integer ciqSign;   //(1已报检，0未报检)报检标志
	
	@Column(name = "REMARK")
	@Excel(name = "备注")
	private String remark;  //备注

	@Column(name = "CD_NUM")
	@Excel(name = "报关单号")
	private String cdNum;  //报关单号
	
	@Column(name = "CIQ_NUM")
	@Excel(name = "报检单号")
	private String ciqNum;  //报检单号
	
	public String getForId() {
		return forId;
	}

	public void setForId(String forId) {
		this.forId = forId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getDeclarationUnit() {
		return declarationUnit;
	}

	public void setDeclarationUnit(String declarationUnit) {
		this.declarationUnit = declarationUnit;
	}

	public String getCiqDeclarationUnit() {
		return ciqDeclarationUnit;
	}

	public void setCiqDeclarationUnit(String ciqDeclarationUnit) {
		this.ciqDeclarationUnit = ciqDeclarationUnit;
	}

	public String getTradeMode() {
		return tradeMode;
	}

	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public Integer getCtnCont() {
		return ctnCont;
	}

	public void setCtnCont(Integer ctnCont) {
		this.ctnCont = ctnCont;
	}

	public Integer getCdSign() {
		return cdSign;
	}

	public void setCdSign(Integer cdSign) {
		this.cdSign = cdSign;
	}

	public Integer getCiqSign() {
		return ciqSign;
	}

	public void setCiqSign(Integer ciqSign) {
		this.ciqSign = ciqSign;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDeclarationUnitId() {
		return declarationUnitId;
	}

	public void setDeclarationUnitId(String declarationUnitId) {
		this.declarationUnitId = declarationUnitId;
	}

	public String getCiqDeclarationUnitId() {
		return ciqDeclarationUnitId;
	}

	public void setCiqDeclarationUnitId(String ciqDeclarationUnitId) {
		this.ciqDeclarationUnitId = ciqDeclarationUnitId;
	}

	public String getCustomerService() {
		return customerService;
	}

	public void setCustomerService(String customerService) {
		this.customerService = customerService;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCdNum() {
		return cdNum;
	}

	public void setCdNum(String cdNum) {
		this.cdNum = cdNum;
	}

	public String getCiqNum() {
		return ciqNum;
	}

	public void setCiqNum(String ciqNum) {
		this.ciqNum = ciqNum;
	}

	
}