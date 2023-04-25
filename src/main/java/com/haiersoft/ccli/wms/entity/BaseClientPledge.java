package com.haiersoft.ccli.wms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 质押表
 * BaseClientPledge entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BASE_CLIENT_PLEDGE")
public class BaseClientPledge implements java.io.Serializable {

    private static final long serialVersionUID = -3966847925353835662L;
    
	private Integer id;
	@Excel(name = "业务编号")
	private String codeNum;//业务编号
	private String client;//客户ID
	@Excel(name = "客户名称")
	private String clientName; //客户名称
	@Excel(name = "SKU")
	private String skuId;//SKU
	@Excel(name = "数量")
	private Double num;//数量
	@Excel(name = "品名")
	private String pname;//品名
	@Excel(name = "件数")
	private Double piece; //件数(暂时未用）
	@Excel(name = "总净重")
	private Double netWeight; //总净重
	@Excel(name = "规格")
	private String norms;//规格
	@Excel(name = "箱号")
	private String ctnNum;//箱号
	@Excel(name = "提单号")
	private String billNum;//提单号
	private String enterState;//入库类型0（成品） ；1（货损）
	@Excel(name = "监管公司")
	private String company;//监管公司
	@Excel(name = "控货日期")
	private Date ctime;//控货日期
	@Excel(name = "控货人")
	private String cuser;//控货人
	private Integer ptype;//质押类型 1静态质押，2总量质押
	private Integer pclass;//控货类型 1暂停发货2质押'
    private String unit; // 重量单位，默认KG
    private String warehouseId;//仓库ID 关联入库联系单获取
    @Excel(name = "仓库名称")
	private String warehouse;//仓库名称
    
    private String track;//质押来源 1为供应链质押
	// Constructors

	/** default constructor */
	public BaseClientPledge() {
	}
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_CLIENT_PLEDGE")
	@SequenceGenerator(name = "SEQUENCE_CLIENT_PLEDGE", sequenceName = "SEQUENCE_CLIENT_PLEDGE", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "CODENUM")
	public String getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(String codeNum) {
		this.codeNum = codeNum;
	}
	@Column(name = "CLIENT")
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}
	@Column(name = "CLIENT_NAME")
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	@Column(name = "SKU_ID")
	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	@Column(name = "NUM")
	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}
	@Column(name = "PNAME")
	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}
	@Column(name = "NORMS")
	public String getNorms() {
		return norms;
	}

	public void setNorms(String norms) {
		this.norms = norms;
	}
	@Column(name = "CTN_NUM")
	public String getCtnNum() {
		return ctnNum;
	}

	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}
	@Column(name = "BILL_NUM")
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@Column(name = "Piece")
	public Double getPiece() {
		return piece;
	}
	public void setPiece(Double piece) {
		this.piece = piece;
	}
	
	@Column(name = "NET_WEIGHT")
	public Double getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}
	
	@Column(name = "COMPANY")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CTIME")
	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	@Column(name = "CUSER")
	public String getCuser() {
		return cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}
	@Column(name = "PTYPE")
	public Integer getPtype() {
		return ptype;
	}

	public void setPtype(Integer ptype) {
		this.ptype = ptype;
	}
	@Column(name = "PCLASS")
	public Integer getPclass() {
		return pclass;
	}

	public void setPclass(Integer pclass) {
		this.pclass = pclass;
	}
	@Column(name = "ENTER_STATE")
	public String getEnterState() {
		return enterState;
	}
	public void setEnterState(String enterState) {
		this.enterState = enterState;
	}
	
	@Column(name = "UNIT")
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	@Column(name = "WAREHOUSE_ID")
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	@Column(name = "WAREHOUSE")
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	
	@Column(name = "TRACK")
	public String getTrack() {
		return track;
	}
	public void setTrack(String track) {
		this.track = track;
	}
	
	

	 
	 
	 
}