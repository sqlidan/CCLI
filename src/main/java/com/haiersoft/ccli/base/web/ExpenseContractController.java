package com.haiersoft.ccli.base.web;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.ExpenseContract;
import com.haiersoft.ccli.base.entity.ExpenseContractInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseContractInfoService;
import com.haiersoft.ccli.base.service.ExpenseContractService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
/**
 * @author Connor.M
 * @ClassName: ExpenseContractController
 * @Description: 合同Controller
 * @date 2016年2月24日 下午5:20:33
 */
@Controller
@RequestMapping("base/contract")
public class ExpenseContractController extends BaseController {

    @Autowired
    private ExpenseContractService expenseContractService;
    @Autowired
    private ExpenseContractInfoService expenseContractInfoService;
    @Autowired
    private ClientService clientService;

    /**
     * 默认页面
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "base/contract/expenseContractList";
    }

    /**
     * @return
     * @throws ParseException 
     * @throws
     * @author Connor.M
     * @Description: 合同查询
     * @date 2016年2月24日 下午3:42:10
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<ExpenseContract> page = getPage(request);
        page.setOrderBy("expirationTime");
        page.setOrder(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQS_isDel", "0");
        filters.add(filter);
        
        String tag = request.getParameter("expirationIndex");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        if("1".equals(tag)) {
        	Date now=new Date();
        	String end=df.format(now);
        	PropertyFilter filterExpirationIndexEnd = new PropertyFilter("LED_expirationTime", end);
        	filters.add(filterExpirationIndexEnd);
        }
        else if("2".equals(tag)) {
        	 Calendar c = Calendar.getInstance();
        	 c.setTime(new Date());
             c.add(Calendar.MONTH, -1);
             Date lastMonth = c.getTime();
             Date now=new Date();
            String start = df.format(lastMonth);
            String end = df.format(now);
            PropertyFilter filter2 = new PropertyFilter("GED_expirationTime", start);
            PropertyFilter filter3 = new PropertyFilter("LED_expirationTime", end);
            filters.add(filter2);
            filters.add(filter3);
        }else if("3".equals(tag)){
        	 Calendar c = Calendar.getInstance();
        	 c.setTime(new Date());
             c.add(Calendar.MONTH, +1);
             Date afterMonth = c.getTime();
             Date now=new Date();
            String start = df.format(now);
            String end = df.format(afterMonth);
            PropertyFilter filter2 = new PropertyFilter("GED_expirationTime", start);
            PropertyFilter filter3 = new PropertyFilter("LED_expirationTime", end);
            filters.add(filter2);
            filters.add(filter3);
        }

        page = expenseContractService.search(page, filters);
        return getEasyUIData(page);
    }

    /**
     * @param model
     * @return
     * @throws
     * @author Connor.M
     * @Description: 合同录入展示
     * @date 2016年2月24日 下午6:48:15
     */
    @RequestMapping(value = "createContractForm", method = RequestMethod.GET)
    public String createContractForm(Model model) {
        String contractNum = expenseContractService.getContractIdToString();//合同号
        User user = UserUtil.getCurrentUser();

        ExpenseContract contract = new ExpenseContract();
        contract.setContractNum(contractNum);
        contract.setOperatorPerson(user.getName());
        contract.setOperateTime(new Date());

        model.addAttribute("contract", contract);
        model.addAttribute("controller", "createContract");
        return "base/contract/editContractForm";
    }

    /**
     * @param model
     * @return
     * @throws
     * @author Connor.M
     * @Description: 合同修改展示
     * @date 2016年2月27日 上午11:35:02
     */
    @RequestMapping(value = "updateContractForm/{contractNum}", method = RequestMethod.GET)
    public String updateContractForm(Model model, @PathVariable("contractNum") String contractNum) {
        ExpenseContract contract = expenseContractService.get(contractNum);
        model.addAttribute("contract", contract);
        model.addAttribute("controller", "updateContract");
        return "base/contract/editContractForm";
    }


    /**
     * @param model
     * @param contractNum
     * @return
     * @throws
     * @author Connor.M
     * @Description: 合同查看
     * @date 2016年2月28日 下午2:56:57
     */
    @RequestMapping(value = "checkContractForm/{contractNum}", method = RequestMethod.GET)
    public String checkContractForm(Model model, @PathVariable("contractNum") String contractNum) {
        ExpenseContract contract = expenseContractService.get(contractNum);
        model.addAttribute("contract", contract);
        return "base/contract/checkContractForm";
    }

