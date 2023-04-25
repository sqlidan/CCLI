package com.haiersoft.ccli.cost.entity.enumVo;

public enum StandBookEnum {

    CODESTHIRD("换单费","WarehouseA"),
    CODESSECONDE("洗箱费","WarehouseA"),
    CODESSTHIRD("修箱费","WarehouseA"),
    CODESFIF("箱使费","WarehouseA"),
    CODESSIX("押箱代理费","WarehouseA"),
    CODESSEVEN("港杂费","WarehouseA"),
    CODESFIFTY("机械","WarehouseA"),
    CODESEIGHT("操作费","WarehouseA"),
    CODESNINE("超期制冷费","WarehouseA"),
    CODESTEN("单证","WarehouseA"),
    CODESELEVEN("卸船作业","WarehouseA"),
    CODESTWELEN("押金","WarehouseA");



    private String code;

    private String msg;

    StandBookEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByCode(String code) {
        String arr = "";
        for (StandBookEnum value : StandBookEnum.values()) {
            if(value.code.equals(code)){
                arr = value.msg;
                return arr;
            }
        }
        return arr;
    }
}
