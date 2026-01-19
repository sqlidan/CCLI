package com.haiersoft.ccli.supervision.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.*;
import com.haiersoft.ccli.supervision.service.*;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
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
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分类监管 核放单controller
 * @author 
 *
 */

@Controller
@RequestMapping("supervision/opMani")
public class OpManiController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(OpManiController.class);
	@Autowired
	OpManiHeadService opManiHeadService;
	
	@Autowired
	OpManiInfoService opManiInfoService;


	@Autowired
	GetKeyService getKeyService;

	@Autowired
	FljgWsClient fljgWsClient;
	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fljg/opManiList";
	}

	//新增空车核放单
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createEmp(Model model) {

		return "fljg/opManiForm";
	}
	
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<OpManiHead> page = getPage(request);
		page.orderBy("createTime").order("desc");
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = opManiHeadService.search(page, filters);
		return getEasyUIData(page);
	}

	/**
	 * 添加核放单
	 *
	 * @param opManiHead
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid OpManiHead opManiHead, Model model) {
		//本地状态为1，保存
		opManiHead.setLocalStatus("1");
		opManiHead.setCreateTime(new Date());
		opManiHeadService.save(opManiHead);
		return "success";
	}



	//删除核放单
	@Transactional
	@RequestMapping(value="/del/{ids}")
	@ResponseBody
	public String deleteManiHead(@PathVariable("ids") List<String> ids) throws RemoteException, ServiceException {
				
		List<OpManiHead> toDeleteRemote = new ArrayList<OpManiHead>();
		List<OpManiHead> toDeleteLocal = new ArrayList<OpManiHead>();
		//检查本地状态,先整理出需要接口删除的核放单
		for(String id : ids) {
			OpManiHead opManiHead = opManiHeadService.get(id);
			if((null ==opManiHead.getManifestId()) || ("".equals(opManiHead.getManifestId()))) {
				toDeleteLocal.add(opManiHead);
			}else {
				toDeleteRemote.add(opManiHead);
			}				
		}
		
		//需要调用接口删除的mani
		if(toDeleteRemote.size() != 0) {
			// 1 获得key
			String tickId = getKeyService.builder();
			System.out.println(tickId);
			if((null ==tickId) || ("".equals(tickId)))
			{
				return "没有获得tickId,请重新操作";
			}
			// 2 调用接口
			//设置接口名
			String serviceName = "ManiDelete";
			for(OpManiHead mani:toDeleteRemote) {
				String manidelete="{\"KeyModel\":{\"ManifestId\":\""+mani.getManifestId()+"\"}}";
				logger.error(">>>>>>>>>>>>>>>>>调用核放单删除json： "+manidelete);
				String result = fljgWsClient.getResult(manidelete, tickId, serviceName);
				System.out.println("result: " + result);
				logger.error(">>>>>>>>>>>>>>>>>调用核放单删除result： "+result);
				JSONObject jsonObject = JSON.parseObject(result);
		        String state = jsonObject.getString("state");
		        //如果接口state返回1为删除成功
		        if(state.equals("1")) {
		        	//删除本地数据库中的记录
		        	opManiHeadService.delete(mani.getId());
					opManiInfoService.deleteByHeadId(mani.getId());
		        	return "success";
		        }else {
		        	
		        	return "接口错误";
		        }
			}			
		}

		//不需要调用接口删除的 list
		for(OpManiHead mani:toDeleteLocal) {
			opManiHeadService.delete(mani.getId());
			opManiInfoService.deleteByHeadId(mani.getId());
		}

		return "success";


	}
	//作废核放单
	@Transactional
	@RequestMapping(value="/cancel/{ids}")
	@ResponseBody
	public String cancelMani(@PathVariable("ids") List<String> ids) throws RemoteException, ServiceException {

//		List<OpManiHead> toCancelRemote = new ArrayList<OpManiHead>();
//		List<OpManiHead> toCancelLocal = new ArrayList<OpManiHead>();
//		//检查本地状态,先整理出需要接口作废的核放单
//		for(String id : ids) {
//			OpManiHead opManiHead = opManiHeadService.get(id);
//			if((null ==opManiHead.getManifestId()) || ("".equals(opManiHead.getManifestId()))) {
//				toCancelLocal.add(opManiHead);
//			}else {
//				toCancelRemote.add(opManiHead);
//			}
//		}
//
//		//需要调用接口作废的mani
//		if(toCancelRemote.size() != 0) {
//			// 1 获得key
//			String tickId = getKeyService.builder();
//			System.out.println(tickId);
//			if((null ==tickId) || ("".equals(tickId)))
//			{
//				return "没有获得tickId,请重新操作";
//			}
//			// 2 调用接口
//			//设置接口名
//			String serviceName = "ManiNullify";
//			for(OpManiHead mani:toCancelRemote) {
//				String maninullify="{\"KeyModel\":{\"ManifestId\":\""+mani.getManifestId()+"\"}}";
//				logger.error(">>>>>>>>>>>>>>>>>调用核放单作废json： "+maninullify);
//				String result = fljgWsClient.getResult(maninullify, tickId, serviceName);
//				System.out.println("result: " + result);
//				logger.error(">>>>>>>>>>>>>>>>>调用核放单作废result： "+result);
//				JSONObject jsonObject = JSON.parseObject(result);
//				String state = jsonObject.getString("state");
//				//如果接口state返回1为作废成功
//				if(state.equals("1")) {
//					//作废本地数据库中的记录
//					mani.setDeclType("2");
//					mani.setLocalStatus("3");
//					opManiHeadService.save(mani);
//					return "success";
//				}else {
//
//					return "接口错误";
//				}
//			}
//		}
//
//		//不需要调用接口作废的 list
//		for(OpManiHead mani:toCancelLocal) {
//			mani.setLocalStatus("2");
//			opManiHeadService.save(mani);
//		}

		return "success";
	}

	//单条更新核放单详情
	@RequestMapping(value = "applyDetail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String applyDetail(@PathVariable("id") String id) throws IOException, ServiceException {
		OpManiHead opManiHead = opManiHeadService.get(id);
		// 1 获得key
		String tickId = getKeyService.builder();
		System.out.println(tickId);
		// 2 调用接口
		// 设置接口名
		String serviceName = "ManiGet";

		String maniGet = "{\"KeyModel\":{\"ManifestId\":\"" + opManiHead.getManifestId() + "\"}}";
		logger.error(">>>>>>>>>>>>>>>>>调用手工核放单更新核放json： " + maniGet);

		String result = fljgWsClient.getResult(maniGet, tickId, serviceName);
		System.out.println("result: " + result);
		logger.error(">>>>>>>>>>>>>>>>>调用手工核放单更新核放result： " + result);
		JSONObject jsObject = JSON.parseObject(result);
		OpManiHead mh = JSONObject.parseObject(jsObject.getString("Manibase"), OpManiHead.class);
		if (null != mh){
			opManiHead.setStatus(mh.getStatus());
			opManiHead.setPassStatus(mh.getPassStatus());
			opManiHeadService.update(opManiHead);
			return "success";
		}else{
			return "error";
		}
	}
	//到货确认
	//@Transactional
	@ResponseBody
	@RequestMapping(value = "/request/{id}", method = RequestMethod.GET)
	public String confirmRequest(@PathVariable("id") String headId) throws RemoteException, ServiceException {
		OpManiHead opManiHead = opManiHeadService.get(headId);
		List<OpManiHead> maniHeadList = new ArrayList<OpManiHead>();
		maniHeadList.add(opManiHead);

		if(opManiHead.getPassStatus().equals("2")) {
			return "此核放单已到货!";
		}

		if(maniHeadList.size()==0) {
			return "没有需要提交到货确认的核放单";
		}

		// 1 获得key
		String tickId = getKeyService.builder();
		System.out.println(tickId);
		//设置接口名
		String serviceName = "ManiConfirm";
		String json = buildJson(opManiHead);
		logger.error(">>>>>>>>>>>>>>>>>调用手工核放单到货确认json： "+json);
		String result = fljgWsClient.getResult(json, tickId, serviceName);
		System.out.println("result: " + result);
		logger.error(">>>>>>>>>>>>>>>>>调用手工核放单到货确认result： "+result);
		JSONObject jsonObject = JSON.parseObject(result);
		String state = jsonObject.getString("state");
		//如果接口state返回1为提交成功
		if(state.equals("1")) {
			opManiHead.setPassStatus("2");
			opManiHead.setManiConfirmStatus("Y");
			opManiHeadService.update(opManiHead);
		}else {
			return "接口错误";
		}
		return "success";

	}

	public String buildJson(OpManiHead opManiHead) {

		List<PropertyFilter> infofilters = new ArrayList<PropertyFilter>();
		infofilters.add(new PropertyFilter("EQS_headId", opManiHead.getId()));
		List<OpManiInfo> maniInfoList = opManiInfoService.search(infofilters);
		//拼装Json
		JSONObject jsonObject = new JSONObject();
		//构建接口json Manibase 表头
		JSONObject manibase = new JSONObject();

		manibase.put("ManifestId", opManiHead.getManifestId()==null?null:opManiHead.getManifestId());
		manibase.put("TradeCode",  opManiHead.getTradeCode()==null?null:opManiHead.getTradeCode());
		manibase.put("TradeName",  opManiHead.getTradeName()==null?null:opManiHead.getTradeName());
		manibase.put("CustomsCode",opManiHead.getCustomsCode()==null?null:opManiHead.getCustomsCode());
		manibase.put("IeFlag",opManiHead.getIeFlag()==null?null:opManiHead.getIeFlag());
		manibase.put("GoodsValue", opManiHead.getGoodsValue()==null?null:opManiHead.getGoodsValue());
		manibase.put("GrossWt", opManiHead.getGrossWt()==null?null:opManiHead.getGrossWt());
		manibase.put("VehicleId", opManiHead.getVehicleId()==null?null:opManiHead.getVehicleId());
		manibase.put("VehicleWeight", opManiHead.getVehicleWeight()==null?null:opManiHead.getVehicleWeight());
		manibase.put("Status", opManiHead.getStatus()==null?null:opManiHead.getStatus());
		manibase.put("PassStatus", opManiHead.getPassStatus()==null?null:opManiHead.getPassStatus());
		manibase.put("InputEr", opManiHead.getInputEr()==null?null:opManiHead.getInputEr());
		manibase.put("InputDate", opManiHead.getInputDate()==null?null:opManiHead.getInputDate());
		manibase.put("DDate", opManiHead.getDDate()==null?null:opManiHead.getDDate());
		manibase.put("DNote", opManiHead.getDNote()==null?null:opManiHead.getDNote());
		manibase.put("ApprDate", opManiHead.getApprDate()==null?null:opManiHead.getApprDate());
		manibase.put("ApprNote", opManiHead.getApprNote()==null?null:opManiHead.getApprNote());
		manibase.put("OrgCode", opManiHead.getOrgCode()==null?null:opManiHead.getOrgCode());
		manibase.put("ContaId", opManiHead.getContaId()==null?null:opManiHead.getContaId());
		manibase.put("PassportDate", opManiHead.getPassportDate()==null?null:opManiHead.getPassportDate());
		manibase.put("DeclType", opManiHead.getDeclType()==null?null:opManiHead.getDeclType());
		manibase.put("SeqNo", opManiHead.getSeqNo()==null?null:opManiHead.getSeqNo());
		manibase.put("CusStatus", opManiHead.getCusStatus()==null?null:opManiHead.getCusStatus());
		manibase.put("CusRmk", opManiHead.getCusRmk()==null?null:opManiHead.getCusRmk());

		//表头加入json Manibase中
		jsonObject.put("Manibase", manibase);

		//构建货物信息
		JSONArray jsonArray = new JSONArray();


		for (OpManiInfo opManiInfo :maniInfoList) {
			JSONObject maniGoods = new JSONObject();
			maniGoods.put("ManifestId", opManiInfo.getManifestId()==null?null:opManiInfo.getManifestId());
			maniGoods.put("GNo",opManiInfo.getgNo()==null?null:opManiInfo.getgNo());
			maniGoods.put("ApprId", opManiInfo.getApprId()==null?null:opManiInfo.getApprId());
			maniGoods.put("ApprGNo", opManiInfo.getApprGNo()==null?null:opManiInfo.getApprGNo());
			maniGoods.put("CodeTs",opManiInfo.getCodeTs()==null?null:opManiInfo.getCodeTs());
			maniGoods.put("GName", opManiInfo.getGName()==null?null:opManiInfo.getGName());
			maniGoods.put("GModel",opManiInfo.getGModel()==null?null:opManiInfo.getGModel());
			maniGoods.put("GUnit",opManiInfo.getGUnit()==null?null:opManiInfo.getGUnit());
			maniGoods.put("GQty", opManiInfo.getGQty()==null?null:opManiInfo.getGQty());
			maniGoods.put("Unit1", opManiInfo.getGUnit1()==null?null:opManiInfo.getGUnit1());
			maniGoods.put("Qty1", opManiInfo.getGQty1()==null?null:opManiInfo.getGQty1());
			maniGoods.put("GrossWt",opManiInfo.getGrossWt()==null?null:opManiInfo.getGrossWt());
			maniGoods.put("ConfirmQty",opManiInfo.getGQty()==null?null:opManiInfo.getGQty());
			maniGoods.put("EmsGNo",opManiInfo.getEmsGNo()==null?null:opManiInfo.getEmsGNo());
			maniGoods.put("GdsMtno",opManiInfo.getGdsMtno()==null?null:opManiInfo.getGdsMtno());
			jsonArray.add(maniGoods);
		}
		//货物信息加入
		jsonObject.put("ManiGoods", jsonArray);
		jsonObject.put("DeclType","1");//1首次申报2变更3作废
		jsonObject.put("SaveType","1");// 0-暂存，1-申报
		return jsonObject.toJSONString();
	}



	// 核放单申报
	@Transactional
	@RequestMapping(value = "apply/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String applyData(HttpServletRequest request, @PathVariable("id") String id) throws IOException, ServiceException {
		//根据linkId查询ManiHead
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_id", id));
		OpManiHead manihead = opManiHeadService.find("id",id);
	
		//根据ManiHead的ID查询ManiInfo
		List<PropertyFilter> infofilters = new ArrayList<PropertyFilter>();
		infofilters.add(new PropertyFilter("EQS_headId",manihead.getId()));
		List<OpManiInfo> maniInfoList = opManiInfoService.search(infofilters);

		//拼装Json
//			Map<String,Object> map = new HashMap<String, Object>();
//			map.put("SaveType", "0");
//			map.put("DeclType", "1");
//			map.put("ManiGoods", maniInfoList);
//			map.put("Manibase", maniheadList.get(0));

		JSONObject jsonObject = new JSONObject();
		//构建接口json.表头
		JSONObject manibase = new JSONObject();

		manibase.put("ManifestId", manihead.getManifestId()==null?null:manihead.getManifestId());
		manibase.put("TradeCode",  manihead.getTradeCode()==null?null:manihead.getTradeCode());
		manibase.put("TradeName",  manihead.getTradeName()==null?null:manihead.getTradeName());
		manibase.put("CustomsCode",manihead.getCustomsCode()==null?null:manihead.getCustomsCode());
		manibase.put("IeFlag",manihead.getIeFlag()==null?null:manihead.getIeFlag());
		manibase.put("GoodsValue", manihead.getGoodsValue()==null?null:manihead.getGoodsValue());

		manibase.put("GrossWt", opManiInfoService.sumGrossWtByHeadId(id));

		manibase.put("VehicleId", manihead.getVehicleId()==null?null:manihead.getVehicleId());
		manibase.put("VehicleWeight", manihead.getVehicleWeight()==null?null:manihead.getVehicleWeight());
		manibase.put("Status", manihead.getStatus()==null?null:manihead.getStatus());
		manibase.put("PassStatus", manihead.getPassStatus()==null?null:manihead.getPassStatus());
		manibase.put("InputEr", manihead.getInputEr()==null?null:manihead.getInputEr());
		manibase.put("InputDate", manihead.getInputDate()==null?null:manihead.getInputDate());
		manibase.put("DDate", manihead.getDDate()==null?null:manihead.getDDate());
		manibase.put("DNote", manihead.getDNote()==null?null:manihead.getDNote());
		manibase.put("ApprDate", manihead.getApprDate()==null?null:manihead.getApprDate());
		manibase.put("ApprNote", manihead.getApprNote()==null?null:manihead.getApprNote());
		manibase.put("OrgCode", manihead.getOrgCode()==null?null:manihead.getOrgCode());
		manibase.put("ContaId", manihead.getContaId()==null?null:manihead.getContaId());
		manibase.put("PassportDate", manihead.getPassportDate()==null?null:manihead.getPassportDate());
		manibase.put("DeclType", manihead.getDeclType()==null?null:manihead.getDeclType());
		manibase.put("SeqNo", manihead.getSeqNo()==null?null:manihead.getSeqNo());
		manibase.put("CusStatus", manihead.getCusStatus()==null?null:manihead.getCusStatus());
		manibase.put("CusRmk", manihead.getCusRmk()==null?null:manihead.getCusRmk());

		//表头加入json中
		jsonObject.put("Manibase", manibase);

		//构建货物信息
		JSONArray jsonArray = new JSONArray();

		for (OpManiInfo maniInfo :maniInfoList) {
			JSONObject maniGoods = new JSONObject();
			maniGoods.put("ManifestId", maniInfo.getManifestId()==null?null:maniInfo.getManifestId());
			maniGoods.put("GNo",maniInfo.getgNo()==null?null:maniInfo.getgNo());
			maniGoods.put("ApprId", maniInfo.getApprId()==null?null:maniInfo.getApprId());
			maniGoods.put("ApprGNo", maniInfo.getApprGNo()==null?null:maniInfo.getApprGNo());
			maniGoods.put("CodeTs",maniInfo.getCodeTs()==null?null:maniInfo.getCodeTs());
			maniGoods.put("GName", maniInfo.getGName()==null?null:maniInfo.getGName());
			maniGoods.put("GModel",maniInfo.getGModel()==null?null:maniInfo.getGModel());
			maniGoods.put("GUnit",maniInfo.getGUnit()==null?null:maniInfo.getGUnit());
			maniGoods.put("GQty", maniInfo.getGQty()==null?null:maniInfo.getGQty());
			maniGoods.put("Unit1", maniInfo.getGUnit1()==null?null:maniInfo.getGUnit1());
			maniGoods.put("Qty1", maniInfo.getGQty1()==null?null:maniInfo.getGQty1());
			maniGoods.put("GrossWt",maniInfo.getGrossWt()==null?null:maniInfo.getGrossWt());
			maniGoods.put("ConfirmQty",maniInfo.getConfirmQty()==null?null:maniInfo.getConfirmQty());
			maniGoods.put("EmsGNo",maniInfo.getEmsGNo()==null?null:maniInfo.getEmsGNo());
			maniGoods.put("GdsMtno",maniInfo.getGdsMtno()==null?null:maniInfo.getGdsMtno());
			jsonArray.add(maniGoods);

		}

		//货物信息加入
		jsonObject.put("ManiGoods", jsonArray);
		jsonObject.put("DeclType","1");//1首次申报2变更3作废
		jsonObject.put("SaveType","1");// 0-暂存，1-申报
		System.out.println(">>>>>>>>>>>>>>>>>调用核放单申报json： "+jsonObject.toJSONString());
		logger.error(">>>>>>>>>>>>>>>>>调用核放单申报json： "+jsonObject.toJSONString());
		//调用接口
		// 1 获得key
		String tickId = getKeyService.builder();
		System.out.println(tickId);
		// 2 调用接口
		//设置接口名
		String serviceName = "ManiSave";

		// 调用 申请单保存
		String result = fljgWsClient.getResult(jsonObject.toJSONString(), tickId, serviceName);
		System.out.println("result: " + result);
		logger.error(">>>>>>>>>>>>>>>>>调用核放单申报result： "+result);
		//收到返回值后处理，保存核放单号至MainHead 和 MainInfo
		JSONObject jsonResult = JSON.parseObject(result);
		String state = jsonResult.getString("state");
		if(state.equals("1")) {
			String data = jsonResult.getString("data");
			//根据id设置记录中的Apprid，同时更新stauts状态
			opManiHeadService.updateManiFestIdById(data,"1" ,id);
			opManiInfoService.updateManiFestIdbyHeadId(data, id);
			return "success";
		}else {
			String message = jsonResult.getString("message");
			String CheckInfos = jsonResult.getString("CheckInfos");
			return message+CheckInfos;
		}
	}
	


}
