package com.haiersoft.ccli.wms.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.InspectStock;
import com.haiersoft.ccli.wms.entity.InspectStockMasterline;
import com.haiersoft.ccli.wms.service.InOutService;
import com.haiersoft.ccli.wms.service.InspectStockMasterLineService;
import com.haiersoft.ccli.wms.service.InspectStockService;
import com.haiersoft.ccli.wms.service.TrayInfoService;

/**
 * 取样模块
 */
@Controller
@RequestMapping("wms/inspect")
public class InspectStockController extends BaseController {

    private static final int UNAUDIT = 0;

    private static final int AUDIT = 1;

    @Autowired
    private InspectStockService inspectStockService;

    @Autowired
    private InOutService inOutService;
    
    @Autowired
    private TrayInfoService trayInfoService;

    //@Autowired
    //private StockReportService stockReportService;
    
    @Autowired
    private InspectStockMasterLineService inspectStockMasterLineService;
    
    @RequestMapping("index")
    public String index() {
        return "wms/inspect/index";
    }

    @RequestMapping("plans/add")
    public String addPlan() {
        return "wms/inspect/addPlans";
    }

    @RequestMapping("plans/update")
    public String updatePlan(Model model, String inspectId) {

        model.addAttribute("inspectId", inspectId);

        return "wms/inspect/updatePlans";
    }
    
    @RequestMapping("plans/havealook")
    public String lookPlan(Model model, String inspectId) {

        model.addAttribute("inspectId", inspectId);

        return "wms/inspect/lookPlan";
    }

    @RequestMapping(value = "plans/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> findInspectPlans(HttpServletRequest request) {

        Page<InspectStockMasterline> page = getPage(request);
        page.orderBy("operateDate").order(Page.DESC);//page.orderBy("operateTime").order(Page.DESC);
        InspectStockMasterline inspectStockMasterline = new InspectStockMasterline();
        parameterReflect.reflectParameter(inspectStockMasterline, request);

        //page = inspectStockService.findInspectPlans(page, inspectStockMasterline);
        page = inspectStockMasterLineService.findInspectPlans(page, inspectStockMasterline);

        return getEasyUIData(page);
    }
    
    /**
     * 获取取样明细列表
     */
    @RequestMapping(value = "plans/getInspectInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> findInspectPlansInfo(HttpServletRequest request) {
    	String inspectMasterId = request.getParameter("inspectMasterId");
        Page<InspectStock> page = getPage(request);
        
        InspectStock inspectStock = new InspectStock();
        inspectStock.setInspectMasterId(inspectMasterId);
        parameterReflect.reflectParameter(inspectStock, request);
        
        page = inspectStockService.findInspectPlansInfo(page, inspectStock);
        //刷新明细时候调用的方法，刷新明细时对结果集进行一次遍历，更改总表的审核状态和件数。
        List<InspectStock> list = page.getResult();
        if(list.size()>0){
        	Integer count = 0;
            
            Integer checkflag = 1;
            for(InspectStock is : list){
            	count += is.getInspectTotal();
            	if(is.getOperateState() == 0){
            		checkflag = 0;
            	}
            }
            InspectStockMasterline inspectStockMasterLine = inspectStockMasterLineService.get(inspectMasterId);
            inspectStockMasterLine.setInspectTotal(count);
            inspectStockMasterLine.setOperateState(checkflag);
            inspectStockMasterLineService.merge(inspectStockMasterLine);
        }
        
        
        
        
        return getEasyUIData(page);
    }
    
    
    /**
     * 获取取样单号
     */
    @RequestMapping(value = "plans/getNewInspectId", method = RequestMethod.POST)
    @ResponseBody
    public String getNewInspectId() {
        String inspectId = generateID();//取样单号
        //System.out.println(inspectId);
        return inspectId;
    }
    
    
    /**
     * 添加取样计划
     */
    @RequestMapping(value = "plans/add", method = RequestMethod.POST)
    @ResponseBody
    public String addInspectPlans(HttpServletRequest request) {

        User user = UserUtil.getCurrentUser();
        Date now = new Date();
        String inspectid = request.getParameter("inspectId");
        
        InspectStockMasterline inspectStockMasterLine = new InspectStockMasterline();
        parameterReflect.reflectParameter(inspectStockMasterLine, request);
        
        
        inspectStockMasterLine.setInspectId(inspectid);
        inspectStockMasterLine.setOperateState(UNAUDIT);
        inspectStockMasterLine.setOperateUserId(user.getId() + "");
        inspectStockMasterLine.setOperateUserName(user.getName());
        inspectStockMasterLine.setOperateDate(now);
        inspectStockMasterLine.setCreateUserId(user.getId() + "");
        inspectStockMasterLine.setCreateUserName(user.getName());
        inspectStockMasterLine.setCreateDate(now);
        inspectStockMasterLine.setState(1);
       
        inspectStockMasterLineService.merge(inspectStockMasterLine);

        return SUCCESS;
    }
    
