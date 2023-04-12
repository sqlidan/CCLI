package com.haiersoft.ccli.cost.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author PYL
 * @ClassName: WorkReport
 * @Description: 计件统计查询条件实体类
 */
public class WorkReport implements Serializable {

    private static final long serialVersionUID = 2799111007612445959L;

    private String bigType; //作业大类
    private String jobType; //作业类型
    private String personType; //人员类型
    private String type; //工作类型：1入库  2出库
    private String man;
    private Double workload;
    private String first;
    private String personName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date workTimeS;//作业时间（开始）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date workTimeE;//作业时间（结束）
    
    private Double realGrossWeight;
    
    private Double ratio;
    
    private String ruleJobType;
    
	public String getBigType() {
		return bigType;
	}

	public void setBigType(String bigType) {
		this.bigType = bigType;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getWorkTimeS() {
		return workTimeS;
	}

	public void setWorkTimeS(Date workTimeS) {
		this.workTimeS = workTimeS;
	}

	public Date getWorkTimeE() {
		return workTimeE;
	}

	public void setWorkTimeE(Date workTimeE) {
		this.workTimeE = workTimeE;
	}

	public String getMan() {
		return man;
	}

	public void setMan(String man) {
		this.man = man;
	}

	public Double getWorkload() {
		return workload;
	}

	public void setWorkload(Double workload) {
		this.workload = workload;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public Double getRealGrossWeight() {
		return realGrossWeight;
	}

	public void setRealGrossWeight(Double realGrossWeight) {
		this.realGrossWeight = realGrossWeight;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	public String getRuleJobType() {
		return ruleJobType;
	}

	public void setRuleJobType(String ruleJobType) {
		this.ruleJobType = ruleJobType;
	}
	
	
}
