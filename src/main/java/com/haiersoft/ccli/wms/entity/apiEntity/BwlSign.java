package com.haiersoft.ccli.wms.entity.apiEntity;

//@Data
//@ApiModel(value = "BwlSign")
public class BwlSign implements java.io.Serializable{
//    @ApiModelProperty(value = "仓库账册号")
    private String bwlNo;

//    @ApiModelProperty(value = "经营单位编码")
    private String tradeCode;

//    @ApiModelProperty(value = "经营单位社会信用代码")
    private String tradeCreditCode;

    public String getBwlNo() {
        return bwlNo;
    }

    public void setBwlNo(String bwlNo) {
        this.bwlNo = bwlNo;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getTradeCreditCode() {
        return tradeCreditCode;
    }

    public void setTradeCreditCode(String tradeCreditCode) {
        this.tradeCreditCode = tradeCreditCode;
    }
}
