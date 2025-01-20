package com.haiersoft.ccli.report.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "A_ACCOUNTBOOK")
public class AAccountBook implements Serializable {

    private static final long serialVersionUID = 2799111007612445959L;


    @Column(name = "EMSNO")
    private String EmsNo ; // 账册号;
    @Id
    @Column(name = "GNO")
    private String GNo ; // 序号;
    @Column(name = "CODETS")
    private String CodeTs ; // 商品编码;
    @Column(name = "GNAME")
    private String GName ; // 商品名称;
    @Column(name = "GMODEL")
    private String GModel ; // 商品规格型号;
    @Column(name = "UNIT")
    private String Unit ; // 申报计量单位;
    @Column(name = "APPRQTY")
    private String ApprQty ; // 申请数量;
    @Column(name = "CUTQTY")
    private String CutQty ; // 剩余数量;
    @Column(name = "DEDUCTQTY")
    private String DeductQty ; // 过闸数量;
    @Column(name = "CONFIRMQTY")
    private String ConfirmQty ; // 到货数量;
    @Column(name = "SRCBILLID")
    private String SrcBillId ; // 来源单号;
    @Column(name = "SRCGNO")
    private String SrcGNo ; // SrcGNo;
    @Column(name = "QTY1")
    private String Qty1 ; // 法定数量;
    @Column(name = "GDSMTNO")
    private String GdsMtno ; // 商品料号;
    @Column(name = "FINISH")
    private String finish ; // 完结;
    @Column(name = "ISS")
    private String iss ; // 申报标识;
    @Column(name = "APPRID")
    private String appid ; // 申报标识;
    @Column(name = "ISH")
    private String ish ; // 核放标识;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEmsNo() {
        return EmsNo;
    }

    public void setEmsNo(String emsNo) {
        EmsNo = emsNo;
    }

    public String getGNo() {
        return GNo;
    }

    public void setGNo(String GNo) {
        this.GNo = GNo;
    }

    public String getCodeTs() {
        return CodeTs;
    }

    public void setCodeTs(String codeTs) {
        CodeTs = codeTs;
    }

    public String getGName() {
        return GName;
    }

    public void setGName(String GName) {
        this.GName = GName;
    }

    public String getGModel() {
        return GModel;
    }

    public void setGModel(String GModel) {
        this.GModel = GModel;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getApprQty() {
        return ApprQty;
    }

    public void setApprQty(String apprQty) {
        ApprQty = apprQty;
    }

    public String getCutQty() {
        return CutQty;
    }

    public void setCutQty(String cutQty) {
        CutQty = cutQty;
    }

    public String getDeductQty() {
        return DeductQty;
    }

    public void setDeductQty(String deductQty) {
        DeductQty = deductQty;
    }

    public String getConfirmQty() {
        return ConfirmQty;
    }

    public void setConfirmQty(String confirmQty) {
        ConfirmQty = confirmQty;
    }

    public String getSrcBillId() {
        return SrcBillId;
    }

    public void setSrcBillId(String srcBillId) {
        SrcBillId = srcBillId;
    }

    public String getSrcGNo() {
        return SrcGNo;
    }

    public void setSrcGNo(String srcGNo) {
        SrcGNo = srcGNo;
    }

    public String getQty1() {
        return Qty1;
    }

    public void setQty1(String qty1) {
        Qty1 = qty1;
    }

    public String getGdsMtno() {
        return GdsMtno;
    }

    public void setGdsMtno(String gdsMtno) {
        GdsMtno = gdsMtno;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getIsh() {
        return ish;
    }

    public void setIsh(String ish) {
        this.ish = ish;
    }
}
