package com.haiersoft.ccli.wms.web.preEntry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.apiEntity.*;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryInfoService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryService;
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
 * @ClassName: PreEntryController
 */
@Controller
@RequestMapping("wms/preEntry")
public class PreEntryController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(PreEntryController.class);

    @Autowired
    private PreEntryService preEntryService;
    @Autowired
    private PreEntryInfoService preEntryInfoService;

    private static final String memberCode = "eimskipMember";
    private static final String pass = "66668888";
    private static final String icCode = "2100030046252";

    /**
     * 预报单总览默认页面
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "wms/preEntry/preEntry";
    }

    /**
     * 预报单总览默认页面
     */
    @RequestMapping(value = "listBGH", method = RequestMethod.GET)
    public String listBGH() {
        return "wms/preEntry/preEntryBGH";
    }

    /**
     * 预报单
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<BisPreEntry> page = getPage(request);
        page.orderBy("forId").order(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("NEQS_state", "-1");//已删除
        filters.add(filter);
        page = preEntryService.search(page, filters);
        return getEasyUIData(page);
    }

    /**
     * 预报单管理默认页面（清空）
     */
    @RequestMapping(value = "manager", method = RequestMethod.GET)
    public String manager(Model model) {
        User user = UserUtil.getCurrentUser();
        model.addAttribute("preEntry", new BisPreEntry());
        model.addAttribute("date", new Date());
        model.addAttribute("user", user.getName());
        model.addAttribute("action", "create");
        return "wms/preEntry/preEntryManagerAdd";
    }

    /*
     * 获取新的预报单号
     */
    @RequestMapping(value = "getNewForId", method = RequestMethod.POST)
    @ResponseBody
    public String getNewForId() {
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
        String linkId = "F" + userCode + StringUtils.timeToString();
        return linkId;
    }

    /**
     * 判断预报单是否已保存
     */
    @RequestMapping(value = "ifsave/{forId}", method = RequestMethod.GET)
    @ResponseBody
    public String ifsave(@PathVariable("forId") String forId) {
        BisPreEntry bisPreEntry = preEntryService.get(forId);
        if (bisPreEntry != null && !"".equals(bisPreEntry)) {
            return "success";
        } else {
            return "false";
        }
    }

    /**
     * 添加预报单跳转
     *
     * @param model
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        User user = UserUtil.getCurrentUser();
        model.addAttribute("preEntry", new BisPreEntry());
        model.addAttribute("date", new Date());
        model.addAttribute("user", user.getName());
        model.addAttribute("action", "create");
        return "wms/preEntry/preEntryManagerAdd";
    }

    /**
     * 添加预报单
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid BisPreEntry bisPreEntry, Model model, HttpServletRequest request) {
        bisPreEntry.setCdSign(0);//报关标识
        bisPreEntry.setState("0");//状态
        bisPreEntry.setCtnCont(0);//箱量
        bisPreEntry.setUpAndDown("0");//未上传
        User user = UserUtil.getCurrentUser();
        bisPreEntry.setCreateBy(user.getName());//创建人
        bisPreEntry.setCreateTime(new Date());//创建时间
        preEntryService.save(bisPreEntry);
        return "success";
    }

    /**
     * 预报单修改
     */
    @RequestMapping(value = "update/{forId}", method = RequestMethod.GET)
    public String updateContractForm(Model model, @PathVariable("forId") String forId) {
        BisPreEntry bisPreEntry = preEntryService.get(forId);
        model.addAttribute("preEntry", bisPreEntry);
        model.addAttribute("action", "update");
        return "wms/preEntry/preEntryManagerEdit";
    }

    /**
     * 修改预报单
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid BisPreEntry bisPreEntry, Model model, HttpServletRequest request) {
        //保留原对象信息
        BisPreEntry queryBisPreEntry = preEntryService.get(bisPreEntry.getForId());
        if (isNotNullOrEmpty(bisPreEntry.getServiceProject())) {
            queryBisPreEntry.setServiceProject(bisPreEntry.getServiceProject());
        }
        if (isNotNullOrEmpty(bisPreEntry.getState())) {
            queryBisPreEntry.setState(bisPreEntry.getState());
        }
        if (isNotNullOrEmpty(bisPreEntry.getCreateBy())) {
            queryBisPreEntry.setCreateBy(bisPreEntry.getCreateBy());
        }
        if (isNotNullOrEmpty(bisPreEntry.getCreateTime())) {
            queryBisPreEntry.setCreateTime(bisPreEntry.getCreateTime());
        }
        if (isNotNullOrEmpty(bisPreEntry.getJlAudit())) {
            queryBisPreEntry.setJlAudit(bisPreEntry.getJlAudit());
        }
        if (isNotNullOrEmpty(bisPreEntry.getJlAuditTime())) {
            queryBisPreEntry.setJlAuditTime(bisPreEntry.getJlAuditTime());
        }
        if (isNotNullOrEmpty(bisPreEntry.getZgAudit())) {
            queryBisPreEntry.setZgAudit(bisPreEntry.getZgAudit());
        }
        if (isNotNullOrEmpty(bisPreEntry.getZgAuditTime())) {
            queryBisPreEntry.setZgAuditTime(bisPreEntry.getZgAuditTime());
        }
        if (isNotNullOrEmpty(bisPreEntry.getCdBy())) {
            queryBisPreEntry.setCdBy(bisPreEntry.getCdBy());
        }
        if (isNotNullOrEmpty(bisPreEntry.getCdTime())) {
            queryBisPreEntry.setCdTime(bisPreEntry.getCdTime());
        }
        if (isNotNullOrEmpty(bisPreEntry.getUpAndDown())) {
            queryBisPreEntry.setUpAndDown(bisPreEntry.getUpAndDown());
        }
        if (isNotNullOrEmpty(bisPreEntry.getUpBy())) {
            queryBisPreEntry.setUpBy(bisPreEntry.getUpBy());
        }
        if (isNotNullOrEmpty(bisPreEntry.getUpTime())) {
            queryBisPreEntry.setUpTime(bisPreEntry.getUpTime());
        }
        if (isNotNullOrEmpty(bisPreEntry.getUpFileName())) {
            queryBisPreEntry.setUpFileName(bisPreEntry.getUpFileName());
        }
        if (isNotNullOrEmpty(bisPreEntry.getCheckListNo())) {
            queryBisPreEntry.setCheckListNo(bisPreEntry.getCheckListNo());
        }
        if (isNotNullOrEmpty(bisPreEntry.getCdNum())) {
            queryBisPreEntry.setCdNum(bisPreEntry.getCdNum());
        }
        if (isNotNullOrEmpty(bisPreEntry.getClientId())) {
            queryBisPreEntry.setClientId(bisPreEntry.getClientId());
        }
        if (isNotNullOrEmpty(bisPreEntry.getClientName())) {
            queryBisPreEntry.setClientName(bisPreEntry.getClientName());
        }
        if (isNotNullOrEmpty(bisPreEntry.getDeclarationUnitId())) {
            queryBisPreEntry.setDeclarationUnitId(bisPreEntry.getDeclarationUnitId());
        }
        if (isNotNullOrEmpty(bisPreEntry.getDeclarationUnit())) {
            queryBisPreEntry.setDeclarationUnit(bisPreEntry.getDeclarationUnit());
        }
        if (isNotNullOrEmpty(bisPreEntry.getBillNum())) {
            queryBisPreEntry.setBillNum(bisPreEntry.getBillNum());
        }
        if (isNotNullOrEmpty(bisPreEntry.getCtnCont())) {
            queryBisPreEntry.setCtnCont(bisPreEntry.getCtnCont());
        }
        if (isNotNullOrEmpty(bisPreEntry.getTradeMode())) {
            queryBisPreEntry.setTradeMode(bisPreEntry.getTradeMode());
        }
        if (isNotNullOrEmpty(bisPreEntry.getCdSign())) {
            queryBisPreEntry.setCdSign(bisPreEntry.getCdSign());
        }
        if (isNotNullOrEmpty(bisPreEntry.getRemark())) {
            queryBisPreEntry.setRemark(bisPreEntry.getRemark());
        }
        if (isNotNullOrEmpty(bisPreEntry.getCustomerService())) {
            queryBisPreEntry.setCustomerService(bisPreEntry.getCustomerService());
        }
        if (isNotNullOrEmpty(bisPreEntry.getProductName())) {
            queryBisPreEntry.setProductName(bisPreEntry.getProductName());
        }
        if (isNotNullOrEmpty(bisPreEntry.getHsNo())) {
            queryBisPreEntry.setHsNo(bisPreEntry.getHsNo());
        }
        if (isNotNullOrEmpty(bisPreEntry.getPrice())) {
            queryBisPreEntry.setPrice(bisPreEntry.getPrice());
        }
        if (isNotNullOrEmpty(bisPreEntry.getNetWeight())) {
            queryBisPreEntry.setNetWeight(bisPreEntry.getNetWeight());
        }
        if (isNotNullOrEmpty(bisPreEntry.getConsignee())) {
            queryBisPreEntry.setConsignee(bisPreEntry.getConsignee());
        }
        if (isNotNullOrEmpty(bisPreEntry.getConsignor())) {
            queryBisPreEntry.setConsignor(bisPreEntry.getConsignor());
        }
        if (isNotNullOrEmpty(bisPreEntry.getContryOragin())) {
            queryBisPreEntry.setContryOragin(bisPreEntry.getContryOragin());
        }

        User user = UserUtil.getCurrentUser();
        queryBisPreEntry.setUpdateBy(user.getName());//修改人
        queryBisPreEntry.setUpdateTime(new Date());//修改时间
        preEntryService.merge(queryBisPreEntry);
        return "success";
    }

    /**
     * 预报单修改
     */
    @RequestMapping(value = "updateBGH/{forId}", method = RequestMethod.GET)
    public String updateBGH(Model model, @PathVariable("forId") String forId) {
        BisPreEntry bisPreEntry = preEntryService.get(forId);
        //录入单位编码
        if(!isNotNullOrEmpty(bisPreEntry.getLRDWBM())){
            bisPreEntry.setLRDWBM("3702631016");
        }
        //录入单位社会信用代码
        if(!isNotNullOrEmpty(bisPreEntry.getLRDWSHXYDM())){
            bisPreEntry.setLRDWSHXYDM("91370220395949850B");
        }
        //录入单位名称
        if(!isNotNullOrEmpty(bisPreEntry.getLRDWMC())){
            bisPreEntry.setLRDWMC("青岛港怡之航冷链物流有限公司");
        }
        model.addAttribute("bisPreEntry", bisPreEntry);
        BisPreEntryInfo bisPreEntryInfo = new BisPreEntryInfo();
        bisPreEntryInfo.setForId(forId);
        model.addAttribute("bisPreEntryInfo", bisPreEntryInfo);
        model.addAttribute("date", new Date());
        return "wms/preEntry/preEntryBGHManagerEdit";
    }

    /**
     * 修改预报单
     */
    @RequestMapping(value = "updateBGH", method = RequestMethod.POST)
    @ResponseBody
    public String updateBGH(@Valid BisPreEntry bisPreEntry, Model model, HttpServletRequest request) {
        //保留原对象信息
        BisPreEntry queryBisPreEntry = preEntryService.get(bisPreEntry.getForId());
        if (isNotNullOrEmpty(queryBisPreEntry.getServiceProject())) {
            bisPreEntry.setServiceProject(queryBisPreEntry.getServiceProject());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getState())) {
            bisPreEntry.setState(queryBisPreEntry.getState());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getCreateBy())) {
            bisPreEntry.setCreateBy(queryBisPreEntry.getCreateBy());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getCreateTime())) {
            bisPreEntry.setCreateTime(queryBisPreEntry.getCreateTime());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getJlAudit())) {
            bisPreEntry.setJlAudit(queryBisPreEntry.getJlAudit());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getJlAuditTime())) {
            bisPreEntry.setJlAuditTime(queryBisPreEntry.getJlAuditTime());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getZgAudit())) {
            bisPreEntry.setZgAudit(queryBisPreEntry.getZgAudit());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getZgAuditTime())) {
            bisPreEntry.setZgAuditTime(queryBisPreEntry.getZgAuditTime());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getCdBy())) {
            bisPreEntry.setCdBy(queryBisPreEntry.getCdBy());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getCdTime())) {
            bisPreEntry.setCdTime(queryBisPreEntry.getCdTime());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getUpAndDown())) {
            bisPreEntry.setUpAndDown(queryBisPreEntry.getUpAndDown());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getUpBy())) {
            bisPreEntry.setUpBy(queryBisPreEntry.getUpBy());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getUpTime())) {
            bisPreEntry.setUpTime(queryBisPreEntry.getUpTime());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getUpFileName())) {
            bisPreEntry.setUpFileName(queryBisPreEntry.getUpFileName());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getCheckListNo())) {
            bisPreEntry.setCheckListNo(queryBisPreEntry.getCheckListNo());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getCdNum())) {
            bisPreEntry.setCdNum(queryBisPreEntry.getCdNum());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getClientId())) {
            bisPreEntry.setClientId(queryBisPreEntry.getClientId());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getClientName())) {
            bisPreEntry.setClientName(queryBisPreEntry.getClientName());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getDeclarationUnitId())) {
            bisPreEntry.setDeclarationUnitId(queryBisPreEntry.getDeclarationUnitId());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getDeclarationUnit())) {
            bisPreEntry.setDeclarationUnit(queryBisPreEntry.getDeclarationUnit());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getBillNum())) {
            bisPreEntry.setBillNum(queryBisPreEntry.getBillNum());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getCtnCont())) {
            bisPreEntry.setCtnCont(queryBisPreEntry.getCtnCont());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getTradeMode())) {
            bisPreEntry.setTradeMode(queryBisPreEntry.getTradeMode());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getCdSign())) {
            bisPreEntry.setCdSign(queryBisPreEntry.getCdSign());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getRemark())) {
            bisPreEntry.setRemark(queryBisPreEntry.getRemark());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getCustomerService())) {
            bisPreEntry.setCustomerService(queryBisPreEntry.getCustomerService());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getProductName())) {
            bisPreEntry.setProductName(queryBisPreEntry.getProductName());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getHsNo())) {
            bisPreEntry.setHsNo(queryBisPreEntry.getHsNo());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getPrice())) {
            bisPreEntry.setPrice(queryBisPreEntry.getPrice());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getNetWeight())) {
            bisPreEntry.setNetWeight(queryBisPreEntry.getNetWeight());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getConsignee())) {
            bisPreEntry.setConsignee(queryBisPreEntry.getConsignee());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getConsignor())) {
            bisPreEntry.setConsignor(queryBisPreEntry.getConsignor());
        }
        if (isNotNullOrEmpty(queryBisPreEntry.getContryOragin())) {
            bisPreEntry.setContryOragin(queryBisPreEntry.getContryOragin());
        }

        User user = UserUtil.getCurrentUser();
        bisPreEntry.setUpdateBy(user.getName());//修改人
        bisPreEntry.setUpdateTime(new Date());//修改时间
        preEntryService.merge(bisPreEntry);
        return "success";
    }


    /**
     * 删除
     */
    @RequestMapping(value = "delete/{forId}")
    @ResponseBody
    public String deleteEnterStock(@PathVariable("forId") String forId) {
//        preEntryService.delete(forId);
        User user = UserUtil.getCurrentUser();
        BisPreEntry bisPreEntry = preEntryService.get(forId);
        bisPreEntry.setState("-1");//删除
        bisPreEntry.setUpdateBy(user.getName());//修改人
        bisPreEntry.setUpdateTime(new Date());//修改时间
        preEntryService.merge(bisPreEntry);
        return "success";
    }

    /**
     * 预报单查看
     */
    @RequestMapping(value = "updateCK/{forId}", method = RequestMethod.GET)
    public String updateCK(Model model, @PathVariable("forId") String forId) {
        BisPreEntry bisPreEntry = preEntryService.get(forId);
        model.addAttribute("bisPreEntry", bisPreEntry);
        BisPreEntryInfo bisPreEntryInfo = new BisPreEntryInfo();
        bisPreEntryInfo.setForId(forId);
        model.addAttribute("bisPreEntryInfo", bisPreEntryInfo);
        model.addAttribute("date", new Date());
        return "wms/preEntry/preEntryManagerDetail";
    }

    /**
     * 状态修改
     */
    @RequestMapping(value = "UpdateState/{forId}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public String jlUpdateOkContractForm(Model model, @PathVariable("forId") String forId, @PathVariable("type") String type, HttpServletRequest request) throws RemoteException, ServiceException {
        User user = UserUtil.getCurrentUser();
        BisPreEntry bisPreEntry = preEntryService.get(forId);
        if (bisPreEntry != null) {
            String state = bisPreEntry.getState();
            if ("jlUpdateOk".equals(type)) {//初审通过
                if(!"1".equals(state)){
                    return "预报单信息暂未完善提交，不能初审。";
                }
                bisPreEntry.setState("2");
                bisPreEntry.setJlAudit(user.getName());
                bisPreEntry.setJlAuditTime(new Date());
            } else if ("jlUpdateNo".equals(type)) {//初审驳回
                if(!"1".equals(state)){
                    return "预报单信息暂未完善提交，不能初审。";
                }
                bisPreEntry.setState("0");
                if(request.getParameter("reason")!=null && request.getParameter("reason").toString().trim().length() > 0){
                    bisPreEntry.setJlRejectReason(request.getParameter("reason").toString().trim());
                }else{
                    bisPreEntry.setJlRejectReason(null);
                }
                bisPreEntry.setJlAudit(user.getName());
                bisPreEntry.setJlAuditTime(new Date());
            } else if ("zgUpdateOk".equals(type)) {//复审通过
                if(!"2".equals(state)){
                    return "预报单信息暂未初审通过，不能复审。";
                }
                bisPreEntry.setState("3");
                bisPreEntry.setZgAudit(user.getName());
                bisPreEntry.setZgAuditTime(new Date());
            } else if ("zgUpdateNo".equals(type)) {//复审驳回
                if(!"2".equals(state)){
                    return "预报单信息暂未初审通过，不能复审。";
                }
                bisPreEntry.setState("1");
                if(request.getParameter("reason")!=null && request.getParameter("reason").toString().trim().length() > 0){
                    bisPreEntry.setZgRejectReason(request.getParameter("reason").toString().trim());
                }else{
                    bisPreEntry.setZgRejectReason(null);
                }
                bisPreEntry.setZgAudit(user.getName());
                bisPreEntry.setZgAuditTime(new Date());
            } else if ("bghSubmit".equals(type)) {//提交审核
                if(Integer.parseInt(state) > 0){
                    return "预报单信息已提交审核，不能再次提交。";
                }
                bisPreEntry.setState("1");
            } else if ("bghDeclarationCheckList".equals(type)) {//申报核注清单
                if(Integer.parseInt(state) < 3){
                    return "预报单信息暂未复审通过，不能申报。";
                }
                if(Integer.parseInt(state) > 3){
                    return "预报单信息已进行申报，不能再次申报。";
                }
                bisPreEntry.setState("4");
                //调用申报核注清单接口
                Map<String, Object> resultMap = getHZQDPort(forId);
                if ("200".equals(resultMap.get("code").toString())) {
                    bisPreEntry.setSeqNo(resultMap.get("data").toString());
                } else {
                    return resultMap.get("msg").toString();
                }
            }
            bisPreEntry.setUpdateBy(user.getName());//修改人
            bisPreEntry.setUpdateTime(new Date());//修改时间
            preEntryService.merge(bisPreEntry);
            return "success";
        } else {
            return "error";
        }
    }

    //申报核注清单
    public Map<String, Object> getHZQDPort(String forId) throws RemoteException, ServiceException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //获取表头数据
        BisPreEntry bisPreEntryHead = new BisPreEntry();
        List<BisPreEntry> bisPreEntryList = preEntryService.getList(forId);
        if (bisPreEntryList == null || bisPreEntryList.size() == 0) {
            resultMap.put("code","500");
            resultMap.put("msg","未获取到核注清单表头数据");
            return resultMap;
        } else {
            bisPreEntryHead = bisPreEntryList.get(0);
        }
        //获取表体数据
        List<BisPreEntryInfo> bisPreEntryInfoList = preEntryInfoService.getList(forId);
        if (bisPreEntryInfoList == null || bisPreEntryInfoList.size() == 0) {
            resultMap.put("code","500");
            resultMap.put("msg","未获取到核注清单表头数据");
            return resultMap;
        }

        //核注清单表头
        //表头参数字段（页面字段-报文字段）
        InvtHeadType invtHeadType = new InvtHeadType();
        invtHeadType.setAddTime(null);
        invtHeadType.setApplyNo(isNullOrEmpty(bisPreEntryHead.getSBBBH()));//(选填)申请表编号
        invtHeadType.setBizopEtpsNm(isNullOrEmpty(bisPreEntryHead.getJYDWMC()));//(必填)经营企业名称
        invtHeadType.setBizopEtpsno(isNullOrEmpty(bisPreEntryHead.getJYDWBM()));//(必填)经营企业编号
        invtHeadType.setBizopEtpsSccd(isNullOrEmpty(bisPreEntryHead.getJYDWSHXYDM()));//(选填)经营企业社会信用代码
        if(isNullOrEmpty(bisPreEntryHead.getQDBH()).length() > 0) {
            invtHeadType.setBondInvtNo(isNullOrEmpty(bisPreEntryHead.getQDBH()));//(选填)清单编号
        }else{
            invtHeadType.setBondInvtNo("");//(选填)清单编号
        }
        invtHeadType.setChgTmsCnt(isNullOrEmpty("0"));//(默认)变更次数
        invtHeadType.setDclcusTypecd(isNullOrEmpty(bisPreEntryHead.getBGLX()));//(选填)报关类型
        if(isNullOrEmpty(bisPreEntryHead.getDYBGDDWBM()).length() > 0) {
            invtHeadType.setCorrEntryDclEtpsNo(isNullOrEmpty(bisPreEntryHead.getDYBGDDWBM()));//(选填)对应报关单申报单位代码
        }else{
            invtHeadType.setCorrEntryDclEtpsNo("");//(选填)对应报关单申报单位代码
        }
        if(isNullOrEmpty(bisPreEntryHead.getDYBGDDWSHXYDM()).length() > 0) {
            invtHeadType.setCorrEntryDclEtpsSccd(isNullOrEmpty(bisPreEntryHead.getDYBGDDWSHXYDM()));//(选填)对应报关单申报单位社会信用代码
        }else{
            invtHeadType.setCorrEntryDclEtpsSccd("");//(选填)对应报关单申报单位社会信用代码
        }
        if(isNullOrEmpty(bisPreEntryHead.getDYBGDDWMC()).length() > 0) {
            invtHeadType.setCorrEntryDclEtpsNm(isNullOrEmpty(bisPreEntryHead.getDYBGDDWMC()));//(选填)对应报关单申报单位名称
        }else{
            invtHeadType.setCorrEntryDclEtpsNm("");//(选填)对应报关单申报单位名称
        }
