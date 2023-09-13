package com.haiersoft.ccli.cost.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.AESUtils;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.cost.dao.BisCheckingBookDao;
import com.haiersoft.ccli.cost.dao.StandingBookMidGroupDao;
import com.haiersoft.ccli.cost.entity.BisCheckingBook;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.entity.enumVo.CostClassifyEnum;
import com.haiersoft.ccli.cost.entity.enumVo.StandBookEnum;
import com.haiersoft.ccli.cost.entity.vo.BisPayVo;
import com.haiersoft.ccli.cost.entity.vo.MidGroupVo;
import com.haiersoft.ccli.supervision.web.ApprController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.BatchUpdateException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 * @ClassName: StandingBookMidGroupService
 * @Description: 费用台账上传到中台 Service
 * @date 2022年10月13日 下午3:13:31
 */

@Service
@Transactional(readOnly = true)
public class StandingBookMidGroupService extends BaseService<BisStandingBook, Integer> {

    @Autowired
    private StandingBookMidGroupDao standingBookMidGroupDao;
    @Autowired
    private BisCheckingBookService checkingBookService;

    @Autowired
    private BisCheckingBookDao checkingBookDao;
    @Autowired
    private ClientService clientService;
    @Autowired
    private BisPayMidGroupServeice bisPayMidGroupServeice;
    @Autowired
    private BaseExpenseCategoryDetailService baseExpenseCategoryDetailService;

    @Autowired
    private HttpGo httpGo;

//    private final static String STANDINGBOOK_MIDGROUP_URL = "http://10.135.123.32:8080/api/cost";
//    private static final String CUSTOMER_URL = "http://10.135.123.32:8080/api/customer/getCustomerbyCode";
//    private static final String BISPAYORDER_URL = "http://10.135.123.32:8080/api/order";
//    private static final String TRUE_STATEMENT = "http://10.135.123.32:8080/api/statement/deleteCostByStatementNo?statementNo";
//    private static final String FALSE_STATEMENT = "http://10.135.123.32:8080/api/cost/remove?origCostId";
//    private static final String CLEINT_URL = "http://10.135.123.32:8080/api/customer/getCust调用申请单申报resultomerbyCode";
//    private static final String COSTCODE_URL = "http://10.135.123.32:8080/api/costCode/getCostCodeListByAppId";
//    private static final String STATU = "已上传";
//    private static final String NOTSTATU = "未上传";
//    private static final String STATEMENT = "已生成结算单";
//    private static final String NOTSTATEMENT = "未生成结算单";
//    private static final String PASSWORD = "71104c2ffcc2c5a2";

    private final static String STANDINGBOOK_MIDGROUP_URL = "http://kubemaster.sdlandsea.net:30156/api/cost";
    private static final String CUSTOMER_URL = "http://kubemaster.sdlandsea.net:30156/api/customer/getCustomerbyCode";
    private static final String BISPAYORDER_URL = "http://kubemaster.sdlandsea.net:30156/api/order";
    private static final String TRUE_STATEMENT = "http://kubemaster.sdlandsea.net:30156/api/statement/deleteCostByStatementNo?statementNo";
    private static final String FALSE_STATEMENT = "http://kubemaster.sdlandsea.net:30156/api/cost/remove?origCostId";
    private static final String CLEINT_URL = "http://kubemaster.sdlandsea.net:30156/api/customer/getCust调用申请单申报resultomerbyCode";
    private static final String COSTCODE_URL = "http://kubemaster.sdlandsea.net:30156/api/costCode/getCostCodeListByAppId";
    private static final String STATU = "已上传";
    private static final String NOTSTATU = "未上传";
    private static final String STATEMENT = "已生成结算单";
    private static final String NOTSTATEMENT = "未生成结算单";
    private static final String PASSWORD = "71104c2ffcc2c5a2";


    private static Logger log = LoggerFactory.getLogger(StandingBookMidGroupService.class);

    @Override
    public HibernateDao<BisStandingBook, Integer> getEntityDao() {
        return standingBookMidGroupDao;
    }

    public String selectOneByCodeNum(List<String> codeNums, Boolean flag) throws Exception {
        log.info("生成接口7777");
        List<Map<String, Object>> bisCheckingBooks = standingBookMidGroupDao.selectOneByCodeNum(codeNums);
        String message = this.bulidParam(bisCheckingBooks, codeNums, flag);
        return message;
    }

