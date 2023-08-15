package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @ClassName: InvtGoodsType
 * @Description: 简单加工、一纳成品内销成品明细[核注清单商品表体(保存集报清单料件信息)]
 * @Author chenp
 * @Date 2020/12/2 16:04
 *
 */
//@ApiModel(value = "简单加工、一纳成品内销成品明细[核注清单商品表体(保存集报清单料件信息)]")
//@Data
public class InvtGoodsType implements java.io.Serializable{
    //@ApiModelProperty(value = "中心统一编号")
    private String SeqNo;
    //@ApiModelProperty(value = "商品序号")
    private String GdsSeqno;
    //@ApiModelProperty(value = "备案序号")
    private String PutrecSeqno;
    //@ApiModelProperty(value = "商品料号")
    private String GdsMtno;
    //@ApiModelProperty(value = "商品编码")
    private String Gdecd;
    //@ApiModelProperty(value = "商品名称")
    private String GdsNm;
    //@ApiModelProperty(value = "商品规格型号描述")
    private String GdsSpcfModelDesc;
    //@ApiModelProperty(value = "申报计量单位代码")
    private String DclUnitcd;
    //@ApiModelProperty(value = "法定计量单位代码")
    private String LawfUnitcd;
    //@ApiModelProperty(value = "第二法定计量单位代码")
    private String SecdLawfUnitcd;
    //@ApiModelProperty(value = "原产国")
    private String Natcd;
    //@ApiModelProperty(value = "申报单价金额")
    private String DclUprcAmt;
    //@ApiModelProperty(value = "申报总金额")
    private String DclTotalAmt;
    //@ApiModelProperty(value = "美元统计总金额")
    private String UsdStatTotalAmt;
    //@ApiModelProperty(value = "申报币制代码")
    private String DclCurrcd;
    //@ApiModelProperty(value = "法定数量")
    private String LawfQty;
    //@ApiModelProperty(value = "第二法定数量")
    private String SecdLawfQty;
    //@ApiModelProperty(value = "重量比例因子值")
    private String WtSfVal;
    //@ApiModelProperty(value = "第一比例因子值")
    private String FstSfVal;
    //@ApiModelProperty(value = "第二比例因子值")
    private String SecdSfVal;
    //@ApiModelProperty(value = "申报数量")
    private String DclQty;
    //@ApiModelProperty(value = "毛重量")
    private String GrossWt;
    //@ApiModelProperty(value = "净重量")
    private String NetWt;
    //@ApiModelProperty(value = "用途代码")
    private String UseCd;
    //@ApiModelProperty(value = "征减免方式代码")
    private String LvyrlfModecd;
    //@ApiModelProperty(value = "单耗版本号")
    private String UcnsVerno;
    //@ApiModelProperty(value = "报关单商品序号")
    private String EntryGdsSeqno;
    //@ApiModelProperty(value = "申请表序号")
    private String ApplyTbSeqno;
    //@ApiModelProperty(value = "归类标记代码")
    private String ClyMarkcd;
    //@ApiModelProperty(value = "备注")
    private String Rmk;
    //@ApiModelProperty(value = "最终目的国（地区）")
    private String DestinationNatcd;
    //@ApiModelProperty(value = "修改标志(0-未修改 1-修改 2-删除 3-增加)")
    private String ModfMarkcd;

    public String getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(String seqNo) {
        SeqNo = seqNo;
    }

    public String getGdsSeqno() {
        return GdsSeqno;
    }

    public void setGdsSeqno(String gdsSeqno) {
        GdsSeqno = gdsSeqno;
    }

    public String getPutrecSeqno() {
        return PutrecSeqno;
    }

    public void setPutrecSeqno(String putrecSeqno) {
        PutrecSeqno = putrecSeqno;
    }

    public String getGdsMtno() {
        return GdsMtno;
    }

    public void setGdsMtno(String gdsMtno) {
        GdsMtno = gdsMtno;
    }

    public String getGdecd() {
        return Gdecd;
    }

    public void setGdecd(String gdecd) {
        Gdecd = gdecd;
    }

    public String getGdsNm() {
        return GdsNm;
    }

    public void setGdsNm(String gdsNm) {
        GdsNm = gdsNm;
    }

    public String getGdsSpcfModelDesc() {
        return GdsSpcfModelDesc;
    }

    public void setGdsSpcfModelDesc(String gdsSpcfModelDesc) {
        GdsSpcfModelDesc = gdsSpcfModelDesc;
    }

