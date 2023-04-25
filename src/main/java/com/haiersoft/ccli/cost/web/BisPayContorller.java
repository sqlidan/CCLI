package com.haiersoft.ccli.cost.web;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.NumberToCN;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.Dict;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.service.DictService;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.cost.service.BisPayInfoService;
import com.haiersoft.ccli.cost.service.BisPayService;
import com.haiersoft.ccli.cost.entity.BisPay;
import com.haiersoft.ccli.cost.entity.BisPayInfo;
import com.haiersoft.ccli.base.service.ClientService;
/**
 * BisPayContorller
 * @author PYL
 * @date 2016年3月23日
 */
@Controller
@RequestMapping("cost/bispay")
public class BisPayContorller extends BaseController {
	
	@Autowired
	private BisPayService bisPayService;//业务付款单
	@Autowired
	private BisPayInfoService bisPayInfoService;//业务付款单明细
	@Autowired
	private ClientService clientService;//客户
	@Autowired
	private DictService dictService;
	
	/*跳转业务付款单列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "cost/bisPay/bisPay";
	}
	
	/**
	 * 跳转业务付款单管理页面
	 */
	@RequestMapping(value="manager", method = RequestMethod.GET)
	public String manager(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user", user.getName());
		model.addAttribute("date", new Date());
		model.addAttribute("action", "create");
		return "cost/bisPay/bisPayManager";
	}
	
