package com.haiersoft.ccli.wms.web.passPort;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.bounded.service.BaseBoundedService;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.supervision.service.GetKeyService;
import com.haiersoft.ccli.system.entity.ScheduleJob;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPort;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfo;
import com.haiersoft.ccli.wms.service.passPort.PassPortInfoService;
import com.haiersoft.ccli.wms.service.passPort.PassPortService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 执行核放单回执
 */
@DisallowConcurrentExecution
public class PassPortTaskController implements Job {

    private static final Logger logger = LoggerFactory.getLogger(PassPortTaskController.class);

    @Autowired
    private PassPortService passPortService;
    @Autowired
    private PassPortInfoService passPortInfoService;
    @Autowired
    GetKeyService getKeyService;
    @Autowired
    private BaseBoundedService baseBoundedService;

    @Override
    public void execute(JobExecutionContext context) {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        getPassPortHZ();
        System.out.println("任务名称 = [" + scheduleJob.getName() + "]" + " 在 " + dateFormat.format(new Date()) + " 时运行");
    }

    public static final String FTP_RECEIVE_PATH = "/Receive/";//FTP回执文件存放路径
    public static final String LOCAL_PATH = "C:/HuiZhi/";//本地文件临时存放位置
    public static final String FTP_TARGET_PATH = "/BAK/Receive/";//FTP回执文件备份路径

