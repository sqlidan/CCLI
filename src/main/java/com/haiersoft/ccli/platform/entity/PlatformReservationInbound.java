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
@Table(name = "PLATFORM_RESERVATION_INBOUND")
@DynamicUpdate
@DynamicInsert
public class PlatformReservationInbound implements Serializable {
    private static final long serialVersionUID = 4040788502963084203L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "CUSTOMER_SERVICE")
    private String customerService;

    @Column(name = "CONSUME_COMPANY")
    private String consumeCompany;

    @Column(name = "BILL_NO")
    private String billNo;

    @Column(name = "CONTAINER_NO")
    private String containerNo;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_TYPE")
    private String productType;

    @Column(name = "NUM")
    private String num;

    @Column(name = "NET_WEIGHT")
    private Double netWeight;

    @Column(name = "APPOINT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date appointDate;

    @Column(name = "STORAGE_TEMPERATURE")
    private String storageTemperature;

    @Column(name = "IS_CHECK")
    private String isCheck;

    @Column(name = "IS_FREETAX")
    private String isFreetax;

    @Column(name = "IS_ZDMT")
    private String isZdmt;

    @Column(name = "SEAL_NO")
    private String sealNo;

    @Column(name = "ORIGIN_COUNTRY")
    private String originCountry;

    @Column(name = "FACTORY_NO")
    private String factoryNo;

    @Column(name = "REPORT_NUMBER")
    private String reportNumber;

    @Column(name = "SHIPPING_PORT")
    private String shippingPort;

    @Column(name = "TXMTGS")
    private String txmtgs;

    @Column(name = "ARRIVE_PORT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date arrivePortTime;

    @Column(name = "CUSTOMS_CLEARANCE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date customsClearanceTime;
    @Column(name = "REPORT_BILL_CONTACT")
    private String reportBillContact;
    @Column(name = "REPORT_BILL_CONSIGNOR")
    private String reportBillConsignor;
    @Column(name = "CHECK_INSTRUCTIONS")
    private String checkInstructions;
    @Column(name = "DGFKCZ")
    private String dgfkcz;
    @Column(name = "CAR_NUMBER")
    private String carNumber;
     @Column(name = "DRIVER_NAME")
        private String driverName;
     @Column(name = "DRIVER_MOBILE")
        private String driverMobile;
     @Column(name = "DRIVER_ID_NUMBER")
        private String driverIdNumber;
     @Column(name = "CUSTOMS_DECLARATION_VALUE")
        private String customsDeclarationValue;
     @Column(name = "DISINFECTION_REPORT_CUSTOMER")
        private String disinfectionReportCustomer;
     @Column(name = "NUCLEIC_ACID_PROOF_CUSTOMER")
        private String nucleicAcidProofCustomer;
     @Column(name = "YYID")
        private String yyid;
     @Column(name = "QUEUING_TIME")
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
     private Date queuingTime;
     @Column(name = "CREATED_TIME")
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
        private Date createdTime ;
     @Column(name = "UPDATED_TIME")
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
        private Date updatedTime;
     @Column(name = "DELETED_FLAG")
        private Integer delFlag;
     @Column(name = "STATUS")
     private  String status;
    @Column(name = "TYPE")
     private  String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCustomerService() {
        return customerService;
    }

    public void setCustomerService(String customerService) {
        this.customerService = customerService;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Date getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(Date appointDate) {
        this.appointDate = appointDate;
    }

    public String getStorageTemperature() {
        return storageTemperature;
    }

    public void setStorageTemperature(String storageTemperature) {
        this.storageTemperature = storageTemperature;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getIsFreetax() {
        return isFreetax;
    }

    public void setIsFreetax(String isFreetax) {
        this.isFreetax = isFreetax;
    }

    public String getIsZdmt() {
        return isZdmt;
    }

    public void setIsZdmt(String isZdmt) {
        this.isZdmt = isZdmt;
    }

    public String getSealNo() {
        return sealNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getFactoryNo() {
        return factoryNo;
    }

    public void setFactoryNo(String factoryNo) {
        this.factoryNo = factoryNo;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getShippingPort() {
        return shippingPort;
    }

    public void setShippingPort(String shippingPort) {
        this.shippingPort = shippingPort;
    }

    public String getTxmtgs() {
        return txmtgs;
    }

    public void setTxmtgs(String txmtgs) {
        this.txmtgs = txmtgs;
    }

    public Date getArrivePortTime() {
        return arrivePortTime;
    }

    public void setArrivePortTime(Date arrivePortTime) {
        this.arrivePortTime = arrivePortTime;
    }

    public Date getCustomsClearanceTime() {
        return customsClearanceTime;
    }

    public void setCustomsClearanceTime(Date customsClearanceTime) {
        this.customsClearanceTime = customsClearanceTime;
    }

    public String getReportBillContact() {
        return reportBillContact;
    }

    public void setReportBillContact(String reportBillContact) {
        this.reportBillContact = reportBillContact;
    }

    public String getReportBillConsignor() {
        return reportBillConsignor;
    }

    public void setReportBillConsignor(String reportBillConsignor) {
        this.reportBillConsignor = reportBillConsignor;
    }

    public String getCheckInstructions() {
        return checkInstructions;
    }

    public void setCheckInstructions(String checkInstructions) {
        this.checkInstructions = checkInstructions;
    }

    public String getDgfkcz() {
        return dgfkcz;
    }

    public void setDgfkcz(String dgfkcz) {
        this.dgfkcz = dgfkcz;
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

    public String getDriverIdNumber() {
        return driverIdNumber;
    }

    public void setDriverIdNumber(String driverIdNumber) {
        this.driverIdNumber = driverIdNumber;
    }

    public String getCustomsDeclarationValue() {
        return customsDeclarationValue;
    }

    public void setCustomsDeclarationValue(String customsDeclarationValue) {
        this.customsDeclarationValue = customsDeclarationValue;
    }

    public String getDisinfectionReportCustomer() {
        return disinfectionReportCustomer;
    }

    public void setDisinfectionReportCustomer(String disinfectionReportCustomer) {
        this.disinfectionReportCustomer = disinfectionReportCustomer;
    }

    public String getNucleicAcidProofCustomer() {
        return nucleicAcidProofCustomer;
    }

    public void setNucleicAcidProofCustomer(String nucleicAcidProofCustomer) {
        this.nucleicAcidProofCustomer = nucleicAcidProofCustomer;
    }

    public String getYyid() {
        return yyid;
    }

    public void setYyid(String yyid) {
        this.yyid = yyid;
    }

    public Date getQueuingTime() {
        return queuingTime;
    }

    public void setQueuingTime(Date queuingTime) {
        this.queuingTime = queuingTime;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
