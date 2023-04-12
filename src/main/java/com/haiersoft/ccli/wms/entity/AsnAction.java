package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Connor.M
 * @ClassName: AsnAction
 * @Description: ASN计费区间  实体类
 * @date 2016年3月4日 下午4:01:12
 */
@Entity
@Table(name = "BIS_ASN_ACTION")
@DynamicUpdate
@DynamicInsert
public class AsnAction implements Serializable {

    private static final long serialVersionUID = 4151326454264563334L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ASN_ACTION")
  	@SequenceGenerator(name = "SEQ_ASN_ACTION", sequenceName = "SEQ_ASN_ACTION", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;//主键Id
    
    @Column(name = "ASN")
    private String asn;//ASN
    
    @Column(name = "SKU")
    private String sku;//SKU
    
    @Column(name = "BILL_CODE")
    private String billCode;//提单号
    
    @Column(name = "CARGO_NAME")
    private String cargoName;//品名
    
    @Column(name = "CLIENT_ID")
    private String clientId;//客户id
    
    @Column(name = "JFCLIENT_ID")
    private String jfClientId;//结算客户id
    
    @Column(name = "NUM")
    private Integer num;//数量
    
    @Column(name = "NET_WEIGHT")
    private Double netWeight;//净重
    
    @Column(name = "GROSS_WEIGHT")
    private Double grossWeight;//毛重
    
    @Column(name = "CHARGE_STA_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date chargeStaDate;//计费开始日期
    
    @Column(name = "CHARGE_END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date chargeEndDate;//计费结束日期 
    
    @Column(name = "PLAN_END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date planEndDate;//计费计划结束日期
    
    @Column(name = "LAST_SETTL_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date lastSettlDate;//上次结算日期
    
    @Column(name = "CLIENT_DAY")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Integer clientDay;//客户账单日
    
    @Column(name = "STATUS")
    private String status;//状态（1正常，0取消）
    
    @Column(name = "FEE_PLAN_ID")
    private String feePlanId;//费用方案ID
    
    @Column(name = "ENTER_ID")
    private String enterId;//入库联系单ID
    
    @Column(name = "OUT_ID")
    private String outId;//出库联系单ID
    
    @Column(name = "TRANSFER_ID")
    private String transferId;//货转联系单ID
    
    @Column(name = "LINK_TRANSFER_ID")
    private String linkTransferId;//关联货转单ID
    
    @Column(name = "SCRAP_CODE")
    private String scrapCode;//货损单号
    
    @Column(name = "OUT_LINK_ID")
    private String outLinkId;//出库订单号
    
    @Column(name = "CLEAN_SIGN")
    private String cleanSign="0";//清库标记（0正常， 1清库）
    
    @Column(name = "CHARGE_DAY")
    private Integer chargeDay;//计费天数
    
    @Column(name = "TRANSFER_SIGN")
    private String transferSign;//货转存储费标记：标记是货转单生成的关联记录，不清空，关联货转id有清空所以建立这个字段记录货转

    
	public String getTransferSign() {
		return transferSign;
	}

	public void setTransferSign(String transferSign) {
		this.transferSign = transferSign;
	}

	public String getScrapCode() {
		return scrapCode;
	}

	public void setScrapCode(String scrapCode) {
		this.scrapCode = scrapCode;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public String getOutLinkId() {
		return outLinkId;
	}

	public void setOutLinkId(String outLinkId) {
		this.outLinkId = outLinkId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAsn() {
		return asn;
	}

	public void setAsn(String asn) {
		this.asn = asn;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Date getChargeStaDate() {
		return chargeStaDate;
	}

	public void setChargeStaDate(Date chargeStaDate) {
		this.chargeStaDate = chargeStaDate;
	}

	public Date getChargeEndDate() {
		return chargeEndDate;
	}

	public void setChargeEndDate(Date chargeEndDate) {
		this.chargeEndDate = chargeEndDate;
	}

	public Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public Date getLastSettlDate() {
		return lastSettlDate;
	}

	public void setLastSettlDate(Date lastSettlDate) {
		this.lastSettlDate = lastSettlDate;
	}

	public Integer getClientDay() {
		return clientDay;
	}

	public void setClientDay(Integer clientDay) {
		this.clientDay = clientDay;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFeePlanId() {
		return feePlanId;
	}

	public void setFeePlanId(String feePlanId) {
		this.feePlanId = feePlanId;
	}

	public String getEnterId() {
		return enterId;
	}

	public void setEnterId(String enterId) {
		this.enterId = enterId;
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getLinkTransferId() {
		return linkTransferId;
	}

	public void setLinkTransferId(String linkTransferId) {
		this.linkTransferId = linkTransferId;
	}

	public String getCleanSign() {
		return cleanSign;
	}

	public void setCleanSign(String cleanSign) {
		this.cleanSign = cleanSign;
	}

	public Integer getChargeDay() {
		return chargeDay;
	}

	public void setChargeDay(Integer chargeDay) {
		this.chargeDay = chargeDay;
	}

	public String getJfClientId() {
		return jfClientId;
	}

	public void setJfClientId(String jfClientId) {
		this.jfClientId = jfClientId;
	}
    
}
