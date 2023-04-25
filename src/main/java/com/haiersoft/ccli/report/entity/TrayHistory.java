package com.haiersoft.ccli.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dxl584327830 on 16/8/15.
 */
public class TrayHistory implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3713218720291137263L;
	private String trayCode;//托盘号
    private String billCode;//提单号
    private String asn;//ASN
    private String sku;//SKU
    private String contactCode;//联系单号
    private String clientId;//客户id
    private String clientName;//客户名称
    private String locationCode;//库位号
    private String warehouse;//仓库名 gzq 20160627 添加
    private String warehouseId;//仓库ID gzq 20160627 添加
    private String cargoName;//产品名称
    private String cargoType;//产品类型
    private Integer nowNum;//现有数量
    private Double netWeight;//净重
    private Double grossWeight;//毛重
    private String units;//单位
    private String state;//默认为00 00已收货（理货确认）01已上架（库管确认）10出库中（装车单）11出库理货（库管确认）12已出库（理货确认）20待回库 21回库收货（理货确认）  99报损货物状态
    private String ctnNum;//箱号
    private String enterPerson;//入库理货人员
    private String enterOp;//入库操作人员

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date enterTime;//入库操作时间

    private Date searchStrartTime;//开始时间
    private Date searchEndTime;//结束时间

    private String strartTime;//开始时间
    private String endTime;//结束时间

    private String locationType;//是否存在库位  1有，2没有

    private String reportType;

    private String backupTime; //备份时间

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getAsn() {
        return asn;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getContactCode() {
        return contactCode;
    }

    public void setContactCode(String contactCode) {
        this.contactCode = contactCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
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

    public Integer getNowNum() {
        return nowNum;
    }

    public void setNowNum(Integer nowNum) {
        this.nowNum = nowNum;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCtnNum() {
        return ctnNum;
    }

    public void setCtnNum(String ctnNum) {
        this.ctnNum = ctnNum;
    }

    public String getEnterPerson() {
        return enterPerson;
    }

    public void setEnterPerson(String enterPerson) {
        this.enterPerson = enterPerson;
    }

    public String getEnterOp() {
        return enterOp;
    }

    public void setEnterOp(String enterOp) {
        this.enterOp = enterOp;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public Date getSearchStrartTime() {
        return searchStrartTime;
    }

    public void setSearchStrartTime(Date searchStrartTime) {
        this.searchStrartTime = searchStrartTime;
    }

    public Date getSearchEndTime() {
        return searchEndTime;
    }

    public void setSearchEndTime(Date searchEndTime) {
        this.searchEndTime = searchEndTime;
    }

    public String getStrartTime() {
        return strartTime;
    }

    public void setStrartTime(String strartTime) {
        this.strartTime = strartTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getBackupTime() {
        return backupTime;
    }

    public void setBackupTime(String backupTime) {
        this.backupTime = backupTime;
    }
}
