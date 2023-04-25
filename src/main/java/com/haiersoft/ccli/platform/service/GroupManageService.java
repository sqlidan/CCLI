package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.platform.dao.GroupManageDao;
import com.haiersoft.ccli.platform.entity.GroupManage;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.CustomsClearanceDao;
import com.haiersoft.ccli.wms.entity.BisCustomsClearance;
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
public class GroupManageService extends BaseService<GroupManage, Integer> {
	
	@Autowired
	private GroupManageDao  groupManageDao  ;
	
	@Override
	public HibernateDao<GroupManage, Integer> getEntityDao() {
		return groupManageDao;
	}
		

	
	
	public Page<GroupManage> seachCustomsClearanceSql(Page<GroupManage> page,GroupManage customsClearance){
		return groupManageDao.seachCustomsClearanceSql(page, customsClearance);
	}
	




	
}
