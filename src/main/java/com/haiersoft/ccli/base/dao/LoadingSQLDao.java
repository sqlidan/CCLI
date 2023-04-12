package com.haiersoft.ccli.base.dao;

import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BaseLoadingSQL;
import com.haiersoft.ccli.common.persistence.HibernateDao;
@Repository
public class LoadingSQLDao  extends HibernateDao<BaseLoadingSQL, Integer> {
	 
	/**
	 * 根据策略编码执行删除操作
	 * @param strategyCode 策略编码
	 */
	public void deleteStrategy(Integer strategyCode){
		String hql="delete BaseLoadingSQL where id=?0 ";
		batchExecute(hql, strategyCode);
	}
}
