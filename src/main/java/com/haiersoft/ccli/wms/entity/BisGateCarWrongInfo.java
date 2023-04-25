package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "BIS_GATE_CAR_WRONG_INFO")
public class BisGateCarWrongInfo implements Serializable{

	private static final long serialVersionUID = -4489593373628482490L;
	
	@Id
    @GeneratedValue(generator="wrongInfoGenerator")
	@GenericGenerator(name="wrongInfoGenerator", strategy="uuid")
	@Column(name = "ID", unique = true, nullable = false)
	private String id;  //id 
	@Column(name = "CAR_NUM")
    private String carNum;//车牌号
	@Column(name = "CTN_NUM")
	private String ctnNum;  //箱号
	@Column(name = "REASON")
	private String reason;  //原因
	@Column(name="CREATE_DATE")
	private String createDate; //日期
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getCtnNum() {
		return ctnNum;
	}
	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "BisGateCarWrongInfo [id=" + id + ", carNum=" + carNum + ", ctnNum=" + ctnNum + ", reason=" + reason
				+ ", createDate=" + createDate + "]";
	}
	


}