package com.haiersoft.ccli.supervision.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.supervision.dao.ApprInfoDao;
import com.haiersoft.ccli.supervision.dao.OpApprInfoDao;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.entity.OpApprInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpApprInfoService extends BaseService<OpApprInfo, String>{
	
	@Autowired
	OpApprInfoDao opApprInfoDao;

	@Override
	public HibernateDao<OpApprInfo, String> getEntityDao() {
		// TODO Auto-generated method stub
		return opApprInfoDao;
	}

	public Integer getApprGnoByHeadId(String headId) {
		return opApprInfoDao.getApprGnoByHeadId(headId);
	}

	public String sumQtyByHeadId(String headId) {
		return opApprInfoDao.sumQtyByHeadId(headId);
	}

	public String sumWeightByHeadId(String headId) {
		return opApprInfoDao.sumWeightByHeadId(headId);
	}

	public List<OpApprInfo> getListByHeadId(String headId) {

		return opApprInfoDao.listByHeadId(headId);
	}

	public void deleteByHeadId(String headId) {
		opApprInfoDao.deleteByHeadId(headId);
	}
}
