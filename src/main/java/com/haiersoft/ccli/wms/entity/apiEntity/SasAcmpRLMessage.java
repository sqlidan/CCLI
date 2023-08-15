package com.haiersoft.ccli.wms.entity.apiEntity;

import java.util.List;

/**
 * @ClassName: SasAcmpRLMessage
 * @Description: TODO
 * @Author chenp
 * @Date 2021/3/13 11:01
 *
 */
//@Data
public class SasAcmpRLMessage {

    private String blsNo;
    private String blsType;
    private String chgTmsCnt;
    private List<SasAcmpRLType> sasAcmpRLList;
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

    public List<SasAcmpRLType> getSasAcmpRLList() {
        return sasAcmpRLList;
    }

    public void setSasAcmpRLList(List<SasAcmpRLType> sasAcmpRLList) {
        this.sasAcmpRLList = sasAcmpRLList;
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
