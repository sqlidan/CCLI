package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @ClassName: InvtHeadType
 * @Description: 核注清单表头
 * @Author chenp
 * @Date 2020/11/30 13:59
 */
//@ApiModel(value = "核注清单表头")
//@Data
public class InvtHeadType implements java.io.Serializable{
    //@ApiModelProperty(value = "添加时间")
    private String addTime;//
    //@ApiModelProperty(value = "申请编号")
    private String applyNo;//申请编号
    //@ApiModelProperty(value = "经营企业名称")
    private String bizopEtpsNm;//经营企业名称
    //@ApiModelProperty(value = "经营企业编号")
    private String bizopEtpsno;//经营企业编号
    //@ApiModelProperty(value = "经营企业社会信用代码")
    private String bizopEtpsSccd;//经营企业社会信用代码
    //@ApiModelProperty(value = "保税清单编号")
    private String bondInvtNo;//保税清单编号
    //@ApiModelProperty(value = "变更次数")
    private String chgTmsCnt;//变更次数
    //@ApiModelProperty(value = "对应报关单申报单位名称")
    private String corrEntryDclEtpsNm;//对应报关单申报单位名称
    //@ApiModelProperty(value = "对应报关单申报单位社会统一信用代码")
    private String corrEntryDclEtpsSccd;//对应报关单申报单位社会统一信用代码
    //@ApiModelProperty(value = "对应报关单申报单位编码")
    private String corrEntryDclEtpsNo;//对应报关单申报单位社会统一信用代码
    //@ApiModelProperty(value = "是否报关标志（1-报关 2-非报关）")
    private String dclcusFlag;//是否报关标志（1-报关 2-非报关）
    //@ApiModelProperty(value = "报关类型代码")
    private String dclcusTypecd;//报关类型代码
    //@ApiModelProperty(value = "申报企业名称")
    private String dclEtpsNm;//申报企业名称
    //@ApiModelProperty(value = "申报企业编号")
    private String dclEtpsno;//申报企业编号
    //@ApiModelProperty(value = "申报企业社会信用代码")
    private String dclEtpsSccd;//申报企业社会信用代码
    //@ApiModelProperty(value = "申报地关区代码")
    private String dclPlcCuscd;//申报地关区代码
    //@ApiModelProperty(value = "报关单类型")
    private String decType;//报关单类型
    //@ApiModelProperty(value = "报关单申报时间（格式：yyyyMMdd）")
    private String entryDclTime;//报关单申报时间（格式：yyyyMMdd）
    //@ApiModelProperty(value = "对应报关单编号")
    private String entryNo;//对应报关单编号
    //@ApiModelProperty(value = "对应报关单编号")
    private String entryStucd;//对应报关单编号
    //@ApiModelProperty(value = "企业内部清单编号")
    private String eepsInnerInvtNo;//企业内部清单编号
    //@ApiModelProperty(value = "企业内部清单编号")
    private String etpsInnerInvtNo;//企业内部清单编号
    //@ApiModelProperty(value = "正式核扣时间")
    private String formalVrfdedTime;//正式核扣时间
    //@ApiModelProperty(value = "上传人IC卡号")
    private String icCardNo;//上传人IC卡号
    //@ApiModelProperty(value = "进出口标记代码（I-进口 E-出口）")
    private String impexpMarkcd;//进出口标记代码（I-进口 E-出口）
    //@ApiModelProperty(value = "进出口口岸代码")
    private String impexpPortcd;//进出口口岸代码
    //@ApiModelProperty(value = "录入单位代码")
    private String inputCode;//录入单位代码
    //@ApiModelProperty(value = "录入单位社会信用代码")
    private String inputCreditCode;//录入单位社会信用代码
    //@ApiModelProperty(value = "录入单位名称")
    private String inputName;//录入单位名称
    //@ApiModelProperty(value = "录入日期（格式：yyyyMMdd）")
    private String inputTime;//录入日期（格式：yyyyMMdd）
    //@ApiModelProperty(value = "清单申报时间（格式：yyyyMMdd）")
    private String invtDclTime;//清单申报时间（格式：yyyyMMdd）
    //@ApiModelProperty(value = "清单进出卡口状态代码")
    private String invtIochkptStucd;//清单进出卡口状态代码
    //@ApiModelProperty(value = "清单类型")
    private String invtType;//清单类型
    //@ApiModelProperty(value = "清单状态")
    private String listStat;//清单状态
    //@ApiModelProperty(value = "流转类型")
    private String listType;//流转类型
    //@ApiModelProperty(value = "料件成品标记代码（I-料件 E-成品）")
    private String mtpckEndprdMarkcd;//料件成品标记代码（I-料件 E-成品）
    private String Param1;//
    private String Param2;//
    private String Param3;//
    private String Param4;//
    //@ApiModelProperty(value = "核放单生成标志代码")
    private String passportUsedTypeCd;//核放单生成标志代码
    //@ApiModelProperty(value = "预核扣时间")
    private String prevdTime;//预核扣时间
    //@ApiModelProperty(value = "备案编号")
    private String putrecNo;//备案编号
    //@ApiModelProperty(value = "收货企业名称")
    private String rcvgdEtpsNm;//收货企业名称
    //@ApiModelProperty(value = "收货企业编号")
    private String rcvgdEtpsno;//收货企业编号
    //@ApiModelProperty(value = "关联报关单境内收发货人名称")
    private String rltEntryBizopEtpsNm;//关联报关单境内收发货人名称
    //@ApiModelProperty(value = "关联报关单境内收发货人编号")
    private String rltEntryBizopEtpsno;//关联报关单境内收发货人编号
    //@ApiModelProperty(value = "关联报关单境内收发货人社会信用代码")
    private String rltEntryBizopEtpsSccd;//关联报关单境内收发货人社会信用代码
    //@ApiModelProperty(value = "关联报关单申报单位名称")
    private String rltEntryDclEtpsNm;//关联报关单申报单位名称
    //@ApiModelProperty(value = "关联报关单申报单位编号")
    private String rltEntryDclEtpsno;//关联报关单申报单位编号
    //@ApiModelProperty(value = "关联报关单申报单位社会信用代码")
    private String rltEntryDclEtpsSccd;//关联报关单申报单位社会信用代码
    //@ApiModelProperty(value = "关联报关单编号")
    private String rltEntryNo;//关联报关单编号
    //@ApiModelProperty(value = "关联报关单收发货单位名称")
    private String rltEntryRcvgdEtpsNm;//关联报关单收发货单位名称
    //@ApiModelProperty(value = "关联报关单收发货单位编号")
    private String rltEntryRcvgdEtpsno;//关联报关单收发货单位编号
    //@ApiModelProperty(value = "关联报关单收发货单位社会信用代码")
    private String rltEntryRvsngdEtpsSccd;//关联报关单收发货单位社会信用代码
    //@ApiModelProperty(value = "RltInvtNo")
    private String rltInvtNo;//RltInvtNo
    //@ApiModelProperty(value = "关联备案编号")
    private String rltPutrecNo;//关联备案编号
    //@ApiModelProperty(value = "备注")
    private String rmk;//备注
    //@ApiModelProperty(value = "收发货企业社会信用代码")
    private String rvsngdEtpsSccd;//收发货企业社会信用代码
    //@ApiModelProperty(value = "中心统一编号")
    private String seqNo;//中心统一编号
    //@ApiModelProperty(value = "起运/运抵国(地区）")
    private String stshipTrsarvNatcd;//起运/运抵国(地区）
    //@ApiModelProperty(value = "监管方式代码")
    private String supvModecd;//监管方式代码
    //@ApiModelProperty(value = "运输方式代码")
    private String trspModecd;//运输方式代码
    //@ApiModelProperty(value = "核扣标记代码（0-未核扣 1-预核扣 2-已核扣 3-已核销）")
    private String vrfdedMarkcd;//核扣标记代码（0-未核扣 1-预核扣 2-已核扣 3-已核销）
    //@ApiModelProperty(value = "是否生成报关单")
    private String genDecFlag;//运输方式代码
    //@ApiModelProperty(value = "报关单统一编号")
    private String entrySeqNo;//运输方式代码
    //@ApiModelProperty(value = "申报类型")
    private String dclTypecd;//申报类型
    //@ApiModelProperty(value = "报关单同步修改标志")
    private String needEntryModified;//报关单同步修改标志
    //@ApiModelProperty(value = "计征金额")
    private String levyBlAmt;//计征金额

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
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

