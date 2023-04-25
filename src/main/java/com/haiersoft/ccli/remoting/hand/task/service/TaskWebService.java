package com.haiersoft.ccli.remoting.hand.task.service;
import javax.jws.WebService;
import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.wms.service.TaskRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author PYL
 * @ClassName: TaskWebService
 * @Description: 任务提醒webService接口
 */
@WebService
@Service
public class TaskWebService {

    //@Autowired
    //private UserService userService;

    @Autowired
    private TaskRemindService taskRemindService;

    /**
     * 根据用户名查找是否有未读任务提醒
     *
     * @param name 提醒用户名
     * @return 0有未读 1没有未读
     */
    public String hasUnreadTask(String name) {

        String hasUnreadTask = taskRemindService.hasUnreadTask(name);

        Result<String> result = new Result<String>();

        if ("0".equals(hasUnreadTask)) {
            result.setObj(hasUnreadTask);
            result.setCode(0);
            result.setMsg("有未读消息");
        } else {
            result.setObj(hasUnreadTask);
            result.setCode(0);
            result.setMsg("没有未读消息");
        }

        return JSON.toJSONString(result);
    }

}