package com.haiersoft.ccli.supervision.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.OpApprHead;
import com.haiersoft.ccli.supervision.entity.OpApprInfo;
import com.haiersoft.ccli.supervision.entity.OpManiHead;
import com.haiersoft.ccli.supervision.entity.OpManiInfo;
import com.haiersoft.ccli.supervision.service.OpApprHeadService;
import com.haiersoft.ccli.supervision.service.OpApprInfoService;
import com.haiersoft.ccli.supervision.service.OpManiHeadService;
import com.haiersoft.ccli.supervision.service.OpManiInfoService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分类监管 申请单controller
 *
 * @author
 */

@Controller
@RequestMapping("supervision/opManiInfo")
public class OpManiInfoController extends BaseController {

    @Autowired
    private OpManiHeadService opManiHeadService;
    @Autowired
    private OpManiInfoService opManiInfoService;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        return "fljg/opManiInfoForm";
    }

    @RequestMapping(value = "json/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request, @PathVariable("id") String id) {
        Page<OpManiInfo> page = getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQS_headId", id);
        page.orderBy("apprGNo");
        filters.add(filter);
        page = opManiInfoService.search(page, filters);
        return getEasyUIData(page);
    }


    /**
     * 添加核放单信息
     *
     * @param opManiInfo
     * @param model
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid OpManiInfo opManiInfo, Model model) {
        opManiInfo.setCreateTime(new Date());
        opManiInfoService.save(opManiInfo);
        return "success";
    }

    //删除核放单信息
    @Transactional
    @RequestMapping(value = "/del/{id}")
    @ResponseBody
    public String deleteAppr(@PathVariable("id") String id) throws RemoteException, ServiceException {
        OpManiInfo opManiInfo = opManiInfoService.find("id", id);
        OpManiHead opManiHead = opManiHeadService.find("id", opManiInfo.getHeadId());
        if ((null == opManiHead.getManifestId()) || ("".equals(opManiHead.getManifestId()))) {
            opManiInfoService.delete(opManiInfo);
            return "success";
        }
        return "已申报的记录不能删除";

    }
}
