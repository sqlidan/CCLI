package com.haiersoft.ccli.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 人员类型基础表  20170930
 * @author SL YHN
 * 
 */
@Entity
@Table(name = "BIS_LOADING_ORDER_WORKER")
public class BisLoadingOrderWorker {
	
	/*id             VARCHAR2(12) not null,
	  link_id        VARCHAR2(18),
	  worker_typeid  NUMBER,
	  worker_ypename VARCHAR2(100),
	  worker_name    VARCHAR2(50),
	  ratio          NUMBER(12,4),
	  piece          NUMBER(8),
	  net_weight     NUMBER(12,4),
	  gross_weight   NUMBER(12,4)*/
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private String id;
	
	
	@Column(name = "LINK_ID")
	private String linkId;
	
	@Column(name = "WORKER_TYPEID")
	private Integer workerTypeId;
	
	@Column(name = "WORKER_YPENAME")
	private String workerTypeName;
	
	@Column(name = "WORKER_NAME")
	private String workerName;
	
	@Column(name = "RATIO")
	private Double ratio;
	
	@Column(name = "PIECE")
	private Double piece;
	
	@Column(name = "NET_WEIGHT")
	private Double netWeight;
	
	@Column(name = "GROSS_WEIGHT")
	private Double grossWeight;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public Integer getWorkerTypeId() {
		return workerTypeId;
	}

	public void setWorkerTypeId(Integer workerTypeId) {
		this.workerTypeId = workerTypeId;
	}

	public String getWorkerTypeName() {
		return workerTypeName;
	}

	public void setWorkerTypeName(String workerTypeName) {
		this.workerTypeName = workerTypeName;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	public Double getPiece() {
		return piece;
	}

	public void setPiece(Double piece) {
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

	
	
}
