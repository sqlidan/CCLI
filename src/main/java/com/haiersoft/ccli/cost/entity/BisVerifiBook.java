package com.haiersoft.ccli.cost.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 核销主表
 */
@Entity
@Table(name = "BIS_VERIFI_BOOK")
public class BisVerifiBook implements java.io.Serializable {
   
	private static final long serialVersionUID = -7981382549070615240L;
	@Id
    @Column(name = "VERIFI_NUM", unique = true, nullable = false)
	private String verifiNum; //ID (HX年月日时分秒)
	@Column(name = "CURRENCY")
	private String currency;  // 币种
	@Column(name = "SUMMONEY")
	private Double sumMoney;  //核销金额
	@Column(name = "MONEYRMB")
	private Double moneyRmb;  //核销金额RMB
	@Column(name = "TAXNUM")
	private Double taxNum;  //汇率
	@Column(name = "PZ_NUM")
	private String pzNum;  // 金蝶凭证号
	@Column(name = "BANK_NUM")
	private String bankNum;  // 银行号
	@Column(name = "BANK_NAME")
	private String bankName;  // 银行
	@Column(name = "POSTSTATE")
	private Integer postState=0;//状态  0未发送，1已发送。2.发送失败，3发送成功
	@Column(name = "POSTTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date postTime; //发送时间
	@Column(name = "NSTATE")
	private Integer nState=1;//核销状态  1核销，2取消核销
	@Column(name = "NTYPE")
	private Integer nType=1;//核销类型  1应收，2应付
	@Column(name = "CTYPE")
	private Integer cType;//操作类型  1,挂账，2现结，3冲账
	@Column(name = "CRUSER")
	private String crUser;  //核销人
	@Column(name = "CRTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date crTime; //日期
	@Column(name = "UPUSER")
	private String upUser;  //更改人
	@Column(name = "UPTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date upTime; //更改日期
	@Column(name = "CUSTOMID")
	private Integer customId;  //核销人
	@Column(name = "CUSTOMNAME")
	private String customName;//核销状态 0未核销，1核销，2取消核销
	/******************收集页面信息******************************************************/
	@Transient
	private String ids;//台账ids集合
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getVerifiNum() {
		return verifiNum;
	}
	public void setVerifiNum(String verifiNum) {
		this.verifiNum = verifiNum;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(Double sumMoney) {
		this.sumMoney = sumMoney;
	}
	public String getPzNum() {
		return pzNum;
	}
	public void setPzNum(String pzNum) {
		this.pzNum = pzNum;
	}
	public String getBankNum() {
		return bankNum;
	}
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCrUser() {
		return crUser;
	}
	public void setCrUser(String crUser) {
		this.crUser = crUser;
	}
	public Date getCrTime() {
		return crTime;
	}
	public void setCrTime(Date crTime) {
		this.crTime = crTime;
	}
	public Integer getnState() {
		return nState;
	}
	public void setnState(Integer nState) {
		this.nState = nState;
	}
	public Integer getnType() {
		return nType;
	}
	public void setnType(Integer nType) {
		this.nType = nType;
	}
	public String getUpUser() {
		return upUser;
	}
	public void setUpUser(String upUser) {
		this.upUser = upUser;
	}
	public Date getUpTime() {
		return upTime;
	}
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	public Double getMoneyRmb() {
		return moneyRmb;
	}
	public void setMoneyRmb(Double moneyRmb) {
		this.moneyRmb = moneyRmb;
	}
	public Double getTaxNum() {
		return taxNum;
	}
	public void setTaxNum(Double taxNum) {
		this.taxNum = taxNum;
	}
	public Integer getCustomId() {
		return customId;
	}
	public void setCustomId(Integer customId) {
		this.customId = customId;
	}
	public String getCustomName() {
		return customName;
	}
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	public Integer getPostState() {
		return postState;
	}
	public void setPostState(Integer postState) {
		this.postState = postState;
	}
	public Date getPostTime() {
		return postTime;
	}
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	public Integer getcType() {
		return cType;
	}
	public void setcType(Integer cType) {
		this.cType = cType;
	}
}
