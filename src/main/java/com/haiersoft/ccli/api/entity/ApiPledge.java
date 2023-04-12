package com.haiersoft.ccli.api.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author
 * @ClassName:
 * @Description:
 * @date
 */
@Entity
@Table(name = "API_PLEDGE")
@DynamicUpdate
@DynamicInsert
public class ApiPledge implements Serializable {

	private static final long serialVersionUID = 705593496752901715L;

	@Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRAY")
	// @SequenceGenerator(name = "SEQ_TRAY", sequenceName = "CCLI.SEQ_TRAY",
	// allocationSize = 1)
	@GeneratedValue(generator = "apiPledgeGenerator")
	@GenericGenerator(name = "apiPledgeGenerator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false)
	private String id; // 主键id

	@Column(name = "ACCOUNT_ID")
	private String accountId;// 账户ID

	@Column(name = "TRAYINFO_ID")
	private Integer trayInfoId;// 在库明细主键

	@Column(name = "PLEDGE_NUMBER")
	private Integer pledgeNumber;// 质押监管数量

	@Column(name = "PLEDGE_WEIGHT")
	private Double pledgeWeight;// 质押监管重量

	@Column(name = "STATE")
	private Integer state;// 质押状态 0静态质押解除1静态质押中2动态质押中3动态质押解除 4换货解押

	@Column(name = "ITEM_CLASS")
	private Integer itemClass;// 货种代码
	
	@Column(name = "ITEM_CLASS_NAME")
	private String itemClassName;// 货种代码名称

	@Column(name = "CREATE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createDate;// 修改时间
	
	@Column(name = "CUSTOMER_CODE")
	private String customerCode;// 货主code
	
	@Column(name = "CUSTOMER_ID")
	private Integer customerId;// 货主id

	@Column(name = "CONFIRM_STATUS")
	private Integer confirmStatus;// 当前状态1已质押2驳回

	@Column(name = "COMFIRM_DATE")
	private Date comfirmDate;// 生效时间
	@Column(name = "TREND_ID")
	private String trendId;// 质押/解除质押唯一标识
	@Column(name = "RELATED_TREND_ID")
	private String relatedTrendId;// 原质押唯一标识

	@Column(name = "BILL_NUM")
	private String billNum;// billNum

	@Column(name = "CUSTOMER_NAME")
	private String CustomerName;// 客户名称

	@Column(name = "SKU_ID")
	private String sku;// sku

	@Column(name = "CTN_NUM")
	private String ctnNum;// 箱号

	@Column(name = "PNAME")
	private String pName;// 品名
	
	@Column(name = "ENTER_STATE")
	private String enterState;// 入库状态
	
	@Column(name = "WAREHOUSE")
	private String wareHouse;// 仓库名
	
	@Column(name = "WAREHOUSE_ID")
	private String wareHouseId;// 仓库ID
	
	@Column(name = "BUSINESS_ID")
	private String businessId;// 动态质押时传来的 业务主键ID

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

	public Integer getTrayInfoId() {
		return trayInfoId;
	}

	public void setTrayInfoId(Integer trayInfoId) {
		this.trayInfoId = trayInfoId;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getItemClass() {
		return itemClass;
	}

	public void setItemClass(Integer itemClass) {
		this.itemClass = itemClass;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(Integer confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public Date getComfirmDate() {
		return comfirmDate;
	}

	public void setComfirmDate(Date comfirmDate) {
		this.comfirmDate = comfirmDate;
	}

	public String getTrendId() {
		return trendId;
	}

	public void setTrendId(String trendId) {
		this.trendId = trendId;
	}

	public String getRelatedTrendId() {
		return relatedTrendId;
	}

	public void setRelatedTrendId(String relatedTrendId) {
		this.relatedTrendId = relatedTrendId;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getCtnNum() {
		return ctnNum;
	}

	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getEnterState() {
		return enterState;
	}

	public void setEnterState(String enterState) {
		this.enterState = enterState;
	}

	public String getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}

	public String getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(String wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getItemClassName() {
		return itemClassName;
	}

	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}


}
