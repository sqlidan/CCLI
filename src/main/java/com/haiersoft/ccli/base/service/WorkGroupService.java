package com.haiersoft.ccli.base.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.WorkGroupDao;
import com.haiersoft.ccli.base.entity.BaseWorkGroup;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
@Service
public class WorkGroupService extends BaseService<BaseWorkGroup, Integer> {
	@Autowired
	private WorkGroupDao workGroupDao;
	
	@Override
	public HibernateDao<BaseWorkGroup, Integer> getEntityDao() {
		return workGroupDao;
	}

	public Page<BaseWorkGroup> searchKG(Page<BaseWorkGroup> page, BaseWorkGroup workGroup) {
		return workGroupDao.searchKG(page, workGroup);
	}

	public List<BaseWorkGroup> findBy(Map<String,Object> params) {
		return workGroupDao.findBy(params);
	}

	public List<Map<String, Object>> findKgByName(String name) {
		return workGroupDao.findKgByName(name);
	}

	public List<BaseWorkGroup> findByF(List<PropertyFilter> filters) {
		return workGroupDao.find(filters);
	}

	public Page<BaseWorkGroup> searchOther(Page<BaseWorkGroup> page,BaseWorkGroup obj) {
		return workGroupDao.searchOther(page, obj);
	}
	
	public Page<BaseWorkGroup> searchAllOther(Page<BaseWorkGroup> page,BaseWorkGroup obj) {
		return workGroupDao.searchAllOther(page, obj);
	}

	public List<Map<String, Object>> findUsersByName(String name) {
		return workGroupDao.findUsersByName(name);
	}
	
}