//        if ("2".equals(invtHeadType.getDclcusTypecd().toString())) {
//            if (bisPreEntryHead.getDYBGDDWBM() == null || bisPreEntryHead.getDYBGDDWBM().toString().trim().length() == 0) {
//                return "如报关类型为关联报关时,对应报关单申报单位代码需必填";
//            } else {
//                invtHeadType.setCorrEntryDclEtpsNo(isNullOrEmpty(bisPreEntryHead.getDYBGDDWBM()));//(选填)对应报关单申报单位代码
//            }
//            invtHeadType.setCorrEntryDclEtpsSccd(isNullOrEmpty(bisPreEntryHead.getDYBGDDWSHXYDM()));//(选填)对应报关单申报单位社会信用代码
//            if (bisPreEntryHead.getDYBGDDWMC() == null || bisPreEntryHead.getDYBGDDWMC().toString().trim().length() == 0) {
//                return "如报关类型为关联报关时,对应报关单申报单位名称需必填";
//            } else {
//                invtHeadType.setCorrEntryDclEtpsNm(isNullOrEmpty(bisPreEntryHead.getDYBGDDWMC()));//(选填)对应报关单申报单位名称
//            }
//        } else {
//            invtHeadType.setCorrEntryDclEtpsNo(isNullOrEmpty(bisPreEntryHead.getDYBGDDWBM()));//(选填)对应报关单申报单位代码
//            invtHeadType.setCorrEntryDclEtpsSccd(isNullOrEmpty(bisPreEntryHead.getDYBGDDWSHXYDM()));//(选填)对应报关单申报单位社会信用代码
//            invtHeadType.setCorrEntryDclEtpsNm(isNullOrEmpty(bisPreEntryHead.getDYBGDDWMC()));//(选填)对应报关单申报单位名称
//        }
        invtHeadType.setDclEtpsno(isNullOrEmpty(bisPreEntryHead.getSBDWBM()));//(必填)申报企业编号
        invtHeadType.setDclEtpsSccd(isNullOrEmpty(bisPreEntryHead.getSBDWSHXYDM()));//(选填)申报企业社会信用代码
        invtHeadType.setDclEtpsNm(isNullOrEmpty(bisPreEntryHead.getSBDWMC()));//(必填)申报企业名称
        invtHeadType.setDclPlcCuscd(isNullOrEmpty(bisPreEntryHead.getZGHG()));//(必填)主管海关-申报地关区代码
        invtHeadType.setEntryDclTime(simpleDateFormat.format(bisPreEntryHead.getBGDSBRQ() == null ? (new Date()) : bisPreEntryHead.getBGDSBRQ()));//(选填)报关单申报日期
        invtHeadType.setEntryDclTime("");//(选填)报关单申报日期
        invtHeadType.setDclcusFlag(isNullOrEmpty(bisPreEntryHead.getBGBZ()));//(必填)是否报关标志
        invtHeadType.setDecType(isNullOrEmpty(bisPreEntryHead.getBGDLX()));//(必填)报关单类型
        if(isNullOrEmpty(bisPreEntryHead.getDYBGDBH()).length() > 0) {
            invtHeadType.setEntryNo(isNullOrEmpty(bisPreEntryHead.getDYBGDBH()));//(选填)对应报关单编号
        }else{
            invtHeadType.setEntryNo("");//(选填)对应报关单编号
        }
