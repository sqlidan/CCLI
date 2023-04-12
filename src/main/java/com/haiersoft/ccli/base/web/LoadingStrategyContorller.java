package com.haiersoft.ccli.base.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.base.entity.BaseLoadingStrategy;
import com.haiersoft.ccli.base.service.LoadingStrategyService;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.TransferService;
/**
 * 拣货策略controller
 * @author lzg
 * @date 2016年3月18日
 */
@Controller
@RequestMapping("base/stategy")
public class LoadingStrategyContorller extends BaseController {
	@Autowired
	private LoadingStrategyService loadingStartegyService;
	@Autowired
	private LoadingInfoService loadingInfoService;//装车单
	@Autowired
	private TransferService transferService;//货转单
	/**
	 * 添加策略跳转
	 */
	@RequiresPermissions("base:stategy:view")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		List<Map<String,Object>> getList=loadingStartegyService.findList();
		if(getList==null ){
			getList=new ArrayList<Map<String,Object>>();
		}
		model.addAttribute("list", getList);
		model.addAttribute("action", "create");
		return "base/strategy/strategyForm";
	}
	/**
	 * 生成装车单选择策略跳转
	 */
	@RequestMapping(value = "check/{ordnum}", method = RequestMethod.GET)
	public String checkForm(@PathVariable("ordnum") String ordnum,Model model) {
		List<Map<String,Object>> getList=loadingStartegyService.findList();
		if(getList==null ){
			getList=new ArrayList<Map<String,Object>>();
		}
		model.addAttribute("ordnum", ordnum);
		model.addAttribute("list", getList);
		model.addAttribute("action", "create");
		return "wms/loadingorder/checkStrategyForm";
	}
	/**
	 * 执行添加策略
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> create(@Valid BaseLoadingStrategy strategy, Model model) {
		Map<String,Object> rets=loadingStartegyService.saveStrategy(strategy);
		if("success".equals(rets.get("retStr")) && strategy.getOrderNum()!=null && !"".equals(strategy.getOrderNum())){
			if("O".equals(strategy.getOrderNum().substring(0,1))){
				rets=loadingInfoService.createTruck(strategy.getOrderNum(), rets.get("id").toString());
			}else if("T".equals(strategy.getOrderNum().substring(0,1))){
				rets=transferService.createTruck(strategy.getOrderNum(), rets.get("id").toString());
			}
		}
		return rets;
	}
	
	/**
	 * 根据策略编码获取策略详情
	 * @param request
	 * @return
	 */
	@RequestMapping(value="jsonlist", method = RequestMethod.POST)
	@ResponseBody
	public List<BaseLoadingStrategy> getSearchData(HttpServletRequest request) {
		List<BaseLoadingStrategy> retList= new ArrayList<BaseLoadingStrategy>();
		if(request.getParameter("strategynum")!=null){
			String linkNum=request.getParameter("strategynum").toString();//策略编码
			retList=loadingStartegyService.findStrategyObjList(Integer.valueOf(linkNum));
		}
		return retList;
	}
	
	/**
	 * 根据策略编码删除策略详情
	 * @param id 策略编码
	 * @return
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public Map<String,Object>  delete(@PathVariable("id") Integer code) {
		Map<String,Object> rets=new HashMap<String,Object>();
		if(code>0){
			rets=loadingStartegyService.deleteStrategy(code);
		}
		return rets;
	}
}
