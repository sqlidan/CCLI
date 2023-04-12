package com.haiersoft.ccli.cost.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *
 * @author Connor.M
 * @ClassName: BisStandingBook
 * @Description: 费用台账  实体类
 * @date 2016年3月31日 下午1:24:15
 */
@Entity
@Table(name = "BIS_STANDING_BOOK")//,uniqueConstraints = {@UniqueConstraint(columnNames={"CUSTOMS_NUM","BILL_NUM","LINK_ID","FEE_PLAN","IF_RECEIVE","FEE_CODE"})})
@DynamicUpdate
@DynamicInsert
public class BisStandingBook implements java.io.Serializable {

	private static final long serialVersionUID = 5765160688826212763L;

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STANDING_BOOK")
	//@SequenceGenerator(name = "SEQ_STANDING_BOOK", sequenceName = "SEQ_STANDING_BOOK", allocationSize = 1)
	@Column(name = "STANDING_NUM", unique = true, nullable = false)
	private Integer standingNum;//台账编号

	@Column(name = "CUSTOMS_NUM")
	private String customsNum; //客户编号

	@Column(name = "CUSTOMS_NAME")
	private String customsName; //客户名称

	@Column(name = "BILL_NUM")
	private String billNum; //提单号

	@Column(name = "LINK_ID")
	private String linkId;//联系单号

	@Column(name = "CRK_SIGN")
	private Integer crkSign; //出入库标志 :入库 1  出库2  货转3

