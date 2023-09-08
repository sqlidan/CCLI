package com.haiersoft.ccli.wms.web.PreEntryInvtQuery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.apiEntity.*;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfoDJ;
import com.haiersoft.ccli.wms.service.PreEntryInvtQuery.PreEntryInvtQueryService;
import com.haiersoft.ccli.wms.web.preEntry.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping(value = "wms/preEntryInvtQuery")
public class PreEntryInvtQueryController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PreEntryInvtQueryController.class);
	@Autowired
	private PreEntryInvtQueryService preEntryInvtQueryService;

	private static final String memberCode = "eimskipMember";
	private static final String pass = "66668888";
	private static final String icCode = "2100030046252";


	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list() {
		return "wms/preEntryInvtQuery/preEntryInvtQuery";
	}

	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisPreEntryInvtQuery> page = getPage(request);
		page.setOrder("asc");
		page.setOrderBy("createTime");
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = preEntryInvtQueryService.search(page, filters);
		return getEasyUIData(page);
	}

	@RequestMapping(value="getPreEntryInvtQuery/{bondInvtNo}",method = RequestMethod.GET)
	@ResponseBody
	public String getPreEntryInvtQuery(@PathVariable("bondInvtNo") String bondInvtNo) {
		if (bondInvtNo == null ||bondInvtNo.trim().length() == 0){
			return "未获取到核注清单号。";
		}

		List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();

		//查询保税核注清单列表
        InvtQueryListRequest invtQueryListRequest = new InvtQueryListRequest();
        invtQueryListRequest.setBondInvtNo(bondInvtNo);
        invtQueryListRequest.setMemberCode(memberCode);
        invtQueryListRequest.setPass(pass);
        invtQueryListRequest.setIcCode(icCode);
        //调用服务
        Map<String, Object> invtQueryListMap = InvtQueryListService(invtQueryListRequest);
        if("500".equals(invtQueryListMap.get("code").toString())){
        	if(invtQueryListMap.get("msg") ==null || invtQueryListMap.get("msg").toString().trim().length() == 0){
				return "error";
			}else{
				return invtQueryListMap.get("msg").toString().trim();
			}
		}else{
			//处理结果
			InvtQueryListResponse invtQueryListResponse = (InvtQueryListResponse) invtQueryListMap.get("data");
			List<InvtQueryListResponseResultList> invtQueryListResponseResultLists = invtQueryListResponse.getResultList();
			if(invtQueryListResponseResultLists != null && invtQueryListResponseResultLists.size() >0){
				for (InvtQueryListResponseResultList forInvtQueryListResponseResultList:invtQueryListResponseResultLists) {
					BisPreEntryInvtQuery insertBisPreEntryInvtQuery = new BisPreEntryInvtQuery();
					insertBisPreEntryInvtQuery.setBondInvtNo(bondInvtNo);

					//查询保税核注清单详细
					NemsCommonSeqNoRequest nemsCommonSeqNoRequest = new NemsCommonSeqNoRequest();
					nemsCommonSeqNoRequest.setSeqNo(forInvtQueryListResponseResultList.getSeqNo());
					nemsCommonSeqNoRequest.setIcCode(icCode);
					nemsCommonSeqNoRequest.setMemberCode(memberCode);
					nemsCommonSeqNoRequest.setPass(pass);
					//调用服务
					Map<String, Object> invtDetailMap = InvtDetailService(nemsCommonSeqNoRequest);
					if("500".equals(invtDetailMap.get("code").toString())){
						if(invtDetailMap.get("msg") ==null || invtDetailMap.get("msg").toString().trim().length() == 0){
							return "error";
						}else{
							return invtDetailMap.get("msg").toString().trim();
						}
					}else{
						//处理结果
						InvtMessage invtMessage = (InvtMessage) invtDetailMap.get("data");
						insertBisPreEntryInvtQuery.setInvtQueryList(JSON.toJSONString(forInvtQueryListResponseResultList));
						insertBisPreEntryInvtQuery.setInvtDecHeadType(JSON.toJSONString(invtMessage.getInvtDecHeadType()));
						insertBisPreEntryInvtQuery.setInvtDecListType(JSON.toJSONString(invtMessage.getInvtDecListType()));
						insertBisPreEntryInvtQuery.setInvtGoodsType(JSON.toJSONString(invtMessage.getInvtGoodsType()));
						insertBisPreEntryInvtQuery.setInvtHeadType(JSON.toJSONString(invtMessage.getInvtHeadType()));
						insertBisPreEntryInvtQuery.setInvtListType(JSON.toJSONString(invtMessage.getInvtListType()));
						insertBisPreEntryInvtQuery.setInvtWarehouseType(JSON.toJSONString(invtMessage.getInvtWarehouseType()));
						insertBisPreEntryInvtQuery.setListStat(JSON.toJSONString(invtMessage.getListStat()));
						insertBisPreEntryInvtQuery.setOperCusRegCode(JSON.toJSONString(invtMessage.getOperCusRegCode()));
						insertBisPreEntryInvtQuery.setSysId(JSON.toJSONString(invtMessage.getSysId()));

						bisPreEntryInvtQueryList.add(insertBisPreEntryInvtQuery);
					}
				}
			}
		}
        if(bisPreEntryInvtQueryList.size() > 0){
			for (BisPreEntryInvtQuery forBisPreEntryInvtQuery:bisPreEntryInvtQueryList) {
				List<BisPreEntryInvtQuery> queryBisPreEntryInvtQueryList = new ArrayList<>();
				queryBisPreEntryInvtQueryList = preEntryInvtQueryService.getList(bondInvtNo);
				if (queryBisPreEntryInvtQueryList!=null && queryBisPreEntryInvtQueryList.size() > 0){
					for (int i = 0; i < queryBisPreEntryInvtQueryList.size(); i++) {
						if(i == 0){
							BisPreEntryInvtQuery updateBisPreEntryInvtQuery = queryBisPreEntryInvtQueryList.get(i);
							updateBisPreEntryInvtQuery.setInvtQueryList(forBisPreEntryInvtQuery.getInvtQueryList());
							updateBisPreEntryInvtQuery.setInvtDecHeadType(forBisPreEntryInvtQuery.getInvtDecHeadType());
							updateBisPreEntryInvtQuery.setInvtDecListType(forBisPreEntryInvtQuery.getInvtDecListType());
							updateBisPreEntryInvtQuery.setInvtGoodsType(forBisPreEntryInvtQuery.getInvtGoodsType());
							updateBisPreEntryInvtQuery.setInvtHeadType(forBisPreEntryInvtQuery.getInvtHeadType());
							updateBisPreEntryInvtQuery.setInvtListType(forBisPreEntryInvtQuery.getInvtListType());
							updateBisPreEntryInvtQuery.setInvtWarehouseType((forBisPreEntryInvtQuery.getInvtWarehouseType()));
							updateBisPreEntryInvtQuery.setListStat(forBisPreEntryInvtQuery.getListStat());
							updateBisPreEntryInvtQuery.setOperCusRegCode(forBisPreEntryInvtQuery.getOperCusRegCode());
							updateBisPreEntryInvtQuery.setSysId(forBisPreEntryInvtQuery.getSysId());
							User user = UserUtil.getCurrentUser();
							updateBisPreEntryInvtQuery.setUpdateBy(user.getName());
							updateBisPreEntryInvtQuery.setUpdateTime(new Date());
							preEntryInvtQueryService.merge(updateBisPreEntryInvtQuery);
						}else{
							BisPreEntryInvtQuery deleteBisPreEntryInvtQuery = queryBisPreEntryInvtQueryList.get(i);
							preEntryInvtQueryService.delete(deleteBisPreEntryInvtQuery.getId());
						}
					}
				}else{
					User user = UserUtil.getCurrentUser();
					forBisPreEntryInvtQuery.setCreateBy(user.getName());
					forBisPreEntryInvtQuery.setCreateTime(new Date());
					preEntryInvtQueryService.save(forBisPreEntryInvtQuery);
				}
			}
		}
		return "success";
	}

