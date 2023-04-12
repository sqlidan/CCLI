package com.haiersoft.ccli.base.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseSchemeInfoDao
 * @Description: 费用方案明细DAO
 * @date 2016年2月28日 下午3:28:52
 */
@Repository
public class ExpenseSchemeInfoDao extends HibernateDao<ExpenseSchemeInfo, String>{

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getFeeCodeByBill(String billNum) {
		StringBuffer sb=new StringBuffer("select b.id,b.fee_code,b.fee_name from base_expense_scheme_info b ");
		sb.append("left join bis_enter_stock c on c.item_num = :billNum and c.fee_id = b.scheme_num");
		sb.append(" where c.del_flag = '0'");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("billNum", billNum);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getFeeCodeByNum(String schemeNum) {
		StringBuffer sb=new StringBuffer("select b.id,b.fee_code,b.fee_name from base_expense_scheme_info b ");
		sb.append(" where b.scheme_num=:schemeNum and FEE_NAME like '%短倒费%' ");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("schemeNum", schemeNum);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	//更新垫付状态
	public void updatePay(String a) {
		String sql="update base_expense_scheme_info info set if_pay = '1' where info.id in ( " + a + ")";
		SQLQuery sqlQuery=createSQLQuery(sql);
		sqlQuery.executeUpdate();
	}

	public void updateNoPay(String a) {
		String sql="update base_expense_scheme_info info set if_pay = '0' where info.id in ( " + a + ")";
		SQLQuery sqlQuery=createSQLQuery(sql);
		sqlQuery.executeUpdate();
	}
}
