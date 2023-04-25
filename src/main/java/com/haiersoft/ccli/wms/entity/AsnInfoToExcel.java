package com.haiersoft.ccli.wms.entity;
import java.io.Serializable;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;
/**
 * SKU
 *  @author MyEclipse Persistence Tools
 */
@ExcelTarget("EnterStockInfoToExcel") 
public class AsnInfoToExcel implements Serializable {
	
    private static final long serialVersionUID = 8270056740403940286L;
    
	@Excel(name="品名")
	private String cargoName;//品名
	@Excel(name="大类")
	private String bigTypeName;//大类
	@Excel(name="小类")
	private String littleTypeName;//小类
	@Excel(name="规格")
	private String typeSize;//规格
	@Excel(name="件数")
	private Integer piece;//件数
	@Excel(name="总净")
	private Double netWeight;//总净
	@Excel(name="总毛")
	private Double grossWeight;//总毛
	@Excel(name="单价")
	private Double price;//单价
	@Excel(name="项目号 PRO no")
	private String projectNum;//项目名
	@Excel(name="入库号")
	private String rkNum;//入库号
	@Excel(name="MSC")
	private String mscNum;//MSC
	@Excel(name="LOT No")
	private String lotNum;//LOT No
	@Excel(name="捕捞船名")
	private String shipNum;//捕捞船名
	
	@Excel(name = "HS编码")
	private String hscode;//hs编码
	@Excel(name = "海关品名")
	private  String hsItemname;//海关商品名称
	@Excel(name = "账册商品序号")
	private  String accountBook;//账册商品序号

	
	
	
	
	
	
	public String getHscode() {
		return hscode;
	}
	public void setHscode(String hscode) {
		this.hscode = hscode;
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
	public String getCargoName() {
		return cargoName;
	}
	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	public String getBigTypeName() {
		return bigTypeName;
	}
	public void setBigTypeName(String bigTypeName) {
		this.bigTypeName = bigTypeName;
	}
	public String getLittleTypeName() {
		return littleTypeName;
	}
	public void setLittleTypeName(String littleTypeName) {
		this.littleTypeName = littleTypeName;
	}
	public String getTypeSize() {
		return typeSize;
	}
	public void setTypeSize(String typeSize) {
		this.typeSize = typeSize;
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
	public Double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getProjectNum() {
		return projectNum;
	}
	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	}
	public String getRkNum() {
		return rkNum;
	}
	public void setRkNum(String rkNum) {
		this.rkNum = rkNum;
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
	public String getShipNum() {
		return shipNum;
	}
	public void setShipNum(String shipNum) {
		this.shipNum = shipNum;
	}
	
	
}