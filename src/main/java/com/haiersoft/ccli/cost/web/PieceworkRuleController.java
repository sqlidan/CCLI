package com.haiersoft.ccli.cost.web;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.base.entity.BasePersontypeRule;
import com.haiersoft.ccli.base.entity.BaseWorkGroup;
import com.haiersoft.ccli.base.entity.BisAsnWorker;
import com.haiersoft.ccli.base.entity.BisLoadingOrderWorker;
import com.haiersoft.ccli.base.entity.BisOtherWorker;
import com.haiersoft.ccli.base.service.BasePersontypeRuleService;
import com.haiersoft.ccli.base.service.BisASNWorkerService;
import com.haiersoft.ccli.base.service.BisLoadingOrderWorkerService;
import com.haiersoft.ccli.base.service.BisOtherWorkerService;
import com.haiersoft.ccli.base.service.WorkGroupService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.BasePieceworkRule;
import com.haiersoft.ccli.cost.entity.WorkReport;
import com.haiersoft.ccli.cost.service.PieceworkRuleService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BaseRemindTask;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
import com.haiersoft.ccli.wms.service.TaskRemindService;

/**
 * 
 * @author pyl
 * @ClassName: PieceworkRuleController
 * @Description: 作业列表Controller
 * @date 2017年5月18日 下午3:48:33
 */
@Controller
@RequestMapping("cost/piecework")
public class PieceworkRuleController extends BaseController{
	
