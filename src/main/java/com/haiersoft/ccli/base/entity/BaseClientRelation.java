package com.haiersoft.ccli.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * BaseClientRelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BASE_CLIENT_RELATION")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class BaseClientRelation implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6968108278172472484L;

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CLIENT_RELATION")
	@SequenceGenerator(name="SEQ_CLIENT_RELATION", sequenceName="SEQ_CLIENT_RELATION", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //ID

	@Column(name = "CLIENT_ID")
	private String clientId; //客户ID

	@Column(name = "CLIENT_NAME")
	private String clientName; //客户名称

	@Column(name = "PARENT_ID")
	private String parentId; //父级客户ID

	@Column(name = "PARENT_NAME")
	private String parentName; //父级客户名称

	@Column(name = "REAL_NAME")
	private String realName;//真实陆海通客户名称

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
}
