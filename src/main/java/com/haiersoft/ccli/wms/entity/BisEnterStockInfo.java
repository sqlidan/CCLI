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
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 入库联系单明细的entity
 * @author pyl
 * @date 2016年2月27日
 */
@Entity
@Table(name = "BIS_ENTER_STOCK_INFO")
@DynamicUpdate 
@DynamicInsert
public class BisEnterStockInfo implements Serializable {

    private static final long serialVersionUID = 4040788502963084203L;
    
	// Fields
    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ENTER_STOCK_INFO")
	@SequenceGenerator(name="SEQ_ENTER_STOCK_INFO", sequenceName="SEQ_ENTER_STOCK_INFO", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //主键id

    
    @Column(name = "LINK_ID")
	private String linkId;	//联系单ID
    
    @Column(name = "ITEM_NUM")
	private String itemNum;//提单号
    
    @Column(name = "CARGO_ID")
	private String cargoId;//品名ID
	
	@Column(name = "CARGO_NAME")
	private String cargoName;//品名
	
	@Column(name = "SKU")
	private String sku;//sku

	@Column(name = "TYPE_SIZE")
	private String typeSize;//规格
	
	@Column(name = "CTN_NUM")
	private String ctnNum;//箱号
	
	@Column(name = "PIECE")
	private Integer piece;//件数
	
	@Column(name = "GROSS_WEIGHT")
	private Double grossWeight;//总毛重
	
	@Column(name = "NET_WEIGHT")
	private Double netWeight;//总净重
	
	
	@Column(name = "GROSS_SINGLE")
	private Double grossSingle;//单毛重
	
	@Column(name = "NET_SINGLE")
	private Double netSingle;//单净重
	
	@Column(name = "UNITS")
	private String units;//默认KG重量单位
	
	@Column(name = "PROJECT_NUM")
	private String projectNum;//项目号
	
	@Column(name = "OPERATOR")
	private String operator;//操作人员
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "OPERATE_TIME")
	private Date operateTime;//操作时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;//修改时间
	
	@Column(name = "REMARK")
	private String remark;//备注

	@Column(name = "BIG_TYPE")
	private String bigType;//大类
	
	@Column(name = "BIG_TYPE_NAME")
	private String bigTypeName;//大类名称
	
	@Column(name = "LITTLE_TYPE")
	private String littleType;//小类
	
	@Column(name = "LITTLE_TYPE_NAME")
	private String littleTypeName;//小类名称
	
	@Column(name = "PRICE")
	private Double price;//单价
	
	@Column(name = "RK_NUM")
	private String rkNum;//入库号
	
	@Column(name = "MSC_NUM")
	private String mscNum;//MSC_NUM
	
	@Column(name = "LOT_NUM")
	private String lotNum;//LOT_NUM
	
	@Column(name = "SHIP_NUM")
	private String shipNum;//捕捞船名
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "MAKE_TIME")
	private Date makeTime;//生产时间
	
	@Column(name = "ORDER_NUM")
	private String orderNum;//order号
	
	@Column(name = "CAR_NUM")
	private String carNum;//车牌号
	
	@Column(name = "BGDH")
	private String bgdh;//报关单号2019-1-14
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "BGDHDATE") 
	@Excel(name = "报关申报时间")
	private Date bgdhDate;//报关申报时间
	
	@Column(name = "YCG")
	private String ycg;//原产国
	
	@Column(name = "IF_USE_TRUCK")
	@Excel(name = "是否派车", replace = {"否_0", "是_1"})
	private String ifUseTruck;//派车
	
	@Column(name = "IF_BACK")
	@Excel(name = "是否倒箱", replace = {"否_0", "是_1"})
	private String ifBack;//是否倒箱
	
	@Column(name = "IF_CHECK")
	@Excel(name = "是否查验", replace = {"否_0", "是_1"})
	private String ifCheck;//是否查验
	
	@Column(name = "IF_BAGGING")
	@Excel(name = "是否套袋", replace = {"否_0", "是_1"})
	private String ifBagging;//套袋
	
	@Column(name = "IF_WRAP")
	@Excel(name = "是否缠膜", replace = {"否_0", "是_1"})
	private String ifWrap;//缠膜
	
	@Column(name = "FEECODE")
	@Excel(name = "派车费目", replace = {"短倒费_tc", "短倒费（五期）_tcwq","短倒费（三期）_tcsq","短倒费（四期）_tssiqi"})
	private String feeCode;//选择派车里面费目
	
	@Column(name = "DC_CAR_NUM")
	private String dcarNum;//倒车空车车牌号
	
	@Column(name = "DC_CTN_NUM")
	private String dctnNum;//倒车空车箱号
	/////////////////////////////////////////////////////////////////////////////
	@Column(name = "HS")
	private String hs;//hs编码
	
	@Column(name = "HS_CODE")
	private String hsCode;//hs编码

	@Column(name = "HS_ITEMNAME")
	private String hsItemname;//海关商品名称
	
	@Column(name = "ACCOUNT_BOOK")
	private String accountBook;//账册商品序号
	
	
    //////////////////////////////////////////////////////////////////////////
	

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

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getHs() {
		return hs;
	}

	public void setHs(String hs) {
		this.hs = hs;
	}

	@Transient
	private String ifasn;//是否已导入ASN明细
	
	
	public Date getBgdhDate() {
		return bgdhDate;
	}

	public void setBgdhDate(Date bgdhDate) {
		this.bgdhDate = bgdhDate;
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

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getCargoId() {
		return cargoId;
	}

	public void setCargoId(String cargoId) {
		this.cargoId = cargoId;
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

	public String getCtnNum() {
		return ctnNum;
	}

	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}

	public Integer getPiece() {
		return piece;
	}

	public void setPiece(Integer piece) {
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

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProjectNum() {
		return projectNum;
	}

	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	}

	public Double getGrossSingle() {
		return grossSingle;
	}

	public void setGrossSingle(Double grossSingle) {
		this.grossSingle = grossSingle;
	}

	public Double getNetSingle() {
		return netSingle;
	}

	public void setNetSingle(Double netSingle) {
		this.netSingle = netSingle;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getBigType() {
		return bigType;
	}

	public void setBigType(String bigType) {
		this.bigType = bigType;
	}

	public String getBigTypeName() {
		return bigTypeName;
	}

	public void setBigTypeName(String bigTypeName) {
		this.bigTypeName = bigTypeName;
	}

	public String getLittleType() {
		return littleType;
	}

	public void setLittleType(String littleType) {
		this.littleType = littleType;
	}

	public String getLittleTypeName() {
		return littleTypeName;
	}

	public void setLittleTypeName(String littleTypeName) {
		this.littleTypeName = littleTypeName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public String getIfasn() {
		return ifasn;
	}

	public void setIfasn(String ifasn) {
		this.ifasn = ifasn;
	}

	public Date getMakeTime() {
		return makeTime;
	}

	public void setMakeTime(Date makeTime) {
		this.makeTime = makeTime;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
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
	
}