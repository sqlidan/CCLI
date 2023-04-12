package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Connor.M
 * @ClassName: BisLoadingInfo
 * @Description: 装车单表 实体类
 * @date 2016年3月11日 上午10:11:57
 */
@Entity
@Table(name = "BIS_LOADING_INFO")
@DynamicUpdate
@DynamicInsert
public class BisLoadingInfo implements Serializable {
	
	private static final long serialVersionUID = 8602354543187362292L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOADING")
	@SequenceGenerator(name = "SEQ_LOADING", sequenceName = "SEQ_LOADING", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;//主键Id
	
	@Column(name = "LOADING_TRUCK_NUM")
	private String loadingTruckNum;//装车号;自动生成8位SEQ: SEQ_LOADING_TRUCK_NUM
	
	@Column(name = "TRAY_ID")
	private String trayId; //托盘号
	
	@Column(name = "CHANGE_TRAY_ID")
	private String changeTrayId; //置换的托盘号
	
	@Column(name = "LOADING_PLAN_NUM")
	private String loadingPlanNum;//出库订单号
	
	@Column(name = "OUT_LINK_ID")
	private String outLinkId;//出库联系单ID
	
	@Column(name = "BILL_NUM")
	private String billNum;//提单号
	
	@Column(name = "LOADING_STATE")
	private String loadingState;//状态：0已分配,1已拣货,2已装车,3已置换,4待回库(2016-8-18 废弃),5回库理货,6已回库

	@Column(name = "PIECE")
	private Integer piece;//数量   托盘上件数件数
	
	@Column(name = "CTN_NUM")
	private String ctnNum;//原入库箱号箱号
	
	@Column(name = "CARGO_NAME")
	private String cargoName;//产品名称
	
	@Column(name = "CARGO_TYPE")
	private String cargoType;//产品类型
	
	@Column(name = "SKU_ID")
	private String skuId;//SKU
	
	@Column(name = "MSC_NUM")
	private String mscNum;//msc
	
	@Column(name = "LOT_NUM")
	private String lotNum;//lot
	
	@Column(name = "TYPE_SIZE")
	private String typeSize;//规格
	
	@Column(name = "ASN_ID")
	private String asnId;//asn
	
	@Column(name = "PLATFORM_NUM")
	private String platformNum;//月台号
	
	@Column(name = "CAR_NO")
	private String carNo;//车牌号
	
	@Column(name = "NET_WEIGHT")
	private Double netWeight;//总净重
	
	@Column(name = "GROSS_WEIGHT")
	private Double grossWeight;//总毛重
	
	@Column(name = "STOCK_ID")
	private String stockId;//存货方ID
	
	@Column(name = "CARGO_LOCATION")
	private String cargoLocation;//库位号
	
	@Column(name = "BUILDING_NUM")
    private String buildingNum;//楼号
	
	@Column(name = "FLOOR_NUM")
	private String floorNum;//层号
	
	@Column(name = "ROOM_NUM")
	private String roomNum;//房间号
	
	@Column(name = "AREA_NUM")
	private String areaNum;//区位号
	
	@Column(name = "STOREROOM_NUM")
	private String storeroomNum;//库房号
	
	@Column(name = "LIBRARY_MANAGER")
	private String libraryManager;//库管员 装车单创建人
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "LIBRARY_OPE_TIME")
	private Date libraryOpeTime;//库管操作时间
	
	@Column(name = "TALLY_CLERK")
	private String tallyClerk;//理货员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "TALLY_OPE_TIME")
	private Date tallyOpeTime;//理货操作时间
	
	@Column(name = "LOADING_PERSON")
	private String loadingPerson;//装车人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "LOADING_TIME")
	private Date loadingTime;//装车操作时间
	
	@Column(name = "BACK_STOCK_PERSON")
	private String backStockPerson;//扫描回库人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "BACK_STOCK_TIME")
	private Date backStockTime;//扫描回库时间
	
	@Column(name = "BACK_TALLY_PERSON")
	private String backTallyPerson;//回库理货人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "BACK_TALLY_TIME")
	private Date backTallyTime;//回库理货时间
	
	@Column(name = "BACK_UP_PERSON")
	private String backUpPerson;//回库上架人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "BACK_UP_TIME")
	private Date backUpTime;//回库上架时间

