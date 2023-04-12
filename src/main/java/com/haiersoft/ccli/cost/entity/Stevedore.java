package com.haiersoft.ccli.cost.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 搬运工
 */
@Entity
@Table(name = "STEVEDORE")
public class Stevedore implements Serializable {

	private static final long serialVersionUID = 4047788502973384287L;

	private Integer id;
	private Integer companyId;
	private String companyName;
	private String name;
	private Integer gender;
	private String phone;
	private Date createDate;
	private String state;
	private String delFlag;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STEVEDORE")
	@SequenceGenerator(name = "SEQ_STEVEDORE", sequenceName = "SEQ_STEVEDORE", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(name = "COMPANY_ID", nullable = true)
	public Integer getCompanyId() {
		return companyId;
	}

	@Column(name = "NAME", nullable = true, length = 20)
	public String getName() {
		return name;
	}

	@Column(name = "GENDER", nullable = true)
	public Integer getGender() {
		return gender;
	}

	@Column(name = "PHONE", nullable = true, length = 20)
	public String getPhone() {
		return phone;
	}

	@Column(name = "CREATE_DATE", nullable = true, length = 7)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateDate() {
		return createDate;
	}

	@Column(name = "STATE", nullable = true, length = 1)
	public String getState() {
		return state;
	}

	@Column(name = "DEL_FLAG", nullable = true, length = 1)
	public String getDelFlag() {
		return delFlag;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@Transient
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}