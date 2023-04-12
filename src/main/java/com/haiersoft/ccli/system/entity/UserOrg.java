package com.haiersoft.ccli.system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户机构entity
 * @author kkomge
 * @date 2015年5月9日 
 */
@Entity
@Table(name = "user_org")
public class UserOrg implements java.io.Serializable {

    private static final long serialVersionUID = -2349612239767697615L;
    
	private Integer id;
	private Integer userId;
	private Integer orgId;

	// Constructors

	/** default constructor */
	public UserOrg() {
	}

	/** full constructor */
	public UserOrg(Integer userId, Integer orgId) {
		this.userId = userId;
		this.orgId = orgId;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_ORG")
	@SequenceGenerator(name="SEQ_USER_ORG", sequenceName="SEQ_USER_ORG", allocationSize = 1) 
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "user_id", nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "org_id", nullable = false)
	public Integer getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

}