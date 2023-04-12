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
import com.haiersoft.ccli.supervision.service.ApprInfoService;
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
public class EnterManiController extends BaseController{
	@Autowired
	ManiHeadService maniHeadService;
	
	@Autowired
	ManiInfoService maniInfoService;
	
	@Autowired
	ApprInfoService apprInfoService;
	
    @Autowired
    EnterStockInfoService enterStockInfoService;

	
	@RequestMapping(value="/in/save", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String saveManiHead(ManiHead maniHead) {
		
		//检查货物明细行是否生成有对应的申请单
		//没有申请单的记录不能生成核放单
		String infoIds = maniHead.getInfoIds().replaceAll("[\\[\\]]", "");
		List<String> list = Arrays.asList(infoIds.split(","));
		for(String id :list) {
			List<PropertyFilter> filters = new ArrayList<PropertyFilter>();			
			filters.add(new PropertyFilter("EQS_bisInfoId", id.trim()));
			List<ApprInfo> infoList = apprInfoService.search(filters);
			if (infoList.size() == 0 || infoList == null) {
				return "没有对应的申请单";
			}
			else if (StringUtils.isBlank(infoList.get(0).getApprId()) || infoList.get(0).getApprId() == null) {
				return "没有生成ApprId";
			}
			maniHead.setApprId(infoList.get(0).getApprId());
		}				

		inDoing(maniHead);

		return "success";
	}
	
	@Transactional
	private void inDoing(ManiHead maniHead) {
		Date currDate = new Date();
		// 核放单头
		maniHead.setIeFlag("I");
		maniHead.setEmptyFlag("N");
		maniHead.setIeFlagNote("入库核放");
		maniHead.setLocalStatus("1");
		maniHead.setManiConfirmStatus("N");
		maniHead.setCreateTime(currDate);
		maniHeadService.save(maniHead);
		
		//根据选中的货物信息生成核放单行
		String infoIds = maniHead.getInfoIds().replaceAll("[\\[\\]]", "");
		List<String> list = Arrays.asList(infoIds.split(","));
		
		
		
		for(String str :list) {
			ManiInfo maniInfo = new ManiInfo();
			
			//根据bisInfoId查询对应的入库申请单
			ApprInfo info = apprInfoService.findApprInfosByEntBisInfoIdNew(str.trim());
			
			//填充数据
			maniInfo.setApprId(info.getApprId());
			maniInfo.setApprGNo(info.getApprGNo());
			maniInfo.setgNo(info.getApprGNo());
			maniInfo.setGName(info.getgName());
			maniInfo.setGModel(info.getgModel());
			maniInfo.setGUnit(info.getgUnit());
			maniInfo.setGQty(info.getgQty());
			maniInfo.setGrossWt(info.getGrossWt());	
			maniInfo.setHeadId(maniHead.getId());
			maniInfo.setCreateTime(currDate);
			maniInfo.setBisInfoId(str.trim());
			
			BisEnterStockInfo bisInfo = enterStockInfoService.get(Integer.valueOf(str.trim()));
			maniInfo.setBisInfoLinkId(bisInfo.getLinkId());
			maniInfo.setBisItemNum(bisInfo.getItemNum());
			maniInfo.setBisSku(bisInfo.getSku());
			maniInfoService.save(maniInfo);			
		}

		//20220527 取消入库时生成的空车核放
//		// 空车核放单头
//		ManiHead mh = new ManiHead();
//		mh.setIeFlag("E");
//		mh.setEmptyFlag("Y");
//		mh.setIeFlagNote("入库空车核放");
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
//		maniInfo.setCreateTime(currDate);
//
//		//前端传来的箱号
//		maniInfo.setContaId(maniHead.getContaId());
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
	
	
}
