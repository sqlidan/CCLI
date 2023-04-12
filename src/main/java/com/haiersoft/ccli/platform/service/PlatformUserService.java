package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.platform.dao.GroupManageDao;
import com.haiersoft.ccli.platform.dao.PlatformUserDao;
import com.haiersoft.ccli.platform.entity.GroupManage;
import com.haiersoft.ccli.platform.entity.PlatformUser;
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
public class PlatformUserService extends BaseService<PlatformUser, String> {
	
	@Autowired
	private PlatformUserDao platformUserDao  ;
	
	@Override
	public HibernateDao<PlatformUser, String> getEntityDao() {
		return platformUserDao;
	}
		

	
	
/*
	public Page<PlatformUser> seachCustomsClearanceSql(Page<PlatformUser> page,PlatformUser customsClearance){
		return platformUserDao.seachCustomsClearanceSql(page, customsClearance);
	}
*/



	//获取client(无视大小写)
	public List<Map<String,Object>> findUser(String param) {
		return platformUserDao.findUser(param);
	}



}
