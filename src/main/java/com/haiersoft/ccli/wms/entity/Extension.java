package com.haiersoft.ccli.wms.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Extension entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BASE_EXTENSION")
public class Extension implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5096013579659083983L;

	// Fields
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //
	
	@Column(name = "WARNING_DAY")
	private Integer warningDay;//预警间隔（天）
	
	@Column(name = "OUT_WARNING_DAY")
	private Integer outWarningDay;   //弹出预警间隔（天）
	
	@Column(name = "EXTENSION_DAY")
	private Integer extensionDay;   // 展期时间（天）
	
	@Column(name = "EXTENSION_NUM")
	private Integer extensionNum;   // 展期次数 

	@Column(name = "UPDATE_PERSON")
	private String updatePerson;   // 展期参数更新人
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;   // 展期参数更新时间
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWarningDay() {
		return warningDay;
	}

	public void setWarningDay(Integer warningDay) {
		this.warningDay = warningDay;
	}

	public Integer getOutWarningDay() {
		return outWarningDay;
	}

	public void setOutWarningDay(Integer outWarningDay) {
		this.outWarningDay = outWarningDay;
	}

	public Integer getExtensionDay() {
		return extensionDay;
	}

	public void setExtensionDay(Integer extensionDay) {
		this.extensionDay = extensionDay;
	}

	public Integer getExtensionNum() {
		return extensionNum;
	}

	public void setExtensionNum(Integer extensionNum) {
		this.extensionNum = extensionNum;
	}

	public String getUpdatePerson() {
		return updatePerson;
	}

	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}