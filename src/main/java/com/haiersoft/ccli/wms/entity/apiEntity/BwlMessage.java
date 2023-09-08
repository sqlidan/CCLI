package com.haiersoft.ccli.wms.entity.apiEntity;

import java.util.List;

//@Data
//@ApiModel(value = "BwlMessage")
public class BwlMessage implements java.io.Serializable{
//    @ApiModelProperty(value = "最大序号")
    private int billMaxGNo;

//    @ApiModelProperty(value = "物流账册表头")
    private BwlHeadType bwlHead;

//    @ApiModelProperty(value = "物流账册表体")
    private List<BwlListType> bwlList;

//    @ApiModelProperty(value = "")
    private Pagination bwlListPage;

//    @ApiModelProperty(value = "")
    private BwlSign bwlSign;

//    @ApiModelProperty(value = "操作卡的海关十位")
    private String operCusRegCode;

//    @ApiModelProperty(value = "操作信息")
    private String operInfo;

//    @ApiModelProperty(value = "状态")
    private String status;
}
