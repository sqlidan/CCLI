package com.haiersoft.ccli.junit;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.supervision.service.SupervisionService;
import com.haiersoft.ccli.system.entity.ScheduleJob;
import com.haiersoft.ccli.wms.dao.TaskRemindDAO;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.text.SimpleDateFormat;
import java.util.Date;

@ContextConfiguration(locations = {"/applicationContext.xml"})
public class CcliTest extends AbstractJUnit4SpringContextTests {

    //@Autowired
    //private UserService userService;

    @Autowired
    private TaskRemindDAO taskRemindDAO;

    @Autowired
    private SupervisionService supervisionService;
    @Test
    public void quartzTest() throws Exception {
        String result = taskRemindDAO.hasUnreadTask("a");
        System.out.println(result);
    }
    @Test
    public void quartzTest1() throws Exception {
        //SupervisionController supervisionController=new SupervisionController();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        supervisionService.createXml();
        System.out.println("任务名称 = [" + "测试分类监管定时任务" + "]"+ " 在 " + dateFormat.format(new Date())+" 时运行");
    }

    @Test
    public void interfaceTest() {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(IBusinessPlatform.class);
        factory.setAddress("http://localhost:8080/ccli/ws/businessPlatformWebService?wsdl");

        IBusinessPlatform service = (IBusinessPlatform) factory.create();

        String result = service.addPlan(
                "string",
                "YY000001",
                "0800",
                "2017-06-23",
                "1115",
                "正通航",
                "TDH000001",
                "XH000001",
                "SKU000001",
                "10",
                "20",
                "鲁A00001",
                "123456789012345678",
                "俄罗斯红肠"
        );

        System.out.println(result);

    }

    @Test
    public void interfaceTest1() {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(IBusinessPlatform.class);
        factory.setAddress("http://localhost:8080/ccli/ws/businessPlatformWebService?wsdl");

        IBusinessPlatform service = (IBusinessPlatform) factory.create();

        String result = service.hasAuth(
                "string", "1115"
        );

        System.out.println(result);

    }

    @Test
    public void businessTest() {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CustomerService.class);
        factory.setAddress("http://10.14.1.1:8089/ws/ws/customerService?wsdl");

        CustomerService service = (CustomerService) factory.create();

        String result = service.addTime("1", "0800", 10, "somenote");

        System.out.println(result);

    }

    @Test
    public void clientTest() {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CustomerService.class);
        factory.setAddress("http://10.14.1.1:8089/ws/ws/customerService?wsdl");

        CustomerService service = (CustomerService) factory.create();

        BaseClientInfo clientInfo = new BaseClientInfo();
        clientInfo.setIds(1);
        clientInfo.setClientName("测试用户");

        String result = service.addInfo(1,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                0,
                "",
                "",
                "");

        System.out.println(result);

    }
}