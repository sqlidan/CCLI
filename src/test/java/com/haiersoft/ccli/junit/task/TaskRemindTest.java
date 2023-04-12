package com.haiersoft.ccli.junit.task;

import com.haiersoft.ccli.remoting.hand.task.service.TaskWebService;
//import com.haiersoft.ccli.system.service.UserService;
//import com.haiersoft.ccli.wms.dao.TaskRemindDAO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"/applicationContext.xml"})
public class TaskRemindTest extends AbstractJUnit4SpringContextTests {

    //@Autowired
    //private UserService userService;

    //@Autowired
    //private TaskRemindDAO taskRemindDAO;

    @Autowired
    private TaskWebService taskWebService;

    @Test
    public void hasUnreadTask() {
        System.out.println(taskWebService.hasUnreadTask("Bob"));
    }

}
