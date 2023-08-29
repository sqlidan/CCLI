package com.haiersoft.ccli.wms.service.preEntry;

import com.haiersoft.ccli.bounded.dao.BaseBoundedDao;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PreEntryBoundedDao;
import com.haiersoft.ccli.wms.dao.PreEntryDao;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryBounded;
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
public class PreEntryBoundedService extends BaseService<BisPreEntryBounded, String> {
	
	@Autowired
	private PreEntryBoundedDao preEntryBoundedDao  ;
	@Autowired
	private PreEntryDao preEntryDao  ;
	
	@Override
	public HibernateDao<BisPreEntryBounded, String> getEntityDao() {
		return preEntryBoundedDao;
	}

	public List<BisPreEntry> getBisPreEntryList(String bondInvtNo){
		return preEntryDao.findBy("checkListNo", bondInvtNo);
	}
}
