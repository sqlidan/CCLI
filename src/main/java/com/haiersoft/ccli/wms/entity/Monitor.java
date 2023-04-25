package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @author GLZ
 * @ClassName: Monitor
 * @Description: 监控实体类
 */
public class Monitor implements Serializable{

    private static final long serialVersionUID = 2799111007612445957L;

	private String jobType;//业务类型  1 入库 2出库
	private String jobState;//作业状态  入：0 在库  1车辆已入场（暂不使用） 2收货中 3已收货未上架 4上架中 5已上架   出：6在途  7车辆已入场（暂不用） 8拣货中 9完成捡货 10装车中 11已完成装车
	private String carNum;//车牌号
	private String clientId;//客户ID
	private String clientName;//客户名称
	private String cargoName;//货品名
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date strTime;//开始作业时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date endTime;//结束作业时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date strTimeS;//开始作业开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date strTimeE;//开始作业结束时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date endTimeS;//结束作业开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date endTimeE;//结束作业结束时间
	private String linkId;//入库联系单号
	private String billNum;//提单号
	private String asnNum;//asn号
	private String sku;//sku号
	private String ctnNum;//箱号
	private Double allNum;// 入库：收货件数加上架件数   出库 ：拣货件数+上架件数
	private Double allWeight;// 入库：收货吨数加上架吨数   出库 ：拣货吨数+上架吨数
	//入库用
	private Double planNum;//计划件数
	private Double planWeight;//计划吨数
	private Double shNum;//收货件数
	private Double shWeight;//已收货吨数
	private Double sjNum;//asn上架件数
	private Double sjWeight;//asn上架吨数
	private String operator1;//入库：收货人员   出库：拣货人员
	private String operator2;//入库：上家人员   出库：装车人员
	//出库用
	private String loadingNum;//装车单号
	private Double jhNum;//拣货件数
	private Double jhWeight;//拣货吨数
	private Double zcNum;//装车件数
	private Double zcWeight;//装车吨数
	private String platformNum;//月台号
	private String carNo;//车牌号
	@JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date ccTime ; //创建日期
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date ccTimeS;//创建开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date ccTimeE;//创建结束时间
	private String ccLike;//用于查询创建日期的
	private String linkCreater;//联系单创建人
	
	public String getCcLike() {
		return ccLike;
	}
	public void setCcLike(String ccLike) {
		this.ccLike = ccLike;
	}
	public Date getCcTime() {
		return ccTime;
	}
	public void setCcTime(Date ccTime) {
		this.ccTime = ccTime;
	}
	public String getCtnNum() {
		return ctnNum;
	}
	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getJobState() {
		return jobState;
	}
	public void setJobState(String jobState) {
		this.jobState = jobState;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
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
	public String getCargoName() {
		return cargoName;
	}
	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	public Double getPlanNum() {
		return planNum;
	}
	public void setPlanNum(Double planNum) {
		this.planNum = planNum;
	}
	public Double getPlanWeight() {
		return planWeight;
	}
	public void setPlanWeight(Double planWeight) {
		this.planWeight = planWeight;
	}
	
	public Double getShNum() {
		return shNum;
	}
	public void setShNum(Double shNum) {
		this.shNum = shNum;
	}
	public Double getShWeight() {
		return shWeight;
	}
	public void setShWeight(Double shWeight) {
		this.shWeight = shWeight;
	}
	public Double getSjNum() {
		return sjNum;
	}
	public void setSjNum(Double sjNum) {
		this.sjNum = sjNum;
	}
	public Double getSjWeight() {
		return sjWeight;
	}
	public void setSjWeight(Double sjWeight) {
		this.sjWeight = sjWeight;
	}
	public Date getStrTimeS() {
		return strTimeS;
	}
	public void setStrTimeS(Date strTimeS) {
		this.strTimeS = strTimeS;
	}
	public Date getStrTimeE() {
		return strTimeE;
	}
	public void setStrTimeE(Date strTimeE) {
		this.strTimeE = strTimeE;
	}
	public Date getEndTimeS() {
		return endTimeS;
	}
	public void setEndTimeS(Date endTimeS) {
		this.endTimeS = endTimeS;
	}
	public Date getEndTimeE() {
		return endTimeE;
	}
	public void setEndTimeE(Date endTimeE) {
		this.endTimeE = endTimeE;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getAsnNum() {
		return asnNum;
	}
	public void setAsnNum(String asnNum) {
		this.asnNum = asnNum;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getOperator1() {
		return operator1;
	}
	public void setOperator1(String operator1) {
		this.operator1 = operator1;
	}
	public String getOperator2() {
		return operator2;
	}
	public void setOperator2(String operator2) {
		this.operator2 = operator2;
	}
	public String getLoadingNum() {
		return loadingNum;
	}
	public void setLoadingNum(String loadingNum) {
		this.loadingNum = loadingNum;
	}
	public Double getJhNum() {
		return jhNum;
	}
	public void setJhNum(Double jhNum) {
		this.jhNum = jhNum;
	}
	public Double getJhWeight() {
		return jhWeight;
	}
	public void setJhWeight(Double jhWeight) {
		this.jhWeight = jhWeight;
	}
	public Double getZcNum() {
		return zcNum;
	}
	public void setZcNum(Double zcNum) {
		this.zcNum = zcNum;
	}
	public Double getZcWeight() {
		return zcWeight;
	}
	public void setZcWeight(Double zcWeight) {
		this.zcWeight = zcWeight;
	}
	public Date getStrTime() {
		return strTime;
	}
	public void setStrTime(Date strTime) {
		this.strTime = strTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getPlatformNum() {
		return platformNum;
	}
	public void setPlatformNum(String platformNum) {
		this.platformNum = platformNum;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public Double getAllNum() {
		return allNum;
	}
	public void setAllNum(Double allNum) {
		this.allNum = allNum;
	}
	public Double getAllWeight() {
		return allWeight;
	}
	public void setAllWeight(Double allWeight) {
		this.allWeight = allWeight;
	}
	public Date getCcTimeS() {
		return ccTimeS;
	}
	public void setCcTimeS(Date ccTimeS) {
		this.ccTimeS = ccTimeS;
	}
	public Date getCcTimeE() {
		return ccTimeE;
	}
	public void setCcTimeE(Date ccTimeE) {
		this.ccTimeE = ccTimeE;
	}
	public String getLinkCreater() {
		return linkCreater;
	}
	public void setLinkCreater(String linkCreater) {
		this.linkCreater = linkCreater;
	}
	
}
