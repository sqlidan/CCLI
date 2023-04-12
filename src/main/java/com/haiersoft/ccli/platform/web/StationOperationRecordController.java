package com.haiersoft.ccli.platform.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.StationOperationRecord;
import com.haiersoft.ccli.platform.service.StationOperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */
@Controller
@RequestMapping(value = "platform/station/operationRecord")
public class StationOperationRecordController extends BaseController {

    @Autowired
    private StationOperationRecordService service;

    /**
     * @Description: 月台操作记录查询
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    public String list(){
        return "platform/station/operationrecord";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> search(HttpServletRequest request){
        Page<StationOperationRecord> page=getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page.orderBy("createdTime").order("desc");
        page = service.search(page,filters);
        return getEasyUIData(page);
    }


}
