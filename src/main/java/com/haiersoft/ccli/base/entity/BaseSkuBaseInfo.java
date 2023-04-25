package com.haiersoft.ccli.base.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * SKU
 *  @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BASE_SKU_BASE_INFO")
public class BaseSkuBaseInfo implements java.io.Serializable {
    
	private static final long serialVersionUID = 7701615065461868779L;
    
	private String skuId;//S+人员代码(三位，自动补齐)+ YYMMDDHHMMSS+SEQ(2位循环)
	private String cargoState;//库存类型 1成品（默认）2箱损 3货损 
	private String producingArea;//描述 Sku描述即为品名+规格 (备注2016-09-12 备注错误,应是产地描述,因不宜删除,特此备注)
	private Date validityTime;//默认两年 有效时
	private String attribute1;//属性1 
	private String attribute2;//属性2 
	private String attribute3;//属性3
	private String cargoName;//产品名 
	private String cargoType;//大类：牛肉、羊肉、鱼等产品类型
	private String typeName;//大类名称
	private String classType;//小类
	private String className;//小类名称
	private String typeSize;//规格
	private Integer piece;//件数 
	private Double netWeight;//总净重 
	private Double grossWeight;//总毛重
	private Double netSingle;//单净 根据件数和总净自动计算 
	private Double grossSingle;//单毛 根据件数和总毛自动计算 
	private String units="1";//重量单位  默认KG
	private String mscNum;//MSC
	private String lotNum;//lotNum1
	private String proNum;//proNum1
	private String rkdh;//入库号
	private String shipNum;//捕捞船名
	private String operator;//操作人员
	private Date operateTime;//操作时间
	private String updateUser;//修改人员
	private Date updateTime;//修改时间
	private String remark;//备注1
	private Integer delFlag=0;//删除标记，0正常，1删除
	@Column(name = "HS_CODE")
	private String hsCode;//hs编码

	@Column(name = "HS_ITEMNAME")
	private String hsItemname;//海关商品名称
	
	@Column(name = "ACCOUNT_BOOK")
	private String accountBook;//账册商品序号

	// Constructors

	/** default constructor */
	public BaseSkuBaseInfo() {
		
	}

	/** full constructor */
	public BaseSkuBaseInfo(String skuId, String cargoState,
			String producingArea, Date validityTime, String attribute1,
			String attribute2, String attribute3, String cargoName,
			String cargoType, String typeSize, Integer piece, Double netWeight,
			Double grossWeight, Double netSingle, Double grossSingle,
			String units, String mscNum,String operator, Date operateTime,
			Date updateTime, String remark) {
		this.skuId = skuId;
		this.cargoState = cargoState;
		this.producingArea = producingArea;
		this.validityTime = validityTime;
		this.attribute1 = attribute1;
		this.attribute2 = attribute2;
		this.attribute3 = attribute3;
		this.cargoName = cargoName;
		this.cargoType = cargoType;
		this.typeSize = typeSize;
		this.piece = piece;
		this.netWeight = netWeight;
		this.grossWeight = grossWeight;
		this.netSingle = netSingle;
		this.grossSingle = grossSingle;
		this.units = units;
		this.mscNum = mscNum;
		this.operator = operator;
		this.operateTime = operateTime;
		this.updateTime = updateTime;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@Column(name = "SKU_ID", unique = true)
	public String getSkuId() {
		return this.skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	@Column(name = "DEL_FLAG")
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	@Column(name = "CARGO_STATE")
	public String getCargoState() {
		return this.cargoState;
	}

	public void setCargoState(String cargoState) {
		this.cargoState = cargoState;
	}

	@Column(name = "PRODUCING_AREA")
	public String getProducingArea() {
		return this.producingArea;
	}

	public void setProducingArea(String producingArea) {
		this.producingArea = producingArea;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "VALIDITY_TIME")
	public Date getValidityTime() {
		return this.validityTime;
	}

	public void setValidityTime(Date validityTime) {
		this.validityTime = validityTime;
	}

	@Column(name = "ATTRIBUTE1")
	public String getAttribute1() {
		return this.attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	@Column(name = "ATTRIBUTE2")
	public String getAttribute2() {
		return this.attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	@Column(name = "ATTRIBUTE3")
	public String getAttribute3() {
		return this.attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	@Column(name = "CARGO_NAME")
	public String getCargoName() {
		return this.cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	@Column(name = "CARGO_TYPE")
	public String getCargoType() {
		return this.cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}
	@Column(name = "TYPE_NAME")
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Column(name = "CLASS_TYPE")
	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}
	@Column(name = "CLASS_NAME")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "TYPE_SIZE")
	public String getTypeSize() {
		return this.typeSize;
	}

	public void setTypeSize(String typeSize) {
		this.typeSize = typeSize;
	}

	@Column(name = "PIECE")
	public Integer getPiece() {
		return this.piece;
	}

	public void setPiece(Integer piece) {
		this.piece = piece;
	}

	@Column(name = "NET_WEIGHT")
	public Double getNetWeight() {
		return this.netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	@Column(name = "GROSS_WEIGHT")
	public Double getGrossWeight() {
		return this.grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	@Column(name = "NET_SINGLE")
	public Double getNetSingle() {
		return this.netSingle;
	}

	public void setNetSingle(Double netSingle) {
		this.netSingle = netSingle;
	}

	@Column(name = "GROSS_SINGLE")
	public Double getGrossSingle() {
		return this.grossSingle;
	}

	public void setGrossSingle(Double grossSingle) {
		this.grossSingle = grossSingle;
	}

	@Column(name = "UNITS")
	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Column(name = "MSC_NUM")
	public String getMscNum() {
		return this.mscNum;
	}

	public void setMscNum(String mscNum) {
		this.mscNum = mscNum;
	}
	 
	@Column(name = "OPERATOR")
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	 
	@Column(name = "OPERATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	@Column(name = "UPDATE_USER")
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	 
	@Column(name = "UPDATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "LOT_NUM")
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	@Column(name = "PRO_NUM")
	public String getProNum() {
		return proNum;
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

	public void setProNum(String proNum) {
		this.proNum = proNum;
	}
	@Column(name = "RKDH")
	public String getRkdh() {
		return rkdh;
	}

	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}
	@Column(name = "SHIPNUM")
	public String getShipNum() {
		return shipNum;
	}

	public void setShipNum(String shipNum) {
		this.shipNum = shipNum;
	}
}