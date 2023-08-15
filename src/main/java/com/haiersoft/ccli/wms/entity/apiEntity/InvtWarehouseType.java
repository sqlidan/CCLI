package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @Author chenp
 * @Description 核注清单出入库单集报表体
 * @Date 11:00 2020/12/5
 **/
//@Data
//@ApiModel(value = "核注清单出入库单集报表体")
public class InvtWarehouseType {
    //@ApiModelProperty(value = "中心统一编号")
    private String seqNo;
    //@ApiModelProperty(value = "序号")
    private String gNo;
    //@ApiModelProperty(value = "保税清单编号")
    private String bondInvtNo;
    //@ApiModelProperty(value = "出入库单编号")
    private String sasStockNo;

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getgNo() {
        return gNo;
    }

    public void setgNo(String gNo) {
        this.gNo = gNo;
    }

    public String getBondInvtNo() {
        return bondInvtNo;
    }

    public void setBondInvtNo(String bondInvtNo) {
        this.bondInvtNo = bondInvtNo;
    }

    public String getSasStockNo() {
        return sasStockNo;
    }

    public void setSasStockNo(String sasStockNo) {
        this.sasStockNo = sasStockNo;
    }
}
