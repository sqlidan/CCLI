package com.haiersoft.ccli.platform.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author A0047794
 * @日期 2021/12/9
 * @描述
 */

public class StationCurrentInfo implements Serializable {

    private String platform;

    private Integer platformNo;

    private String warehouseNo;

    private String platformStatus;

    private String platformType;

    private String platNo;

    private String ctnNum;



    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getPlatNo() {
        return platNo;
    }

    public void setPlatNo(String platNo) {
        this.platNo = platNo;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public Integer getPlatformNo() {
        return platformNo;
    }

    public String getCtnNum() {
        return ctnNum;
    }

    public void setCtnNum(String ctnNum) {
        this.ctnNum = ctnNum;
    }

    public void setPlatformNo(Integer platformNo) {
        this.platformNo = platformNo;
    }
}
