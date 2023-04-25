package com.haiersoft.ccli.wms.web;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.*;
import com.haiersoft.ccli.wms.service.*;

//import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 闸口管理
 */
@Controller
@RequestMapping("wms/gate")
public class GateController extends BaseController {

    //@Autowired
    //private GateService gateService;

    @Autowired
    private InOutService inOutService;

    @Autowired
    private PresenceBoxService presenceBoxService;

    @Autowired
    private GateCarService gateCarService;

    @Autowired
    private InOutHistoryService inOutHistoryService;

    @Autowired
    private PresenceBoxHistoryService presenceBoxHistoryService;
    
    @Autowired
    private ASNService asnService;
    @Autowired
    private ASNInfoService asnInfoService;
    @Autowired
    private LoadingInfoService loadingInfoService;
    @Autowired
    private LoadingOrderService loadingOrderService;
    @Autowired
    private LoadingOrderInfoService loadingOrderInfoService;
    @Autowired
    private LuodiService luodiService;
    @Autowired
    private CallCarService callCarService;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(Model model) {
    	model.addAttribute("fullip", PropertiesUtil.getPropertiesByName("fullip", "application"));
        return "wms/gate/gateNew";
    }
    
    @RequestMapping(value = "inOutList", method = RequestMethod.GET)
    public String inOutList() {
        return "wms/gate/inOutList";
    }
    @RequestMapping(value = "boxList", method = RequestMethod.GET)
    public String boxList() {
        return "wms/gate/boxList";
    }
    
    @RequestMapping(value = "fullScreen", method = RequestMethod.GET)
    public String fullScreen() {
        return "wms/gate/gateFullScreen";
    }
    
    @RequestMapping(value = "platform/{id}", method = RequestMethod.GET)
    public String platform(Model model,@PathVariable("id") String id) {
    	BisGateCar car = gateCarService.get(Integer.valueOf(id));
    	model.addAttribute("car", car);
        return "wms/gate/gatewithplat";
    }
    @RequestMapping(value = "page")
    @ResponseBody
    public Map<String, Object> page(HttpServletRequest request) {
    	List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        Page<BisGateCar> page = getPage(request);
        page = gateCarService.search(page, filters);
        return getEasyUIData(page);
    }
    
    @RequestMapping(value = "fullJson")
    @ResponseBody
    public Map<String, Object> fullJson(HttpServletRequest request) {
    	List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        Page<BisGateCar> page = getPage(request);
        page = gateCarService.search(page, filters);
        return getEasyUIData(page);
    }
    
    //进出场记录
    @RequestMapping(value = "inOutJson")
    @ResponseBody
    public Map<String, Object> inOutJson(HttpServletRequest request) {
    	String ifH=request.getParameter("ifH");
    	if(StringUtils.isNull(ifH) || ifH.equals("0")){
	    	List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
	        Page<BisInOut> page = getPage(request);
	        page = inOutService.search(page, filters);
	        return getEasyUIData(page);
    	}else{
    		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
	        Page<BisInOutHistory> page = getPage(request);
	        page = inOutHistoryService.search(page, filters);
	        return getEasyUIData(page);
    	}
    }
    
    //在场箱记录
    @RequestMapping(value = "boxJson")
    @ResponseBody
    public Map<String, Object> boxJson(HttpServletRequest request) {
    	String ifH=request.getParameter("ifH");
    	if(StringUtils.isNull(ifH) || ifH.equals("0")){
	    	List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
	        Page<BisPresenceBox> page = getPage(request);
	        page = presenceBoxService.search(page, filters);
	        return getEasyUIData(page);
    	}else{
    		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
	        Page<BisPresenceBoxHistory> page = getPage(request);
	        page = presenceBoxHistoryService.search(page, filters);
	        return getEasyUIData(page);
    	}
    }