	@RequestMapping(value="getNewPayId", method = RequestMethod.POST)
	@ResponseBody
	public String getNewPayId() {
		String payId = bisPayService.getPayIdToString();//入库联系单号
		return payId;
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 业务付款单总览查询
	 * @date 2016年5月12日 下午2:42:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisPay> page = getPage(request);
//		page.setOrder(Page.DESC);
		page.orderBy("payId").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		int size = filters.size();
		for(int i=0;i<size;i++){
			PropertyFilter obj = filters.get(i);
			if("IN".equals(obj.getMatchType().toString()) && "payId".equals(obj.getPropertyName())){
				String[] billNum = (String[]) obj.getMatchValue();
				obj.setMatchValue(billNum);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("billNum", billNum[0]);
				List<BisPayInfo> infoList = bisPayInfoService.findby(params);
				if(!infoList.isEmpty()){
					List<String> payIdList = new ArrayList<String>();
					for(BisPayInfo info:infoList){
						payIdList.add(info.getPayId());
					}
					String[] aa = (String[]) payIdList.toArray(new String[payIdList.size()]);
					obj.setMatchValue(aa);
				}else{
					String[] aa = new String[1];
					aa[0]="000";
					obj.setMatchValue(aa);
				}
			}
		}
		page = bisPayService.search(page, filters);
		return getEasyUIData(page);
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 业务付款单修改
	 * @date 2016年5月12日 
	 * @param model
	 * @throws
	 */
	@RequestMapping(value="update/{payId}", method = RequestMethod.GET)
	public String update(Model model, @PathVariable("payId") String payId) {
		BisPay bisPay = bisPayService.get(payId);
		model.addAttribute("bisPay", bisPay);
		model.addAttribute("action", "update");
		return "cost/bisPay/bisPayManager";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 删除前确认是否有明细
	 * @date 2016年5月12日 上午11:29:50 
	 * @param payId
	 * @return
	 */
	@RequestMapping(value = "ifInfo/{payId}")
	@ResponseBody
	public String ifInfo(@PathVariable("payId") String payId) {
		List<BisPayInfo> infoList = bisPayInfoService.getList(payId);
		if(infoList.isEmpty()){
			return "success";
		}else{
			return "false";
		}
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 判断业务付款单是否已保存
	 * @date 2016年5月13日 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="ifsave/{payId}", method = RequestMethod.GET)
	@ResponseBody
	public String ifsave(@PathVariable("payId") String payId) {
		BisPay bisPay = bisPayService.get(payId);
		if(null != bisPay && !"".equals(bisPay)){
			return "success";
		}else{
			return "false";
		}
		
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 删除
	 * @date 2016年5月12日 上午11:29:50 
	 * @param payId
	 * @return
	 */
	@RequestMapping(value = "delete/{payId}")
	@ResponseBody
	public String delete(@PathVariable("payId") String payId) {
		bisPayService.delete(payId);
		return "success";
	}

	/**
	 * 
	 * @author pyl
	 * @Description: 保存
	 * @date 2016年5月13日 
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String createContract(HttpServletRequest request, HttpServletResponse response) {
		BisPay bisPay = new BisPay();
		parameterReflect.reflectParameter(bisPay, request);//转换对应实体类参数
		bisPayService.save(bisPay);
		return "success";
	}
	
	
	/**
	 * @author pyl
	 * @Description: 获取客户明细
	 * @date 2016年5月16日 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="getClientInfo/{clientId}", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getClientInfo(@PathVariable("clientId") Integer clientId) {
		BaseClientInfo clientInfo = clientService.get(clientId);
		List<String> aa = new ArrayList<String>();
		if(null != clientInfo && !"".equals(clientInfo)){
			aa.add(clientInfo.getRmbBank());
			aa.add(clientInfo.getRmbAccount());
		}
		return aa;
	}
	
	/**
	 * 计算总费用
	 */
	@RequestMapping(value="setprice",method = RequestMethod.GET)
	@ResponseBody
	public String setPrice(HttpServletRequest request,String payId,Double price) {
		BisPay bisPay = bisPayService.get(payId);
		bisPay.setPrice(price);
		bisPayService.update(bisPay);
		return "success";
	}
	
	/**
	 * 查询提单号跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "enterlist", method = RequestMethod.GET)
	public String enterList() {
		return "cost/bisPay/searchBillList";
	}
	
	/**
	 * 业务付款单打印跳转 gzq 20160628 添加
	 * @author gzq
	 * @date 20160628
	 * @param payId
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "print/{payId}", method = RequestMethod.GET)
	public String print(@PathVariable("payId") String payId, Model model) {
		BisPay bisPay = bisPayService.get(payId);
		List<BisPayInfo> bisPayInfoList = bisPayInfoService.getList(payId);
		List<Dict> dict = dictService.getReceptacle("receptacle");
		model.addAttribute("bisPay", bisPay);
		model.addAttribute("dict", dict);
		double money = bisPay.getPrice();
		BigDecimal numberOfMoney = new BigDecimal(money);
		BigDecimal setScale= numberOfMoney.setScale(2,BigDecimal.ROUND_HALF_UP);
	    model.addAttribute("money",setScale);
		String strCnNumber = NumberToCN.number2CNMontrayUnit(setScale);
		model.addAttribute("CnNumber",strCnNumber);
		if(!bisPayInfoList.isEmpty()){
				int l = bisPayInfoList.size();
		    	model.addAttribute("infoSize", l);
				model.addAttribute("bisPayInfoList", bisPayInfoList);
				Integer allPiece = 0;
				Double allNet = 0.00;
				Double allGross = 0.00;
			    model.addAttribute("allPiece", allPiece);
			    model.addAttribute("allNet", allNet);
			    model.addAttribute("allGross", allGross);
		}
		model.addAttribute("action", "print");
		return "cost/bisPay/bisPayPrint";
	}
    
    /**
     * 审核业务付款单
     * @param payId
     * @return
     */
    @RequestMapping(value = "passBisPay/{payId}")
    @ResponseBody
    public String passBisPay(@PathVariable("payId") String payId) {
        return bisPayService.updatePays(payId,"1");
    }
    
    
    /**
     * 取消审核业务付款单
     * @param payId
     * @return
     */
    @RequestMapping(value = "unPassBisPay/{payId}")
    @ResponseBody
    public String unPassBisPay(@PathVariable("payId") String payId) {
        return bisPayService.updatePays(payId,"0");
    }
}
