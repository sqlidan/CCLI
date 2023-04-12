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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * BisOutSteveDoring entity. @author MyEclipse Persistence Tools
 * 出库装卸单
 */
@Entity
@Table(name = "BIS_OUT_STEVEDORING")
@DynamicUpdate 
@DynamicInsert
public class BisOutSteveDoring implements java.io.Serializable {
	 
	private static final long serialVersionUID = 4040788502963084203L;
	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OUT_STEVEDORING")
	@SequenceGenerator(name="SEQ_OUT_STEVEDORING", sequenceName="SEQ_OUT_STEVEDORING", allocationSize = 1)  
    @Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "LOADING_NUM")
	private String loadingNum;  //装车单号
	
	@Column(name = "CLIENT_ID")
	private String clientId;  //装卸队ID
	
	@Column(name = "CLIENT")
	private String client;  //装卸队
	
	@Column(name = "FEE_ID")
	private String feeId;  //装卸队方案ID
	
	@Column(name = "FEE_PLAN")
	private String feePlan;  //装卸队方案
	
	@Column(name = "SORTING_NUM")
	private Double sortingNum;  //应付分拣数量
	
	@Column(name = "MAN_NUM")
	private Double manNum;  //人工装卸数量
	
	@Column(name = "WRAP_NUM")
	private Double wrapNum;  //缠磨数量
	
	@Column(name = "PACK_NUM")
	private Double packNum;  //打包数量
    
	@Column(name = "IF_ALL_MAN")
	private Integer ifAllMan;  //是否全人工 0:否 1：是
	
	@Column(name = "IF_OK")
	private Integer ifOk;  //是否完成 0:否 1：是
	@Transient
	private Double netWeight;//总净重
	
	//droLoadingTime droStockIdName  droStockId droStanindBookId 
	
	@Column(name = "DROLOADINGTIME")
	private Date droLoadingTime;  //装卸时间
	@Column(name = "DROSTOCKIDNAME")
	private String droStockIdName;  //收货方名称
	@Column(name = "DROSTOCKID")
	private String droStockId;  //收获方id
	@Column(name = "DROSTANINDBOOKIDS")
	private String droStanindBookIds;  //台账表id
	@Column(name = "CREATE_USER")
	private String createUser;//创建用户
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	private Date createTime;//创建时间
	
	@Column(name = "NUMPLUS")
	private Double numPlus; //重量系数
	
	public Date getDroLoadingTime() {
		return droLoadingTime;
	}

	public void setDroLoadingTime(Date droLoadingTime) {
		this.droLoadingTime = droLoadingTime;
	}

	public String getDroStockIdName() {
		return droStockIdName;
	}

	public void setDroStockIdName(String droStockIdName) {
		this.droStockIdName = droStockIdName;
	}

	public String getDroStockId() {
		return droStockId;
	}

	public void setDroStockId(String droStockId) {
		this.droStockId = droStockId;
	}

	public String getDroStanindBookIds() {
		return droStanindBookIds;
	}

	public void setDroStanindBookIds(String droStanindBookIds) {
		this.droStanindBookIds = droStanindBookIds;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoadingNum() {
		return loadingNum;
	}

	public void setLoadingNum(String loadingNum) {
		this.loadingNum = loadingNum;
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

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getNumPlus() {
		return numPlus;
	}

	public void setNumPlus(Double numPlus) {
		this.numPlus = numPlus;
	}
	
}