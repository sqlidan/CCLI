package com.haiersoft.ccli.platform.web;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BasePlatform;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.PlatformService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.PlatformUser;
import com.haiersoft.ccli.platform.service.PlatformUserService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.service.UserService;
import com.haiersoft.ccli.system.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 */
@Controller
@RequestMapping("platform/user/manage")
public class PlatformUserController extends BaseController{
	
	@Autowired
	private PlatformUserService platformUserService ;
	@Autowired
	private UserService userService;
	@Autowired
	PlatformService platformService;

	
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "platform/user/platformUserManage";
	}
	

/*	@RequestMapping(value="manager", method = RequestMethod.GET)
	public String manager(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user", user.getName());
		model.addAttribute("date", new Date());
		model.addAttribute("action", "create");
		return "platform/user/platformUserInfo";
	}
  */
	

	

	
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
/*		Page<PlatformUser> page = getPage(request);
//		page.orderBy("id").order(Page.DESC); 
		PlatformUser platformUser = new PlatformUser();
        parameterReflect.reflectParameter(platformUser, request);
//		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);
		page = platformUserService.seachCustomsClearanceSql(page, platformUser);
		return getEasyUIData(page);*/


		Page<PlatformUser> page=getPage(request);
		page.setOrder("asc");
		page.setOrderBy("platformNo");
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = platformUserService.search(page,filters);

		return getEasyUIData(page);
	}

	
	/**
	 * @param model
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("platformUser", new PlatformUser());
		model.addAttribute("date", new Date());
		model.addAttribute("user",user.getName());
		model.addAttribute("action", "create");
		return "platform/user/platformUserInfo";
	}
	
	
	/**
	 * 新增保存报关单
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(HttpServletRequest request, HttpServletResponse response) {
		PlatformUser platformUser = new PlatformUser();
		parameterReflect.reflectParameter(platformUser, request);//转换对应实体类参数

	/*	List<PlatformUser> userResultList = getPlatformUserPage("EQI_userId", platformUser.getUserId());


		if(userResultList != null && userResultList.size() >0){ // 操作员存在

			return "userIdFail";
		}
*/
		List<PlatformUser> platformResultList = getPlatformUserPage("EQI_platformId", platformUser.getPlatformId());
		if(platformResultList != null && platformResultList.size() >0){ // 月台存在

			return "platformIdFail";
		}

		User user = UserUtil.getCurrentUser();
		//platformUser.setOperator(user.getName());
		platformUser.setCreatedTime(new Date());
		platformUser.setDeletedFlag("0");
		BasePlatform platform=platformService.get(platformUser.getPlatformId());
		platformUser.setPlatformNo(platform.getPlatformNo());

		try {
			platformUserService.save(platformUser);
		}
		catch(DataIntegrityViolationException ex){
			return "duplicate";
		}
		
		return "success";
	}

	private List<PlatformUser> getPlatformUserPage(String key ,Integer value) {

		List<PropertyFilter> filters = new ArrayList();

		PropertyFilter f1 = new PropertyFilter(key, value);
		filters.add(f1);
		//Page<PlatformUser> page = new Page<>();
		List<PlatformUser> search = platformUserService.search(filters);
		//List<PlatformUser> search = platformUserService.search(filters,"platformNo",true);

		return search;
	}

	//修改跳转
	@RequestMapping(value="update/{id}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("id") String id) {
		PlatformUser platformUser = platformUserService.get(id);
		model.addAttribute("platformUser", platformUser);
		model.addAttribute("action", "update");
		return "platform/user/platformUserInfo";
	}
	
	/**
	 * 修改保存报关单
	 * @throws ParseException 
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		/*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String operateTime = request.getParameter("operateTime");
		Date date = simpleDateFormat.parse(operateTime);*/
		PlatformUser platformUser = new PlatformUser();
		parameterReflect.reflectParameter(platformUser, request);//转换对应实体类参数

		platformUser.setUpdatedTime(new Date());
		//platformUser.setCreateTime(date);
		BasePlatform platform=platformService.get(platformUser.getPlatformId());
		platformUser.setPlatformNo(platform.getPlatformNo());
		platformUserService.update(platformUser);
		return "success";
	}


	/**
	 * 存在自身
	 * @throws ParseException
	 */
	@RequestMapping(value = "existOfSelf", method = RequestMethod.POST)
	@ResponseBody
	public String existOfSelf(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		/*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String operateTime = request.getParameter("operateTime");
		Date date = simpleDateFormat.parse(operateTime);*/
		PlatformUser platformUser = new PlatformUser();
		parameterReflect.reflectParameter(platformUser, request);//转换对应实体类参数
		//已审核的不能修改

	/*	List<PlatformUser> userResultList = getPlatformUserPage("EQI_userId", platformUser.getUserId());




		if(userResultList != null && userResultList.size() >0){ // userId存在

			for(PlatformUser platformUserExist :userResultList){

				if(!platformUserExist.getId().equals(platformUser.getId())){
					//存在的不是自身
					return "userIdFail";
				}

			}

		}*/

		List<PlatformUser>	platformResultList = getPlatformUserPage("EQI_platformId", platformUser.getPlatformId());


		if(platformResultList != null && platformResultList.size() >0){ // platformId存在

			for(PlatformUser platformUserExist :platformResultList){

				if(!platformUserExist.getId().equals(platformUser.getId())){
					//存在的不是自身
					return "platformIdFail";
				}
			}
		}

		return "success";
	}

	



	
	//删除
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") String id) {
		if(id==null&&id.equals("")) {
			return "error";
		}

		platformUserService.delete(id);

		return "success";
		
	}

	/**
	 * 获取月台口
	 */
	@RequestMapping(value="getPlatformAll",method = RequestMethod.GET)
	@ResponseBody
	public List<BasePlatform> getPlatformAll(HttpServletRequest request) {
		//List<BasePlatform> clientInfos = new ArrayList<BasePlatform>();
	//	List<BasePlatform> listC = platformService.getAll();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		filters.add(new PropertyFilter("EQS_deletedFlag", "0"));

		//List<BasePlatform> listC=platformService.search(filters);
		List<BasePlatform> listC = platformService.search(filters,"platformNo",true);

/*		int size = listC.size();
		int aa = 0;
		String bb = "";
		BasePlatform info = null;
		for (int i = 0; i < size; i++) {
			info = new BasePlatform();
			aa = listC.get(i).getId();
			info.setId(aa);
			bb = listC.get(i).getPlatform();
			info.setPlatform(bb);
			clientInfos.add(info);
		}*/
		return listC;
	}

	/**
	 * @throws UnsupportedEncodingException
	 * @Description: 获得所有客户(无视大小写)
	 */
	@RequestMapping(value = "getUserAll", method = RequestMethod.GET)
	@ResponseBody
	public List<User> getClientAll(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<User> clientInfos = new ArrayList<User>();
		String param = request.getParameter("q");// 搜索值
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

		String setid = request.getParameter("setid");// 原数据填充值
		if ((param != null && !"".equals(param))
				|| (setid != null && !"".equals(setid))) {
			if (param != null && !"".equals(param)) {
/*                try {

        			param=new String(param.getBytes("ISO8859-1"), "UTF-8");
        		} catch (UnsupportedEncodingException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}*/
				param= URLDecoder.decode(param,"UTF-8");
				List<Map<String, Object>> listC = platformUserService.findUser(param);
				int size = listC.size();
				int aa = 0;
				String bb = "";
				User info = null;
				for (int i = 0; i < size; i++) {
					info = new User();
					aa = ((BigDecimal) listC.get(i).get("ID")).intValue();
					info.setId(aa);
					bb = (String) listC.get(i).get("NAME");
					info.setName(bb);
					clientInfos.add(info);
				}
			} else {
				// 根据原值id获取对象
				if("".equals(param)){
				//	List<User> listC = userService.getAll();
					List<User> listC =userService.search(filters);
				/*	int size = listC.size();
					int aa = 0;
					String bb = "";
					User info = null;
					for (int i = 0; i < size; i++) {
						info = new User();
						aa = listC.get(i).getId();
						info.setId(aa);
						bb = listC.get(i).getName();
						info.setName(bb);
						clientInfos.add(info);
					}*/
					return listC;
				}
				User getObj = userService.get(Integer.valueOf(setid));
				if (getObj != null) {
					clientInfos.add(getObj);
				}

			}
		}else{
		//	List<User> listC = userService.getAll();
			List<User> listC =userService.search(filters);

		/*	int size = listC.size();
			int aa = 0;
			String bb = "";
			User info = null;
			for (int i = 0; i < size; i++) {
				info = new User();
				aa = listC.get(i).getId();
				info.setId(aa);
				bb = listC.get(i).getName();
				info.setName(bb);
				clientInfos.add(info);
			}

*/

		return  listC;
		}
		return clientInfos;
	}


	
}
