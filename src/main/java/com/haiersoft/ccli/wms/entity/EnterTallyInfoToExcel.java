package com.haiersoft.ccli.wms.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.util.Date;


@ExcelTarget("EnterTallyInfoToExcel")
public class EnterTallyInfoToExcel implements Serializable {
	
    private static final long serialVersionUID = 8270056740403940286L;
    
	@Excel(name="ASN")
	private String asn;//ASN
	@Excel(name="SKU")
	private String sku;//SKU
	@Excel(name="托盘号")
	private String tallyNo;//托盘号
	@Excel(name="状态")
	private String state;//状态0成品，1货损
	@Excel(name="数量")
	private String num;//数量


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

	public String getTallyNo() {
		return tallyNo;
	}

	public void setTallyNo(String tallyNo) {
		this.tallyNo = tallyNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
}