package com.haiersoft.ccli.base.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * BaseClientRelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BASE_CLIENT_RANK")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class BaseClientRank implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3397947878220760454L;

	// Fields
	@Id
	@Column(name = "rank")
	private String rank; //等级
	
	@Column(name = "NICK_NAME")
	private String nickName; //昵称
	
	@Column(name = "MIN_NUM")
	private Double minNum; //最低库存量 (暂时都默认为净重)
	
	@Column(name = "MIN_TYPE")
	private String minType; //库存判断单位，1：件数 2：净重 3：毛重
	
	@Column(name = "CREATE_USER")
	private String createUser; //创建人
	
	@Column(name = "CREATE_TIME")
	private Date createTime; //创建时间

	@Column(name = "REMARK")
	private String remark; //备注

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Double getMinNum() {
		return minNum;
	}

	public void setMinNum(Double minNum) {
		this.minNum = minNum;
	}

	public String getMinType() {
		return minType;
	}

	public void setMinType(String minType) {
		this.minType = minType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	

}