package com.haiersoft.ccli.platform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author A0047794
 * @日期 2021/11/30
 * @描述
 */


@Entity
@Table(name = "PLATFORM_VEHICLE_CHECK")
@DynamicUpdate
@DynamicInsert
public class VehicleCheck {

    private static final long serialVersionUID = 4040788502963084203L;

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "BILL_NO")
    private String billNo;

    @Column(name = "CONTAINER_NO")
    private String containerNo;

    @Column(name = "VEHICLE_NO")
    private String vehicleNo;

    @Column(name = "YYID")
    private String yyid;

    @Column(name = "GATE_ENTRY_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date gateEntryTime;

    @Column(name = "CHECK_STATUS")
    private String checkStatus;

    @Column(name = "CHECK_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date checkTime;

    @Column(name = "DELETED_FLAG")
    private String deletedFlag;

    @Column(name = "CREATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createdTime;

    @Column(name = "UPDATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date updatedTime;
    @Column(name = "ACTUAL_VEHICLE_NO")
    private String actualVehicleNo;

    public String getActualVehicleNo() {
        return actualVehicleNo;
    }

    public void setActualVehicleNo(String actualVehicleNo) {
        this.actualVehicleNo = actualVehicleNo;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String platNo) {
        this.vehicleNo = platNo;
    }

    public Date getGateEntryTime() {
        return gateEntryTime;
    }

    public void setGateEntryTime(Date gateEntryTime) {
        this.gateEntryTime = gateEntryTime;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
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

    public String getYyid() {
        return yyid;
    }

    public void setYyid(String reservationId) {
        this.yyid = reservationId;
    }
}
