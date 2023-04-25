/**
 * 
 */
package com.haiersoft.ccli.remoting.hand.invoice.entity;

import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "BASE_INVOICE_DETAIL")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class BaseInvoiceDetail implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1173551691642375682L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INVOICE_DETAIL")
	@SequenceGenerator(name="SEQ_INVOICE_DETAIL", sequenceName="SEQ_INVOICE_DETAIL", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	@Column(name = "SOURCE_SYS_BILLID")
	private String sourceSysBillID;
	@Column(name = "FM_NAME")
	private String fmName;
	@Column(name = "model")
	private String model;
	@Column(name = "unit")
	private String unit;
	@Column(name = "TAX_RATE")
	private Double taxRate;
	@Column(name = "NO_TAX_PICE")
	private Double noTaxPice;
	@Column(name = "NO_TAX_AMOUNT")
	private Double noTaxAmount;
	@Column(name = "TAX_AMOUNT")
	private Double taxAmount;
	@Column(name = "TAX")
	private Double tax;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	
	public BaseInvoiceDetail() {
		super();
	}
	
	public BaseInvoiceDetail(Integer id, String sourceSysBillID, String fmName, String model, String unit,
			Double taxRate, Double noTaxPice, Double noTaxAmount, Double taxAmount, Double tax, Date createDate) {
		super();
		this.id = id;
		this.sourceSysBillID = sourceSysBillID;
		this.fmName = fmName;
		this.model = model;
		this.unit = unit;
		this.taxRate = taxRate;
		this.noTaxPice = noTaxPice;
		this.noTaxAmount = noTaxAmount;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.createDate = createDate;
	}
	///////////////////////////////////////////////////////////////////
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSourceSysBillID() {
		return sourceSysBillID;
	}
	public void setSourceSysBillID(String sourceSysBillID) {
		this.sourceSysBillID = sourceSysBillID;
	}
	public String getFmName() {
		return fmName;
	}
	public void setFmName(String fmName) {
		this.fmName = fmName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	public Double getNoTaxPice() {
		return noTaxPice;
	}
	public void setNoTaxPice(Double noTaxPice) {
		this.noTaxPice = noTaxPice;
	}
	public Double getNoTaxAmount() {
		return noTaxAmount;
	}
	public void setNoTaxAmount(Double noTaxAmount) {
		this.noTaxAmount = noTaxAmount;
	}
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
