package com.haiersoft.ccli.wms.entity.apiEntity;

/**
 * @Author chenp
 * @Description 临时报关单表体
 * @Date 14:33 2020/12/5
 **/
//@Data
//@ApiModel(value = "核注清单报关单商品表体")
public class InvtDecListType implements java.io.Serializable{
//    //@ApiModelProperty(value = "核注清单中心统一编号")
    private String seqNo;
    //@ApiModelProperty(value = "报关单统一编号")
    private String decSeqNo;
    //@ApiModelProperty(value = "报关单商品序号")
    private String entryGdsSeqno;
    //@ApiModelProperty(value = "备案序号")
    private String putrecSeqno;
    //@ApiModelProperty(value = "商品编码")
    private String gdecd;
    //@ApiModelProperty(value = "商品名称")
    private String gdsNm;
    //@ApiModelProperty(value = "商品规格型号描述")
    private String gdsSpcfModelDesc;
    //@ApiModelProperty(value = "申报计量单位代码")
    private String dclUnitcd;
    //@ApiModelProperty(value = "法定计量单位代码")
    private String lawfUnitcd;
    //@ApiModelProperty(value = "第二法定计量单位代码")
    private String secdLawfUnitcd;
    //@ApiModelProperty(value = "申报单价")
    private String dclUprcAmt;
    //@ApiModelProperty(value = "申报总价")
    private String dclTotalAmt;
    //@ApiModelProperty(value = "申报币制代码")
    private String dclCurrCd;
    //@ApiModelProperty(value = "原产国（地区）")
    private String natCd;
    //@ApiModelProperty(value = "最终目的国（地区）")
    private String destinationNatcd;
    //@ApiModelProperty(value = "法定数量")
    private String lawfQty;
    //@ApiModelProperty(value = "第二法定数量")
    private String secdLawfQty;
    //@ApiModelProperty(value = "申报数量")
    private String dclQty;
    //@ApiModelProperty(value = "用途代码")
    private String useCd;
    //@ApiModelProperty(value = "备注")
    private String rmk;
    //@ApiModelProperty(value = "征免方式")
    private String lvyrlfModecd;
    //@ApiModelProperty(value = "检验检疫编码")
    private String ciqCode;
    //@ApiModelProperty(value = "商品英文名称")
    private String declGoodsEname;
    //@ApiModelProperty(value = "原产地区代码")
    private String origPlaceCode;
    //@ApiModelProperty(value = "用途代码")
    private String purpose;
    //@ApiModelProperty(value = "产品有效期")
    private String prodValidDt;
    //@ApiModelProperty(value = "产品保质期")
    private String prodQgp;
    //@ApiModelProperty(value = "货物属性代码")
    private String goodsAttr;
    //@ApiModelProperty(value = "成份/原料/组份")
    private String stuff;
    //@ApiModelProperty(value = "UN编码")
    private String unCode;
    //@ApiModelProperty(value = "危险货物名称")
    private String dangName;
    //@ApiModelProperty(value = "危包类别")
    private String dangPackType;
    //@ApiModelProperty(value = "危包规格")
    private String dangPackSpec;
    //@ApiModelProperty(value = "境外生产企业名称")
    private String engManEntCnm;
    //@ApiModelProperty(value = "非危险化学品")
    private String noDangFlag;
    //@ApiModelProperty(value = "目的地代码")
    private String destCode;
    //@ApiModelProperty(value = "检验检疫货物规格")
    private String goodsSpec;
    //@ApiModelProperty(value = "货物型号")
    private String goodsModel;
    //@ApiModelProperty(value = "货物品牌")
    private String goodsBrand;
    //@ApiModelProperty(value = "生产日期")
    private String produceDate;
    //@ApiModelProperty(value = "生产批号")
    private String prodBatchNo;
    //@ApiModelProperty(value = "境内目的地/境内货源地")
    private String districtCode;
    //@ApiModelProperty(value = "检验检疫名称")
    private String ciqName;
    //@ApiModelProperty(value = "生产单位注册号")
    private String mnufctrRegno;
    //@ApiModelProperty(value = "生产单位名称")
    private String mnufctrRegName;

}

