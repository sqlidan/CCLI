package com.haiersoft.ccli.wms.web.PreEntryInvtQuery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.primitives.Bytes;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.bounded.service.BaseBoundedService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisCustomsClearance;
import com.haiersoft.ccli.wms.entity.BisCustomsClearanceInfo;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BaseBoundedList;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisCustomsClearanceInfoS;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisCustomsClearanceList;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.apiEntity.*;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclaration;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclarationInfo;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfoDJ;
import com.haiersoft.ccli.wms.service.CustomsClearanceInfoService;
import com.haiersoft.ccli.wms.service.CustomsClearanceService;
import com.haiersoft.ccli.wms.service.PreEntryInvtQuery.BaseBoundedListService;
import com.haiersoft.ccli.wms.service.PreEntryInvtQuery.CustomsClearanceInfoSService;
import com.haiersoft.ccli.wms.service.PreEntryInvtQuery.CustomsClearanceListService;
import com.haiersoft.ccli.wms.service.PreEntryInvtQuery.PreEntryInvtQueryService;
import com.haiersoft.ccli.wms.service.customsDeclaration.CDInfoService;
import com.haiersoft.ccli.wms.service.customsDeclaration.CDService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryInfoService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryService;
import com.haiersoft.ccli.wms.web.preEntry.HttpUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping(value = "wms/preEntryInvtQuery")
public class PreEntryInvtQueryController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PreEntryInvtQueryController.class);
	@Autowired
	private PreEntryInvtQueryService preEntryInvtQueryService;
	@Autowired
	private PreEntryService preEntryService;
	@Autowired
	private PreEntryInfoService preEntryInfoService;
	@Autowired
	private CustomsClearanceListService customsClearanceListService;
	@Autowired
	private CustomsClearanceInfoSService customsClearanceInfoSService;
	@Autowired
	private BaseBoundedListService baseBoundedListService;
	@Autowired
	private CDService cdService;
	@Autowired
	private CDInfoService cdInfoService;
	@Autowired
	private ClientService clientService;

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
//		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		Page<BisPreEntryInvtQuery> page = getPage(request);
		page.setOrder("desc");
		page.setOrderBy("orderTime");
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQS_synchronization", "1");
		filters.add(filter);
		page = preEntryInvtQueryService.search(page, filters);
		//解析数据
		List<InvtHeadTypeVo> invtHeadTypeVoList = new ArrayList<>();
		List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();
		bisPreEntryInvtQueryList = page.getResult();
		for (BisPreEntryInvtQuery forBisPreEntryInvtQuery:bisPreEntryInvtQueryList) {
			InvtHeadType invtHeadType = new InvtHeadType();
			try {
				invtHeadType = JSONObject.parseObject(JSON.toJSONString(ByteAryToObject(forBisPreEntryInvtQuery.getInvtHeadType())),InvtHeadType.class);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			InvtHeadTypeVo invtHeadTypeVo = new InvtHeadTypeVo();
			BeanUtils.copyProperties(invtHeadType,invtHeadTypeVo);
			invtHeadTypeVo.setId(forBisPreEntryInvtQuery.getId());
			invtHeadTypeVo.setEtpsInnerInvtNo(forBisPreEntryInvtQuery.getTdNo());
			invtHeadTypeVo.setListStat(forBisPreEntryInvtQuery.getListStat().replaceAll("\"","").replaceAll("'",""));

			invtHeadTypeVo.setCreateBy(forBisPreEntryInvtQuery.getCreateBy());
//			invtHeadTypeVo.setUpdateBy(forBisPreEntryInvtQuery.getUpdateBy());
			if(invtHeadType.getInvtDclTime()!=null && invtHeadType.getInvtDclTime().trim().length() > 0){
				try {
					invtHeadTypeVo.setCreateTime(sdf1.format(sdf2.parse(invtHeadType.getInvtDclTime())));
//					invtHeadTypeVo.setUpdateTime(sdf1.format(sdf2.parse(invtHeadType.getInvtDclTime())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			invtHeadTypeVoList.add(invtHeadTypeVo);
		}

		Page<InvtHeadTypeVo> page2 = getPage(request);
		page2.setResult(invtHeadTypeVoList);
		page2.setTotalCount(page.getTotalCount());
		return getEasyUIData(page2);
	}

	//单个核注清单查询
	@RequestMapping(value="getPreEntryInvtQuery/{bondInvtNo}",method = RequestMethod.GET)
	@ResponseBody
	public String getPreEntryInvtQuery(@PathVariable("bondInvtNo") String bondInvtNo) {
		if (bondInvtNo == null ||bondInvtNo.trim().length() == 0){
			return "未获取到核注清单号。";
		}

		//查询
		String result = null;
		try {
			result = invtQuery(bondInvtNo,false);
		} catch (IOException | ClassNotFoundException | ParseException e) {
			logger.info("单个核注清单查询异常:"+e.getMessage());
			e.printStackTrace();
		}
		if ("success".equals(result)){
			return "success";
		}else{
			return result;
		}
	}

	//批量核注清单同步
	@RequestMapping(value="synchronizationInvtQuery",method = RequestMethod.GET)
	@ResponseBody
	public String synchronizationInvtQuery() {
		String msg = "success";
		//获取要同步的核注清单号
		List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();
		bisPreEntryInvtQueryList = preEntryInvtQueryService.getListBySynchronization();
		if (bisPreEntryInvtQueryList != null && bisPreEntryInvtQueryList.size() > 0){
			for (BisPreEntryInvtQuery forBisPreEntryInvtQuery:bisPreEntryInvtQueryList) {
				if (forBisPreEntryInvtQuery.getBondInvtNo()!=null && forBisPreEntryInvtQuery.getBondInvtNo().toString().trim().length() > 0){
					//查询
					String result = null;
					try {
						result = invtQuery(forBisPreEntryInvtQuery.getBondInvtNo(),true);
					} catch (IOException | ClassNotFoundException | ParseException e) {
						logger.info("批量核注清单同步异常:"+e.getMessage());
						e.printStackTrace();
					}
					if ("success".equals(result)){
						logger.info("核注清单号："+forBisPreEntryInvtQuery.getBondInvtNo()+" 同步成功");
					}else{
						msg = msg + "核注清单号："+forBisPreEntryInvtQuery.getBondInvtNo()+" "+result +";";
					}
				}
			}
		}
		return msg;
	}

	//查询核注清单信息
	public String invtQuery(String bondInvtNo,Boolean createInfo) throws IOException, ClassNotFoundException, ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		User user = UserUtil.getCurrentUser();

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
						insertBisPreEntryInvtQuery.setInvtQueryList(ObjectToByteAry(forInvtQueryListResponseResultList));
						insertBisPreEntryInvtQuery.setInvtDecHeadType(ObjectToByteAry(invtMessage.getInvtDecHeadType()));
						insertBisPreEntryInvtQuery.setInvtDecListType(ObjectToByteAry(invtMessage.getInvtDecListType()));
						insertBisPreEntryInvtQuery.setInvtGoodsType(ObjectToByteAry(invtMessage.getInvtGoodsType()));
						insertBisPreEntryInvtQuery.setInvtHeadType(ObjectToByteAry(invtMessage.getInvtHeadType()));
						insertBisPreEntryInvtQuery.setInvtListType(ObjectToByteAry(invtMessage.getInvtListType()));
						insertBisPreEntryInvtQuery.setInvtWarehouseType(ObjectToByteAry(invtMessage.getInvtWarehouseType()));
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
				//获取核注清单数据
				InvtHeadType invtHeadType = new InvtHeadType();
				invtHeadType = JSONObject.parseObject(JSON.toJSONString(ByteAryToObject(forBisPreEntryInvtQuery.getInvtHeadType())), InvtHeadType.class);
				//整理提单号
				List<InvtListType> invtListType = new ArrayList<>();
				invtListType = JSONArray.parseArray(JSON.toJSONString(ByteAryToObject(forBisPreEntryInvtQuery.getInvtListType())),InvtListType.class);
				String tdNo = "";
				for (InvtListType forInvtListType:invtListType) {
					if(tdNo.trim().length() == 0){
						tdNo = tdNo + forInvtListType.getGdsMtno() + ",";
					}else{
						if (!tdNo.contains(forInvtListType.getGdsMtno())){
							tdNo = tdNo + forInvtListType.getGdsMtno() + ",";
						}
					}
				}
				if(tdNo.contains(",")){
					tdNo = tdNo.substring(0,tdNo.length()-1);
				}
				forBisPreEntryInvtQuery.setBondInvtNo(invtHeadType.getBondInvtNo());
				forBisPreEntryInvtQuery.setTdNo(tdNo);
				forBisPreEntryInvtQuery.setOrderTime(sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInvtDclTime()))));
				forBisPreEntryInvtQuery.setSynchronization("1");
				forBisPreEntryInvtQuery.setCreatePreEntry("1");

				List<BisPreEntryInvtQuery> queryBisPreEntryInvtQueryList = new ArrayList<>();
				queryBisPreEntryInvtQueryList = preEntryInvtQueryService.getList(invtHeadType.getBondInvtNo());
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
							updateBisPreEntryInvtQuery.setSynchronization(forBisPreEntryInvtQuery.getSynchronization());
							updateBisPreEntryInvtQuery.setCreatePreEntry(forBisPreEntryInvtQuery.getCreatePreEntry());
							updateBisPreEntryInvtQuery.setOrderTime(forBisPreEntryInvtQuery.getOrderTime());
							updateBisPreEntryInvtQuery.setTdNo(forBisPreEntryInvtQuery.getTdNo());
							updateBisPreEntryInvtQuery.setUpdateBy(user.getName());
							updateBisPreEntryInvtQuery.setUpdateTime(new Date());
							preEntryInvtQueryService.merge(updateBisPreEntryInvtQuery);
							if (createInfo){
								String result = createInfo(updateBisPreEntryInvtQuery);
								if ("success".equals(result)){

								}else{
									return result;
								}
							}
						}else{
							BisPreEntryInvtQuery deleteBisPreEntryInvtQuery = queryBisPreEntryInvtQueryList.get(i);
							preEntryInvtQueryService.delete(deleteBisPreEntryInvtQuery.getId());
						}
					}
				}else{
					forBisPreEntryInvtQuery.setCreateBy(user.getName());
					forBisPreEntryInvtQuery.setCreateTime(new Date());
					preEntryInvtQueryService.save(forBisPreEntryInvtQuery);
					if (createInfo){
						String result = createInfo(forBisPreEntryInvtQuery);
						if ("success".equals(result)){

						}else{
							return result;
						}
					}
				}
			}
		}
		return "success";
	}

