package com.haiersoft.ccli.supervision.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.HscodeService;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.entity.OutApprHead;
import com.haiersoft.ccli.supervision.service.ApprHeadService;
import com.haiersoft.ccli.supervision.service.ApprInfoService;
import com.haiersoft.ccli.supervision.service.OutApprHeadService;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.wms.service.OutStockInfoService;
import com.haiersoft.ccli.wms.service.OutStockService;

/**
 * 分类监管 申请单controller
 * 
 * @author
 *
 */

@Controller
@RequestMapping("supervision/outappr")
public class OutApprController extends BaseController {

	@Autowired
	private ApprHeadService apprHeadService;
	
	@Autowired
	private OutApprHeadService outApprHeadService;
	@Autowired
	private ApprInfoService apprInfoService;
	@Autowired
	private OutStockService outStockService;


	@Autowired
	private OutStockInfoService outStockInfoService;
	@Autowired
	private ClientService clientService;
	
	@Autowired
	HscodeService hscodeService;


	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String saveApprHead(OutApprHead apprHead) {
		// 判断是否已经存在申请单
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_linkId", apprHead.getLinkId()));
		List<ApprHead> isExist = apprHeadService.search(filters);
		if (!isExist.isEmpty()) {
			return "exist";
		}
		// 根据联系单生成申请单head
		BisOutStock bisOutStock = outStockService.get(apprHead.getLinkId());
		List<ApprInfo> apprInfoList = null;
		if (bisOutStock != null) {
			//判断当前入库联系单生成申请单是否为非保税数据
			if ("1".equals(bisOutStock.getIfBonded())){
				//保税
				return "当前入库联系单为保税数据，不可申请申请单";
			}
			/*
			 * BaseClientInfo baseClientInfo =
			 * clientService.get(Integer.valueOf(bisOutStock.getStockInId())); if
			 * (baseClientInfo != null) { //
			 * apprHead.setOwnerCode(baseClientInfo.getClientCode()); //
			 * apprHead.setOwnerName(baseClientInfo.getClientName());
			 * 
			 * }
			 */
			apprHead.setOwnerCode("3702631016");
			apprHead.setOwnerName("青岛港怡之航冷链物流有限公司");
			apprHead.setLocalStatus("1");
			apprHead.setCreateTime(new Date());

			List<BisOutStockInfo> list = outStockInfoService.getList(apprHead.getLinkId());

			//2024-11-19 徐峥注释
//			//整合提单号，方便申请单中依据提单号关联入库联系单获取商品编号
//			if(list!=null && list.size() > 0){
//				String billNum ="";
//				for (BisOutStockInfo forBisOutStockInfo:list) {
//					if(forBisOutStockInfo.getBillNum()!=null && forBisOutStockInfo.getBillNum().trim().length() > 0){
//						if(!billNum.contains(forBisOutStockInfo.getBillNum())){
//							billNum = billNum + forBisOutStockInfo.getBillNum().trim() + ",";
//						}
//					}
//				}
//				if(billNum.trim().length() > 0){
//					billNum = billNum.substring(0,billNum.length()-1);
//				}
//				apprHead.setItemNum(billNum);
//			}
//
//			//根据提单号查找对应的申请单底项账号
//			apprHead.setgNo(getGnoMethod(list.get(0).getBillNum  ()));
//
//			//从库存信息生成申请单行
//			apprInfoList = copyStockInfo2ApprInfo(list);

			//2024-11-19 徐峥编写
			if(list!=null && list.size() > 0){
				apprInfoList = copyStockInfo2ApprInfo2(list);
				if (apprInfoList == null){
					return "未找到对应的入库申请单信息";
				}
			}
			outApprHeadService.save(apprHead);
		}

		for (ApprInfo info : apprInfoList) {
			info.setHeadId(apprHead.getId());
			info.setIoType("2");
			apprInfoService.save(info);
		}
		return "success";
	}



