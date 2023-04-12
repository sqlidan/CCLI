package com.haiersoft.ccli.wms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 出库订单明细
 * BisLoadingOrderInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_LOADING_ORDER_INFO")
@DynamicUpdate
@DynamicInsert
public class BisLoadingOrderInfo implements java.io.Serializable {

    private static final long serialVersionUID = -2724515242333597233L;
    
	// Fields
	private String loadingPlanNum;// ID 订单号  O+yymmdd+3seq
	private String skuId;//ID SKU
	private String billNum;//ID 提单号
	private String ctnNum;//ID 原入库箱号箱号
	private String outLinkId;//ID 出库联系单ID
	private String enterState;//ID 入库状态 0（成品） ；1（货损）ENTER_STATE
	private Integer piece;//件数
	private Date loadingTiem;//装车时间
	private String loadingTruckNum;//装车牌号
	private Double netSingle;//单净
	private Double grossSingle;//单毛
	private Double grossWeight;//总毛重
	private Double netWeight;//总净重
	private String units;//重量单位
	private String operator;//操作人员
	private Date operateTime;//操作时间
	private Date updateTime;//修改时间
	private String remark1;//备注1
	private String stockIn;//存货方id
	private String stockName;//存货方
	private String catgoName;//货物名称CARGO_NAME
	private String rkNum;//入库号
	private String lotNum;//lotNum1
	private String mscNum;//proNum1
	private String typeSize;//规格
	private String asn;//asn
	//追加列表字段
	private Integer isEdite=0;//标记是否可以修改数据 0可编辑，1不可编辑
	
	 

	/** default constructor */
	public BisLoadingOrderInfo() {
	}

	 
	 
	@Id
	@Column(name = "LOADING_PLAN_NUM")
	public String getLoadingPlanNum() {
		return loadingPlanNum;
	}

	public void setLoadingPlanNum(String loadingPlanNum) {
		this.loadingPlanNum = loadingPlanNum;
	}
	@Id
	@Column(name = "BILL_NUM")
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	@Id
	@Column(name = "SKU_ID")
	public String getSkuId() {
		return this.skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	@Id
	@Column(name = "CTN_NUM")
	public String getCtnNum() {
		return this.ctnNum;
	}

	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}
	@Id
	@Column(name = "ENTER_STATE")
	public String getEnterState() {
		return enterState;
	}



	public void setEnterState(String enterState) {
		this.enterState = enterState;
	}
	@Column(name = "OUT_LINK_ID")
	public String getOutLinkId() {
		return outLinkId;
	}

	public void setOutLinkId(String outLinkId) {
		this.outLinkId = outLinkId;
	}

	@Column(name = "PIECE")
	public Integer getPiece() {
		return this.piece;
	}

	public void setPiece(Integer piece) {
		this.piece = piece;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LOADING_TIEM")
	public Date getLoadingTiem() {
		return this.loadingTiem;
	}

	public void setLoadingTiem(Date loadingTiem) {
		this.loadingTiem = loadingTiem;
	}

	@Column(name = "LOADING_TRUCK_NUM")
	public String getLoadingTruckNum() {
		return this.loadingTruckNum;
	}

	public void setLoadingTruckNum(String loadingTruckNum) {
		this.loadingTruckNum = loadingTruckNum;
	}

	@Column(name = "GROSS_WEIGHT")
	public Double getGrossWeight() {
		return this.grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	@Column(name = "NET_WEIGHT")
	public Double getNetWeight() {
		return this.netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	@Column(name = "UNITS")
	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Column(name = "OPERATOR")
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "OPERATE_TIME")
	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "REMARK1")
	public String getRemark1() {
		return this.remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	@Column(name = "NET_SINGLE")
	public Double getNetSingle() {
		return netSingle;
	}
	public void setNetSingle(Double netSingle) {
		this.netSingle = netSingle;
	}

	@Column(name = "GROSS_SINGLE")
	public Double getGrossSingle() {
		return grossSingle;
	}
	public void setGrossSingle(Double grossSingle) {
		this.grossSingle = grossSingle;
	}
	@Column(name = "STOCK_ID")
	public String getStockIn() {
		return this.stockIn;
	}

	public void setStockIn(String stockIn) {
		this.stockIn = stockIn;
	}
	@Column(name = "STOCK_NAME")
	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	@Column(name = "CARGO_NAME")
	public String getCatgoName() {
		return catgoName;
	}
	public void setCatgoName(String catgoName) {
		this.catgoName = catgoName;
	}

	@Column(name = "RK_NUM")
	public String getRkNum() {
		return rkNum;
	}

	public void setRkNum(String rkNum) {
		this.rkNum = rkNum;
	}
	
	@Column(name = "MSC_NUM")
	public String getMscNum() {
		return this.mscNum;
	}

	public void setMscNum(String mscNum) {
		this.mscNum = mscNum;
	}
	
	@Column(name = "LOT_NUM")
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	@Column(name = "TYPE_SIZE")
	public String getTypeSize() {
		return typeSize;
	}


	public void setTypeSize(String typeSize) {
		this.typeSize = typeSize;
	}
	@Id
	@Column(name = "ASN")
	public String getAsn() {
		return asn;
	}



	public void setAsn(String asn) {
		this.asn = asn;
	}



	@Transient
	public Integer getIsEdite() {
		return isEdite;
	}
	public void setIsEdite(Integer isEdite) {
		this.isEdite = isEdite;
	}
}