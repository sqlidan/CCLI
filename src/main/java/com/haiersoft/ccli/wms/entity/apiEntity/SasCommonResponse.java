package com.haiersoft.ccli.wms.entity.apiEntity;


/**
 * @ClassName: SasCommonResponse
 * @Description: 核放单通用响应信息
 * @Author chenp
 * @Date 2020/12/5 14:05
 *
 */
//@Data
//@ApiModel(value = "核放单通用响应信息")
public class SasCommonResponse implements java.io.Serializable{
//	@ApiModelProperty(value = "中心统一编号")
	private String seqNo;
//	@ApiModelProperty(value = "客户端内部编号")
	private String etpsPreentNo;
//	@ApiModelProperty(value = "响应信息")
	private String checkInfo;
//	@ApiModelProperty(value = "响应代码")
	private String dealFlag;
//	@ApiModelProperty(value = "clientSeqNo")
	private String clientSeqNo;
//	@ApiModelProperty(value = "申报时间")
	private String dclDate;
//	@ApiModelProperty(value = "预录入统一编号")
	private String preNo;
//	@ApiModelProperty(value = "状态")
	private String state;

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getEtpsPreentNo() {
		return etpsPreentNo;
	}

	public void setEtpsPreentNo(String etpsPreentNo) {
		this.etpsPreentNo = etpsPreentNo;
	}

	public String getCheckInfo() {
		return checkInfo;
	}

	public void setCheckInfo(String checkInfo) {
		this.checkInfo = checkInfo;
	}

	public String getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}

	public String getClientSeqNo() {
		return clientSeqNo;
	}

	public void setClientSeqNo(String clientSeqNo) {
		this.clientSeqNo = clientSeqNo;
	}

	public String getDclDate() {
		return dclDate;
	}

	public void setDclDate(String dclDate) {
		this.dclDate = dclDate;
	}

	public String getPreNo() {
		return preNo;
	}

	public void setPreNo(String preNo) {
		this.preNo = preNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
