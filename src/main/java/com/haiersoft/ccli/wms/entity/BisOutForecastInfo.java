package com.haiersoft.ccli.wms.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/**
 * BisForecastInfo entity. @author MyEclipse Persistence Tools
 * 出库预报单货物明细
 */
@Entity
@Table(name = "BIS_OUT_FORECAST_INFO")
public class BisOutForecastInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8042089385911001422L;

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OUT_FORECAST_INFO")
	@SequenceGenerator(name="SEQ_OUT_FORECAST_INFO", sequenceName="SEQ_OUT_FORECAST_INFO", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //主键id
	
	@Column(name = "FOR_ID")
	private String forId;  //预报单ID
	
	@Column(name = "BILL_NUM")
	private String billNum;  //提单号
	
	@Column(name = "PRICE")
	private Integer piece;  //件数
	
	@Column(name = "NET_WEIGHT")
	private Double netWeight;  //净重
	
	@Column(name = "CARGO_NAME")
	private String cargoName;  //商品名称
	
	@Column(name = "HSCODE")
	private String hscode;  //编码
	
	@Column(name = "SPACE")
	private String space;  //规格型号
	
	@Column(name = "TRAY_NUM")
	private Integer trayNum;  //木托数量
	
	@Column(name = "CLEAR_STORE")
	private String clearStore;  //清库、清品名
	
	@Column(name = "REMARK")
	private String remark;  //备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getForId() {
		return forId;
	}

	public void setForId(String forId) {
		this.forId = forId;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
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

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public String getHscode() {
		return hscode;
	}

	public void setHscode(String hscode) {
		this.hscode = hscode;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public Integer getTrayNum() {
		return trayNum;
	}

	public void setTrayNum(Integer trayNum) {
		this.trayNum = trayNum;
	}

	public String getClearStore() {
		return clearStore;
	}

	public void setClearStore(String clearStore) {
		this.clearStore = clearStore;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


}