package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @Author chenp
 * @Description 核注清单表体
 * @Date 10:53 2020/12/5
 **/
//@Data
//@ApiModel(value = "核注清单表体")
public class InvtListType implements java.io.Serializable{
    //@ApiModelProperty(value = "中心统一编号")
    private String seqNo;
    //@ApiModelProperty(value = "商品序号")
    private String gdsSeqno;
    //@ApiModelProperty(value = "备案序号")
    private String putrecSeqno;
    //@ApiModelProperty(value = "商品料号")
    private String gdsMtno;
    //@ApiModelProperty(value = "商品编码")
    private String gdecd;
    //@ApiModelProperty(value = "商品名称")
    private String gdsNm;
    //@ApiModelProperty(value = "商品规格型号描述")
    private String gdsSpcfModelDesc;
    //@ApiModelProperty(value = "申报计量单位代码")
    private String dclUnitcd;
    //@ApiModelProperty(value = "法定计量单位代码")
    private String lawfUnitcd;
    //@ApiModelProperty(value = "第二法定计量单位代码")
    private String secdLawfUnitcd;
    //@ApiModelProperty(value = "原产国")
    private String natcd;
    //@ApiModelProperty(value = "申报单价金额")
    private String dclUprcAmt;
    //@ApiModelProperty(value = "申报总金额")
    private String dclTotalAmt;
    //@ApiModelProperty(value = "美元统计总金额")
    private String usdStatTotalAmt;
    //@ApiModelProperty(value = "申报币制代码")
    private String dclCurrcd;
    //@ApiModelProperty(value = "法定数量")
    private String lawfQty;
    //@ApiModelProperty(value = "第二法定数量")
    private String secdLawfQty;
    //@ApiModelProperty(value = "重量比例因子值")
    private String wtSfVal;
    //@ApiModelProperty(value = "第一比例因子值")
    private String fstSfVal;
    //@ApiModelProperty(value = "第二比例因子值")
    private String secdSfVal;
    //@ApiModelProperty(value = "申报数量")
    private String dclQty;
    //@ApiModelProperty(value = "毛重量")
    private String grossWt;
    //@ApiModelProperty(value = "净重量")
    private String netWt;
    //@ApiModelProperty(value = "用途代码")
    private String useCd;
    //@ApiModelProperty(value = "征减免方式代码")
    private String lvyrlfModecd;
    //@ApiModelProperty(value = "单耗版本号")
    private String ucnsVerno;
    //@ApiModelProperty(value = "报关单商品序号")
    private String entryGdsSeqno;
    //@ApiModelProperty(value = "申请表序号")
    private String applyTbSeqno;
    //@ApiModelProperty(value = "归类标记代码")
    private String clyMarkcd;
    //@ApiModelProperty(value = "备注")
    private String rmk;
    //@ApiModelProperty(value = "最终目的国（地区）")
    private String destinationNatcd;
    //@ApiModelProperty(value = "修改标志(0-未修改 1-修改 2-删除 3-增加)")
    private String modfMarkcd;
    //@ApiModelProperty("actl_pass_qty")
    private String actlPassQty;

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getGdsSeqno() {
        return gdsSeqno;
    }

    public void setGdsSeqno(String gdsSeqno) {
        this.gdsSeqno = gdsSeqno;
    }

    public String getPutrecSeqno() {
        return putrecSeqno;
    }

    public void setPutrecSeqno(String putrecSeqno) {
        this.putrecSeqno = putrecSeqno;
    }

    public String getGdsMtno() {
        return gdsMtno;
    }

    public void setGdsMtno(String gdsMtno) {
        this.gdsMtno = gdsMtno;
    }

    public String getGdecd() {
        return gdecd;
    }

    public void setGdecd(String gdecd) {
        this.gdecd = gdecd;
    }

    public String getGdsNm() {
        return gdsNm;
    }

    public void setGdsNm(String gdsNm) {
        this.gdsNm = gdsNm;
    }

    public String getGdsSpcfModelDesc() {
        return gdsSpcfModelDesc;
    }

    public void setGdsSpcfModelDesc(String gdsSpcfModelDesc) {
        this.gdsSpcfModelDesc = gdsSpcfModelDesc;
    }

    public String getDclUnitcd() {
        return dclUnitcd;
    }

    public void setDclUnitcd(String dclUnitcd) {
        this.dclUnitcd = dclUnitcd;
    }

