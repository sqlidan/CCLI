package com.haiersoft.ccli.wms.web;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BackHistoryInfo;
import com.haiersoft.ccli.wms.service.BackHistoryInfoService;
/**
 *  回库记录 BackHistoryInfoContorller
 */
@Controller
@RequestMapping("wms/back")
public class BackHistoryInfoContorller extends BaseController {
	@Autowired
	private BackHistoryInfoService backHistoryInfoService;
	@Autowired
	private StandingBookService standingBookService;
	 
	 /**
     * 入库联系单总览默认页面
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "wms/back/backStock";
    }

    
    /*
     * 列表页面table获取json
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
    	List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        Page<BackHistoryInfo> page = getPage(request);
        page.orderBy("id").order(Page.DESC);
        page = backHistoryInfoService.search(page, filters);
        
        return getEasyUIData(page);
    }
    
    /**
     * 回库点击计费按钮计算费用
     */
    @RequestMapping(value = "jf", method = RequestMethod.POST)
    @ResponseBody
    public String getFeeForBackData(HttpServletRequest request) {
    	User user=UserUtil.getCurrentUser();
    	String userName = user.getName();
    	String loadingNum = request.getParameter("loadingNum");
    	Page<BackHistoryInfo> page = getPage(request);
    	
    	
		try {
			 return backHistoryInfoService.crFeeForBack(page,loadingNum, userName);
			 
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
        
       
    }
    
    @RequestMapping(value = "checkifjf", method = RequestMethod.POST)
    @ResponseBody
    public String checkIfJf(HttpServletRequest request) {
    	/*User user=UserUtil.getCurrentUser();
    	String userName = user.getName();*/
    	String loadingNum = request.getParameter("loadingNum");
    	String billNum = request.getParameter("billNum");
    	
    	//Page<BisStandingBook> page = getPage(request);
    	BisStandingBook bs = new BisStandingBook();
    	bs.setBillNum(billNum);
    	bs.setRemark("因装车单"+loadingNum);
    	
		
    	List<BisStandingBook> page1	= standingBookService.checkIfJf(bs);
		//List<BisStandingBook> list = page1.getResult();
		
		if(page1.size()>0){
			return "false";
		}else{
			return "success";
		}
        
       
    }
    
}
