package com.haiersoft.ccli.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.system.dao.OrganizationDao;
import com.haiersoft.ccli.system.entity.Organization;

/**
 * 区域service
 * @author kkomge
 * @date 2015年5月09日
 */
@Service
@Transactional(readOnly=true)
public class OrganizationService extends BaseService<Organization, Integer>{
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Override
	public HibernateDao<Organization, Integer> getEntityDao() {
		return organizationDao;
	}

}
