package com.haiersoft.ccli.wms.entity.apiEntity;

import java.io.Serializable;

public enum ApiType implements Serializable {

    // TODO: 2021/12/7 修改域名 https://apiplat.sdland-sea.com http://apiplat.sdlandsea.net 内外网改造 崔玉明通知
    分类监管_空车核放单详情接口("http://apiplat.sdlandsea.net/maniQuery/SasManiGet"),
    分类监管_核放单查询接口("http://apiplat.sdlandsea.net/maniQuery/ManiQuery"),
    分类监管_非保账册商品查询接口("http://apiplat.sdlandsea.net/nonEms/NonEmsGoodsQuery"),
    分类监管_空车核放单作废接口("http://apiplat.sdlandsea.net/maniUpdate/SasManiNullify"),
    分类监管_非保核放单作废接口("http://apiplat.sdlandsea.net/maniUpdate/ManiNullify"),
    分类监管_非保核放单删除接口("http://apiplat.sdlandsea.net/maniUpdate/ManiDelete"),
    分类监管_非保核放单到货确认接口("http://apiplat.sdlandsea.net/maniConFirm/ManiConFirm"),
    分类监管_非保空车核放单申报接口("http://apiplat.sdlandsea.net/maniSave/SasManiSave"),
    分类监管_非保核放单申报接口("http://apiplat.sdlandsea.net/maniSave/ManiSave"),
    分类监管_申请单变更接口("http://apiplat.sdlandsea.net/apply/change"),
    分类监管_申请单删除接口("http://apiplat.sdlandsea.net/apply/delete"),
    分类监管_申请单作废接口("http://apiplat.sdlandsea.net/apply/ApprNullify"),
    分类监管_申请单查询接口("http://apiplat.sdlandsea.net/apprQuery/ApprQuery"),
    分类监管_申请单申报详情查询接口("http://apiplat.sdlandsea.net/apprQuery/ApprGet"),
    分类监管_申请单申报接口("http://apiplat.sdlandsea.net/apprSave/ApprSave"),
    分类监管_非保核放单详情接口("http://apiplat.sdlandsea.net/maniQuery/ManiGet"),
    分类监管_非保底账流水接口("http://apiplat.sdlandsea.net/flow/stockFlowQuery"),
    保税监管_核放单作废服务接口("http://apiplat.sdlandsea.net/passPortNullify/PassPortNullifyService"),
    保税监管_物流账册商品查询服务接口("http://apiplat.sdlandsea.net/BwlGoodsQueryService/BwlGoodsQueryService"),
    保税监管_文件上传接口("http://apiplat.sdlandsea.net/fileUpload/FileUpload"),
    保税监管_随附单据数据保存服务接口("http://apiplat.sdlandsea.net/fileUpload/AcmpRLSaveService"),
    保税监管_核放单暂存接口("http://apiplat.sdlandsea.net/passPortSave/PassPortSaveService"),
    保税监管_核放单申报接口("http://apiplat.sdlandsea.net/passPortSave/PassPortDeclearService"),
    保税监管_核放单列表查询服务接口("http://apiplat.sdlandsea.net/passPort/PassPortQueryListService"),
    保税监管_核放单详细信息查询服务接口("http://apiplat.sdlandsea.net/passPort/PassPortDetailService"),
    保税监管_物流账册列表查询服务接口("http://apiplat.sdlandsea.net/bwl/BwlQueryListService"),
    保税监管_物流账册详细数据查询服务接口("http://apiplat.sdlandsea.net/bwl/BwlDetailService"),
    保税监管_保税核注清单暂存接口("http://apiplat.sdlandsea.net/Invt/InvtSaveService"),
    保税监管_保税核注清单申报接口("http://apiplat.sdlandsea.net/Invt/InvtDeclareService"),
    保税监管_保税核注清单作废接口("http://apiplat.sdlandsea.net/Invt/InvtNullifyService"),
    保税监管_保税核注清单详细查询接口("http://apiplat.sdlandsea.net/bondedApi/InvtDetailService"),
    保税监管_保税核注清单列表查询服务接口("http://apiplat.sdlandsea.net/bondedApi/InvtQueryListService"),
    保税监管_清单结关申报接口("http://apiplat.sdlandsea.net/InvtFinishService/InvtFinishService"),
    保税监管_清单结关查询接口("http://apiplat.sdlandsea.net/InvtFinishQuery/InvtFinishQueryListService"),
    海关放行时间接口("http://apiplat.sdlandsea.net/queryZnck/queryZnckHgfxh"),
    集装箱信息查询接口("http://apiplat.sdlandsea.net/cdct/queryZnckCdct"),
    大宗快捷出区_申请单申报("http://customsapi.sdlandsea.net/api/excq/YcdpApprSecEDecl"),
    大宗快捷出区_申请单查询("http://customsapi.sdlandsea.net/api/excq/SasApprQuery"),
    大宗快捷出区_申请单详情("http://customsapi.sdlandsea.net/api/excq/SasApprGet"),
    大宗快捷出区_申请单作废("http://customsapi.sdlandsea.net/api/excq/SasApprNullify"),
    大宗快捷出区_核放单详情("http://customsapi.sdlandsea.net/api/excq/SasManiGet"),
    大宗快捷出区_核放单查询("http://customsapi.sdlandsea.net/api/excq/SasManiQuery"),
    大宗快捷出区_核放单申报("http://customsapi.sdlandsea.net/api/excq/SasManiSave"),
    大宗快捷出区_核放单作废("http://customsapi.sdlandsea.net/api/excq/SasNullify");


//	分类监管_空车核放单详情接口("https://apiplat.sdland-sea.com/maniQuery/SasManiGet"),
//	分类监管_核放单查询接口("https://apiplat.sdland-sea.com/maniQuery/ManiQuery"),
//	分类监管_非保账册商品查询接口("https://apiplat.sdland-sea.com/nonEms/NonEmsGoodsQuery"),
//	分类监管_空车核放单作废接口("https://apiplat.sdland-sea.com/maniUpdate/SasManiNullify"),
//	分类监管_非保核放单作废接口("https://apiplat.sdland-sea.com/maniUpdate/ManiNullify"),
//	分类监管_非保核放单删除接口("https://apiplat.sdland-sea.com/maniUpdate/ManiDelete"),
//	分类监管_非保核放单到货确认接口("https://apiplat.sdland-sea.com/maniConFirm/ManiConFirm"),
//	分类监管_非保空车核放单申报接口("https://apiplat.sdland-sea.com/maniSave/SasManiSave"),
//	分类监管_非保核放单申报接口("https://apiplat.sdland-sea.com/maniSave/ManiSave"),
//	分类监管_申请单变更接口("https://apiplat.sdland-sea.com/apply/change"),
//	分类监管_申请单删除接口("https://apiplat.sdland-sea.com/apply/delete"),
//	分类监管_申请单作废接口("https://apiplat.sdland-sea.com/apply/ApprNullify"),
//	分类监管_申请单查询接口("https://apiplat.sdland-sea.com/apprQuery/ApprQuery"),
//	分类监管_申请单申报详情查询接口("https://apiplat.sdland-sea.com/apprQuery/ApprGet"),
//	分类监管_申请单申报接口("https://apiplat.sdland-sea.com/apprSave/ApprSave"),
//	分类监管_非保核放单详情接口("https://apiplat.sdland-sea.com/maniQuery/ManiGet"),
//	分类监管_非保底账流水接口("https://apiplat.sdland-sea.com/flow/stockFlowQuery"),
//	保税监管_核放单作废服务接口("https://apiplat.sdland-sea.com/passPortNullify/PassPortNullifyService"),
//	保税监管_物流账册商品查询服务接口("https://apiplat.sdland-sea.com/BwlGoodsQueryService/BwlGoodsQueryService"),
//	保税监管_文件上传接口("https://apiplat.sdland-sea.com/fileUpload/FileUpload"),
//	保税监管_随附单据数据保存服务接口("https://apiplat.sdland-sea.com/fileUpload/AcmpRLSaveService"),
//	保税监管_核放单暂存接口("https://apiplat.sdland-sea.com/passPortSave/PassPortSaveService"),
//	保税监管_核放单申报接口("https://apiplat.sdland-sea.com/passPortSave/PassPortDeclearService"),
//	保税监管_核放单列表查询服务接口("https://apiplat.sdland-sea.com/passPort/PassPortQueryListService"),
//	保税监管_核放单详细信息查询服务接口("https://apiplat.sdland-sea.com/passPort/PassPortDetailService"),
//	保税监管_物流账册列表查询服务接口("https://apiplat.sdland-sea.com/bwl/BwlQueryListService"),
//	保税监管_物流账册详细数据查询服务接口("https://apiplat.sdland-sea.com/bwl/BwlDetailService"),
//	保税监管_保税核注清单暂存接口("https://apiplat.sdland-sea.com/Invt/InvtSaveService"),
//	保税监管_保税核注清单申报接口("https://apiplat.sdland-sea.com/Invt/InvtDeclareService"),
//	保税监管_保税核注清单作废接口("https://apiplat.sdland-sea.com/Invt/InvtNullifyService"),
//	保税监管_保税核注清单详细查询接口("https://apiplat.sdland-sea.com/bondedApi/InvtDetailService"),
//	保税监管_保税核注清单列表查询服务接口("https://apiplat.sdland-sea.com/bondedApi/InvtQueryListService"),
//	保税监管_清单结关申报接口("https://apiplat.sdland-sea.com/InvtFinishService/InvtFinishService"),
//	保税监管_清单结关查询接口("https://apiplat.sdland-sea.com/InvtFinishQuery/InvtFinishQueryListService"),
//	海关放行时间接口("https://apiplat.sdland-sea.com/queryZnck/queryZnckHgfxh"),
//	集装箱信息查询接口("https://apiplat.sdland-sea.com/cdct/queryZnckCdct");

    private String value;

    ApiType(String value) {this.value = value;}

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