	@Column(name = "STORAGE_DTAE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date storageDtae;  //入库时间

	@Column(name = "FEE_CODE")
	private String feeCode;  //费目代码

	@Column(name = "FEE_NAME")
	private String feeName;  //费目名称

	@Column(name = "FEE_PLAN")
	private String feePlan;  //费用方案

	@Column(name = "ASN")
	private String asn;  //对装卸队费使用ASN

	@Column(name = "IF_RECEIVE")
	private Integer ifReceive; //应收应付  1应收 2应付

	@Column(name = "NUM")
	private Double num;   //默认：1   数量

	@Column(name = "PRICE")
	private Double price;//单价

	@Column(name = "RECEIVE_AMOUNT")
	private Double receiveAmount;  //应收金额 : 单价金额*数量

	@Column(name = "REAL_AMOUNT")
	private Double realAmount;  //实收金额

	@Column(name = "CHARGE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date chargeDate; //计费时间

	@Column(name = "COST_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date costDate;  //费用产生事件

	@Column(name = "FILL_SIGN")
	private Integer fillSign;//充补标志  0未补充 1已补充

	@Column(name = "FARE_PERSON")
	private String farePerson; //补票人

	@Column(name = "FARE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date fareDate; //补票时间

	@Column(name = "CURRENCY")
	private String currency;  //币种

	@Column(name = "INPUT_PERSON_ID")
	private String inputPersonId; //录入员ID

	@Column(name = "INPUT_PERSON")
	private String inputPerson; //录入员

	@Column(name = "INPUT_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date inputDate;  //录入时间

	@Column(name = "UPDATE_PERSON")
	private String updatePerson; //修改人

	@Column(name = "UPDATE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date updateDate;  //修改时间

	@Column(name = "COLLECT_INV_NUM")
	private String collectInvNum;  //代收发票号

	@Column(name = "SALESMAN")
	private String salesman; //业务员

	@Column(name = "EXCHANGE_RATE")
	private Double exchangeRate; //汇率

	@Column(name = "EXAMINE_SIGN")
	private Integer examineSign=0; //审核标志  默认：0 未审核 1 已审核

	@Column(name = "EXAMINE_PERSON")
	private String examinePerson; //审核人

	@Column(name = "EXAMINE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date examineDate;  //审核时间

	@Column(name = "BIS_TYPE")
	private String bisType;  //代码业务类型(1其他5在库分拣6人工装卸7缠膜8打包21内标签22外标签23码托24装铁架25拆铁架)

	@Column(name = "SHOULD_RMB")
	private Double shouldRmb;  //应收金额RMB ： 应收金额*汇率

	@Column(name = "REAL_RMB")
	private Double realRmb; //实收金额RMB ： 实收金额*汇率

	@Column(name = "ORG")
	private String org; // 公司 （代码）

	@Column(name = "DEPARTMENT")
	private String department; // 部门（代码）

	@Column(name = "RECONCILE_NUM")
	private String reconcileNum;  //对帐编号

	@Column(name = "RECONCILE_SIGN")
	private Integer reconcileSign=0;//对帐标志 0未对账 1已对账

	@Column(name = "RECONCILE_PERSON")
	private String reconcilePerson; //对帐人

	@Column(name = "RECONCILE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date reconcileDate; //对帐时间

	@Column(name = "SETTLE_SIGN")
	private Integer settleSign=0; //结算标志 0未结算 1已结算

	@Column(name = "SETTLE_PERSON")
	private String settlePerson; //结算人

	@Column(name = "SETTLE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date settleDate; //结算时间

	@Column(name = "TAX_RATE")
	private Double taxRate; //税率 0，6，11，13，17

	@Column(name = "INVOICE_CODE")
	private String invoiceCode; //发票(代码)

	@Column(name = "INVOICE_NUM")
	private String invoiceNum; //发票号

	@Column(name = "SPLIT_SIGN")
	private Integer splitSign=0; //分割标志 0未分割 1已分割

	@Column(name = "REMARK")
	private String remark; //备注

	@Column(name = "CONTACT_TYPE")
    private Integer contactType; //联系单类型（1入库2出库3货转）

	@Column(name = "BOX_SIGN")
    private Integer boxSign=0; //倒箱标记（1：倒箱 0：其他）

	@Column(name = "SHARE_SIGN")
    private Integer shareSign=0; //分摊标记（1:分摊 0：不分摊）

	@Column(name = "PAY_SIGN")
	private String paySign="0"; //垫付标记 0未垫付，1已垫付

	@Column(name = "CHARGE_SIGN")
	private String chargeSign="0";//收款标记 0未收，1已收

	@Column(name = "CHARGE_PERSON")
	private String chargePerson;//收款人

	@Column(name = "CHARGE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date chargeTime;//收款时间

	@Column(name = "BILL_DATE")
	@JsonFormat(pattern = "yyyy-MM", timezone = "GMT+08:00")
    private Date billDate; //账单年月

	@Column(name = "SHARE_LINK")
	private String shareLink;//分摊关联联系单号，出库联系单

	@Column(name = "PAY_STANDING_NUM")
	private Integer payStandingNum;//垫付关联台账ID

	@Column(name = "PAY_ID")
	private Integer payId;//业务付款单明细ID

	@Column(name = "SKU")
	private String sku;//SKU

	@Column(name = "CHARGE_START_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date chargeStartDate;//计费开始日期

	@Column(name = "CHARGE_END_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date chargeEndDate;//计费结束日期

	@Column(name = "CHARGE_DAY")
	private Integer chargeDay;//计费天数

	@Column(name = "STORAGE_STATE")
	private String storageState;//存储费业务类型（ 1：入 2：出 3：在库4：货损）
	@Column(name = "STANDING_CODE")
	private String standingCode;//费用编号格式：一共16位；“LK”+8位年月日（YYYYMMDD）+6位sequence（不足6位前补0）

	@Column(name = "HXUSER")
	private String hxUser;//核销人
	@Column(name = "HXKEY")
	private String hxKey;//核销id
	@Column(name = "HXPZ")
	private String hxPz;//金蝶凭证
	@Column(name = "HXBZ")
	private Integer hxBz;//核销币种
	@Column(name = "HXSTATE")
	private Integer hxState;//核销状态 0，未核销，1已核销
	@Column(name = "HXTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date hxDate;//核销DATE
	@Column(name = "SCALE_ID")
	private Integer scaleId;//关联分摊id


    // 追加列表字段
	@Transient
	private String sDZId;//对账单好
	@Transient
	private String sLinkId;//联系单
	@Transient
	private String sIBillNum;//入提单
	@Transient
	private String sOBillNum;//出提单
	@Transient
	private String sBaoGuan;//报关单号
	@Transient
	private Integer nType;//1入，2出、3在库
	@Transient
	private String sFeePlan;//费用项
	@Transient
	private String sCustom;//客户  委托单位
	@Transient
	private String sInCarCode;//装车单
	@Transient
	private String straTime;//开始年月
	@Transient
	private String endTime;//结束年月
	@Transient
	private Integer isCX;//是否是查询
	@Transient
	private String payNum;//业务付款单号
	@Transient
	private Double sL;//数量
	@Transient
	private Double ysdj;//应收单价
	@Transient
	private Double yfdj;//应付单价
	@Transient
	private Double yS;//应收
	@Transient
	private Double yF;//应付
	@Transient
	private Double lR;//利润
	@Transient
	private Double bg;//报关
	@Transient
	private Double bj;//报检
	@Transient
	private Double sp;//审批
	@Transient
	private Double sjyh;//商检验货
	@Transient
	private Double hgyh;//海关验货
	@Transient
	private String crkType;//出入库类型
	@Transient
	private String gysName;//供应商
	@Transient
	private String reconcileType;//是否对账
	public Double getBg() {
		return bg;
	}

	public Integer getScaleId() {
		return scaleId;
	}

	public void setScaleId(Integer scaleId) {
		this.scaleId = scaleId;
	}

	public void setBg(Double bg) {
		this.bg = bg;
	}

	public Double getBj() {
		return bj;
	}

	public void setBj(Double bj) {
		this.bj = bj;
	}

	public Double getSp() {
		return sp;
	}

	public void setSp(Double sp) {
		this.sp = sp;
	}

	public Double getSjyh() {
		return sjyh;
	}

	public void setSjyh(Double sjyh) {
		this.sjyh = sjyh;
	}

	public Double getHgyh() {
		return hgyh;
	}

	public void setHgyh(Double hgyh) {
		this.hgyh = hgyh;
	}

	public String getCrkType() {
		return crkType;
	}

	public void setCrkType(String crkType) {
		this.crkType = crkType;
	}

	public String getGysName() {
		return gysName;
	}

	public void setGysName(String gysName) {
		this.gysName = gysName;
	}

	public String getReconcileType() {
		return reconcileType;
	}

	public void setReconcileType(String reconcileType) {
		this.reconcileType = reconcileType;
	}

	public Integer getPayStandingNum() {
		return payStandingNum;
	}

	public void setPayStandingNum(Integer payStandingNum) {
		this.payStandingNum = payStandingNum;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	public String getInputPersonId() {
		return inputPersonId;
	}

	public void setInputPersonId(String inputPersonId) {
		this.inputPersonId = inputPersonId;
	}

	public String getUpdatePerson() {
		return updatePerson;
	}

	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getStandingNum() {
		return standingNum;
	}

	public void setStandingNum(Integer standingNum) {
		this.standingNum = standingNum;
	}

	public String getChargeSign() {
		return chargeSign;
	}

	public void setChargeSign(String chargeSign) {
		this.chargeSign = chargeSign;
	}

	public String getChargePerson() {
		return chargePerson;
	}

	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}

	public Date getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(Date chargeTime) {
		this.chargeTime = chargeTime;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public String getCustomsNum() {
		return customsNum;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public void setCustomsNum(String customsNum) {
		this.customsNum = customsNum;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getCustomsName() {
		return customsName;
	}

	public void setCustomsName(String customsName) {
		this.customsName = customsName;
	}

	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getReconcilePerson() {
		return reconcilePerson;
	}

	public void setReconcilePerson(String reconcilePerson) {
		this.reconcilePerson = reconcilePerson;
	}

	public String getSettlePerson() {
		return settlePerson;
	}

	public void setSettlePerson(String settlePerson) {
		this.settlePerson = settlePerson;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public Integer getCrkSign() {
		return crkSign;
	}

	public void setCrkSign(Integer crkSign) {
		this.crkSign = crkSign;
	}

	public Date getStorageDtae() {
		return storageDtae;
	}

	public void setStorageDtae(Date storageDtae) {
		this.storageDtae = storageDtae;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getFeePlan() {
		return feePlan;
	}

	public void setFeePlan(String feePlan) {
		this.feePlan = feePlan;
	}

	public String getAsn() {
		return asn;
	}

	public void setAsn(String asn) {
		this.asn = asn;
	}

	public Integer getIfReceive() {
		return ifReceive;
	}

	public void setIfReceive(Integer ifReceive) {
		this.ifReceive = ifReceive;
	}

	public Double getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(Double receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public Double getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(Double realAmount) {
		this.realAmount = realAmount;
	}

	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	public Date getCostDate() {
		return costDate;
	}

	public void setCostDate(Date costDate) {
		this.costDate = costDate;
	}

	public Integer getFillSign() {
		return fillSign;
	}

	public void setFillSign(Integer fillSign) {
		this.fillSign = fillSign;
	}

	public String getFarePerson() {
		return farePerson;
	}

	public void setFarePerson(String farePerson) {
		this.farePerson = farePerson;
	}

	public Date getFareDate() {
		return fareDate;
	}

	public void setFareDate(Date fareDate) {
		this.fareDate = fareDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getInputPerson() {
		return inputPerson;
	}

	public void setInputPerson(String inputPerson) {
		this.inputPerson = inputPerson;
	}

	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	public String getCollectInvNum() {
		return collectInvNum;
	}

	public void setCollectInvNum(String collectInvNum) {
		this.collectInvNum = collectInvNum;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Integer getExamineSign() {
		return examineSign;
	}

	public void setExamineSign(Integer examineSign) {
		this.examineSign = examineSign;
	}

	public String getExaminePerson() {
		return examinePerson;
	}

	public void setExaminePerson(String examinePerson) {
		this.examinePerson = examinePerson;
	}

	public Date getExamineDate() {
		return examineDate;
	}

	public void setExamineDate(Date examineDate) {
		this.examineDate = examineDate;
	}

	public String getBisType() {
		return bisType;
	}

	public void setBisType(String bisType) {
		this.bisType = bisType;
	}

	public Double getShouldRmb() {
		return shouldRmb;
	}

	public void setShouldRmb(Double shouldRmb) {
		this.shouldRmb = shouldRmb;
	}

	public Double getRealRmb() {
		return realRmb;
	}

	public void setRealRmb(Double realRmb) {
		this.realRmb = realRmb;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getReconcileNum() {
		return reconcileNum;
	}

	public void setReconcileNum(String reconcileNum) {
		this.reconcileNum = reconcileNum;
	}

	public Integer getReconcileSign() {
		return reconcileSign;
	}

	public void setReconcileSign(Integer reconcileSign) {
		this.reconcileSign = reconcileSign;
	}

	public Date getReconcileDate() {
		return reconcileDate;
	}

	public void setReconcileDate(Date reconcileDate) {
		this.reconcileDate = reconcileDate;
	}

	public Integer getSettleSign() {
		return settleSign;
	}

	public void setSettleSign(Integer settleSign) {
		this.settleSign = settleSign;
	}

	public Date getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public Integer getSplitSign() {
		return splitSign;
	}

	public void setSplitSign(Integer splitSign) {
		this.splitSign = splitSign;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getContactType() {
		return contactType;
	}

	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}

	public Integer getBoxSign() {
		return boxSign;
	}

	public void setBoxSign(Integer boxSign) {
		this.boxSign = boxSign;
	}

	public Integer getShareSign() {
		return shareSign;
	}

	public void setShareSign(Integer shareSign) {
		this.shareSign = shareSign;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}


	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Date getChargeStartDate() {
		return chargeStartDate;
	}

	public void setChargeStartDate(Date chargeStartDate) {
		this.chargeStartDate = chargeStartDate;
	}

	public Date getChargeEndDate() {
		return chargeEndDate;
	}

	public void setChargeEndDate(Date chargeEndDate) {
		this.chargeEndDate = chargeEndDate;
	}

	public Integer getChargeDay() {
		return chargeDay;
	}

	public void setChargeDay(Integer chargeDay) {
		this.chargeDay = chargeDay;
	}

	public String getStorageState() {
		return storageState;
	}

	public void setStorageState(String storageState) {
		this.storageState = storageState;
	}

	public String getStandingCode() {
		return standingCode;
	}

	public void setStandingCode(String standingCode) {
		this.standingCode = standingCode;
	}


	public String getsLinkId() {
		return sLinkId;
	}

	public void setsLinkId(String sLinkId) {
		this.sLinkId = sLinkId;
	}

	public String getsIBillNum() {
		return sIBillNum;
	}

	public void setsIBillNum(String sIBillNum) {
		this.sIBillNum = sIBillNum;
	}

	public String getsOBillNum() {
		return sOBillNum;
	}

	public void setsOBillNum(String sOBillNum) {
		this.sOBillNum = sOBillNum;
	}

	public String getsBaoGuan() {
		return sBaoGuan;
	}

	public void setsBaoGuan(String sBaoGuan) {
		this.sBaoGuan = sBaoGuan;
	}

	public Integer getnType() {
		return nType;
	}

	public void setnType(Integer nType) {
		this.nType = nType;
	}

	public String getsFeePlan() {
		return sFeePlan;
	}

	public void setsFeePlan(String sFeePlan) {
		this.sFeePlan = sFeePlan;
	}

	public String getStraTime() {
		return straTime;
	}

	public void setStraTime(String straTime) {
		this.straTime = straTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getsCustom() {
		return sCustom;
	}

	public void setsCustom(String sCustom) {
		this.sCustom = sCustom;
	}

	public String getsInCarCode() {
		return sInCarCode;
	}

	public void setsInCarCode(String sInCarCode) {
		this.sInCarCode = sInCarCode;
	}

	public String getsDZId() {
		return sDZId;
	}

	public void setsDZId(String sDZId) {
		this.sDZId = sDZId;
	}

	public Integer getIsCX() {
		return isCX;
	}

	public void setIsCX(Integer isCX) {
		this.isCX = isCX;
	}

	public String getHxUser() {
		return hxUser;
	}

	public void setHxUser(String hxUser) {
		this.hxUser = hxUser;
	}

	public String getHxKey() {
		return hxKey;
	}

	public void setHxKey(String hxKey) {
		this.hxKey = hxKey;
	}

	public String getHxPz() {
		return hxPz;
	}

	public void setHxPz(String hxPz) {
		this.hxPz = hxPz;
	}

	public Integer getHxBz() {
		return hxBz;
	}

	public void setHxBz(Integer hxBz) {
		this.hxBz = hxBz;
	}

	public Integer getHxState() {
		return hxState;
	}

	public void setHxState(Integer hxState) {
		this.hxState = hxState;
	}

	public Date getHxDate() {
		return hxDate;
	}

	public void setHxDate(Date hxDate) {
		this.hxDate = hxDate;
	}

	public String getPayNum() {
		return payNum;
	}

	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}

	public Double getsL() {
		return sL;
	}

	public void setsL(Double sL) {
		this.sL = sL;
	}

	public Double getYsdj() {
		return ysdj;
	}

	public void setYsdj(Double ysdj) {
		this.ysdj = ysdj;
	}

	public Double getYfdj() {
		return yfdj;
	}

	public void setYfdj(Double yfdj) {
		this.yfdj = yfdj;
	}

	public Double getyS() {
		return yS;
	}

	public void setyS(Double yS) {
		this.yS = yS;
	}

	public Double getyF() {
		return yF;
	}

	public void setyF(Double yF) {
		this.yF = yF;
	}

	public Double getlR() {
		return lR;
	}

	public void setlR(Double lR) {
		this.lR = lR;
	}
}
