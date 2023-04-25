package com.haiersoft.ccli.wms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 闸口管理实体类
 */
public class GateEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5191321476756904012L;
	private String ctnNum;      // 箱号
    private String ctnVersion;  // 箱型
    private String ctnSize;     // 尺寸

    private String billNum;     // 提单号
    private String asn;         // asn号

    private Integer positionId; // 场位id
    private String position;    // 场位
    private String clientId;    // 装卸单位id
    private String clientName;  // 装卸单位名

    private String carNum;      // 车牌号
    private String driverName;  // 司机名

    private String outCarNum;      // 车牌号
    private String outDriverName;  // 司机名

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createDate;  // 录入时间

    private Date startDate;
    private Date endDate;

    public String getCtnNum() {
        return ctnNum;
    }

    public void setCtnNum(String ctnNum) {
        this.ctnNum = ctnNum;
    }

    public String getCtnVersion() {
        return ctnVersion;
    }

    public void setCtnVersion(String ctnVersion) {
        this.ctnVersion = ctnVersion;
    }

    public String getCtnSize() {
        return ctnSize;
    }

    public void setCtnSize(String ctnSize) {
        this.ctnSize = ctnSize;
    }

    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    public String getAsn() {
        return asn;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOutCarNum() {
        return outCarNum;
    }

    public void setOutCarNum(String outCarNum) {
        this.outCarNum = outCarNum;
    }

    public String getOutDriverName() {
        return outDriverName;
    }

    public void setOutDriverName(String outDriverName) {
        this.outDriverName = outDriverName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