	//@Autowired
	//private EnterStockService enterStockService;
	//@Autowired
	//private MonitorService monitorService;
	//@Autowired
	//private ClientService clientService;
	//@Autowired
	//private ExpenseSchemeInfoService expenseSchemeInfoService;
	@Autowired
	private ASNService asnService;
	//@Autowired
	//private ASNInfoService asnInfoService;
	@Autowired
	private LoadingInfoService loadingInfoService;
	//@Autowired
	//private DictService dictService;
	@Autowired
	private TaskRemindService remindService;
	@Autowired
	private PieceworkRuleService pieceworkRuleService;
	@Autowired
	private WorkGroupService workGroupService;
	@Autowired
	private BasePersontypeRuleService basePersonTypeRuleService;
	@Autowired
	private BisASNWorkerService bisAsnWorkerService;
	@Autowired
	private LoadingOrderService loadingOrderService;
	@Autowired
	private BisLoadingOrderWorkerService bisLoadingOrderWorkerService;
	@Autowired
	private BisOtherWorkerService bisOtherWorkerService;
	
	
	/**
	 * 入库作业列表
	 */
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "cost/jobAllocation/enterJobList";
	}
	
	/**
	 * 出库作业列表
	 */
	@RequestMapping(value="list2", method = RequestMethod.GET)
	public String manager(Model model) {
		return "cost/jobAllocation/outJobList";
	}
	
	/**
	 * 计件规则列表
	 */
	@RequestMapping(value="ruleList", method = RequestMethod.GET)
	public String ruleList(Model model) {
		return "cost/jobAllocation/pieceworkRuleList";
	}
	
	/**
	 * 计件报表
	 */
	@RequestMapping(value="reportList", method = RequestMethod.GET)
	public String reportList() {
		return "cost/jobAllocation/workReportList";
	}
	
	/**
	 * 杂项作业计件
	 */
	@RequestMapping(value="pieceOtherWork", method = RequestMethod.GET)
	public String pieceOtherWork(Model model) {
		return "cost/jobAllocation/pieceOtherWork";
	}
	
	/**
	 * 杂项作业计件统计报表
	 */
	@RequestMapping(value="otherWorkReportList", method = RequestMethod.GET)
	public String otherWorkReportList(Model model) {
		return "cost/jobAllocation/otherWorkReportList";
	}
	
	/**
	 * 人员类型计件规则
	 */
	@RequestMapping(value="persontypeRule", method = RequestMethod.GET)
	public String persontypeRule(Model model) {
		return "cost/jobAllocation/persontypeRule";
	}
	
	/*
     * 入库作业列表展示ASN
     */
    @RequestMapping(value = "asnList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> asnList(HttpServletRequest request) {
        Page<BisAsn> page = getPage(request);
        page.orderBy("asn").order(Page.DESC);

        BisAsn bisAsn = new BisAsn();
        parameterReflect.reflectParameter(bisAsn, request);

        page = asnService.getAsnToWork(page, bisAsn);

        return getEasyUIData(page);
    }
    
    /*
     * 出库作业列表展示装车单
     */
    @RequestMapping(value = "loadingList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> loadingList(HttpServletRequest request) {
        Page<BisLoadingInfo> page = getPage(request);
//        page.orderBy("asn").order(Page.DESC);

        BisLoadingInfo bisLoadingInfo = new BisLoadingInfo();
        parameterReflect.reflectParameter(bisLoadingInfo, request);

        page = loadingInfoService.getLoadingToWork(page, bisLoadingInfo);

        return getEasyUIData(page);
    }
    
    /*
     * 计件规则展示
     */
    @RequestMapping(value = "rule", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> rule(HttpServletRequest request) {
        Page<BasePieceworkRule> page = getPage(request);
		page.orderBy("id").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = pieceworkRuleService.search(page, filters);
		return getEasyUIData(page);
    }
	
    /**
     * 指派作业人员 
     * 20170929新增 ：库管人员不再存入ASN中
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "assignManNotToASN/{num}/{type}", method = RequestMethod.GET)
    public String updateFormNotToAsn(HttpServletRequest request,@PathVariable("num") String num,@PathVariable("type") String type, Model model) {
    	if(type.equals("1")){//1的类型应该是入库的情况 下面的else是出库
    		BisAsn getObj = asnService.get(num);
    		
    		model.addAttribute("remindId", getObj.getRemindId()!=null?getObj.getRemindId():"");
    		model.addAttribute("asn", num);
    		model.addAttribute("type", "1");
    		return "cost/jobAllocation/assignWorkMan";
    	}else{
    		if(num!=null && !"".equals(num) && num.contains(",")){
    			String[] ss = num.split(",");//ss[0]订单号，ss[1]装车单号
    			List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        		filters.add(new PropertyFilter("EQS_loadingPlanNum", ss[0]));
        		List<BisLoadingInfo> infos = loadingInfoService.findao(filters);
        		BisLoadingInfo getObj= infos.get(0);
        		//String loadingTruckNum = request.getParameter("loadingTruckNum");
        		model.addAttribute("remindId", getObj.getRemindId()!=null?getObj.getRemindId():"");
        		model.addAttribute("loadingPlanNum", ss[0]);
        		model.addAttribute("type", "2");
        		model.addAttribute("loadingTruckNum", ss[1]);
        		
    		}
    		return "cost/jobAllocation/assignWorkManForZC";
    		
    	}
       
    }
    
    /**
     * 指派作业人员
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "assignMan/{num}/{type}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("num") String num,@PathVariable("type") String type, Model model) {
    	if(type.equals("1")){
    		BisAsn getObj = asnService.get(num);
    		model.addAttribute("workMan",getObj.getWorkMan()!=null?getObj.getWorkMan():"");
    		model.addAttribute("lhPerson",getObj.getLhPerson()!=null?getObj.getLhPerson():"");
    		model.addAttribute("ccPerson",getObj.getCcPerson()!=null?getObj.getCcPerson():"");
    		model.addAttribute("ccPerson2",getObj.getCcPerson2()!=null?getObj.getCcPerson2():"");
    		model.addAttribute("remindId", getObj.getRemindId()!=null?getObj.getRemindId():"");
    		model.addAttribute("asn", num);
    		model.addAttribute("type", "1");
    		return "cost/jobAllocation/asnWorkMan";
    	}else{
    		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    		filters.add(new PropertyFilter("EQS_loadingTruckNum", num));
    		List<BisLoadingInfo> infos = loadingInfoService.findao(filters);
    		BisLoadingInfo getObj= infos.get(0);
    		model.addAttribute("workMan",getObj.getWorkMan()!=null?getObj.getWorkMan():"");
    		model.addAttribute("lhPerson",getObj.getLhPerson()!=null?getObj.getLhPerson():"");
    		model.addAttribute("ccPerson",getObj.getCcPerson()!=null?getObj.getCcPerson():"");
    		model.addAttribute("ccPerson2",getObj.getCcPerson2()!=null?getObj.getCcPerson2():"");
    		model.addAttribute("remindId", getObj.getRemindId()!=null?getObj.getRemindId():"");
    		model.addAttribute("loadingNum", num);
    		model.addAttribute("type", "2");
    		return "cost/jobAllocation/loadingWorkMan";
    	}
       
        
    }
	
	


	
	
	/*
     * ASN指派作业人员
     */
    @RequestMapping(value = "assign", method = RequestMethod.POST)
    @ResponseBody
    public String assign(HttpServletRequest request) {
    	User user=UserUtil.getCurrentUser();
    	String userName=request.getParameter("workMan");
    /*  String lh=request.getParameter("lhPerson");//理货人员
    	String cc=request.getParameter("ccPerson");//叉车人员
    	String cc2=request.getParameter("ccPerson2");//叉车人员2
*/    	String asnId=request.getParameter("asnId");
    	String remindId=request.getParameter("remindId");
    	if(null!=remindId && !remindId.equals("")){
    		//更新任务提醒
    		BaseRemindTask remindNew=new BaseRemindTask();
    		BaseRemindTask remindOld=remindService.get(Integer.valueOf(remindId));
    		BeanUtils.copyProperties(remindOld, remindNew);
    		remindNew.setUserName(userName);
    		remindNew.setState(0);
    		remindNew.setCreateTime(new Date());
    		remindNew.setCreateUser(user.getName());
    		remindNew.setId(null);
    		remindService.delete(remindOld);
    		remindService.save(remindNew);
    		//更新ASN作业人员
    		/*BisAsn asnObj=asnService.get(asnId);
        	asnObj.setWorkMan(userName);
        	asnObj.setRemindId(remindNew.getId());
        	asnObj.setLhPerson(lh);
        	asnObj.setCcPerson(cc);
        	asnObj.setCcPerson2(cc2);
        	asnService.update(asnObj);*/
    	}else{
    		BaseRemindTask remindObj=new BaseRemindTask();
    		remindObj.setContent("ASN号为" + asnId + "的入库单已指派工作，请注意查看");
    		remindObj.setCreateTime(new Date());
    		remindObj.setCreateUser(user.getName());
    		remindObj.setState(0);
    		remindObj.setType("3");
    		remindObj.setUserName(userName);
    		remindService.save(remindObj);
    		/*BisAsn asnObj=asnService.get(asnId);
        	asnObj.setWorkMan(userName);
        	asnObj.setLhPerson(lh);
        	asnObj.setCcPerson(cc);
        	asnObj.setCcPerson2(cc2);
        	asnObj.setRemindId(remindObj.getId());
        	asnService.update(asnObj);*/
    	}
        return "success";
    }
	
    /*
     * 装车单指派作业人员
     */
    @RequestMapping(value = "assignL", method = RequestMethod.POST)
    @ResponseBody
    public String assignL(HttpServletRequest request) {
    	User user=UserUtil.getCurrentUser();
    	String userName=request.getParameter("workMan");
    	String lh=request.getParameter("lhPerson");//理货人员
    	String cc=request.getParameter("ccPerson");//叉车人员
    	String cc2=request.getParameter("ccPerson2");//叉车人员2
    	String remindId=request.getParameter("remindId");
    	String loadingNum=request.getParameter("loadingNum");
    	if(null!=remindId && !remindId.equals("")){
    		//更新任务提醒
    		BaseRemindTask remindNew=new BaseRemindTask();
    		BaseRemindTask remindOld=remindService.get(Integer.valueOf(remindId));
    		BeanUtils.copyProperties(remindOld, remindNew);
    		remindNew.setUserName(userName);
    		remindNew.setState(0);
    		remindNew.setCreateTime(new Date());
    		remindNew.setCreateUser(user.getName());
    		remindNew.setId(null);
    		remindService.delete(remindOld);
    		remindService.save(remindNew);
    		//更新ASN作业人员
    		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    		filters.add(new PropertyFilter("EQS_loadingTruckNum", loadingNum));
    		List<BisLoadingInfo> infos = loadingInfoService.findao(filters);
    		for(BisLoadingInfo loadingObj:infos){
	    		loadingObj.setWorkMan(userName);
	    		loadingObj.setRemindId(remindNew.getId());
	    		loadingObj.setLhPerson(lh);
	    		loadingObj.setCcPerson(cc);
	    		loadingObj.setCcPerson2(cc2);
	    		loadingInfoService.update(loadingObj);
    		}
    	}else{
    		BaseRemindTask remindObj=new BaseRemindTask();
    		remindObj.setContent("装车号号为" + loadingNum + "的出库单已指派工作，请注意查看");
    		remindObj.setCreateTime(new Date());
    		remindObj.setCreateUser(user.getName());
    		remindObj.setState(0);
    		remindObj.setType("4");
    		remindObj.setUserName(userName);
    		remindService.save(remindObj);
    		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    		filters.add(new PropertyFilter("EQS_loadingTruckNum", loadingNum));
    		List<BisLoadingInfo> infos = loadingInfoService.findao(filters);
    		for(BisLoadingInfo loadingObj:infos){
	    		loadingObj.setWorkMan(userName);
	    		loadingObj.setRemindId(remindObj.getId());
	    		loadingObj.setLhPerson(lh);
	    		loadingObj.setCcPerson(cc);
	    		loadingObj.setCcPerson2(cc2);
	        	loadingInfoService.update(loadingObj);
    		}
    	}
        return "success";
    }
	
    
    /**
     * 计件规则添加
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("rule", new BasePieceworkRule());
        User user = UserUtil.getCurrentUser();
        model.addAttribute("user", user.getName());
        model.addAttribute("date", new Date());
        return "cost/jobAllocation/ruleForm";
    }
    
    /*
     * 计件规则查重
     */
    @RequestMapping(value = "checkKey", method = RequestMethod.POST)
    @ResponseBody
    public String checkKey(HttpServletRequest request) {
	      String personType=request.getParameter("personType");
	      String jobType = request.getParameter("jobType");
	      //String ratio = request.getParameter("ratio");
	      String version=request.getParameter("version");
	      String keyId=personType+","+jobType;// +","+Double.valueOf(ratio).toString();
	      if(version.equals("create")){
		      BasePieceworkRule ruleObj=new BasePieceworkRule();
		      ruleObj=pieceworkRuleService.find("keyId", keyId);
		      if(null==ruleObj){
		    	  return "success";
		      }else{
		    	  return "false";
		      }
	      }else{
	    	  String id=request.getParameter("id");
	    	  BasePieceworkRule ruleObj=pieceworkRuleService.get(Integer.valueOf(id));
	    	  if(ruleObj.getKeyId().equals(keyId)){
	    		  return "success";
	    	  }else{
	    		  BasePieceworkRule ruleCheck=new BasePieceworkRule();
	    		  ruleCheck=pieceworkRuleService.find("keyId", keyId);
	    		  if(null==ruleCheck){
			    	  return "success";
			      }else{
			    	  return "false";
			      }
	    	  }
	      }
    }
    
    
    /**
     * 添加计件规则
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid BasePieceworkRule rule, Model model) {
    	rule.setKeyId(rule.getPersonType()+","+rule.getJobType());//+","+rule.getRatio().toString());
    	pieceworkRuleService.save(rule);
        return "success";
    }
    
    
    /**
     * @Description: 删除计件规则
     */
    @RequestMapping(value = "delete/{ids}")
    @ResponseBody
    public String delete(@PathVariable("ids") List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
        	pieceworkRuleService.delete((ids.get(i)));
        }
        return "success";
    }
    
    
    /**
     * 修改计件规则跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("rule", pieceworkRuleService.get(id));
        model.addAttribute("action", "update");
        return "cost/jobAllocation/ruleForm";
    }
    
    
    /**
     * 修改计件规则
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody BasePieceworkRule ruleObj, Model model) {
    	pieceworkRuleService.update(ruleObj);
        return "success";
    }
    
    
    /**
     * 维护计件规则页面跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "selectRule/{num}/{type}", method = RequestMethod.GET)
    public String selectRule(@PathVariable("num") String num,@PathVariable("type") String type, Model model) {
    	if(type.equals("1")){
    		BisAsn getObj = asnService.get(num);
    		model.addAttribute("jobType",getObj.getRuleJobType()!=null?getObj.getRuleJobType():"");
    		model.addAttribute("asn", num);
    		model.addAttribute("type", "1");
    		return "cost/jobAllocation/selectAsnRule";
    	}else{
    		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    		filters.add(new PropertyFilter("EQS_loadingPlanNum", num));
    		BisLoadingOrder info = loadingOrderService.find("orderNum", num);//.findao(filters);
    		//BisLoadingInfo getObj= infos;//.get(0);
    		model.addAttribute("jobType",info.getRuleJobType()!=null?info.getRuleJobType():"");
    		model.addAttribute("loadingPlanNum", num);
    		model.addAttribute("type", "2");
    		return "cost/jobAllocation/selectLoadingRule";
    	}
    }
    
    
    /**
     * 添加计件规则跳转
     */
    @RequestMapping(value = "rulelist", method = RequestMethod.GET)
    public String seletSKUList(Model model) {
        return "cost/jobAllocation/ruleListToSelect";
    }
    
    
    
    
    /**
     * 添加计件规则
     *
     * @param user
     * @param model
     */
    @RequestMapping(value = "addRule/{num}/{type}", method = RequestMethod.POST)
    @ResponseBody
    public String create(HttpServletRequest request,@PathVariable("num") String num,@PathVariable("type") String type) {
    	String ruleJobType = request.getParameter("ruleJobType");
    	//Double ratio = Double.parseDouble(request.getParameter("ratio")); 
        if(type.equals("1")){
        	BisAsn asnObj=asnService.get(num);
        	asnObj.setRuleJobType(ruleJobType);
        	asnService.update(asnObj);
        }else{
        	//List<PropertyFilter> loadPfs = new ArrayList<PropertyFilter>();
			//loadPfs.add(new PropertyFilter("EQS_loadingTruckNum", num));
        	BisLoadingOrder loadingObj=loadingOrderService.find("orderNum", num);
        	
        			loadingObj.setRuleJobType(ruleJobType);
        			
        			
        			loadingOrderService.update(loadingObj);
        		
        
        }
        return "success";
    }
    
    
    /**
	 * 
	 * @author PYL
	 * @Description: 获得库管员
	 * @param code
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "getKgAll", method = RequestMethod.GET)
	@ResponseBody
	public List<BaseWorkGroup> getKgAll(HttpServletRequest request,HttpServletResponse response) {
		List<BaseWorkGroup> objList = new ArrayList<BaseWorkGroup>();
		List<Map<String,Object>> userList = new ArrayList<Map<String,Object>>();
		String param = request.getParameter("q");// 搜索值
		String setid = request.getParameter("setid");// 原数据填充值
		if ((param != null && !"".equals(param)) || (setid != null && !"".equals(setid))) {
			if (param != null && !"".equals(param)) {
				userList = workGroupService.findKgByName(param);
			} else {
				// 根据原值id获取对象
				userList = workGroupService.findKgByName(setid);
			}
		}
		
		for(Map<String,Object> obj:userList){
			BaseWorkGroup user=new BaseWorkGroup();
			user.setPerson(obj.get("PERSON").toString());
			objList.add(user);
		}
		return objList;
	}
	
	@RequestMapping(value = "getAllUsers", method = RequestMethod.GET)
	@ResponseBody
	public List<BaseWorkGroup> getAllUsers(HttpServletRequest request,HttpServletResponse response) {
		List<BaseWorkGroup> objList = new ArrayList<BaseWorkGroup>();
		List<Map<String,Object>> userList = new ArrayList<Map<String,Object>>();
		String param = request.getParameter("q");// 搜索值
		String setid = request.getParameter("setid");// 原数据填充值
		if ((param != null && !"".equals(param)) || (setid != null && !"".equals(setid))) {
			if (param != null && !"".equals(param)) {
				userList = workGroupService.findUsersByName(param);
			} else {
				// 根据原值id获取对象
				userList = workGroupService.findUsersByName(setid);
			}
		}
		
		for(Map<String,Object> obj:userList){
			BaseWorkGroup user=new BaseWorkGroup();
			user.setPerson(obj.get("PERSON").toString());
			objList.add(user);
		}
		return objList;
	}
	
	
	/**
	 * 获取理货和叉车人员
	 */
	@RequestMapping(value="getOther",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> otherList(HttpServletRequest request) {
			Page<BaseWorkGroup> page = getPage(request);
			BaseWorkGroup obj = new BaseWorkGroup();
	        parameterReflect.reflectParameter(obj, request);//转换对应实体类参数

	        page = workGroupService.searchOther(page, obj);

	        return getEasyUIData(page);
	}
	
	@RequestMapping(value="getAllOther",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> AllotherList(HttpServletRequest request) {
			Page<BaseWorkGroup> page = getPage(request);
			BaseWorkGroup obj = new BaseWorkGroup();
	        parameterReflect.reflectParameter(obj, request);//转换对应实体类参数

	        page = workGroupService.searchAllOther(page, obj);

	        return getEasyUIData(page);
	}
	
	/**
	 * 获取人员类型
	 */
	@RequestMapping(value="getAllPersonType",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAllPersonType(HttpServletRequest request) {
		Page<BasePersontypeRule> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = basePersonTypeRuleService.getEntityDao().findPage(page, filters);
		//List<BasePersontypeRule> list = page.getResult();
		return getEasyUIData(page);
	}
	
	/**
	 * 作业计件统计
	 * @param request
	 * @return
	 */
      
    @RequestMapping(value = "report", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {

        Page<WorkReport> page = getPage(request);

        WorkReport workReport = new WorkReport();
        parameterReflect.reflectParameter(workReport, request);//转换对应实体类参数
        if(workReport.getWorkTimeS()==null && workReport.getWorkTimeE()==null && workReport.getFirst().equals("1")){
        	Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date zero = calendar.getTime();
            workReport.setWorkTimeS(zero);
       	    workReport.setWorkTimeE(DateUtils.addDay(zero, 1));
        }else if(workReport.getWorkTimeE()!=null){
        	workReport.setWorkTimeE(DateUtils.addDay(workReport.getWorkTimeE(), 1));
        }
        page = pieceworkRuleService.searchWorkReport(page, workReport);

        return getEasyUIData(page);
    }
    
    /**
     * 添加作业人员到bis_asn_worker
     * @return
     */
    @RequestMapping(value = "saveBisAsnWorker", method = RequestMethod.POST)
    @ResponseBody
    public String saveBisAsnWorker(HttpServletRequest request){
    
    	String personTypeId = request.getParameter("personTypeId");
    	String personTypeName = request.getParameter("personTypeName");
    	String linkId = request.getParameter("linkId");//ASN
    	String person = request.getParameter("person");
    	
    	BisAsnWorker baw = new BisAsnWorker();
    	//parameterReflect.reflectParameter(baw, request);
    	baw.setId(bisAsnWorkerService.getEntityDao().getSequenceId("SEQ_BIS_ASN_WORKER")+"");
    	baw.setLinkID(linkId);
    	baw.setWorkerTypeId(Integer.parseInt(personTypeId));
    	baw.setWorkerTypeName(personTypeName);
    	baw.setWorkerName(person);
    	
    	
    	bisAsnWorkerService.save(baw);
    	
    	//添加提醒记录 "库管兼理货".equals(personTypeName)
    	if((personTypeName.contains("库管") || personTypeName.contains("理货")) && (!personTypeName.contains("叉车"))){
    		BaseRemindTask remindObj=new BaseRemindTask();
    		remindObj.setContent("ASN号为" + linkId + "的入库单已指派工作，请注意查看");
    		remindObj.setCreateTime(new Date());
    		remindObj.setCreateUser(UserUtil.getCurrentUser().getName());
    		remindObj.setState(0);
    		remindObj.setType("3");
    		remindObj.setUserName(person);
    		remindObj.setLinkId(linkId);//asn
    		remindService.save(remindObj);
    		
    	}
    	return "success";
    }
    
	/*
     * 展示作业人员
     */
    @RequestMapping(value = "asnWorkerList/{asn}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> asnWorkerList(HttpServletRequest request,@PathVariable("asn") String asn) {
        Page<BisAsnWorker> page = getPage(request);
        //page.orderBy("asn").order(Page.DESC);

        BisAsnWorker bisAsnWorker = new BisAsnWorker();
        parameterReflect.reflectParameter(bisAsnWorker, request);

        page = bisAsnWorkerService.getAllAsnWorker(page, bisAsnWorker ,asn);

        return getEasyUIData(page);
    }
    
    /**
     * 删除作业人员
     * @return
     */
    @RequestMapping(value = "deleteASNWorker/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String deleteBisAsnWorker(HttpServletRequest request,@PathVariable("id") String id){
    	
    	String[] ss= id.split(",");//id传过来的值是"id,workerName"
    	
    	String linkId = bisAsnWorkerService.get(ss[0]).getLinkID();//asn
    	
    	//remindService.delete();//以asn,人员为条件删除提醒记录
    	if(linkId!=null && !"".equals(linkId) && ss[1]!=null && !"".equals(ss[1])){
    		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    		filters.add(new PropertyFilter("EQS_linkId", linkId));
    		filters.add(new PropertyFilter("EQS_userName", ss[1]));
    		List<BaseRemindTask> infos = remindService.search(filters);
    		if(infos.size()>0){
    			BaseRemindTask getObj= infos.get(0);
        		remindService.delete(getObj.getId());
    		}
    		
    	}
    	
    	bisAsnWorkerService.delete(ss[0]);
    	return "success";
    }
    
    /**
     * 添加出库作业人员到BIS_LOADING_ORDER_WORKER
     * @return
     */
    @RequestMapping(value = "saveBisLoadingOrderWorker", method = RequestMethod.POST)
    @ResponseBody
    public String saveBisLoadingOrderWorker(HttpServletRequest request){
    
    	String personTypeId = request.getParameter("personTypeId");
    	String personTypeName = request.getParameter("personTypeName");
    	String linkId = request.getParameter("linkId");
    	String person = request.getParameter("person");
    	
    	BisLoadingOrderWorker blow = new BisLoadingOrderWorker();
    	blow.setId(bisLoadingOrderWorkerService.getEntityDao().getSequenceId("SEQ_BIS_LOADING_ORDER_WORKER")+"");
    	blow.setLinkId(linkId);
    	blow.setWorkerTypeId(Integer.parseInt(personTypeId));
    	blow.setWorkerTypeName(personTypeName);
    	blow.setWorkerName(person);
    	
    	bisLoadingOrderWorkerService.save(blow);
    	
    	//添加提醒记录
    	if((personTypeName.contains("库管") || personTypeName.contains("理货")) && (!personTypeName.contains("叉车"))){
	    	String loadingNum = request.getParameter("loadingTruckNum");//装车单号
			BaseRemindTask remindObj=new BaseRemindTask();
			remindObj.setContent("装车单号为" + loadingNum + "的出库单已指派工作，请注意查看");
			remindObj.setCreateTime(new Date());
			remindObj.setCreateUser(UserUtil.getCurrentUser().getName());
			remindObj.setState(0);
			remindObj.setType("4");
			remindObj.setUserName(person);
			remindObj.setLinkId(linkId);//出库订单
			remindObj.setLoadingNum(loadingNum);//装车单号
			remindService.save(remindObj);//
    	}
    	return "success";
    }
    
	/*
     * 出库作业界面 指派库管员时的展示作业人员
     */
    @RequestMapping(value = "loadingOrderWorkerList/{loadingPlanNum}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> loadingOrderWorkerList(HttpServletRequest request,@PathVariable("loadingPlanNum") String loadingPlanNum) {
        Page<BisLoadingOrderWorker> page = getPage(request);
        //page.orderBy("asn").order(Page.DESC);

        BisLoadingOrderWorker blow = new BisLoadingOrderWorker();
        parameterReflect.reflectParameter(blow, request);

        page = bisLoadingOrderWorkerService.getAllLoadingOrderWorker(page, blow ,loadingPlanNum);

        return getEasyUIData(page);
    }
    
    /**
     * 删除出库作业人员
     * @return
     */
    @RequestMapping(value = "deleteLoadingOrderWorker/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String deleteBisLoadingOrderWorker(HttpServletRequest request,@PathVariable("id") String id){
    	
    	String[] ss = id.split(",");
    	String linkId = bisLoadingOrderWorkerService.get(ss[0]).getLinkId();//出库订单
    	
    	if(linkId!=null && !"".equals(linkId) && ss[1]!=null && !"".equals(ss[1])){
    		if(linkId!=null && !"".equals(linkId) && ss[1]!=null && !"".equals(ss[1])){
        		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        		filters.add(new PropertyFilter("EQS_linkId", linkId));
        		filters.add(new PropertyFilter("EQS_userName", ss[1]));
        		List<BaseRemindTask> infos = remindService.search(filters);
        		if(infos.size()>0){
        			BaseRemindTask getObj= infos.get(0);
            		remindService.delete(getObj.getId());
        		}
        		
        	}
    	}
    	bisLoadingOrderWorkerService.delete(ss[0]);
    	return "success";
    }
    
    /*
     * 杂项作业计件展示
     */
    @RequestMapping(value = "otherWorkList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> otherWorkList(HttpServletRequest request) {
        Page<BisOtherWorker> page = getPage(request);
		page.orderBy("id").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		//filters.add(new PropertyFilter("EQS_loadingPlanNum", num));
		String workerName = request.getParameter("workerName");
		String workerTypeName = request.getParameter("workerTypeName");
		String workDateS = request.getParameter("workDateS");
		String workDateE = request.getParameter("workDateE");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(workerName!=null && !("".equals(workerName))){
			filters.add(new PropertyFilter("EQS_workerName", workerName));
		}
		if(workerTypeName!=null && !("".equals(workerTypeName))){
			filters.add(new PropertyFilter("EQS_workerTypeName", workerTypeName));
		}
		if(workDateS!=null && !("".equals(workDateS))){
			Date workDateS1;
			try {
				workDateS1 = sdf.parse(workDateS);
				filters.add(new PropertyFilter("GED_workDate", workDateS1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		if(workDateE!=null && !("".equals(workDateE))){
			Date workDateS2;
			try {
				workDateS2 = sdf.parse(workDateE);
				filters.add(new PropertyFilter("LED_workDate", workDateS2));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		
		
		page = bisOtherWorkerService.search(page, filters);
		return getEasyUIData(page);
    }
    
    /**
     * 杂项作业添加
     */
    @RequestMapping(value = "createOtherWork", method = RequestMethod.GET)
    public String createOtherWork(Model model) {
        model.addAttribute("action", "createOtherWork");
        model.addAttribute("otherWorker", new BisOtherWorker());
        User user = UserUtil.getCurrentUser();
        model.addAttribute("user", user.getName());
        model.addAttribute("date", new Date());
        return "cost/jobAllocation/otherWorkForm";
    }
    
    /**
     * 添加杂项作业到bis_other_worker
     */
    @RequestMapping(value = "createOtherWork", method = RequestMethod.POST)
    @ResponseBody
    public String createOtherWork(@Valid BisOtherWorker otherWorker, Model model) {
    	otherWorker.setId(bisOtherWorkerService.getEntityDao().getSequenceId("SEQ_BIS_OTHER_WORKER")+"");
    	bisOtherWorkerService.save(otherWorker);
        return "success";
    }
    
    
    /**
     * 修改杂项计件跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "updateOtherWork/{id}", method = RequestMethod.GET)
    public String updateOtherWorkForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("otherWorker", bisOtherWorkerService.get(id));
        model.addAttribute("action", "updateOtherWork");
        return "cost/jobAllocation/otherWorkForm";
    }
    
    
    /**
     * 修改杂项计件
     * @return
     */
    @RequestMapping(value = "updateOtherWork", method = RequestMethod.POST)
    @ResponseBody
    public String updateOtherWork(@Valid @ModelAttribute @RequestBody BisOtherWorker ruleObj, Model model) {
    	bisOtherWorkerService.update(ruleObj);
        return "success";
    }
    
    /**
     * @Description: 删除杂项作业
     */
    @RequestMapping(value = "deleteOtherWork/{ids}")
    @ResponseBody
    public String deleteOtherWork(@PathVariable("ids") List<String> ids) {
        for (int i = 0; i < ids.size(); i++) {
        	bisOtherWorkerService.delete((ids.get(i)));
        }
        return "success";
    }
    
	/**
	 * 杂项作业计件统计
	 * @param request
	 * @return
	 */
      
    @RequestMapping(value = "otherWorkerReport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getOtherWorkerData(HttpServletRequest request) {

        Page<WorkReport> page = getPage(request);

        WorkReport workReport = new WorkReport();
        parameterReflect.reflectParameter(workReport, request);//转换对应实体类参数
        /*
         * 用WorkReport的对象承载传递数据。构建datagrid
         * 人员：personName,作业类型：jobType,作业日期起止：workTimeS，workTimeE
         * */
        
    	String workerName = request.getParameter("workerName");
		String workerTypeName = request.getParameter("workerTypeName");
		String workDateS = request.getParameter("workDateS");
		String workDateE = request.getParameter("workDateE");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(workerName!=null && !("".equals(workerName))){
			workReport.setPersonName(workerName);
		}
		if(workerTypeName!=null && !("".equals(workerTypeName))){
			workReport.setJobType(workerTypeName);
		}
		if(workDateS!=null && !("".equals(workDateS))){
			Date workDateS1;
			try {
				workDateS1 = sdf.parse(workDateS);
				workReport.setWorkTimeS(workDateS1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		if(workDateE!=null && !("".equals(workDateE))){
			Date workDateS2;
			try {
				workDateS2 = sdf.parse(workDateE);
				workReport.setWorkTimeE(workDateS2);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
        
        
        page = bisOtherWorkerService.searchOtherWorkerReport(page, workReport);

        return getEasyUIData(page);
    }
    
    
	/**
	 * 杂项作业计件统计--合计时间
	 * @param request
	 * @return
	 */
      
    @RequestMapping(value = "otherWorkerReportHeji", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getOtherWorkerHejiData(HttpServletRequest request) {

        Page<WorkReport> page = getPage(request);

        WorkReport workReport = new WorkReport();
        parameterReflect.reflectParameter(workReport, request);//转换对应实体类参数
        /*
         * 用WorkReport的对象承载传递数据。构建datagrid
         * 人员：personName,作业类型：jobType,作业日期起止：workTimeS，workTimeE
         * */
        
    	String workerName = request.getParameter("workerName");
		String workerTypeName = request.getParameter("workerTypeName");
		String workDateS = request.getParameter("workDateS");
		String workDateE = request.getParameter("workDateE");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(workerName!=null && !("".equals(workerName))){
			workReport.setPersonName(workerName);
		}
		if(workerTypeName!=null && !("".equals(workerTypeName))){
			workReport.setJobType(workerTypeName);
		}
		if(workDateS!=null && !("".equals(workDateS))){
			Date workDateS1;
			try {
				workDateS1 = sdf.parse(workDateS);
				workReport.setWorkTimeS(workDateS1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		if(workDateE!=null && !("".equals(workDateE))){
			Date workDateS2;
			try {
				workDateS2 = sdf.parse(workDateE);
				workReport.setWorkTimeE(workDateS2);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
        
        
        page = bisOtherWorkerService.searchOtherWorkerReportHeji(page, workReport);

        return getEasyUIData(page);
    }
    
    /*
     * 杂项作业计件展示
     */
    @RequestMapping(value = "persontypeRuleList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> persontypeRuleList(HttpServletRequest request) {
        Page<BasePersontypeRule> page = getPage(request);
		page.orderBy("id").order(Page.DESC);
		
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		String personTypeName = request.getParameter("personTypeName");
		
		if(personTypeName!=null && !("".equals(personTypeName))){
			filters.add(new PropertyFilter("EQS_personTypeName", personTypeName));
		}	
		page = basePersonTypeRuleService.search(page, filters);
		return getEasyUIData(page);
    }
    
    /**
     * 人员类型计件添加
     */
    @RequestMapping(value = "createPersonTypeRule", method = RequestMethod.GET)
    public String createPersonTypeRule(Model model) {
        model.addAttribute("action", "createPersonTypeRule");
        model.addAttribute("personTypeRule", new BasePersontypeRule());
        User user = UserUtil.getCurrentUser();
        model.addAttribute("user", user.getName());
        model.addAttribute("date", new Date());
        return "cost/jobAllocation/personTypeRuleForm";
    }
    
    /**
     * 添加人员类型计件规则到base_persontype_rule
     */
    @RequestMapping(value = "createPersonTypeRule", method = RequestMethod.POST)
    @ResponseBody
    public String createPersonTypeRule(HttpServletRequest request, @Valid BasePersontypeRule personTypeRule, Model model) {
    	String personTypeName = request.getParameter("aaaName");
    	String personTypeId = request.getParameter("aaaid");
    	personTypeRule.setId(basePersonTypeRuleService.getEntityDao().getSequenceId("SEQ_PERSONTYPE_RULE"));
    	personTypeRule.setPersonTypeName(personTypeName);
    	personTypeRule.setPersonTypeId(ifstr2num(personTypeId)?Integer.parseInt(personTypeId):null);
    	basePersonTypeRuleService.save(personTypeRule);
        return "success";
    }
    
    private boolean ifstr2num(String str){
    	try{
    		Integer.parseInt(str);
    		return true;
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    }
    
    
    /**
     * 修改人员类型计件规则跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "updatePersonTypeRule/{id}", method = RequestMethod.GET)
    public String updatePersonTypeRule(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("personTypeRule", basePersonTypeRuleService.get(id));
        model.addAttribute("action", "updatePersonTypeRule");
        return "cost/jobAllocation/personTypeRuleForm";
    }
    
    
    /**
     * 修改人员类型计件规则
     * @return
     */
    @RequestMapping(value = "updatePersonTypeRule", method = RequestMethod.POST)
    @ResponseBody
    public String updatePersonTypeRule(HttpServletRequest request,@Valid @ModelAttribute @RequestBody BasePersontypeRule ruleObj, Model model) {
    	
    	String personTypeName = request.getParameter("aaaName");
    	String personTypeId = request.getParameter("aaaid");
    	
    	ruleObj.setPersonTypeName(personTypeName);
    	ruleObj.setPersonTypeId(ifstr2num(personTypeId)?Integer.parseInt(personTypeId):null);
    	basePersonTypeRuleService.update(ruleObj);
        return "success";
    }
    
    /**
     * @Description: 删除人员类型计件规则
     */
    @RequestMapping(value = "deletePersonTypeRule/{ids}")
    @ResponseBody
    public String deletePersonTypeRule(@PathVariable("ids") List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
        	basePersonTypeRuleService.delete((ids.get(i)));
        }
        return "success";
    }
    
    /**
     * 查当前要添加的作业人员的系数
     * @return ratio
     */
    @RequestMapping(value = "checkRatioTotal", method = RequestMethod.POST)
    @ResponseBody
    public String checkRatioTotal(HttpServletRequest request) {
    	
    	String personTypeId = request.getParameter("personTypeId");
    	BasePersontypeRule bptr = basePersonTypeRuleService.find("personTypeId", Integer.valueOf(personTypeId));
    	
        return bptr.getRatio()+"";
    }
    
    /**
     * @param request
     * @param response
     * @throws Exception
     * @throws
     * @Description: 导出excel
     * @date 2017年10月26日 下午5:22:55
     */
    @RequestMapping(value = "export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Page<WorkReport> page = getPage(request);
        WorkReport workReport = new WorkReport();
        parameterReflect.reflectParameter(workReport, request);//转换对应实体类参数
        if(workReport.getWorkTimeS()==null && workReport.getWorkTimeE()==null && workReport.getFirst().equals("1")){
        	Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date zero = calendar.getTime();
            workReport.setWorkTimeS(zero);
       	    workReport.setWorkTimeE(DateUtils.addDay(zero, 1));
        }else if(workReport.getWorkTimeE()!=null){
        	workReport.setWorkTimeE(DateUtils.addDay(workReport.getWorkTimeE(), 1));
        }
        page = pieceworkRuleService.searchWorkReport(page, workReport);
        
        String formatFileName = URLEncoder.encode("计件规则报表" + ".xls", "UTF-8");

        ExcelUtil excelUtil = new ExcelUtil();

        String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
        String srcPath = null;
        String desPath = null;
        List<WorkReport> workReportList = page.getResult();
        
        if (filePath == null || "".equals(filePath)) {
        	
        	filePath = request.getSession().getServletContext().getRealPath("/");
        	
        	srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\workReport.xls";
        	desPath = filePath + "WEB-INF\\classes\\excelpost\\workReport.xls";
        }else{
        	
        	srcPath = filePath + "exceltemplate" + File.separator + "workReport.xls";
        	desPath = filePath + "excelpost" + File.separator + "workReport.xls";
        }
        
        excelUtil.setSrcPath(srcPath);
        excelUtil.setDesPath(desPath);
        excelUtil.setSheetName("Sheet1");
        excelUtil.getSheet();
        int myNum = 7;
        if(workReportList!=null && workReportList.size()>0){
        	for(int i=0;i<workReportList.size();i++){
        		WorkReport wr = workReportList.get(i);
        		excelUtil.setCellStrValue(myNum + i , 0, wr.getMan()!=null?wr.getMan():"");
        		excelUtil.setCellStrValue(myNum + i , 1, wr.getPersonType()!=null?wr.getPersonType():"");
        		excelUtil.setCellDoubleValue(myNum + i , 2, wr.getWorkload()!=null?wr.getWorkload():0);
        		excelUtil.setCellStrValue(myNum + i , 3, wr.getRuleJobType()!=null?wr.getRuleJobType():"");
        	}
        	excelUtil.exportToNewFile();
        }

        FileInputStream in = new FileInputStream(new File(desPath));

        int len = 0;

        byte[] buffer = new byte[1024];

        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\""); //设定输出文件头
        response.setContentType("application/msexcel"); //定义输出类型

        OutputStream out = response.getOutputStream();

        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        if (null != in) {
            in.close();
        }

        if (null != out) {
            out.close();
        }
        
    }
    
    
}
