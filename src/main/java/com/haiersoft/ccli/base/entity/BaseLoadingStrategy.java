package com.haiersoft.ccli.base.entity;

 

import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 出库策略
 *  @author MyEclipse Persistence Tools
 * 
 */
@Entity
@Table(name = "BASE_LOADING_STRATEGY")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class BaseLoadingStrategy implements java.io.Serializable {
   
	private static final long serialVersionUID = -5886401567302078989L;
	
	private Integer id;
	private String strategyName;//策略名称
	private Integer strategyCode;//策略编码
	private String ifField;//条件字段
	private String ifType;//条件表达式 
	private String ifVal;//条件值
	private String ifLink;//连接  and 并且，or 或
	private String ordField;//排序字段
	private String ordSort;//正序，倒叙
	private String crUser;
	private Date crTime;
	
	/*************************表单提交数据***********************************/
	private String orderNum;//属性
	private String[] ifsx;//属性
	private String[] ifbds;//表达式
	private String[] ifval;//值
	private String[] iflink;//条件连接
	private String[] ordsx;//排序属性
	private String[] ordpx;//排序
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOADING_STRATEGY")
	@SequenceGenerator(name = "SEQ_LOADING_STRATEGY", sequenceName = "SEQ_LOADING_STRATEGY", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "STRATEGYNAME")
	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	@Column(name = "STRATEGYCODE")
	public Integer getStrategyCode() {
		return strategyCode;
	}

	public void setStrategyCode(Integer strategyCode) {
		this.strategyCode = strategyCode;
	}
	@Column(name = "IFFIELD")
	public String getIfField() {
		return ifField;
	}

	public void setIfField(String ifField) {
		this.ifField = ifField;
	}
	@Column(name = "IFTYPE")
	public String getIfType() {
		return ifType;
	}

	public void setIfType(String ifType) {
		this.ifType = ifType;
	}
	@Column(name = "IFVAL")
	public String getIfVal() {
		return ifVal;
	}

	public void setIfVal(String ifVal) {
		this.ifVal = ifVal;
	}
	@Column(name = "IFLINK")
	public String getIfLink() {
		return ifLink;
	}

	public void setIfLink(String ifLink) {
		this.ifLink = ifLink;
	}
	@Column(name = "ORDFIELD")
	public String getOrdField() {
		return ordField;
	}

	public void setOrdField(String ordField) {
		this.ordField = ordField;
	}
	@Column(name = "ORDSORT")
	public String getOrdSort() {
		return ordSort;
	}

	public void setOrdSort(String ordSort) {
		this.ordSort = ordSort;
	}
	@Column(name = "CRUSER")
	public String getCrUser() {
		return crUser;
	}

	public void setCrUser(String crUser) {
		this.crUser = crUser;
	}
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "CRTIME")
	public Date getCrTime() {
		return crTime;
	}

	public void setCrTime(Date crTime) {
		this.crTime = crTime;
	}

	@Transient
	public String[] getIfsx() {
		return ifsx;
	}

	public void setIfsx(String[] ifsx) {
		this.ifsx = ifsx;
	}
	@Transient
	public String[] getIfbds() {
		return ifbds;
	}

	public void setIfbds(String[] ifbds) {
		this.ifbds = ifbds;
	}
	@Transient
	public String[] getIfval() {
		return ifval;
	}

	public void setIfval(String[] ifval) {
		this.ifval = ifval;
	}
	@Transient
	public String[] getIflink() {
		return iflink;
	}

	public void setIflink(String[] iflink) {
		this.iflink = iflink;
	}
	@Transient
	public String[] getOrdsx() {
		return ordsx;
	}

	public void setOrdsx(String[] ordsx) {
		this.ordsx = ordsx;
	}
	@Transient
	public String[] getOrdpx() {
		return ordpx;
	}

	public void setOrdpx(String[] ordpx) {
		this.ordpx = ordpx;
	}
	@Transient
	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
}