    /**
     * 添加取样计划明细
     */
    @RequestMapping(value = "plans/addInspectInfo", method = RequestMethod.POST)
    @ResponseBody
    public String addInspectPlansInfo(HttpServletRequest request) {
    	User user = UserUtil.getCurrentUser();
        Date now = new Date();
        
        String inspectMasterId = request.getParameter("inspectMasterId");
        String billNum = request.getParameter("billNum");
        String ctnNum = request.getParameter("ctnNum");
        String skuId = request.getParameter("skuId");
        String stockName = request.getParameter("stockName");
        String warehouse = request.getParameter("warehouse");
        String cargoName = request.getParameter("cargoName");
        String contactNum = request.getParameter("contactNum");
        String asn = request.getParameter("asn");
        String inspectNum = request.getParameter("inspectNum");
        String cargoLocation = request.getParameter("cargoLocation");
        String sampleUnit = request.getParameter("sampleUnit");
        
        InspectStock inspectStock = new InspectStock();
        parameterReflect.reflectParameter(inspectStock, request);

        inspectStock.setInspectId(getInspectStockIDfromSEQ());
        inspectStock.setOperateState(UNAUDIT);
        inspectStock.setOperateUserId(user.getId() + "");
        inspectStock.setOperateUserName(user.getName());
        inspectStock.setOperateDate(now);
        inspectStock.setCreateUserId(user.getId() + "");
        inspectStock.setCreateUserName(user.getName());
        inspectStock.setCreateDate(now);
        inspectStock.setState(0);
        inspectStock.setInspectMasterId(inspectMasterId);
        inspectStock.setBillNum(billNum);
        inspectStock.setCtnNum(ctnNum);
        inspectStock.setSkuId(skuId);
        inspectStock.setStockName(stockName);
        inspectStock.setWarehouse(warehouse);
        inspectStock.setCargoName(cargoName);
        inspectStock.setContactNum(contactNum);
        inspectStock.setAsn(asn);
        inspectStock.setInspectTotal(Integer.parseInt(inspectNum));
        inspectStock.setCargoLocation(cargoLocation);
        inspectStock.setSampleUnit(sampleUnit);
        
        inspectStockService.merge(inspectStock);
        
        InspectStockMasterline inspectStockMasterLine = inspectStockMasterLineService.get(inspectMasterId);
        if(inspectStockMasterLine.getStockName() == null || "".equals(inspectStockMasterLine.getStockName())){
        	 //parameterReflect.reflectParameter(inspectStockMasterLine, request);
        	 inspectStockMasterLine.setStockName(stockName);
        	 inspectStockMasterLineService.merge(inspectStockMasterLine);
        }

        return SUCCESS;
    }
   /* @RequestMapping(value = "plans/add", method = RequestMethod.POST)
    @ResponseBody
    public String addInspectPlans(HttpServletRequest request) {

        User user = UserUtil.getCurrentUser();
        Date now = new Date();

        InspectStock inspectStock = new InspectStock();
        parameterReflect.reflectParameter(inspectStock, request);

        inspectStock.setInspectId(generateID());
        inspectStock.setOperateState(UNAUDIT);
        inspectStock.setOperateUserId(user.getId() + "");
        inspectStock.setOperateUserName(user.getName());
        inspectStock.setOperateDate(now);
        inspectStock.setCreateUserId(user.getId() + "");
        inspectStock.setCreateUserName(user.getName());
        inspectStock.setCreateDate(now);
        inspectStock.setState(0);

        inspectStockService.merge(inspectStock);

        return SUCCESS;
    }*/
    private String getInspectStockIDfromSEQ() {

        int seq = inspectStockMasterLineService.getNo();

        return "QY" + StringUtils.lpadStringLeft(8, String.valueOf(seq));
    }
    
