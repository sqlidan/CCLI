package com.haiersoft.ccli.wms.entity.PreEntryInvtQuery;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

/**
 * 
 */
@Entity
@Table(name = "BIS_CUSTOMS_CLEARANCE_LIST")
@DynamicUpdate
@DynamicInsert
public class BisCustomsClearanceList implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5388208904327056983L;

	@Id
	@Column(name = "CD_NUM")
	@Excel(name = "业务单号")
	private String cdNum;// 业务单号

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "DECLARE_TIME")
	@Excel(name = "申报日期")
	private Date declareTime; // 申报日期

	@Column(name = "BILL_NUM")
	@Excel(name = "提单号")
	private String billNum; // 提单号

	@Column(name = "SERVICE_PROJECT")
	@Excel(name = "服务项目")
	private String serviceProject; // 服务项目

	@Column(name = "CONSIGNEE")
	@Excel(name = "收货人")
	private String consignee; // 收货人

	@Column(name = "CONSIGNOR")
	@Excel(name = "发货人 ")
	private String consignor; // 发货人

	@Column(name = "USE_UNIT")
	@Excel(name = "消费者使用单位")
	private String useUnit; // 消费者使用单位

	@Column(name = "MODE_TRADE")
	@Excel(name = "贸易方式")
	private String modeTrade; // 贸易方式

	@Column(name = "STORAGE_PLACE")
	@Excel(name = "存放地点")
	private String storagePlace; // 存放地点

	@Column(name = "PORT_ENTRY")
	@Excel(name = "入境口岸")
	private String portEntry; //入境口岸

	@Column(name = "CONTRY_DEPARTURE")
	@Excel(name = "启运国")
	private String contryDeparture; // 启运国

	@Column(name = "CUSTOMS_DECLARATION_NUMBER")
	@Excel(name = "报关单号")
	private String customsDeclarationNumber; // 报关单号

	@Column(name = "CONTRY_ORAGIN")
	@Excel(name = "原产国")
	private String contryOragin; // 原产国

	@Column(name = "CLIENT_ID")
	@Excel(name = "客户ID")
	private String clientId; // 客户ID
	
	@Column(name = "CLIENT_NAME")
	@Excel(name = "客户名称")
	private String clientName; // 
	
	@Column(name = "CARGO_CLIENT_ID")
	@Excel(name = "货权方ID")
	private String cargoClientId; //货权方ID 
	
	@Column(name = "CARGO_CLIENT_NAME")
	@Excel(name = "货权方名称 ")
	private String cargoClientName; //货权方名称
	
	@Column(name = "COMMENTS")
	@Excel(name = "备注 ")
	private String comments; //备注
	
	@Column(name = "AUDITING_STATE")
	@Excel(name = "是否审核", replace = { "未审核_0","已提交 _1" })
	private String auditingState;// 审核状态 0：保存中/未审核 1：已提交  2: 驳回  3:已审核
	
	@Column(name = "OPERATOR")
	@Excel(name = "操作人")
	private String operator;//操作人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "OPERATE_TIME")
	@Excel(name = "创建时间")
	private Date operateTime;//操作时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;//修改时间
	
	@Column(name = "EXAMINE_PERSON")
	@Excel(name = "审核人")
	private String examinePerson;//审核人
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "EXAMINE_TIME")
	@Excel(name = "审核时间")
	private Date examineTime;//审核时间

	@Transient
	private String searchStrTime;//申报时间起

	@Transient
	private String searchEndTime;//申报时间止

	@Transient
	private String accountBook;

	public String getAccountBook() {
		return accountBook;
	}

	public void setAccountBook(String accountBook) {
		this.accountBook = accountBook;
	}

	public String getCdNum() {
		return cdNum;
	}

	public void setCdNum(String cdNum) {
		this.cdNum = cdNum;
	}

	public Date getDeclareTime() {
		return declareTime;
	}

	public void setDeclareTime(Date declareTime) {
		this.declareTime = declareTime;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getServiceProject() {
		return serviceProject;
	}

	public void setServiceProject(String serviceProject) {
		this.serviceProject = serviceProject;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsignor() {
		return consignor;
	}

	public void setConsignor(String consignor) {
		this.consignor = consignor;
	}

	public String getUseUnit() {
		return useUnit;
	}

	public void setUseUnit(String useUnit) {
		this.useUnit = useUnit;
	}

	public String getModeTrade() {
		return modeTrade;
	}

	public void setModeTrade(String modeTrade) {
		this.modeTrade = modeTrade;
	}

	public String getStoragePlace() {
		return storagePlace;
	}

	public void setStoragePlace(String storagePlace) {
		this.storagePlace = storagePlace;
	}

	public String getPortEntry() {
		return portEntry;
	}

	public void setPortEntry(String portEntry) {
		this.portEntry = portEntry;
	}

	public String getContryDeparture() {
		return contryDeparture;
	}

	public void setContryDeparture(String contryDeparture) {
		this.contryDeparture = contryDeparture;
	}

	public String getCustomsDeclarationNumber() {
		return customsDeclarationNumber;
	}

	public void setCustomsDeclarationNumber(String customsDeclarationNumber) {
		this.customsDeclarationNumber = customsDeclarationNumber;
	}

	public String getContryOragin() {
		return contryOragin;
	}

	public void setContryOragin(String contryOragin) {
		this.contryOragin = contryOragin;
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

	public String getCargoClientId() {
		return cargoClientId;
	}

	public void setCargoClientId(String cargoClientId) {
		this.cargoClientId = cargoClientId;
	}

	public String getCargoClientName() {
		return cargoClientName;
	}

	public void setCargoClientName(String cargoClientName) {
		this.cargoClientName = cargoClientName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAuditingState() {
		return auditingState;
	}

	public void setAuditingState(String auditingState) {
		this.auditingState = auditingState;
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

	public String getExaminePerson() {
		return examinePerson;
	}

	public void setExaminePerson(String examinePerson) {
		this.examinePerson = examinePerson;
	}

	public Date getExamineTime() {
		return examineTime;
	}

	public void setExamineTime(Date examineTime) {
		this.examineTime = examineTime;
	}

	public String getSearchStrTime() {
		return searchStrTime;
	}

	public void setSearchStrTime(String searchStrTime) {
		this.searchStrTime = searchStrTime;
	}

	public String getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(String searchEndTime) {
		this.searchEndTime = searchEndTime;
	}
}