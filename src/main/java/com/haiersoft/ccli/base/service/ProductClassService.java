package com.haiersoft.ccli.base.service;

 

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.ProductClassDao;
import com.haiersoft.ccli.base.entity.BaseProductClass;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
/**
 * 货物类型
 * @author LZG
 *
 */
@Service
public class ProductClassService extends BaseService<BaseProductClass, Integer> {
	@Autowired
	private ProductClassDao productClassDao;
	@Override
	public HibernateDao<BaseProductClass, Integer> getEntityDao() {
		return productClassDao;
	}
	
	
	public Page<BaseProductClass> seachSql(Page<BaseProductClass> page,Map<String, Object> params){
		return productClassDao.seachSql(page, params);
	}

	
	/**
	 * 保存货品小类代码
	 * @param feecode
	 */
	@Transactional(readOnly=false)
	public void save(BaseProductClass baseProductClass) {
		productClassDao.save(baseProductClass);
	}

	
	
	
	
	
	/**
	 * 获取货物类型id
	 * @param pName 货物名称
	 * @param isPrint 是否是大类
	 * @return
	 */
	public int getProductClassId(String pName,boolean isPrint){
		return productClassDao.getProductClassId(pName, isPrint);
	}
	 
	/**
	 * 获取货物类型id
	 * @param pName 货物名称
	 * @param isPrint 是否是小类
	 * @return
	 */
	public int getProductClassId2(String pName,Integer bigId){
		return productClassDao.getProductClassId2(pName,bigId);
	}
}