//        if (("1".equals(invtHeadType.getDclcusFlag().toString()) && ("X".equals(invtHeadType.getDecType().toString()) || "Y".equals(invtHeadType.getDecType().toString())) && ("2".equals(invtHeadType.getDclcusTypecd().toString())))) {
//            if (bisPreEntryHead.getDYBGDBH() == null || bisPreEntryHead.getDYBGDBH().toString().trim().length() == 0) {
//                return "如报关,进口两步申报报关单或进口两步申报备案清单，关联报关时，对应报关单编号需必填";
//            } else {
//                invtHeadType.setEntryNo(isNullOrEmpty(bisPreEntryHead.getDYBGDBH()));//(选填)对应报关单编号
//            }
//        } else {
//            invtHeadType.setEntryNo(isNullOrEmpty(bisPreEntryHead.getDYBGDBH()));//(选填)对应报关单编号
//        }
        invtHeadType.setEtpsInnerInvtNo(isNullOrEmpty(bisPreEntryHead.getQYNBBH()));//(选填)企业内部清单编号
        invtHeadType.setIcCardNo(isNullOrEmpty(bisPreEntryHead.getCZYKH()));//(选填)申报人IC卡号
        if (0 == Integer.parseInt(bisPreEntryHead.getServiceProject()) || "I".equals(bisPreEntryHead.getServiceProject().toString().trim())) {//报进
            invtHeadType.setImpexpMarkcd("I");//(必填)进出口标记代码
        } else if (1 == Integer.parseInt(bisPreEntryHead.getServiceProject()) || "E".equals(bisPreEntryHead.getServiceProject().toString().trim())) {//报出
            invtHeadType.setImpexpMarkcd("E");//(必填)进出口标记代码
        } else {
            resultMap.put("code","500");
            resultMap.put("msg","服务项目数据异常");
            return resultMap;
        }
        invtHeadType.setImpexpPortcd(isNullOrEmpty(bisPreEntryHead.getJCJGB()));//(必填)进出境关别
        invtHeadType.setInputCode("3702631016");
        invtHeadType.setInputCreditCode("91370220395949850B");
        invtHeadType.setInputName("青岛港怡之航冷链物流有限公司");
        invtHeadType.setInputTime(simpleDateFormat.format(bisPreEntryHead.getLRRQ() == null ? (new Date()) : bisPreEntryHead.getLRRQ()));//(选填)录入日期
        invtHeadType.setInvtDclTime(simpleDateFormat.format(bisPreEntryHead.getQDSBRQ() == null ? (new Date()) : bisPreEntryHead.getQDSBRQ()));//(选填)清单申报时间
        invtHeadType.setInvtIochkptStucd(isNullOrEmpty(bisPreEntryHead.getQGJCKKZT()));//(选填)清单进出卡口状态代码
        invtHeadType.setInvtType(isNullOrEmpty(bisPreEntryHead.getQDLX()));//(必填)清单类型
        invtHeadType.setListType(isNullOrEmpty(bisPreEntryHead.getLZLX()));//(必填)流转类型
        invtHeadType.setMtpckEndprdMarkcd(isNullOrEmpty(bisPreEntryHead.getLJCPBZ()));//(必填)料件成品标记代码
        invtHeadType.setPutrecNo(isNullOrEmpty(bisPreEntryHead.getZCBH()));//(必填)账册(备案)编号
        invtHeadType.setRcvgdEtpsNm(isNullOrEmpty(bisPreEntryHead.getJGDWMC()));//(必填)加工单位名称-收货企业名称
        invtHeadType.setRcvgdEtpsno(isNullOrEmpty(bisPreEntryHead.getJGDWBM()));//(必填)加工单位编号-收货企业编号
