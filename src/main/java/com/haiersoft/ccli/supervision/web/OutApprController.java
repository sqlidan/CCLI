package com.haiersoft.ccli.supervision.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

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
			//根据提单号查找对应的申请单底项账号
			apprHead.setItemNum(list.get(0).getBillNum());
			apprHead.setgNo(getGnoMethod(list.get(0).getBillNum()));

			//从库存信息生成申请单行
			apprInfoList = copyStockInfo2ApprInfo(list);
			outApprHeadService.save(apprHead);
		}
		

		for (ApprInfo info : apprInfoList) {
			
			info.setgNo(apprHead.getgNo());
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

	//根据提单号查找对应的申请单底项账号
	private Integer getGnoMethod(String itemNum) {
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_itemNum", itemNum));
		List<ApprHead> result = apprHeadService.search(filters);
        if (result.size() == 1){
            return result.get(0).getgNo();
        }
        return null;
		
	}
}
