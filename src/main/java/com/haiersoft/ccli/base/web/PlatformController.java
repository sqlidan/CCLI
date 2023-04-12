package com.haiersoft.ccli.base.web;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.base.entity.BasePlatform;
import com.haiersoft.ccli.base.service.PlatformService;
/**
 * ClientRankController 客户优先级
 * @author pyl
 * @date 2017年6月19日
 */
@Controller
@RequestMapping("base/platform")
public class PlatformController extends BaseController {

	@Autowired
	private PlatformService platformService;

	/**
	 * 默认页面
	 */
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String list() {
		return "base/platform/platformList";
	}
	
	/**
	 * 获取月台口
	 */
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BasePlatform> page = getPage(request);
		page.setPageSize(35);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = platformService.search(page, filters);
		return getEasyUIData(page);
	}

	/**
	 * 添加月台口跳转 
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("platform", new BasePlatform());
		model.addAttribute("action","create");
		return "base/platform/platformadd";
	}

	/**
	 * 添加月台口
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BasePlatform platform,Model model, HttpServletRequest request) {
		platformService.save(platform);
		return "success";
	}

	/**
	 * 修改月台口跳转 
	 * 
	 * @param model
	 */
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String updateForm(Model model, HttpServletRequest request) {
		String id = request.getParameter("id");
		BasePlatform obj=platformService.get(Integer.valueOf(id));
		model.addAttribute("platform",obj);
		model.addAttribute("action","update");
		return "base/platform/platformadd";
	}
	
	/**
	 * 修改月台口
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid BasePlatform platform,Model model, HttpServletRequest request) {
		platformService.update(platform);
		return "success";
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		platformService.delete(id);
		return "success";
	}
	
    /**
     * Ajax请求校验月台口是否唯一。
     */
    @RequestMapping(value = "checkPlatform")
    @ResponseBody
    public String checkRank(String platform) {
        BasePlatform obj = platformService.find("platform", platform);
        if (null!=obj) {
            return "false";
        } else {
            return "true";
        }
    }
    
    
//    /**
//     * @Description: 获得客户优先级设置(无视大小写)
//     */
//    @RequestMapping(value = "getRankAll", method = RequestMethod.GET)
//    @ResponseBody
//    public List<BaseClientRank> getRankAll(HttpServletRequest request, HttpServletResponse response) {
//        List<BaseClientRank> rankInfos = new ArrayList<BaseClientRank>();
//        String param = request.getParameter("q");// 搜索值
//        String setid = request.getParameter("setid");// 原数据填充值
//        if ((param != null && !"".equals(param)) || (setid != null && !"".equals(setid))) {
//            if (param != null && !"".equals(param)) {
//                List<Map<String, Object>> listC = clientRankService.findRank(param);
//                int size = listC.size();
//                String aa = "";
//                String bb = "";
//                BaseClientRank obj = null;
//                for (int i = 0; i < size; i++) {
//                	obj = new BaseClientRank();
//                    aa = (String) listC.get(i).get("RANK");
//                    obj.setRank(aa);
//                    bb = (String) listC.get(i).get("NICK_NAME");
//                    obj.setNickName(bb);
//                    rankInfos.add(obj);
//                }
//            } else {
//                // 根据原值id获取对象
//                BaseClientRank getObj = clientRankService.get(setid);
//                if (getObj != null) {
//                	rankInfos.add(getObj);
//                }
//
//            }
//        }
//        return rankInfos;
//    }
    
}