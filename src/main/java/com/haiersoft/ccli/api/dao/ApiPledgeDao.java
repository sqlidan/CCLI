/**
 * 
 */
package com.haiersoft.ccli.api.dao;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;

/**
 * @author Administrator
 *
 */
@Repository
public class ApiPledgeDao extends HibernateDao<ApiPledge, String>{
//	//静态库存查询
//	@SuppressWarnings("unchecked")
//	public List<TrayInfo> queryCheck(Integer id) {
//		StringBuffer buffer =new StringBuffer();
//		buffer.append("SELECT ");
//		buffer.append("* ");
//		buffer.append("FROM ");
//		buffer.append("BIS_TRAY_INFO bis ");
//		buffer.append("WHERE ");
//		buffer.append("bis.id = "+id);
//		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString()).addEntity(TrayInfo.class);
//		List<TrayInfo>list = query.list();
//		return list;
//	}
	/**
	 * 获取信息
	 * @param page 页面
	 * @param params 参数
	 * @return
	 */
	public Page<ApiPledge> seachStaticApiPledgeSql(Page<ApiPledge> page, Map<String, Object> params) {
		/*
		 * StringBuffer sb=new StringBuffer();
		 * sb.append(" SELECT                                                ");
		 * sb.append("   ID ,                                          ");
		 * sb.append("   PNAME ,                            ");
		 * sb.append("   PRINTID                            ");
		 * sb.append("   FROM                     ");
		 * sb.append("   BASE_PRODUCT_CALSS                     ");
		 * sb.append("   WHERE   1=1                    "); if(null!=params) {
		 * if(null!=params.get("ID")&&!"".equals(params.get("ID"))){
		 * sb.append(" and ID like '%"+params.get("ID")+"%'"); }
		 * sb.append("  ORDER BY  ID ASC                    "); }
		 */
		StringBuffer buffer=new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("api.*, ");
		buffer.append("tray.STOCK_NAME ");
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append("LEFT JOIN BIS_TRAY_INFO tray ON api.TRAYINFO_ID = tray.ID ");
		buffer.append("WHERE 1=1 ");
		buffer.append("AND api.state in(0,1,4,5)  ");
		if(params.get("STATUS") != null && !"".equals(params.get("STATUS"))) {
			buffer.append(" AND api.CONFIRM_STATUS = '"+params.get("STATUS")+"'");
		}
		if(params.get("STATE") != null && !"".equals(params.get("STATE"))) {
			buffer.append(" AND api.STATE = '"+params.get("STATE")+"'");
		}
		if(params.get("ACCOUNT_ID") != null && !"".equals(params.get("ACCOUNT_ID"))) {
			buffer.append(" AND api.ACCOUNT_ID = '"+params.get("ACCOUNT_ID")+"'");
		}
		
		if(params.get("CUSTOMER_NAME") != null && !"".equals(params.get("CUSTOMER_NAME"))) {
			buffer.append(" AND api.CUSTOMER_NAME like '%"+params.get("CUSTOMER_NAME")+"%'");
		}
		if(params.get("BILL_NUM") != null && !"".equals(params.get("BILL_NUM"))) {
			buffer.append(" AND api.BILL_NUM = '"+params.get("BILL_NUM")+"'");
		}
		if(params.get("CTN_NUM") != null && !"".equals(params.get("CTN_NUM"))) {
			buffer.append(" AND api.CTN_NUM = '"+params.get("CTN_NUM")+"'");
		}
		if(params.get("PNAME") != null && !"".equals(params.get("PNAME"))) {
			buffer.append(" AND api.PNAME like '%"+params.get("PNAME")+"%'");
		}
		
		buffer.append(" ORDER BY CREATE_DATE DESC ");
		return findPageSql(page, buffer.toString(),null);
	}
	
	
	/**
	 * 获取信息
	 * @param page 页面
	 * @param params 参数
	 * @return
	 */
	public Page<ApiPledge> seachActiveApiPledgeSql(Page<ApiPledge> page, Map<String, Object> params) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("api.*, ");
		buffer.append("client.CLIENT_NAME ");
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append("LEFT JOIN BASE_CLIENT_INFO client ON api.CUSTOMER_ID = client.IDS  ");
		buffer.append("WHERE 1=1 ");
		buffer.append("AND api.state in(2,3)  ");
		if(params.get("STATUS") != null && !"".equals(params.get("STATUS"))) {
			buffer.append(" AND api.CONFIRM_STATUS =  '"+params.get("STATUS")+"'");
		}
		if(params.get("STATE") != null && !"".equals(params.get("STATE"))) {
			buffer.append(" AND api.STATE = '"+params.get("STATE")+"'");
		}
		if(params.get("ACCOUNT_ID") != null && !"".equals(params.get("ACCOUNT_ID"))) {
			buffer.append(" AND api.ACCOUNT_ID = '"+params.get("ACCOUNT_ID")+"' ");
		}
		
		
		if(params.get("CUSTOMER_NAME") != null && !"".equals(params.get("CUSTOMER_NAME"))) {
			buffer.append(" AND api.CUSTOMER_NAME like '%"+params.get("CUSTOMER_NAME")+"%' ");
		}
		if(params.get("PNAME") != null && !"".equals(params.get("PNAME"))) {
			buffer.append(" AND api.PNAME like '%"+params.get("PNAME")+"%' ");
		}
		
		
		buffer.append(" ORDER BY CREATE_DATE DESC ");
		return findPageSql(page, buffer.toString(),null);
	}
	
	//动态库存查询
	public HashMap<String, Object> activeQuery(String id,Integer itemClass) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("sum(NOW_PIECE), ");
		buffer.append("sum(GROSS_WEIGHT) ");
		buffer.append("FROM ");
		buffer.append("BIS_TRAY_INFO bis ");
		buffer.append("WHERE bis.IF_TRANSFER = 0 ");//去除静态质押的库存
		buffer.append("AND bis.STOCK_IN in ( "+id+") ");
		buffer.append(" AND");
		buffer.append(" bis.SKU_ID IN ");
		buffer.append(" ( SELECT sku.SKU_ID FROM BASE_SKU_BASE_INFO sku WHERE sku.CLASS_TYPE = "+itemClass+" ) ");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		Object[] aa = (Object[]) query.uniqueResult();
		HashMap<String, Object>result = new HashMap<String, Object>();
		if(aa[0]!=null||aa[1]!=null) {
//			result.put("saveNum", new BigDecimal(0));
//			result.put("saveWeight", new BigDecimal(0));
			result.put("saveNum", aa[0].toString());
			result.put("saveWeight", aa[1].toString());
		}
		return result;  
	}
	
	//根据唯一质押编号查询质押总数量(动态)
	public HashMap<String, Object> activeSumQueryBytrendId(String relatedTrendId) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("api.PLEDGE_NUMBER, ");//最低质押数量
		buffer.append("api.PLEDGE_WEIGHT ");//最低质押重量
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append(" WHERE ");
		buffer.append(" api.CONFIRM_STATUS = 1 ");
		buffer.append(" and api.STATE = 2 ");
		buffer.append(" and api.TREND_ID = '"+relatedTrendId+"'");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		Object[] aa = (Object[]) query.uniqueResult();
		HashMap<String, Object>result = new HashMap<String, Object>();
		if(aa!=null) {
			result.put("saveNum", aa[0].toString());
			result.put("saveWeight", aa[1].toString());
		}else {
			result.put("saveNum",  0);
			result.put("saveWeight", 0);
		}
		return result;  
	}
	//根据唯一质押编号查询解押总数量(动态)
	@SuppressWarnings({ "unchecked", "unused" })
	public HashMap<String, Object> activeSumReleaseQueryBytrendId(String relatedTrendId) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("api.PLEDGE_NUMBER, ");//最低质押数量
		buffer.append("api.PLEDGE_WEIGHT ");//最低质押重量
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append(" WHERE ");
		buffer.append(" api.CONFIRM_STATUS = 1");
		buffer.append(" and api.RELATED_TREND_ID = '"+relatedTrendId+"'");
		buffer.append(" ORDER BY api.COMFIRM_DATE desc");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		List<Object> aa = query.list();
		HashMap<String, Object>result = new HashMap<String, Object>();
		if(aa.size()>0) {
			Object[] bb=(Object[]) aa.get(0);
				result.put("releaseNum", bb[0].toString());
				result.put("releaseWeight", bb[1].toString());
		}
		
	
	
		return result;  
	}
	
	//根据货主账号查询ID
	@SuppressWarnings({ "unchecked" })
	public List<BaseClientInfo> QueryIdsByCustomerNumber(String customerNumber) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("*");
		buffer.append("FROM ");
		buffer.append("BASE_CLIENT_INFO client  ");
		buffer.append("WHERE ");
		buffer.append("client.CLIENT_CODE = '"+customerNumber+"'");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString()).addEntity(BaseClientInfo.class);;
		List<BaseClientInfo>list = query.list();
		return list;  
	}
	
	//根据sku查询货种小类
	@SuppressWarnings({ "unchecked" })
	public List<BaseSkuBaseInfo> QueryCodeBySku(String sku) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("*");
		buffer.append("FROM ");
		buffer.append("BASE_SKU_BASE_INFO sku  ");
		buffer.append("WHERE ");
		buffer.append("sku.SKU_ID = '"+sku+"'");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString()).addEntity(BaseSkuBaseInfo.class);;
		List<BaseSkuBaseInfo>list = query.list();
		return list;  
	}

	//根据在库主键ID查询客户
	public HashMap<String, String> QueryCustomerNumberByID(Integer id) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT client.CLIENT_CODE,client.IDS FROM BASE_CLIENT_INFO client ");
		buffer.append("WHERE client.IDS IN (SELECT STOCK_IN FROM BIS_TRAY_INFO WHERE id = "+id+" )");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		Object[] aa = (Object[]) query.uniqueResult();
		HashMap<String, String>result = new HashMap<String, String>();
		if(aa!=null) {
			result.put("code", aa[0].toString());
			result.put("ids", aa[1].toString());
		}
		return result;  
	}
	
	
	
