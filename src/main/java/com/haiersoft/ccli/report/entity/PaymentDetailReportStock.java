package com.haiersoft.ccli.report.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Connor.M
 * @ClassName: Stock
 * @Description: 库存信息表
 * @date 2016年3月9日 下午3:44:43
 */
public class PaymentDetailReportStock implements Serializable{

    private static final long serialVersionUID = 2799111007612445959L;
	
	private String payId;//业务付款单号
	private String clientName;//客户名称
	private String sellMan;//销售人员
	private String billNum;//提单号
	private String payee;//收款人
	private double ctnAmount;//箱量
	private String askMan;//申请人
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date askDate;//申请日期
	private Double cfs;//CFS
	private Double cx;//拆箱
	private Double fw;//服务
	private Double gj;//港建
	private Double wx;//外协
	private Double xd;//下毒
	private Date searchStrTime;//开始年月
	private Date searchEndTime;//结束年月
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getSellMan() {
		return sellMan;
	}
	public void setSellMan(String sellMan) {
		this.sellMan = sellMan;
	}
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public double getCtnAmount() {
		return ctnAmount;
	}
	public void setCtnAmount(double ctnAmount) {
		this.ctnAmount = ctnAmount;
	}
	public String getAskMan() {
		return askMan;
	}
	public void setAskMan(String askMan) {
		this.askMan = askMan;
	}
	public Date getAskDate() {
		return askDate;
	}
	public void setAskDate(Date askDate) {
		this.askDate = askDate;
	}
	public Double getCfs() {
		return cfs;
	}
	public void setCfs(Double cfs) {
		this.cfs = cfs;
	}
	public Double getCx() {
		return cx;
	}
	public void setCx(Double cx) {
		this.cx = cx;
	}
	public Double getFw() {
		return fw;
	}
	public void setFw(Double fw) {
		this.fw = fw;
	}
	public Double getGj() {
		return gj;
	}
	public void setGj(Double gj) {
		this.gj = gj;
	}
	public Double getWx() {
		return wx;
	}
	public void setWx(Double wx) {
		this.wx = wx;
	}
	public Double getXd() {
		return xd;
	}
	public void setXd(Double xd) {
		this.xd = xd;
	}
	public Date getSearchStrTime() {
		return searchStrTime;
	}
	public void setSearchStrTime(Date searchStrTime) {
		this.searchStrTime = searchStrTime;
	}
	public Date getSearchEndTime() {
		return searchEndTime;
	}
	public void setSearchEndTime(Date searchEndTime) {
		this.searchEndTime = searchEndTime;
	}
	
	
}
