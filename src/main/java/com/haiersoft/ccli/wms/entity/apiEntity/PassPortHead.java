package com.haiersoft.ccli.wms.entity.apiEntity;


/**
 * @Author sunzhijie
 *
 */
//@Data
//@ApiModel(value = "核放单表头(PassportHead)")
public class PassPortHead implements java.io.Serializable{
//	@ApiModelProperty(value = "区内企业名称")
//	@JSONField(name = "AreainEtpsNm")
	private String areainEtpsNm;
//	@ApiModelProperty(value = "区内企业编码")
//	@JSONField(name = "AreainEtpsno")
	private String areainEtpsno;
//	@ApiModelProperty(value = "区内企业社会信用代码")
//	@JSONField(name = "AreainEtpsSccd")
	private String areainEtpsSccd;
//	@ApiModelProperty(value = "区内账册编号")
//	@JSONField(name = "AreainOriactNo")
	private String areainOriactNo;
//	@ApiModelProperty(value = "绑定类型代码")
//	@JSONField(name = "BindTypecd")
	private String bindTypecd;
//	@ApiModelProperty(value = "集装箱号")
//	@JSONField(name = "ContainerNo")
	private String containerNo;
//	@ApiModelProperty(value = "集装箱箱型")
//	@JSONField(name = "ContainerType")
	private String containerType;
//	@ApiModelProperty(value = "集装箱重")
//	@JSONField(name = "ContainerWt")
	private String containerWt;
//	@ApiModelProperty(value = "申请人")
//	@JSONField(name = "DclEr")
	private String dclEr;
//	@ApiModelProperty(value = "申请人及联系方式")
//	@JSONField(name = "DclErConc")
	private String dclErConc;
//	@ApiModelProperty(value = "申报企业名称")
//	@JSONField(name = "DclEtpsNm")
	private String dclEtpsNm;
//	@ApiModelProperty(value = "申报企业编号")
//	@JSONField(name = "DclEtpsno")
	private String dclEtpsno;
//	@ApiModelProperty(value = "申报企业社会信用代码")
//	@JSONField(name = "DclEtpsSccd")
	private String dclEtpsSccd;
//	@ApiModelProperty(value = "申报时间")
//	@JSONField(name = "DclTime")
	private String dclTime;
//	@ApiModelProperty(value = "申报类型：1-备案、2-变更、3-作废。目前核放单只允许备案")
//	@JSONField(name = "DclTypecd")
	private String dclTypecd;
//	@ApiModelProperty(value = "审批标记代码")
//	@JSONField(name = "EmapvMarkcd")
	private String emapvMarkcd;
//	@ApiModelProperty(value = "企业内部编号")
//	@JSONField(name = "EtpsPreentNo")
	private String etpsPreentNo;
//	@ApiModelProperty(value = "录入单位代码")
//	@JSONField(name = "InputCode")
	private String inputCode;
//	@ApiModelProperty(value = "录入日期（格式：yyyyMMdd）")
//	@JSONField(name = "InputDate")
	private String inputDate;
//	@ApiModelProperty(value = "录入单位名称")
//	@JSONField(name = "InputName")
	private String inputName;
//	@ApiModelProperty(value = "录入单位社会信用代码")
//	@JSONField(name = "InputSccd")
	private String inputSccd;
//	@ApiModelProperty(value = "进出标志代码")
//	@JSONField(name = "IoTypecd")
	private String ioTypecd;
//	@ApiModelProperty(value = "物流状态代码")
//	@JSONField(name = "LogisticsStucd")
	private String logisticsStucd;
//	@ApiModelProperty(value = "主管海关")
//	@JSONField(name = "MasterCuscd")
	private String masterCuscd;
//	@ApiModelProperty(value = "卡口地磅采集重量")
//	@JSONField(name = "PassCollectWt")
	private String passCollectWt;
//	@ApiModelProperty(value = "卡口ID1")
//	@JSONField(name = "PassId1")
	private String passId1;
//	@ApiModelProperty(value = "卡口ID2")
//	@JSONField(name = "PassId2")
	private String passId2;
//	@ApiModelProperty(value = "核放单编号")
//	@JSONField(name = "PassportNo")
	private String passportNo;
//	@ApiModelProperty(value = "核放单类型")
//	@JSONField(name = "PassportTypecd")
	private String passportTypecd;
//	@ApiModelProperty(value = "过卡状态")
//	@JSONField(name = "PassStatus")
	private String passStatus;
//	@ApiModelProperty(value = "过卡时间1")
//	@JSONField(name = "PassTime1")
	private String passTime1;
//	@ApiModelProperty(value = "过卡时间2")
//	@JSONField(name = "PassTime2")
	private String passTime2;
//	@ApiModelProperty(value = "关联单证编码")
//	@JSONField(name = "RltNo")
	private String rltNo;
//	@ApiModelProperty(value = "关联单证类型")
//	@JSONField(name = "RltTbTypecd")
	private String rltTbTypecd;
//	@ApiModelProperty(value = "备注")
//	@JSONField(name = "Rmk")
	private String rmk;
//	@ApiModelProperty(value = "中心统一编号")
//	@JSONField(name = "SeqNo")
	private String seqNo;
//	@ApiModelProperty(value = "状态")
//	@JSONField(name = "Stucd")
	private String stucd;
//	@ApiModelProperty(value = "货物总毛重")
//	@JSONField(name = "TotalGrossWt")
	private String totalGrossWt;
//	@ApiModelProperty(value = "货物总净重")
//	@JSONField(name = "TotalNetWt")
	private String totalNetWt;
//	@ApiModelProperty(value = "总重量")
//	@JSONField(name = "TotalWt")
	private String totalWt;
//	@ApiModelProperty(value = "车架号")
//	@JSONField(name = "VehicleFrameNo")
	private String vehicleFrameNo;
//	@ApiModelProperty(value = "车架重")
//	@JSONField(name = "VehicleFrameWt")
	private String vehicleFrameWt;
//	@ApiModelProperty(value = "IC卡号(电子车牌）")
//	@JSONField(name = "VehicleIcNo")
	private String vehicleIcNo;
//	@ApiModelProperty(value = "承运车车牌号")
//	@JSONField(name = "VehicleNo")
	private String vehicleNo;
//	@ApiModelProperty(value = "车自重")
//	@JSONField(name = "VehicleWt")
	private String vehicleWt;
//	@ApiModelProperty(value = "重量比对误差")
//	@JSONField(name = "WtError")
	private String wtError;

