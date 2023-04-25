package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;


/**
 * SKU
 *  @author MyEclipse Persistence Tools
 */
@ExcelTarget("OutStockInfoToExcel") 
public class OutStockInfoToExcel implements Serializable {
	
    private static final long serialVersionUID = 8270056740403940287L;
    
    @Excel(name = "提单号")
	private String billNum;//提单号
	@Excel(name="箱号")
	private String ctnNum;//MR
	@Excel(name = "品名")
	private String cargoName;//品名
	@Excel(name = "sku")
	private String sku;//sku
	@Excel(name = "入库号")
	private String rkNum;//入库号
	@Excel(name = "规格")
	private String typeSize;//规格
	@Excel(name="出库件数")
	private Integer outNum;//出库件数
	@Excel(name="货物状态")
	private String enterState;//默认为0（成品） ；1 货物货损 ；2 包装破损   入库状态
	@Excel(name="销售号")
	private String salesNum;//销售号
	@Excel(name="ASN")
	private String asn;
	@Excel(name="抄码数")
	private Integer codeNum;//抄码数
	
	
	
	public Integer getCodeNum() {
		return codeNum;
	}
	public void setCodeNum(Integer codeNum) {
		this.codeNum = codeNum;
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
	public String getCargoName() {
		return cargoName;
	}
	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getTypeSize() {
		return typeSize;
	}
	public void setTypeSize(String typeSize) {
		this.typeSize = typeSize;
	}
	public Integer getOutNum() {
		return outNum;
	}
	public void setOutNum(Integer outNum) {
		this.outNum = outNum;
	}
	public String getEnterState() {
		return enterState;
	}
	public void setEnterState(String enterState) {
		this.enterState = enterState;
	}
	public String getSalesNum() {
		return salesNum;
	}
	public void setSalesNum(String salesNum) {
		this.salesNum = salesNum;
	}
	public String getRkNum() {
		return rkNum;
	}
	public void setRkNum(String rkNum) {
		this.rkNum = rkNum;
	}
	public String getAsn() {
		return asn;
	}
	public void setAsn(String asn) {
		this.asn = asn;
	}
	
	
}