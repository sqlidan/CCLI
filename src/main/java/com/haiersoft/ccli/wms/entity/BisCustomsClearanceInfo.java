package com.haiersoft.ccli.wms.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * BisCustomsDeclarationInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_CUSTOMS_CLEARANCE_INFO")
public class BisCustomsClearanceInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5155824809351547579L;

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUSTOMS_INFO")
	@SequenceGenerator(name="SEQ_CUSTOMS_INFO", sequenceName="SEQ_CUSTOMS_INFO", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //ID
	
	@Column(name = "CUS_ID")
	private String cusId; //主表ID
	
	@Column(name = "COMMODITY_CODE")
	private String commodityCode; //商品编码
	
	@Column(name = "COMMODITY_NAME")
	private String commodityName; //商品名称
	
	@Column(name = "LATIN_NAME")
	private String latinName; //拉丁文名
	
	@Column(name = "SPECIFICATION")
	private String specification; //规格
	
	@Column(name = "NUM")
	private BigDecimal num; //数量
	
	@Column(name = "NET_WEIGHT")
	private BigDecimal netWeight; //净重
	
	@Column(name = "GROSS_WEIGHT")
	private BigDecimal grossWeight; //毛重
	
	@Column(name = "MONEY")
	private BigDecimal money; //金额
	
	@Column(name = "CURRENCY_VALUE")
	private String currencyValue; //币制  0：人民币  1：美元  2：日元 3：欧元 4：英镑

	@Column(name = "FIRM_NAME")
	private String firmName; //生产企业名称及注册号
	
	@Column(name = "PACKAGED_FORM")
	private String packagedForm; //包装形式
	
	@Column(name = "IF_WOODEN_PACKING")
	private String ifWoodenPacking; //有无木质包装

	@Column(name = "WOODEN_NO")
	private String woodenNo; //木托编号

	@Column(name = "ACCOUNT_BOOK")
	private String accountBook;

	public String getAccountBook() {
		return accountBook;
	}

	public void setAccountBook(String accountBook) {
		this.accountBook = accountBook;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getCommodityCode() {
		return commodityCode;
	}

	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getLatinName() {
		return latinName;
	}

	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public BigDecimal getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(BigDecimal netWeight) {
		this.netWeight = netWeight;
	}

	public BigDecimal getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(BigDecimal grossWeight) {
		this.grossWeight = grossWeight;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(String currencyValue) {
		this.currencyValue = currencyValue;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getPackagedForm() {
		return packagedForm;
	}

	public void setPackagedForm(String packagedForm) {
		this.packagedForm = packagedForm;
	}

	public String getIfWoodenPacking() {
		return ifWoodenPacking;
	}

	public void setIfWoodenPacking(String ifWoodenPacking) {
		this.ifWoodenPacking = ifWoodenPacking;
	}

	public String getWoodenNo() {
		return woodenNo;
	}

	public void setWoodenNo(String woodenNo) {
		this.woodenNo = woodenNo;
	}
}