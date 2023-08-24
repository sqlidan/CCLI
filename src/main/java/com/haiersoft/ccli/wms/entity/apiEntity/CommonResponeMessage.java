
package com.haiersoft.ccli.wms.entity.apiEntity;


//@Data
//@ApiModel(value = "导入通用响应信息")
public class CommonResponeMessage {
//	@ApiModelProperty(value = "中心统一编号")
    private String seqNo;
//	@ApiModelProperty(value = "客户端内部编号")
    private String etpsPreentNo;
//	@ApiModelProperty(value = "响应信息")
    private String checkInfo;
//	@ApiModelProperty(value = "响应代码")
    private String dealFlag;

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
}