    private String bulidParam(List<Map<String, Object>> bisCheckingBooks, List<String> codeNums, Boolean flag) throws Exception {
        log.info("生成接口8888");
        NumberFormat numberFormat = NumberFormat.getInstance();
        String error = "";
        BaseClientInfo baseClientInfo = new BaseClientInfo();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<MidGroupVo> midGroupVoList = new ArrayList<>();
        BigDecimal allOrigAm = new BigDecimal(0.000000);
        for (Map<String, Object> bisCheckingBook : bisCheckingBooks) {
            log.info("生成接口9999");
            int client = 0;
            String detailCode ="";
            String detailcodename ="";

            Object codenum = bisCheckingBook.get("CODENUM");

            Object billnum = bisCheckingBook.get("STANDINGNUM");
            if (StringUtils.isEmpty(billnum)) {
                error = error + "应收费用账单编号为空:" + codenum;
                continue;
            }
            String standingNum = billnum.toString();

            Object currency = bisCheckingBook.get("CURRENCY");//计量单位


            BigDecimal origAm = (BigDecimal) bisCheckingBook.get("SHOULDRMB"); //应收  - 原始金额
            BigDecimal origAmount = origAm.setScale(2, RoundingMode.HALF_UP);

            Object unitPrice = bisCheckingBook.get("PRICE");

            Object quantity = bisCheckingBook.get("PIECE");

            String feecodes = bisCheckingBook.get("FEECODE").toString();

            Map<String,Object> mapBase = new HashMap<>();
            if (!StringUtils.isEmpty(feecodes)) {
                log.info("生成接口1010");
                mapBase = baseExpenseCategoryDetailService.getCodeByFeeCode(feecodes);
            }
            Object detail = mapBase.get("DETAIL_CODE");
            Object detailname = mapBase.get("DETAIL_CODE_NAME");
            if (StringUtils.isEmpty(detail)) {
                error = error + "对应的费目code为空;";
                continue;
            }
            if(StringUtils.isEmpty(detailname)){
                error=error+"数据库中未查到该"+standingNum+"数据明细的费目名称";
                continue;
            }
            detailCode = detail.toString();
            detailcodename =detailname.toString();
            String costClassifyCode = BisPayMidGroupServeice.sendParamReturncostClassifyCode(detailcodename);
            if(StringUtils.isEmpty(costClassifyCode)){
                error=error+"原始业务编号为"+standingNum+"的客户费用代码对应的费目类别编码异常;";
                continue;
            }
            Object pr = bisCheckingBook.get("REALRMB");
            log.info("生成接口11");
            BigDecimal price = BigDecimal.ZERO;
            BigDecimal pri = BigDecimal.ZERO;

            if (StringUtils.isEmpty(pr)) {
                price = new BigDecimal("0.00");
            } else {
                pri = (BigDecimal) pr;
                price = pri.setScale(2, RoundingMode.HALF_UP);
            }
            log.info("生成接口12");
            BigDecimal subtractOrigAm = new BigDecimal(0.000000);
            BigDecimal subtract = BigDecimal.ZERO;  //调整金额
            if (price != null) {
                subtractOrigAm = origAm.subtract(pri);
                allOrigAm = allOrigAm.add(subtractOrigAm);
                subtract = origAmount.subtract(price);//应收  - 实收=调整金额
            } else {
                subtractOrigAm = origAm;
                allOrigAm = allOrigAm.add(subtractOrigAm);
                subtract = origAmount;
            }
            Object direction = "R";//应付 P
            String isCashs = "";
            String isCash = bisCheckingBook.get("JSFS").toString();//     - 现结标志
            if (StringUtils.isEmpty(isCash)) {
                error = error + "isCash支付方式为空";
                continue;
            }
            if (isCash.equals("X")) {
                isCashs = "1";
            }
            if (isCash.equals("Y")) {
                isCashs = "0";
            }

            Object bookTime = bisCheckingBook.get("CRTIME");
            log.info("生成接口13");
            Object customeid = bisCheckingBook.get("CUSTOMEID");
            if (StringUtils.isEmpty(customeid)) {
                error = error + "CUSTOMEID不存在;";
                continue;
            }
            client = Integer.parseInt(String.valueOf(customeid));
            log.info("生成接口14");
            baseClientInfo = clientService.get(client);
            if (baseClientInfo == null || StringUtils.isEmpty(baseClientInfo.getRealClientName())) {
                error = error + "海陆通客户名称没查询到;";
                continue;
            }
            log.info("应收费用查找陆海通客户名称" + baseClientInfo.getRealClientName());
            Map map = new HashMap();
            Gson gson = new Gson();
            map.put("isForeign", false);
            map.put("customerName", baseClientInfo.getRealClientName());
            String jsonClient = gson.toJson(map);
            String encrypt = AESUtils.encrypt(jsonClient, PASSWORD);
            log.info("生成接口15");
            String responseParam = httpGo.sendRequestHeader(CUSTOMER_URL, encrypt);
            log.info("生成接口16 "+responseParam);
            JSONObject jsonObject = JSONObject.parseObject(responseParam);
            log.info("生成接口17"+jsonObject.toJSONString());
            Integer code = (Integer) jsonObject.get("code");
            if (200 != code) {
                String msg = jsonObject.get("msg").toString();

                log.info("应收费用获取结算单位报错");
                error = error + "根据库户名称查找返回"+msg;
//                /*Object msg =  jsonObject.get("msg");*/
//                String msg = jsonObject.toString();
                continue;
            }
            log.info("生成接口18");
            Object data = jsonObject.get("data");
            JSONObject jsonData = JSONObject.parseObject(data.toString());
            Object setCode = jsonData.get("code");
            Object customerName = jsonData.get("customerName");
            MidGroupVo md = new MidGroupVo();
            md.setSettleObjectCode(setCode.toString());
            md.setSettleObjectName(customerName.toString());
            if (price != null) {
                md.setAdjAmount(price);
            } else {
                md.setAdjAmount(new BigDecimal(0));
            }
            md.setCostClassifyCode(costClassifyCode);
            md.setOrigAmount(subtract);
            md.setTaxRate("6");
            md.setOrigBizId(codenum.toString());
            md.setIsCash(isCashs);
            md.setOrigCostId(standingNum);

            md.setUnit(currency.toString());

            if (quantity != null && quantity.equals("")) {
                md.setQuantity(new BigDecimal(quantity.toString()));
            } else {
                md.setQuantity(new BigDecimal(1));
            }
            md.setDirection(direction.toString());

            md.setUnitPrice(new BigDecimal(unitPrice.toString()));
            md.setType("1");

            md.setCostCode(detailCode);


            String data2 = null;
            Date date = DateUtils.parseDate(bookTime);
            try {
                data2 = simpleDateFormat.format(date);
            } catch (Exception e) {
                log.info(e + "应付时间转换格式报错");
            }
            log.info("生成接口19");
            md.setBookTime(data2);
            md.setCurrency("cny");
            md.setActAmount(origAmount);
            md.setOrgCode("080013");
            md.setCostCenterCode("00001661");//业务开发部
//            md.setCostCenterCode("00001662002");//客服操作部
            md.setOperator("2550010");
            midGroupVoList.add(md);
        }
        log.info("生成接口20");
        BigDecimal allOrigAmTemp = BigDecimal.ZERO;
        BigDecimal allSubtract = BigDecimal.ZERO;
        allOrigAmTemp = allOrigAm.setScale(2, RoundingMode.HALF_UP);
        if(midGroupVoList !=null && midGroupVoList.size() > 0){
            for (int i = 1; i <= midGroupVoList.size(); i++) {
                MidGroupVo md = midGroupVoList.get(i-1);
                if(i < midGroupVoList.size()){
                    allSubtract = allSubtract.add(md.getOrigAmount());
                }else{
                    md.setOrigAmount((allOrigAmTemp.subtract(allSubtract)));
                }
            }
        }
        log.info("生成接口21");
        if (CollectionUtils.isEmpty(midGroupVoList)) {
            log.info("应收费用传过去的数据为空");
            return error;
        }
        if(error != "" || !error.equals("")){
            return error;
        }
        Gson gson = new Gson();
        Map mapParams = new HashMap();
        mapParams.put("orgCode", "080013");
        mapParams.put("appId", "7e86aa901e86de01");
        mapParams.put("costList", midGroupVoList);
        mapParams.put("isMerge", flag);
        String paramJson = gson.toJson(mapParams);
        log.info("应收费用发送过去的数据是:" + paramJson);
        HashMap<String, String> map = new HashMap();
        String encrypt = AESUtils.encrypt(paramJson, PASSWORD);
        String code = new String();
        String  responseParam = httpGo.sendRequestHead(STANDINGBOOK_MIDGROUP_URL, map, encrypt);
        log.info("生成接口2121 "+responseParam);
        if (!StringUtils.isEmpty(responseParam)) {
            JSONObject jsonObject = new JSONObject();
            try{
                log.info("生成接口22");
                 jsonObject = JSONObject.parseObject(responseParam);
            }catch (Exception  e){
                log.info("生成接口23");
                throw new Exception("数据已经上传，请勿重复上传");
            }
            log.info("生成接口24");
            code = jsonObject.getString("code");
            if ("200".equals(code)) {
                log.info("生成接口25");
                checkingBookDao.updateStatusByPayId(codeNums);
                if (flag == true) {
                    log.info("生成接口26");
                    String statementNo = "";
                    String data = jsonObject.getString("data");
                    MidGroupVo midGroupVo = midGroupVoList.get(0);
                    String decrypt = AESUtils.decrypt(data, PASSWORD);
                    //将集合转成JSONArray
                    log.info("生成接口27");
                    JSONArray jsonArray = JSONArray.parseArray(decrypt);
                    log.info("生成接口28");
                    for (int i = 0; i < jsonArray.size(); i++) {  //遍历JSONArray数组
                        log.info("生成接口29");
                        JSONObject object1 = JSONArray.parseObject(jsonArray.get(i).toString()); //将JSONArray数组转成JSONObject对象
                        statementNo = object1.getString("statementNo");
                         //通过getString获取对象的属性
                        checkingBookDao.addStatementNoAndCostId(midGroupVo.getOrigBizId(), statementNo);
                    }
                }else {
                    log.info("生成接口30");
                    //hmj 不生成结算单，更新statementNo
                    MidGroupVo midGroupVo = midGroupVoList.get(0);
                    checkingBookDao.addStatementNoAndCostId(midGroupVo.getOrigBizId(), "");
                }
                return "success";
            } else {
                log.info("生成接口31");
                /*Object msg = jsonObject.get("msg");*/
                String msg = jsonObject.toString();
                String msg1 = jsonObject.getString("msg");
                if(msg1.contains("BatchUpdateException")){
                    return "数据已经上传，请勿重复上传";
                }
                log.info("msg" + msg);
                return msg;
            }
        } else {
            log.info("生成接口32");
            return "超时";
        }
    }
    public static String sendParamReturncostClassifyCode(String clientOne) {
        return StandBookEnum.getMsgByCode(clientOne);
    }

