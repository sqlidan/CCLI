package com.haiersoft.ccli.wms.entity.apiEntity;

import java.util.List;

public class InvtMessage implements java.io.Serializable{
    //@ApiModelProperty(value = "核注清单报关单表头")
    private InvtDecHeadType invtDecHeadType;
    //@ApiModelProperty(value = "核注清单报关单商品表体")
    private List<InvtDecListType> invtDecListType;
    //@ApiModelProperty(value = "简单加工、一纳成品内销成品明细[核注清单商品表体(保存集报清单料件信息)]")
    private List<InvtGoodsType> invtGoodsType;
    //@ApiModelProperty(value = "核注清单表头")
    private InvtHeadType invtHeadType;
    //@ApiModelProperty(value = "核注清单表体")
    private List<InvtListType> invtListType;
    //@ApiModelProperty(value = "核注清单出入库单集报表体")
    private List<InvtWarehouseType> invtWarehouseType;
    //@ApiModelProperty(value = "清单状态")
    private String listStat;
    //@ApiModelProperty(value = "操作卡的海关十位")
    private String operCusRegCode;
    //@ApiModelProperty(value = "子系统ID 95 加工贸易账册系统 B1 加工贸易手册系统 B2 加工贸易担保管理系统 B3 保税货物流转系统二期 Z7 海关特殊监管区域管理系统 Z8 保税物流管理系统")
    private String sysId;
    	//@ApiModelProperty(value = "鉴权实体")
//	private SecurityModel securityModel;
    //@ApiModelProperty(value = "电子口岸卡密码")
    private String pass;
    //@ApiModelProperty(value = "关务平台租户编码")
    private String memberCode;
    //@ApiModelProperty(value = "海关卡卡号")
    private String icCode;
    //@ApiModelProperty(value = "api中台秘钥")
    private String key;

    public InvtDecHeadType getInvtDecHeadType() {
        return invtDecHeadType;
    }

    public void setInvtDecHeadType(InvtDecHeadType invtDecHeadType) {
        this.invtDecHeadType = invtDecHeadType;
    }

    public List<InvtDecListType> getInvtDecListType() {
        return invtDecListType;
    }

    public void setInvtDecListType(List<InvtDecListType> invtDecListType) {
        this.invtDecListType = invtDecListType;
    }

    public List<InvtGoodsType> getInvtGoodsType() {
        return invtGoodsType;
    }

    public void setInvtGoodsType(List<InvtGoodsType> invtGoodsType) {
        this.invtGoodsType = invtGoodsType;
    }

    public InvtHeadType getInvtHeadType() {
        return invtHeadType;
    }

    public void setInvtHeadType(InvtHeadType invtHeadType) {
        this.invtHeadType = invtHeadType;
    }

    public List<InvtListType> getInvtListType() {
        return invtListType;
    }

    public void setInvtListType(List<InvtListType> invtListType) {
        this.invtListType = invtListType;
    }

    public List<InvtWarehouseType> getInvtWarehouseType() {
        return invtWarehouseType;
    }

    public void setInvtWarehouseType(List<InvtWarehouseType> invtWarehouseType) {
        this.invtWarehouseType = invtWarehouseType;
    }

    public String getListStat() {
        return listStat;
    }

    public void setListStat(String listStat) {
        this.listStat = listStat;
    }

    public String getOperCusRegCode() {
        return operCusRegCode;
    }

    public void setOperCusRegCode(String operCusRegCode) {
        this.operCusRegCode = operCusRegCode;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getIcCode() {
        return icCode;
    }

    public void setIcCode(String icCode) {
        this.icCode = icCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