//	//根据唯一质押编号查询质押总数量(静态)
//	public HashMap<String, BigDecimal> pledgeSaveSumQueryByTrendId(Integer trayInfoId,String relatedTrendId) {
//		StringBuffer buffer =new StringBuffer();
//		buffer.append("SELECT ");
//		buffer.append("PLEDGE_NUMBER, ");
//		buffer.append("PLEDGE_WEIGHT ");
//		buffer.append("FROM ");
//		buffer.append("API_PLEDGE api ");
//		buffer.append("WHERE ");
//		buffer.append("api.TRAYINFO_ID = "+trayInfoId);
//		buffer.append(" AND api.TREND_ID = '"+relatedTrendId+"'");
//		buffer.append(" and api.STATE = 1");
//		buffer.append(" and api.CONFIRM_STATUS = 1");
//		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
//		Object[] aa = (Object[]) query.uniqueResult();
//		HashMap<String, BigDecimal>result = new HashMap<String, BigDecimal>();
//		if(aa==null||aa[0]==null||aa[1]==null) {
//			result.put("saveNum", new BigDecimal(0));
//			result.put("saveWeight", new BigDecimal(0));
//		}else {
//			result.put("saveNum", new BigDecimal(aa[0].toString()));
//			result.put("saveWeight", new BigDecimal(aa[1].toString()));
//		}
//		return result;  
//	}
	

	
	//根据唯一质押编号查询解押总数量(静态)
	@SuppressWarnings("unchecked")
	public HashMap<String, BigDecimal> staticReleaseQueryBytrendId(String relatedTrendId) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("api.PLEDGE_NUMBER, ");//最低质押数量
		buffer.append("api.PLEDGE_WEIGHT ");//最低质押重量
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append(" WHERE ");
		buffer.append(" api.CONFIRM_STATUS = 1");
		buffer.append(" and api.RELATED_TREND_ID = '"+relatedTrendId+"'");
		buffer.append(" ORDER BY api.COMFIRM_DATE desc");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		List<Object> aa = query.list();
		HashMap<String, BigDecimal>result = new HashMap<String, BigDecimal>();
		if(aa.size()>0) {
			Object[] bb=(Object[]) aa.get(0);
			result.put("releaseNum", new BigDecimal(bb[0].toString()));
			result.put("releaseWeight", new BigDecimal(bb[1].toString()));
		}
		
		return result;  
	}
	

	public ApiPledge findBeforeStaticReleaseBytrendId(String relatedTrendId) {
		
		String hql="from ApiPledge where confirmStatus = 1 and relatedTrendId=:relatedTrendId ORDER BY comfirmDate desc";
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("relatedTrendId", relatedTrendId);

		return this.findUnique(hql,parme);
	}
	
	//质押总数量查询(静态)
	public HashMap<String, BigDecimal> pledgeSaveSumQuery(Integer trayInfoId) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("sum(PLEDGE_NUMBER), ");
		buffer.append("sum(PLEDGE_WEIGHT) ");
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append("WHERE ");
		buffer.append("api.TRAYINFO_ID = "+trayInfoId);
		buffer.append(" and api.STATE = 1");
		buffer.append(" and api.CONFIRM_STATUS = 1");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		Object[] aa = (Object[]) query.uniqueResult();
		HashMap<String, BigDecimal>result = new HashMap<String, BigDecimal>();
		if(aa[0]==null||aa[1]==null) {
			result.put("saveNum", new BigDecimal(0));
			result.put("saveWeight", new BigDecimal(0));
		}else {
			result.put("saveNum", new BigDecimal(aa[0].toString()));
			result.put("saveWeight", new BigDecimal(aa[1].toString()));
		}
		return result;  
	}
	//解押总数量查询(静态)
	public HashMap<String, BigDecimal> pledgeReleaseSumQuery(Integer trayInfoId,String relatedTrendId) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("sum( sumnum ), ");
		buffer.append("sum(sumweight) ");
		buffer.append("FROM ");
		buffer.append("( ");
		buffer.append("SELECT ");
		buffer.append("t.RELATED_TREND_ID, ");
		buffer.append("	min( t.PLEDGE_NUMBER ) AS sumnum,");
		buffer.append(" min( t.PLEDGE_WEIGHT ) AS sumweight ");
		buffer.append(" FROM");
		buffer.append(" API_PLEDGE t ");
		buffer.append(" WHERE ");
		buffer.append(" t.STATE = 0  ");
		buffer.append(" AND t.CONFIRM_STATUS = 1 ");
		buffer.append(" AND t.TRAYINFO_ID =  "+trayInfoId+"");	
		buffer.append(" and t.RELATED_TREND_ID ='"+relatedTrendId+"' ");	
		buffer.append(" GROUP BY  ");	
		buffer.append(" t.RELATED_TREND_ID");	
		buffer.append(" )");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		Object[] aa = (Object[]) query.uniqueResult();
		HashMap<String, BigDecimal>result = new HashMap<String, BigDecimal>();
		if(aa[0]!=null||aa[1]!=null) {
			result.put("releaseNumber", new BigDecimal(aa[0].toString()));
			result.put("releaseWeight", new BigDecimal(aa[1].toString()));
		}
		
		return result;  
	}
	
	//质押总数量查询(动态)
		public HashMap<String, Object> activeSaveSumQuery(String customerNumber,Integer itemClass) {
			StringBuffer buffer =new StringBuffer();
			buffer.append("SELECT ");
			buffer.append("sum(PLEDGE_NUMBER), ");
			buffer.append("sum(PLEDGE_WEIGHT) ");
			buffer.append("FROM ");
			buffer.append("API_PLEDGE api ");
			buffer.append("WHERE ");
			buffer.append("api.CUSTOMER_ID = '"+customerNumber+"'");
			buffer.append(" and api.STATE = 2");
			buffer.append(" and api.CONFIRM_STATUS = 1");
			buffer.append(" and api.ITEM_CLASS = "+itemClass);
			SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
			Object[] aa = (Object[]) query.uniqueResult();
			HashMap<String, Object>result = new HashMap<String, Object>();
			if(aa[0]==null||aa[1]==null) {
				result.put("alreadyNum", "0");
				result.put("alreadyWeight", "0.0");
			}else {
				result.put("alreadyNum", aa[0].toString());
				result.put("alreadyWeight", aa[1].toString());
			}
			return result;  
		}
		
		//解押总数量查询(动态)
		public HashMap<String, Object> activeReleaseSumQuery(String customerNumber,Integer itemClass) {
			StringBuffer buffer =new StringBuffer();
			buffer.append("SELECT ");
			buffer.append("sum( sumnum ), ");
			buffer.append("sum(sumweight) ");
			buffer.append("FROM ");
			buffer.append("( ");
			buffer.append("SELECT ");
			buffer.append("t.RELATED_TREND_ID, ");
			buffer.append("	min( t.PLEDGE_NUMBER ) AS sumnum,");
			buffer.append(" min( t.PLEDGE_WEIGHT ) AS sumweight ");
			buffer.append(" FROM");
			buffer.append(" API_PLEDGE t ");
			buffer.append(" WHERE ");
			buffer.append(" t.STATE = 3  ");
			buffer.append(" AND t.CONFIRM_STATUS = 1 ");
			buffer.append(" AND t.CUSTOMER_ID =  '"+customerNumber+"'");	
			buffer.append(" AND t.ITEM_CLASS =  '"+itemClass+"'");	
			buffer.append(" GROUP BY  ");	
			buffer.append(" t.RELATED_TREND_ID");	
			buffer.append(" )");	
			SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
			Object[] aa = (Object[]) query.uniqueResult();
			HashMap<String, Object>result = new HashMap<String, Object>();
			if(aa[0]!=null||aa[1]!=null) {
				result.put("releaseNum", aa[0].toString());
				result.put("releaseWeight", aa[1].toString());
			}
			return result;  
		}
		
	
	/*
	 * 静态质押监管查询
	 * @author:
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getStaticApiPledge(String status,String state,String accountId){
		StringBuffer buffer=new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("api.*, ");
		buffer.append("tray.STOCK_NAME ");
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append("LEFT JOIN BIS_TRAY_INFO tray ON api.TRAYINFO_ID = tray.ID ");
		buffer.append("WHERE 1=1 ");
		buffer.append("AND api.state in(0,1,4,5)  ");
		if(status != null && !"".equals(status)) {
			buffer.append(" AND api.CONFIRM_STATUS = "+status);
		}
		if(state != null && !"".equals(state)) {
			buffer.append(" AND api.STATE = "+state);
		}
		if(accountId != null && !"".equals(accountId)) {
			buffer.append(" AND api.ACCOUNT_ID = "+accountId);
		}
		buffer.append(" ORDER BY CREATE_DATE DESC ");
		SQLQuery sqlQuery=createSQLQuery(buffer.toString());
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}	
	
	/*
	 * 动态质押监管查询
	 * @author:PYL
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getActiveApiPledge(String status,String state,String accountId){
		StringBuffer buffer=new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("api.*, ");
		buffer.append("client.CLIENT_NAME ");
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append("LEFT JOIN BASE_CLIENT_INFO client ON api.CUSTOMER_ID = client.IDS  ");
		buffer.append("WHERE 1=1 ");
		buffer.append("AND api.state in(2,3)  ");
		if(status != null && !"".equals(status)) {
			buffer.append(" AND api.CONFIRM_STATUS = "+status);
		}
		if(state != null && !"".equals(state)) {
			buffer.append(" AND api.STATE = "+state);
		}
		if(accountId != null && !"".equals(accountId)) {
			buffer.append(" AND api.ACCOUNT_ID = "+accountId);
		}
		buffer.append(" ORDER BY CREATE_DATE DESC ");
		SQLQuery sqlQuery=createSQLQuery(buffer.toString());
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}	
	
	//静态质押情况查询（根据ID查询该条库存是否存在质押情况）
//	@SuppressWarnings("unchecked")
//	public List<ApiPledge> staticPledgeCheck(Integer trayInfoId) {
//		StringBuffer buffer =new StringBuffer();
//		buffer.append("SELECT ");
//		buffer.append("* ");
//		buffer.append("FROM ");
//		buffer.append("API_PLEDGE api ");
//		buffer.append("WHERE ");
//		buffer.append("AND api.TRAYINFO_ID = "+trayInfoId);
//		buffer.append("AND api.STATE = 1");
//		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString()).addEntity(ApiPledge.class);
//		List<ApiPledge>list = query.list();
//		return list;
//	}
	
	
	//根据在库主键ID查询所属客户下所有动态质押的数量
//	public HashMap<String, BigDecimal> QuerySumCustomerById(String trayInfoId) {
//		StringBuffer buffer =new StringBuffer();
//		buffer.append("SELECT ");
//		buffer.append("sum( api.PLEDGE_NUMBER ), ");
//		buffer.append("sum( api.PLEDGE_WEIGHT ) ");
//		buffer.append("FROM ");
//		buffer.append("API_PLEDGE api   ");
//		buffer.append("WHERE ");
//		buffer.append("api.CUSTOMER_NUMBER = ( ");
//		buffer.append("SELECT client.CLIENT_CODE FROM BASE_CLIENT_INFO client ");
//		buffer.append("WHERE client.IDS IN (SELECT STOCK_IN FROM BIS_TRAY_INFO WHERE id = 9999999 )) ");
//		buffer.append(" AND api.STATE = 2 ");
//		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
//		Object[] aa = (Object[]) query.uniqueResult();
//		HashMap<String, BigDecimal>result = new HashMap<String, BigDecimal>();
//		if(aa[0]==null||aa[1]==null) {
//			result.put("saveNumber", new BigDecimal(0));
//			result.put("saveWeight", new BigDecimal(0));
//		}else {
//			result.put("saveNumber", new BigDecimal(aa[0].toString()));
//			result.put("saveWeight", new BigDecimal(aa[1].toString()));
//		}
//		return result;  
//	}
//	//根据在库主键ID查询所属客户下所有动态解押的数量
//	public HashMap<String, BigDecimal> QueryRelatedCustomerById(String trayInfoId) {
//		StringBuffer buffer =new StringBuffer();
//		buffer.append("SELECT ");
//		buffer.append("sum( api.PLEDGE_NUMBER ), ");
//		buffer.append("sum( api.PLEDGE_WEIGHT ) ");
//		buffer.append("FROM ");
//		buffer.append("API_PLEDGE api ");
//		buffer.append("WHERE ");
//		buffer.append("api.CUSTOMER_ID IN( ");
//		buffer.append("SELECT client.CLIENT_CODE FROM BASE_CLIENT_INFO client ");
//		buffer.append("WHERE client.IDS IN (SELECT STOCK_IN FROM BIS_TRAY_INFO WHERE id = 9999999 )) ");
//		buffer.append("AND api.STATE = 4 ");
//		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
//		Object[] aa = (Object[]) query.uniqueResult();
//		HashMap<String, BigDecimal>result = new HashMap<String, BigDecimal>();
//		if(aa[0]==null||aa[1]==null) {
//			result.put("releaseNumber", new BigDecimal(0));
//			result.put("releaseWeight", new BigDecimal(0));
//		}else {
//			result.put("releaseNumber", new BigDecimal(aa[0].toString()));
//			result.put("releaseWeight", new BigDecimal(aa[1].toString()));
//		}
//		return result;  
//	}
	
	//静态查询质押编号
	@SuppressWarnings("unchecked")
	public List<ApiPledge> checkTrendId(String trendId) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("* ");
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append("WHERE ");
		buffer.append(" api.TREND_ID = '"+trendId+"'");
		buffer.append(" and api.STATE in(0,1)");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString()).addEntity(ApiPledge.class);
		List<ApiPledge>list = query.list();
		return list;
	}
	
	//动态查询质押编号
		@SuppressWarnings("unchecked")
		public List<ApiPledge> checkTrendIdActive(String trendId) {
			StringBuffer buffer =new StringBuffer();
			buffer.append("SELECT ");
			buffer.append("* ");
			buffer.append("FROM ");
			buffer.append("API_PLEDGE api ");
			buffer.append("WHERE ");
			buffer.append(" api.TREND_ID = '"+trendId+"'");
			buffer.append(" and api.STATE in(2,3)");
			SQLQuery query =  this.getSession().createSQLQuery(buffer.toString()).addEntity(ApiPledge.class);
			List<ApiPledge>list = query.list();
			return list;
		}
	
	//查询最新一条记录（动态）
	@SuppressWarnings("unchecked")
	public List<ApiPledge>activeNowQuery(String customerNum) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("* ");
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append(" WHERE ");
		buffer.append(" api.CONFIRM_STATUS = 1");
		buffer.append(" and api.CUSTOMER_ID = '"+customerNum+"'");
		buffer.append(" ORDER BY api.COMFIRM_DATE desc");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString()).addEntity(ApiPledge.class);
		List<ApiPledge>list = query.list();
		return list;  
	}
	
	//查询最新一条记录（静态）
	@SuppressWarnings("unchecked")
	public List<ApiPledge>staticNowQuery(Integer trayInfoId) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("* ");
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append(" WHERE ");
		buffer.append(" api.CONFIRM_STATUS = 1");
		buffer.append(" and api.TRAYINFO_ID = "+trayInfoId+"");
		buffer.append(" ORDER BY api.COMFIRM_DATE desc");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString()).addEntity(ApiPledge.class);
		List<ApiPledge>list = query.list();
		return list;  
	}
	
	//静态质押编号查询
	@SuppressWarnings("unchecked")
	public List<String> queryTrendId(Integer trayInfoId,String relatedTrendId) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("api.TREND_ID ");
		buffer.append("FROM ");
		buffer.append("API_PLEDGE api ");
		buffer.append("WHERE ");
		buffer.append(" api.TRAYINFO_ID = "+trayInfoId );
		buffer.append(" and api.STATE = 1");
		buffer.append(" and api.TREND_ID != '"+relatedTrendId+"'");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		List<String> list =  query.list();
		return list;
	}

	//根据客户ID 和货品小类查询relateTrendId 动态质押
	public List<String> findRelatedTrendIds(Integer customerId, Integer itemClass) {
		
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT DISTINCT(p.RELATED_TREND_ID) FROM API_PLEDGE p where ");
		buffer.append("p.CUSTOMER_ID = "+customerId);
		buffer.append(" and p.ITEM_CLASS = "+itemClass);
		buffer.append(" and p.CONFIRM_STATUS = 1 and STATE in (2,3) and p.RELATED_TREND_ID is not null");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		List<String> list =  query.list();
		return list;
	}

	//根据relatedTrendId查询此组质押/解押记录 按日期排序
	public List<ApiPledge> findOrderedListByRelatedTrendId(String relatedTrendId) {
		
		StringBuffer buffer =new StringBuffer();
		buffer.append("select ");
		buffer.append("a.id,a.ACCOUNT_ID accountId,a.TRAYINFO_ID trayInfoId, ");
		buffer.append("a.PLEDGE_NUMBER pledgeNumber,a.PLEDGE_WEIGHT pledgeWeight,a.STATE state, ");
		buffer.append("a.ITEM_CLASS itemClass,a.create_date createDate,a.CUSTOMER_CODE customerCode, ");
		buffer.append("a.CUSTOMER_ID customerId,a.CONFIRM_STATUS confirmStatus,a.COMFIRM_DATE comfirmDate, ");
		buffer.append("a.TREND_ID trendId,a.RELATED_TREND_ID relatedTrendId,a.BILL_NUM billNum, ");
		buffer.append("a.CUSTOMER_NAME CustomerName,a.SKU_ID sku,a.CTN_NUM ctnNum,a.PNAME pName,a.ENTER_STATE enterState, ");
		buffer.append("a.WAREHOUSE wareHouse,a.WAREHOUSE_ID wareHouseId,a.BUSINESS_ID businessId ");
		buffer.append("from API_PLEDGE a where ");
		buffer.append("RELATED_TREND_ID = '"+relatedTrendId +"'");
		buffer.append(" and CONFIRM_STATUS = 1");
		buffer.append(" and STATE in (2,3)");
		buffer.append(" order by CREATE_DATE desc");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		query.addScalar("accountId");
		query.addScalar("trayInfoId",StandardBasicTypes.INTEGER);
		query.addScalar("pledgeNumber",StandardBasicTypes.INTEGER);
		query.addScalar("pledgeWeight",StandardBasicTypes.DOUBLE);
		query.addScalar("state",StandardBasicTypes.INTEGER);
		query.addScalar("itemClass",StandardBasicTypes.INTEGER);
		query.addScalar("customerCode");
		query.addScalar("customerId",StandardBasicTypes.INTEGER);
		query.addScalar("confirmStatus",StandardBasicTypes.INTEGER);
		query.addScalar("comfirmDate");
		query.addScalar("trendId");
		query.addScalar("relatedTrendId");
		query.addScalar("billNum");
		query.addScalar("CustomerName");
		query.addScalar("sku");
		query.addScalar("ctnNum");
		query.addScalar("pName");
		query.addScalar("enterState");
		query.addScalar("wareHouse");
		query.addScalar("wareHouseId");
		query.addScalar("businessId");
		query.setResultTransformer(Transformers.aliasToBean(ApiPledge.class));
		List<ApiPledge> list =  query.list();
		return list;
	}
	
	
	//库存查询
	public HashMap<String, Object> trayQuery(Integer customerId,Integer itemClass) {
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("nvl(sum(NOW_PIECE),0), ");
		buffer.append("nvl(sum(GROSS_WEIGHT),0) ");
		buffer.append("FROM ");
		buffer.append("BIS_TRAY_INFO bis ");
		buffer.append("WHERE ");
		buffer.append(" bis.STOCK_IN in ( '"+customerId+"') ");
		buffer.append(" AND");
		buffer.append(" bis.SKU_ID IN ");
		buffer.append(" ( SELECT sku.SKU_ID FROM BASE_SKU_BASE_INFO sku WHERE sku.CLASS_TYPE = "+itemClass+" ) ");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		Object[] aa = (Object[]) query.uniqueResult();
		HashMap<String, Object>result = new HashMap<String, Object>();
		if(aa[0]!=null||aa[1]!=null) {
			result.put("sumNum", aa[0].toString());
			result.put("sumWeight", aa[1].toString());
		}
		return result;  
	}
	
	//根据客户ID和小类 静态质押数量查询
	public HashMap<String, Object> countStaticPledgedNum(Integer customerId, Integer itemClass){
		StringBuffer buffer =new StringBuffer();
		buffer.append(" SELECT ");
		buffer.append(" sum( PLEDGE_NUMBER ) num, ");
		buffer.append(" sum( PLEDGE_WEIGHT ) weight  ");
		buffer.append(" FROM ");
		buffer.append(" ( SELECT rn,TREND_ID,CUSTOMER_CODE,ITEM_CLASS,PNAME,PLEDGE_NUMBER, ");
		buffer.append(" PLEDGE_WEIGHT,CUSTOMER_NAME,RELATED_TREND_ID,COMFIRM_DATE,CONFIRM_STATUS,state ");
		buffer.append(" FROM ");
		buffer.append(" (SELECT ROW_NUMBER ( ) OVER ( PARTITION BY RELATED_TREND_ID ORDER BY COMFIRM_DATE DESC ) rn, ");
		buffer.append(" TREND_ID, ");
		buffer.append(" CUSTOMER_CODE, ");
		buffer.append(" ITEM_CLASS, ");
		buffer.append(" PNAME, ");
		buffer.append(" PLEDGE_NUMBER, ");
		buffer.append(" PLEDGE_WEIGHT, ");
		buffer.append(" CUSTOMER_NAME, ");
		buffer.append(" RELATED_TREND_ID, ");
		buffer.append(" COMFIRM_DATE, ");
		buffer.append(" CONFIRM_STATUS, ");
		buffer.append(" state ");
		buffer.append(" FROM API_PLEDGE");
		buffer.append(" WHERE ");
		buffer.append(" STATE in(0,1) ");
		buffer.append(" AND CONFIRM_STATUS = 1  ");
		buffer.append(" AND CUSTOMER_ID = '"+ customerId+"'");
		buffer.append(" AND ITEM_CLASS = '"+itemClass+"'");
		buffer.append(" ) WHERE rn = 1) ");
		
//		buffer.append("select nvl(pledged.num - released.num,0),nvl(pledged.weight- released.weight,0) from  ");
//		buffer.append("(select sum(t.PLEDGE_NUMBER) num,sum(t.PLEDGE_WEIGHT) weight from API_PLEDGE t where t.STATE = 0 and CONFIRM_STATUS = 1 ");
//		buffer.append("and  CUSTOMER_ID = "+customerId);
//		buffer.append(" and ITEM_CLASS = "+itemClass);
//		buffer.append(" ) released,");
//		buffer.append("(select sum(t.PLEDGE_NUMBER) num,sum(t.PLEDGE_WEIGHT) weight from API_PLEDGE t where t.STATE = 1 and CONFIRM_STATUS = 1 ");
//		buffer.append("and  CUSTOMER_ID = "+customerId);
//		buffer.append(" and ITEM_CLASS = "+itemClass);
//		buffer.append(" ) pledged");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		List list = query.list();
		HashMap<String, Object>result = new HashMap<String, Object>();
		if(list.size()>0) {
			Object[] aa = (Object[]) list.get(0);		
			if(aa[0]!=null||aa[1]!=null) {
				result.put("sumNum", aa[0].toString());
				result.put("sumWeight", aa[1].toString());
			}else {
				result.put("sumNum", 0);
				result.put("sumWeight", 0);
			}
		}else {
			result.put("sumNum", 0);
			result.put("sumWeight", 0);
		}
		
		return result;  
	}
	
	
	//静态质押 根据trendId查询当前可解押数量 即已质押数量 - 已解押数量
	public HashMap<String, Object> countStaticPledgedNumByTrendId(String trendId){
		StringBuffer buffer =new StringBuffer();
		buffer.append("select nvl(pledged.num- released.num,0), nvl(pledged.weight- released.weight,0) from ");
		buffer.append("(select t.pledge_number num,t.pledge_weight weight from API_PLEDGE t where ");
		buffer.append("t.trend_id = '"+trendId+"' ");
		buffer.append("and t.CONFIRM_STATUS = 1) pledged, ");
		buffer.append("(select nvl(sum(t.pledge_number),0) num,nvl(sum(t.pledge_weight),0) weight from API_PLEDGE t ");
		buffer.append("where t.RELATED_TREND_ID = '"+trendId+"' ");
		buffer.append("and t.CONFIRM_STATUS = 1) released");
		SQLQuery query =  this.getSession().createSQLQuery(buffer.toString());
		Object[] aa = (Object[]) query.uniqueResult();
		HashMap<String, Object>result = new HashMap<String, Object>();
		if(aa[0]!=null||aa[1]!=null) {
			result.put("sumNum", aa[0].toString());
			result.put("sumWeight", aa[1].toString());
		}
		return result;  
	}

	//查询已静态质押的数量
	public HashMap<String, Object> countPledgedByTrayInfoId(Integer trayInfoId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(
				"select nvl(sum(t.pledge_number),0) num,nvl(sum(t.pledge_weight),0) weight from API_PLEDGE t where t.CONFIRM_STATUS = 1 and t.state = 1 ");

		buffer.append("and t.TRAYINFO_ID =" + trayInfoId);

		SQLQuery query = this.getSession().createSQLQuery(buffer.toString());
		Object[] aa = (Object[]) query.uniqueResult();
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("sumNum", aa[0].toString());
		result.put("sumWeight", aa[1].toString());
		return result;
	}
	
	
	//根据库存主键查询当前静态质押的数量，即全部静态质押的数量 - 全部解押的数量
	public HashMap<String, Object> countTotalPledgedByTrayInfoId(Integer trayInfoId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT t.PLEDGE_NUMBER num,  ");
		buffer.append(" t.PLEDGE_WEIGHT weight ");
		buffer.append(" FROM ");
		buffer.append(" API_PLEDGE t WHERE ");
		buffer.append("	t.CONFIRM_STATUS = 1 ");
		buffer.append("	AND t.state in (0,1) ");
		buffer.append(" AND t.TRAYINFO_ID = '"+trayInfoId+"'" );
		buffer.append(" order by t.CREATE_DATE DESC" );
		SQLQuery query = this.getSession().createSQLQuery(buffer.toString());
		List list =  query.list();
		HashMap<String, Object> result = new HashMap<String, Object>();
		if(list.size()>0) {
			Object[] aa = (Object[]) list.get(0);		
			if(aa[0]!=null||aa[1]!=null) {
				result.put("sumNum", aa[0].toString());
				result.put("sumWeight", aa[1].toString());
			}else {
				result.put("sumNum", 0);
				result.put("sumWeight", 0);
			}
		}else {
			result.put("sumNum", 0);
			result.put("sumWeight", 0);
		}
		
		return result;
	}
	
	//根据解押ID查询当前静态质押的数量，即全部静态质押的数量 - 全部解押的数量
	public HashMap<String, Object> countRelatedTrendIdByRelatedTrendId(String relatedTrendId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT t.PLEDGE_NUMBER num,  ");
		buffer.append(" t.PLEDGE_WEIGHT weight  ");
		buffer.append(" FROM ");
		buffer.append(" API_PLEDGE t WHERE ");
		buffer.append("	t.RELATED_TREND_ID = '"+relatedTrendId+"'");
		buffer.append("	AND t.CONFIRM_STATUS = 1  ");
		buffer.append(" order by t.CREATE_DATE DESC" );
		SQLQuery query = this.getSession().createSQLQuery(buffer.toString());
		List list =  query.list();
		HashMap<String, Object> result = new HashMap<String, Object>();
		if(list.size()>0) {
			Object[] aa = (Object[]) list.get(0);		
			if(aa[0]!=null||aa[1]!=null) {
				result.put("sumNum", aa[0].toString());
				result.put("sumWeight", aa[1].toString());
			}else {
				result.put("sumNum", 0);
				result.put("sumWeight", 0);
			}
		}else {
			result.put("sumNum", 0);
			result.put("sumWeight", 0);
		}
		
		return result;
	}
	
	
	
	// 动态质押时获取SKU以及仓库地址
	public HashMap<String, String> getSKUandWarehouse(ApiPledge apiPledge){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select sum(NOW_PIECE) sum,bis.SKU_ID,bis.WAREHOUSE,bis.WAREHOUSE_ID FROM BIS_TRAY_INFO bis ");
		buffer.append("WHERE bis.STOCK_IN = "+ apiPledge.getCustomerId() );
		buffer.append(" and bis.SKU_ID IN  ( SELECT sku.SKU_ID FROM BASE_SKU_BASE_INFO sku WHERE sku.CLASS_TYPE = "+ apiPledge.getItemClass() +" )" );
		buffer.append(" and NOW_PIECE != 0 GROUP BY bis.SKU_ID,bis.WAREHOUSE,bis.WAREHOUSE_ID" );
		buffer.append(" order by sum desc");
		SQLQuery query = this.getSession().createSQLQuery(buffer.toString());
		List list = query.list();
		Object[] obj = (Object[]) list.get(0);
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("sumNum",  obj[0].toString());
		result.put("SKU_ID", obj[1].toString());
		result.put("WAREHOUSE", obj[2].toString());
		result.put("WAREHOUSE_ID", obj[3].toString());
		return result;
	}
	
	//根据库存ID查询质押总量
	public HashMap<String, Object> sumStaticPledgedNum(Integer trayInfoId){
		StringBuffer buffer =new StringBuffer();
		buffer.append(" SELECT ");
		buffer.append(" sum( PLEDGE_NUMBER ) num, ");
		buffer.append(" sum( PLEDGE_WEIGHT ) weight  ");
		buffer.append(" FROM ");
		buffer.append(" ( SELECT rn,TREND_ID,CUSTOMER_CODE,ITEM_CLASS,PNAME,PLEDGE_NUMBER, ");
		buffer.append(" PLEDGE_WEIGHT,CUSTOMER_NAME,RELATED_TREND_ID,COMFIRM_DATE,CONFIRM_STATUS,state ");
		buffer.append(" FROM ");
		buffer.append(" (SELECT ROW_NUMBER ( ) OVER ( PARTITION BY RELATED_TREND_ID ORDER BY COMFIRM_DATE DESC ) rn, ");
		buffer.append(" TREND_ID, ");
		buffer.append(" CUSTOMER_CODE, ");
		buffer.append(" ITEM_CLASS, ");
		buffer.append(" PNAME, ");
		buffer.append(" PLEDGE_NUMBER, ");
		buffer.append(" PLEDGE_WEIGHT, ");
		buffer.append(" CUSTOMER_NAME, ");
		buffer.append(" RELATED_TREND_ID, ");
		buffer.append(" COMFIRM_DATE, ");
		buffer.append(" CONFIRM_STATUS, ");
		buffer.append(" state ");
		buffer.append(" FROM API_PLEDGE");
		buffer.append(" WHERE ");
		buffer.append(" STATE in(0,1) ");
		buffer.append(" AND CONFIRM_STATUS = 1  ");
		buffer.append(" AND TRAYINFO_ID = '"+ trayInfoId+"'");
		buffer.append(" ) WHERE rn = 1) ");
		SQLQuery query = this.getSession().createSQLQuery(buffer.toString());
		List list =  query.list();
		Object[] aa = (Object[]) list.get(0);
		HashMap<String, Object> result = new HashMap<String, Object>();
		if(aa[0]==null||aa[1]==null) {
			result.put("sumNum", 0);
			result.put("sumWeight", 0);
		}else {
			result.put("sumNum", aa[0].toString());
			result.put("sumWeight", aa[1].toString());
		}
		return result;
	}
}
