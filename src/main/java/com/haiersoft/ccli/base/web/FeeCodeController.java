package com.haiersoft.ccli.base.web;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.base.entity.FeeCode;
import com.haiersoft.ccli.base.service.FeeCodeService;

/**
 * 费目controller
 * @author ty
 * @date 2015年1月13日
 */
@Controller
@RequestMapping("base/feecode")
public class FeeCodeController extends BaseController {

	@Autowired
	private FeeCodeService feeCodeService;
	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "base/feeCode";
	}
	
	/**
	 * 获取费用代码表
	 */
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<FeeCode> page = getPage(request);
		page.orderBy("ID").order(Page.DESC);
		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);
		page=feeCodeService.seachSql(page,map);
		return getEasyUIData(page);
	}
	/**
	 * 添加费目代码跳转
	 * 
	 * @param model
	 */
	@RequiresPermissions("base:feecode:input")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("feecode", new FeeCode());
		model.addAttribute("action", "create");
		return "base/feeCodeAdd";
	}

	/**
	 * 添加费目
	 * 
	 * @param user
	 * @param model
	 */
	@RequiresPermissions("base:feecode:input")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid FeeCode feeCode,Model model, HttpServletRequest request) {
		feeCodeService.save(feeCode);
		return "success";
	}

	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 添加费用方法  ajax的提交方式
	 * @date 2016年2月26日 下午7:00:48 
	 * @param feeCode
	 * @param model
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "createByAjax", method = RequestMethod.POST)
	@ResponseBody
	public String createByAjax(HttpServletRequest request, HttpServletResponse response) {
		FeeCode feeCode = new FeeCode();
		parameterReflect.reflectParameter(feeCode, request);//转换对应实体类参数
		feeCode.setId(null);//保存主键ID不能存在
		feeCodeService.save(feeCode);
		return "success";
	}
	
	/**
	 * 复制费目跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("base:feecode:copy")
	@RequestMapping(value = "copy/{id}", method = RequestMethod.GET)
	public String copyForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("feeCode", feeCodeService.get(id));
		model.addAttribute("action", "copy");
		return "base/feeCodeAdd";
	}
	
	/**
	 * 复制后添加费目
	 * 
	 * @param user
	 * @param model
	 */
	@RequiresPermissions("base:feecode:input")
	@RequestMapping(value = "copy", method = RequestMethod.POST)
	@ResponseBody
	public String createCopy(@Valid FeeCode feeCode,Model model) {
		feeCodeService.save(feeCode);
		return "success";
	}
	/**
	 * 修改费目跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("base:feecode:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("feeCode", feeCodeService.get(id));
		model.addAttribute("action", "update");
		return "base/feeCodeAdd";
	}

	/**
	 * 修改费目
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("base:feecode:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody FeeCode feeCode,Model model) {
		feeCodeService.update(feeCode);
		return "success";
	}

	/**
	 * 删除费目
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("base:feecode:delete")
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		feeCodeService.delete(id);
		return "success";
	}
	/**
	 * Ajax请求校验feeCode是否唯一。
	 */
	@RequestMapping(value = "checkFeeCode")
	@ResponseBody
	public String checkFeeCode(String code) {
		if (feeCodeService.getFeeCode(code) == null) {
			return "true";
		} else {
			return "false";
		}
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得所有费目检索
	 * @date 2016年4月6日 下午5:07:05 
	 * @param request
	 * @param response
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "getFeeCodeAll", method = RequestMethod.GET)
	@ResponseBody
	public List<FeeCode> getFeeCodeAll(HttpServletRequest request, HttpServletResponse response){
		List<FeeCode> feeCodes = new ArrayList<FeeCode>();
		
		String param = request.getParameter("q");//搜索值
		String setid = request.getParameter("setid");//搜索值
		
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		
		if(!StringUtils.isNull(param)){
			filters.add(new PropertyFilter("LIKES_nameC", param));
		}else if(!StringUtils.isNull(setid)){
			filters.add(new PropertyFilter("EQS_code", setid));
		}
		
		if(null != filters && filters.size() > 0){
			feeCodes = feeCodeService.search(filters);
		}
		return feeCodes;
	}
	
	/**
	 * 
	 * @Description: 获得费目
	 * @date 2016年6月6日 
	 * @param request
	 * @param response
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "getFeeAll", method = RequestMethod.GET)
	@ResponseBody
	public List<FeeCode> getFeeAll(HttpServletRequest request, HttpServletResponse response) {
		List<FeeCode> feeCodeLisst = new ArrayList<FeeCode>();
		String param = request.getParameter("q");// 搜索值 
		String setid = request.getParameter("setid");// 原数据填充值
		if ( (param != null && !"".equals(param)) || (setid != null && !"".equals(setid)) ) {
			if (param != null && !"".equals(param)) {
				List<Map<String, Object>> listC = feeCodeService.findFeeCode(param);
				int size = listC.size();
				String aa = "";
				String bb = "";
				FeeCode info = null;
				for (int i = 0; i < size; i++) {
					info = new FeeCode();
					aa = (String) listC.get(i).get("CODE");
					info.setCode(aa);
					bb = (String) listC.get(i).get("NAME_C");
					info.setNameC(bb);
					feeCodeLisst.add(info);
				}
			} else {
				// 根据原值id获取对象
				FeeCode getObj = feeCodeService.find("code",setid);
				if (getObj != null) {
					feeCodeLisst.add(getObj);
				}
			}
		}
		return feeCodeLisst;
	}
	
} 
