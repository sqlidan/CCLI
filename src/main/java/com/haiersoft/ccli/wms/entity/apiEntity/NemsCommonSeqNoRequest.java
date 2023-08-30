package com.haiersoft.ccli.wms.entity.apiEntity;


/**
 * @Author sunzhijie
 */
//@Data
//@ApiModel(value = "保税核注清单详细查询服务--传输")
public class NemsCommonSeqNoRequest implements java.io.Serializable {
//    @ApiModelProperty(value = "业务单据统一编号")
    private String blsNo;
//    @ApiModelProperty(value = "业务单证类型")
    private String blsType;
//    @ApiModelProperty(value = "变更或报核次数")
    private String chgTmsCnt;
//    @ApiModelProperty(value = "操作卡的海关十位")
    private String operCusRegCode;
//    @ApiModelProperty(value = "预录入统一编号")
    private String seqNo;
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

    public String getBlsNo() {
        return blsNo;
    }

    public void setBlsNo(String blsNo) {
        this.blsNo = blsNo;
    }

    public String getBlsType() {
        return blsType;
    }

    public void setBlsType(String blsType) {
        this.blsType = blsType;
    }

    public String getChgTmsCnt() {
        return chgTmsCnt;
    }

    public void setChgTmsCnt(String chgTmsCnt) {
        this.chgTmsCnt = chgTmsCnt;
    }

    public String getOperCusRegCode() {
        return operCusRegCode;
    }

    public void setOperCusRegCode(String operCusRegCode) {
        this.operCusRegCode = operCusRegCode;
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