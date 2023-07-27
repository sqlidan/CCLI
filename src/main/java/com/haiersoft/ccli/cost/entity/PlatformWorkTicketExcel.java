package com.haiersoft.ccli.cost.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.platform.entity.PlatformReservationInbound;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.platform.entity.VehicleQueue;
import com.haiersoft.ccli.system.entity.User;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;


public class PlatformWorkTicketExcel {




    private String yyid;


    @Excel(name = "日期",format = "yyyy-MM-dd")
    private Date createdTime;//创建时间


    @Excel(name = "类型", replace = {"入库_1", "出库_2"})
    private String inOutBoundFlag;



    @Excel(name = "ASN/装车号")
    private String asnTransNum;

    @Excel(name = "品名")
    private String productName;


    @Excel(name = "箱号")
    private String containerNo;

    @Excel(name = "车号")
    private String platNo;


    @Excel(name = "作业吨")
    private Double weight;

    @Excel(name = "作业岗位")
    private String post;


    @Excel(name = "操作吨位")
    private Double operationWeight;

    @Excel(name = "实际操作")
    private Double actualWeightx;

    @Excel(name = "系数")
    private String numPlus;

    @Excel(name = "姓名")
    private String name;



    public String getYyid() {
        return yyid;
    }

    public void setYyid(String yyid) {
        this.yyid = yyid;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }
    public String getPlatNo() {
        return platNo;
    }

    public void setPlatNo(String platNo) {
        this.platNo = platNo;
    }

    public String getInOutBoundFlag() {
        return inOutBoundFlag;
    }

    public void setInOutBoundFlag(String inOutBoundFlag) {
        this.inOutBoundFlag = inOutBoundFlag;
    }

    public String getAsnTransNum() {
        return asnTransNum;
    }

    public void setAsnTransNum(String asnTransNum) {
        this.asnTransNum = asnTransNum;
    }



    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }



    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getActualWeightx() {
        return actualWeightx;
    }

    public void setActualWeightx(Double actualWeightx) {
        this.actualWeightx = actualWeightx;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Double getOperationWeight() {
        return operationWeight;
    }

    public void setOperationWeight(Double operationWeight) {
        this.operationWeight = operationWeight;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNumPlus() {
        return numPlus;
    }

    public void setNumPlus(String numPlus) {
        this.numPlus = numPlus;
    }
}
