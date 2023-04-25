package com.haiersoft.ccli.api.constant;

/**
 * @Author 86185
 * @Date 2022/4/28 16:53
 * @Version 1.0
 */

public interface OpenGateConstant {

    //中台测试环境 地址
    //String baseUrl="http://10.199.18.10:10883";

    //中台测试环境 地址2
    String baseUrl="http://10.199.18.9:8086";
    /**
     *     外网测试用地址
     */
    //String baseUrl = "http://221.0.130.224:8086";

    String OPEN_GATE_TOKEN_URL = baseUrl+"/open-gate/query/gateway.getToken.service";
    // 2022修改之前的地址
    // String OPEN_GATE_GOODS_TYPE_SYN_URL = baseUrl+"/api/coldchain/goods-type-syn";

    /**
     * 货种小类信息同步
     */
    //String OPEN_GATE_GOODS_TYPE_SYN_URL = "http://10.199.18.10:10883/open-gate/coldchain/riskmanage.coldChain.coldchainGoodsTypeSyn.service";

    /**
     * 货种小类信息同步
     */
    String OPEN_GATE_GOODS_TYPE_SYN_URL = baseUrl+"/open-gate/coldchain/riskmanage.coldChain.coldchainGoodsTypeSyn.service";

    /**
     * 同步静态质押监管（解除）指令结果
     */
    String OPEN_GATE_SYNC_PLEDGE_STATIC_URL = baseUrl+"/open-gate/sync/riskmanage.pledgeOrReliefPledgeOrder.syncPledgeStatic.service";

    /**
     * 同步动态质押监管（解除）指令结果
     */
    String OPEN_GATE_SYNC_PLEDGE_DYNAMIC_URL = baseUrl+" /open-gate/sync/riskmanage.pledgeOrReliefPledgeOrder.syncPledgeDynamic.service";

    /**
     * 同步换货指令结果
     */
    String OPEN_GATE_SYNC_EXCHANGE_URL = baseUrl+" /open-gate/sync/riskmanage.pledgeOrReliefPledgeOrder.syncExchange.service";

    String RECEIVE_APPLY_RESULT_URL = baseUrl+"/open-gate/receive/riskmanage.coldChain.receiveApplyResult.service";

    String HEADER_APP_TYPE= "rpc";

    String APP_ID= "Mz4BVXCc";
    String APP_SECRET="6f17475be5f00f9d297fb7876a264631758d7a76";
    String CHAR_SET="UTF-8";
    String USER_ID="3616";
    String CHANNEL="L00";
    String VERSION="0.0.1";

    String CHAN_TYPE="0";
}
