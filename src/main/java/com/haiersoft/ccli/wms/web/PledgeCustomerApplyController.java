package com.haiersoft.ccli.wms.web;

import com.haiersoft.ccli.api.entity.ApiCustomerQueryApply;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.service.*;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ClientPledgeService;
import com.haiersoft.ccli.wms.service.PledgeComfirmService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("pledge/customerApply")
public class PledgeCustomerApplyController extends BaseController{

    @Autowired
    CustomerQueryApplyService customerQueryApplyService;

    @Autowired
    CustomerQueryConfirmService customerQueryConfirmService;

    @Autowired
    ReceiveApplyResultService receiveApplyResultService;

    /*跳转质押用户查询申请页面*/
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "wms/pledge/customerApply";
    }

    /**
     * @return
     * @throws
     * @author
     * @Description:质押用户查询申请
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<ApiCustomerQueryApply> page = getPage(request);
        List<PropertyFilter> list = PropertyFilter.buildFromHttpRequest(request);
        page.orderBy("createTime").order("desc");
        page = customerQueryApplyService.search(page, list);
        return getEasyUIData(page);
    }


    /**
     * @return
     * @throws
     * @author
     * @Description:押用户查询申请确认
     */
    @RequestMapping(value = "comfirm/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String pass(@PathVariable("id") String id) {
        ApiCustomerQueryApply apply = customerQueryApplyService.get(id);
        apply.setConfirmStatus(1);
        customerQueryApplyService.save(apply);
        return "success";
    }

    /**

     * @Description:押用户查询申请驳回
     */
    @RequestMapping(value = "refuse/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String refuse(@PathVariable("id") String id) {
        ApiCustomerQueryApply apply = customerQueryApplyService.get(id);
        apply.setConfirmStatus(2);
        apply.setComfirmTime(new Date());
        customerQueryApplyService.save(apply);
        receiveApplyResultService.sendRefuseInfo(apply);
        return "success";
    }


    @RequestMapping(value = "{recordId}/pass")
    public String passApply(@PathVariable("recordId") String recordId, Model model) {
        model.addAttribute("recordId", recordId);
        return "wms/pledge/customerList";
    }

    /**
     * 申请通过
     * @param recordId
     * @param newCustomerList
     * @return
     */
    @RequestMapping(value = "{recordId}/updateCustomerInfo")
    @ResponseBody
    public String updateCustomerInfo(@PathVariable("recordId") String recordId, @RequestBody List<Integer> newCustomerList) {
        ApiCustomerQueryApply apply = customerQueryApplyService.get(recordId);
        apply.setConfirmStatus(1);
        apply.setComfirmTime(new Date());
        customerQueryApplyService.save(apply);
        customerQueryConfirmService.updateCustomerInfo(recordId,newCustomerList);
        receiveApplyResultService.sendPassInfo(apply);
        return "success";
    }

    @RequestMapping(value = "{recordId}/customerInfo")
    @ResponseBody
    public List<String> getCustomerInfo(@PathVariable("recordId") String recordId) {
        return customerQueryConfirmService.getClientIdList(recordId);
    }


}
