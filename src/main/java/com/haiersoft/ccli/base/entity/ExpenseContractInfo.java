package com.haiersoft.ccli.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseContractInfo
 * @Description: 合同明细实体
 * @date 2016年2月25日 上午9:14:49
 */
@Entity
@Table(name = "BASE_EXPENSE_CONTRACT_INFO")
@DynamicUpdate
@DynamicInsert
public class ExpenseContractInfo implements Serializable{

    private static final long serialVersionUID = 5700414803693351779L;
	
    @Id
    @Column(name = "ID", unique = true, nullable = false)
	private String id;//十位合同号+SEQ四位序号 
	
    @Column(name = "CONTRACT_NUM")
    @Excel(name="合同号")
	private String contractNum;//合同号 
	
    @Column(name = "EXPENSE_CODE")
    @Excel(name="费目")
	private String expenseCode;//费目 
	
    @Column(name = "PRICE")
    @Excel(name="价格")
	private Double price;//价格 
	
    @Column(name = "CURRENCY_TYPE")
    @Excel(name = "币种", replace = {"人民币_0", "美元_1","日元_2","阿尔及利亚第纳尔_201"})
	private String currencyType;//币种 
	
    @Column(name = "CARGO_NAME")
    @Excel(name="商品名称")
	private String cargoName;//商品名称  费目名称
	
    @Column(name = "SPACE")
    @Excel(name="规格")
	private String space;//规格 
	
    @Column(name = "BILL_UNIT")
    @Excel(name="计量单位", replace = {"按件数_1", "按毛重（吨）_2","按净重（吨）_3","按托_4","净重（千克）_5","毛重（千克）_6","体积（立方米）_7","票_8","箱_9","件/天_10","净重（千克）/天_11","净重（吨）/天_12","毛重（吨）/天_13","毛重（千克）/天_14","体积（立方米）/天_15","票/天_16","张_17","次_18"})
	private String billUnit;//计量单位 
	
    @Column(name = "FEE_TYPE")
    @Excel(name="费用类别 ", replace = {"其他_1", "仓储费_2","出入库费_3","分拣费_4","应付分拣费_5","人工装卸费_6","缠膜费_7","打包费_8"})
  	private String feeType;//费用类别  （1其他 2仓储费 3出入库费 4分拣费 5应付分拣费 6人工装卸费 7缠膜费 8打包费）
    
    @Column(name = "TERM_ATTRIBUTE")
    @Excel(name="条件属性", replace = {"sku_1", "净重_2","毛重_3"})
	private String termAttribute;//条件属性：1sku，2净重，3毛重 ，4按天
    
    @Column(name = "MIN_PRICE")
    @Excel(name="最小限价")
	private Double minPrice;//最小限价 
	
    @Column(name = "MAX_PRICE")
    @Excel(name="最大限价")
	private Double maxPrice;//最大限价 
    
    @Column(name = "GEAR_CODE")
	private String gearCode;//档位代码：默认为N 1-5 5-10  
	
    @Column(name = "GEAR_EXP")
	private String gearExp;//档位说明
    
    @Column(name = "ON_SALE")
   	private Double onSale;//折扣  默认1
    
    @Column(name = "REMARK")
	private String remark;//备注
	
    @Column(name = "OPERATE_PERSON")
	private String operatePerson;//操作人员 
	
    @Column(name = "OPERATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date operateTime;//操作时间 
	
    @Column(name = "UPDATE_PERSON")
 	private String updatePerson;//修改人
    
    @Column(name = "UPDATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date updateTime;//修改时间
	
    @Column(name = "REMARK1")
	private String remark1;//备注1
    /////////////////////////////////页面属性配置/////////////////////////////
    @Transient
    private String unit;//单位值
    @Transient
    private String term;//条件属性值
    
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Double getOnSale() {
		return onSale;
	}

	public void setOnSale(Double onSale) {
		this.onSale = onSale;
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

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getExpenseCode() {
		return expenseCode;
	}

	public void setExpenseCode(String expenseCode) {
		this.expenseCode = expenseCode;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getBillUnit() {
		return billUnit;
	}

	public void setBillUnit(String billUnit) {
		this.billUnit = billUnit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperatePerson() {
		return operatePerson;
	}

	public void setOperatePerson(String operatePerson) {
		this.operatePerson = operatePerson;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getUpdatePerson() {
		return updatePerson;
	}

	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	
    
	
}
