package com.haiersoft.ccli.wms.web.passPort;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.bounded.service.BaseBoundedService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.tempuri.ReturnModel;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.apiEntity.*;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPort;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfo;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfoDJ;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfoDJ;
import com.haiersoft.ccli.wms.service.PreEntryInvtQuery.PreEntryInvtQueryService;
import com.haiersoft.ccli.wms.service.passPort.PassPortInfoDJService;
import com.haiersoft.ccli.wms.service.passPort.PassPortInfoService;
import com.haiersoft.ccli.wms.service.passPort.PassPortService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryInfoDJService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryInfoService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryService;
import com.haiersoft.ccli.wms.web.preEntry.HttpUtils;
import org.apache.commons.collections.ArrayStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * @ClassName: PassPortInfoController
 */
@Controller
@RequestMapping("wms/passPortInfo")
public class PassPortInfoController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(PassPortInfoController.class);

    @Autowired
    private PassPortService passPortService;
    @Autowired
    private PassPortInfoService passPortInfoService;
    @Autowired
    private PassPortInfoDJService passPortInfoDJService;
    @Autowired
    private PreEntryInvtQueryService preEntryInvtQueryService;

    private static final String memberCode = "eimskipMember";
    private static final String pass = "66668888";
    private static final String icCode = "2100030046252";

    /**
     * 信息
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request, @RequestParam("passPortId") String passPortId) {
        if (passPortId == null || passPortId.trim().length() == 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rows", new ArrayList<>());
            map.put("total", 0);
            return map;
        } else {
            Page<BisPassPortInfo> page = getPage(request);
            List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

            PropertyFilter filter = new PropertyFilter("EQS_passPortId", passPortId);
            filters.add(filter);
            page = passPortInfoService.search(page, filters);
            return getEasyUIData(page);
        }
    }

    /**
     * 获取单个信息
     */
    @RequestMapping(value = "getGdsInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getGdsInfo(HttpServletRequest request, @RequestParam("rltGdsSeqno") String rltGdsSeqno, @RequestParam("passPortId") String passPortId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BisPassPort queryBisPassPort = passPortService.get(passPortId);
            if(queryBisPassPort == null){
                map.put("code", "500");
                map.put("msg", "未查询到对应的表头信息!");
            }
            if(queryBisPassPort.getRltNo() == null || queryBisPassPort.getRltNo().trim().length() == 0){
                map.put("code", "500");
                map.put("msg", "未获取到关联单证编号!");
            }
            List<BaseBounded> baseBoundedList = new ArrayList<>();
            //原逻辑 2023-11-02作废
    //        baseBoundedList = passPortInfoService.getGdsInfo(rltGdsSeqno);
            //新逻辑
            baseBoundedList = invtQuery(queryBisPassPort.getRltNo(),rltGdsSeqno);
            if (baseBoundedList == null || baseBoundedList.size() == 0 || baseBoundedList.get(0) == null) {
                map.put("code", "200");
                map.put("data", null);
                map.put("msg", "success");
            } else {
                map.put("code", "200");
                map.put("data", baseBoundedList.get(0));
                map.put("msg", "success");
            }
        } catch (Exception e) {
            map.put("code", "500");
            map.put("msg", "error");
            e.printStackTrace();
        }
        return map;
    }

    //获取核注清单明细信息
    public List<BaseBounded> invtQuery(String bondInvtNo,String rltGdsSeqno) throws IOException, ClassNotFoundException {
        List<BaseBounded> baseBoundedList = new ArrayList<>();
        BaseBounded baseBounded = new BaseBounded();

        //查询保税核注清单列表
        InvtQueryListRequest invtQueryListRequest = new InvtQueryListRequest();
        invtQueryListRequest.setBondInvtNo(bondInvtNo);
        invtQueryListRequest.setMemberCode(memberCode);
        invtQueryListRequest.setPass(pass);
        invtQueryListRequest.setIcCode(icCode);
        //调用服务
        Map<String, Object> invtQueryListMap = InvtQueryListService(invtQueryListRequest);
        if("500".equals(invtQueryListMap.get("code").toString())){
            return baseBoundedList;
        }else{
            //处理结果
            InvtQueryListResponse invtQueryListResponse = (InvtQueryListResponse) invtQueryListMap.get("data");
            List<InvtQueryListResponseResultList> invtQueryListResponseResultLists = invtQueryListResponse.getResultList();
            if(invtQueryListResponseResultLists != null && invtQueryListResponseResultLists.size() >0){
                for (InvtQueryListResponseResultList forInvtQueryListResponseResultList:invtQueryListResponseResultLists) {
                    //查询保税核注清单详细
                    NemsCommonSeqNoRequest nemsCommonSeqNoRequest = new NemsCommonSeqNoRequest();
                    nemsCommonSeqNoRequest.setSeqNo(forInvtQueryListResponseResultList.getSeqNo());
                    nemsCommonSeqNoRequest.setIcCode(icCode);
                    nemsCommonSeqNoRequest.setMemberCode(memberCode);
                    nemsCommonSeqNoRequest.setPass(pass);
                    //调用服务
                    Map<String, Object> invtDetailMap = InvtDetailService(nemsCommonSeqNoRequest);
                    if("500".equals(invtDetailMap.get("code").toString())){
                        return baseBoundedList;
                    }else{
//                        //处理结果
                        InvtMessage invtMessage = (InvtMessage) invtDetailMap.get("data");
                        List<InvtListType> invtListTypeList = invtMessage.getInvtListType();

//                        BisPreEntryInvtQuery bisPreEntryInvtQuery = preEntryInvtQueryService.get("9C1E9CCE74F5480ABFC999586639D088");
//                        List<InvtListType> invtListTypeList = JSONArray.parseArray(JSON.toJSONString(ByteAryToObject(bisPreEntryInvtQuery.getInvtListType())),InvtListType.class);
                        for (InvtListType forInvtListType:invtListTypeList) {
                            if(forInvtListType.getGdsSeqno()!=null && forInvtListType.getGdsSeqno().equals(rltGdsSeqno)){
                                baseBounded.setGdsMtno(forInvtListType.getGdsMtno());
                                baseBounded.setHsCode(forInvtListType.getGdecd());
                                baseBounded.setHsItemname(forInvtListType.getGdsNm());
                                baseBounded.setDclUnit(forInvtListType.getDclUnitcd());
                                baseBounded.setDclQty(forInvtListType.getDclQty()==null?0:Double.parseDouble(forInvtListType.getDclQty()));
                                baseBounded.setDclTotalAmt(forInvtListType.getDclTotalAmt()==null?0:Double.parseDouble(forInvtListType.getDclTotalAmt()));
                                baseBounded.setGrossWeight(forInvtListType.getGrossWt()==null?0:Double.parseDouble(forInvtListType.getGrossWt()));
                                baseBounded.setNetWeight(forInvtListType.getNetWt()==null?0:Double.parseDouble(forInvtListType.getNetWt()));
                            }
                        }
                    }
                }
            }
        }
        baseBoundedList.add(baseBounded);
        return baseBoundedList;
    }

    /**
     * 核放单-新增
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> create(@Valid BisPassPort bisPassPort, Model model, HttpServletRequest request) {
        bisPassPort.setId(getNewPassPortId());
        bisPassPort.setState("0");//状态
        bisPassPort.setLockage("0");//过卡状态
        User user = UserUtil.getCurrentUser();
        bisPassPort.setCreateBy(user.getName());//创建人
        bisPassPort.setCreateTime(new Date());//创建时间
        passPortService.save(bisPassPort);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "200");
        map.put("msg", "success");
        map.put("id", bisPassPort.getId());
        return map;
    }

    public String getNewPassPortId() {
        User user = UserUtil.getCurrentUser();
        String userCode = user.getUserCode();
        //判断用户码是否为空
        if (StringUtils.isNull(user.getUserCode())) {
            userCode = "YZH";
        } else {//判断用户码 的长度
            if (userCode.length() > 3) {
                userCode = userCode.substring(0, 3);
            } else if (userCode.length() < 3) {
                userCode = StringUtils.lpadStringLeft(3, userCode);
            }
        }
        String linkId = "P" + userCode + StringUtils.timeToString();
        return linkId;
    }

    /**
     * 核放单-编辑
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid BisPassPort updateBisPassPort, Model model, HttpServletRequest request) {
        //保留原对象信息
        BisPassPort queryBisPassPort = passPortService.get(updateBisPassPort.getId());
        queryBisPassPort.setSeqNo(updateBisPassPort.getSeqNo());
        queryBisPassPort.setPassportNo(updateBisPassPort.getPassportNo());
        queryBisPassPort.setPassportTypecd(updateBisPassPort.getPassportTypecd());
        queryBisPassPort.setIoTypecd(updateBisPassPort.getIoTypecd());
        queryBisPassPort.setBindTypecd(updateBisPassPort.getBindTypecd());
        queryBisPassPort.setRltTbTypecd(updateBisPassPort.getRltTbTypecd());
        queryBisPassPort.setRltNo(updateBisPassPort.getRltNo());
        queryBisPassPort.setMasterCuscd(updateBisPassPort.getMasterCuscd());
        queryBisPassPort.setAreainOriactNo(updateBisPassPort.getAreainOriactNo());
        queryBisPassPort.setAreainEtpsno(updateBisPassPort.getAreainEtpsno());
        queryBisPassPort.setAreainEtpsSccd(updateBisPassPort.getAreainEtpsSccd());
        queryBisPassPort.setAreainEtpsNm(updateBisPassPort.getAreainEtpsNm());
        queryBisPassPort.setDclEtpsno(updateBisPassPort.getDclEtpsno());
        queryBisPassPort.setDclEtpsSccd(updateBisPassPort.getDclEtpsSccd());
        queryBisPassPort.setDclEtpsNm(updateBisPassPort.getDclEtpsNm());
        queryBisPassPort.setEtpsPreentNo(updateBisPassPort.getEtpsPreentNo());
        queryBisPassPort.setVehicleNo(updateBisPassPort.getVehicleNo());
        queryBisPassPort.setVehicleIcNo(updateBisPassPort.getVehicleIcNo());
        queryBisPassPort.setVehicleWt(updateBisPassPort.getVehicleWt());
        queryBisPassPort.setVehicleFrameNo(updateBisPassPort.getVehicleFrameNo());
        queryBisPassPort.setVehicleFrameWt(updateBisPassPort.getVehicleFrameWt());
        queryBisPassPort.setContainerNo(updateBisPassPort.getContainerNo());
        queryBisPassPort.setContainerType(updateBisPassPort.getContainerType());
        queryBisPassPort.setContainerWt(updateBisPassPort.getContainerWt());
        queryBisPassPort.setTotalGrossWt(updateBisPassPort.getTotalGrossWt());
        queryBisPassPort.setTotalNetWt(updateBisPassPort.getTotalNetWt());
        queryBisPassPort.setTotalWt(updateBisPassPort.getTotalWt());
        if (updateBisPassPort.getLockage()==null || updateBisPassPort.getLockage().trim().length() == 0){
            queryBisPassPort.setLockage("0");
        }else{
            queryBisPassPort.setLockage(updateBisPassPort.getLockage());
        }
        queryBisPassPort.setLockageTime1(updateBisPassPort.getLockageTime1());
        queryBisPassPort.setLockageTime2(updateBisPassPort.getLockageTime2());
        queryBisPassPort.setInputCode(updateBisPassPort.getInputCode());
        queryBisPassPort.setInputSccd(updateBisPassPort.getInputSccd());
        queryBisPassPort.setInputName(updateBisPassPort.getInputName());
        queryBisPassPort.setDclTypecd(updateBisPassPort.getDclTypecd());
        queryBisPassPort.setDclBy(updateBisPassPort.getDclBy());
        queryBisPassPort.setDclTime(updateBisPassPort.getDclTime());
        queryBisPassPort.setCol1(updateBisPassPort.getCol1());
        queryBisPassPort.setRmk(updateBisPassPort.getRmk());

        User user = UserUtil.getCurrentUser();
        queryBisPassPort.setUpdateBy(user.getName());//修改人
        queryBisPassPort.setUpdateTime(new Date());//修改时间
        passPortService.merge(queryBisPassPort);
        return "success";
    }

    /**
     * 添加表体信息
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid BisPassPortInfo bisPassPortInfo, @RequestParam("passPortId") String passPortId, Model model, HttpServletRequest request) {
        bisPassPortInfo.setPassPortId(passPortId);
        User user = UserUtil.getCurrentUser();
        bisPassPortInfo.setCreateBy(user.getName());//创建人
        bisPassPortInfo.setCreateTime(new Date());//创建时间
        passPortInfoService.save(bisPassPortInfo);
        return "success";
    }

    /**
     * 删除货物信息
     */
    @RequestMapping(value = "deleteinfo/{ids}")
    @ResponseBody
    public String deleteinfo(@PathVariable("ids") List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            passPortInfoService.delete(ids.get(i));
        }
        return "success";
    }

    /**
     * 关联单证列表
     */
    @RequestMapping(value = "jsonDJ", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getDJData(HttpServletRequest request, @RequestParam("passPortId") String passPortId) {
        if (passPortId == null || passPortId.trim().length() == 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rows", new ArrayList<>());
            map.put("total", 0);
            return map;
        } else {
            Page<BisPassPortInfoDJ> page = getPage(request);
            List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

            PropertyFilter filter = new PropertyFilter("EQS_passPortId", passPortId);
            filters.add(filter);
            page = passPortInfoDJService.search(page, filters);
            return getEasyUIData(page);
        }
    }

    /**
     * 打开关联单证上传弹窗
     */
    @RequestMapping(value = "createDJ", method = RequestMethod.GET)
    public String createDJ(Model model, @RequestParam("passPortId") String passPortId) {
        model.addAttribute("passPortId", passPortId);
        return "wms/passPort/passPortManagerUpload";
    }

    /**
     * 添加关联单证
     */
    @RequestMapping(value = "addDJ", method = RequestMethod.POST)
    @ResponseBody
    public String addDJ(@RequestParam("passPortId") String passPortId, HttpServletRequest request) {
        User user = UserUtil.getCurrentUser();
        BisPassPortInfoDJ bisPassPortInfoDJ = new BisPassPortInfoDJ();
        bisPassPortInfoDJ.setPassPortId(passPortId);
        bisPassPortInfoDJ.setCreateBy(user.getName());
        bisPassPortInfoDJ.setCreateTime(new Date());
        String remark = request.getParameter("remark");
        bisPassPortInfoDJ.setRemark(remark);
        String rltTbTypecd = request.getParameter("rltTbTypecd");
        bisPassPortInfoDJ.setRtlTbTypecd(rltTbTypecd);
        String rtlNo = request.getParameter("rtlNo");
        bisPassPortInfoDJ.setRtlNo(rtlNo);
        passPortInfoDJService.save(bisPassPortInfoDJ);
        return "success";
    }

    /**
     * 删除关联单证
     */
    @RequestMapping(value = "deleteDJinfo/{ids}")
    @ResponseBody
    public String deleteDJinfo(@PathVariable("ids") List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            passPortInfoDJService.delete(ids.get(i));
        }
        return "success";
    }

    /**
     * 提交关联单证
     */
    @RequestMapping(value = "submitDJinfo")
    @ResponseBody
    public String submitDJinfo(@RequestParam("passPortId") String passPortId) {
        BisPassPort bisPassPort = passPortService.get(passPortId);
        if (bisPassPort != null) {
            List<BisPassPortInfoDJ> bisPassPortInfoDJList = passPortInfoDJService.getList(bisPassPort.getId());
            if (bisPassPortInfoDJList == null || bisPassPortInfoDJList.size() == 0) {
                return "暂无随附数据，无需提交。";
            } else {
                return "提交成功";
            }
        }
        return "未获取到预录入信息";
    }
