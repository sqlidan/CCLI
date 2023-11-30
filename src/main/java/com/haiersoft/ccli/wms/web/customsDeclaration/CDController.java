package com.haiersoft.ccli.wms.web.customsDeclaration;

import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclaration;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclarationInfo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import com.haiersoft.ccli.wms.service.customsDeclaration.CDService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: CustomsDeclarationController
 */
@Controller
@RequestMapping("wms/customsDeclaration")
public class CDController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CDController.class);

    @Autowired
    private CDService CDService;

    /**
     * 报关单总览默认页面
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "wms/customsDeclaration/customsDeclaration";
    }

    /**
     * 报关单总览默认页面
     */
    @RequestMapping(value = "listBGH", method = RequestMethod.GET)
    public String listBGH() {
        return "wms/customsDeclaration/customsDeclarationBGH";
    }

    /**
     * 报关单
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<BsCustomsDeclaration> page = getPage(request);
        page.orderBy("createTime").order(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("NEQS_state", "-1");//已删除
        filters.add(filter);
        page = CDService.search(page, filters);
        return getEasyUIData(page);
    }

    /*
     * 获取新的报关单号
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
        String linkId = "BGD" + userCode + StringUtils.timeToString();
        return linkId;
    }

    /**
     * 判断报关单是否已保存
     */
    @RequestMapping(value = "ifsave/{forId}", method = RequestMethod.GET)
    @ResponseBody
    public String ifsave(@PathVariable("forId") String forId) {
        BsCustomsDeclaration bsCustomsDeclaration = CDService.get(forId);
        if (bsCustomsDeclaration != null && !"".equals(bsCustomsDeclaration)) {
            return "success";
        } else {
            return "false";
        }
    }

    /**
     * 添加报关单
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid BsCustomsDeclaration bsCustomsDeclaration, Model model, HttpServletRequest request) {
        bsCustomsDeclaration.setState("0");//状态
        bsCustomsDeclaration.setUpAndDown("0");//未上传
        User user = UserUtil.getCurrentUser();
        bsCustomsDeclaration.setCreateBy(user.getName());//创建人
        bsCustomsDeclaration.setCreateTime(new Date());//创建时间
        CDService.save(bsCustomsDeclaration);
        return "success";
    }

    /**
     * 报关单修改
     */
    @RequestMapping(value = "update/{forId}", method = RequestMethod.GET)
    public String updateContractForm(Model model, @PathVariable("forId") String forId) {
        User user = UserUtil.getCurrentUser();
        BsCustomsDeclaration bsCustomsDeclaration = CDService.get(forId);
        model.addAttribute("bsCustomsDeclaration", bsCustomsDeclaration);
        BsCustomsDeclarationInfo bsCustomsDeclarationInfo = new BsCustomsDeclarationInfo();
        bsCustomsDeclarationInfo.setForId(forId);
        model.addAttribute("bsCustomsDeclarationInfo", bsCustomsDeclarationInfo);
        model.addAttribute("sbTime", bsCustomsDeclaration.getSbTime());
        model.addAttribute("cdTime", bsCustomsDeclaration.getCdTime());
        model.addAttribute("user", user.getName());
        model.addAttribute("action", "update");
        return "wms/customsDeclaration/customsDeclarationManager";
    }

    /**
     * 修改报关单
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid BsCustomsDeclaration bsCustomsDeclaration, Model model, HttpServletRequest request) {
        //保留原对象信息
        BsCustomsDeclaration queryBsCustomsDeclaration = CDService.get(bsCustomsDeclaration.getForId());
        if(queryBsCustomsDeclaration == null){
            return "未找到对应的报关单信息";
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getServiceProject())) {
            queryBsCustomsDeclaration.setServiceProject(bsCustomsDeclaration.getServiceProject());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getCheckListNo())) {
            queryBsCustomsDeclaration.setCheckListNo(bsCustomsDeclaration.getCheckListNo());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getCdNum())) {
            queryBsCustomsDeclaration.setCdNum(bsCustomsDeclaration.getCdNum());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getClientId())) {
            queryBsCustomsDeclaration.setClientId(bsCustomsDeclaration.getClientId());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getClientName())) {
            queryBsCustomsDeclaration.setClientName(bsCustomsDeclaration.getClientName());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getDeclarationUnitId())) {
            queryBsCustomsDeclaration.setDeclarationUnitId(bsCustomsDeclaration.getDeclarationUnitId());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getDeclarationUnit())) {
            queryBsCustomsDeclaration.setDeclarationUnit(bsCustomsDeclaration.getDeclarationUnit());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getBillNum())) {
            queryBsCustomsDeclaration.setBillNum(bsCustomsDeclaration.getBillNum());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getTradeMode())) {
            queryBsCustomsDeclaration.setTradeMode(bsCustomsDeclaration.getTradeMode());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getRemark())) {
            queryBsCustomsDeclaration.setRemark(bsCustomsDeclaration.getRemark());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getDty())) {
            queryBsCustomsDeclaration.setDty(bsCustomsDeclaration.getDty());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getGrossWeight())) {
            queryBsCustomsDeclaration.setGrossWeight(bsCustomsDeclaration.getGrossWeight());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getNetWeight())) {
            queryBsCustomsDeclaration.setNetWeight(bsCustomsDeclaration.getNetWeight());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getConsignee())) {
            queryBsCustomsDeclaration.setConsignee(bsCustomsDeclaration.getConsignee());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getConsignor())) {
            queryBsCustomsDeclaration.setConsignor(bsCustomsDeclaration.getConsignor());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getMyg())) {
            queryBsCustomsDeclaration.setMyg(bsCustomsDeclaration.getMyg());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getQyg())) {
            queryBsCustomsDeclaration.setQyg(bsCustomsDeclaration.getQyg());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getSbTime())) {
            queryBsCustomsDeclaration.setSbTime(bsCustomsDeclaration.getSbTime());
        }
        if (isNotNullOrEmpty(bsCustomsDeclaration.getCdTime())) {
            queryBsCustomsDeclaration.setCdTime(bsCustomsDeclaration.getCdTime());
        }

        User user = UserUtil.getCurrentUser();
        queryBsCustomsDeclaration.setUpdateBy(user.getName());//修改人
        queryBsCustomsDeclaration.setUpdateTime(new Date());//修改时间
        CDService.merge(queryBsCustomsDeclaration);
        return "success";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "delete/{forId}")
    @ResponseBody
    public String deleteEnterStock(@PathVariable("forId") String forId) {
        User user = UserUtil.getCurrentUser();
        BsCustomsDeclaration bsCustomsDeclaration = CDService.get(forId);
        bsCustomsDeclaration.setState("-1");//删除
        bsCustomsDeclaration.setUpdateBy(user.getName());//修改人
        bsCustomsDeclaration.setUpdateTime(new Date());//修改时间
        CDService.merge(bsCustomsDeclaration);
        return "success";
    }

    /**
     * 状态修改
     */
    @RequestMapping(value = "UpdateState/{forId}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public String jlUpdateOkContractForm(Model model, @PathVariable("forId") String forId, @PathVariable("type") String type, HttpServletRequest request) throws RemoteException, ServiceException {
        User user = UserUtil.getCurrentUser();
        BsCustomsDeclaration bsCustomsDeclaration = CDService.get(forId);
        if (bsCustomsDeclaration != null) {
            String state = bsCustomsDeclaration.getState();
            if ("jlUpdateOk".equals(type)) {//初审通过
                if(!"1".equals(state)){
                    return "报关单信息暂未完善提交，不能初审。";
                }
                bsCustomsDeclaration.setState("2");
                bsCustomsDeclaration.setJlAudit(user.getName());
                bsCustomsDeclaration.setJlAuditTime(new Date());
            } else if ("jlUpdateNo".equals(type)) {//初审驳回
                if(!"1".equals(state) && !"2".equals(state)){
                    return "当前报关单信息不可初审驳回。";
                }
                bsCustomsDeclaration.setState("0");
                if(request.getParameter("reason")!=null && request.getParameter("reason").toString().trim().length() > 0){
                    bsCustomsDeclaration.setJlRejectReason(request.getParameter("reason").toString().trim());
                }else{
                    bsCustomsDeclaration.setJlRejectReason(null);
                }
                bsCustomsDeclaration.setJlAudit(user.getName());
                bsCustomsDeclaration.setJlAuditTime(new Date());
            } else if ("zgUpdateOk".equals(type)) {//复审通过
                if(!"2".equals(state)){
                    return "报关单信息暂未初审通过，不能复审。";
                }
                bsCustomsDeclaration.setState("3");
                bsCustomsDeclaration.setZgAudit(user.getName());
                bsCustomsDeclaration.setZgAuditTime(new Date());
            } else if ("zgUpdateNo".equals(type)) {//复审驳回
                if(!"2".equals(state) && !"3".equals(state)){
                    return "当前报关单信息不可复审驳回。";
                }
                bsCustomsDeclaration.setState("1");
                if(request.getParameter("reason")!=null && request.getParameter("reason").toString().trim().length() > 0){
                    bsCustomsDeclaration.setZgRejectReason(request.getParameter("reason").toString().trim());
                }else{
                    bsCustomsDeclaration.setZgRejectReason(null);
                }
                bsCustomsDeclaration.setZgAudit(user.getName());
                bsCustomsDeclaration.setZgAuditTime(new Date());
            } else if ("bghSubmit".equals(type)) {//提交审核
                if(Integer.parseInt(state) > 0){
                    return "报关单信息已提交审核，不能再次提交。";
                }
                bsCustomsDeclaration.setState("1");
            }
            bsCustomsDeclaration.setUpdateBy(user.getName());//修改人
            bsCustomsDeclaration.setUpdateTime(new Date());//修改时间
            CDService.merge(bsCustomsDeclaration);
            return "success";
        } else {
            return "error";
        }
    }