    public String getBondInvtNo() {
        return bondInvtNo;
    }

    public void setBondInvtNo(String bondInvtNo) {
        this.bondInvtNo = bondInvtNo;
    }

    public String getChgTmsCnt() {
        return chgTmsCnt;
    }

    public void setChgTmsCnt(String chgTmsCnt) {
        this.chgTmsCnt = chgTmsCnt;
    }

    public String getCorrEntryDclEtpsNm() {
        return corrEntryDclEtpsNm;
    }

    public void setCorrEntryDclEtpsNm(String corrEntryDclEtpsNm) {
        this.corrEntryDclEtpsNm = corrEntryDclEtpsNm;
    }

    public String getCorrEntryDclEtpsSccd() {
        return corrEntryDclEtpsSccd;
    }

    public void setCorrEntryDclEtpsSccd(String corrEntryDclEtpsSccd) {
        this.corrEntryDclEtpsSccd = corrEntryDclEtpsSccd;
    }

    public String getCorrEntryDclEtpsNo() {
        return corrEntryDclEtpsNo;
    }

    public void setCorrEntryDclEtpsNo(String corrEntryDclEtpsNo) {
        this.corrEntryDclEtpsNo = corrEntryDclEtpsNo;
    }

    public String getDclcusFlag() {
        return dclcusFlag;
    }

