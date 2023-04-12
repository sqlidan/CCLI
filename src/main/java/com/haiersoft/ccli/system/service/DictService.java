package com.haiersoft.ccli.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.system.dao.DictDao;
import com.haiersoft.ccli.system.entity.Dict;

/**
 * 字典service
 * @author ty
 * @date 2015年1月13日
 */
@Service
@Transactional(readOnly=true)
public class DictService extends BaseService<Dict, Integer> {
	
	@Autowired
	private DictDao dictDao;
	
	
    public Dict findDict(String type,String value){
    	return dictDao.findDict(type, value);
    }

	/*
	 * @author PYL 
	 * 根据type获取
	 */
	public List<Dict> getReceptacle(String type){
		return dictDao.findBy("type", type);
	}
	
	@Override
	public HibernateDao<Dict, Integer> getEntityDao() {
		return dictDao;
	}
}
