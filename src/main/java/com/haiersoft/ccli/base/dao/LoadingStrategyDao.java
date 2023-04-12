package com.haiersoft.ccli.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BaseLoadingStrategy;
import com.haiersoft.ccli.common.persistence.HibernateDao;

@Repository
public class LoadingStrategyDao  extends HibernateDao<BaseLoadingStrategy, Integer> {
	/**
	 * 获取分拣策略列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findList(){
		StringBuffer sb=new StringBuffer("select max(b.strategyname) as name,strategycode from base_loading_strategy b group by b.strategycode");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
/*		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String,Object>> getList = new ArrayList<Map<String,Object>>();
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		SQLQuery sqlQuery2=this.getSession().createSQLQuery(sb.toString());
		List<Object[]> datas = sqlQuery.list();
		for (Object[] ob:datas){
			Map<String, Object> paramss = new HashMap<String, Object>();
			paramss.put("name", ob[0]==null?"":ob[0].toString());
			paramss.put("strategycode", ob[1]==null?"":ob[1].toString());
			getList.add(paramss);
			
		}*/
	}
	/**
	 * 根据策略编号获取策略详情
	 * @param strategyCode
	 * @return
	 */
	public List<BaseLoadingStrategy> findStrategyObjList(Integer strategyCode){
		StringBuffer sb=new StringBuffer("from BaseLoadingStrategy where strategyCode=:strategyCode");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("strategyCode", strategyCode);
		return find(sb.toString(),parme);
		
	}
	/**
	 * 根据策略编码执行删除操作
	 * @param strategyCode
	 */
	public void deleteStrategy(Integer strategyCode){
		String hql="delete BaseLoadingStrategy where strategyCode=?0 ";
		batchExecute(hql, strategyCode);
	}
	
}
