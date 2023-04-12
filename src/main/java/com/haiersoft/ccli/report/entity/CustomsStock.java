package com.haiersoft.ccli.report.entity;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Mazy
 * @ClassName: OutStock
 * @Description: 海关出库报表
 * @date 2016年6月24日 上午9:44:43
 */

public class CustomsStock implements Serializable{

    private static final long serialVersionUID = 2799111007612445959L;
	
	// Fields
	private String ITEM_NUM;//项号
	private String CARGO_NAME;//品名	
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date DECLARE_TIME;//入库报关时间	
	private String CD_NUM;//入库报关单号	
	private String BILL_NUM;//入库提单号	
	private Double NET_WEIGHT;//入库净重
	private Date OUTDECLARETIME;//出库报关时间	
	private String OUTCDNUM;//出库报关单号	
	private Double OUTNETWEIGHT;//出库净重
	private Double UNIT_PRICE;//单价
	private Double KCNETWEIGHT;//库存净重	
	private Double KCSCALAR;//件数	
	private Double TOTALPRICES;//货值
	private Date starTime;//开始年月
	private Date endTime;//结束年月
	public String getITEM_NUM() {
		return ITEM_NUM;
	}
	public void setITEM_NUM(String iTEM_NUM) {
		ITEM_NUM = iTEM_NUM;
	}
	public String getCARGO_NAME() {
		return CARGO_NAME;
	}
	public void setCARGO_NAME(String cARGO_NAME) {
		CARGO_NAME = cARGO_NAME;
	}
	public Date getDECLARE_TIME() {
		return DECLARE_TIME;
	}
	public void setDECLARE_TIME(Date dECLARE_TIME) {
		DECLARE_TIME = dECLARE_TIME;
	}
	public String getCD_NUM() {
		return CD_NUM;
	}
	public void setCD_NUM(String cD_NUM) {
		CD_NUM = cD_NUM;
	}
	public String getBILL_NUM() {
		return BILL_NUM;
	}
	public void setBILL_NUM(String bILL_NUM) {
		BILL_NUM = bILL_NUM;
	}
	public Double getNET_WEIGHT() {
		return NET_WEIGHT;
	}
	public void setNET_WEIGHT(Double nET_WEIGHT) {
		NET_WEIGHT = nET_WEIGHT;
	}
	public Date getOUTDECLARETIME() {
		return OUTDECLARETIME;
	}
	public void setOUTDECLARETIME(Date oUTDECLARETIME) {
		OUTDECLARETIME = oUTDECLARETIME;
	}
	public String getOUTCDNUM() {
		return OUTCDNUM;
	}
	public void setOUTCDNUM(String oUTCDNUM) {
		OUTCDNUM = oUTCDNUM;
	}
	public Double getOUTNETWEIGHT() {
		return OUTNETWEIGHT;
	}
	public void setOUTNETWEIGHT(Double oUTNETWEIGHT) {
		OUTNETWEIGHT = oUTNETWEIGHT;
	}
	public Double getUNIT_PRICE() {
		return UNIT_PRICE;
	}
	public void setUNIT_PRICE(Double uNIT_PRICE) {
		UNIT_PRICE = uNIT_PRICE;
	}
	public Double getKCNETWEIGHT() {
		return KCNETWEIGHT;
	}
	public void setKCNETWEIGHT(Double kCNETWEIGHT) {
		KCNETWEIGHT = kCNETWEIGHT;
	}
	public Double getKCSCALAR() {
		return KCSCALAR;
	}
	public void setKCSCALAR(Double kCSCALAR) {
		KCSCALAR = kCSCALAR;
	}
	public Double getTOTALPRICES() {
		return TOTALPRICES;
	}
	public void setTOTALPRICES(Double tOTALPRICES) {
		TOTALPRICES = tOTALPRICES;
	}
	public Date getStarTime() {
		return starTime;
	}
	public void setStarTime(Date starTime) {
		this.starTime = starTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	

}
