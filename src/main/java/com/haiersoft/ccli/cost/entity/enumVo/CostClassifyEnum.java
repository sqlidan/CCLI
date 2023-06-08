package com.haiersoft.ccli.cost.entity.enumVo;

public enum CostClassifyEnum {

    CODE("仓储","WarehouseS"),
    CODESECONDE("出入库及一次性降温","WarehouseC"),
    CODETHIRD("插电制冷","WarehouseC"),
    CODEFIF("海关全面消杀搬倒费","WarehouseC"),
    CODESIX("分拣费","WarehouseC"),
    CODESEVEN("查验作业费","WarehouseC"),
    CODEEIGHT("吊箱","WarehouseC"),
    CODENINE("倒箱","WarehouseC"),
    CODETEN("插电","WarehouseC"),
    CODEELEVEN("抄码","WarehouseC"),
    CODETWELEN("标签（内标签 外标签）","WarehouseC"),
    CODETHRITY("拍照","WarehouseC"),
    CODEFOURTY("套袋费","WarehouseC"),
    CODEFIFTY("缠膜","WarehouseC"),
    CODESIXTY("人工消杀搬倒费","WarehouseC"),
    CODESEVENTY("搬倒费","WarehouseC"),
    CODESTHIRD("商检验货代理","WarehouseA"),
    CODESSECONDE("短倒费","WarehouseA"),
    CODESSTHIRD("审批","WarehouseA"),
    CODESFIF("修箱","WarehouseA"),
    CODESSIX("报关","WarehouseA"),
    CODESSEVEN("报检","WarehouseA"),
    CODESEIGHT("换单代理","WarehouseA"),
    CODESNINE("押箱代理","WarehouseA"),
    CODESTEN("洗箱","WarehouseA"),
    CODESELEVEN("换单","WarehouseA"),
    CODESTWELEN("箱使","WarehouseA"),
    CODESTHRITY("出库报关","WarehouseA"),
    CODESFOURTY("港杂","WarehouseA"),
    CODESFIFTY("机械","WarehouseA"),
    CODESSIXTY("操作","WarehouseA"),
    CODESSEVENTY("指定箱号费","WarehouseA"),
    CODESTHRIT("超期制冷","WarehouseA"),
    CODESFOURT("单证费","WarehouseA"),
    CODESFIFT("改单费","WarehouseA"),

    CODESEIGHTQS("海关验货代理","WarehouseA"),
    CODESEIGHTQ("海关验货代理费","WarehouseA"),
    CODESNINEQ("查验服务费","WarehouseC"),
    CODESTENQ("散货船代理费","WarehouseA"),
    CODESELEVENQ("库场使用费","WarehouseC"),
    CODESTWELENQ("质押监管费","WarehouseA"),
    CODESTHRITYQ("监管费","WarehouseA"),
    CODESFOURTYQ("港建费","WarehouseA"),
    CODESFIFTYQ("海关删单费","WarehouseA"),
    CODESSIXTYQ("商检删单费","WarehouseA"),
    CODESSEVENTYQ("取样费","WarehouseC"),
    CODESTHRITQ("拍照费","WarehouseC"),
    CODESFOURTQ("进口操作费","WarehouseA"),
    CODESFIFTQ("抄码费","WarehouseC"),
    CODESEIGHTQW("吊箱费","WarehouseC"),
    CODESNINEQW("水产平台服务费","WarehouseC"),
    CODESTENQW("标签整改费","WarehouseC"),
    CODESELEVENQW("吊装费","WarehouseC"),
    CODESTWELENQW("出库报检费","WarehouseA"),
    CODESTHRITYQW("出库报关费","WarehouseA"),
    CODESFOURTYQW("加班费","WarehouseC"),
    CODESFIFTYQW("缠膜费","WarehouseC"),
    CODESSIXTYQW("代理审批费","WarehouseA"),
    CODESSEVENTYQW("滞报金","WarehouseA"),
    CODESTHRITQW("洗箱费","WarehouseA"),
    CODESFOURTQW("箱使费","WarehouseA"),
    CODESFIFTQW("修箱费","WarehouseA"),
    CODESTHRITYQWS("卸船作业","WarehouseA"),
    CODESFOURTYQWS("THC","WarehouseA"),
    CODESFIFTYQWS("商检验货代理费","WarehouseA"),
    CODESSIXTYQWS("快递费","WarehouseC"),
    CODESSEVENTYQWS("进口文件费","WarehouseA"),
    CODESTHRITQWS("换单费","WarehouseA"),
    CODESFOURTQWS("港杂费","WarehouseA"),
    CODESFIFTQWS("打印标签费","WarehouseC"),
    CODESTWELENQWG("称重费","WarehouseC"),
    CODESTHRITYQWG("操作费","WarehouseA"),
    CODESFOURTYQWG("超期制冷费","WarehouseA"),
    CODESFIFTYQWG("出入库及一次性降温费","WarehouseC"),
    CODESSIXTYQWG("插电费","WarehouseC"),
    CODESSEVENTYQWG("仓储费","WarehouseS"),
    CODESTHRITQWG("报关费","WarehouseA"),
    CODESFOURTQWG("报检费","WarehouseA"),
    CODESFIFTQWG("插电制冷费","WarehouseC"),
    CODESTHRITYQWSG("作业包干费","WarehouseC"),
    CODESFOURTYQWSG("短倒费（海关查验不消杀）","WarehouseA"),
    CODESFIFTYQWSG("短倒费（背箱落地）","WarehouseA"),
    CODESSIXTYQWSG("二次换单","WarehouseA"),
    CODESSEVENTYQWSG("短倒费（查验消杀入库）","WarehouseA"),
    CODESTHRITQWSG("拖车（查验消杀入库）","WarehouseA"),
    CODESFOURTQWSG("短倒费（核酸检测返港）","WarehouseA"),
    CODESFIFTQWSG("换单代理费","WarehouseA"),
    CODESFOURTYQWST("押箱代理费","WarehouseA"),
    CODESFIFTYQWST("进口代理费","WarehouseA"),
    CODESSIXTYQWST("短倒费（核酸消毒海关查验消杀返港）","WarehouseA"),
    CODESSEVENTYQWST("短倒费（不查验消杀入库）","WarehouseA"),
    CODESTHRITQWST("短倒费（核酸后消毒返码头）","WarehouseA"),
    CODESFOURTQWST("短倒费（全面消杀返码头）","WarehouseA"),
    CODESFIFTQWST("短倒费（核酸后消毒海关查验返码头）","WarehouseA"),
    CODESTWELENQWGT("外标签费","WarehouseC"),
    CODESTHRITYQWGT("内标签费","WarehouseC"),
    CODESFOURTYQWGT("倒箱费","WarehouseC"),
    CODESFIFTYQWGT("果蔬查验作业费","WarehouseC"),
    CODESSIXTYQWGT("码托费","WarehouseC"),
    CODESSEVENTYQWGT("出库查验作业费","WarehouseC"),
    CODESTHRITQWGT("海关罚款","WarehouseA");



    private String code;

    private String msg;

    CostClassifyEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByCode(String code) {
        String arr = "";
        for (CostClassifyEnum value : CostClassifyEnum.values()) {
            if(value.code.equals(code)){
                arr = value.msg;
                return arr;
            }
        }
        return arr;
    }
}