//        if ("1".equals(invtHeadType.getDclcusTypecd().toString())) {
//            if (bisPreEntryHead.getBLBGDJNSFHRBM() == null || bisPreEntryHead.getBLBGDJNSFHRBM().toString().trim().length() == 0) {
//                return "如报关类型为关联报关时,关联报关单境内收发货人编号需必填";
//            } else {
//                invtHeadType.setRltEntryBizopEtpsno(isNullOrEmpty(bisPreEntryHead.getBLBGDJNSFHRBM()));//(选填)关联报关单境内收发货人编号
//            }
//            if (bisPreEntryHead.getBLBGDJNSFHRMC() == null || bisPreEntryHead.getBLBGDJNSFHRMC().toString().trim().length() == 0) {
//                return "如报关类型为关联报关时,关联报关单境内收发货人名称需必填";
//            } else {
//                invtHeadType.setRltEntryBizopEtpsNm(isNullOrEmpty(bisPreEntryHead.getBLBGDJNSFHRMC()));//(必填)关联报关单境内收发货人名称
//            }
//            invtHeadType.setRltEntryBizopEtpsSccd(isNullOrEmpty(bisPreEntryHead.getBLBGDJNSFHRSHXYDM()));//(选填)关联报关单境内收发货人社会信用代码
//            if (bisPreEntryHead.getBLBGDSBDWMC() == null || bisPreEntryHead.getBLBGDSBDWMC().toString().trim().length() == 0) {
//                return "如报关类型为关联报关时,关联报关单申报单位名称需必填";
//            } else {
//                invtHeadType.setRltEntryDclEtpsNm(isNullOrEmpty(bisPreEntryHead.getBLBGDSBDWMC()));//(选填)关联报关单申报单位名称
//            }
//            if (bisPreEntryHead.getBLBGDSBDWBM() == null || bisPreEntryHead.getBLBGDSBDWBM().toString().trim().length() == 0) {
//                return "如报关类型为关联报关时,关联报关单申报单位编号需必填";
//            } else {
//                invtHeadType.setRltEntryDclEtpsno(isNullOrEmpty(bisPreEntryHead.getBLBGDSBDWBM()));//(选填)关联报关单申报单位编号
//            }
//            invtHeadType.setRltEntryDclEtpsSccd(isNullOrEmpty(bisPreEntryHead.getBLBGDSBDWSHXYDM()));//(选填)关联报关单申报单位社会统一信用代码
//            if (bisPreEntryHead.getGLBGDSCXSDWBM() == null || bisPreEntryHead.getGLBGDSCXSDWBM().toString().trim().length() == 0) {
//                return "如报关类型为关联报关时,关联报关单生产销售(消费使用)单位编码需必填";
//            } else {
//                invtHeadType.setRltEntryRcvgdEtpsno(isNullOrEmpty(bisPreEntryHead.getGLBGDSCXSDWBM()));//(选填)关联报关单生产销售(消费使用)单位编码
//            }
//            invtHeadType.setRltEntryRvsngdEtpsSccd(isNullOrEmpty(bisPreEntryHead.getGLBGDSCXSDWSHXYDM()));//(选填)关联报关单生产销售(消费使用)单位社会信用代码
//            if (bisPreEntryHead.getGLBGDSCXSDWMC() == null || bisPreEntryHead.getGLBGDSCXSDWMC().toString().trim().length() == 0) {
//                return "如报关类型为关联报关时,关联报关单生产销售(消费使用)单位名称需必填";
//            } else {
//                invtHeadType.setRltEntryRcvgdEtpsNm(isNullOrEmpty(bisPreEntryHead.getGLBGDSCXSDWMC()));//(选填)关联报关单生产销售(消费使用)单位名称
//            }
//        } else {
        if(isNullOrEmpty(bisPreEntryHead.getBLBGDJNSFHRBM()).length() > 0) {
            invtHeadType.setRltEntryBizopEtpsno(isNullOrEmpty(bisPreEntryHead.getBLBGDJNSFHRBM()));//(选填)关联报关单境内收发货人编号
        }else{
            invtHeadType.setRltEntryBizopEtpsno("");//(选填)关联报关单境内收发货人编号
        }
        if(isNullOrEmpty(bisPreEntryHead.getBLBGDJNSFHRMC()).length() > 0) {
            invtHeadType.setRltEntryBizopEtpsNm(isNullOrEmpty(bisPreEntryHead.getBLBGDJNSFHRMC()));//(必填)关联报关单境内收发货人名称
        }else{
            invtHeadType.setRltEntryBizopEtpsNm("");//(选填)关联报关单境内收发货人名称
        }
        if(isNullOrEmpty(bisPreEntryHead.getBLBGDJNSFHRSHXYDM()).length() > 0) {
            invtHeadType.setRltEntryBizopEtpsSccd(isNullOrEmpty(bisPreEntryHead.getBLBGDJNSFHRSHXYDM()));//(选填)关联报关单境内收发货人社会信用代码
        }else{
            invtHeadType.setRltEntryBizopEtpsSccd("");//(选填)关联报关单境内收发货人社会信用代码
        }
        if(isNullOrEmpty(bisPreEntryHead.getBLBGDSBDWMC()).length() > 0) {
            invtHeadType.setRltEntryDclEtpsNm(isNullOrEmpty(bisPreEntryHead.getBLBGDSBDWMC()));//(选填)关联报关单申报单位名称
        }else{
            invtHeadType.setRltEntryDclEtpsNm("");//(选填)关联报关单申报单位名称
        }
        if(isNullOrEmpty(bisPreEntryHead.getBLBGDSBDWBM()).length() > 0) {
            invtHeadType.setRltEntryDclEtpsno(isNullOrEmpty(bisPreEntryHead.getBLBGDSBDWBM()));//(选填)关联报关单申报单位编号
        }else{
            invtHeadType.setRltEntryDclEtpsno("");//(选填)关联报关单申报单位编号
        }
        if(isNullOrEmpty(bisPreEntryHead.getBLBGDSBDWSHXYDM()).length() > 0) {
            invtHeadType.setRltEntryDclEtpsSccd(isNullOrEmpty(bisPreEntryHead.getBLBGDSBDWSHXYDM()));//(选填)关联报关单申报单位社会统一信用代码
        }else{
            invtHeadType.setRltEntryDclEtpsSccd("");//(选填)关联报关单申报单位社会统一信用代码
        }
        if(isNullOrEmpty(bisPreEntryHead.getGLBGDSCXSDWBM()).length() > 0) {
            invtHeadType.setRltEntryRcvgdEtpsno(isNullOrEmpty(bisPreEntryHead.getGLBGDSCXSDWBM()));//(选填)关联报关单生产销售(消费使用)单位编码
        }else{
            invtHeadType.setRltEntryRcvgdEtpsno("");//(选填)关联报关单生产销售(消费使用)单位编码
        }
        if(isNullOrEmpty(bisPreEntryHead.getGLBGDSCXSDWMC()).length() > 0) {
            invtHeadType.setRltEntryRcvgdEtpsNm(isNullOrEmpty(bisPreEntryHead.getGLBGDSCXSDWMC()));//(选填)关联报关单生产销售(消费使用)单位名称
        }else{
            invtHeadType.setRltEntryRcvgdEtpsNm("");//(选填)关联报关单生产销售(消费使用)单位名称
        }
        if(isNullOrEmpty(bisPreEntryHead.getGLBGDSCXSDWSHXYDM()).length() > 0) {
            invtHeadType.setRltEntryRvsngdEtpsSccd(isNullOrEmpty(bisPreEntryHead.getGLBGDSCXSDWSHXYDM()));//(选填)关联报关单生产销售(消费使用)单位社会信用代码
        }else{
            invtHeadType.setRltEntryRvsngdEtpsSccd("");//(选填)关联报关单生产销售(消费使用)单位社会信用代码
        }
