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
 * BisCustomsDeclarationInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_CUSTOMS_DECLARATION_INFO")
public class BisCustomsDeclarationInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5155824809351547579L;

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUSTOMS_INFO")
	@SequenceGenerator(name="SEQ_CUSTOMS_INFO", sequenceName="SEQ_CUSTOMS_INFO", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //ID
	
	@Column(name = "CUS_ID")
	private String cusId; //主表ID
	
	@Column(name = "CD_NUM")
	@Excel(name="海关编码")
	private String cdNum; //(海关编码)报关单号
	
	@Column(name = "ITEM_NUM")
	@Excel(name="项号")
	private String itemNum; //项号(注册的货物电子账册序号)
	
	@Column(name = "CARGO_NAME")
	@Excel(name="商品名称")
	private String cargoName; //商品名称
	
	@Column(name = "SPEC")
	@Excel(name="规格型号")
	private String spec;//规格型号
	
	@Column(name = "SCALAR")
	@Excel(name="件数")
	private Integer scalar; //件数
	
	@Column(name = "NET_WEIGHT")
	@Excel(name="净重")
	private Double netWeight; //净重
	
	@Column(name = "UNITS")
	@Excel(name="单位")
	private String units;  //默认：KG（千克）单位
	
	@Column(name = "DESTINATION")
	@Excel(name="目的地")
	private String destination;  //目的地
	
	@Column(name = "UNIT_PRICE")
	@Excel(name="单价")
	private Double unitPrice; //单价
	
	@Column(name = "TOTAL_PRICES")
	@Excel(name="总价")
	private Double totalPrices;  //总价
	
	@Column(name = "CURRENCY_TYPE")
	@Excel(name="币种")
	private String currencyType; //币种
	
	@Column(name = "FREE_LAVY")
	@Excel(name="征免",replace = {"不免征_0", "免征_1"})
	private Integer freeLavy; //征免
	
	@Column(name = "DUTY")
	@Excel(name="关税")
	private Double duty;  //关税
	
	@Column(name = "RECORD_MAN")
	@Excel(name="录入员")
	private String recordMan; //录入员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "RECORD_TIME")
	@Excel(name="录入时间")
	private Date recordTime;  //录入时间
	
	@Column(name = "CHECK_OUT")
	private Integer checkOut; //核销标志
	
	@Column(name = "CHECK_MAN")
	private String checkMan; //核销人
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CHECK_TIME")
	private Date checkTime;  //核销时间
	
	@Column(name = "REMARK1")
	@Excel(name="备注")
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

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getCdNum() {
		return cdNum;
	}

	public void setCdNum(String cdNum) {
		this.cdNum = cdNum;
	}

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
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

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getTotalPrices() {
		return totalPrices;
	}

	public void setTotalPrices(Double totalPrices) {
		this.totalPrices = totalPrices;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getFreeLavy() {
		return freeLavy;
	}

	public void setFreeLavy(Integer freeLavy) {
		this.freeLavy = freeLavy;
	}

	public Double getDuty() {
		return duty;
	}

	public void setDuty(Double duty) {
		this.duty = duty;
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

	public Integer getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Integer checkOut) {
		this.checkOut = checkOut;
	}

	public String getCheckMan() {
		return checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
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