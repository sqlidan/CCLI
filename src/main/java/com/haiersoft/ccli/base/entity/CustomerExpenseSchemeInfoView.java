package com.haiersoft.ccli.base.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 客户费用方案明细导出的费用方案子表数据。
 */
public class CustomerExpenseSchemeInfoView {

    @Excel(name = "方案编号")
    private String schemeNum;
    @Excel(name = "方案名称")
    private String schemeName;
    @Excel(name = "方案客户")
    private String customsName;
    @Excel(name = "合同号")
    private String contractNum;
    @Excel(name = "合同客户")
    private String contractClientName;
    @Excel(name = "费目代码")
    private String feeCode;
    @Excel(name = "费目")
    private String feeName;
    @Excel(name = "币种")
    private String currency;
    @Excel(name = "计费方式")
    private String billing;
    @Excel(name = "单价")
    private Double unit;
    @Excel(name = "费用类别")
    private String feeType;
    @Excel(name = "条件属性")
    private String termAttribute;
    @Excel(name = "下限")
    private Double minPrice;
    @Excel(name = "上限")
    private Double maxPrice;
    @Excel(name = "档位代码")
    private String gearCode;
    @Excel(name = "档位说明")
    private String gearExp;
    @Excel(name = "是否垫付")
    private Integer ifPay;
    @Excel(name = "备注")
    private String remark;

    public String getSchemeNum() { return schemeNum; }
    public void setSchemeNum(String schemeNum) { this.schemeNum = schemeNum; }
    public String getSchemeName() { return schemeName; }
    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }
    public String getCustomsName() { return customsName; }
    public void setCustomsName(String customsName) { this.customsName = customsName; }
    public String getContractNum() { return contractNum; }
    public void setContractNum(String contractNum) { this.contractNum = contractNum; }
    public String getContractClientName() { return contractClientName; }
    public void setContractClientName(String contractClientName) { this.contractClientName = contractClientName; }
    public String getFeeCode() { return feeCode; }
    public void setFeeCode(String feeCode) { this.feeCode = feeCode; }
    public String getFeeName() { return feeName; }
    public void setFeeName(String feeName) { this.feeName = feeName; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getBilling() { return billing; }
    public void setBilling(String billing) { this.billing = billing; }
    public Double getUnit() { return unit; }
    public void setUnit(Double unit) { this.unit = unit; }
    public String getFeeType() { return feeType; }
    public void setFeeType(String feeType) { this.feeType = feeType; }
    public String getTermAttribute() { return termAttribute; }
    public void setTermAttribute(String termAttribute) { this.termAttribute = termAttribute; }
    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }
    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }
    public String getGearCode() { return gearCode; }
    public void setGearCode(String gearCode) { this.gearCode = gearCode; }
    public String getGearExp() { return gearExp; }
    public void setGearExp(String gearExp) { this.gearExp = gearExp; }
    public Integer getIfPay() { return ifPay; }
    public void setIfPay(Integer ifPay) { this.ifPay = ifPay; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
