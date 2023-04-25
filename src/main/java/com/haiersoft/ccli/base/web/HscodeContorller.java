package com.haiersoft.ccli.base.web;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.axis.message.MessageWithAttachments;
import org.apache.poi.poifs.storage.ListManagedBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.haiersoft.ccli.base.entity.BaseHscode;
import com.haiersoft.ccli.base.service.HscodeService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;

import net.sf.json.JSONArray;
/**
 * HS编码controller
 */
@Controller
@RequestMapping("base/hscode")
public class HscodeContorller extends BaseController {
	@Autowired
	private HscodeService hscodeService;
	
	/**
	 * 默认页面
	 */
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "base/hscode/hscode";
	}
	
	/**
	 * 添加Hscode跳转
	 */
/*	@RequestMapping(value = "hscodelist", method = RequestMethod.GET)
	public String hscodeList() {
		return "base/hscode/hscodeList";
	}*/
	
	/**
	 * 
	 * @author pyl
	 * @Description: HS基础数据展示
	 * @date 2016年6月6日 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="listjson", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BaseHscode> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = hscodeService.search(page, filters);
		return getEasyUIData(page);
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: HS基础数据展示
	 * @date 2016年6月6日 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="lisths", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public List<Map<String, Object>> getHsData(HttpServletRequest request) {
		Page<BaseHscode> page = getPage(request);
		String param = request.getParameter("q");// 搜索值
		List<Object[]>  res=hscodeService.getHsData(param);
    
       // JsonObject jsonObject=new JsonObject();
        List<Map<String,Object>> list=new ArrayList<>();
       
        for(int i=0;i<res.size();i++){
            Map<String, Object> map =new HashMap<String, Object>();
        	map.put("id",res.get(i)[0].toString()!= null ? res.get(i)[0].toString() : "" );
        	map.put("code",res.get(i)[1].toString()!= null ? res.get(i)[1].toString() : "" );
        	list.add(map);
        }
		return list;
	}
	/**
	 * 
	 * @author pyl
	 * @Description: 删除
	 * @date 2016年4月18日 上午11:29:50 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		hscodeService.delete(id);
		return "success";
	}
	
	/**
	 * 添加跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("hscode", new BaseHscode());
		model.addAttribute("action", "create");
		return "base/hscode/hscodeAdd";
	}
	
	/**
	 * 添加HS编码基础数据
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BaseHscode baseHscode,Model model, HttpServletRequest request) {
		hscodeService.save(baseHscode);
		return "success";
	}
	
	/**
	 * 修改入库联系单明细跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("hscode", hscodeService.get(id));
		model.addAttribute("action", "update");
		return "base/hscode/hscodeAdd";
	}
	
	/**
	 * 修改入库联系单明细
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody BaseHscode baseHscode,Model model) {
		hscodeService.update(baseHscode);
		return "success";
	}
	
	/**
	 * 校验HS编码是否唯一
	 */
	@RequestMapping(value = "checkhscode")
	@ResponseBody
	public String checkItemNum(String code) {
		BaseHscode hscode = hscodeService.find("code",code);
		if (null == hscode || "".equals(hscode)) {
			return "true";
		}else{
			return "false";
		}
	}
	
}