//        }
        if(isNullOrEmpty(bisPreEntryHead.getGLBGDBH()).length() > 0) {
            invtHeadType.setRltEntryNo(isNullOrEmpty(bisPreEntryHead.getGLBGDBH()));//(选填)关联报关单编号
        }else{
            invtHeadType.setRltEntryNo("");//(选填)关联报关单编号
        }
//        if (("1".equals(invtHeadType.getDclcusFlag().toString()) && ("X".equals(invtHeadType.getDecType().toString()) || "Y".equals(invtHeadType.getDecType().toString())) && ("1".equals(invtHeadType.getDecType().toString())))) {
//            if (bisPreEntryHead.getGLBGDBH() == null || bisPreEntryHead.getGLBGDBH().toString().trim().length() == 0) {
//                return "如报关,进口两步申报报关单或进口两步申报备案清单，关联报关时，关联报关单编号需必填";
//            } else {
//                invtHeadType.setRltEntryNo(isNullOrEmpty(bisPreEntryHead.getGLBGDBH()));//(选填)关联报关单编号
//            }
//        } else {
//            invtHeadType.setRltEntryNo(isNullOrEmpty(bisPreEntryHead.getGLBGDBH()));//(选填)关联报关单编号
//        }
        invtHeadType.setRltInvtNo(isNullOrEmpty(bisPreEntryHead.getGLQDBH()));//(选填)关联清单编号
        invtHeadType.setRltPutrecNo(isNullOrEmpty(bisPreEntryHead.getGLSCBAH()));//(选填)关联备案编号
        invtHeadType.setRmk(isNullOrEmpty(bisPreEntryHead.getBZ()));//(选填)备注
        invtHeadType.setRvsngdEtpsSccd(isNullOrEmpty(bisPreEntryHead.getJGDWSHXYDM()));//(选填)加工单位社会信用代码-收发货企业社会信用代码
        if(isNullOrEmpty(bisPreEntryHead.getYLRTYBH()).length() > 0) {
            invtHeadType.setSeqNo(isNullOrEmpty(bisPreEntryHead.getYLRTYBH()));//(选填)清单预录入统一编号
        }else{
            invtHeadType.setSeqNo("");//(选填)清单预录入统一编号
        }
        invtHeadType.setStshipTrsarvNatcd(isNullOrEmpty(bisPreEntryHead.getQYG()));//(必填)起运/运抵国(地区）
        invtHeadType.setSupvModecd(isNullOrEmpty(bisPreEntryHead.getJGFS()));//(必填)监管方式代码
        invtHeadType.setTrspModecd(isNullOrEmpty(bisPreEntryHead.getYSFS()));//(必填)运输方式代码
        invtHeadType.setVrfdedMarkcd("0");//(选填)核扣标记代码
        invtHeadType.setGenDecFlag(isNullOrEmpty(bisPreEntryHead.getSFXTSCBGD()));//(必填)是否生成报关单
        invtHeadType.setEntrySeqNo(isNullOrEmpty(bisPreEntryHead.getYSFS()));//(必填)运输方式代码
        invtHeadType.setDclTypecd("1");//(默认)申报类型 1-备案申请 2-变更申请 3-删除申请
        invtHeadType.setEntryStucd("");//(选填)对应报关单编号
