package com.haiersoft.ccli.wms.service.passPort;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PassPortInfoDJDao;
import com.haiersoft.ccli.wms.dao.PreEntryInfoDJDao;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfoDJ;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfoDJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @ClassName: PassPortInfoDJService
 */
@Service
@Transactional(readOnly = true)
public class PassPortInfoDJService extends BaseService<BisPassPortInfoDJ, Integer> {

	@Autowired
	private PassPortInfoDJDao passPortInfoDJDao;

	@Override
    public HibernateDao<BisPassPortInfoDJ, Integer> getEntityDao() {
	    return passPortInfoDJDao;
    }
	public List<BisPassPortInfoDJ> getList(String passPortId){
		return passPortInfoDJDao.findBy("passPortId", passPortId);
	}

}
