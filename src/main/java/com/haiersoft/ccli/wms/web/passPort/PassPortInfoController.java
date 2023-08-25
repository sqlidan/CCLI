package com.haiersoft.ccli.wms.web.passPort;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.tempuri.ReturnModel;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.apiEntity.*;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPort;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfo;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfoDJ;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfoDJ;
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
     * 核放单-新增
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> create(@Valid BisPassPort bisPassPort, Model model, HttpServletRequest request) {
        bisPassPort.setId(getNewPassPortId());
        bisPassPort.setState("0");//状态
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
        queryBisPassPort.setLockage(updateBisPassPort.getLockage());
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

}
