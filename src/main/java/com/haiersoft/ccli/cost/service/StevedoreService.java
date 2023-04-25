package com.haiersoft.ccli.cost.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.security.Digests;
import com.haiersoft.ccli.common.utils.security.Encodes;
import com.haiersoft.ccli.cost.dao.StevedoreDao;
import com.haiersoft.ccli.cost.entity.Stevedore;
import com.haiersoft.ccli.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Stevedore
 * @author test
 * @date 2022
 */
@Service
@Transactional(readOnly = true)
public class StevedoreService extends BaseService<Stevedore, Integer> {

	@Autowired
	private StevedoreDao stevedoreDao;

	@Override
	public HibernateDao<Stevedore, Integer> getEntityDao() {
		return stevedoreDao;
	}

	@Transactional(readOnly=false)
	public void save(Stevedore stevedore) {
		stevedore.setCreateDate(DateUtils.getSysTimestamp());
		stevedoreDao.save(stevedore);
	}

	@Transactional(readOnly=false)
	public void delete(Integer id){
		stevedoreDao.delete(id);
	}

	public Stevedore getStevedore(String name) {
		return stevedoreDao.findUniqueBy("name", name);
	}

//	public List<Map<String,Object>> findUserByName(String param) {
//		List<Map<String,Object>> userList=stevedoreDao.findUserByName(param);
//		return userList;
//	}

	public Page<Stevedore> pageStevedore(Page<Stevedore> page,List<PropertyFilter> filters) {
		return stevedoreDao.pageStevedore(page,filters);
	}

}
