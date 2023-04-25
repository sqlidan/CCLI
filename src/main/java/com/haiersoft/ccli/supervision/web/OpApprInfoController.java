package com.haiersoft.ccli.supervision.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.entity.OpApprHead;
import com.haiersoft.ccli.supervision.entity.OpApprInfo;
import com.haiersoft.ccli.supervision.service.ApprInfoService;
import com.haiersoft.ccli.supervision.service.OpApprHeadService;
import com.haiersoft.ccli.supervision.service.OpApprInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分类监管 申请单controller
 *
 * @author
 */

@Controller
@RequestMapping("supervision/opApprInfo")
public class OpApprInfoController extends BaseController {

    @Autowired
    private OpApprHeadService opApprHeadService;
    @Autowired
    private OpApprInfoService opApprInfoService;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        return "fljg/opApprInfoForm";
    }

    @RequestMapping(value = "json/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request, @PathVariable("id") String id) {
        Page<OpApprInfo> page = getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQS_headId", id);
        page.orderBy("apprGNo");
        filters.add(filter);
        page = opApprInfoService.search(page, filters);
        return getEasyUIData(page);
    }


    /**
     * 添加申请信息
     *
     * @param opApprInfo
     * @param model
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid OpApprInfo opApprInfo, Model model) {

        opApprInfo.setApprGNo(opApprInfoService.getApprGnoByHeadId(opApprInfo.getHeadId()));
        opApprInfo.setQty1(opApprInfo.getgQty());
        opApprInfo.setUnit1(opApprInfo.getgUnit());
        opApprInfo.setCreateTime(new Date());
        opApprInfoService.save(opApprInfo);
        return "success";
    }

    //删除申请
    @Transactional
    @RequestMapping(value = "/del/{id}")
    @ResponseBody
    public String deleteAppr(@PathVariable("id") String id) throws RemoteException, ServiceException {
        OpApprInfo opApprInfo = opApprInfoService.find("id", id);
        OpApprHead opApprHead = opApprHeadService.find("id", opApprInfo.getHeadId());
        if (!(null == opApprHead.getApprId()) || !("".equals(opApprHead.getApprId()))) {
            return "已申报的记录不能删除";
        }
        opApprInfoService.delete(opApprInfo);
        return "success";
    }
}
