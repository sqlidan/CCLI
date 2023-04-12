package com.haiersoft.ccli.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.dao.WarehouseDao;
import com.haiersoft.ccli.base.entity.Warehouse;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;


/**
 * 
 * @author Connor.M
 * @ClassName: WarehouseService
 * @Description: 仓库Service
 * @date 2016年3月3日 下午3:46:04
 */
@Service
public class WarehouseService extends BaseService<Warehouse, String> {

	@Autowired
	private  WarehouseDao warehouseDao;
	
	@Override
    public HibernateDao<Warehouse, String> getEntityDao() {
	    return warehouseDao;
    }
	
	
}
