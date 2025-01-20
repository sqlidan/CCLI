package com.haiersoft.ccli.wms.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * BisOutStock entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_OUT_STOCK")
public class BisOutStock implements java.io.Serializable {

	private static final long serialVersionUID = 4040788502963084203L;
	// Fields
	@Id
	@Column(name = "OUT_LINK_ID")
	@Excel(name = "出库联系单ID")
	private String outLinkId; //出库联系单ID
	
	@Column(name = "RECEIVER_ID")
	private String receiverId; //收货方ID
	
	@Column(name = "RECEIVER")
	@Excel(name = "收货方")
	private String receiver; //收货方
	
	@Column(name = "RECEIVER_LINKER")
	@Excel(name = "收货方联系人")
	private String receiverLinker; //收货方联系人
	
	@Column(name = "STOCK_IN_ID")
	private String stockInId;  //存货方ID
	
	@Column(name = "OLD_OWNER_ID")
	private String oldOwnerId; //原货主ID
	
	@Column(name = "SETTLE_ORG_ID")
	private String settleOrgId; //结算单位ID
	
	@Column(name = "SETTLE_ORG")
	@Excel(name = "结算单位")
	private String settleOrg; //结算单位
	
	@Column(name = "STOCK_IN")
	@Excel(name = "存货方")
	private String stockIn;  //存货方
	
	@Column(name = "OLD_OWNER")
	private String oldOwner;  //原货主
	
	@Column(name = "FEE_SCHEME")
	private String feeScheme;  //费用方案
	
	@Column(name = "AUDITING_STATE")
	@Excel(name = "方案编号", replace = {"未审核_0", "已审核_1"})
	private String auditingState; //审核状态
	
	@Column(name = "SUPPLY_COMPANY")
	@Excel(name = "代报公司")
	private String supplyCompany; //代报公司
	
