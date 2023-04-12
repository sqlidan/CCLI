package com.haiersoft.ccli.wms.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 出库联系单明细
 * BisOutStockInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_OUT_STOCK_INFO")
public class BisOutStockInfo implements java.io.Serializable {
	 
	private static final long serialVersionUID = 4040788502963084203L;
	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OUT_STOCK_INFO")
	@SequenceGenerator(name="SEQ_OUT_STOCK_INFO", sequenceName="SEQ_OUT_STOCK_INFO", allocationSize = 1)  
    @Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "OUT_LINK_ID")
	private String outLinkId;  //出库联系单ID
	
	@Column(name = "BILL_NUM")
	private String billNum;   //提单号
	
	@Column(name = "WAREHOUSE_ID")
	private String warehouseId;  //仓库ID
	
	@Column(name = "CTN_NUM")
	private String ctnNum;   //箱号
	
	@Column(name = "CARGO_NAME")
	private String cargoName;  //品名
	
	@Column(name = "SKU_ID")
	private String skuId;  //sku
	
	@Column(name = "TYPE_SIZE")
	private String typeSize;   //规格
	
	@Column(name = "PIECE")
	private Integer piece;  //sku总件数
	
	@Column(name = "GROSS_SINGLE")
	private Double grossSingle;  //总毛重
	
	@Column(name = "NET_SINGLE")
	private Double netSingle;  //总净重
	
	@Column(name = "GROSS_WEIGHT")
	private Double grossWeight;  //总毛重
	
	@Column(name = "NET_WEIGHT")
	private Double netWeight;  //总净重
	
	@Column(name = "UNITS")
	private String units;  //重量单位
	
	@Column(name = "SALES_NUM")
	private String salesNum;  //销售号
	
	@Column(name = "OUT_NUM")
	private Integer outNum;  //出库件数
	
	@Column(name = "OPERATOR")
	private String operator;  //操作人员
	
	@Column(name = "OPERATE_TIME")
	private Date operateTime;  //操作时间
	
	@Column(name = "REMARK1")
	private String remark;  //备注

	@Column(name = "ENTER_STATE")
	private String enterState;  
	//默认为0（成品） ；1 货物货损 ；2 包装破损   入库状态
	
	@Column(name = "RK_NUM")
	private String rkNum;  //入库号
	
	
	@Column(name = "ORDER_NUM")
	private String orderNum;  //order号
	
	@Column(name = "PROJECT_NUM")
	private String projectNum;  //项目号
	
	@Column(name = "SHIP_NUM")
	private String shipNum;  //船号
	
	@Column(name = "MSC_NUM")
	private String mscNum;//MSC
	
	@Column(name = "LOT_NUM")
	private String lotNum;//lotNum1
	
	@Column(name = "ASN")
	private String asn;//asn
	
	@Column(name = "MAKE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date makeTime;  //制造时间
	
	@Column(name = "CAR_NUM")
	private String carNum;//车牌号
	
	@Column(name = "CODE_NUM")
	private Integer codeNum;  //抄码数
	
	
	
	
	public Integer getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(Integer codeNum) {
		this.codeNum = codeNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOutLinkId() {
		return outLinkId;
	}

	public void setOutLinkId(String outLinkId) {
		this.outLinkId = outLinkId;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
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

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
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

	public String getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(String salesNum) {
		this.salesNum = salesNum;
	}

	public Integer getOutNum() {
		return outNum;
	}

	public void setOutNum(Integer outNum) {
		this.outNum = outNum;
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

	public String getRkNum() {
		return rkNum;
	}

	public void setRkNum(String rkNum) {
		this.rkNum = rkNum;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getProjectNum() {
		return projectNum;
	}

	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	}

	public String getShipNum() {
		return shipNum;
	}

	public void setShipNum(String shipNum) {
		this.shipNum = shipNum;
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

	public Date getMakeTime() {
		return makeTime;
	}

	public void setMakeTime(Date makeTime) {
		this.makeTime = makeTime;
	}

	public String getAsn() {
		return asn;
	}

	public void setAsn(String asn) {
		this.asn = asn;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
}