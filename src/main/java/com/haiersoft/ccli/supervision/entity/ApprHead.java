package com.haiersoft.ccli.supervision.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 分类监管 申请单头实体类
 * 
 * @author
 *
 */

@Entity
@Table(name = "FLJG_APPR_HEAD")
@DynamicUpdate
@DynamicInsert
public class ApprHead implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2082868128958098068L;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false)
	private String id; // 主键id

	@JsonProperty(value = "ApprId")
	@Column(name = "APPR_ID")
	private String apprId;

	@JsonProperty(value = "LinkId")
	@Column(name = "LINK_ID")
	private String linkId;

	//出库申请单填写此列
	@JsonProperty(value = "OrderNum")
	@Column(name = "ORDER_NUM")
	private String orderNum;
	
	@JsonProperty(value = "ApprType")
	@Column(name = "APPR_TYPE")
	private String apprType;

	@JsonProperty(value = "IoType")
	@Column(name = "IO_TYPE")
	private String ioType;

	@JsonProperty(value = "BondInvtNo")
	@Column(name = "BONDINV_NO")
	private String bondInvtNo;

	@JsonProperty(value = "PackNo")
	@Column(name = "PACK_NO")
	private String packNo;

	@JsonProperty(value = "GrossWt")
	@Column(name = "GROSS_WT")
	private String grossWt;

	@JsonProperty(value = "CustomsCode")
	@Column(name = "CUSTOMES_CODE")
	private String customsCode;

	@JsonProperty(value = "EmsNo")
	@Column(name = "EMS_NO")
	private String emsNo;

	@Id
	@JsonProperty(value = "GNo")
	@Column(name = "GNO", unique = true, nullable = false)
	private Integer gNo;

	@JsonProperty(value = "DNote")
	@Column(name = "DNOTE")
	private String dNote;

	@JsonProperty(value = "ItemNum")
	@Column(name = "ITEM_NUM") //提单号
	private String itemNum;

	@JsonProperty(value = "InputEr")
	@Column(name = "INPUTER")
	private String inputEr;

	@JsonProperty(value = "InputDate")
	@Column(name = "INPUT_DATE")
	private String inputDate;

	@JsonProperty(value = "DDate")
	@Column(name = "DDATE")
	private String dDate;

	@JsonProperty(value = "ApproveDate")
	@Column(name = "APPROVE_DATE")
	private String approveDate;

	@JsonProperty(value = "ApproveNote")
	@Column(name = "APPROVE_NOTE")
	private String approveNote;

	@JsonProperty(value = "TradeCode")
	@Column(name = "TRADE_CODE")
	private String tradeCode;

	@JsonProperty(value = "TradeName")
	@Column(name = "TRADE_NAME")
	private String tradeName;

	@JsonProperty(value = "OwnerCode")
	@Column(name = "OWNER_CODE")
	private String ownerCode;

	@JsonProperty(value = "OwnerName")
	@Column(name = "OWNER_NAME")
	private String ownerName;

	@JsonProperty(value = "AgentCode")
	@Column(name = "AGENT_CODE")
	private String agentCode;

	@JsonProperty(value = "AgentName")
	@Column(name = "AGENT_NAME")
	private String agentName;

	@JsonProperty(value = "LoadingFlag")
	@Column(name = "LOADING_FLAG")
	private String loadingFlag;

	@JsonProperty(value = "DeclType")
	@Column(name = "DECL_TYPE")
	private String declType;

	//本地状态 1为正常 2为作废
	@JsonProperty(value = "LocalStatus")
	@Column(name = "LOCAL_STATUS")
	private String localStatus;

	@JsonProperty(value = "Status")
	@Column(name = "STATUS")
	private String status;

	@JsonProperty(value = "PassStatus")
	@Column(name = "PASS_STATUS")
	private String passStatus;
	
	@JsonProperty(value = "CREATETIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "CREATE_TIME")
    private Date createTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApprId() {
		return apprId;
	}

	public void setApprId(String apprId) {
		this.apprId = apprId;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getApprType() {
		return apprType;
	}

	public void setApprType(String apprType) {
		this.apprType = apprType;
	}

	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}

	public String getBondInvtNo() {
		return bondInvtNo;
	}

	public void setBondInvtNo(String bondInvtNo) {
		this.bondInvtNo = bondInvtNo;
	}

	public String getPackNo() {
		return packNo;
	}

	public void setPackNo(String packNo) {
		this.packNo = packNo;
	}

	public String getGrossWt() {
		return grossWt;
	}

	public void setGrossWt(String grossWt) {
		this.grossWt = grossWt;
	}

	public String getCustomsCode() {
		return customsCode;
	}

	public void setCustomsCode(String customsCode) {
		this.customsCode = customsCode;
	}

	public String getEmsNo() {
		return emsNo;
	}

	public void setEmsNo(String emsNo) {
		this.emsNo = emsNo;
	}

	public Integer getgNo() {
		return gNo;
	}

	public void setgNo(Integer gNo) {
		this.gNo = gNo;
	}

	public String getdNote() {
		return dNote;
	}

	public void setdNote(String dNote) {
		this.dNote = dNote;
	}

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}

	public String getInputEr() {
		return inputEr;
	}

	public void setInputEr(String inputEr) {
		this.inputEr = inputEr;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getdDate() {
		return dDate;
	}

	public void setdDate(String dDate) {
		this.dDate = dDate;
	}

	public String getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	public String getApproveNote() {
		return approveNote;
	}

	public void setApproveNote(String approveNote) {
		this.approveNote = approveNote;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getLoadingFlag() {
		return loadingFlag;
	}

	public void setLoadingFlag(String loadingFlag) {
		this.loadingFlag = loadingFlag;
	}

	public String getDeclType() {
		return declType;
	}

	public void setDeclType(String declType) {
		this.declType = declType;
	}

	public String getLocalStatus() {
		return localStatus;
	}

	public void setLocalStatus(String localStatus) {
		this.localStatus = localStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPassStatus() {
		return passStatus;
	}

	public void setPassStatus(String passStatus) {
		this.passStatus = passStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
