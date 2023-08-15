package com.haiersoft.ccli.wms.entity.apiEntity;


/**
 * @ClassName: FileMessage
 * @Description: TODO
 * @Author chenp
 * @Date 2021/3/13 11:04
 *
 */
//@Data
//@ApiModel(value = "随附单证信息实体", description = "随附单证信息实体")
public class FileMessage {
    //@ApiModelProperty(" 随附单证序号")
    private String acmpFormSeqno;
    //@ApiModelProperty("分中心统一编号")
    private String dclSeqNo;
    //@ApiModelProperty("附件名称")
    private String fileName;
    //@ApiModelProperty(" 单证号")
    private String blsNo;
    //@ApiModelProperty("单证类型")
    private String acmpFormTypeCd;
    //@ApiModelProperty("单子号")
    private String acmpFormTypeNo;
    //@ApiModelProperty("单证文件源名称")
    private String acmpFormFileNm;
    //@ApiModelProperty("清单序号")
    private Long invtGdsSeqNo;
    //@ApiModelProperty("卡号")
    private String icCardNo;
    //@ApiModelProperty("")
    private String transferTradeCode;
    //@ApiModelProperty("修改标志")
    private String modfMarkCD;
    //@ApiModelProperty("文件类型")
    private String fileType;
    //@ApiModelProperty("文件内容")
    private String fileContent;
    //@ApiModelProperty("")
    private String appCode;
    //@ApiModelProperty("")
    private String replace;

    public String getAcmpFormSeqno() {
        return acmpFormSeqno;
    }

    public void setAcmpFormSeqno(String acmpFormSeqno) {
        this.acmpFormSeqno = acmpFormSeqno;
    }

    public String getDclSeqNo() {
        return dclSeqNo;
    }

    public void setDclSeqNo(String dclSeqNo) {
        this.dclSeqNo = dclSeqNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBlsNo() {
        return blsNo;
    }

    public void setBlsNo(String blsNo) {
        this.blsNo = blsNo;
    }

    public String getAcmpFormTypeCd() {
        return acmpFormTypeCd;
    }

    public void setAcmpFormTypeCd(String acmpFormTypeCd) {
        this.acmpFormTypeCd = acmpFormTypeCd;
    }

    public String getAcmpFormTypeNo() {
        return acmpFormTypeNo;
    }

    public void setAcmpFormTypeNo(String acmpFormTypeNo) {
        this.acmpFormTypeNo = acmpFormTypeNo;
    }

    public String getAcmpFormFileNm() {
        return acmpFormFileNm;
    }

    public void setAcmpFormFileNm(String acmpFormFileNm) {
        this.acmpFormFileNm = acmpFormFileNm;
    }

    public Long getInvtGdsSeqNo() {
        return invtGdsSeqNo;
    }

    public void setInvtGdsSeqNo(Long invtGdsSeqNo) {
        this.invtGdsSeqNo = invtGdsSeqNo;
    }

    public String getIcCardNo() {
        return icCardNo;
    }

    public void setIcCardNo(String icCardNo) {
        this.icCardNo = icCardNo;
    }

    public String getTransferTradeCode() {
        return transferTradeCode;
    }

    public void setTransferTradeCode(String transferTradeCode) {
        this.transferTradeCode = transferTradeCode;
    }

    public String getModfMarkCD() {
        return modfMarkCD;
    }

    public void setModfMarkCD(String modfMarkCD) {
        this.modfMarkCD = modfMarkCD;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }
}

