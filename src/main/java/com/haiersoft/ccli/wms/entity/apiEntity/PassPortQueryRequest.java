package com.haiersoft.ccli.wms.entity.apiEntity;

//@Data
//@ApiModel(value = "PassPortQueryRequest")
public class PassPortQueryRequest implements java.io.Serializable{
//    @ApiModelProperty(value = "区内企业编码")
    private String areainEtpsNo;
//    @ApiModelProperty(value = "绑定类型代码")
    private String bindTypecd;
//    @ApiModelProperty(value = "企业内部编号")
    private String etpsPreentNo;
//    @ApiModelProperty(value = "录入单位代码")
    private String inputCode;
//    @ApiModelProperty(value = "录入日期结束")
    private String inputDateEnd;
//    @ApiModelProperty(value = "录入日期开始")
    private String inputDateStart;
//    @ApiModelProperty(value = "进出标志代码")
    private String ioTypecd;
//    @ApiModelProperty(value = "核放单编号")
    private String passportNo;
//    @ApiModelProperty(value = "核放单类型")
    private String passportTypecd;
//    @ApiModelProperty(value = "关联单证编码")
    private String rltNo;
//    @ApiModelProperty(value = "关联单证类型")
    private String rltTbTypecd;
//    @ApiModelProperty(value = "海关编码")
    private String selTradeCode;
//    @ApiModelProperty(value = "预录入统一编码")
    private String seqNo;
//    @ApiModelProperty(value = "状态")
    private String status;

    //	private SecurityModel securityModel;
//    @ApiModelProperty(value = "电子口岸卡密码")
    private String pass;
//    @ApiModelProperty(value = "关务平台租户编码")
    private String memberCode;
//    @ApiModelProperty(value = "海关卡卡号")
    private String icCode;
//    @ApiModelProperty(value = "api中台秘钥")
    private String key;

    public String getAreainEtpsNo() {
        return areainEtpsNo;
    }

    public void setAreainEtpsNo(String areainEtpsNo) {
        this.areainEtpsNo = areainEtpsNo;
    }

    public String getBindTypecd() {
        return bindTypecd;
    }

    public void setBindTypecd(String bindTypecd) {
        this.bindTypecd = bindTypecd;
    }

    public String getEtpsPreentNo() {
        return etpsPreentNo;
    }

    public void setEtpsPreentNo(String etpsPreentNo) {
        this.etpsPreentNo = etpsPreentNo;
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

    public String getIoTypecd() {
        return ioTypecd;
    }

    public void setIoTypecd(String ioTypecd) {
        this.ioTypecd = ioTypecd;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportTypecd() {
        return passportTypecd;
    }

    public void setPassportTypecd(String passportTypecd) {
        this.passportTypecd = passportTypecd;
    }

    public String getRltNo() {
        return rltNo;
    }

    public void setRltNo(String rltNo) {
        this.rltNo = rltNo;
    }

    public String getRltTbTypecd() {
        return rltTbTypecd;
    }

    public void setRltTbTypecd(String rltTbTypecd) {
        this.rltTbTypecd = rltTbTypecd;
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
}