    //核放单回执
    @Transactional
    public void getPassPortHZ() {
        try {
            ftpFileList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //FTP执行回执
    public void ftpFileList() throws IOException, ParseException {
        String date = "2024-07-24 00:00:00";
        PassPortFTPUtils passPortFTPUtils = PassPortFTPUtils.getInstance();
        // 获取路径下所有的回执文件
        FTPFile[] fileList = passPortFTPUtils.getFilesList(FTP_RECEIVE_PATH);
        //获取路径下所有的回执文件
        if (fileList.length > 0) {
            //给etpsPreentNo2字段赋值
            for (FTPFile ftpFile : fileList) {
                String fileName = ftpFile.getName();
                String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (!"COPSASX".equals(substring)) {
                    System.out.println("fileName1 ==> "+fileName);
                    continue;
                } else {
                    if (fileName.contains("Successed_") || fileName.contains("Failed_")) {
                        //只获取并解析2024-07-24之后的回执
                        Date fileLastModifyTime = ftpFile.getTimestamp().getTime();
                        System.out.println("modifyTime==>>"+fileLastModifyTime);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        if (fileLastModifyTime.getTime() < sdf.parse(date).getTime()){
                            continue;
                        }

                        String xml = "";
                        InputStream in = passPortFTPUtils.retrieveFile(FTP_RECEIVE_PATH, fileName);
                        InputStreamReader read = new InputStreamReader(in, "GBK");
                        BufferedReader reader = new BufferedReader(read);
                        String line;
                        while ((line = reader.readLine()) != null) {
                            xml += line;
                        }
                        read.close();
                        String SeqNo = null;
                        String EtpsPreentNo = null;
                        org.json.JSONObject jsonObject = XML.toJSONObject(xml);
                        if (jsonObject.get("CommonResponeMessage") != null) {
                            JSONObject commonResponeMessage = JSONObject.parseObject(jsonObject.get("CommonResponeMessage").toString());
                            if (commonResponeMessage.get("EtpsPreentNo") != null && commonResponeMessage.get("EtpsPreentNo").toString().trim().length() > 0) {
                                EtpsPreentNo = commonResponeMessage.get("EtpsPreentNo").toString();
                            }
                            if(commonResponeMessage.get("SeqNo")!=null && commonResponeMessage.get("SeqNo").toString().trim().length() > 0) {
                                SeqNo = commonResponeMessage.get("SeqNo").toString();
                                List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
                                PropertyFilter filter = new PropertyFilter("EQS_seqNo", EtpsPreentNo);
                                filters.add(filter);
                                List<BisPassPort> bisPassPortList = passPortService.search(filters);
                                if (bisPassPortList != null && bisPassPortList.size() == 1) {
                                    for (BisPassPort forBisPassPort : bisPassPortList) {
                                        forBisPassPort.setEtpsPreentNo2(SeqNo);
                                        forBisPassPort.setUpdateBy("SYSTEM");
                                        forBisPassPort.setUpdateTime(new Date());
                                        passPortService.merge(forBisPassPort);
                                    }
                                }
                            }
                            if (EtpsPreentNo != null && EtpsPreentNo.trim().length() > 0) {
                                //将文件挪放至日期下的预录入编号文件夹中
                                copyFile(SeqNo, fileName, null, ftpFile);
                            }
                        }
                    }
                }
            }
            //修改审核状态或过闸状态
            for (FTPFile ftpFile : fileList) {
                String fileName = ftpFile.getName();
                String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (!"COPSASX".equals(substring)) {
                    System.out.println("fileName1 ==> "+fileName);
                    continue;
                } else {
                    System.out.println("fileName2 ==> "+fileName);
                    if (fileName.contains("Receipt")) {//单一窗口返回回执
                        //只获取并解析2024-07-24之后的回执
                        Date fileLastModifyTime = ftpFile.getTimestamp().getTime();
                        System.out.println("modifyTime==>>"+fileLastModifyTime);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        if (fileLastModifyTime.getTime() < sdf.parse(date).getTime()){
                            continue;
                        }

                        String xml = "";
                        InputStream in = passPortFTPUtils.retrieveFile(FTP_RECEIVE_PATH, fileName);
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
        }
        return;
    }

    //解析数据执行业务逻辑
    public void execute(String xml, String fileName, File file, FTPFile ftpFile) {
        //获取回执数据
        org.json.JSONObject jsonObject = XML.toJSONObject(xml);
        if (jsonObject.get("Package") != null) {
            JSONObject Package = JSONObject.parseObject(jsonObject.get("Package").toString());

            JSONObject DataInfo = JSONObject.parseObject(Package.get("DataInfo").toString());

            JSONObject BussinessData = JSONObject.parseObject(DataInfo.get("BussinessData").toString());


            //区分校验回执
            if (BussinessData.get("SAS221") != null) {//核放单回执报文
                SAS221Mothed(BussinessData,fileName,file,ftpFile);
            }
            if (BussinessData.get("SAS222") != null) {//核放单修改回执报文
                SAS222Mothed(BussinessData,fileName,file,ftpFile);
            }
            if (BussinessData.get("SAS223") != null) {//核放单过卡回执报文
                SAS223Mothed(BussinessData,fileName,file,ftpFile);
            }
            if (BussinessData.get("SAS224") != null) {//核放单查验处置回执报文
                SAS224Mothed(BussinessData,fileName,file,ftpFile);
            }
            if (BussinessData.get("SAS231") != null) {//车辆信息审核回执报文
                SAS231Mothed(BussinessData,fileName,file,ftpFile);
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

    //核放单回执报文
    public void SAS221Mothed(JSONObject BussinessData, String fileName, File file, FTPFile ftpFile) {
        String etpsPreentNo = null;
        JSONObject SAS221 = JSONObject.parseObject(BussinessData.get("SAS221").toString());
        //清单审批回执
        if (SAS221.get("HdeApprResult") != null) {
            JSONObject HdeApprResult = JSONObject.parseObject(SAS221.get("HdeApprResult").toString());
            Map<String, Object> hdeApprResultMap = JSON.parseObject(HdeApprResult.toString());
            if (hdeApprResultMap.get("etpsPreentNo") != null || hdeApprResultMap.get("etpsPreentNo").toString().trim().length() > 0) {
                etpsPreentNo = hdeApprResultMap.get("etpsPreentNo").toString().trim();
            }

            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            PropertyFilter filter = new PropertyFilter("EQS_etpsPreentNo2", etpsPreentNo);
            filters.add(filter);
            List<BisPassPort> bisPassPortList = new ArrayList<>();
            bisPassPortList = passPortService.search(filters);
            if (bisPassPortList != null && bisPassPortList.size() == 1) {
                for (BisPassPort forBisPassPort : bisPassPortList) {
                    //1-通过2-转人工3-退单Y-入库成功Z-入库失败
                    //状态-1-删除;0-新增;1-申报成功;4-成功发送海关;5-海关接收成功;6-海关接收失败;B-海关终审通过;C-退单;E-删除;T-转人工;
                    if ("1".equals(hdeApprResultMap.get("manageResult") == null ? "" : hdeApprResultMap.get("manageResult").toString())) {
                        forBisPassPort.setState("B");//通过
                        //反写核放单号
                        forBisPassPort.setPassportNo(hdeApprResultMap.get("businessId") == null ? "未获取到核放单号" : hdeApprResultMap.get("businessId").toString());
                    }
                    if ("2".equals(hdeApprResultMap.get("manageResult") == null ? "" : hdeApprResultMap.get("manageResult").toString())) {
                        forBisPassPort.setState("T");//转人工
                        if (SAS221.get("CheckInfo") != null && SAS221.get("CheckInfo").toString().trim().length() > 0) {
                            JSONObject CheckInfo = JSONObject.parseObject(SAS221.get("CheckInfo").toString());
                            Map<String, Object> checkInfoMap = JSON.parseObject(CheckInfo.toString());
                            if (checkInfoMap.get("note") != null && checkInfoMap.get("note").toString().trim().length() > 0) {
                                forBisPassPort.setRmk(checkInfoMap.get("note").toString());//单一窗口回执返回备注信息
                            }
                        }
                    }
                    if ("3".equals(hdeApprResultMap.get("manageResult") == null ? "" : hdeApprResultMap.get("manageResult").toString())) {
                        forBisPassPort.setState("C");//退单
                    }
                    if ("Y".equals(hdeApprResultMap.get("manageResult") == null ? "" : hdeApprResultMap.get("manageResult").toString())) {
                        forBisPassPort.setState("Y");//入库成功
                    }
                    if ("Z".equals(hdeApprResultMap.get("manageResult") == null ? "" : hdeApprResultMap.get("manageResult").toString())) {
                        forBisPassPort.setState("Z");//入库失败
                    }
                    forBisPassPort.setUpdateBy("SYSTEM");
                    forBisPassPort.setUpdateTime(new Date());
                    passPortService.merge(forBisPassPort);
                }
                //将文件挪放至日期下的预录入编号文件夹中
                if (file != null && ftpFile == null) {
                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
                        try {
                            copyFile(etpsPreentNo, fileName, file, null);//本地测试
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (file == null && ftpFile != null) {
                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
                        try {
                            copyFile(etpsPreentNo, fileName, null, ftpFile);//ftp
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    //核放单修改回执报文
    public void SAS222Mothed(JSONObject BussinessData, String fileName, File file, FTPFile ftpFile) {

    }

    //核放单过卡回执报文
    public void SAS223Mothed(JSONObject BussinessData, String fileName, File file, FTPFile ftpFile) {
        String etpsPreentNo = null;
        JSONObject SAS223 = JSONObject.parseObject(BussinessData.get("SAS223").toString());
        //清单审批回执
        if (SAS223.get("HdeApprResult") != null) {
            JSONObject HdeApprResult = JSONObject.parseObject(SAS223.get("HdeApprResult").toString());
            Map<String, Object> hdeApprResultMap = JSON.parseObject(HdeApprResult.toString());
            if (hdeApprResultMap.get("etpsPreentNo") != null || hdeApprResultMap.get("etpsPreentNo").toString().trim().length() > 0) {
                etpsPreentNo = hdeApprResultMap.get("etpsPreentNo").toString().trim();
            }

            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            PropertyFilter filter = new PropertyFilter("EQS_etpsPreentNo2", etpsPreentNo);
            filters.add(filter);
            List<BisPassPort> bisPassPortList = new ArrayList<>();
            bisPassPortList = passPortService.search(filters);
            if (bisPassPortList != null && bisPassPortList.size() == 1) {
                for (BisPassPort forBisPassPort : bisPassPortList) {
                    //1.已过卡2.未过卡
                    //0.已申请1.已审批2.已过卡3.已过一卡4.已过二卡5.已删除6.已作废
                    if ("1".equals(hdeApprResultMap.get("manageResult") == null ? "" : hdeApprResultMap.get("manageResult").toString())) {
                        forBisPassPort.setLockage("2");//已过卡
                        forBisPassPort.setUpdateBy("SYSTEM");
                        forBisPassPort.setUpdateTime(new Date());
                        passPortService.merge(forBisPassPort);

//                        //非空车出区则进行底账变更
//                        if(!"6".equals(forBisPassPort.getPassportTypecd().trim())){//非空车
//                            if ("E".equals(forBisPassPort.getIoTypecd().toString().trim())) {//出区
//                                List<BisPassPortInfo> bisPassPortInfoList = passPortInfoService.getList(forBisPassPort.getId());
//                                if (bisPassPortInfoList!=null && bisPassPortInfoList.size() > 0){
//                                    for (BisPassPortInfo forBisPassPortInfo:bisPassPortInfoList) {
//                                        try {
//                                            BaseBounded bounded = new BaseBounded();
//                                            bounded = baseBoundedService.find("accountBook",forBisPassPortInfo.getRltGdsSeqno());
//                                            if(bounded!=null && bounded.getDclQty() != null){
//                                                Double dclQtyOrg = bounded.getDclQty();
//                                                bounded.setDclQty(bounded.getDclQty() - Double.parseDouble(forBisPassPortInfo.getDclQty()));//申报重量
//                                                bounded.setUpdatedTime(new Date());
//                                                logger.info("BASE_BOUNDED修改通关底账信息==>(" + dclQtyOrg.toString() + "==>" + bounded.getDclQty() + ") " + JSON.toJSONString(bounded));
//                                                baseBoundedService.merge(bounded);
//                                                logger.info("BASE_BOUNDED修改通关底账信息成功");
//                                            }
//                                        } catch (Exception e) {
//                                            logger.error("BASE_BOUNDED修改通关底账信息失败==> " + e.getMessage());
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    }
                    if ("2".equals(hdeApprResultMap.get("manageResult") == null ? "" : hdeApprResultMap.get("manageResult").toString())) {
                        forBisPassPort.setLockage("0");//已申请
                        forBisPassPort.setUpdateBy("SYSTEM");
                        forBisPassPort.setUpdateTime(new Date());
                        passPortService.merge(forBisPassPort);
                    }
                }
                //将文件挪放至日期下的预录入编号文件夹中
                if (file != null && ftpFile == null) {
                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
                        try {
                            copyFile(etpsPreentNo, fileName, file, null);//本地测试
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (file == null && ftpFile != null) {
                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
                        try {
                            copyFile(etpsPreentNo, fileName, null, ftpFile);//ftp
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    //核放单查验处置回执报文
    public void SAS224Mothed(JSONObject BussinessData, String fileName, File file, FTPFile ftpFile) {
        String etpsPreentNo = null;
        JSONObject SAS224 = JSONObject.parseObject(BussinessData.get("SAS224").toString());
        //清单审批回执
        if (SAS224.get("HdeApprResult") != null) {
            JSONObject HdeApprResult = JSONObject.parseObject(SAS224.get("HdeApprResult").toString());
            Map<String, Object> hdeApprResultMap = JSON.parseObject(HdeApprResult.toString());
            if (hdeApprResultMap.get("etpsPreentNo") != null || hdeApprResultMap.get("etpsPreentNo").toString().trim().length() > 0) {
                etpsPreentNo = hdeApprResultMap.get("etpsPreentNo").toString().trim();
            }

            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            PropertyFilter filter = new PropertyFilter("EQS_etpsPreentNo2", etpsPreentNo);
            filters.add(filter);
            List<BisPassPort> bisPassPortList = new ArrayList<>();
            bisPassPortList = passPortService.search(filters);
            if (bisPassPortList != null && bisPassPortList.size() == 1) {
                for (BisPassPort forBisPassPort : bisPassPortList) {
                    //2.拒绝过卡 3.卡口放行
                    //0.已申请1.已审批2.已过卡3.已过一卡4.已过二卡5.已删除6.已作废
                    if ("2".equals(hdeApprResultMap.get("manageResult") == null ? "" : hdeApprResultMap.get("manageResult").toString())) {
                        forBisPassPort.setLockage("6");//拒绝过卡
                    }
                    if ("3".equals(hdeApprResultMap.get("manageResult") == null ? "" : hdeApprResultMap.get("manageResult").toString())) {
                        forBisPassPort.setLockage("1");//卡口放行
                    }
                    forBisPassPort.setUpdateBy("SYSTEM");
                    forBisPassPort.setUpdateTime(new Date());
                    passPortService.merge(forBisPassPort);
                }
                //将文件挪放至日期下的预录入编号文件夹中
                if (file != null && ftpFile == null) {
                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
                        try {
                            copyFile(etpsPreentNo, fileName, file, null);//本地测试
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (file == null && ftpFile != null) {
                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
                        try {
                            copyFile(etpsPreentNo, fileName, null, ftpFile);//ftp
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    //核放单修改回执报文
    public void SAS231Mothed(JSONObject BussinessData, String fileName, File file, FTPFile ftpFile) {

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
        //FTP
        if (file == null && ftpFile != null) {
            String localPath = LOCAL_PATH + simpleDateFormat.format(new Date()) + "/" + EtpsPreentNo;
            String targetPath = FTP_TARGET_PATH + simpleDateFormat.format(new Date()) + "/" + EtpsPreentNo;

            PassPortFTPUtils passPortFTPUtils = PassPortFTPUtils.getInstance();
            //下载至本地
            Boolean downResult = passPortFTPUtils.downFile(FTP_RECEIVE_PATH, fileName, localPath);
            System.out.println("downResult  "+downResult);
            //上传至FTP服务器备份
            Boolean upResult = passPortFTPUtils.upFile(localPath, fileName, targetPath);
            System.out.println("upResult  "+upResult);
        }
    }
}
