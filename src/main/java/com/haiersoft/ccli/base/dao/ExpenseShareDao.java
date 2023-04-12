package com.haiersoft.ccli.base.dao;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BaseExpenseShare;
import com.haiersoft.ccli.common.persistence.HibernateDao;
@Repository
public class ExpenseShareDao  extends HibernateDao<BaseExpenseShare, Integer> {

	//根据费用方案号删除
	public void delSchemeNum(String schemeNum) {
		if(null!=schemeNum && !"".equals(schemeNum)){
			String hql="delete BaseExpenseShare a where a.schemeNum=?0";
			batchExecute(hql, schemeNum);
		}
	}

	//费用方案审核
	public void okpass(String id) {
		if(null!=id && !"".equals(id)){
			String sql="update base_expense_share set program_state = '1' where scheme_num =?0";
			SQLQuery sqlQuery=createSQLQuery(sql, id);
			sqlQuery.executeUpdate();
		}
	}
	
	//费用方案取消审核
	public void nopass(String id) {
		if(null!=id && !"".equals(id)){
		   String sql="update base_expense_share set program_state = '0' where scheme_num =?0";
		   SQLQuery sqlQuery=createSQLQuery(sql, id);
		   sqlQuery.executeUpdate();
		}
	}
	
}
