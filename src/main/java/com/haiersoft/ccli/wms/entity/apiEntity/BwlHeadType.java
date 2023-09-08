package com.haiersoft.ccli.wms.entity.apiEntity;

//@Data
//@ApiModel(value = "BwlHead")
public class BwlHeadType implements java.io.Serializable{
    //@ApiModelProperty(value = "添加时间")
    private String addTime;

    //@ApiModelProperty(value = "记账模式(1：可累计，2：不累计)")
    private String appendTypeCd;

    //@ApiModelProperty(value = "经营企业名称")
    private String bizopEtpsNm;

    //@ApiModelProperty(value = "经营企业编号")
    private String bizopEtpsno;

    //@ApiModelProperty(value = "经营企业社会信用代码")
    private String bizopEtpsSccd;

    //@ApiModelProperty(value = "仓库账册号")
    private String bwlNo;

    //@ApiModelProperty(value = "区域场所类别(A:保税物流中心A、B:保税物流中心B、D:公共保税仓库、E:液体保税仓库、F:寄售维修保税仓库、G:暂为空，H:特殊商品保税仓库、I:备料保税仓库、P:出口配送监管仓、J:为国内结转监管仓、K:保税区、L:出口加工区、M:保税物流园区、N:保税港区、Z:综合保税区,Q:跨境工业园区、S:特殊区域设备账册) ")
    private String bwlTypeCd;

    //@ApiModelProperty(value = "变更批准时间")
    private String chgApprTime;

    //@ApiModelProperty(value = "变更或报核次数")
    private String chgTmsCnt;

    //@ApiModelProperty(value = "联系人")
    private String contactEr;

    //@ApiModelProperty(value = "联系电话")
    private String contactTele;

    //@ApiModelProperty(value = "申报企业名称")
    private String dclEtpsNm;

    //@ApiModelProperty(value = "申报企业编号")
    private String dclEtpsno;

    //@ApiModelProperty(value = "申报企业社会信用代码")
    private String dclEtpsSccd;

    //@ApiModelProperty(value = "申报单位类型代码")
    private String dclEtpsTypeCd;

    //@ApiModelProperty(value = "申报标记代码(1-电子口岸申报 2-地方平台申报 3- 其它申报)")
    private String dclMarkCd;

    //@ApiModelProperty(value = "申报时间")
    private String dclTime;

    //@ApiModelProperty(value = "申报类型")
    private String dclTypeCd;

    //@ApiModelProperty(value = "审批状态代码")
    private String emapvStucd;

    //@ApiModelProperty(value = "企业内部编号")
    private String etpsPreentNo;

    //@ApiModelProperty(value = "结束有效日期(与仓库证书有效期一致；人工调整(考虑设置提前一个月）YYYYMMDD)")
    private String finishValidTime;

    //@ApiModelProperty(value = "仓库地址")
    private String houseAddress;

    //@ApiModelProperty(value = "仓库面积")
    private String houseArea;

    //@ApiModelProperty(value = "仓库名称")
    private String houseNm;

    //@ApiModelProperty(value = "仓库编号")
    private String houseNo;

    //@ApiModelProperty(value = "企业类型")
    private String houseTypeCd;

    //@ApiModelProperty(value = "仓库容积")
    private String houseVolume;

    //@ApiModelProperty(value = "录入单位代码")
    private String inputCode;

    //@ApiModelProperty(value = "录入日期（格式：yyyyMMdd）")
    private String inputDate;

    //@ApiModelProperty(value = "录入单位名称")
    private String inputName;

    //@ApiModelProperty(value = "录入单位社会信用代码")
    private String inputSccd;

    //@ApiModelProperty(value = "主管海关")
    private String masterCuscd;

    //@ApiModelProperty(value = "暂停变更标记代码")
    private String pauseChgMarkCd;

    //@ApiModelProperty(value = "备案批准时间")
    private String putrecApprTime;

    //@ApiModelProperty(value = "备注")
    private String rmk;

