package com.haiersoft.ccli.wms.entity.apiEntity;

//@Data
//@ApiModel(value = "BwlQueryListRequest")
public class BwlQueryListRequest implements java.io.Serializable{
//    @ApiModelProperty(value = "经营企业编号")
    private String bizopEtpsno;
//    @ApiModelProperty("经营企业社会信用代码")
    private String bizopEtpsSccd;
//    @ApiModelProperty(value = "仓库账册号")
    private String bwlNo;
//    @ApiModelProperty(value = "录入单位代码")
    private String inputCode;
//    @ApiModelProperty(value = "录入日期结束")
    private String inputDateEnd;
//    @ApiModelProperty(value = "录入日期开始")
    private String inputDateStart;
//    @ApiModelProperty(value = "海关编码")
    private String selTradeCode;
//    @ApiModelProperty(value = "预录入统一编号")
    private String seqNo;
//    @ApiModelProperty(value = "状态")
    private String status;
    //	@ApiModelProperty(value = "鉴权实体")
//	private SecurityModel securityModel;
//    @ApiModelProperty(value = "电子口岸卡密码")
    private String pass;
//    @ApiModelProperty(value = "关务平台租户编码")
    private String memberCode;
//    @ApiModelProperty(value = "海关卡卡号")
    private String icCode;
//    @ApiModelProperty(value = "api中台秘钥")
    private String key;

//    @ApiModelProperty(value = "商品序号")
    private String gdsSeqNo;

    public String getBizopEtpsno() {
        return bizopEtpsno;
    }

    public void setBizopEtpsno(String bizopEtpsno) {
        this.bizopEtpsno = bizopEtpsno;
    }

    public String getBizopEtpsSccd() {
        return bizopEtpsSccd;
    }

    public void setBizopEtpsSccd(String bizopEtpsSccd) {
        this.bizopEtpsSccd = bizopEtpsSccd;
    }

    public String getBwlNo() {
        return bwlNo;
    }

    public void setBwlNo(String bwlNo) {
        this.bwlNo = bwlNo;
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

    public String getGdsSeqNo() {
        return gdsSeqNo;
    }

    public void setGdsSeqNo(String gdsSeqNo) {
        this.gdsSeqNo = gdsSeqNo;
    }
}