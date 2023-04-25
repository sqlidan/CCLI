package com.haiersoft.ccli.platform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Persister;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */


@Entity
@Table(name = "PLATFORM_QUEUE")
@DynamicUpdate
@DynamicInsert
public class VehicleQueue implements Serializable {
    private static final long serialVersionUID = 4040788502963084203L;

    @Id
    @GeneratedValue(generator = "vehicleQueueGenerator")
    @GenericGenerator(name = "vehicleQueueGenerator", strategy = "uuid")
    @Column(name = "ID",unique = true)
    private String id;
    @Column(name = "WAREHOUSE_NO")
    private String warehouseNo;
    @Column(name = "PLAT_NO")
    private String platNo;
    @Column(name = "CONTAINER_NO")
    private String containerNo;
    @Column(name = "PLATFORM_NO")
    private String platformNo;
    @Column(name = "PLATFORM_ID")
    private Integer platformId;
    @Column(name = "PLATFORM_NAME")
    private String platformName;
    @Column(name = "YYID")
    private String yyid;
    @Column(name = "INOUT_BOUND_FLAG")
    private String inoutBoundFlag;
    @Column(name = "AUTO_MANUAL_FLAG")
    private String autoManualFlag;
    @Column(name = "QUEUE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date queueTime;
    @Column(name = "STATUS_FLAG")
    private String statusFlag;
    @Column(name = "DELETED_FLAG")
    private String deletedFlag;
    @Column(name = "CREATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createdTime;
    @Column(name = "UPDATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date updatedTime;

    @Transient
    private String colorType;

    public void setColorType(String colorType) {
        this.colorType = colorType;
    }

    public String getColorType() {
        return colorType;
    }

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

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
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

    public Date getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(Date queueTime) {
        this.queueTime = queueTime;
    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
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

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getYyid() {
        return yyid;
    }

    public void setYyid(String reservationId) {
        this.yyid = reservationId;
    }
}
