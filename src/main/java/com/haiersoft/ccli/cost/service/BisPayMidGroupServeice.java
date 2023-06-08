package com.haiersoft.ccli.cost.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.service.ClientRelationService;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.cost.dao.BisPayMidGroupDao;
import com.haiersoft.ccli.cost.dao.StandingBookMidGroupDao;
import com.haiersoft.ccli.cost.entity.BaseExpenseCategoryDetail;
import com.haiersoft.ccli.cost.entity.BisPay;
import com.haiersoft.ccli.cost.entity.enumVo.CostClassifyEnum;
import com.haiersoft.ccli.cost.entity.vo.BisPayVo;
import com.haiersoft.ccli.cost.entity.vo.MidGroupVo;
import com.haiersoft.ccli.cost.util.AESUtils;
import com.haiersoft.ccli.system.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.BatchUpdateException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BisPayMidGroupServeice extends BaseService<BisPay, String> {

    @Autowired
    private HttpGo httpGo;
    @Autowired
    private BisPayMidGroupDao bisPayMidGroupDao;

    @Autowired
    private BaseExpenseCategoryDetailService baseExpenseCategoryDetailService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRelationService clientRelationService;
    @Autowired
    private StandingBookMidGroupDao standingBookMidGroupDao;

    private static final  String CUSTOMER_URL = "http://10.135.123.7:8080/api/customer/getCustomerbyCode";
    private static final  String STANDINGBOOK_MIDGROUP_URL="http://10.135.123.7:8080/api/cost";
    private static final String BISPAYORDER_URL="http://10.135.123.7:8080/api/order";
    private static final String STATU = "已上传";
    private static final String NOTSTATU = "未上传";
    private static final String STATEMENT = "已生成结算单";
    private static final String NOTSTATEMENT = "未生成结算单";
    private static final String PASSWORD="71104c2ffcc2c5a2";
    private static Logger log = LoggerFactory.getLogger(BisPayMidGroupDao.class);
    @Override
    public HibernateDao<BisPay, String> getEntityDao() {
        return bisPayMidGroupDao;
    }


    public String selectBisOneByCodeNum(List<String> codeNums,Boolean flag){
        BaseClientInfo baseClientInfo = new BaseClientInfo();
        NumberFormat numberFormat = NumberFormat.getInstance();
        List<Map<String, Object>> allDatas = bisPayMidGroupDao.selectOneByCodeNum(codeNums);
        log.info("需要上传的订单详情数据是"+allDatas);
//        allDatas.stream().collect(Collectors.collectingAndThen(
//                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(p -> (String) p.get("PAYID")))),
//                ArrayList::new));
        /*
        * 将中台所需要的数据发送至中台
        * */
        String error = "";
        List<MidGroupVo> midGroupVoList = new ArrayList<>();

        for (Map<String, Object> allData : allDatas) {
            String  detailCode = "";
            String detailcodename= "";
            Object origBizId = allData.get("PAYID"); //原始业务编号
            String feecodes = allData.get("FEECODE").toString();
            Map<String,Object> mapBase =baseExpenseCategoryDetailService.getCodeByFeeCode(feecodes);
            String units = standingBookMidGroupDao.queryUnitsByFeeCode(feecodes);
            Object detail = mapBase.get("DETAIL_CODE");
            Object detailname = mapBase.get("DETAIL_CODE_NAME");
            if(StringUtils.isEmpty(detail)){
                error =error+"原始业务编号为"+origBizId+"下费用编号"+feecodes+"的财务中台客户费用代码为空，请联系开发人员维护;";
                continue;
            }

//            if(StringUtils.isEmpty(detailname)){
//                error=error+"原始业务编号为"+origBizId+"的客户费目编码为空;";
//                continue;
//            }
            detailCode= detail.toString();
            detailcodename =detailname.toString();
            //String costClassifyCode = StandingBookMidGroupService.sendParamReturncostClassifyCode(detailcodename);
            if(StringUtils.isEmpty(mapBase.get("WAREHOUSE"))&&StringUtils.isEmpty(mapBase.get("WAREHOUSE_PAY"))){
                error=error+"原始业务编号为"+origBizId+"的财务中台客户费用代码"+detail+"对应的费目类别Warehouse和Warehouse_pay都为空;";
                continue;
            }
            String warehouse = StringUtils.isEmpty(mapBase.get("WAREHOUSE_PAY"))? 
            		mapBase.get("WAREHOUSE").toString():mapBase.get("WAREHOUSE_PAY").toString();
            //数据库客户名称
            String clientname = (String) allData.get("CLIENTNAME");
            Object clientid = allData.get("CLIENTID");
            Integer clientId = Integer.valueOf(clientid.toString());
            baseClientInfo = clientService.get(clientId);
            if(StringUtils.isEmpty(clientname) ){
                error =error+"原始业务编号为"+origBizId+"的客户查询异常，部分明细无法上传;";
                continue;
            }

            Map map = new HashMap();
            Gson gson = new Gson();
            map.put("isForeign",false);
            map.put("customerName",baseClientInfo.getRealClientName());
            String jsonClient = gson.toJson(map);
            log.info("查询客户名称:"+jsonClient);
            String encrypt = AESUtils.encrypt(jsonClient,PASSWORD);
            String responseParam = httpGo.sendRequestHeader(CUSTOMER_URL, encrypt);
            try {
    	        JSON.parse(responseParam);
    	    } catch (Exception e) {
    	    	return "解析财务中台获取客户信息接口返回异常："+responseParam;
    	    }
            //返回
            JSONObject jsonObject =JSON.parseObject(responseParam);
            Integer code = (Integer)jsonObject.get("code");
            if(200 != code){
                error = error+".客户名称"+clientname+"查询错误;";
                continue;
            }

            Object data = jsonObject.get("data");
            JSONObject jsonData = JSONObject.parseObject(data.toString());
            Object setCode = jsonData.get("code");
            Object customerName = jsonData.get("customerName");

            MidGroupVo md = new MidGroupVo();
            md.setSettleObjectCode(setCode.toString());
            md.setSettleObjectName(customerName.toString());

            Object origCostId = allData.get("ID");// 原始费用编号
            String origCostIds = origCostId.toString();
//            Object unit = allData.get("CURRENCY"); //计量单位
            Object unitPrice = allData.get("UNITPRICE");// 单价  -  单价
            Object quantity = allData.get("NUM");// 数量 - 数量
            BigDecimal origAm = (BigDecimal) allData.get("SHOULDRMB"); //应收  - 原始金额
            BigDecimal origAmount = origAm.setScale(2, RoundingMode.HALF_UP);
            BigDecimal subtract = null;
            if(origAmount != null){
                subtract =origAmount.subtract(origAmount);//应收  - 实收=调整金额
            }
            Object exchRate = allData.get("EXCHANGERATE");//汇率
            Object direction = "P";//应付 P
            Object settleDate = allData.get("ASKDATE");
            // 获取税率
            Object taxrate = allData.get("TAXRATE");
            if(StringUtils.isEmpty(taxrate) || taxrate == "0"){
                md.setTaxRate("0");
            }
            if(!StringUtils.isEmpty(taxrate)){
                Float aFloat = Float.valueOf(taxrate.toString());
                md.setTaxRate(numberFormat.format(aFloat * 100));
            }
            md.setCostClassifyCode(warehouse);
            md.setActAmount(origAmount); //实际金额
            md.setOrigBizId(origBizId.toString());
            md.setBookTime(settleDate.toString());
            md.setOrigCostId(origCostIds);
            if(StringUtils.isEmpty(units)){
                md.setUnit("箱");
            }
            if(!StringUtils.isEmpty(units)){
                md.setUnit(units);
            }
            md.setQuantity(new BigDecimal(quantity.toString()));
            md.setExchRate(new BigDecimal(exchRate.toString()));
            md.setDirection(direction.toString());
            md.setIsCash("1");
            md.setAdjAmount(subtract); //adjAmount
            md.setType("1");
            md.setUnitPrice(new BigDecimal(unitPrice.toString()));

            md.setCurrency("CNY");
            md.setOrigAmount(origAmount);  //原始金额
            md.setCostCode(detailCode);
            md.setCostCenterCode("00001663002");//业务开发部
            md.setOperator("2550010");//韩总的员工号
            md.setOrgCode("080013");
            midGroupVoList.add(md);
        }
        if (CollectionUtils.isEmpty(midGroupVoList)) {
            log.info("应付费用传过去的数据为空");
            return error;
        }
        if(error != "" || !error.equals("")){
            return error;
        }
        Gson gson = new Gson();
        Map mapParams = new HashMap();
        mapParams.put("orgCode","080013");//冷链组织编号
        mapParams.put("appId","7e86aa901e86de01");
        mapParams.put("costList",midGroupVoList);
        mapParams.put("isMerge",flag);
        String paramJson = gson.toJson(mapParams);
        HashMap<String,String> map = new HashMap();
        map.put("orgCode","080013");
        String encrypt = AESUtils.encrypt(paramJson,PASSWORD);
        String responseParam = httpGo.sendRequestHead(STANDINGBOOK_MIDGROUP_URL, map, encrypt);

        if(!StringUtils.isEmpty(responseParam)){
        	try {
      	        JSON.parse(responseParam);
      	    } catch (Exception e) {
      	    	return "解析财务中台上传费用接口返回异常："+responseParam;
      	    }
            JSONObject jsonObject = JSONObject.parseObject(responseParam);
            Object code = jsonObject.get("code");
            String codes = String.valueOf(code);
            String msg = jsonObject.get("msg").toString();
            if("200".equals(codes)){
                for (String codeNum : codeNums) {
                    bisPayMidGroupDao.updateStatusByPayId(codeNum);
                }
                if(flag == true){
                    String statementNo = "";
                    String  data = jsonObject.get("data").toString();
                    MidGroupVo midGroupVo = midGroupVoList.get(0);
                    String decrypt = AESUtils.decrypt(data, PASSWORD);
                    //将集合转成JSONArray
                    JSONArray jsonArray = JSONArray.parseArray(decrypt);
                    //打印JSONArray数组
                    System.out.println("jsonArray ="+jsonArray);
                    for (int i = 0; i <jsonArray.size() ; i++) {  //遍历JSONArray数组
                        JSONObject object1 = JSONArray.parseObject(jsonArray.get(i).toString()); //将JSONArray数组转成JSONObject对象
                        statementNo = object1.getString("statementNo");
                        System.out.println(statementNo); //通过getString获取对象的属性
                        bisPayMidGroupDao.addStatementNoAndCostId(midGroupVo.getOrigBizId(), statementNo);
                    }
                    log.info("请求明细参数是："+JSON.toJSONString(midGroupVoList)+",返回值是："+JSON.toJSONString(jsonObject));
                    return "success";
                }
            }
            log.info("请求明细参数是："+JSON.toJSONString(midGroupVoList)+",返回值是："+JSON.toJSONString(jsonObject));
             return msg;
        }
        return error;
    }

    /*
    * 传过来的id集合
    * */
    @Transactional
    public String subbitJson(List<String> ids,Boolean flag) {
        //先将订单传过去
        log.info("前端应付传过来的ids:{}",ids);
        String messages = this.selectBisAllByCodeNumSendOrder(ids);
        log.info("前端应付订单上传返回的信息:{}",messages);
        if(messages.equals("success")){
            // 在传明细
            return  this.selectBisOneByCodeNum(ids,flag);
        }
        return messages;
    }

    public String selectBisAllByCodeNumSendOrder(List<String> codeNums){
        List<BisPayVo> bisPayVo = new ArrayList<>();
        String messages = null;
        List<Map<String, Object>> allDatas =bisPayMidGroupDao.selectBisAllByCodeNumSendOrder(codeNums);
        for (Map<String, Object> allData : allDatas) {
            Object origBizId = allData.get("PAYID"); //原始业务编号
            Object midgroupstatic1 = allData.get("MIDGROUPSTATIC");
            Object settleDate = allData.get("ASKDATE");
            Object clientId = allData.get("CLIENTID");
            Map<String,String> map = new HashMap<>();
            int client = Integer.parseInt(String.valueOf(clientId));
            BaseClientInfo baseClientInfo = clientService.get(client);
            String clientCode = new String();
            if(null != baseClientInfo.getRealClientName()){
                clientCode = baseClientInfo.getRealClientName();
            }else{
                return "当前海路通的客户名字为空";
            }

            map.put("customerName",clientCode.trim());
            if("MAR".equals(clientCode)){
                map.put("isForeign","true");
            }else{
                map.put("isForeign","false");
            }
            String clientOne = this.sendParamReturnClientCode(map);
            if("".equals(clientCode)){
                return "客户名称为:"+clientCode+"未在天眼查中检索到数据";
            }
            BisPayVo payVo = new BisPayVo();
            payVo.setCostCenterCode("00001663002");
            payVo.setCustomerCode(clientOne);
            payVo.setBizTime(settleDate.toString());
            payVo.setOrigBizId((String) origBizId);
            payVo.setOperator("2550010");
            payVo.setOrgCode("080013");
            payVo.setStatus((String) midgroupstatic1);
            bisPayVo.add(payVo);
        }
        /*
        * 将数据 判断是否为已发送
        * */
        List<BisPayVo> bisPayVoList = new ArrayList<>();
        for (BisPayVo payVo : bisPayVo) {
            if(!StringUtils.isEmpty(payVo.getStatus())){
                if(STATU.equals(payVo.getStatus())){
                    continue;
                }else{
                    bisPayVoList.add(payVo);
                    bisPayMidGroupDao.updateStatusByPayId(payVo.getOrigBizId());
                }
            }else {
                payVo.setOrgCode("080013");
                bisPayVoList.add(payVo);
            }
        }
        HashMap<String,String> map = new HashMap();
        map.put("orgCode","080013");
        Gson gson = new Gson();
        String jsonArray = gson.toJson(bisPayVo);
        String encrypt = AESUtils.encrypt(jsonArray,PASSWORD);
        String responseParam = httpGo.sendRequestHead(BISPAYORDER_URL, map, encrypt);
        try {
	        JSON.parse(responseParam);
	    } catch (Exception e) {
	    	return "解析财务中台上传订单接口返回异常："+responseParam;
	    }
        JSONObject jsonObject = JSON.parseObject(responseParam);
        log.info("应付订单返回的数据:{}",jsonObject);
        Integer code = (Integer) jsonObject.get("code");
        if(200 == code){
            return "success";
        }else{
            Object message = jsonObject.get("msg");
            String msg = message.toString();
            if(msg.contains("重复")){
                return "success";
            }
            if(msg.contains("客户编码不存在")){
                return  "客户编码为"+bisPayVo.get(0).getCustomerCode()+"不存在，请联系中台";
            }
            messages = jsonObject.toString();
        }
        return messages;
    }

    public static   String sendParamReturncostClassifyCode(String clientOne) {
        return CostClassifyEnum.getMsgByCode(clientOne);
    }

    //根据参数 返回客户code
    public String sendParamReturnClientCode(Map<String,String> params){
        Gson gson = new Gson();
        String jsonArray = gson.toJson(params);
        log.info("获取客户编码从本系统到到中台的数据:{}",jsonArray);
        String encrypt = AESUtils.encrypt(jsonArray,PASSWORD);
        String response = httpGo.sendRequestHeader(CUSTOMER_URL,encrypt);
        log.info("财务中台获取客户信息接口返回的数据为:{}",response);
        if(!StringUtils.isEmpty(response)){
            try {
    	        JSON.parse(response);
    	    } catch (Exception e) {
    	    	return "解析财务中台获取客户信息接口返回异常："+response;
    	    }
            JSONObject jsonObject = JSON.parseObject(response);
            if( jsonObject.getInteger("code")==200 ){
                Object datas =  jsonObject.get("data");
                JSONObject data = JSON.parseObject(datas.toString());
                return data.getString("code");
            }else{
                return "";
            }
        }
		return "";
    }

    /*
    * 撤回中台的数据
    * */
    public String returnUploadByCodeNum(List<String> ids) {
        String msgSe = "";
        String error = "";
        List<BisPay> bisPayList = new ArrayList<>();
        List<Map<String, Object>> bisPays = bisPayMidGroupDao.selectAll(ids);
        if(CollectionUtils.isEmpty(bisPays)){
           error +="选中的数据未在数据库中查到";
        }
        for (Map<String, Object> bisPay : bisPays) {
            BisPay bisPayInfo  = new BisPay();
            if(StringUtils.isEmpty(bisPay.get("MIDGROUPSTATIC")) || StringUtils.isEmpty(bisPay.get("PAY_ID"))){
                return "上传状态为未上传";
            }
            String midgroupstatic = bisPay.get("MIDGROUPSTATIC").toString();
            String payId = bisPay.get("PAY_ID").toString();
            bisPayInfo.setPayId(payId.toString());
            if(StringUtils.isEmpty(midgroupstatic)){
                error+="请查看该数据"+payId+"是否是已经撤回的状态";
                return error;
            }
            bisPayInfo.setMidGroupStatic(midgroupstatic);
            bisPayList.add(bisPayInfo);
        }
        /*
        * 自动过滤掉 选中的数据 且数据为 未上传
        * */
        List<BisPay> bisPaysArrayList = new ArrayList<>();
        bisPayList.forEach(x->{
            if(STATU.equals(x.getMidGroupStatic())){
                bisPaysArrayList.add(x);
            }
        });
        for (BisPay bisPay : bisPaysArrayList) {
            String arr =new String();
            if(null != bisPay.getPayId()){
                String payId = bisPay.getPayId();
                List<String> billNums  = bisPayMidGroupDao.getBillNum(payId);
                for (String billNum : billNums) {
                    arr+=billNum+",";
                }
                String encrypt = AESUtils.encrypt(arr,PASSWORD);
                cn.hutool.json.JSONObject jsonObject = StandingBookMidGroupService.delete(encrypt);
                Object code = jsonObject.get("code");
                Integer codes = Integer.valueOf(code.toString());
                if(200 == codes){
                    //修改上传状态
                    bisPayMidGroupDao.updateNoStatusByPayId(bisPay.getPayId());
                    log.info("未生成结算单的已撤回："+jsonObject);
                    return  "未生成结算单的已撤回";
                }
                String msg = jsonObject.get("msg").toString();
                error+=msg;
                if(msg.contains("已生成结算单")){
                    String  statementNo =  bisPayMidGroupDao.selectStatementNo(ids.get(0));
                    if(StringUtils.isEmpty(statementNo)){
                        return "该单号的结算单号尚未保存到数据库";
                    }
                    String encryptNO = AESUtils.encrypt(statementNo,PASSWORD);
                    String bodys = HttpUtil.createPost("http://10.135.123.7:8080/api/statement/removeCostByStatementNo?statementNo="+encryptNO)
                                .header("Content-Type", "application/json")
                                .header("APPID","7e86aa901e86de01")
                                .header("Fmp-Tenant-Data-Node","080013")
                                .execute()
                                .body();
                    JSONObject jsonObject1 = JSONObject.parseObject(bodys);
                    String code1 = jsonObject1.get("code").toString();
                    log.info("应付结算单撤回的响应:"+jsonObject1);
                    String msg1 = jsonObject1.get("msg").toString();
                    error+=msg1;
                    if(("操作成功").equals(msg1)){
                        cn.hutool.json.JSONObject jsonObjectSe = StandingBookMidGroupService.delete(encrypt);
                        //修改上传状态
                        String codesSe = jsonObjectSe.get("code").toString();
                        if("200".equals(codesSe)){
                            //修改上传状态
                            bisPayMidGroupDao.updateNoStatusByPayId(bisPay.getPayId());
                            log.info("应收费用未生成结算单的已撤回："+jsonObjectSe);
                            return "生成结算单已撤回";
                        }
                    }
                }
                return  error;
            }
        }

        return  "请确认数据为:"+ids.get(0)+"是否为已上传";
    }




}
