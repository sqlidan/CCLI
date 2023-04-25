package com.haiersoft.ccli.base.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/**
 * BisHscode entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BASE_HSCODE")
public class BaseHscode implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -124332070504732004L;

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HSCODE")
	@SequenceGenerator(name="SEQ_HSCODE", sequenceName="SEQ_HSCODE", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //ID
	
	@Column(name = "ITEM_NUM")
	private String itemNum; //单位代码
	
	@Column(name = "CODE")
	private String code; //海关编码
	
	@Column(name = "CARGO_NAME")
	private String cargoName; //商品名称
	
	@Column(name = "SPEC")
	private String spec; //规格

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
	
	
	

}