package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.base.dao.PlatformDao;
import com.haiersoft.ccli.base.entity.BasePlatform;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.platform.dao.BasePlatformDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BasePlatformService extends BaseService<BasePlatform, Integer> {
	@Autowired
	private BasePlatformDao platformDao;
	
	@Override
	public HibernateDao<BasePlatform, Integer> getEntityDao() {
		return platformDao;
	}


	public List<BasePlatform> getActivePlatforms(Page<BasePlatform> page, BasePlatform basePlatform) {
		return platformDao.getActivePlatforms(page,basePlatform);
	}

	public BasePlatform findByPlatformNo(Integer platformId){
		return platformDao.findByPlatformNo(platformId);
	}


	public List<Map<String, Object>> getCurrentInfo(){
		return platformDao.getCurrentInfo();
	}
}