	@Column(name = "REMARK")
	private String remark;//备注
	
	@Column(name = "ENTER_STATE")
	private String enterState;//默认为0（成品；1（货损）入库状态
	
	@Column(name = "OPERATOR")
	private String operator;//客服人员
	
	@Column(name = "IF_ALLOW")
	private Integer ifAllow;//是否授权  0 否 1是
	
	@Column(name = "WORK_MAN")
	private String workMan;//作业人员
	
	@Column(name = "LH_PERSON")
	private String lhPerson;//分配理货人员
	
	@Column(name = "CC_PERSON")
	private String ccPerson;//分配叉车人员
	
	@Column(name = "CC_PERSON2")
	private String ccPerson2;//分配叉车人员2
	
	@Column(name = "KG_NUMBER")
	private Double kgNumber;//库管人员计件数量
	
	@Column(name = "LH_NUMBER")
	private Double lhNumber;//理货人员计件数量
	
	@Column(name = "CC_NUMBER")
	private Double ccNumber;//叉车人员计件数量
	
	@Column(name = "CREATE_USER")
	private String createUser;//创建人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	private Date createTime;//创建时间
	
	@Column(name = "REMIND_ID")
	private Integer remindId;//任务提醒ID
	
	@Column(name = "RULE_JOB_TYPE")
	private String ruleJobType;
	
	@Transient 
	private String personType;
	
	@Transient 
	private String jobType;
	
	@Transient 
	private Double ratio;
	
	
	@Transient
	private String className;
	
	@Transient
	private String producingArea;
	
	@Transient
	private Integer jpiece;
	
	public String getCargoName() {
		return cargoName;
	}

	public String getBackStockPerson() {
		return backStockPerson;
	}

	public String getBuildingNum() {
		return buildingNum;
	}

	public void setBuildingNum(String buildingNum) {
		this.buildingNum = buildingNum;
	}

	public void setBackStockPerson(String backStockPerson) {
		this.backStockPerson = backStockPerson;
	}

	public Date getBackStockTime() {
		return backStockTime;
	}

	public void setBackStockTime(Date backStockTime) {
		this.backStockTime = backStockTime;
	}

	public String getBackTallyPerson() {
		return backTallyPerson;
	}

	public void setBackTallyPerson(String backTallyPerson) {
		this.backTallyPerson = backTallyPerson;
	}

	public Date getBackTallyTime() {
		return backTallyTime;
	}

	public void setBackTallyTime(Date backTallyTime) {
		this.backTallyTime = backTallyTime;
	}

	public String getBackUpPerson() {
		return backUpPerson;
	}

	public void setBackUpPerson(String backUpPerson) {
		this.backUpPerson = backUpPerson;
	}

	public Date getBackUpTime() {
		return backUpTime;
	}

