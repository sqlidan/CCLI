package com.haiersoft.ccli.platform.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.OutBoundQueueVO;
import com.haiersoft.ccli.platform.entity.VehicleQueue;
import com.haiersoft.ccli.platform.service.OutBoundQueueService;
import com.haiersoft.ccli.platform.service.VehicleQueueService;
import com.haiersoft.ccli.wms.entity.CountTemplete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述 出库来车列表
 */
@Controller
@RequestMapping(value = "platform/outBoundQueue")
public class OutBoundQueueController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OutBoundQueueController.class);

    @Autowired
    OutBoundQueueService outBoundQueueService;




    /**
     * @Description: 出库来车列表
     * @param
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(){
        return "platform/vehicle/outboundQueue";
    }

    @RequestMapping(value = "/serach", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> serach(HttpServletRequest request){




        Page<OutBoundQueueVO> page = getPage(request);
        OutBoundQueueVO outBoundQueueVO = new OutBoundQueueVO();//实体累
        parameterReflect.reflectParameter(outBoundQueueVO, request);//转换对应实体类参数
        page = outBoundQueueService.searchOutBondQueue(page, outBoundQueueVO);
        return getEasyUIData(page);
    }






}
