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

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * BisEnterSteveDoring entity. @author MyEclipse Persistence Tools
 * 入库装卸单
 */
@Entity
@Table(name = "BIS_ENTER_STEVEDORING")
public class BisEnterSteveDoring implements java.io.Serializable {
	 
	private static final long serialVersionUID = 4040788502963084203L;
	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ENTER_STEVEDORING")
	@SequenceGenerator(name="SEQ_ENTER_STEVEDORING", sequenceName="SEQ_ENTER_STEVEDORING", allocationSize = 1)  
    @Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "ASN_ID")
	private String asnId;  //ASN
	
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
	
	@Column(name = "DROMR")
	private String droMR;  //mr 
	@Column(name = "DROSTOCKINID")
	private String droStockInId;  //收货方id 
	@Column(name = "DROSTOCKINNAME")
	private String droStockInName;  //收货方名称
	@Column(name = "DROENTERSTOCKTIME")
	private Date droEnterStockTime;  //入库日期	
	@Column(name = "DROSTANINDBOOKIDS")
	private String droStanindBookIds;//账单ids
	@Column(name = "CREATE_USER")
	private String createUser;//创建用户
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	private Date createTime;//创建时间
	
	@Column(name = "NUMPLUS")
	private Double numPlus; //重量系数
	
	public String getDroStanindBookIds() {
		return droStanindBookIds;
	}

	public void setDroStanindBookIds(String droStanindBookIds) {
		this.droStanindBookIds = droStanindBookIds;
	}

	public String getDroMR() {
		return droMR;
	}

	public void setDroMR(String droMR) {
		this.droMR = droMR;
	}

	public String getDroStockInId() {
		return droStockInId;
	}

	public void setDroStockInId(String droStockInId) {
		this.droStockInId = droStockInId;
	}

	public String getDroStockInName() {
		return droStockInName;
	}

	public void setDroStockInName(String droStockInName) {
		this.droStockInName = droStockInName;
	}

	public Date getDroEnterStockTime() {
		return droEnterStockTime;
	}

	public void setDroEnterStockTime(Date droEnterStockTime) {
		this.droEnterStockTime = droEnterStockTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAsnId() {
		return asnId;
	}

	public void setAsnId(String asnId) {
		this.asnId = asnId;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BisEnterSteveDoring [id=");
		builder.append(id);
		builder.append(", asnId=");
		builder.append(asnId);
		builder.append(", clientId=");
		builder.append(clientId);
		builder.append(", client=");
		builder.append(client);
		builder.append(", feeId=");
		builder.append(feeId);
		builder.append(", feePlan=");
		builder.append(feePlan);
		builder.append(", sortingNum=");
		builder.append(sortingNum);
		builder.append(", manNum=");
		builder.append(manNum);
		builder.append(", wrapNum=");
		builder.append(wrapNum);
		builder.append(", packNum=");
		builder.append(packNum);
		builder.append(", ifAllMan=");
		builder.append(ifAllMan);
		builder.append(", netWeight=");
		builder.append(netWeight);
		builder.append("]");
		return builder.toString();
	}

	
	
}