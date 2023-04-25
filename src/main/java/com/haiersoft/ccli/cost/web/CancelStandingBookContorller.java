package com.haiersoft.ccli.cost.web;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.BisVerifiBook;
import com.haiersoft.ccli.cost.service.BisVerifiBookInfoService;
import com.haiersoft.ccli.cost.service.BisVerifiBookService;
import com.haiersoft.ccli.cost.service.StandingBookService;
/**
 * 台账核销
 * @author LZG
 *
 */
@Controller
@RequestMapping("cost/cancelafter")
public class CancelStandingBookContorller extends BaseController {
	@Autowired
	private StandingBookService standingBookService;//台账
	@Autowired
	private BisVerifiBookService bisVerifiBookService;//核销
	@Autowired
	private BisVerifiBookInfoService  bisVerifiBookInfoService;//核销明细
	@RequestMapping(value = "slist", method = RequestMethod.GET)
	/**
	 * 应收跳转
	 * @return
	 */
	public String toSList() {
		return "cost/cancelAfterBook/ysList";
	}
	/**
	 * 应付跳转
	 * @return
	 */
	@RequestMapping(value = "flist", method = RequestMethod.GET)
	public String toFList() {
		return "cost/cancelAfterBook/yfList";
	}
	/**
	 * 列表页面table获取应付对账内容
	 * @param request
	 * @return
	 */
	@RequestMapping(value="listjson/{ntype}",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getJsonData(@PathVariable("ntype") Integer ntype,HttpServletRequest request) {
		Map<String,Object> returnMap =null;
		int pageNumber=Integer.valueOf(request.getParameter("pageNumber")!=null?request.getParameter("pageNumber").toString():"1");
		int pageSize=Integer.valueOf(request.getParameter("pageSize")!=null?request.getParameter("pageSize").toString():"20");
		int receiver=0;
		try {
			receiver = Integer.valueOf(request.getParameter("receiver")!=null && !"".equals(request.getParameter("receiver").toString()) ?request.getParameter("receiver").toString():"0");//客户
		} catch (NumberFormatException e) {
			//获取的查询客户id不能转为int类型
			return  new HashMap<String,Object>();
		}
		int nGZ=Integer.valueOf(request.getParameter("ngz")!=null?request.getParameter("ngz").toString():"1");//查询是否挂账 2挂账，1 所有
		String strTime=request.getParameter("strTime")!=null?request.getParameter("strTime").toString():"";//账单年月开始
		String endTime=request.getParameter("endTime")!=null?request.getParameter("endTime").toString():"";
		if(receiver>0){
			returnMap=standingBookService.getCancelParameterList(receiver,ntype, strTime, endTime,nGZ, pageNumber, pageSize);
		}
		if(returnMap==null){
			returnMap = new HashMap<String,Object>();
		}
		return  returnMap;
	}
	/**
	 * 跳转到明细页面
	 * @param customId 客户id
	 * @param ym 账单年月
	 * @param ntype 类型
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bkinfo/{customId}/{ym}/{ntype}", method = RequestMethod.GET)
	public String bkinfo(@PathVariable("customId") String customId,@PathVariable("ym") String ym,@PathVariable("ntype") Integer ntype,Model model) {
		model.addAttribute("customId", customId);
		model.addAttribute("ym", ym);
		model.addAttribute("ntype", ntype);
		return "cost/cancelAfterBook/bkinfo";
	} 
	
	/***
	 * 异步获取账单明细
	 * @param request
	 * @return
	 */
	@RequestMapping(value="jsonbkinfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getInfoJsonData(HttpServletRequest request) {
		Map<String,Object> returnMap =null;
		int pageNumber=Integer.valueOf(request.getParameter("pageNumber")!=null?request.getParameter("pageNumber").toString():"1");
		int pageSize=Integer.valueOf(request.getParameter("pageSize")!=null?request.getParameter("pageSize").toString():"20");
		int receiver=0;
		try {
			receiver = Integer.valueOf(request.getParameter("receiver")!=null?request.getParameter("receiver").toString():"0");
		} catch (NumberFormatException e) {
			//获取的查询客户id不能转为int类型
			return  new HashMap<String,Object>();
		}
		int ntype=Integer.valueOf(request.getParameter("ntype")!=null?request.getParameter("ntype").toString():"1");
		String ymTime=request.getParameter("ymTime")!=null?request.getParameter("ymTime").toString():"";
		returnMap=standingBookService.getCancelParameterInfoList(receiver, ymTime, ntype, pageNumber, pageSize);
		if(returnMap==null){
			returnMap = new HashMap<String,Object>();
		}
		return  returnMap;
	}
	/**
	 * 打开添加弹窗
	 * @return
	 */
	@RequestMapping(value = "open/{ntype}/{ctype}", method = RequestMethod.GET)
	public String openAdd(@PathVariable("ntype") Integer ntype,@PathVariable("ctype") Integer ctype,Model model) {
		model.addAttribute("ntype", ntype);
		model.addAttribute("ctype", ctype);
		return "cost/cancelAfterBook/addForm";
	}
	/**
	 * 异步获取核销银行信息
	 * @param request
	 * @return
	 */
 
	@RequestMapping(value="banksjson",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getBankJsonData(HttpServletRequest request) {
		List<Map<String, Object>> returnList =null;
		returnList=bisVerifiBookService.getBanklist();
		if(returnList==null){
			returnList = new ArrayList<Map<String, Object>>();
		}
		return  returnList;
	}
	/**
	 * 异步获取选中核销总金额
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getsummoneyjson",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getSumMoney(HttpServletRequest request) {
		List<Map<String, Object>> returnList =null;
		String ids=request.getParameter("ids")!=null?request.getParameter("ids").toString():"";
		if(!"".equals(ids.trim()) && ids.trim().lastIndexOf(",")==(ids.length()-1) ){
			ids+="0";
		}
		returnList=standingBookService.countSumMoney(ids);
		if(returnList!=null && returnList.size()>0){
			return returnList.get(0);
		}
		return  new  HashMap<String, Object>();
	}
	
	/**
	 * 添加核销信息
	 */
	 
	@RequestMapping(value = "addhx", method = RequestMethod.POST)
	@ResponseBody
	public String addHx(@Valid BisVerifiBook obj, Model model) {
		String retStr = "error"; 
		if(obj.getIds()!=null && !"".equals(obj.getIds()) && obj.getSumMoney()!=null && obj.getSumMoney()>0){
			if(obj.getcType()!=null && 1==obj.getcType()){
				retStr=this.bisVerifiBookService.saveVerifiBook(obj);
			}else{
				retStr=this.bisVerifiBookService.saveHx(obj);
			}
		}
		return retStr;
	}
	
	/**
	 * 打开添加弹窗
	 * @return
	 */
	@RequestMapping(value = "hxlist", method = RequestMethod.GET)
	public String hxList() {
		return "cost/cancelAfterBook/hxList";
	}
	
/**
	@RequestMapping(value="hxsjson",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getHxJsonData(HttpServletRequest request) {
		Map<String,Object> returnMap =null;
		int pageNumber=Integer.valueOf(request.getParameter("pageNumber")!=null?request.getParameter("pageNumber").toString():"1");
		int pageSize=Integer.valueOf(request.getParameter("pageSize")!=null?request.getParameter("pageSize").toString():"20");
		int receiver=Integer.valueOf(request.getParameter("receiver")!=null && !"".equals(request.getParameter("receiver").toString()) ?request.getParameter("receiver").toString():"0");//客户
		int ntype=Integer.valueOf(request.getParameter("ntype")!=null?request.getParameter("ntype").toString():"1");//类型应收1应付2
		String pzNum=request.getParameter("pzNum")!=null?request.getParameter("pzNum").toString():"";//凭证号
		//returnMap=standingBookService.getCancelParameterList(receiver,ntype, strTime, endTime, pageNumber, pageSize);
		if(returnMap==null){
			returnMap = new HashMap<String,Object>();
		}
		return  returnMap;
	}
	*/
	
	/*
	 * 列表页面table获取json
	 */
	@RequestMapping(value = "hxsjson", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getHxJsonData(HttpServletRequest request) {
		Page<BisVerifiBook> page = getPage(request);
		page.orderBy("crTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = bisVerifiBookService.search(page, filters);
		return getEasyUIData(page);
	}
	/**
	 * 取消核销
	 * @param num 核销编号
	 * @return
	 */
	@RequestMapping(value = "delete/{num}")
	@ResponseBody
	public String delete(@PathVariable("num") String num) {
		String retStr = "error";
		retStr=bisVerifiBookService.delHx(num);
		return retStr;
	}
	/**
	 * 跳转到核销明细页面
	 * @param num 核销编号
	 * @return
	 */
	@RequestMapping(value = "hxinfo/{num}", method = RequestMethod.GET)
	public String bkinfo(@PathVariable("num") String num,Model model) {
		model.addAttribute("num", num);
		return "cost/cancelAfterBook/hxinfo";
	} 
	
	/***
	 * 异步获取账单明细
	 * @param request
	 * @return
	 */
	@RequestMapping(value="jsonhxinfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getHxInfoJsonData(HttpServletRequest request) {
		Map<String,Object> returnMap =null;
		int pageNumber=Integer.valueOf(request.getParameter("pageNumber")!=null?request.getParameter("pageNumber").toString():"1");
		int pageSize=Integer.valueOf(request.getParameter("pageSize")!=null?request.getParameter("pageSize").toString():"20");
		String num=request.getParameter("num")!=null?request.getParameter("num").toString():"";
		returnMap=bisVerifiBookInfoService.getBisVerifiBookInfoList(num, pageNumber, pageSize);
		if(returnMap==null){
			returnMap = new HashMap<String,Object>();
		}
		return  returnMap;
	}
	
	
	@RequestMapping(value = "postjd/{code}/{ntype}/{ctype}")
	@ResponseBody
	public String delete(@PathVariable("code") String code,@PathVariable("ntype")  Integer ntype,@PathVariable("ctype") Integer ctype) {
		String retStr = "error";
		if(code!=null && !"".equals(code) && ntype!=null && ntype>0 && ctype!=null && ctype>0){
			retStr=bisVerifiBookService.saveSFToJDGX(code, ctype, ntype, ctype);
		}
		return retStr;
	}
}
