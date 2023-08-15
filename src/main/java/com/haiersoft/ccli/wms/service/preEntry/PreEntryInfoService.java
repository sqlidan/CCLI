package com.haiersoft.ccli.wms.service.preEntry;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PreEntryInfoDao;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @ClassName: PreEntryInfoService
 * @Description: 预报单货物Service
 */
@Service
@Transactional(readOnly = true)
public class PreEntryInfoService extends BaseService<BisPreEntryInfo, Integer> {

	@Autowired
	private PreEntryInfoDao preEntryInfoDao;

	@Override
    public HibernateDao<BisPreEntryInfo, Integer> getEntityDao() {
	    return preEntryInfoDao;
    }
	//根据预报单ID获得预报单明细
	public List<BisPreEntryInfo> getList(String forId){
		return preEntryInfoDao.findBy("forId", forId);
	}
}
