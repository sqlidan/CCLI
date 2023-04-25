package com.haiersoft.ccli.cost.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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





import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.entity.LoadingTeamDaywork;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.TaxRateService;
import com.haiersoft.ccli.cost.service.LoadingTeamDayworkService;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * 装卸队日工统计controller
 * @author mzy
 * @date 2016年6月29日
 */
@Controller
@RequestMapping("cost/loadingTeam")
public class LoadingTeamDayworkContorller extends BaseController {
	@Autowired
	private LoadingTeamDayworkService loadingTeamDayworkService;
	@Autowired
	private StandingBookService standingBookService;//台账
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;//台账
	@Autowired
	private TaxRateService taxRateService;//汇率
	
	
	/*跳转列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "cost/loadingTeam/loadingTeamDaywork";
	}
	
	/*
	 * 列表页面table获取json
	 * */
	@RequestMapping(value="listjson",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<LoadingTeamDaywork> page = getPage(request);
		page.orderBy("createTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQI_delFlag", String.valueOf(0));
		filters.add(filter);
		page = loadingTeamDayworkService.search(page, filters);
		return getEasyUIData(page);
	}
	 
	/**
	 * 添加日工跳转跳转
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("action", "create");
		return "cost/loadingTeam/dayworkForm";
	}
	
	/**
	 * 添加
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid LoadingTeamDaywork daywork, Model model) {
		daywork.setId(loadingTeamDayworkService.getDayworkId());//id
		User user = UserUtil.getCurrentUser();
		if(user!=null){
			daywork.setCreater(user.getName());
		}
		daywork.setCreateTime(new Date());
		daywork.setDelFlag(0);
		daywork.setState(0);
		loadingTeamDayworkService.save(daywork);
		return "success";
	}
	
	/**
	 * 修改日工跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("daywork", loadingTeamDayworkService.get(id));
		model.addAttribute("action", "update");
		return "cost/loadingTeam/dayworkForm";
	}

	
	/**
	 * 修改日工
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody LoadingTeamDaywork daywork,Model model) {

		User user = UserUtil.getCurrentUser();
		if(user!=null){
			daywork.setUpdateUser(user.getName());
		}
		//修改保存时保留部分信息不被清空
		daywork.setUpdateTime(new Date());
		//daywork.setDelFlag(0);
		//daywork.setCreater(daywork.getCreater());
		//daywork.setCreateTime(daywork.getCreateTime());
		//daywork.setStandingNum(daywork.getStandingNum());
		//daywork.setState(daywork.getState());
		loadingTeamDayworkService.update(daywork);
		return "success";
	}
	/**
	 * 删除日工
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") String id) {
		if(id!=null){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("id", id);
			LoadingTeamDaywork  getObj=loadingTeamDayworkService.getDayworkInfo(id);
			getObj.setDelFlag(1);
			User user = UserUtil.getCurrentUser();
			if(user!=null){
				getObj.setDeleteUser(user.getName());
			}
			//修改保存时保留部分信息不被清空
			getObj.setDeleteTime(new Date());
			loadingTeamDayworkService.update(getObj);
		}
		return "success";
	}
	
	/*
	 * 添加日工台账信息
	 */
	@RequestMapping(value="addstandbook/{id}",method = RequestMethod.GET)
	@ResponseBody
	public String addStandBook(@PathVariable("id") String id){
		User user = UserUtil.getCurrentUser();
		List<LoadingTeamDaywork>  dayworkList = new ArrayList<LoadingTeamDaywork>();
		dayworkList = loadingTeamDayworkService.getDayworkById(id);
		LoadingTeamDaywork loadingTeamDaywork = null;
		int size = dayworkList.size();
		for(int i=0;i<size;i++){
			loadingTeamDaywork = new LoadingTeamDaywork();
			loadingTeamDaywork = dayworkList.get(i);
			BisStandingBook standingBook = new BisStandingBook();
			standingBook.setCustomsNum(loadingTeamDaywork.getClientId());    //客户ID
			standingBook.setCustomsName(loadingTeamDaywork.getClient());     //客户名
			standingBook.setBillNum(loadingTeamDaywork.getBillNum());          //提单号
			standingBook.setFeePlan(loadingTeamDaywork.getSchemeId());   //费用方案ID
			standingBook.setIfReceive(2);  //应收/应付
			standingBook.setFillSign(0);    //补充标志
			standingBook.setChargeDate(new Date());     //计费时间
			standingBook.setBillDate(loadingTeamDaywork.getBillDate());    //账单年月
			standingBook.setCostDate(new Date());       //费用产生时间
			standingBook.setInputPerson(user.getName());   //录入员
			standingBook.setInputDate(new Date());      //录入时间
			standingBook.setSplitSign(0);         //分割标志
			standingBook.setSettleSign(0);        //结算标志
			standingBook.setReconcileSign(0);     //对帐标志
			standingBook.setBoxSign(0);           //倒箱标记
			standingBook.setShareSign(0);          //分摊标记
			standingBook.setPaySign("0");          //垫付标记
			standingBook.setChargeSign("0");      //收款标记
			standingBook.setExamineSign(1);       //审核标志
			//用于保存的新实体类
			BisStandingBook standingBookNew = null;
			//获取费用方案信息
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("schemeNum", loadingTeamDaywork.getSchemeId());
			List<ExpenseSchemeInfo> expenseList = null;
			ExpenseSchemeInfo expenseInfo  = null;
			BaseTaxRate taxRate = null;
			//生成日工台账信息
			if(loadingTeamDaywork.getDayworkNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "15");
				expenseList = new ArrayList<ExpenseSchemeInfo>();
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo = new ExpenseSchemeInfo();
					expenseInfo = expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());          //费目代码
					standingBookNew.setPrice(expenseInfo.getUnit());               //单价
					standingBookNew.setNum(Double.valueOf(loadingTeamDaywork.getDayworkNum()));         //日工数
					standingBookNew.setReceiveAmount(loadingTeamDaywork.getDayworkNum() * expenseInfo.getUnit());     //计算日工费用
					taxRate = new BaseTaxRate();
					taxRate = taxRateService.getTaxByC(expenseInfo.getCurrency());  
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());       //汇率
					standingBookNew.setCurrency(expenseInfo.getCurrency());      //币种
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());    //应收费用
					standingBookNew.setBisType("15");     //费目代码
					standingBookNew.setFeeName(expenseInfo.getFeeName());     //费目名称
				}else{
					expenseInfo = new ExpenseSchemeInfo();
					standingBookNew.setNum(Double.valueOf(loadingTeamDaywork.getDayworkNum()));
					standingBookNew.setBisType("15");
				}
 				standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
				standingBookService.save(standingBookNew);
				//回写台账ID
				loadingTeamDayworkService.updateTZID(id,standingBookNew.getStandingNum());
			}
		}
		
		//更新 是否完成 状态,回写台账ID
		loadingTeamDayworkService.updateState(id);
		return "success";
	}
	
	/*
	 * 取消日工台账信息
	 */
	@RequestMapping(value="concel/{id}",method = RequestMethod.GET)
	@ResponseBody
	public String concelStandBook(@PathVariable("id") String id){
		List<LoadingTeamDaywork>  dayworkList = new ArrayList<LoadingTeamDaywork>();
		dayworkList = loadingTeamDayworkService.getDayworkById(id);         //根据ID获取装卸表信息
		LoadingTeamDaywork loadingTeamDaywork = null;
		String standingBookis=""; //台账IDs
		loadingTeamDaywork = new LoadingTeamDaywork();
		loadingTeamDaywork = dayworkList.get(0);
		standingBookis=loadingTeamDaywork.getStandingNum().toString();       //获取台账ID
		if (standingBookis.trim().length()>0) {
			List<String> ids=Arrays.asList(standingBookis.split(","));          //分隔取出台账ID
			standingBookService.deleteStandingBookBatch(ids);                   //删除对应台账
		}	
		//更新 是否完成 状态,回写台账ID
		loadingTeamDayworkService.updateState2(id);
		return "success";
	}
	
}