	public void setBackUpTime(Date backUpTime) {
		this.backUpTime = backUpTime;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getLoadingTruckNum() {
		return loadingTruckNum;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getChangeTrayId() {
		return changeTrayId;
	}
	
	public void setChangeTrayId(String changeTrayId) {
		this.changeTrayId = changeTrayId;
	}
	
	public String getBillNum() {
		return billNum;
	}
	
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	public String getFloorNum() {
		return floorNum;
	}
	
	public void setFloorNum(String floorNum) {
		this.floorNum = floorNum;
	}
	
	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getAreaNum() {
		return areaNum;
	}
	
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	
	public String getStoreroomNum() {
		return storeroomNum;
	}
	
	public void setStoreroomNum(String storeroomNum) {
		this.storeroomNum = storeroomNum;
	}
	
	public String getLoadingPerson() {
		return loadingPerson;
	}
	
	public void setLoadingPerson(String loadingPerson) {
		this.loadingPerson = loadingPerson;
	}
	
	public Date getLoadingTime() {
		return loadingTime;
	}
	
	public void setLoadingTime(Date loadingTime) {
		this.loadingTime = loadingTime;
	}
	
	public void setLoadingTruckNum(String loadingTruckNum) {
		this.loadingTruckNum = loadingTruckNum;
	}
	
	public String getTrayId() {
		return trayId;
	}
	
	public void setTrayId(String trayId) {
		this.trayId = trayId;
	}
	
	public String getLoadingPlanNum() {
		return loadingPlanNum;
	}
	
	public void setLoadingPlanNum(String loadingPlanNum) {
		this.loadingPlanNum = loadingPlanNum;
	}
	
	public String getOutLinkId() {
		return outLinkId;
	}
	
	public void setOutLinkId(String outLinkId) {
		this.outLinkId = outLinkId;
	}
	
	public String getLoadingState() {
		return loadingState;
	}
	
	public void setLoadingState(String loadingState) {
		this.loadingState = loadingState;
	}
	
	public Integer getPiece() {
		return piece;
	}
	
	public void setPiece(Integer piece) {
		this.piece = piece;
	}
	
	public String getCargoLocation() {
		return cargoLocation;
	}
	
	public void setCargoLocation(String cargoLocation) {
		this.cargoLocation = cargoLocation;
	}
	
	public String getCtnNum() {
		return ctnNum;
	}
	
	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}
	
	public String getSkuId() {
		return skuId;
	}
	
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	
	public String getMscNum() {
		return mscNum;
	}

	public void setMscNum(String mscNum) {
		this.mscNum = mscNum;
	}

	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	public String getTypeSize() {
		return typeSize;
	}

	public void setTypeSize(String typeSize) {
		this.typeSize = typeSize;
	}

	public String getAsnId() {
		return asnId;
	}
	
	public void setAsnId(String asnId) {
		this.asnId = asnId;
	}
	
	public String getPlatformNum() {
		return platformNum;
	}
	
	public void setPlatformNum(String platformNum) {
		this.platformNum = platformNum;
	}
	
	public String getCarNo() {
		return carNo;
	}
	
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	
	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public String getStockId() {
		return stockId;
	}
	
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	
	public String getLibraryManager() {
		return libraryManager;
	}
	
	public void setLibraryManager(String libraryManager) {
		this.libraryManager = libraryManager;
	}
	
	public Date getLibraryOpeTime() {
		return libraryOpeTime;
	}
	
	public void setLibraryOpeTime(Date libraryOpeTime) {
		this.libraryOpeTime = libraryOpeTime;
	}
	
	public String getTallyClerk() {
		return tallyClerk;
	}
	
	public void setTallyClerk(String tallyClerk) {
		this.tallyClerk = tallyClerk;
	}
	
	public Date getTallyOpeTime() {
		return tallyOpeTime;
	}
	
	public void setTallyOpeTime(Date tallyOpeTime) {
		this.tallyOpeTime = tallyOpeTime;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEnterState() {
		return enterState;
	}

	public void setEnterState(String enterState) {
		this.enterState = enterState;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getProducingArea() {
		return producingArea;
	}

	public void setProducingArea(String producingArea) {
		this.producingArea = producingArea;
	}

	public Integer getIfAllow() {
		return ifAllow;
	}

	public void setIfAllow(Integer ifAllow) {
		this.ifAllow = ifAllow;
	}

	public String getWorkMan() {
		return workMan;
	}

	public void setWorkMan(String workMan) {
		this.workMan = workMan;
	}
	
	public String getLhPerson() {
		return lhPerson;
	}

	public void setLhPerson(String lhPerson) {
		this.lhPerson = lhPerson;
	}

	public String getCcPerson() {
		return ccPerson;
	}

	public void setCcPerson(String ccPerson) {
		this.ccPerson = ccPerson;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getRemindId() {
		return remindId;
	}

	public void setRemindId(Integer remindId) {
		this.remindId = remindId;
	}

	public String getRuleJobType() {
		return ruleJobType;
	}

	public void setRuleJobType(String ruleJobType) {
		this.ruleJobType = ruleJobType;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	public String getCcPerson2() {
		return ccPerson2;
	}

	public void setCcPerson2(String ccPerson2) {
		this.ccPerson2 = ccPerson2;
	}

	public Double getKgNumber() {
		return kgNumber;
	}

	public void setKgNumber(Double kgNumber) {
		this.kgNumber = kgNumber;
	}

	public Double getLhNumber() {
		return lhNumber;
	}

	public void setLhNumber(Double lhNumber) {
		this.lhNumber = lhNumber;
	}

	public Double getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(Double ccNumber) {
		this.ccNumber = ccNumber;
	}

	public Integer getJpiece() {
		return jpiece;
	}

	public void setJpiece(Integer jpiece) {
		this.jpiece = jpiece;
	}
	
	
}
