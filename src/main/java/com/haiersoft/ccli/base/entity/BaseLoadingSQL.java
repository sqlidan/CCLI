package com.haiersoft.ccli.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 出库策略
 *  @author MyEclipse Persistence Tools
 * 
 */
@Entity
@Table(name = "BASE_LOADING_SQL")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class BaseLoadingSQL implements java.io.Serializable {
	
    private static final long serialVersionUID = -388074142548627730L;
    
	private Integer id;
	private String ifStr;//if sql
	private String ordStr;//order sql
	@Id
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "IFSTR")
	public String getIfStr() {
		return ifStr;
	}
	public void setIfStr(String ifStr) {
		this.ifStr = ifStr;
	}
	@Column(name = "ORDSTR")
	public String getOrdStr() {
		return ordStr;
	}
	public void setOrdStr(String ordStr) {
		this.ordStr = ordStr;
	}
	
	 
}