//================================================================================================================================
	public static byte[] ObjectToByteAry(Object object) throws IOException{
		if(object == null){
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream sOut = null;
		sOut = new ObjectOutputStream(out);
		sOut.writeObject(object);
		sOut.flush();
		byte[] bytes = out.toByteArray();
		return bytes;
	}
	public static Object ByteAryToObject(byte[] bytes) throws IOException, ClassNotFoundException {
		if(bytes == null){
			return null;
		}
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream sIn = null;
		Object obj = null;
		sIn = new ObjectInputStream(in);
		obj = sIn.readObject();
		return obj;
	}
//	//测试
//	public static void main(String[] args) throws IOException, ClassNotFoundException {
////		String a1 = "{\"bizopEtpsNM\":\"青岛港怡之航冷链物流有限公司\",\"bondInvtNo\":\"QD423023I000001047\",\"bondInvtType\":\"0\",\"dclTypecd\":\"1\",\"dclcusFlag\":\"1\",\"dclcusTypeCD\":\"1\",\"impexpMarkcd\":\"I\",\"impexpPortCD\":\"4258\",\"invtDclTime\":\"20230907\",\"invtStucd\":\"0\",\"listStat\":\"P\",\"mtpckEndprdMarkcd\":\"I\",\"putrecNo\":\"T4230W000036\",\"rcvgdEtpsNM\":\"青岛港怡之航冷链物流有限公司\",\"seqNo\":\"I42302300000002395\",\"singleNo\":\"202300000101857246\",\"supvModeCD\":\"5000\",\"trspModecd\":\"9\",\"vrfdedMarkcd\":\"1\"}";
////		InvtQueryListResponseResultList invtQueryListResponseResultList1 = new InvtQueryListResponseResultList();
////		invtQueryListResponseResultList1 = JSON.parseObject(a1,InvtQueryListResponseResultList.class);
////		Object obj1 = changeObject(invtQueryListResponseResultList1);
////		invtQueryListResponseResultList1 = JSONObject.parseObject(JSON.toJSONString(obj1),InvtQueryListResponseResultList.class);
////		System.out.println("invtQueryListResponseResultList1 "+invtQueryListResponseResultList1);
////
////
////		String a2 = "{\"bizopEtpsNm\":\"青岛港怡之航冷链物流有限公司\",\"bizopEtpsSccd\":\"91370220395949850B\",\"bizopEtpsno\":\"3702631016\",\"bondInvtNo\":\"QD423023I000001047\",\"chgTmsCnt\":\"0\",\"dclEtpsNm\":\"青岛三坤物流有限公司\",\"dclEtpsSccd\":\"913702110530754601\",\"dclEtpsno\":\"3702289916\",\"dclPlcCuscd\":\"4230\",\"dclTypecd\":\"1\",\"dclcusFlag\":\"1\",\"dclcusTypecd\":\"1\",\"impexpMarkcd\":\"I\",\"impexpPortcd\":\"4258\",\"inputTime\":\"20230907\",\"invtDclTime\":\"20230907\",\"invtIochkptStucd\":\"1\",\"invtType\":\"0\",\"levyBlAmt\":\"0\",\"listStat\":\"0\",\"mtpckEndprdMarkcd\":\"I\",\"passportUsedTypeCd\":\"1\",\"prevdTime\":\"20230907\",\"putrecNo\":\"T4230W000036\",\"rcvgdEtpsNm\":\"青岛港怡之航冷链物流有限公司\",\"rcvgdEtpsno\":\"3702631016\",\"rltEntryBizopEtpsNm\":\"威海威东日综合食品有限公司\",\"rltEntryBizopEtpsSccd\":\"91371000613752889J\",\"rltEntryBizopEtpsno\":\"3710947212\",\"rvsngdEtpsSccd\":\"91370220395949850B\",\"seqNo\":\"I42302300000002395\",\"stshipTrsarvNatcd\":\"142\",\"supvModecd\":\"5000\",\"trspModecd\":\"9\",\"vrfdedMarkcd\":\"1\"}";
////		InvtHeadType invtHeadType1 = new InvtHeadType();
////		invtHeadType1 = JSON.parseObject(a2,InvtHeadType.class);
////		Object obj2 = changeObject(invtHeadType1);
////		invtHeadType1 = JSONObject.parseObject(JSON.toJSONString(obj2),InvtHeadType.class);
////		System.out.println("invtHeadType2 "+invtHeadType1);
//
//
////		String jsonString = "[{\"clyMarkcd\":\"0\",\"dclCurrcd\":\"502\",\"dclQty\":\"1500\",\"dclTotalAmt\":\"38250\",\"dclUnitcd\":\"120\",\"dclUprcAmt\":\"25.5\",\"destinationNatcd\":\"142\",\"entryGdsSeqno\":\"1\",\"gdecd\":\"1605550000\",\"gdsMtno\":\"威海威东日20230904\",\"gdsNm\":\"冻章鱼丸\",\"gdsSeqno\":\"1\",\"gdsSpcfModelDesc\":\"冻\",\"lawfQty\":\"7200\",\"lawfUnitcd\":\"035\",\"lvyrlfModecd\":\"1\",\"modfMarkcd\":\"0\",\"natcd\":\"142\",\"seqNo\":\"I42302300000002395\"}]";
////		List<InvtListType> aaList = new ArrayList<>();
////		aaList = JSONArray.parseArray(jsonString,InvtListType.class);
////		Object obj = changeObject(aaList);
////		List<InvtListType> bbList = new ArrayList<>();
////		bbList = JSONArray.parseArray(JSON.toJSONString(obj),InvtListType.class);
////		System.out.println("bbList"+JSON.toJSONString(bbList));
//
//	}

	//批量生成预报单
	@RequestMapping(value="createPreEntry",method = RequestMethod.GET)
	@ResponseBody
	public String createPreEntry() {
		String msg = "success";
		//获取要同步的核注清单号
		List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();
		bisPreEntryInvtQueryList = preEntryInvtQueryService.getListByCreatePreEntry();
		if (bisPreEntryInvtQueryList != null && bisPreEntryInvtQueryList.size() > 0){
			for (BisPreEntryInvtQuery forBisPreEntryInvtQuery:bisPreEntryInvtQueryList) {
				if (forBisPreEntryInvtQuery.getBondInvtNo()!=null && forBisPreEntryInvtQuery.getBondInvtNo().toString().trim().length() > 0){
					//查询
					String result = null;
					try {
						result = createPreEntry(forBisPreEntryInvtQuery);
					} catch (IOException | ClassNotFoundException | ParseException e) {
						logger.info("批量生成预报单异常:"+e.getMessage());
						e.printStackTrace();
					}
					if ("success".equals(result)){
						User user = UserUtil.getCurrentUser();
						forBisPreEntryInvtQuery.setUpdateBy(user.getName());
						forBisPreEntryInvtQuery.setUpdateTime(new Date());
						forBisPreEntryInvtQuery.setCreatePreEntry("1");
						preEntryInvtQueryService.merge(forBisPreEntryInvtQuery);
						logger.info("核注清单号："+forBisPreEntryInvtQuery.getBondInvtNo()+" 生成预报单成功");
					}else{
						msg = msg + "核注清单号："+forBisPreEntryInvtQuery.getBondInvtNo()+" "+result +";";
					}
				}
			}
		}
		return msg;
	}

	//生成预报单
	public String createPreEntry(BisPreEntryInvtQuery bisPreEntryInvtQuery) throws IOException, ClassNotFoundException, ParseException {
		//获取核注清单数据
		InvtHeadType invtHeadType = new InvtHeadType();
		List<InvtListType> invtListType = new ArrayList<>();
		if (bisPreEntryInvtQuery.getInvtHeadType() == null){
			return "生成预报单时未获取到表头信息";
		}else{
			invtHeadType = JSONObject.parseObject(JSON.toJSONString(ByteAryToObject(bisPreEntryInvtQuery.getInvtHeadType())),InvtHeadType.class);
		}
		if (bisPreEntryInvtQuery.getInvtListType() == null || "[]".equals(bisPreEntryInvtQuery.getInvtListType())){
			return "生成预报单时未获取到表体信息";
		}else{
			invtListType = JSONArray.parseArray(JSON.toJSONString(ByteAryToObject(bisPreEntryInvtQuery.getInvtListType())),InvtListType.class);
		}

		//创建预报单对象
		BisPreEntry bisPreEntry = new BisPreEntry();
		List<BisPreEntryInfo> bisPreEntryInfoList = new ArrayList<>();

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

		String linkId = UUID.randomUUID().toString();
		bisPreEntry.setForId(linkId);
		bisPreEntry.setState("5");//状态 5-申报核注清单通过，状态为5

		//服务项目
		if (invtHeadType.getImpexpMarkcd() == null || "I".equals(invtHeadType.getImpexpMarkcd())){
			bisPreEntry.setServiceProject("0");
		}else{
			bisPreEntry.setServiceProject("1");
		}
		User user = UserUtil.getCurrentUser();
		bisPreEntry.setCreateBy(user.getName());
		if(invtHeadType.getInputTime()!=null){
			bisPreEntry.setCreateTime(sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInputTime()))));
		}
		bisPreEntry.setUpdateBy(user.getName());
		if(invtHeadType.getInvtDclTime()!=null){
			bisPreEntry.setUpdateTime(sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInvtDclTime()))));
		}
		bisPreEntry.setJlAudit(user.getName());
		bisPreEntry.setJlAuditTime(bisPreEntry.getUpdateTime());
		bisPreEntry.setZgAudit(user.getName());
		bisPreEntry.setZgAuditTime(bisPreEntry.getUpdateTime());
		bisPreEntry.setUpAndDown("0");
		bisPreEntry.setCheckListNo(invtHeadType.getBondInvtNo());//核注清单号
		bisPreEntry.setDeclarationUnit(invtHeadType.getDclEtpsNm());//报关公司
		bisPreEntry.setBillNum(bisPreEntryInvtQuery.getTdNo());//提单号
		bisPreEntry.setCdNum(invtHeadType.getEntryNo() == null ? "" : invtHeadType.getEntryNo());//报关单号
		bisPreEntry.setClientName(invtHeadType.getRltEntryBizopEtpsNm());//客户名称
		bisPreEntry.setBillNum(invtHeadType.getApplyNo() == null ? "" : invtHeadType.getApplyNo().trim());//提单号
		bisPreEntry.setCtnCont(0);//箱量
		bisPreEntry.setTradeMode(invtHeadType.getTrspModecd());//贸易方式
		bisPreEntry.setCdSign(Integer.parseInt(invtHeadType.getDclcusFlag()));//报关标志
		bisPreEntry.setConsignee(invtHeadType.getRcvgdEtpsNm() == null ? "" : invtHeadType.getRcvgdEtpsNm());//收货人
		bisPreEntry.setConsignor(invtHeadType.getRltEntryBizopEtpsno());//发货人
		bisPreEntry.setContryOragin(invtHeadType.getStshipTrsarvNatcd());//原产国

		bisPreEntry.setYLRTYBH(invtHeadType.getSeqNo());//预录入统一编号
		bisPreEntry.setQDBH(invtHeadType.getBondInvtNo());//清单编号
		bisPreEntry.setQDLX(invtHeadType.getInvtType());//清单类型
		bisPreEntry.setZCBH(invtHeadType.getPutrecNo());//账册编号
		bisPreEntry.setJYDWBM(invtHeadType.getBizopEtpsno());//经营单位编码
		bisPreEntry.setJYDWSHXYDM(invtHeadType.getBizopEtpsSccd());//经营单位社会信用代码
		bisPreEntry.setJYDWMC(invtHeadType.getBizopEtpsNm());//经营单位名称
		bisPreEntry.setJGDWBM(invtHeadType.getRcvgdEtpsno());//加工单位编码
		bisPreEntry.setJGDWSHXYDM(invtHeadType.getRvsngdEtpsSccd());//加工单位社会信用代码
		bisPreEntry.setJGDWMC(invtHeadType.getRcvgdEtpsNm());//加工单位名称
		bisPreEntry.setSBDWBM(invtHeadType.getDclEtpsno());//申报单位编码
		bisPreEntry.setSBDWSHXYDM(invtHeadType.getDclEtpsSccd());//申报单位社会信用代码
		bisPreEntry.setSBDWMC(invtHeadType.getDclEtpsNm());//申报单位名称
		bisPreEntry.setLRDWBM(invtHeadType.getInputCode());//录入单位编码
		bisPreEntry.setLRDWSHXYDM(invtHeadType.getInputCreditCode());//录入单位社会信用代码
		bisPreEntry.setLRDWMC(invtHeadType.getInputName());//录入单位名称
		bisPreEntry.setQYNBBH(invtHeadType.getEtpsInnerInvtNo());//企业内部编号
		bisPreEntry.setSBLX(invtHeadType.getDclTypecd());//申报类型
		if(invtHeadType.getInputTime()!=null){
			bisPreEntry.setLRRQ(sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInputTime()))));//录入日期
		}
		if(invtHeadType.getInvtDclTime()!=null){
			bisPreEntry.setQDSBRQ(sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInvtDclTime()))));//清单申报日期
		}
		bisPreEntry.setLJCPBZ(invtHeadType.getMtpckEndprdMarkcd());//料件、成品标志
		bisPreEntry.setJGFS(invtHeadType.getSupvModecd());//监管方式
		bisPreEntry.setYSFS(invtHeadType.getTrspModecd());//运输方式
		bisPreEntry.setJCJGB(invtHeadType.getImpexpPortcd());//进出境关别
		bisPreEntry.setZGHG(invtHeadType.getDclPlcCuscd());//主管海关
		bisPreEntry.setHKBZ(invtHeadType.getVrfdedMarkcd());//核扣标志
		bisPreEntry.setQYG(invtHeadType.getStshipTrsarvNatcd());//起运国(地区)
		bisPreEntry.setQGJCKKZT(invtHeadType.getInvtIochkptStucd());//清单进出卡口状态
		bisPreEntry.setSBBBH(invtHeadType.getApplyNo());//申报表编号
		bisPreEntry.setLZLX(invtHeadType.getListType());//流转类型
		bisPreEntry.setBGBZ(invtHeadType.getDclcusFlag());//报关标志
		bisPreEntry.setSFXTSCBGD(invtHeadType.getGenDecFlag());//是否系统生成报关单
		bisPreEntry.setBGDLX(invtHeadType.getDecType());//报关单类型
		bisPreEntry.setBGLX(invtHeadType.getDclcusTypecd());//报关类型
		bisPreEntry.setDYBGDBH(invtHeadType.getEntryNo());//对应报关单编号
		bisPreEntry.setDYBGDDWBM(invtHeadType.getCorrEntryDclEtpsNo());//对应报关单申报单位编码
		bisPreEntry.setDYBGDDWSHXYDM(invtHeadType.getCorrEntryDclEtpsSccd());//对应报关单申报单位社会信用代码
		bisPreEntry.setDYBGDDWMC(invtHeadType.getCorrEntryDclEtpsNm());//对应报关单申报单位名称
		bisPreEntry.setGLBGDBH(invtHeadType.getRltEntryNo());//关联报关单编号
		bisPreEntry.setGLQDBH(invtHeadType.getRltInvtNo());//关联清单编号
		bisPreEntry.setGLSCBAH(invtHeadType.getRltPutrecNo());//关联手(账)册备案号
		bisPreEntry.setBLBGDJNSFHRBM(invtHeadType.getRltEntryBizopEtpsno());//关联报关单境内收发货人编码
		bisPreEntry.setBLBGDJNSFHRSHXYDM(invtHeadType.getRltEntryBizopEtpsSccd());//关联报关单境内收发货人社会信用代码
		bisPreEntry.setBLBGDJNSFHRMC(invtHeadType.getRltEntryBizopEtpsNm());//关联报关单境内收发货人名称
		bisPreEntry.setGLBGDSCXSDWBM(invtHeadType.getRltEntryRcvgdEtpsno());//关联报关单生产销售(消费使用)单位编码
		bisPreEntry.setGLBGDSCXSDWSHXYDM(invtHeadType.getRltEntryRvsngdEtpsSccd());//关联报关单生产销售(消费使用)单位社会信用代码
		bisPreEntry.setGLBGDSCXSDWMC(invtHeadType.getRltEntryRcvgdEtpsNm());//关联报关单生产销售(消费使用)单位名称
		bisPreEntry.setBLBGDSBDWBM(invtHeadType.getRltEntryDclEtpsno());//关联报关单申报单位编码
		bisPreEntry.setBLBGDSBDWSHXYDM(invtHeadType.getRltEntryDclEtpsSccd());//关联报关单申报单位社会信用代码
		bisPreEntry.setBLBGDSBDWMC(invtHeadType.getRltEntryDclEtpsNm());//关联报关单申报单位名称
		if(invtHeadType.getEntryDclTime()!=null){
			bisPreEntry.setBGDSBRQ(sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getEntryDclTime()))));//报关单申报日期
		}
		bisPreEntry.setBZ(invtHeadType.getRmk());//备注
		bisPreEntry.setBGDTYBH(invtHeadType.getEntrySeqNo());//报关单统一编号
		bisPreEntry.setCZYKH(invtHeadType.getIcCardNo());//操作员卡号

		String productName = "";
		String hsNo = "";
		Double price = 0.00;
		Double netWeight = 0.00;
		for (InvtListType forInvtListType:invtListType) {
			if(!productName.contains(forInvtListType.getGdsNm())){
				if(productName.trim().length() > 0){
					productName = productName + "," + forInvtListType.getGdsNm();
				}else{
					productName = forInvtListType.getGdsNm();
				}
			}
			if(!hsNo.contains(forInvtListType.getGdecd())){
				if(hsNo.trim().length() > 0){
					hsNo = hsNo + "," + forInvtListType.getGdecd();
				}else{
					hsNo = forInvtListType.getGdecd();
				}
			}
			BisPreEntryInfo bisPreEntryInfo = new BisPreEntryInfo();
			bisPreEntryInfo.setForId(linkId);
			bisPreEntryInfo.setXh(forInvtListType.getGdsSeqno());//序号
			bisPreEntryInfo.setBaxh(forInvtListType.getPutrecSeqno());//备案序号
			bisPreEntryInfo.setSplh(forInvtListType.getGdsMtno());//商品料号
			bisPreEntryInfo.setBgdspxh(forInvtListType.getEntryGdsSeqno());//报关单商品序号
			bisPreEntryInfo.setLzsbbxh(forInvtListType.getApplyTbSeqno());//流转申报表序号
			bisPreEntryInfo.setSpxh(forInvtListType.getGdecd());//商品编号
			bisPreEntryInfo.setSpmc(forInvtListType.getGdsNm());//商品名称
			bisPreEntryInfo.setGgxh(forInvtListType.getGdsSpcfModelDesc());//规格型号
			bisPreEntryInfo.setBzt(forInvtListType.getDclCurrcd());//币制
			bisPreEntryInfo.setSbjldw(forInvtListType.getDclUnitcd());//申报计量单位
			bisPreEntryInfo.setFdjldw(forInvtListType.getLawfUnitcd());//法定计量单位
			bisPreEntryInfo.setFddejldw(forInvtListType.getSecdLawfUnitcd());//法定第二计量单位
			if(forInvtListType.getDclQty() == null || forInvtListType.getDclQty().trim().length() == 0){
				bisPreEntryInfo.setSbsl(Double.parseDouble("0"));//申报数量
			}else{
				price = price + Double.parseDouble(forInvtListType.getDclQty());
				bisPreEntryInfo.setSbsl(Double.parseDouble(forInvtListType.getDclQty()));//申报数量
			}
			if(forInvtListType.getLawfQty() == null || forInvtListType.getLawfQty().trim().length() == 0){
				bisPreEntryInfo.setFdsl(Double.parseDouble("0"));//法定数量
			}else{
				bisPreEntryInfo.setFdsl(Double.parseDouble(forInvtListType.getLawfQty()));//法定数量
			}
			bisPreEntryInfo.setDefdsl(forInvtListType.getSecdLawfQty());//第二法定数量
			if(forInvtListType.getDclUprcAmt() == null || forInvtListType.getDclUprcAmt().trim().length() == 0){
				bisPreEntryInfo.setQysbdj(Double.parseDouble("0"));//企业申报单价
			}else{
				bisPreEntryInfo.setQysbdj(Double.parseDouble(forInvtListType.getDclUprcAmt()));//企业申报单价
			}
			if(forInvtListType.getDclTotalAmt() == null || forInvtListType.getDclTotalAmt().trim().length() == 0){
				bisPreEntryInfo.setQysbzj(Double.parseDouble("0"));//企业申报总价
			}else{
				bisPreEntryInfo.setQysbzj(Double.parseDouble(forInvtListType.getDclTotalAmt()));//企业申报总价
			}
			if(forInvtListType.getUsdStatTotalAmt() == null || forInvtListType.getUsdStatTotalAmt().trim().length() == 0){
				bisPreEntryInfo.setMytjzje(Double.parseDouble("0"));//美元统计总金额
			}else{
				bisPreEntryInfo.setMytjzje(Double.parseDouble(forInvtListType.getUsdStatTotalAmt()));//美元统计总金额
			}
			bisPreEntryInfo.setYcg(forInvtListType.getNatcd());//原产国(地区)
			bisPreEntryInfo.setZlblyz(forInvtListType.getWtSfVal());//重量比例因子
			bisPreEntryInfo.setDyblyz(forInvtListType.getFstSfVal());//第一比例因子
			bisPreEntryInfo.setDeblyz(forInvtListType.getSecdSfVal());//第二比例因子
			if(forInvtListType.getGrossWt() == null || forInvtListType.getGrossWt().trim().length() == 0){
				bisPreEntryInfo.setMz(Double.parseDouble("0"));//毛重
			}else{
				bisPreEntryInfo.setMz(Double.parseDouble(forInvtListType.getGrossWt()));//毛重
			}
			if(forInvtListType.getNetWt() == null || forInvtListType.getNetWt().trim().length() == 0){
				bisPreEntryInfo.setJz(Double.parseDouble("0"));//净重
			}else{
				netWeight = netWeight + Double.parseDouble(forInvtListType.getNetWt());
				bisPreEntryInfo.setJz(Double.parseDouble(forInvtListType.getNetWt()));//净重
			}
			bisPreEntryInfo.setZmfs(forInvtListType.getLvyrlfModecd());//征免方式
			bisPreEntryInfo.setDhbbh(forInvtListType.getUcnsVerno());//单耗版本号
			bisPreEntryInfo.setZzmdg(forInvtListType.getDestinationNatcd());//最终目的国
			bisPreEntryInfo.setXgbz(forInvtListType.getModfMarkcd());//修改标志
			bisPreEntryInfo.setRemark(forInvtListType.getRmk());//备注

			bisPreEntryInfoList.add(bisPreEntryInfo);
		}

		if(invtListType.size() > 0){
			bisPreEntry.setProductName(productName);//品名
			bisPreEntry.setHsNo(hsNo);//商品编码
			bisPreEntry.setPrice(price.toString());//件数
			bisPreEntry.setNetWeight(netWeight);//重量
		}
		preEntryService.save(bisPreEntry);
		if (bisPreEntryInfoList.size() > 0){
			for (BisPreEntryInfo forBisPreEntryInfo:bisPreEntryInfoList) {
				preEntryInfoService.save(forBisPreEntryInfo);
			}
		}
		return "success";
	}