    /**
     * 修改取样计划
     */
    @RequestMapping(value = "plans/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateInspectPlan(HttpServletRequest request) {

        User user = UserUtil.getCurrentUser();
        Date now = new Date();

        InspectStock inspectStock = parameterReflect.reflectParameter(request, InspectStock.class);

        inspectStock.setOperateUserId(user.getId() + "");
        inspectStock.setOperateUserName(user.getName());
        inspectStock.setOperateDate(now);

        inspectStockService.merge(inspectStock);

        return SUCCESS;
    }
    
    /**
     * 修改取样计划的主表数据 20170928
     */
    @RequestMapping(value = "plans/updateMasterLine", method = RequestMethod.POST)
    @ResponseBody
    public String updateInspectPlanMasterLine(HttpServletRequest request) {

        User user = UserUtil.getCurrentUser();
        Date now = new Date();

        InspectStockMasterline inspectStockMasterline = parameterReflect.reflectParameter(request, InspectStockMasterline.class);
        InspectStockMasterline inspectStockMasterline1 = getInspectPlanMaster(inspectStockMasterline.getInspectId());
        
        inspectStockMasterline1.setOperateUserId(user.getId() + "");
        inspectStockMasterline1.setOperateUserName(user.getName());
        inspectStockMasterline1.setOperateDate(now);
        inspectStockMasterline1.setDescription(inspectStockMasterline.getDescription());
        inspectStockMasterLineService.merge(inspectStockMasterline1);

        return SUCCESS;
    }

    /**
     * 删除取样计划
     */
    @RequestMapping(value = "plans/delete")
    @ResponseBody
    public String deleteInspectPlan(String inspectId) {

        InspectStock inspectStock = inspectStockService.get(inspectId);

        inspectStock.setState(2);//状态为2表示已删除，0未保存，1已保存

        inspectStockService.merge(inspectStock);

        return SUCCESS;
    }
    
    /**
     * 删除主从表的全部取样计划
     */
    @RequestMapping(value = "plans/deleteAll")
    @ResponseBody
    public String deleteAllInspectPlan(HttpServletRequest request) {
    	
    	String inspectId = request.getParameter("inspectId");
    	
    	List<InspectStock> list = inspectStockService.findInspectStocksByMasterId(inspectId);
    	if(list.size()>0){
    		for(InspectStock is : list){
    			is.setState(2);//状态为2表示已删除，0未保存，1已保存
    			inspectStockService.merge(is);
    		}
    	}
        InspectStockMasterline ism = inspectStockMasterLineService.get(inspectId);
        ism.setState(2);
        inspectStockMasterLineService.merge(ism);
        
        return SUCCESS;
    	
    	
    	
    	/*InspectStock inspectStock = inspectStockService.get(inspectId);

        inspectStock.setState(1);

        inspectStockService.merge(inspectStock);
        
        return SUCCESS;*/

    }
    

    /**
     * 获取取样计划
     */
    @RequestMapping(value = "plan/get", method = RequestMethod.GET)
    @ResponseBody
    public InspectStock getInspectPlan(String inspectId) {
        return inspectStockService.get(inspectId);
    }
    /**
     * 获取主表对象
     * @param inspectId
     * @return
     */
    @RequestMapping(value = "plan/getIMaster", method = RequestMethod.GET)
    @ResponseBody
    public InspectStockMasterline getInspectPlanMaster(String inspectId) {
        return inspectStockMasterLineService.get(inspectId);
    }

    @RequestMapping(value = "plans/audit", method = RequestMethod.GET)
    public String auditInspectPlans(Model model, String inspectId) {

        model.addAttribute("inspectId", inspectId);

        return "wms/inspect/auditForm";
    }

    @RequestMapping(value = "plans/auditInfo", method = RequestMethod.GET)
    public String auditInspectPlansInfo(Model model, String inspectId) {

        model.addAttribute("inspectId", inspectId);

        return "wms/inspect/auditFormInfo";
    }
    
