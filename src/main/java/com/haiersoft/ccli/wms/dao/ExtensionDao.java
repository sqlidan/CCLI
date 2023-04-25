package com.haiersoft.ccli.wms.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.Extension;
/**
 * 
 * @author pyl
 * @ClassName: ExtensionDao
 * @Description: 展期DAO
 * @date 2016年4月15日 下午3:52:06
 */
@Repository
public class ExtensionDao extends HibernateDao<Extension, Integer>{
	@Autowired
	protected SessionFactory sessionFactory;
	
	//获取报关报检单数据
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getList(String billNum){
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select a.id as id,a.CD_NUM as cdNum,ex.warning_day as warningday,ex.out_warning_day as outWarningDay,a.bill_num as billNum,a.client_name as clientName,a.DECLARE_TIME as declareTime,a.RELEASE_TIME as releaseTime,a.extension,trunc(add_months(a.RELEASE_TIME,12) - sysdate) as days,concat('1',a.in_out_sign) as ztype ");
		sb.append(" from bis_customs_declaration a");
		sb.append(" left join base_extension ex on ex.id='1'");
		sb.append(" where ex.id='1' and a.RELEASE_TIME is not null and a.IN_OUT_SIGN='1' and ex.WARNING_DAY >= trunc(add_months(a.RELEASE_TIME,12) - sysdate) ");
		if(!billNum.equals("0")){
			sb.append(" and lower(a.bill_num) like lower(:billNum) ");
			params.put("billNum", "%"+billNum+"%");
		}
//		sb.append(" union ");
//		sb.append(" select a.id as id,ex.warning_day as warningday,ex.out_warning_day as outWarningDay,a.bill_num as billNum,a.client_name as clientName,a.DECLARE_TIME as declareTime,a.RELEASE_TIME as releaseTime,a.extension,trunc(add_months(a.RELEASE_TIME,12) - sysdate) as days,concat('2',a.in_out_sign) as ztype ");
//		sb.append(" from bis_ciq_declaration a");
//		sb.append(" left join base_extension ex on ex.id='1' ");
//		sb.append(" where ex.id='1' and a.RELEASE_TIME is not null " );
//		if(!billNum.equals("0")){
//			sb.append(" and lower(a.bill_num) like lower(:billNum) ");
//			params.put("billNum", "%"+billNum+"%");
//		}
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	
	//获取超过弹出报警时间的数据
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getWarningList(){
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select ex.out_warning_day as outWarningDay,a.bill_num as billNum, trunc(add_months(a.RELEASE_TIME,12) - sysdate) as days ");
		sb.append(" from bis_customs_declaration a");
		sb.append(" left join base_extension ex on ex.id='1'");
		sb.append(" where ex.out_warning_day >= trunc(add_months(a.RELEASE_TIME,12) - sysdate)" );
		sb.append(" union ");
		sb.append(" select  ex.out_warning_day as outWarningDay,a.bill_num as billNum,trunc(add_months(a.RELEASE_TIME,12) - sysdate) as days ");
		sb.append(" from bis_ciq_declaration a");
		sb.append(" left join base_extension ex on ex.id='1' ");
		sb.append(" where ex.out_warning_day >= trunc(add_months(a.RELEASE_TIME,12) - sysdate)" );
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
}
