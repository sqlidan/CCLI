package com.haiersoft.ccli.base.dao;

import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.base.entity.BaseTaxRate;


/**
 * 汇率DAO
 * @author ty
 * @date 2015年1月13日
 */
@Repository
public class TaxRateDao extends HibernateDao<BaseTaxRate, Integer>{
}
