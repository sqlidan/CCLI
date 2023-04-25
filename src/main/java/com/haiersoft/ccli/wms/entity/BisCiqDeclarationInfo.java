package com.haiersoft.ccli.wms.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * BisCiqDeclarationInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_CIQ_DECLARATION_INFO")
public class BisCiqDeclarationInfo implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5718947032772639724L;

	// Fields
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CIQ_INFO")
    @SequenceGenerator(name = "SEQ_CIQ_INFO", sequenceName = "SEQ_CIQ_INFO", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id; //ID

    @Column(name = "CIQ_ID")
    private String ciqId; //主表ID

    @Column(name = "CIQ_NUM")
    @Excel(name = "海关编码")
    private String ciqNum; //(海关编码)报检单号

    @Column(name = "CARGO_NAME")
    @Excel(name = "商品名称")
    private String cargoName; //商品名称

    @Column(name = "SCALAR")
    @Excel(name = "件数")
    private Integer scalar; //件数

    @Column(name = "NET_WEIGHT")
    @Excel(name = "净重")
    private Double netWeight; //净重

    @Column(name = "BAG_TYPE")
    @Excel(name = "包装种类")
    private String bagType;  //包装种类

    @Column(name = "PRICE")
    @Excel(name = "货值")
    private Double price;  //货值

    @Column(name = "RECORD_MAN")
    @Excel(name = "录入员")
    private String recordMan; //录入员

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "RECORD_TIME")
    @Excel(name = "录入时间")
    private Date recordTime;  //录入时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "UPDATE_TIME")
    private Date updateTime;  //修改时间

    @Column(name = "REMARK1")
    @Excel(name = "备注")
    private String remark1; //备注1

    @Column(name = "REMARK2")
    private String remark2;//备注2

    @Column(name = "REMARK3")
    private String remark3;//备注3

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCiqId() {
        return ciqId;
    }

    public void setCiqId(String ciqId) {
        this.ciqId = ciqId;
    }

    public String getCiqNum() {
        return ciqNum;
    }

    public void setCiqNum(String ciqNum) {
        this.ciqNum = ciqNum;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public Integer getScalar() {
        return scalar;
    }

    public void setScalar(Integer scalar) {
        this.scalar = scalar;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public String getBagType() {
        return bagType;
    }

    public void setBagType(String bagType) {
        this.bagType = bagType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRecordMan() {
        return recordMan;
    }

    public void setRecordMan(String recordMan) {
        this.recordMan = recordMan;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }


}