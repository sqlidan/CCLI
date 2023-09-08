package com.haiersoft.ccli.wms.entity.apiEntity;

//@Data
//@ApiModel(value = "ResultList")
public class BwlQueryListResponseResultList implements java.io.Serializable{
//    @ApiModelProperty(value = "经营企业名称")
    private String bizopEtpsNm;
//    @ApiModelProperty(value = "经营企业编号")
    private String bizopEtpsno;
//    @ApiModelProperty("经营企业社会信用代码")
    private String bizopEtpsSccd;
//    @ApiModelProperty(value = "仓库账册号")
    private String bwlNo;
//    @ApiModelProperty(value = "申报时间")
    private String dclTime;
//    @ApiModelProperty(value = "申报类型：1-备案、2-变更、3-作废。目前核放单只允许备案")
    private String dclTypeCd;
//    @ApiModelProperty(value = "企业内部编号")
    private String etpsPreentNo;
//    @ApiModelProperty(value = "结束有效日期")
    private String finishValidDate;
//    @ApiModelProperty(value = "预录入统一编号")
    private String seqNo;
//    @ApiModelProperty(value = "状态")
    private String status;

    public String getBizopEtpsNm() {
        return bizopEtpsNm;
    }

    public void setBizopEtpsNm(String bizopEtpsNm) {
        this.bizopEtpsNm = bizopEtpsNm;
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

    public String getDclTime() {
        return dclTime;
    }

    public void setDclTime(String dclTime) {
        this.dclTime = dclTime;
    }

    public String getDclTypeCd() {
        return dclTypeCd;
    }

    public void setDclTypeCd(String dclTypeCd) {
        this.dclTypeCd = dclTypeCd;
    }

    public String getEtpsPreentNo() {
        return etpsPreentNo;
    }

    public void setEtpsPreentNo(String etpsPreentNo) {
        this.etpsPreentNo = etpsPreentNo;
    }

    public String getFinishValidDate() {
        return finishValidDate;
    }

    public void setFinishValidDate(String finishValidDate) {
        this.finishValidDate = finishValidDate;
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
}
