package com.haiersoft.ccli.wms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.GateCarWrongInfoDao;
import com.haiersoft.ccli.wms.entity.BisGateCarWrongInfo;

@Service
public class GateCarWrongInfoService extends BaseService<BisGateCarWrongInfo, Integer> {

    @Autowired
    private GateCarWrongInfoDao gateCarWrongInfoDao;
	
	@Override
	public HibernateDao<BisGateCarWrongInfo, Integer> getEntityDao() {
		return gateCarWrongInfoDao;
	}
	
    public List<Map<String, Object>> findInCarList() {
        return gateCarWrongInfoDao.findWrongCarInfoList();
    }


    public BisGateCarWrongInfo findCarByCarNum(String carNum) {
        return gateCarWrongInfoDao.findUniqueBy("carNum", carNum);
    }

	public List<BisGateCarWrongInfo> findBy(Map<String, Object> params) {
		return gateCarWrongInfoDao.findBy(params);
	}
}
