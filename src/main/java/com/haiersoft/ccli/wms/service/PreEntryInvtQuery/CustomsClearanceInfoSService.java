package com.haiersoft.ccli.wms.service.PreEntryInvtQuery;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.CustomsClearanceInfoDao;
import com.haiersoft.ccli.wms.dao.CustomsClearanceInfoSDao;
import com.haiersoft.ccli.wms.entity.BisCustomsClearanceInfo;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisCustomsClearanceInfoS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 * @ClassName: 
 * @Description: 
 * @date 
 */
@Service
@Transactional(readOnly = true)
public class CustomsClearanceInfoSService extends BaseService<BisCustomsClearanceInfoS, Integer> {
	
	@Autowired
	private CustomsClearanceInfoSDao customsClearanceInfoSDao;
	
	
	@Override
    public HibernateDao<BisCustomsClearanceInfoS, Integer> getEntityDao() {
	    return customsClearanceInfoSDao;
    }

}