//================================================================================================================================
	private static BisPreEntryInvtQuery bisPreEntryInvtQueryTemp;
	/**
	 * 查看核注清单详情
	 */
	@RequestMapping(value = "invtDetail/{id}", method = RequestMethod.GET)
	public String invtDetail(Model model, @PathVariable("id") String id) throws ParseException {
		BisPreEntryInvtQuery bisPreEntryInvtQuery = preEntryInvtQueryService.get(id);
		bisPreEntryInvtQueryTemp = bisPreEntryInvtQuery;
		model.addAttribute("ID", bisPreEntryInvtQuery.getId());
		if(bisPreEntryInvtQuery.getInvtHeadType() != null){
			InvtHeadType invtHeadType = JSON.parseObject(bisPreEntryInvtQuery.getInvtHeadType(),InvtHeadType.class);
			model.addAttribute("bisPreEntry", invtHeadType);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			if(invtHeadType.getInputTime()!=null && invtHeadType.getInputTime().toString().trim().length() >0){
				model.addAttribute("inputTime", sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInputTime()))));//录入日期
			}
			if(invtHeadType.getInputTime()!=null && invtHeadType.getInputTime().toString().trim().length() >0){
				model.addAttribute("invtDclTime", sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInvtDclTime()))));//清单申报日期
			}
			if(invtHeadType.getInputTime()!=null && invtHeadType.getInputTime().toString().trim().length() >0){
				model.addAttribute("entryDclTime", sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getEntryDclTime()))));//报关单申报日期
			}
		}
		return "wms/preEntryInvtQuery/preEntryInvtDetail";
	}

	@RequestMapping(value = "jsonInvtList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> jsonInvtList(HttpServletRequest request) {
		Page<InvtListType> page = getPage(request);
		List<InvtListType> invtListType = new ArrayList<>();
		if(bisPreEntryInvtQueryTemp.getInvtListType() != null){
			invtListType = JSON.parseArray(bisPreEntryInvtQueryTemp.getInvtListType(),InvtListType.class);
			page.setResult(invtListType);
			page.setTotalCount(invtListType.size());
		}else{
			page.setResult(invtListType);
			page.setTotalCount(invtListType.size());
		}
		return getEasyUIData(page);
	}

