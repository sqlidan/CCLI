package com.haiersoft.ccli.supervision.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.entity.ManiHead;
import com.haiersoft.ccli.supervision.entity.ManiInfo;
import com.haiersoft.ccli.supervision.service.ApprHeadService;
import com.haiersoft.ccli.supervision.service.ApprInfoService;
import com.haiersoft.ccli.supervision.service.ManiHeadService;
import com.haiersoft.ccli.supervision.service.ManiInfoService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
import com.haiersoft.ccli.wms.service.OutStockInfoService;

/**
 * 分类监管 核放单controller
 * @author 
 *
 */

@Controller
@RequestMapping("supervision/mani")
public class OutManiController extends BaseController{
	@Autowired
	ManiHeadService maniHeadService;
	
	@Autowired
	ManiInfoService maniInfoService;
	
	@Autowired
	ApprInfoService apprInfoService;
	
	@Autowired
	ApprHeadService apprHeadService;
	
    @Autowired
    private LoadingOrderService loadingOrderService;
    @Autowired
    private LoadingOrderInfoService loadingOrderInfoService;

	
	@RequestMapping(value="/out/save", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String saveManiHead(ManiHead maniHead) {
		
		//检查货物明细行是否生成有对应的申请单
		//没有申请单的记录不能生成核放单
		List<String> list = Arrays.asList(maniHead.getInfoIds().split(","));
		//查询到订单

		for(String orderNo:list) {
			BisLoadingOrder order = loadingOrderService.get(orderNo);
			//根据订单查到联系单号查找申请单
			List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQS_linkId", order.getOutLinkId()));
			List<ApprHead> headList = apprHeadService.search(filters);
			//如果没有对应的申请单或者ApprId，不能生成核放单
			if (headList.size() == 0 || headList == null) {
				return "没有对应的申请单";
			}else if (StringUtils.isBlank(headList.get(0).getApprId()) || headList.get(0).getApprId() == null) {
				return "没有生成ApprId";
			}

		}

//		outDoing(maniHead);;//2024-11-20 徐峥注释
		return outDoing2(maniHead);//2024-11-20 徐峥编写
	}
	
	@Autowired
	OutStockInfoService outStockInfoService;
	
	@Transactional
	String outDoing2(ManiHead maniHead) {
		Date currDate = new Date();
		// 核放单头
		maniHead.setIeFlag("E");
		maniHead.setEmptyFlag("N");
		maniHead.setIeFlagNote("出库核放");
		maniHead.setLocalStatus("1");
		maniHead.setCreateTime(currDate);
		//截取最前边的箱号作为核放单头部的箱号
		String contaIdStr = maniHead.getContaId();
		String contaId = "";
		if (contaIdStr!= null && contaIdStr.trim().length() > 0){
			if (contaIdStr.contains(",")){
				String[] strings = contaIdStr.split(",");
				for (int i = 0; i < strings.length; i++) {
					if (strings[i]!= null && strings[i].trim().length() == 11){
						contaId = strings[i];
						break;
					}
				}
			}else{
				contaId = contaIdStr;
			}
		}
		if (contaId!=null && contaId.trim().length() == 11){
			maniHead.setContaId(contaId);
		}else{
			return "箱号格式不正确";
		}
		maniHeadService.save(maniHead);
		
		List<String> list = Arrays.asList(maniHead.getInfoIds().split(","));
		for(String orderNum:list) {
			//根据选中的货物信息生成核放单行
			List<String> billNumList = new ArrayList<>();
			List<BisLoadingOrderInfo> infos = loadingOrderInfoService.getInfoList(orderNum);
			for(BisLoadingOrderInfo orderInfo : infos) {
				if (!billNumList.contains(orderInfo.getBillNum())){
					billNumList.add(orderInfo.getBillNum());
				}
			}
			for (int i = 0; i < billNumList.size(); i++) {
				Integer piece = 0;
				Double grossWeight = 0.00;
				for(BisLoadingOrderInfo orderInfo : infos) {
					piece += (orderInfo.getPiece()==null?0:orderInfo.getPiece());
					grossWeight += (orderInfo.getGrossWeight()==null?0.00:orderInfo.getGrossWeight());
				}
				//根据提单号和出库联系单号查找出库联系单明细
				List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
				filters.add(new PropertyFilter("EQS_outLinkId", infos.get(0).getOutLinkId()));
				filters.add(new PropertyFilter("EQS_billNum", billNumList.get(i)));
				List<BisOutStockInfo> bisOutStockInfoList = outStockInfoService.search(filters);
				//根据提单号和出库联系单号查找出库联系单明细
				List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
				filters2.add(new PropertyFilter("EQS_linkId", infos.get(0).getOutLinkId()));
				filters2.add(new PropertyFilter("LIKES_bisInfoId", bisOutStockInfoList.get(0).getId()));
				List<ApprInfo> apprInfoList = apprInfoService.search(filters2);

				ManiInfo maInfo = new ManiInfo();
				maInfo.setApprId(apprInfoList.get(0).getApprId());
				maInfo.setApprGNo(apprInfoList.get(0).getApprGNo());
				maInfo.setgNo(i+1);
				maInfo.setCodeTs(apprInfoList.get(0).getCodeTs());
				maInfo.setGName(apprInfoList.get(0).getgName());
				maInfo.setGModel(apprInfoList.get(0).getgName());
				maInfo.setGUnit("035");
				maInfo.setGQty(String.valueOf(piece));
				maInfo.setGrossWt(String.valueOf(grossWeight));
				maInfo.setGQty1(maInfo.getGQty());
				maInfo.setGUnit1(maInfo.getGUnit());
				maInfo.setEmsGNo(String.valueOf(apprInfoList.get(0).getgNo()));
				maInfo.setHeadId(maniHead.getId());
				maInfo.setContaId(apprInfoList.get(0).getCtnNum());
				maInfo.setCreateTime(currDate);
				maniInfoService.save(maInfo);
			}
		}

		//2024-12-06 经沟通后注释此处(出库空车核放，进区信息)信息及明细的创建
//		// 空车核放单头
//		ManiHead mh = new ManiHead();
//		mh.setIeFlag("I");
//		mh.setEmptyFlag("Y");
//		mh.setIeFlagNote("出库空车核放");
//		mh.setVehicleId(maniHead.getVehicleId());
//		mh.setVehicleWeight(maniHead.getVehicleWeight());
//		mh.setLocalStatus("1");
//		mh.setDNote(maniHead.getDNote());
//		mh.setCreateTime(currDate);
//		maniHeadService.save(mh);
//
//		// 空车核放单表体
//		ManiInfo maniInfo = new ManiInfo();
//		maniInfo.setHeadId(mh.getId());
//
//		//前端传来的箱号
//		//maniInfo.setContaId(maniHead.getContaId());
//		//前端传来的箱型
//		maniInfo.setContaType(maniHead.getCtnTypeSize());
//		//根据前端传来的箱型匹配箱重
//		if(maniHead.getCtnTypeSize().equals("20")) {
//			maniInfo.setContaWeight("2800");
//		}else if(maniHead.getCtnTypeSize().equals("40")){
//			maniInfo.setContaWeight("47000");
//		}else {
//			maniInfo.setContaWeight("");
//		}
//		maniInfoService.save(maniInfo);
		return "success";
	}

	@Transactional
	void outDoing(ManiHead maniHead) {
		Date currDate = new Date();
		// 核放单头
		maniHead.setIeFlag("E");
		maniHead.setEmptyFlag("N");
		maniHead.setIeFlagNote("出库核放");
		maniHead.setLocalStatus("1");
		maniHead.setCreateTime(currDate);

		//截取最前边的箱号作为核放单头部的箱号
		String contaIdStr = maniHead.getContaId();
		maniHead.setContaId(contaIdStr.substring(0, contaIdStr.indexOf(",")));
		maniHeadService.save(maniHead);

		List<String> list = Arrays.asList(maniHead.getInfoIds().split(","));
		int index = 1;
		for(String orderNum:list) {
			BisLoadingOrder order = loadingOrderService.get(orderNum);

			//根据联系单号查询对应的申请单
//			List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
//			filters.add(new PropertyFilter("EQS_linkId", order.getOutLinkId()));
//			List<ApprInfo> apprInfoList = apprInfoService.search(filters);

			//填充核放单表体
			//根据选中的货物信息生成核放单行
			List<BisLoadingOrderInfo> infos = loadingOrderInfoService.getInfoList(orderNum);

			for(BisLoadingOrderInfo orderInfo : infos) {

				//根据箱号和联系单号查找出库联系单info
				List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
				filters.add(new PropertyFilter("EQS_outLinkId", orderInfo.getOutLinkId()));
				filters.add(new PropertyFilter("EQS_ctnNum", orderInfo.getCtnNum()));
				List<BisOutStockInfo> BisOutStockInfo = outStockInfoService.search(filters);
				//根据联系单info的ID查找申请单info
				ApprInfo apprInfo = apprInfoService.findApprInfosByOutBisInfoId(String.valueOf(BisOutStockInfo.get(0).getId()));

				ManiInfo maInfo = new ManiInfo();

				maInfo.setApprId(apprInfo.getApprId());
				maInfo.setApprGNo(apprInfo.getApprGNo());

				//gno底账项号为 原入库货物申请单的底账项号
				ApprInfo apprInfoIn = this.findGnoFromInAppr(orderInfo.getBillNum(),orderInfo.getCtnNum(),orderInfo.getCatgoName());
				if (apprInfoIn != null) {
					maInfo.setgNo(apprInfoIn.getgNo());
					maInfo.setCodeTs(apprInfoIn.getCodeTs());
				}

				maInfo.setGName(orderInfo.getCatgoName());
				maInfo.setGModel(orderInfo.getTypeSize());
				maInfo.setGUnit("035");
				maInfo.setGQty(String.valueOf(orderInfo.getPiece()));
				maInfo.setGrossWt(String.valueOf(orderInfo.getGrossWeight()));
				maInfo.setHeadId(maniHead.getId());
				maInfo.setContaId(orderInfo.getCtnNum());
				maInfo.setCreateTime(currDate);
				maniInfoService.save(maInfo);
			}

		}

		//2024-12-06 经沟通后注释此处(出库空车核放，进区信息)信息及明细的创建
//		// 空车核放单头
//		ManiHead mh = new ManiHead();
//		mh.setIeFlag("I");
//		mh.setEmptyFlag("Y");
//		mh.setIeFlagNote("出库空车核放");
//		mh.setVehicleId(maniHead.getVehicleId());
//		mh.setVehicleWeight(maniHead.getVehicleWeight());
//		mh.setLocalStatus("1");
//		mh.setDNote(maniHead.getDNote());
//		mh.setCreateTime(currDate);
//		maniHeadService.save(mh);
//
//		// 空车核放单表体
//		ManiInfo maniInfo = new ManiInfo();
//		maniInfo.setHeadId(mh.getId());
//
//		//前端传来的箱号
//		//maniInfo.setContaId(maniHead.getContaId());
//		//前端传来的箱型
//		maniInfo.setContaType(maniHead.getCtnTypeSize());
//		//根据前端传来的箱型匹配箱重
//		if(maniHead.getCtnTypeSize().equals("20")) {
//			maniInfo.setContaWeight("2800");
//		}else if(maniHead.getCtnTypeSize().equals("40")){
//			maniInfo.setContaWeight("47000");
//		}else {
//			maniInfo.setContaWeight("");
//		}
//		maniInfoService.save(maniInfo);

	}

	/**
	 *
	 * @param billNum 提单号
	 * @param ctnNum  箱号
	 * @param catgoName 货物名称
	 * @return
	 */

	private ApprInfo findGnoFromInAppr(String billNum, String ctnNum, String catgoName) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_itemNum", billNum));
		List<ApprHead> headList = apprHeadService.search(filters);
		//没有查到原入库申请单的，返回0
		if (headList.size() == 0 || headList == null) {
			return null;
		}
		ApprHead apprHead = headList.get(0);

		List<PropertyFilter> infofilters = new ArrayList<PropertyFilter>();
		infofilters.add(new PropertyFilter("EQS_headId", apprHead.getId()));
		infofilters.add(new PropertyFilter("EQS_ctnNum", ctnNum));
		infofilters.add(new PropertyFilter("EQS_gName", catgoName));
		List<ApprInfo> infoList = apprInfoService.search(infofilters);
		//没有查到原入库申请单详情的，返回0
		if (infoList.size() == 0 || infoList == null) {
			return null;
		}
		return infoList.get(0);
	}


}
