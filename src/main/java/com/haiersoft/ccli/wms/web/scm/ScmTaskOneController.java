package com.haiersoft.ccli.wms.web.scm;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.system.entity.ScheduleJob;
import com.haiersoft.ccli.wms.service.scm.ScmDictService;
import net.sf.json.JSONArray;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统推送内容
 * 1.增量推送入库订单明细(0 0 1 * * ? )
 * 2.增量推送入库明细(0 30 1 * * ? )
 * 3.增量推送出库明细(0 0 2 * * ? )
 */
@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class ScmTaskOneController implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ScmTaskOneController.class);

    @Autowired
    private ScmDictService scmDictService;

    public static final String warehouseName = "青岛港怡之航冷链物流有限公司";//仓库企业全称
    public static final String warehouseCode = "91370220395949850B";//仓库企业统一社会信用代码
    public static final String key = "b32ecfb40b224b218bccb119701193d5";//平台分配的独立 API 请求密钥

    public static final String inboundOrderAddUrl = "https://apiplat.sdland-sea.com/scm/inboundOrderAdd";//增量推送入库订单明细

    public static List<ScmDict> scmDictAllList;

    @Override
    public void execute(JobExecutionContext context) {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        logger.info("任务名称 = [" + scheduleJob.getName() + "]" + " 在 " + dateFormat.format(new Date()) + " 时开始运行");
        scmTask();
        logger.info("任务名称 = [" + scheduleJob.getName() + "]" + " 在 " + dateFormat.format(new Date()) + " 时结束运行");
    }

    @Transactional
    public void scmTask() {
        try {
            selectScmDictData();
            sortData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //整理数据
    public void sortData() throws IOException, ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1); // 将日期往前推一天
        // 将Calendar对象转换为Date对象
        Date dateD = calendar.getTime();
        SimpleDateFormat startTemp = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endTemp = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String startTime = startTemp.format(dateD);
        String endTime = endTemp.format(dateD);

        startTime = "2024-11-01 00:00:00";
        endTime = "2024-11-02 00:00:00";

        //查询增量推送入库订单明细
        List<Map<String,Object>> inboundOrderAddList = new ArrayList<>();
        inboundOrderAddList = scmDictService.queryInboundOrderAddList(startTime,endTime);
        if (inboundOrderAddList!=null && inboundOrderAddList.size() > 0 && inboundOrderAddList.get(0)!=null){
            Map<String,String> scmDictAllMap = converDictListToMap(scmDictAllList);
            for (Map<String,Object> forMap:inboundOrderAddList) {
                forMap.put("isFutures","0");
                forMap.put("isFuturesLabel",scmDictAllMap.get("is_futures-0"));
//                forMap.put("businessType","XXXX");//String-业务类型，字典项编码-是-字典项：business_type。
                forMap.put("businessTypeLabel",scmDictAllMap.get("business_type-1"));//String-业务类型，字典项标签-是-字典项：business_type
//                forMap.put("tradeType","XXXX");//String-贸易类型，字典项编码-是-字典项：trade_type
                forMap.put("tradeTypeLabel",scmDictAllMap.get("trade_type-"+forMap.get("TRADETYPE")));//String-贸易类型，字典项标签-是-字典项：trade_type
//                forMap.put("tradeMode","XXXX");//String-贸易方式，字典项编码-是-字典项：trade_mode
                forMap.put("tradeModeLabel",scmDictAllMap.get("trade_mode-"+forMap.get("TRADEMODE")));//String-贸易方式，字典项标签-是-字典项：trade_mode
//                forMap.put("inboundType","XXXX");//String-入库类型，字典项编码-是-字典项：inbound_type
                forMap.put("inboundTypeLabel",scmDictAllMap.get("inbound_type-"+forMap.get("INBOUNDTYPE")));//String-入库类型，字典项标签-是-字典项：inbound_type
//                forMap.put("productType","XXXX");//String-货种，字典项编码-是-多个逗号分隔。字典项：product_type
                forMap.put("productTypeLabel",scmDictAllMap.get("product_type-QT"));//String-货种，字典项标签-是-多个逗号分隔。字典项：product_type
                forMap.put("countOneUnit","11");//String-数量一级单位，字典项编码-是-多个用 / 分隔。字典项：unit
                forMap.put("countOneUnitLabel",scmDictAllMap.get("unit-11"));//String-数量一级单位，字典项标签-是-多个用 / 分隔。字典项：unit
                forMap.put("countTwoUnit","11");//String-数量二级单位，字典项编码-是-多个用 / 分隔。字典项：unit
                forMap.put("countTwoUnitLabel",scmDictAllMap.get("unit-11"));//String-数量二级单位，字典项标签-是-多个用 / 分隔。字典项：unit
                forMap.put("weightUnit","35");//String-重量单位，字典项编码-是-多个用 / 分隔。字典项：unit
                forMap.put("weightUnitLabel",scmDictAllMap.get("unit-35"));//String-重量单位，字典项标签-是-多个用 / 分隔。字典项：unit
                forMap.put("volumeUnit","113");//String-体积单位，字典项编码-是-多个用 / 分隔。字典项：unit
                forMap.put("volumeUnitLabel",scmDictAllMap.get("unit-113"));//String-体积单位，字典项标签-是-多个用 / 分隔。字典项：unit
                forMap.put("lengthUnit","30");//String-长度单位，字典项编码-是-多个用 / 分隔。字典项：unit
                forMap.put("lengthUnitLabel",scmDictAllMap.get("unit-30"));//String-长度单位，字典项标签-是-多个用 / 分隔。字典项：unit
            }
        }else{
            inboundOrderAddList = new ArrayList<>();
        }

        //执行发送
        execute(inboundOrderAddList);

        return;
    }

    //执行发送
    public void execute(List<Map<String,Object>> inboundOrderAddList) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        //增量推送入库订单明细
        Map<String, Object> params1 = new HashMap<String, Object>();
        UUID uuid1 = UUID.randomUUID();
        String str1 = uuid1.toString().replace("-","");
        params1.put("requestId",str1);
        params1.put("requestDate",sdf.format(date));
        params1.put("warehouseName",warehouseName);
        params1.put("warehouseCode",warehouseCode);
        params1.put("key",key);
        params1.put("list",inboundOrderAddList);
        params1.put("data",inboundOrderAddList);
        logger.info("增量推送入库订单明细参数："+ JSON.toJSONString(params1));
        String bodys1 = HttpUtil.createPost(inboundOrderAddUrl)
                .body(JSON.toJSONString(params1))
                .execute()
                .body();
        JSONObject respones1 = JSONObject.parseObject(bodys1);
        logger.info("增量推送入库订单明细结果："+ respones1.toJSONString());
        if(0 != respones1.getIntValue("code")){
            logger.info("增量推送入库订单明细推送失败："+respones1.getString("msg"));
        }
    }

    //本地查询字典数据
    public void selectScmDictData() {
        List<ScmDict> scmDictList = new ArrayList<>();
        List<PropertyFilter> filters = new ArrayList<>();
        scmDictList = scmDictService.search(filters);
        scmDictAllList = new ArrayList<>();
        scmDictAllList.addAll(scmDictList);
    }

    public static Map<String,String> converDictListToMap(List<ScmDict> scmDictList){
        Map<String,String> map = new HashMap<>();
        for (ScmDict forScmDict:scmDictList) {
            map.put(forScmDict.getDictType()+"-"+forScmDict.getDictValue(),forScmDict.getDictLabel());
        }
        return map;
    }

