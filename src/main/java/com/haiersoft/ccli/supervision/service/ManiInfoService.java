package com.haiersoft.ccli.supervision.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.supervision.dao.ManiInfoDao;
import com.haiersoft.ccli.supervision.entity.ManiInfo;

@Service
public class ManiInfoService extends BaseService<ManiInfo, String>{
	
	@Autowired
	ManiInfoDao maniInfoDao;

	@Override
	public HibernateDao<ManiInfo, String> getEntityDao() {
		// TODO Auto-generated method stub
		return maniInfoDao;
	}

	public void deleteByHeadId(String headId) {
		maniInfoDao.deleteByHeadId(headId);
	}

	//根据mainHead的ID更新Info中的ApprId
	public void updateManiFestIdbyHeadId(String maniFestId, String headId) {
		maniInfoDao.updateManiFestIdbyHeadId(maniFestId, headId);
		
	}
	
}
