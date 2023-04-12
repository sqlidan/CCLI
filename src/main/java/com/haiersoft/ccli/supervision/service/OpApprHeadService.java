package com.haiersoft.ccli.supervision.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.supervision.dao.OpApprHeadDao;
import com.haiersoft.ccli.supervision.entity.OpApprHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpApprHeadService extends BaseService<OpApprHead, String>{
	
	@Autowired
	OpApprHeadDao opApprHeadDao;

	@Override
	public HibernateDao<OpApprHead, String> getEntityDao() {
		// TODO Auto-generated method stub
		return opApprHeadDao;
	}
	
	public void updateArrpIdById(String apprId, String localStatus,String id) {
		opApprHeadDao.updateArrpIdbyId(apprId, localStatus,id);
	}

	public void updateByApprId(OpApprHead apprHead) {
		opApprHeadDao.updateByApprId(apprHead);
	}


	public void updateLocalStatusbyId(String status, String id) {
		opApprHeadDao.updateLocalStatusbyId(status,id);
		
	}
	
	public List<String> findAllUpdateApprID() {
		return opApprHeadDao.findAllUpdateApprID();
		
	}
	
	public String getMaxGno() {
		return opApprHeadDao.getMaxGno();
	}

	public List<String> getAllEnterSorckId() {
		// TODO Auto-generated method stub
		return opApprHeadDao.getAllEnterSorckId();
	}

	public List<String> getAllLinkIdForBuildMani() {
		// TODO Auto-generated method stub
		return opApprHeadDao.getAllLinkIdForBuildMani();
	}

	public List<String> findAllBatchApplyApprID() {
		// TODO Auto-generated method stub
		return opApprHeadDao.findAllBatchApplyApprID();
	}

	public void deleteById(String id) {
		opApprHeadDao.deleteById(id);
		
	}

}