//================================================================================================================================
		//批量生成台账
		@RequestMapping(value="createClearance",method = RequestMethod.GET)
		@ResponseBody
		public String createClearance() {
			String msg = "success";
			//获取要同步的核注清单号
			List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();
			bisPreEntryInvtQueryList = preEntryInvtQueryService.getListByCreateClearance();
			if (bisPreEntryInvtQueryList != null && bisPreEntryInvtQueryList.size() > 0){
				for (BisPreEntryInvtQuery forBisPreEntryInvtQuery:bisPreEntryInvtQueryList) {
					if (forBisPreEntryInvtQuery.getBondInvtNo()!=null && forBisPreEntryInvtQuery.getBondInvtNo().toString().trim().length() > 0){
						//查询
						String result = null;
						try {
							result = createInfo(forBisPreEntryInvtQuery);
						} catch (IOException | ClassNotFoundException e) {
							logger.info("批量生成预报单异常:"+e.getMessage());
							e.printStackTrace();
						}
						if ("success".equals(result)){
							User user = UserUtil.getCurrentUser();
							forBisPreEntryInvtQuery.setUpdateBy(user.getName());
							forBisPreEntryInvtQuery.setUpdateTime(new Date());
							forBisPreEntryInvtQuery.setCreateClearance("1");
							preEntryInvtQueryService.merge(forBisPreEntryInvtQuery);
							logger.info("核注清单号："+forBisPreEntryInvtQuery.getBondInvtNo()+" 生成预报单成功");
						}else{
							msg = msg + "核注清单号："+forBisPreEntryInvtQuery.getBondInvtNo()+" "+result +";";
						}
					}
				}
			}
			return msg;
		}

	//生成台账底账
	public String createInfo(BisPreEntryInvtQuery bisPreEntryInvtQuery) throws IOException, ClassNotFoundException {
		InvtHeadType invtHeadType = new InvtHeadType();
		List<InvtListType> invtListType = new ArrayList<>();
		if (bisPreEntryInvtQuery.getInvtHeadType() == null){
			return "生成台账时未获取到表头信息";
		}else{
			invtHeadType = JSONObject.parseObject(JSON.toJSONString(ByteAryToObject(bisPreEntryInvtQuery.getInvtHeadType())),InvtHeadType.class);
		}
		if (bisPreEntryInvtQuery.getInvtListType() == null || "[]".equals(bisPreEntryInvtQuery.getInvtListType())){
			return "生成台账时未获取到表体信息";
		}else{
			invtListType = JSONArray.parseArray(JSON.toJSONString(ByteAryToObject(bisPreEntryInvtQuery.getInvtListType())),InvtListType.class);
		}

		//业务单号
		User user = UserUtil.getCurrentUser();
		String userCode = user.getUserCode();
		//判断用户码是否为空
		if (StringUtils.isNull(userCode)) {
			userCode = "YZH";
		} else {//判断用户码 的长度
			if (userCode.length() > 3) {
				userCode = userCode.substring(0, 3);
			} else if (userCode.length() < 3) {
				userCode = StringUtils.lpadStringLeft(3, userCode);
			}
		}
		String linkId = "F" + "YZH" + StringUtils.timeToString();
		//添加台账
		BisCustomsClearanceList bisCustoms = new BisCustomsClearanceList();
		bisCustoms.setCdNum(linkId);//业务单号
		bisCustoms.setBillNum(bisPreEntryInvtQuery.getTdNo());//提单号
		bisCustoms.setContryOragin(invtListType.get(0).getNatcd());//原产国
		bisCustoms.setCustomsDeclarationNumber(invtHeadType.getEntryNo() == null ? "" : invtHeadType.getEntryNo());//报关单号
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		if(invtHeadType.getInvtDclTime() != null && invtHeadType.getInvtDclTime().trim().length() > 0){
			Date declareTime = null;
			try {
				declareTime = sdf.parse(sdf.format(sdf2.parse(invtHeadType.getInvtDclTime())));
			}catch (Exception e){
				logger.error(e.getMessage());
			}
			bisCustoms.setDeclareTime(declareTime);//申报日期
		}
		bisCustoms.setBillNum(bisPreEntryInvtQuery.getTdNo());//提单号
		//服务项目
		if (invtHeadType.getImpexpMarkcd() == null) {
			bisCustoms.setServiceProject("0");//报进
		} else {
			if ("I".equals(invtHeadType.getImpexpMarkcd())) {//进口
				bisCustoms.setServiceProject("0");//报进
			} else if ("E".equals(invtHeadType.getImpexpMarkcd())) {
				bisCustoms.setServiceProject("1");//报出
			}
		}
		bisCustoms.setConsignee(invtHeadType.getRcvgdEtpsNm() == null ? "" : invtHeadType.getRcvgdEtpsNm());//收货人
		bisCustoms.setConsignor(invtHeadType.getRltEntryBizopEtpsNm() == null ? invtHeadType.getRltEntryRcvgdEtpsNm() == null? "":invtHeadType.getRltEntryRcvgdEtpsNm() : invtHeadType.getRltEntryBizopEtpsNm());//发货人
		bisCustoms.setUseUnit(invtHeadType.getRcvgdEtpsNm() == null ? invtHeadType.getRltEntryRcvgdEtpsNm() == null ? "" : invtHeadType.getRltEntryRcvgdEtpsNm() : invtHeadType.getRcvgdEtpsNm());//消费者使用单位/收货人/收货企业名称
		bisCustoms.setModeTrade("0");//贸易方式
		bisCustoms.setStoragePlace("青岛港怡之航冷链物流有限公司");//存放地点
		bisCustoms.setPortEntry("黄岛");//入境口岸
		if (invtHeadType.getStshipTrsarvNatcd() != null && invtHeadType.getStshipTrsarvNatcd().trim().length() > 0) {
			//起运国，从字典中依据编号转成名称
			List<BisPreEntryDictData> bisPreEntryDictDataList = new ArrayList<>();
			bisPreEntryDictDataList = preEntryService.getDictDataByCode("CUS_STSHIP_TRSARV_NATCD");
			for (BisPreEntryDictData forBisPreEntryDictData : bisPreEntryDictDataList) {
				if (forBisPreEntryDictData.getValue().equals(invtHeadType.getStshipTrsarvNatcd()) || forBisPreEntryDictData.getLabel().equals(invtHeadType.getStshipTrsarvNatcd())) {
					bisCustoms.setContryDeparture(forBisPreEntryDictData.getLabel());//起运国
					break;
				}
			}
		} else {
			bisCustoms.setContryDeparture(null);//起运国
		}

		bisCustoms.setAuditingState("3");//审核通过
		bisCustoms.setClientId(null);//客户ID
		bisCustoms.setClientName(invtHeadType.getRltEntryBizopEtpsNm());//客户名称
		bisCustoms.setCargoClientId(null);
		bisCustoms.setCargoClientName(invtHeadType.getRltEntryBizopEtpsNm() == null ? invtHeadType.getRltEntryRcvgdEtpsNm() == null? "":invtHeadType.getRltEntryRcvgdEtpsNm() : invtHeadType.getRltEntryBizopEtpsNm());//货权方名称/消费者使用单位/收货人/收货企业名称
		bisCustoms.setComments(invtHeadType.getRmk());//备注
		bisCustoms.setOperator(user.getName());//创建人
		bisCustoms.setOperateTime(new Date());//创建事件
		bisCustoms.setExaminePerson(user.getName());//审核人
		bisCustoms.setExamineTime(new Date());//创建事件
		customsClearanceListService.save(bisCustoms);

		//币制，从字典中依据编号转成名称
		List<BisPreEntryDictData> bisPreEntryDictDataList = new ArrayList<>();
		bisPreEntryDictDataList = preEntryService.getDictDataByCode("CUS_DCLCURRCD");
		//清单明细
		for (InvtListType forInvtListType : invtListType) {
			//添加台账明细
			BisCustomsClearanceInfoS bisCustomsClearanceInfo = new BisCustomsClearanceInfoS();
			bisCustomsClearanceInfo.setCusId(linkId);//业务ID
			bisCustomsClearanceInfo.setCommodityCode(forInvtListType.getGdecd() == null ? "" : forInvtListType.getGdecd().trim());//商品编码
			bisCustomsClearanceInfo.setCommodityName(forInvtListType.getGdsNm() == null ? "" : forInvtListType.getGdsNm().trim());//商品名称
			bisCustomsClearanceInfo.setLatinName("");//拉丁文名
			bisCustomsClearanceInfo.setSpecification(forInvtListType.getGdsSpcfModelDesc() == null ? "" : forInvtListType.getGdsSpcfModelDesc().trim());//规格
			bisCustomsClearanceInfo.setNum(BigDecimal.valueOf(Double.parseDouble(forInvtListType.getDclQty() == null ? "0" : forInvtListType.getDclQty().toString().trim().length() == 0 ? "0" : forInvtListType.getDclQty().trim())));//数量
			bisCustomsClearanceInfo.setNetWeight(BigDecimal.valueOf(Double.parseDouble(forInvtListType.getNetWt() == null ? "0" : forInvtListType.getNetWt().toString().trim().length() == 0 ? "0" : forInvtListType.getNetWt().trim())));//净重
			bisCustomsClearanceInfo.setGrossWeight(BigDecimal.valueOf(Double.parseDouble(forInvtListType.getGrossWt() == null ? "0" : forInvtListType.getGrossWt().toString().trim().length() == 0 ? "0" : forInvtListType.getGrossWt().trim())));//毛重
			if (forInvtListType.getUsdStatTotalAmt() != null && forInvtListType.getUsdStatTotalAmt().toString().length() > 0) {
				String usdStatTotalAmtS = forInvtListType.getUsdStatTotalAmt().toString();
				Double usdStatTotalAmtD = Double.parseDouble(usdStatTotalAmtS);
				BigDecimal usdStatTotalAmtB = BigDecimal.valueOf(usdStatTotalAmtD);
				bisCustomsClearanceInfo.setMoney(usdStatTotalAmtB);//金额
			} else {
				bisCustomsClearanceInfo.setMoney(new BigDecimal(0));//金额
			}
			if (forInvtListType.getDclCurrcd() != null && forInvtListType.getDclCurrcd().trim().length() > 0) {
				for (BisPreEntryDictData forBisPreEntryDictData : bisPreEntryDictDataList) {
					if (forBisPreEntryDictData.getValue().equals(forInvtListType.getDclCurrcd()) || forBisPreEntryDictData.getLabel().equals(forInvtListType.getDclCurrcd())) {
						bisCustomsClearanceInfo.setCurrencyValue(forBisPreEntryDictData.getLabel());//币制
						break;
					}
				}
			}
			if (forInvtListType.getPutrecSeqno() != null && forInvtListType.getPutrecSeqno().trim().length() > 0) {
				bisCustomsClearanceInfo.setAccountBook(forInvtListType.getPutrecSeqno());//账册商品序号
			}
			bisCustomsClearanceInfo.setFirmName("");//生产企业名称及注册号
			bisCustomsClearanceInfo.setPackagedForm("");//包装形式
			bisCustomsClearanceInfo.setIfWoodenPacking("");//有无木质包装
			bisCustomsClearanceInfo.setWoodenNo("");//木托编号
			customsClearanceInfoSService.save(bisCustomsClearanceInfo);
			logger.info("新增通关台账信息成功");
//			//通关底账
//			if ("I".equals(invtHeadType.getImpexpMarkcd())) {//进口(报进)
//				try {
//					//只需要添加一条底账信息即可
//					BaseBoundedList bounded = new BaseBoundedList();
//					bounded.setClientId(bisCustoms.getClientId());//客户ID
//					bounded.setClientName(bisCustoms.getClientName());//客户名称
//					bounded.setBillNum(bisCustoms.getBillNum());//提单号
//					bounded.setCdNum(bisCustoms.getCustomsDeclarationNumber());//报关单号
//					bounded.setCtnNum(null);//MR/集装箱号
//					bounded.setItemName(null);//货物描述
//					bounded.setStorageDate(null);//入库时间
//					bounded.setPiece(0);//件数
//					bounded.setNetWeight(0.00);//总净值
//					bounded.setCustomerServiceName(user.getName());//所属客服
//					bounded.setHsCode(forInvtListType.getGdecd());//HS编码
//					bounded.setHsItemname(bisCustomsClearanceInfo.getCommodityName());//海关品名
//					bounded.setAccountBook(bisCustomsClearanceInfo.getAccountBook());//账册商品序号
//					bounded.setHsQty(Double.parseDouble(bisCustomsClearanceInfo.getGrossWeight().toString()));//海关库存重量
//					bounded.setTypeSize(bisCustomsClearanceInfo.getSpecification());//规格
//					bounded.setCargoLocation(null);//库位
//					bounded.setCreatedTime(new Date());//创建时间
//					bounded.setUpdatedTime(new Date());//修改时间
//					bounded.setCargoArea(null);//库区
//					bounded.setDclQty(Double.parseDouble(bisCustomsClearanceInfo.getNum().toString()));//申报重量
//					bounded.setDclUnit(forInvtListType.getDclUnitcd() == null ? "" : forInvtListType.getDclUnitcd());//申报计量单位
//					logger.info("新增通关底账信息=> " + JSON.toJSONString(bounded));
//					baseBoundedListService.save(bounded);
//					logger.info("新增通关底账信息成功");
//				} catch (Exception e) {
//					logger.error("新增通关底账信息失败=> " + e.getMessage());
//				}
//			}else if ("E".equals(invtHeadType.getImpexpMarkcd())) {
//				//出区
//				BaseBoundedList bounded = new BaseBoundedList();
//				bounded = baseBoundedListService.find("accountBook",forInvtListType.getPutrecSeqno());
//				if(bounded!=null && bounded.getDclQty() != null){
//					//有数据扣减
//					Double dclQtyOrg = bounded.getDclQty();
//					bounded.setDclQty(bounded.getDclQty() - Double.parseDouble(forInvtListType.getDclQty()));//申报重量
//					bounded.setUpdatedTime(new Date());
//					logger.info("修改通关底账信息==>(" + dclQtyOrg.toString() + "==>" + bounded.getDclQty() + ") " + JSON.toJSONString(bounded));
//					baseBoundedListService.merge(bounded);
//					logger.info("修改通关底账信息成功");
//				}else{
//					try {
//						//没数据新增为负数
//						BaseBoundedList insertounded = new BaseBoundedList();
//						insertounded.setClientId(bisCustoms.getClientId());//客户ID
//						insertounded.setClientName(bisCustoms.getClientName());//客户名称
//						insertounded.setBillNum(bisCustoms.getBillNum());//提单号
//						insertounded.setCdNum(bisCustoms.getCustomsDeclarationNumber());//报关单号
//						insertounded.setCtnNum(null);//MR/集装箱号
//						insertounded.setItemName(null);//货物描述
//						insertounded.setStorageDate(null);//入库时间
//						insertounded.setPiece(0);//件数
//						insertounded.setNetWeight(0.00);//总净值
//						insertounded.setCustomerServiceName(user.getName());//所属客服
//						insertounded.setHsCode(forInvtListType.getGdecd());//HS编码
//						insertounded.setHsItemname(bisCustomsClearanceInfo.getCommodityName());//海关品名
//						insertounded.setAccountBook(bisCustomsClearanceInfo.getAccountBook());//账册商品序号
//						insertounded.setHsQty(Double.parseDouble(bisCustomsClearanceInfo.getGrossWeight().toString()));//海关库存重量
//						insertounded.setTypeSize(bisCustomsClearanceInfo.getSpecification());//规格
//						insertounded.setCargoLocation(null);//库位
//						insertounded.setCreatedTime(new Date());//创建时间
//						insertounded.setUpdatedTime(new Date());//修改时间
//						insertounded.setCargoArea(null);//库区
//						insertounded.setDclQty(Double.parseDouble("0") - Double.parseDouble(forInvtListType.getDclQty()));//申报重量
//						insertounded.setDclUnit(forInvtListType.getDclUnitcd() == null ? "" : forInvtListType.getDclUnitcd());//申报计量单位
//						logger.info("新增通关底账信息==> " + JSON.toJSONString(insertounded));
//						baseBoundedListService.save(insertounded);
//						logger.info("新增通关底账信息成功");
//					} catch (Exception e) {
//						logger.error("新增通关底账信息失败==> " + e.getMessage());
//					}
//				}
//			}
		}
		return "success";
	}