//        invtHeadType.setEepsInnerInvtNo(null);
//        invtHeadType.setPrevdTime(null);//(选填)预核扣时间
//        invtHeadType.setFormalVrfdedTime(null);//(选填)正式核扣时间
        invtHeadType.setListStat("");//(选填)清单状态
//        invtHeadType.setParam1(null);//(选填)
//        invtHeadType.setParam2(null);//(选填)
//        invtHeadType.setParam3(null);//(选填)
//        invtHeadType.setParam4(null);//(选填)
//        invtHeadType.setPassportUsedTypeCd(null);//(选填)核放单生成标志代码
        invtHeadType.setNeedEntryModified("");//(选填)报关单同步修改标志
//        invtHeadType.setLevyBlAmt(null);//(选填)计征金额

        // 表体参数字段
        List<InvtListType> invtListTypeList = new ArrayList<>();
        for (BisPreEntryInfo forBisPreEntryInfo : bisPreEntryInfoList) {
            InvtListType invtListType = new InvtListType();
            if(isNullOrEmpty(invtHeadType.getSeqNo()).length() > 0) {
                invtListType.setSeqNo(invtHeadType.getSeqNo());//中心统一编号
            }else{
                invtListType.setSeqNo("");//中心统一编号
            }
            if(isNullOrEmpty(forBisPreEntryInfo.getXh()).length() > 0) {
                invtListType.setGdsSeqno(isNullOrEmpty(forBisPreEntryInfo.getXh()));//(必填)序号
            }
            if(isNullOrEmpty(forBisPreEntryInfo.getBaxh()).length() > 0) {
                invtListType.setPutrecSeqno(isNullOrEmpty(forBisPreEntryInfo.getBaxh()));//(必填)备案序号（对应底账序号）
            }
            invtListType.setGdsMtno(isNullOrEmpty(forBisPreEntryInfo.getSplh()));//(必填)商品料号
            invtListType.setGdecd(isNullOrEmpty(forBisPreEntryInfo.getSpxh()));//(必填)商品编码
            invtListType.setGdsNm(isNullOrEmpty(forBisPreEntryInfo.getSpmc()));//(必填)商品名称
            invtListType.setGdsSpcfModelDesc(isNullOrEmpty(forBisPreEntryInfo.getGgxh()));//(必填)商品规格型号
            invtListType.setDclUnitcd(isNullOrEmpty(forBisPreEntryInfo.getSbjldw()));//(必填)申报计量单位
            invtListType.setLawfUnitcd(isNullOrEmpty(forBisPreEntryInfo.getFddejldw()));//(选填)法定计量单位
            if(isNullOrEmpty(forBisPreEntryInfo.getFddejldw()).length() > 0) {
                invtListType.setSecdLawfUnitcd(isNullOrEmpty(forBisPreEntryInfo.getFddejldw()));//(选填)法定第二计量单位
            }
            invtListType.setNatcd(isNullOrEmpty(forBisPreEntryInfo.getYcg()));//(必填)原产国(地区)
            if(isNullOrEmpty(forBisPreEntryInfo.getQysbdj()).length() > 0){
                invtListType.setDclUprcAmt(isNullOrEmpty(forBisPreEntryInfo.getQysbdj()));//(必填)企业申报单价
            }
            if(isNullOrEmpty(forBisPreEntryInfo.getQysbzj()).length() > 0){
                invtListType.setDclTotalAmt(isNullOrEmpty(forBisPreEntryInfo.getQysbzj()));//(必填)企业申报总价
            }
            if(isNullOrEmpty(forBisPreEntryInfo.getMytjzje()).length() > 0){
                invtListType.setUsdStatTotalAmt(isNullOrEmpty(forBisPreEntryInfo.getMytjzje()));//(必填)美元统计总金额
            }
            if(isNullOrEmpty(forBisPreEntryInfo.getBzt()).length() > 0){
                invtListType.setDclCurrcd(isNullOrEmpty(forBisPreEntryInfo.getBzt()));//(必填)币制
            }
            if(isNullOrEmpty(forBisPreEntryInfo.getFdsl()).length() > 0){
                invtListType.setLawfQty(isNullOrEmpty(forBisPreEntryInfo.getFdsl()));//(必填)法定数量
            }
//            if (forBisPreEntryInfo.getFddejldw() == null || forBisPreEntryInfo.getFddejldw().toString().trim().length() == 0) {
//                if(isNullOrEmpty(forBisPreEntryInfo.getDefdsl()).length() > 0) {
//                    invtListType.setSecdLawfQty(forBisPreEntryInfo.getDefdsl());//(选填)第二法定数量
//                }
//            } else {
//                if (forBisPreEntryInfo.getDefdsl() == null || forBisPreEntryInfo.getDefdsl().toString().trim().length() == 0) {
//                    return "法定第二计量单位不为空时，第二法定数量必填";
//                } else {
//                    if(isNullOrEmpty(forBisPreEntryInfo.getDefdsl()).length() > 0) {
//                        invtListType.setSecdLawfQty(forBisPreEntryInfo.getDefdsl());//(选填)第二法定数量
//                    }
//                }
//            }
//            if(isNullOrEmpty(forBisPreEntryInfo.getZlblyz()).length() > 0){
//                invtListType.setWtSfVal(forBisPreEntryInfo.getZlblyz());//(选填)重量比例因子
//            }
//            if(isNullOrEmpty(forBisPreEntryInfo.getDyblyz()).length() > 0) {
//                invtListType.setFstSfVal(isNullOrEmpty(forBisPreEntryInfo.getDyblyz()));//(选填)第一比例因子
//            }
//            if(isNullOrEmpty(forBisPreEntryInfo.getDeblyz()).length() > 0) {
//                invtListType.setSecdSfVal(isNullOrEmpty(forBisPreEntryInfo.getDeblyz()));//(选填)第二比例因子
//            }
            if(isNullOrEmpty(forBisPreEntryInfo.getSbsl()).length() > 0) {
                invtListType.setDclQty(isNullOrEmpty(forBisPreEntryInfo.getSbsl()));//(必填)申报数量
            }
//            if(isNullOrEmpty(forBisPreEntryInfo.getMz()).length() > 0) {
//                invtListType.setGrossWt(isNullOrEmpty(forBisPreEntryInfo.getMz()));//(选填)毛重
//            }
//            if(isNullOrEmpty(forBisPreEntryInfo.getJz()).length() > 0) {
//                invtListType.setNetWt(isNullOrEmpty(forBisPreEntryInfo.getJz()));//(选填)净重
//            }

            invtListType.setUseCd(isNullOrEmpty(forBisPreEntryInfo.getYtdm()));//(选填)用途代码 取消该字段使用；不需要填写
            invtListType.setLvyrlfModecd(isNullOrEmpty(forBisPreEntryInfo.getZmfs()));//(必填)征免方式
            if(isNullOrEmpty(forBisPreEntryInfo.getDhbbh()).length() > 0) {
                invtListType.setUcnsVerno(isNullOrEmpty(forBisPreEntryInfo.getDhbbh()));//(选填)单耗版本号
            }else{
                invtListType.setUcnsVerno("");//(选填)单耗版本号
            }
            if(isNullOrEmpty(forBisPreEntryInfo.getBgdspxh()).length() > 0) {
                invtListType.setEntryGdsSeqno(isNullOrEmpty(forBisPreEntryInfo.getBgdspxh()));//(选填)报关单商品序号
            }else{
                invtListType.setEntryGdsSeqno("");//(选填)报关单商品序号
            }
            if(isNullOrEmpty(forBisPreEntryInfo.getLzsbbxh()).length() > 0) {
                invtListType.setApplyTbSeqno(isNullOrEmpty(forBisPreEntryInfo.getLzsbbxh()));//(选填)流转申报表序号
            }else{
                invtListType.setApplyTbSeqno("");//(选填)流转申报表序号
            }
            invtListType.setRmk(isNullOrEmpty(forBisPreEntryInfo.getRemark()));//(选填)备注
            invtListType.setDestinationNatcd(isNullOrEmpty(forBisPreEntryInfo.getZzmdg()));//(必填)最终目的国
            invtListType.setModfMarkcd("0");//(必填)修改标志 0-未修改 1-修改 2-删除 3-增加
            invtListType.setClyMarkcd("0");//(选填)归类标记代码
//            invtListType.setActlPassQty(null);

            invtListTypeList.add(invtListType);
        }

        InvtMessage invtVo = new InvtMessage();

        invtVo.setInvtHeadType(invtHeadType);//核注清单表头
        invtVo.setInvtListType(invtListTypeList);//核注清单表体
        invtVo.setMemberCode(memberCode);
        invtVo.setPass(pass);
        invtVo.setIcCode(icCode);
        //核注清单暂存
        System.out.println("invtVo== "+JSON.toJSONString(invtVo));
        resultMap = InvtSaveService(invtVo);

        //保税核注清单列表查询服务
