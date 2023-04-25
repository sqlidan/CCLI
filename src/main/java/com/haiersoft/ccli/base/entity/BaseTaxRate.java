package com.haiersoft.ccli.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 税率entity
 * @author pyl
 * @date 2016年2月27日
 */
@Entity
@Table(name = "BASE_TAX_RATE")
@DynamicUpdate 
@DynamicInsert
public class BaseTaxRate implements Serializable {

    private static final long serialVersionUID = 4040788502963084203L;
    
	// Fields
    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TAXRATE")
	@SequenceGenerator(name="SEQ_TAXRATE", sequenceName="SEQ_TAXRATE", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //主键id

    @Column(name = "CURRENCY_TYPE", length = 5)
	private String currencyType;	//币种 （0人民币 1美元 2日元。。。。。。。）
   
    @Column(name = "EXCHANGE_RATE")
	private Double exchangeRate;//汇率
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "THE_DATE")
	private Date theDate;//日期
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "REPAIR_DATE")
	private Date repairDate;//维护日期


	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getCurrencyType() {
		return this.currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	
	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	

	public Date getTheDate() {
		return this.theDate;
	}

	public void setTheDate(Date theDate) {
		this.theDate = theDate;
	}
	

	public Date getRepairDate() {
		return this.repairDate;
	}

	public void setRepairDate(Date repairDate) {
		this.repairDate = repairDate;
	}
}