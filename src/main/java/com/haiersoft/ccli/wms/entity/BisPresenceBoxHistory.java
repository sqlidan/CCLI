package com.haiersoft.ccli.wms.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * Created by galaxy on 2017/6/21.
 */
@Entity
@Table(name = "BIS_PRESENCE_BOX_HISTORY")
public class BisPresenceBoxHistory {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRESENCE_BOX_HISTORY")
	@SequenceGenerator(name = "SEQ_PRESENCE_BOX_HISTORY", sequenceName = "SEQ_PRESENCE_BOX_HISTORY", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;  //id 
	@Column(name = "CAR_NUM")
    private String carNum;     //车牌号
	@Column(name = "CTN_NUM")
	private String ctnNum;  //箱号
	@Column(name = "CTN_TYPE")
	private String ctnType;  //箱型
	@Column(name = "CTN_SIZE")
	private String ctnSize;  //箱尺寸
	@Column(name = "ASN")
	private String asn;  //ASN
	@Column(name = "BILL_NUM")
	private String billNum;  //提单号
	@Column(name = "STOCK_ID")
	private String stockId;   //客户ID
	@Column(name = "STOCK_NAME")
	private String stockName;  //客户名称
	@Column(name = "ENTER_NUM")
	private Double enterNum;   //计划入库件数
	@Column(name = "DRIVER_NAME")
	private String driverName;   //司机名
	@Column(name = "STATE")
	private String state;   //箱状态 ： 1.入场 2在场 3出场
	@Column(name = "CREATE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createDate;    //创建日期
	@Column(name = "OUT_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date outDate;    //箱离场时间
	@Column(name = "CREATE_USER")
	private String createUser;   //创建人
	@Column(name = "START_CD_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date startCdTime;    //开始插电时间
	@Column(name = "END_CD_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date endCdTime;    //结束插电时间
	@Column(name = "IF_CD")
	private String ifCd;   //是否插电0：否  1：是
	@Column(name = "CD_STATE")
	private String cdState;   //插电状态  0：未开始  1：开始插电  2：结束插电
	@Transient
	private String ifH;//0在场 1已离场
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public String getCtnType() {
		return ctnType;
	}
	public void setCtnType(String ctnType) {
		this.ctnType = ctnType;
	}
	public String getCtnSize() {
		return ctnSize;
	}
	public void setCtnSize(String ctnSize) {
		this.ctnSize = ctnSize;
	}
	public String getAsn() {
		return asn;
	}
	public void setAsn(String asn) {
		this.asn = asn;
	}
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public Double getEnterNum() {
		return enterNum;
	}
	public void setEnterNum(Double enterNum) {
		this.enterNum = enterNum;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public String getIfH() {
		return ifH;
	}
	public void setIfH(String ifH) {
		this.ifH = ifH;
	}
	public Date getStartCdTime() {
		return startCdTime;
	}
	public void setStartCdTime(Date startCdTime) {
		this.startCdTime = startCdTime;
	}
	public Date getEndCdTime() {
		return endCdTime;
	}
	public void setEndCdTime(Date endCdTime) {
		this.endCdTime = endCdTime;
	}
	public String getIfCd() {
		return ifCd;
	}
	public void setIfCd(String ifCd) {
		this.ifCd = ifCd;
	}
	public String getCdState() {
		return cdState;
	}
	public void setCdState(String cdState) {
		this.cdState = cdState;
	}
	 
}
