package com.haiersoft.ccli.cost.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author PYL
 * @ClassName: BasePieceworkRule
 * @Description: 计件规则表
 * @date 2017年5月12日 
 */
@Entity
@Table(name = "BASE_PIECEWORK_RULE")
@DynamicUpdate 
@DynamicInsert
public class BasePieceworkRule implements java.io.Serializable {

	private static final long serialVersionUID = 5765160688826212395L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_JOBWORK_RULE")
   	@SequenceGenerator(name="SEQ_JOBWORK_RULE", sequenceName="SEQ_JOBWORK_RULE", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;//id
	
	@Column(name = "KEY_ID", unique = true, nullable = false)
	private String keyId;//主键，为人员类型+作业类型+比率
	
	@Column(name = "PERSON_TYPE")
	private String personType; //人员类型
	
	@Column(name = "JOB_TYPE") 
	private String jobType; //作业类型

	@Column(name = "BIG_TYPE") 
	private String bigType; //作业大类
	
	@Column(name = "RATIO")
	private Double ratio; //比例
	
	@Column(name = "OPERATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date operateTime;   //操作时间
	
	@Column(name = "OPERATOR")
	private String operator;//操作人
	
	@Column(name = "REMARK")
	private String remark;  //备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}
	 
	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBigType() {
		return bigType;
	}

	public void setBigType(String bigType) {
		this.bigType = bigType;
	}

	
	
	
}