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

/**
 * 货转联系单托盘明细
 * BisTransferStock entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_TRANSFER_STOCK_TRAYINFO")
public class BisTransferStockTrayInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRANSFER")
	@SequenceGenerator(name = "SEQ_TRANSFER", sequenceName = "SEQ_TRANSFER", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	@Column(name = "TRANSFER_LINK_ID")
	private String transferId;//T（货转标志）+操作人员代码(三位，自动补齐)+YYMMDDHHMMS 
	@Column(name = "TRAYINFOID")
	private int trayInfoId;//库存明细id
	@Column(name = "STOCKNUM")
	private String stockNum;//托盘号
	@Column(name = "BILL_NUM")
	private String billNum;//提单号
	@Column(name = "CTN_NUM")
	private String ctnNum;//厢号号
	@Column(name = "SKU_ID")
	private String sku;//SKU号 
	@Column(name = "CARGO_NAME")
	private String cargoName;//品名 
	@Column(name = "TYPE_SIZE")
	private String typeSize;//规格
	@Column(name = "PIECE")
	private Double piece;//件数
	@Column(name = "GROSS_WEIGHT")
	private Double grossWeight;//总毛重
	@Column(name = "NET_WEIGHT")
	private Double netWeight;//总净	
	@Column(name = "UNITS")
	private String untis;//计量单位
	@Column(name = "OPERATOR")
	private String crUser;//创建人
	@Column(name = "OPERATE_TIME")
	private Date crTime;//创建时间
	@Column(name = "REMARK")
	private String remark;//备注
	@Column(name = "ENTER_STATE")
	private String enterState;//货物类型 0成品 1货损
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	public int getTrayInfoId() {
		return trayInfoId;
	}
	public void setTrayInfoId(int trayInfoId) {
		this.trayInfoId = trayInfoId;
	}
	public String getStockNum() {
		return stockNum;
	}
	public void setStockNum(String stockNum) {
		this.stockNum = stockNum;
	}
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getCtnNum() {
		return ctnNum;
	}
	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getCargoName() {
		return cargoName;
	}
	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	public String getTypeSize() {
		return typeSize;
	}
	public void setTypeSize(String typeSize) {
		this.typeSize = typeSize;
	}
	public Double getPiece() {
		return piece;
	}
	public void setPiece(Double piece) {
		this.piece = piece;
	}
	public Double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public Double getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}
	public String getUntis() {
		return untis;
	}
	public void setUntis(String untis) {
		this.untis = untis;
	}
	public String getCrUser() {
		return crUser;
	}
	public void setCrUser(String crUser) {
		this.crUser = crUser;
	}
	public Date getCrTime() {
		return crTime;
	}
	public void setCrTime(Date crTime) {
		this.crTime = crTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEnterState() {
		return enterState;
	}
	public void setEnterState(String enterState) {
		this.enterState = enterState;
	}

}