package com.haiersoft.ccli.wms.service;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.CustomsClearanceInfoDao;
import com.haiersoft.ccli.wms.entity.BisCustomsClearanceInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

/**
 * 
 * @author 
 * @ClassName: 
 * @Description: 
 * @date 
 */
@Service
@Transactional(readOnly = true)
public class CustomsClearanceInfoService extends BaseService<BisCustomsClearanceInfo, Integer> {
	
	@Autowired
	private CustomsClearanceInfoDao customsClearanceInfoDao;
	
	
	@Override
    public HibernateDao<BisCustomsClearanceInfo, Integer> getEntityDao() {
	    return customsClearanceInfoDao;
    }

	//根据入库报关单ID获得明细列表
	public List<BisCustomsClearanceInfo> getByCusId(String id) {
		return customsClearanceInfoDao.findBy("cusId", id);
	}
	
	//获取
	public List<Map<String, Object>> getByAllWeight(String customsDeclarationNumber) {
		
		return customsClearanceInfoDao.getByAllWeight(customsDeclarationNumber);
	}

	public List<Map<String, Object>> getRecordByDNumber(String customsDeclarationNumber) {
		
		return customsClearanceInfoDao.getRecordByDNumber(customsDeclarationNumber);
	}
	
}
