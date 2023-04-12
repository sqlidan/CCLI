package com.haiersoft.ccli.base.dao;

import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.base.entity.Warehouse;
import com.haiersoft.ccli.common.persistence.HibernateDao;

/**
 * 
 * @author Connor.M
 * @ClassName: WarehouseDao
 * @Description: 仓库DAO
 * @date 2016年3月3日 下午3:48:01
 */
@Repository
public class WarehouseDao extends HibernateDao<Warehouse, String> {
	
}
