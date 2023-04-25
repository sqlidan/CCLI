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
 * @ClassName: BackHistoryInfo
 * @Description: 回库记录实体类
 * @date 2016年3月3日 下午5:50:38
 */
@Entity
@Table(name = "BIS_BACK_HISTORY_INFO")
@DynamicUpdate
@DynamicInsert
public class BackHistoryInfo implements Serializable{

    private static final long serialVersionUID = 705593496752901715L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BACK")
	@SequenceGenerator(name = "SEQ_BACK", sequenceName = "SEQ_BACK", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id; //主键id
    
    @Column(name = "TRAY_ID")
	private String trayId;//托盘号
	
    @Column(name = "CTN_NUM")
	private String ctnNum;//箱号
	
    @Column(name = "BILL_NUM")
	private String billNum;//提单号
    
    @Column(name = "ASN")
   	private String asn;//asn
	
    @Column(name = "SKU_ID")
	private String skuId;//SKU
	
    @Column(name = "STOCK_IN")
	private String stockIn;//现存货方Id
    
    @Column(name = "STOCK_NAME")
	private String stockName;//现存货方名称
	
    @Column(name = "NOW_PIECE")
	private Integer nowPiece;//现有件数
    
    
	@Column(name = "CARGO_NAME")
	private String cargoName;//产品名称
	
	@Column(name = "CARGO_TYPE")
	private String cargoType;//产品类型
	
    @Column(name = "NET_WEIGHT")
	private Double netWeight;//总净重
	
    @Column(name = "GROSS_WEIGHT")
	private Double grossWeight;//总毛重
    
    @Column(name = "NET_SINGLE")
	private Double netSingle;//单净	
	
	@Column(name = "GROSS_SINGLE")
	private Double grossSingle;//单毛
	
	
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
    
    @Column(name = "BACK_PERSON")
    private String backPerson;//回库操作员
    
    @Column(name = "BACK_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date backTime;//回库时间
    
    @Column(name = "LOADING_NUM")
    private String loadingNum;
    
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

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Integer getNowPiece() {
		return nowPiece;
	}

	public void setNowPiece(Integer nowPiece) {
		this.nowPiece = nowPiece;
	}

	public String getCargoName() {
		return cargoName;
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

	public String getCargoLocation() {
		return cargoLocation;
	}

	public void setCargoLocation(String cargoLocation) {
		this.cargoLocation = cargoLocation;
	}

	public String getBuildingNum() {
		return buildingNum;
	}

	public void setBuildingNum(String buildingNum) {
		this.buildingNum = buildingNum;
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

	public String getBackPerson() {
		return backPerson;
	}

	public void setBackPerson(String backPerson) {
		this.backPerson = backPerson;
	}

	public Date getBackTime() {
		return backTime;
	}

	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}

	public String getLoadingNum() {
		return loadingNum;
	}

	public void setLoadingNum(String loadingNum) {
		this.loadingNum = loadingNum;
	}
	
    
}
