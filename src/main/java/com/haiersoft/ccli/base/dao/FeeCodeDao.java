package com.haiersoft.ccli.base.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.base.entity.FeeCode;


/**
 * 费目DAO
 * @author ty
 * @date 2015年1月13日
 */
@Repository
public class FeeCodeDao extends HibernateDao<FeeCode, Integer>{
	/**
	 * 根据业务主键删除
	 * @author pyl
	 * @date 2016年2月25日
	 */  
	public void deleteCode(String code){
		
	}
	
	public Page<FeeCode> seachSql(Page<FeeCode> page,Map<String, Object> params){
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT                                                ");
		sb.append("   feecode.*,                                          ");
		sb.append("   unit.label as unitLable,                            ");
		sb.append("   term.label as termLable,                            ");
		sb.append("   currency.label as currencyLable,                    ");
		sb.append("   feetype.label as feetypeLable                       ");
		sb.append(" FROM                                                  ");
		sb.append("   BASE_EXPENSE_CATEGORY_INFO feecode                  ");
		sb.append(" LEFT JOIN (                                           ");
		sb.append("   SELECT                                              ");
		sb.append("     unit.label,                                       ");
		sb.append("     unit.value                                        ");
		sb.append("   FROM                                                ");
		sb.append("     dict unit                                         ");
		sb.append("   WHERE                                               ");
		sb.append("     unit.type = 'units'                               ");
		sb.append(" ) unit ON feecode.units = unit.value                  ");
		sb.append("                                                       ");
		sb.append(" LEFT JOIN (                                           ");
		sb.append("   SELECT                                              ");
		sb.append("     term.label,                                       ");
		sb.append("     term.value                                        ");
		sb.append("   FROM                                                ");
		sb.append("     dict term                                         ");
		sb.append("   WHERE                                               ");
		sb.append("     term.type = 'termAttribute'                       ");
		sb.append(" ) term ON feecode.term_attribute = term.value         ");
		sb.append("                                                       ");
		sb.append(" LEFT JOIN (                                           ");
		sb.append("   SELECT                                              ");
		sb.append("     currency.label,                                   ");
		sb.append("     currency.value                                    ");
		sb.append("   FROM                                                ");
		sb.append("     dict currency                                     ");
		sb.append("   WHERE                                               ");
		sb.append("     currency.type = 'currencyType'                    ");
		sb.append(" ) currency ON feecode.currency_type = currency.value  ");
		sb.append("                                                       ");
		sb.append(" LEFT JOIN (                                           ");
		sb.append("   SELECT                                              ");
		sb.append("     feetype.label,                                    ");
		sb.append("     feetype.value                                     ");
		sb.append("   FROM                                                ");
		sb.append("     dict feetype                                      ");
		sb.append("   WHERE                                               ");
		sb.append("     feetype.type = 'feeType'                          ");
		sb.append(" ) feetype ON feecode.fee_type = feetype.value         ");
		sb.append(" where 1=1                                             ");
		if(null!=params){
			if(null!=params.get("code")&&!"".equals(params.get("code"))){
				sb.append(" and feecode.CODE like '%"+params.get("code")+"%'");
			}
			if(null!=params.get("nameC")&&!"".equals(params.get("nameC"))){
				sb.append(" and feecode.NAME_C like '%"+params.get("nameC")+"%'");
			}
			if(null!=params.get("feeType")&&!"".equals(params.get("feeType"))){
				sb.append(" and feecode.FEE_TYPE="+params.get("feeType"));
			}
	     }
		return findPageSql(page, sb.toString(),null);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findFeeCode(String param) {
		StringBuffer sb=new StringBuffer("select CODE,NAME_C from BASE_EXPENSE_CATEGORY_INFO where NAME_C like :NAME");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("NAME", "%"+param+"%");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findFeeCodeByName(String name) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.* from BASE_EXPENSE_CATEGORY_INFO t where replace(t.NAME_C,'　','') = :NAME");
		HashMap<String,Object> parme = new HashMap<String,Object>();
		parme.put("NAME",name);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

}