    public String getDclUnitcd() {
        return DclUnitcd;
    }

    public void setDclUnitcd(String dclUnitcd) {
        DclUnitcd = dclUnitcd;
    }

    public String getLawfUnitcd() {
        return LawfUnitcd;
    }

    public void setLawfUnitcd(String lawfUnitcd) {
        LawfUnitcd = lawfUnitcd;
    }

    public String getSecdLawfUnitcd() {
        return SecdLawfUnitcd;
    }

    public void setSecdLawfUnitcd(String secdLawfUnitcd) {
        SecdLawfUnitcd = secdLawfUnitcd;
    }

    public String getNatcd() {
        return Natcd;
    }

    public void setNatcd(String natcd) {
        Natcd = natcd;
    }

    public String getDclUprcAmt() {
        return DclUprcAmt;
    }

    public void setDclUprcAmt(String dclUprcAmt) {
        DclUprcAmt = dclUprcAmt;
    }

    public String getDclTotalAmt() {
        return DclTotalAmt;
    }

    public void setDclTotalAmt(String dclTotalAmt) {
        DclTotalAmt = dclTotalAmt;
    }

    public String getUsdStatTotalAmt() {
        return UsdStatTotalAmt;
    }

    public void setUsdStatTotalAmt(String usdStatTotalAmt) {
        UsdStatTotalAmt = usdStatTotalAmt;
    }

    public String getDclCurrcd() {
        return DclCurrcd;
    }

    public void setDclCurrcd(String dclCurrcd) {
        DclCurrcd = dclCurrcd;
    }

    public String getLawfQty() {
        return LawfQty;
    }

    public void setLawfQty(String lawfQty) {
        LawfQty = lawfQty;
    }

    public String getSecdLawfQty() {
        return SecdLawfQty;
    }

    public void setSecdLawfQty(String secdLawfQty) {
        SecdLawfQty = secdLawfQty;
    }

    public String getWtSfVal() {
        return WtSfVal;
    }

    public void setWtSfVal(String wtSfVal) {
        WtSfVal = wtSfVal;
    }

    public String getFstSfVal() {
        return FstSfVal;
    }

    public void setFstSfVal(String fstSfVal) {
        FstSfVal = fstSfVal;
    }

    public String getSecdSfVal() {
        return SecdSfVal;
    }

    public void setSecdSfVal(String secdSfVal) {
        SecdSfVal = secdSfVal;
    }

    public String getDclQty() {
        return DclQty;
    }

    public void setDclQty(String dclQty) {
        DclQty = dclQty;
    }

    public String getGrossWt() {
        return GrossWt;
    }

    public void setGrossWt(String grossWt) {
        GrossWt = grossWt;
    }

    public String getNetWt() {
        return NetWt;
    }

    public void setNetWt(String netWt) {
        NetWt = netWt;
    }

    public String getUseCd() {
        return UseCd;
    }

    public void setUseCd(String useCd) {
        UseCd = useCd;
    }

    public String getLvyrlfModecd() {
        return LvyrlfModecd;
    }

    public void setLvyrlfModecd(String lvyrlfModecd) {
        LvyrlfModecd = lvyrlfModecd;
    }

    public String getUcnsVerno() {
        return UcnsVerno;
    }

    public void setUcnsVerno(String ucnsVerno) {
        UcnsVerno = ucnsVerno;
    }

    public String getEntryGdsSeqno() {
        return EntryGdsSeqno;
    }

    public void setEntryGdsSeqno(String entryGdsSeqno) {
        EntryGdsSeqno = entryGdsSeqno;
    }

    public String getApplyTbSeqno() {
        return ApplyTbSeqno;
    }

    public void setApplyTbSeqno(String applyTbSeqno) {
        ApplyTbSeqno = applyTbSeqno;
    }

    public String getClyMarkcd() {
        return ClyMarkcd;
    }

    public void setClyMarkcd(String clyMarkcd) {
        ClyMarkcd = clyMarkcd;
    }

    public String getRmk() {
        return Rmk;
    }

    public void setRmk(String rmk) {
        Rmk = rmk;
    }

    public String getDestinationNatcd() {
        return DestinationNatcd;
    }

    public void setDestinationNatcd(String destinationNatcd) {
        DestinationNatcd = destinationNatcd;
    }

    public String getModfMarkcd() {
        return ModfMarkcd;
    }

    public void setModfMarkcd(String modfMarkcd) {
        ModfMarkcd = modfMarkcd;
    }
}

