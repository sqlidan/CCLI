package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * ASN基础表
 * BisAsn entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_ASN")
public class BisAsn implements Serializable {
	
    private static final long serialVersionUID = -2445139913899725243L;
    
	// Fields
	private String asn;
	private String asnState;//ASN单据状态 1 在途（默认）2 收货中（第一个理货）3已上架（第一个入库）4已完成
	private String ctnNum;//箱号
	private Date transportTime;//运输日期
	private String orderNum;//订单号 
	private String linkId;//(联系单ID) 
	private String stockIn;//存货方
	private String stockName;//存货方名称
	private String billNum;//提单号
	private String remark;//
	private String mark;//0正常，1货转标记
	private String tallyUser;//理货人员
	private String ifSecondEnter;//入库类型 1.在库2.重收3.在库分拣
	private Date inboundTime;//入库时间
	private String isBonded;//是否保税
	private String warehouseId;//仓库ID 关联入库联系单获取
	private String warehouse;//仓库名称
	private Integer ifPlanTime;//是否填写计划时间
	private String createUser;//操作人员
	private Date createTime;//操作时间
	private String workMan;//库管人员
	private String lhPerson;//理货人员
	private String ccPerson;//叉车人员
	private String ccPerson2;//叉车人员
	private Double kgNumber;//库管人员计件数量
	private Double lhNumber;//理货人员计件数量
	private Double ccNumber;//叉车人员计件数量
	private Integer ifAllow=0;//是否授权  0 否 1是
	private Integer remindId;//任务提醒ID
	private String ruleJobType;
	private String personType;
	private String jobType;
	private Double ratio;
	//追加列表字段
	private Integer isEdite=0;//标记是否可以修改数据 0可编辑，1不可编辑
	private String [] addid;//sku集合
	private Double [] piece;//件数集合
	private Double [] netWeight;//总净重集合
	private Double [] grossWeight;//总毛重集合
	private String [] salesNum;//SALES REF NO集合
	private String [] rkNum;//入库号集合
	private String [] proTime;//入库号集合
	private String [] remark1;//备注集合
	private String [] delSkus;//修改时删除的sku集合
	
	private String [] hsCode;//hs编码
	private String [] hsItemname;//海关品名
	private String [] accountBook;//商品账册序号
	
	
	
	private Double realGrossWeight;//总毛重 20170823
	// Constructors

	/** default constructor */
	public BisAsn() {
	}

	/** minimal constructor */
	public BisAsn(String asn, String asnState, String ctnNum, String linkId,
			String stockIn, String billNum) {
		this.asn = asn;
		this.asnState = asnState;
		this.ctnNum = ctnNum;
		this.linkId = linkId;
		this.stockIn = stockIn;
		this.billNum = billNum;
	}

	/** full constructor */
	public BisAsn(String asn, String asnState, String ctnNum,
			Date transportTime, String orderNum, String linkId, String stockIn,
			String billNum, String remark, String mark) {
		this.asn = asn;
		this.asnState = asnState;
		this.ctnNum = ctnNum;
		this.transportTime = transportTime;
		this.orderNum = orderNum;
		this.linkId = linkId;
		this.stockIn = stockIn;
		this.billNum = billNum;
		this.remark = remark;
		this.mark = mark;
	}

	// Property accessors
	@Id
	@Column(name = "ASN")
	public String getAsn() {
		return this.asn;
	}

	public void setAsn(String asn) {
		this.asn = asn;
	}

	@Column(name = "ASN_STATE")
	public String getAsnState() {
		return this.asnState;
	}

	public void setAsnState(String asnState) {
		this.asnState = asnState;
	}

