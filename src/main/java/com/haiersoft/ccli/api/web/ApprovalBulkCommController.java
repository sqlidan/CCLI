package com.haiersoft.ccli.api.web;

import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.api.service.ApprovalBulkCommService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.AuditRecord;
import com.haiersoft.ccli.wms.entity.BisLuodiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 大宗商品审批
 */
@Controller
@RequestMapping(value = "wms/bulk")
public class ApprovalBulkCommController extends BaseController {
    @Autowired
    private ApprovalBulkCommService approvalBulkCommService;

    @RequestMapping(value = "index")
    public String index() {
        return "wms/bulk/index";
    }

    /**
     * 审批列表查询
     */
    @RequestMapping(value = "page")
    @ResponseBody
    public Map<String, Object> pageList(HttpServletRequest request) {
        Page<AuditRecord> page = getPage(request);
        AuditRecord entity = reflectParameter(request, AuditRecord.class);
        page = approvalBulkCommService.pageList(page, entity);
        return getEasyUIData(page);
    }

    /**
     * 明细弹框内容
     */
    @RequestMapping(value = "form/{id}")
    public String getDetailIinfo(Model model, @PathVariable("id") String id) {
        List<Map<String,Object>> obj = approvalBulkCommService.findInfoById(id);
        model.addAttribute("bulk", obj);
        return "wms/bulk/planForm";
    }

    /**
     * 审核通过按钮，发送审核状态并记录
     */
    @RequestMapping(value = "approvedPass")
    @ResponseBody
    @Transactional
    public String approvedPass(String id) {
        //审核通过修改记录表中的状态
        String result = approvalBulkCommService.approvedPass(id);
        return result;
    }

    /**
     * 审核拒绝按钮，发送审核状态并记录
     */
    @RequestMapping(value = "auditReject")
    @ResponseBody
    @Transactional
    public String auditReject(String id) {
        String result = approvalBulkCommService.auditReject(id);
        return result;
    }

    /**
     * 客户查询接口
     */
    @RequestMapping(value = "customerQuery")
    @ResponseBody
    @Transactional
    public ResponseVo customerQuery(@RequestBody Map<String, String> params) {
        List<Map<String,Object>> list = approvalBulkCommService.customerQuery(params);
        if(list==null || list.size()==0){
            ResponseVo.errDz(null,"查询客户失败，请检查客户名称和客户号是否正确");
        }
        return ResponseVo.resDz(list,"查询成功");
    }

    /**
     * 货种查询接口
     */
    @RequestMapping(value = "typeQuery")
    @ResponseBody
    @Transactional
    public ResponseVo typeQuery(@RequestBody Map<String, String> params) {
        List<Map<String,Object>> list = approvalBulkCommService.typeQuery(params);
        if(list==null || list.size()==0){
            ResponseVo.errDz(null,"查询货种失败，请检查是否正确");
        }
        return ResponseVo.resDz(list,"查询成功");
    }
}
