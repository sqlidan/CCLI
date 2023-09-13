package com.haiersoft.ccli.wms.service.PreEntryInvtQuery;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.CustomsClearanceDao;
import com.haiersoft.ccli.wms.dao.CustomsClearanceListDao;
import com.haiersoft.ccli.wms.entity.BisCustomsClearance;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisCustomsClearanceList;
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
public class CustomsClearanceListService extends BaseService<BisCustomsClearanceList, String> {
	
	@Autowired
	private CustomsClearanceListDao customsClearanceListDao;
	
	@Override
	public HibernateDao<BisCustomsClearanceList, String> getEntityDao() {
		return customsClearanceListDao;
	}

}
