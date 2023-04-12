package com.haiersoft.ccli.base.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.ExpenseScheme;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.StringUtils;
/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseSchemeDao
 * @Description: 费用方案DAO
 * @date 2016年2月24日 下午5:26:39
 */
@Repository
public class ExpenseSchemeDao extends HibernateDao<ExpenseScheme, String>{

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getShare(String contractId,String clientId,String ifGet) {
		HashMap<String,Object> params=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select s.scheme_num,s.scheme_name,s.contract_id,s.program_type,s.customs_name,s.bis_type,s.program_state from base_expense_scheme s ");
		sb.append(" where s.is_del='0' ");
		if(!StringUtils.isNull(contractId)){
			sb.append(" and s.contract_id=:contractId ");
			params.put("contractId", contractId);
		}
		sb.append(" and s.if_get=:ifGet");
		params.put("ifGet", ifGet);
		if(!StringUtils.isNull(clientId)){
			sb.append(" and (  s.customs_id =:clientId or s.scheme_num in ( select t.scheme_num from base_expense_share t where t.client_id=:shareId ) )");
			params.put("clientId", clientId);
			params.put("shareId", clientId);
		}
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
}


