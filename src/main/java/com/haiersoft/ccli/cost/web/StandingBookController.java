package com.haiersoft.ccli.cost.web;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.haiersoft.ccli.wms.entity.*;
import com.haiersoft.ccli.wms.service.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.service.BaseInvoiceService;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.BisCheckingBook;
import com.haiersoft.ccli.cost.entity.BisPayInfo;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.service.AsnActionLogService;
import com.haiersoft.ccli.cost.service.BisCheckingBookService;
import com.haiersoft.ccli.cost.service.BisPayInfoService;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.remoting.hand.invoice.entity.BaseInvoice;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * @author Connor.M
 * @ClassName: StandingBookController
 * @Description: 费用台账 Controller
 * @date 2016年3月31日 下午3:13:31
 */
@Controller
@RequestMapping("cost/standingBook")
public class StandingBookController extends BaseController {

    @Autowired
    private AsnActionLogService asnActionLogService;
    @Autowired
    private StandingBookService standingBookService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private EnterStockService enterStockService;
    @Autowired
    private OutStockService outStockService;
    @Autowired
    private LoadingOrderInfoService loadingOrderInfoService;
    @Autowired
    private BisCheckingBookService bisCheckingBookService;//对账单
    @Autowired
    private LoadingOrderService loadingOrderService;
    @Autowired
    private AsnActionService asnActionService;
    @Autowired
    private BisPayInfoService bisPayInfoService;
    @Autowired
    private BaseInvoiceService baseInvoiceService;
    @Autowired
    private ASNService asnService;


