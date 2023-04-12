package com.haiersoft.ccli.wms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.OutForecastInfoDao;
import com.haiersoft.ccli.wms.entity.BisOutForecastInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

/**
 * 
 * @author pyl
 * @Description: 出库预报单明细Service
 * @date 2016年2月25日 下午4:55:48
 */
@Service
@Transactional(readOnly = true)
public class OutForecastInfoService extends BaseService<BisOutForecastInfo, Integer> {
	
	@Autowired
	private OutForecastInfoDao outForecastInfoDao;
	//@Autowired
	//private FeeCodeDao feeCodeDao;
	
	@Override
    public HibernateDao<BisOutForecastInfo, Integer> getEntityDao() {
	    return outForecastInfoDao;
    }
	//根据入库预报单ID获得入库预报单明细
	public List<BisOutForecastInfo> getList(String forId){
		return outForecastInfoDao.findBy("forId", forId);
	}
}
