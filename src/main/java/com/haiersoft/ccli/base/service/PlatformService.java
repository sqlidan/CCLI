package com.haiersoft.ccli.base.service;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.PlatformDao;
import com.haiersoft.ccli.base.entity.BasePlatform;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

import java.util.List;
import java.util.Map;

@Service
public class PlatformService extends BaseService<BasePlatform, Integer> {
	@Autowired
	private PlatformDao platformDao;
	
	@Override
	public HibernateDao<BasePlatform, Integer> getEntityDao() {
		return platformDao;
	}

	public List<Map<String, Object>> getActivePlatform(){
		return platformDao.getActivePlatform();
	}

	public List<BasePlatform> getActivePlatforms(Page<BasePlatform> page, BasePlatform basePlatform) {
		return platformDao.getActivePlatforms(page,basePlatform);
	}
}
