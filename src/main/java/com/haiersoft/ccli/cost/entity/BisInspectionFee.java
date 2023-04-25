package com.haiersoft.ccli.cost.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 查验主表
 * @author PYL
 *
 */
@Entity
@Table(name = "BIS_INSPECTION_FEE")
public class BisInspectionFee implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6116016021835594928L;

	@Id
	@Excel(name = "查验单号")
	@Column(name = "FEE_ID", unique = true, nullable = false)
	private String feeId; //主键，查验ID
	@Excel(name = "提单号")
	@Column(name = "BILL_NUM")
	private String billNum; //提单号
	@Excel(name = "箱号")
	@Column(name = "CTN_NUM")
	private String ctnNum; //箱号（多个用逗号分隔）
	@Excel(name = "箱型")
	@Column(name = "CTN_TYPE")
	private String ctnType; //箱型
	@Excel(name = "箱量")
	@Column(name = "CTN_AMOUNT")
	private Integer ctnAmount; //箱量
	@Excel(name = "查验日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CHECK_DATE")
	private Date checkDate;  //查验日期
	@Excel(name = "查验品类",replace = {"水产_1", "冻肉_2", "水果_3","其他_4"})
	@Column(name = "CHECK_TYPE")
	private String checkType; //查验品类  1:水产 2：冻肉  3:水果 4:其他
	@Excel(name = "零星客户",replace = {"是_1", "否_0"})
	@Column(name = "IF_LX")
	private String ifLx; //是否零星客户 0否1是
	
	@Column(name = "IF_JS")
	private String ifJs;//是否已结算0否1是
	@Excel(name = "结算方式",replace = {"月结 _1", "现结_2"})
	@Column(name = "BALANCE_WAY")
	private String balanceWay;//结算方式 1:月结 2:现结
	@Excel(name = "费用金额")
	@Column(name = "COST_AMOUNT")
	private Double costAmount;//费用金额
	
	@Column(name = "INFO_AMOUNT")
	private Double infoAmount;//子单金额合计（现在不用了）
	@Excel(name = "是否审核",replace = {"已审核 _1", "未审核_2"})
	@Column(name = "IF_PASS")
	private Integer ifPass;//审核状态 0：未审核 1：已审核
	
	@Column(name = "CLIENT_ID")
	private String clientId; //收款客户ID
	@Excel(name = "客户")
	@Column(name = "CLIENT_NAME")
	private String clientName;//收款客户
	
	@Column(name = "IF_NOW")
	private Integer ifNow;//是否现结 0：否 1：是
	@Excel(name = "操作人员")
	@Column(name = "OPERATE_PERSON")
	private String operatePerson;//操作人员
	@Excel(name = "操作日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "OPERATE_TIME")
	private Date operateTime;  //操作时间
	@Excel(name = "备注")
	@Column(name = "REMARK")
	private String remark;//备注
	
	@Column(name = "INSPECTION_NUM")
	private String inspectionNum;//报检号

	@Transient
	private Date startTime;
	@Transient
	private Date endTime;
	
	
	
	public String getInspectionNum() {
		return inspectionNum;
	}

	public void setInspectionNum(String inspectionNum) {
		this.inspectionNum = inspectionNum;
	}

	public String getIfJs() {
		return ifJs;
	}

	public void setIfJs(String ifJs) {
		this.ifJs = ifJs;
	}

	public String getIfLx() {
		return ifLx;
	}

	public void setIfLx(String ifLx) {
		this.ifLx = ifLx;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getCtnNum() {
		return ctnNum;
	}

	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}

	public String getCtnType() {
		return ctnType;
	}

	public void setCtnType(String ctnType) {
		this.ctnType = ctnType;
	}

	public Integer getCtnAmount() {
		return ctnAmount;
	}

	public void setCtnAmount(Integer ctnAmount) {
		this.ctnAmount = ctnAmount;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getBalanceWay() {
		return balanceWay;
	}

	public void setBalanceWay(String balanceWay) {
		this.balanceWay = balanceWay;
	}

	public Double getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(Double costAmount) {
		this.costAmount = costAmount;
	}

	public Double getInfoAmount() {
		return infoAmount;
	}

	public void setInfoAmount(Double infoAmount) {
		this.infoAmount = infoAmount;
	}

	public Integer getIfPass() {
		return ifPass;
	}

	public void setIfPass(Integer ifPass) {
		this.ifPass = ifPass;
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

	public Integer getIfNow() {
		return ifNow;
	}

	public void setIfNow(Integer ifNow) {
		this.ifNow = ifNow;
	}

	public String getOperatePerson() {
		return operatePerson;
	}

	public void setOperatePerson(String operatePerson) {
		this.operatePerson = operatePerson;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}