	@Column(name = "BOX_NUM")
	@Excel(name = "箱量")
	private Integer boxNum; //箱量
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "BOX_DATE")
	@Excel(name = "装箱日期")
	private Date boxDate;  //装箱日期
	
	@Column(name = "PRINT_NUM")
	@Excel(name = "打印标签数")
	private Integer printNum;  //打印标签数

	@Column(name = "REPEAT_NUM")
	@Excel(name = "重贴标签数量")
	private Integer repeatNum;  //重贴标签数量
	
	@Column(name = "CAR_NUM")
	@Excel(name = "车号")
	private String carNum; //车号
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "ETD_WAREHOUSE")
	@Excel(name = "计划出库时间")
	private Date etdWarehouse; //计划出库时间
	
	@Column(name = "IF_CLEAR_STORE")
	@Excel(name = "是否清库", replace = {"否_0", "是_1"})
	private String ifClearStore; //是否清库
	
	@Column(name = "IF_RELEASE")
	@Excel(name = "是否放行", replace = {"否_0", "是_1"})
	private String ifRelease;  //是否放行
	
	@Column(name = "CIQ_NUM")
	private String ciqNum;   //报检号
	
	@Column(name = "CD_NUM")
	@Excel(name = "报关号")
	private String cdNum;  //报关号
	
	@Column(name = "IF_SELF_CUSTOMS_CLEARANCE")
	@Excel(name = "是否客户自行清关", replace = {"否_0", "是_1"})
	private String ifSelfCustomsClearance;  //客户自行清关
	
	@Column(name = "IF_CUSTOMS_CLEARANCE")
	@Excel(name = "是否通关", replace = {"否_0", "是_1"})
	private String ifCustomsClearance;    //是否通关
	
	@Column(name = "IF_BONDED")
	@Excel(name = "是否保税", replace = {"否_0", "是_1"})
	private String ifBonded;   //是否报税货
	
	@Column(name = "IF_USE_TRUCK")
	@Excel(name = "是否派车", replace = {"否_0", "是_1"})
	private String ifUseTruck;  //是否派车
	
	@Column(name = "IF_WRAP")
	@Excel(name = "是否缠膜", replace = {"否_0", "是_1"})
	private String ifWrap;  //是否缠膜
	
	@Column(name = "IF_REPEAT_LABLE")
	@Excel(name = "是否重贴标签", replace = {"否_0", "是_1"})
	private String ifRepeatLable; //是否重贴标签
	
	@Column(name = "IF_WEIGH")
	@Excel(name = "是否称重", replace = {"否_0", "是_1"})
	private String ifWeigh;  //是否称重
	
	@Column(name = "WEIGH_NUM")
	@Excel(name = "称重数量")
	private Integer weighNum;  //称重数量
	
	@Column(name = "IF_MACHINE_HANDLING")
	@Excel(name = "是否机械装卸", replace = {"否_0", "是_1"})
	private String ifMachineHandling;   //是否机械装卸

	@Column(name = "IF_MANPOWER_HANDLING")
	@Excel(name = "是否人工装卸", replace = {"否_0", "是_1"})
	private String ifManpowerHandling;  //是否人工装卸
	
	@Column(name = "OUT_CUSTOMS_COUNT")
	@Excel(name = "出库报关票数")
	private Integer outCustomsCount; //出库报关票数
	
	@Column(name = "OUT_CIQ_COUNT")
	@Excel(name = "出库报检票数")
	private Integer outCiqCount;   //出库报检票数
	
	@Column(name = "APPROVE_COUNT")
	@Excel(name = "审批票数")
	private Integer approveCount;//审批票数
	
	@Column(name = "IF_BUYER_PAY")
	@Excel(name = "是否买方付费", replace = {"否_0", "是_1"})
	private String ifBuyerPay;  //是否买方付费
	 
	@Column(name = "CLEAR_SIGN")
	@Excel(name = "是否已清库", replace = {"否_0", "是_1"})
	private Integer clearSign;   //清库结算(1：已结算 0：未结算 )
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "START_STORE_TIME")
	@Excel(name = "仓储费开始日期")
	private Date startStoreTiem;  //仓储费开始日期
	
	@Column(name = "SEPCIAL_ASK")
	@Excel(name = "特殊作业要求")
	private String sepcialAsk;//特殊作业要求
	
	@Column(name = "REMARK")
	@Excel(name = "备注")
	private String remark;  //备注
	
	@Column(name = "AUDIT_PERSON")
	private String auditPerson;  //审核人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "AUDIT_TIME")
	private Date auditTime;   //审核时间
	
	@Column(name = "OPERATOR")
	private String operator;  //操作人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "OPERATE_TIME")
	@Excel(name = "操作时间")
	private Date operateTime; //操作时间
	
	@Column(name = "DEPARTMENT")
	private String department; //部门
	
	@Column(name = "COMPANY")
	private String company; //公司
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	@Excel(name = "更新时间")
	private Date updateTime; //更新时间
	
	@Column(name = "REMARK1")
	private String remark1;  //备注1
	
	@Column(name = "CK_TIME")
	private String ckTime;  //出库时间，所有
	
	@Column(name = "REMARK2")
	private String remark2;  //备注2
	@Column(name = "WAREHOUSE_ID")
	private String warehouseId;//仓库ID 关联入库联系单获取
	@Column(name = "WAREHOUSE")
	private String warehouse;//仓库名称
	@Column(name = "CIQ_COMPANY_ID")
	private String ciqCompanyId;//报检代报公司ID  
	@Column(name = "CUSTOMS_COMPANY_ID")
	private String customsCompanyId;//报关代报公司ID 
	@Column(name = "CIQ_COMPANY")
	private String ciqCompany;//报检代报公司 
	@Column(name = "CUSTOMS_COMPANY")
	private String customsCompany;//报关代报公司
	@Column(name = "PLAN_FEE_STATE")
	private String planFeeState;//计划费用状态 0未完成 1完成
	@Column(name = "FINISH_FEE_STATE")
	private String finishFeeState;//费用完成状态 0未完成 1完成
	@Column(name = "CLEAR_PERSON")
	private String clearPerson;//清库操作人
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CLEAR_DATE")
	private Date clearDate; //清库时间

	@Column(name = "SELL_FEE")
	private String sellFee;//存货方承担
	@Column(name = "BUY_FEE")
	private String buyFee;//收货方承担
	
	@Column(name = "IF_CODE_COPY")
	@Excel(name = "是否抄码", replace = {"否_0", "是_1"})
	private String ifCodeCopy;//是否抄码
	
	@Column(name = "IF_RECORD")
	@Excel(name = "是否审批", replace = {"否_0", "是_1"})
	private String ifRecord;//是否审批
	
	/********************************页面查询属性收集字段*********************************************/
	@Transient
	private Integer ntype;//导出类型 1 普通，2 日本 ，3 otc
	@Transient
	private String searchItemNum;//装车单号
	@Transient
	private String searchBillNum;//提单号
	@Transient
	private String searchCunNum;//厢号
	@Transient
	private String searchStockIn;//客户id
	@Transient
	private String searchLinkId;//联系单
	@Transient
	private String searchStrTime;//入库时间起
	@Transient
	private String searchEndTime;//入库时间止
	@Transient
	private String locationType;//是否存在库位  1有，2没有
	////////////////////////////////////////////////////////////////////////////

	@Column(name = "CHECK_LIST_NO")
	private  String checkListNo;//核注清单号

	public String getCheckListNo() {
		return checkListNo;
	}

	public void setCheckListNo(String checkListNo) {
		this.checkListNo = checkListNo;
	}
	
	
	public String getIfRecord() {
		return ifRecord;
	}

	public Integer getApproveCount() {
		return approveCount;
	}

	public void setApproveCount(Integer approveCount) {
		this.approveCount = approveCount;
	}

	public void setIfRecord(String ifRecord) {
		this.ifRecord = ifRecord;
	}

	public String getIfCodeCopy() {
		return ifCodeCopy;
	}

	public void setIfCodeCopy(String ifCodeCopy) {
		this.ifCodeCopy = ifCodeCopy;
	}

	public String getOutLinkId() {
		return outLinkId;
	}

	public void setOutLinkId(String outLinkId) {
		this.outLinkId = outLinkId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverLinker() {
		return receiverLinker;
	}

	public void setReceiverLinker(String receiverLinker) {
		this.receiverLinker = receiverLinker;
	}

	public String getStockInId() {
		return stockInId;
	}

	public void setStockInId(String stockInId) {
		this.stockInId = stockInId;
	}

	public String getOldOwnerId() {
		return oldOwnerId;
	}

	public void setOldOwnerId(String oldOwnerId) {
		this.oldOwnerId = oldOwnerId;
	}

	public String getSettleOrgId() {
		return settleOrgId;
	}

	public void setSettleOrgId(String settleOrgId) {
		this.settleOrgId = settleOrgId;
	}

	public String getSettleOrg() {
		return settleOrg;
	}

	public void setSettleOrg(String settleOrg) {
		this.settleOrg = settleOrg;
	}

	public String getStockIn() {
		return stockIn;
	}

	public void setStockIn(String stockIn) {
		this.stockIn = stockIn;
	}

	public String getOldOwner() {
		return oldOwner;
	}

	public void setOldOwner(String oldOwner) {
		this.oldOwner = oldOwner;
	}

	public String getFeeScheme() {
		return feeScheme;
	}

	public void setFeeScheme(String feeScheme) {
		this.feeScheme = feeScheme;
	}

	public String getAuditingState() {
		return auditingState;
	}

	public void setAuditingState(String auditingState) {
		this.auditingState = auditingState;
	}

	public String getSupplyCompany() {
		return supplyCompany;
	}

	public void setSupplyCompany(String supplyCompany) {
		this.supplyCompany = supplyCompany;
	}

	public String getIfClearStore() {
		return ifClearStore;
	}

	public void setIfClearStore(String ifClearStore) {
		this.ifClearStore = ifClearStore;
	}

	public String getIfRelease() {
		return ifRelease;
	}

	public void setIfRelease(String ifRelease) {
		this.ifRelease = ifRelease;
	}

	public String getCiqNum() {
		return ciqNum;
	}

	public void setCiqNum(String ciqNum) {
		this.ciqNum = ciqNum;
	}

	public String getCdNum() {
		return cdNum;
	}

	public void setCdNum(String cdNum) {
		this.cdNum = cdNum;
	}

	public Integer getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}

	public Date getBoxDate() {
		return boxDate;
	}

	public void setBoxDate(Date boxDate) {
		this.boxDate = boxDate;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public Integer getRepeatNum() {
		return repeatNum;
	}

	public void setRepeatNum(Integer repeatNum) {
		this.repeatNum = repeatNum;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public Date getEtdWarehouse() {
		return etdWarehouse;
	}

	public void setEtdWarehouse(Date etdWarehouse) {
		this.etdWarehouse = etdWarehouse;
	}

	public String getIfSelfCustomsClearance() {
		return ifSelfCustomsClearance;
	}

	public void setIfSelfCustomsClearance(String ifSelfCustomsClearance) {
		this.ifSelfCustomsClearance = ifSelfCustomsClearance;
	}

	public String getIfCustomsClearance() {
		return ifCustomsClearance;
	}

	public void setIfCustomsClearance(String ifCustomsClearance) {
		this.ifCustomsClearance = ifCustomsClearance;
	}

	public String getIfBonded() {
		return ifBonded;
	}

	public void setIfBonded(String ifBonded) {
		this.ifBonded = ifBonded;
	}

	public String getIfUseTruck() {
		return ifUseTruck;
	}

	public void setIfUseTruck(String ifUseTruck) {
		this.ifUseTruck = ifUseTruck;
	}

	public String getIfWrap() {
		return ifWrap;
	}

	public void setIfWrap(String ifWrap) {
		this.ifWrap = ifWrap;
	}

	public String getIfRepeatLable() {
		return ifRepeatLable;
	}

	public void setIfRepeatLable(String ifRepeatLable) {
		this.ifRepeatLable = ifRepeatLable;
	}

	public String getIfWeigh() {
		return ifWeigh;
	}

	public void setIfWeigh(String ifWeigh) {
		this.ifWeigh = ifWeigh;
	}

	public Integer getWeighNum() {
		return weighNum;
	}

	public void setWeighNum(Integer weighNum) {
		this.weighNum = weighNum;
	}

	public String getIfMachineHandling() {
		return ifMachineHandling;
	}

	public void setIfMachineHandling(String ifMachineHandling) {
		this.ifMachineHandling = ifMachineHandling;
	}

	public String getIfManpowerHandling() {
		return ifManpowerHandling;
	}

	public void setIfManpowerHandling(String ifManpowerHandling) {
		this.ifManpowerHandling = ifManpowerHandling;
	}

	public Integer getOutCustomsCount() {
		return outCustomsCount;
	}

	public void setOutCustomsCount(Integer outCustomsCount) {
		this.outCustomsCount = outCustomsCount;
	}

	public Integer getOutCiqCount() {
		return outCiqCount;
	}

	public void setOutCiqCount(Integer outCiqCount) {
		this.outCiqCount = outCiqCount;
	}

	public String getIfBuyerPay() {
		return ifBuyerPay;
	}

	public void setIfBuyerPay(String ifBuyerPay) {
		this.ifBuyerPay = ifBuyerPay;
	}

	public Integer getClearSign() {
		return clearSign;
	}

	public void setClearSign(Integer clearSign) {
		this.clearSign = clearSign;
	}

	public Date getStartStoreTiem() {
		return startStoreTiem;
	}

	public void setStartStoreTiem(Date startStoreTiem) {
		this.startStoreTiem = startStoreTiem;
	}

	public String getSepcialAsk() {
		return sepcialAsk;
	}

	public void setSepcialAsk(String sepcialAsk) {
		this.sepcialAsk = sepcialAsk;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getAuditPerson() {
		return auditPerson;
	}

	public void setAuditPerson(String auditPerson) {
		this.auditPerson = auditPerson;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public Integer getNtype() {
		return ntype;
	}

	public void setNtype(Integer ntype) {
		this.ntype = ntype;
	}
	
	public String getSearchBillNum() {
		return searchBillNum;
	}

	public void setSearchBillNum(String searchBillNum) {
		this.searchBillNum = searchBillNum;
	}

	public String getSearchItemNum() {
		return searchItemNum;
	}

	public void setSearchItemNum(String searchItemNum) {
		this.searchItemNum = searchItemNum;
	}

	public String getSearchCunNum() {
		return searchCunNum;
	}

	public void setSearchCunNum(String searchCunNum) {
		this.searchCunNum = searchCunNum;
	}

	public String getSearchStockIn() {
		return searchStockIn;
	}

	public void setSearchStockIn(String searchStockIn) {
		this.searchStockIn = searchStockIn;
	}

	public String getSearchLinkId() {
		return searchLinkId;
	}

	public void setSearchLinkId(String searchLinkId) {
		this.searchLinkId = searchLinkId;
	}

	public String getSearchStrTime() {
		return searchStrTime;
	}

	public void setSearchStrTime(String searchStrTime) {
		this.searchStrTime = searchStrTime;
	}

	public String getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(String searchEndTime) {
		this.searchEndTime = searchEndTime;
	}

	public String getCiqCompanyId() {
		return ciqCompanyId;
	}

	public void setCiqCompanyId(String ciqCompanyId) {
		this.ciqCompanyId = ciqCompanyId;
	}

	public String getCustomsCompanyId() {
		return customsCompanyId;
	}

	public void setCustomsCompanyId(String customsCompanyId) {
		this.customsCompanyId = customsCompanyId;
	}

	public String getCiqCompany() {
		return ciqCompany;
	}

	public void setCiqCompany(String ciqCompany) {
		this.ciqCompany = ciqCompany;
	}

	public String getCustomsCompany() {
		return customsCompany;
	}

	public void setCustomsCompany(String customsCompany) {
		this.customsCompany = customsCompany;
	}

	public String getPlanFeeState() {
		return planFeeState;
	}

	public void setPlanFeeState(String planFeeState) {
		this.planFeeState = planFeeState;
	}

	public String getFinishFeeState() {
		return finishFeeState;
	}

	public void setFinishFeeState(String finishFeeState) {
		this.finishFeeState = finishFeeState;
	}

	public String getClearPerson() {
		return clearPerson;
	}

	public void setClearPerson(String clearPerson) {
		this.clearPerson = clearPerson;
	}

	public Date getClearDate() {
		return clearDate;
	}

	public void setClearDate(Date clearDate) {
		this.clearDate = clearDate;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getCkTime() {
		return ckTime;
	}

	public void setCkTime(String ckTime) {
		this.ckTime = ckTime;
	}
	public String getSellFee() {
		return sellFee;
	}
	
	public void setSellFee(String sellFee) {
		this.sellFee = sellFee;
	}
	
	public String getBuyFee() {
		return buyFee;
	}
	
	public void setBuyFee(String buyFee) {
		this.buyFee = buyFee;
	}
	

}