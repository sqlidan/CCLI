package com.haiersoft.ccli.supervision.web;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.base.entity.BaseHscode;
import com.haiersoft.ccli.base.service.HscodeService;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ManiHead;
import com.haiersoft.ccli.supervision.entity.ManiInfo;
import com.haiersoft.ccli.supervision.service.FljgWsClient;
import com.haiersoft.ccli.supervision.service.GetKeyService;
import com.haiersoft.ccli.supervision.service.ManiHeadService;
import com.haiersoft.ccli.supervision.service.ManiInfoService;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ASNInfoService;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.TrayInfoService;

@Controller
@RequestMapping("supervision/maniconfirm")
public class ManiConfirmController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(ManiConfirmController.class);

	@Autowired
	ManiHeadService maniHeadService;

	@Autowired
	ManiInfoService maniInfoService;

	@Autowired
	private ASNService asnService;
	
	@Autowired
	private ASNInfoService asnInfoService;
	
	@Autowired
	HscodeService hscodeService;
	
	@Autowired
	FljgWsClient fljgWsClient;
	
	@Autowired
	GetKeyService getKeyService;

	//到货确认
	//@Transactional
	@ResponseBody
	@RequestMapping(value = "/request/{id}", method = RequestMethod.GET)
	public String confirmRequest(@PathVariable("id") String headId) throws RemoteException, ServiceException {
		ManiHead ihead = maniHeadService.get(headId);
		List<ManiHead> maniHeadList = new ArrayList<ManiHead>();
		maniHeadList.add(ihead);
		
		if(ihead.getPassStatus().equals("2")) {
			return "此核放单已到货!";
		}

		if(maniHeadList.size()==0) {
			return "没有需要提交到货确认的核放单";
		}
//		// 填充ConfirmQty 字段
//		for (ManiHead mh : maniHeadList) {
//			List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
//			filters.add(new PropertyFilter("EQS_headId", mh.getId()));
//			List<ManiInfo> mInfoList = maniInfoService.search(filters);
//			for (ManiInfo mInfo : mInfoList) {
//				if (null==mInfo.ConfirmQty || mInfo.ConfirmQty.equals("")) {
//					//这里要注意ManiInfo中的提单号与BisAsn中的billNum 有唯一对应的关系
//					//BisAsn asn = asnService.find("billNum", mInfo.BisItemNum);
//
//					//根据 提单号/箱号/入库类型为”正常”/ASN状态为”已上架”来查询唯一的asn，如果存在即为到货，查询不到即为未到货
//	                Map<String, Object> params = new HashMap<String, Object>();
//	                params.put("billNum", mInfo.BisItemNum);
//	                params.put("ctnNum", mh.getContaId());
//	                params.put("ifSecondEnter", "1"); // 入库类型 1.在库
//	                params.put("asnState", "3"); //已上架
//	                List<BisAsn> asnList = asnService.getList(params);
//					// 排除 asn状态不等于"3 已上架的"
//					if (0==asnList.size()) {
//						//maniHeadList.remove(mh);
//						//无操作
//					} else {
//						//填充ConfirmQty 字段
//						BisAsn asn = asnList.get(0);
//						BisAsnInfo asnInfo = asnInfoService.findBisAsnInfo(asn.getAsn(), mInfo.getBisSku());
//						if(null != asnInfo) {
//
//							String hsCode = asnInfo.getHsCode();
//							if(null !=hsCode) {
//								//根据HScode 查询对应的单位代码
////								BaseHscode bhs = hscodeService.find("code", hsCode);
////								if(bhs.getItemNum()=="035") {
////									//单位为035时 实际入库件数*单净
////									mInfo.setConfirmQty(String.valueOf(asnInfo.getPieceReal()*asnInfo.getNetSingle()));
////								}else {
////									//单位不是 035时 发 实际入库件数
////									mInfo.setConfirmQty(String.valueOf(asnInfo.getPieceReal()));
////								}
//
//							}
//
////							mInfo.setConfirmQty(String.valueOf(asnInfo.getPieceReal()));
//							String counts = cuntTrayInfoPieces(asn.getAsn(), mInfo.getBisSku());
//							if(!"0".equals(counts)) {
//								mInfo.setConfirmQty(counts);
//								//保存ManiInfo ，实际上是保存ConfirmQty字段
//								maniInfoService.save(mInfo);
//							}
//
//
//						}
//						//此处要增加找不到asn到货信息时的处理
//
//					}
//
//				}
//
//			}
//
//		}

		// 1 获得key
		String tickId = getKeyService.builder();
		System.out.println(tickId);
		//设置接口名
		String serviceName = "ManiConfirm";
		String json = buildJson(ihead);
		logger.error(">>>>>>>>>>>>>>>>>调用核放单到货确认json： "+json);
		String result = fljgWsClient.getResult(json, tickId, serviceName);
		System.out.println("result: " + result);
		logger.error(">>>>>>>>>>>>>>>>>调用核放单到货确认result： "+result);
		JSONObject jsonObject = JSON.parseObject(result);
		String state = jsonObject.getString("state");
		//如果接口state返回1为提交成功
		if(state.equals("1")) {
			ihead.setPassStatus("2");
			ihead.setManiConfirmStatus("Y");
			maniHeadService.save(ihead);
		}else {
			return "接口错误";
		}
		return "success";

	}
	
	public String buildJson(ManiHead maniHead) {
		
		List<PropertyFilter> infofilters = new ArrayList<PropertyFilter>();
		infofilters.add(new PropertyFilter("EQS_headId", maniHead.getId()));
		List<ManiInfo> maniInfoList = maniInfoService.search(infofilters);		
		//拼装Json		
		JSONObject jsonObject = new JSONObject();
		//构建接口json Manibase 表头
        JSONObject manibase = new JSONObject();
        
        manibase.put("ManifestId", maniHead.getManifestId()==null?null:maniHead.getManifestId());
        manibase.put("TradeCode",  maniHead.getTradeCode()==null?null:maniHead.getTradeCode());
        manibase.put("TradeName",  maniHead.getTradeName()==null?null:maniHead.getTradeName());
        manibase.put("CustomsCode",maniHead.getCustomsCode()==null?null:maniHead.getCustomsCode());
        manibase.put("IeFlag",maniHead.getIeFlag()==null?null:maniHead.getIeFlag());
        manibase.put("GoodsValue", maniHead.getGoodsValue()==null?null:maniHead.getGoodsValue());
        manibase.put("GrossWt", maniHead.getGrossWt()==null?null:maniHead.getGrossWt());
        manibase.put("VehicleId", maniHead.getVehicleId()==null?null:maniHead.getVehicleId());
        manibase.put("VehicleWeight", maniHead.getVehicleWeight()==null?null:maniHead.getVehicleWeight());
        manibase.put("Status", maniHead.getStatus()==null?null:maniHead.getStatus());
        manibase.put("PassStatus", maniHead.getPassStatus()==null?null:maniHead.getPassStatus());
        manibase.put("InputEr", maniHead.getInputEr()==null?null:maniHead.getInputEr());
        manibase.put("InputDate", maniHead.getInputDate()==null?null:maniHead.getInputDate());
        manibase.put("DDate", maniHead.getDDate()==null?null:maniHead.getDDate());
        manibase.put("DNote", maniHead.getDNote()==null?null:maniHead.getDNote());
        manibase.put("ApprDate", maniHead.getApprDate()==null?null:maniHead.getApprDate());
        manibase.put("ApprNote", maniHead.getApprNote()==null?null:maniHead.getApprNote());
        manibase.put("OrgCode", maniHead.getOrgCode()==null?null:maniHead.getOrgCode());
        manibase.put("ContaId", maniHead.getContaId()==null?null:maniHead.getContaId());
        manibase.put("PassportDate", maniHead.getPassportDate()==null?null:maniHead.getPassportDate());
        manibase.put("DeclType", maniHead.getDeclType()==null?null:maniHead.getDeclType());
        manibase.put("SeqNo", maniHead.getSeqNo()==null?null:maniHead.getSeqNo());
        manibase.put("CusStatus", maniHead.getCusStatus()==null?null:maniHead.getCusStatus());
        manibase.put("CusRmk", maniHead.getCusRmk()==null?null:maniHead.getCusRmk());
		
        //表头加入json Manibase中
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
            maniGoods.put("ConfirmQty",maniInfo.getGQty()==null?null:maniInfo.getGQty());
            maniGoods.put("EmsGNo",maniInfo.getEmsGNo()==null?null:maniInfo.getEmsGNo());
            maniGoods.put("GdsMtno",maniInfo.getGdsMtno()==null?null:maniInfo.getGdsMtno());
            jsonArray.add(maniGoods);
        }        
        //货物信息加入
        jsonObject.put("ManiGoods", jsonArray);
        jsonObject.put("DeclType","1");//1首次申报2变更3作废
        jsonObject.put("SaveType","1");// 0-暂存，1-申报
		return jsonObject.toJSONString();
		
	}
	
	@Autowired
	TrayInfoService trayInfoService;

	// 根据库存查询到货件数
	private String cuntTrayInfoPieces(String asn, String sku) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("asn", asn);
		params.put("skuId", sku);
		List<TrayInfo> trayObj = trayInfoService.findBySku(params);
		Integer real = 0;
		if (!trayObj.isEmpty()) {
			for (TrayInfo obj : trayObj) {
				if (!obj.getCargoState().equals("99") && !obj.getCargoState().equals("00")) {
					real += obj.getOriginalPiece();
				}
			}
			return real.toString();
		} else {
			return "0";

		}

	}
}
