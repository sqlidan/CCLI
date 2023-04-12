package com.haiersoft.ccli.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员类型基础表  20170930
 * @author SL YHN
 * 
 */
@Entity
@Table(name = "BASE_PERSONTYPE_RULE")
public class BasePersontypeRule {
	
	/*ID             NUMBER not null,
	  PERSONTYPEID   NUMBER,
	  PERSONTYPENAME VARCHAR2(100),
	  RATIO          NUMBER(12,4),
	  REMARK         VARCHAR2(400)*/
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "PERSON_TYPEID")
	private Integer personTypeId;  //人员类型ID
	
	@Column(name = "PERSON_TYPENAME")
	private String personTypeName; //人员类型名称
	
	@Column(name = "RATIO")
	private Double ratio;			//系数
	
	@Column(name = "REMARK")
	private String remark;			//备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPersonTypeId() {
		return personTypeId;
	}

	public void setPersonTypeId(Integer personTypeId) {
		this.personTypeId = personTypeId;
	}

	public String getPersonTypeName() {
		return personTypeName;
	}

	public void setPersonTypeName(String personTypeName) {
		this.personTypeName = personTypeName;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
