package com.haiersoft.ccli.wms.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * BisCiqDeclaration entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_CIQ_DECLARATION")
public class BisCiqDeclaration implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 151633360288021774L;

	// Fields
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private String id; //YYYYMMDDHH+6位SEQID
	
	@Column(name = "CIQ_CODE")
	@Excel(name="报检号")
	private String ciqCode;//报检号
	
	@Column(name = "IN_OUT_SIGN")
	@Excel(name="进出库",replace = {"进库_1", "出库_2"})
	private String inOutSign;   //进出标志  1:进库 2:出库
	
	@Column(name = "CONSIGNOR")
	@Excel(name="发货人 ")
	private String consignor;   // 发货人 
	
	@Column(name = "CONSIGNEE")
	@Excel(name="收货人 ")
	private String consignee;   // 收货人 
	
	@Column(name = "BILL_NUM")
	@Excel(name="提单号 ")
	private String billNum; //提单号
	
	@Column(name = "VESSEL_NAME")
	@Excel(name="船名 ")
	private String vesselName;   //入库时船名
	
	@Column(name = "VOYAGE_NUM")
	@Excel(name="航次")
	private String voyageNum;  //入库时航次
	
	@Column(name = "TRADE_TYPE")
	@Excel(name="贸易方式 ")
	private String tradeType;  //贸易方式
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "DECLARE_TIME")
	@Excel(name="申报日期 ")
	private Date declareTime;  //申报日期
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "RELEASE_TIME")
	@Excel(name="放行日期 ")
	private Date releaseTime;  //放行日期
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CERTIFICATE_TIME")
	@Excel(name="检验证明日期")
	private Date certificateTime;  //检验证明日期(入库有，出库没有)
	
	@Column(name = "PIECE")
	@Excel(name="件数")
	private Integer piece;   //件数
	
	@Column(name = "NET_WEIGHT")
	@Excel(name="净重 ")
	private Double netWeight;  //净重（千克）
	
	
	@Column(name = "RECORD_MAN")
	@Excel(name="录入员 ")
	private String recordMan; //录入员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "RECORD_TIME")
	@Excel(name="录入时间")
	private Date recordTime; // 录入时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime; //修改日期
	
	@Column(name = "REMARK1")
	@Excel(name="备注一 ")
	private String remark1; //备注一
	
	@Column(name = "REMARK2")
	@Excel(name="备注二")
	private String remark2;//备注二
	
	@Column(name = "REMARK3")
	private String remark3;//备注三
	
	@Column(name = "EXTENSION")
	private Integer extension;//展期次数
	
	@Column(name = "EXTENSION_PERSON")
	private String extensionPerson;//展期操作人
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "EXTENSION_TIME")
	private Date extensionTime;//展期操作时间

	@Column(name = "FOR_ID")
	private String forId;//预报单ID
	
	@Column(name = "CLIENT_ID")
	private String clientId;//客户ID
	
	@Column(name = "CLIENT_NAME")
	private String clientName;//客户名称
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCiqCode() {
		return ciqCode;
	}

	public void setCiqCode(String ciqCode) {
		this.ciqCode = ciqCode;
	}

	public String getInOutSign() {
		return inOutSign;
	}

	public void setInOutSign(String inOutSign) {
		this.inOutSign = inOutSign;
	}

	public String getConsignor() {
		return consignor;
	}

	public void setConsignor(String consignor) {
		this.consignor = consignor;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getVoyageNum() {
		return voyageNum;
	}

	public void setVoyageNum(String voyageNum) {
		this.voyageNum = voyageNum;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public Date getDeclareTime() {
		return declareTime;
	}

	public void setDeclareTime(Date declareTime) {
		this.declareTime = declareTime;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Date getCertificateTime() {
		return certificateTime;
	}

	public void setCertificateTime(Date certificateTime) {
		this.certificateTime = certificateTime;
	}

	public Integer getPiece() {
		return piece;
	}

	public void setPiece(Integer piece) {
		this.piece = piece;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public String getRecordMan() {
		return recordMan;
	}

	public void setRecordMan(String recordMan) {
		this.recordMan = recordMan;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public Integer getExtension() {
		return extension;
	}

	public void setExtension(Integer extension) {
		this.extension = extension;
	}

	public String getExtensionPerson() {
		return extensionPerson;
	}

	public void setExtensionPerson(String extensionPerson) {
		this.extensionPerson = extensionPerson;
	}

	public Date getExtensionTime() {
		return extensionTime;
	}

	public void setExtensionTime(Date extensionTime) {
		this.extensionTime = extensionTime;
	}

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


	
	
}