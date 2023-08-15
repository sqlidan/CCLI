package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @Author chenp
 * @Description 核注清单报关单表头临时报关单表头
 * @Date 10:09 2020/12/2
 **/
//@Data
//@ApiModel(value = "核注清单报关单表头")
public class InvtDecHeadType implements java.io.Serializable{
//@ApiModelProperty(value = "添加时间")
    private String addTime;//
//@ApiModelProperty(value = "经营企业名称")
    private String bizopEtpsNm;//经营企业名称
//@ApiModelProperty(value = "经营企业编号")
    private String bizopEtpsno;//经营企业编号
//@ApiModelProperty(value = "经营企业社会信用代码")
    private String bizopEtpsSccd;//经营企业社会信用代码
//@ApiModelProperty(value = "是否已生成报关单")
    private String createFlag;//是否已生成报关单
//@ApiModelProperty(value = "申报企业名称")
    private String dclEtpsNm;//申报企业名称
//@ApiModelProperty(value = "申报企业编号")
    private String dclEtpsno;//申报企业编号
//@ApiModelProperty(value = "申报企业社会信用代码")
    private String dclEtpsSccd;//申报企业社会信用代码
//@ApiModelProperty(value = "申报地关区代码")
    private String dclPlcCuscd;//申报地关区代码
//@ApiModelProperty(value = "报关单统一编号")
    private String decSeqNo;//报关单统一编号
//@ApiModelProperty(value = "报关单类型")
    private String decType;//报关单类型
//@ApiModelProperty(value = "进出口标记代码（I-进口 E-出口）")
    private String impexpMarkcd;//进出口标记代码（I-进口 E-出口）
//@ApiModelProperty(value = "进出境关别")
    private String impexpPortcd;//进出境关别
//@ApiModelProperty(value = "录入单位代码")
    private String inputCode;//录入单位代码
//@ApiModelProperty(value = "录入单位社会信用代码")
    private String inputCreditCode;//录入单位社会信用代码
//@ApiModelProperty(value = "录入单位名称")
    private String inputName;//录入单位名称
//@ApiModelProperty(value = "备案编号")
    private String putrecNo;//备案编号
//@ApiModelProperty(value = "收货企业名称")
    private String rcvgdEtpsNm;//收货企业名称
//@ApiModelProperty(value = "收货企业编号")
    private String rcvgdEtpsno;//收货企业编号
//@ApiModelProperty(value = "备注")
    private String rmk;//备注
//@ApiModelProperty(value = "收发货企业社会信用代码")
    private String rvsngdEtpsSccd;//收发货企业社会信用代码
//@ApiModelProperty(value = "中心统一编号")
    private String seqNo;//中心统一编号
//@ApiModelProperty(value = "监管方式代码")
    private String supvModecd;//监管方式代码
//@ApiModelProperty(value = "运输方式代码")
    private String trspModecd;//运输方式代码

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

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

    public String getCreateFlag() {
        return createFlag;
    }

    public void setCreateFlag(String createFlag) {
        this.createFlag = createFlag;
    }

    public String getDclEtpsNm() {
        return dclEtpsNm;
    }

    public void setDclEtpsNm(String dclEtpsNm) {
        this.dclEtpsNm = dclEtpsNm;
    }

    public String getDclEtpsno() {
        return dclEtpsno;
    }

    public void setDclEtpsno(String dclEtpsno) {
        this.dclEtpsno = dclEtpsno;
    }

    public String getDclEtpsSccd() {
        return dclEtpsSccd;
    }

    public void setDclEtpsSccd(String dclEtpsSccd) {
        this.dclEtpsSccd = dclEtpsSccd;
    }

    public String getDclPlcCuscd() {
        return dclPlcCuscd;
    }

    public void setDclPlcCuscd(String dclPlcCuscd) {
        this.dclPlcCuscd = dclPlcCuscd;
    }

    public String getDecSeqNo() {
        return decSeqNo;
    }

    public void setDecSeqNo(String decSeqNo) {
        this.decSeqNo = decSeqNo;
    }

    public String getDecType() {
        return decType;
    }

    public void setDecType(String decType) {
        this.decType = decType;
    }

    public String getImpexpMarkcd() {
        return impexpMarkcd;
    }

    public void setImpexpMarkcd(String impexpMarkcd) {
        this.impexpMarkcd = impexpMarkcd;
    }

    public String getImpexpPortcd() {
        return impexpPortcd;
    }

    public void setImpexpPortcd(String impexpPortcd) {
        this.impexpPortcd = impexpPortcd;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    public String getInputCreditCode() {
        return inputCreditCode;
    }

    public void setInputCreditCode(String inputCreditCode) {
        this.inputCreditCode = inputCreditCode;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getPutrecNo() {
        return putrecNo;
    }

    public void setPutrecNo(String putrecNo) {
        this.putrecNo = putrecNo;
    }

    public String getRcvgdEtpsNm() {
        return rcvgdEtpsNm;
    }

    public void setRcvgdEtpsNm(String rcvgdEtpsNm) {
        this.rcvgdEtpsNm = rcvgdEtpsNm;
    }

    public String getRcvgdEtpsno() {
        return rcvgdEtpsno;
    }

    public void setRcvgdEtpsno(String rcvgdEtpsno) {
        this.rcvgdEtpsno = rcvgdEtpsno;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getRvsngdEtpsSccd() {
        return rvsngdEtpsSccd;
    }

    public void setRvsngdEtpsSccd(String rvsngdEtpsSccd) {
        this.rvsngdEtpsSccd = rvsngdEtpsSccd;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getSupvModecd() {
        return supvModecd;
    }

    public void setSupvModecd(String supvModecd) {
        this.supvModecd = supvModecd;
    }

    public String getTrspModecd() {
        return trspModecd;
    }

    public void setTrspModecd(String trspModecd) {
        this.trspModecd = trspModecd;
    }
}