    //@ApiModelProperty(value = "统一编号")
    private String seqNo;

    //@ApiModelProperty(value = "退税标志(入区、入场所退税标志。Y-是、N-否 )")
    private String taxTypeCd;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAppendTypeCd() {
        return appendTypeCd;
    }

    public void setAppendTypeCd(String appendTypeCd) {
        this.appendTypeCd = appendTypeCd;
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

    public String getBwlNo() {
        return bwlNo;
    }

    public void setBwlNo(String bwlNo) {
        this.bwlNo = bwlNo;
    }

    public String getBwlTypeCd() {
        return bwlTypeCd;
    }

    public void setBwlTypeCd(String bwlTypeCd) {
        this.bwlTypeCd = bwlTypeCd;
    }

    public String getChgApprTime() {
        return chgApprTime;
    }

    public void setChgApprTime(String chgApprTime) {
        this.chgApprTime = chgApprTime;
    }

    public String getChgTmsCnt() {
        return chgTmsCnt;
    }

    public void setChgTmsCnt(String chgTmsCnt) {
        this.chgTmsCnt = chgTmsCnt;
    }

    public String getContactEr() {
        return contactEr;
    }

    public void setContactEr(String contactEr) {
        this.contactEr = contactEr;
    }

    public String getContactTele() {
        return contactTele;
    }

    public void setContactTele(String contactTele) {
        this.contactTele = contactTele;
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

    public String getDclEtpsTypeCd() {
        return dclEtpsTypeCd;
    }

    public void setDclEtpsTypeCd(String dclEtpsTypeCd) {
        this.dclEtpsTypeCd = dclEtpsTypeCd;
    }

    public String getDclMarkCd() {
        return dclMarkCd;
    }

    public void setDclMarkCd(String dclMarkCd) {
        this.dclMarkCd = dclMarkCd;
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

    public String getEmapvStucd() {
        return emapvStucd;
    }

    public void setEmapvStucd(String emapvStucd) {
        this.emapvStucd = emapvStucd;
    }

    public String getEtpsPreentNo() {
        return etpsPreentNo;
    }

    public void setEtpsPreentNo(String etpsPreentNo) {
        this.etpsPreentNo = etpsPreentNo;
    }

    public String getFinishValidTime() {
        return finishValidTime;
    }

    public void setFinishValidTime(String finishValidTime) {
        this.finishValidTime = finishValidTime;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(String houseArea) {
        this.houseArea = houseArea;
    }

    public String getHouseNm() {
        return houseNm;
    }

    public void setHouseNm(String houseNm) {
        this.houseNm = houseNm;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getHouseTypeCd() {
        return houseTypeCd;
    }

    public void setHouseTypeCd(String houseTypeCd) {
        this.houseTypeCd = houseTypeCd;
    }

    public String getHouseVolume() {
        return houseVolume;
    }

    public void setHouseVolume(String houseVolume) {
        this.houseVolume = houseVolume;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    public String getInputDate() {
        return inputDate;
    }

    public void setInputDate(String inputDate) {
        this.inputDate = inputDate;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getInputSccd() {
        return inputSccd;
    }

    public void setInputSccd(String inputSccd) {
        this.inputSccd = inputSccd;
    }

    public String getMasterCuscd() {
        return masterCuscd;
    }

    public void setMasterCuscd(String masterCuscd) {
        this.masterCuscd = masterCuscd;
    }

    public String getPauseChgMarkCd() {
        return pauseChgMarkCd;
    }

    public void setPauseChgMarkCd(String pauseChgMarkCd) {
        this.pauseChgMarkCd = pauseChgMarkCd;
    }

    public String getPutrecApprTime() {
        return putrecApprTime;
    }

    public void setPutrecApprTime(String putrecApprTime) {
        this.putrecApprTime = putrecApprTime;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getTaxTypeCd() {
        return taxTypeCd;
    }

    public void setTaxTypeCd(String taxTypeCd) {
        this.taxTypeCd = taxTypeCd;
    }
}
