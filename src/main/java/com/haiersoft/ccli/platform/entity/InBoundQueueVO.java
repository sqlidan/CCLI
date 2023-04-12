package com.haiersoft.ccli.platform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 */

public class InBoundQueueVO implements Serializable {
	
    private static final long serialVersionUID =1L;
	String yyid;
	String consumeCompany;
	String billNo;
	String containerNo;
	String productName;
	String productType;
	String num;
	String weight;
	String carNumber;
	String originCountry;


	//isCheck  checkStatus checkTime isFreetax isZdmt

	String isCheck;
	String checkStatus;
	String isFreetax;
	String isZdmt;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	Date checkTime;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	Date appointDate;

	Date appointDateStart;

	Date appointDateEnd;

	//排队库号
	String warehouseNo;

	String platformNo;
	String platformName;
	String autoManualFlag;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	Date queueTime;

	String statusFlag;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	Date startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	Date endTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	Date leaveTime;

	public String getConsumeCompany() {
		return consumeCompany;
	}

	public void setConsumeCompany(String consumeCompany) {
		this.consumeCompany = consumeCompany;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public Date getAppointDate() {
		return appointDate;
	}

	public void setAppointDate(Date appointDate) {
		this.appointDate = appointDate;
	}

	public Date getAppointDateStart() {
		return appointDateStart;
	}

	public void setAppointDateStart(Date appointDateStart) {
		this.appointDateStart = appointDateStart;
	}

	public Date getAppointDateEnd() {
		return appointDateEnd;
	}

	public void setAppointDateEnd(Date appointDateEnd) {
		this.appointDateEnd = appointDateEnd;
	}

	public String getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}



	public String getPlatformNo() {
		return platformNo;
	}

	public void setPlatformNo(String platformNo) {
		this.platformNo = platformNo;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getAutoManualFlag() {
		return autoManualFlag;
	}

	public void setAutoManualFlag(String autoManualFlag) {
		this.autoManualFlag = autoManualFlag;
	}

	public Date getQueueTime() {
		return queueTime;
	}

	public void setQueueTime(Date queueTime) {
		this.queueTime = queueTime;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
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

	public Date getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getIsFreetax() {
		return isFreetax;
	}

	public void setIsFreetax(String isFreetax) {
		this.isFreetax = isFreetax;
	}

	public String getIsZdmt() {
		return isZdmt;
	}

	public void setIsZdmt(String isZdmt) {
		this.isZdmt = isZdmt;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getYyid() {
		return yyid;
	}

	public void setYyid(String yyid) {
		this.yyid = yyid;
	}
}