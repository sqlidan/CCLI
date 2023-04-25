package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 货转联系单主表
 * BisTransferStock entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_TRANSFER_STOCK")
public class BisTransferStock implements Serializable {

    private static final long serialVersionUID = 154173816242172006L;
    
	private String transferId;//T（货转标志）+操作人员代码(三位，自动补齐)+YYMMDDHHMMS 
	private String receiver;//收货方id
	private String receiverName;//收货方
	private String receiverLinker;//收货方联系人
	private String stockIn;//存货方
	private String stockInId;//存货方id
	private String oldOwnerId;//原货主id
	private String receiverOrgId;//收货方结算单位id
	private String receiverOrg;//收货方结算单位
	private String oldOwner;//原货主
	private String ifTransfer;//1正常2货转 3 质押货转标志
	private String feeId;//费用方案id
	private String feePlan;//费用方案
	private String auditingState;//未审核 1((默认)审核 2(审核后该单不可修改)审核状态
	private String supplyCompany;//代报公司
	private String ifClearStore;//是否清库
	private String cdNum;//报关号
	private String ifSelfCustomsClearance;//客户自行清关
	private Date startStoreDate;//仓储费开始日期
	private String remark;//备注
	private String operator;//操作人员
	private Date operateTime;//操作时间
	private String department;//部门
	private String company;//公司
	private Date updateTime;//修改时间
	private String isBuyFee;//是否买方付费 （1：是 0：否）
	private String sellFee;//存货方承担）
	private String sellFeeName;//存货方承担
	private String buyFee;//收货方承担
	private String buyFeeName;//收货方承担
	private String warehouseId;//仓库ID 关联入库联系单获取
	private String warehouse;//仓库名称
	//追加列表字段
	private Integer isEdite=0;//标记是否可以修改数据 0可编辑，1不可编辑
	private Integer isTSpliet=0;//是否可以生成货转拆托  0初始状态  ，2 保存已拆分完（不可编辑），1 保存未拆分（可编辑）
	private String[] skuList;//sku集合
	private String[] rkList;//入库号集合
	private String[] billList;//提单
	private String[] ctnList;//厢号
	private String[] foodList;//品名
	private String[] intoList;//入库类型
	private Double[] peacList;//出库件数
	private Double[] netList;//单净重
	private Double[] grossList;//单毛重
	private String [] delList;//明细删除列
	private Integer ntype;//导出类型 1 普通，2 日本 ，3 otc
	private String searchTransferNum;//货转单号
	private String churu;//1入库 2出库
	private String ifBonded;//是否保税
	// Constructors

	/** default constructor */
	public BisTransferStock() {
	}

	 
	// Property accessors
	@Id
	@Column(name = "TRANSFER_ID" )
	public String getTransferId() {
		return this.transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	 
	@Column(name = "RECEIVER" )
	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Column(name = "RECEIVER_LINKER" )
	public String getReceiverLinker() {
		return this.receiverLinker;
	}

	public void setReceiverLinker(String receiverLinker) {
		this.receiverLinker = receiverLinker;
	}

	@Column(name = "STOCK_IN" )
	public String getStockIn() {
		return this.stockIn;
	}

	public void setStockIn(String stockIn) {
		this.stockIn = stockIn;
	}

	@Column(name = "STOCK_IN_ID" )
	public String getStockInId() {
		return this.stockInId;
	}

	public void setStockInId(String stockInId) {
		this.stockInId = stockInId;
	}

	@Column(name = "OLD_OWNER_ID" )
	public String getOldOwnerId() {
		return this.oldOwnerId;
	}

	public void setOldOwnerId(String oldOwnerId) {
		this.oldOwnerId = oldOwnerId;
	}

	@Column(name = "RECEIVER_ORG_ID" )
	public String getReceiverOrgId() {
		return this.receiverOrgId;
	}

	public void setReceiverOrgId(String receiverOrgId) {
		this.receiverOrgId = receiverOrgId;
	}

	@Column(name = "RECEIVER_ORG" )
	public String getReceiverOrg() {
		return this.receiverOrg;
	}

	public void setReceiverOrg(String receiverOrg) {
		this.receiverOrg = receiverOrg;
	}

	@Column(name = "OLD_OWNER" )
	public String getOldOwner() {
		return this.oldOwner;
	}

	public void setOldOwner(String oldOwner) {
		this.oldOwner = oldOwner;
	}

	@Column(name = "IF_TRANSFER" )
	public String getIfTransfer() {
		return this.ifTransfer;
	}

	public void setIfTransfer(String ifTransfer) {
		this.ifTransfer = ifTransfer;
	}
	@Column(name = "FEE_ID" )
	public String getFeeId() {
		return feeId;
	}


	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}


	@Column(name = "FEE_PLAN" )
	public String getFeePlan() {
		return this.feePlan;
	}

	public void setFeePlan(String feePlan) {
		this.feePlan = feePlan;
	}

	@Column(name = "AUDITING_STATE" )
	public String getAuditingState() {
		return this.auditingState;
	}

	public void setAuditingState(String auditingState) {
		this.auditingState = auditingState;
	}

