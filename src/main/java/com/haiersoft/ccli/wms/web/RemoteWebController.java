package com.haiersoft.ccli.wms.web;

import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.remoting.platform.base.JsonBean;
import com.haiersoft.ccli.wms.entity.BaseLuodiPosition;
import com.haiersoft.ccli.wms.entity.BisLuodiInfo;
import com.haiersoft.ccli.wms.entity.BisPresenceBox;
import com.haiersoft.ccli.wms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
/**
 * Created by galaxy on 2017/6/9.
 */
@Controller
@RequestMapping("remote/web")
public class RemoteWebController extends BaseController {

    @Autowired
    private LuodiService luodiService;

    @Autowired
    private LuodiPositionService luodiPositionService;

    @Autowired
    private PresenceBoxService presenceBoxService;

    /**
     * 完成落地计划
     *
     * @param planId   计划任务id
     * @param ctnNum   箱号
     * @param taskType 操作类型
     * @param time     结束时间
     * @param position 目的场位名 箱型大于20时，必须为01位置的场号
     * @return code 0.成功 1.失败 result 返回信息
     */
    @RequestMapping(value = "luodi/complete")
    @ResponseBody
    public String completeLuodiPlan(String planId, String ctnNum, String taskType, String position, Date time) {

        JsonBean<String> result = new JsonBean<>("string");

        // 获取场位信息
        BaseLuodiPosition luodiPosition = luodiPositionService.getLuodiPositionByName(position);

        if (luodiPosition == null) {
            result.setCode("1");
            result.setResult("目的场位错误");
            return result.toJson();
        }

        // 获取落地箱信息
        BisLuodiInfo luodiInfo = luodiService.getLuodiInfoByCtn(ctnNum, taskType);

        if (luodiInfo == null) {
            result.setCode("1");
            result.setResult("箱号错误");
            return result.toJson();
        }

        String state = luodiInfo.getState();

        if (!"1".equals(state)) {
            result.setCode("1");
            result.setResult("计划未处于激活状态");
            return result.toJson();
        }

        BisPresenceBox bisPresenceBox = presenceBoxService.getBoxInfo(ctnNum);

        if (bisPresenceBox == null) {
            result.setCode("1");
            result.setResult("箱号错误");
            return result.toJson();
        }

        // 在场箱操作返回值
        String st = null;

        if ("F".equals(taskType)) {
            luodiInfo.setEndTime(time);
            st = presenceBoxService.luodiFromCar(ctnNum);
            result.setResult("完成落地");
        } else if ("L".equals(taskType)) {
            luodiInfo.setEndTime(time);
            st = presenceBoxService.upCar(ctnNum, luodiInfo.getCarNum());
            result.setResult("完成装车");
        } else if ("M".equals(taskType)) {
            luodiInfo.setEndTime(time);
            result.setResult("完成场内转移");
        }

        if (!"success".equals(st)) {
            result.setCode("1");
            result.setResult("完成计划失败");
            return result.toJson();
        }

        // 修改落地计划状态为"完成"
        luodiInfo.setEndTime(time);
        luodiInfo.setState("2"); // 任务完成
        luodiService.merge(luodiInfo);

        // 修改场位信息
        int ctnVersion = Integer.parseInt(bisPresenceBox.getCtnSize());

        String startPosition = null;
        String otherStartPosition = null;
        String endPosition = null;
        String otherEndPosition = null;

        if (ctnVersion > 20) {

            if ("F".equals(taskType)) {
                endPosition = luodiPositionService.get(Integer.valueOf(luodiInfo.getEndPosition())).getPositionName();
                otherEndPosition = LuodiController.getOtherPosition(endPosition);
                luodiPositionService.addPositionNumByName(endPosition);
                luodiPositionService.addPositionNumByName(otherEndPosition);
            } else if ("L".equals(taskType)) {
                endPosition = luodiPositionService.get(Integer.valueOf(luodiInfo.getEndPosition())).getPositionName();
                otherEndPosition = LuodiController.getOtherPosition(endPosition);
                luodiPositionService.reducePositionNumByName(endPosition);
                luodiPositionService.reducePositionNumByName(otherEndPosition);
            } else if ("M".equals(taskType)) {
                startPosition = luodiPositionService.get(Integer.valueOf(luodiInfo.getStartPosition())).getPositionName();
                otherStartPosition = LuodiController.getOtherPosition(startPosition);
                endPosition = luodiPositionService.get(Integer.valueOf(luodiInfo.getEndPosition())).getPositionName();
                otherEndPosition = LuodiController.getOtherPosition(endPosition);
                luodiPositionService.reducePositionNumByName(startPosition);
                luodiPositionService.reducePositionNumByName(otherStartPosition);
                luodiPositionService.addPositionNumByName(endPosition);
                luodiPositionService.addPositionNumByName(otherEndPosition);
            }

        } else {

            if ("F".equals(taskType)) {
                endPosition = luodiPositionService.get(Integer.valueOf(luodiInfo.getEndPosition())).getPositionName();
                luodiPositionService.addPositionNumByName(endPosition);
            } else if ("L".equals(taskType)) {
                endPosition = luodiPositionService.get(Integer.valueOf(luodiInfo.getEndPosition())).getPositionName();
                luodiPositionService.addPositionNumByName(endPosition);
            } else if ("M".equals(taskType)) {
                startPosition = luodiPositionService.get(Integer.valueOf(luodiInfo.getStartPosition())).getPositionName();
                endPosition = luodiPositionService.get(Integer.valueOf(luodiInfo.getEndPosition())).getPositionName();
                luodiPositionService.reducePositionNumByName(startPosition);
                luodiPositionService.addPositionNumByName(endPosition);
            }

        }

        return result.toJson();

    }

}
