package com.haiersoft.ccli.wms.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.*;
import com.haiersoft.ccli.wms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 落地插电模块
 */
@Controller
@RequestMapping(value = "wms/luodi")
public class LuodiController extends BaseController {

    @Autowired
    private LuodiService luodiService;

    @Autowired
    private LuodiPositionService luodiPositionService;

    @Autowired
    private EnterStockService enterStockService;

    //@Autowired
    //private InOutService inOutService;

    @Autowired
    private HttpGo httpGo;

    @Autowired
    private PresenceBoxService presenceBoxService;

    @Autowired
    private StandingBookService standingBookService;

    // ------------------ 落地计划管理 ------------------

    /**
     * 落地计划管理
     */
    @RequestMapping(value = "index")
    public String index() {
        return "wms/luodi/index";
    }

    /**
     * 落地计划分页查询
     */
    @RequestMapping(value = "page")
    @ResponseBody
    public Map<String, Object> pageLuodi(HttpServletRequest request) {

        Page<BisLuodiInfo> page = getPage(request);

        BisLuodiInfo entity = reflectParameter(request, BisLuodiInfo.class);

        page = luodiService.pageLuodi(page, entity);

        return getEasyUIData(page);
    }

    /**
     * 落地计划录入弹出框
     */
    @RequestMapping(value = "formAdd")
    public String addLuodi(Model model) {
        model.addAttribute("luodi", new BisLuodiInfo());
        model.addAttribute("action", "add");

        return "wms/luodi/planForm";
    }

    /**
     * 落地计划修改弹出框
     */
    @RequestMapping(value = "form/{id}")
    public String updateLuodi(Model model, @PathVariable("id") Integer id) {
        BisLuodiInfo obj = luodiService.get(id);
        model.addAttribute("luodi", obj);
        model.addAttribute("action", "update");

        return "wms/luodi/planForm";
    }

    /**
     * 落地计划录入
     */
    @SuppressWarnings("unused")
	@RequestMapping(value = "form/add")
    @ResponseBody
    public String addLuodiForm(@ModelAttribute @RequestBody BisLuodiInfo bisLuodiInfo) {

        User user = UserUtil.getCurrentUser();
        Date now = new Date();

        String taskType = bisLuodiInfo.getLuodiType();

        String startPosition = bisLuodiInfo.getStartPosition();
        String endPosition = bisLuodiInfo.getEndPosition();

        BaseLuodiPosition startLuodiPosition = null;
        String startName = null;
        String startOtherName = null;

        if ("M".equals(taskType)) {
            // 初始场位
            startLuodiPosition = luodiPositionService.get(Integer.valueOf(startPosition));
            startName = startLuodiPosition.getPositionName();
            startOtherName = null;
        }

        // 目的场位
        BaseLuodiPosition endLuodiPosition = luodiPositionService.get(Integer.valueOf(endPosition));
        String endName = endLuodiPosition.getPositionName();
        String endOtherName = null;

        // 判断提单号是否存在
        BisEnterStock enterStock = enterStockService.getEnterStock(bisLuodiInfo.getBillCode());

        if (enterStock == null) return "提单号不存在";

        BisPresenceBox bisPresenceBox = presenceBoxService.getBoxInfo(bisLuodiInfo.getCtnNum());

        if (bisPresenceBox == null) return "在场箱不存在";

        int ctnVersion = Integer.parseInt(bisPresenceBox.getCtnSize());

        // 通过箱型判断场位能否放下箱子
        if (ctnVersion > 20) {

            if (isNotNull(endPosition) && bigBoxCheckIsFull(endPosition)) {
                return "场位已满";
            }

        } else {

            if (isNotNull(endPosition) && checkPositionIsFullByName(endName)) {
                return "场位已满";
            }

        }

        String luodiCode = luodiService.getSchemeIdToString();

        bisLuodiInfo.setCtnVersion(bisPresenceBox.getCtnType());
        bisLuodiInfo.setCtnSize(bisPresenceBox.getCtnSize());
        bisLuodiInfo.setStartTime(bisLuodiInfo.getLuodiTime());
        bisLuodiInfo.setLuodiCode(luodiCode);
        bisLuodiInfo.setState("0");
        bisLuodiInfo.setCreateUser(user.getName());
        bisLuodiInfo.setCreateTime(now);
        bisLuodiInfo.setLuodiType(taskType);

        luodiService.merge(bisLuodiInfo);

        // 修改在场箱记录，箱子需要插电
        if ("F".equals(taskType) && bisLuodiInfo.getIsElectricity() == 1) {
            presenceBoxService.ifCd(bisLuodiInfo.getCtnNum());
        }

        return "success";
    }