//======================================================================================================================
    /**
     * 跳转上传报关单画面
     */
    @RequestMapping(value = "fileUploadH/{forId}", method = RequestMethod.GET)
    public String fileUploadH(Model model, @PathVariable("forId") String forId) {
        model.addAttribute("action", "fileUpload");
        model.addAttribute("forId", forId);
        return "wms/customsDeclaration/customsDeclarationManagerUpload";
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
                BsCustomsDeclaration bsCustomsDeclaration = CDService.get(forId);
                bsCustomsDeclaration.setUpAndDown("1");//已上传
                bsCustomsDeclaration.setUpBy(user.getName());//上传人
                bsCustomsDeclaration.setUpTime(new Date());//上传时间
                bsCustomsDeclaration.setUpFileName(dateStr + "/" + file.getOriginalFilename());//上传文件名称
                bsCustomsDeclaration.setUpdateBy(user.getName());//修改人
                bsCustomsDeclaration.setUpdateTime(new Date());//修改时间
                CDService.merge(bsCustomsDeclaration);
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
        BsCustomsDeclaration bsCustomsDeclaration = CDService.get(forId);
        bsCustomsDeclaration.setUpAndDown("2");//已下载
        bsCustomsDeclaration.setUpdateBy(user.getName());//修改人
        bsCustomsDeclaration.setUpdateTime(new Date());//修改时间
        CDService.merge(bsCustomsDeclaration);
    }

//======================================================================================================================



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
