package com.haiersoft.ccli.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * BaseExpenseShare entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BASE_EXPENSE_SHARE")
public class BaseExpenseShare implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8192045069704307853L;

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EXPENSE_SHARE")
	@SequenceGenerator(name="SEQ_EXPENSE_SHARE", sequenceName="SEQ_EXPENSE_SHARE", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //ID
	
	@Column(name = "CLIENT_ID")
	private String clientId; //客户ID
	
	@Column(name = "CLIENT_NAME")
	private String clientName; //客户名称
	
	@Column(name = "SCHEME_NUM")
	private String schemeNum; //方案号
	
	@Column(name = "SCHEME_NAME")
	private String schemeName; //方案名称
	
	@Column(name = "IF_PARENT")
	private String ifParent; //是否为此费用方案的父级客户 0:否 1:是
	
	@Column(name = "PROGRAM_STATE")
	private String programState; //方案是否审核 0:未审核 1：已审核
	
    ////////////////////////////////////////////////////////////////
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getSchemeNum() {
		return schemeNum;
	}

	public void setSchemeNum(String schemeNum) {
		this.schemeNum = schemeNum;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getProgramState() {
		return programState;
	}

	public void setProgramState(String programState) {
		this.programState = programState;
	}

	public String getIfParent() {
		return ifParent;
	}

	public void setIfParent(String ifParent) {
		this.ifParent = ifParent;
	}

	
	
}