//        InvtQueryListRequest invtQueryListRequest = new InvtQueryListRequest();
//        invtQueryListRequest.setBondInvtNo("QD423023E000001087");
//        invtQueryListRequest.setMemberCode(memberCode);
//        invtQueryListRequest.setPass(pass);
//        invtQueryListRequest.setIcCode(icCode);
//        Map<String, Object> resultMap = InvtQueryListService(invtQueryListRequest);

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
        bisPreEntryDictDataList = preEntryService.getDictDataByCode(code);
        if(bisPreEntryDictDataList == null || bisPreEntryDictDataList.size() == 0){
            map.put("rows", new ArrayList<>());
            map.put("total", 0);
        }else{
            map.put("rows", bisPreEntryDictDataList);
            map.put("total", bisPreEntryDictDataList.size());
        }
        return map;
    }

//======================================================================================================================
    /**
     * 跳转上传报关单画面
     */
    @RequestMapping(value = "fileUploadH/{forId}", method = RequestMethod.GET)
    public String fileUploadH(Model model, @PathVariable("forId") String forId) {
        model.addAttribute("action", "fileUpload");
        model.addAttribute("forId", forId);
        return "wms/preEntry/preEntryUpload";
    }

    //存放地址
    private static String url = "D://BGD";
    /***
     * 单个文件上传
     */
    @ResponseBody
    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    public ResponseVo fileUpload(@RequestParam("filename") MultipartFile file, @RequestParam("forId") String forId, HttpServletRequest request) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String dateStr = dateFormat.format(new Date());
                // 文件保存路径
                String path = url + "//" + dateStr + "//" + file.getOriginalFilename();
                // 转存文件
                File f = new File(path);
                if (!f.exists()) {//路径如果不存在 要创建
                    f.mkdirs();
                }
                file.transferTo(f);
                User user = UserUtil.getCurrentUser();
                BisPreEntry bisPreEntry = preEntryService.get(forId);
                bisPreEntry.setUpAndDown("1");//已上传
                bisPreEntry.setUpBy(user.getName());//上传人
                bisPreEntry.setUpTime(new Date());//上传时间
                bisPreEntry.setUpFileName(dateStr + "/" + file.getOriginalFilename());//上传文件名称
                bisPreEntry.setUpdateBy(user.getName());//修改人
                bisPreEntry.setUpdateTime(new Date());//修改时间
                preEntryService.merge(bisPreEntry);
                return ResponseVo.ok("上传成功");
            } catch (Exception e) {
                return ResponseVo.error(e.getMessage());
            }
        } else {
            return ResponseVo.error("上传失败");
        }
    }

    /***
     * 单个文件下载
     */
    @RequestMapping(value = "fileDownload", method = RequestMethod.GET)
    @ResponseBody
    public void fileDownload(HttpServletResponse response, @RequestParam("fileName") String fileName, @RequestParam("forId") String forId) throws IOException {
        BufferedInputStream br = null;
        OutputStream out = null;
        try {
            // 格式化拼接资源的相对路径
            String filePath = url + "/" + fileName;
            File file = new File(filePath);
            if (!file.exists()) {
                response.sendError(404, "File not found!");
                return;
            }
            br = new BufferedInputStream(new FileInputStream(file));
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset(); // 非常重要
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            out = response.getOutputStream();
            while ((len = br.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            out.flush();
            out.close();
            br.close();
        }
        User user = UserUtil.getCurrentUser();
        BisPreEntry bisPreEntry = preEntryService.get(forId);
        bisPreEntry.setUpAndDown("2");//已下载
        bisPreEntry.setUpdateBy(user.getName());//修改人
        bisPreEntry.setUpdateTime(new Date());//修改时间
        preEntryService.merge(bisPreEntry);
    }

//======================================================================================================================

    /**
     * @return java.util.concurrent.Callable<com.chenay.bean.entity.customs.InvtQueryListResponse>
     * @Author chenp
     * @Description 保税核注清单列表查询服务
     * @Date 9:14 2021/1/30
     * @Param [invtQueryListRequest]
     **/
    public Map<String,Object> InvtQueryListService(InvtQueryListRequest invtQueryListRequest) {
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
     * @return com.chenay.core.tool.api.R<com.chenay.bean.entity.customs.InvtCommonResponse>
     * @Author chenp
     * @Description 核注清单暂存
     * @Date 13:20 2021/3/12
     * @Param [invtVo]
     **/
    public Map<String,Object> InvtSaveService(InvtMessage invtVo) {
        Map<String,Object> resultMap = new HashMap<>();
        InvtCommonResponse baseResult = new InvtCommonResponse();
        String dataStr = "";
        try {
            invtVo.setKey(ApiKey.保税监管_保税核注清单保存服务秘钥.getValue());
            String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_保税核注清单暂存接口.getValue(), JSON.toJSONString(invtVo));
            //{"code":200,"msg":null,"data":{"seqNo":"I29232300000000001","etpsPreentNo":null,"checkInfo":null,"dealFlag":null,"invtDclTime":"20230731"}}
            System.out.println("保税核注清单暂存接口结果："+s);
            JSONObject jsonObject = JSON.parseObject(s);
            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
            if ("200".equals(code)) {
                Object data = jsonObject.get("data");
                if (data != null) {
                    dataStr = data.toString();
                }
                baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), InvtCommonResponse.class);
                resultMap.put("code","200");
                resultMap.put("msg","success");
                resultMap.put("data",baseResult.getSeqNo());
            } else {
                Object data = jsonObject.get("msg");
                if (data != null) {
                    dataStr = data.toString();
                }
                logger.error("保税监管_保税核注清单暂存接口"+invtVo.getInvtHeadType().getEepsInnerInvtNo(),"结果"+dataStr);
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

//    /**
//     * @return com.chenay.core.tool.api.R<com.chenay.bean.entity.customs.InvtCommonResponse>
//     * @Author chenp
//     * @Description 保税核注清单申报接口
//     * @Date 9:09 2021/1/30
//     * @Param [invtVo]
//     **/
//    public Map<String,Object> InvtDeclareService(InvtMessage invtVo) {
//        Map<String,Object> resultMap = new HashMap<>();
//        InvtCommonResponse baseResult = new InvtCommonResponse();
//        String dataStr = "";
//        try {
//            invtVo.setKey(ApiKey.保税监管_保税核注清单保存服务秘钥.getValue());
//            logger.info("保税核注清单申报"+new Date(),JSON.toJSONString(invtVo));
//            String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_保税核注清单申报接口.getValue(), JSON.toJSONString(invtVo));
//            System.out.println("保税核注清单申报接口结果："+s);
//            JSONObject jsonObject = JSON.parseObject(s);
//            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
//            if ("200".equals(code)) {
//                Object data = jsonObject.get("data");
//                if (data != null) {
//                    dataStr = data.toString();
//                }
//                baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), InvtCommonResponse.class);
//                resultMap.put("code","200");
//                resultMap.put("msg","success");
//                resultMap.put("data",baseResult.getSeqNo());
//            } else {
//                Object data = jsonObject.get("msg");
//                if (data != null) {
//                    dataStr = data.toString();
//                }
//                logger.error("保税监管_保税核注清单申报接口"+invtVo.getInvtHeadType().getEepsInnerInvtNo(),"结果"+dataStr);
//                logger.error(dataStr);
//                resultMap.put("code","500");
//                resultMap.put("msg",dataStr);
//                resultMap.put("data",null);
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//        return resultMap;
//    }


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
