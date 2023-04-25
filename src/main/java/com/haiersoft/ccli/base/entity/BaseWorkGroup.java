package com.haiersoft.ccli.base.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * BaseWorkGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BASE_WORK_GROUP")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class BaseWorkGroup implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5323953125478185513L;

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORK_GROUP")
	@SequenceGenerator(name = "SEQ_WORK_GROUP", sequenceName = "SEQ_WORK_GROUP", allocationSize = 1)
	@Column(name = "ID")
	private Integer id; //id
	
	@Column(name = "PERSON")
	private String person; //人员名称
	
	@Column(name = "WORK_TYPE")
	private String workType; //人员类型，1:库管人员  2:理货人员  3：叉车人员
	
	@Column(name = "PARENT_ID")
	private Integer parentId; //所属ID   0 代表为父类，有库管无叉车理货

	@Transient
	private Integer lhNum;  //理货人员数量
	@Transient
	private Integer ccNum;  //叉车人员数量
	@Transient
	private String kgPerson;  //库管人员名称 
	@Transient
	private String lhPerson;  //理货人员名称 
	@Transient
	private String ccPerson;  // 叉车人员名称
	
	public BaseWorkGroup(){
	
	}
	
	public BaseWorkGroup(String person,String workType,Integer parentId){
		this.person=person;
		this.workType=workType;
		this.parentId=parentId;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getLhNum() {
		return lhNum;
	}

	public void setLhNum(Integer lhNum) {
		this.lhNum = lhNum;
	}

	public Integer getCcNum() {
		return ccNum;
	}

	public void setCcNum(Integer ccNum) {
		this.ccNum = ccNum;
	}

	public String getKgPerson() {
		return kgPerson;
	}

	public void setKgPerson(String kgPerson) {
		this.kgPerson = kgPerson;
	}

	public String getLhPerson() {
		return lhPerson;
	}

	public void setLhPerson(String lhPerson) {
		this.lhPerson = lhPerson;
	}

	public String getCcPerson() {
		return ccPerson;
	}

	public void setCcPerson(String ccPerson) {
		this.ccPerson = ccPerson;
	}

	
	 

}