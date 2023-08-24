package com.haiersoft.ccli.wms.entity.apiEntity;


/**
 * @Author sunzhijie
 *
 */
//@Data
//@ApiModel(value = "核放单表体(PassPortList)")
public class PassPortList implements java.io.Serializable{
//	@ApiModelProperty(value = "申报数量")
//	@JSONField(name = "DclQty")
	private String dclQty;
//	@ApiModelProperty(value = "申报单位代码")
//	@JSONField(name = "DclUnitcd")
	private String dclUnitcd;
//	@ApiModelProperty(value = "商品编码")
//	@JSONField(name = "Gdecd")
	private String gdecd;
//	@ApiModelProperty(value = "商品料号")
//	@JSONField(name = "GdsMtno")
	 private String gdsMtno;
//	@ApiModelProperty(value = "商品名称")
//	@JSONField(name = "GdsNm")
	private String gdsNm;
//	@ApiModelProperty(value = "经营单位代码 反填")
//	@JSONField(name = "GdsSpcfModelDesc")
	private String gdsSpcfModelDesc;
//	@ApiModelProperty(value = "货物毛重")
//	@JSONField(name = "GrossWt")
	private String grossWt;
//	@ApiModelProperty(value = "货物净重")
//	@JSONField(name = "NetWt")
	private String netWt;
//	@ApiModelProperty(value = "备案序号")
//	@JSONField(name = "OriactGdsSeqno")
	private String oriactGdsSeqno;
//	@ApiModelProperty(value = "核放单编号")
//	@JSONField(name = "PassportNo")
	private String passportNo;
//	@ApiModelProperty(value = "商品序号")
//	@JSONField(name = "PassportSeqNo")
	private String passportSeqNo;
//	@ApiModelProperty(value = "关联商品序号")
//	@JSONField(name = "RltGdsSeqno")
	private String rltGdsSeqno;
//	@ApiModelProperty(value = "备注")
//	@JSONField(name = "Rmk")
	private String rmk;
//	@ApiModelProperty(value = "预录入统一编号")
//	@JSONField(name = "SeqNo")
	private String seqNo;

	public String getDclQty() {
		return dclQty;
	}

	public void setDclQty(String dclQty) {
		this.dclQty = dclQty;
	}

	public String getDclUnitcd() {
		return dclUnitcd;
	}

	public void setDclUnitcd(String dclUnitcd) {
		this.dclUnitcd = dclUnitcd;
	}

	public String getGdecd() {
		return gdecd;
	}

	public void setGdecd(String gdecd) {
		this.gdecd = gdecd;
	}

	public String getGdsMtno() {
		return gdsMtno;
	}

	public void setGdsMtno(String gdsMtno) {
		this.gdsMtno = gdsMtno;
	}

	public String getGdsNm() {
		return gdsNm;
	}

	public void setGdsNm(String gdsNm) {
		this.gdsNm = gdsNm;
	}

	public String getGdsSpcfModelDesc() {
		return gdsSpcfModelDesc;
	}

	public void setGdsSpcfModelDesc(String gdsSpcfModelDesc) {
		this.gdsSpcfModelDesc = gdsSpcfModelDesc;
	}

	public String getGrossWt() {
		return grossWt;
	}

	public void setGrossWt(String grossWt) {
		this.grossWt = grossWt;
	}

	public String getNetWt() {
		return netWt;
	}

	public void setNetWt(String netWt) {
		this.netWt = netWt;
	}

	public String getOriactGdsSeqno() {
		return oriactGdsSeqno;
	}

	public void setOriactGdsSeqno(String oriactGdsSeqno) {
		this.oriactGdsSeqno = oriactGdsSeqno;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getPassportSeqNo() {
		return passportSeqNo;
	}

	public void setPassportSeqNo(String passportSeqNo) {
		this.passportSeqNo = passportSeqNo;
	}

	public String getRltGdsSeqno() {
		return rltGdsSeqno;
	}

	public void setRltGdsSeqno(String rltGdsSeqno) {
		this.rltGdsSeqno = rltGdsSeqno;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
}
