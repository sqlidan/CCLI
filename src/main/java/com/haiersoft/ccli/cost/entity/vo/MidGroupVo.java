package com.haiersoft.ccli.cost.entity.vo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
public class MidGroupVo  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String origBizId; //原始业务编号  codeNum  完

    private String origCostId; //	原始费用编号

    private String costCode; //费目代码code 完

    private String unit; //计量单位

    private BigDecimal unitPrice; //	单价 完

    private BigDecimal quantity; //	数量 完

    private BigDecimal origAmount; //	原始金额

    private BigDecimal adjAmount; //	调整金额

    private BigDecimal actAmount; //实际金额 完

    private String currency; //	币种

    private BigDecimal exchRate; //汇率 2

    private String direction; //	方向 R:应收 P:应付

    private String settleObjectCode; //结算单位code

    private String settleObjectName; //结算单位名称（跨区查验用）

    private String customerCode; //客户编码code，供应链必填，其他系统忽略 2

    private String supplierCode; //供应商编码code，供应链必填，其他系统忽略 2

    private String invoiceCode; //开票费目code 2

    private String costCenterCode; //	成本中心code

    private String taxRate; //税率 0, 3, 6, 9, 10 ,13等 完

    private String isCash; //	现结标志 0：非现结 1：现结

    private String bookTime; //	记账时间

    private String operator; //	操作员code（必须在单点登录系统存在）

    private String isEstimate; //	暂估标志 0：非暂估（默认） 1：暂估 2

    private String type; //	对账标志 0：未对账 1：已对账  完

    private String orgCode; //组织编码（由财务中台分配）/
    //到这
    private String categoryName; //	商品类别名称  2

    private String costClassifyCode; //	费目类别编码（如果为空，默认为费目代码） 2   财务

    private BigDecimal carryoverAmount; //	结转成本（供应链货款使用） 2

    private BigDecimal carryoverQuantity; //结转数量（供应链货款使用） 2

    private String isDue; //是否尾款（供应链货款使用, 0：否 1：是） 2

    private String isDeposit; //是否押金（0：否 1：是） 2    先付 后退  1

    private String contractNo; //	合同编号 2

    private Date payoffTime; //	收（付）款时间 2  审批通过之后  进行记录  完

    private String payment; //支付方式（从数据字典”payment”取值） 2  you

    private String isBank; //	是否为银行借还款 2

    private String invoiceNum; //	发票号码，多个用英文逗号隔开 2  财务中台返回 发票


    private String midGroupStatic;//上传状态

    public MidGroupVo(String origBizId, String origCostId, String costCode, String unit, BigDecimal unitPrice, BigDecimal quantity, BigDecimal origAmount, BigDecimal adjAmount, BigDecimal actAmount, String currency, BigDecimal exchRate, String direction, String settleObjectCode, String settleObjectName, String customerCode, String supplierCode, String invoiceCode, String costCenterCode, String taxRate, String isCash, String bookTime, String operator, String isEstimate, String type, String orgCode, String categoryName, String costClassifyCode, BigDecimal carryoverAmount, BigDecimal carryoverQuantity, String isDue, String isDeposit, String contractNo, Date payoffTime, String payment, String isBank, String invoiceNum, Long id, String midGroupStatic) {
        this.origBizId = origBizId;
        this.origCostId = origCostId;
        this.costCode = costCode;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.origAmount = origAmount;
        this.adjAmount = adjAmount;
        this.actAmount = actAmount;
        this.currency = currency;
        this.exchRate = exchRate;
        this.direction = direction;
        this.settleObjectCode = settleObjectCode;
        this.settleObjectName = settleObjectName;
        this.customerCode = customerCode;
        this.supplierCode = supplierCode;
        this.invoiceCode = invoiceCode;
        this.costCenterCode = costCenterCode;
        this.taxRate = taxRate;
        this.isCash = isCash;
        this.bookTime = bookTime;
        this.operator = operator;
        this.isEstimate = isEstimate;
        this.type = type;
        this.orgCode = orgCode;
        this.categoryName = categoryName;
        this.costClassifyCode = costClassifyCode;
        this.carryoverAmount = carryoverAmount;
        this.carryoverQuantity = carryoverQuantity;
        this.isDue = isDue;
        this.isDeposit = isDeposit;
        this.contractNo = contractNo;
        this.payoffTime = payoffTime;
        this.payment = payment;
        this.isBank = isBank;
        this.invoiceNum = invoiceNum;
        this.id = id;
        this.midGroupStatic = midGroupStatic;
    }

    public MidGroupVo() {
    }

    public String getOrigBizId() {
        return origBizId;
    }

    public void setOrigBizId(String origBizId) {
        this.origBizId = origBizId;
    }

    public String getOrigCostId() {
        return origCostId;
    }

    public void setOrigCostId(String origCostId) {
        this.origCostId = origCostId;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getOrigAmount() {
        return origAmount;
    }

    public void setOrigAmount(BigDecimal origAmount) {
        this.origAmount = origAmount;
    }

    public BigDecimal getAdjAmount() {
        return adjAmount;
    }

    public void setAdjAmount(BigDecimal adjAmount) {
        this.adjAmount = adjAmount;
    }

    public BigDecimal getActAmount() {
        return actAmount;
    }

    public void setActAmount(BigDecimal actAmount) {
        this.actAmount = actAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchRate() {
        return exchRate;
    }

    public void setExchRate(BigDecimal exchRate) {
        this.exchRate = exchRate;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getSettleObjectName() {
        return settleObjectName;
    }

    public void setSettleObjectName(String settleObjectName) {
        this.settleObjectName = settleObjectName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getCostCenterCode() {
        return costCenterCode;
    }

    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getIsCash() {
        return isCash;
    }

    public void setIsCash(String isCash) {
        this.isCash = isCash;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getIsEstimate() {
        return isEstimate;
    }

    public void setIsEstimate(String isEstimate) {
        this.isEstimate = isEstimate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCostClassifyCode() {
        return costClassifyCode;
    }

    public void setCostClassifyCode(String costClassifyCode) {
        this.costClassifyCode = costClassifyCode;
    }

    public BigDecimal getCarryoverAmount() {
        return carryoverAmount;
    }

    public void setCarryoverAmount(BigDecimal carryoverAmount) {
        this.carryoverAmount = carryoverAmount;
    }

    public BigDecimal getCarryoverQuantity() {
        return carryoverQuantity;
    }

    public void setCarryoverQuantity(BigDecimal carryoverQuantity) {
        this.carryoverQuantity = carryoverQuantity;
    }

    public String getIsDue() {
        return isDue;
    }

    public void setIsDue(String isDue) {
        this.isDue = isDue;
    }

    public String getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(String isDeposit) {
        this.isDeposit = isDeposit;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Date getPayoffTime() {
        return payoffTime;
    }

    public void setPayoffTime(Date payoffTime) {
        this.payoffTime = payoffTime;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getIsBank() {
        return isBank;
    }

    public void setIsBank(String isBank) {
        this.isBank = isBank;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMidGroupStatic() {
        return midGroupStatic;
    }

    public void setMidGroupStatic(String midGroupStatic) {
        this.midGroupStatic = midGroupStatic;
    }

    @Override
    public String toString() {
        return "MidGroupVo{" +
                "origBizId='" + origBizId + '\'' +
                ", origCostId='" + origCostId + '\'' +
                ", costCode='" + costCode + '\'' +
                ", unit='" + unit + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", origAmount=" + origAmount +
                ", adjAmount=" + adjAmount +
                ", actAmount=" + actAmount +
                ", currency='" + currency + '\'' +
                ", exchRate=" + exchRate +
                ", direction='" + direction + '\'' +
                ", settleObjectCode='" + settleObjectCode + '\'' +
                ", settleObjectName='" + settleObjectName + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", supplierCode='" + supplierCode + '\'' +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", costCenterCode='" + costCenterCode + '\'' +
                ", taxRate='" + taxRate + '\'' +
                ", isCash='" + isCash + '\'' +
                ", bookTime='" + bookTime + '\'' +
                ", operator='" + operator + '\'' +
                ", isEstimate='" + isEstimate + '\'' +
                ", type='" + type + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", costClassifyCode='" + costClassifyCode + '\'' +
                ", carryoverAmount=" + carryoverAmount +
                ", carryoverQuantity=" + carryoverQuantity +
                ", isDue='" + isDue + '\'' +
                ", isDeposit='" + isDeposit + '\'' +
                ", contractNo='" + contractNo + '\'' +
                ", payoffTime=" + payoffTime +
                ", payment='" + payment + '\'' +
                ", isBank='" + isBank + '\'' +
                ", invoiceNum='" + invoiceNum + '\'' +
                ", id=" + id +
                ", midGroupStatic='" + midGroupStatic + '\'' +
                '}';
    }


}
