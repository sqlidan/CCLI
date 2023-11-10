package com.haiersoft.ccli.wms.entity.customsDeclaration;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 报关单
 */
@Entity
@Table(name = "BS_CUSTOMS_DECLARATION")
public class BsCustomsDeclaration implements java.io.Serializable {

	private static final long serialVersionUID = -4326979420246878235L;

	@Id
	@Column(name = "FOR_ID")
	private String forId;//报关单ID

	@Column(name = "SERVICE_PROJECT")
	private String serviceProject;  //服务项目 报进_0;报出_1

	/**
	 * 状态
	 * 0-待完善，状态为0
	 * 1-待除核，状态为1
	 * 2-待复审，状态为2
	 * 3-待申报，状态为3
	 * 4-申报核注清单中，状态为4
	 * 5-申报核注清单通过，状态为5
	 * 6-申报报关中，状态为6
	 * 7-报关通过，状态为7
	 */
	@Column(name = "STATE")
	private String state;  //状态

	@Column(name = "CREATE_BY")
	private String createBy; //创建人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	private Date createTime;  //创建日期

	@Column(name = "UPDATE_BY")
	private String updateBy; //修改人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;  //修改日期

	@Column(name = "JL_AUDIT")
	private String jlAudit; //初审人

	@Column(name = "JL_REJECT_REASON")
	private String jlRejectReason; //初审驳回原因

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "JL_AUDIT_TIME")
	private Date jlAuditTime;  //初审时间

	@Column(name = "ZG_AUDIT")
	private String zgAudit; //复审审核

	@Column(name = "ZG_REJECT_REASON")
	private String zgRejectReason; //复审驳回原因

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "ZG_AUDIT_TIME")
	private Date zgAuditTime;  //复审时间

	@Column(name = "CD_BY")
	private String cdBy; //报关人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CD_TIME")
	private Date cdTime;  //报关时间

	@Column(name = "UP_AND_DOWN")
	private String upAndDown; //上传/下载,0-未上传;1-已上传;2-已下载

	@Column(name = "UP_BY")
	private String upBy; //上传人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UP_TIME")
	private Date upTime;  //上传时间

	@Column(name = "UP_FILE_NAME")
	private String upFileName; //上传文件名

	@Column(name = "CHECK_LIST_NO")
	private String checkListNo; //核注清单号

	@Column(name = "CD_NUM")
	private String cdNum;  //报关单号

	@Column(name = "CLIENT_ID")
	private String clientId; //客户ID

	@Column(name = "CLIENT_NAME")
	private String clientName; //客户名称

	@Column(name = "DECLARATION_UNIT_ID")
	private String declarationUnitId; //报关公司ID

	@Column(name = "DECLARATION_UNIT")
	private String declarationUnit; //报关公司

	@Column(name = "BILL_NUM")
	private String billNum;  //提单号

	@Column(name = "TRADE_MODE")
	private String tradeMode;  //贸易方式

	@Column(name = "REMARK")
	private String remark;  //备注

	@Column(name = "STORAGE_PLACE")
	private String storagePlace;  //货物存放地点

	@Column(name = "DTY")
	private String dty;  //件数

	@Column(name = "GROSS_WEIGHT")
	private Double grossWeight;  //毛重

	@Column(name = "NET_WEIGHT")
	private Double netWeight;  //净重

	@Column(name = "CONSIGNEE")
	private String consignee;   // 收货人

	@Column(name = "CONSIGNOR")
	private String consignor;   // 发货人

	@Column(name = "MYG")
	private String myg; // 贸易国

	@Column(name = "QYG")
	private String qyg; // 启运国

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "SB_TIME")
	private Date sbTime;  //申报时间


	public String getForId() {
		return forId;
	}

	public void setForId(String forId) {
		this.forId = forId;
	}

	public String getServiceProject() {
		return serviceProject;
	}

	public void setServiceProject(String serviceProject) {
		this.serviceProject = serviceProject;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getJlAudit() {
		return jlAudit;
	}

	public void setJlAudit(String jlAudit) {
		this.jlAudit = jlAudit;
	}

	public String getJlRejectReason() {
		return jlRejectReason;
	}

	public void setJlRejectReason(String jlRejectReason) {
		this.jlRejectReason = jlRejectReason;
	}

	public Date getJlAuditTime() {
		return jlAuditTime;
	}

	public void setJlAuditTime(Date jlAuditTime) {
		this.jlAuditTime = jlAuditTime;
	}

	public String getZgAudit() {
		return zgAudit;
	}

	public void setZgAudit(String zgAudit) {
		this.zgAudit = zgAudit;
	}

	public String getZgRejectReason() {
		return zgRejectReason;
	}

	public void setZgRejectReason(String zgRejectReason) {
		this.zgRejectReason = zgRejectReason;
	}

	public Date getZgAuditTime() {
		return zgAuditTime;
	}

	public void setZgAuditTime(Date zgAuditTime) {
		this.zgAuditTime = zgAuditTime;
	}

	public String getCdBy() {
		return cdBy;
	}

	public void setCdBy(String cdBy) {
		this.cdBy = cdBy;
	}

	public Date getCdTime() {
		return cdTime;
	}

	public void setCdTime(Date cdTime) {
		this.cdTime = cdTime;
	}

	public String getUpAndDown() {
		return upAndDown;
	}

	public void setUpAndDown(String upAndDown) {
		this.upAndDown = upAndDown;
	}

	public String getUpBy() {
		return upBy;
	}

	public void setUpBy(String upBy) {
		this.upBy = upBy;
	}

	public Date getUpTime() {
		return upTime;
	}

	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}

	public String getUpFileName() {
		return upFileName;
	}

	public void setUpFileName(String upFileName) {
		this.upFileName = upFileName;
	}

	public String getCheckListNo() {
		return checkListNo;
	}

	public void setCheckListNo(String checkListNo) {
		this.checkListNo = checkListNo;
	}

	public String getCdNum() {
		return cdNum;
	}

	public void setCdNum(String cdNum) {
		this.cdNum = cdNum;
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

	public String getDeclarationUnitId() {
		return declarationUnitId;
	}

	public void setDeclarationUnitId(String declarationUnitId) {
		this.declarationUnitId = declarationUnitId;
	}

	public String getDeclarationUnit() {
		return declarationUnit;
	}

	public void setDeclarationUnit(String declarationUnit) {
		this.declarationUnit = declarationUnit;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getTradeMode() {
		return tradeMode;
	}

	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStoragePlace() {
		return storagePlace;
	}

	public void setStoragePlace(String storagePlace) {
		this.storagePlace = storagePlace;
	}

	public String getDty() {
		return dty;
	}

	public void setDty(String dty) {
		this.dty = dty;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
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

	public String getMyg() {
		return myg;
	}

	public void setMyg(String myg) {
		this.myg = myg;
	}

	public String getQyg() {
		return qyg;
	}

	public void setQyg(String qyg) {
		this.qyg = qyg;
	}

	public Date getSbTime() {
		return sbTime;
	}

	public void setSbTime(Date sbTime) {
		this.sbTime = sbTime;
	}
}