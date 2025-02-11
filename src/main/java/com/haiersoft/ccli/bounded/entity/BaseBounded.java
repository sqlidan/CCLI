package com.haiersoft.ccli.bounded.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
@Entity
@Table(name = "BASE_BOUNDED")
@DynamicUpdate
@DynamicInsert
public class BaseBounded implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(generator = "baseBoundedGenerator")
	@GenericGenerator(name = "baseBoundedGenerator", strategy = "uuid")
	@Column(name = "ID",unique = true)
	private String id;

	@Column(name = "CLIENT_ID")
	private String clientId;// 客户ID

	@Excel(name = "客户名称")
	@Column(name = "CLIENT_NAME")
	private String clientName;

	@Excel(name = "提单号")
	@Column(name = "BILL_NUM")
	private String billNum;

	@Excel(name = "报关单号")
	@Column(name = "CD_NUM")
	private String cdNum;

	//@Excel(name = "MR/集装箱号")
	@Column(name = "CTN_NUM")
	private String ctnNum;

	//@Excel(name = "货物描述")
	@Column(name = "ITEM_NAME")
	private String itemName;

	//@Excel(name = "件数")
	@Column(name = "PIECE")
	private Integer piece;

	//@Excel(name = "总净值")
	@Column(name = "NET_WEIGHT")
	private Double netWeight;

	@Excel(name = "所属客服")
	@Column(name = "CUSTOMER_SERVICE_NAME")
	private String customerServiceName;

	@Excel(name = "hs编码")
	@Column(name = "HS_CODE")
	private String hsCode;

	@Excel(name = "海关品名")
	@Column(name = "HS_ITEMNAME")
	private String hsItemname;//

	@Excel(name = "账册商品序号")
	@Column(name = "ACCOUNT_BOOK")
	private String accountBook;

	@Excel(name = "申报重量")
	@Column(name = "DCL_QTY")
	private Double dclQty;

	@Excel(name = "申报计量单位")
	@Column(name = "DCL_UNIT")
	private String dclUnit;


	@Excel(name = "海关库存重量")
	@Column(name = "HS_QTY")
	private Double hsQty;

	@Excel(name = "规格")
	@Column(name = "TYPE_SIZE")
	private String typeSize;

	@Excel(name = "库位号")
	@Column(name = "CARGO_LOCATION")
	private String cargoLocation;

	@Excel(name = "库区")
	@Column(name = "CARGO_AREA")
	private String cargoArea;

	//@Excel(name = "入库时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "STORAGE_DATE")
	private Date storageDate;


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATED_TIME")
	private Date createdTime; // 创建日期


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATED_TIME")
	private Date updatedTime;//修改时间

	@Excel(name = "账册号")
	@Column(name = "EMS_NO")
	private String emsNo;

	@Transient
	private String gdsMtno;
	@Transient
	private Double grossWeight;
	@Transient
	private Double dclTotalAmt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Transient
	private Date makeTimes;//生产开始日期
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Transient
	private Date makeTimee;//生产截至日期
	@Transient
	private Integer day;//距离生产日期之间日差数

	public Date getMakeTimes() {
		return makeTimes;
	}

	public void setMakeTimes(Date makeTimes) {
		this.makeTimes = makeTimes;
	}

	public Date getMakeTimee() {
		return makeTimee;
	}

	public void setMakeTimee(Date makeTimee) {
		this.makeTimee = makeTimee;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getEmsNo() {
		return emsNo;
	}

	public void setEmsNo(String emsNo) {
		this.emsNo = emsNo;
	}

	public String getGdsMtno() {
		return gdsMtno;
	}

	public void setGdsMtno(String gdsMtno) {
		this.gdsMtno = gdsMtno;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getDclTotalAmt() {
		return dclTotalAmt;
	}

	public void setDclTotalAmt(Double dclTotalAmt) {
		this.dclTotalAmt = dclTotalAmt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getCdNum() {
		return cdNum;
	}

	public void setCdNum(String cdNum) {
		this.cdNum = cdNum;
	}

	public String getCtnNum() {
		return ctnNum;
	}

	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getPiece() {
		return piece;
	}

	public void setPiece(Integer piece) {
		this.piece = piece;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public String getCustomerServiceName() {
		return customerServiceName;
	}

	public void setCustomerServiceName(String customerServiceName) {
		this.customerServiceName = customerServiceName;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getHsItemname() {
		return hsItemname;
	}

	public void setHsItemname(String hsItemname) {
		this.hsItemname = hsItemname;
	}

	public String getAccountBook() {
		return accountBook;
	}

	public void setAccountBook(String accountBook) {
		this.accountBook = accountBook;
	}

	public Double getHsQty() {
		return hsQty;
	}

	public void setHsQty(Double hsQty) {
		this.hsQty = hsQty;
	}

	public String getTypeSize() {
		return typeSize;
	}

	public void setTypeSize(String typeSize) {
		this.typeSize = typeSize;
	}

	public String getCargoLocation() {
		return cargoLocation;
	}

	public void setCargoLocation(String cargoLocation) {
		this.cargoLocation = cargoLocation;
	}

	public Date getStorageDate() {
		return storageDate;
	}

	public void setStorageDate(Date storageDate) {
		this.storageDate = storageDate;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getCargoArea() {
		return cargoArea;
	}

	public void setCargoArea(String cargoArea) {
		this.cargoArea = cargoArea;
	}

	public Double getDclQty() {
		return dclQty;
	}

	public void setDclQty(Double dclQty) {
		this.dclQty = dclQty;
	}

	public String getDclUnit() {
		return dclUnit;
	}

	public void setDclUnit(String dclUnit) {
		this.dclUnit = dclUnit;
	}
}