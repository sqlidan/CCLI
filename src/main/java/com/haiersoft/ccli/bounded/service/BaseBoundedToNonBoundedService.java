package com.haiersoft.ccli.bounded.service;

import com.haiersoft.ccli.bounded.dao.BaseBoundedDao;
import com.haiersoft.ccli.bounded.dao.BaseBoundedToNonBoundedDao;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.bounded.entity.BaseBoundedToNonBounded;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
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
public class BaseBoundedToNonBoundedService extends BaseService<BaseBoundedToNonBounded, String> {
	
	@Autowired
	private BaseBoundedToNonBoundedDao baseBoundedToNonBoundedDao  ;
	
	@Override
	public HibernateDao<BaseBoundedToNonBounded, String> getEntityDao() {
		return baseBoundedToNonBoundedDao;
	}

}
