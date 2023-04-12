package com.haiersoft.ccli.base.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.dao.LoadingSQLDao;
import com.haiersoft.ccli.base.entity.BaseLoadingSQL;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class LoadingSQLService extends BaseService<BaseLoadingSQL, Integer> {
	@Autowired
	private LoadingSQLDao loadingSQLDao;
	@Override
	public HibernateDao<BaseLoadingSQL, Integer> getEntityDao() {
		return loadingSQLDao;
	}
	/**
	 * 根据策略编码执行删除操作
	 * @param strategyCode 策略编码
	 */
	public void deleteStrategy(Integer strategyCode){
		loadingSQLDao.deleteStrategy(strategyCode);
	}
}