	@Column(name = "CTN_NUM")
	public String getCtnNum() {
		return this.ctnNum;
	}

	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "TRANSPORT_TIME")
	public Date getTransportTime() {
		return this.transportTime;
	}

	public void setTransportTime(Date transportTime) {
		this.transportTime = transportTime;
	}

	@Column(name = "ORDER_NUM")
	public String getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	@Column(name = "LINK_ID")
	public String getLinkId() {
		return this.linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	@Column(name = "STOCK_IN")
	public String getStockIn() {
		return this.stockIn;
	}

	public void setStockIn(String stockIn) {
		this.stockIn = stockIn;
	}

	@Column(name = "BILL_NUM")
	public String getBillNum() {
		return this.billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "MARK")
	public String getMark() {
		return this.mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	@Column(name = "TALLY_USER")
	public String getTallyUser() {
		return tallyUser;
	}

	public void setTallyUser(String tallyUser) {
		this.tallyUser = tallyUser;
	}
	@Column(name = "IF_SECOND_ENTER")
	public String getIfSecondEnter() {
		return ifSecondEnter;
	}

	public void setIfSecondEnter(String ifSecondEnter) {
		this.ifSecondEnter = ifSecondEnter;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "INBOUND_DATE")
	public Date getInboundTime() {
		return inboundTime;
	}

	public void setInboundTime(Date inboundTime) {
		this.inboundTime = inboundTime;
	}
	@Column(name = "STOCK_NAME")
	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	@Column(name = "IS_BONDED")
	public String getIsBonded() {
		return isBonded;
	}

	public void setIsBonded(String isBonded) {
		this.isBonded = isBonded;
	}
	@Column(name = "WAREHOUSE_ID")
	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	@Column(name = "WAREHOUSE")
	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	
	@Column(name = "IF_PLAN_TIME")
	public Integer getIfPlanTime() {
		return ifPlanTime;
	}

	public void setIfPlanTime(Integer ifPlanTime) {
		this.ifPlanTime = ifPlanTime;
	}
	
	@Column(name = "IF_ALLOW")
	public Integer getIfAllow() {
		return ifAllow;
	}

	public void setIfAllow(Integer ifAllow) {
		this.ifAllow = ifAllow;
	}
	
	
	@Column(name = "CREATE_USER")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	@Column(name = "WORK_MAN")
	public String getWorkMan() {
		return workMan;
	}

	public void setWorkMan(String workMan) {
		this.workMan = workMan;
	}

	
	@Column(name = "LH_PERSON")
	public String getLhPerson() {
		return lhPerson;
	}

	public void setLhPerson(String lhPerson) {
		this.lhPerson = lhPerson;
	}

	@Column(name = "CC_PERSON")
	public String getCcPerson() {
		return ccPerson;
	}

	public void setCcPerson(String ccPerson) {
		this.ccPerson = ccPerson;
	}

	@Column(name = "CC_PERSON2")
	public String getCcPerson2() {
		return ccPerson2;
	}

	public void setCcPerson2(String ccPerson2) {
		this.ccPerson2 = ccPerson2;
	}

	@Column(name = "KG_NUMBER")
	public Double getKgNumber() {
		return kgNumber;
	}

	public void setKgNumber(Double kgNumber) {
		this.kgNumber = kgNumber;
	}

	@Column(name = "LH_NUMBER")
	public Double getLhNumber() {
		return lhNumber;
	}

	public void setLhNumber(Double lhNumber) {
		this.lhNumber = lhNumber;
	}

	@Column(name = "CC_NUMBER")
	public Double getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(Double ccNumber) {
		this.ccNumber = ccNumber;
	}

	@Column(name = "REMIND_ID")
	public Integer getRemindId() {
		return remindId;
	}

	public void setRemindId(Integer remindId) {
		this.remindId = remindId;
	}
	
	@Column(name = "RULE_JOB_TYPE")
	public void setRuleJobType(String ruleJobType) {
		this.ruleJobType = ruleJobType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}


	@Transient 
	public String getPersonType() {
		return personType;
	}

	public String getRuleJobType() {
		return ruleJobType;
	}

	
	@Transient 
	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	@Transient 
	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	/**********************************表单提交数据采集*************************************************/
	@Transient 
	public String[] getAddid() {
		return addid;
	}

	public void setAddid(String[] addid) {
		this.addid = addid;
	}
	@Transient
	public Double[] getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double[] netWeight) {
		this.netWeight = netWeight;
	}
	@Transient
	public Double[] getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double[] grossWeight) {
		this.grossWeight = grossWeight;
	}
	@Transient
	public String[] getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(String[] salesNum) {
		this.salesNum = salesNum;
	}
	@Transient
	public String[] getRkNum() {
		return rkNum;
	}

	public void setRkNum(String[] rkNum) {
		this.rkNum = rkNum;
	}
	@Transient
	public String[] getProTime() {
		return proTime;
	}

	public void setProTime(String[] proTime) {
		this.proTime = proTime;
	}
	@Transient
	public String[] getRemark1() {
		return remark1;
	}

	public void setRemark1(String[] remark1) {
		this.remark1 = remark1;
	}

	
	public String[] getHsCode() {
		return hsCode;
	}

	public void setHsCode(String[] hsCode) {
		this.hsCode = hsCode;
	}

	public String[] getHsItemname() {
		return hsItemname;
	}

	public void setHsItemname(String[] hsItemname) {
		this.hsItemname = hsItemname;
	}

	public String[] getAccountBook() {
		return accountBook;
	}

	public void setAccountBook(String[] accountBook) {
		this.accountBook = accountBook;
	}

	@Transient
	public Double[] getPiece() {
		return piece;
	}
	
	public void setPiece(Double[] piece) {
		this.piece = piece;
	}
	@Transient
	public Integer getIsEdite() {
		return isEdite;
	}

	public void setIsEdite(Integer isEdite) {
		this.isEdite = isEdite;
	}
	@Transient
	public String[] getDelSkus() {
		return delSkus;
	}

	public void setDelSkus(String[] delSkus) {
		this.delSkus = delSkus;
	}
	@Column(name = "REAL_GROSS_WEIGHT")
	public Double getRealGrossWeight() {
		return realGrossWeight;
	}

	public void setRealGrossWeight(Double realGrossWeight) {
		this.realGrossWeight = realGrossWeight;
	}

	
	 
} 
