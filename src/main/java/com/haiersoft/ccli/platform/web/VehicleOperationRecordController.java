package com.haiersoft.ccli.platform.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.VehicleOperationRecord;
import com.haiersoft.ccli.platform.service.VehicleOperationRecordService;
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
@RequestMapping(value = "platform/vehicle/operationRecord")
public class VehicleOperationRecordController extends BaseController {

    /**
     * @Description: 车辆靠口记录
     * @param
     * @return
     */
    @Autowired
    VehicleOperationRecordService vehicleOperationRecordService;
    @RequestMapping(value = "/list")
    public String list(){
        return "platform/vehicle/operationrecord";
    }

    @RequestMapping(value = "/serach", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> serach(HttpServletRequest request){
        Page<VehicleOperationRecord> page=getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page.orderBy("createdTime").order("desc");
        page = vehicleOperationRecordService.search(page,filters);
        return getEasyUIData(page);
    }
}
