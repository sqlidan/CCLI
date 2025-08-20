package com.haiersoft.ccli.wms.web.scm;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.system.entity.ScheduleJob;
import com.haiersoft.ccli.wms.service.scm.ScmDictService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
public class ScmTaskTwoController implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ScmTaskThirdController.class);

    @Autowired
    private ScmDictService scmDictService;

    public static final String warehouseName = "青岛港怡之航冷链物流有限公司";//仓库企业全称
    public static final String warehouseCode = "91370220395949850B";//仓库企业统一社会信用代码
    public static final String key = "b32ecfb40b224b218bccb119701193d5";//平台分配的独立 API 请求密钥

    public static final String inboundAddUrl = "https://apiplat.sdland-sea.com/scm/inboundAdd";//增量推送入库明细

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

        //查询增量推送入库明细
        List<Map<String,Object>> inboundAddList = new ArrayList<>();
        inboundAddList = scmDictService.queryInboundAddList(startTime,endTime);
        if (inboundAddList!=null && inboundAddList.size() > 0 && inboundAddList.get(0)!=null) {
            Map<String,String> scmDictAllMap = converDictListToMap(scmDictAllList);
            for (Map<String,Object> forMap:inboundAddList) {
                forMap.put("isFutures","0");
                forMap.put("isFuturesLabel",scmDictAllMap.get("is_futures-0"));
//                forMap.put("businessType","XXXX");//String-业务类型，字典项编码-是-字典项：business_type。
                forMap.put("businessTypeLabel",scmDictAllMap.get("business_type-1"));//String-业务类型，字典项标签-是-字典项：business_type
//                forMap.put("tradeType","XXXX");//String-贸易类型，字典项编码-是-字典项：trade_type
                forMap.put("tradeTypeLabel",scmDictAllMap.get("trade_type-"+forMap.get("tradeType")));//String-贸易类型，字典项标签-是-字典项：trade_type
//                forMap.put("tradeMode","XXXX");//String-贸易方式，字典项编码-是-字典项：trade_mode
                forMap.put("tradeModeLabel",scmDictAllMap.get("trade_mode-"+forMap.get("tradeMode")));//String-贸易方式，字典项标签-是-字典项：trade_mode
//                forMap.put("inboundType","XXXX");//String-入库类型，字典项编码-是-字典项：inbound_type
                forMap.put("inboundTypeLabel",scmDictAllMap.get("inbound_type-"+forMap.get("inboundType")));//String-入库类型，字典项标签-是-字典项：inbound_type
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
            inboundAddList = new ArrayList<>();
        }

        //执行发送
        execute(inboundAddList);

        return;
    }

    //执行发送
    public void execute(List<Map<String,Object>> inboundAddList) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        //增量推送入库明细
        Map<String, Object> params2 = new HashMap<String, Object>();
        UUID uuid2 = UUID.randomUUID();
        String str2 = uuid2.toString().replace("-", "");
        params2.put("requestId", str2);
        params2.put("requestDate", sdf.format(date));
        params2.put("warehouseName", warehouseName);
        params2.put("warehouseCode", warehouseCode);
        params2.put("key", key);
        params2.put("data", inboundAddList);
        logger.info("增量推送入库明细参数：" + JSON.toJSONString(params2));
        String bodys2 = HttpUtil.createPost(inboundAddUrl)
                .body(JSON.toJSONString(params2))
                .execute()
                .body();
        JSONObject respones2 = JSONObject.parseObject(bodys2);
        logger.info("增量推送入库明细结果：" + respones2.toJSONString());
        if (200 != respones2.getIntValue("code")) {
            logger.info("增量推送入库明细推送失败：" + respones2.getString("msg"));
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


}
