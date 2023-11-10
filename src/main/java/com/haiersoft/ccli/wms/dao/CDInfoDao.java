package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclarationInfo;
import org.springframework.stereotype.Repository;

/**
 * 
 * @ClassName: PreEntryInfoDao
 * @Description: 报关单明细DAO
 */
@Repository
public class CDInfoDao extends HibernateDao<BsCustomsDeclarationInfo, Integer> {
	
}
