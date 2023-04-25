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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Connor.M
 * @ClassName: BisScrapTray
 * @Description: 托盘报损实体
 * @date 2016年3月21日 下午3:16:18
 */
@Entity
@Table(name = "BIS_SCRAP_TRAY")
@DynamicUpdate
@DynamicInsert
public class BisScrapTray implements Serializable{

    private static final long serialVersionUID = 2167084924018874257L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SCRAP_TRAY")
	@SequenceGenerator(name = "SEQ_SCRAP_TRAY", sequenceName = "SEQ_SCRAP_TRAY", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;//主键
    
    @Column(name = "SCRAP_CODE")
    private String scrapCode;//报损单号:SEQ_SCRAP_TRAY_CODE
    
    @Column(name = "TRAY_CODE")
    private String trayCode;//托盘号
    
    @Column(name = "BILL_NUM")
	private String billNum;//提单号
    
    @Column(name = "SCRAP_TYPE")
    private String scrapType;//报损类型：1，普通报损，2,库内分拣报损
    
    @Column(name = "SCRAP_STATE")
    private String scrapState;//报损状态，0待确认，1已确认
    
    @Column(name = "ASN")
    private String asn;//ASN
    
    @Column(name = "SKU")
    private String sku;//SKU
    
    @Column(name = "CLIENT_NAME")
   	private String clientName;//客户名称
       
    @Column(name = "CLIENT_ID")
   	private String clientId;//客户ID
    
    @Column(name = "CARGO_NAME")
	private String cargoName;//产品名称
	
	@Column(name = "CARGO_TYPE")
	private String cargoType;//产品类型
	
	@Column(name = "CARGO_LOCATION")
	private String cargoLocation;//库位号
    
	@Column(name = "NUM")
    private Integer num;//数量
    
    @Column(name = "NET_WEIGHT")
   	private Double netWeight;//总净重
   	
    @Column(name = "GROSS_WEIGHT")
   	private Double grossWeight;//总毛重
    
    @Column(name = "UNITS")
  	private String units;//重量单位
    
    @Column(name = "SCRAP_PERSON")
    private String scrapPerson;//报损人
	
	@Column(name = "SCRAP_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date scrapDate;//报损时间
	
	@Column(name = "CONFIRM_PSERSON")
	private String confirmPserson;//确认人
	
	@Column(name = "CONFIRM_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date confirmDate;//确认时间

	public Integer getId() {
		return id;
	}

	public String getScrapState() {
		return scrapState;
	}

	public String getConfirmPserson() {
		return confirmPserson;
	}

	public void setConfirmPserson(String confirmPserson) {
		this.confirmPserson = confirmPserson;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public void setScrapState(String scrapState) {
		this.scrapState = scrapState;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScrapCode() {
		return scrapCode;
	}

	public void setScrapCode(String scrapCode) {
		this.scrapCode = scrapCode;
	}

	public String getTrayCode() {
		return trayCode;
	}

	public void setTrayCode(String trayCode) {
		this.trayCode = trayCode;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getScrapType() {
		return scrapType;
	}

	public void setScrapType(String scrapType) {
		this.scrapType = scrapType;
	}

	public String getAsn() {
		return asn;
	}

	public void setAsn(String asn) {
		this.asn = asn;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getCargoLocation() {
		return cargoLocation;
	}

	public void setCargoLocation(String cargoLocation) {
		this.cargoLocation = cargoLocation;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
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

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getScrapPerson() {
		return scrapPerson;
	}

	public void setScrapPerson(String scrapPerson) {
		this.scrapPerson = scrapPerson;
	}

	public Date getScrapDate() {
		return scrapDate;
	}

	public void setScrapDate(Date scrapDate) {
		this.scrapDate = scrapDate;
	}
    
}
