/**
 * 
 */
package com.haiersoft.ccli.remoting.hand.invoice.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "BASE_INVOICE")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class BaseInvoice implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8766463427055079211L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INVOICE")
	@SequenceGenerator(name="SEQ_INVOICE", sequenceName="SEQ_INVOICE", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	@Column(name = "SRC_BIZ_DOC_ID")
	private String srcBizDocId;//源业务单据主键
	@Column(name = "SRC_BIZ_DOC_NO")
	private String srcBizDocNo;//源业务单据主键
	@Column(name = "SRC_SYSTEM")
	private String srcSystem;//源系统编码
	@Column(name = "INVOICE_CODE")
	private String invoiceCode;//发票代码
	@Column(name = "INVOICE_NO")
	private String invoiceNo;//发票号码
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "INV_DATE")
	private Date invDate;//开票日期
	@Column(name = "TAX")
	private Double tax;//税额
	@Column(name = "INVOICEPKID")
	private String invoicepkid;//开票单ID
	@Column(name = "TAX_NO")
	private String taxno;//税号
	@Column(name = "CUSTOM_NAME")
	private String customName;//客户名称（付款单位）
	@Column(name = "CREATE_USER")
	private String createUser;//制单人
	@Column(name = "CHECK_USER")
	private String checkUser;//复核人
	@Column(name = "OPERATOR_USER")
	private String operatorUser;
	@Column(name = "REMARK")
	private String remark;//备注
	@Column(name = "INVOICE_TYPE")
	private String invoiceType;//发票类型
	@Column(name = "RED_STATUS")
	private String redStatus;//冲红状态
	@Column(name = "CANCEL_STATUS")
	private String cancelStatus;//作废状态
	@Column(name = "TOTAL_TAX_AMOUNT")
	private Double totalTaxAmount;//含税金额
	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;//发票金额（元）
	@Column(name = "NO_TAX_AMOUNT")
	private Double noTaxAmount;//不含税金额
	@Column(name = "PARAM_A")
	private String param_a;//预留字段a
	@Column(name = "PARAM_B")
	private String param_b;//预留字段b
	@Column(name = "PARAM_C")
	private String param_c;//预留字段c
	
	
	public BaseInvoice() {
		super();
	}

    
	public BaseInvoice(Integer id, String srcBizDocId, String srcBizDocNo, String srcSystem, String invoiceCode,
			String invoiceNo, Date invDate, Double tax, String invoicepkid, String taxno, String customName,
			String createUser, String checkUser, String operatorUser, String remark, String invoiceType,
			String redStatus, String cancelStatus, Double totalTaxAmount, Double totalAmount, Double noTaxAmount,
			String param_a, String param_b, String param_c) {
		super();
		this.id = id;
		this.srcBizDocId = srcBizDocId;
		this.srcBizDocNo = srcBizDocNo;
		this.srcSystem = srcSystem;
		this.invoiceCode = invoiceCode;
		this.invoiceNo = invoiceNo;
		this.invDate = invDate;
		this.tax = tax;
		this.invoicepkid = invoicepkid;
		this.taxno = taxno;
		this.customName = customName;
		this.createUser = createUser;
		this.checkUser = checkUser;
		this.operatorUser = operatorUser;
		this.remark = remark;
		this.invoiceType = invoiceType;
		this.redStatus = redStatus;
		this.cancelStatus = cancelStatus;
		this.totalTaxAmount = totalTaxAmount;
		this.totalAmount = totalAmount;
		this.noTaxAmount = noTaxAmount;
		this.param_a = param_a;
		this.param_b = param_b;
		this.param_c = param_c;
	}


	////////////////////////////////////////////////////////////////////////////
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSrcBizDocId() {
		return srcBizDocId;
	}

	public void setSrcBizDocId(String srcBizDocId) {
		this.srcBizDocId = srcBizDocId;
	}

	public String getSrcBizDocNo() {
		return srcBizDocNo;
	}

	public void setSrcBizDocNo(String srcBizDocNo) {
		this.srcBizDocNo = srcBizDocNo;
	}

	public String getSrcSystem() {
		return srcSystem;
	}

	public void setSrcSystem(String srcSystem) {
		this.srcSystem = srcSystem;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getInvDate() {
		return invDate;
	}

	public void setInvDate(Date invDate) {
		this.invDate = invDate;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public String getInvoicepkid() {
		return invoicepkid;
	}

	public void setInvoicepkid(String invoicepkid) {
		this.invoicepkid = invoicepkid;
	}

	public String getTaxno() {
		return taxno;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getRedStatus() {
		return redStatus;
	}

	public void setRedStatus(String redStatus) {
		this.redStatus = redStatus;
	}

	public String getCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(String cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getNoTaxAmount() {
		return noTaxAmount;
	}

	public void setNoTaxAmount(Double noTaxAmount) {
		this.noTaxAmount = noTaxAmount;
	}

	public String getParam_a() {
		return param_a;
	}

	public void setParam_a(String param_a) {
		this.param_a = param_a;
	}

	public String getParam_b() {
		return param_b;
	}

	public void setParam_b(String param_b) {
		this.param_b = param_b;
	}

	public String getParam_c() {
		return param_c;
	}

	public void setParam_c(String param_c) {
		this.param_c = param_c;
	}


	public String getOperatorUser() {
		return operatorUser;
	}


	public void setOperatorUser(String operatorUser) {
		this.operatorUser = operatorUser;
	}
    
}
