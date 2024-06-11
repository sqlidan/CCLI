package com.haiersoft.ccli.wms.service.customsDeclaration;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.CDDao;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclaration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: CustomsDeclarationService
 * @Description: 报关单Service
 */
@Service
@Transactional(readOnly = true)
public class CDService extends BaseService<BsCustomsDeclaration, String> {
	
	@Autowired
	private CDDao CDDao;

	@Override
	public HibernateDao<BsCustomsDeclaration, String> getEntityDao() {
		return CDDao;
	}

	//根据预报单ID获得预报单明细
	public List<BsCustomsDeclaration> getList(String forId){
		return CDDao.findBy("forId", forId);
	}

	public List<Map<String,Object>> findOneBaseBounded(String putrecSeqno){
		return CDDao.findOneBaseBounded(putrecSeqno);
	}

	public List<Map<String,Object>> getUserByName(String userName){

		return CDDao.getUserByName(userName);
	}
	public Integer getCount(String bondInvtNo){
		List<Map<String,Object>> mapList = new ArrayList<>();
		mapList = CDDao.getCount(bondInvtNo);
		if (mapList==null || mapList.size() == 0){
			return 0;
		}
		return mapList.size();
	}
}