    public void setDclcusFlag(String dclcusFlag) {
        this.dclcusFlag = dclcusFlag;
    }

    public String getDclcusTypecd() {
        return dclcusTypecd;
    }

    public void setDclcusTypecd(String dclcusTypecd) {
        this.dclcusTypecd = dclcusTypecd;
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

    public String getDecType() {
        return decType;
    }

    public void setDecType(String decType) {
        this.decType = decType;
    }

    public String getEntryDclTime() {
        return entryDclTime;
    }

    public void setEntryDclTime(String entryDclTime) {
        this.entryDclTime = entryDclTime;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public String getEntryStucd() {
        return entryStucd;
    }

    public void setEntryStucd(String entryStucd) {
        this.entryStucd = entryStucd;
    }

    public String getEepsInnerInvtNo() {
        return eepsInnerInvtNo;
    }

    public void setEepsInnerInvtNo(String eepsInnerInvtNo) {
        this.eepsInnerInvtNo = eepsInnerInvtNo;
    }

    public String getEtpsInnerInvtNo() {
        return etpsInnerInvtNo;
    }

    public void setEtpsInnerInvtNo(String etpsInnerInvtNo) {
        this.etpsInnerInvtNo = etpsInnerInvtNo;
    }

    public String getFormalVrfdedTime() {
        return formalVrfdedTime;
    }

    public void setFormalVrfdedTime(String formalVrfdedTime) {
        this.formalVrfdedTime = formalVrfdedTime;
    }

    public String getIcCardNo() {
        return icCardNo;
    }

    public void setIcCardNo(String icCardNo) {
        this.icCardNo = icCardNo;
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

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }

    public String getInvtDclTime() {
        return invtDclTime;
    }

    public void setInvtDclTime(String invtDclTime) {
        this.invtDclTime = invtDclTime;
    }

    public String getInvtIochkptStucd() {
        return invtIochkptStucd;
    }

    public void setInvtIochkptStucd(String invtIochkptStucd) {
        this.invtIochkptStucd = invtIochkptStucd;
    }

    public String getInvtType() {
        return invtType;
    }

    public void setInvtType(String invtType) {
        this.invtType = invtType;
    }

    public String getListStat() {
        return listStat;
    }

    public void setListStat(String listStat) {
        this.listStat = listStat;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getMtpckEndprdMarkcd() {
        return mtpckEndprdMarkcd;
    }

    public void setMtpckEndprdMarkcd(String mtpckEndprdMarkcd) {
        this.mtpckEndprdMarkcd = mtpckEndprdMarkcd;
    }

    public String getParam1() {
        return Param1;
    }

    public void setParam1(String param1) {
        Param1 = param1;
    }

    public String getParam2() {
        return Param2;
    }

    public void setParam2(String param2) {
        Param2 = param2;
    }

    public String getParam3() {
        return Param3;
    }

    public void setParam3(String param3) {
        Param3 = param3;
    }

    public String getParam4() {
        return Param4;
    }

    public void setParam4(String param4) {
        Param4 = param4;
    }

    public String getPassportUsedTypeCd() {
        return passportUsedTypeCd;
    }

    public void setPassportUsedTypeCd(String passportUsedTypeCd) {
        this.passportUsedTypeCd = passportUsedTypeCd;
    }

    public String getPrevdTime() {
        return prevdTime;
    }

    public void setPrevdTime(String prevdTime) {
        this.prevdTime = prevdTime;
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

    public String getRltEntryBizopEtpsNm() {
        return rltEntryBizopEtpsNm;
    }

    public void setRltEntryBizopEtpsNm(String rltEntryBizopEtpsNm) {
        this.rltEntryBizopEtpsNm = rltEntryBizopEtpsNm;
    }

    public String getRltEntryBizopEtpsno() {
        return rltEntryBizopEtpsno;
    }

    public void setRltEntryBizopEtpsno(String rltEntryBizopEtpsno) {
        this.rltEntryBizopEtpsno = rltEntryBizopEtpsno;
    }

    public String getRltEntryBizopEtpsSccd() {
        return rltEntryBizopEtpsSccd;
    }

    public void setRltEntryBizopEtpsSccd(String rltEntryBizopEtpsSccd) {
        this.rltEntryBizopEtpsSccd = rltEntryBizopEtpsSccd;
    }

    public String getRltEntryDclEtpsNm() {
        return rltEntryDclEtpsNm;
    }

    public void setRltEntryDclEtpsNm(String rltEntryDclEtpsNm) {
        this.rltEntryDclEtpsNm = rltEntryDclEtpsNm;
    }

    public String getRltEntryDclEtpsno() {
        return rltEntryDclEtpsno;
    }

    public void setRltEntryDclEtpsno(String rltEntryDclEtpsno) {
        this.rltEntryDclEtpsno = rltEntryDclEtpsno;
    }

    public String getRltEntryDclEtpsSccd() {
        return rltEntryDclEtpsSccd;
    }

    public void setRltEntryDclEtpsSccd(String rltEntryDclEtpsSccd) {
        this.rltEntryDclEtpsSccd = rltEntryDclEtpsSccd;
    }

    public String getRltEntryNo() {
        return rltEntryNo;
    }

    public void setRltEntryNo(String rltEntryNo) {
        this.rltEntryNo = rltEntryNo;
    }

    public String getRltEntryRcvgdEtpsNm() {
        return rltEntryRcvgdEtpsNm;
    }

    public void setRltEntryRcvgdEtpsNm(String rltEntryRcvgdEtpsNm) {
        this.rltEntryRcvgdEtpsNm = rltEntryRcvgdEtpsNm;
    }

    public String getRltEntryRcvgdEtpsno() {
        return rltEntryRcvgdEtpsno;
    }

    public void setRltEntryRcvgdEtpsno(String rltEntryRcvgdEtpsno) {
        this.rltEntryRcvgdEtpsno = rltEntryRcvgdEtpsno;
    }

    public String getRltEntryRvsngdEtpsSccd() {
        return rltEntryRvsngdEtpsSccd;
    }

    public void setRltEntryRvsngdEtpsSccd(String rltEntryRvsngdEtpsSccd) {
        this.rltEntryRvsngdEtpsSccd = rltEntryRvsngdEtpsSccd;
    }

    public String getRltInvtNo() {
        return rltInvtNo;
    }

    public void setRltInvtNo(String rltInvtNo) {
        this.rltInvtNo = rltInvtNo;
    }

    public String getRltPutrecNo() {
        return rltPutrecNo;
    }

    public void setRltPutrecNo(String rltPutrecNo) {
        this.rltPutrecNo = rltPutrecNo;
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

    public String getStshipTrsarvNatcd() {
        return stshipTrsarvNatcd;
    }

    public void setStshipTrsarvNatcd(String stshipTrsarvNatcd) {
        this.stshipTrsarvNatcd = stshipTrsarvNatcd;
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

    public String getVrfdedMarkcd() {
        return vrfdedMarkcd;
    }

    public void setVrfdedMarkcd(String vrfdedMarkcd) {
        this.vrfdedMarkcd = vrfdedMarkcd;
    }

    public String getGenDecFlag() {
        return genDecFlag;
    }

    public void setGenDecFlag(String genDecFlag) {
        this.genDecFlag = genDecFlag;
    }

    public String getEntrySeqNo() {
        return entrySeqNo;
    }

    public void setEntrySeqNo(String entrySeqNo) {
        this.entrySeqNo = entrySeqNo;
    }

    public String getDclTypecd() {
        return dclTypecd;
    }

    public void setDclTypecd(String dclTypecd) {
        this.dclTypecd = dclTypecd;
    }

    public String getNeedEntryModified() {
        return needEntryModified;
    }

    public void setNeedEntryModified(String needEntryModified) {
        this.needEntryModified = needEntryModified;
    }

    public String getLevyBlAmt() {
        return levyBlAmt;
    }

    public void setLevyBlAmt(String levyBlAmt) {
        this.levyBlAmt = levyBlAmt;
    }
}
