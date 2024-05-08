package com.haiersoft.ccli.wms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "AUDIT_RECORD")
public class AuditRecord implements Serializable {
    private static final long serialVersionUID = 4151326454254563334L;

    @Id
    @Column(name = "OPER_CODE", unique = true, nullable = false)
    private String operCode;

    @Column(name = "CUSTOMER_CODE")
    private String customerCode;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "OPER_TYPE")
    private String operType;

    @Column(name = "MK_DD")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date mkDd;

    @Column(name = "EXEC_DD")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date execDd;

    @Column(name = "OPER_STATE")
    private String operState;

    @Column(name = "REJECT_REASON")
    private String rejectReason;

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public Date getMkDd() {
        return mkDd;
    }

    public void setMkDd(Date mkDd) {
        this.mkDd = mkDd;
    }

    public Date getExecDd() {
        return execDd;
    }

    public void setExecDd(Date execDd) {
        this.execDd = execDd;
    }

    public String getOperState() {
        return operState;
    }

    public void setOperState(String operState) {
        this.operState = operState;
    }
}
