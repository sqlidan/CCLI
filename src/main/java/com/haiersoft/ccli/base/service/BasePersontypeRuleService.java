package com.haiersoft.ccli.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.dao.BasePersontypeRuleDao;
import com.haiersoft.ccli.base.entity.BasePersontypeRule;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
@Service
public class BasePersontypeRuleService extends BaseService<BasePersontypeRule, Integer> {
	
	@Autowired
	private BasePersontypeRuleDao basePerSonTypeRuleDao;
	
	@Override
	public HibernateDao<BasePersontypeRule, Integer> getEntityDao() {
		return basePerSonTypeRuleDao;
	}
}
