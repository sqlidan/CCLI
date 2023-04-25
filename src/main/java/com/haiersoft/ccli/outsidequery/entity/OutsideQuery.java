package com.haiersoft.ccli.outsidequery.entity;

/**
 * 在库信息返回
 * @author 
 *
 */
//@Entity
public class OutsideQuery {


	private Integer pieces; //件数

	private String clientName; //客户名称
	private String cargoName; //产品名称
	private Double allnet;//总净重
	private Double allgross;//总毛重
	private String simName;//小类名称

	private String operationTime;//操作时间 出库时为出库时间，入库时为入库时间，查询库存时为入库时间

	public Integer getPieces() {
		return pieces;
	}

	public void setPieces(Integer pieces) {
		this.pieces = pieces;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public Double getAllnet() {
		return allnet;
	}

	public void setAllnet(Double allnet) {
		this.allnet = allnet;
	}

	public Double getAllgross() {
		return allgross;
	}

	public void setAllgross(Double allgross) {
		this.allgross = allgross;
	}

	public String getSimName() {
		return simName;
	}

	public void setSimName(String simName) {
		this.simName = simName;
	}

	public String getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}



}
