package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @Author sunzhijie
 *
 */
//@Data
//@ApiModel(value = "ResultList")
public class InvtQueryListResponseResultList implements java.io.Serializable{
    //    @ApiModelProperty(value = "经营企业名称")
    private String bizopEtpsNM;
    //    @ApiModelProperty(value = "保税清单编号")
    private String bondInvtNo;
    //    @ApiModelProperty(value = "清单类型")
    private String bondInvtType;
    //    @ApiModelProperty("是否报关标志（1-报关 2-非报关）")
    private String dclcusFlag;
    //    @ApiModelProperty("报关类型代码")
    private String dclcusTypeCD;
    //    @ApiModelProperty("进出境关别")
    private String impexpPortCD;
    //    @ApiModelProperty("清单申报时间（格式：yyyyMMdd）")
    private String invtDclTime;
    //    @ApiModelProperty(value = "清单状态")
    private String listStat;
    //    @ApiModelProperty("备案编号,账册号")
    private String putrecNo;
    //    @ApiModelProperty("收货企业名称")
    private String rcvgdEtpsNM;
    //    @ApiModelProperty(value = "预录入统一编号")
    private String seqNo;
    //    @ApiModelProperty("监管方式代码")
    private String supvModeCD;
    //    @ApiModelProperty(value = "核扣标记代码（0-未核扣 1-预核扣 2-已核扣 3-已核销）")
    private String vrfdedMarkcd;
    //    @ApiModelProperty(value = "报关单号")
    private String entryNo;
    //    @ApiModelProperty(value = "备案编号")
    private String putrecSeqno;
    //    @ApiModelProperty(value = "申报类型")
    private String dclTypecd;
    //    @ApiModelProperty(value = "清单审批状态")
    private String invtStucd;
    //    @ApiModelProperty(value = "单一窗口编号")
    private String singleNo;
    //    @ApiModelProperty(value = "进出口标记代码（I-进口 E-出口）")
    private String impexpMarkcd;
    //    @ApiModelProperty(value = "料件成品标记代码（I-料件 E-成品）")
    private String mtpckEndprdMarkcd;
    //    @ApiModelProperty(value = "运输方式代码")
    private String trspModecd;//运输方式代码
    //    @ApiModelProperty(value = "商品序号")
    private String gdsSeqno;
    //    @ApiModelProperty(value = "申报数量")
    private String dclQty;
    //    @ApiModelProperty(value = "申报单位")
    private String dclUnitcd;
    //    @ApiModelProperty(value = "商品料号")
    private String gdsMtno;
    //    @ApiModelProperty(value = "经营企业编号")
    private String bizopEtpsno;
    //    @ApiModelProperty(value = "申报企业名称")
    private String dclEtpsNm;
    //    @ApiModelProperty(value = "企业内部编号")
    private String etpsInnerInvtNo;

    public String getBizopEtpsNM() {
        return bizopEtpsNM;
    }

    public void setBizopEtpsNM(String bizopEtpsNM) {
        this.bizopEtpsNM = bizopEtpsNM;
    }

    public String getBondInvtNo() {
        return bondInvtNo;
    }

    public void setBondInvtNo(String bondInvtNo) {
        this.bondInvtNo = bondInvtNo;
    }

    public String getBondInvtType() {
        return bondInvtType;
    }

    public void setBondInvtType(String bondInvtType) {
        this.bondInvtType = bondInvtType;
    }

    public String getDclcusFlag() {
        return dclcusFlag;
    }

    public void setDclcusFlag(String dclcusFlag) {
        this.dclcusFlag = dclcusFlag;
    }

    public String getDclcusTypeCD() {
        return dclcusTypeCD;
    }

    public void setDclcusTypeCD(String dclcusTypeCD) {
        this.dclcusTypeCD = dclcusTypeCD;
    }

    public String getImpexpPortCD() {
        return impexpPortCD;
    }