	public String getAreainEtpsNm() {
		return areainEtpsNm;
	}

	public void setAreainEtpsNm(String areainEtpsNm) {
		this.areainEtpsNm = areainEtpsNm;
	}

	public String getAreainEtpsno() {
		return areainEtpsno;
	}

	public void setAreainEtpsno(String areainEtpsno) {
		this.areainEtpsno = areainEtpsno;
	}

	public String getAreainEtpsSccd() {
		return areainEtpsSccd;
	}

	public void setAreainEtpsSccd(String areainEtpsSccd) {
		this.areainEtpsSccd = areainEtpsSccd;
	}

	public String getAreainOriactNo() {
		return areainOriactNo;
	}

	public void setAreainOriactNo(String areainOriactNo) {
		this.areainOriactNo = areainOriactNo;
	}

	public String getBindTypecd() {
		return bindTypecd;
	}

	public void setBindTypecd(String bindTypecd) {
		this.bindTypecd = bindTypecd;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
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

	public String getDclEr() {
		return dclEr;
	}

	public void setDclEr(String dclEr) {
		this.dclEr = dclEr;
	}

	public String getDclErConc() {
		return dclErConc;
	}

	public void setDclErConc(String dclErConc) {
		this.dclErConc = dclErConc;
	}

	public String getDclEtpsNm() {
		return dclEtpsNm;
	}

	public void setDclEtpsNm(String dclEtpsNm) {
		this.dclEtpsNm = dclEtpsNm;
	}

	public String getDclEtpsno() {
		return dclEtpsno;
	}

	public void setDclEtpsno(String dclEtpsno) {
		this.dclEtpsno = dclEtpsno;
	}

	public String getDclEtpsSccd() {
		return dclEtpsSccd;
	}

	public void setDclEtpsSccd(String dclEtpsSccd) {
		this.dclEtpsSccd = dclEtpsSccd;
	}

	public String getDclTime() {
		return dclTime;
	}

	public void setDclTime(String dclTime) {
		this.dclTime = dclTime;
	}

	public String getDclTypecd() {
		return dclTypecd;
	}

	public void setDclTypecd(String dclTypecd) {
		this.dclTypecd = dclTypecd;
	}

	public String getEmapvMarkcd() {
		return emapvMarkcd;
	}

	public void setEmapvMarkcd(String emapvMarkcd) {
		this.emapvMarkcd = emapvMarkcd;
	}

	public String getEtpsPreentNo() {
		return etpsPreentNo;
	}

	public void setEtpsPreentNo(String etpsPreentNo) {
		this.etpsPreentNo = etpsPreentNo;
	}

	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getInputSccd() {
		return inputSccd;
	}

	public void setInputSccd(String inputSccd) {
		this.inputSccd = inputSccd;
	}

	public String getIoTypecd() {
		return ioTypecd;
	}

	public void setIoTypecd(String ioTypecd) {
		this.ioTypecd = ioTypecd;
	}

	public String getLogisticsStucd() {
		return logisticsStucd;
	}

	public void setLogisticsStucd(String logisticsStucd) {
		this.logisticsStucd = logisticsStucd;
	}

	public String getMasterCuscd() {
		return masterCuscd;
	}

	public void setMasterCuscd(String masterCuscd) {
		this.masterCuscd = masterCuscd;
	}

	public String getPassCollectWt() {
		return passCollectWt;
	}

	public void setPassCollectWt(String passCollectWt) {
		this.passCollectWt = passCollectWt;
	}

	public String getPassId1() {
		return passId1;
	}

	public void setPassId1(String passId1) {
		this.passId1 = passId1;
	}

	public String getPassId2() {
		return passId2;
	}

	public void setPassId2(String passId2) {
		this.passId2 = passId2;
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

	public String getPassStatus() {
		return passStatus;
	}

	public void setPassStatus(String passStatus) {
		this.passStatus = passStatus;
	}

	public String getPassTime1() {
		return passTime1;
	}

	public void setPassTime1(String passTime1) {
		this.passTime1 = passTime1;
	}

	public String getPassTime2() {
		return passTime2;
	}

	public void setPassTime2(String passTime2) {
		this.passTime2 = passTime2;
	}

	public String getRltNo() {
		return rltNo;
	}

	public void setRltNo(String rltNo) {
		this.rltNo = rltNo;
	}

	public String getRltTbTypecd() {
		return rltTbTypecd;
	}

	public void setRltTbTypecd(String rltTbTypecd) {
		this.rltTbTypecd = rltTbTypecd;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getStucd() {
		return stucd;
	}

	public void setStucd(String stucd) {
		this.stucd = stucd;
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

	public String getTotalWt() {
		return totalWt;
	}

	public void setTotalWt(String totalWt) {
		this.totalWt = totalWt;
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

	public String getVehicleIcNo() {
		return vehicleIcNo;
	}

	public void setVehicleIcNo(String vehicleIcNo) {
		this.vehicleIcNo = vehicleIcNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getVehicleWt() {
		return vehicleWt;
	}

	public void setVehicleWt(String vehicleWt) {
		this.vehicleWt = vehicleWt;
	}

	public String getWtError() {
		return wtError;
	}

	public void setWtError(String wtError) {
		this.wtError = wtError;
	}
}
