package com.haiersoft.ccli.cost.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "BIS_CHEKING_BOOK_AUTO")
public class BisCheckingBookAuto implements java.io.Serializable {

	private static final long serialVersionUID = -6104025419241423610L;

	@Id
	@Column(name = "CODENUM", unique = true, nullable = false)
	private String codeNum;
	@Column(name = "ISTRUE")
	private Integer isTrue;
	@Column(name = "CUSTOME")
	private String custom;
	@Column(name = "CUSTOMEID")
	private String customID;
	@Column(name = "YEARMONTH")
	private String yearMonth;
	@Column(name = "REMARK")
	private String remark;
	@Column(name = "CRUSER")
	private String operator;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CRTIME")
	private Date operateTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPTIME")
	private Date updateTime;
	@Column(name = "UPUSER")
	private String updateOperator;
	@Column(name = "SRC_CUST_CODE")
	private String srcCustCode;
	@Column(name = "SRC_CUST_NAME")
	private String srcCustName;
	@Column(name = "RESULT")
	private String result;
	@Column(name = "JSFS")
	private String jsfs;
	@Column(name = "INVOICECODE")
	private String invoiceCode;
	@Column(name = "INVOICENUM")
	private String invoiceNum;
	@Column(name = "MIDGROUPSTATIC")
	private String midGroupStatic;
	@Column(name = "STATEMENT_NO")
	private String statementNo;
	@Column(name = "AUDIT_STATE")
	private Integer auditState;
	@Lob
	@Column(name = "STANDING_NUM_LIST")
	private String standingNumList;

	public String getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(String codeNum) {
		this.codeNum = codeNum;
	}

	public Integer getIsTrue() {
		return isTrue;
	}

	public void setIsTrue(Integer isTrue) {
		this.isTrue = isTrue;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getCustomID() {
		return customID;
	}

	public void setCustomID(String customID) {
		this.customID = customID;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateOperator() {
		return updateOperator;
	}

	public void setUpdateOperator(String updateOperator) {
		this.updateOperator = updateOperator;
	}

	public String getSrcCustCode() {
		return srcCustCode;
	}

	public void setSrcCustCode(String srcCustCode) {
		this.srcCustCode = srcCustCode;
	}

	public String getSrcCustName() {
		return srcCustName;
	}

	public void setSrcCustName(String srcCustName) {
		this.srcCustName = srcCustName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getJsfs() {
		return jsfs;
	}

	public void setJsfs(String jsfs) {
		this.jsfs = jsfs;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public String getMidGroupStatic() {
		return midGroupStatic;
	}

	public void setMidGroupStatic(String midGroupStatic) {
		this.midGroupStatic = midGroupStatic;
	}

	public String getStatementNo() {
		return statementNo;
	}

	public void setStatementNo(String statementNo) {
		this.statementNo = statementNo;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public String getStandingNumList() {
		return standingNumList;
	}

	public void setStandingNumList(String standingNumList) {
		this.standingNumList = standingNumList;
	}
}
