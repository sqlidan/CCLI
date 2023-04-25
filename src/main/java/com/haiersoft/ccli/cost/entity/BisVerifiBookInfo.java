package com.haiersoft.ccli.cost.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 核销明细表
 */
@Entity
@Table(name ="BIS_VERIFI_BOOK_INFO")
public class BisVerifiBookInfo implements java.io.Serializable {
	private static final long serialVersionUID = -6661944091611292419L;
	@Id
    @Column(name = "VERIFI_NUM")
	private String verifiNum; //ID (HX年月日时分秒)
	@Id
	@Column(name = "STANDING_NUM")
	private Integer standingNum;  //台账id
	@Column(name = "YSMONEY")
	private Double sumMoney;  //核销时台账已收金额
	@Column(name = "NOWMONEY")
	private Double nowMoney;  //核销金额RMB
	@Column(name = "TAXNUM")
	private Double taxNum;  //汇率
	@Column(name = "FEEDCODE")
	private String feedCode;  //费目id
	
	public String getVerifiNum() {
		return verifiNum;
	}
	public void setVerifiNum(String verifiNum) {
		this.verifiNum = verifiNum;
	}
	public Integer getStandingNum() {
		return standingNum;
	}
	public void setStandingNum(Integer standingNum) {
		this.standingNum = standingNum;
	}
	public Double getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(Double sumMoney) {
		this.sumMoney = sumMoney;
	}
	public Double getNowMoney() {
		return nowMoney;
	}
	public void setNowMoney(Double nowMoney) {
		this.nowMoney = nowMoney;
	}
	public Double getTaxNum() {
		return taxNum;
	}
	public void setTaxNum(Double taxNum) {
		this.taxNum = taxNum;
	}
	public String getFeedCode() {
		return feedCode;
	}
	public void setFeedCode(String feedCode) {
		this.feedCode = feedCode;
	}
	
	 
	 
}
