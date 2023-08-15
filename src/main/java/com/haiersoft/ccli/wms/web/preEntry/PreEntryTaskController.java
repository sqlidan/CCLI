package com.haiersoft.ccli.wms.web.preEntry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.bounded.service.BaseBoundedService;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.supervision.service.GetKeyService;
import com.haiersoft.ccli.system.entity.ScheduleJob;
import com.haiersoft.ccli.wms.entity.BisCustomsClearance;
import com.haiersoft.ccli.wms.entity.BisCustomsClearanceInfo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.service.CustomsClearanceInfoService;
import com.haiersoft.ccli.wms.service.CustomsClearanceService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.json.XML;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 执行核注清单回执
 */
@Service
@DisallowConcurrentExecution
public class PreEntryTaskController implements Job {

    private static final Logger logger =  LoggerFactory.getLogger(PreEntryTaskController.class);

    @Autowired
    private PreEntryService preEntryService;
    @Autowired
    GetKeyService getKeyService;
    @Autowired
    private CustomsClearanceService customsClearanceService;
    @Autowired
    private CustomsClearanceInfoService customsClearanceInfoService;
    @Autowired
    private BaseBoundedService baseBoundedService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        getHZQDPort2HZ();
        System.out.println("任务名称 = [" + scheduleJob.getName() + "]" + " 在 " + dateFormat.format(new Date()) + " 时运行");
    }

    public static final String FTP_RECEIVE_PATH = "/Receive/";//FTP回执文件存放路径
    public static final String LOCAL_PATH = "D:/HuiZhi/";//本地文件临时存放位置
    public static final String FTP_TARGET_PATH = "/BAK/Receive/";//FTP回执文件备份路径

    //核注清单回执
    @Transactional
    public void getHZQDPort2HZ() {
        try {
//            localFileList();
            ftpFileList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    //本地测试执行回执
//    public void localFileList() throws IOException, ParseException {
//        File fileOrg = new File("C:\\Users\\Administrator\\Desktop\\冷链关务\\I425823B0000029830");
//        File[] fileList = fileOrg.listFiles();
//        // 获取路径下所有的回执文件
//        if (fileList.length > 0) {
//            //第一步，先处理海关分中心回执，确定预录入编号和单一回执的关系
//            for (File file : fileList) {
//                String fileName = file.getName();
//                String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
//                if (!"COPSASX".equals(substring)) {
//                    continue;
//                }else {
//                    if (fileName.contains("Successed_") || fileName.contains("Failed_")) {
//                        String SeqNo = null;
//                        String EtpsPreentNo = null;
//                        String xml = "";
//                        InputStream in = new FileInputStream(file);
//                        InputStreamReader read = new InputStreamReader(in, "GBK");
//                        BufferedReader reader = new BufferedReader(read);
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            xml += line;
//                        }
//                        read.close();
//                        org.json.JSONObject jsonObject = XML.toJSONObject(xml);
//                        if (jsonObject.get("CommonResponeMessage") != null) {
//                            JSONObject commonResponeMessage = JSONObject.parseObject(jsonObject.get("CommonResponeMessage").toString());
//                            if(commonResponeMessage.get("SeqNo")!=null && commonResponeMessage.get("SeqNo").toString().trim().length() > 0){
//                                SeqNo = commonResponeMessage.get("SeqNo").toString();
//                            }
//                            if(commonResponeMessage.get("EtpsPreentNo")!=null && commonResponeMessage.get("EtpsPreentNo").toString().trim().length() > 0) {
//                                EtpsPreentNo = commonResponeMessage.get("EtpsPreentNo").toString();
//                            }
//                            if(EtpsPreentNo != null) {//分中心返回
//                                //执行保存内部编号
//                                List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
//                                PropertyFilter filter = new PropertyFilter("EQS_seqNo", EtpsPreentNo);
//                                filters.add(filter);
//                                List<BisPreEntry> bisPreEntryList = preEntryService.search(filters);
//                                if(bisPreEntryList != null && bisPreEntryList.size() == 1){
//                                    for (BisPreEntry forBisPreEntry:bisPreEntryList) {
//                                        if(fileName.contains("Successed_")){//分中心返回成功
//                                            forBisPreEntry.setEtpsInnerInvtNo(SeqNo);//海关分中心内部编号
//                                        }
//                                        if(commonResponeMessage.get("CheckInfo")!=null && commonResponeMessage.get("CheckInfo").toString().trim().length() > 0){
//                                            forBisPreEntry.setRemark(commonResponeMessage.get("CheckInfo").toString());//海关分中心备注
//                                        }
//                                        forBisPreEntry.setUpdateBy("SYSTEM");
//                                        forBisPreEntry.setUpdateTime(new Date());
//                                        preEntryService.merge(forBisPreEntry);
//                                    }
//                                }
//                            }
//                            if(EtpsPreentNo != null && EtpsPreentNo.trim().length() >0) {
//                                //将文件挪放至日期下的预录入编号文件夹中
//                                copyFile(EtpsPreentNo, fileName, file, null);
//                            }
//                        }
//                    }
//                }
//            }
//            //第二步，处理单一窗口返回的回执
//            for (File file : fileList) {
//                String fileName = file.getName();
//                String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
//                if (!"COPSASX".equals(substring)) {
//                    continue;
//                }else {
//                    if (fileName.contains("Receipt")) {//单一窗口返回回执
//                        //对应保存分中心生成的内部编号
//                        String xml = "";
//                        InputStream in = new FileInputStream(file);
//                        InputStreamReader read = new InputStreamReader(in, "GBK");
//                        BufferedReader reader = new BufferedReader(read);
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            xml += line;
//                        }
//                        read.close();
//                        if (StringUtils.isNotBlank(xml)) {
//                            execute(xml,fileName,file,null);
//                        }
//                    }else {
//                        continue;
//                    }
//                }
//            }
//        }
//    }

    //FTP执行回执
    public void ftpFileList() throws IOException, ParseException {
        PreEntryFTPUtils preEntryFTPUtils = PreEntryFTPUtils.getInstance();
        // 获取路径下所有的回执文件
        FTPFile[] fileList = preEntryFTPUtils.getFilesList(FTP_RECEIVE_PATH);


        //获取路径下所有的回执文件
        if (fileList.length > 0) {
//            //或用不到
//            for (FTPFile ftpFile : fileList) {
//                String fileName = ftpFile.getName();
//                String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
//                if (!"COPSASX".equals(substring)) {
//                    continue;
//                } else {
//                    if (fileName.contains("Successed_") || fileName.contains("Failed_")) {
//                        String SeqNo = null;
//                        String EtpsPreentNo = null;
//                        //对应保存分中心生成的内部编号
//                        String xml = "";
//                        InputStream in = preEntryFTPUtils.retrieveFile(FTP_RECEIVE_PATH, fileName);
//                        InputStreamReader read = new InputStreamReader(in, "GBK");
//                        BufferedReader reader = new BufferedReader(read);
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            xml += line;
//                        }
//                        read.close();
//                        org.json.JSONObject jsonObject = XML.toJSONObject(xml);
//                        if (jsonObject.get("CommonResponeMessage") != null) {
//                            JSONObject commonResponeMessage = JSONObject.parseObject(jsonObject.get("CommonResponeMessage").toString());
//                            if(commonResponeMessage.get("EtpsPreentNo")!=null && commonResponeMessage.get("EtpsPreentNo").toString().trim().length() > 0) {
//                                EtpsPreentNo = commonResponeMessage.get("EtpsPreentNo").toString();
//                            }
////                            if(commonResponeMessage.get("SeqNo")!=null && commonResponeMessage.get("SeqNo").toString().trim().length() > 0) {
////                                SeqNo = commonResponeMessage.get("SeqNo").toString();
////                                //执行保存内部编号
////                                List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
////                                PropertyFilter filter = new PropertyFilter("EQS_seqNo", SeqNo);
////                                filters.add(filter);
////                                List<BisPreEntry> bisPreEntryList = preEntryService.search(filters);
////                                if (bisPreEntryList != null && bisPreEntryList.size() == 1) {
////                                    for (BisPreEntry forBisPreEntry : bisPreEntryList) {
////                                        if (commonResponeMessage.get("CheckInfo") != null && commonResponeMessage.get("CheckInfo").toString().trim().length() > 0) {
////                                            forBisPreEntry.setRemark(commonResponeMessage.get("CheckInfo").toString());//海关分中心备注
////                                        }
////                                        forBisPreEntry.setUpdateBy("SYSTEM");
////                                        forBisPreEntry.setUpdateTime(new Date());
////                                        preEntryService.merge(forBisPreEntry);
////                                    }
////                                }
////                            }
//                            if(EtpsPreentNo != null && EtpsPreentNo.trim().length() >0) {
//                                //将文件挪放至日期下的预录入编号文件夹中
//                                copyFile(EtpsPreentNo, fileName, null, ftpFile);
//                            }
//                        }
//                    }
//                }
//            }
            for (FTPFile ftpFile : fileList) {
                String fileName = ftpFile.getName();
                String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (!"COPSASX".equals(substring)) {
                    System.out.println("fileName1 ==> "+fileName);
                    continue;
                } else {
                    System.out.println("fileName2 ==> "+fileName);
                    if (fileName.contains("Receipt")) {//单一窗口返回回执
                        //对应保存分中心生成的内部编号
                        String xml = "";
                        InputStream in = preEntryFTPUtils.retrieveFile(FTP_RECEIVE_PATH, fileName);
                        InputStreamReader read = new InputStreamReader(in, "GBK");
                        BufferedReader reader = new BufferedReader(read);
                        String line;
                        while ((line = reader.readLine()) != null) {
                            xml += line;
                        }
                        read.close();
                        if (StringUtils.isNotBlank(xml)) {
                            execute(xml, fileName, null, ftpFile);
                        }
                    } else {
                        continue;
                    }
                }
            }
        } else {
            return;
        }
    }

    //解析数据执行业务逻辑
    public void execute(String xml, String fileName, File file, FTPFile ftpFile) throws ParseException, IOException {
        //获取回执数据
        org.json.JSONObject jsonObject = XML.toJSONObject(xml);
        if (jsonObject.get("Package") != null) {
            JSONObject Package = JSONObject.parseObject(jsonObject.get("Package").toString());

            String xmlns = Package.get("xmlns").toString();
            JSONObject EnvelopInfo = JSONObject.parseObject(Package.get("EnvelopInfo").toString());
            JSONObject DataInfo = JSONObject.parseObject(Package.get("DataInfo").toString());

            JSONObject PocketInfo = JSONObject.parseObject(DataInfo.get("PocketInfo").toString());
            JSONObject BussinessData = JSONObject.parseObject(DataInfo.get("BussinessData").toString());

            String etpsPreentNo = null;
            //区分校验回执
            if (BussinessData.get("INV201") != null) {//核注清单回执
                JSONObject INV201 = JSONObject.parseObject(BussinessData.get("INV201").toString());
                //清单审批回执
                if (INV201.get("HdeApprResult") != null) {
                    JSONObject HdeApprResult = JSONObject.parseObject(INV201.get("HdeApprResult").toString());
                    Map<String, Object> hdeApprResultMap = JSON.parseObject(HdeApprResult.toString());
                    if(hdeApprResultMap.get("etpsPreentNo") != null || hdeApprResultMap.get("etpsPreentNo").toString().trim().length() > 0){
                        etpsPreentNo = hdeApprResultMap.get("etpsPreentNo").toString().trim();
                    }

                    List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
                    PropertyFilter filter = new PropertyFilter("EQS_seqNo", hdeApprResultMap.get("etpsPreentNo")==null?"":hdeApprResultMap.get("etpsPreentNo").toString());
                    filters.add(filter);
                    List<BisPreEntry> bisPreEntryList = preEntryService.search(filters);
                    if (bisPreEntryList != null && bisPreEntryList.size() == 1) {
                        for (BisPreEntry forBisPreEntry : bisPreEntryList) {
                            if ("1".equals(hdeApprResultMap.get("manageResult")==null?"":hdeApprResultMap.get("manageResult").toString())) {
                                //反写核注清单号
                                forBisPreEntry.setState("5");//状态修改为5
                                forBisPreEntry.setCheckListNo(hdeApprResultMap.get("businessId")==null?"未获取到核注清单号":hdeApprResultMap.get("businessId").toString());
                                forBisPreEntry.setUpdateBy("SYSTEM");
                                forBisPreEntry.setUpdateTime(new Date());
                                preEntryService.merge(forBisPreEntry);

                                BisPreEntry bisPreEntry = new BisPreEntry();
                                bisPreEntry = forBisPreEntry;

                                //清单基本 1-通过（已核扣）；5-通过（未核扣）
                                if (INV201.get("BondInvtBsc")!=null && INV201.get("BondInvtBsc").toString().trim().length() > 0) {
                                    JSONObject BondInvtBsc = JSONObject.parseObject(INV201.get("BondInvtBsc").toString());
                                    Map<String, Object> bondInvtBscMap = JSON.parseObject(BondInvtBsc.toString());

                                    //通过，插入台账，修改底账
                                    //业务单号
                                    String userCode = "YZH";
                                    if (bisPreEntry.getCreateBy() != null) {
                                        List<Map<String, Object>> userList = preEntryService.getUserByName(bisPreEntry.getCreateBy());
                                        if (userList != null && userList.size() > 0) {
                                            userCode = userList.get(0).get("USER_CODE") == null ? "YZH" : userList.get(0).get("USER_CODE").toString();
                                        }
                                    }
                                    //判断用户码是否为空
                                    if (StringUtils.isNull(userCode)) {
                                        userCode = "YZH";
                                    } else {//判断用户码 的长度
                                        if (userCode.length() > 3) {
                                            userCode = userCode.substring(0, 3);
                                        } else if (userCode.length() < 3) {
                                            userCode = StringUtils.lpadStringLeft(3, userCode);
                                        }
                                    }
                                    String linkId = "F" + userCode + StringUtils.timeToString();
                                    //添加台账
                                    BisCustomsClearance bisCustoms = new BisCustomsClearance();
                                    bisCustoms.setCdNum(linkId);//业务单号
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    if(bondInvtBscMap.get("invtDclTime") != null && bondInvtBscMap.get("invtDclTime").toString().trim().length() > 0){
                                        Date declareTime = null;
                                        try {
                                            declareTime = sdf.parse(bondInvtBscMap.get("invtDclTime").toString());
                                        }catch (Exception e){
                                            logger.error(e.getMessage());
                                        }
                                        bisCustoms.setDeclareTime(declareTime);//申报日期
                                    }
                                    bisCustoms.setBillNum(bisPreEntry.getBillNum() == null ? "" : bisPreEntry.getBillNum().toString().trim());//提单号
                                    //服务项目
                                    if (bondInvtBscMap.get("impexpMarkcd") == null) {
                                        bisCustoms.setServiceProject("0");//报进
                                    } else {
                                        if ("I".equals(bondInvtBscMap.get("impexpMarkcd").toString())) {//进口
                                            bisCustoms.setServiceProject("0");//报进
                                        } else if ("E".equals(bondInvtBscMap.get("impexpMarkcd").toString())) {
                                            bisCustoms.setServiceProject("1");//报出
                                        }
                                    }
                                    bisCustoms.setConsignee(bisPreEntry.getConsignee() == null ? "" : bisPreEntry.getConsignee().toString().trim());//收货人
                                    bisCustoms.setConsignor(bisPreEntry.getConsignor() == null ? "" : bisPreEntry.getConsignor().toString().trim());//发货人
                                    bisCustoms.setUseUnit(bisPreEntry.getConsignee() == null ? bondInvtBscMap.get("rcvgdEtpsNm") == null ? "" : bondInvtBscMap.get("rcvgdEtpsNm").toString() : bisPreEntry.getConsignee().toString().trim());//消费者使用单位/收货人/收货企业名称
                                    bisCustoms.setModeTrade("0");//贸易方式
                                    bisCustoms.setStoragePlace("青岛港怡之航冷链物流有限公司");//存放地点
                                    bisCustoms.setPortEntry("黄岛");//入境口岸
                                    if (bondInvtBscMap.get("stshipTrsarvNatcd") != null && bondInvtBscMap.get("stshipTrsarvNatcd").toString().trim().length() > 0) {
                                        //起运国，从字典中依据编号转成名称
                                        List<BisPreEntryDictData> bisPreEntryDictDataList = new ArrayList<>();
                                        bisPreEntryDictDataList = preEntryService.getDictDataByCode("CUS_STSHIP_TRSARV_NATCD");
                                        for (BisPreEntryDictData forBisPreEntryDictData : bisPreEntryDictDataList) {
                                            if (forBisPreEntryDictData.getValue().equals(bondInvtBscMap.get("stshipTrsarvNatcd").toString()) || forBisPreEntryDictData.getLabel().equals(bondInvtBscMap.get("stshipTrsarvNatcd").toString())) {
                                                bisCustoms.setContryDeparture(forBisPreEntryDictData.getLabel());//起运国
                                                break;
                                            }
                                        }
                                    } else {
                                        bisCustoms.setContryDeparture(null);//起运国
                                    }
                                    bisCustoms.setCustomsDeclarationNumber(bondInvtBscMap.get("entryNo") == null ? "" : bondInvtBscMap.get("entryNo").toString());//报关单号
                                    bisCustoms.setContryOragin(bisPreEntry.getContryOragin() == null ? "" : bisPreEntry.getContryOragin().toString().trim());//原产国
                                    bisCustoms.setAuditingState("3");//审核通过
                                    bisCustoms.setClientId(bisPreEntry.getClientId() == null ? "" : bisPreEntry.getClientId().toString().trim());//客户ID
                                    bisCustoms.setClientName(bisPreEntry.getClientName() == null ? "" : bisPreEntry.getClientName().toString().trim());//客户名称
                                    bisCustoms.setCargoClientId(bondInvtBscMap.get("rcvgdEtpsno") == null ? "" : bondInvtBscMap.get("rcvgdEtpsno").toString());
                                    bisCustoms.setCargoClientName(bisPreEntry.getConsignee() == null ? bondInvtBscMap.get("rcvgdEtpsNm") == null ? "" : bondInvtBscMap.get("rcvgdEtpsNm").toString() : bisPreEntry.getConsignee().toString().trim());//货权方名称/消费者使用单位/收货人/收货企业名称
                                    bisCustoms.setComments(bondInvtBscMap.get("rmk") == null ? "" : bondInvtBscMap.get("rmk").toString());//备注
                                    bisCustoms.setOperator("SYSTEM");//创建人
                                    bisCustoms.setOperateTime(new Date());//创建事件
                                    bisCustoms.setExaminePerson("SYSTEM");//审核人
                                    bisCustoms.setExamineTime(new Date());//创建事件
                                    customsClearanceService.save(bisCustoms);

                                    if (INV201.get("BondInvtDt")!=null && INV201.get("BondInvtDt").toString().trim().length() > 0) {
                                        //币制，从字典中依据编号转成名称
                                        List<BisPreEntryDictData> bisPreEntryDictDataList = new ArrayList<>();
                                        bisPreEntryDictDataList = preEntryService.getDictDataByCode("CUS_DCLCURRCD");
                                        //清单明细
                                        List<JSONObject> BondInvtDt = JSONArray.parseArray(INV201.get("BondInvtDt").toString(), JSONObject.class);
                                        for (JSONObject forJSONObject : BondInvtDt) {
                                            //添加通关台账主信息
                                            if(forJSONObject == null){
                                                continue;
                                            }
                                            Map<String, Object> bondInvtDtMap = JSON.parseObject(forJSONObject.toString());
                                            //添加台账明细
                                            BisCustomsClearanceInfo bisCustomsClearanceInfo = new BisCustomsClearanceInfo();
                                            bisCustomsClearanceInfo.setCusId(linkId);//业务ID
                                            bisCustomsClearanceInfo.setCommodityCode(bondInvtDtMap.get("gdecd") == null ? "" : bondInvtDtMap.get("gdecd").toString().trim());//商品编码
                                            bisCustomsClearanceInfo.setCommodityName(bondInvtDtMap.get("gdsNm") == null ? "" : bondInvtDtMap.get("gdsNm").toString().trim());//商品名称
                                            bisCustomsClearanceInfo.setLatinName("");//拉丁文名
                                            bisCustomsClearanceInfo.setSpecification(bondInvtDtMap.get("gdsSpcfModelDesc") == null ? "" : bondInvtDtMap.get("gdsSpcfModelDesc").toString().trim());//规格
                                            bisCustomsClearanceInfo.setNum(BigDecimal.valueOf(Double.parseDouble(bondInvtDtMap.get("dclQty") == null ? "0" : bondInvtDtMap.get("dclQty").toString().trim().length() == 0?"0":bondInvtDtMap.get("dclQty").toString().trim())));//数量
                                            bisCustomsClearanceInfo.setNetWeight(BigDecimal.valueOf(Double.parseDouble(bondInvtDtMap.get("netWt") == null ? "0" : bondInvtDtMap.get("netWt").toString().trim().length() == 0?"0":bondInvtDtMap.get("netWt").toString().trim())));//净重
                                            bisCustomsClearanceInfo.setGrossWeight(BigDecimal.valueOf(Double.parseDouble(bondInvtDtMap.get("grossWt") == null ? "0" : bondInvtDtMap.get("grossWt").toString().trim().length() == 0?"0":bondInvtDtMap.get("grossWt").toString().trim())));//毛重
                                            if(bondInvtDtMap.get("usdStatTotalAmt") != null && bondInvtDtMap.get("usdStatTotalAmt").toString().length() > 0){
                                                String usdStatTotalAmtS = bondInvtDtMap.get("usdStatTotalAmt").toString();
                                                Double usdStatTotalAmtD = Double.parseDouble(usdStatTotalAmtS);
                                                BigDecimal usdStatTotalAmtB = BigDecimal.valueOf(usdStatTotalAmtD);
                                                bisCustomsClearanceInfo.setMoney(usdStatTotalAmtB);//金额
                                            }else{
                                                bisCustomsClearanceInfo.setMoney(new BigDecimal(0));//金额
                                            }
                                            if (bondInvtDtMap.get("dclCurrcd") != null && bondInvtDtMap.get("dclCurrcd").toString().trim().length() > 0) {
                                                for (BisPreEntryDictData forBisPreEntryDictData : bisPreEntryDictDataList) {
                                                    if (forBisPreEntryDictData.getValue().equals(bondInvtDtMap.get("dclCurrcd").toString()) || forBisPreEntryDictData.getLabel().equals(bondInvtDtMap.get("dclCurrcd").toString())) {
                                                        bisCustomsClearanceInfo.setCurrencyValue(forBisPreEntryDictData.getLabel());//币制
                                                        break;
                                                    }
                                                }
                                            }
                                            if (bondInvtDtMap.get("putrecSeqno") != null && bondInvtDtMap.get("putrecSeqno").toString().trim().length() > 0) {
                                                bisCustomsClearanceInfo.setAccountBook(bondInvtDtMap.get("putrecSeqno").toString());//账册商品序号
                                            }
                                            bisCustomsClearanceInfo.setFirmName("");//生产企业名称及注册号
                                            bisCustomsClearanceInfo.setPackagedForm("");//包装形式
                                            bisCustomsClearanceInfo.setIfWoodenPacking("");//有无木质包装
                                            bisCustomsClearanceInfo.setWoodenNo("");//木托编号
                                            customsClearanceInfoService.save(bisCustomsClearanceInfo);

                                            //通关底账
                                            if ("I".equals(bondInvtBscMap.get("impexpMarkcd").toString())) {//进口(报进)
                                                try {
                                                    //只需要添加一条底账信息即可
                                                    BaseBounded bounded = new BaseBounded();
                                                    bounded.setClientId(bisCustoms.getClientId());//客户ID
                                                    bounded.setClientName(bisCustoms.getClientName());//客户名称
                                                    bounded.setBillNum(bisCustoms.getBillNum());//提单号
                                                    bounded.setCdNum(bisCustoms.getCustomsDeclarationNumber());//报关单号
                                                    bounded.setCtnNum(null);//MR/集装箱号
                                                    bounded.setItemName(null);//货物描述
                                                    bounded.setStorageDate(null);//入库时间
                                                    bounded.setPiece(0);//件数
                                                    bounded.setNetWeight(0.00);//总净值
                                                    bounded.setCustomerServiceName(bisPreEntry.getCustomerService() == null ? "" : bisPreEntry.getCustomerService().toString().trim());//所属客服
                                                    bounded.setHsCode(bisPreEntry.getHsNo() == null ? "" : bisPreEntry.getHsNo().toString().trim());//HS编码
                                                    bounded.setHsItemname(bisCustomsClearanceInfo.getCommodityName());//海关品名
                                                    bounded.setAccountBook(bisCustomsClearanceInfo.getAccountBook());//账册商品序号
                                                    bounded.setHsQty(Double.parseDouble(bisCustomsClearanceInfo.getGrossWeight().toString()));//海关库存重量
                                                    bounded.setTypeSize(null);//规格
                                                    bounded.setCargoLocation(null);//库位
                                                    bounded.setCreatedTime(new Date());//创建时间
                                                    bounded.setUpdatedTime(new Date());//修改时间
                                                    bounded.setCargoArea(null);//库区
                                                    bounded.setDclQty(Double.parseDouble(bondInvtDtMap.get("dclQty") == null ? "0" : bondInvtDtMap.get("dclQty").toString().trim().length() == 0?"0":bondInvtDtMap.get("dclQty").toString().trim()));//申报重量
                                                    bounded.setDclUnit(bondInvtDtMap.get("dclUnitcd") == null ? "" : bondInvtDtMap.get("dclUnitcd").toString());//申报计量单位
                                                    logger.info("BASE_BOUNDED添加通关底账信息==> "+JSON.toJSONString(bounded));
                                                    baseBoundedService.save(bounded);
                                                    logger.info("BASE_BOUNDED添加通关底账信息成功");
                                                }catch (Exception e){
                                                    logger.error("BASE_BOUNDED添加通关底账信息失败==> "+e.getMessage());
                                                }
                                            } else if ("E".equals(bondInvtBscMap.get("impexpMarkcd").toString())) {//出口(报出)
                                                //修改原底账信息中的数量字段
                                                if (bondInvtDtMap.get("putrecSeqno") != null && bondInvtDtMap.get("putrecSeqno").toString().trim().length() > 0) {
                                                    List<Map<String,Object>> boundedListMap = preEntryService.findOneBaseBounded(bondInvtDtMap.get("putrecSeqno").toString().trim());
                                                    if (boundedListMap != null && boundedListMap.size() == 1) {
                                                        try {
                                                            BaseBounded bounded = new BaseBounded();
                                                            bounded = baseBoundedService.find("id",boundedListMap.get(0).get("ID").toString());
                                                            Double dclQtyOrg = bounded.getDclQty();
                                                            bounded.setDclQty(bounded.getDclQty() - Double.parseDouble(bondInvtDtMap.get("dclQty") == null ? "0" : bondInvtDtMap.get("dclQty").toString().trim().length() == 0?"0":bondInvtDtMap.get("dclQty").toString().trim()));//申报重量
                                                            bounded.setUpdatedTime(new Date());
                                                            logger.info("BASE_BOUNDED修改通关底账信息==>("+dclQtyOrg.toString()+"==>"+bounded.getDclQty()+") "+JSON.toJSONString(bounded));
                                                            baseBoundedService.merge(bounded);
                                                            logger.info("BASE_BOUNDED修改通关底账信息成功");
                                                        }catch (Exception e){
                                                            logger.error("BASE_BOUNDED修改通关底账信息失败==> "+e.getMessage());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                //修改单一窗口回执返回备注信息
                                if (INV201.get("CheckInfo") != null && INV201.get("CheckInfo").toString().trim().length() > 0) {
                                    JSONObject CheckInfo = JSONObject.parseObject(INV201.get("CheckInfo").toString());
                                    Map<String, Object> checkInfoMap = JSON.parseObject(CheckInfo.toString());
                                    if (checkInfoMap.get("note") != null && checkInfoMap.get("note").toString().trim().length() > 0) {
                                        forBisPreEntry.setRemark(checkInfoMap.get("note").toString());//单一窗口回执返回备注信息
                                        forBisPreEntry.setUpdateBy("SYSTEM");
                                        forBisPreEntry.setUpdateTime(new Date());
                                        preEntryService.merge(forBisPreEntry);
                                    }
                                }
                            }
                        }
                    }
                }
            }
//            //将文件挪放至日期下的预录入编号文件夹中
//            if (file != null && ftpFile == null) {
//                if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
//                    copyFile(etpsPreentNo, fileName, file, null);//本地测试
//                }
//            }
//            if (file == null && ftpFile != null) {
//                if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
//                    copyFile(etpsPreentNo, fileName, null, ftpFile);//ftp
//                }
//            }
        }
    }

    /**
     * 复制文件
     *
     * @param EtpsPreentNo 业务系统内部编号（forId）
     * @param fileName     文件名
     * @param file         文件
     * @param ftpFile      文件
     */
    public static void copyFile(String EtpsPreentNo, String fileName, File file, FTPFile ftpFile) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //本地测试
        if (file != null && ftpFile == null) {
            //创建文件路径
            File descFiledir = new File("E:/HZHZ/" + simpleDateFormat.format(new Date()) + "/" + EtpsPreentNo);
            if (!descFiledir.exists()) {
                Boolean result = descFiledir.mkdirs();
            }
            //创建空白文件
            File descFile = new File(descFiledir + "\\" + fileName);
            // 判断文件是否存在
            if (!descFile.exists()) {
                Boolean result = descFile.createNewFile();
            }
            FileUtils.copyFile(file, descFile);
        }
        //FTP
        if (file == null && ftpFile != null) {
            String localPath = LOCAL_PATH + simpleDateFormat.format(new Date()) + "/" + EtpsPreentNo;
            String targetPath = FTP_TARGET_PATH + simpleDateFormat.format(new Date()) + "/" + EtpsPreentNo;

            PreEntryFTPUtils preEntryFTPUtils = PreEntryFTPUtils.getInstance();
            //下载至本地
            Boolean downResult = preEntryFTPUtils.downFile(FTP_RECEIVE_PATH, fileName, localPath);
//            System.out.println("downResult  "+downResult);
            //上传至FTP服务器备份
            Boolean upResult = preEntryFTPUtils.upFile(localPath, fileName, targetPath);
//            System.out.println("upResult  "+upResult);
        }
    }
}
