package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import org.springframework.stereotype.Repository;

/**
 *
 * @ClassName: PassPortInfoDao
 */
@Repository
public class PassPortInfoDao extends HibernateDao<BisPassPortInfo, Integer> {
	
}
