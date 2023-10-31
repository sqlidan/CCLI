package com.haiersoft.ccli.wms.web.passPort;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import com.haiersoft.ccli.wms.service.passPort.PassPortInfoDJService;
import com.haiersoft.ccli.wms.service.passPort.PassPortInfoService;
import com.haiersoft.ccli.wms.service.passPort.PassPortService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryInfoService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryService;
import com.haiersoft.ccli.wms.web.preEntry.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * 获取单条核放单信息中的总重量
     */
    @RequestMapping(value = "checkTotalWt", method = RequestMethod.GET)
    @ResponseBody
    public String checkTotalWt(HttpServletRequest request) {
        Long start = System.currentTimeMillis();
        logger.info("STARTTIME:"+start+";MOTHED:checkTotalWt;PARAM:"+request.getParameter("PLATE_NO"));

        String totalWt = "";

        String PLATE_NO = request.getParameter("PLATE_NO");
        if(PLATE_NO == null || PLATE_NO.trim().length() == 0){
            return "承运车牌号为必填参数";
        }
        System.out.println("PLATE_NO："+PLATE_NO);
        totalWt = passPortService.getDataByVehicleNo(PLATE_NO);

        Long end = System.currentTimeMillis();
        logger.info("ENDTIME:"+end+";MOTHED:checkTotalWt;RESULT:"+totalWt);

        return totalWt;
    }


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
        page.orderBy("id").order(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("NEQS_state", "-1");//已删除
        filters.add(filter);
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
            /**
             * 1-申报
             * 2-通过
             * 3-作废
             */
            bisPassPort.setState(type);
            if ("1".equals(type)) {
                bisPassPort.setDclBy(user.getName());//申报人
                bisPassPort.setDclTime(new Date());//申报时间
                //调用申报核放单接口
                Map<String, Object> resultMap = passPortSave(id);
                if ("200".equals(resultMap.get("code").toString())) {
                    bisPassPort.setEtpsPreentNo2(resultMap.get("data").toString());
                } else {
                    return resultMap.get("msg").toString();
                }
            }
            if ("3".equals(type)) {
                //调用申报核放单接口
                Map<String, Object> resultMap = passPortNullify(id);
                if ("200".equals(resultMap.get("code").toString())) {
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

    /**
     * 同步
     */
    @RequestMapping(value = "synchronization/{id}")
    @ResponseBody
    public String synchronization(@PathVariable("id") String id) {
        User user = UserUtil.getCurrentUser();
        BisPassPort bisPassPort = passPortService.get(id);
        if (bisPassPort != null) {
            //调用申报核放单同步接口
            Map<String, Object> resultMap = passPortSynchronization(id);
            if ("200".equals(resultMap.get("code").toString())) {
                //同步成功
                System.out.println("同步成功");
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
            System.out.println("继续判断");
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
        //核放单类型为空车进出区时不校验表体和关联单据淑君
        if(!empty){
            //获取表体数据
            bisPassPortInfoList = passPortInfoService.getList(id);
            if (bisPassPortInfoList == null || bisPassPortInfoList.size() == 0) {
                resultMap.put("code","500");
                resultMap.put("msg","未获取到核放单表体数据");
                return resultMap;
            }
            //获取表体数据
            bisPassPortInfoDJList = passPortInfoDJService.getList(id);
            if (bisPassPortInfoDJList == null || bisPassPortInfoDJList.size() == 0) {
                resultMap.put("code","500");
                resultMap.put("msg","未获取到核放单关联单证数据");
                return resultMap;
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
        //核放单暂存
        System.out.println("passPortMessage== "+JSON.toJSONString(passPortMessage));
//        resultMap = PassPortSaveService(passPortMessage);
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
        System.out.println("sasCommonSeqNoRequest== "+JSON.toJSONString(sasCommonSeqNoRequest));
        resultMap = PassPortNullifyService(sasCommonSeqNoRequest);

        return resultMap;
    }

    //同步核放单
    public Map<String, Object> passPortSynchronization(String id){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code","200");
        resultMap.put("msg","success");
        resultMap.put("data","");
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

}