    //台账页面
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        return "base/standingBook";
    }

    //台账页面
    @RequestMapping(value = "querylist", method = RequestMethod.GET)
    public String queryList(HttpServletRequest request, Model model) {
        return "base/standingBookQuery";
    }
    
    //月费目汇总表页面调整
    @RequestMapping(value = "summonthlist", method = RequestMethod.GET)
    public String summonthList(HttpServletRequest request, Model model) {
        return "base/standingBookSumMonthQuery";
    }
    
    /*
     * 列表页面table获取月费目汇总表数据
     * */
    @RequiresPermissions("bis:checkbook:summonthview")
    @RequestMapping(value = "listsummonthjson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSumMonthJsonData(HttpServletRequest request) {
    	Page<BisStandingBook> page = getPage(request);
    	page.orderBy(page.getOrderBy()).order(page.getOrder());
    	BisStandingBook stock = new BisStandingBook();
    	parameterReflect.reflectParameter(stock, request);
    	page = standingBookService.getStocks(page, stock);
        return getEasyUIData(page);
    }
    
    //月费目客户明细汇总
    @RequestMapping(value = "customerlist", method = RequestMethod.GET)
    public String customerlist(HttpServletRequest request, Model model) {
        return "base/standingBookSumCustomerQuery";
    }
    
    /*
     * 列表页面table获取月客户费目明细查询表格
     * */
    @RequiresPermissions("bis:checkbook:customerview")
    @RequestMapping(value = "listcustomerjson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getCustomerJsonData(HttpServletRequest request) {
    	Page<BisStandingBook> page = getPage(request);
    	page.orderBy(page.getOrderBy()).order(page.getOrder());
    	BisStandingBook stock = new BisStandingBook();
    	parameterReflect.reflectParameter(stock, request);
    	page = standingBookService.getCustomerStocks(page, stock);
        return getEasyUIData(page);
    }
    //月供应商对账单	
    @RequestMapping(value = "supplierlist", method = RequestMethod.GET)
    public String supplierlist(HttpServletRequest request, Model model) {
        return "base/standingBookSupplierQuery";
    }
    /**
     * 调整发票
     * @param codeNum
     * @param model
     * @return
     */
    @RequestMapping(value = "editUpload/{codeNum}", method = RequestMethod.GET)
    public String editUpload(@PathVariable("codeNum") String codeNum, Model model) {
    	BisCheckingBook obj =bisCheckingBookService.get(codeNum);
    	BaseClientInfo clientInfo=clientService.get(Integer.parseInt(obj.getCustomID()));
    	List<Map<String, Object>> list=bisCheckingBookService.getFeeRmb(codeNum);
    	model.addAttribute("list",list);
    	model.addAttribute("obj", obj);
    	model.addAttribute("custom",clientInfo.getSrcCustName()!=null?clientInfo.getSrcCustName():clientInfo.getClientName());
        model.addAttribute("action", "editUpload");
        return "base/standingBookUpload";
    }
    /**
     * 财务发票确认无误后提交
     * @param codeNum
     * @param model
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "editUpload", method = RequestMethod.POST)
    @ResponseBody
    public String editUpload(HttpServletRequest request){
    	return bisCheckingBookService.editUpload(request);
    }

    //上传发票	
    @RequestMapping(value = "upload/{codeNum}", method = RequestMethod.GET)
    public String upload(@PathVariable("codeNum") String codeNum, Model model) {
    	BisCheckingBook obj =bisCheckingBookService.get(codeNum);
    	BaseClientInfo clientInfo=clientService.get(Integer.parseInt(obj.getCustomID()));
    	List<Map<String, Object>> list=bisCheckingBookService.getFeeRmb(codeNum);
    	model.addAttribute("list",list);
    	model.addAttribute("obj", obj);
    	model.addAttribute("custom",clientInfo.getSrcCustName()!=null?clientInfo.getSrcCustName():clientInfo.getClientName());
        model.addAttribute("action", "upload");
        return "base/standingBookUpload";
    }
    /**
     * 财务发票确认无误后提交
     * @param codeNum
     * @param model
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public String uploadSumbit(HttpServletRequest request){
    	return bisCheckingBookService.upload(request);
    }
    
    //开通发票	
    @RequestMapping(value = "open/{codeNum}", method = RequestMethod.GET)
    public String open(@PathVariable("codeNum") String codeNum, Model model) {
    	BisCheckingBook obj =bisCheckingBookService.get(codeNum);
    	BaseClientInfo clientInfo=clientService.get(Integer.parseInt(obj.getCustomID()));
    	List<Map<String, Object>> list=bisCheckingBookService.getFeeRmb(codeNum);
    	model.addAttribute("list",list);
    	model.addAttribute("obj", obj);
    	model.addAttribute("custom",clientInfo.getSrcCustName()!=null?clientInfo.getSrcCustName():clientInfo.getClientName());
        model.addAttribute("action", "open");
        return "base/standingBookUpload";
    }
    /**
     * 开票确认操作
     * @param request
     * @return
     */
    @RequestMapping(value = "open", method = RequestMethod.POST)
    @ResponseBody
    public String openSumbit(HttpServletRequest request){
    	return bisCheckingBookService.open(request);
    }
    
    //打印发票	
    @RequestMapping(value = "print/{codeNum}", method = RequestMethod.GET)
    public String print(@PathVariable("codeNum") String codeNum, Model model) {
    	BisCheckingBook bisCheckingBook=bisCheckingBookService.find("codeNum",codeNum);
    	if("success".equals(bisCheckingBook.getResult())&&"X".equals(bisCheckingBook.getJsfs())){
    		model.addAttribute("invoiceNo",bisCheckingBook.getInvoiceNum());
    		model.addAttribute("invoiceCode",bisCheckingBook.getInvoiceCode());
    		model.addAttribute("custom",bisCheckingBook.getSrcCustName());
    	}else{
    		BaseInvoice baseInvoice=baseInvoiceService.find("srcBizDocId",codeNum);
    		model.addAttribute("invoiceNo",baseInvoice.getInvoiceNo());
    		model.addAttribute("invoiceCode",baseInvoice.getInvoiceCode());
    		model.addAttribute("custom",baseInvoice.getCustomName());
    	}
    	model.addAttribute("codeNum",codeNum);
		model.addAttribute("action","print");
        return "base/standingBookPrint";
    }
    /**
     * 打印发票数据
     * @param codeNum
     * @return
     */
    @RequestMapping(value = "print", method = RequestMethod.POST)
    @ResponseBody
    public String printData(HttpServletRequest request){
    	return bisCheckingBookService.printDate(request);
    }
    
    
    //发票作废	
    @RequestMapping(value = "cancel/{codeNum}", method = RequestMethod.GET)
    public String cancel(@PathVariable("codeNum") String codeNum, Model model) {
    	BisCheckingBook bisCheckingBook=bisCheckingBookService.find("codeNum",codeNum);
    	BaseInvoice baseInvoice=baseInvoiceService.find("srcBizDocId",codeNum);
    	if(null!=baseInvoice){
    		model.addAttribute("invoiceNo",baseInvoice.getInvoiceNo());
    		model.addAttribute("invoiceCode",baseInvoice.getInvoiceCode());
    		model.addAttribute("custom",baseInvoice.getCustomName());
    	}else{
    		model.addAttribute("invoiceNo",bisCheckingBook.getInvoiceNum());
    		model.addAttribute("invoiceCode",bisCheckingBook.getInvoiceCode());
    		model.addAttribute("custom",bisCheckingBook.getSrcCustName());
    	}
    	model.addAttribute("codeNum",codeNum);
		model.addAttribute("action","cancel");
        return "base/standingBookPrint";
    }
    
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    @ResponseBody
    public String cancelData(HttpServletRequest request){
    	return bisCheckingBookService.cancelData(request);
    }

    @RequestMapping(value = "delData/{codeNum}", method = RequestMethod.POST)
    @ResponseBody
    public String delData(@PathVariable("codeNum") String codeNum){
       return bisCheckingBookService.delData(codeNum);
    }
    
    @RequiresPermissions("bis:checkbook:supplierview")
    @RequestMapping(value = "listsupplierjson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSupplierJsonData(HttpServletRequest request) {
    	Page<BisStandingBook> page = getPage(request);
    	page.orderBy(page.getOrderBy()).order(page.getOrder());
    	BisStandingBook stock = new BisStandingBook();
    	parameterReflect.reflectParameter(stock, request);
    	page = standingBookService.getSupplierStocks(page, stock);
        return getEasyUIData(page);
    }

    /*
     * 列表页面table获取应付对账内容
     * */
    @RequiresPermissions("bis:checkbook:view")
    @RequestMapping(value = "listjson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getJsonData(HttpServletRequest request) {
        Page<BisCheckingBook> page = getPage(request);
        page.orderBy("operateTime").order(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = bisCheckingBookService.search(page, filters);
        return getEasyUIData(page);
    }

    //应付账单添加页面跳转
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public String query(HttpServletRequest request, Model model) {
        BisCheckingBook obj = new BisCheckingBook();
        obj.setCodeNum(bisCheckingBookService.getKeyCode());
        model.addAttribute("obj", obj);
        model.addAttribute("action", "create");
        return "base/standingBookCheck";
    }
    /**
     * 加入判断未保存的方法
     * @return
     */
    /**
	 * 
	 * @author pyl
	 * @Description: 判断业务付款单是否已保存
	 * @date 2016年5月13日 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="ifsave/{codeNum}", method = RequestMethod.GET)
	@ResponseBody
	public String ifsave(@PathVariable("codeNum") String codeNum) {
		BisCheckingBook obj = bisCheckingBookService.find("codeNum", codeNum);
		if(null != obj && !"".equals(obj)){
			return "success";
		}else{
			return "false";
		}
	}

    //应付账单修改页面跳转
    @RequestMapping(value = "upquery/{linkId}", method = RequestMethod.GET)
    public String upquery(@PathVariable("linkId") String linkId, Model model) {
        BisCheckingBook obj = new BisCheckingBook();
        obj = bisCheckingBookService.get(linkId);
        model.addAttribute("obj", obj);
        model.addAttribute("action", "update");
        return "base/standingBookCheck";
    }

    /**
     * 删除对账单主单信息，修改台账对账单号为null
     */
    @RequiresPermissions("bis:checkbook:del")
    @RequestMapping(value = "delquery/{linkId}", method = RequestMethod.POST)
    @ResponseBody
    public String delquery(@PathVariable("linkId") String linkId, Model model) {
        String endStr = "error";
        if (linkId != null && !"".equals(linkId)) {
            BisCheckingBook newObj = bisCheckingBookService.get(linkId);
            if (newObj != null && newObj.getCodeNum() != null) {
                endStr = bisCheckingBookService.delStingBookCodeList(newObj.getCodeNum());
                if ("success".equals(endStr)) {
                    bisCheckingBookService.delete(newObj);
                }
            }
        }
        return endStr;
    }

    //打开添加对账明细页面弹窗
    @RequestMapping(value = "addInfo/{linkId}", method = RequestMethod.GET)
    public String openAddInfo(@PathVariable("linkId") String linkId, Model model) {
        model.addAttribute("code", linkId);
        return "base/addStandingBookCheck";
    }

    //添加对账明细添加--临时添加
    @RequestMapping(value = "postaddInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> postAddInfo(HttpServletRequest request, Model model) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("endStr", "error");
        if (request.getParameter("code") != null && !"".equals(request.getParameter("code").toString())) {
            String idsa = request.getParameter("ids1") != null && !"".equals(request.getParameter("ids1").toString()) ? request.getParameter("ids1").toString() : "0";
            String idsb = request.getParameter("ids2") != null && !"".equals(request.getParameter("ids2").toString()) ? request.getParameter("ids2").toString() : "0";
            String idsc = request.getParameter("ids3") != null && !"".equals(request.getParameter("ids3").toString()) ? request.getParameter("ids3").toString() : "0";
            StringBuffer sbIds = new StringBuffer(idsa);
            sbIds.append(",").append(idsb).append(",").append(idsc);
            retMap = standingBookService.updateStingBookCodeList(request.getParameter("code").toString(), sbIds.toString());
        }
        return retMap;
    }

    /**
     * 添加对账单主单信息
     */
    @RequiresPermissions("bis:checkbook:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String save(@Valid BisCheckingBook obj, Model model) {
        String endStr = "error";
        obj.setOperateTime(new Date());
        User user = UserUtil.getCurrentUser();
        if (user != null) {
            obj.setOperator(user.getName());
        }
        bisCheckingBookService.merge(obj);
        //更新明细信息
        if (obj.getIsTrue().equals("1")) {
            endStr = bisCheckingBookService.addStingBookCodeList(obj.getCodeNum());
        } else {
            endStr = "success";
        }
        return endStr;
    }

    /**
     * 添加对账单主单信息
     */
    @RequiresPermissions("bis:checkbook:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid BisCheckingBook obj, Model model) {
        String endStr = "error";
        if (obj.getCodeNum() != null && !"".equals(obj.getCodeNum())) {
            BisCheckingBook newObj = bisCheckingBookService.get(obj.getCodeNum());
            newObj.setCustom(obj.getCustom());
            newObj.setCustomID(obj.getCustomID());
            newObj.setIsTrue(obj.getIsTrue());
            newObj.setJsfs(obj.getJsfs());
            newObj.setYearMonth(obj.getYearMonth());
            newObj.setRemark(obj.getRemark());
            newObj.setUpdateTime(new Date());
            User user = UserUtil.getCurrentUser();
            if (user != null) {
                obj.setUpdateOperator(user.getName());
            }
            bisCheckingBookService.update(newObj);
            if ("1".equals(newObj.getIsTrue())) {
                endStr = bisCheckingBookService.addStingBookCodeList(obj.getCodeNum());
            } else {
                endStr = "success";
            }
        }
        return endStr;
    }

    //删除对账明细添加--直接删除
    @RequestMapping(value = "postdelInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> postdelInfo(HttpServletRequest request, Model model) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("endStr", "error");
        if (request.getParameter("code") != null && !"".equals(request.getParameter("code").toString())) {
            String idsa = request.getParameter("ids1") != null && !"".equals(request.getParameter("ids1").toString()) ? request.getParameter("ids1").toString() : "0";
            String idsb = request.getParameter("ids2") != null && !"".equals(request.getParameter("ids2").toString()) ? request.getParameter("ids2").toString() : "0";
            String idsc = request.getParameter("ids3") != null && !"".equals(request.getParameter("ids3").toString()) ? request.getParameter("ids3").toString() : "0";
            StringBuffer sbIds = new StringBuffer(idsa);
            sbIds.append(",").append(idsb).append(",").append(idsc);
            retMap = standingBookService.delStingBookCodeList(request.getParameter("code").toString(), sbIds.toString());
        }
        return retMap;
    }

    //应付对账明细分组查询
    @RequestMapping(value = "jsoncheckinfo", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getCheckingBookInfo(BisCheckingBook obj) {
        List<Map<String, Object>> getList = standingBookService.getCheckingBookInfoList(obj.getnType(), obj.getCodeNum());
        if (getList == null) {
            getList = new ArrayList<Map<String, Object>>();
        } else if ((obj.getnType() == 2 || obj.getnType() == 3) && getList.size() != 0) {

            int size = getList.size();

            double tmpMoney = 0;

            String oldBillNum = "";

            for (int i = 0; i < size; i++) {

                Map<String, Object> item = getList.get(i);
                Object objBillNum = item.get("BILL_NUM");

                if (objBillNum == null) {
                    if (tmpMoney > 0) {
                        Map<String, Object> newItem = new HashMap<>();
                        newItem.put("LINK_ID", oldBillNum + "的金额合计");
                        newItem.put("RMB", tmpMoney + "");

                        getList.add(i, newItem);

                        tmpMoney = 0;
                        size += 1;
                        i++;

                    }
                    continue;
                }

                String billNum = objBillNum.toString();

                if (!oldBillNum.equals(billNum) && i > 0 && tmpMoney > 0) {

                    Map<String, Object> newItem = new HashMap<>();
                    newItem.put("LINK_ID", oldBillNum + "的金额合计");
                    newItem.put("RMB", tmpMoney + "");

                    getList.add(i, newItem);

                    size += 1;
                    i++;
                    tmpMoney = 0;
                }

                double money = Double.valueOf(item.get("RMB").toString());

                tmpMoney += money;

                oldBillNum = billNum;

            }

            if (tmpMoney != 0) {
                Map<String, Object> newItem = new HashMap<>();
                newItem.put("LINK_ID", oldBillNum + "的金额合计");
                newItem.put("RMB", tmpMoney + "");

                getList.add(newItem);
            }
        }

        return getList;
    }

    //应付对账明细分组查询
    @RequestMapping(value = "jsongroup", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getGroupStandingBook(BisStandingBook obj) {

        List<Map<String, Object>> getList = new ArrayList<>();

        if (obj.getIsCX() != null && obj.getIsCX() == 1) {
            getList = standingBookService.getStandingBookGroupList(
                    obj.getsDZId(),
                    obj.getnType(),
                    obj.getsLinkId(),
                    obj.getsIBillNum(),
                    obj.getsOBillNum(),
                    obj.getsBaoGuan(),
                    obj.getsCustom(),
                    obj.getsFeePlan(),
                    obj.getStraTime(),
                    obj.getEndTime()
            );
        }

        return getList;
    }

    //台账查询
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<BisStandingBook> page = getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = standingBookService.search(page, filters);
        return getEasyUIData(page);
    }
    
    @RequestMapping(value = "saveCost", method = RequestMethod.POST)
    @ResponseBody
    public String saveCost(HttpServletRequest request) throws Exception{
    	String linkId=request.getParameter("linkId");
    	String feeId=request.getParameter("feeId");
    	BisEnterStock obj = enterStockService.get(linkId);//入库联系单id
    	List<String> feeIds=new ArrayList<String>();
	    feeIds.add(feeId);
	    //审核后在调用系统费用
	    if(null==obj.getAuditingState()||2!=obj.getAuditingState()){
	    	return "费用未进行审核不能进行费用引入操作！";
	    }
	    if(null!=obj.getFinishFeeState()&&"1".equals(obj.getFinishFeeState())){
    		return "费用已经完成请取消完成后在进行后续操作！";
    	}
	    return standingBookService.addInStandingScheme(linkId,feeIds);
    }
    /**
     * @param model
     * @param linkId  入库联系单号
     * @param itemNum 提单号
     * @return
     * @throws
     * @author Connor.M
     * @Description: 入库费用调整客服页面
     * @date 2019年1月15日 上午11:15
     */
    @RequestMapping(value = "inKfList/{linkId}/{itemNum}/{stockId}/{feeId}", method = RequestMethod.GET)
    public String inKfList(Model model, @PathVariable("linkId") String linkId, @PathVariable("itemNum") String itemNum, @PathVariable("stockId") String stockId,@PathVariable("feeId") String feeId) {
        User user = UserUtil.getCurrentUser();
        BisEnterStock obj = enterStockService.get(linkId);//入库联系单id
        model.addAttribute("stockOrgId", obj.getStockOrgId()); //结算单位ID
        model.addAttribute("linkId", linkId);
        itemNum = itemNum.replace("(╯' - ')╯︵ ┻━┻", "/");
        model.addAttribute("itemNum", itemNum);//提单号
        model.addAttribute("feeId", feeId);//费用方案
        model.addAttribute("stockId", stockId);//存货放id
        model.addAttribute("planFeeState", enterStockService.get(linkId).getPlanFeeState());
        model.addAttribute("finishFeeState", enterStockService.get(linkId).getFinishFeeState());
        model.addAttribute("userId", user.getId());
        return "cost/standingBook/inStockAdjustList";
    }

    @RequestMapping(value = "saveCwCost", method = RequestMethod.POST)
    @ResponseBody
    public String saveCwCost(HttpServletRequest request) throws Exception{
    	String linkId=request.getParameter("linkId");
    	String feeId=request.getParameter("feeId");
    	BisEnterStock obj = enterStockService.get(linkId);//入库联系单id
    	List<String> feeIds=new ArrayList<String>();
        feeIds.add(feeId);
    	if(null!=obj.getRkTime()&&!"".equals(obj.getRkTime())&&!"1".equals(obj.getFinishFeeState())){
    		standingBookService.addInStandingSchemeBatch(linkId,feeIds);
        }
		return "success";
    }
    /**
     * @param model
     * @param linkId  入库联系单号
     * @param itemNum 提单号
     * @return
     * @throws
     * @author Connor.M
     * @Description: 财务入库费用调整 入场页面
     * @date 2016年3月31日 下午4:03:48
     */
    @RequestMapping(value = "inList/{linkId}/{itemNum}/{stockId}/{feeId}", method = RequestMethod.GET)
    public String inList(Model model, @PathVariable("linkId") String linkId, @PathVariable("itemNum") String itemNum, @PathVariable("stockId") String stockId,@PathVariable("feeId") String feeId) {
        User user = UserUtil.getCurrentUser();
        BisEnterStock obj = enterStockService.get(linkId);//入库联系单id
        model.addAttribute("stockOrgId", obj.getStockOrgId()); //结算单位ID
        model.addAttribute("linkId", linkId);
        itemNum = itemNum.replace("(╯' - ')╯︵ ┻━┻", "/");
        model.addAttribute("itemNum", itemNum);//提单号
        model.addAttribute("feeId", feeId);//费用方案
        model.addAttribute("stockId", stockId);//存货放id
        model.addAttribute("planFeeState", enterStockService.get(linkId).getPlanFeeState());
        model.addAttribute("finishFeeState", enterStockService.get(linkId).getFinishFeeState());
        model.addAttribute("userId", user.getId());
        return "cost/standingBook/inStockAdjustList";
    }

    /**
     * @param model
     * @param linkId
     * @param stockId
     * @return
     * @throws
     * @author Connor.M
     * @Description: 出库财务费用调整  入场页面
     * @date 2016年4月14日 下午1:44:42
     */
    @RequestMapping(value = "outList/{linkId}/{stockId}", method = RequestMethod.GET)
    public String outList(Model model, @PathVariable("linkId") String linkId, @PathVariable("stockId") String stockId) {
        User user = UserUtil.getCurrentUser();
        BisOutStock outStock = outStockService.get(linkId);
        if ("1".equals(outStock.getIfClearStore())) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("outLinkId", linkId);
            params.put("lastCar", 1);//最后一车
            List<BisLoadingOrder> orderList = loadingOrderService.findList(params);
            if (!orderList.isEmpty()) {
                if ("2".equals(orderList.get(0).getOrderState())) {
                    model.addAttribute("clear", "1");
                } else {
                    model.addAttribute("clear", "0");
                }
            } else {
                model.addAttribute("clear", "0");
            }
        } else {
            model.addAttribute("clear", "0");
        }
        model.addAttribute("settleOrgId", outStock.getSettleOrgId());
        model.addAttribute("linkId", linkId);
        model.addAttribute("stockId", stockId);
        model.addAttribute("planFeeState", outStockService.get(linkId).getPlanFeeState());
        model.addAttribute("finishFeeState", outStockService.get(linkId).getFinishFeeState());
        model.addAttribute("userId", user.getId());
        return "cost/standingBook/outStockAdjustList";
    }
    
    /**
     * @param model
     * @param linkId
     * @param stockId
     * @return
     * @throws
     * @author Connor.M
     * @Description: 出库客服费用调整  入场页面
     * @date 2016年4月14日 下午1:44:42
     */
    @RequestMapping(value = "outkfList/{linkId}/{stockId}", method = RequestMethod.GET)
    public String outkfList(Model model, @PathVariable("linkId") String linkId, @PathVariable("stockId") String stockId) {
        User user = UserUtil.getCurrentUser();
        BisOutStock outStock = outStockService.get(linkId);
        if ("1".equals(outStock.getIfClearStore())) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("outLinkId", linkId);
            params.put("lastCar", 1);//最后一车
            List<BisLoadingOrder> orderList = loadingOrderService.findList(params);
            if (!orderList.isEmpty()) {
                if ("2".equals(orderList.get(0).getOrderState())) {
                    model.addAttribute("clear", "1");
                } else {
                    model.addAttribute("clear", "0");
                }
            } else {
                model.addAttribute("clear", "0");
            }
        } else {
            model.addAttribute("clear", "0");
        }
        model.addAttribute("settleOrgId", outStock.getSettleOrgId());
        model.addAttribute("linkId", linkId);
        model.addAttribute("stockId", stockId);
        model.addAttribute("planFeeState", outStockService.get(linkId).getPlanFeeState());
        model.addAttribute("finishFeeState", outStockService.get(linkId).getFinishFeeState());
        model.addAttribute("userId", user.getId());
        if (null!=outStock.getIfCodeCopy()&&"1".equals(outStock.getIfCodeCopy())) {
        	//计算抄码费用
        	try {
				standingBookService.addOutStandingScheme(linkId);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return "cost/standingBook/outStockAdjustList";
    }

    /**
     * @param model
     * @param linkId
     * @param stockId
     * @return
     * @throws
     * @author Connor.M
     * @Description: 货转费用调整
     * @date 2016年5月6日 下午2:08:45
     */
    @RequestMapping(value = "transferList/{linkId}/{stockId}", method = RequestMethod.GET)
    public String transferList(Model model, @PathVariable("linkId") String linkId, @PathVariable("stockId") String stockId) {
        User user = UserUtil.getCurrentUser();

        model.addAttribute("linkId", linkId);
        model.addAttribute("stockId", stockId);
        model.addAttribute("userId", user.getId());
        return "cost/standingBook/transferStockAdjustList";
    }

    /**
     * @param request
     * @return
     * @throws
     * @author Connor.M
     * @Description: 入库费用调整  分页查询
     * @date 2016年3月31日 下午3:26:22
     */
    @RequestMapping(value = "getInData", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getInData(HttpServletRequest request) {
        Page<BisStandingBook> page = getPage(request);
        page.orderBy("standingNum").setOrder(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = standingBookService.search(page, filters);
        if(!page.getResult().isEmpty()){
            List<BisStandingBook> returnObjList = new ArrayList<>();
        	List<BisStandingBook> objList=page.getResult();
        	int size = objList.size();
        	String payNum="";
        	Integer payId=0;
            String feeCode="";
            String asn="";
            for(int i=0;i<size;i++){
                feeCode=objList.get(i).getFeeCode();
                if(null!=feeCode && feeCode.trim().length() > 0 && "crk".equals(feeCode)){
                    asn=objList.get(i).getAsn();
                    if(null!=asn && asn.trim().length() > 0){
                        BisAsn bisAsn=asnService.get(objList.get(i).getAsn());
                        if(null!=bisAsn && bisAsn.getAsnState()!=null && Integer.parseInt(bisAsn.getAsnState()) >= 3){
                            //符合条件，继续执行
                        }else{
                            //跳过
                            continue;
                        }
                    }
                }
                payId=objList.get(i).getPayId();
                if(null!=payId && payId!=0){
                    BisPayInfo bisPayInfo=bisPayInfoService.get(objList.get(i).getPayId());
                    if(null==bisPayInfo)
                        continue;
                    payNum=bisPayInfoService.get(objList.get(i).getPayId()).getPayId();
                    objList.get(i).setPayNum(payNum);
                }
                returnObjList.add(objList.get(i));
        	}
            page.setResult(returnObjList);
        }
        return getEasyUIData(page);
    }

    /**
     * @param linkId
     * @param ids
     * @return
     * @throws
     * @author Connor.M
     * @Description: 入库  费目调整 添加方案  费目
     * @date 2016年4月5日 上午9:54:04
     */
    @RequestMapping(value = "{linkId}/addInStandingSchemeBatch")
    @ResponseBody
    public String addInStandingSchemeBatch(@PathVariable("linkId") String linkId, @RequestBody List<String> ids) throws Exception {
    	return standingBookService.addInStandingSchemeBatch(linkId, ids);
    }

    /**
     * @param linkId
     * @param ids
     * @return
     * @throws
     * @author Connor.M
     * @Description: 出库 费目调整 添加方案  费目
     * @date 2016年4月14日 下午2:04:22
     */
    @RequestMapping(value = "{linkId}/addOutStandingSchemeBatch")
    @ResponseBody
    public String addOutStandingSchemeBatch(@PathVariable("linkId") String linkId, @RequestBody List<String> ids) throws Exception {
        return standingBookService.addOutStandingSchemeBatch(linkId, ids);
    }

    /**
     * @param ids
     * @return
     * @throws
     * @author Connor.M
     * @Description: 删除 台账的费目
     * @date 2016年4月5日 下午5:24:11
     */
    @RequestMapping(value = "deleteStandingBook/{standingNum}")
    @ResponseBody
    public String deleteStanding(@PathVariable("standingNum") String standingNum) {
        standingBookService.delete(Integer.parseInt(standingNum));
        return "success";
    }

    /**
     * @param ids
     * @return
     * @throws
     * @author Connor.M
     * @Description: 批量删除  台账的费目
     * @date 2016年4月6日 上午11:38:02
     */
    @RequestMapping(value = "deleteStandingBookBatch")
    @ResponseBody
    public String deleteStandingBookBatch(@RequestBody List<String> ids) {
        return standingBookService.deleteStandingBookBatch(ids);
    }

    /**
     * @param ids
     * @return
     * @throws
     * @author Connor.M
     * @Description: 批量 审核 通过
     * @date 2016年4月6日 下午1:51:11
     */
    @RequestMapping(value = "isOkStandingBookBatch")
    @ResponseBody
    public String isOkStandingBookBatch(@RequestBody List<String> ids) {
        standingBookService.isOkStandingBookBatch(ids);
        return "success";
    }

    /**
     * @param ids
     * @return
     * @throws
     * @author PYL
     * @Description: 批量 取消审核
     * @date 2016年5月18日
     */
    @RequestMapping(value = "isNotOkStandingBookBatch")
    @ResponseBody
    public String isNotOkStandingBookBatch(@RequestBody List<String> ids) {
        standingBookService.isNotOkStandingBookBatch(ids);
        return "success";
    }

    /**
     * @param standingNum 主键ID
     * @param asn         asn
     * @param num         数量
     * @param feeCode     费目编号
     * @param currency    币种
     * @param billDate    账单日期
     * @param remark      备注
     * @return
     * @throws ParseException 
     * @throws
     * @author Connor.M
     * @Description: 编辑费目明细
     * @date 2016年4月7日 下午7:26:28
     */
    @RequestMapping(value = "{standingNum}/{linkId}/editStandingBookByFee")
    @ResponseBody
    public String editStandingBookByFee(@PathVariable("standingNum") String standingNum, @PathVariable("linkId") String linkId,
                                        @RequestParam String crkSign, @RequestParam String type, @RequestParam String stockId, @RequestParam String billNum,
                                        @RequestParam String asn, @RequestParam String num, @RequestParam String price, @RequestParam String taxRate, @RequestParam String feeCode,
                                        @RequestParam String currency, @RequestParam String billDate, @RequestParam String remark) throws ParseException {

        BisStandingBook standingBook = new BisStandingBook();
        if (standingNum.equals("undefined")) {
            Date now = new Date();//当前时间
            User user = UserUtil.getCurrentUser();
            BaseClientInfo clientInfo = clientService.get(Integer.parseInt(stockId));
            standingBook.setCustomsNum(stockId);
            standingBook.setCustomsName(clientInfo.getClientName());
            standingBook.setLinkId(linkId);
            standingBook.setCrkSign(Integer.parseInt(crkSign));
            if (crkSign.equals("1")) {
                BisEnterStock enterStock = enterStockService.get(linkId);
                standingBook.setBillNum(enterStock.getItemNum());
                if(enterStock.getRkTime()!=null){
                	standingBook.setStorageDtae(enterStockService.getFirstTime(enterStock.getRkTime()) );
                }
                standingBook.setBillDate(enterStock.getRkTime() == null ? now : enterStockService.getFirstTime(enterStock.getRkTime()));
            } else if (crkSign.equals("2")) {
                BisLoadingOrderInfo loadingOrderInfo = loadingOrderInfoService.getOrderInfoByLinkIdForMaxDate(linkId);
                standingBook.setBillNum(billNum);
                standingBook.setBillDate(loadingOrderInfo.getLoadingTiem() == null ? now : loadingOrderInfo.getLoadingTiem());
            }
            standingBook.setIfReceive(Integer.parseInt(type));
            standingBook.setFillSign(0);
            standingBook.setInputPersonId(user.getId().toString());
            standingBook.setInputPerson(user.getName());
            standingBook.setInputDate(now);
            standingBook.setChargeDate(now);
            standingBook.setCostDate(now);
            standingBook.setExamineSign(0);
            standingBook.setShareSign(0);
            standingBook.setReconcileSign(0);
            standingBook.setSettleSign(0);
            standingBook.setSplitSign(0);
            standingBook.setContactType(1);
            standingBook.setPaySign("0");
            standingBook.setChargeSign("0");
        } else {
            standingBook = standingBookService.get(Integer.parseInt(standingNum));
            BaseClientInfo clientInfo = clientService.get(Integer.parseInt(stockId));
            //判断客户是否一样 不同则修改客户
            if (!standingBook.getCustomsNum().equals(stockId)) {
                standingBook.setCustomsNum(stockId);
                standingBook.setCustomsName(clientInfo.getClientName());
            }
            if (crkSign.equals("2")) {
                standingBook.setBillNum(billNum);
            }
        }
        standingBook.setAsn(asn);
        standingBook.setNum(Double.parseDouble(StringUtils.isNull(num) ? "0.0" : num));
        standingBook.setPrice(Double.parseDouble(StringUtils.isNull(price) ? "1" : price));
        standingBook.setTaxRate(Double.parseDouble(StringUtils.isNull(taxRate) ? "0.0" : taxRate));
        standingBook.setBillDate(DateUtils.parseDate(StringUtils.isNull(billDate) ? DateUtils.getTimeMonth() : billDate));
        standingBook.setRemark(remark);
        if (null == standingBook.getStandingNum() || "".equals(standingBook.getStandingNum())) {
            standingBook.setStandingNum(standingBookService.getSequenceId());
            standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()), new Date()));
        }
        standingBookService.editStandingBookByFee(standingBook, feeCode, currency);
        return "success";
    }

    /**
     * @param linkId 联系单Id
     * @return
     * @throws
     * @author Connor.M
     * @Description: 根据联系单Id统计  应收应付费用
     * @date 2016年4月9日 上午11:41:14
     */
    @RequestMapping(value = "{linkId}/sumStandingBookByLinkId")
    @ResponseBody
    public Map<Object, String> sumStandingBookByLinkId(@PathVariable("linkId") String linkId) {
        return standingBookService.sumStandingBookByLinkId(linkId);
    }

    /**
     * @param standingNum
     * @param stockId
     * @return
     * @throws
     * @author Connor.M
     * @Description: 根据台账Id  垫付操作
     * @date 2016年4月12日 下午4:38:41
     */
    @RequestMapping(value = "{standingNum}/updatePaySignByStandingNum")
    @ResponseBody
    public String updatePaySignByStandingNum(@PathVariable("standingNum") String standingNum, @RequestParam String stockId) {
        BisStandingBook standingBook = standingBookService.get(Integer.parseInt(standingNum));
        return standingBookService.updatePaySignByStandingNum(standingBook, stockId);
    }

    /**
     * @param standingNum
     * @param stockId
     * @return
     * @throws
     * @author Connor.M
     * @Description: 取消  垫付操作
     * @date 2016年5月6日 下午2:50:36
     */
    @RequestMapping(value = "{standingNum}/deletePaySignByStandingNum")
    @ResponseBody
    public String deletePaySignByStandingNum(@PathVariable("standingNum") String standingNum, @RequestParam String stockId) {
        return standingBookService.deletePaySignByStandingNum(Integer.parseInt(standingNum));
    }

    /**
     * @param linkId
     * @param editDate
     * @return
     * @throws
     * @author Connor.M
     * @Description: 统一修改结账日期
     * @date 2016年4月13日 上午10:10:08
     */
    @RequestMapping(value = "{linkId}/updateDateByLinkId")
    @ResponseBody
    public String updateDateByLinkId(@PathVariable("linkId") String linkId, @RequestParam String editDate) throws Exception {
        standingBookService.updateDateByLinkId(linkId, editDate);
        return "success";
    }

    /*
     * 清库操作
     * */
    @RequestMapping(value = "clearance/{linkId}", method = RequestMethod.GET)
    @ResponseBody
    public String clearance(@PathVariable("linkId") String outLinkId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("outLinkId", outLinkId);
        params.put("ifHasCleared", 0);
        params.put("lastCar", 1);
        List<BisLoadingOrder> orderList = loadingOrderService.findList(params);
        if (!orderList.isEmpty()) {
            BisLoadingOrder order = orderList.get(0);
            //查找选择了【是最后一车】的出库订单，按照出库订单的明细，按照group by asn、sku
            List<BisLoadingInfo> objList = loadingOrderService.getObjList(outLinkId);
            BisOutStock outStock = outStockService.get(outLinkId);
            String clientId = outStock.getStockInId();
            BaseClientInfo clientInfo = clientService.get(Integer.valueOf(clientId));
            //获取原来的ASN区间表
            for (BisLoadingInfo loading : objList) {
                List<PropertyFilter> actionPfs = new ArrayList<PropertyFilter>();
                actionPfs.add(new PropertyFilter("EQS_status", "1"));//状态，正常
                if (loading.getAsnId() == null || "".equals(loading.getAsnId())) {
                    actionPfs.add(new PropertyFilter("NULLS_asn", "")); //ASN
                } else {
                    actionPfs.add(new PropertyFilter("EQS_asn", loading.getAsnId()));//ASN
                }
                actionPfs.add(new PropertyFilter("EQS_sku", loading.getSkuId()));//sku
                actionPfs.add(new PropertyFilter("EQS_clientId", clientId));//客户
                actionPfs.add(new PropertyFilter("NULLS_chargeEndDate", ""));//时间  为 null
                actionPfs.add(new PropertyFilter("NULLS_outId", ""));
                List<AsnAction> asnActions = asnActionService.getAsnAction(actionPfs);
                if (null != asnActions && asnActions.size() > 0) {
                    //获得 asnAction 区间表 对象
                    AsnAction asnAction = asnActions.get(0);
                    //查看 货转单 是否存在
                    if (asnAction.getLinkTransferId() != null) {
                        //查询 是否存在  货转单
                        List<PropertyFilter> pf = new ArrayList<PropertyFilter>();
                        if (loading.getAsnId() == null || "".equals(loading.getAsnId())) {
                            pf.add(new PropertyFilter("NULLS_asn", "")); //ASN
                        } else {
                            pf.add(new PropertyFilter("EQS_asn", loading.getAsnId()));
                        }
                        pf.add(new PropertyFilter("EQS_sku", loading.getSkuId()));//sku
                        pf.add(new PropertyFilter("EQS_linkTransferId", asnAction.getLinkTransferId()));
                        pf.add(new PropertyFilter("NULLS_outId", ""));
						pf.add(new PropertyFilter("NULLS_outLinkId", ""));
                        pf.add(new PropertyFilter("EQS_status", "1"));
                        List<AsnAction> actions = asnActionService.getAsnAction(pf);
                        if (null != actions && actions.size() > 0) {
                            for (AsnAction action : actions) {
                                //修改 主 AsnAction的数据
                                int num = action.getNum() - loading.getPiece();
                                action.setNum(num);
                                action.setNetWeight(BigDecimalUtil.sub(action.getNetWeight(), loading.getNetWeight()));
                                action.setGrossWeight(BigDecimalUtil.sub(action.getGrossWeight(), loading.getGrossWeight()));
                                asnActionService.save(action);
//                                List<Map<String,Object>> trayInfoList=trayInfoService.updateAsnAction(loading);
//								if(!trayInfoList.isEmpty()){
//									Integer pieceN = ((BigDecimal)trayInfoList.get(0).get("PIECE")).intValue();
//									action.setNum(pieceN);
//									Double netN = ((BigDecimal)trayInfoList.get(0).get("NET")).doubleValue();
//									action.setNetWeight(netN);
//									Double grossN = ((BigDecimal)trayInfoList.get(0).get("GROSS")).doubleValue();
//									action.setGrossWeight(grossN);
//									asnActionService.update(action);
//								}else{
//									System.out.print("ASN区间表未找到库存！！"+action.getId().toString());
//									action.setNum(0);
//									action.setNetWeight(0D);
//									action.setGrossWeight(0D);
//									asnActionService.update(action);
//								}
                                asnActionLogService.saveLog(asnAction, "5", asnAction.getNum() + loading.getPiece(), 0 - loading.getPiece(), "点击清库时，原ASN区间表记录");
                                //复制 生成  子  AsnAction的数据
                                AsnAction action2 = new AsnAction();
                                BeanUtils.copyProperties(action, action2);//复制
                                action2.setId(null);//主键
                                //判断 计费结束日期
                                if (action2.getChargeEndDate() == null || order.getPlanTime().getTime() < action2.getChargeEndDate().getTime()) {
                                    action2.setChargeEndDate(order.getPlanTime());//出库时间
                                }
                                action2.setNum(loading.getPiece());
                                action2.setNetWeight(loading.getNetWeight());
                                action2.setGrossWeight(loading.getGrossWeight());
                                action2.setTransferId(null);//货转单
                                action2.setLinkTransferId(null);
                                action2.setOutLinkId(order.getOrderNum());//出库订单

                                //只有存货方客户的区间记录才更新出库单号、出库订单、结算客户，其他客户的货转记录保持单号是原来的状态，即原来的数据记录在入库联系单上，不记录在出库联系单上，出库联系单只记录当前收货方的存储费
                                if (action2.getClientId().equals(outStock.getStockInId())) {
                                    action2.setOutId(outLinkId);//出库联系单
                                    action2.setJfClientId(outStock.getSettleOrgId()); //结算单位ID
                                    action2.setClientDay(clientInfo.getCheckDay());//结算日
                                }
                                //action2的清库标记改为清库（费用调整的修改）
                                action2.setCleanSign("1");
                                asnActionService.save(action2);
                                asnActionLogService.saveLog(action2, "5", 0, loading.getPiece(), "点击清库时，新生成的ASN区间表记录");
                            }
                        }
                    } else {
                        //修改 主 AsnAction的数据
                        int num = asnAction.getNum() - loading.getPiece();
                        asnAction.setNum(num);
                        asnAction.setNetWeight(BigDecimalUtil.sub(asnAction.getNetWeight(), loading.getNetWeight()));
                        asnAction.setGrossWeight(BigDecimalUtil.sub(asnAction.getGrossWeight(), loading.getGrossWeight()));
                        asnActionService.save(asnAction);
//                      List<Map<String,Object>> trayInfoList=trayInfoService.updateAsnAction(loading);
//						if(!trayInfoList.isEmpty()){
//							Integer pieceN = ((BigDecimal)trayInfoList.get(0).get("PIECE")).intValue();
//							asnAction.setNum(pieceN);
//							Double netN = ((BigDecimal)trayInfoList.get(0).get("NET")).doubleValue();
//							asnAction.setNetWeight(netN);
//							Double grossN = ((BigDecimal)trayInfoList.get(0).get("GROSS")).doubleValue();
//							asnAction.setGrossWeight(grossN);
//							asnActionService.update(asnAction);
//						}else{
//							System.out.print("ASN区间表未找到库存！！"+asnAction.getId().toString());
//							asnAction.setNum(0);
//							asnAction.setNetWeight(0D);
//							asnAction.setGrossWeight(0D);
//							asnActionService.update(asnAction);
//						}
                        asnActionLogService.saveLog(asnAction, "5", asnAction.getNum() + loading.getPiece(), 0 - loading.getPiece(), "点击清库时，原ASN区间表记录");
                        //复制生成  子  AsnAction的数据
                        AsnAction asnAction2 = new AsnAction();
                        BeanUtils.copyProperties(asnAction, asnAction2);//复制

                        asnAction2.setId(null);//主键
                        //判断 计费结束日期
                        if (asnAction2.getChargeEndDate() == null || order.getPlanTime().getTime() < asnAction2.getChargeEndDate().getTime()) {
                            asnAction2.setChargeEndDate(order.getPlanTime());//出库时间
                        }
                        asnAction2.setNum(loading.getPiece());
                        asnAction2.setNetWeight(loading.getNetWeight());
                        asnAction2.setGrossWeight(loading.getGrossWeight());
                        asnAction2.setTransferId(null);//货转单
                        asnAction2.setLinkTransferId(null);
                        asnAction2.setOutLinkId(order.getOrderNum());//出库订单

                        //只有存货方客户的区间记录才更新出库单号、出库订单、结算客户，其他客户的货转记录保持单号是原来的状态，即原来的数据记录在入库联系单上，不记录在出库联系单上，出库联系单只记录当前收货方的存储费
                        if (asnAction2.getClientId().equals(outStock.getStockInId())) {
                            asnAction2.setOutId(outLinkId);//出库联系单
                            asnAction2.setJfClientId(outStock.getSettleOrgId()); //结算单位ID
                            asnAction2.setClientDay(clientInfo.getCheckDay());//结算日
                        }
                        asnAction2.setCleanSign("1");
                        asnActionService.save(asnAction2);
                        asnActionLogService.saveLog(asnAction2, "5", 0, loading.getPiece(), "点击清库时，新生成的ASN区间表记录");
                    }
                }
            }
            order.setIfHasCleared(1);
            loadingOrderService.update(order);
            String cunchu = this.cunchu(outLinkId);
            if (cunchu.equals("1")) {
                return "success";
            } else {
                return "nocc";
            }
        } else {
            return "false";
        }
    }

    //存储过程
	public String cunchu(String outLinkId) {
		String driver = PropertiesUtil.getPropertiesByName("jdbc.driver", "application");
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		String testPrint = "0";
		try {
			Class.forName(driver);
			conn = standingBookService.getConnect();
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call pro_InStandingBookByOutId(?,?,?) }");
			proc.setString(1, outLinkId);
			proc.registerOutParameter(2, Types.INTEGER);
			proc.registerOutParameter(3, Types.VARCHAR);
			proc.execute();
			testPrint = proc.getString(2);
		} catch (SQLException ex2) {
			ex2.printStackTrace();
		} catch (Exception ex2) {
			ex2.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				}
			} catch (SQLException ex1) {
			}
		}
		return testPrint;
	}


    //获取总金额
    @RequestMapping(value = "gettotal", method = RequestMethod.POST)
    @ResponseBody
    public String gettotal(HttpServletRequest request, String codeNum) {
        List<Map<String, Object>> objList = bisCheckingBookService.getTotal(codeNum);
        if (!objList.isEmpty()) {
            String total = objList.get(0).get("RMB").toString();
            return total;
        } else {
            return "0";
        }
    }

    //获取提单号
    @RequestMapping(value = "connectBillNum", method = RequestMethod.POST)
    @ResponseBody
    public String connectBillNum(HttpServletRequest request, String linkId) {
        List<Map<String, Object>> objList = bisCheckingBookService.connectBillNum(linkId);
        String result = "";
        if (!objList.isEmpty()) {
            result = (String) objList.get(0).get("BILLNUM");
        }
        return result;
    }
    /**
     * 导出excel月费目汇总表
     * @param stock
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportExcel")
    @ResponseBody
    public void exportExcel(@Valid @ModelAttribute @RequestBody BisStandingBook stock, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams("exceltemplate/summonth.xlsx");
        String excelName = "冷库各费目汇总表.xlsx";
        //数据查询
        List<Map<String, Object>> listMap=standingBookService.findRepot(stock);
        if (null != listMap && listMap.size() > 0) {
        	//查出汇总的map
        	List<Map<String, Object>> summap=standingBookService.sum(stock);
        	if(null!=summap&&summap.size()>0){
        		listMap.add(summap.get(0));
        	}
            map.put("maplist", listMap);
        }
        //汇总金额查询
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.getSheetAt(0).setForceFormulaRecalculation(true);//强制执行公式
        String formatFileName = new String(excelName.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }
    
    /**
     * 冷库费用明细的导出工作
     * @param stock
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportCustomerExcel")
    @ResponseBody
    public void exportCustomerExcel(@Valid @ModelAttribute @RequestBody BisStandingBook stock, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams("exceltemplate/customerExcel.xlsx");
        String excelName = "冷库各费目明细表.xlsx";
        //数据查询
        List<Map<String, Object>> listMap=standingBookService.findCustomerRepot(stock);
        if (null != listMap && listMap.size() > 0) {
        	//查出汇总的map
        	List<Map<String, Object>> summap=standingBookService.customersum(stock);
        	if(null!=summap&&summap.size()>0){
        		listMap.add(summap.get(0));
        	}
            map.put("maplist", listMap);
        }
        //汇总金额查询
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.getSheetAt(0).setForceFormulaRecalculation(true);//强制执行公式
        String formatFileName = new String(excelName.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }
    
    /**
     * 冷链月代理费用对账单导出
     * @param stock
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportSupplierExcel")
    @ResponseBody
    public void exportSupplierExcel(@Valid @ModelAttribute @RequestBody BisStandingBook stock, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams("exceltemplate/supplierExcel.xlsx");
        String excelName = "月代理费用对账单.xlsx";
        //数据查询
        List<Map<String, Object>> listMap=standingBookService.findSupplierRepot(stock);
        if (null != listMap && listMap.size() > 0) {
        	//查出汇总的map
        	List<Map<String, Object>> summap=standingBookService.suppliersum(stock);
        	if(null!=summap&&summap.size()>0){
        		listMap.add(summap.get(0));
        	}
            map.put("maplist", listMap);
        }
        //汇总金额查询
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.getSheetAt(0).setForceFormulaRecalculation(true);//强制执行公式
        String formatFileName = new String(excelName.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }
}