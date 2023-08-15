package com.haiersoft.ccli.wms.entity.apiEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: SasAcmpRLType
 * @Description: TODO
 * @Author chenp
 * @Date 2021/3/13 10:53
 */
//@Data
public class SasAcmpRLType {

    private String acmpBlsStatus;
    private String acmpFormFileNm;
    private String acmpFormFmt;
    private String acmpFormNo;
    @JsonProperty(value = "acmpFormSeqNo")
    private String acmpFormSeqNo;
    private String acmpFormTypeCD;
    private String blsNo;
    private String blsType;
    private String chgTmsCnt;
    private String fileName;
    private String icCardNo;
    private String indbTime;
    private String invtGdsSeqNo;
    private String modfMarkCD;
    private String rmk;
    private String seqNo;
    private String transferTradeCode;

    public String getAcmpBlsStatus() {
        return acmpBlsStatus;
    }

    public void setAcmpBlsStatus(String acmpBlsStatus) {
        this.acmpBlsStatus = acmpBlsStatus;
    }

    public String getAcmpFormFileNm() {
        return acmpFormFileNm;
    }

    public void setAcmpFormFileNm(String acmpFormFileNm) {
        this.acmpFormFileNm = acmpFormFileNm;
    }

    public String getAcmpFormFmt() {
        return acmpFormFmt;
    }

    public void setAcmpFormFmt(String acmpFormFmt) {
        this.acmpFormFmt = acmpFormFmt;
    }

    public String getAcmpFormNo() {
        return acmpFormNo;
    }

    public void setAcmpFormNo(String acmpFormNo) {
        this.acmpFormNo = acmpFormNo;
    }

    public String getAcmpFormSeqNo() {
        return acmpFormSeqNo;
    }

    public void setAcmpFormSeqNo(String acmpFormSeqNo) {
        this.acmpFormSeqNo = acmpFormSeqNo;
    }

    public String getAcmpFormTypeCD() {
        return acmpFormTypeCD;
    }

    public void setAcmpFormTypeCD(String acmpFormTypeCD) {
        this.acmpFormTypeCD = acmpFormTypeCD;
    }

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIcCardNo() {
        return icCardNo;
    }

    public void setIcCardNo(String icCardNo) {
        this.icCardNo = icCardNo;
    }

    public String getIndbTime() {
        return indbTime;
    }

    public void setIndbTime(String indbTime) {
        this.indbTime = indbTime;
    }

    public String getInvtGdsSeqNo() {
        return invtGdsSeqNo;
    }

    public void setInvtGdsSeqNo(String invtGdsSeqNo) {
        this.invtGdsSeqNo = invtGdsSeqNo;
    }

    public String getModfMarkCD() {
        return modfMarkCD;
    }

    public void setModfMarkCD(String modfMarkCD) {
        this.modfMarkCD = modfMarkCD;
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

    public String getTransferTradeCode() {
        return transferTradeCode;
    }

    public void setTransferTradeCode(String transferTradeCode) {
        this.transferTradeCode = transferTradeCode;
    }
}