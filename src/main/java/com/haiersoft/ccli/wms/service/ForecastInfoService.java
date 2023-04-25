package com.haiersoft.ccli.wms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.ForecastInfoDao;
import com.haiersoft.ccli.wms.entity.BisForecastInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

/**
 * 
 * @author pyl
 * @ClassName: ForecastInfoService
 * @Description: 入库预报单货物Service
 * @date 2016年4月15日 下午4:55:48
 */
@Service
@Transactional(readOnly = true)
public class ForecastInfoService extends BaseService<BisForecastInfo, Integer> {
	
	@Autowired
	private ForecastInfoDao forecastInfoDao;
	//@Autowired
	//private FeeCodeDao feeCodeDao;
	
	@Override
    public HibernateDao<BisForecastInfo, Integer> getEntityDao() {
	    return forecastInfoDao;
    }
	//根据入库预报单ID获得入库预报单明细
	public List<BisForecastInfo> getList(String forId){
		return forecastInfoDao.findBy("forId", forId);
	}
}
