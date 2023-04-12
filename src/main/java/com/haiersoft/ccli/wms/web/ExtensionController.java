package com.haiersoft.ccli.wms.web;
import java.util.Date;
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
import com.haiersoft.ccli.wms.entity.BisCiqDeclaration;
import com.haiersoft.ccli.wms.entity.BisCustomsDeclaration;
import com.haiersoft.ccli.wms.entity.Extension;
import com.haiersoft.ccli.wms.service.CiqDeclarationService;
import com.haiersoft.ccli.wms.service.CustomsDeclarationService;
import com.haiersoft.ccli.wms.service.ExtensionService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * 
 * @author pyl
 * @ClassName: ExtensionController
 * @Description: 展期Control1ler
 * @date 2016年5月31日 
 */
@Controller
@RequestMapping("wms/extension")
public class ExtensionController extends BaseController{
	
	@Autowired
	private ExtensionService extensionService;
	@Autowired
	private CustomsDeclarationService customsDeclarationService;
	@Autowired
	private CiqDeclarationService ciqDeclarationService;
	
	/**
	 * 展期总览默认页面
	 */
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "wms/extension/extension";
	}
	
	/*
	 * 展期列表
	 * @param asn
	 */
	@RequestMapping(value="json/{billNum}",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> json(@PathVariable("billNum") String billNum){
		List<Map<String,Object>> ybList = extensionService.getList(billNum);
		return ybList;
	}
	
	/**
	 * 展期参数设置页面
	 */
	@RequestMapping(value="manager", method = RequestMethod.GET)
	public String manager(Model model) {
//		User user = UserUtil.getCurrentUser();
		Extension extension = extensionService.get(1);
		model.addAttribute("extension",extension);
		return "wms/extension/extensionManager";
	}
	
	/*
	 * 展期参数设置
	 */
	@RequestMapping(value="change", method = RequestMethod.POST)
	@ResponseBody
	public String change(HttpServletRequest request, HttpServletResponse response){
		Extension extension = new Extension();
		parameterReflect.reflectParameter(extension, request);//转换对应实体类参数
		User user = UserUtil.getCurrentUser();
		extension.setUpdatePerson(user.getName());
		extension.setUpdateTime(new Date());
		extensionService.update(extension);
		return "success";
	}
	
	/*
	 * 延期操作
	 */
	@RequestMapping(value="later/{id}/{ztype}",method = RequestMethod.GET)
	@ResponseBody
	public String later(@PathVariable("id") String id,@PathVariable("ztype") String ztype,Model mode){
		Extension extension = extensionService.get(1);
		Integer num = extension.getExtensionNum();
		Integer dayAmount = extension.getExtensionDay();
		User user = UserUtil.getCurrentUser();
		//当类型为报关时
		if(ztype.equals("11") || ztype.equals("12")){
			BisCustomsDeclaration customs = customsDeclarationService.get(id);
			if(customs.getExtension() < num){
				customs.setExtension(customs.getExtension()+1);
				customs.setExtensionPerson(user.getName());
				customs.setExtensionTime(new Date());
				customs.setReleaseTime(DateUtils.addDay(customs.getReleaseTime(), dayAmount));
				customsDeclarationService.update(customs);
				return "success";
			}else{
				return "false";
			}
		}else if(ztype.equals("21") || ztype.equals("22")){
			BisCiqDeclaration ciq = ciqDeclarationService.get(id);
			if(ciq.getExtension() < num){
				ciq.setExtension(ciq.getExtension()+1);
				ciq.setExtensionPerson(user.getName());
				ciq.setExtensionTime(new Date());
				ciq.setReleaseTime(DateUtils.addDay(ciq.getReleaseTime(), dayAmount));
				ciqDeclarationService.update(ciq);
				return "success";
			}else{
				return "false";
			}
		}else{
			return "notype";
		}
	}

	
	/*
	 * 弹出预警报警
	 */
	@RequestMapping(value="outwarning", method = RequestMethod.GET)
	@ResponseBody
	public String outWarning(){
		List<Map<String,Object>> warningList = extensionService.getWarningList();
		if(!warningList.isEmpty()){
			String billNum = "";
			int size = warningList.size();
			for(int i=0;i<size;i++){
				billNum += warningList.get(i).get("BILLNUM") + " , " ;
			}
			if(!billNum.equals("")){
				billNum = billNum.substring(0, billNum.length()-2);
			}
			return billNum;
		}else{
			return "success";
		}
	}
}
