package com.haiersoft.ccli.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseContract
 * @Description: 合同实体
 * @date 2016年2月24日 下午4:55:22
 */
@Entity
@Table(name = "BASE_EXPENSE_CONTRACT")
@DynamicUpdate
@DynamicInsert
public class ExpenseContract implements Serializable{

    private static final long serialVersionUID = -7924733559716849293L;
    
    @Id
    @Column(name = "CONTRACT_NUM", unique = true, nullable = false)
    @Excel(name="合同号")
    private String contractNum;//合同号   C+操作人员代码(三位，自动补齐)+6位SEQ按序成生
    
    @Column(name = "CLIENT_ID")
    private String clientId;//客户id
    
    @Column(name = "CLIENT_NAME")
    @Excel(name="客户")
    private String clientName;//客户名称
    
    @Column(name = "CONTRACT_STATE")
    @Excel(name = "审核状态", replace = {"未审核_0", "已审核_1"})
    private String contractState;//默认未审核：1审核，0未审核
    
    @Column(name = "SIGN_TIME")
    @Excel(name="签订时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date signTime;//签订时间 
    
    @Column(name = "EXPIRATION_TIME")
    @Excel(name="到期时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date expirationTime;//到期时间 
    
    @Column(name = "CANVASSION_PERSON")
    @Excel(name="揽货人")
    private String canvassionPerson;//揽货人 
    
    @Column(name = "REMARK")
    @Excel(name="备注")
    private String remark;//备注 
    
    @Column(name = "OPERATOR_PERSON")
    private String operatorPerson;//操作人员 
    
    @Column(name = "OPERATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date operateTime;//操作时间 
    
    @Column(name = "EXAMINE_PERSON")
    private String examinePerson;//审核人员 
    
    @Column(name = "EXAMINE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date examineTime;//审核时间 
    
    @Column(name = "UPDATE_PERSON")
    private String updatePerson;//修改人
    
    @Column(name = "UPDATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date updateTime;//修改时间 
    
    @Column(name = "IS_DEL")
    private String isDel;//是否删除，0正常，1删除
    
    @Column(name = "REMARK1")
    private String remark1;//备注1 
    
    @Column(name = "IF_MAN")
    private String ifMan;//是否人工报价，0 否（机械报价） 1是
    

    public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
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

	public String getContractState() {
		return contractState;
	}

	public void setContractState(String contractState) {
		this.contractState = contractState;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getCanvassionPerson() {
		return canvassionPerson;
	}

	public void setCanvassionPerson(String canvassionPerson) {
		this.canvassionPerson = canvassionPerson;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperatorPerson() {
		return operatorPerson;
	}

	public void setOperatorPerson(String operatorPerson) {
		this.operatorPerson = operatorPerson;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getExaminePerson() {
		return examinePerson;
	}

	public void setExaminePerson(String examinePerson) {
		this.examinePerson = examinePerson;
	}

	public Date getExamineTime() {
		return examineTime;
	}

	public void setExamineTime(Date examineTime) {
		this.examineTime = examineTime;
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

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getIfMan() {
		return ifMan;
	}

	public void setIfMan(String ifMan) {
		this.ifMan = ifMan;
	}

	
    
	
}
