package com.haiersoft.ccli.platform.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.InBoundQueueVO;
import com.haiersoft.ccli.platform.entity.OutBoundQueueVO;
import com.haiersoft.ccli.platform.service.InBoundQueueService;
import com.haiersoft.ccli.platform.service.OutBoundQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述 入库来车列表
 */
@Controller
@RequestMapping(value = "platform/inBoundQueue")
public class InBoundQueueController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(InBoundQueueController.class);

    @Autowired
    InBoundQueueService inBoundQueueService;




    /**
     * @Description: 入库库来车列表
     * @param
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(){
        return "platform/vehicle/inboundQueue";
    }

    @RequestMapping(value = "/serach", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> serach(HttpServletRequest request){




        Page<InBoundQueueVO> page = getPage(request);
        InBoundQueueVO inBoundQueueVO = new InBoundQueueVO();//实体累
        parameterReflect.reflectParameter(inBoundQueueVO, request);//转换对应实体类参数
        page = inBoundQueueService.searchInBondQueue(page, inBoundQueueVO);
        return getEasyUIData(page);
    }






}