    public String getLawfUnitcd() {
        return lawfUnitcd;
    }

    public void setLawfUnitcd(String lawfUnitcd) {
        this.lawfUnitcd = lawfUnitcd;
    }

    public String getSecdLawfUnitcd() {
        return secdLawfUnitcd;
    }

    public void setSecdLawfUnitcd(String secdLawfUnitcd) {
        this.secdLawfUnitcd = secdLawfUnitcd;
    }

    public String getNatcd() {
        return natcd;
    }

    public void setNatcd(String natcd) {
        this.natcd = natcd;
    }

    public String getDclUprcAmt() {
        return dclUprcAmt;
    }

    public void setDclUprcAmt(String dclUprcAmt) {
        this.dclUprcAmt = dclUprcAmt;
    }

    public String getDclTotalAmt() {
        return dclTotalAmt;
    }

    public void setDclTotalAmt(String dclTotalAmt) {
        this.dclTotalAmt = dclTotalAmt;
    }

    public String getUsdStatTotalAmt() {
        return usdStatTotalAmt;
    }

    public void setUsdStatTotalAmt(String usdStatTotalAmt) {
        this.usdStatTotalAmt = usdStatTotalAmt;
    }

    public String getDclCurrcd() {
        return dclCurrcd;
    }

    public void setDclCurrcd(String dclCurrcd) {
        this.dclCurrcd = dclCurrcd;
    }

    public String getLawfQty() {
        return lawfQty;
    }

    public void setLawfQty(String lawfQty) {
        this.lawfQty = lawfQty;
    }

    public String getSecdLawfQty() {
        return secdLawfQty;
    }

    public void setSecdLawfQty(String secdLawfQty) {
        this.secdLawfQty = secdLawfQty;
    }

    public String getWtSfVal() {
        return wtSfVal;
    }

    public void setWtSfVal(String wtSfVal) {
        this.wtSfVal = wtSfVal;
    }

    public String getFstSfVal() {
        return fstSfVal;
    }

    public void setFstSfVal(String fstSfVal) {
        this.fstSfVal = fstSfVal;
    }

    public String getSecdSfVal() {
        return secdSfVal;
    }

    public void setSecdSfVal(String secdSfVal) {
        this.secdSfVal = secdSfVal;
    }

    public String getDclQty() {
        return dclQty;
    }

    public void setDclQty(String dclQty) {
        this.dclQty = dclQty;
    }

    public String getGrossWt() {
        return grossWt;
    }

    public void setGrossWt(String grossWt) {
        this.grossWt = grossWt;
    }

    public String getNetWt() {
        return netWt;
    }

    public void setNetWt(String netWt) {
        this.netWt = netWt;
    }

    public String getUseCd() {
        return useCd;
    }

    public void setUseCd(String useCd) {
        this.useCd = useCd;
    }

    public String getLvyrlfModecd() {
        return lvyrlfModecd;
    }

    public void setLvyrlfModecd(String lvyrlfModecd) {
        this.lvyrlfModecd = lvyrlfModecd;
    }

    public String getUcnsVerno() {
        return ucnsVerno;
    }

    public void setUcnsVerno(String ucnsVerno) {
        this.ucnsVerno = ucnsVerno;
    }

    public String getEntryGdsSeqno() {
        return entryGdsSeqno;
    }

    public void setEntryGdsSeqno(String entryGdsSeqno) {
        this.entryGdsSeqno = entryGdsSeqno;
    }

    public String getApplyTbSeqno() {
        return applyTbSeqno;
    }

    public void setApplyTbSeqno(String applyTbSeqno) {
        this.applyTbSeqno = applyTbSeqno;
    }

    public String getClyMarkcd() {
        return clyMarkcd;
    }

    public void setClyMarkcd(String clyMarkcd) {
        this.clyMarkcd = clyMarkcd;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getDestinationNatcd() {
        return destinationNatcd;
    }

    public void setDestinationNatcd(String destinationNatcd) {
        this.destinationNatcd = destinationNatcd;
    }

    public String getModfMarkcd() {
        return modfMarkcd;
    }

    public void setModfMarkcd(String modfMarkcd) {
        this.modfMarkcd = modfMarkcd;
    }

    public String getActlPassQty() {
        return actlPassQty;
    }

    public void setActlPassQty(String actlPassQty) {
        this.actlPassQty = actlPassQty;
    }
}