//    public static void main(String[] args) {
//        String aa = "[\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"business_type\",\n" +
//                "\t\t\t\"dictName\": \"业务类型\",\n" +
//                "\t\t\t\"dictLabel\": \"出口\",\n" +
//                "\t\t\t\"dictValue\": \"3\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"business_type\",\n" +
//                "\t\t\t\"dictName\": \"业务类型\",\n" +
//                "\t\t\t\"dictLabel\": \"进口\",\n" +
//                "\t\t\t\"dictValue\": \"1\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"business_type\",\n" +
//                "\t\t\t\"dictName\": \"业务类型\",\n" +
//                "\t\t\t\"dictLabel\": \"国产\",\n" +
//                "\t\t\t\"dictValue\": \"2\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"inbound_type\",\n" +
//                "\t\t\t\"dictName\": \"入库类型\",\n" +
//                "\t\t\t\"dictLabel\": \"集装箱\",\n" +
//                "\t\t\t\"dictValue\": \"1\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"inbound_type\",\n" +
//                "\t\t\t\"dictName\": \"入库类型\",\n" +
//                "\t\t\t\"dictLabel\": \"散杂货\",\n" +
//                "\t\t\t\"dictValue\": \"2\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"inbound_type\",\n" +
//                "\t\t\t\"dictName\": \"入库类型\",\n" +
//                "\t\t\t\"dictLabel\": \"拼箱\",\n" +
//                "\t\t\t\"dictValue\": \"3\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"is_futures\",\n" +
//                "\t\t\t\"dictName\": \"是否期货\",\n" +
//                "\t\t\t\"dictLabel\": \"普货\",\n" +
//                "\t\t\t\"dictValue\": \"0\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"is_futures\",\n" +
//                "\t\t\t\"dictName\": \"是否期货\",\n" +
//                "\t\t\t\"dictLabel\": \"期货\",\n" +
//                "\t\t\t\"dictValue\": \"1\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_status\",\n" +
//                "\t\t\t\"dictName\": \"期货仓单状态\",\n" +
//                "\t\t\t\"dictLabel\": \"普货\",\n" +
//                "\t\t\t\"dictValue\": \"PH\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_status\",\n" +
//                "\t\t\t\"dictName\": \"期货仓单状态\",\n" +
//                "\t\t\t\"dictLabel\": \"期货\",\n" +
//                "\t\t\t\"dictValue\": \"QH\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_status\",\n" +
//                "\t\t\t\"dictName\": \"期货仓单状态\",\n" +
//                "\t\t\t\"dictLabel\": \"延伸\",\n" +
//                "\t\t\t\"dictValue\": \"YS\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_status\",\n" +
//                "\t\t\t\"dictName\": \"期货仓单状态\",\n" +
//                "\t\t\t\"dictLabel\": \"现货\",\n" +
//                "\t\t\t\"dictValue\": \"XH\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"进口橡胶\",\n" +
//                "\t\t\t\"dictValue\": \"XJ\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"进口纸浆\",\n" +
//                "\t\t\t\"dictValue\": \"ZJ\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"矿类\",\n" +
//                "\t\t\t\"dictValue\": \"KL\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"设备及其他\",\n" +
//                "\t\t\t\"dictValue\": \"SBJQT\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"原木\",\n" +
//                "\t\t\t\"dictValue\": \"YM\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"板材\",\n" +
//                "\t\t\t\"dictValue\": \"BC\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"塑料颗粒\",\n" +
//                "\t\t\t\"dictValue\": \"SLKL\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"期货天然橡胶\",\n" +
//                "\t\t\t\"dictValue\": \"QHTRXJ\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"期货20号胶\",\n" +
//                "\t\t\t\"dictValue\": \"QH20XJ\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"期货纸浆\",\n" +
//                "\t\t\t\"dictValue\": \"QHZJ\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"国产橡胶\",\n" +
//                "\t\t\t\"dictValue\": \"GCXJ\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"橡胶颗粒\",\n" +
//                "\t\t\t\"dictValue\": \"XJKL\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"牛皮纸\",\n" +
//                "\t\t\t\"dictValue\": \"NPZ\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"集中监管货种\",\n" +
//                "\t\t\t\"dictValue\": \"JZJGHZ\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"金属硅\",\n" +
//                "\t\t\t\"dictValue\": \"JSG\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"进口日化\",\n" +
//                "\t\t\t\"dictValue\": \"JKRH\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"进口化妆品\",\n" +
//                "\t\t\t\"dictValue\": \"JKHZP\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"日化用品\",\n" +
//                "\t\t\t\"dictValue\": \"RHYP\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"橡胶\",\n" +
//                "\t\t\t\"dictValue\": \"XJ1\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"煤炭\",\n" +
//                "\t\t\t\"dictValue\": \"MT\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"胶合板\",\n" +
//                "\t\t\t\"dictValue\": \"JHB\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"product_type\",\n" +
//                "\t\t\t\"dictName\": \"货种\",\n" +
//                "\t\t\t\"dictLabel\": \"其他\",\n" +
//                "\t\t\t\"dictValue\": \"QT\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"trade_mode\",\n" +
//                "\t\t\t\"dictName\": \"贸易方式\",\n" +
//                "\t\t\t\"dictLabel\": \"一般贸易\",\n" +
//                "\t\t\t\"dictValue\": \"1\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"trade_mode\",\n" +
//                "\t\t\t\"dictName\": \"贸易方式\",\n" +
//                "\t\t\t\"dictLabel\": \"来料加工\",\n" +
//                "\t\t\t\"dictValue\": \"2\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"trade_mode\",\n" +
//                "\t\t\t\"dictName\": \"贸易方式\",\n" +
//                "\t\t\t\"dictLabel\": \"进料贸易\",\n" +
//                "\t\t\t\"dictValue\": \"3\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"trade_mode\",\n" +
//                "\t\t\t\"dictName\": \"贸易方式\",\n" +
//                "\t\t\t\"dictLabel\": \"保税仓库货物\",\n" +
//                "\t\t\t\"dictValue\": \"4\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"trade_mode\",\n" +
//                "\t\t\t\"dictName\": \"贸易方式\",\n" +
//                "\t\t\t\"dictLabel\": \"保税间货物\",\n" +
//                "\t\t\t\"dictValue\": \"5\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"trade_mode\",\n" +
//                "\t\t\t\"dictName\": \"贸易方式\",\n" +
//                "\t\t\t\"dictLabel\": \"其他进出口免费\",\n" +
//                "\t\t\t\"dictValue\": \"6\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"trade_mode\",\n" +
//                "\t\t\t\"dictName\": \"贸易方式\",\n" +
//                "\t\t\t\"dictLabel\": \"货样广告品\",\n" +
//                "\t\t\t\"dictValue\": \"7\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"trade_mode\",\n" +
//                "\t\t\t\"dictName\": \"贸易方式\",\n" +
//                "\t\t\t\"dictLabel\": \"直接退运\",\n" +
//                "\t\t\t\"dictValue\": \"8\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"trade_type\",\n" +
//                "\t\t\t\"dictName\": \"贸易类型\",\n" +
//                "\t\t\t\"dictLabel\": \"保税\",\n" +
//                "\t\t\t\"dictValue\": \"BS\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"trade_type\",\n" +
//                "\t\t\t\"dictName\": \"贸易类型\",\n" +
//                "\t\t\t\"dictLabel\": \"非保税\",\n" +
//                "\t\t\t\"dictValue\": \"FBS\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"件\",\n" +
//                "\t\t\t\"dictValue\": \"11\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"托\",\n" +
//                "\t\t\t\"dictValue\": \"tuo\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"包\",\n" +
//                "\t\t\t\"dictValue\": \"111\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"卷\",\n" +
//                "\t\t\t\"dictValue\": \"114\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"块\",\n" +
//                "\t\t\t\"dictValue\": \"123\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"个\",\n" +
//                "\t\t\t\"dictValue\": \"7\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"吨\",\n" +
//                "\t\t\t\"dictValue\": \"112\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"立方米\",\n" +
//                "\t\t\t\"dictValue\": \"113\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"米\",\n" +
//                "\t\t\t\"dictValue\": \"30\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"台\",\n" +
//                "\t\t\t\"dictValue\": \"1\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"条\",\n" +
//                "\t\t\t\"dictValue\": \"15\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"双\",\n" +
//                "\t\t\t\"dictValue\": \"25\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"箱\",\n" +
//                "\t\t\t\"dictValue\": \"xiang\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"次\",\n" +
//                "\t\t\t\"dictValue\": \"ci\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"架\",\n" +
//                "\t\t\t\"dictValue\": \"5\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"片\",\n" +
//                "\t\t\t\"dictValue\": \"pian\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"艘\",\n" +
//                "\t\t\t\"dictValue\": \"4\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"幅\",\n" +
//                "\t\t\t\"dictValue\": \"23\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"座\",\n" +
//                "\t\t\t\"dictValue\": \"2\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"副\",\n" +
//                "\t\t\t\"dictValue\": \"19\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"根\",\n" +
//                "\t\t\t\"dictValue\": \"14\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"平方米\",\n" +
//                "\t\t\t\"dictValue\": \"32\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"千瓦时\",\n" +
//                "\t\t\t\"dictValue\": \"62\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"升\",\n" +
//                "\t\t\t\"dictValue\": \"95\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"千升\",\n" +
//                "\t\t\t\"dictValue\": \"63\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"支\",\n" +
//                "\t\t\t\"dictValue\": \"12\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"把\",\n" +
//                "\t\t\t\"dictValue\": \"16\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"株\",\n" +
//                "\t\t\t\"dictValue\": \"28\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"垛\",\n" +
//                "\t\t\t\"dictValue\": \"duo\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"车\",\n" +
//                "\t\t\t\"dictValue\": \"che\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"票\",\n" +
//                "\t\t\t\"dictValue\": \"piao\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"辆\",\n" +
//                "\t\t\t\"dictValue\": \"3\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"套\",\n" +
//                "\t\t\t\"dictValue\": \"6\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"只\",\n" +
//                "\t\t\t\"dictValue\": \"8\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"克\",\n" +
//                "\t\t\t\"dictValue\": \"36\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"克拉\",\n" +
//                "\t\t\t\"dictValue\": \"84\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"千克\",\n" +
//                "\t\t\t\"dictValue\": \"35\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"小时\",\n" +
//                "\t\t\t\"dictValue\": \"XS\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"万吨\",\n" +
//                "\t\t\t\"dictValue\": \"WD\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"万TEU\",\n" +
//                "\t\t\t\"dictValue\": \"WTEU\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"袋\",\n" +
//                "\t\t\t\"dictValue\": \"dai\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \"~\",\n" +
//                "\t\t\t\"dictValue\": \"100\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"dictType\": \"unit\",\n" +
//                "\t\t\t\"dictName\": \"计量单位\",\n" +
//                "\t\t\t\"dictLabel\": \" \",\n" +
//                "\t\t\t\"dictValue\": \"101\"\n" +
//                "\t\t}\n" +
//                "\t]";
//        List<ScmDict> scmDictList = new ArrayList<>();
//        scmDictList = (List<ScmDict>)JSON.parseArray(aa,ScmDict.class);
//        System.out.println(JSON.toJSONString(scmDictList));
//        StringBuffer sb = new StringBuffer();
//        int id = 300;
//        for (ScmDict forScmDict:scmDictList) {
//            sb.append(" insert into BASE_SCM_DICT (DICT_TYPE,DICT_TYPE_NAME,DICT_LABEL,DICT_VALUE) " +
//                    "values ('"+forScmDict.getDictType()+"','"+forScmDict.getDictName()+"','"+forScmDict.getDictLabel()+"','"+forScmDict.getDictValue()+"'); ");
//            id++;
//        }
//        System.out.println(sb.toString());
//    }

}
