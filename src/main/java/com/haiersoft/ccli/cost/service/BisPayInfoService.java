package com.haiersoft.ccli.cost.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.BisPayInfoDao;
import com.haiersoft.ccli.cost.entity.BisPayInfo;
 
@Service
public class BisPayInfoService extends BaseService<BisPayInfo, Integer> {
	@Autowired
	private BisPayInfoDao bisPayInfoDao;
	
	@Override
	public HibernateDao<BisPayInfo, Integer> getEntityDao() {
		return bisPayInfoDao;
}

	public List<BisPayInfo> getList(String payId) {
		return bisPayInfoDao.findByPayId(payId);
	}

	public List<BisPayInfo> findby(Map<String, Object> params) {
		return bisPayInfoDao.findBy(params);
	}
	
	
	
}
