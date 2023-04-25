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
public class PaymentReportStock implements Serializable{

    private static final long serialVersionUID = 2799111007612445959L;
	
	private String payId;//业务付款单号
	private String clientName;//客户名称
	private String payee;//收款人
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date askDate;//申请日期
	private Double sum;//总价
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
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public Date getAskDate() {
		return askDate;
	}
	public void setAskDate(Date askDate) {
		this.askDate = askDate;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
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
