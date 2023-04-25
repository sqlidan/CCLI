package com.haiersoft.ccli.wms.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BaseRemindTask;
import com.haiersoft.ccli.wms.service.TaskRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("wms/task")
public class TaskRemindController extends BaseController {

    @Autowired
    private TaskRemindService taskRemindService;

    @RequestMapping("index")
    public String index() {
        return "wms/task/index";
    }

    /**
     * 分页查询任务提醒
     */
    @RequestMapping("page")
    @ResponseBody
    public Map<String, Object> page(HttpServletRequest request) {

        User user = UserUtil.getCurrentUser();

        BaseRemindTask remindTask = reflectParameter(request, BaseRemindTask.class);
        remindTask.setUserName(user.getName());
        remindTask.setState(remindTask.getState() - 1);

        Page<BaseRemindTask> page = getPage(request);

        Page<BaseRemindTask> result = taskRemindService.page(page, remindTask);

        return getEasyUIData(result);
    }

    /**
     * 将任务提醒标记为已读
     *
     * @param id 任务id号
     */
    @RequestMapping("read")
    @ResponseBody
    public String readMsg(Integer id) {

        BaseRemindTask remindTask = taskRemindService.get(id);

        if (remindTask == null) return FAILED;

        remindTask.setState(BaseRemindTask.READ);

        taskRemindService.update(remindTask);

        return SUCCESS;
    }

    /**
     * 弹出预警报警
     */
    @RequestMapping(value = "warning", method = RequestMethod.GET)
    @ResponseBody
    public String warning() {

        User user = UserUtil.getCurrentUser();

        List<BaseRemindTask> warningList = taskRemindService.getSteveWarningList(user.getName());
        if (!warningList.isEmpty()) {
            StringBuffer sbA = new StringBuffer();
            StringBuffer sbL = new StringBuffer();
            for (BaseRemindTask obj : warningList) {
                if (obj.getType().equals("1")) {
                    sbA.append(obj.getContent().split(" ")[1] + ",");
                } else if (obj.getType().equals("2")) {
                    sbL.append(obj.getContent().split(" ")[1] + ",");
                }
            }

            //ASN号拼接
            String asn = sbA.toString();
            if (asn.equals("")) {
                asn = asn.substring(0, asn.length() - 1);
            }

            //装车单号拼接
            String loadingNum = sbL.toString();
            if (loadingNum.equals("")) {
                loadingNum = loadingNum.substring(0, loadingNum.length() - 1);
            }

            //返回处理
            if (!asn.equals("") && loadingNum.equals("")) {
                return "ASN号为：" + asn + "的单据产生了人工成本，请及时维护应收费用！";
            } else if (asn.equals("") && !loadingNum.equals("")) {
                return "装车单号为：" + loadingNum + "的单据产生了人工成本，请及时维护应收费用！";
            } else if (!asn.equals("") && !loadingNum.equals("")) {
                return "ASN号为：" + asn + "和装车单号为：" + loadingNum + "的单据产生了人工成本，请及时维护应收费用！";
            } else {
                return "";
            }

        } else {
            return "success";
        }
    }

}