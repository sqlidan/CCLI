package com.haiersoft.ccli.base.web;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseClientRank;
import com.haiersoft.ccli.base.service.ClientRankService;
import com.haiersoft.ccli.base.service.ClientService;

/**
 * ClientRankController 客户优先级
 * @author pyl
 * @date 2017年6月19日
 */
@Controller
@RequestMapping("base/rank")
public class ClientRankController extends BaseController {

	@Autowired
	private ClientRankService clientRankService;
	@Autowired
	private ClientService clientService;

	/**
	 * 默认页面
	 */
	@RequestMapping(value="rankList",method = RequestMethod.GET)
	public String list() {
		return "base/rank/rankList";
	}
	
	/**
	 * 获取等级
	 */
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BaseClientRank> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = clientRankService.search(page, filters);
		return getEasyUIData(page);
	}

	/**
	 * 添加费目代码跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("rank", new BaseClientRank());
		model.addAttribute("action","create");
		return "base/rank/rankForm";
	}

	/**
	 * 添加rank
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BaseClientRank baseClientRank,Model model, HttpServletRequest request) {
		clientRankService.save(baseClientRank);
		return "success";
	}

	 

	/**
	 * 删除rank
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete/{rank}")
	@ResponseBody
	public String delete(@PathVariable("rank") String rank) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("customerLevel", rank);
		params.put("delFlag", 0);
		List<BaseClientInfo>  clientList = clientService.findBy(params);
		if(clientList.isEmpty()){
			clientRankService.delete(rank);
			return "success";
		}else{
			return "false";
		}
	}
	
    /**
     * Ajax请求校验Rank是否唯一。
     */
    @RequestMapping(value = "checkRank")
    @ResponseBody
    public String checkRank(String rank) {
        BaseClientRank obj = clientRankService.find("rank", rank);
        if (null!=obj) {
            return "false";
        } else {
            return "true";
        }
    }
    
    
    /**
     * @Description: 获得客户优先级设置(无视大小写)
     */
    @RequestMapping(value = "getRankAll", method = RequestMethod.GET)
    @ResponseBody
    public List<BaseClientRank> getRankAll(HttpServletRequest request, HttpServletResponse response) {
        List<BaseClientRank> rankInfos = new ArrayList<BaseClientRank>();
        String param = request.getParameter("q");// 搜索值
        String setid = request.getParameter("setid");// 原数据填充值
        if ((param != null && !"".equals(param)) || (setid != null && !"".equals(setid))) {
            if (param != null && !"".equals(param)) {
                List<Map<String, Object>> listC = clientRankService.findRank(param);
                int size = listC.size();
                String aa = "";
                String bb = "";
                BaseClientRank obj = null;
                for (int i = 0; i < size; i++) {
                	obj = new BaseClientRank();
                    aa = (String) listC.get(i).get("RANK");
                    obj.setRank(aa);
                    bb = (String) listC.get(i).get("NICK_NAME");
                    obj.setNickName(bb);
                    rankInfos.add(obj);
                }
            } else {
                // 根据原值id获取对象
                BaseClientRank getObj = clientRankService.get(setid);
                if (getObj != null) {
                	rankInfos.add(getObj);
                }

            }
        }
        return rankInfos;
    }
	
}