    /**
     * 比较两个场位名，返回小号为"01"的位置的场位名
     *
     * @param position1 场位名
     * @param position2 场位名
     * @return 小号01位置的场位名
     */
    public static String comparePositionName(String position1, String position2) {

        if (position1 == null) return position2;
        if (position2 == null) return position1;

        String[] pp1 = position1.split("-");

        String first1 = pp1[0];
        String last1 = pp1[1];

        char[] cc1 = last1.toCharArray();

        String[] pp2 = position2.split("-");

        String first2 = pp2[0];
        String last2 = pp2[1];

        char[] cc2 = last2.toCharArray();

        if (!first1.equals(first2))
            return null;

        return cc1[1] < cc2[1] ? position1 : position2;
    }

    /**
     * 根据场位小号获取另一个场位小号
     *
     * @param positionName 场位名
     * @return
     */
    public static String getOtherPosition(String positionName) {

        String[] pp = positionName.split("-");

        String first = pp[0];
        String last = pp[1];

        char[] cc = last.toCharArray();

        if (cc[1] == '1') {
            return first + "-" + cc[0] + "2";
        } else if (cc[1] == '2') {
            return first + "-" + cc[0] + "1";
        }

        return null;

    }

    /**
     * 判断场位是否已放满箱子
     *
     * @param positionName 场位名
     * @return true.已满 false.未满
     */
    private boolean checkPositionIsFullByName(String positionName) {

        BaseLuodiPosition position = luodiPositionService.getLuodiPositionByName(positionName);

        return position.getNowNum() >= position.getPositionMax();
    }

    /**
     * 判断另一个场位是否已放满箱子
     *
     * @param luodiPosition 场位
     * @return true.已满 false.未满
     */
    private boolean checkOtherPositionIsFull(BaseLuodiPosition luodiPosition) {

        String first = luodiPosition.getPositionFirst();
        String last = luodiPosition.getPositionLast();

        char[] cc = last.toCharArray();

        if (cc[1] == '1') {
            return checkPositionIsFullByName(first + "-" + cc[0] + "2");
        } else if (cc[1] == '2') {
            return checkPositionIsFullByName(first + "-" + cc[0] + "1");
        }

        return false;

    }

    /**
     * 大箱子检查是否能放进场位
     *
     * @param positionId 场位id
     * @return true.已满 false.未满
     */
    private boolean bigBoxCheckIsFull(String positionId) {

        BaseLuodiPosition luodiPosition = luodiPositionService.get(Integer.valueOf(positionId));

        boolean isFull = checkPositionIsFullByName(luodiPosition.getPositionName());
        boolean isOtherFull = checkOtherPositionIsFull(luodiPosition);

        return isFull || isOtherFull;
    }

    /**
     * 开始插电
     */
    @RequestMapping(value = "startElectrcity")
    @ResponseBody
    private String startElectrcity(Integer id) {

        BisLuodiInfo bisLuodiInfo = luodiService.get(id);

        Date now = new Date();

        String result = presenceBoxService.startCd(bisLuodiInfo.getCtnNum(), now);

        return result;
    }

    /**
     * 结束插电
     */
    @RequestMapping(value = "endElectrcity")
    @ResponseBody
    private String endElectrcity(Integer id) {

        BisLuodiInfo bisLuodiInfo = luodiService.get(id);

        Date now = new Date();

        String result = presenceBoxService.endCd(bisLuodiInfo.getCtnNum(), now);

        if (SUCCESS.equals(result)) {
            // 结束插电成功则计算费用
            standingBookService.luoDiMoney(
                    bisLuodiInfo.getCtnNum(),
                    bisLuodiInfo.getBillCode(),
                    bisLuodiInfo.getClientId(),
                    bisLuodiInfo.getYfClientId()
            );
        }

        return result;
    }

