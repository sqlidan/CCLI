package com.haiersoft.ccli.supervision.web;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.alibaba.fastjson.JSONArray;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.report.entity.AAccountBook;
import com.haiersoft.ccli.report.service.AAccountBookService;
import com.haiersoft.ccli.system.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.service.FljgWsClient;
import com.haiersoft.ccli.supervision.service.GetKeyService;

/**
 * 分类监管 非保税账册查询controller
 * 
 * @author
 *
 */

@Controller
@RequestMapping("supervision/nonEmsQuery")
public class NonEmsQueryController extends BaseController {

	@Autowired
	GetKeyService getKeyService;
	@Autowired
	AAccountBookService aAccountBookService;

	@Autowired
	FljgWsClient fljgWsClient;

	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fljg/nonEmsQuery";
	}

	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request, String EmsNo, String CodeTs, String GNo) throws RemoteException, ServiceException {
		Page<Object> page = getPage(request);
		  //查询条件为空时返回空的列表 Map;
		  String returnstr="{\r\n" +
			  		"	\"state\": 1,\r\n" +
			  		"	\"message\": null,\r\n" +
			  		"	\"rows\": null,\r\n" +
			  		"	\"TotalCount\": 0,\r\n" +
			  		"	\"CheckInfos\": null\r\n" +
			  		"}";
		  Map<String, Object> empMaps = (Map<String, Object>)JSON.parse(returnstr);
		  if(StringUtils.isBlank(EmsNo) && StringUtils.isBlank(CodeTs) && StringUtils.isBlank(GNo)) {

			  return empMaps;

		  }

		  Map<String,Integer> pagemap = new HashMap<>();
		  pagemap.put("PageSize",page.getPageSize());
		  pagemap.put("PageIndex",page.getPageNo());

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("EmsNo", EmsNo);
		jsonObject.put("CodeTs", CodeTs);
		jsonObject.put("GNo", GNo);
		jsonObject.put("Page", pagemap);
		// 1 获得key
		// String tickid = getKeyService.builderTest();
		String tickId = getKeyService.builder();
		System.out.println(tickId);
		//如果获取tickId为空时返回空列表
		if((null ==tickId) || ("".equals(tickId)))
		{
			return empMaps;
		}
		// 2 调用接口
		// 调用保税账册查询
		String serviceName = "NonEmsGoodsQuery";

		String result = fljgWsClient.getResult(jsonObject, tickId, serviceName);
		System.out.println("result: " + result);

		Map<String, Object> maps = (Map<String, Object>) JSON.parse(result);
//		List<AAccountBook> aAccountBookList = JSONArray.parseArray(maps.get("PageList").toString(),AAccountBook.class);
//		if (aAccountBookList!=null && aAccountBookList.size() > 0){
//			for (AAccountBook forAAccountBook:aAccountBookList) {
//				if (forAAccountBook.getCutQty()!=null && Integer.parseInt(forAAccountBook.getCutQty()) <= 0){
//					forAAccountBook.setFinish("1");
//				}
//				List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
//				filters.add(new PropertyFilter("EQS_GNo", forAAccountBook.getGNo()));
//				List<AAccountBook> aAccountBookS = aAccountBookService.search(filters);
//				if (aAccountBookS!=null && aAccountBookS.size() > 0){
//					aAccountBookService.merge(aAccountBookS.get(0));
//				}else{
//					aAccountBookService.save(forAAccountBook);
//				}
//			}
//		}

		maps.put("rows", maps.remove("PageList"));
		maps.put("total", maps.remove("TotalCount"));
		return maps;
	}

}
