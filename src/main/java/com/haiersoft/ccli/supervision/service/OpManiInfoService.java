package com.haiersoft.ccli.supervision.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.supervision.dao.ManiInfoDao;
import com.haiersoft.ccli.supervision.dao.OpManiInfoDao;
import com.haiersoft.ccli.supervision.entity.ManiInfo;
import com.haiersoft.ccli.supervision.entity.OpManiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpManiInfoService extends BaseService<OpManiInfo, String>{
	
	@Autowired
	OpManiInfoDao opManiInfoDao;

	@Override
	public HibernateDao<OpManiInfo, String> getEntityDao() {
		// TODO Auto-generated method stub
		return opManiInfoDao;
	}

	public void deleteByHeadId(String headId) {
		opManiInfoDao.deleteByHeadId(headId);
	}

	//根据mainHead的ID更新Info中的ApprId
	public void updateManiFestIdbyHeadId(String maniFestId, String headId) {
		opManiInfoDao.updateManiFestIdbyHeadId(maniFestId, headId);
		
	}

	public String sumGrossWtByHeadId(String headId) {
		return opManiInfoDao.sumGrossWtByHeadId(headId);
	}
}
