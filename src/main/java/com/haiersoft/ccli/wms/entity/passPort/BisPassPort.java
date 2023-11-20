package com.haiersoft.ccli.wms.entity.passPort;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 核放单表头
 */
@Entity
@Table(name = "BIS_PASSPORT")
public class BisPassPort implements java.io.Serializable {

	private static final long serialVersionUID = -4326979420246878235L;
	@Id
	@Column(name = "ID")
	private String id;
	@Column(name = "STATE")
	private String state;
	@Column(name = "CREATE_BY")
	private String createBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	private Date createTime;
	@Column(name = "UPDATE_BY")
	private String updateBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	@Column(name = "SEQ_NO")
	private String seqNo;
	@Column(name = "PASSPORT_NO")
	private String passportNo;
	@Column(name = "PASSPORT_TYPECD")
	private String passportTypecd;
	@Column(name = "MASTER_CUSCD")
	private String masterCuscd;
	@Column(name = "DCL_TYPECD")
	private String dclTypecd;
	@Column(name = "IO_TYPECD")
	private String ioTypecd;
	@Column(name = "BIND_TYPECD")
	private String bindTypecd;
	@Column(name = "RLT_TB_TYPECD")
	private String rltTbTypecd;
	@Column(name = "RLT_NO")
	private String rltNo;
	@Column(name = "AREAIN_ORIACT_NO")
	private String areainOriactNo;
	@Column(name = "AREAIN_ETPSNO")
	private String areainEtpsno;
	@Column(name = "AREAIN_ETPS_NM")
	private String areainEtpsNm;
	@Column(name = "AREAIN_ETPS_SCCD")
	private String areainEtpsSccd;
	@Column(name = "VEHICLE_NO")
	private String vehicleNo;
	@Column(name = "VEHICLE_IC_NO")
	private String vehicleIcNo;
	@Column(name = "CONTAINER_NO")
	private String containerNo;
	@Column(name = "VEHICLE_WT")
	private String vehicleWt;
	@Column(name = "VEHICLE_FRAME_NO")
	private String vehicleFrameNo;
	@Column(name = "VEHICLE_FRAME_WT")
	private String vehicleFrameWt;
	@Column(name = "CONTAINER_TYPE")
	private String containerType;
	@Column(name = "CONTAINER_WT")
	private String containerWt;
	@Column(name = "TOTAL_WT")
	private String totalWt;
	@Column(name = "TOTAL_GROSS_WT")
	private String totalGrossWt;
	@Column(name = "TOTAL_NET_WT")
	private String totalNetWt;
	@Column(name = "DCL_ER_CONC")
	private String dclErConc;
	@Column(name = "DCL_ETPSNO")
	private String dclEtpsno;
	@Column(name = "DCL_ETPS_NM")
	private String dclEtpsNm;
	@Column(name = "DCL_ETPS_SCCD")
	private String dclEtpsSccd;
	@Column(name = "INPUT_CODE")
	private String inputCode;
	@Column(name = "INPUT_SCCD")
	private String inputSccd;
	@Column(name = "INPUT_NAME")
	private String inputName;
	@Column(name = "ETPS_PREENT_NO")
	private String etpsPreentNo;
	@Column(name = "RMK")
	private String rmk;
	@Column(name = "COL1")
	private String col1;
	@Column(name = "COL2")
	private String col2;
	@Column(name = "COL3")
	private String col3;
	@Column(name = "COL4")
	private String col4;
	@Column(name = "LOCKAGE")
	private String lockage;
	@Column(name = "LOCKAGE_TIME1")
	private String lockageTime1;
	@Column(name = "LOCKAGE_TIME2")
	private String lockageTime2;
	@Column(name = "CHECK_RESULT")
	private String checkResult;
	@Column(name = "DCL_BY")
	private String dclBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "DCL_TIME")
	private Date dclTime;
	@Column(name = "ETPS_PREENT_NO2")
	private String etpsPreentNo2;

	public String getEtpsPreentNo2() {
		return etpsPreentNo2;
	}

	public void setEtpsPreentNo2(String etpsPreentNo2) {
		this.etpsPreentNo2 = etpsPreentNo2;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getPassportTypecd() {
		return passportTypecd;
	}

	public void setPassportTypecd(String passportTypecd) {
		this.passportTypecd = passportTypecd;
	}

	public String getMasterCuscd() {
		return masterCuscd;
	}

	public void setMasterCuscd(String masterCuscd) {
		this.masterCuscd = masterCuscd;
	}

	public String getDclTypecd() {
		return dclTypecd;
	}

	public void setDclTypecd(String dclTypecd) {
		this.dclTypecd = dclTypecd;
	}

	public String getIoTypecd() {
		return ioTypecd;
	}

	public void setIoTypecd(String ioTypecd) {
		this.ioTypecd = ioTypecd;
	}

	public String getBindTypecd() {
		return bindTypecd;
	}

	public void setBindTypecd(String bindTypecd) {
		this.bindTypecd = bindTypecd;
	}

	public String getRltTbTypecd() {
		return rltTbTypecd;
	}

	public void setRltTbTypecd(String rltTbTypecd) {
		this.rltTbTypecd = rltTbTypecd;
	}

	public String getRltNo() {
		return rltNo;
	}

	public void setRltNo(String rltNo) {
		this.rltNo = rltNo;
	}

	public String getAreainOriactNo() {
		return areainOriactNo;
	}

	public void setAreainOriactNo(String areainOriactNo) {
		this.areainOriactNo = areainOriactNo;
	}

	public String getAreainEtpsno() {
		return areainEtpsno;
	}

	public void setAreainEtpsno(String areainEtpsno) {
		this.areainEtpsno = areainEtpsno;
	}

	public String getAreainEtpsNm() {
		return areainEtpsNm;
	}

	public void setAreainEtpsNm(String areainEtpsNm) {
		this.areainEtpsNm = areainEtpsNm;
	}

	public String getAreainEtpsSccd() {
		return areainEtpsSccd;
	}

	public void setAreainEtpsSccd(String areainEtpsSccd) {
		this.areainEtpsSccd = areainEtpsSccd;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getVehicleIcNo() {
		return vehicleIcNo;
	}

	public void setVehicleIcNo(String vehicleIcNo) {
		this.vehicleIcNo = vehicleIcNo;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getVehicleWt() {
		return vehicleWt;
	}

	public void setVehicleWt(String vehicleWt) {
		this.vehicleWt = vehicleWt;
	}

	public String getVehicleFrameNo() {
		return vehicleFrameNo;
	}

	public void setVehicleFrameNo(String vehicleFrameNo) {
		this.vehicleFrameNo = vehicleFrameNo;
	}

	public String getVehicleFrameWt() {
		return vehicleFrameWt;
	}

	public void setVehicleFrameWt(String vehicleFrameWt) {
		this.vehicleFrameWt = vehicleFrameWt;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public String getContainerWt() {
		return containerWt;
	}

	public void setContainerWt(String containerWt) {
		this.containerWt = containerWt;
	}

	public String getTotalWt() {
		return totalWt;
	}

	public void setTotalWt(String totalWt) {
		this.totalWt = totalWt;
	}

	public String getTotalGrossWt() {
		return totalGrossWt;
	}

	public void setTotalGrossWt(String totalGrossWt) {
		this.totalGrossWt = totalGrossWt;
	}

	public String getTotalNetWt() {
		return totalNetWt;
	}

	public void setTotalNetWt(String totalNetWt) {
		this.totalNetWt = totalNetWt;
	}

	public String getDclErConc() {
		return dclErConc;
	}

	public void setDclErConc(String dclErConc) {
		this.dclErConc = dclErConc;
	}

	public String getDclEtpsno() {
		return dclEtpsno;
	}

	public void setDclEtpsno(String dclEtpsno) {
		this.dclEtpsno = dclEtpsno;
	}

	public String getDclEtpsNm() {
		return dclEtpsNm;
	}

	public void setDclEtpsNm(String dclEtpsNm) {
		this.dclEtpsNm = dclEtpsNm;
	}

	public String getDclEtpsSccd() {
		return dclEtpsSccd;
	}

	public void setDclEtpsSccd(String dclEtpsSccd) {
		this.dclEtpsSccd = dclEtpsSccd;
	}

	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	public String getInputSccd() {
		return inputSccd;
	}

	public void setInputSccd(String inputSccd) {
		this.inputSccd = inputSccd;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getEtpsPreentNo() {
		return etpsPreentNo;
	}

	public void setEtpsPreentNo(String etpsPreentNo) {
		this.etpsPreentNo = etpsPreentNo;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getCol1() {
		return col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getCol2() {
		return col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	public String getCol3() {
		return col3;
	}

	public void setCol3(String col3) {
		this.col3 = col3;
	}

	public String getCol4() {
		return col4;
	}

	public void setCol4(String col4) {
		this.col4 = col4;
	}

	public String getLockage() {
		return lockage;
	}

	public void setLockage(String lockage) {
		this.lockage = lockage;
	}

	public String getLockageTime1() {
		return lockageTime1;
	}

	public void setLockageTime1(String lockageTime1) {
		this.lockageTime1 = lockageTime1;
	}

	public String getLockageTime2() {
		return lockageTime2;
	}

	public void setLockageTime2(String lockageTime2) {
		this.lockageTime2 = lockageTime2;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getDclBy() {
		return dclBy;
	}

	public void setDclBy(String dclBy) {
		this.dclBy = dclBy;
	}

	public Date getDclTime() {
		return dclTime;
	}

	public void setDclTime(Date dclTime) {
		this.dclTime = dclTime;
	}
}