//================================================================================================================================
	//生成报关单
	@RequestMapping(value="getBGDInfo/{forId}",method = RequestMethod.GET)
	@ResponseBody
	public String getBGDInfo(@PathVariable("forId") String forId,@RequestParam("checkListNoVal") String checkListNoVal) {
		String msg = "success";
		//获取要同步的核注清单号
		BisPreEntryInvtQuery bisPreEntryInvtQuery = new BisPreEntryInvtQuery();
		bisPreEntryInvtQuery = preEntryInvtQueryService.find("bondInvtNo",checkListNoVal);
		if (bisPreEntryInvtQuery != null){//本地已获取
			if (bisPreEntryInvtQuery.getBondInvtNo()!=null && bisPreEntryInvtQuery.getBondInvtNo().toString().trim().length() > 0){
				int count = cdService.getCount(bisPreEntryInvtQuery.getBondInvtNo());
				if(count > 0){
					return "当前核注清单已生成过报关单，请查询后修改。";
				}
				//查询
				String result = null;
				try {
					result = createBGD(forId,bisPreEntryInvtQuery);
				} catch (IOException | ClassNotFoundException | ParseException e) {
					logger.info("批量生成报关单异常:"+e.getMessage());
					e.printStackTrace();
				}
				if ("success".equals(result)){
					User user = UserUtil.getCurrentUser();
					bisPreEntryInvtQuery.setUpdateBy(user.getName());
					bisPreEntryInvtQuery.setUpdateTime(new Date());
					bisPreEntryInvtQuery.setCreateBgd("1");
					preEntryInvtQueryService.merge(bisPreEntryInvtQuery);
					logger.info("核注清单号："+bisPreEntryInvtQuery.getBondInvtNo()+" 生成报关单成功");
				}else{
					msg = msg + "本地存在，核注清单号："+bisPreEntryInvtQuery.getBondInvtNo()+" "+result +";";
					logger.info("生成报关单失败:"+msg);
				}
			}
		}else{//本地未获取，需查询
			//查询
			String result = null;
			try {
				result = invtQuery(checkListNoVal,false);
				if ("success".equals(result)){
					result = createBGD(forId,bisPreEntryInvtQuery);
					if ("success".equals(result)){
						User user = UserUtil.getCurrentUser();
						bisPreEntryInvtQuery.setUpdateBy(user.getName());
						bisPreEntryInvtQuery.setUpdateTime(new Date());
						bisPreEntryInvtQuery.setCreateBgd("1");
						preEntryInvtQueryService.merge(bisPreEntryInvtQuery);
						logger.info("核注清单号："+bisPreEntryInvtQuery.getBondInvtNo()+" 生成报关单成功");
					}else{
						msg = msg + "接口获取核注清单号："+bisPreEntryInvtQuery.getBondInvtNo()+"成功，生成报关单失败。 "+result +";";
						logger.info("生成报关单失败:"+msg);
					}
				}else{
					msg = msg + "接口获取核注清单号："+bisPreEntryInvtQuery.getBondInvtNo()+"失败。 "+result +";";
					logger.info("生成报关单失败:"+msg);
				}
			} catch (IOException | ClassNotFoundException | ParseException e) {
				logger.info("批量生成报关单异常:"+e.getMessage());
				e.printStackTrace();
			}
		}
		return msg;
	}

	//生成报关单
	@RequestMapping(value="createOneBGD/{id}",method = RequestMethod.GET)
	@ResponseBody
	public String createOneBGD(@PathVariable("id") String id) {
		String msg = "success";
		//获取要同步的核注清单号
		BisPreEntryInvtQuery bisPreEntryInvtQuery = new BisPreEntryInvtQuery();
		bisPreEntryInvtQuery = preEntryInvtQueryService.get(id);
		if (bisPreEntryInvtQuery != null){
			if (bisPreEntryInvtQuery.getBondInvtNo()!=null && bisPreEntryInvtQuery.getBondInvtNo().toString().trim().length() > 0){
				int count = cdService.getCount(bisPreEntryInvtQuery.getBondInvtNo());
				if(count > 0){
					return "当前核注清单已生成过报关单，请联系管理员。";
				}
				//查询
				String result = null;
				try {
					result = createBGD(null,bisPreEntryInvtQuery);
				} catch (IOException | ClassNotFoundException | ParseException e) {
					logger.info("批量生成报关单异常:"+e.getMessage());
					e.printStackTrace();
				}
				if ("success".equals(result)){
					User user = UserUtil.getCurrentUser();
					bisPreEntryInvtQuery.setUpdateBy(user.getName());
					bisPreEntryInvtQuery.setUpdateTime(new Date());
					bisPreEntryInvtQuery.setCreateBgd("1");
					preEntryInvtQueryService.merge(bisPreEntryInvtQuery);
					logger.info("核注清单号："+bisPreEntryInvtQuery.getBondInvtNo()+" 生成报关单成功");
				}else{
					msg = msg + "核注清单号："+bisPreEntryInvtQuery.getBondInvtNo()+" "+result +";";
				}
			}
		}
		return msg;
	}

	//批量生成报关单
	@RequestMapping(value="createAllBGD",method = RequestMethod.GET)
	@ResponseBody
	public String createAllBGD() {
		String msg = "success";
		//获取要同步的核注清单号
		List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();
		bisPreEntryInvtQueryList = preEntryInvtQueryService.getListByCreateBGD();
		if (bisPreEntryInvtQueryList != null && bisPreEntryInvtQueryList.size() > 0){
			for (BisPreEntryInvtQuery forBisPreEntryInvtQuery:bisPreEntryInvtQueryList) {
				if (forBisPreEntryInvtQuery.getBondInvtNo()!=null && forBisPreEntryInvtQuery.getBondInvtNo().toString().trim().length() > 0){
					int count = cdService.getCount(forBisPreEntryInvtQuery.getBondInvtNo());
					if(count > 0){
						return "当前核注清单:"+forBisPreEntryInvtQuery.getBondInvtNo()+"已生成过报关单，请联系管理员。";
//						continue;
					}
					//查询
					String result = null;
					try {
						result = createBGD(null,forBisPreEntryInvtQuery);
//						result = createBGD2(forBisPreEntryInvtQuery);
					} catch (IOException | ClassNotFoundException | ParseException e) {
						logger.info("批量生成报关单异常:"+e.getMessage());
						e.printStackTrace();
					}
					if ("success".equals(result)){
						User user = UserUtil.getCurrentUser();
						forBisPreEntryInvtQuery.setUpdateBy(user.getName());
						forBisPreEntryInvtQuery.setUpdateTime(new Date());
						forBisPreEntryInvtQuery.setCreateBgd("1");
						preEntryInvtQueryService.merge(forBisPreEntryInvtQuery);
						logger.info("核注清单号："+forBisPreEntryInvtQuery.getBondInvtNo()+" 生成报关单成功");
					}else{
						msg = msg + "核注清单号："+forBisPreEntryInvtQuery.getBondInvtNo()+" "+result +";";
					}
				}
			}
		}
		return msg;
	}
	//生成报关单
	public String createBGD2(BisPreEntryInvtQuery bisPreEntryInvtQuery) throws IOException, ClassNotFoundException, ParseException {
		BsCustomsDeclaration bsCustomsDeclaration = new BsCustomsDeclaration();
		List<BsCustomsDeclarationInfo> bsCustomsDeclarationInfoList = new ArrayList<>();
		List<InvtListType> invtListType = new ArrayList<>();

		invtListType = JSONArray.parseArray(JSON.toJSONString(ByteAryToObject(bisPreEntryInvtQuery.getInvtListType())),InvtListType.class);

		String checkListNo = bisPreEntryInvtQuery.getBondInvtNo();
		bsCustomsDeclaration = cdService.find("checkListNo",checkListNo);
		bsCustomsDeclarationInfoList = cdInfoService.getList(bsCustomsDeclaration.getForId());
		if(bsCustomsDeclarationInfoList!=null && bsCustomsDeclarationInfoList.size() > 0){
			for (BsCustomsDeclarationInfo forBsCustomsDeclarationInfo:bsCustomsDeclarationInfoList) {
				for (InvtListType forInvtListType:invtListType) {
					if(forInvtListType.getGdsSeqno().equals(forBsCustomsDeclarationInfo.getXh())){
						forBsCustomsDeclarationInfo.setAccountBook(forInvtListType.getPutrecSeqno());
						cdInfoService.save(forBsCustomsDeclarationInfo);
						break;
					}
				}
			}
		}
		return "success";
	}

	//生成报关单
	public String createBGD(String linkId,BisPreEntryInvtQuery bisPreEntryInvtQuery) throws IOException, ClassNotFoundException, ParseException {
		//获取核注清单数据
		InvtHeadType invtHeadType = new InvtHeadType();
		List<InvtListType> invtListType = new ArrayList<>();
		if (bisPreEntryInvtQuery.getInvtHeadType() == null){
			return "生成预报单时未获取到表头信息";
		}else{
			invtHeadType = JSONObject.parseObject(JSON.toJSONString(ByteAryToObject(bisPreEntryInvtQuery.getInvtHeadType())),InvtHeadType.class);
		}
		//非报关不生成报关单
		if(invtHeadType.getDclcusFlag() == null || "2".equals(invtHeadType.getDclcusFlag())){
			return "success";
		}
		if (bisPreEntryInvtQuery.getInvtListType() == null || "[]".equals(bisPreEntryInvtQuery.getInvtListType())){
			return "生成预报单时未获取到表体信息";
		}else{
			invtListType = JSONArray.parseArray(JSON.toJSONString(ByteAryToObject(bisPreEntryInvtQuery.getInvtListType())),InvtListType.class);
		}

		//创建报关单对象
		BsCustomsDeclaration bsCustomsDeclaration = new BsCustomsDeclaration();
		List<BsCustomsDeclarationInfo> bsCustomsDeclarationInfoList = new ArrayList<>();

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

		if (linkId == null){
			linkId = UUID.randomUUID().toString();
		}
		bsCustomsDeclaration.setForId(linkId);
		bsCustomsDeclaration.setState("4");//状态 5-申报核注清单通过，状态为5

		//服务项目
		if (invtHeadType.getImpexpMarkcd() == null || "I".equals(invtHeadType.getImpexpMarkcd())){
			bsCustomsDeclaration.setServiceProject("0");
		}else{
			bsCustomsDeclaration.setServiceProject("1");
		}
		User user = UserUtil.getCurrentUser();
		bsCustomsDeclaration.setCreateBy("SYSTEM");
		if(invtHeadType.getInputTime()!=null){
			bsCustomsDeclaration.setCreateTime(sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInputTime()))));
		}
		bsCustomsDeclaration.setUpdateBy("SYSTEM");
		if(invtHeadType.getInvtDclTime()!=null){
			bsCustomsDeclaration.setUpdateTime(sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInvtDclTime()))));
		}
		bsCustomsDeclaration.setJlAudit("李晓静");
		bsCustomsDeclaration.setJlAuditTime(bsCustomsDeclaration.getUpdateTime());
		bsCustomsDeclaration.setZgAudit("王巧玲");
		bsCustomsDeclaration.setZgAuditTime(bsCustomsDeclaration.getUpdateTime());
		bsCustomsDeclaration.setUpAndDown("0");//上传/下载,0-未上传;1-已上传;2-已下载
		bsCustomsDeclaration.setCheckListNo(invtHeadType.getBondInvtNo());//核注清单号
		if (invtHeadType.getDclEtpsNm()!=null && invtHeadType.getDclEtpsNm().trim().length() > 0){
			BaseClientInfo baseClientInfo = clientService.find("clientName",invtHeadType.getDclEtpsNm());
			if(baseClientInfo!=null && baseClientInfo.getIds()!=null){
				bsCustomsDeclaration.setDeclarationUnitId(baseClientInfo.getIds().toString());//报关公司ID
			}
		}
		bsCustomsDeclaration.setDeclarationUnit(invtHeadType.getDclEtpsNm());//报关公司名称
		String cdNum = "";
		if(invtHeadType.getEntryNo() != null && invtHeadType.getEntryNo().toString().trim().length() > 0){
			cdNum = invtHeadType.getEntryNo().toString().trim();
		}else{
			if(invtHeadType.getRltEntryNo() != null && invtHeadType.getRltEntryNo().toString().trim().length() > 0){
				cdNum = invtHeadType.getRltEntryNo().toString().trim();
			}
		}
		bsCustomsDeclaration.setCdNum(cdNum);//报关单号
		if (invtHeadType.getRltEntryBizopEtpsNm()!=null && invtHeadType.getRltEntryBizopEtpsNm().trim().length() > 0){
			BaseClientInfo baseClientInfo = clientService.find("clientName",invtHeadType.getRltEntryBizopEtpsNm());
			if(baseClientInfo!=null && baseClientInfo.getIds()!=null){
				bsCustomsDeclaration.setClientId(baseClientInfo.getIds().toString());//客户ID
			}
		}
		bsCustomsDeclaration.setClientName(invtHeadType.getRltEntryBizopEtpsNm());//客户名称
		bsCustomsDeclaration.setBillNum(bisPreEntryInvtQuery.getTdNo().trim());//提单号
		bsCustomsDeclaration.setTradeMode(invtHeadType.getTrspModecd());//贸易方式
		bsCustomsDeclaration.setStoragePlace("青岛港怡之航冷链物流有限公司");//货物存放地点
		bsCustomsDeclaration.setConsignee(invtHeadType.getRcvgdEtpsNm() == null ? "" : invtHeadType.getRcvgdEtpsNm());//收货人
		bsCustomsDeclaration.setConsignor(invtHeadType.getRltEntryBizopEtpsno());//发货人

		//起运国，从字典中依据编号转成名称
		List<BisPreEntryDictData> bisPreEntryDictDataList = new ArrayList<>();
		bisPreEntryDictDataList = preEntryService.getDictDataByCode("CUS_STSHIP_TRSARV_NATCD");
		for (BisPreEntryDictData forBisPreEntryDictData : bisPreEntryDictDataList) {
			if (forBisPreEntryDictData.getValue().equals(invtHeadType.getStshipTrsarvNatcd()) || forBisPreEntryDictData.getLabel().equals(invtHeadType.getStshipTrsarvNatcd())) {
				bsCustomsDeclaration.setMyg(forBisPreEntryDictData.getLabel());//贸易国
				bsCustomsDeclaration.setQyg(forBisPreEntryDictData.getLabel());//启运国
				break;
			}
		}
		bsCustomsDeclaration.setCdBy("韩飞");//报关人
		if(invtHeadType.getEntryDclTime()!=null){
			bsCustomsDeclaration.setCdTime(sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getEntryDclTime()))));//报关时间
			bsCustomsDeclaration.setSbTime(sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getEntryDclTime()))));//申报日期
		}
		bsCustomsDeclaration.setRemark(invtHeadType.getRmk());

		Double dty = 0.00;
		Double netWeight = 0.00;//净重
		Double grossWeight = 0.00;//毛重
		for (InvtListType forInvtListType:invtListType) {
			BsCustomsDeclarationInfo bsCustomsDeclarationInfo = new BsCustomsDeclarationInfo();
			bsCustomsDeclarationInfo.setForId(linkId);
			bsCustomsDeclarationInfo.setXh(forInvtListType.getGdsSeqno());//序号
			bsCustomsDeclarationInfo.setAccountBook(forInvtListType.getPutrecSeqno());//账册商品序号
			bsCustomsDeclarationInfo.setSpbh(forInvtListType.getGdecd());//商品编号
			bsCustomsDeclarationInfo.setSpmc(forInvtListType.getGdsNm());//商品名称
			bsCustomsDeclarationInfo.setGgxh(forInvtListType.getGdsSpcfModelDesc());//规格型号
			bsCustomsDeclarationInfo.setBzt(forInvtListType.getDclCurrcd());//币制
			bsCustomsDeclarationInfo.setSbjldw(forInvtListType.getDclUnitcd());//申报计量单位
			if(forInvtListType.getDclQty() == null || forInvtListType.getDclQty().trim().length() == 0){
				bsCustomsDeclarationInfo.setSbsl(Double.parseDouble("0"));//申报数量
			}else{
				dty = dty + Double.parseDouble(forInvtListType.getDclQty());
				bsCustomsDeclarationInfo.setSbsl(Double.parseDouble(forInvtListType.getDclQty()));//申报数量
			}
			if(forInvtListType.getDclUprcAmt() == null || forInvtListType.getDclUprcAmt().trim().length() == 0){
				bsCustomsDeclarationInfo.setQysbdj(Double.parseDouble("0"));//单价
			}else{
				bsCustomsDeclarationInfo.setQysbdj(Double.parseDouble(forInvtListType.getDclUprcAmt()));//单价
			}
			if(forInvtListType.getUsdStatTotalAmt() == null || forInvtListType.getUsdStatTotalAmt().trim().length() == 0){
				bsCustomsDeclarationInfo.setMytjzje(Double.parseDouble("0"));//美元总价
			}else{
				bsCustomsDeclarationInfo.setMytjzje(Double.parseDouble(forInvtListType.getUsdStatTotalAmt()));//美元总价
			}

			if(forInvtListType.getGrossWt() == null || forInvtListType.getGrossWt().trim().length() == 0){

			}else{
				grossWeight = grossWeight + Double.parseDouble(forInvtListType.getGrossWt());//毛重
			}
			if(forInvtListType.getNetWt() == null || forInvtListType.getNetWt().trim().length() == 0){

			}else{
				netWeight = netWeight + Double.parseDouble(forInvtListType.getNetWt());//净重
			}
			bsCustomsDeclarationInfo.setYcg(forInvtListType.getNatcd());//原产国(地区)
			bsCustomsDeclarationInfo.setZzmdg(forInvtListType.getDestinationNatcd());//最终目的国
			bsCustomsDeclarationInfo.setJnmdd("(37026/370211)青岛前湾保税港区、青岛西海岸和胶州湾综合保税区/青岛市黄岛区");//境内目的地
			bsCustomsDeclarationInfo.setZmfs(forInvtListType.getLvyrlfModecd());//征免方式
			bsCustomsDeclarationInfo.setRemark(forInvtListType.getRmk());//备注

			bsCustomsDeclarationInfoList.add(bsCustomsDeclarationInfo);
		}

		if(invtListType.size() > 0){
			bsCustomsDeclaration.setDty(dty.toString());//件数
			bsCustomsDeclaration.setGrossWeight(grossWeight);//毛重
			bsCustomsDeclaration.setNetWeight(netWeight);//净重
		}
		cdService.save(bsCustomsDeclaration);
		if (bsCustomsDeclarationInfoList.size() > 0){
			for (BsCustomsDeclarationInfo bsCustomsDeclarationInfo:bsCustomsDeclarationInfoList) {
				cdInfoService.save(bsCustomsDeclarationInfo);
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
	public String invtDetail(Model model, @PathVariable("id") String id) throws ParseException, IOException, ClassNotFoundException {
		BisPreEntryInvtQuery bisPreEntryInvtQuery = preEntryInvtQueryService.get(id);
		bisPreEntryInvtQueryTemp = bisPreEntryInvtQuery;
		model.addAttribute("ID", bisPreEntryInvtQuery.getId());
		if(bisPreEntryInvtQuery.getInvtHeadType() != null){
			InvtHeadType invtHeadType = JSON.parseObject(JSON.toJSONString(ByteAryToObject(bisPreEntryInvtQuery.getInvtHeadType())),InvtHeadType.class);
			invtHeadType.setEtpsInnerInvtNo(bisPreEntryInvtQuery.getTdNo());
			model.addAttribute("bisPreEntry", invtHeadType);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			if(invtHeadType.getInputTime()!=null && invtHeadType.getInputTime().toString().trim().length() >0){
				model.addAttribute("inputTime", sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInputTime()))));//录入日期
			}
			if(invtHeadType.getInvtDclTime()!=null && invtHeadType.getInvtDclTime().toString().trim().length() >0){
				model.addAttribute("invtDclTime", sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getInvtDclTime()))));//清单申报日期
			}
			if(invtHeadType.getEntryDclTime()!=null && invtHeadType.getEntryDclTime().toString().trim().length() >0){
				model.addAttribute("entryDclTime", sdf1.parse(sdf1.format(sdf2.parse(invtHeadType.getEntryDclTime()))));//报关单申报日期
			}
		}
		return "wms/preEntryInvtQuery/preEntryInvtDetail";
	}

	@RequestMapping(value = "jsonInvtList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> jsonInvtList(HttpServletRequest request) throws IOException, ClassNotFoundException {
		Page<InvtListType> page = getPage(request);
		List<InvtListType> invtListType = new ArrayList<>();
		if(bisPreEntryInvtQueryTemp.getInvtListType() != null){
			invtListType = JSON.parseArray(JSON.toJSONString(ByteAryToObject(bisPreEntryInvtQueryTemp.getInvtListType())),InvtListType.class);
			for (InvtListType forInvtListType:invtListType) {
				forInvtListType.setGdsMtno(bisPreEntryInvtQueryTemp.getTdNo());
			}
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
