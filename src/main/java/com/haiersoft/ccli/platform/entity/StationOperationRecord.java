package com.haiersoft.ccli.platform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */


@Entity
@Table(name = "PLATFORM_OPERATION_LOG")
@DynamicUpdate
public class StationOperationRecord {

    private static final long serialVersionUID = 4040788502963084203L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "QUEUE_ID")
    private String queueId;

    @Column(name = "PLATFORM_ID")
    private Integer platformId;

    @Column(name = "PLATFORM_NO")
    private String platformNo;

    @Column(name = "PLATFORM_NAME")
    private String platformName;

    @Column(name = "INOUT_BOUND_FLAG")
    private String inoutBoundFlag;

    @Column(name = "AUTO_MANUAL_FLAG")
    private String autoManualFlag;

    @Column(name = "START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date startTime;

    @Column(name = "END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date endTime;

    @Column(name = "ARRIVE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date arriveTime;

    @Column(name = "LEAVE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date leaveTime;

    @Column(name = "WAREHOUSE_NO")
    private String warehouseNo;

    @Column(name = "PLAT_NO")
    private String platNo;

    @Column(name = "CONTAINER_NO")
    private String containerNo;

    @Column(name = "DELETED_FLAG")
    private String deletedFlag;

    @Column(name = "CREATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createdTime;

    @Column(name = "UPDATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date updatedTime;

    @Column(name = "YYID")
    private String yyid;

    public String getYyid() {
        return yyid;
    }

    public void setYyid(String yyid) {
        this.yyid = yyid;
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

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
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

    public String getAutoManualFlag() {
        return autoManualFlag;
    }

    public void setAutoManualFlag(String autoManualFlag) {
        this.autoManualFlag = autoManualFlag;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
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

    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
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

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
