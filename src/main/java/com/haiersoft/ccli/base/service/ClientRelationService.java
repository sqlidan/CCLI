package com.haiersoft.ccli.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.dao.ClientRelationDao;
import com.haiersoft.ccli.base.entity.BaseClientRelation;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class ClientRelationService extends BaseService<BaseClientRelation, Integer> {
	@Autowired
	private ClientRelationDao clientRelationDao;
	
	@Override
	public HibernateDao<BaseClientRelation, Integer> getEntityDao() {
		return clientRelationDao;
	}
	/**
	 * //根据父客户id获取对象集合
	 * @param clientId 父客户id
	 * @return
	 */
	public List<BaseClientRelation> getList(String clientId) {
		return clientRelationDao.findBy("parentId", clientId);
	}
	/**
	 * 根据客户id获取唯一对象
	 * @param clientId 客户id
	 * @return
	 */
	public BaseClientRelation getObjIfClientId(String clientId){
		return clientRelationDao.findUniqueBy("clientId", clientId);
	}
	
}
