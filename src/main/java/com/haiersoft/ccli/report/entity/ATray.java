package com.haiersoft.ccli.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "A_TRAY")
public class ATray implements Serializable {

    private static final long serialVersionUID = 2799111007612445959L;

    @Id
    @Column(name = "ID")
    private Integer id; //ID

    @Column(name = "BILLCODE")
    private String billCode; //提单号

    @Column(name = "NOWNUM")
    private BigDecimal nowNum; //现有数量

    @Column(name = "ALLNET")
    private BigDecimal allnet;//总净重

    @Column(name = "ALLGROSS")
    private BigDecimal allgross;//总毛重

    @Column(name = "XH")
    private BigDecimal xh;//项号

    @Column(name = "CREATESQD")
    private Integer createsqd;//生成申请单标识

    @Column(name = "CREATESB")
    private Integer createsb;//申报标识

    @Column(name = "CREATEXNHFD")
    private Integer createxnhfd;//生成虚拟核放单标识

    @Column(name = "CREATEDHQR")
    private Integer createdhqr;//到货确认标识

    @Transient
    private String linkId; //业务单号

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public BigDecimal getNowNum() {
        return nowNum;
    }

    public void setNowNum(BigDecimal nowNum) {
        this.nowNum = nowNum;
    }

    public BigDecimal getAllnet() {
        return allnet;
    }

    public void setAllnet(BigDecimal allnet) {
        this.allnet = allnet;
    }

    public BigDecimal getAllgross() {
        return allgross;
    }

    public void setAllgross(BigDecimal allgross) {
        this.allgross = allgross;
    }

    public BigDecimal getXh() {
        return xh;
    }

    public void setXh(BigDecimal xh) {
        this.xh = xh;
    }

    public Integer getCreatesqd() {
        return createsqd;
    }

    public void setCreatesqd(Integer createsqd) {
        this.createsqd = createsqd;
    }

    public Integer getCreatesb() {
        return createsb;
    }

    public void setCreatesb(Integer createsb) {
        this.createsb = createsb;
    }

    public Integer getCreatexnhfd() {
        return createxnhfd;
    }

    public void setCreatexnhfd(Integer createxnhfd) {
        this.createxnhfd = createxnhfd;
    }

    public Integer getCreatedhqr() {
        return createdhqr;
    }

    public void setCreatedhqr(Integer createdhqr) {
        this.createdhqr = createdhqr;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }
}
