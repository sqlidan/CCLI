package com.haiersoft.ccli.wms.service.scm;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.ScmDictDao;
import com.haiersoft.ccli.wms.web.scm.ScmDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 
 */
@Service
@Transactional(readOnly = true)
public class ScmDictService extends BaseService<ScmDict, String> {
	
	@Autowired
	private ScmDictDao scmDictDao;

	@Override
	public HibernateDao<ScmDict, String> getEntityDao() {
		return scmDictDao;
	}

	public List<Map<String,Object>> queryInboundOrderAddList(String startTime, String endTime){
		return scmDictDao.queryInboundOrderAddList(startTime,endTime);
	}

	public List<Map<String,Object>> queryInboundAddList(String startTime, String endTime){
		return scmDictDao.queryInboundAddList(startTime,endTime);
	}

	public List<Map<String,Object>> queryOutboundAddList(String startTime, String endTime){
		return scmDictDao.queryOutboundAddList(startTime,endTime);
	}
}
