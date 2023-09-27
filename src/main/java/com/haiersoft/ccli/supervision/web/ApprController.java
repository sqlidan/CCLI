package com.haiersoft.ccli.supervision.web;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.xml.rpc.ServiceException;

import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.haiersoft.ccli.wms.service.OutStockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.haiersoft.ccli.base.service.HscodeService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.service.ApprHeadService;
import com.haiersoft.ccli.supervision.service.ApprInfoService;
import com.haiersoft.ccli.supervision.service.FljgWsClient;
import com.haiersoft.ccli.supervision.service.GetKeyService;

/**
 * 分类监管 申请单controller
 * 
 * @author
 *
 */

@Controller
@RequestMapping("supervision/appr")
public class ApprController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(ApprController.class);

	@Autowired
	private ApprHeadService apprHeadService;

	@Autowired
	private ApprInfoService apprInfoService;

	@Autowired
	HscodeService hscodeService;
	@Autowired
	GetKeyService getKeyService;
	@Autowired
	FljgWsClient fljgWsClient;

	@Autowired
	private EnterStockInfoService enterStockInfoService;

	@Autowired
	private OutStockInfoService outStockInfoService;

	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fljg/apprList";
	}

	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<ApprHead> page = getPage(request);
		page.orderBy("createTime").order("desc");
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = apprHeadService.search(page, filters);
		return getEasyUIData(page);
	}



	// 申请单申报
	@Transactional
	@RequestMapping(value = "apply/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String applyData(HttpServletRequest request, @PathVariable("id") String id) throws IOException, ServiceException {

		//根据linkId查询ApprHead
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_id", id));
		List<ApprHead> apprheadList = apprHeadService.search(filters);

		String jsonString = "";
		//根据ApprHead的ID查询ApprInfo
		if (!apprheadList.isEmpty()) {
			List<PropertyFilter> infofilters = new ArrayList<PropertyFilter>();
			infofilters.add(new PropertyFilter("EQS_headId", apprheadList.get(0).getId()));
			List<ApprInfo> apprInfoList = apprInfoService.search(infofilters);
			/*
			 * JSONObject jsonObject = new JSONObject(); jsonObject.put("ApprHead",
			 * apprheadList.get(0)); jsonObject.put("ApprLists", apprInfoList);
			 * jsonObject.put("DeclType", "1"); jsonObject.put("SaveType", "0");
			 * System.out.println(jsonObject);
			 */

			//拼装Json
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("SaveType", "1");//这里0为暂存，咱们不存在暂存申请，直接发送1
			map.put("DeclType", "1");

			//入区不用调整，出区需要调整
			if("2".equals(apprheadList.get(0).getIoType())){
				if(apprInfoList!=null && apprInfoList.size() > 1){//多个
					String tdNo = apprheadList.get(0).getItemNum();
					System.out.println("tdNo:"+tdNo);
					String[] tdNoAry = null;
					if(tdNo.contains(",")){
						tdNoAry = tdNo.split(",");
					}else{
						tdNoAry = new String[1];
						tdNoAry[0] = tdNo;
					}
					for (int i = 0; i < tdNoAry.length; i++) {
						List<BisEnterStockInfo> bisEnterStockInfoList = new ArrayList<BisEnterStockInfo>();
						List<PropertyFilter> enterStockFilters = PropertyFilter.buildFromHttpRequest(request);
						enterStockFilters.add(new PropertyFilter("EQS_itemNum", tdNoAry[i]));
						bisEnterStockInfoList = enterStockInfoService.search(enterStockFilters);
						for (ApprInfo forApprInfo : apprInfoList) {
							for (BisEnterStockInfo forBisEnterStockInfo:bisEnterStockInfoList) {
								if (forApprInfo.getgName().equals(forBisEnterStockInfo.getCargoName())) {
//								forApprInfo.setgNo(forBisEnterStockInfo.getAccountBook());//底账项号
									forApprInfo.setCodeTs(forBisEnterStockInfo.getHsCode());//HS编码/商品编码
								}
								apprInfoService.merge(forApprInfo);
							}
						}
					}

//					//原逻辑-依据联系单获取提单号，依据提单号获取入库申请单及明细
//					List<BisOutStockInfo> outStockInfos = new ArrayList<BisOutStockInfo>();
//					BisOutStockInfo outStockInfo = new BisOutStockInfo();
//					outStockInfo.setOutLinkId(linkId);
//					outStockInfos = outStockInfoService.searchBillCodeByLinkId(outStockInfo);
//					if(outStockInfos!=null && outStockInfos.size() >0){
//						for (BisOutStockInfo forBisOutStockInfo:outStockInfos) {
//							//依据出区获取入区
//							List<PropertyFilter> filtersIn = new ArrayList<PropertyFilter>();
//							filtersIn.add(new PropertyFilter("EQS_itemNum", forBisOutStockInfo.getBillNum()));
//							filtersIn.add(new PropertyFilter("EQS_ioType", "1"));
//							List<ApprHead> apprheadInList = apprHeadService.search(filtersIn);
//							//依据入区完善出区申报信息
//							if (!apprheadInList.isEmpty()) {
//								List<PropertyFilter> infofilterIn = new ArrayList<PropertyFilter>();
//								infofilterIn.add(new PropertyFilter("EQS_headId", apprheadInList.get(0).getId()));
//								List<ApprInfo> apprInfoInList = apprInfoService.search(infofilterIn);
//								logger.error("apprInfoList： "+JSON.toJSONString(apprInfoList));
//								logger.error("apprInfoInList： "+JSON.toJSONString(apprInfoInList));
//								for (ApprInfo forApprInfo:apprInfoList) {
//									for (ApprInfo forApprInfoIn:apprInfoInList) {
//										if(forApprInfo.getgName().equals(forApprInfoIn.getgName())){
//											forApprInfo.setgNo(forApprInfoIn.getgNo());//底账项号
//											forApprInfo.setCodeTs(forApprInfoIn.getCodeTs());//HS编码/商品编码
//										}
//									}
//									apprInfoService.merge(forApprInfo);
//								}
//							}
//						}
//					}
				}else{//一个
					//依据出区获取入区
					List<PropertyFilter> filtersIn = new ArrayList<PropertyFilter>();
					filtersIn.add(new PropertyFilter("EQS_itemNum", apprheadList.get(0).getItemNum()));
					filtersIn.add(new PropertyFilter("EQS_ioType", "1"));
					List<ApprHead> apprheadInList = apprHeadService.search(filtersIn);
					//依据入区完善出区申报信息
					if (!apprheadInList.isEmpty()) {
						List<PropertyFilter> infofilterIn = new ArrayList<PropertyFilter>();
						infofilterIn.add(new PropertyFilter("EQS_headId", apprheadInList.get(0).getId()));
						List<ApprInfo> apprInfoInList = apprInfoService.search(infofilterIn);
						logger.error("apprInfoList： "+JSON.toJSONString(apprInfoList));
						logger.error("apprInfoInList： "+JSON.toJSONString(apprInfoInList));
						for (ApprInfo forApprInfo:apprInfoList) {
							for (ApprInfo forApprInfoIn:apprInfoInList) {
								if(forApprInfo.getgName().equals(forApprInfoIn.getgName())){
									forApprInfo.setgNo(forApprInfoIn.getgNo());//底账项号
									forApprInfo.setCodeTs(forApprInfoIn.getCodeTs());//HS编码/商品编码
								}
							}
							apprInfoService.merge(forApprInfo);
						}
					}
				}
				for (ApprInfo forApprInfo:apprInfoList) {
					forApprInfo.setgUnit("035");//申报计量单位
				}
				logger.error("apprInfoList2： "+JSON.toJSONString(apprInfoList));
				map.put("ApprLists", apprInfoList);
				map.put("ApprHead", apprheadList.get(0));
			}else{
				for (ApprInfo forApprInfo:apprInfoList) {
					forApprInfo.setgUnit("035");//申报计量单位
				}
				logger.error("apprInfoList1： "+JSON.toJSONString(apprInfoList));
				map.put("ApprLists", apprInfoList);
				map.put("ApprHead", apprheadList.get(0));
			}

			ObjectMapper objectMapper = new ObjectMapper();
			//String json = objectMapper.writeValueAsString(apprheadList.get(0));
			String json = objectMapper.writeValueAsString(map);
			System.out.println(json);
			
			//删除不需要发送的字段
			JsonNode rootNode = objectMapper.readTree(json);			
			for (JsonNode personNode : rootNode) {
			    if (personNode instanceof ObjectNode) {
			        ObjectNode object = (ObjectNode) personNode;
			        object.remove("id");
			        object.remove("LocalStatus");

			    }else {
			        for (JsonNode sonNode : personNode) {
			        	 if (sonNode instanceof ObjectNode) {
						        ObjectNode object2 = (ObjectNode) sonNode;
						        System.out.println(object2);
						        object2.remove("id");
						        object2.remove("HeadId");
						        object2.remove("CREATETIME");
			        	 }
			        }
		        }
			}
			System.out.println(rootNode.toString());
			jsonString = rootNode.toString();

		}
		logger.error(">>>>>>>>>>>>>>>>>调用申请单申报json： "+jsonString);
		// 1 获得key
		String tickId = getKeyService.builder();
		System.out.println(tickId);
		// 2 调用接口
		//设置接口名
		String serviceName = "ApprSave";
		// 调用 申请单保存
		String result = fljgWsClient.getResult(jsonString, tickId, serviceName);
		System.out.println("ApprSave result: " + result);
		logger.error(">>>>>>>>>>>>>>>>>调用申请单申报result： "+result);
		/*
		 * //测试返回字符串 String tempRes = "{\r\n" + "    \"state\":1,\r\n" +
		 * "    \"message\":\"\",\r\n" + "    \"data\":\"A42302000000000001\",\r\n" +
		 * "    \"CheckInfos\":null\r\n" + "}";
		 */
		
		JSONObject jsonObject = JSON.parseObject(result);
        String state = jsonObject.getString("state");
        
        if(state.equals("1")) {
        	String data = jsonObject.getString("data");    
        	apprHeadService.updateArrpIdById(data,"1" ,id);
        	apprInfoService.updateArrpIdbyHeadId(data, id);
        	return "success";
        }else {
        	String CheckInfos = jsonObject.getString("CheckInfos");
        	return CheckInfos;

        }

//		return jsonObject.toJSONString();
	}
	


	//删除申请
	@Transactional
	@RequestMapping(value="/del/{ids}")
	@ResponseBody
	public String deleteAppr(@PathVariable("ids") List<String> ids) throws RemoteException, ServiceException {

		List<ApprHead> toDelRemote = new ArrayList<ApprHead>();
		List<ApprHead> toDelLocal = new ArrayList<ApprHead>();		
		//检查本地状态,先整理出需要接口删除的申请单
		for(String id : ids) {			
			ApprHead apprHead = apprHeadService.find("id", id);
			if((null ==apprHead.getApprId()) || ("".equals(apprHead.getApprId()))) {				
				toDelLocal.add(apprHead);
			}else {
				toDelRemote.add(apprHead);
			}
				
		}
		
		//需要调用接口删除的appr
		if(toDelRemote.size() != 0) {
			// 1 获得key
			String tickId = getKeyService.builder();
			System.out.println(tickId);
			if((null ==tickId) || ("".equals(tickId)))
			{
				return "没有获得tickId,请重新操作";
			}
			// 2 调用接口
			//设置接口名
			String serviceName = "ApprDelete";
			for(ApprHead app:toDelRemote) {
				String apprdelete="{\"KeyModel\":{\"ApprId\":\""+app.getApprId()+"\"}}";
				logger.error(">>>>>>>>>>>>>>>>>调用申请单删除json： "+apprdelete);				
				String result = fljgWsClient.getResult(apprdelete, tickId, serviceName);
				logger.error(">>>>>>>>>>>>>>>>>调用申请单申报result： "+result);
				System.out.println("result: " + result);
				JSONObject jsonObject = JSON.parseObject(result);
		        String state = jsonObject.getString("state");
		        //如果接口state返回1为删除成功
		        if(state.equals("1")) {
		        	//删除本地数据库中的记录
					apprHeadService.deleteById(app.getId());
					apprInfoService.deleteByHeadId(app.getId());
		        	return "success";
		        }else {
		        	
		        	return "接口错误";
		        }
			}
			
		}
		
		//不需要调用接口删除的 list
		for(ApprHead app:toDelLocal) {
			apprHeadService.deleteById(app.getId());
			apprInfoService.deleteByHeadId(app.getId());
		}

		return "success";
		
	}
	
	//作废申请
	@Transactional
	@RequestMapping(value="/cancel/{ids}")
	@ResponseBody
	public String cancelAppr(@PathVariable("ids") List<String> ids) throws RemoteException, ServiceException {

		List<ApprHead> toCancelRemote = new ArrayList<ApprHead>();
		List<ApprHead> toCancelLocal = new ArrayList<ApprHead>();		
		//检查本地状态,先整理出需要接口删除的申请单
		for(String id : ids) {			
			ApprHead apprHead = apprHeadService.find("id",id);
			if((null ==apprHead.getApprId()) || ("".equals(apprHead.getApprId()))) {		
				toCancelLocal.add(apprHead);
			}else {
				toCancelRemote.add(apprHead);
			}
				
		}
		
		//需要调用接口作废的appr
		if(toCancelRemote.size() != 0) {
			// 1 获得key
			String tickId = getKeyService.builder();
			System.out.println(tickId);
			if((null ==tickId) || ("".equals(tickId)))
			{
				return "没有获得tickId,请重新操作";
			}
			// 2 调用接口
			//设置接口名
			String serviceName = "ApprNullify";
			for(ApprHead app:toCancelRemote) {
				String apprdelete="{\"KeyModel\":{\"ApprId\":\""+app.getApprId()+"\"}}";
				logger.error(">>>>>>>>>>>>>>>>>调用申请单作废json： "+apprdelete);
				String result = fljgWsClient.getResult(apprdelete, tickId, serviceName);
				System.out.println("result: " + result);
				logger.error(">>>>>>>>>>>>>>>>>调用申请单作废result： "+ result);
				JSONObject jsonObject = JSON.parseObject(result);
		        String state = jsonObject.getString("state");
		        //如果接口state返回1为删除成功
		        if(state.equals("1")) {
		        	app.setDeclType("3");
		        	app.setLocalStatus("2");
					apprHeadService.update(app);
		        	return "success";
		        }else {
		        	
		        	return "接口错误";
		        }
			}
			
		}
		
		//不需要调用接口作废的 list
		for(ApprHead app:toCancelLocal) {
			app.setLocalStatus("2");
			apprHeadService.save(app);;
		}

		return "success";		
	}
	

	
	

}
