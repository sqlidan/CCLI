package com.haiersoft.ccli.base.entity;
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
import org.hibernate.annotations.GenericGenerator;
/**
 * BisHscode entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BASE_ITEMNAME")
public class BaseItemname implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -124332070504732004L;

	// Fields

	private Integer id; //ID
	
	@Column(name = "CODE")
	private String code; //商品编码
	
	@Column(name = "CARGO_NAME")
	private String cargoName; //商品名称

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ITEMNAME")
	@SequenceGenerator(name="SEQ_ITEMNAME", sequenceName="SEQ_ITEMNAME", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)  
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	
	

}