package com.haiersoft.ccli.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * BasePlatform entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BASE_PLATFORM")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class BasePlatform implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4974778054100225669L;

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLATFORM")
	@SequenceGenerator(name = "SEQ_PLATFORM", sequenceName = "SEQ_PLATFORM", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer Id; //Id
	
	@Column(name = "PLATFORM")
	private String platform; //月台口名称
	
	@Column(name = "BAK1")
	private String bak1; //备用
	
	@Column(name = "BAK2")
	private String bak2; //备用
	
	@Column(name = "BAK3")
	private Double BAK3; //备用
	
	@Column(name = "BAK4")
	private Date bak4; //备用

	//库号，对应BaseTrayRoom类中的RoomNum库号
	@Column(name = "TRAY_ROOM_NUM")
	private String trayRoomNum;

	//是否参与排队标识，0:否，1：是
	@Column(name = "QUERY_FLAG")
	private Integer queryFlag;

	@Column(name = "SORT")
	private String sort;
	@Column(name = "INOUT_BOUND_FLAG")
	private String inoutBoundFlag;
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	@Column(name = "CREATED_TIME")
	private java.sql.Date createdTime;
	@Column(name = "UPDATED_TIME")
	private java.sql.Date updatedTime;
	@Column(name = "PLATFORM_STATUS")
	private String platformStatus;
	@Column(name = "PLATFORM_NO")
	private Integer platformNo;
	@Column(name = "PLATFORM_TYPE")
	private String platformType;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getBak1() {
		return bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

	public String getBak2() {
		return bak2;
	}

	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}

	public Double getBAK3() {
		return BAK3;
	}

	public void setBAK3(Double bAK3) {
		BAK3 = bAK3;
	}

	public Date getBak4() {
		return bak4;
	}

	public void setBak4(Date bak4) {
		this.bak4 = bak4;
	}

	public String getTrayRoomNum() {
		return trayRoomNum;
	}

	public void setTrayRoomNum(String trayRoomNum) {
		this.trayRoomNum = trayRoomNum;
	}

	public Integer getQueryFlag() {
		return queryFlag;
	}

	public void setQueryFlag(Integer queryFlag) {
		this.queryFlag = queryFlag;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getInoutBoundFlag() {
		return inoutBoundFlag;
	}

	public void setInoutBoundFlag(String inoutBoundFlag) {
		this.inoutBoundFlag = inoutBoundFlag;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public java.sql.Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(java.sql.Date createdTime) {
		this.createdTime = createdTime;
	}

	public java.sql.Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(java.sql.Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getPlatformStatus() {
		return platformStatus;
	}

	public void setPlatformStatus(String platformStatus) {
		this.platformStatus = platformStatus;
	}

	public Integer getPlatformNo() {
		return platformNo;
	}

	public void setPlatformNo(Integer platformNo) {
		this.platformNo = platformNo;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
}