package com.haiersoft.ccli.system.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 用户entity
 * @author ty
 * @date 2015年1月13日
 */
@Entity
@Table(name = "users")
@DynamicUpdate
@DynamicInsert
public class User implements Serializable {
	
	private static final long serialVersionUID = 9196481546689311668L;
	
	private Integer id;
	@Excel(name = "账号")
	private String loginName;
	
	@Excel(name = "工号")
	private String workNo;
	
	@Excel(name = "姓名")
	private String name;
	
	private String password;
	private String plainPassword;
	private String salt;
	
	@Excel(name = "出生日期", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", isImportField = "true")
	private Timestamp birthday;
	
	@Excel(name = "性别", replace = {"男_1", "女_0"})
	private Short gender;
	
	@Excel(name = "邮箱")
	private String email;
	
	@Excel(name = "电话")
	private String phone;
	
	private String icon;
	@Excel(name = "创建时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", isImportField = "true")
	
	private Timestamp createDate;
	private String state;
	private String description;
	
	@Excel(name = "登录次数")
	private Integer loginCount;
	
	@Excel(name = "上次登录时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", isImportField = "true")
	private Timestamp previousVisit;
	
	private Timestamp lastVisit;
	private String delFlag;
	
	@Column(name = "USER_CODE", length = 3)
	private String userCode;//用户代码，长度为3位

	@Column(name = "PASSWORD_UPDATE_DATE")
	private Timestamp passwordUpdateDate;

	public Timestamp getPasswordUpdateDate() {
		return passwordUpdateDate;
	}

	public void setPasswordUpdateDate(Timestamp passwordUpdateDate) {
		this.passwordUpdateDate = passwordUpdateDate;
	}

	@JsonIgnore
	private Set<UserRole> userRoles = new HashSet<UserRole>(0);
	
	
	// Constructors
	/** default constructor */
	public User() {
	}
	
	public User(Integer id) {
		this.id = id;
	}
	
	/** minimal constructor */
	public User(String loginName, String name, String password) {
		this.loginName = loginName;
		this.name = name;
		this.password = password;
	}
	
	/** full constructor */
	public User(String loginName,String workNo, String name, String password, String salt, Timestamp birthday, Short gender, String email, String phone, String icon, Timestamp createDate, String state, String description, Integer loginCount, Timestamp previousVisit, Timestamp lastVisit, String delFlag, Set<UserRole> userRoles) {
		this.loginName = loginName;
		this.workNo = workNo;
		this.name = name;
		this.password = password;
		this.salt = salt;
		this.birthday = birthday;
		this.gender = gender;
		this.email = email;
		this.phone = phone;
		this.icon = icon;
		this.createDate = createDate;
		this.state = state;
		this.description = description;
		this.loginCount = loginCount;
		this.previousVisit = previousVisit;
		this.lastVisit = lastVisit;
		this.delFlag = delFlag;
		this.userRoles = userRoles;
	}
	
	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USERS")
	@SequenceGenerator(name = "SEQ_USERS", sequenceName = "SEQ_USERS", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "LOGIN_NAME", nullable = false, length = 20)
	public String getLoginName() {
		return this.loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = "WORK_NO")
	public String getWorkNo() {
		return this.workNo;
	}
	
	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}
	
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Column(name = "NAME", nullable = false, length = 20)
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "PASSWORD", nullable = false)
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "SALT")
	public String getSalt() {
		return this.salt;
	}
	
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@Column(name = "BIRTHDAY", length = 19)
	public Timestamp getBirthday() {
		return this.birthday;
	}
	
	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}
	
	@Column(name = "GENDER")
	public Short getGender() {
		return this.gender;
	}
	
	public void setGender(Short gender) {
		this.gender = gender;
	}
	
	@Column(name = "EMAIL", length = 50)
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "PHONE", length = 20)
	public String getPhone() {
		return this.phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(name = "ICON", length = 500)
	public String getIcon() {
		return this.icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Column(name = "CREATE_DATE", length = 19)
	public Timestamp getCreateDate() {
		return this.createDate;
	}
	
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "STATE", length = 1)
	public String getState() {
		return this.state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "LOGIN_COUNT")
	public Integer getLoginCount() {
		return this.loginCount;
	}
	
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}
	
	@Column(name = "PREVIOUS_VISIT", length = 19)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Timestamp getPreviousVisit() {
		return this.previousVisit;
	}
	
	public void setPreviousVisit(Timestamp previousVisit) {
		this.previousVisit = previousVisit;
	}
	
	@Column(name = "LAST_VISIT", length = 19)
	public Timestamp getLastVisit() {
		return this.lastVisit;
	}
	
	public void setLastVisit(Timestamp lastVisit) {
		this.lastVisit = lastVisit;
	}
	
	@Column(name = "DEL_FLAG", length = 1)
	public String getDelFlag() {
		return this.delFlag;
	}
	
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	public Set<UserRole> getUserRoles() {
		return this.userRoles;
	}
	
	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
	// 不持久化到数据库，也不显示在Restful接口的属性.
	@Transient
	@JsonIgnore
	public String getPlainPassword() {
		return plainPassword;
	}
	
	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}
}
