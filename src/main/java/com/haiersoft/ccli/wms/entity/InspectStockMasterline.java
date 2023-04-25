package com.haiersoft.ccli.wms.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 取样管理新需求添加的取样明细的主表
 * @author yhn 20170924
 *
 */

@Entity
@Table(name = "BIS_INSPECT_STOCK_MASTERLINE")
public class InspectStockMasterline {
	@Id
	@Column(name = "INSPECT_ID")
	private String inspectId;
	
	@Basic
	@Column(name = "STOCK_ID")
	private String stockId;
	
	@Basic
	@Column(name = "STOCK_NAME")
	private String stockName;
	
	@Basic
	@Column(name = "INSPECT_TOTAL")
	private Integer inspectTotal;
	
	@Basic
	@Column(name = "OPERATE_STATE")
	private Integer operateState;
	
	@Basic
	@Column(name = "OPERATE_DATE")
	private Date operateDate;
	
	@Basic
	@Column(name = "OPERATE_USER_NAME")
	private String operateUserName;
	
	@Basic
	@Column(name = "OPERATE_USER_ID")
	private String operateUserId;
	
	@Basic
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Basic
	@Column(name = "CREATE_USER_NAME")
	private String createUserName;
	
	@Basic
	@Column(name = "CREATE_USER_ID")
	private String createUserId;
	
	@Basic
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Basic
	@Column(name = "STATE")
	private Integer state;
	
	@Basic
	@Column(name = "CHECK_DATE")
	private Date checkDate;
	
	@Basic
	@Column(name = "SAMPLE_DATE")
	private Date sampleDate;

	public String getInspectId() {
		return inspectId;
	}

	public void setInspectId(String inspectId) {
		this.inspectId = inspectId;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Integer getInspectTotal() {
		return inspectTotal;
	}

	public void setInspectTotal(Integer inspectTotal) {
		this.inspectTotal = inspectTotal;
	}

	public Integer getOperateState() {
		return operateState;
	}

	public void setOperateState(Integer operateState) {
		this.operateState = operateState;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperateUserName() {
		return operateUserName;
	}

	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}

	public String getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(String operateUserId) {
		this.operateUserId = operateUserId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getSampleDate() {
		return sampleDate;
	}

	public void setSampleDate(Date sampleDate) {
		this.sampleDate = sampleDate;
	}
	
	
}
