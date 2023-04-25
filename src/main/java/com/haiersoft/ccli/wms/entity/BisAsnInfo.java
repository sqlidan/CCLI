package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * ASN的entity
 * @author pyl
 * @date 2016年2月27日
 */
@Entity
@Table(name = "BIS_ASN_INFO")
@DynamicUpdate 
@DynamicInsert
public class BisAsnInfo implements Serializable {

    private static final long serialVersionUID = 4040788502963084203L;
    @Id
    @Column(name = "SKU_ID")
	private String skuId;	//SKU
   
    @Id
    @Column(name = "ASN_ID")
   	private String asnId;	//ASN
    
    @Column(name = "RK_NUM")
	private String rkNum;//入库号
	
	@Column(name = "SKU_DESCRIBE")
	private String skuDescribe;//sku描述
	
	@Column(name = "CARGO_STATE")
	private String cargoState;//库存类型
	
	@Column(name = "PRODUCING_AREA")
	private String producingArea;//产地
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "VALIDITY_TIME")
	private Date validityTime;//有效时间

	@Column(name = "ATTRIBUTE1")
	private String attribute1;//属性一
	
	@Column(name = "ATTRIBUTE2")
	private String attribute2;//属性二
	
	@Column(name = "ATTRIBUTE3")
	private String attribute3;//属性三
	
	@Column(name = "CARGO_NAME")
	private String cargoName;//品名
	
	@Column(name = "CARGO_TYPE")
	private String cargoType;//产品种类
	
	@Column(name = "TYPE_SIZE")
	private String typeSize;//规格
	
	@Column(name = "PIECE_REAL")
	private Double pieceReal;//实际入库件数
	
	@Column(name = "CARGO_NUM")
	private Double cargoNum;//货损件数
	
	@Column(name = "PIECE")
	private Double piece;//件数
	
	@Column(name = "TRANSFER_PIECE")
	private Double transferPiece;//货转件数
	
	@Column(name = "IF_SECOND_ENTER")
	private Integer ifSecondEnter;//入库类型(记录是否多次入库1：重收 0：正常 2：分拣)
	
	@Column(name = "NET_WEIGHT")
	private Double netWeight;//总净重

	@Column(name = "GROSS_WEIGHT")
	private Double grossWeight;//总毛重
	
	@Column(name = "NET_SINGLE")
	private Double netSingle;//单净	
	
	@Column(name = "GROSS_SINGLE")
	private Double grossSingle;//单毛
	
	@Column(name = "UNITS")
	private String units;//重量单位
	
	@Column(name = "MSC_NUM")
	private String mscNum;//MSC
	
	@Column(name = "LOT_NUM")
	private String lotNum;//LOT
	
	@Column(name = "PRO_NUM")
	private String proNum;//PRO
	
	@Column(name = "SALES_NUM")
	private String salesNum;//SALES REF NO
	
	@Column(name = "PRO_TIME")
	private String proTime;//生产日期
	
	@Column(name = "OPERATOR")
	private String operator;//操作人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "OPERATE_TIME")
	private Date operateTime;//操作时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;//修改时间
	
	@Column(name = "REMARK1")
	private String remark1;//备注1

	@Transient
	private String piecein ;
	
	@Transient
	private String pieceinfo ;
	
	@Column(name = "HS_CODE")
	private String hsCode;//hs编码

	@Column(name = "HS_ITEMNAME")
	private String hsItemname;//海关商品名称
	
	@Column(name = "ACCOUNT_BOOK")
	private String accountBook;//账册商品序号

	
	
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

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getAsnId() {
		return asnId;
	}

	public void setAsnId(String asnId) {
		this.asnId = asnId;
	}

	public String getRkNum() {
		return rkNum;
	}

	public void setRkNum(String rkNum) {
		this.rkNum = rkNum;
	}

	public String getSkuDescribe() {
		return skuDescribe;
	}

	public void setSkuDescribe(String skuDescribe) {
		this.skuDescribe = skuDescribe;
	}

	public String getCargoState() {
		return cargoState;
	}

	public void setCargoState(String cargoState) {
		this.cargoState = cargoState;
	}

	public String getProducingArea() {
		return producingArea;
	}

	public void setProducingArea(String producingArea) {
		this.producingArea = producingArea;
	}

	public Date getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(Date validityTime) {
		this.validityTime = validityTime;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getTypeSize() {
		return typeSize;
	}

	public void setTypeSize(String typeSize) {
		this.typeSize = typeSize;
	}

	public Double getPieceReal() {
		return pieceReal;
	}

	public void setPieceReal(Double pieceReal) {
		this.pieceReal = pieceReal;
	}

	public Double getCargoNum() {
		return cargoNum;
	}

	public void setCargoNum(Double cargoNum) {
		this.cargoNum = cargoNum;
	}

	public Double getPiece() {
		return piece;
	}

	public void setPiece(Double piece) {
		this.piece = piece;
	}

	public Double getTransferPiece() {
		return transferPiece;
	}

	public void setTransferPiece(Double transferPiece) {
		this.transferPiece = transferPiece;
	}

	public Integer getIfSecondEnter() {
		return ifSecondEnter;
	}

	public void setIfSecondEnter(Integer ifSecondEnter) {
		this.ifSecondEnter = ifSecondEnter;
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

	public Double getNetSingle() {
		return netSingle;
	}

	public void setNetSingle(Double netSingle) {
		this.netSingle = netSingle;
	}

	public Double getGrossSingle() {
		return grossSingle;
	}

	public void setGrossSingle(Double grossSingle) {
		this.grossSingle = grossSingle;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getMscNum() {
		return mscNum;
	}

	public void setMscNum(String mscNum) {
		this.mscNum = mscNum;
	}

	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	public String getProNum() {
		return proNum;
	}

	public void setProNum(String proNum) {
		this.proNum = proNum;
	}

	public String getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(String salesNum) {
		this.salesNum = salesNum;
	}

	public String getProTime() {
		return proTime;
	}

	public void setProTime(String proTime) {
		this.proTime = proTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getPiecein() {
		return piecein;
	}

	public void setPiecein(String piecein) {
		this.piecein = piecein;
	}

	public String getPieceinfo() {
		return pieceinfo;
	}

	public void setPieceinfo(String pieceinfo) {
		this.pieceinfo = pieceinfo;
	}
	
	
	
}