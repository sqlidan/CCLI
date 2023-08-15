package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @Author sunzhijie
 */
//@Data
//@ApiModel(value = "InvtQueryListRequest")
public class InvtQueryListRequest implements java.io.Serializable {
    //    @ApiModelProperty(value = "经营企业编号")
    public String bizopEtpsNo;
    //    @ApiModelProperty(value = "保税清单编号")
    public String bondInvtNo;
    //    @ApiModelProperty("进出口标记代码（I-进口 E-出口）")
    public String impExpMarkCd;
    //    @ApiModelProperty(value = "录入单位代码")
    public String inputCode;
    //    @ApiModelProperty(value = "录入日期结束")
    public String inputDateEnd;
    //    @ApiModelProperty(value = "录入日期开始")
    public String inputDateStart;
    //    @ApiModelProperty(value = "海关编码")
    public String selTradeCode;
    //    @ApiModelProperty(value = "预录入统一编号")
    public String seqNo;
    //    @ApiModelProperty(value = "状态")
    public String status;
    //    @ApiModelProperty(value = "子系统ID")
    public String sysId;
    //	//    @ApiModelProperty(value = "鉴权实体")
//	private SecurityModel securityModel;
    //    @ApiModelProperty(value = "电子口岸卡密码")
    private String pass;
    //    @ApiModelProperty(value = "关务平台租户编码")
    private String memberCode;
    //    @ApiModelProperty(value = "海关卡卡号")
    private String icCode;
    //    @ApiModelProperty(value = "api中台秘钥")
    private String key;

    public String getBizopEtpsNo() {
        return bizopEtpsNo;
    }

    public void setBizopEtpsNo(String bizopEtpsNo) {
        this.bizopEtpsNo = bizopEtpsNo;
    }

    public String getBondInvtNo() {
        return bondInvtNo;
    }

    public void setBondInvtNo(String bondInvtNo) {
        this.bondInvtNo = bondInvtNo;
    }

    public String getImpExpMarkCd() {
        return impExpMarkCd;
    }

    public void setImpExpMarkCd(String impExpMarkCd) {
        this.impExpMarkCd = impExpMarkCd;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    public String getInputDateEnd() {
        return inputDateEnd;
    }

    public void setInputDateEnd(String inputDateEnd) {
        this.inputDateEnd = inputDateEnd;
    }

    public String getInputDateStart() {
        return inputDateStart;
    }

    public void setInputDateStart(String inputDateStart) {
        this.inputDateStart = inputDateStart;
    }

    public String getSelTradeCode() {
        return selTradeCode;
    }

    public void setSelTradeCode(String selTradeCode) {
        this.selTradeCode = selTradeCode;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
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