package com.haiersoft.ccli.wms.entity;

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
 * 入库联系单的entity
 * @author pyl
 * @date 2016年2月27日
 */
@Entity
@Table(name = "BIS_ENTER_STOCK")
@DynamicUpdate 
@DynamicInsert
public class BisEnterStock implements Serializable {

    private static final long serialVersionUID = 4040788502963084203L;
    
    @Id
    @Column(name = "LINK_ID")
    @Excel(name = "入库联系单ID")
	private String linkId;	//入库联系单ID
   
    @Column(name = "ITEM_NUM")
    @Excel(name = "提单号")
	private String itemNum;//提单号
	
	@Column(name = "VESSEL_NAME")
	private String vesselName;//入库时船名
	
	@Column(name = "VOYAGE_NUM")
	private String voyageNum;//入库时航次
	
	@Column(name = "STOCK_IN")
	@Excel(name = "存货方")
	private String stockIn;//存货方

	@Column(name = "STOCK_ID")
	private String stockId;//存货方ID

	@Column(name = "AUDITING_STATE")
	@Excel(name = "审核状态", replace = {"暂存_0", "已提交_1", "已审核_2"})
	private Integer auditingState;//审核状态
	
	@Column(name = "FEE_ID")
	private String feeId;//费用方案ID
	
	@Column(name = "FEE_PLAN")
	@Excel(name = "费用方案")
	private String feePlan;//费用方案
	
	@Column(name = "STOCK_ORG_ID")
	private String stockOrgId;//存货方结算单位ID
	
	@Column(name = "STOCK_ORG")
	@Excel(name = "结算单位")
	private String stockOrg;//存货方结算单位	
	
