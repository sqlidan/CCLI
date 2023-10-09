package com.haiersoft.ccli.supervision.web;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.entity.ManiHead;
import com.haiersoft.ccli.supervision.entity.ManiInfo;
import com.haiersoft.ccli.supervision.service.FljgWsClient;
import com.haiersoft.ccli.supervision.service.GetKeyService;
import com.haiersoft.ccli.supervision.service.ManiHeadService;
import com.haiersoft.ccli.supervision.service.ManiInfoService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;

/**
 * 分类监管 核放单controller
 * @author 
 *
 */

@Controller
@RequestMapping("supervision/mani")
public class ManiController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(ManiController.class);
	@Autowired
	ManiHeadService maniHeadService;
	
	@Autowired
	ManiInfoService maniInfoService;
	
    @Autowired
    EnterStockInfoService enterStockInfoService;
	
	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fljg/maniList";
	}
	
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<ManiHead> page = getPage(request);
		page.orderBy("createTime").order("desc");
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = maniHeadService.search(page, filters);
		return getEasyUIData(page);
	}
	
	
	//删除核放单
	@Transactional
	@RequestMapping(value="/del/{ids}")
	@ResponseBody
	public String deleteManiHead(@PathVariable("ids") List<String> ids) throws RemoteException, ServiceException {
				
		List<ManiHead> toDeleteRemote = new ArrayList<ManiHead>();
		List<ManiHead> toDeleteLocal = new ArrayList<ManiHead>();		
		//检查本地状态,先整理出需要接口删除的核放单
		for(String id : ids) {			
			ManiHead maniHead = maniHeadService.get(id);
			if((null ==maniHead.getManifestId()) || ("".equals(maniHead.getManifestId()))) {				
				toDeleteLocal.add(maniHead);
			}else {
				toDeleteRemote.add(maniHead);
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
			for(ManiHead mani:toDeleteRemote) {
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
		        	maniHeadService.delete(mani.getId());
		        	maniInfoService.deleteByHeadId(mani.getId());
		        	return "success";
		        }else {
		        	
		        	return "接口错误";
		        }
			}			
		}

		//不需要调用接口删除的 list
		for(ManiHead mani:toDeleteLocal) {
        	maniHeadService.delete(mani.getId());
        	maniInfoService.deleteByHeadId(mani.getId());
		}

		return "success";


	}
	
	//作废核放单
	@Transactional
	@RequestMapping(value="/cancel/{ids}")
	@ResponseBody
	public String cancelMani(@PathVariable("ids") List<String> ids) throws RemoteException, ServiceException {
		
		List<ManiHead> toCancelRemote = new ArrayList<ManiHead>();
		List<ManiHead> toCancelLocal = new ArrayList<ManiHead>();		
		//检查本地状态,先整理出需要接口作废的核放单
		for(String id : ids) {			
			ManiHead maniHead = maniHeadService.get(id);
			if((null ==maniHead.getManifestId()) || ("".equals(maniHead.getManifestId()))) {		
				toCancelLocal.add(maniHead);
			}else {
				toCancelRemote.add(maniHead);
			}				
		}
		
		//需要调用接口作废的mani
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
			String serviceName = "ManiNullify";
			for(ManiHead mani:toCancelRemote) {
				String maninullify="{\"KeyModel\":{\"ManifestId\":\""+mani.getManifestId()+"\"}}";
				logger.error(">>>>>>>>>>>>>>>>>调用核放单作废json： "+maninullify);
				String result = fljgWsClient.getResult(maninullify, tickId, serviceName);
				System.out.println("result: " + result);
				logger.error(">>>>>>>>>>>>>>>>>调用核放单作废result： "+result);
				JSONObject jsonObject = JSON.parseObject(result);
		        String state = jsonObject.getString("state");
		        //如果接口state返回1为作废成功
		        if(state.equals("1")) {
		        	//作废本地数据库中的记录
		        	mani.setDeclType("2");
		        	mani.setLocalStatus("3");
		        	maniHeadService.save(mani);
		        	return "success";
		        }else {
		        	
		        	return "接口错误";
		        }
			}			
		}		

		//不需要调用接口作废的 list
		for(ManiHead mani:toCancelLocal) {
			mani.setLocalStatus("2");
			maniHeadService.save(mani);
		}

		return "success";		
	}	

	//新增空车核放单
	@RequestMapping(value = "createEmp", method = RequestMethod.GET)
	public String createEmp(Model model) {

		return "fljg/empManiForm";
	}
	
	//保存空车核放单
	@RequestMapping(value = "emp/save", method = RequestMethod.POST)
	public String empSave(ManiHead maniHead) {

		maniHead.setIeFlag("I");
		maniHead.setEmptyFlag("Y");
		maniHead.setIeFlagNote("虚拟核放单");
		maniHead.setLocalStatus("1");
		maniHeadService.save(maniHead);
		return "success";
	}
	
	
	@Autowired
	GetKeyService getKeyService;
	
	@Autowired
	FljgWsClient fljgWsClient;
	// 核放单申报
	@Transactional
	@RequestMapping(value = "apply/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String applyData(HttpServletRequest request, @PathVariable("id") String id) throws IOException, ServiceException {
		//TODO 临时作废 申报时海关会校验，如果没有库存让业务再去确认就好
//		//2023-10-09 徐峥添加核放单申报之前校验是否入库到货确认逻辑
//		ManiHead ihead = maniHeadService.get(id);
//		if(ihead == null) {
//			return "未找到对应的核放单";
//		}
//		if(ihead.getPassStatus()==null || !ihead.getPassStatus().equals("2")) {
//			return "请先执行入库到货确认!";
//		}

		//根据linkId查询ManiHead
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_id", id));
		List<ManiHead> maniheadList = maniHeadService.search(filters);		
	
		//根据ApprHead的ID查询ApprInfo
		if (!maniheadList.isEmpty()) {
			List<PropertyFilter> infofilters = new ArrayList<PropertyFilter>();
			infofilters.add(new PropertyFilter("EQS_headId", maniheadList.get(0).getId()));
			List<ManiInfo> maniInfoList = maniInfoService.search(infofilters);
			
			//拼装Json
//			Map<String,Object> map = new HashMap<String, Object>();
//			map.put("SaveType", "0");
//			map.put("DeclType", "1");
//			map.put("ManiGoods", maniInfoList);
//			map.put("Manibase", maniheadList.get(0));	
			ManiHead manihead = maniheadList.get(0);
			
			JSONObject jsonObject = new JSONObject();
			//构建接口json.表头
            JSONObject manibase = new JSONObject();
            
            manibase.put("ManifestId", manihead.getManifestId()==null?null:manihead.getManifestId());
            manibase.put("TradeCode",  manihead.getTradeCode()==null?null:manihead.getTradeCode());
            manibase.put("TradeName",  manihead.getTradeName()==null?null:manihead.getTradeName());
            manibase.put("CustomsCode",manihead.getCustomsCode()==null?null:manihead.getCustomsCode());
            manibase.put("IeFlag",manihead.getIeFlag()==null?null:manihead.getIeFlag());
            manibase.put("GoodsValue", manihead.getGoodsValue()==null?null:manihead.getGoodsValue());
            manibase.put("GrossWt", manihead.getGrossWt()==null?null:manihead.getGrossWt());
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
            
            for (ManiInfo maniInfo :maniInfoList) {
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
            	maniHeadService.updateManiFestIdById(data,"1" ,id);
            	maniInfoService.updateManiFestIdbyHeadId(data, id);
            	return "success";
            }else {
            	String message = jsonResult.getString("message");
            	String CheckInfos = jsonResult.getString("CheckInfos");
            	return message+CheckInfos;
            }
			
		}
		return "success";
		
	}
	


}
