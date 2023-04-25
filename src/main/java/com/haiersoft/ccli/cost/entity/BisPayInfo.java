package com.haiersoft.ccli.cost.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * BisPayInfo entity. @author MyEclipse Persistence Tools
 * 业务付款单明细
 */
@Entity
@Table(name = "BIS_PAY_INFO")
public class BisPayInfo implements java.io.Serializable {
	 
	private static final long serialVersionUID = 4040788502963084204L;
	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAY_MANAGER")
	@SequenceGenerator(name="SEQ_PAY_MANAGER", sequenceName="SEQ_PAY_MANAGER", allocationSize = 1)  
    @Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "PAY_ID")
	private String payId;  //主表ID
	
	@Column(name = "CHECK_SIGN")
	private Integer checkSign;  //审核标志（0：未审核 1：已审核）
	
	@Column(name = "SELL_MAN")
	private String sellMan;  //销售人员
	
	@Column(name = "CLIENT_ID")
	private String clientId;  //客户ID
	
	@Column(name = "CLIENT_NAME")
	private String clientName;  //客户名称
	
	@Column(name = "BILL_NUM")
	@Excel(name = "提单号")
	private String billNum;  //提单号
	
	@Column(name = "FEE_Id")
	private String feeId;  //费目ID
	
	@Column(name = "FEE_CODE")
	private String feeCode;  //费目代码
	
	@Column(name = "FEE_NAME")
	@Excel(name = "费目")
	private String feeName;  //费目名称
	
	@Column(name = "UNIT_PRICE")
	@Excel(name = "单价")
	private Double unitPrice;  //单价
	
	@Column(name = "NUM")
	private Double num;  //数量
	
	@Column(name = "TOTEL_PRICE")
	@Excel(name = "总价")
	private Double totelPrice;  //金额
	
	@Column(name = "TAX_RATE")
	@Excel(name = "税率")
	private Double taxRate;  //税率
    
	@Column(name = "CTN_AMOUNT")
	private Integer ctnAmount;  //箱量
	
	@Column(name = "HELP_PAY")
	@Excel(name = "是否垫付", replace = {"否_0", "是_1"})
	private Integer helpPay;  //是否垫代垫付 0:否 1：是
	
	@Column(name = "SHARE_STATE")
	private String shareState;  //分配状态 1:分配成功 2：分配失敗 3:分配失败（费用已完成）
	
	@Column(name = "REMARK")
	private String remark;  //备注

	@Column(name = "STANDING_NUM")
	private Integer standingNum;  //台账ID
	
	@Column(name = "IF_REVERSE")
	@Excel(name = "是否冲账", replace = {"否_0", "是_1"})
	private Integer ifReverse;  //是否冲账 0:否 1：是
	
	@Transient
	private String saler;  //揽货人
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public Integer getCheckSign() {
		return checkSign;
	}

	public void setCheckSign(Integer checkSign) {
		this.checkSign = checkSign;
	}

	public String getSellMan() {
		return sellMan;
	}

	public void setSellMan(String sellMan) {
		this.sellMan = sellMan;
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

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
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

	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
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

	public Integer getCtnAmount() {
		return ctnAmount;
	}

	public void setCtnAmount(Integer ctnAmount) {
		this.ctnAmount = ctnAmount;
	}

	public Integer getHelpPay() {
		return helpPay;
	}

	public void setHelpPay(Integer helpPay) {
		this.helpPay = helpPay;
	}

	public String getShareState() {
		return shareState;
	}

	public void setShareState(String shareState) {
		this.shareState = shareState;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public Integer getStandingNum() {
		return standingNum;
	}

	public void setStandingNum(Integer standingNum) {
		this.standingNum = standingNum;
	}

	public String getSaler() {
		return saler;
	}

	public void setSaler(String saler) {
		this.saler = saler;
	}

	public Integer getIfReverse() {
		return ifReverse;
	}

	public void setIfReverse(Integer ifReverse) {
		this.ifReverse = ifReverse;
	}
	
	
}