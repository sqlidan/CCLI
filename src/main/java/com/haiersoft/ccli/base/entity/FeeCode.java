package com.haiersoft.ccli.base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
/**
 * 费目entity
 * @author ty
 * @date 2015年1月13日
 */
@Entity
@Table(name = "BASE_EXPENSE_CATEGORY_INFO")
@DynamicUpdate 
@DynamicInsert
public class FeeCode implements Serializable {

    private static final long serialVersionUID = 4040788502963084203L;
    
	// Fields
    //主键id
	private Integer id;
	//费目代码
	private String code;
	//费目中文名
	private String nameC;
	//费目英文名
	private String nameE;
	//发票名
	private String nameInvoice;
	//基础价格
	private Double priceBase;
	//计量单位 (1按箱，2按毛重，3按净重，4按托)
	private Integer units;
	//条件属性：1sku，2净重，3毛重 ，4按天
	private String termAttribute;
	//币种 (0人民币 1美元 2日元........)
	private Integer currencyType;
	//买方承担（0不承担1承担）
	private Integer buyBill;
	//卖方承担（0不承担1承担）
	private Integer sellBill;
	//备注
	private String remark;
	//是否付款单费目(0否，1是)
	private String fukuandan;
	//费用类别  （1其他 2仓储费 3出入库费 4分拣费 5在库分拣费 6人工装卸费 7缠膜费 8打包费21内标签22外标签23码托24装铁架25拆铁架）
	private Integer feeType;
	//最小限价 
	private Double minPrice;
	//最大限价 
	private Double maxPrice;
	/*********************核销金蝶接口字段*******************************/
	
	private String ysCode;//应收编码
	
	private String yfCode;//应付编码
	
	private String ysInfo;//应收摘要
	
	private String yfInfo;//应付摘要
	
	private String jdNum;//金蝶科目编码
	// Constructors

	/** default constructor */
	public FeeCode() {
	}

	/** minimal constructor */
	public FeeCode(String code, String nameE) {
		this.code = code;
		this.nameE = nameE;
	}

	/** full constructor */
	public FeeCode(String code, String nameC,String nameE, String nameInvoice,
			Float priceBase, String units, String currencyType, String buyBill, String sellBill,
			String remark, String feeType) {
		this.code = code;
		this.nameC = nameC;
		this.nameE = nameE;
		this.nameInvoice = nameInvoice;
		
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FEECODE")
	@SequenceGenerator(name="SEQ_FEECODE", sequenceName="SEQ_FEECODE", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	@Column(name = "CODE", unique = true, nullable = false, length = 6)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME_C", length = 20)
	public String getNameC() {
		return this.nameC;
	}

	public void setNameC(String nameC) {
		this.nameC = nameC;
	}
	
	@Column(name = "NAME_E", length = 20)
	public String getNameE() {
		return this.nameE;
	}

	public void setNameE(String nameE) {
		this.nameE = nameE;
	}

	@Column(name = "NAME_INVOICE", length = 20)
	public String getNameInvoice() {
		return this.nameInvoice;
	}
	
	public void setNameInvoice(String nameInvoice) {
		this.nameInvoice = nameInvoice;
	}

	@Column(name = "TERM_ATTRIBUTE")	
	public String getTermAttribute() {
		return termAttribute;
	}

	public void setTermAttribute(String termAttribute) {
		this.termAttribute = termAttribute;
	}

	@Column(name = "UNITS", length = 4)
	public Integer getUnits() {
		return this.units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	@Column(name = "CURRENCY_TYPE", length = 5)
	public Integer getCurrencyType() {
		return this.currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	@Column(name = "BUY_BILL", length = 1)
	public Integer getBuyBill() {
		return this.buyBill;
	}

	public void setBuyBill(Integer buyBill) {
		this.buyBill = buyBill;
	}

	@Column(name = "SELL_BILL", length = 1)
	public Integer getSellBill() {
		return this.sellBill;
	}

	public void setSellBill(Integer sellBill) {
		this.sellBill = sellBill;
	}

	@Column(name = "FEE_TYPE", length = 1)
	public Integer getFeeType() {
		return this.feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	
	@Column(name = "REMARK", length = 40)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "PRICE_BASE", length = 12)
	public Double getPriceBase() {
		return this.priceBase;
	}

	public void setPriceBase(Double priceBase) {
		this.priceBase = priceBase;
	}
	@Column(name = "YSCODE")
	public String getYsCode() {
		return ysCode;
	}

	public void setYsCode(String ysCode) {
		this.ysCode = ysCode;
	}
	@Column(name = "YFCODE")
	public String getYfCode() {
		return yfCode;
	}
	
	public void setYfCode(String yfCode) {
		this.yfCode = yfCode;
	}
	@Column(name = "YSINFO")
	public String getYsInfo() {
		return ysInfo;
	}
	
	public void setYsInfo(String ysInfo) {
		this.ysInfo = ysInfo;
	}
	@Column(name = "YFINFO")
	public String getYfInfo() {
		return yfInfo;
	}

	public void setYfInfo(String yfInfo) {
		this.yfInfo = yfInfo;
	}

	@Column(name = "JDNUM")
	public String getJdNum() {
		return jdNum;
	}

	public void setJdNum(String jdNum) {
		this.jdNum = jdNum;
	}

	@Column(name = "MIN_PRICE")
	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	@Column(name = "MAX_PRICE")
	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	@Column(name = "FUKUANDAN")
	public String getFukuandan() {
		return fukuandan;
	}

	public void setFukuandan(String fukuandan) {
		this.fukuandan = fukuandan;
	}
	
	
}