package com.haiersoft.ccli.base.dao;

import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.base.entity.ExpenseContractInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;

/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseContractInfoDao
 * @Description: 合同费目名称DAO
 * @date 2016年2月25日 下午4:57:34
 */
@Repository
public class ExpenseContractInfoDao extends HibernateDao<ExpenseContractInfo, String>{
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 查询SEQ
	 * @date 2016年2月25日 下午5:05:38 
	 * @return
	 * @throws
	 */
	public int getContractInfoId() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SEQ_CONTRACT_INFO.nextval AS NUM FROM DUAL ");
	    int maxId = (Integer)(this.getSession().createSQLQuery(sb.toString()).addScalar("NUM", StandardBasicTypes.INTEGER)).uniqueResult();
		return maxId;
	}
	
	
}