    /**
     * @param model
     * @return
     * @throws
     * @author Connor.M
     * @Description: 保存
     * @date 2016年2月25日 下午3:13:25
     */
    @RequestMapping(value = "createContract", method = RequestMethod.POST)
    @ResponseBody
    public String createContract(HttpServletRequest request, HttpServletResponse response) {
        ExpenseContract contract = new ExpenseContract();
        parameterReflect.reflectParameter(contract, request);//转换对应实体类参数
        BaseClientInfo client = clientService.get(StringUtils.toInteger(contract.getClientId()));
        contract.setClientName(client.getClientName());
        contract.setIsDel("0");//删除标志，正常
        expenseContractService.merge(contract);
        return "success";
    }

    /**
     * @param contract
     * @param model
     * @return
     * @throws
     * @author Connor.M
     * @Description: 修改
     * @date 2016年2月27日 下午12:44:49
     */
    @RequestMapping(value = "updateContract", method = RequestMethod.POST)
    @ResponseBody
    public String updateContract(@Valid ExpenseContract contract, Model model) {
        BaseClientInfo client = clientService.get(StringUtils.toInteger(contract.getClientId()));
        User user = UserUtil.getCurrentUser();

        contract.setClientName(client.getClientName());
        contract.setUpdatePerson(user.getName());
        contract.setUpdateTime(new Date());
        contract.setIsDel("0");//删除标志，正常
        expenseContractService.update(contract);
        return "success";
    }

    /**
     * @param id
     * @return
     * @throws
     * @author Connor.M
     * @Description: 删除
     * @date 2016年2月25日 下午4:09:50
     */
    @RequestMapping(value = "deleteContract/{id}")
    @ResponseBody
    public String deleteContract(@PathVariable("id") String id) {
        User user = UserUtil.getCurrentUser();
        ExpenseContract contract = expenseContractService.get(id);
        contract.setIsDel("1");//删除
        contract.setUpdatePerson(user.getName());
        contract.setUpdateTime(new Date());
        expenseContractService.update(contract);
        return "success";
    }

    /**
     * @param id
     * @return
     * @throws
     * @author PYL
     * @Description: 判断是否有未维护价钱的明细
     * @date 2016年7月14日 下午4:20:07
     */
    @RequestMapping(value = "ifnoprice/{contractNum}")
    @ResponseBody
    public String ifnoprice(@PathVariable("contractNum") String contractNum) {
        Integer obj = expenseContractService.getNoPrice(contractNum);
        if (obj == 0) {
            return "success";
        } else {
            return "false";
        }
    }

    /**
     * @param id
     * @return
     * @throws
     * @author Connor.M
     * @Description: 审核通过
     * @date 2016年2月25日 下午4:20:07
     */
    @RequestMapping(value = "passOkContract/{id}")
    @ResponseBody
    public String passOkContract(@PathVariable("id") String id) {
        User user = UserUtil.getCurrentUser();

        ExpenseContract contract = expenseContractService.get(id);
        contract.setContractState("1");
        contract.setExaminePerson(user.getName());
        contract.setExamineTime(new Date());

        expenseContractService.update(contract);
        return "success";
    }

    /**
     * @param id
     * @return
     * @throws
     * @author Connor.M
     * @Description: 取消审核
     * @date 2016年2月27日 下午2:43:55
     */
    @RequestMapping(value = "passNoContract/{id}")
    @ResponseBody
    public String passNoContract(@PathVariable("id") String id) {
        User user = UserUtil.getCurrentUser();

        ExpenseContract contract = expenseContractService.get(id);

        contract.setContractState("0");
        contract.setUpdatePerson(user.getName());
        contract.setUpdateTime(new Date());
        contract.setExaminePerson(user.getName());
        contract.setExamineTime(new Date());

        expenseContractService.update(contract);
        return "success";
    }

    /**
     * @param id
     * @return
     * @throws
     * @author Connor.M
     * @Description: 复制合同
     * @date 2016年2月25日 下午4:20:07
     */
    @RequestMapping(value = "copyContract/{id}")
    @ResponseBody
    public String copyContract(@PathVariable("id") String id) {
        expenseContractService.copyContract(id);
        return "success";
    }

    /**
     * @param id
     * @return
     * @throws
     * @author Connor.M
     * @Description: 保存并复制
     * @date 2016年2月27日 下午1:58:56
     */
    @RequestMapping(value = "createCopyContract", method = RequestMethod.POST)
    @ResponseBody
    public String createCopyContract(HttpServletRequest request, HttpServletResponse response, Model model) {
        ExpenseContract contract = new ExpenseContract();
        parameterReflect.reflectParameter(contract, request);//转换对应实体类参数

        BaseClientInfo client = clientService.get(StringUtils.toInteger(contract.getClientId()));
        contract.setClientName(client.getClientName());
        contract.setIsDel("0");//删除标志，正常
        expenseContractService.merge(contract);

        //复制
        ExpenseContract newContract = expenseContractService.copyContract(contract.getContractNum());

        model.addAttribute("contract", newContract);
        model.addAttribute("controller", "createContract");
        return newContract.getContractNum();
    }

