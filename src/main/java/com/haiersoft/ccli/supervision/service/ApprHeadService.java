package com.haiersoft.ccli.supervision.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.supervision.dao.ApprHeadDao;
import com.haiersoft.ccli.supervision.entity.ApprHead;

@Service
public class ApprHeadService extends BaseService<ApprHead, String>{
	
	@Autowired
	ApprHeadDao apprHeadDao;

	@Override
	public HibernateDao<ApprHead, String> getEntityDao() {
		// TODO Auto-generated method stub
		return apprHeadDao;
	}
	
	public void updateArrpIdById(String apprId, String localStatus,String id) {
		apprHeadDao.updateArrpIdbyId(apprId, localStatus,id);
	}

	public void updateByApprId(ApprHead apprHead) {
		apprHeadDao.updateByApprId(apprHead);
	}


	public void updateLocalStatusbyId(String status, String id) {
		apprHeadDao.updateLocalStatusbyId(status,id);	
		
	}
	
	public List<String> findAllUpdateApprID() {
		return apprHeadDao.findAllUpdateApprID();	
		
	}
	
	public String getMaxGno() {
		return apprHeadDao.getMaxGno();
	}

	public List<String> getAllEnterSorckId() {
		// TODO Auto-generated method stub
		return apprHeadDao.getAllEnterSorckId();
	}

	public List<String> getAllLinkIdForBuildMani() {
		// TODO Auto-generated method stub
		return apprHeadDao.getAllLinkIdForBuildMani();
	}

	public List<String> findAllBatchApplyApprID() {
		// TODO Auto-generated method stub
		return apprHeadDao.findAllBatchApplyApprID();
	}

	public void deleteById(String id) {
		apprHeadDao.deleteById(id);
		
	}

}
