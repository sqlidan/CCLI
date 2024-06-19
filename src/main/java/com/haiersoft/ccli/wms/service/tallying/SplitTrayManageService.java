package com.haiersoft.ccli.wms.service.tallying;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.tallying.SplitTrayManageDao;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 拆托
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class SplitTrayManageService extends BaseService<BisPreEntryInvtQuery, String> {
	
	@Autowired
	private SplitTrayManageDao splitTrayDao  ;

	@Override
	public HibernateDao<BisPreEntryInvtQuery, String> getEntityDao() {
		return splitTrayDao;
	}


}
