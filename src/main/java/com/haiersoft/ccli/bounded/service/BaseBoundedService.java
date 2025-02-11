package com.haiersoft.ccli.bounded.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.bounded.dao.BaseBoundedDao;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.report.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class BaseBoundedService extends BaseService<BaseBounded, String> {
	
	@Autowired
	private BaseBoundedDao baseBoundedDao  ;
	
	@Override
	public HibernateDao<BaseBounded, String> getEntityDao() {
		return baseBoundedDao;
	}


	public Page<BaseBounded> searchBaseBounded(Page<BaseBounded> page, BaseBounded baseBounded){
		return baseBoundedDao.searchBaseBounded(page, baseBounded);
	}
	
/*
	public Page<PlatformUser> seachCustomsClearanceSql(Page<PlatformUser> page,PlatformUser customsClearance){
		return platformUserDao.seachCustomsClearanceSql(page, customsClearance);
	}
*/

	public List<Map<String, Object>> getLoactionsByBillNum(BaseBounded baseBounded){
		return baseBoundedDao.getLoactionsByBillNum(baseBounded);
	}




}
