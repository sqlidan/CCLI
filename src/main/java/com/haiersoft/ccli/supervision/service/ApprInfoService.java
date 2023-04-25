package com.haiersoft.ccli.supervision.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.supervision.dao.ApprInfoDao;
import com.haiersoft.ccli.supervision.entity.ApprInfo;

@Service
public class ApprInfoService extends BaseService<ApprInfo, String>{
	
	@Autowired
	ApprInfoDao apprInfoDao;

	@Override
	public HibernateDao<ApprInfo, String> getEntityDao() {
		// TODO Auto-generated method stub
		return apprInfoDao;
	}
		
	public void updateArrpIdbyHeadId(String apprId, String headId) {
		apprInfoDao.updateArrpIdbyHeadId(apprId, headId);
	}
	
	//根据bisInfoId查询对应的申请单信息
	public List<ApprInfo> findApprInfosByBisInfoId(String bisInfoId){
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();			
		filters.add(new PropertyFilter("EQS_bisInfoId", bisInfoId));
		List<ApprInfo> infoList = super.search(filters);
		return infoList;
	}
	
	//根据bisInfoId查询最新的对应的入库申请单信息
	public ApprInfo findApprInfosByEntBisInfoIdNew(String bisInfoId){
		
		return apprInfoDao.findByEntBisInfoIdNew(bisInfoId);
	}
	
	//根据bisInfoId查询对应的出库申请单信息
	public ApprInfo findApprInfosByOutBisInfoId(String bisInfoId){
		
		return apprInfoDao.findByOutBisInfoId(bisInfoId);
	}
	
	public void deleteByHeadId(String headId) {
		apprInfoDao.deleteByHeadId(headId);
		
	}

}
