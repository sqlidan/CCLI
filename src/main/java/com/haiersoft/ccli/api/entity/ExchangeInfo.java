package com.haiersoft.ccli.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 换货实体类
 * 
 *
 */
@Entity
@Table(name = "API_EXCHANGE_INFO")
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
//@DynamicUpdate @DynamicInsert
public class ExchangeInfo {

	@Id
	@GeneratedValue(generator = "exchangeInfoGenerator")
	@GenericGenerator(name = "exchangeInfoGenerator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false)
	private String id; // 主键id
	
	@Column(name = "ACCOUNT_ID")
	private String accountId; // 账户
	
	@Column(name = "ORIGINAL_PLEDGE_ID")
	private Integer originalPledgeId; // 原押品在库明细主键
	
	@Column(name = "RELIEVE_NUMBER")
	private Integer relieveNumber; // 解除质押后扔被质押监管的数量
	
	@Column(name = "RELIEVE_WEIGHT")
	private Double relieveWeight; // 解除质押后仍被质押监管的重量
	
	@Column(name = "RELIEVE_ORDER")
	private String relieveOrder; // 解除质押指令
	
	@Column(name = "NEW_PLEDGE_ID")
	private Integer newPledgeId; // 新押品在库明细主键
	
	@Column(name = "PLEDGE_NUMBER")
	private Integer pledgeNumber; // 质押监管的数量
	
	@Column(name = "PLEDGE_WEIGHT")
	private Double pledgeWeight; // 质押监管的重量
	
	@Column(name = "PLEDGE_ORDER")
	private String pledgeOrder; // 新押品质押监管指令
	
	@Column(name = "SOURCE_TREND_ID")
	private String sourceTrendId; // 解除质押唯一标识
	
	@Column(name = "RELATED_TREND_ID")
	private String relatedTrendId; // 解除质押时,原质押唯一标识
	
	@Column(name = "TREND_ID")
	private String trendId; // 新质押唯一标识

	@Column(name = "CREATE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createDate; // 创建时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CONFIRM_DATE")
	private Date confirmDate; // 确认时间
	
	@Column(name = "STATUS")
	private String status; // 换货生效状态 0/未生效 1/已生效
	
	//解押货物信息
	@Column(name = "REALESED_ITEM_CLASS")
	private Integer ReleasedItemClass;// 货种代码

	@Column(name = "REALESED_CUSTOMER_CODE")
	private String ReleasedCustomerCode;// 货主code
	
	@Column(name = "REALESED_CUSTOMER_ID")
	private Integer ReleasedCustomerId;// 货主id
	
	@Column(name = "REALESED_CUSTOMER_NAME")
	private String ReleasedCustomerName;// 货主名称
	
	@Column(name = "REALESED_BILL_NUM")
	private String ReleasedBillNum;// billNum

	@Column(name = "REALESED_SKU_ID")
	private String ReleasedSku;// sku

	@Column(name = "REALESED_CTN_NUM")
	private String ReleasedCtnNum;// 箱号

	@Column(name = "REALESED_PNAME")
	private String ReleasedPName;// 品名
	
	@Column(name = "REALESED_ENTER_STATE")
	private String ReleasedEnterState;// 入库状态
	
	@Column(name = "REALESED_WAREHOUSE")
	private String ReleasedWareHouse;// 仓库名
	
	@Column(name = "REALESED_WAREHOUSE_ID")
	private String ReleasedWareHouseId;// 仓库ID
	
	//质押货物信息	
	@Column(name = "PLEDGED_ITEM_CLASS")
	private Integer PledgedItemClass;// 货种代码

	@Column(name = "PLEDGED_CUSTOMER_CODE")
	private String PledgedCustomerCode;// 货主code
	
	@Column(name = "PLEDGED_CUSTOMER_ID")
	private Integer PledgedCustomerId;// 货主id
	
	@Column(name = "PLEDGED_CUSTOMER_NAME")
	private String PledgedCustomerName;// 货主名称
	
	@Column(name = "PLEDGED_BILL_NUM")
	private String PledgedBillNum;// billNum

	@Column(name = "PLEDGED_SKU_ID")
	private String PledgedSku;// sku

	@Column(name = "PLEDGED_CTN_NUM")
	private String PledgedCtnNum;// 箱号

	@Column(name = "PLEDGED_PNAME")
	private String PledgedPName;// 品名
	
	@Column(name = "PLEDGED_ENTER_STATE")
	private String PledgedEnterState;// 入库状态
	
	@Column(name = "PLEDGED_WAREHOUSE")
	private String PledgedWareHouse;// 仓库名
	
	@Column(name = "PLEDGED_WAREHOUSE_ID")
	private String PledgedWareHouseId;// 仓库ID

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Integer getOriginalPledgeId() {
		return originalPledgeId;
	}

	public void setOriginalPledgeId(Integer originalPledgeId) {
		this.originalPledgeId = originalPledgeId;
	}

	public Integer getRelieveNumber() {
		return relieveNumber;
	}

	public void setRelieveNumber(Integer relieveNumber) {
		this.relieveNumber = relieveNumber;
	}

	public Double getRelieveWeight() {
		return relieveWeight;
	}

	public void setRelieveWeight(Double relieveWeight) {
		this.relieveWeight = relieveWeight;
	}

	public String getRelieveOrder() {
		return relieveOrder;
	}

	public void setRelieveOrder(String relieveOrder) {
		this.relieveOrder = relieveOrder;
	}

	public Integer getNewPledgeId() {
		return newPledgeId;
	}

	public void setNewPledgeId(Integer newPledgeId) {
		this.newPledgeId = newPledgeId;
	}

	public Integer getPledgeNumber() {
		return pledgeNumber;
	}

	public void setPledgeNumber(Integer pledgeNumber) {
		this.pledgeNumber = pledgeNumber;
	}

	public Double getPledgeWeight() {
		return pledgeWeight;
	}

	public void setPledgeWeight(Double pledgeWeight) {
		this.pledgeWeight = pledgeWeight;
	}

	public String getPledgeOrder() {
		return pledgeOrder;
	}

	public void setPledgeOrder(String pledgeOrder) {
		this.pledgeOrder = pledgeOrder;
	}

	public String getSourceTrendId() {
		return sourceTrendId;
	}

	public void setSourceTrendId(String sourceTrendId) {
		this.sourceTrendId = sourceTrendId;
	}

	public String getRelatedTrendId() {
		return relatedTrendId;
	}

	public void setRelatedTrendId(String relatedTrendId) {
		this.relatedTrendId = relatedTrendId;
	}

	public String getTrendId() {
		return trendId;
	}

	public void setTrendId(String trendId) {
		this.trendId = trendId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getReleasedItemClass() {
		return ReleasedItemClass;
	}

	public void setReleasedItemClass(Integer releasedItemClass) {
		ReleasedItemClass = releasedItemClass;
	}

	public String getReleasedCustomerCode() {
		return ReleasedCustomerCode;
	}

	public void setReleasedCustomerCode(String releasedCustomerCode) {
		ReleasedCustomerCode = releasedCustomerCode;
	}

	public Integer getReleasedCustomerId() {
		return ReleasedCustomerId;
	}

	public void setReleasedCustomerId(Integer releasedCustomerId) {
		ReleasedCustomerId = releasedCustomerId;
	}

	public String getReleasedCustomerName() {
		return ReleasedCustomerName;
	}

	public void setReleasedCustomerName(String releasedCustomerName) {
		ReleasedCustomerName = releasedCustomerName;
	}

	public String getReleasedBillNum() {
		return ReleasedBillNum;
	}

	public void setReleasedBillNum(String releasedBillNum) {
		ReleasedBillNum = releasedBillNum;
	}

	public String getReleasedSku() {
		return ReleasedSku;
	}

	public void setReleasedSku(String releasedSku) {
		ReleasedSku = releasedSku;
	}

	public String getReleasedCtnNum() {
		return ReleasedCtnNum;
	}

	public void setReleasedCtnNum(String releasedCtnNum) {
		ReleasedCtnNum = releasedCtnNum;
	}

	public String getReleasedPName() {
		return ReleasedPName;
	}

	public void setReleasedPName(String releasedPName) {
		ReleasedPName = releasedPName;
	}

	public String getReleasedEnterState() {
		return ReleasedEnterState;
	}

	public void setReleasedEnterState(String releasedEnterState) {
		ReleasedEnterState = releasedEnterState;
	}

	public String getReleasedWareHouse() {
		return ReleasedWareHouse;
	}

	public void setReleasedWareHouse(String releasedWareHouse) {
		ReleasedWareHouse = releasedWareHouse;
	}

	public String getReleasedWareHouseId() {
		return ReleasedWareHouseId;
	}

	public void setReleasedWareHouseId(String releasedWareHouseId) {
		ReleasedWareHouseId = releasedWareHouseId;
	}

	public Integer getPledgedItemClass() {
		return PledgedItemClass;
	}

	public void setPledgedItemClass(Integer pledgedItemClass) {
		PledgedItemClass = pledgedItemClass;
	}

	public String getPledgedCustomerCode() {
		return PledgedCustomerCode;
	}

	public void setPledgedCustomerCode(String pledgedCustomerCode) {
		PledgedCustomerCode = pledgedCustomerCode;
	}

	public Integer getPledgedCustomerId() {
		return PledgedCustomerId;
	}

	public void setPledgedCustomerId(Integer pledgedCustomerId) {
		PledgedCustomerId = pledgedCustomerId;
	}

	public String getPledgedCustomerName() {
		return PledgedCustomerName;
	}

	public void setPledgedCustomerName(String pledgedCustomerName) {
		PledgedCustomerName = pledgedCustomerName;
	}

	public String getPledgedBillNum() {
		return PledgedBillNum;
	}

	public void setPledgedBillNum(String pledgedBillNum) {
		PledgedBillNum = pledgedBillNum;
	}

	public String getPledgedSku() {
		return PledgedSku;
	}

	public void setPledgedSku(String pledgedSku) {
		PledgedSku = pledgedSku;
	}

	public String getPledgedCtnNum() {
		return PledgedCtnNum;
	}

	public void setPledgedCtnNum(String pledgedCtnNum) {
		PledgedCtnNum = pledgedCtnNum;
	}

	public String getPledgedPName() {
		return PledgedPName;
	}

	public void setPledgedPName(String pledgedPName) {
		PledgedPName = pledgedPName;
	}

	public String getPledgedEnterState() {
		return PledgedEnterState;
	}

	public void setPledgedEnterState(String pledgedEnterState) {
		PledgedEnterState = pledgedEnterState;
	}

	public String getPledgedWareHouse() {
		return PledgedWareHouse;
	}

	public void setPledgedWareHouse(String pledgedWareHouse) {
		PledgedWareHouse = pledgedWareHouse;
	}

	public String getPledgedWareHouseId() {
		return PledgedWareHouseId;
	}

	public void setPledgedWareHouseId(String pledgedWareHouseId) {
		PledgedWareHouseId = pledgedWareHouseId;
	}

}