    /**
     * 激活任务
     */
    @RequestMapping(value = "startTask")
    @ResponseBody
    private String startTask(Integer id) {

        BisLuodiInfo bisLuodiInfo = luodiService.get(id);

        BisPresenceBox bisPresenceBox = presenceBoxService.getBoxInfo(bisLuodiInfo.getCtnNum());

        // 计划已经完成，不能激活
//        if ("2".equals(bisLuodiInfo.getState())) {
//            return "计划已经完成，不能激活";
//        }

        // 装车操作时，需要结束插电
        if ("L".equals(bisLuodiInfo.getLuodiType()) && "1".equals(bisPresenceBox.getIfCd()) && !"2".equals(bisPresenceBox.getCdState())) {
            return "装车前需要结束插电";
        }

        Date now = new Date();

        // 设置为"激活状态"
        bisLuodiInfo.setState("1");
        bisLuodiInfo.setStartTime(now);

        luodiService.merge(bisLuodiInfo);

        String startPositionId = bisLuodiInfo.getStartPosition();
        String endPositionId = bisLuodiInfo.getEndPosition();

        String startPosition = "";
        String endPosition = "";

        if (isNotNull(startPositionId)) {
            Integer positionId = Integer.valueOf(startPositionId);
            BaseLuodiPosition baseLuodiPosition = luodiPositionService.get(positionId);
            startPosition = baseLuodiPosition.getPositionName();
        }

        if (isNotNull(endPositionId)) {
            Integer positionId = Integer.valueOf(endPositionId);
            BaseLuodiPosition baseLuodiPosition = luodiPositionService.get(positionId);
            endPosition = baseLuodiPosition.getPositionName();
        }

        Map<String, String> params = new HashMap<>();
        params.put("type", bisLuodiInfo.getLuodiType());
        params.put("tdh", bisLuodiInfo.getBillCode());
        params.put("xh", bisLuodiInfo.getCtnNum());
        params.put("xx", bisLuodiInfo.getCtnVersion());
        params.put("cc", bisLuodiInfo.getCtnSize());
        params.put("cswz", startPosition);
        params.put("mdwz", endPosition);
        params.put("cph", bisLuodiInfo.getCarNum());
        // 远程调用完成接口时使用
        params.put("planId", bisLuodiInfo.getId() + "");

        String url = PropertiesUtil.getPropertiesByName("luodi_url", "remote-interface-url");

        String result = httpGo.sendRequest(url, params);

        System.out.println("TAG - " + result);

        return SUCCESS;
    }

    /**
     * 通过提单号获取客户信息
     */
    @RequestMapping(value = "client")
    @ResponseBody
    private BisEnterStock getClientInfo(String billCode) {

        Map<String, Object> params = new HashMap<>();
        params.put("itemNum", billCode);
        params.put("delFlag", "0");

        List<BisEnterStock> result = enterStockService.findEnterList(params);

        if (result.size() > 0)
            return result.get(0);

        return null;

    }

    // ------------------ 落地场位管理 ------------------

    @RequestMapping(value = "position")
    public String position() {
        return "wms/luodi/position";
    }

    @RequestMapping(value = "position/page")
    @ResponseBody
    public Map<String, Object> pagePositionLuodi(HttpServletRequest request) {

        Page<BaseLuodiPosition> page = getPage(request);

        BaseLuodiPosition entity = reflectParameter(request, BaseLuodiPosition.class);

        page = luodiPositionService.pageLuodiPosition(page, entity);

        return getEasyUIData(page);
    }

    /**
     * 全部落地场位，不包括有箱子的
     */
    @RequestMapping(value = "position/list")
    @ResponseBody
    public List<Map<String, Object>> listLuodiPosition(HttpServletRequest request) {

        String param = request.getParameter("q");

        return luodiPositionService.listLuodiPosition(param);
    }

    @RequestMapping(value = "position/form")
    public String createForm(Model model, String action, Integer id) {

        model.addAttribute("action", action);

        if ("update".equals(action)) {
            BaseLuodiPosition baseLuodiPosition = luodiPositionService.get(id);

            model.addAttribute("id", baseLuodiPosition.getId());
            model.addAttribute("positionFirst", baseLuodiPosition.getPositionFirst());
            model.addAttribute("positionLast", baseLuodiPosition.getPositionLast());
        }

        return "wms/luodi/positionForm";
    }

    /**
     * 添加场位
     */
    @RequestMapping(value = "position/add")
    public String addPosition(BaseLuodiPosition obj) {

        obj.setPositionName(obj.getPositionFirst() + "-" + obj.getPositionLast());
        obj.setPositionMax(1);
        obj.setNowNum(0);
        obj.setState(0);

        luodiPositionService.merge(obj);

        return "wms/luodi/position";
    }

    /**
     * 修改场位
     */
    @RequestMapping(value = "position/update")
    public String updatePosition(BaseLuodiPosition baseLuodiPosition) {

        BaseLuodiPosition obj = luodiPositionService.get(baseLuodiPosition.getId());
        obj.setPositionName(baseLuodiPosition.getPositionFirst() + "-" + baseLuodiPosition.getPositionLast());
        obj.setPositionFirst(baseLuodiPosition.getPositionFirst());
        obj.setPositionLast(baseLuodiPosition.getPositionLast());

        luodiPositionService.merge(obj);

        return "wms/luodi/position";
    }

    /**
     * 删除场位
     */
    @RequestMapping(value = "position/delete")
    @ResponseBody
    public String deleteTime(Integer id) {

        BaseLuodiPosition obj = luodiPositionService.get(id);

        obj.setState(1);

        luodiPositionService.merge(obj);

        return SUCCESS;
    }

    /**
     * 删除落地箱
     */
    @RequestMapping(value = "deleteLuoDi/{id}")
    @ResponseBody
    public String deleteEnterStock(@PathVariable("id") Integer id) {
        luodiService.delete(id);
        return "success";
    }

}
