package com.haiersoft.ccli.wms.entity.apiEntity;

//@Data
//@ApiModel(value = "ResultList")
public class PassPortQueryListResponseResultList implements java.io.Serializable{
//    @ApiModelProperty(value = "区内企业名称")
    private String areainEtpsNm;
//    @ApiModelProperty(value = "绑定类型代码")
    private String bindTypecd;
//    @ApiModelProperty(value = "经营企业编号")
    private String bizopEtpsno;
//    @ApiModelProperty("经营企业社会信用代码")
    private String bizopEtpsSccd;
//    @ApiModelProperty(value = "仓库账册号")
    private String bwlNo;

    private String col4;
//    @ApiModelProperty(value = "申报时间")
    private String dclTime;
//    @ApiModelProperty(value = "申报类型：1-备案、2-变更、3-作废。目前核放单只允许备案")
    private String dclTypecd;
//    @ApiModelProperty(value = "企业内部编号")
    private String etpsPreentNo;
//    @ApiModelProperty(value = "进出标志代码")
    private String ioTypecd;
//    @ApiModelProperty(value = "核放单编号")
    private String passportNo;
//    @ApiModelProperty(value = "核放单类型")
    private String passportTypecd;

    private String passStucd;
//    @ApiModelProperty(value = "关联单证编码")
    private String rltNo;
//    @ApiModelProperty(value = "预录入统一编号")
    private String seqNo;
//    @ApiModelProperty(value = "核放单状态")
    private String stucd;

    public String getAreainEtpsNm() {
        return areainEtpsNm;
    }

    public void setAreainEtpsNm(String areainEtpsNm) {
        this.areainEtpsNm = areainEtpsNm;
    }

    public String getBindTypecd() {
        return bindTypecd;
    }

    public void setBindTypecd(String bindTypecd) {
        this.bindTypecd = bindTypecd;
    }

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

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getDclTime() {
        return dclTime;
    }

    public void setDclTime(String dclTime) {
        this.dclTime = dclTime;
    }

    public String getDclTypecd() {
        return dclTypecd;
    }

    public void setDclTypecd(String dclTypecd) {
        this.dclTypecd = dclTypecd;
    }

    public String getEtpsPreentNo() {
        return etpsPreentNo;
    }

    public void setEtpsPreentNo(String etpsPreentNo) {
        this.etpsPreentNo = etpsPreentNo;
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

    public String getPassStucd() {
        return passStucd;
    }

    public void setPassStucd(String passStucd) {
        this.passStucd = passStucd;
    }

    public String getRltNo() {
        return rltNo;
    }

    public void setRltNo(String rltNo) {
        this.rltNo = rltNo;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getStucd() {
        return stucd;
    }

    public void setStucd(String stucd) {
        this.stucd = stucd;
    }
}

