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
 * BisForecastInfo entity. @author MyEclipse Persistence Tools
 * 入库预报单货物明细
 */
@Entity
@Table(name = "BIS_FORECAST_INFO")
public class BisForecastInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4298965739518170761L;

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FORECAST_INFO")
	@SequenceGenerator(name="SEQ_FORECAST_INFO", sequenceName="SEQ_FORECAST_INFO", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //主键id
	
	@Column(name = "FOR_ID")
	private String forId;  //预报单ID
	
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
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@Column(name = "ETA")
	private Date eta;  //ETA
	
	@Column(name = "TRAY_NUM")
	private Integer trayNum;  //木托数量
	
	@Column(name = "ITEM_NUM")
	private String itemNum;  //项号
	
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

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Integer getTrayNum() {
		return trayNum;
	}

	public void setTrayNum(Integer trayNum) {
		this.trayNum = trayNum;
	}

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


}