    public void setImpexpPortCD(String impexpPortCD) {
        this.impexpPortCD = impexpPortCD;
    }

    public String getInvtDclTime() {
        return invtDclTime;
    }

    public void setInvtDclTime(String invtDclTime) {
        this.invtDclTime = invtDclTime;
    }

    public String getListStat() {
        return listStat;
    }

    public void setListStat(String listStat) {
        this.listStat = listStat;
    }

    public String getPutrecNo() {
        return putrecNo;
    }

    public void setPutrecNo(String putrecNo) {
        this.putrecNo = putrecNo;
    }

    public String getRcvgdEtpsNM() {
        return rcvgdEtpsNM;
    }

    public void setRcvgdEtpsNM(String rcvgdEtpsNM) {
        this.rcvgdEtpsNM = rcvgdEtpsNM;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getSupvModeCD() {
        return supvModeCD;
    }

    public void setSupvModeCD(String supvModeCD) {
        this.supvModeCD = supvModeCD;
    }

    public String getVrfdedMarkcd() {
        return vrfdedMarkcd;
    }

    public void setVrfdedMarkcd(String vrfdedMarkcd) {
        this.vrfdedMarkcd = vrfdedMarkcd;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public String getPutrecSeqno() {
        return putrecSeqno;
    }

    public void setPutrecSeqno(String putrecSeqno) {
        this.putrecSeqno = putrecSeqno;
    }

    public String getDclTypecd() {
        return dclTypecd;
    }

    public void setDclTypecd(String dclTypecd) {
        this.dclTypecd = dclTypecd;
    }

    public String getInvtStucd() {
        return invtStucd;
    }

    public void setInvtStucd(String invtStucd) {
        this.invtStucd = invtStucd;
    }

    public String getSingleNo() {
        return singleNo;
    }

    public void setSingleNo(String singleNo) {
        this.singleNo = singleNo;
    }

    public String getImpexpMarkcd() {
        return impexpMarkcd;
    }

    public void setImpexpMarkcd(String impexpMarkcd) {
        this.impexpMarkcd = impexpMarkcd;
    }

    public String getMtpckEndprdMarkcd() {
        return mtpckEndprdMarkcd;
    }

    public void setMtpckEndprdMarkcd(String mtpckEndprdMarkcd) {
        this.mtpckEndprdMarkcd = mtpckEndprdMarkcd;
    }

    public String getTrspModecd() {
        return trspModecd;
    }

    public void setTrspModecd(String trspModecd) {
        this.trspModecd = trspModecd;
    }

    public String getGdsSeqno() {
        return gdsSeqno;
    }

    public void setGdsSeqno(String gdsSeqno) {
        this.gdsSeqno = gdsSeqno;
    }

    public String getDclQty() {
        return dclQty;
    }

    public void setDclQty(String dclQty) {
        this.dclQty = dclQty;
    }

    public String getDclUnitcd() {
        return dclUnitcd;
    }

    public void setDclUnitcd(String dclUnitcd) {
        this.dclUnitcd = dclUnitcd;
    }

    public String getGdsMtno() {
        return gdsMtno;
    }

    public void setGdsMtno(String gdsMtno) {
        this.gdsMtno = gdsMtno;
    }

    public String getBizopEtpsno() {
        return bizopEtpsno;
    }

    public void setBizopEtpsno(String bizopEtpsno) {
        this.bizopEtpsno = bizopEtpsno;
    }

    public String getDclEtpsNm() {
        return dclEtpsNm;
    }

    public void setDclEtpsNm(String dclEtpsNm) {
        this.dclEtpsNm = dclEtpsNm;
    }

    public String getEtpsInnerInvtNo() {
        return etpsInnerInvtNo;
    }

    public void setEtpsInnerInvtNo(String etpsInnerInvtNo) {
        this.etpsInnerInvtNo = etpsInnerInvtNo;
    }
}

