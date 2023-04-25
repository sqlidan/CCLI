package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class BisGateCarParams implements Serializable{

	private static final long serialVersionUID = 2764816925707935086L;
	
	private String carNum; //车牌号
	private List<String> ctnNum; //箱号
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public List<String> getCtnNum() {
		return ctnNum;
	}
	public void setCtnNum(List<String> ctnNum) {
		this.ctnNum = ctnNum;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "BisGateCarParams [carNum=" + carNum + ", ctnNum=" + ctnNum + "]";
	}
	

	


	
}
