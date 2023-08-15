package com.haiersoft.ccli.wms.service.preEntry;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PreEntryInfoDJDao;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfoDJ;
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
public class PreEntryInfoDJService extends BaseService<BisPreEntryInfoDJ, Integer> {

	@Autowired
	private PreEntryInfoDJDao preEntryInfoDJDao;

	@Override
    public HibernateDao<BisPreEntryInfoDJ, Integer> getEntityDao() {
	    return preEntryInfoDJDao;
    }
	//根据预报单ID获得预报单明细
	public List<BisPreEntryInfoDJ> getList(String forId){
		return preEntryInfoDJDao.findBy("forId", forId);
	}

	public int saveDJ(String forId,String userName,String fileName,String fileSize,byte[] fileContent,String remark){
		return preEntryInfoDJDao.saveDJ(forId,userName,fileName,fileSize,fileContent,remark);
	}
}
