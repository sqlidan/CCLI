package com.haiersoft.ccli.system.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 权限 entity
 * @author ty
 * @date 2015年1月13日
 */
@Entity
@Table(name = "permission")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class Permission implements Serializable {

    private static final long serialVersionUID = 2948000433040353408L;
    
	private Integer id;
	private Integer pid;
	private String name;
	private String type;
	private Integer sort;
	private String url;
	private String icon;
	private String permCode;
	private String description;
	private String state;
	@Column(name = "IF_SHOW")
	private Integer ifShow = 0;
	
	@JsonIgnore
	private Set<RolePermission> rolePermissions = new HashSet<RolePermission>(0);

	// Constructors

	/** default constructor */
	public Permission() {
	}

	/** minimal constructor */
	public Permission(String name) {
		this.name = name;
	}
	
	public Permission(Integer id) {
		this.id=id;
	}
	
	public Permission (Integer id,Integer pid,String name){
		this.id=id;
		this.pid=pid;
		this.name=name;
	}
	
	public Permission (Integer pid,String name, String type,String url,String permCode){
		this.pid=pid;
		this.name=name;
		this.type=type;
		this.url=url;
		this.permCode=permCode;
	}

	/** full constructor */
	public Permission(Integer pid, String name, String type, Integer sort, String url, String icon, String permCode, String description, String state, Set<RolePermission> rolePermissions) {
		this.pid = pid;
		this.name = name;
		this.type = type;
		this.sort = sort;
		this.url = url;
		this.icon = icon;
		this.permCode = permCode;
		this.description = description;
		this.state=state;
		this.rolePermissions = rolePermissions;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERMISSION")
	@SequenceGenerator(name="SEQ_PERMISSION", sequenceName="SEQ_PERMISSION", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "PID")
	public Integer getPid() {
		return this.pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TYPE", length = 20)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "SORT")
	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Column(name = "URL")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "ICON")
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "PERM_CODE", length = 50)
	public String getPermCode() {
		return this.permCode;
	}

	public void setPermCode(String permCode) {
		this.permCode = permCode;
	}

	@Column(name = "DESCRIPTION", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "permission")
	public Set<RolePermission> getRolePermissions() {
		return this.rolePermissions;
	}

	public void setRolePermissions(Set<RolePermission> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

	@Column(name = "STATE", length = 1)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getIfShow() {
		return ifShow;
	}

	public void setIfShow(Integer ifShow) {
		this.ifShow = ifShow;
	}

	
}