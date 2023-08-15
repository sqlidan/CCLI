package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @ClassName: CNemsInvtDecHeadType
 * @Description: 随附单据
 * @Author chenp
 * @Date 2021/2/01 8:41
 *
 */
//@ApiModel(value = "随附单据", description = "随附单据")
//@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
//@TableName("c_nems_acmp_rl_type")
public class CNemsAcmpRlType  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预录入统一编号
     */
    //@ApiModelProperty("预录入统一编号")
    private String seqNo;
    /**
     * 附件文件名称（非结构化必填）
     */
    //@ApiModelProperty("附件文件名称（非结构化必填）")
    private String fileName;
    /**
     * 变更次数
     */
    //@ApiModelProperty("变更次数")
    private int chgTmsCnt;
    /**
     * 随附单证格式
     */
    //@ApiModelProperty("随附单证格式")
    private String acmpFormFmt;
    /**
     * 业务单证类型
     */
    //@ApiModelProperty("业务单证类型")
    private String blsType;
    /**
     * 随附单证序号
     */
    //@ApiModelProperty("随附单证序号")
    private String acmpFormSeqno;
    /**
     * 随附单证类型代码
     */
    //@ApiModelProperty("随附单证类型代码")
    private String acmpFormTypeCd;
    /**
     * 随附单证编号
     */
    //@ApiModelProperty("随附单证编号")
    private String acmpFormNo;
    /**
     * 随附单证文件名称
     */
    //@ApiModelProperty("随附单证文件名称")
    private String acmpFormFileNm;
    /**
     * 清单商品序号
     */
    //@ApiModelProperty("清单商品序号")
    private String invtGdsSeqNo;
    /**
     * 上传人IC卡号
     */
    //@ApiModelProperty("上传人IC卡号")
    private String icCardNo;
    /**
     * 上传单位海关编码
     */
    //@ApiModelProperty("上传单位海关编码")
    private String transferTradeCode;
    /**
     * 备注
     */
    //@ApiModelProperty("备注")
    private String rmk;
    /**
     * 修改标记代码
     */
    //@ApiModelProperty("修改标记代码")
    private String modfMarkCd;

    /**
     * 包ID
     */
    //@ApiModelProperty("包ID")
    private String pocketId;
    /**
     * 文件上传路径
     */
    //@ApiModelProperty("文件上传路径")
    private String filePath;
    /**
     * 当前包序号
     */
    //@ApiModelProperty("当前包序号")
    private String curPocketNo;
    /**
     * 总包数
     */
    //@ApiModelProperty("总包数")
    private String totalPocketQty;

    //@ApiModelProperty("核注清单表头id")
//    @JsonSerialize(using= ToStringSerializer.class)
    private Long headId;


    /**
     * 经营企业社会信用代码
     */
    //@ApiModelProperty("经营企业社会信用代码")
//    @TableField(exist = false)
    private String bizopEtpsSccd;

    /**
     * 经营企业编号
     */
    //@ApiModelProperty("经营企业编号")
//    @TableField(exist = false)
    private String bizopEtpsno;

    /**
     * 经营企业名称
     */
    //@ApiModelProperty("经营企业名称")
//    @TableField(exist = false)
    private String bizopEtpsNm;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getChgTmsCnt() {
        return chgTmsCnt;
    }

    public void setChgTmsCnt(int chgTmsCnt) {
        this.chgTmsCnt = chgTmsCnt;
    }

    public String getAcmpFormFmt() {
        return acmpFormFmt;
    }

    public void setAcmpFormFmt(String acmpFormFmt) {
        this.acmpFormFmt = acmpFormFmt;
    }

    public String getBlsType() {
        return blsType;
    }

    public void setBlsType(String blsType) {
        this.blsType = blsType;
    }

    public String getAcmpFormSeqno() {
        return acmpFormSeqno;
    }

    public void setAcmpFormSeqno(String acmpFormSeqno) {
        this.acmpFormSeqno = acmpFormSeqno;
    }

    public String getAcmpFormTypeCd() {
        return acmpFormTypeCd;
    }

    public void setAcmpFormTypeCd(String acmpFormTypeCd) {
        this.acmpFormTypeCd = acmpFormTypeCd;
    }

    public String getAcmpFormNo() {
        return acmpFormNo;
    }

    public void setAcmpFormNo(String acmpFormNo) {
        this.acmpFormNo = acmpFormNo;
    }

    public String getAcmpFormFileNm() {
        return acmpFormFileNm;
    }

    public void setAcmpFormFileNm(String acmpFormFileNm) {
        this.acmpFormFileNm = acmpFormFileNm;
    }

    public String getInvtGdsSeqNo() {
        return invtGdsSeqNo;
    }

    public void setInvtGdsSeqNo(String invtGdsSeqNo) {
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

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getModfMarkCd() {
        return modfMarkCd;
    }

    public void setModfMarkCd(String modfMarkCd) {
        this.modfMarkCd = modfMarkCd;
    }

    public String getPocketId() {
        return pocketId;
    }

    public void setPocketId(String pocketId) {
        this.pocketId = pocketId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCurPocketNo() {
        return curPocketNo;
    }

    public void setCurPocketNo(String curPocketNo) {
        this.curPocketNo = curPocketNo;
    }

    public String getTotalPocketQty() {
        return totalPocketQty;
    }

    public void setTotalPocketQty(String totalPocketQty) {
        this.totalPocketQty = totalPocketQty;
    }

    public Long getHeadId() {
        return headId;
    }

    public void setHeadId(Long headId) {
        this.headId = headId;
    }

    public String getBizopEtpsSccd() {
        return bizopEtpsSccd;
    }

    public void setBizopEtpsSccd(String bizopEtpsSccd) {
        this.bizopEtpsSccd = bizopEtpsSccd;
    }

    public String getBizopEtpsno() {
        return bizopEtpsno;
    }

    public void setBizopEtpsno(String bizopEtpsno) {
        this.bizopEtpsno = bizopEtpsno;
    }

    public String getBizopEtpsNm() {
        return bizopEtpsNm;
    }

    public void setBizopEtpsNm(String bizopEtpsNm) {
        this.bizopEtpsNm = bizopEtpsNm;
    }
}
