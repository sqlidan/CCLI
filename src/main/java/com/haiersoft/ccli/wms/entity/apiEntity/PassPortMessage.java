package com.haiersoft.ccli.wms.entity.apiEntity;


import java.util.List;

/**
 * @Author sunzhijie
 */
//@Data
//@ApiModel(value = "PassPortMessage")
//@NoArgsConstructor
public class PassPortMessage implements java.io.Serializable {
//	@ApiModelProperty(value = "操作卡的海关十位")
//	@JSONField(name = "OperCusRegCode")
	private String operCusRegCode;
//	@ApiModelProperty(value = "操作信息")
//	@JSONField(name = "OperInfo")
	private String operInfo;
//	@ApiModelProperty(value = "核放单关联单证")
//	@JSONField(name = "PassportAcmp")
	private List<PassPortAcmp> passportAcmp;
//	@ApiModelProperty(value = "核放单表头")
//	@JSONField(name = "PassportHead")
	private PassPortHead passportHead;
//	@ApiModelProperty(value = "核放单表体")
//	@JSONField(name = "PassportList")
	private List<PassPortList> passportList;
//	@ApiModelProperty(value = "状态")
//	@JSONField(name = "Status")
	private String status;
	//	@ApiModelProperty(value = "鉴权实体")
//	private SecurityModel securityModel;
//	@ApiModelProperty(value = "电子口岸卡密码")
	private String pass;
//	@ApiModelProperty(value = "关务平台租户编码")
	private String memberCode;
//	@ApiModelProperty(value = "海关卡卡号")
	private String icCode;
//	@ApiModelProperty(value = "api中台秘钥")
	private String key;

	public String getOperCusRegCode() {
		return operCusRegCode;
	}

	public void setOperCusRegCode(String operCusRegCode) {
		this.operCusRegCode = operCusRegCode;
	}

	public String getOperInfo() {
		return operInfo;
	}

	public void setOperInfo(String operInfo) {
		this.operInfo = operInfo;
	}

	public List<PassPortAcmp> getPassportAcmp() {
		return passportAcmp;
	}

	public void setPassportAcmp(List<PassPortAcmp> passportAcmp) {
		this.passportAcmp = passportAcmp;
	}

	public PassPortHead getPassportHead() {
		return passportHead;
	}

	public void setPassportHead(PassPortHead passportHead) {
		this.passportHead = passportHead;
	}

	public List<PassPortList> getPassportList() {
		return passportList;
	}

	public void setPassportList(List<PassPortList> passportList) {
		this.passportList = passportList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getIcCode() {
		return icCode;
	}

	public void setIcCode(String icCode) {
		this.icCode = icCode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
