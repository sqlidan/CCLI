package com.haiersoft.ccli.base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseSchemeInfo
 * @Description: 费用方案明细实体类
 * @date 2016年2月28日 下午3:06:02
 */
@Entity
@Table(name = "BASE_EXPENSE_SCHEME_INFO")
@DynamicUpdate
@DynamicInsert
public class ExpenseSchemeInfo implements Serializable{

    private static final long serialVersionUID = 130193930506347965L;
	
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    private String id;//8位SEQ按序成生ID
    
    @Column(name = "SCHEME_NUM")
    private String schemeNum;//方案编号
    
    @Column(name = "FEE_CODE")
    private String feeCode;//费目代码
    
    @Column(name = "FEE_NAME")
    @Excel(name = "费目")
    private String feeName;//费目名称
	
    @Column(name = "CURRENCY")
    @Excel(name = "币种", replace = {"人民币_0", "美元_1", "日元_2"})
	private String currency;//币种默认：RMB币别
	
    @Column(name = "BILLING")
    @Excel(name = "计费方式", replace = {"按箱_1", "按毛重_2", "按净重_3", "按托_4"})
	private String billing;//计费方式：1:按件数 2:按毛重（吨） 3：按净重（吨） 4：按托 5：净重（千克） 6：毛重（千克） 7.体积（立方米） 8：票 9：箱10：件/天 11：净重（千克/天）12：净重（吨）/天 13：毛重（吨）/天 14：毛重（千克/天）15：体积（立方米）/天 16：票/天 17：张 18：次
  	    
	
    @Column(name = "UNIT")
    @Excel(name = "价格")
	private Double unit;//单价 
	
    @Column(name = "GEAR_CODE")
    @Excel(name = "档位代码")
	private String gearCode;//档位代码：默认为N 1-5 5-10  
	
    @Column(name = "GEAR_EXP")
    @Excel(name = "档位说明")
	private String gearExp;//档位说明
	
    @Column(name = "TERM_ATTRIBUTE")
    @Excel(name = "条件属性", replace = {"按SKU_1", "按净重_2", "按毛重_3"})
	private String termAttribute;//条件属性：1sku，2净重，3毛重 
    
    @Column(name = "FEE_TYPE")
   	private String feeType;//费用类别  （1其他 2仓储费 3出入库费 4分拣费 5在库分拣费 6人工装卸费 7缠膜费 8打包费）
	
    @Column(name = "MIN_PRICE")
    @Excel(name = "下限")
	private Double minPrice;//最小限价 
	
    @Column(name = "MAX_PRICE")
    @Excel(name = "上限")
	private Double maxPrice;//最大限价 
	
    @Column(name = "REMARK")
	private String remark;//备注

    @Column(name = "IF_PAY")
   	private Integer ifPay;//是否垫付 0：否  1：是
    
    @Transient
    private String currencyName;//货币名称
    @Transient
    private String unitName;//计算方式名称
    @Transient
    private String termattributeName;//条件属性名称
    
    public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getTermattributeName() {
		return termattributeName;
	}

	public void setTermattributeName(String termattributeName) {
		this.termattributeName = termattributeName;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchemeNum() {
		return schemeNum;
	}

	public void setSchemeNum(String schemeNum) {
		this.schemeNum = schemeNum;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBilling() {
		return billing;
	}

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public Double getUnit() {
		return unit;
	}

	public void setUnit(Double unit) {
		this.unit = unit;
	}

	public String getGearCode() {
		return gearCode;
	}

	public void setGearCode(String gearCode) {
		this.gearCode = gearCode;
	}

	public String getGearExp() {
		return gearExp;
	}

	public void setGearExp(String gearExp) {
		this.gearExp = gearExp;
	}

	public String getTermAttribute() {
		return termAttribute;
	}

	public void setTermAttribute(String termAttribute) {
		this.termAttribute = termAttribute;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIfPay() {
		return ifPay;
	}

	public void setIfPay(Integer ifPay) {
		this.ifPay = ifPay;
	}

	
}
