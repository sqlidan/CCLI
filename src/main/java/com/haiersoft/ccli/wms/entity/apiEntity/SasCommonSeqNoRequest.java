package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @Author sunzhijie
 */
//@Data
//@ApiModel(value = "物流账册详细数据查询服务--传输")
//@NoArgsConstructor
public class SasCommonSeqNoRequest implements java.io.Serializable {
//    @ApiModelProperty(value = "业务单据统一编号")
    private String BlsNo;

//    @ApiModelProperty(value = "业务单证类型")
    private String BlsType;

//    @ApiModelProperty(value = "变更次数")
    private String ChgTmsCnt;

//    @ApiModelProperty(value = "操作卡海关十位编码")
    private String OperCusRegCode;

//    @ApiModelProperty(value = "关联商品序号")
    private String RltGdsSeqno;

//    @ApiModelProperty(value = "中心统一编号")
    private String seqNo;

    //	private SecurityModel securityModel;
//    @ApiModelProperty(value = "电子口岸卡密码")
    private String pass;
//    @ApiModelProperty(value = "关务平台租户编码")
    private String memberCode;
//    @ApiModelProperty(value = "海关卡卡号")
    private String icCode;
//    @ApiModelProperty(value = "api中台秘钥")
    private String key;

    public String getBlsNo() {
        return BlsNo;
    }

    public void setBlsNo(String blsNo) {
        BlsNo = blsNo;
    }
}

