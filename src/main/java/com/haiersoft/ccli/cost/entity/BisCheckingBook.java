package com.haiersoft.ccli.cost.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 对账单主表
 * @author LZG
 *
 */
@Entity
@Table(name = "BIS_CHEKING_BOOK")
@DynamicUpdate
@DynamicInsert
public class BisCheckingBook implements java.io.Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 4996378226790891884L;
	@Id
	@Column(name = "CODENUM", unique = true, nullable = false)
	private String codeNum; //编号
	@Column(name = "ISTRUE")
	private String isTrue; //是否确定
	@Column(name = "CUSTOME")
	private String custom; //客户
	@Column(name = "CUSTOMEID")
	private String customID; //客户编号
	@Column(name = "YEARMONTH")
	private String yearMonth;  //账单年月
	@Column(name = "REMARK")
	private String remark; //备注
	@Column(name = "CRUSER")
	private String operator;//操作人员
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CRTIME")
	private Date operateTime;//操作时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPTIME")
	private Date updateTime;//修改时间
	@Column(name = "UPUSER")
	private String updateOperator;//操作人员
	@Transient
	private Integer nType;//类型
	@Transient
	private String type;//导出是否带箱号跟lot号的标志
	//对接增值税发票接口新加字段
	@Column(name = "SRC_CUST_CODE")
    private String srcCustCode;//增值税对应客户代码
	@Column(name = "SRC_CUST_NAME")
	private String srcCustName;//增值税对应客户名称
	@Column(name = "RESULT")
	private String result;//返回结果状态
	@Column(name = "JSFS")
	private String jsfs;//结算方式Y=月结，X=现结
	@Column(name = "INVOICECODE")
	private String invoiceCode;//发票代码
	@Column(name = "INVOICENUM")
	private String invoiceNum;//发票编号
	@Column(name = "MIDGROUPSTATIC")
	private String midGroupStatic;//上传状态
	@Column(name = "STATEMENT_NO")
	private String statementNo;//结算单号

	/////////////////////////////////////////////////////////////////////////////////
	public String getStatementNo(){
		return statementNo;
	}
	public void setStatementNo(String statementNo){
		this.statementNo = statementNo;
	}
	public String getMidGroupStatic(){
		return midGroupStatic;
	}
	public void setMidGroupStatic(String midGroupStatic){
		this.midGroupStatic = midGroupStatic;
	}
	public String getCodeNum() {
		return codeNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public void setCodeNum(String codeNum) {
		this.codeNum = codeNum;
	}
	public String getIsTrue() {
		return isTrue;
	}
	public void setIsTrue(String isTrue) {
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
	public Integer getnType() {
		return nType;
	}
	public void setnType(Integer nType) {
		this.nType = nType;
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

}
