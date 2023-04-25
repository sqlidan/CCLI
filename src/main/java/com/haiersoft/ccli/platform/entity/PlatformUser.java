package com.haiersoft.ccli.platform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
@Entity
@Table(name = "PLATFORM_USER")
@DynamicUpdate
@DynamicInsert
public class PlatformUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLATFORM_USER")
	@SequenceGenerator(name="SEQ_PLATFORM_USER", sequenceName="SEQ_PLATFORM_USER", allocationSize = 1)
	@Column(name = "id")
	private Integer id;// 主键
*/


	@Id
	@GeneratedValue(generator = "platformUserGenerator")
	@GenericGenerator(name = "platformUserGenerator", strategy = "uuid")
	@Column(name = "ID",unique = true)
	private String id;

	@Column(name = "PLATFORM_ID")
	private Integer platformId;// 月台Id

	@Column(name = "PLATFORM_NO")
	private Integer platformNo;//月台号

	@Column(name = "PLATFORM_NAME")
	private String platformName;//月台名称

	@Column(name = "USER_ID")
	private Integer userId;// 用户Id

	@Column(name = "USER_NAME")
	private String userName;//用户名称

	@Column(name = "DELETED_FLAG")
	private String deletedFlag;//删除标志

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATED_TIME")
	private Date createdTime; // 创建日期


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATED_TIME")
	private Date updatedTime;//修改时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createdTime;
	}

	public void setCreateTime(Date createTime) {
		this.createdTime = createTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updateTime) {
		this.updatedTime = updateTime;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public Integer getPlatformNo() {
		return platformNo;
	}

	public void setPlatformNo(Integer platformNo) {
		this.platformNo = platformNo;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
}