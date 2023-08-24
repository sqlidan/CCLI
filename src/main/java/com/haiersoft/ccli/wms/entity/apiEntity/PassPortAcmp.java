package com.haiersoft.ccli.wms.entity.apiEntity;


/**
 * @Author sunzhijie
 *
 */
//@Data
//@ApiModel(value = "核放单关联单证(PassportAcmp)")
public class PassPortAcmp implements java.io.Serializable{
//	@ApiModelProperty(value = "核放单关联单证统一编号")
	private String passportAcmpSeqNo;
//	@ApiModelProperty(value = "核放单编号")
	private String passPortNo;
//	@ApiModelProperty(value = "关联单证编号")
	private String rtlBillNo;
//	@ApiModelProperty(value = "关联单证类型")
	private String rtlBillTypecd;
//	@ApiModelProperty(value = "预录入统一编号")
	private String seqNo;

	public String getPassportAcmpSeqNo() {
		return passportAcmpSeqNo;
	}

	public void setPassportAcmpSeqNo(String passportAcmpSeqNo) {
		this.passportAcmpSeqNo = passportAcmpSeqNo;
	}

	public String getPassPortNo() {
		return passPortNo;
	}

	public void setPassPortNo(String passPortNo) {
		this.passPortNo = passPortNo;
	}

	public String getRtlBillNo() {
		return rtlBillNo;
	}

	public void setRtlBillNo(String rtlBillNo) {
		this.rtlBillNo = rtlBillNo;
	}

	public String getRtlBillTypecd() {
		return rtlBillTypecd;
	}

	public void setRtlBillTypecd(String rtlBillTypecd) {
		this.rtlBillTypecd = rtlBillTypecd;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
}