    /*
     * 传过来的 是JSON字符串
     * */
    @Transactional
    public String subbitJson(List<String> ids, Boolean flag) {
        try {
            if (!CollectionUtils.isEmpty(ids)) {
                String message = this.selectOrderByIds(ids);
                if (message.equals("success") || "success" == message) {
                    log.info("生成接口6666");
                    String msg = this.selectOneByCodeNum(ids, flag);
                    return msg;
                } else {
                    log.info("应收订单已经传过去");
                    return message;
                }
            }
        } catch (Exception e) {
            log.info("异常" + e);
        }
        return "后端报错,请看日志原因可能是（api超时）";
    }

    private String selectOrderByIds(List<String> ids) {
        log.info("生成接口开始");
        String error = "";
        List<BisPayVo> bisPayVo = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String id : ids) {
            log.info("生成接口1111");
            Map<String, String> map = new HashMap<>();
            log.info("生成接口id "+id);
            BisCheckingBook bisCheckingBook = checkingBookService.get(id);
            String customID = bisCheckingBook.getCustomID();
            log.info("生成接口customID "+customID);
            int clinetCode = Integer.parseInt(customID);
            BaseClientInfo baseClientInfo = clientService.get(clinetCode);
            String cCode = baseClientInfo.getClientCode();
            map.put("customerName", baseClientInfo.getRealClientName());
            if ("MAR".equals(cCode)) {
                map.put("isForeign", "true");
            } else {
                map.put("isForeign", "false");
            }

            String clientCode = bisPayMidGroupServeice.sendParamReturnClientCode(map);
            if ("error".equals(clientCode) || "error" == clientCode) {
                return "客户名称为:" + baseClientInfo.getClientName() + "未在天眼查中检索到数据";
            }
            BisPayVo payVo = new BisPayVo();
            payVo.setCostCenterCode("00001661");//业务开发部
//            payVo.setCostCenterCode("00001662002");//客服操作部
            payVo.setCustomerCode(clientCode);
            String data = null;
            Date date = DateUtils.parseDate(bisCheckingBook.getYearMonth());
            try {
                data = simpleDateFormat.format(date);
            } catch (Exception e) {
                log.info("应收订单时间异常" + e);
            }
            payVo.setBizTime(data);
            payVo.setOrigBizId(bisCheckingBook.getCodeNum());
            payVo.setOperator("2550010");
            payVo.setOrgCode("080013");

            if(bisCheckingBook.getMidGroupStatic() == null){
                payVo.setStatus(null);
            }else{
                if("未上传".equals(bisCheckingBook.getMidGroupStatic()) || bisCheckingBook.getMidGroupStatic().trim().length() == 0){
                    payVo.setStatus(null);
                }else{
                    payVo.setStatus(bisCheckingBook.getMidGroupStatic());
                }
            }
            bisPayVo.add(payVo);
        }
        /*
         * 将数据 判断是否为已发送
         * */
        List<BisPayVo> bisPayVoList = new ArrayList<>();
        for (BisPayVo payVo : bisPayVo) {
            if (!StringUtils.isEmpty(payVo.getStatus())) {
                if (!STATU.equals(payVo.getStatus())) {
                    bisPayVoList.add(payVo);
                }
            } else {
                payVo.setOrgCode("080013");
                bisPayVoList.add(payVo);
            }
        }
       /* if (CollectionUtils.isEmpty(bisPayVoList)) {
            return "传过去订单为空";
        }*/
        HashMap<String, String> map = new HashMap();
        map.put("orgCode", "080013");
        Gson gson = new Gson();
        String jsonArray = gson.toJson(bisPayVo);
        String encrypt = AESUtils.encrypt(jsonArray, PASSWORD);
        log.info("生成接口2222");
        String responseParam = httpGo.sendRequestHead(BISPAYORDER_URL, map, encrypt);
        log.info("生成接口2222 "+responseParam);
        JSONObject jsonObject = JSONObject.parseObject(responseParam);
        log.info("生成接口3333");
        log.info("jsonObject"+jsonObject.toJSONString());
        Integer code = (Integer) jsonObject.get("code");
        if (200 == code) {
            log.info("生成接口4444");
            return "success";
        } else {
            log.info("生成接口5555");
            Object message = jsonObject.get("msg");
            String msg = String.valueOf(message);
            if (msg.contains("重复")) {
                return "success";
            }
            String messages = jsonObject.toString();
            log.info("应收订单" + messages);
            return messages;
        }
    }

    /*
     * 从本系统将中台的数据撤回 本系统的数据 有已上传 修改为未上传
     *
     * */
    public String returnUpload(List<String> ids) {
        log.info("撤回接口开始");
        Map<String, String> headers = new HashMap<>();
        headers.put("APPID", "7e86aa901e86de01");
        List<BisCheckingBook> bisPayList = new ArrayList<>();
        String error = "";
        String msgSe = "";
        List<Map<String, Object>> bisPays = checkingBookDao.selectAll(ids);
        if (!CollectionUtils.isEmpty(bisPays)) {
            log.info("撤回接口1111");
            for (Map<String, Object> bisPay : bisPays) {
                if(StringUtils.isEmpty(bisPay.get("MIDGROUPSTATIC")) || StringUtils.isEmpty(bisPay.get("CODENUM"))){
                    return "请确认数据是否为已上传";
                }
                BisCheckingBook bisPayInfo = new BisCheckingBook();
                String midgroupstatic = bisPay.get("MIDGROUPSTATIC").toString();
                String payId = bisPay.get("CODENUM").toString();
                if (NOTSTATU.equals(midgroupstatic)) {
                     error += "请确认改数据" + payId + "状态是否为已撤回";
                    return error;
                }
                bisPayInfo.setMidGroupStatic(midgroupstatic);
                bisPayInfo.setCodeNum(payId);
                bisPayList.add(bisPayInfo);
                log.info("撤回接口2222");
            }
        }

        /*
         * 自动过滤掉 选中的数据 且数据为 未上传
         * */
        List<BisCheckingBook> bisPaysArrayList = new ArrayList<>();
        for (BisCheckingBook bisPay : bisPayList) {
            if (STATU.equals(bisPay.getMidGroupStatic())) {
                bisPaysArrayList.add(bisPay);
            } else {
                continue;
            }
        }
        for (BisCheckingBook bisPay : bisPaysArrayList) {
            String payId = bisPay.getCodeNum();
            String billNum = checkingBookDao.getBillNum(payId);
            log.info("billNum "+billNum);
            if (null != bisPay.getCodeNum()) {
                log.info("撤回接口3333");
                //根据codeNum 获取应收对账单
                BisCheckingBook obj = checkingBookService.find("codeNum", bisPay.getCodeNum());
                // 如果结算单号为空说明不生成结算单，撤回走未结算单接口
                if(StringUtils.isEmpty(obj.getStatementNo())) {
                    String encrypt = AESUtils.encrypt(billNum, PASSWORD);
                    cn.hutool.json.JSONObject jsonObject = this.delete(encrypt);
                    //修改上传状态
                    Object code = jsonObject.get("code");
                    Integer codes = Integer.valueOf(code.toString());
                    if (200 == codes) {
                        //修改上传状态
                        checkingBookDao.updateStatusByPayId(bisPay.getCodeNum());
                        log.info("应收费用未生成结算单的已撤回：" + jsonObject);
                        return "success";
                    }
                }else {
                    log.info("撤回接口4444");
                    String statementNo = checkingBookDao.selectStatementNo(ids.get(0));
                    if (StringUtils.isEmpty(statementNo)) {
                        return "该单号的结算单号尚未保存到数据库";
                    }
                    log.info("撤回接口5555");
                    String encrypt = AESUtils.encrypt(billNum, PASSWORD);
                    String encryptNO = AESUtils.encrypt(statementNo, PASSWORD);
                    String bodys = HttpUtil.createPost("http://kubemaster.sdlandsea.net:30156/api/statement/deleteCostByStatementNo?statementNo=" + encryptNO)
                            .header("Content-Type", "application/json")
                            .header("APPID", "7e86aa901e86de01")
                            .header("Fmp-Tenant-Data-Node", "080013")
                            .execute()
                            .body();
                    log.info("撤回接口6666");
                    JSONObject jsonObject1 = JSONObject.parseObject(bodys);
                    log.info("应收撤回的响应:" + jsonObject1.toString());
                    String code1 = jsonObject1.get("code").toString();
                    if ("200".equals(code1)) {
//                        cn.hutool.json.JSONObject jsonObjectSe = this.delete(encrypt);
//                        String code2 = jsonObjectSe.get("code").toString();
                        //修改上传状态
//                        if(("200").equals(code2)){
                            log.info("撤回接口7777");
                            checkingBookDao.updateStatusByPayId(bisPay.getCodeNum());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            log.info(simpleDateFormat.format(new Date())+" 应收费用生成结算单的已撤回,编号:"+bisPay.getCodeNum()+";上传状态修改为:未上传; 结算单号:"+statementNo+"修改为空。");
                            return "success";
//                        }
//                        String msg1 = jsonObjectSe.get("msg").toString();
//                        if (!("200").equals(code1)) {
//                            error += msg1;
//                        }
//                        return error;

                    }
                    if(!"200".equals(code1)){
                        log.info("撤回接口8888");
                        String msg1 = jsonObject1.get("msg").toString();
                        return  msg1;
                    }

                }
            }
            log.info("应收付费用该数据没有税号为空");
        }
        return "error";
    }

    static cn.hutool.json.JSONObject delete(String encrypt) {

        String body = HttpUtil.createPost("http://kubemaster.sdlandsea.net:30156/api/cost/remove?origCostId=" + encrypt)
                .header("Content-Type", "application/json")
                .header("APPID", "7e86aa901e86de01")
                .header("Fmp-Tenant-Data-Node", "080013")
                .execute()
                .body();
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(body);
        return jsonObject;
    }
}
