package com.haiersoft.ccli.cost.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * BisOutStockInfo entity. @author MyEclipse Persistence Tools
 * 倒箱装卸单
 */
@Entity
@Table(name = "BIS_BACK_STEVEDORING")
public class BisBackSteveDoring implements java.io.Serializable {
	 
	private static final long serialVersionUID = 4040788502963084204L;
	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BACK_STEVEDORING")
	@SequenceGenerator(name="SEQ_BACK_STEVEDORING", sequenceName="SEQ_BACK_STEVEDORING", allocationSize = 1)  
    @Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "BILL_NUM")
	private String billNum;  //提单号
	
	@Column(name = "CLIENT_ID")
	private String clientId;  //装卸队ID
	
	@Column(name = "CLIENT")
	private String client;  //装卸队
	
	@Column(name = "FEE_ID")
	private String feeId;  //装卸队方案ID
	
	@Column(name = "FEE_PLAN")
	private String feePlan;  //装卸队方案
	
	@Column(name = "MR")
	private String mr;  //MR
	
	@Column(name = "SORTING_NUM")
	private Double sortingNum;  //在库分拣数量
	
	@Column(name = "MAN_NUM")
	private Double manNum;  //人工装卸数量
	
	@Column(name = "WRAP_NUM")
	private Double wrapNum;  //缠磨数量
	
	@Column(name = "PACK_NUM")
	private Double packNum;  //打包数量
	
	@Column(name = "NLABLE_NUM")
	private Double nbqNum;  //内标签数量
	
	@Column(name = "WLABLE_NUM")
	private Double wbqNum;  //外标签数量
	
	@Column(name = "MT_NUM")
	private Double mtNum;  //码托
	
	@Column(name = "ZTJ_NUM")
	private Double ztjNum;  //装铁架
	
	@Column(name = "CTJ_NUM")
	private Double ctjNum;  //拆铁架
	
    
	@Column(name = "IF_ALL_MAN")
	private Integer ifAllMan;  //是否全人工 0:否 1：是
	
	@Column(name = "IF_OK")
	private Integer ifOk;  //是否完成 0:否 1：是
	@Transient
	private Double weight;//总净重 
	
	//drostockId drobackdate standingbookids; drostockIn 
	@Column(name = "DROSTOCKID")
	private String drostockId;  //客户id
	@Column(name = "DROSTOCKIN")
	private String drostockIn;  //客户名称
	@Column(name = "STANDINGBOOKIDS")
	private String standingbookids;  //回写台账ids
	@Column(name = "DROBACKDATE")
	private Date drobackdate;  //倒箱日期
	
	@Transient
	private Date date;
	
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDrostockId() {
		return drostockId;
	}

	public void setDrostockId(String drostockId) {
		this.drostockId = drostockId;
	}

	public String getDrostockIn() {
		return drostockIn;
	}

	public void setDrostockIn(String drostockIn) {
		this.drostockIn = drostockIn;
	}

	public String getStandingbookids() {
		return standingbookids;
	}

	public void setStandingbookids(String standingbookids) {
		this.standingbookids = standingbookids;
	}

	public Date getDrobackdate() {
		return drobackdate;
	}

	public void setDrobackdate(Date drobackdate) {
		this.drobackdate = drobackdate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}


	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getFeePlan() {
		return feePlan;
	}

	public void setFeePlan(String feePlan) {
		this.feePlan = feePlan;
	}

	public String getMr() {
		return mr;
	}

	public void setMr(String mr) {
		this.mr = mr;
	}

	public Double getSortingNum() {
		return sortingNum;
	}

	public void setSortingNum(Double sortingNum) {
		this.sortingNum = sortingNum;
	}

	public Double getManNum() {
		return manNum;
	}

	public void setManNum(Double manNum) {
		this.manNum = manNum;
	}

	public Double getWrapNum() {
		return wrapNum;
	}

	public void setWrapNum(Double wrapNum) {
		this.wrapNum = wrapNum;
	}

	public Double getPackNum() {
		return packNum;
	}

	public void setPackNum(Double packNum) {
		this.packNum = packNum;
	}

	public Integer getIfAllMan() {
		return ifAllMan;
	}

	public void setIfAllMan(Integer ifAllMan) {
		this.ifAllMan = ifAllMan;
	}

	public Integer getIfOk() {
		return ifOk;
	}

	public void setIfOk(Integer ifOk) {
		this.ifOk = ifOk;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getNbqNum() {
		return nbqNum;
	}

	public void setNbqNum(Double nbqNum) {
		this.nbqNum = nbqNum;
	}

	public Double getWbqNum() {
		return wbqNum;
	}

	public void setWbqNum(Double wbqNum) {
		this.wbqNum = wbqNum;
	}

	public Double getMtNum() {
		return mtNum;
	}

	public void setMtNum(Double mtNum) {
		this.mtNum = mtNum;
	}

	public Double getZtjNum() {
		return ztjNum;
	}

	public void setZtjNum(Double ztjNum) {
		this.ztjNum = ztjNum;
	}

	public Double getCtjNum() {
		return ctjNum;
	}

	public void setCtjNum(Double ctjNum) {
		this.ctjNum = ctjNum;
	}
}