package com.haiersoft.ccli.platform.web;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.base.entity.BasePlatform;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.interceptor.OutboundInterceptor;
import com.haiersoft.ccli.platform.service.BasePlatformService;
import com.haiersoft.ccli.platform.utils.HttpUtil;
import com.haiersoft.ccli.platform.utils.PlatformConsts;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */
@Controller
@RequestMapping(value = "platform/station/maintenance")
public class StationMaintenanceController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(StationMaintenanceController.class);

    @Autowired
    private BasePlatformService platformService;
    @Autowired
    private HttpUtil httpUtil;
    /**
     * @Description: 月台维护
     * @param
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(){
        return "platform/station/maintenance";
    }

    /**
     * 获取月台口
     */
    @RequestMapping(value="json",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
      /*  Page<BasePlatform> page = getPage(request);
        page.setPageSize(35);
        BasePlatform basePlatform = new BasePlatform();


        List<BasePlatform> platformList = platformService.getActivePlatforms(page,basePlatform);

        page.setResult(platformList);
        return getEasyUIData(page);*/
        Page<BasePlatform> page=getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        filters.add(new PropertyFilter("EQS_deletedFlag", "0"));
        //  filters.add(new PropertyFilter("NNULLS_platformNo", null));

        dealRequestOfFilter(request, filters);


        page = platformService.search(page,filters);
        return getEasyUIData(page);
    }

    private void dealRequestOfFilter(HttpServletRequest request, List<PropertyFilter> filters) {
        String inoutBoundFlag= request.getParameter("inoutBoundFlag");
        String trayRoomNum= request.getParameter("trayRoomNum");
        //出入库标志为空  是月台维护的查询接口。。 查询全部
        if(StringUtils.isEmpty(inoutBoundFlag)){

            return ;
        }


        //出入库标志不为空  是车辆指派月台接口    只有出库才限制筛选选择的月台
        // 2022/02/10 暂时注销  要求出库也查全部月台
    /*    if(inoutBoundFlag.equals("2")){

            //理论上肯定有库号
        *//*    if(StringUtils.isNotEmpty(trayRoomNum)){


            }
        *//*
        filters.add(new PropertyFilter("EQS_trayRoomNum", trayRoomNum));
        }*/
    }

    /**
     * @Description: 修改月台 手动自动
     * @param
     * @return
     */
    @RequestMapping(value = "/updateType/{id}",method = RequestMethod.GET)
    public String updateType(@PathVariable("id") Integer id, Model model){
        model.addAttribute("baseplatform", platformService.get(id));
        model.addAttribute("action", "updateAutoManualFlag/{id}");
        return "platform/station/updateType";
    }

    /**
     * @Description: 修改月台  停用启用
     * @param
     * @return
     */
    @RequestMapping(value = "/updateStatus/{id}",method = RequestMethod.GET)
    public String updateStatus(@PathVariable("id") Integer id, Model model){
        model.addAttribute("baseplatform", platformService.get(id));
        model.addAttribute("action", "updateStopOrStart/{id}");
        return "platform/station/updateStatus";
    }


    /**
     * 修改月台口停用
     *
     * @param platform
     * @param model
     */
    @RequestMapping(value = "/updateStopOrStart/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String updateStopOrStart(@Valid BasePlatform platform, Model model, HttpServletRequest request) {
        BasePlatform basePlatform = platformService.get(platform.getId());
        Map<String,String> sendMap = new HashMap<>();
        sendMap.put("id",String.valueOf(basePlatform.getId()));


        if (platform.getPlatformStatus()!=null){
            basePlatform.setPlatformStatus(platform.getPlatformStatus());
            if ("3" .equals(platform.getPlatformStatus())){
                basePlatform.setQueryFlag(0);
                sendMap.put("status","stop");
            }else {
                basePlatform.setQueryFlag(1);
                sendMap.put("status","start");
            }

        }


        try{
            httpUtil.doPost(PlatformConsts.PLATFORM_URL_UPDATE_PLATROM_QUEUE, JSON.toJSONString(sendMap));
        }catch (HttpHostConnectException ex){
            logger.info(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        platformService.update(basePlatform);
        return "success";
    }

    /**
     * 修改手动 /自动
     *
     * @param platform
     * @param model
     */
    @RequestMapping(value = "/updateAutoManualFlag/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid BasePlatform platform, Model model, HttpServletRequest request) {
        BasePlatform basePlatform = platformService.get(platform.getId());



        if (platform.getPlatformType()!=null){
            basePlatform.setPlatformType(platform.getPlatformType());
            if ("2"==platform.getPlatformType()){
                //basePlatform.setQueryFlag(0);
            }else {
               // basePlatform.setQueryFlag(1);
            }
        }
        platformService.update(basePlatform);
        return "success";
    }

}
