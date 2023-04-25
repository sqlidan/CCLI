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
@Entity
@Table(name = "BASE_PRODUCT_CALSS")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class BaseProductClass {
	private Integer  id;
	private String pName;//产品名称
	private Integer printId;//父类ID
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_PRODUCT_CLASS")
	@SequenceGenerator(name = "SEQUENCE_PRODUCT_CLASS", sequenceName = "SEQUENCE_PRODUCT_CLASS", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "PNAME")
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	@Column(name = "PRINTID")
	public Integer getPrintId() {
		return printId;
	}
	public void setPrintId(Integer printId) {
		this.printId = printId;
	}
}