    /**
     * @param request
     * @param response
     * @return
     * @throws
     * @author Connor.M
     * @Description: 获得合同集合
     * @date 2016年2月29日 下午2:35:09
     */
    @RequestMapping(value = "getContractAll", method = RequestMethod.GET)
    @ResponseBody
    public List<ExpenseContract> getContractAll(HttpServletRequest request, HttpServletResponse response, String customsId) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();

        PropertyFilter filter = new PropertyFilter("EQS_isDel", "0");
        filters.add(filter);
        PropertyFilter filter2 = new PropertyFilter("EQS_contractState", "1");
        filters.add(filter2);
        PropertyFilter filter3 = new PropertyFilter("EQS_clientId", customsId);
        filters.add(filter3);
        List<ExpenseContract> contracts = expenseContractService.search(filters);
        return contracts;
    }

    /**
     * @return
     * @throws
     * @author Connor.M
     * @Description: 获得最新的 合同
     * @date 2016年5月5日 下午2:58:00
     */
    @RequestMapping(value = "showContract", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> showLastContract(HttpServletRequest request) {
        Page<ExpenseContract> page = getPage(request);
        page.orderBy("signTime").order(Page.DESC);

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        PropertyFilter filter = new PropertyFilter("EQS_isDel", "0");
        filters.add(filter);

        page = expenseContractService.search(page, filters);
        return getEasyUIData(page);
    }

    /**
     * @return
     * @throws
     * @author PYL
     * @Description: 获得客户的揽货人
     * @date 2016年6月3日
     */
    @RequestMapping(value = "getSaler/{clientId}", method = RequestMethod.GET)
    @ResponseBody
    public String getSaler(@PathVariable("clientId") Integer id) {
        BaseClientInfo client = clientService.get(id);
        String saler = client.getSaler();
        return saler;
    }

    /**
     * @param request
     * @param response
     * @throws Exception
     * @throws
     * @Description: 导出excel(不带货物）
     * @date 2016年4月18日 14:09:55
     */
    @RequestMapping(value = "export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
//        PropertyFilter filter = new PropertyFilter("EQS_contractState", "1");
//       filters.add(filter);
        PropertyFilter filter2 = new PropertyFilter("EQS_isDel", "0");
        filters.add(filter2);
        List<ExpenseContract> contractList = expenseContractService.search(filters);

        ExportParams params = new ExportParams("合同", "合同sheet", ExcelType.XSSF);

        Workbook workbook = ExcelExportUtil.exportExcel(params, ExpenseContract.class, contractList);

        //       String formatFileName = URLEncoder.encode("入库报关单" +".xlsx","UTF-8");
        String formatFileNameP = "合同" + ".xlsx";
        String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }

    /**
     * @param request
     * @param response
     * @throws Exception
     * @throws
     * @Description: 导出excel(带货物）
     * @date 2016年4月18日 15:54:55
     */
    @RequestMapping(value = "exportwith")
    @ResponseBody
    public void exportWith(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获得主表信息
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
//        PropertyFilter filter = new PropertyFilter("EQS_contractState", "1");
//        filters.add(filter);
        PropertyFilter filter2 = new PropertyFilter("EQS_isDel", "0");
        filters.add(filter2);
        List<ExpenseContract> contractList = expenseContractService.search(filters);

        List<ExpenseContractInfo> contractInfoList = new ArrayList<ExpenseContractInfo>();
        List<ExpenseContractInfo> objList = new ArrayList<ExpenseContractInfo>();
        for (ExpenseContract obj : contractList) {
            contractInfoList = expenseContractInfoService.findByNum(obj.getContractNum());
            objList.addAll(contractInfoList);
        }
        ExportParams params = new ExportParams("合同", "合同sheet", ExcelType.XSSF);
        ExportParams params2 = new ExportParams("合同明细", "合同明细sheet", ExcelType.XSSF);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> p1 = new HashMap<String, Object>();
        p1.put("title", params);
        p1.put("entity", ExpenseContract.class);
        p1.put("data", contractList);
        list.add(p1);
        Map<String, Object> p2 = new HashMap<String, Object>();
        p2.put("title", params2);
        p2.put("entity", ExpenseContractInfo.class);
        p2.put("data", objList);
        list.add(p2);
        Workbook workbook = ExcelExportUtil.exportExcel(list, "abc");
//        String formatFileName = URLEncoder.encode("入库报关单（带货物）" +".xlsx","UTF-8");
        String formatFileNameP = "合同明细" + ".xlsx";
        String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }
}
