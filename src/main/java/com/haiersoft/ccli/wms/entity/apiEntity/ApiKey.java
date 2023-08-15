package com.haiersoft.ccli.wms.entity.apiEntity;

import java.io.Serializable;

public enum ApiKey implements Serializable {

    分类监管_非保核放单查询秘钥("ec227aee0a2f47bb95fbd2f94d529117"),
    分类监管_非保核放单到货确认秘钥("643ea5ce27a340ba8a5e45f3d37a5b3e"),
    分类监管_非保账册查询秘钥("c5fbb63ec2444498866eaecdd41e410c"),
    分类监管_非保核放单变更服务秘钥("df833f9a8c7447a89ea9d68e9194348f"),
    分类监管_非保核放单申报秘钥("0c20f80aca754c37bbaede1f927f6a61"),
    分类监管_申请单查询秘钥("acbe0561c69d489c81cfe0a89b623f1d"),
    分类监管_申请单申报秘钥("70523957f5f34e978efaeb181c0801a6"),
    分类监管_申请单变更服务接口秘钥("92b051267a3640438f680136e8f2d1c7"),
    分类监管_非保底账流水接口秘钥("20f1cf33ae544d9984aa4184c43faae1"),
    保税监管_物流账册商品查询服务秘钥("4555614b4e2a49119aa20fa1db6b4a78"),
    保税监管_保税核放单作废秘钥("14491dc6e92c49f4badea866c5a9cf7c"),
    保税监管_保税核注清单查询服务秘钥("e1cf82c9e2f84984ae654371e35ac347"),
    保税监管_随附单据上传秘钥("0f7e20979f514998b929f00690ef595a"),
    保税监管_清单结关查询秘钥("230a74bb752d42faa0168cb0f0971bda"),
    保税监管_清单结关申报秘钥("aeea46ead3134f6e8b9ca30bcc11516c"),
    保税监管_保税核放单保存服务秘钥("87ace34407d34595a9ea649f41027eeb"),
    保税监管_保税核放单查询服务秘钥("ad7d9fe82f9744429b7e4f4bf35d89ab"),
    保税监管_物流账册查询服务秘钥("d34402b635d643c29408f4894612976d"),
    保税监管_保税核注清单保存服务秘钥("863ee8c585824ef6a4a8ef80c641467f"),
    获取海关放行时间接口秘钥("5007fbd6cc8a4feb9a1e42039152e36b"),
    获取集装箱信息查询接口秘钥("dd56af0c23ed4c0989228237e49b7624");


    private String value;

    ApiKey(String value) {this.value = value;}

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new StringBuffer("{\"dictValue\":").append(getValue())
                .append(",\"dictName\":\"").append(name())
                .append("\"}").toString();
    }
}
