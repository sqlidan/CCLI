package com.haiersoft.ccli.cost.entity.finance;

import java.math.BigDecimal;
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

@Entity
@Table(name = "BIS_RECEIVABLE_FEE_STAT")
public class BisReceivableFeeStat implements java.io.Serializable {

    private static final long serialVersionUID = 3930482616321842915L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RECEIVABLE_FEE_STAT")
    @SequenceGenerator(name = "SEQ_RECEIVABLE_FEE_STAT", sequenceName = "SEQ_RECEIVABLE_FEE_STAT", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Excel(name = "客户名称")
    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Excel(name = "账期")
    @Column(name = "ACCOUNT_PERIOD")
    private String accountPeriod;

    @Excel(name = "统计日期", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @Column(name = "STAT_DATE")
    private Date statDate;

    @Excel(name = "费目代码")
    @Column(name = "FEE_CODE")
    private String feeCode;

    @Excel(name = "费目")
    @Column(name = "FEE_NAME")
    private String feeName;

    @Excel(name = "金额")
    @Column(name = "AMOUNT", precision = 18, scale = 4)
    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(String accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
