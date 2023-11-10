package com.haiersoft.ccli.wms.service.customsDeclaration;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.CDInfoDao;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclarationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @ClassName: CustomsDeclarationInfoService
 * @Description: 报关单货物Service
 */
@Service
@Transactional(readOnly = true)
public class CDInfoService extends BaseService<BsCustomsDeclarationInfo, Integer> {

	@Autowired
	private CDInfoDao CDInfoDao;

	@Override
    public HibernateDao<BsCustomsDeclarationInfo, Integer> getEntityDao() {
	    return CDInfoDao;
    }
	//根据报关单ID获得报关单明细
	public List<BsCustomsDeclarationInfo> getList(String forId){
		return CDInfoDao.findBy("forId", forId);
	}
}
