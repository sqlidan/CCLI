package com.haiersoft.ccli.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.base.entity.BaseProductClass;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
/**
 * 货物类型
 * @author LZG
 *
 */
@Repository
public class ProductClassDao  extends HibernateDao<BaseProductClass, Integer> {
	/**
	 * 获取货物类型id
	 * @param pName 货物名称
	 * @param isPrint 是否是大类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getProductClassId(String pName,boolean isPrint){
		if(pName!=null && !"".equals(pName)){
			HashMap<String,Object> parme=new HashMap<String,Object>();
			StringBuffer sql=new StringBuffer("SELECT ID FROM BASE_PRODUCT_CALSS P WHERE P.PNAME=:pname");
			parme.put("pname", pName);
			if(true==isPrint){
				sql.append(" AND P.PRINTID=0");
			}else{
				sql.append(" AND P.PRINTID>0");
			}
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
			List<Map<String,Object>> getList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if(getList!=null && getList.size()>0){
				Map<String,Object> getMap=getList.get(0);
				if(getMap!=null){
					return Integer.valueOf(getMap.get("ID").toString());
				}
			}
		}
		return 0;
	}
	
	/**
	 * 获取货物类型id
	 * @param pName 货物名称
	 * @param isPrint 是否是小类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getProductClassId2(String pName,Integer bigId){
		if(pName!=null && !"".equals(pName)){
			HashMap<String,Object> parme=new HashMap<String,Object>();
			StringBuffer sql=new StringBuffer("SELECT ID FROM BASE_PRODUCT_CALSS P WHERE P.PNAME=:pname and P.PRINTID=:bigid ");
			parme.put("pname", pName);
			parme.put("bigid", bigId);
			sql.append(" AND P.PRINTID >0");
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
			List<Map<String,Object>> getList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if(getList!=null && getList.size()>0){
				Map<String,Object> getMap=getList.get(0);
				if(getMap!=null){
					return Integer.valueOf(getMap.get("ID").toString());
				}
			}
		}
		return 0;
	}

	/**
	 * 获取货品小类信息
	 * @param page 页面
	 * @param params 参数
	 * @return
	 */
	public Page<BaseProductClass> seachSql(Page<BaseProductClass> page, Map<String, Object> params) {
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT                                                ");
		sb.append("   ID ,                                          ");
		sb.append("   PNAME ,                            ");
		sb.append("   PRINTID                            ");
		sb.append("   FROM                     ");
		sb.append("   BASE_PRODUCT_CALSS                     ");
		sb.append("   WHERE   1=1                    ");
		if(null!=params) {
			if(null!=params.get("ID")&&!"".equals(params.get("ID"))){
				sb.append(" and ID like '%"+params.get("ID")+"%'");
			}
			if(null!=params.get("PRINTID")&&!"".equals(params.get("PRINTID"))){
				sb.append(" and PRINTID = "+params.get("PRINTID")+"");
			}
		sb.append("  ORDER BY  ID ASC                    ");
		}
		return findPageSql(page, sb.toString(),null);
	}
}
