package com.haiersoft.ccli.wms.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.GateCarDAO;
import com.haiersoft.ccli.wms.entity.BisGateCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GateCarService extends BaseService<BisGateCar, Integer> {

    @Autowired
    private GateCarDAO gateCarDAO;

    @Override
    public HibernateDao<BisGateCar, Integer> getEntityDao() {
        return gateCarDAO;
    }

    public List<Map<String, Object>> findInCarList() {
        return gateCarDAO.findInCarList();
    }


    public BisGateCar findCarByCarNum(String carNum) {
        return gateCarDAO.findUniqueBy("carNum", carNum);
    }

	public List<BisGateCar> findBy(Map<String, Object> params) {
		return gateCarDAO.findBy(params);
	}

}
