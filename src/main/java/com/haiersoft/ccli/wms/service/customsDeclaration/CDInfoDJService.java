package com.haiersoft.ccli.wms.service.customsDeclaration;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.CDInfoDJDao;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclarationInfoDJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @ClassName: PreEntryInfoService
 * @Description: 报关单货物Service
 */
@Service
@Transactional(readOnly = true)
public class CDInfoDJService extends BaseService<BsCustomsDeclarationInfoDJ, Integer> {

	@Autowired
	private CDInfoDJDao CDInfoDJDao;

	@Override
    public HibernateDao<BsCustomsDeclarationInfoDJ, Integer> getEntityDao() {
	    return CDInfoDJDao;
    }
	//根据预报单ID获得预报单明细
	public List<BsCustomsDeclarationInfoDJ> getList(String forId){
		return CDInfoDJDao.findBy("forId", forId);
	}

	public int saveDJ(String forId,String userName,String fileName,String fileSize,byte[] fileContent,String remark){
		return CDInfoDJDao.saveDJ(forId,userName,fileName,fileSize,fileContent,remark);
	}
}