	@Column(name = "SUPPLY_COMPANY" )
	public String getSupplyCompany() {
		return this.supplyCompany;
	}

	public void setSupplyCompany(String supplyCompany) {
		this.supplyCompany = supplyCompany;
	}

	@Column(name = "IF_CLEAR_STORE" )
	public String getIfClearStore() {
		return this.ifClearStore;
	}

	public void setIfClearStore(String ifClearStore) {
		this.ifClearStore = ifClearStore;
	}

	@Column(name = "CD_NUM" )
	public String getCdNum() {
		return this.cdNum;
	}

	public void setCdNum(String cdNum) {
		this.cdNum = cdNum;
	}

	@Column(name = "IF_SELF_CUSTOMS_CLEARANCE" )
	public String getIfSelfCustomsClearance() {
		return this.ifSelfCustomsClearance;
	}

	public void setIfSelfCustomsClearance(String ifSelfCustomsClearance) {
		this.ifSelfCustomsClearance = ifSelfCustomsClearance;
	}
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "START_STORE_DATE" )
	public Date getStartStoreDate() {
		return this.startStoreDate;
	}

	public void setStartStoreDate(Date startStoreDate) {
		this.startStoreDate = startStoreDate;
	}

	@Column(name = "REMARK" )
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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


	@Column(name = "OPERATOR" )
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "OPERATE_TIME" )
	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	@Column(name = "DEPARTMENT" )
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "COMPANY" )
	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME" )
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	 
	@Column(name = "IS_BUY_FEE" )
	public String getIsBuyFee() {
		return isBuyFee;
	}


	public void setIsBuyFee(String isBuyFee) {
		this.isBuyFee = isBuyFee;
	}
	 
	@Column(name = "RECEIVER_NAME" )
	public String getReceiverName() {
		return receiverName;
	}


	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	@Column(name = "SELLFEE" )
	public String getSellFee() {
		return sellFee;
	}


	public void setSellFee(String sellFee) {
		this.sellFee = sellFee;
	}

	@Column(name = "SELLFEENAME" )
	public String getSellFeeName() {
		return sellFeeName;
	}


	public void setSellFeeName(String sellFeeName) {
		this.sellFeeName = sellFeeName;
	}

	@Column(name = "BUYFEE" )
	public String getBuyFee() {
		return buyFee;
	}


	public void setBuyFee(String buyFee) {
		this.buyFee = buyFee;
	}

	@Column(name = "BUYFEENAME" )
	public String getBuyFeeName() {
		return buyFeeName;
	}


	public void setBuyFeeName(String buyFeeName) {
		this.buyFeeName = buyFeeName;
	}


	@Transient
	public Integer getIsTSpliet() {
		return isTSpliet;
	}


	public void setIsTSpliet(Integer isTSpliet) {
		this.isTSpliet = isTSpliet;
	}


	@Transient
	public Integer getIsEdite() {
		return isEdite;
	}

	public void setIsEdite(Integer isEdite) {
		this.isEdite = isEdite;
	}

	@Transient
	public String[] getSkuList() {
		return skuList;
	}


	public void setSkuList(String[] skuList) {
		this.skuList = skuList;
	}

	@Transient
	public String[] getRkList() {
		return rkList;
	}


	public void setRkList(String[] rkList) {
		this.rkList = rkList;
	}


	@Transient
	public String[] getBillList() {
		return billList;
	}


	public void setBillList(String[] billList) {
		this.billList = billList;
	}

	@Transient
	public String[] getCtnList() {
		return ctnList;
	}


	public void setCtnList(String[] ctnList) {
		this.ctnList = ctnList;
	}

	@Transient
	public String[] getFoodList() {
		return foodList;
	}


	public void setFoodList(String[] foodList) {
		this.foodList = foodList;
	}

	@Transient
	public String[] getIntoList() {
		return intoList;
	}


	public void setIntoList(String[] intoList) {
		this.intoList = intoList;
	}

	@Transient
	public Double[] getPeacList() {
		return peacList;
	}


	public void setPeacList(Double[] peacList) {
		this.peacList = peacList;
	}

	@Transient
	public Double[] getNetList() {
		return netList;
	}


	public void setNetList(Double[] netList) {
		this.netList = netList;
	}

	@Transient
	public Double[] getGrossList() {
		return grossList;
	}


	public void setGrossList(Double[] grossList) {
		this.grossList = grossList;
	}

	@Transient
	public String[] getDelList() {
		return delList;
	}


	public void setDelList(String[] delList) {
		this.delList = delList;
	}

	@Transient
	public Integer getNtype() {
		return ntype;
	}


	public void setNtype(Integer ntype) {
		this.ntype = ntype;
	}

	@Transient
	public String getSearchTransferNum() {
		return searchTransferNum;
	}


	public void setSearchTransferNum(String searchTransferNum) {
		this.searchTransferNum = searchTransferNum;
	}

	@Transient
	public String getChuru() {
		return churu;
	}

	public void setChuru(String churu) {
		this.churu = churu;
	}

	@Transient
	public String getIfBonded() {
		return ifBonded;
	}


	public void setIfBonded(String ifBonded) {
		this.ifBonded = ifBonded;
	}
}