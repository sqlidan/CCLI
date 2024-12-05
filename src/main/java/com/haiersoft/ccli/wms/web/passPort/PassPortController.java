package com.haiersoft.ccli.wms.web.passPort;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.Log;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.apiEntity.*;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPort;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfo;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfoDJ;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryBounded;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import com.haiersoft.ccli.wms.service.passPort.PassPortInfoDJService;
import com.haiersoft.ccli.wms.service.passPort.PassPortInfoService;
import com.haiersoft.ccli.wms.service.passPort.PassPortService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryInfoService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryService;
import com.haiersoft.ccli.wms.web.preEntry.HttpUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.net.ftp.FTPFile;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: PassPortController
 */
@Controller
@RequestMapping("wms/passPort")
public class PassPortController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(PassPortController.class);

    @Autowired
    private PassPortService passPortService;
    @Autowired
    private PassPortInfoService passPortInfoService;
    @Autowired
    private PassPortInfoDJService passPortInfoDJService;

    private static final String memberCode = "eimskipMember";
    private static final String pass = "66668888";
    private static final String icCode = "2100030046252";

    /**
     * 核放单页面
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "wms/passPort/passPort";
    }

    /**
     * 核放单列表
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<BisPassPort> page = getPage(request);
        page.orderBy("createTime").order(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("NEQS_state", "-1");//已删除
        filters.add(filter);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -3); // 将日期往前推三天
        // 将Calendar对象转换为Date对象
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String formattedDate = sdf.format(date);
        PropertyFilter filterCreateTime = new PropertyFilter("GED_createTime", formattedDate);//三天内数据
        filters.add(filterCreateTime);
        page = passPortService.search(page, filters);
        return getEasyUIData(page);
    }

    /**
     * 核放单-跳转新增页面
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String manager(Model model) {
        User user = UserUtil.getCurrentUser();
        BisPassPort bisPassPort = new BisPassPort();
        bisPassPort.setMasterCuscd("4230");//主管关区
        bisPassPort.setAreainEtpsno("3702631016");//区内企业编码
        bisPassPort.setAreainEtpsSccd("91370220395949850B");//区内企业社会信用代码
        bisPassPort.setAreainEtpsNm("青岛港怡之航冷链物流有限公司");//区内企业名称
        bisPassPort.setDclEtpsno("3702631016");//申报单位编码
        bisPassPort.setDclEtpsSccd("91370220395949850B");//申报单位社会信用代码
        bisPassPort.setDclEtpsNm("青岛港怡之航冷链物流有限公司");//申报单位名称
        bisPassPort.setInputCode("3702631016");//录入单位编码
        bisPassPort.setInputSccd("91370220395949850B");//录入单位社会信用代码
        bisPassPort.setInputName("青岛港怡之航冷链物流有限公司");//录入单位名称
        bisPassPort.setEtpsPreentNo("3702631016");//企业内部编号
        bisPassPort.setDclTypecd("1");//申报类型
        bisPassPort.setDclBy(user.getName());
        model.addAttribute("passPort", bisPassPort);
        model.addAttribute("date", new Date());
//        model.addAttribute("dclTime", new Date());
        model.addAttribute("user", user.getName());
        model.addAttribute("action", "create");
        return "wms/passPort/passPortManager";
    }

    /**
     * 核放单-跳转编辑页面
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateContractForm(Model model, @PathVariable("id") String id) {
        User user = UserUtil.getCurrentUser();
        BisPassPort bisPassPort = passPortService.get(id);
        model.addAttribute("passPort", bisPassPort);
        model.addAttribute("date", bisPassPort.getCreateTime());
        model.addAttribute("lockageTime1", bisPassPort.getLockageTime1());
        model.addAttribute("lockageTime2", bisPassPort.getLockageTime2());
        model.addAttribute("dclTime", bisPassPort.getDclTime());
        model.addAttribute("user", user.getName());
        model.addAttribute("action", "update");
        return "wms/passPort/passPortManager";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String deleteEnterStock(@PathVariable("id") String id) {
        User user = UserUtil.getCurrentUser();
        BisPassPort bisPassPort = passPortService.get(id);
        bisPassPort.setState("-1");//删除
        bisPassPort.setUpdateBy(user.getName());//修改人
        bisPassPort.setUpdateTime(new Date());//修改时间
        passPortService.merge(bisPassPort);
        return "success";
    }

    /**
     * 修改核放单状态
     */
    @RequestMapping(value = "UpdateState/{id}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public String jlUpdateOkContractForm(Model model, @PathVariable("id") String id, @PathVariable("type") String type) throws RemoteException, ServiceException {
        User user = UserUtil.getCurrentUser();
        BisPassPort bisPassPort = passPortService.get(id);
        if (bisPassPort != null) {
            if ("1".equals(type)) {
                bisPassPort.setDclBy(user.getName());//申报人
                bisPassPort.setDclTime(new Date());//申报时间
                //调用申报核放单接口
                Map<String, Object> resultMap = passPortSave(id);
                if ("200".equals(resultMap.get("code").toString())) {
                    bisPassPort.setState("1");
                    bisPassPort.setDclTime(new Date());
                    if(resultMap.get("data") != null){
                        bisPassPort.setSeqNo(resultMap.get("data").toString());
                    }
                } else {
                    return resultMap.get("msg").toString();
                }
            }
            if ("C".equals(type)) {
                //调用申报核放单接口
                Map<String, Object> resultMap = passPortNullify(id);
                if ("200".equals(resultMap.get("code").toString())) {
                    bisPassPort.setState("E");//E-删除；C-退单
                    bisPassPort.setLockage("6");//已作废
                    //作废申请成功
                    System.out.println("作废申请成功");
                } else {
                    return resultMap.get("msg").toString();
                }
            }
            bisPassPort.setUpdateBy(user.getName());//修改人
            bisPassPort.setUpdateTime(new Date());//修改时间
            passPortService.merge(bisPassPort);
            return "success";
        } else {
            return "error";
        }
    }

    public static void main(String[] args) {

    }

    /**
     * 同步
     */
    @RequestMapping(value = "synchronization/{id}")
    @ResponseBody
    public String synchronization(@PathVariable("id") String id) throws ParseException {
        User user = UserUtil.getCurrentUser();
        BisPassPort bisPassPort = passPortService.get(id);
        if (bisPassPort != null) {
            //调用申报核放单同步接口
            Map<String, Object> resultMap = passPortSynchronization(bisPassPort.getSeqNo());
            logger.info("resultMap== "+resultMap.toString());
            if ("200".equals(resultMap.get("code").toString())) {
                    //处理结果
                    PassPortMessage passPortMessage = (PassPortMessage) resultMap.get("data");
                    String status = passPortMessage.getStatus()==null?"0":passPortMessage.getStatus();
                    logger.info("status== "+status);
                    bisPassPort.setState(status);
                    PassPortHead passPortHead = passPortMessage.getPassportHead();
                    logger.info("passPortHead== "+JSON.toJSONString(passPortHead));
                    if (passPortHead !=null){
                        if(passPortHead.getPassportNo()!=null && passPortHead.getPassportNo().trim().length() > 0){
                            bisPassPort.setPassportNo(passPortHead.getPassportNo());
                        }
                        if(passPortHead.getPassStatus()!=null && passPortHead.getPassStatus().trim().length() > 0){
                            bisPassPort.setLockage(passPortHead.getPassStatus());
                        }
                        if(passPortHead.getPassTime1()!=null && passPortHead.getPassTime1().trim().length() > 0){
                            bisPassPort.setLockageTime1(passPortHead.getPassTime1());
                        }
                        if(passPortHead.getPassTime2()!=null && passPortHead.getPassTime2().trim().length() > 0){
                            bisPassPort.setLockageTime2(passPortHead.getPassTime2());
                        }
                    }
            } else {
                return resultMap.get("msg").toString();
            }
            bisPassPort.setUpdateBy(user.getName());//修改人
            bisPassPort.setUpdateTime(new Date());//修改时间
            passPortService.merge(bisPassPort);
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 获取最新状态
     */
    @RequestMapping(value = "synchronizationStatus")
    @ResponseBody
    public String synchronizationStatus() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        try {
            logger.info("开始");
            ftpFileList();
            logger.info("结束");
            System.out.println("任务名称 = [同步状态]" + " 在 " + dateFormat.format(new Date()) + " 时运行");
            logger.info("任务名称 = []" + " 在 " + dateFormat.format(new Date()) + " 时运行");
        } catch (Exception e) {
            logger.info("异常");
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 手动创建定时任务
     */
    @RequestMapping(value = "createScheduledExecutor")
    @ResponseBody
    public String createScheduledExecutor() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        // 创建一个异步定时任务
        Runnable task = () -> {
            try {
                logger.info("开始");
                logger.info("执行......");
                ftpFileList();
                logger.info("结束");
                System.out.println("任务名称 = [手动创建定时任务]" + " 在 " + dateFormat.format(new Date()) + " 时运行");
                logger.info("任务名称 = [手动创建定时任务]" + " 在 " + dateFormat.format(new Date()) + " 时运行");
            } catch (Exception e) {
                logger.info("异常");
                executorService.shutdown();
            }
        };
        executorService.scheduleAtFixedRate(task, 0, 25, TimeUnit.SECONDS);
        return "success";
    }

    //申报核放单
    public Map<String, Object> passPortSave(String id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取表头数据
        BisPassPort bisPassPortHead = new BisPassPort();
        List<BisPassPort> bisPassPortList = passPortService.getList(id);
        if (bisPassPortList == null || bisPassPortList.size() == 0) {
            resultMap.put("code","500");
            resultMap.put("msg","未获取到核放单表头数据");
            return resultMap;
        } else {
            bisPassPortHead = bisPassPortList.get(0);
        }
        //校验是否是空车进出区
        boolean empty = false;
        if(bisPassPortHead != null && bisPassPortHead.getPassportTypecd() != null && bisPassPortHead.getPassportTypecd().trim().length() > 0){
            //继续判断
            if("6".equals(bisPassPortHead.getPassportTypecd().trim())){
                empty = true;
            }
        }else{
            resultMap.put("code","500");
            resultMap.put("msg","核放单类型必填");
            return resultMap;
        }

        List<BisPassPortInfo> bisPassPortInfoList = new ArrayList<>();
        List<BisPassPortInfoDJ> bisPassPortInfoDJList = new ArrayList<>();
        //核放单类型为空车进出区时不校验表体和关联单据
        if(!empty){
            //获取表体数据
            bisPassPortInfoList = passPortInfoService.getList(id);
//            if (bisPassPortInfoList == null || bisPassPortInfoList.size() == 0) {
//                resultMap.put("code","500");
//                resultMap.put("msg","未获取到核放单表体数据");
//                return resultMap;
//            }
            //获取表体数据
            bisPassPortInfoDJList = passPortInfoDJService.getList(id);
        }
        //核绑定类型为一票多车时需要校验关联单据不能为空
        boolean multipleCars = false;
        if(bisPassPortHead != null && bisPassPortHead.getBindTypecd() != null && bisPassPortHead.getBindTypecd().trim().length() > 0){
            //继续判断
            if("3".equals(bisPassPortHead.getBindTypecd().trim())){
                //一票多车
                multipleCars = true;
            }
            if(multipleCars){
                if (bisPassPortInfoDJList == null || bisPassPortInfoDJList.size() == 0) {
                    resultMap.put("code","500");
                    resultMap.put("msg","未获取到核放单关联单证数据");
                    return resultMap;
                }
            }
        }

        String seqNo = isNullOrEmpty(bisPassPortHead.getSeqNo());
        String passportNo = isNullOrEmpty(bisPassPortHead.getPassportNo());


        //核放单表头
        PassPortHead passPortHead = new PassPortHead();
        passPortHead.setSeqNo(seqNo);
        passPortHead.setPassportNo(passportNo);
        passPortHead.setAreainEtpsNm(isNullOrEmpty(bisPassPortHead.getAreainEtpsNm()));
        passPortHead.setAreainEtpsno(isNullOrEmpty(bisPassPortHead.getAreainEtpsno()));
        passPortHead.setAreainEtpsSccd(isNullOrEmpty(bisPassPortHead.getAreainEtpsSccd()));
        passPortHead.setAreainOriactNo(isNullOrEmpty(bisPassPortHead.getAreainOriactNo()));
        passPortHead.setBindTypecd(isNullOrEmpty(bisPassPortHead.getBindTypecd()));
        passPortHead.setContainerNo(isNullOrEmpty(bisPassPortHead.getContainerNo()));
        passPortHead.setContainerType(isNullOrEmpty(bisPassPortHead.getContainerType()));
        passPortHead.setContainerWt(isNullOrEmpty(bisPassPortHead.getContainerWt()));
        passPortHead.setDclEr(isNullOrEmpty(bisPassPortHead.getDclBy()));
        passPortHead.setDclErConc(isNullOrEmpty(bisPassPortHead.getDclErConc()));
        passPortHead.setDclEtpsNm(isNullOrEmpty(bisPassPortHead.getDclEtpsNm()));
        passPortHead.setDclEtpsno(isNullOrEmpty(bisPassPortHead.getDclEtpsno()));
        passPortHead.setDclEtpsSccd(isNullOrEmpty(bisPassPortHead.getDclEtpsSccd()));
        passPortHead.setDclTime(simpleDateFormat.format(bisPassPortHead.getDclTime() == null ? (new Date()) : bisPassPortHead.getDclTime()));
        passPortHead.setDclTypecd(isNullOrEmpty(bisPassPortHead.getDclTypecd()));
        passPortHead.setEtpsPreentNo(isNullOrEmpty(bisPassPortHead.getEtpsPreentNo()));
        passPortHead.setInputCode(isNullOrEmpty(bisPassPortHead.getInputCode()));
        passPortHead.setInputDate(simpleDateFormat.format(bisPassPortHead.getCreateTime() == null ? (new Date()) : bisPassPortHead.getCreateTime()));
        passPortHead.setInputName(isNullOrEmpty(bisPassPortHead.getInputName()));
        passPortHead.setInputSccd(isNullOrEmpty(bisPassPortHead.getInputSccd()));
        passPortHead.setIoTypecd(isNullOrEmpty(bisPassPortHead.getIoTypecd()));
        passPortHead.setMasterCuscd(isNullOrEmpty(bisPassPortHead.getMasterCuscd()));
        passPortHead.setPassportTypecd(isNullOrEmpty(bisPassPortHead.getPassportTypecd()));
//        passPortHead.setPassStatus(isNullOrEmpty(bisPassPortHead.getLockage()));
//        passPortHead.setPassTime1(isNullOrEmpty(bisPassPortHead.getLockageTime1()));
//        passPortHead.setPassTime2(isNullOrEmpty(bisPassPortHead.getLockageTime2()));
        passPortHead.setRltNo(isNullOrEmpty(bisPassPortHead.getRltNo()));
        passPortHead.setRltTbTypecd(isNullOrEmpty(bisPassPortHead.getRltTbTypecd()));
        passPortHead.setRmk(isNullOrEmpty(bisPassPortHead.getRmk()));
        passPortHead.setTotalGrossWt(isNullOrEmpty(bisPassPortHead.getTotalGrossWt()));
        passPortHead.setTotalNetWt(isNullOrEmpty(bisPassPortHead.getTotalNetWt()));
        passPortHead.setTotalWt(isNullOrEmpty(bisPassPortHead.getTotalWt()));
        passPortHead.setVehicleFrameNo(isNullOrEmpty(bisPassPortHead.getVehicleFrameNo()));
        passPortHead.setVehicleFrameWt(isNullOrEmpty(bisPassPortHead.getVehicleFrameWt()));
        passPortHead.setVehicleIcNo(isNullOrEmpty(bisPassPortHead.getVehicleIcNo()));
        passPortHead.setVehicleNo(isNullOrEmpty(bisPassPortHead.getVehicleNo()));
        passPortHead.setVehicleWt(isNullOrEmpty(bisPassPortHead.getVehicleWt()));
//        passPortHead.setWtError("");//重量比对误差
//        passPortHead.setEmapvMarkcd("");//审批标记代码
//        passPortHead.setLogisticsStucd("");//物流状态代码
//        passPortHead.setPassCollectWt("");//卡口地磅采集重量
//        passPortHead.setPassId1("");//卡口ID1
//        passPortHead.setPassId2("");//卡口ID2
//        passPortHead.setStucd("");//状态

        List<PassPortList> passPortLists = new ArrayList<>();
        List<PassPortAcmp> passPortAcmpList = new ArrayList<>();
        //核放单类型为空车进出区时不校验表体和关联单据淑君
        if(!empty) {
            // 表体参数字段
            for (BisPassPortInfo forBisPassPortInfo : bisPassPortInfoList) {
                PassPortList passPortList = new PassPortList();
                passPortList.setSeqNo(seqNo);
                passPortList.setPassportNo(passportNo);
                passPortList.setDclQty(isNullOrEmpty(forBisPassPortInfo.getDclQty()));
                passPortList.setDclUnitcd(isNullOrEmpty(forBisPassPortInfo.getDclUnitcd()));
                passPortList.setGdecd(isNullOrEmpty(forBisPassPortInfo.getGdecd()));
                passPortList.setGdsMtno(isNullOrEmpty(forBisPassPortInfo.getGdsMtno()));
                passPortList.setGdsNm(isNullOrEmpty(forBisPassPortInfo.getGdsNm()));
                passPortList.setGrossWt(isNullOrEmpty(forBisPassPortInfo.getGrossWt()));
                passPortList.setNetWt(isNullOrEmpty(forBisPassPortInfo.getNetWt()));
                passPortList.setPassportSeqNo(isNullOrEmpty(forBisPassPortInfo.getPassportSeqno()));
                passPortList.setRltGdsSeqno(isNullOrEmpty(forBisPassPortInfo.getRltGdsSeqno()));
                passPortList.setRmk(isNullOrEmpty(forBisPassPortInfo.getRemark()));
//            passPortList.setOriactGdsSeqno("");//备案序号
//            passPortList.setGdsSpcfModelDesc("");//经营单位代码 反填

                passPortLists.add(passPortList);
            }

            //关联单证参数字段
            for (BisPassPortInfoDJ forBisPassPortInfoDJ : bisPassPortInfoDJList) {
                PassPortAcmp passPortAcmp = new PassPortAcmp();
                passPortAcmp.setSeqNo(seqNo);
                passPortAcmp.setPassPortNo(passportNo);
                passPortAcmp.setRtlBillNo(isNullOrEmpty(forBisPassPortInfoDJ.getRtlNo()));
                passPortAcmp.setRtlBillTypecd(isNullOrEmpty(forBisPassPortInfoDJ.getRtlTbTypecd()));
//            passPortAcmp.setPassportAcmpSeqNo("");//核放单关联单证统一编号

                passPortAcmpList.add(passPortAcmp);
            }
        }

        PassPortMessage passPortMessage = new PassPortMessage();

        passPortMessage.setPassportHead(passPortHead);//表头
        passPortMessage.setPassportList(passPortLists);//表体
        passPortMessage.setPassportAcmp(passPortAcmpList);//关联单证
        passPortMessage.setMemberCode(memberCode);
        passPortMessage.setPass(pass);
        passPortMessage.setIcCode(icCode);
        logger.info("passPortMessage== "+JSON.toJSONString(passPortMessage));
//        //核放单申报
        resultMap = PassPortDeclearService(passPortMessage);

        return resultMap;
    }

    //作废核放单
    public Map<String, Object> passPortNullify(String id){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取表头数据
        BisPassPort bisPassPortHead = new BisPassPort();
        List<BisPassPort> bisPassPortList = passPortService.getList(id);
        if (bisPassPortList == null || bisPassPortList.size() == 0) {
            resultMap.put("code","500");
            resultMap.put("msg","未获取到核放单表头数据");
            return resultMap;
        } else {
            bisPassPortHead = bisPassPortList.get(0);
        }
        SasCommonSeqNoRequest sasCommonSeqNoRequest = new SasCommonSeqNoRequest();
//        sasCommonSeqNoRequest.setBlsNo(null);
//        sasCommonSeqNoRequest.setBlsType(null);
//        sasCommonSeqNoRequest.setChgTmsCnt(null);
//        sasCommonSeqNoRequest.setRltGdsSeqno(null);
        sasCommonSeqNoRequest.setSeqNo(bisPassPortHead.getSeqNo());
        sasCommonSeqNoRequest.setMemberCode(memberCode);
        sasCommonSeqNoRequest.setPass(pass);
        sasCommonSeqNoRequest.setIcCode(icCode);
        //核放单作废
        logger.info("sasCommonSeqNoRequest== "+JSON.toJSONString(sasCommonSeqNoRequest));
        resultMap = PassPortNullifyService(sasCommonSeqNoRequest);

        return resultMap;
    }

    //同步核放单
    public Map<String, Object> passPortSynchronization(String seqNo){
        //核放单列表查询服务
        Map<String, Object> resultMap = new HashMap<String, Object>();
        SasCommonSeqNoRequest sasCommonSeqNoRequest = new SasCommonSeqNoRequest();
        sasCommonSeqNoRequest.setSeqNo(seqNo);
        sasCommonSeqNoRequest.setMemberCode(memberCode);
        sasCommonSeqNoRequest.setPass(pass);
        sasCommonSeqNoRequest.setIcCode(icCode);
        logger.info("sasCommonSeqNoRequest== "+JSON.toJSONString(sasCommonSeqNoRequest));
        resultMap = PassPortDetailService(sasCommonSeqNoRequest);
        logger.info("sasCommonSeqNoResult== "+resultMap.toString());
        return resultMap;
    }

//======================================================================================================================

    /**
     * 获取字典数据
     */
    @RequestMapping(value="dictData/{code}",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> dictList(@PathVariable("code") String code) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<BisPreEntryDictData> bisPreEntryDictDataList = new ArrayList<>();
        bisPreEntryDictDataList = passPortService.getDictDataByCode(code);
        if(bisPreEntryDictDataList == null || bisPreEntryDictDataList.size() == 0){
            map.put("rows", new ArrayList<>());
            map.put("total", 0);
        }else{
            map.put("rows", bisPreEntryDictDataList);
            map.put("total", bisPreEntryDictDataList.size());
        }
        return map;
    }


    /**
     * @return com.chenay.core.tool.api.R<com.chenay.bean.entity.customs.SasCommonResponse>
     * @Author chenp
     * @Description 核放单暂存
     * @Date 13:21 2021/3/12
     * @Param [passPortMessage]
     **/
    public Map<String,Object> PassPortSaveService(PassPortMessage passPortMessage) {
        logger.info("核放单暂存:"+JSON.toJSONString(passPortMessage));
        Map<String,Object> resultMap = new HashMap<>();
        SasCommonResponse baseResult = new SasCommonResponse();
        String dataStr = "";
        try {
            passPortMessage.setKey(ApiKey.保税监管_保税核放单保存服务秘钥.getValue());
            String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_核放单暂存接口.getValue(), JSON.toJSONString(passPortMessage));
            JSONObject jsonObject = JSON.parseObject(s);
            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
            if ("200".equals(code)) {
                Object data = jsonObject.get("data");

                if (data != null) {
                    dataStr = data.toString();
                }
                baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), SasCommonResponse.class);
                resultMap.put("code","200");
                resultMap.put("msg","success");
                resultMap.put("data",baseResult.getSeqNo());
            } else {
                Object data = jsonObject.get("msg");
                if (data != null) {
                    dataStr = data.toString();
                }
                logger.error("保税监管_核放单暂存接口"+passPortMessage.getPassportHead().getVehicleNo(),"结果"+dataStr);
                logger.error(dataStr);
                resultMap.put("code","500");
                resultMap.put("msg",dataStr);
                resultMap.put("data",null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return resultMap;
    }

    /**
     * @return com.chenay.core.tool.api.R<com.chenay.bean.entity.customs.SasCommonResponse>
     * @Author chenp
     * @Description 核放单申报接口
     * @Date 9:10 2021/1/30
     * @Param [passPortMessage]
     **/
    public Map<String,Object> PassPortDeclearService(PassPortMessage passPortMessage) {
        logger.info("核放单申报接口:"+JSON.toJSONString(passPortMessage));
        Map<String,Object> resultMap = new HashMap<>();
        SasCommonResponse baseResult = new SasCommonResponse();
        String dataStr = "";
        try {
            passPortMessage.setKey(ApiKey.保税监管_保税核放单保存服务秘钥.getValue());
            String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_核放单申报接口.getValue(), JSON.toJSONString(passPortMessage));
            logger.info("核放单申报接口结果:"+s);
//            JSONObject jsonObject = JSON.parseObject("{\"code\":200,\"msg\":null,\"data\":{\"seqNo\":null,\"etpsPreentNo\":null,\"checkInfo\":null,\"dealFlag\":null,\"clientSeqNo\":null,\"dclDate\":\"20231101\",\"preNo\":\"P42302300000032509\",\"state\":\"1\"}}");
            JSONObject jsonObject = JSON.parseObject(s);
            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
            if ("200".equals(code)) {
                Object data = jsonObject.get("data");

                if (data != null) {
                    dataStr = data.toString();
                }
                baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), SasCommonResponse.class);
                resultMap.put("code","200");
                resultMap.put("msg","success");
                resultMap.put("data",baseResult.getPreNo());
            } else {
                Object data = jsonObject.get("msg");
                if (data != null) {
                    dataStr = data.toString();
                }
                logger.error("保税监管_核放单申报接口"+passPortMessage.getPassportHead().getVehicleNo(),"结果"+dataStr);
                logger.error(dataStr);
                resultMap.put("code","500");
                resultMap.put("msg",dataStr);
                resultMap.put("data",null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return resultMap;
    }

    /**
     * @return com.chenay.core.tool.api.R<com.chenay.bean.entity.customs.CommonResponeMessage>
     * @Author chenp
     * @Description 核放单作废服务
     * @Date 9:26 2021/1/30
     * @Param [sasCommonSeqNoRequest]
     **/
    public Map<String,Object> PassPortNullifyService(SasCommonSeqNoRequest sasCommonSeqNoRequest) {
        logger.info("核放单作废服务:"+JSON.toJSONString(sasCommonSeqNoRequest));
        Map<String,Object> resultMap = new HashMap<>();
        CommonResponeMessage commonResponeMessage = new CommonResponeMessage();
        try {
            sasCommonSeqNoRequest.setKey(ApiKey.保税监管_保税核放单作废秘钥.getValue());
            String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_核放单作废服务接口.getValue(), JSON.toJSONString(sasCommonSeqNoRequest));
            logger.info("核放单作废服务结果:"+s);
            JSONObject jsonObject = JSON.parseObject(s);
            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
            if ("200".equals(code)) {
                Object data = jsonObject.get("data");
                String dataStr = "";
                if (data != null) {
                    dataStr = data.toString();
                }
                commonResponeMessage = JSON.toJavaObject(JSON.parseObject(dataStr), CommonResponeMessage.class);
                resultMap.put("code","200");
                resultMap.put("msg","success");
                resultMap.put("data",commonResponeMessage);
            } else {
                Object data = jsonObject.get("msg");
                String dataStr = "";
                if (data != null) {
                    dataStr = data.toString();
                }
                logger.error("保税监管_核放单作废服务接口","结果"+dataStr);
                logger.error(dataStr);
                resultMap.put("code","500");
                resultMap.put("msg",dataStr);
                resultMap.put("data",null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return resultMap;
    }

    /**
     * @return java.util.concurrent.Callable<com.chenay.bean.entity.customs.PassPortQueryListResponse>
     * @Author chenp
     * @Description 核放单列表查询服务
     * @Date 9:21 2021/1/30
     * @Param [passPortQueryRequest]
     **/
    public Map<String,Object> PassPortQueryListService(PassPortQueryRequest passPortQueryRequest) {
        logger.info("核放单列表查询服务:"+JSON.toJSONString(passPortQueryRequest));
        Map<String,Object> resultMap = new HashMap<>();
        PassPortQueryListResponse baseResult = new PassPortQueryListResponse();
        try {
            passPortQueryRequest.setKey(ApiKey.保税监管_保税核放单查询服务秘钥.getValue());
            String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_核放单列表查询服务接口.getValue(), JSON.toJSONString(passPortQueryRequest));
            JSONObject jsonObject = JSON.parseObject(s);
            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
            if ("200".equals(code)) {
                Object data = jsonObject.get("data");
                String dataStr = "";
                if (data != null) {
                    dataStr = data.toString();
                }
                baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), PassPortQueryListResponse.class);
                resultMap.put("code","200");
                resultMap.put("msg","success");
                resultMap.put("data",baseResult);
            } else {
                Object data = jsonObject.get("msg");
                String dataStr = "";
                if (data != null) {
                    dataStr = data.toString();
                }
                logger.error("保税监管_核放单列表查询服务接口","结果"+dataStr);
                logger.error(dataStr);
                resultMap.put("code","500");
                resultMap.put("msg",dataStr);
                resultMap.put("data",null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return resultMap;
    }

    /**
     * @return com.chenay.core.tool.api.R<com.chenay.bean.entity.customs.PassPortMessage>
     * @Author chenp
     * @Description 核放单详细查询服务
     * @Date 9:23 2021/1/30
     * @Param [sasCommonSeqNoRequest]
     **/
    public Map<String,Object> PassPortDetailService(SasCommonSeqNoRequest sasCommonSeqNoRequest) {
        logger.info("核放单详细查询服务:"+JSON.toJSONString(sasCommonSeqNoRequest));
        Map<String,Object> resultMap = new HashMap<>();
        PassPortMessage baseResult = new PassPortMessage();
        try {
            sasCommonSeqNoRequest.setKey(ApiKey.保税监管_保税核放单查询服务秘钥.getValue());
            String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_核放单详细信息查询服务接口.getValue(), JSON.toJSONString(sasCommonSeqNoRequest, SerializerFeature.WriteNullStringAsEmpty));
            JSONObject jsonObject = JSON.parseObject(s);
            logger.info("jsonObject==:"+jsonObject.toJSONString());
            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
            if ("200".equals(code)) {
                Object data = jsonObject.get("data");
                String dataStr = "";
                if (data != null) {
                    dataStr = data.toString();
                }
                baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), PassPortMessage.class);
                resultMap.put("code","200");
                resultMap.put("msg","success");
                resultMap.put("data",baseResult);
            } else {
                Object data = jsonObject.get("msg");
                String dataStr = "";
                if (data != null) {
                    dataStr = data.toString();
                }
                logger.error("保税监管_核放单详细信息查询服务接口","结果"+dataStr);
                logger.error(dataStr);
                resultMap.put("code","500");
                resultMap.put("msg",dataStr);
                resultMap.put("data",null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return resultMap;
    }

//======================================================================================================================
    //校验是否为空
    public Boolean isNotNullOrEmpty(Object object) {
        if (object == null) {
            return false;
        }
        if (object.toString().trim().length() == 0) {
            return false;
        }
        return true;
    }

    //将字段转成字符串
    public String isNullOrEmpty(Object object) {
        if (object == null) {
            return "";
        }
        if (object.toString().trim().length() == 0) {
            return "";
        }
        return object+"";
    }
//======================================================================================================================
    public static final String FTP_RECEIVE_PATH = "/Receive/";//FTP回执文件存放路径
    public static final String LOCAL_PATH = "C:/HuiZhi/";//本地文件临时存放位置
    public static final String FTP_TARGET_PATH = "/BAK/Receive/";//FTP回执文件备份路径

    //FTP执行回执
    public void ftpFileList() throws IOException, ParseException {
//        String date = "2024-08-20 00:00:00";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1); // 将日期往前推三天
        // 将Calendar对象转换为Date对象
        Date dateD = calendar.getTime();
        SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String date = sdfTemp.format(dateD);

        PassPortFTPUtils passPortFTPUtils = PassPortFTPUtils.getInstance();
        // 获取路径下所有的回执文件
        FTPFile[] fileList = passPortFTPUtils.getFilesList(FTP_RECEIVE_PATH);
        //获取路径下所有的回执文件
        if (fileList.length > 0) {
            List<BisPassPort> updateBisPassPortList = new ArrayList<>();
            //给etpsPreentNo2字段赋值
            Map<String,List<BisPassPort>> listMapSuccessed = new HashMap<String,List<BisPassPort>>();
            for (FTPFile ftpFile : fileList) {
                String fileName = ftpFile.getName();
                try{
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
//                                logger.info("EtpsPreentNo1:"+commonResponeMessage.get("EtpsPreentNo"));
                                if (commonResponeMessage.get("EtpsPreentNo") != null && commonResponeMessage.get("EtpsPreentNo").toString().trim().length() > 0) {
                                    EtpsPreentNo = commonResponeMessage.get("EtpsPreentNo").toString();
//                                    logger.info("EtpsPreentNo2:"+EtpsPreentNo);
                                }
//                                logger.info("EtpsPreentNo3:"+EtpsPreentNo);
                                if(commonResponeMessage.get("SeqNo")!=null && commonResponeMessage.get("SeqNo").toString().trim().length() > 0) {
                                    SeqNo = commonResponeMessage.get("SeqNo").toString();

                                    List<BisPassPort> bisPassPortList = new ArrayList<>();
                                    if (listMapSuccessed.get(EtpsPreentNo) != null) {
                                        bisPassPortList = listMapSuccessed.get(EtpsPreentNo);
                                    } else {
                                        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
                                        PropertyFilter filter = new PropertyFilter("EQS_seqNo", EtpsPreentNo);
                                        filters.add(filter);
                                        bisPassPortList = passPortService.search(filters);
                                        listMapSuccessed.put(EtpsPreentNo,bisPassPortList);
//                                        logger.info("bisPassPortList,结果：" + JSON.toJSONString(bisPassPortList));
                                    }
                                    if (bisPassPortList != null && bisPassPortList.size() == 1) {
                                        for (BisPassPort forBisPassPort : bisPassPortList) {
                                            forBisPassPort.setEtpsPreentNo2(SeqNo);
                                            forBisPassPort.setUpdateBy("SYSTEM");
                                            forBisPassPort.setUpdateTime(new Date());
//                                            logger.info("执行merge方法");
                                            updateBisPassPortList.add(forBisPassPort);
//                                            passPortService.merge(forBisPassPort);
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
                } catch (Exception e) {
                    logger.info("Successed_异常,文件名："+fileName+".异常："+e.getMessage());
                    e.printStackTrace();
                }
            }
            if (updateBisPassPortList.size() > 0){
                for (BisPassPort forBisPassPort:updateBisPassPortList) {
                    passPortService.merge(forBisPassPort);
                }
            }
            //修改审核状态或过闸状态
            Map<String,List<BisPassPort>> listMap = new HashMap<String,List<BisPassPort>>();
            for (FTPFile ftpFile : fileList) {
                String fileName = ftpFile.getName();
                try{
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
//                                logger.info("执行execute方法");
                                execute(xml, fileName, null, ftpFile,listMap);
                            }
                        } else {
                            continue;
                        }
                    }
                } catch (Exception e) {
                    logger.info("Receipt_异常,文件名："+fileName+".异常："+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    //解析数据执行业务逻辑
    public void execute(String xml, String fileName, File file, FTPFile ftpFile,Map<String,List<BisPassPort>> listMap) {
        //获取回执数据
        org.json.JSONObject jsonObject = XML.toJSONObject(xml);
        if (jsonObject.get("Package") != null) {
            JSONObject Package = JSONObject.parseObject(jsonObject.get("Package").toString());

            JSONObject DataInfo = JSONObject.parseObject(Package.get("DataInfo").toString());

            JSONObject BussinessData = JSONObject.parseObject(DataInfo.get("BussinessData").toString());

            String etpsPreentNo = null;
            String manageResult = null;
            //区分校验回执
            if (BussinessData.get("SAS221") != null) {//核放单回执报文
//                logger.info("解析SAS221回执");
                JSONObject SAS221 = JSONObject.parseObject(BussinessData.get("SAS221").toString());
                //清单审批回执
                if (SAS221.get("HdeApprResult") != null) {
                    JSONObject HdeApprResult = JSONObject.parseObject(SAS221.get("HdeApprResult").toString());
                    Map<String, Object> hdeApprResultMap = JSON.parseObject(HdeApprResult.toString());
                    if (hdeApprResultMap.get("etpsPreentNo") != null || hdeApprResultMap.get("etpsPreentNo").toString().trim().length() > 0) {
                        etpsPreentNo = hdeApprResultMap.get("etpsPreentNo").toString().trim();
                    }
                    if (hdeApprResultMap.get("manageResult") == null ){
                        manageResult = "";
                    }else{
                        manageResult = hdeApprResultMap.get("manageResult").toString();
                    }

//                    logger.info("SAS221解析后执行查询,参数:" + etpsPreentNo);
                    List<BisPassPort> bisPassPortList = new ArrayList<>();
                    if (listMap.get(etpsPreentNo) != null) {
                        bisPassPortList = listMap.get(etpsPreentNo);
                    } else {
                        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
                        PropertyFilter filter = new PropertyFilter("EQS_etpsPreentNo2", etpsPreentNo);
                        filters.add(filter);
                        bisPassPortList = passPortService.search(filters);
                        listMap.put(etpsPreentNo,bisPassPortList);
//                        logger.info("SAS221解析后执行查询,结果：" + JSON.toJSONString(bisPassPortList));
                    }

                    SAS221Mothed(fileName, file, ftpFile, bisPassPortList, manageResult,SAS221,hdeApprResultMap);
                }
            }
            if (BussinessData.get("SAS222") != null) {//核放单修改回执报文
//                logger.info("解析SAS222回执");
                SAS222Mothed();
            }
            if (BussinessData.get("SAS223") != null) {//核放单过卡回执报文
//                logger.info("解析SAS223回执");
                JSONObject SAS223 = JSONObject.parseObject(BussinessData.get("SAS223").toString());
                //清单审批回执
                if (SAS223.get("HdeApprResult") != null) {
                    JSONObject HdeApprResult = JSONObject.parseObject(SAS223.get("HdeApprResult").toString());
                    Map<String, Object> hdeApprResultMap = JSON.parseObject(HdeApprResult.toString());
                    if (hdeApprResultMap.get("etpsPreentNo") != null || hdeApprResultMap.get("etpsPreentNo").toString().trim().length() > 0) {
                        etpsPreentNo = hdeApprResultMap.get("etpsPreentNo").toString().trim();
                    }
                    if (hdeApprResultMap.get("manageResult") == null ){
                        manageResult = "";
                    }else{
                        manageResult = hdeApprResultMap.get("manageResult").toString();
                    }

//                    logger.info("SAS223解析后执行查询,参数:" + etpsPreentNo);
                    List<BisPassPort> bisPassPortList = new ArrayList<>();
                    if (listMap.get(etpsPreentNo)!=null){
                        bisPassPortList = listMap.get(etpsPreentNo);
                    }else{
                        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
                        PropertyFilter filter = new PropertyFilter("EQS_etpsPreentNo2", etpsPreentNo);
                        filters.add(filter);
                        bisPassPortList = passPortService.search(filters);
                        listMap.put(etpsPreentNo,bisPassPortList);
//                        logger.info("SAS223解析后执行查询,结果：" + JSON.toJSONString(bisPassPortList));
                    }
                    SAS223Mothed(fileName, file, ftpFile,bisPassPortList,manageResult);
                }
            }
            if (BussinessData.get("SAS224") != null) {//核放单查验处置回执报文
//                logger.info("解析SAS224回执");
                JSONObject SAS224 = JSONObject.parseObject(BussinessData.get("SAS224").toString());
                //清单审批回执
                if (SAS224.get("HdeApprResult") != null) {
                    JSONObject HdeApprResult = JSONObject.parseObject(SAS224.get("HdeApprResult").toString());
                    Map<String, Object> hdeApprResultMap = JSON.parseObject(HdeApprResult.toString());
                    if (hdeApprResultMap.get("etpsPreentNo") != null || hdeApprResultMap.get("etpsPreentNo").toString().trim().length() > 0) {
                        etpsPreentNo = hdeApprResultMap.get("etpsPreentNo").toString().trim();
                    }
                    if (hdeApprResultMap.get("manageResult") == null ){
                        manageResult = "";
                    }else{
                        manageResult = hdeApprResultMap.get("manageResult").toString();
                    }

//                    logger.info("SAS224解析后执行查询,参数:" + etpsPreentNo);
                    List<BisPassPort> bisPassPortList = new ArrayList<>();
                    if (listMap.get(etpsPreentNo)!=null){
                        bisPassPortList = listMap.get(etpsPreentNo);
                    }else{
                        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
                        PropertyFilter filter = new PropertyFilter("EQS_etpsPreentNo2", etpsPreentNo);
                        filters.add(filter);
                        bisPassPortList = passPortService.search(filters);
                        listMap.put(etpsPreentNo,bisPassPortList);
//                        logger.info("SAS224解析后执行查询,结果：" + JSON.toJSONString(bisPassPortList));
                    }

                    SAS224Mothed(fileName, file, ftpFile,bisPassPortList,manageResult);
                }
            }
            if (BussinessData.get("SAS231") != null) {//车辆信息审核回执报文
//                logger.info("解析SAS231回执");
                SAS231Mothed();
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

    //核放单回执报文
    public void SAS221Mothed(String fileName, File file, FTPFile ftpFile, List<BisPassPort> bisPassPortList, String manageResult, JSONObject SAS221,Map<String, Object> hdeApprResultMap) {
        try{
            if (bisPassPortList != null && bisPassPortList.size() == 1) {
//                logger.info("SAS221解析后执行查询有数据");
                for (BisPassPort forBisPassPort : bisPassPortList) {
                    //1-通过2-转人工3-退单Y-入库成功Z-入库失败
                    //状态-1-删除;0-新增;1-申报成功;4-成功发送海关;5-海关接收成功;6-海关接收失败;B-海关终审通过;C-退单;E-删除;T-转人工;Y-入库成功;Z-入库失败;
                    if ("6".equals(forBisPassPort.getLockage())){
                        continue;
                    }
                    if ("1".equals(manageResult)) {
                        forBisPassPort.setState("B");//通过
                        //反写核放单号
                        forBisPassPort.setPassportNo(hdeApprResultMap.get("businessId") == null ? "未获取到核放单号" : hdeApprResultMap.get("businessId").toString());
                    }
                    if ("2".equals(manageResult)) {
                        forBisPassPort.setState("T");//转人工
                        if (SAS221.get("CheckInfo") != null && SAS221.get("CheckInfo").toString().trim().length() > 0) {
                            JSONObject CheckInfo = JSONObject.parseObject(SAS221.get("CheckInfo").toString());
                            Map<String, Object> checkInfoMap = JSON.parseObject(CheckInfo.toString());
                            if (checkInfoMap.get("note") != null && checkInfoMap.get("note").toString().trim().length() > 0) {
                                forBisPassPort.setRmk(checkInfoMap.get("note").toString());//单一窗口回执返回备注信息
                            }
                        }
                    }
                    if ("3".equals(manageResult)) {
                        forBisPassPort.setState("C");//退单
                    }
                    if ("Y".equals(manageResult)) {
                        forBisPassPort.setState("Y");//入库成功
                    }
                    if ("Z".equals(manageResult)) {
                        forBisPassPort.setState("Z");//入库失败
                    }
                    forBisPassPort.setUpdateBy("SYSTEM");
                    forBisPassPort.setUpdateTime(new Date());
//                    logger.info("SAS221解析后执行修改");
                    passPortService.merge(forBisPassPort);
                }
//                //将文件挪放至日期下的预录入编号文件夹中
//                if (file != null && ftpFile == null) {
//                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
//                        try {
//                            logger.info("本地测试-copy");
//                            copyFile(etpsPreentNo, fileName, file, null);//本地测试
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                if (file == null && ftpFile != null) {
//                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
//                        try {
//                            logger.info("ftp-copy");
//                            copyFile(etpsPreentNo, fileName, null, ftpFile);//ftp
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
            }
        } catch (Exception e) {
            logger.info("异常："+e.getMessage());
        }
    }

    //核放单修改回执报文
    public void SAS222Mothed() {

    }

    //核放单过卡回执报文
    public void SAS223Mothed(String fileName, File file, FTPFile ftpFile, List<BisPassPort> bisPassPortList, String manageResult) {
        try{
            if (bisPassPortList != null && bisPassPortList.size() == 1) {
//                logger.info("SAS223解析后执行查询有数据");
                for (BisPassPort forBisPassPort : bisPassPortList) {
                    //1.已过卡2.未过卡
                    //0.已申请1.已审批2.已过卡3.已过一卡4.已过二卡5.已删除6.已作废
                    if ("6".equals(forBisPassPort.getLockage())){
                        continue;
                    }
                    if ("1".equals(manageResult)) {
                        forBisPassPort.setLockage("2");//已过卡
                        forBisPassPort.setUpdateBy("SYSTEM");
                        forBisPassPort.setUpdateTime(new Date());
//                        logger.info("SAS223解析后执行已过卡修改");
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
                    if ("2".equals(manageResult)) {
                        forBisPassPort.setLockage("0");//已申请
                        forBisPassPort.setUpdateBy("SYSTEM");
                        forBisPassPort.setUpdateTime(new Date());
//                        logger.info("SAS221解析后执行已申请修改");
                        passPortService.merge(forBisPassPort);
                    }
                }
//                //将文件挪放至日期下的预录入编号文件夹中
//                if (file != null && ftpFile == null) {
//                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
//                        try {
//                            logger.info("本地测试-copy");
//                            copyFile(etpsPreentNo, fileName, file, null);//本地测试
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                if (file == null && ftpFile != null) {
//                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
//                        try {
//                            logger.info("ftp-copy");
//                            copyFile(etpsPreentNo, fileName, null, ftpFile);//ftp
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
            }
        } catch (Exception e) {
            logger.info("异常："+e.getMessage());
        }
    }

    //核放单查验处置回执报文
    public void SAS224Mothed(String fileName, File file, FTPFile ftpFile, List<BisPassPort> bisPassPortList, String manageResult) {
        try{
            if (bisPassPortList != null && bisPassPortList.size() == 1) {
//                logger.info("SAS224解析后执行查询有数据");
                for (BisPassPort forBisPassPort : bisPassPortList) {
                    //2.拒绝过卡 3.卡口放行
                    //0.已申请1.已审批2.已过卡3.已过一卡4.已过二卡5.已删除6.已作废
                    if ("6".equals(forBisPassPort.getLockage())){
                        continue;
                    }
                    if ("2".equals(manageResult)) {
                        forBisPassPort.setLockage("6");//拒绝过卡
                    }
                    if ("3".equals(manageResult)) {
                        forBisPassPort.setLockage("1");//卡口放行
                    }
                    forBisPassPort.setUpdateBy("SYSTEM");
                    forBisPassPort.setUpdateTime(new Date());
//                    logger.info("SAS224解析后执行修改");
                    passPortService.merge(forBisPassPort);
                }
//                //将文件挪放至日期下的预录入编号文件夹中
//                if (file != null && ftpFile == null) {
//                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
//                        try {
//                            logger.info("本地测试-copy");
//                            copyFile(etpsPreentNo, fileName, file, null);//本地测试
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                if (file == null && ftpFile != null) {
//                    if (etpsPreentNo != null && etpsPreentNo.trim().length() > 0) {
//                        try {
//                            logger.info("ftp-copy");
//                            copyFile(etpsPreentNo, fileName, null, ftpFile);//ftp
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
            }
        } catch (Exception e) {
            logger.info("异常："+e.getMessage());
        }
    }

    //核放单修改回执报文
    public void SAS231Mothed() {

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
