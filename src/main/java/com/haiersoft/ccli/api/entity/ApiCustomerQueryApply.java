package com.haiersoft.ccli.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author
 * @ClassName:
 * @Description:
 * @date
 */
@Entity
@Table(name = "API_CUSTOMER_QUERY_APPLY")
@DynamicUpdate
@DynamicInsert
public class ApiCustomerQueryApply implements Serializable {


    // 主键id
	@Id
    @GeneratedValue(generator = "apiCustomerQueryApply")
    @GenericGenerator(name = "apiCustomerQueryApply", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false)
	private String id;

    //
    @Column(name = "ACCOUNT_ID")
    private String accountId;

    //
    @Column(name = "TRACE_ID")
    private String traceId;

	// 货主code
	@Column(name = "CUSTOMER_NUMBER")
	private String customerNumber;

	// 客户名称
	@Column(name = "CUSTOMER_NAME")
	private String CustomerName;

    // 当前默认：0，通过：1 ,拒绝 2
    @Column(name = "CONFIRM_STATUS")
    private Integer confirmStatus;

    //通过时间
    @Column(name = "COMFIRM_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date comfirmTime;

    // 税号
	@Column(name = "TAX_NUMBER")
	private String taxNumber;

    @Column(name = "FILE_URL")
    private String fileUrl;

	@Column(name = "REMARK")
    private String remark;

    // 创建时间
    @Column(name = "CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;

    @Column(name = "REASON")
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public Date getComfirmTime() {
        return comfirmTime;
    }

    public void setComfirmTime(Date comfirmTime) {
        this.comfirmTime = comfirmTime;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
