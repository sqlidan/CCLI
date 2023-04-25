package com.haiersoft.ccli.supervision.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.entity.ManiHead;
import com.haiersoft.ccli.supervision.entity.ManiInfo;
import com.haiersoft.ccli.supervision.service.ApprHeadService;
import com.haiersoft.ccli.supervision.service.ApprInfoService;
import com.haiersoft.ccli.supervision.service.ManiHeadService;
import com.haiersoft.ccli.supervision.service.ManiInfoService;

@Controller
@RequestMapping("supervision/appr")
public class VirtualManiController {
	
	@Autowired
	private ApprHeadService apprHeadService;

	@Autowired
	private ApprInfoService apprInfoService;
	
	@Autowired
	private ManiHeadService maniHeadService;
	
	@Autowired
	private ManiInfoService maniInfoService;
	
	//生成虚拟核放单
	@Transactional
	@RequestMapping(value="/virtualmani/{id}")
	@ResponseBody
	public String virtualMani(@PathVariable("id") String id) {
		
		ApprHead apprHead = apprHeadService.find("id",id);
		
		//先查询申请单号是否已存在对应的核放单
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_ApprId", apprHead.getApprId()));
		List<ManiInfo> maniinfoList = maniInfoService.search(filters);

		if(maniinfoList.size() !=0) {
			return "已存在对应的核放单，不能生成虚拟核放单";
		}

		ManiHead maniHead = new ManiHead();
		maniHead.setIeFlag(getIeFlag(apprHead.getIoType()));
		maniHead.setGrossWt(apprHead.getGrossWt());		
		
		//生成虚拟车号
		maniHead.setVehicleId("V"+(int)((Math.random()*9+1)*100000));
		maniHead.setVehicleWeight("20");
		maniHead.setLocalStatus("1");
		maniHead.setIeFlagNote("虚拟核放单");
		maniHead.setEmptyFlag("N");
		maniHead.setCreateTime(new Date());
		
		maniHeadService.save(maniHead);

		List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
		filters2.add(new PropertyFilter("EQS_headId", id));
		List<ApprInfo> apprInfoList = apprInfoService.search(filters2);
		
		for(ApprInfo apprInfo:apprInfoList) {
			ManiInfo maniInfo= new ManiInfo();
			maniInfo.setHeadId(maniHead.getId());
			maniInfo.setgNo(apprInfo.getgNo());
			maniInfo.setApprId(apprInfo.getApprId());
			maniInfo.setApprGNo(apprInfo.getApprGNo());
			maniInfo.setCodeTs(apprInfo.getCodeTs());
			maniInfo.setGName(apprInfo.getgName());
			maniInfo.setGModel(apprInfo.getgModel());
			maniInfo.setGUnit(apprInfo.getgUnit());
			maniInfo.setGQty(apprInfo.getgQty());
			maniInfo.setGrossWt(apprInfo.getGrossWt());
			
			maniInfoService.save(maniInfo);


		}
		return "success";		
	}
	
	private String getIeFlag(String IoType) {
		if(IoType.equals("1"))
			return "I";
		else if(IoType.equals("2"))
			return "E";
		else
			return "0";
		
	}
}