    /**
     * 添加取样明细件数
     */
    @RequestMapping(value = "plans/addInspectNumInfo", method = RequestMethod.GET)
    public String auditInspectPlans(Model model) {

        return "wms/inspect/addInspectNumForm";
    }
    /**
     * 审核取样计划
     */
    @RequestMapping(value = "plans/audit", method = RequestMethod.POST)
    @ResponseBody
    public String auditInspectPlans(String inspectId, String trayId) {

        InspectStock inspectStock = inspectStockService.get(inspectId);

        if (inspectStock == null) return FAILED;
        
        List<Map<String,Object>> list = new ArrayList<>();
        list = trayInfoService.findTrayListForSample(
        		inspectStock.getStockName(), 
        		inspectStock.getBillNum(),
        		inspectStock.getCtnNum(),
        		inspectStock.getAsn(),
        		inspectStock.getSkuId(),
        		trayId);
        
        if(list.size()>0){
        	//for(Map<String,Object> map : list){
        		//if(map.get("TRAY_ID").equals(trayId)){
        			User user = UserUtil.getCurrentUser();
                    Date now = new Date();

                    inspectStock.setTrayId(trayId);
                    inspectStock.setOperateState(AUDIT);
                    inspectStock.setOperateUserId(user.getId() + "");
                    inspectStock.setOperateUserName(user.getName());
                    inspectStock.setOperateDate(now);
                    inspectStock.setCheckDate(now);
                    if(inspectStock.getSampleDate()==null || inspectStock.getSampleDate().equals("")){
                    	inspectStock.setSampleDate(now);
                    }

                    inspectStockService.update(inspectStock);
                    
                    //在审核时候 给主表更新实际取样时间
                    InspectStockMasterline ism = inspectStockMasterLineService.get(inspectStock.getInspectMasterId());
                    ism.setSampleDate(now);
                    inspectStockMasterLineService.merge(ism);
                    
                    return SUCCESS;
        		//}
        	//} 
        }
        return FAILED;
    }

    /**
     * 取消审核取样计划
     */
    @RequestMapping(value = "plans/audit/cancel")
    @ResponseBody
    public String cancelAuditInspectPlans(String inspectId) {

        InspectStock inspectStock = inspectStockService.get(inspectId);

        if (inspectStock == null) return FAILED;

        User user = UserUtil.getCurrentUser();
        Date now = new Date();

        inspectStock.setTrayId("");
        inspectStock.setOperateState(UNAUDIT);
        inspectStock.setOperateUserId(user.getId() + "");
        inspectStock.setOperateUserName(user.getName());
        inspectStock.setOperateDate(now);

        inspectStockService.merge(inspectStock);

        return SUCCESS;
    }

    private String generateID() {

        int seq = inOutService.getNo();

        return "QY" + StringUtils.lpadStringLeft(8, String.valueOf(seq));
    }

    @RequestMapping(value = "print/{inspectId}", method = RequestMethod.GET)
	public String print(@PathVariable("inspectId") String inspectId, Model model) {
    	User user=UserUtil.getCurrentUser();
    	model.addAttribute("user", user.getName());
    	InspectStockMasterline inspectStockM = inspectStockMasterLineService.get(inspectId);
    	//List<Map<String,Object>> list = stockReportService.getTrayInfoByTrayId(inspectStock.getTrayId());
    	
    	//样品明细具体内容
    	List<InspectStock> list = inspectStockService.findInspectStocksByMasterId(inspectId);
    	/*String sampleInfo = "";
    	if(list.size()>0){
    		for(InspectStock is : list){
    			if(!(is.getState() == 2)){
    				sampleInfo +="["+is.getCargoName()+",数量："+is.getInspectTotal()+"]</br>";
    			}
    		}
    		if(sampleInfo.length()>1){
    			sampleInfo = sampleInfo.substring(0, sampleInfo.length()-1);
    		}
    		
    	}*/
    	
    	
    	String nowDateString = "";
    	Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
    	nowDateString = sdf.format(date);
    	
    	model.addAttribute("stockName",inspectStockM.getStockName());
    	model.addAttribute("sampleDate", inspectStockM.getSampleDate()!=null?sdf.format(inspectStockM.getSampleDate()):"");
    	model.addAttribute("checkDate", nowDateString);
    	//model.addAttribute("cargoName", inspectStock.getCargoName());
    	//model.addAttribute("billNum", inspectStock.getBillNum());
    	//model.addAttribute("ctnNum",inspectStock.getCtnNum());
    	//model.addAttribute("trayId", list.size()>0?list.get(0).get("CARGO_LOCATION").toString():"");
    	model.addAttribute("inspectTotal", inspectStockM.getInspectTotal());
    	
    	model.addAttribute("sampleInfo",list);
    	model.addAttribute("obj", inspectStockM);
		model.addAttribute("action", "print");
		
    	return "wms/inspect/indexPrint";

	}
    
}
