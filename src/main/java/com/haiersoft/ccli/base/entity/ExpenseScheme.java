package com.haiersoft.ccli.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseScheme
 * @Description: 费用方案实体
 * @date 2016年2月24日 下午4:54:58
 */
@Entity
@Table(name = "BASE_EXPENSE_SCHEME")
@DynamicUpdate
@DynamicInsert
public class ExpenseScheme implements Serializable{

    private static final long serialVersionUID = 3024953784131138018L;
    
    @Id
    @Column(name = "SCHEME_NUM", unique = true, nullable = false)
    @Excel(name = "方案编号")
    private String schemeNum;//10位SEQ按序成生  编号
    
    @Column(name = "SCHEME_NAME")
    @Excel(name = "方案名称")
    private String schemeName;//方案名称
    
    @Column(name = "CONTRACT_ID")
    @Excel(name = "合同号")
	private String contractId;//合同id 
	
    @Column(name = "CUSTOMS_ID")
	private String customsId;//客户ID 
	
    @Column(name = "CUSTOMS_NAME")
    @Excel(name = "客户名称")
	private String customsName;//客户名称 
	
    @Column(name = "PROGRAM_TYPE")
    @Excel(name = "方案类型")
	private String programType;//代码,方案类型 
	
    @Column(name = "BIS_TYPE")
    @Excel(name = "业务类型")
	private String bisType;//代码,业务类型 
    
    @Column(name = "IF_GET")
	private String ifGet;// 1应收 2应付 
	
    @Column(name = "PROGRAM_STATE")
    @Excel(name = "方案状态", replace = {"未审核_0", "已审核_1"})
	private String programState;//默认：0未审核 1 已审核
	
    @Column(name = "EXAMINE_PERSON")
	private String examinePerson;//审核人 
	
    @Column(name = "EXAMINE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date examineTime;//审核时间 
	
    @Column(name = "OPERATOR_PERSON")
    @Excel(name = "创建人")
	private String operatorPerson;//操作人员 
	
    @Column(name = "OPERATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date operateTime;//操作时间 
	
    @Column(name = "UPDATE_PERSON")
	private String updatePerson;//操作人员 
	
    @Column(name = "UPDATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date updateTime;//修改时间 
	
    @Column(name = "REMARK")
	private String remark;//备注
    
    @Column(name = "IS_DEL")
   	private String isDel;//是否删除，0正常，1删除

    @Transient
    private String shareIds;//共享客户 
    
	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getIfGet() {
		return ifGet;
	}

	public void setIfGet(String ifGet) {
		this.ifGet = ifGet;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public String getSchemeNum() {
		return schemeNum;
	}

	public void setSchemeNum(String schemeNum) {
		this.schemeNum = schemeNum;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getCustomsId() {
		return customsId;
	}

	public void setCustomsId(String customsId) {
		this.customsId = customsId;
	}

	public String getCustomsName() {
		return customsName;
	}

	public void setCustomsName(String customsName) {
		this.customsName = customsName;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public String getBisType() {
		return bisType;
	}

	public void setBisType(String bisType) {
		this.bisType = bisType;
	}

	public String getProgramState() {
		return programState;
	}

	public void setProgramState(String programState) {
		this.programState = programState;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShareIds() {
		return shareIds;
	}

	public void setShareIds(String shareIds) {
		this.shareIds = shareIds;
	}
    
    
}
