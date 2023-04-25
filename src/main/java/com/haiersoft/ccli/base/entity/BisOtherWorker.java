package com.haiersoft.ccli.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 杂项计件的实体类 20171008
 * @author SL YHN
 *
 */
@Entity
@Table(name = "BIS_OTHER_WORKER")
public class BisOtherWorker implements java.io.Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1648408744982153139L;

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private String id;					//id
	
	@Column(name = "LINK_ID")
	private String linkId;				//
	
	@Column(name = "WORKER_TYPEID")
	private Integer workerTypeId;		//作业类型ID
	
	@Column(name = "WORKER_YPENAME")
	private String workerTypeName;		//作业类型名称
	
	@Column(name = "WORKER_NAME")
	private String workerName;			//作业人员名
	
	@Column(name = "WORK_DATE")
	private Date workDate;				//作业日期
	
	@Column(name = "WORK_DURATION")
	private Double workDuration;		//作业时长			

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public Integer getWorkerTypeId() {
		return workerTypeId;
	}

	public void setWorkerTypeId(Integer workerTypeId) {
		this.workerTypeId = workerTypeId;
	}

	public String getWorkerTypeName() {
		return workerTypeName;
	}

	public void setWorkerTypeName(String workerTypeName) {
		this.workerTypeName = workerTypeName;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public Double getWorkDuration() {
		return workDuration;
	}

	public void setWorkDuration(Double workDuration) {
		this.workDuration = workDuration;
	}
	
	
}
