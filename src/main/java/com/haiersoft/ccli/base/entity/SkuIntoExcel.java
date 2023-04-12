package com.haiersoft.ccli.base.entity;

import java.io.Serializable;
import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;


/**
 * SKU
 *  @author MyEclipse Persistence Tools
 */
@ExcelTarget("skuIntoExcel") 
public class SkuIntoExcel implements Serializable {
	
    private static final long serialVersionUID = 8270056740403940286L;
    
	@Excel(name="库存类型",replace = {"成品_1","箱损_3","货损_2"}) 
	private String cargoState;//库存类型 1成品（默认）3箱损 2货损 
	@Excel(name="有效时间")
	private Date validityTime;//默认两年 有效时
	@Excel(name="属性1")
	private String attribute1;//属性1
	@Excel(name="属性2")
	private String attribute2;//属性2
	@Excel(name="属性3")
	private String attribute3;//属性3
	@Excel(name="产品名称")
	private String cargoName;//产品名 
	@Excel(name="产品大类")
	private String cargoType;//大类：牛肉、羊肉、鱼等产品类型
	@Excel(name="产品小类")
	private String classType;//小类
	@Excel(name="规格")
	private String typeSize;//规格
	@Excel(name="单净")
	private Double netSingle;//单净 根据件数和总净自动计算
	@Excel(name="单毛")
	private Double grossSingle;//单毛 根据件数和总毛自动计算 
	@Excel(name="计量单位",replace = {"千克_1","吨_2"})
	private String units;//重量单位  默认KG
	@Excel(name="MSC")
	private String mscNum;//MSC
	@Excel(name="LOT")
	private String lotNum;//lotNum
	@Excel(name="PRO")
	private String proNum;//proNum
	@Excel(name="入库号")
	private String rkdh;//入库号
	@Excel(name="捕捞船号")
	private String shipNum;//捕捞船号
	@Excel(name="备注")
	private String remark;//备注1
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
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getCargoState() {
		return cargoState;
	}
	public void setCargoState(String cargoState) {
		this.cargoState = cargoState;
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
	public String getTypeSize() {
		return typeSize;
	}
	public void setTypeSize(String typeSize) {
		this.typeSize = typeSize;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRkdh() {
		return rkdh;
	}
	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}
	public String getShipNum() {
		return shipNum;
	}
	public void setShipNum(String shipNum) {
		this.shipNum = shipNum;
	}
	
}