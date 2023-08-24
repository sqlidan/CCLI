package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @Author sunzhijie
 */
//@Data
//@ApiModel(value = "物流账册详细数据查询服务--传输")
//@NoArgsConstructor
public class SasCommonSeqNoRequest implements java.io.Serializable {
//    @ApiModelProperty(value = "业务单据统一编号")
    private String BlsNo;

//    @ApiModelProperty(value = "业务单证类型")
    private String BlsType;

//    @ApiModelProperty(value = "变更次数")
    private String ChgTmsCnt;

//    @ApiModelProperty(value = "操作卡海关十位编码")
    private String OperCusRegCode;

//    @ApiModelProperty(value = "关联商品序号")
    private String RltGdsSeqno;

//    @ApiModelProperty(value = "中心统一编号")
    private String seqNo;

    //	private SecurityModel securityModel;
//    @ApiModelProperty(value = "电子口岸卡密码")
    private String pass;
//    @ApiModelProperty(value = "关务平台租户编码")
    private String memberCode;
//    @ApiModelProperty(value = "海关卡卡号")
    private String icCode;
//    @ApiModelProperty(value = "api中台秘钥")
    private String key;

    public String getBlsNo() {
        return BlsNo;
    }

    public void setBlsNo(String blsNo) {
        BlsNo = blsNo;
    }

    public String getBlsType() {
        return BlsType;
    }

    public void setBlsType(String blsType) {
        BlsType = blsType;
    }

    public String getChgTmsCnt() {
        return ChgTmsCnt;
    }

    public void setChgTmsCnt(String chgTmsCnt) {
        ChgTmsCnt = chgTmsCnt;
    }

    public String getOperCusRegCode() {
        return OperCusRegCode;
    }

    public void setOperCusRegCode(String operCusRegCode) {
        OperCusRegCode = operCusRegCode;
    }

    public String getRltGdsSeqno() {
        return RltGdsSeqno;
    }

    public void setRltGdsSeqno(String rltGdsSeqno) {
        RltGdsSeqno = rltGdsSeqno;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
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

