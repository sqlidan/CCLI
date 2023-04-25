package com.haiersoft.ccli.supervision.web;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.xml.rpc.ServiceException;

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
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ManiHead;
import com.haiersoft.ccli.supervision.service.FljgWsClient;
import com.haiersoft.ccli.supervision.service.GetKeyService;
import com.haiersoft.ccli.supervision.service.ManiHeadService;
import com.haiersoft.ccli.supervision.service.ManiInfoService;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;

/**
 * 分类监管 空车核放单controller
 * 空车核放单 申报/作废/保存
 * 
 * @author
 *
 */

@Controller
@RequestMapping("supervision/mani/empty/")
public class EmptyManiController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(EmptyManiController.class);
	@Autowired
	ManiHeadService maniHeadService;

	@Autowired
	ManiInfoService maniInfoService;

	@Autowired
	EnterStockInfoService enterStockInfoService;

	@Autowired
	GetKeyService getKeyService;

	@Autowired
	FljgWsClient fljgWsClient;

	// 空车核放单申报
	@Transactional
	@RequestMapping(value = "apply/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String applyData(HttpServletRequest request, @PathVariable("id") String id)
			throws IOException, ServiceException {
		// 根据linkId查询ManiHead
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_id", id));
		List<ManiHead> maniheadList = maniHeadService.search(filters);


			ManiHead manihead = maniheadList.get(0);

			JSONObject jsonObject = new JSONObject();
			// 构建接口json.表头
			JSONObject manibase = new JSONObject();

			manibase.put("ManifestId", manihead.getManifestId() == null ? null : manihead.getManifestId());
			manibase.put("TradeCode", manihead.getTradeCode() == null ? null : manihead.getTradeCode());
			manibase.put("TradeName", manihead.getTradeName() == null ? null : manihead.getTradeName());
			manibase.put("CustomsCode", manihead.getCustomsCode() == null ? null : manihead.getCustomsCode());
			manibase.put("IeFlag", manihead.getIeFlag() == null ? null : (manihead.getIeFlag().equals("E")?"2":"1"));
			manibase.put("GoodsValue", manihead.getGoodsValue() == null ? null : manihead.getGoodsValue());
			manibase.put("GrossWt", manihead.getGrossWt() == null ? null : manihead.getGrossWt());
			manibase.put("VehicleId", manihead.getVehicleId() == null ? null : manihead.getVehicleId());
			manibase.put("VehicleWeight", manihead.getVehicleWeight() == null ? null : manihead.getVehicleWeight());
			manibase.put("Status", manihead.getStatus() == null ? null : manihead.getStatus());
			manibase.put("PassStatus", manihead.getPassStatus() == null ? null : manihead.getPassStatus());
			manibase.put("InputEr", manihead.getInputEr() == null ? null : manihead.getInputEr());
			manibase.put("InputDate", manihead.getInputDate() == null ? null : manihead.getInputDate());
			manibase.put("DDate", manihead.getDDate() == null ? null : manihead.getDDate());
			manibase.put("DNote", manihead.getDNote() == null ? null : manihead.getDNote());
			manibase.put("ApprDate", manihead.getApprDate() == null ? null : manihead.getApprDate());
			manibase.put("ApprNote", manihead.getApprNote() == null ? null : manihead.getApprNote());
			manibase.put("OrgCode", manihead.getOrgCode() == null ? null : manihead.getOrgCode());
			//manibase.put("ContaId", manihead.getContaId() == null ? null : manihead.getContaId());
			//manibase.put("PassportDate", manihead.getPassportDate() == null ? null : manihead.getPassportDate());
			//manibase.put("DeclType", manihead.getDeclType() == null ? null : manihead.getDeclType());
			//manibase.put("SeqNo", manihead.getSeqNo() == null ? null : manihead.getSeqNo());
			//manibase.put("CusStatus", manihead.getCusStatus() == null ? null : manihead.getCusStatus());
			//manibase.put("CusRmk", manihead.getCusRmk() == null ? null : manihead.getCusRmk());

			// 表头加入json中
			jsonObject.put("Manibase", manibase);

			// 货物信息加入
			jsonObject.put("ManiGoods", null);
			jsonObject.put("DeclType", "1");// 1首次申报2变更3作废
			jsonObject.put("SaveType", "1");// 0-暂存，1-申报
			jsonObject.put("ManiContas", null);

			System.out.println(jsonObject.toJSONString());
			logger.error(">>>>>>>>>>>>>>>>>调用空车核放单申报json： "+jsonObject.toJSONString());
			// 调用接口
			// 1 获得key
			String tickId = getKeyService.builder();
			//System.out.println(tickId);
			if((null ==tickId) || ("".equals(tickId)))
			{
				return "没有获得tickId,请重新操作";
			}
			// 2 调用接口
			// 设置接口名
			String serviceName = "SasManiSave";

			String result = fljgWsClient.getResult(jsonObject.toJSONString(), tickId, serviceName);
			System.out.println("result: " + result);
			logger.error(">>>>>>>>>>>>>>>>>调用空车核放单申报result: "+result);
			// 收到返回值后处理，保存核放单号至MainHead
			JSONObject jsonResult = JSON.parseObject(result);
			String state = jsonResult.getString("state");
			if (state.equals("1")) {
				String data = jsonResult.getString("data");
				// 根据id设置记录中的Apprid，同时更新stauts状态
				maniHeadService.updateManiFestIdById(data, "1", id);
				return "success";
			} else {
				String message = jsonResult.getString("message");
				String CheckInfos = jsonResult.getString("CheckInfos");
				return message + CheckInfos;
			}

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
			String serviceName = "SasManiNullify";
			for(ManiHead mani:toCancelRemote) {
				String maninullify="{\"KeyModel\":{\"ManifestId\":\""+mani.getManifestId()+"\"}}";
				logger.error(">>>>>>>>>>>>>>>>>调用空车核放单作废json： "+maninullify);
				String result = fljgWsClient.getResult(maninullify, tickId, serviceName);
				System.out.println("result: " + result);
				logger.error(">>>>>>>>>>>>>>>>>调用空车核放单作废result： "+result);
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
	
	@RequestMapping(value="/save", method = RequestMethod.POST)
	@ResponseBody
	public String saveManiHead(ManiHead maniHead) {			

		makeEmptyMini(maniHead);

		return "success";
	}
	
	
	@Transactional
	private void makeEmptyMini(ManiHead maniHead) {
		// 核放单头
		maniHead.setEmptyFlag("Y");
		maniHead.setIeFlagNote("空车核放");
		maniHead.setLocalStatus("1");
		maniHead.setManiConfirmStatus("N");
		maniHead.setCreateTime(new Date());
		maniHeadService.save(maniHead);
		
	}
}
