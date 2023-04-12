package com.haiersoft.ccli.wms.entity;
import java.io.Serializable;
import java.util.Date;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;


/**
 * SKU
 *  @author MyEclipse Persistence Tools
 */
@ExcelTarget("EnterStockInfoToExcel") 
public class EnterStockInfoToExcel implements Serializable {
	
    private static final long serialVersionUID = 8270056740403940286L;
    
	@Excel(name="入库联系单号") 
	private String linkId;//入库联系单号
	@Excel(name="提单号")
	private String itemNum;//提单号
	@Excel(name="集装箱号")
	private String ctnNum;//MR
	@Excel(name="车牌号")
	private String carNum;//车牌号
	@Excel(name="报关单号")
	private String bgdh;//报关单号
	@Excel(name="原产国")
	private String ycg;//原产国
	@Excel(name="order号")
	private String orderNum;//MR
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
	@Excel(name="生产日期")
	private Date makeTime;//MR
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
	@Excel(name = "是否派车", replace = {"否_0", "是_1"})
	private String ifUseTruck;//派车
	@Excel(name = "是否倒箱", replace = {"否_0", "是_1"})
	private String ifBack;//是否倒箱
	@Excel(name = "是否查验", replace = {"否_0", "是_1"})
	private String ifCheck;//是否查验
	@Excel(name = "是否套袋", replace = {"否_0", "是_1"})
	private String ifBagging;//套袋
	@Excel(name = "是否缠膜", replace = {"否_0", "是_1"})
	private String ifWrap;//缠膜
	@Excel(name = "派车费目", replace = {"短倒费_tc", "短倒费（五期）_tcwq","短倒费（三期）_tcsq","短倒费（四期）_tssiqi"})
	private String feeCode;//选择派车里面费目
	@Excel(name = "倒车车牌号")
	private String dcarNum;//倒车空车车牌号
	@Excel(name = "倒车箱号")
	private String dctnNum;//倒车空车箱号
	@Excel(name = "HS编码")
	private String hscode;//hs编码
	@Excel(name = "海关品名")
	private  String hsItemname;//海关商品名称
	@Excel(name = "账册商品序号")
	private  String accountBook;//账册商品序号
	
	
	public String getAccountBook() {
		return accountBook;
	}
	public void setAccountBook(String accountBook) {
		this.accountBook = accountBook;
	}
	public String getHsItemname() {
		return hsItemname;
	}
	public void setHsItemname(String hsItemname) {
		this.hsItemname = hsItemname;
	}
	public String getHscode() {
		return hscode;
	}
	public void setHscode(String hscode) {
		this.hscode = hscode;
	}
	public String getDcarNum() {
		return dcarNum;
	}
	public void setDcarNum(String dcarNum) {
		this.dcarNum = dcarNum;
	}
	public String getDctnNum() {
		return dctnNum;
	}
	public void setDctnNum(String dctnNum) {
		this.dctnNum = dctnNum;
	}
	public String getBgdh() {
		return bgdh;
	}
	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}
	public String getYcg() {
		return ycg;
	}
	public void setYcg(String ycg) {
		this.ycg = ycg;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
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
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public Date getMakeTime() {
		return makeTime;
	}
	public void setMakeTime(Date makeTime) {
		this.makeTime = makeTime;
	}
	public String getIfUseTruck() {
		return ifUseTruck;
	}
	public void setIfUseTruck(String ifUseTruck) {
		this.ifUseTruck = ifUseTruck;
	}
	public String getIfBack() {
		return ifBack;
	}
	public void setIfBack(String ifBack) {
		this.ifBack = ifBack;
	}
	public String getIfCheck() {
		return ifCheck;
	}
	public void setIfCheck(String ifCheck) {
		this.ifCheck = ifCheck;
	}
	public String getIfBagging() {
		return ifBagging;
	}
	public void setIfBagging(String ifBagging) {
		this.ifBagging = ifBagging;
	}
	public String getIfWrap() {
		return ifWrap;
	}
	public void setIfWrap(String ifWrap) {
		this.ifWrap = ifWrap;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
}