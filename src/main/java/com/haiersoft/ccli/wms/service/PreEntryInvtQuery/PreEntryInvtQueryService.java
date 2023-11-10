package com.haiersoft.ccli.wms.service.PreEntryInvtQuery;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PreEntryBoundedDao;
import com.haiersoft.ccli.wms.dao.PreEntryDao;
import com.haiersoft.ccli.wms.dao.PreEntryInvtQueryDao;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryBounded;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class PreEntryInvtQueryService extends BaseService<BisPreEntryInvtQuery, String> {
	
	@Autowired
	private PreEntryInvtQueryDao preEntryInvtQueryDao  ;

	@Override
	public HibernateDao<BisPreEntryInvtQuery, String> getEntityDao() {
		return preEntryInvtQueryDao;
	}

	public List<BisPreEntryInvtQuery> getList(String bondInvtNo){
		return preEntryInvtQueryDao.findBy("bondInvtNo", bondInvtNo);
	}

	public List<BisPreEntryInvtQuery> getListBySynchronization(){
		return preEntryInvtQueryDao.findBy("synchronization", "0");
	}

	public List<BisPreEntryInvtQuery> getListByCreatePreEntry(){
		return preEntryInvtQueryDao.findBy("createPreEntry", "0");
	}
	public List<BisPreEntryInvtQuery> getListByCreateClearance(){
		return preEntryInvtQueryDao.findBy("createClearance", "0");
	}
	public List<BisPreEntryInvtQuery> getListByCreateBGD(){
		return preEntryInvtQueryDao.findBy("createBgd", "0");
	}
}
