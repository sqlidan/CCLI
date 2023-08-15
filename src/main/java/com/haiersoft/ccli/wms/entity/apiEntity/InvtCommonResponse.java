package com.haiersoft.ccli.wms.entity.apiEntity;

//@ApiModel(value = "核注清单暂存、申报响应")
//@Data
public class InvtCommonResponse implements java.io.Serializable{
    //@ApiModelProperty(value = "统一编号")
    private String seqNo;
    //@ApiModelProperty(value = "手册号、账册号")
    private String etpsPreentNo;
    //@ApiModelProperty(value = "响应信息")
    private String checkInfo;
    //@ApiModelProperty(value = "响应代码")
    private String dealFlag;
    //@ApiModelProperty(value = "申报日期")
    private String InvtDclTime;

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getEtpsPreentNo() {
        return etpsPreentNo;
    }

    public void setEtpsPreentNo(String etpsPreentNo) {
        this.etpsPreentNo = etpsPreentNo;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

    public String getDealFlag() {
        return dealFlag;
    }

    public void setDealFlag(String dealFlag) {
        this.dealFlag = dealFlag;
    }

    public String getInvtDclTime() {
        return InvtDclTime;
    }

    public void setInvtDclTime(String invtDclTime) {
        InvtDclTime = invtDclTime;
    }
}
