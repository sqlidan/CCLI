package com.haiersoft.ccli.base.web;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.base.entity.ExpenseContractInfo;
import com.haiersoft.ccli.base.service.ExpenseContractInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseContractInfoController
 * @Description: 合同费目明细Controller
 * @date 2016年2月25日 下午4:52:22
 */
@Controller
@RequestMapping("base/contractInfo")
public class ExpenseContractInfoController extends BaseController{
	
	@Autowired
	private ExpenseContractInfoService expenseContractInfoService;
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 合同查询
	 * @date 2016年2月24日 下午3:42:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(@PathVariable("id") String contractNum, HttpServletRequest request) {
		Page<ExpenseContractInfo> page = getPage(request);
		page = expenseContractInfoService.seachSql(page,contractNum);
		return getEasyUIData(page);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 批量保存费目明细
	 * @date 2016年2月26日 下午12:23:23 
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value="{contractNum}/createContractInfoIds")
	@ResponseBody
	public String createContractInfoById(@PathVariable("contractNum") String contractNum, @RequestBody List<Integer> newIdsList) {
		return expenseContractInfoService.createContractInfoMore(contractNum, newIdsList);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 根据费目代码  添加合同明细
	 * @date 2016年2月26日 下午4:45:44 
	 * @param contractNum
	 * @param code
	 * @return
	 * @throws
	 */
	@RequestMapping(value="{contractNum}/{code}/createContractInfoByCode")
	@ResponseBody
	public String createContractInfoByCode(@PathVariable("contractNum") String contractNum, @PathVariable("code") String code) {
		expenseContractInfoService.createContractInfoByCode(contractNum, code);
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 删除
	 * @date 2016年2月26日 下午3:18:12 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "deleteContractInfo/{ids}")
	@ResponseBody
	public String deleteContractInfo(@PathVariable("ids") List<String> ids) {
//		expenseContractInfoService.delete(id);
		for(int i = 0;i<ids.size();i++){
			expenseContractInfoService.delete((ids.get(i)));
		}
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 复制
	 * @date 2016年2月26日 下午3:19:29 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "copyContractInfo/{id}")
	@ResponseBody
	public String copyContractInfo(@PathVariable("id") String id) {
		expenseContractInfoService.createCopyContractInfo(id);
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 合同明细   编辑
	 * @date 2016年4月22日 上午9:37:37 
	 * @param id
	 * @param minPrice
	 * @param maxPrice
	 * @param termAttribute
	 * @param gearCode
	 * @param gearExp
	 * @return
	 * @throws
	 */
	@RequestMapping("{id}/updateContractInfo")
	@ResponseBody
	public String updateContractInfo(@PathVariable("id") String id, @RequestParam String price, @RequestParam String billUnit,
			@RequestParam String minPrice, @RequestParam String maxPrice,
			@RequestParam String termAttribute, @RequestParam String gearCode, @RequestParam String gearExp, @RequestParam String onSale, @RequestParam String remark) {
		ExpenseContractInfo contractInfo = expenseContractInfoService.get(id);
		contractInfo.setPrice(StringUtils.isNull(price) ? 1D : Double.parseDouble(price));
		contractInfo.setBillUnit(billUnit);
		contractInfo.setMinPrice(StringUtils.isNull(minPrice) ? null : Double.parseDouble(minPrice));
		contractInfo.setMaxPrice(StringUtils.isNull(maxPrice) ? null :Double.parseDouble(maxPrice));
		contractInfo.setTermAttribute(termAttribute);
		contractInfo.setGearCode(gearCode);
		contractInfo.setGearExp(gearExp);
		contractInfo.setRemark(remark);
		contractInfo.setOnSale(StringUtils.isNull(onSale) ? 1D : Double.parseDouble(onSale));
		expenseContractInfoService.update(contractInfo);
		return "success";
	}
	
}