	@Column(name = "EXAMINE_PERSON")
	@Excel(name = "审核人")
	private String examinePerson;//审核人
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "EXAMINE_TIME")
	@Excel(name = "审核时间")
	private Date examineTime;//审核时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "ETA_SHIP")
	private Date etaShip;//ETA

	@Column(name = "CTN_TYPE_SIZE")
	@Excel(name = "箱型尺寸")
	private String ctnTypeSize;//箱型尺寸
	
	@Column(name = "CTN_AMOUNT")
	@Excel(name = "箱量")
	private Double ctnAmount;//箱量
	
	@Column(name = "IF_SORTING")
	@Excel(name = "是否分拣", replace = {"否_0", "是_1"})
	private String ifSorting;//是否分拣
	
	@Column(name = "SORTING_ASK1")
	private String sortingAsk1;//分拣要求1
	
	@Column(name = "SORTING_ASK2")
	private String sortingAsk2;//分拣要求2
	
	@Column(name = "SORTING_ASK3")
	private String sortingAsk3;//分拣要求3
	
	@Column(name = "SORTING_ASK4")
	private String sortingAsk4;//分拣要求4
	
	@Column(name = "SORTING_ASK5")
	private String sortingAsk5;//分拣要求5
	
	@Column(name = "SORTING_ASK6")
	private String sortingAsk6;//分拣要求6
	
	@Column(name = "SORTING_SPECIAL_ASK")
	private String sortingSpecialAsk;//特殊分拣
	
	@Column(name = "SORTING_SPECIAL")
	private String sortingSpecial;//特殊要求
	
	@Column(name = "WAREHOUSE_ID")
	private String warehouseId;//入库仓库ID
	
	@Column(name = "WAREHOUSE")
	@Excel(name = "仓库")
	private String warehouse;//仓库名
	
	@Column(name = "TEMPERATURE")
	@Excel(name = "存储温度")
	private String temperature;//存储温度,默认为-18℃存储温度
	
	@Column(name = "RECEPTACLE")
	private String receptacle;//物流容器
	
	@Column(name = "IF_BAGGING")
	@Excel(name = "是否套袋", replace = {"否_0", "是_1"})
	private String ifBagging;//套袋
	
	@Column(name = "IF_WRAP")
	@Excel(name = "是否缠膜", replace = {"否_0", "是_1"})
	private String ifWrap;//缠膜
	
	@Column(name = "IF_WITH_WOODEN")
	@Excel(name = "是否带木托", replace = {"否_0", "是_1"})
	private String ifWithWooden;//自带木托
	
	@Column(name = "IF_ATTACH_BILL")
	private String ifAttachBill;//附带提单
	
	@Column(name = "IF_ATTACH_CTN_INFO")
	private String ifAttachCtnInfo;//附带箱单
	
	@Column(name = "IF_ATTACH_DETIAL")
	private String ifAttachDetial;//附带入库明细
	
	@Column(name = "IF_BONDED")
	@Excel(name = "是否保税", replace = {"否_0", "是_1"})
	private String ifBonded;//保税
	
	@Column(name = "IF_TO_CUSTOMS")
	@Excel(name = "是否报关", replace = {"否_0", "是_1"})
	private String ifToCustoms;//报关
	
	@Column(name = "IF_TO_CIQ")
	@Excel(name = "是否报检", replace = {"否_0", "是_1"})
	private String ifToCiq;//报检
	
	@Column(name = "CUSTOMS_COMPANY_ID")
	private String customsCompanyId;//报关代理单位Id
	
	@Column(name = "CUSTOMS_COMPANY")
	@Excel(name = "报关代理公司")
	private String customsCompany;//报关代理单位
	
	@Column(name = "CIQ_COMPANY_ID")
	private String ciqCompanyId;//报检代理单位Id
	
	@Column(name = "CIQ_COMPANY")
	@Excel(name = "报检代理公司")
	private String ciqCompany;//报检代理单位
	
	@Column(name = "IF_USE_TRUCK")
	private String ifUseTruck;//派车
	
	@Column(name = "IF_WEIGH")
	private String ifWeigh;//称重
	
	@Column(name = "IF_REPEAT_LABLE")
	private String ifRepeatLable;//重贴标签
	
	@Column(name = "IF_MAC_ADMIT")
	@Excel(name = "是否MSC认证", replace = {"否_0", "是_1"})
	private String ifMacAdmit;//MSC认证
	
	@Column(name = "IF_CHILD_WAREHOUSE_")
	private String ifChildWarehouse;//报分库
	
	@Column(name = "IF_SELF_CUSTOMS_CLEARANCE")
	private String ifSelfCustomsClearance;//客户自行清关
	
	@Column(name = "IF_PROPORTION_EXES")
	private String ifProportionExes;//按比例收费
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "ETA_WAREHOUSE")
	@Excel(name = "计划入库时间")
	private Date etaWarehouse;//计划入库时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "ETD_WAREHOUSE")
	private Date etdWarehouse;//计划出库时间
	
	@Column(name = "ORDER_NUM")
	@Excel(name = "订单号")
	private String orderNum;//订单号
	
	@Column(name = "RK_NUM")
	@Excel(name = "入库号")
	private String rkNum;//入库号
	
	@Column(name = "REMARK")
	@Excel(name = "备注")
	private String remark;//备注

	@Column(name = "SEPCIAL_ASK")
	@Excel(name = "特殊要求")
	private String sepcialAsk;//特殊要求
	
	@Column(name = "OPERATOR")
	@Excel(name = "操作人")
	private String operator;//操作人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "OPERATE_TIME")
	@Excel(name = "创建时间")
	private Date operateTime;//操作时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;//修改时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "ENTER_TIME")
	private Date enterTime;//实际入库时间  最小值
	
	@Column(name = "DEPARTMENT")
	private String department;//部门
	
	@Column(name = "COMPANY")
	private String company;//公司
	
	@Column(name = "REMARK1")
	private String remark1;//备注1
	
	@Column(name = "DEL_FLAG")
	private String delFlag;//删除标记 0正常 1删除
	
	@Column(name = "PLAN_FEE_STATE")
	@Excel(name = "计划费用状态", replace = {"未完成_0", "完成_1"})
	private String planFeeState;//计划费用状态 0未完成 1完成
	
	@Column(name = "FINISH_FEE_STATE")
	@Excel(name = "费用完成状态", replace = {"未完成_0", "完成_1"})
	private String finishFeeState;//费用完成状态 0未完成 1完成

	@Column(name = "RK_TIME")
	private String rkTime;//所有入库时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "BACKDATE") 
	@Excel(name = "倒箱日期")
	private Date backDate;//倒箱日期
	
	@Column(name = "IF_BACK")
	@Excel(name = "是否倒箱", replace = {"否_0", "是_1"})
	private String ifBack;//是否倒箱
	
	@Column(name = "IF_CHECK")
	@Excel(name = "是否查验", replace = {"否_0", "是_1"})
	private String ifCheck;//是否查验
	
	@Column(name = "IF_PRINT_LABLE")
	@Excel(name = "是否打印标签", replace = {"否_0", "是_1"})
	private String ifPrintLable;//是否打印标签
	
	@Column(name = "PRINT_LABLE_AMOUNT")
	@Excel(name = "打印标签数量")
	private Double printLableAmount;//录入多少数量
	
	@Column(name = "IF_PRINT_NLABLE")
	@Excel(name = "是否贴内标签", replace = {"否_0", "是_1"})
	private String ifPrintNLable;//是否贴内标签
	
	@Column(name = "PRINT_NLABLE_AMOUNT")
	@Excel(name = "内标签数量")
	private Double printNLableAmount;//录入多少数量
	
	@Column(name = "IF_PRINT_WLABLE")
	@Excel(name = "是否贴外标签", replace = {"否_0", "是_1"})
	private String ifPrintWLable;//是否贴外标签
	
	@Column(name = "PRINT_WLABLE_AMOUNT")
	@Excel(name = "外标签数量")
	private Double printWLableAmount;//录入多少数量
	
	@Column(name = "IF_OUT_FIT")
	@Excel(name = "是否装铁架", replace = {"否_0", "是_1"})
	private String ifOutFit;//是否装铁架
	
	@Column(name = "OUT_FIT_AMOUNT")
	@Excel(name = "装铁架数量")
	private Double outFitAmount;//录入多少数量
	
	@Column(name = "IF_TAKE_FIT")
	@Excel(name = "是否拆铁架", replace = {"否_0", "是_1"})
	private String ifTakeFit;//是否拆铁架
	
	@Column(name = "TAKE_FIT_AMOUNT")
	@Excel(name = "拆铁架数量")
	private Double takeFitAmount;//录入多少数量
	
	@Column(name = "BGDH")
	private String bgdh;//报关单号2019-1-14
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "BGDHDATE") 
	private Date bgdhDate;//报关申报时间
	
	@Column(name = "YCG")
	private String ycg;//原产国
	
	@Column(name = "IF_YB_MY")
	@Excel(name = "是否一般贸易", replace = {"否_0", "是_1"})
	private String ifYbMy;//是否一般贸易
	
	@Column(name = "IF_RECORD")
	@Excel(name = "是否审批", replace = {"否_0", "是_1"})
	private String ifRecord;//是否审批
		
	/********************************页面查询属性收集字段*********************************************/
	@Transient
	private Integer ntype;//导出类型 1 普通，2 日本 ，3 otc
	@Transient
	private String searchItemNum;//提单号
	@Transient
	private String searchCunNum;//箱号
	@Transient
	private String searchStockIn;//客户id
	@Transient
	private String searchLinkId;//联系单
	@Transient
	private String searchSKUNum;//SKU
	@Transient
	private String searchStrTime;//入库时间起
	@Transient
	private String searchEndTime;//入库时间止
	
	@Transient
	private String searchDxStrTime;//倒箱起止时间起
	@Transient
	private String searchDxEndTime;//倒箱结束时间
	@Column(name = "HSCODE")
	private String hscode;//倒箱结束时间
	
	@Column(name = "ACCOUNT_BOOK")
	private String accountBook;//账册商品序号
	@Column(name = "ENGLISH_NAME")
	private  String englishName;//英文船名
	@Column(name = "DOCK")
	//@Excel(name = "码头", replace = {"三期_3", "四期_4","五期_5"})
	private  String dock;//码头
	@Column(name = "IS_SEND")
	//@Excel(name = "是否发送BSL报文", replace = {"是_1", "否_0"})
	private String isSend;//是否发送bsl报文"是_1", "否_0"}
	
	@Column(name = "CHINESE_NAME")
	private  String chineseName;//中文船名
    //////////////////////////////////////////////////////////////////////////


	@Transient
	private  String cargoName;//品名
	@Transient
	private  String piece;//箱数(合计)

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public String getPiece() {
		return piece;
	}

	public void setPiece(String piece) {
		this.piece = piece;
	}

	public Date getBgdhDate() {
		return bgdhDate;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getDock() {
		return dock;
	}

	public void setDock(String dock) {
		this.dock = dock;
	}



	public String getAccountBook() {
		return accountBook;
	}

	public void setAccountBook(String accountBook) {
		this.accountBook = accountBook;
	}

	public void setBgdhDate(Date bgdhDate) {
		this.bgdhDate = bgdhDate;
	}

	public String getBgdh() {
		return bgdh;
	}
	
	public String getIfRecord() {
		return ifRecord;
	}

	public void setIfRecord(String ifRecord) {
		this.ifRecord = ifRecord;
	}

	public String getIfYbMy() {
		return ifYbMy;
	}

	public void setIfYbMy(String ifYbMy) {
		this.ifYbMy = ifYbMy;
	}

	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}

	public String getYcg() {
		return ycg;
	}

	public void setYcg(String ycg) {
		this.ycg = ycg;
	}

	public Date getBackDate() {
		return backDate;
	}

	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}

	public String getVesselName() {
		return vesselName;
	}

	public Date getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getStockIn() {
		return stockIn;
	}

	public void setStockIn(String stockIn) {
		this.stockIn = stockIn;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public Integer getAuditingState() {
		return auditingState;
	}

	public void setAuditingState(Integer auditingState) {
		this.auditingState = auditingState;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getFeePlan() {
		return feePlan;
	}

	public void setFeePlan(String feePlan) {
		this.feePlan = feePlan;
	}

	public String getStockOrgId() {
		return stockOrgId;
	}

	public void setStockOrgId(String stockOrgId) {
		this.stockOrgId = stockOrgId;
	}

	public String getStockOrg() {
		return stockOrg;
	}

	public void setStockOrg(String stockOrg) {
		this.stockOrg = stockOrg;
	}

	public String getExaminePerson() {
		return examinePerson;
	}

	public void setExaminePerson(String examinePerson) {
		this.examinePerson = examinePerson;
	}

	public Date getExamineTime() {
		return examineTime;
	}

	public void setExamineTime(Date examineTime) {
		this.examineTime = examineTime;
	}

	public Date getEtaShip() {
		return etaShip;
	}

	public void setEtaShip(Date etaShip) {
		this.etaShip = etaShip;
	}

	public String getCtnTypeSize() {
		return ctnTypeSize;
	}

	public void setCtnTypeSize(String ctnTypeSize) {
		this.ctnTypeSize = ctnTypeSize;
	}

	public Double getCtnAmount() {
		return ctnAmount;
	}

	public void setCtnAmount(Double ctnAmount) {
		this.ctnAmount = ctnAmount;
	}

	public String getIfSorting() {
		return ifSorting;
	}

	public void setIfSorting(String ifSorting) {
		this.ifSorting = ifSorting;
	}

	public String getSortingAsk1() {
		return sortingAsk1;
	}

	public void setSortingAsk1(String sortingAsk1) {
		this.sortingAsk1 = sortingAsk1;
	}

	public String getSortingAsk2() {
		return sortingAsk2;
	}

	public void setSortingAsk2(String sortingAsk2) {
		this.sortingAsk2 = sortingAsk2;
	}

	public String getSortingAsk3() {
		return sortingAsk3;
	}

	public void setSortingAsk3(String sortingAsk3) {
		this.sortingAsk3 = sortingAsk3;
	}

	public String getSortingAsk4() {
		return sortingAsk4;
	}

	public void setSortingAsk4(String sortingAsk4) {
		this.sortingAsk4 = sortingAsk4;
	}

	public String getSortingAsk5() {
		return sortingAsk5;
	}

	public void setSortingAsk5(String sortingAsk5) {
		this.sortingAsk5 = sortingAsk5;
	}

	public String getSortingAsk6() {
		return sortingAsk6;
	}

	public void setSortingAsk6(String sortingAsk6) {
		this.sortingAsk6 = sortingAsk6;
	}

	public String getSortingSpecialAsk() {
		return sortingSpecialAsk;
	}

	public void setSortingSpecialAsk(String sortingSpecialAsk) {
		this.sortingSpecialAsk = sortingSpecialAsk;
	}

	public String getSortingSpecial() {
		return sortingSpecial;
	}

	public void setSortingSpecial(String sortingSpecial) {
		this.sortingSpecial = sortingSpecial;
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

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getReceptacle() {
		return receptacle;
	}

	public void setReceptacle(String receptacle) {
		this.receptacle = receptacle;
	}

	public String getIfBagging() {
		return ifBagging;
	}

	public void setIfBagging(String ifBagging) {
		this.ifBagging = ifBagging;
	}

	public String getIfWrap() {
		return ifWrap;
	}

	public void setIfWrap(String ifWrap) {
		this.ifWrap = ifWrap;
	}

	public String getIfWithWooden() {
		return ifWithWooden;
	}

	public void setIfWithWooden(String ifWithWooden) {
		this.ifWithWooden = ifWithWooden;
	}

	public String getIfAttachBill() {
		return ifAttachBill;
	}

	public void setIfAttachBill(String ifAttachBill) {
		this.ifAttachBill = ifAttachBill;
	}

	public String getIfAttachCtnInfo() {
		return ifAttachCtnInfo;
	}

	public void setIfAttachCtnInfo(String ifAttachCtnInfo) {
		this.ifAttachCtnInfo = ifAttachCtnInfo;
	}

	public String getIfAttachDetial() {
		return ifAttachDetial;
	}

	public void setIfAttachDetial(String ifAttachDetial) {
		this.ifAttachDetial = ifAttachDetial;
	}

	public String getIfBonded() {
		return ifBonded;
	}

	public void setIfBonded(String ifBonded) {
		this.ifBonded = ifBonded;
	}

	public String getIfToCustoms() {
		return ifToCustoms;
	}

	public void setIfToCustoms(String ifToCustoms) {
		this.ifToCustoms = ifToCustoms;
	}

	public String getIfToCiq() {
		return ifToCiq;
	}

	public void setIfToCiq(String ifToCiq) {
		this.ifToCiq = ifToCiq;
	}


	public String getCustomsCompanyId() {
		return customsCompanyId;
	}

	public void setCustomsCompanyId(String customsCompanyId) {
		this.customsCompanyId = customsCompanyId;
	}

	public String getCustomsCompany() {
		return customsCompany;
	}

	public void setCustomsCompany(String customsCompany) {
		this.customsCompany = customsCompany;
	}

	public String getCiqCompanyId() {
		return ciqCompanyId;
	}

	public void setCiqCompanyId(String ciqCompanyId) {
		this.ciqCompanyId = ciqCompanyId;
	}

	public String getCiqCompany() {
		return ciqCompany;
	}

	public void setCiqCompany(String ciqCompany) {
		this.ciqCompany = ciqCompany;
	}

	public String getIfUseTruck() {
		return ifUseTruck;
	}

	public void setIfUseTruck(String ifUseTruck) {
		this.ifUseTruck = ifUseTruck;
	}

	public String getIfWeigh() {
		return ifWeigh;
	}

	public void setIfWeigh(String ifWeigh) {
		this.ifWeigh = ifWeigh;
	}

	public String getIfRepeatLable() {
		return ifRepeatLable;
	}

	public void setIfRepeatLable(String ifRepeatLable) {
		this.ifRepeatLable = ifRepeatLable;
	}

	public String getIfMacAdmit() {
		return ifMacAdmit;
	}

	public void setIfMacAdmit(String ifMacAdmit) {
		this.ifMacAdmit = ifMacAdmit;
	}

	public String getIfChildWarehouse() {
		return ifChildWarehouse;
	}

	public void setIfChildWarehouse(String ifChildWarehouse) {
		this.ifChildWarehouse = ifChildWarehouse;
	}

	public String getIfSelfCustomsClearance() {
		return ifSelfCustomsClearance;
	}

	public void setIfSelfCustomsClearance(String ifSelfCustomsClearance) {
		this.ifSelfCustomsClearance = ifSelfCustomsClearance;
	}

	public String getIfProportionExes() {
		return ifProportionExes;
	}

	public void setIfProportionExes(String ifProportionExes) {
		this.ifProportionExes = ifProportionExes;
	}

	public Date getEtaWarehouse() {
		return etaWarehouse;
	}

	public void setEtaWarehouse(Date etaWarehouse) {
		this.etaWarehouse = etaWarehouse;
	}

	public Date getEtdWarehouse() {
		return etdWarehouse;
	}

	public void setEtdWarehouse(Date etdWarehouse) {
		this.etdWarehouse = etdWarehouse;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getRkNum() {
		return rkNum;
	}

	public void setRkNum(String rkNum) {
		this.rkNum = rkNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSepcialAsk() {
		return sepcialAsk;
	}

	public void setSepcialAsk(String sepcialAsk) {
		this.sepcialAsk = sepcialAsk;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getVoyageNum() {
		return voyageNum;
	}

	public void setVoyageNum(String voyageNum) {
		this.voyageNum = voyageNum;
	}

	public Integer getNtype() {
		return ntype;
	}

	public void setNtype(Integer ntype) {
		this.ntype = ntype;
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

	public String getSearchSKUNum() {
		return searchSKUNum;
	}

	public void setSearchSKUNum(String searchSKUNum) {
		this.searchSKUNum = searchSKUNum;
	}

	public String getRkTime() {
		return rkTime;
	}

	public void setRkTime(String rkTime) {
		this.rkTime = rkTime;
	}
	
	public String getIfCheck() {
		return ifCheck;
	}

	public void setIfCheck(String ifCheck) {
		this.ifCheck = ifCheck;
	}

	public String getIfPrintLable() {
		return ifPrintLable;
	}

	public void setIfPrintLable(String ifPrintLable) {
		this.ifPrintLable = ifPrintLable;
	}

	public String getIfPrintNLable() {
		return ifPrintNLable;
	}

	public void setIfPrintNLable(String ifPrintNLable) {
		this.ifPrintNLable = ifPrintNLable;
	}

	public String getIfPrintWLable() {
		return ifPrintWLable;
	}

	public void setIfPrintWLable(String ifPrintWLable) {
		this.ifPrintWLable = ifPrintWLable;
	}

	public String getIfOutFit() {
		return ifOutFit;
	}

	public void setIfOutFit(String ifOutFit) {
		this.ifOutFit = ifOutFit;
	}

	public String getIfTakeFit() {
		return ifTakeFit;
	}

	public void setIfTakeFit(String ifTakeFit) {
		this.ifTakeFit = ifTakeFit;
	}

	public Double getPrintLableAmount() {
		return printLableAmount;
	}

	public void setPrintLableAmount(Double printLableAmount) {
		this.printLableAmount = printLableAmount;
	}

	public Double getPrintNLableAmount() {
		return printNLableAmount;
	}

	public void setPrintNLableAmount(Double printNLableAmount) {
		this.printNLableAmount = printNLableAmount;
	}

	public Double getPrintWLableAmount() {
		return printWLableAmount;
	}

	public void setPrintWLableAmount(Double printWLableAmount) {
		this.printWLableAmount = printWLableAmount;
	}

	public Double getOutFitAmount() {
		return outFitAmount;
	}

	public void setOutFitAmount(Double outFitAmount) {
		this.outFitAmount = outFitAmount;
	}

	public Double getTakeFitAmount() {
		return takeFitAmount;
	}

	public void setTakeFitAmount(Double takeFitAmount) {
		this.takeFitAmount = takeFitAmount;
	}

	public String getIfBack() {
		return ifBack;
	}

	public void setIfBack(String ifBack) {
		this.ifBack = ifBack;
	}

	public String getSearchDxStrTime() {
		return searchDxStrTime;
	}

	public void setSearchDxStrTime(String searchDxStrTime) {
		this.searchDxStrTime = searchDxStrTime;
	}

	public String getSearchDxEndTime() {
		return searchDxEndTime;
	}

	public void setSearchDxEndTime(String searchDxEndTime) {
		this.searchDxEndTime = searchDxEndTime;
	}

	public String getHscode() {
		return hscode;
	}

	public void setHscode(String hscode) {
		this.hscode = hscode;
	}
	
}