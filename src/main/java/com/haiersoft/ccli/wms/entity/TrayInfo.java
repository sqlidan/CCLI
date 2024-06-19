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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Connor.M
 * @ClassName: TrayInfo
 * @Description: 库存实体类
 * @date 2016年3月3日 下午5:50:38
 */
@Entity
@Table(name = "BIS_TRAY_INFO")
@DynamicUpdate
@DynamicInsert
public class TrayInfo implements Serializable{

    private static final long serialVersionUID = 705593496752901715L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRAY")
	@SequenceGenerator(name = "SEQ_TRAY", sequenceName = "SEQ_TRAY", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id; //主键id
    
    @Column(name = "TRAY_ID")
	private String trayId;//托盘号
	
    @Column(name = "CTN_NUM")
	private String ctnNum;//箱号
	
    @Column(name = "BILL_NUM")
	private String billNum;//提单号
    
    @Column(name = "CONTACT_TYPE")
   	private String contactType;//联系单类型（1入库2出库3货转） 
    
    @Column(name = "CONTACT_NUM")
   	private String contactNum;//联系单号
    
    @Column(name = "ASN")
   	private String asn;//asn
	
    @Column(name = "SKU_ID")
	private String skuId;//SKU
	
    @Column(name = "STOCK_IN")
	private String stockIn;//现存货方Id
    
    @Column(name = "STOCK_NAME")
	private String stockName;//现存货方名称
    
    @Column(name = "BEFORE_STOCK_IN")
	private String beforeStockIn;//原存货方Id
    
    @Column(name = "BEFORE_STOCK_NAME")
	private String beforeStockName;//原存货方名称
	
    @Column(name = "ORIGINAL_PIECE")
	private Integer originalPiece;//初始件数
	
    @Column(name = "NOW_PIECE")
	private Integer nowPiece;//现有件数
    
    @Column(name = "PLEDGE_PIECE")
  	private Integer pledgePiece;//质押件数
    
    @Column(name = "REMOVE_PIECE")
   	private Integer removePiece;//拆托数量
    
	@Column(name = "CARGO_NAME")
	private String cargoName;//产品名称
	
	@Column(name = "CARGO_TYPE")
	private String cargoType;//产品类型
	
    @Column(name = "IF_TRANSFER")
	private String ifTransfer;//记录是否存在货转  0 正常  1货转   2质押货转标志
	
    @Column(name = "IF_SECOND_ENTER")
	private String ifSecondEnter;//记录多次该托盘是否多次入库 Y：重收 N：正常  J：分拣入库类型
	
    @Column(name = "NET_WEIGHT")
	private Double netWeight;//总净重
	
    @Column(name = "GROSS_WEIGHT")
	private Double grossWeight;//总毛重
    
    @Column(name = "PLEDGE_GROSS_WEIGHT")
  	private Double pledgeGrossWeight;//质押重量
    
    @Column(name = "NET_SINGLE")
	private Double netSingle;//单净	
	
	@Column(name = "GROSS_SINGLE")
	private Double grossSingle;//单毛
	
    @Column(name = "UNITS")
	private String units;//重量单位 1千克 2吨
	
    @Column(name = "WAREHOUSE_ID")
	private String warehouseId;//仓库id
    
    @Column(name = "WAREHOUSE")
   	private String warehouse;//仓库代码
    
    @Column(name = "CARGO_LOCATION")
   	private String cargoLocation;//库位号
    
    @Column(name = "BUILDING_NUM")
    private String buildingNum;//楼号
    
    @Column(name = "FLOOR_NUM")
    private String floorNum;//层号
    
    @Column(name = "ROOM_NUM")
    private String RoomNum;//房间号
    
    @Column(name = "AREA_NUM")
    private String areaNum;//区位号
    
    @Column(name = "STOREROOM_NUM")
    private String storeroomNum;//库房号
	
    @Column(name = "ENTER_STOCK_NUM")
	private String enterStockNum;//联系单信息带出入库号
	
    @Column(name = "ENTER_TALLY_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date enterTallyTime;//入库理货时间
	
    @Column(name = "ENTER_TALLY_OPERSON")
	private String enterTallyOperson;//入库理货员
	
    @Column(name = "OUT_TALLY_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date outTallyTime;//出库理货时间
	
    @Column(name = "OUT_TALLY_OPERSON")
	private String outTallyOperson;//出库理货员
	
    @Column(name = "ENTER_STOCK_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date enterStockTime;//默认入库时当前时间 入库时间
	
    @Column(name = "ENTER_OPERSON")
	private String enterOperson;//入库操作人员
	
    @Column(name = "OUT_STOCK_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date outStockTime;//默认出库时当前时间出库时间
	
    @Column(name = "OUT_OPERSON")
	private String outOperson;//出库操作人员
	
    @Column(name = "OUT_OPERATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date outOperateTime;//出库操作时间
	
    @Column(name = "ENTER_STATE")
	private String enterState;//默认为0（成品；1（货物货损）2包装破损 入库状态
	
    @Column(name = "CARGO_STATE")
	private String cargoState;//默认为00 00已收货（理货确认）01已上架（库管确认）10出库中（装车单）11出库理货（库管确认）12已出库（理货确认）20待回库(弃用) 21回库收货（理货确认）  99报损货物状态
	
    @Column(name = "UPDATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date updateTime;//修改时间
	
    @Column(name = "REMARK")
	private String remark;//备注
    
    @Column(name = "ISTRUCK")
   	private String isTruck = "0";//标示在装车单锁定状态 0，正常，1锁定
    
    @Column(name = "ENTER_REMARK")
   	private String enterRemark ;//入库时间修改备注
    
    @Column(name = "ENTER_REMARK2")
   	private String enterRemark2 ;//上架件数修改备注
    
    @Column(name = "TRAY_NEW_CODE")
   	private String trayNewCode ;//新托盘号
   
    @Column(name = "QTY")
   	private Integer qty ;//jianshu 
    
    @Column(name = "IS_BONDED")
    private String isBonded;//保税非保税
    
    @Column(name = "UPLOADER")
    private String uploader;//保税转一般贸易人员

    @Column(name = "UPLOAD_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date uploadDate;//修改时间

	@Column(name = "ACTUAL_STOREROOM_X")
	private String actualStoreroomX;//实际托盘位置X
	@Column(name = "ACTUAL_STOREROOM_Y")
	private String actualStoreroomY;//实际托盘位置Y
	@Column(name = "ACTUAL_STOREROOM_Z")
	private String actualStoreroomZ;//实际托盘位置Z

    
/*    @Column(name = "IS_ALL_FORWARD")
    private String isAllForward;//保税货物是否完全转一般贸易
    
    
	public String getIsAllForward() {
		return isAllForward;
	}

	public void setIsAllForward(String isAllForward) {
		this.isAllForward = isAllForward;
	}
	
*/
    
	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getIsBonded() {
		return isBonded;
	}

	public void setIsBonded(String isBonded) {
		this.isBonded = isBonded;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getTrayNewCode() {
		return trayNewCode;
	}

	public void setTrayNewCode(String trayNewCode) {
		this.trayNewCode = trayNewCode;
	}

	public String getBeforeStockIn() {
		return beforeStockIn;
	}

	public Double getNetSingle() {
		return netSingle;
	}

	public void setNetSingle(Double netSingle) {
		this.netSingle = netSingle;
	}

	public Double getGrossSingle() {
		return grossSingle;
	}

	public void setGrossSingle(Double grossSingle) {
		this.grossSingle = grossSingle;
	}

	public void setBeforeStockIn(String beforeStockIn) {
		this.beforeStockIn = beforeStockIn;
	}

	public String getBeforeStockName() {
		return beforeStockName;
	}

	public void setBeforeStockName(String beforeStockName) {
		this.beforeStockName = beforeStockName;
	}

	public String getCargoName() {
		return cargoName;
	}

	public String getFloorNum() {
		return floorNum;
	}

	public void setFloorNum(String floorNum) {
		this.floorNum = floorNum;
	}

	public String getRoomNum() {
		return RoomNum;
	}

	public void setRoomNum(String roomNum) {
		RoomNum = roomNum;
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

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTrayId() {
		return trayId;
	}

	public void setTrayId(String trayId) {
		this.trayId = trayId;
	}

	public String getCtnNum() {
		return ctnNum;
	}

	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getAsn() {
		return asn;
	}

	public void setAsn(String asn) {
		this.asn = asn;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getStockIn() {
		return stockIn;
	}

	public void setStockIn(String stockIn) {
		this.stockIn = stockIn;
	}

	public Integer getOriginalPiece() {
		return originalPiece;
	}

	public void setOriginalPiece(Integer originalPiece) {
		this.originalPiece = originalPiece;
	}

	public Integer getNowPiece() {
		return nowPiece;
	}

	public void setNowPiece(Integer nowPiece) {
		this.nowPiece = nowPiece;
	}

	public String getIfTransfer() {
		return ifTransfer;
	}

	public void setIfTransfer(String ifTransfer) {
		this.ifTransfer = ifTransfer;
	}

	public String getIfSecondEnter() {
		return ifSecondEnter;
	}

	public void setIfSecondEnter(String ifSecondEnter) {
		this.ifSecondEnter = ifSecondEnter;
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

	public String getUnits() {
		return units;
	}
	

	public void setUnits(String units) {
		this.units = units;
	}

	public String getCargoLocation() {
		return cargoLocation;
	}

	public void setCargoLocation(String cargoLocation) {
		this.cargoLocation = cargoLocation;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getEnterStockNum() {
		return enterStockNum;
	}

	public void setEnterStockNum(String enterStockNum) {
		this.enterStockNum = enterStockNum;
	}

	public Date getEnterTallyTime() {
		return enterTallyTime;
	}

	public void setEnterTallyTime(Date enterTallyTime) {
		this.enterTallyTime = enterTallyTime;
	}

	public String getEnterTallyOperson() {
		return enterTallyOperson;
	}

	public void setEnterTallyOperson(String enterTallyOperson) {
		this.enterTallyOperson = enterTallyOperson;
	}

	public Date getOutTallyTime() {
		return outTallyTime;
	}

	public void setOutTallyTime(Date outTallyTime) {
		this.outTallyTime = outTallyTime;
	}

	public String getOutTallyOperson() {
		return outTallyOperson;
	}

	public void setOutTallyOperson(String outTallyOperson) {
		this.outTallyOperson = outTallyOperson;
	}

	public Date getEnterStockTime() {
		return enterStockTime;
	}

	public void setEnterStockTime(Date enterStockTime) {
		this.enterStockTime = enterStockTime;
	}

	public String getEnterOperson() {
		return enterOperson;
	}

	public void setEnterOperson(String enterOperson) {
		this.enterOperson = enterOperson;
	}

	public Date getOutStockTime() {
		return outStockTime;
	}

	public void setOutStockTime(Date outStockTime) {
		this.outStockTime = outStockTime;
	}

	public String getOutOperson() {
		return outOperson;
	}

	public void setOutOperson(String outOperson) {
		this.outOperson = outOperson;
	}

	public Date getOutOperateTime() {
		return outOperateTime;
	}

	public void setOutOperateTime(Date outOperateTime) {
		this.outOperateTime = outOperateTime;
	}

	public String getEnterState() {
		return enterState;
	}

	public void setEnterState(String enterState) {
		this.enterState = enterState;
	}

	public String getCargoState() {
		return cargoState;
	}

	public void setCargoState(String cargoState) {
		this.cargoState = cargoState;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getRemovePiece() {
		return removePiece;
	}

	public void setRemovePiece(Integer removePiece) {
		this.removePiece = removePiece;
	}

	public String getIsTruck() {
		return isTruck;
	}

	public String getBuildingNum() {
		return buildingNum;
	}

	public void setBuildingNum(String buildingNum) {
		this.buildingNum = buildingNum;
	}

	public void setIsTruck(String isTruck) {
		this.isTruck = isTruck;
	}

	public String getEnterRemark() {
		return enterRemark;
	}

	public void setEnterRemark(String enterRemark) {
		this.enterRemark = enterRemark;
	}

	public String getEnterRemark2() {
		return enterRemark2;
	}

	public void setEnterRemark2(String enterRemark2) {
		this.enterRemark2 = enterRemark2;
	}
   
	public Integer getPledgePiece() {
		return pledgePiece;
	}

	public void setPledgePiece(Integer pledgePiece) {
		this.pledgePiece = pledgePiece;
	}

	public Double getPledgeGrossWeight() {
		return pledgeGrossWeight;
	}

	public void setPledgeGrossWeight(Double pledgeGrossWeight) {
		this.pledgeGrossWeight = pledgeGrossWeight;
	}

	public String getActualStoreroomX() {
		return actualStoreroomX;
	}

	public void setActualStoreroomX(String actualStoreroomX) {
		this.actualStoreroomX = actualStoreroomX;
	}

	public String getActualStoreroomY() {
		return actualStoreroomY;
	}

	public void setActualStoreroomY(String actualStoreroomY) {
		this.actualStoreroomY = actualStoreroomY;
	}

	public String getActualStoreroomZ() {
		return actualStoreroomZ;
	}

	public void setActualStoreroomZ(String actualStoreroomZ) {
		this.actualStoreroomZ = actualStoreroomZ;
	}
}