//	@RequestMapping(value = "jsonDJ/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public Map<String, Object> getDJData(HttpServletRequest request, @PathVariable("id") String id) {
//
//		return getEasyUIData(page);
//	}

//================================================================================================================================


	/**
	 * @return java.util.concurrent.Callable<com.chenay.bean.entity.customs.InvtQueryListResponse>
	 * @Author chenp
	 * @Description 保税核注清单列表查询服务
	 * @Date 9:14 2021/1/30
	 * @Param [invtQueryListRequest]
	 **/
	public Map<String,Object> InvtQueryListService(InvtQueryListRequest invtQueryListRequest) {
		logger.info("保税核注清单列表查询服务:"+JSON.toJSONString(invtQueryListRequest));
		Map<String,Object> resultMap = new HashMap<>();
		InvtQueryListResponse baseResult = new InvtQueryListResponse();
		String dataStr = "";
		try {
			invtQueryListRequest.setKey(ApiKey.保税监管_保税核注清单查询服务秘钥.getValue());
			String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_保税核注清单列表查询服务接口.getValue(), JSON.toJSONString(invtQueryListRequest, SerializerFeature.WriteNullStringAsEmpty));
			JSONObject jsonObject = JSON.parseObject(s);
			String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
			if ("200".equals(code)) {
				Object data = jsonObject.get("data");
				if (data != null) {
					dataStr = data.toString();
				}
				baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), InvtQueryListResponse.class);
				logger.error("baseResult "+JSON.toJSONString(baseResult));
				resultMap.put("code","200");
				resultMap.put("msg","success");
				resultMap.put("data",baseResult);
			} else {
				Object data = jsonObject.get("msg");
				if (data != null) {
					dataStr = data.toString();
				}
				logger.error("保税监管_保税核注清单列表查询服务接口"+invtQueryListRequest.getBondInvtNo(),"结果"+dataStr);
				logger.error(dataStr);
				resultMap.put("code","500");
				resultMap.put("msg",dataStr);
				resultMap.put("data",null);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return resultMap;
	}

	/**
	 * @return java.util.concurrent.Callable<com.chenay.bean.entity.customs.InvtMessage>
	 * @Author chenp
	 * @Description 保税核注清单详细查询服务
	 * @Date 9:12 2021/1/30
	 * @Param [nemsCommonSeqNoRequest]
	 **/
	public Map<String,Object> InvtDetailService(NemsCommonSeqNoRequest nemsCommonSeqNoRequest) {
		logger.info("保税核注清单详细查询服务:"+JSON.toJSONString(nemsCommonSeqNoRequest));
		Map<String,Object> resultMap = new HashMap<>();
		InvtMessage baseResult = new InvtMessage();
		String dataStr = "";
		try {
			nemsCommonSeqNoRequest.setKey(ApiKey.保税监管_保税核注清单查询服务秘钥.getValue());
			String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_保税核注清单详细查询接口.getValue(), JSON.toJSONString(nemsCommonSeqNoRequest));
			JSONObject jsonObject = JSON.parseObject(s);
			String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
			if ("200".equals(code)) {
				Object data = jsonObject.get("data");

				if (data != null) {
					dataStr = data.toString();
				}
				baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), InvtMessage.class);
				resultMap.put("code","200");
				resultMap.put("msg","success");
				resultMap.put("data",baseResult);
			} else {
				Object data = jsonObject.get("msg");
				if (data != null) {
					dataStr = data.toString();
				}
				logger.error("保税监管_保税核注清单详细查询接口"+nemsCommonSeqNoRequest.getBlsNo(),"结果"+dataStr);
				logger.error(dataStr);
				resultMap.put("code","500");
				resultMap.put("msg",dataStr);
				resultMap.put("data",null);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return resultMap;
	}

	/**
	 * @return java.util.concurrent.Callable<com.chenay.bean.entity.customs.BwlQueryListResponse>
	 * @Author chenp
	 * @Description 物流账册列表查询服务
	 * @Date 9:18 2021/1/30
	 * @Param [bwlQueryListRequest]
	 **/
	public Map<String,Object> BwlQueryListService(BwlQueryListRequest bwlQueryListRequest) {
		logger.info("物流账册列表查询服务:"+JSON.toJSONString(bwlQueryListRequest));
		Map<String,Object> resultMap = new HashMap<>();
		BwlQueryListResponse baseResult = new BwlQueryListResponse();
		try {
			bwlQueryListRequest.setKey(ApiKey.保税监管_物流账册查询服务秘钥.getValue());
			String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_物流账册列表查询服务接口.getValue(), JSON.toJSONString(bwlQueryListRequest, SerializerFeature.WriteNullStringAsEmpty));
			JSONObject jsonObject = JSON.parseObject(s);
			String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
			if ("200".equals(code)) {
				Object data = jsonObject.get("data");
				String dataStr = "";
				if (data != null) {
					dataStr = data.toString();
				}
				baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), BwlQueryListResponse.class);
				resultMap.put("code","200");
				resultMap.put("msg","success");
				resultMap.put("data",baseResult);
			} else {
				Object data = jsonObject.get("msg");
				String dataStr = "";
				if (data != null) {
					dataStr = data.toString();
				}
				logger.error("保税监管_物流账册列表查询服务接口"+bwlQueryListRequest.getBwlNo(),"结果"+dataStr);
				logger.error(dataStr);
				resultMap.put("code","500");
				resultMap.put("msg",dataStr);
				resultMap.put("data",null);
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return resultMap;
	}


	/**
	 * @return com.chenay.core.tool.api.R<com.chenay.bean.entity.customs.BwlMessage>
	 * @Author chenp
	 * @Description 物流账册详细数据查询服务
	 * @Date 9:18 2021/1/30
	 * @Param [sasCommonSeqNoRequest]
	 **/
	public Map<String,Object> BwlDetailService(SasCommonSeqNoRequest sasCommonSeqNoRequest) {
		logger.info("物流账册详细数据查询服务:"+JSON.toJSONString(sasCommonSeqNoRequest));
		Map<String,Object> resultMap = new HashMap<>();
		BwlMessage baseResult = new BwlMessage();
		try {
			sasCommonSeqNoRequest.setKey(ApiKey.保税监管_物流账册查询服务秘钥.getValue());
			String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_物流账册详细数据查询服务接口.getValue(), JSON.toJSONString(sasCommonSeqNoRequest, SerializerFeature.WriteNullStringAsEmpty));
			JSONObject jsonObject = JSON.parseObject(s);
			String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
			if ("200".equals(code)) {
				Object data = jsonObject.get("data");
				String dataStr = "";
				if (data != null) {
					dataStr = data.toString();
				}
				baseResult = JSON.toJavaObject(JSON.parseObject(dataStr), BwlMessage.class);
				resultMap.put("code","200");
				resultMap.put("msg","success");
				resultMap.put("data",baseResult);
			} else {
				Object data = jsonObject.get("msg");
				String dataStr = "";
				if (data != null) {
					dataStr = data.toString();
				}
				logger.error("保税监管_物流账册详细数据查询服务接口","结果"+dataStr);
				logger.error(dataStr);
				resultMap.put("code","500");
				resultMap.put("msg",dataStr);
				resultMap.put("data",null);
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return resultMap;
	}
}
