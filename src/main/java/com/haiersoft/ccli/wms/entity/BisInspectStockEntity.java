package com.haiersoft.ccli.wms.entity;
import java.sql.Time;

/**
 * Created by galaxy on 2017/5/2.
 */
public class BisInspectStockEntity {

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

    public String getInspectId() {
        return inspectId;
    }

    public void setInspectId(String inspectId) {
        this.inspectId = inspectId;
    }

    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
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

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Long getInspectTotal() {
        return inspectTotal;
    }

    public void setInspectTotal(Long inspectTotal) {
        this.inspectTotal = inspectTotal;
    }

    public Long getOperateState() {
        return operateState;
    }

    public void setOperateState(Long operateState) {
        this.operateState = operateState;
    }

    public Time getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Time operateDate) {
        this.operateDate = operateDate;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public Time getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Time createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

}
