package com.haiersoft.ccli.wms.web.preEntry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.haiersoft.ccli.bounded.service.BaseBoundedService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.apiEntity.*;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryBounded;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryBoundedService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;


@Controller
@RequestMapping(value = "wms/preEntryBonded")
public class PreEntryBondedController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PreEntryBondedController.class);
	@Autowired
	private PreEntryBoundedService preEntryBoundedService;

	private static final String memberCode = "eimskipMember";
	private static final String pass = "66668888";
	private static final String icCode = "2100030046252";


	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list() {
		return "wms/preEntry/preEntryBounded";
	}

	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisPreEntryBounded> page = getPage(request);
		page.setOrder("asc");
		page.setOrderBy("accountBook");
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = preEntryBoundedService.search(page, filters);
		return getEasyUIData(page);
	}

	@RequestMapping(value = "exportExcel")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		List<BisPreEntryBounded> resultExcelList = preEntryBoundedService.search(filters, "accountBook", true);

		ExportParams params = new ExportParams("保税底账查询导出", "保税底账Sheet", ExcelType.XSSF);
		Workbook workbook = ExcelExportUtil.exportExcel(params, BisPreEntryBounded.class, resultExcelList);
		String formatFileNameP = "保税底账查询" + ".xlsx";
		String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");
		response.setContentType("application/msexcel");
		OutputStream os = response.getOutputStream();
		workbook.write(os);
		os.close();
	}

	@RequestMapping(value="getPreEntryBonded/{bondInvtNo}",method = RequestMethod.GET)
	@ResponseBody
	public String getPreEntryBonded(@PathVariable("bondInvtNo") String bondInvtNo) {
		if (bondInvtNo == null ||bondInvtNo.trim().length() == 0){
			return "未获取到核注清单号。";
		}

		BisPreEntry bisPreEntry = new BisPreEntry();
		List<BisPreEntry> bisPreEntryList = new ArrayList<>();
		bisPreEntryList = preEntryBoundedService.getBisPreEntryList(bondInvtNo);
		if(bisPreEntryList != null && bisPreEntryList.size() >0){
			bisPreEntry = bisPreEntryList.get(0);
		}

		//查询保税核注清单列表
        InvtQueryListRequest invtQueryListRequest = new InvtQueryListRequest();
        invtQueryListRequest.setBondInvtNo(bondInvtNo);
        invtQueryListRequest.setMemberCode(memberCode);
        invtQueryListRequest.setPass(pass);
        invtQueryListRequest.setIcCode(icCode);
        //调用服务
        Map<String, Object> resultMap = InvtQueryListService(invtQueryListRequest);
        if("500".equals(resultMap.get("code").toString())){
        	if(resultMap.get("msg") ==null || resultMap.get("msg").toString().trim().length() == 0){
				return "error";
			}else{
				return resultMap.get("msg").toString().trim();
			}
		}else{
			List<BisPreEntryBounded> bisPreEntryBoundedList = new ArrayList<>();
			//处理结果
			InvtQueryListResponse invtQueryListResponse = (InvtQueryListResponse) resultMap.get("data");
			List<InvtQueryListResponseResultList> invtQueryListResponseResultLists = invtQueryListResponse.getResultList();
			if(invtQueryListResponseResultLists != null && invtQueryListResponseResultLists.size() >0){
				for (InvtQueryListResponseResultList forInvtQueryListResponseResultList:invtQueryListResponseResultLists) {
					BisPreEntryBounded bisPreEntryBounded = new BisPreEntryBounded();

					bisPreEntryBounded.setClientId(bisPreEntry.getClientId());//客户ID
					bisPreEntryBounded.setClientName(bisPreEntry.getClientName());//客户名称
					bisPreEntryBounded.setBillNum(bisPreEntry.getBillNum());//提单号
					bisPreEntryBounded.setCdNum(forInvtQueryListResponseResultList.getEntryNo()==null?null:forInvtQueryListResponseResultList.getEntryNo());//报关单号
					bisPreEntryBounded.setCtnNum(null);//MR/集装箱号
					bisPreEntryBounded.setItemName(null);//货物描述
					bisPreEntryBounded.setPiece(forInvtQueryListResponseResultList.getDclQty()==null?0:Integer.parseInt(forInvtQueryListResponseResultList.getDclQty()));//件数
					bisPreEntryBounded.setNetWeight(0.00);//总净值
					bisPreEntryBounded.setCustomerServiceName(bisPreEntry.getCustomerService() == null ? "" : bisPreEntry.getCustomerService().toString().trim());//所属客服
					bisPreEntryBounded.setHsCode(bisPreEntry.getHsNo() == null ? "" : bisPreEntry.getHsNo().toString().trim());//HS编码
					bisPreEntryBounded.setHsItemname(forInvtQueryListResponseResultList.getGdsMtno());//海关品名
					bisPreEntryBounded.setAccountBook(forInvtQueryListResponseResultList.getGdsSeqno());//账册商品序号
//					bisPreEntryBounded.setDclQty(Double.parseDouble(bondInvtDtMap.get("dclQty") == null ? "0" : bondInvtDtMap.get("dclQty").toString().trim().length() == 0?"0":bondInvtDtMap.get("dclQty").toString().trim()));//申报重量
//					bisPreEntryBounded.setDclUnit(bondInvtDtMap.get("dclUnitcd") == null ? "" : bondInvtDtMap.get("dclUnitcd").toString());//申报计量单位
//					bisPreEntryBounded.setHsQty(Double.parseDouble(bisCustomsClearanceInfo.getGrossWeight().toString()));//海关库存重量
					bisPreEntryBounded.setTypeSize(null);//规格
					bisPreEntryBounded.setCargoLocation(null);//库位
					bisPreEntryBounded.setCargoArea(null);//库区
					bisPreEntryBounded.setStorageDate(null);//入库时间
					bisPreEntryBounded.setCreatedTime(new Date());

					bisPreEntryBoundedList.add(bisPreEntryBounded);
				}
				//判断是否有需要新增的数据
				if(bisPreEntryBoundedList.size() > 0){
					for (BisPreEntryBounded forBisPreEntryBounded:bisPreEntryBoundedList) {
						preEntryBoundedService.save(forBisPreEntryBounded);
					}
				}
			}
			return "success";
		}
	}

//======================================================================================================================

	/**
	 * @return java.util.concurrent.Callable<com.chenay.bean.entity.customs.InvtQueryListResponse>
	 * @Author chenp
	 * @Description 保税核注清单列表查询服务
	 * @Date 9:14 2021/1/30
	 * @Param [invtQueryListRequest]
	 **/
	public Map<String,Object> InvtQueryListService(InvtQueryListRequest invtQueryListRequest) {
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
}
