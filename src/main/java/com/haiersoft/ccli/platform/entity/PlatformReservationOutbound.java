package com.haiersoft.ccli.platform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author A0047794
 * @日期 2021/11/28
 * @描述
 */
@Entity
@Table(name = "PLATFORM_RESERVATION_OUTBOUND")
@DynamicUpdate
@DynamicInsert
public class PlatformReservationOutbound implements Serializable {

    private static final long serialVersionUID = 4040788502963084203L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "YYID")
    private String yyid;

     @Column(name = "CUSTOMER_SERVICE")
    private String customerService;

     @Column(name = "APPOINT_DATE")
     @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
     private Date appointDate;

     @Column(name = "CONSUME_COMPANY")
    private String consumeCompany;

     @Column(name = "BILL_NO")
    private String billNo;

     @Column(name = "CONTAINER_NO")
    private String containerNo;

     @Column(name = "ORIGIN_COUNTRY")
    private String originCountry;

     @Column(name = "PRODUCT_TYPE")
    private String productType;

     @Column(name = "PRODUCT_NAME")
    private String productName;

     @Column(name = "NUM")
    private String num;

       @Column(name = "WEIGHT")
    private Double weight;

       @Column(name = "CAR_NUMBER")
    private String carNumber;

       @Column(name = "DRIVER_NAME")
    private String driverName;

       @Column(name = "DRIVER_MOBILE")
    private String driverMobile;

       @Column(name = "DRIVER_ID_NUMBER")
    private String dirverIdNumber;

       @Column(name = "DES_COMPANY")
    private String desCompany;

       @Column(name = "PROVINCE")
    private String province;

       @Column(name = "CITY")
    private String city;

       @Column(name = "AREA")
    private String area;

       @Column(name = "ADDRESS")
    private String address;

       @Column(name = "DES_CONTACT_NAME")
       private String desContactName;

       @Column(name = "DES_CONTACT_PHONE")
       private String desContactPhone;

       @Column(name = "QUEUING_TIME")
       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
       private Date queuingTime;

       @Column(name = "LOCATION_NO")
       private String locationNo;

    //库号
       @Column(name = "ROOM_NUM")
       private String roomNum;

       @Column(name = "CREATED_TIME")
       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
       private Date createdTime;

       @Column(name = "UPDATED_TIME")
       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
       private Date updatedTime;

       @Column(name = "DELETED_FLAG")
        private Integer deletedFlag;
       @Column(name = "STATUS")
       private String status;
       //  是否拆分
       @Column(name = "IS_SPLIT")
       private String isSplit;

       @Column(name = "MANIFEST_ID")
       public String ManifestId;

       @Column(name = "LOCAL_STATUS")
       public String LocalStatus;
       //生成的报文名称
       @Column(name = "FILE_NAME")
       public String fileName;
       //0:已申报   1:申报成功   2：申报失败
       @Column(name = "IS_SUCCESS")
        public String isSuccess;

        @Column(name = "IS_BULK_CARGO")
        public String isBulkCargo;

    @Column(name = "TYPE")
    private  String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManifestId() {
        return ManifestId;
    }

    public void setManifestId(String manifestId) {
        ManifestId = manifestId;
    }

    public String getLocalStatus() {
        return LocalStatus;
    }

    public void setLocalStatus(String localStatus) {
        LocalStatus = localStatus;
    }

    public String getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(String isSplit) {
        this.isSplit = isSplit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYyid() {
        return yyid;
    }

    public void setYyid(String yyid) {
        this.yyid = yyid;
    }

    public String getCustomerService() {
        return customerService;
    }

    public void setCustomerService(String customerService) {
        this.customerService = customerService;
    }

    public Date getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(Date appointDate) {
        this.appointDate = appointDate;
    }

    public String getConsumeCompany() {
        return consumeCompany;
    }

    public void setConsumeCompany(String consumeCompany) {
        this.consumeCompany = consumeCompany;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public String getDirverIdNumber() {
        return dirverIdNumber;
    }

    public void setDirverIdNumber(String dirverIdNumber) {
        this.dirverIdNumber = dirverIdNumber;
    }

    public String getDesCompany() {
        return desCompany;
    }

    public void setDesCompany(String desCompany) {
        this.desCompany = desCompany;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesContactName() {
        return desContactName;
    }

    public void setDesContactName(String desContactName) {
        this.desContactName = desContactName;
    }

    public String getDesContactPhone() {
        return desContactPhone;
    }

    public void setDesContactPhone(String desContactPhone) {
        this.desContactPhone = desContactPhone;
    }

    public Date getQueuingTime() {
        return queuingTime;
    }

    public void setQueuingTime(Date queuingTime) {
        this.queuingTime = queuingTime;
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getIsBulkCargo() {
        return isBulkCargo;
    }

    public void setIsBulkCargo(String isBulkCargo) {
        this.isBulkCargo = isBulkCargo;
    }
}
