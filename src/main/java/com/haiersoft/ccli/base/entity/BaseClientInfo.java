package com.haiersoft.ccli.base.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 客户
 *  @author MyEclipse Persistence Tools
 *
 */
@Entity
@Table(name = "BASE_CLIENT_INFO")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class BaseClientInfo implements java.io.Serializable {

    private static final long serialVersionUID = 1219829303374727009L;
	// Fields
	private Integer ids;
	private String clientCode;//客户代码
	@Excel(name = "客户名称")
	private String clientName;//客户名称
	@Excel(name = "客商平台客户名称")
	private String syncClientName;//客商平台客户名称
	@Excel(name = "客商平台ComCode")
	private String syncComcode;//客商平台ComCode
	@Excel(name = "客户类别", replace = {"客户_0", "供应商_1"})
	private String clientSort;//客户类别 ，1供应商0客户  2装卸队
	@Excel(name = "联系人")
	private String contactMan;//联系人
	@Excel(name = "电话")
	private String telNum;//电话
	@Excel(name = "地址")
	private String address;//地址
	@Excel(name = "/邮箱")
	private String userEMail;//邮箱
	@Excel(name = "人民币银行")
	private String rmbBank;//人民币银行
	@Excel(name = "人民币账号")
	private String rmbAccount;//人民币账号
	private String usdBank;//美元银行
	private String usdAccount;//美元账号
	private String taxAccount;//税号
	@Excel(name = "结账日")
	private Integer checkDay;//结帐日 默认26号
	private String taxpayer;//一般纳税人
	private String useUnit;//使用单位
	@Excel(name = "揽货人")
	private String saler;//揽货人
	private String pledgeType="0";//质押状态 1质押，0正常
	private Integer delFlag=0;//删除标记，0正常，1删除
	private String cwNum;//客户财务代码
	private String gysNum; //财务供应商码
	// Constructors
	private String fClientId;//父客户代码
	private String fClientName;//父客户名称
	private String serviceCode; //客服人员编码
	private String limit;//网上查询权限，0有，1无
	private String customerLevel;//客户级别
	private String note;//备注
	//对接增值税发票接口新加字段
	private String srcCustCode;//增值税对应客户代码
	private String srcCustName;//增值税对应客户名称

	private String realClientName;// 陆海空名称

	/** default constructor */
	public BaseClientInfo() {
	}

	public String getRealClientName() {
		return realClientName;
	}

	public void setRealClientName(String realClientName) {
		this.realClientName = realClientName;
	}

	/** minimal constructor */
	public BaseClientInfo(String clientCode, String clientName,
			String clientSort) {
		this.clientCode = clientCode;
		this.clientName = clientName;
		this.clientSort = clientSort;
	}

	public BaseClientInfo(Integer ids, String clientCode, String clientName,String syncClientName, String syncComcode,String clientSort, String contactMan,
			String telNum, String address, String userEMail, String rmbBank, String rmbAccount, String usdBank,
			String usdAccount, String taxAccount, Integer checkDay, String taxpayer, String useUnit, String saler,
			String pledgeType, Integer delFlag, String cwNum, String gysNum, String fClientId, String fClientName,
			String serviceCode, String limit, String customerLevel, String note, String srcCustCode,
			String srcCustName) {
		super();
		this.ids = ids;
		this.clientCode = clientCode;
		this.clientName = clientName;
		this.syncClientName = syncClientName;
		this.syncComcode = syncComcode;
		this.clientSort = clientSort;
		this.contactMan = contactMan;
		this.telNum = telNum;
		this.address = address;
		this.userEMail = userEMail;
		this.rmbBank = rmbBank;
		this.rmbAccount = rmbAccount;
		this.usdBank = usdBank;
		this.usdAccount = usdAccount;
		this.taxAccount = taxAccount;
		this.checkDay = checkDay;
		this.taxpayer = taxpayer;
		this.useUnit = useUnit;
		this.saler = saler;
		this.pledgeType = pledgeType;
		this.delFlag = delFlag;
		this.cwNum = cwNum;
		this.gysNum = gysNum;
		this.fClientId = fClientId;
		this.fClientName = fClientName;
		this.serviceCode = serviceCode;
		this.limit = limit;
		this.customerLevel = customerLevel;
		this.note = note;
		this.srcCustCode = srcCustCode;
		this.srcCustName = srcCustName;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_CLIENTS")
	@SequenceGenerator(name = "SEQUENCE_CLIENTS", sequenceName = "SEQUENCE_CLIENTS", allocationSize = 1)
	@Column(name = "IDS", unique = true, nullable = false)
	public Integer getIds() {
		return ids;
	}

	public void setIds(Integer ids) {
		this.ids = ids;
	}


	@Column(name = "CLIENT_CODE")
	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	@Column(name = "CLIENT_NAME")
	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Column(name = "CLIENT_SORT")
	public String getClientSort() {
		return this.clientSort;
	}

	public void setClientSort(String clientSort) {
		this.clientSort = clientSort;
	}

	@Column(name = "CONTACT_MAN")
	public String getContactMan() {
		return this.contactMan;
	}

	public void setContactMan(String contactMan) {
		this.contactMan = contactMan;
	}
	@Column(name = "DEL_FLAG")
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	@Column(name = "TEL_NUM")
	public String getTelNum() {
		return this.telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "E_MAIL")
	public String getUserEMail() {
		return userEMail;
	}

	public void setUserEMail(String userEMail) {
		this.userEMail = userEMail;
	}

	@Column(name = "RMB_BANK")
	public String getRmbBank() {
		return this.rmbBank;
	}

	public void setRmbBank(String rmbBank) {
		this.rmbBank = rmbBank;
	}

	@Column(name = "RMB_ACCOUNT" )
	public String getRmbAccount() {
		return this.rmbAccount;
	}

	public void setRmbAccount(String rmbAccount) {
		this.rmbAccount = rmbAccount;
	}

	@Column(name = "USD_BANK")
	public String getUsdBank() {
		return this.usdBank;
	}

	public void setUsdBank(String usdBank) {
		this.usdBank = usdBank;
	}

	@Column(name = "USD_ACCOUNT")
	public String getUsdAccount() {
		return this.usdAccount;
	}

	public void setUsdAccount(String usdAccount) {
		this.usdAccount = usdAccount;
	}

	@Column(name = "TAX_ACCOUNT")
	public String getTaxAccount() {
		return this.taxAccount;
	}

	public void setTaxAccount(String taxAccount) {
		this.taxAccount = taxAccount;
	}

	@Column(name = "CHECK_DAY")
	public Integer getCheckDay() {
		return this.checkDay;
	}

	public void setCheckDay(Integer checkDay) {
		this.checkDay = checkDay;
	}

	@Column(name = "TAXPAYER")
	public String getTaxpayer() {
		return this.taxpayer;
	}

	public void setTaxpayer(String taxpayer) {
		this.taxpayer = taxpayer;
	}

	@Column(name = "USE_UNIT")
	public String getUseUnit() {
		return this.useUnit;
	}

	public void setUseUnit(String useUnit) {
		this.useUnit = useUnit;
	}

	@Column(name = "SALER")
	public String getSaler() {
		return this.saler;
	}

	public void setSaler(String saler) {
		this.saler = saler;
	}

	@Column(name = "PLEDGE_TYPE")
	public String getPledgeType() {
		return this.pledgeType;
	}

	public void setPledgeType(String pledgeType) {
		this.pledgeType = pledgeType;
	}

	@Transient
	public String getfClientId() {
		return fClientId;
	}

	public void setfClientId(String fClientId) {
		this.fClientId = fClientId;
	}
	@Transient
	public String getfClientName() {
		return fClientName;
	}

	public void setfClientName(String fClientName) {
		this.fClientName = fClientName;
	}

	@Column(name = "CWNUM")
	public String getCwNum() {
		return cwNum;
	}

	public void setCwNum(String cwNum) {
		this.cwNum = cwNum;
	}

	@Column(name = "GYSNUM")
	public String getGysNum() {
		return gysNum;
	}

	public void setGysNum(String gysNum) {
		this.gysNum = gysNum;
	}

	@Column(name = "SERVICE_CODE")
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	@Column(name = "LIMIT")
	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	@Column(name = "CUSTOMER_LEVEL")
	public String getCustomerLevel() {
		return customerLevel;
	}

	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	@Column(name = "SRC_CUST_CODE")
	public String getSrcCustCode() {
		return srcCustCode;
	}

	public void setSrcCustCode(String srcCustCode) {
		this.srcCustCode = srcCustCode;
	}
	@Column(name = "SRC_CUST_NAME")
	public String getSrcCustName() {
		return srcCustName;
	}

	public void setSrcCustName(String srcCustName) {
		this.srcCustName = srcCustName;
	}
	@Column(name = "SYNC_CLIENT_NAME")
	public String getSyncClientName() {
		return syncClientName;
	}

	public void setSyncClientName(String syncClientName) {
		this.syncClientName = syncClientName;
	}
	@Column(name = "SYNC_COMCODE")
	public String getSyncComcode() {
		return syncComcode;
	}

	public void setSyncComcode(String syncComcode) {
		this.syncComcode = syncComcode;
	}
}
