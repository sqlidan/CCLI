package com.haiersoft.ccli.platform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.cxf.databinding.DataReader;
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
 * @日期 2021/12/9
 * @描述
 */

@Entity
@Table(name = "PLATFORM_ALLOCATION_LOG")
@DynamicUpdate
@DynamicInsert
public class PlatformAllocationLog implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "PLATFORM_ID")
    private String platformId;

    @Column(name = "QUEUE_ID")
    private String queueId;

    @Column(name = "PLATFORM_NO")
    private String platformNo;

    @Column(name = "PLATFORM_NAME")
    private String platformName;

    @Column(name = "INOUT_BOUND_FLAG")
    private String inoutBoundFlag;

    @Column(name = "BUSINESS_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date businessDate;

    @Column(name = "WAREHOUSE_NO")
    private String warehouseNo;

    @Column(name = "PLAT_NO")
    private String platNo;

    @Column(name = "CONTAINER_NO")
    private String containerNo;

    @Column(name = "DELETED_FLAG")
    private String deleteFlag;

    @Column(name = "CREATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createdTime;

    @Column(name = "UPDATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private String updatedTime;


    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public String getInoutBoundFlag() {
        return inoutBoundFlag;
    }

    public void setInoutBoundFlag(String inoutBoundFlag) {
        this.inoutBoundFlag = inoutBoundFlag;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getPlatNo() {
        return platNo;
    }

    public void setPlatNo(String platNo) {
        this.platNo = platNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }
}
