package com.haiersoft.ccli.wms.entity;

import javax.persistence.*;
import java.sql.Time;

/**
 * Created by galaxy on 2017/5/3.
 */
@Entity
@Table(name = "BIS_INSPECT_STOCK")
public class BisInspectStock {

    private String inspectId;
    private String billNum;
    private String ctnNum;
    private String skuId;
    private String stockName;
    private Long inspectTotal;
    private Long operateState;
    private Time operateDate;
    private String operateUserName;
    private String operateUserId;
    private Time createDate;
    private String createUserName;
    private String createUserId;
    private String description;
    private String trayId;
    private String warehouse;
    private String cargoName;
    private String contactNum;
    private String asn;

    @Basic
    @Id
    @Column(name = "INSPECT_ID")
    public String getInspectId() {
        return inspectId;
    }

    public void setInspectId(String inspectId) {
        this.inspectId = inspectId;
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
    @Column(name = "STOCK_NAME")
    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Basic
    @Column(name = "INSPECT_TOTAL")
    public Long getInspectTotal() {
        return inspectTotal;
    }

    public void setInspectTotal(Long inspectTotal) {
        this.inspectTotal = inspectTotal;
    }

    @Basic
    @Column(name = "OPERATE_STATE")
    public Long getOperateState() {
        return operateState;
    }

    public void setOperateState(Long operateState) {
        this.operateState = operateState;
    }

    @Basic
    @Column(name = "OPERATE_DATE")
    public Time getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Time operateDate) {
        this.operateDate = operateDate;
    }

    @Basic
    @Column(name = "OPERATE_USER_NAME")
    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }

    @Basic
    @Column(name = "OPERATE_USER_ID")
    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    @Basic
    @Column(name = "CREATE_DATE")
    public Time getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Time createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "CREATE_USER_NAME")
    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    @Basic
    @Column(name = "CREATE_USER_ID")
    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BisInspectStock that = (BisInspectStock) o;

        if (inspectId != null ? !inspectId.equals(that.inspectId) : that.inspectId != null) return false;
        if (billNum != null ? !billNum.equals(that.billNum) : that.billNum != null) return false;
        if (ctnNum != null ? !ctnNum.equals(that.ctnNum) : that.ctnNum != null) return false;
        if (skuId != null ? !skuId.equals(that.skuId) : that.skuId != null) return false;
        if (stockName != null ? !stockName.equals(that.stockName) : that.stockName != null) return false;
        if (inspectTotal != null ? !inspectTotal.equals(that.inspectTotal) : that.inspectTotal != null) return false;
        if (operateState != null ? !operateState.equals(that.operateState) : that.operateState != null) return false;
        if (operateDate != null ? !operateDate.equals(that.operateDate) : that.operateDate != null) return false;
        if (operateUserName != null ? !operateUserName.equals(that.operateUserName) : that.operateUserName != null)
            return false;
        if (operateUserId != null ? !operateUserId.equals(that.operateUserId) : that.operateUserId != null)
            return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (createUserName != null ? !createUserName.equals(that.createUserName) : that.createUserName != null)
            return false;
        if (createUserId != null ? !createUserId.equals(that.createUserId) : that.createUserId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inspectId != null ? inspectId.hashCode() : 0;
        result = 31 * result + (billNum != null ? billNum.hashCode() : 0);
        result = 31 * result + (ctnNum != null ? ctnNum.hashCode() : 0);
        result = 31 * result + (skuId != null ? skuId.hashCode() : 0);
        result = 31 * result + (stockName != null ? stockName.hashCode() : 0);
        result = 31 * result + (inspectTotal != null ? inspectTotal.hashCode() : 0);
        result = 31 * result + (operateState != null ? operateState.hashCode() : 0);
        result = 31 * result + (operateDate != null ? operateDate.hashCode() : 0);
        result = 31 * result + (operateUserName != null ? operateUserName.hashCode() : 0);
        result = 31 * result + (operateUserId != null ? operateUserId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createUserName != null ? createUserName.hashCode() : 0);
        result = 31 * result + (createUserId != null ? createUserId.hashCode() : 0);
        return result;
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
    @Column(name = "TRAY_ID")
    public String getTrayId() {
        return trayId;
    }

    public void setTrayId(String trayId) {
        this.trayId = trayId;
    }

    @Basic
    @Column(name = "WAREHOUSE")
    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    @Basic
    @Column(name = "CARGO_NAME")
    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    @Basic
    @Column(name = "CONTACT_NUM")
    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    @Basic
    @Column(name = "ASN")
    public String getAsn() {
        return asn;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }

}
