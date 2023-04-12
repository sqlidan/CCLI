package com.haiersoft.ccli.wms.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * BisCustomsDeclaration entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_CUSTOMS_DECLARATION")
public class BisCustomsDeclaration implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1611684073880152552L;

	// Fields
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private String id; //YYYYMMDDHH+6位SEQID
	
	@Column(name = "CD_NUM")
	@Excel(name="业务单号")
	private String cdNum;//业务单号
	
	@Column(name = "IMPORT_PORT")
	@Excel(name="进口口岸")
	private String importPort;  //(默认：青开发区)进口口岸
	
	@Column(name = "IN_OUT_SIGN")
	@Excel(name="进出库",replace = {"进库_1", "出库_2"})
	private String inOutSign;   //进出标志  1:进库 2:出库
	
	@Column(name = "MANAGEMENT_UNIT")
	@Excel(name="经营单位")
	private String managementUnit;//经营单位(入库时为：青岛怡之航冷藏经营单位)
	
	@Column(name = "CONSIGNEE")
	@Excel(name="收货单位 ")
	private String consignee;   // 收货单位   （入库时为：青岛怡之航仓库收货单位）
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "IMPORT_TIME")
	@Excel(name="进口日期 ")
	private Date importTime; //进口日期
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "DECLARE_TIME")
	@Excel(name="申报日期 ")
	private Date declareTime;  //申报日期
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "RELEASE_TIME")
	@Excel(name="放行日期 ")
	private Date releaseTime;  //放行日期
	
	@Column(name = "TRADE_TYPE")
	@Excel(name="贸易方式 ")
	private String tradeType;  //贸易方式
	
	@Column(name = "BILL_NUM")
	@Excel(name="提单号 ")
	private String billNum; //提单号
	
	@Column(name = "CTN_NUM")
	private String ctnNum; //箱号
	
	@Column(name = "CONTAINER_NUM")
	private Integer containerNum; //箱量
	
	@Column(name = "CONTAINER_LOAD_TYPE")
	private String containerLoadType; //装箱方式 (01拼箱（暂时没有）02整箱（默认）)
	
	@Column(name = "VESSEL_NAME")
	@Excel(name="船名 ")
	private String vesselName;   //入库时船名
	
	@Column(name = "VOYAGE_NUM")
	@Excel(name="航次")
	private String voyageNum;  //入库时航次
	
	@Column(name = "PIECE")
	@Excel(name="件数")
	private Integer piece;   //件数
	
	@Column(name = "GROSS_WEIGHT")
	private Double grossWeight; //毛重（千克）
	
	@Column(name = "NET_WEIGHT")
	@Excel(name="净重 ")
	private Double netWeight;  //净重（千克）
	
	@Column(name = "OLD_CD_NUM")
	private String oldCdNum;  //(出库报关单使用)原报关单号
	
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
	
	@Column(name = "EXAM_NUM")
	@Excel(name="审批单号")
	private String examNum;  //审批单号
	
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

	public String getCdNum() {
		return cdNum;
	}

	public void setCdNum(String cdNum) {
		this.cdNum = cdNum;
	}

	public String getImportPort() {
		return importPort;
	}

	public void setImportPort(String importPort) {
		this.importPort = importPort;
	}

	public String getInOutSign() {
		return inOutSign;
	}

	public void setInOutSign(String inOutSign) {
		this.inOutSign = inOutSign;
	}

	public String getManagementUnit() {
		return managementUnit;
	}

	public void setManagementUnit(String managementUnit) {
		this.managementUnit = managementUnit;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
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

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
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

	public Integer getContainerNum() {
		return containerNum;
	}

	public void setContainerNum(Integer containerNum) {
		this.containerNum = containerNum;
	}

	public String getContainerLoadType() {
		return containerLoadType;
	}

	public void setContainerLoadType(String containerLoadType) {
		this.containerLoadType = containerLoadType;
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

	public Integer getPiece() {
		return piece;
	}

	public void setPiece(Integer piece) {
		this.piece = piece;
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

	public String getOldCdNum() {
		return oldCdNum;
	}

	public void setOldCdNum(String oldCdNum) {
		this.oldCdNum = oldCdNum;
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

	public String getExamNum() {
		return examNum;
	}

	public void setExamNum(String examNum) {
		this.examNum = examNum;
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