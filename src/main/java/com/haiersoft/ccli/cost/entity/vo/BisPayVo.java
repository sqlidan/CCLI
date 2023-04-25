package com.haiersoft.ccli.cost.entity.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

//上传订单  用来将数据 上传至中台 时 先传订单
@Entity
public class BisPayVo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String origBizId;  //原始业务编号

    private String customerCode; // 客户编码，月结的业务原则上必填，现结的可以不填

    private String supplierCode; //供应商编码

    private String costCenterCode; // 	成本中心code

    private String descn;  //业务描述

    private String  bizTime; // 业务发生时间

    private String tradeType; //内外贸 N:内贸 W：外贸

    private String  operator; //操作员code（必须在单点登录系统存在

    private String salesman; // 业务员

    private String orgCode;//组织编码（由财务中台分配）
    private String status; //状态是否 上传


    public BisPayVo() {

    }

    public BisPayVo(Long id, String origBizId, String customerCode, String supplierCode, String costCenterCode, String descn, String bizTime, String tradeType, String operator, String salesman, String orgCode,String status) {
        this.id = id;
        this.origBizId = origBizId;
        this.customerCode = customerCode;
        this.supplierCode = supplierCode;
        this.costCenterCode = costCenterCode;
        this.descn = descn;
        this.bizTime = bizTime;
        this.tradeType = tradeType;
        this.operator = operator;
        this.salesman = salesman;
        this.orgCode = orgCode;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getOrigBizId() {
        return origBizId;
    }

    public void setOrigBizId(String origBizId) {
        this.origBizId = origBizId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getCostCenterCode() {
        return costCenterCode;
    }

    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public String getBizTime() {
        return bizTime;
    }

    public void setBizTime(String bizTime) {
        this.bizTime = bizTime;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Override
    public String toString() {
        return "BisPayVo{" +
                "id=" + id +
                ", origBizId='" + origBizId + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", supplierCode='" + supplierCode + '\'' +
                ", costCenterCode='" + costCenterCode + '\'' +
                ", descn='" + descn + '\'' +
                ", bizTime=" + bizTime +
                ", tradeType='" + tradeType + '\'' +
                ", operator='" + operator + '\'' +
                ", salesman='" + salesman + '\'' +
                ", orgCode='" + orgCode + '\'' +
                '}';
    }
}
