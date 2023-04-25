package com.haiersoft.ccli.base.web;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.base.entity.BaseWorkGroup;
import com.haiersoft.ccli.base.service.WorkGroupService;

/**
 * ClientRankController 客户优先级
 * @author pyl
 * @date 2017年6月19日
 */
@Controller
@RequestMapping("base/work")
public class WorkGroupController extends BaseController {

	@Autowired
	private WorkGroupService workGroupService;

	/**
	 * 默认页面
	 */
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String list() {
		return "base/work/workList";
	}
	
	 /**
     * @param request
     * @return
     * @throws
     * @author PYL
     * @Description: 库管人员组查询
     * @date 2017年6月27日 下午3:34:57
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {

        Page<BaseWorkGroup> page = getPage(request);

        BaseWorkGroup workGroup = new BaseWorkGroup();
        parameterReflect.reflectParameter(workGroup, request);//转换对应实体类参数

        page = workGroupService.searchKG(page, workGroup);

        return getEasyUIData(page);
    }

    /**
	 * 
	 * @author pyl
	 * @Description: 库管人员组明细查询
	 * @return
	 * @throws
	 */
	@RequestMapping(value="infoJson/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request,@PathVariable("id") Integer id) {
		Page<BaseWorkGroup> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQI_parentId", id);
		filters.add(filter);
		page = workGroupService.search(page, filters);
		return getEasyUIData(page);
	}
	 
	 /**
     * 增加库管员页面跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "addKg", method = RequestMethod.GET)
    public String addKg() {
    	return "base/work/addKg";
    }
	
	


	
	
	/**
     * 增加库管员
     */
    @RequestMapping(value = "addKg", method = RequestMethod.POST)
    @ResponseBody
    public String addKgInfo(HttpServletRequest request) {
    	String name=request.getParameter("kgPerson");
    	BaseWorkGroup obj=new BaseWorkGroup(name,"1",0);
    	workGroupService.save(obj);
        return "success";
    }
	
	/**
	 * 
	 * @author pyl
	 * @Description: 删除
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String deleteEnterStock(@PathVariable("id") Integer id) {
		workGroupService.delete(id);
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("parentId", id);
		List<BaseWorkGroup> objList = workGroupService.findBy(params);
		for(BaseWorkGroup obj:objList){
			workGroupService.delete(obj);
		}
		return "success";
 	}
	
	
	 /**
     * 增加其他人员页面跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "addOther/{id}", method = RequestMethod.GET)
    public String addOther(Model model,@PathVariable("id") Integer id) {
    	model.addAttribute("id", id);
    	return "base/work/addOther";
    }
	
	


	
	
	/**
     * 增加其他人员
     */
    @RequestMapping(value = "addOther/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String addOtherInfo(HttpServletRequest request,@PathVariable("id") Integer id) {
    	String lhName=request.getParameter("lhPerson");
    	String ccName=request.getParameter("ccPerson");
    	if(!StringUtils.isNull(lhName)){
    		BaseWorkGroup lh=new BaseWorkGroup(lhName,"2",id);
        	workGroupService.save(lh);
    	}
    	if(!StringUtils.isNull(ccName)){
    		BaseWorkGroup cc=new BaseWorkGroup(ccName,"3",id);
        	workGroupService.save(cc);
    	}
    	
        return "success";
    }
	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 删除组信息
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "deleteInfo/{ids}")
	@ResponseBody
	public String deleteInfo(@PathVariable("ids") List<Integer> ids) {
		for(Integer id:ids){
			workGroupService.delete(id);
		}
		return "success";
 	}
	
	
	
	/**
    * 校验库管员
    *
    */
   @RequestMapping(value = "checkKg/{name}", method = RequestMethod.GET)
   @ResponseBody
   public String create(HttpServletRequest request,@PathVariable("name") String name) {
	   Map<String,Object> params=new HashMap<String,Object>();
	   params.put("person", name);
	   List<BaseWorkGroup> objList = workGroupService.findBy(params);
	   if(objList.isEmpty()){
		   return "success";
	   }else{
		   return "false";
	   }
   }
}