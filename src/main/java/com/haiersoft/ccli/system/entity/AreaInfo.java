package com.haiersoft.ccli.system.entity;

import java.io.Serializable;

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
 * 区域entity
 * @author kkomge
 * @date 2015年5月9日 
 */
@Entity
@Table(name = "area_info")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class AreaInfo implements Serializable {

    private static final long serialVersionUID = -6436303372489567415L;

    private Integer id;
	private String areaCode;
	private String areaName;
	private Integer pid;
	private Integer sort;

	// Constructors

	/** default constructor */
	public AreaInfo() {
	}

	/** minimal constructor */
	public AreaInfo(String areaCode, String areaName, Integer pid) {
		this.areaCode = areaCode;
		this.areaName = areaName;
		this.pid = pid;
	}

	/** full constructor */
	public AreaInfo(String areaCode, String areaName, Integer pid, Integer sort) {
		this.areaCode = areaCode;
		this.areaName = areaName;
		this.pid = pid;
		this.sort = sort;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AREA")
	@SequenceGenerator(name="SEQ_AREA", sequenceName="SEQ_AREA", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "AREA_CODE", nullable = false, length = 12)
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "AREA_NAME", nullable = false, length = 50)
	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name = "PID", nullable = false)
	public Integer getPid() {
		return this.pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Column(name = "SORT")
	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}