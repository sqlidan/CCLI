package com.haiersoft.ccli.wms.web.PreEntryInvtQuery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import com.haiersoft.ccli.wms.service.preEntry.PreEntryService;
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
	private CustomsClearanceListService customsClearanceListService;
	@Autowired
	private CustomsClearanceInfoSService customsClearanceInfoSService;
	@Autowired
	private BaseBoundedListService baseBoundedListService;

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
		PropertyFilter filter = new PropertyFilter("EQS_synchronization", "1");
		filters.add(filter);
		page = preEntryInvtQueryService.search(page, filters);
		return getEasyUIData(page);
	}

	//单个核注清单查询
	@RequestMapping(value="getPreEntryInvtQuery/{bondInvtNo}",method = RequestMethod.GET)
	@ResponseBody
	public String getPreEntryInvtQuery(@PathVariable("bondInvtNo") String bondInvtNo) {
		if (bondInvtNo == null ||bondInvtNo.trim().length() == 0){
			return "未获取到核注清单号。";
		}

		//查询
		String result = invtQuery(bondInvtNo,false);
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
			int count = 0;
			for (BisPreEntryInvtQuery forBisPreEntryInvtQuery:bisPreEntryInvtQueryList) {
				if (forBisPreEntryInvtQuery.getBondInvtNo()!=null && forBisPreEntryInvtQuery.getBondInvtNo().toString().trim().length() > 0){
					//查询
					String result = invtQuery(forBisPreEntryInvtQuery.getBondInvtNo(),true);
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
	public String invtQuery(String bondInvtNo,Boolean createInfo){
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
							updateBisPreEntryInvtQuery.setSynchronization("1");
							User user = UserUtil.getCurrentUser();
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
					forBisPreEntryInvtQuery.setSynchronization("1");
					User user = UserUtil.getCurrentUser();
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

	//生成台账底账
	public String createInfo(BisPreEntryInvtQuery bisPreEntryInvtQuery){
		InvtHeadType invtHeadType = new InvtHeadType();
		List<InvtListType> invtListType = new ArrayList<>();
		if (bisPreEntryInvtQuery.getInvtHeadType() == null){
			return "生成台账时未获取到表头信息";
		}else{
			invtHeadType = JSON.parseObject(bisPreEntryInvtQuery.getInvtHeadType(),InvtHeadType.class);
		}
		if (bisPreEntryInvtQuery.getInvtListType() == null || "[]".equals(bisPreEntryInvtQuery.getInvtListType())){
			return "生成台账时未获取到表体信息";
		}else{
			invtListType = JSONArray.parseArray(bisPreEntryInvtQuery.getInvtListType(),InvtListType.class);
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
		String linkId = "F" + userCode + StringUtils.timeToString();
		//添加台账
		BisCustomsClearanceList bisCustoms = new BisCustomsClearanceList();
		bisCustoms.setCdNum(linkId);//业务单号
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
		bisCustoms.setBillNum(invtHeadType.getApplyNo() == null ? "" : invtHeadType.getApplyNo().trim());//提单号
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
		bisCustoms.setCustomsDeclarationNumber(invtHeadType.getEntryNo() == null ? "" : invtHeadType.getEntryNo());//报关单号
		bisCustoms.setContryOragin("");//原产国
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

			//通关底账
			if ("I".equals(invtHeadType.getImpexpMarkcd())) {//进口(报进)
				try {
					//只需要添加一条底账信息即可
					BaseBoundedList bounded = new BaseBoundedList();
					bounded.setClientId(bisCustoms.getClientId());//客户ID
					bounded.setClientName(bisCustoms.getClientName());//客户名称
					bounded.setBillNum(bisCustoms.getBillNum());//提单号
					bounded.setCdNum(bisCustoms.getCustomsDeclarationNumber());//报关单号
					bounded.setCtnNum(null);//MR/集装箱号
					bounded.setItemName(null);//货物描述
					bounded.setStorageDate(null);//入库时间
					bounded.setPiece(0);//件数
					bounded.setNetWeight(0.00);//总净值
					bounded.setCustomerServiceName(user.getName());//所属客服
					bounded.setHsCode(forInvtListType.getGdecd());//HS编码
					bounded.setHsItemname(bisCustomsClearanceInfo.getCommodityName());//海关品名
					bounded.setAccountBook(bisCustomsClearanceInfo.getAccountBook());//账册商品序号
					bounded.setHsQty(Double.parseDouble(bisCustomsClearanceInfo.getGrossWeight().toString()));//海关库存重量
					bounded.setTypeSize(bisCustomsClearanceInfo.getSpecification());//规格
					bounded.setCargoLocation(null);//库位
					bounded.setCreatedTime(new Date());//创建时间
					bounded.setUpdatedTime(new Date());//修改时间
					bounded.setCargoArea(null);//库区
					bounded.setDclQty(Double.parseDouble(bisCustomsClearanceInfo.getNum().toString()));//申报重量
					bounded.setDclUnit(forInvtListType.getDclUnitcd() == null ? "" : forInvtListType.getDclUnitcd());//申报计量单位
					logger.info("新增通关底账信息=> " + JSON.toJSONString(bounded));
					baseBoundedListService.save(bounded);
					logger.info("新增通关底账信息成功");
				} catch (Exception e) {
					logger.error("新增通关底账信息失败=> " + e.getMessage());
				}
			}else if ("E".equals(invtHeadType.getImpexpMarkcd())) {
				//出区
				BaseBoundedList bounded = new BaseBoundedList();
				bounded = baseBoundedListService.find("accountBook",forInvtListType.getPutrecSeqno());
				if(bounded!=null && bounded.getDclQty() != null){
					//有数据扣减
					Double dclQtyOrg = bounded.getDclQty();
					bounded.setDclQty(bounded.getDclQty() - Double.parseDouble(forInvtListType.getDclQty()));//申报重量
					bounded.setUpdatedTime(new Date());
					logger.info("修改通关底账信息==>(" + dclQtyOrg.toString() + "==>" + bounded.getDclQty() + ") " + JSON.toJSONString(bounded));
					baseBoundedListService.merge(bounded);
					logger.info("修改通关底账信息成功");
				}else{
					try {
						//没数据新增为负数
						BaseBoundedList insertounded = new BaseBoundedList();
						insertounded.setClientId(bisCustoms.getClientId());//客户ID
						insertounded.setClientName(bisCustoms.getClientName());//客户名称
						insertounded.setBillNum(bisCustoms.getBillNum());//提单号
						insertounded.setCdNum(bisCustoms.getCustomsDeclarationNumber());//报关单号
						insertounded.setCtnNum(null);//MR/集装箱号
						insertounded.setItemName(null);//货物描述
						insertounded.setStorageDate(null);//入库时间
						insertounded.setPiece(0);//件数
						insertounded.setNetWeight(0.00);//总净值
						insertounded.setCustomerServiceName(user.getName());//所属客服
						insertounded.setHsCode(forInvtListType.getGdecd());//HS编码
						insertounded.setHsItemname(bisCustomsClearanceInfo.getCommodityName());//海关品名
						insertounded.setAccountBook(bisCustomsClearanceInfo.getAccountBook());//账册商品序号
						insertounded.setHsQty(Double.parseDouble(bisCustomsClearanceInfo.getGrossWeight().toString()));//海关库存重量
						insertounded.setTypeSize(bisCustomsClearanceInfo.getSpecification());//规格
						insertounded.setCargoLocation(null);//库位
						insertounded.setCreatedTime(new Date());//创建时间
						insertounded.setUpdatedTime(new Date());//修改时间
						insertounded.setCargoArea(null);//库区
						insertounded.setDclQty(Double.parseDouble("0") - Double.parseDouble(forInvtListType.getDclQty()));//申报重量
						insertounded.setDclUnit(forInvtListType.getDclUnitcd() == null ? "" : forInvtListType.getDclUnitcd());//申报计量单位
						logger.info("新增通关底账信息==> " + JSON.toJSONString(insertounded));
						baseBoundedListService.save(insertounded);
						logger.info("新增通关底账信息成功");
					} catch (Exception e) {
						logger.error("新增通关底账信息失败==> " + e.getMessage());
					}
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
