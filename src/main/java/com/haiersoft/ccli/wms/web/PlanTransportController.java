package com.haiersoft.ccli.wms.web;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BasePlanTime;
import com.haiersoft.ccli.wms.entity.BisPlanTransport;
import com.haiersoft.ccli.wms.service.BusinessService;
import com.haiersoft.ccli.wms.service.PlanTimeService;
import com.haiersoft.ccli.wms.service.PlanTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 网上预约模块
 */
@Controller
@RequestMapping("wms/plan")
public class PlanTransportController extends BaseController {

    @Autowired
    private PlanTransportService planTransportService;

    @Autowired
    private PlanTimeService planTimeService;

    @Autowired
    private BusinessService businessService;

    //@Autowired
    //private LuodiService luodiService;

    // ---------------------------- 预约时间表 ----------------------------

    /**
     * 预约时间表页面
     */
    @RequestMapping("time")
    public String time() {
        return "wms/plan/time";
    }

    /**
     * 预约时间表分页查询
     */
    @RequestMapping(value = "time/page")
    @ResponseBody
    public Map<String, Object> pageTimes(HttpServletRequest request) {

        Page<BasePlanTime> page = getPage(request);

        BasePlanTime entity = reflectParameter(request, BasePlanTime.class);

        page = planTimeService.pageTime(page, entity);

        return getEasyUIData(page);
    }

    /**
     * 预约时间表添加、修改页面
     */
    @RequestMapping(value = "time/form")
    public String createForm(Model model, String action, Integer id) {

        model.addAttribute("action", action);

        if ("update".equals(action)) {
            BasePlanTime basePlanTime = planTimeService.get(id);
            model.addAttribute("id", basePlanTime.getId());
            model.addAttribute("planDate", basePlanTime.getPlanDate());
            model.addAttribute("largeNo", basePlanTime.getLargeNo());
            model.addAttribute("description", basePlanTime.getDescription());
            model.addAttribute("state", basePlanTime.getState());
        }

        return "wms/plan/timeForm";
    }

    /**
     * 添加预约时间表
     */
    @RequestMapping(value = "time/add")
    public String addTime(BasePlanTime basePlanTime) {

        String date = format(basePlanTime.getTimeHour()) + format(basePlanTime.getTimeMin());

        Integer id = planTimeService.getSeqId();

        basePlanTime.setId(id);
        basePlanTime.setPlanDate(date);
        basePlanTime.setState(0);

        planTimeService.merge(basePlanTime);

        //预约时间表添加
        businessService.planTimeAdd(basePlanTime);

        return "wms/plan/time";
    }

    /**
     * 修改预约时间表
     */
    @RequestMapping(value = "time/update")
    public String updateTime(BasePlanTime basePlanTime) {

        String date = format(basePlanTime.getTimeHour()) + format(basePlanTime.getTimeMin());

        basePlanTime.setPlanDate(date);

        planTimeService.merge(basePlanTime);

        //预约时间表修改
        businessService.planTimeUpdate(basePlanTime);

        return "wms/plan/time";
    }

    /**
     * 删除预约时间表
     */
    @RequestMapping(value = "time/delete")
    @ResponseBody
    public String deleteTime(Integer id) {

        BasePlanTime basePlanTime = planTimeService.get(id);

        basePlanTime.setState(1);

        planTimeService.merge(basePlanTime);
        //预约时间表删除
        businessService.planTimeDelete(basePlanTime);

        return SUCCESS;
    }

    private String format(Integer number) {
        return number > 9 ? number + "" : "0" + number;
    }

    // ---------------------------- 预约计划 ----------------------------

    /**
     * 预约计划页面
     */
    @RequestMapping("index")
    public String index() {
        return "wms/plan/index";
    }

    /**
     * 预约计划分页查询
     */
    @RequestMapping(value = "page")
    @ResponseBody
    public Map<String, Object> pagePlans(HttpServletRequest request) {

        Page<BisPlanTransport> page = getPage(request);

        BisPlanTransport entity = reflectParameter(request, BisPlanTransport.class);

        page = planTransportService.pagePlans(page, entity);

        return getEasyUIData(page);
    }

    @RequestMapping(value = "time/select")
    @ResponseBody
    public List<Map<String, Object>> selectPlanTime(HttpServletRequest request) {

        String param = request.getParameter("q");

        return planTransportService.listPlanTime(param);
    }

    /**
     * 客服审核
     */
    @RequestMapping(value = "check/service")
    @ResponseBody
    public String serviceCheck(Integer id) {

        User user = UserUtil.getCurrentUser();
        Date now = new Date();

        BisPlanTransport entity = planTransportService.get(id);
        entity.setCheckState("1");
        entity.setCheckDate(now);
        entity.setCheckUser(user.getName());

        planTransportService.merge(entity);

        return SUCCESS;
    }

    /**
     * 计划审核
     */
    @RequestMapping(value = "check/plan")
    @ResponseBody
    public String checkPlan(Integer id) {

        User user = UserUtil.getCurrentUser();
        Date now = new Date();

        BisPlanTransport entity = planTransportService.get(id);

        entity.setCheckState("2");
        entity.setCheckDate(now);
        entity.setCheckUser(user.getName());

        planTransportService.merge(entity);
        //预约计划审核
        String result = businessService.planCheck(entity);

        if (!SUCCESS.equals(result)) {
            entity.setCheckState("1");
            planTransportService.merge(entity);
        }

        return SUCCESS;
    }

    /**
     * 驳回
     */
    @RequestMapping(value = "check/reject")
    @ResponseBody
    public String rejectCheck(Integer id) {

        User user = UserUtil.getCurrentUser();
        Date now = new Date();

        String reState;

        BisPlanTransport entity = planTransportService.get(id);

        reState = entity.getCheckState();

        entity.setCheckState("-1");
        entity.setCheckDate(now);
        entity.setCheckUser(user.getName());

        planTransportService.merge(entity);
        //预约计划审核
        String result = businessService.planCheck(entity);

        if (!SUCCESS.equals(result)) {
            entity.setCheckState(reState);
            planTransportService.merge(entity);
        }

        return SUCCESS;
    }

    /**
     * 选择预约计划弹出框
     */
    @RequestMapping(value = "form")
    public String planForm(Model model, String action, Integer id) {

        model.addAttribute("action", action);
        model.addAttribute("planId", id);

        return "wms/plan/planForm";
    }

    /**
     * 为预约计划，添加时间
     */
    @RequestMapping(value = "form/add")
    public String addPlanForm(Integer selectTimeType, Integer planId) throws ParseException {

        BasePlanTime basePlanTime = planTimeService.get(selectTimeType);

        BisPlanTransport entity = planTransportService.get(planId);

        Date now = new Date();

        Date tomorrow = DateUtils.addDay(now, 1);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(tomorrow);
        Date newTomorrow = formatter.parse(dateString);

        entity.setPlanType(basePlanTime.getId());
        entity.setPlanTime(basePlanTime.getPlanDate());
        entity.setPlanDate(newTomorrow);

        planTransportService.merge(entity);

        return "wms/plan/index";
    }

    /**
     * 查询第二天预约车次的数据，预约时间、最大车次、以约车次
     */
    @RequestMapping(value = "time/list")
    @ResponseBody
    public List<Map<String, Object>> listTimes() {
        return planTimeService.listTime();
    }

}
