package com.haiersoft.ccli.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.base.dao.TaxRateDao;
import com.haiersoft.ccli.base.entity.BaseTaxRate;

/**
 * 汇率service
 * @author ty
 * @date 2015年1月13日
 */
@Service
@Transactional(readOnly = true)
public class TaxRateService extends BaseService<BaseTaxRate, Integer> {
	
	@Autowired
	private TaxRateDao taxRateDao;

	@Override
	public HibernateDao<BaseTaxRate, Integer> getEntityDao() {
		return taxRateDao;
	}

	
	/**
	 * 保存汇率
	 * @param feecode
	 */
	@Transactional(readOnly=false)
	public void save(BaseTaxRate baseTaxRate) {
		taxRateDao.save(baseTaxRate);
	}

	/**
	 * 删除用户
	 * @param id
	 */
	@Transactional(readOnly=false)
	public void delete(Integer id){
		taxRateDao.delete(id);
	}
	
	/**
	 * 按币种查询
	 * @param feecode
	 * @return 费目代码
	 */
	public List<BaseTaxRate> getCurrency(String currency) {
		Map<String,Object> parme=new HashMap<String,Object>();
		String HQL = "from BaseTaxRate where currencyType = :currencyType";
		parme.put("currencyType", currency);
		return taxRateDao.find(HQL,parme);
	}
	
	/**
	 * 获取所有汇率缓存map
	 * @return
	 */
	public Map<String,Double> getTaxMap() {
		Map<String,Double> taxMap=new HashMap<String,Double>();
		String HQL = "from BaseTaxRate ";
		List<BaseTaxRate> getList= taxRateDao.find(HQL,new HashMap<String,Object>());
		if(getList!=null && getList.size()>0){
			for(BaseTaxRate getObj:getList){
				taxMap.put(getObj.getCurrencyType(), getObj.getExchangeRate());
			}
		}
		return taxMap;
	}
	/**
	 * 按币种查询(更方便的方法，上面的方法回头优化)
	 * @param feecode
	 * @return 费目代码
	 */
	public BaseTaxRate getTaxByC(String currency){
		return taxRateDao.findUniqueBy("currencyType", currency);
	}
	
}
