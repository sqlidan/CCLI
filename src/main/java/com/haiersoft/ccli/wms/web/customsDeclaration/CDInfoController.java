package com.haiersoft.ccli.wms.web.customsDeclaration;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.tempuri.ReturnModel;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisCustomsClearanceInfo;
import com.haiersoft.ccli.wms.entity.apiEntity.*;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclaration;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclarationInfo;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclarationInfoDJ;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfoDJ;
import com.haiersoft.ccli.wms.service.customsDeclaration.CDInfoDJService;
import com.haiersoft.ccli.wms.service.customsDeclaration.CDInfoService;
import com.haiersoft.ccli.wms.service.customsDeclaration.CDService;
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
 * @ClassName: CustomsDeclarationInfoController
 * @Description: 报关单货物明细Controller
 */
@Controller
@RequestMapping("wms/customsDeclarationInfo")
public class CDInfoController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(CDInfoController.class);

    @Autowired
    private CDService CDService;
    @Autowired
    private CDInfoService CDInfoService;
    @Autowired
    private CDInfoDJService CDInfoDJService;

    /**
     * 货物信息列表
     */
    @RequestMapping(value = "json/{forId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request, @PathVariable("forId") String forId) {
        Page<BsCustomsDeclarationInfo> page = getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        PropertyFilter filter = new PropertyFilter("EQS_forId", forId);
        filters.add(filter);
        page = CDInfoService.search(page, filters);
        return getEasyUIData(page);

    }

    @RequestMapping(value = "create/{forId}", method = RequestMethod.GET)
    public String createForm(@PathVariable("forId") String forId,Model model) {
        User user = UserUtil.getCurrentUser();
        model.addAttribute("user",user.getName());
        model.addAttribute("date",new Date());
        BsCustomsDeclarationInfo bsCustomsDeclarationInfo = new BsCustomsDeclarationInfo();
        bsCustomsDeclarationInfo.setForId(forId);
        model.addAttribute("bsCustomsDeclarationInfo",bsCustomsDeclarationInfo);
        model.addAttribute("action", "create");
        return "wms/customsDeclaration/customsDeclarationInfoManager";
    }

    /**
     * 添加货物信息
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid BsCustomsDeclarationInfo bsCustomsDeclarationInfo, HttpServletRequest request) {
        User user = UserUtil.getCurrentUser();
        bsCustomsDeclarationInfo.setCreateBy(user.getName());//创建人
        bsCustomsDeclarationInfo.setCreateTime(new Date());//创建时间
        CDInfoService.save(bsCustomsDeclarationInfo);
        return "success";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("bsCustomsDeclarationInfo", CDInfoService.get(id));
        model.addAttribute("action", "update");
        return "wms/customsDeclaration/customsDeclarationInfoManager";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody BsCustomsDeclarationInfo bsCustomsDeclarationInfo,Model model) {
        CDInfoService.update(bsCustomsDeclarationInfo);
        return "success";
    }

    /**
     * 删除货物信息
     */
    @RequestMapping(value = "deleteinfo/{ids}")
    @ResponseBody
    public String deleteinfo(@PathVariable("ids") List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            CDInfoService.delete(ids.get(i));
        }
        return "success";
    }

}