//======================================================================================================================

    /**
     * 获取下拉框数据
     */
    @RequestMapping(value = "dictData/{code}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> dictList(@PathVariable("code") String code) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<BisPreEntryDictData> bisPreEntryDictDataList = new ArrayList<>();
        bisPreEntryDictDataList = passPortInfoService.getDictDataByCode(code);
        if (bisPreEntryDictDataList == null || bisPreEntryDictDataList.size() == 0) {
            map.put("rows", new ArrayList<>());
            map.put("total", 0);
        } else {
            map.put("rows", bisPreEntryDictDataList);
            map.put("total", bisPreEntryDictDataList.size());
        }
        return map;
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
        return object + "";
    }
//================================================================================================================================


    /**
     * @return java.util.concurrent.Callable<com.chenay.bean.entity.customs.InvtQueryListResponse>
     * @Author chenp
     * @Description 保税核注清单列表查询服务
     * @Date 9:14 2021/1/30
     * @Param [invtQueryListRequest]
     **/
    public Map<String,Object> InvtQueryListService(InvtQueryListRequest invtQueryListRequest) {
        logger.info("保税核注清单列表查询服务:"+JSON.toJSONString(invtQueryListRequest));
        Map<String,Object> resultMap = new HashMap<>();
        InvtQueryListResponse baseResult = new InvtQueryListResponse();
        String dataStr = "";
        try {
            invtQueryListRequest.setKey(ApiKey.保税监管_保税核注清单查询服务秘钥.getValue());
            String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_保税核注清单列表查询服务接口.getValue(), JSON.toJSONString(invtQueryListRequest, SerializerFeature.WriteNullStringAsEmpty));
            JSONObject jsonObject = JSON.parseObject(s);
            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
            if ("200".equals(code)) {
                Object data = jsonObject.get("data");
                if (data != null) {
                    dataStr = data.toString();
                }
                baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), InvtQueryListResponse.class);
                logger.error("baseResult "+JSON.toJSONString(baseResult));
                resultMap.put("code","200");
                resultMap.put("msg","success");
                resultMap.put("data",baseResult);
            } else {
                Object data = jsonObject.get("msg");
                if (data != null) {
                    dataStr = data.toString();
                }
                logger.error("保税监管_保税核注清单列表查询服务接口"+invtQueryListRequest.getBondInvtNo(),"结果"+dataStr);
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
     * @return java.util.concurrent.Callable<com.chenay.bean.entity.customs.InvtMessage>
     * @Author chenp
     * @Description 保税核注清单详细查询服务
     * @Date 9:12 2021/1/30
     * @Param [nemsCommonSeqNoRequest]
     **/
    public Map<String,Object> InvtDetailService(NemsCommonSeqNoRequest nemsCommonSeqNoRequest) {
        logger.info("保税核注清单详细查询服务:"+JSON.toJSONString(nemsCommonSeqNoRequest));
        Map<String,Object> resultMap = new HashMap<>();
        InvtMessage baseResult = new InvtMessage();
        String dataStr = "";
        try {
            nemsCommonSeqNoRequest.setKey(ApiKey.保税监管_保税核注清单查询服务秘钥.getValue());
            String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_保税核注清单详细查询接口.getValue(), JSON.toJSONString(nemsCommonSeqNoRequest));
            JSONObject jsonObject = JSON.parseObject(s);
            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
            if ("200".equals(code)) {
                Object data = jsonObject.get("data");

                if (data != null) {
                    dataStr = data.toString();
                }
                baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), InvtMessage.class);
                resultMap.put("code","200");
                resultMap.put("msg","success");
                resultMap.put("data",baseResult);
            } else {
                Object data = jsonObject.get("msg");
                if (data != null) {
                    dataStr = data.toString();
                }
                logger.error("保税监管_保税核注清单详细查询接口"+nemsCommonSeqNoRequest.getBlsNo(),"结果"+dataStr);
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

    public static Object ByteAryToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        if(bytes == null){
            return null;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn = null;
        Object obj = null;
        sIn = new ObjectInputStream(in);
        obj = sIn.readObject();
        return obj;
    }

}
