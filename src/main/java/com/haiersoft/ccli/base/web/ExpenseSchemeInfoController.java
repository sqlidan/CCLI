package com.haiersoft.ccli.base.web;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;

@Controller
@RequestMapping("base/schemeInfo")
public class ExpenseSchemeInfoController extends BaseController{
	
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 方案明细查询
	 * @date 2016年2月29日 下午4:37:46 
	 * @param contractNum
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(@PathVariable("id") String schemeNum, HttpServletRequest request) {
		Page<ExpenseSchemeInfo> page = getPage(request);
		page = expenseSchemeInfoService.seachSql(page, schemeNum);
		return getEasyUIData(page);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 批量保存费目
	 * @date 2016年2月29日 下午5:23:02 
	 * @param contractNum
	 * @param newIdsList
	 * @return
	 * @throws
	 */
	@RequestMapping(value="{schemeNum}/createSchemeInfoBatch")
	@ResponseBody
	public String createSchemeInfoBatch(@PathVariable("schemeNum") String schemeNum, @RequestBody List<String> ids) {
		expenseSchemeInfoService.createSchemeInfoBatch(schemeNum, ids);
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 删除
	 * @date 2016年2月29日 下午6:34:11 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "deleteSchemeInfo/{ids}")
	@ResponseBody
	public String deleteSchemeInfo(@PathVariable("ids") List<String> ids) {
//		expenseSchemeInfoService.delete(id);
		for(int i = 0;i<ids.size();i++){
			expenseSchemeInfoService.delete((ids.get(i)));
		}
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 复制
	 * @date 2016年2月29日 下午6:36:30 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "copySchemeInfo/{id}")
	@ResponseBody
	public String copySchemeInfo(@PathVariable("id") String id) {
		expenseSchemeInfoService.createCopySchemeInfo(id);
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 费用方案明细   弹框
	 * @date 2016年3月1日 上午10:38:25 
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "createSchemeFeeCodeForm", method = RequestMethod.GET)
	public String createSchemeFeeCodeForm(Model model) {
		return "base/scheme/schemeFeeCodeForm";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 保存
	 * @date 2016年3月1日 下午2:30:54 
	 * @param schemeNum
	 * @param request
	 * @param response
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "createSchemeInfoFeeCode/{schemeNum}", method = RequestMethod.POST)
	@ResponseBody
	public String createSchemeInfoFeeCode(@PathVariable("schemeNum") String schemeNum, HttpServletRequest request, HttpServletResponse response) {
		ExpenseSchemeInfo schemeInfo = new ExpenseSchemeInfo();
		parameterReflect.reflectParameter(schemeInfo, request);//转换对应实体类参数
		String num = expenseSchemeInfoService.getSchemeInfoSeq();
		schemeInfo.setId(num);
		schemeInfo.setSchemeNum(schemeNum);
		expenseSchemeInfoService.save(schemeInfo);
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 修改方案明细
	 * @date 2016年3月1日 下午3:05:08 
	 * @return
	 * @throws
	 */
	@RequestMapping("{id}/updateSchemeInfo")
	@ResponseBody
	public String updateSchemeInfo(@PathVariable("id") String id, @RequestParam String minPrice, @RequestParam String maxPrice,
			@RequestParam String termAttribute, @RequestParam String gearCode, @RequestParam String gearExp) {
		ExpenseSchemeInfo schemeInfo = expenseSchemeInfoService.get(id);
		schemeInfo.setMinPrice(StringUtils.isNull(minPrice) ? null : Double.parseDouble(minPrice));
		schemeInfo.setMaxPrice(StringUtils.isNull(maxPrice) ? null : Double.parseDouble(maxPrice));
		schemeInfo.setTermAttribute(termAttribute);
		schemeInfo.setGearCode(gearCode);
		schemeInfo.setGearExp(gearExp);
		expenseSchemeInfoService.update(schemeInfo);
		return "success";
	}
	
	//根据提单号获得可选择的所有费目
	@RequestMapping(value = "getfeecode/{billNum}", method = RequestMethod.GET)
	@ResponseBody
	public List<ExpenseSchemeInfo> getFeeCode(@PathVariable("billNum") String billNum) {
		List<Map<String,Object>> listCode = expenseSchemeInfoService.getFeeCodeByBill(billNum);
		List<ExpenseSchemeInfo> feeCode = new ArrayList<ExpenseSchemeInfo>();
		ExpenseSchemeInfo info = null;
		String code = "";
		String name="";
		String id="";
		int size = listCode.size();
		for(int i=0;i<size;i++){
			info = new ExpenseSchemeInfo();
			code = (String)listCode.get(i).get("FEE_CODE");
			info.setFeeCode(code);
			name=(String) listCode.get(i).get("FEE_NAME");
			info.setFeeName(name);
			id=(String) listCode.get(i).get("ID");
			info.setId(id);
			feeCode.add(info);
		}
		return feeCode;
	}
	
	
	    //根据方案编号获得可选择的所有费目
		@RequestMapping(value = "getSchemeNum/{schemeNum}", method = RequestMethod.GET)
		@ResponseBody
		public List<ExpenseSchemeInfo> getSchemeNum(@PathVariable("schemeNum") String schemeNum) {
			List<Map<String,Object>> listCode = expenseSchemeInfoService.getFeeCodeByNum(schemeNum);
			List<ExpenseSchemeInfo> feeCode = new ArrayList<ExpenseSchemeInfo>();
			ExpenseSchemeInfo info = null;
			String code = "";
			String name="";
			String id="";
			int size = listCode.size();
			for(int i=0;i<size;i++){
				info = new ExpenseSchemeInfo();
				code = (String)listCode.get(i).get("FEE_CODE");
				info.setFeeCode(code);
				name=(String) listCode.get(i).get("FEE_NAME");
				info.setFeeName(name);
				id=(String) listCode.get(i).get("ID");
				info.setId(id);
				feeCode.add(info);
			}
			return feeCode;
		}
	
	/**
	 * 
	 * @Description: 垫付
	 * @param ids
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "pay/{ids}")
	@ResponseBody
	public String pay(@PathVariable("ids") List<String> ids) {
//		String[] aa = (String[]) ids.toArray(new String[ids.size()]);
		String a="";
		for(String s:ids){
			a+=s+",";
		}
		if(!"".equals(a)){
			a=a.substring(0, a.length()-1);
		} 
		expenseSchemeInfoService.updatePay(a);
		return "success";
	}
	
	/**
	 * 
	 * @Description: 取消垫付
	 * @param ids
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "nopay/{ids}")
	@ResponseBody
	public String nopay(@PathVariable("ids") List<String> ids) {
//		String[] aa = (String[]) ids.toArray(new String[ids.size()]);
		String a="";
		for(String s:ids){
			a+=s+",";
		}
		if(!"".equals(a)){
			a=a.substring(0, a.length()-1);
		} 
		expenseSchemeInfoService.updateNoPay(a);
		return "success";
	}
}
