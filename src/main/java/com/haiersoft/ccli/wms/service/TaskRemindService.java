package com.haiersoft.ccli.wms.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.TaskRemindDAO;
import com.haiersoft.ccli.wms.entity.BaseRemindTask;
import com.haiersoft.ccli.wms.entity.BisAsn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskRemindService extends BaseService<BaseRemindTask, Integer> {

    @Autowired
    private TaskRemindDAO taskRemindDAO;

    @Override
    public HibernateDao<BaseRemindTask, Integer> getEntityDao() {
        return taskRemindDAO;
    }

    public String hasUnreadTask(String userName) {
        return taskRemindDAO.hasUnreadTask(userName);
    }

    public Page<BaseRemindTask> page(Page<BaseRemindTask> page, BaseRemindTask remindTask) {
        return taskRemindDAO.page(page, remindTask);
    }

    public String readMsg(String id) {
        return taskRemindDAO.readMsg(id);
    }

    //生成ASN单据的提醒
    public void steveTask(BisAsn asnObj, String type) {
        BaseRemindTask taskObj = new BaseRemindTask();
        User user = UserUtil.getCurrentUser();
        taskObj.setState(0);
        taskObj.setCreateTime(new Date());
        taskObj.setCreateUser(user.getName());
        taskObj.setType(type);
        taskObj.setUserName(asnObj.getCreateUser());
        taskObj.setContent("ASN号为 " + asnObj.getAsn() + " 的单据产生了人工成本，请及时维护应收费用。");
        taskRemindDAO.save(taskObj);
    }

    //生成装车单据的提醒
    public void steveTask(String operator, String loadingNum, String type) {
        BaseRemindTask taskObj = new BaseRemindTask();
        User user = UserUtil.getCurrentUser();
        taskObj.setState(0);
        taskObj.setCreateTime(new Date());
        taskObj.setCreateUser(user.getName());
        taskObj.setType(type);
        taskObj.setUserName(operator);
        taskObj.setContent("装车单号为 " + loadingNum + " 的单据产生了人工成本，请及时维护应收费用。");
        taskRemindDAO.save(taskObj);
    }

    //获取装卸的任务提醒
    public List<BaseRemindTask> getSteveWarningList(String userName) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_userName", userName));
        filters.add(new PropertyFilter("EQI_state", 0));
        filters.add(new PropertyFilter("INAS_type", new String[]{"1", "2"}));
        List<BaseRemindTask> infos = taskRemindDAO.find(filters);
        return infos;
    }
}
