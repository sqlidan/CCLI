package com.haiersoft.ccli.base.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.BisExpenseSchemeScale;
import com.haiersoft.ccli.base.service.ExpenseSchemeScaleService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;

/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseSchemeScaleController
 * @Description: 费用分摊  Controller
 * @date 2016年4月18日 下午3:50:01
 */
@Controller
@RequestMapping("base/share")
public class ExpenseSchemeScaleController extends BaseController{

	@Autowired
	private ExpenseSchemeScaleService expenseSchemeScaleService;

	@RequestMapping(value = "updateCostData/{id}",method = RequestMethod.GET)
	@ResponseBody
	public String updateCostData(@PathVariable("id") Integer id,@RequestParam Double fentan,@RequestParam Double receiveAmount,@RequestParam Double num){
		BisExpenseSchemeScale scale=expenseSchemeScaleService.get(id);
		scale.setExamineSign("1");
		scale.setFentan(fentan);
		scale.setFentanAmount(BigDecimalUtil.mul(fentan,receiveAmount));
		scale.setNum(BigDecimalUtil.mul(fentan,num));
		expenseSchemeScaleService.merge(scale);
		////////////同步更新另外分摊费用比例///////////////////////////////
		List<BisExpenseSchemeScale> list=expenseSchemeScaleService.getSchemeScale(scale.getLinkId(),scale.getFeeCode(),("0".equals(scale.getBosSign())?"1":"0"),scale.getStandingNum());
		for (int i = 0; i < list.size(); i++) {
			BisExpenseSchemeScale ob=list.get(i);
			ob.setExamineSign("1");
			ob.setFentan((1-fentan));
			ob.setFentanAmount(BigDecimalUtil.mul((1-fentan),receiveAmount));
			ob.setNum(BigDecimalUtil.mul((1-fentan),num));
			expenseSchemeScaleService.merge(ob);
		}
		return "success";
	}
	/**
	 * 出库费用调整   进入  费用分摊界面
	 */
	@RequestMapping(value="costSharelist/{linkId}/{stockId}", method = RequestMethod.GET)
	public String costSharelist(Model model, @PathVariable("linkId") String linkId, @PathVariable("stockId") String stockId) {
		model.addAttribute("linkId", linkId);
		model.addAttribute("stockId", stockId);
		return "cost/standingBook/costSharelist";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 费用分摊   发货方 分页
	 * @date 2016年4月18日 下午4:38:01 
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="getToCostData", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getToCostData(HttpServletRequest request) {
		Page<BisExpenseSchemeScale> page = getPage(request);
		BisExpenseSchemeScale scale = new BisExpenseSchemeScale();
		parameterReflect.reflectParameter(scale, request);//转换对应实体类参数
		page = expenseSchemeScaleService.searchToCastShare(page, scale);
		return getEasyUIData(page);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description:  费用分摊   收货方  分页
	 * @date 2016年4月19日 上午11:13:21 
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="getFromCostData", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getFromCostData(HttpServletRequest request) {
		Page<BisExpenseSchemeScale> page = getPage(request);
		BisExpenseSchemeScale scale = new BisExpenseSchemeScale();
		parameterReflect.reflectParameter(scale, request);//转换对应实体类参数
		page = expenseSchemeScaleService.searchFromCastShare(page, scale);
		return getEasyUIData(page);
	}
	/**
	 * 
	 * @author Connor.M
	 * @Description:  费用分摊  操作
	 * @date 2016年4月19日 下午3:23:06 
	 * @param linkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value="startCostShare/{linkId}/{stockId}")
	@ResponseBody
	public String startCostShare(@PathVariable("linkId") String linkId, @PathVariable("stockId") String stockId){
		BisExpenseSchemeScale scale = new BisExpenseSchemeScale();
		scale.setLinkId(linkId);
		scale.setExamineSign("1");
		return expenseSchemeScaleService.startCostShare(scale);
	}
	/**
	 * 
	 * @author Connor.M
	 * @Description: 取消分摊操作
	 * @date 2016年5月4日 下午5:39:46 
	 * @param linkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value="cancelCostShare/{linkId}/{stockId}")
	@ResponseBody
	public String cancelCostShare(@PathVariable("linkId") String linkId, @PathVariable("stockId") String stockId){
		BisExpenseSchemeScale scale = new BisExpenseSchemeScale();
		scale.setLinkId(linkId);
		scale.setCustomsId(stockId);
		return expenseSchemeScaleService.cancelCostShare(scale);
	}
}
