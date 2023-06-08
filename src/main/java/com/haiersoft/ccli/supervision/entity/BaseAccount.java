package com.haiersoft.ccli.supervision.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 黄瑞乾
 * @日期 2023/3/23
 * @描述
 */
@Entity
@Table(name = "BASE_ACCOUNT")
@DynamicUpdate
@DynamicInsert
public class BaseAccount implements Serializable {

    private static final long serialVersionUID = 4040788502963084203L;


    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BASE_ACCOUNT")
    @SequenceGenerator(name = "SEQ_BASE_ACCOUNT", sequenceName = "SEQ_BASE_ACCOUNT", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    /**
     * 提单号
     */
    @Column(name = "BILL_NO")
    @Excel(name = "提单号")
    private String billNo;

    /**
     * 箱号
     */
    @Column(name = "CONTAINER_NO")
    @Excel(name = "箱号")
    private String containerNo;

    @Column(name = "PRODUCT_TYPE")
    @Excel(name = "货类")
    private String productType;

    @Column(name = "PRODUCT_NAME")
    @Excel(name = "货物名称(品名)")
    private String productName;

    @Column(name = "NUM")
    @Excel(name = "件数")
    private String num;

    @Column(name = "WEIGHT")
    @Excel(name = "重量")
    private String weight;

    /**
     * 库存id
     */
    @Column(name = "STOCK_ID")
    private String stockId;

    /**
     * 0:入库，1：出库
     */
    @Column(name = "STOCK_TYPE")
    @Excel(name = "库存类型")
    private String stockType;

    /**
     * 创建时间
     */
    @Column(name = "CREATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "UPDATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date updatedTime;

    /**
     * 备注
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 是否拆分
     */
    @Column(name = "IS_SPLIT")
    private String isSplit;


    //pojo字段

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Excel(name = "入库日期")
    private Date appointDate;
    @Excel(name = "状态")
    private String status;

    private String rSumnum;
    //原申请重量
    private String rSumweight;
    //剩余件数
    private String surplusNum;
    //剩余重量
    private String surplusWeight;
    //项号
    private Integer xid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(String isSplit) {
        this.isSplit = isSplit;
    }

    public Date getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(Date appointDate) {
        this.appointDate = appointDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getrSumnum() {
        return rSumnum;
    }

    public void setrSumnum(String rSumnum) {
        this.rSumnum = rSumnum;
    }

    public String getrSumweight() {
        return rSumweight;
    }

    public void setrSumweight(String rSumweight) {
        this.rSumweight = rSumweight;
    }

    public String getSurplusNum() {
        return surplusNum;
    }

    public void setSurplusNum(String surplusNum) {
        this.surplusNum = surplusNum;
    }

    public String getSurplusWeight() {
        return surplusWeight;
    }

    public void setSurplusWeight(String surplusWeight) {
        this.surplusWeight = surplusWeight;
    }

    public Integer getXid() {
        return xid;
    }

    public void setXid(Integer xid) {
        this.xid = xid;
    }
}
