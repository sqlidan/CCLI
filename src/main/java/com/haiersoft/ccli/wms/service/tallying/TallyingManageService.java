package com.haiersoft.ccli.wms.service.tallying;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.tallying.TallyingManageDao;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 理货入库
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class TallyingManageService extends BaseService<TrayInfo, Integer> {
	
	@Autowired
	private TallyingManageDao tallyingDao  ;

	@Override
	public HibernateDao<TrayInfo, Integer> getEntityDao() {
		return tallyingDao;
	}


}