    /**
     * 特殊入闸添加或更新(BisGateCar,BisInOut,BisPresenceBox)
     */
    @RequestMapping(value = "submitTsr", method = RequestMethod.POST)
    @ResponseBody
    public String mergeTsrForm(@ModelAttribute @RequestBody BisGateCar gateEntity) {
    	User user=UserUtil.getCurrentUser();
        Date now = DateUtils.getNow();
        //判断进出场记录表中是否有重复车牌号
        String carNo=gateEntity.getCarNum();
        List<PropertyFilter> filters =new ArrayList<PropertyFilter>();
        PropertyFilter filter = new PropertyFilter("EQS_carNum", carNo);
        filters.add(filter);
        List<BisInOut> inOutList = inOutService.search(filters);
        if(!inOutList.isEmpty()){
        	return "overCar";
        }
        
        //判断在场箱表中是否有重复箱号
        String ctnNo=gateEntity.getCtnNum();
        List<PropertyFilter> filters2 =new ArrayList<PropertyFilter>();
        PropertyFilter filter2 = new PropertyFilter("EQS_ctnNum", ctnNo);
        filters2.add(filter2);
        List<BisPresenceBox> boxList = presenceBoxService.search(filters2);
        if(!boxList.isEmpty()){
        	return "overBox";
        }
        // 车辆信息
        gateEntity.setCreateDate(now); 
        gateEntity.setCreateUser(user.getName());
        gateEntity.setJobType("1");
        gateEntity.setBisType("4");
        // 进出场信息
        BisInOut inOut = new BisInOut();
        BeanUtils.copyProperties(gateEntity, inOut);//复制
        // 在场箱信息
        BisPresenceBox presenceBox = new BisPresenceBox();
        BeanUtils.copyProperties(gateEntity, presenceBox);//复制
        presenceBox.setState("1");
        
        gateCarService.save(gateEntity);
        inOutService.save(inOut);
        presenceBoxService.save(presenceBox);
        return SUCCESS;
    }
    /**
     * 添加或更新(BisGateCar,BisInOut,BisPresenceBox)
     */
    @RequestMapping(value = "submitBis", method = RequestMethod.POST)
    @ResponseBody
    public String mergeForm(@ModelAttribute @RequestBody BisGateCar gateEntity) {
    	User user=UserUtil.getCurrentUser();
        Date now = DateUtils.getNow();
        //判断进出场记录表中是否有重复车牌号
        String carNo=gateEntity.getCarNum();
        List<PropertyFilter> filters =new ArrayList<PropertyFilter>();
        PropertyFilter filter = new PropertyFilter("EQS_carNum", carNo);
        filters.add(filter);
        List<BisInOut> inOutList = inOutService.search(filters);
        if(!inOutList.isEmpty()){
        	return "overCar";
        }
        
        //判断在场箱表中是否有重复箱号
        String ctnNo=gateEntity.getCtnNum();
        List<PropertyFilter> filters2 =new ArrayList<PropertyFilter>();
        PropertyFilter filter2 = new PropertyFilter("EQS_ctnNum", ctnNo);
        filters2.add(filter2);
        List<BisPresenceBox> boxList = presenceBoxService.search(filters2);
        if(!boxList.isEmpty()){
        	return "overBox";
        }
        // 车辆信息
        gateEntity.setCreateDate(now); 
        gateEntity.setCreateUser(user.getName());
        gateEntity.setJobType("1");
        // 进出场信息
        BisInOut inOut = new BisInOut();
        BeanUtils.copyProperties(gateEntity, inOut);//复制
        // 在场箱信息
        BisPresenceBox presenceBox = new BisPresenceBox();
        BeanUtils.copyProperties(gateEntity, presenceBox);//复制
        presenceBox.setState("1");
        
        gateCarService.save(gateEntity);
        inOutService.save(inOut);
        presenceBoxService.save(presenceBox);
        return SUCCESS;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteForm(String id) {
        BisGateCar car = gateCarService.get(Integer.valueOf(id));
        if(!StringUtils.isNull(car.getCtnNum())){
        	if(luodiService.getLuodiInfoByCtn(car.getCtnNum()).isEmpty()){
        		presenceBoxService.deleteList(car.getCtnNum());
        	}else{
        		return "ld";
        	}
        }
        inOutService.deleteList(car.getCarNum());
        gateCarService.delete(car);
        return SUCCESS;
    }
    
	/**
	 * 修改车牌号跳转 
	 * 
	 * @param model
	 */
	@RequestMapping(value = "updateCarNo/{id}", method = RequestMethod.GET)
	public String createForm(Model model,@PathVariable("id") String id) {
		BisGateCar car = gateCarService.get(Integer.valueOf(id));
		model.addAttribute("bisGateCar", car);
		model.addAttribute("action","updateCarNo");
		return "wms/gate/gateCarUpdate";
	}
    
	@RequestMapping(value = "updateCarNo", method = RequestMethod.POST)
	@ResponseBody
	public String create(Model model, HttpServletRequest request) {
		 String gateCarNoNew = request.getParameter("gateCarNoNew");
		 String id = request.getParameter("id");
		 BisGateCar car = gateCarService.get(Integer.valueOf(id));
	        if(!StringUtils.isNull(car.getCtnNum())){
	        	List<BisLuodiInfo> bisluodiInfoList = luodiService.getLuodiInfoByCtnWithoutState(car.getCtnNum());
	        	if( !bisluodiInfoList.isEmpty()){
	        		for(Iterator<BisLuodiInfo> ccar = bisluodiInfoList.iterator();ccar.hasNext();){
	        			BisLuodiInfo bisluodiInfo = ccar.next();
	        			bisluodiInfo.setCarNum(gateCarNoNew);
	        			luodiService.update(bisluodiInfo);
	        		}
	        	}
	        	BisPresenceBox box = presenceBoxService.find("ctnNum", car.getCtnNum());
	    		box.setCarNum(gateCarNoNew);
	        	presenceBoxService.update(box);
	        }
        BisInOut inOut=inOutService.find("carNum", car.getCarNum());
        inOut.setCarNum(gateCarNoNew);
    	inOutService.update(inOut);
    	car.setCarNum(gateCarNoNew);
        gateCarService.update(car);
		return "success";
	}

    /**
     * 寻找业务类型
     * @return
     */
    @RequestMapping(value = "searchBis", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchBis(HttpServletRequest request) {
    	String ctnNum=request.getParameter("ctnNum");
    	String carNum=request.getParameter("carNum");
    	Map<String,Object> resultMap=new HashMap<String,Object>();
    	resultMap.put("str", "empty");
    	List<PropertyFilter> asnF = new ArrayList<PropertyFilter>();
    	asnF.add(new PropertyFilter("EQS_ctnNum", ctnNum));
    	asnF.add(new PropertyFilter("INAS_asnState", new String[] {"1", "2"}));
		List<BisAsn> asnList = asnService.findByF(asnF);
        if(!asnList.isEmpty()){
        	BisAsn asn = asnList.get(0);
        	List<BisAsnInfo> infoList = asnInfoService.getASNInfoList(asn.getAsn());
        	Double entNum=0d;
        	for(BisAsnInfo info:infoList){
        		entNum+=info.getPiece();
        	}
        	resultMap.put("str", "success");
        	resultMap.put("type", "ent");
        	resultMap.put("billNum", asn.getBillNum());
        	resultMap.put("asn", asn.getAsn());
        	resultMap.put("clientId", asn.getStockIn());
        	resultMap.put("clientName", asn.getStockName());
        	resultMap.put("entNum", entNum);
        	return resultMap;
        }else{
        	List<PropertyFilter> orderF = new ArrayList<PropertyFilter>();
        	orderF.add(new PropertyFilter("EQS_carNum", carNum));
        	orderF.add(new PropertyFilter("INAS_orderState", new String[] {"1", "2", "3"}));
    		List<BisLoadingOrder> orderList = loadingOrderService.search(orderF);
    		if(!orderList.isEmpty()){
    			BisLoadingOrder order = orderList.get(0);
    			List<BisLoadingOrderInfo> infoList=loadingOrderInfoService.getInfoList(order.getOrderNum());
    			Double outNum=0d;
    			for(BisLoadingOrderInfo info:infoList){
    				outNum += info.getPiece();
    			}
    			resultMap.put("str", "success");
    			resultMap.put("type", "out");
    			resultMap.put("outNum", outNum);
    			resultMap.put("clientId", order.getStockIn());
    			resultMap.put("clientName", order.getStockName());
    			List<BisLoadingInfo> loadingList = loadingInfoService.getLoadingByOrder(order.getOrderNum());
    			if(loadingList.isEmpty()){
    				resultMap.put("loadingNum", "");
    			}else{
    				resultMap.put("loadingNum", loadingList.get(0).getLoadingTruckNum());
    			}
    			return resultMap;
    		}else{
    			return resultMap;
    		}
        }

        
    }

    /**
     * 出闸操作
     */
    @RequestMapping(value = "leave", method = RequestMethod.POST)
    @ResponseBody
    public String outGate(String id) {
    	//找到车辆信息记录
    	if(null==id||"".equals(id)){
    		return "记录发生变化请刷新页面在进行操作！";
    	}
    	Date now = new Date();
        BisGateCar gateCar = gateCarService.get(Integer.valueOf(id));
        if(!StringUtils.isNull(gateCar.getCarNum())){
        	List<PropertyFilter> search= new ArrayList<PropertyFilter>();
        	search.add(new PropertyFilter("EQS_carNum",gateCar.getCarNum()));
        	//根据车牌号找到进场记录
        	List<BisInOut> inoutList=inOutService.search(search);
            if(inoutList!=null&&inoutList.size()>0){
            	for (int i = 0; i < inoutList.size(); i++) {
            		BisInOut inOut=inoutList.get(i);
            		BisInOutHistory inOutHistory = new BisInOutHistory();
                    BeanUtils.copyProperties(inOut,inOutHistory);//复制
                    inOutHistory.setId(null);
                    inOutHistory.setOutDate(now);
                    inOutHistory.setJobType("2");
                    inOutHistoryService.save(inOutHistory);
                    inOutService.delete(inOut);
				}
            }
        }
        if(!StringUtils.isNull(gateCar.getCtnNum())){
        	List<PropertyFilter> search= new ArrayList<PropertyFilter>();
        	search.add(new PropertyFilter("EQS_ctnNum",gateCar.getCtnNum()));
        	List<BisPresenceBox> boxList=presenceBoxService.search(search);
        	if(boxList != null&&boxList.size()>0){
        		for (int i = 0; i < boxList.size(); i++) {
        			BisPresenceBox box=boxList.get(i);
        			BisPresenceBoxHistory boxHistory=new BisPresenceBoxHistory();
                	BeanUtils.copyProperties(box,boxHistory);//复制
                	boxHistory.setOutDate(now);
                	boxHistory.setState("3");
                	boxHistory.setId(null);
                	//删除入场箱记录；保存出场箱记录
                	presenceBoxHistoryService.save(boxHistory);
                	presenceBoxService.delete(box);
				}
            }
        }
        //删除车辆信息、入场纪录；保存出场纪录
        gateCarService.delete(gateCar);
        return SUCCESS;
    }
    
    
    /**
     * 判断作业任务是否完成
     */
    @RequestMapping(value = "ifEnd", method = RequestMethod.POST)
    @ResponseBody
    public String ifEnd(String id) {
    	//找到车辆信息记录
    	if(null==id||"".equals(id)){
    		return "记录发生变化请刷新界面进行重新操作！";
    	}
        BisGateCar gateCar = gateCarService.get(Integer.valueOf(id));
        if(null==gateCar){
        	return "记录发生变化请刷新界面进行重新操作！";
        }
        if(null==gateCar.getBisType()){
        	return SUCCESS;
        }
        if("1".equals(gateCar.getBisType())){
        	 List<PropertyFilter> filters =new ArrayList<PropertyFilter>();
             PropertyFilter filter = new PropertyFilter("EQS_billNum", gateCar.getBillNum());
             PropertyFilter filter2 = new PropertyFilter("EQS_ctnNum", gateCar.getCtnNum());
             PropertyFilter filter3 = new PropertyFilter("INAS_asnState", new String[]{"3","4"});
             filters.add(filter);
             filters.add(filter2);
             filters.add(filter3);
             List<BisAsn> asnList = asnService.search(filters);
             if(asnList.isEmpty()){
            	 return "ent";
             }else{
            	 return "success";
             }
        }else if("2".equals(gateCar.getBisType())){
        	 if(null==gateCar.getLoadingNum()){
        		 return "out";
        	 }
        	 List<PropertyFilter> filters =new ArrayList<PropertyFilter>();
             PropertyFilter filter = new PropertyFilter("EQS_carNo", gateCar.getCarNum());
             PropertyFilter filter2 = new PropertyFilter("EQS_loadingTruckNum", gateCar.getLoadingNum());
             PropertyFilter filter3 = new PropertyFilter("INAS_loadingState", new String[]{"2","3","4","5","6"});
             filters.add(filter);
             filters.add(filter2);
             filters.add(filter3);
             List<BisLoadingInfo> orderList = loadingInfoService.search(filters);
             if(orderList.isEmpty()){
            	 return "out";
             }else{
            	 return "success";
             }
        }else{
        	return SUCCESS;
        }
    }
    
	/**
	 * 添加月台口
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "addplat", method = RequestMethod.POST)
	@ResponseBody
	public String addplat(HttpServletRequest request) {
		String id=request.getParameter("id");
		String platform = request.getParameter("platform");
		String platformtwo = request.getParameter("platformtwo");
		BisGateCar car = gateCarService.get(Integer.valueOf(id));
		car.setPlatform(platform);
		car.setPlatformtwo(platformtwo);
		gateCarService.update(car);
		return "success";
	}
	
	/**
	 * 呼叫车辆
	 * @param id
	 * @return
	 */
	 @RequestMapping(value = "callCar", method = RequestMethod.POST)
	 @ResponseBody
	 public String callCar(String carNum,String platform,String platformtwo) {
		 CallCar car = callCarService.get(carNum);
		 //Session session = callCarService.getEntityDao().getSession();
		 if(null==car){
			 //如果月台口2为null，正常呼叫月台口1，否则呼叫月台口2.
			 if("".equals(platformtwo) || platformtwo == null){//car为空 月台口2为null，正常呼叫1
				 car = new CallCar(carNum,platform,3);
				 callCarService.save(car);
				 return "success";
			 }else{//car为空，月台口2不为空 ，呼叫月台口2
				 car = new CallCar(carNum,platformtwo,3);
				 callCarService.save(car);
				 return "success";
			 }
			 
			 
		 }else{
			 if("".equals(platformtwo) || platformtwo == null){//car不为空，月台口2为空
				 if(car.getPlatform().equals(platform)){//car有呼叫1的任务，改三遍更新
					 car.setTime(3);
					 callCarService.update(car);
					 return "success";
				 }else{//car没有呼叫1的任务，原有的是呼叫2的任务，把1新加3遍\\yhn20180115改
					 CallCar car1 = new CallCar(carNum,platform,3);
					 callCarService.save(car1);
					 return "success";
				 }
			 }else{//car不空 月台口2不空
				 if(car.getPlatform().equals(platformtwo)){//原本就是呼叫2的任务 改三遍更新
					 car.setTime(3);
					 callCarService.update(car);
					 return "success";
				 }else{//原来是呼叫1的任务 //20180115 新加月台口2呼叫
					 CallCar car1 = new CallCar(carNum,platformtwo,3);
					 callCarService.save(car1);
					 return "success";
				 }
			 }
		 }
	 }
	 
	 
	 	/**
		 * 获取呼叫车辆列表
		 * @param id
		 * @return
		 */
		 @RequestMapping(value = "callCarList", method = RequestMethod.POST)
		 @ResponseBody
		 public List<CallCar> callCarList() {
			 callCarService.deleteZero();
			 List<CallCar> carList=callCarService.getAll();
			 return carList;
		 }
		 
		 
		 	/**
			 * 减少呼叫剩余次数
			 * @param id
			 * @return
			 */
			 @RequestMapping(value = "reduce", method = RequestMethod.POST)
			 @ResponseBody
			 public String reduce(String carNum) {
				 CallCar car = callCarService.get(carNum);
				 if(car.getTime()<2){
					 callCarService.delete(car);
				 }else{
					 car.setTime(car.getTime()-1);
					 callCarService.update(car);
				 }
				 return "success";
			 }
}