	//从库存信息生成申请单行
	private List<ApprInfo> copyStockInfo2ApprInfo(List<BisOutStockInfo> list) {
		List<ApprInfo> la = new ArrayList<ApprInfo>();
		int index = 1;
		for (BisOutStockInfo info : list) {
			ApprInfo ai = new ApprInfo();
			ai.setBisInfoId(String.valueOf(info.getId()));
			
			ai.setLinkId(info.getOutLinkId());
			ai.setApprGNo(index++);
	
			ai.setgName(info.getCargoName());

			//规格
			ai.setgModel(info.getTypeSize());
		
			ai.setgQty(String.valueOf(info.getPiece()));
			ai.setUnit1(ai.getgUnit());
			ai.setQty1(ai.getgQty());

			ai.setGrossWt(String.valueOf(info.getGrossWeight()));
			ai.setCreateTime(new Date());
			la.add(ai);
		}
		return la;
	}

	//从库存信息生成申请单行
	private List<ApprInfo> copyStockInfo2ApprInfo2(List<BisOutStockInfo> list) {
		List<ApprInfo> la = new ArrayList<ApprInfo>();
		//循环获取有多少个提单
		List<String> billNumList = new ArrayList<>();
		for (BisOutStockInfo forBisOutStockInfo:list) {
			if (!billNumList.contains(forBisOutStockInfo.getBillNum())){
				billNumList.add(forBisOutStockInfo.getBillNum());
			}
		}
		for (int i = 0; i < billNumList.size(); i++) {
			List<ApprInfo> apprInfoList = getGnoMethod(billNumList.get(i));
			if (apprInfoList==null){
				return null;
			}
			ApprInfo ai = new ApprInfo();
			String bisInfoId = "";
			Integer piece = 0;
			Double grossWeight = 0.00;
			String ctnNum = "";
			for (BisOutStockInfo forBisOutStockInfoTemp:list) {
				if (billNumList.get(i).equals(forBisOutStockInfoTemp.getBillNum())){
					if ("".equals(bisInfoId)){
						ctnNum = forBisOutStockInfoTemp.getCtnNum();
					}
					bisInfoId += String.valueOf(forBisOutStockInfoTemp.getId())+",";
					piece += (forBisOutStockInfoTemp.getOutNum()==null?0:forBisOutStockInfoTemp.getOutNum());
					grossWeight += (forBisOutStockInfoTemp.getGrossWeight()==null?0.00:forBisOutStockInfoTemp.getGrossWeight());
				}
			}
			if (bisInfoId.length() > 0 && bisInfoId.contains(",")){
				bisInfoId = bisInfoId.substring(0,bisInfoId.length()-1);
			}
			ai.setBisInfoId(bisInfoId);
			ai.setLinkId(list.get(0).getOutLinkId());
			ai.setApprGNo(i+1);
			//依据提单关联入库申请单获取信息
			ai.setCodeTs(apprInfoList.get(0).getCodeTs());
			ai.setgName(apprInfoList.get(0).getgName());
			ai.setgModel(apprInfoList.get(0).getgModel());
			ai.setgNo(apprInfoList.get(0).getgNo());
			ai.setCtnNum(ctnNum);
			ai.setgQty(String.valueOf(piece));
			ai.setGrossWt(String.valueOf(grossWeight));
			ai.setUnit1(ai.getgUnit());
			ai.setQty1(ai.getgQty());
			ai.setCreateTime(new Date());
			la.add(ai);
		}
		return la;
	}

	//根据提单号查找对应的申请单底项账号
	private List<ApprInfo> getGnoMethod(String itemNum) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_itemNum", itemNum));
		filters.add(new PropertyFilter("EQS_ioType", "1"));
		List<ApprHead> result = apprHeadService.search(filters);
        if (result.size() > 0){
			List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
			filters2.add(new PropertyFilter("EQS_headId", result.get(0).getId()));
			List<ApprInfo> apprinfoList = apprInfoService.search(filters2);
            return apprinfoList;
        }
        return null;
		
	}
}
