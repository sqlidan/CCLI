package com.haiersoft.ccli.wms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BIS_PLAN_TRANSPORT")
public class BisPlanTransport {

    private Integer id;
    private String planCode;
    private String planTime;
    private Integer planType;
    private Date planDate;

    private String client;
    private String clientName;

    private String billNum;
    private String ctnNum;
    private String skuId;
    private String idCode;
    private String cargoName;

    private String loadNum;
    private String quantity;

    private String carCode;
    private String checkState; // -1驳回 0未审核 1已计划审核 2已客服审核
    private Date checkDate;
    private String checkUser;

    private String isCreateOrder;

    private String description;

    private Date createDate;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CIQ_INFO")
    @SequenceGenerator(name = "SEQ_CIQ_INFO", sequenceName = "SEQ_CIQ_INFO", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PLAN_CODE")
    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    @Basic
    @Column(name = "CLIENT")
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Basic
    @Column(name = "CLIENT_NAME")
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Basic
    @Column(name = "BILL_NUM")
    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    @Basic
    @Column(name = "CTN_NUM")
    public String getCtnNum() {
        return ctnNum;
    }

    public void setCtnNum(String ctnNum) {
        this.ctnNum = ctnNum;
    }

    @Basic
    @Column(name = "SKU_ID")
    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    @Basic
    @Column(name = "LOAD_NUM")
    public String getLoadNum() {
        return loadNum;
    }

    public void setLoadNum(String loadNum) {
        this.loadNum = loadNum;
    }

    @Basic
    @Column(name = "QUANTITY")
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "CAR_CODE")
    public String getCarCode() {
        return carCode;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }

    @Basic
    @Column(name = "CHECK_STATE")
    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    @Basic
    @Column(name = "CHECK_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    @Basic
    @Column(name = "CHECK_USER")
    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    @Basic
    @Column(name = "IS_CREATE_ORDER")
    public String getIsCreateOrder() {
        return isCreateOrder;
    }

    public void setIsCreateOrder(String isCreateOrder) {
        this.isCreateOrder = isCreateOrder;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "PLAN_TYPE")
    public Integer getPlanType() {
        return planType;
    }

    public void setPlanType(Integer planType) {
        this.planType = planType;
    }

    @Basic
    @Column(name = "PLAN_TIME")
    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    @Basic
    @Column(name = "PLAN_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    @Basic
    @Column(name = "ID_CODE")
    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    @Basic
    @Column(name = "CARGO_NAME")
    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    @Override
    public String toString() {
        return "BisPlanTransport{" +
                "id=" + id +
                ", planCode='" + planCode + '\'' +
                ", client='" + client + '\'' +
                ", clientName='" + clientName + '\'' +
                ", billNum='" + billNum + '\'' +
                ", ctnNum='" + ctnNum + '\'' +
                ", skuId='" + skuId + '\'' +
                ", loadNum='" + loadNum + '\'' +
                ", quantity='" + quantity + '\'' +
                ", carCode='" + carCode + '\'' +
                ", checkState='" + checkState + '\'' +
                ", checkDate=" + checkDate +
                ", checkUser='" + checkUser + '\'' +
                ", isCreateOrder='" + isCreateOrder + '\'' +
                ", description='" + description + '\'' +
                ", planType=" + planType +
                ", planDate=" + planDate +
                ", planTime='" + planTime + '\'' +
                ", createDate=" + createDate +
                ", idCode='" + idCode + '\'' +
                ", cargoName='" + cargoName + '\'' +
                '}';
    }

}
