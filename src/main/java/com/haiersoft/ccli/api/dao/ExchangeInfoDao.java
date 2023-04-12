package com.haiersoft.ccli.api.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.entity.ExchangeInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;

@Repository
public class ExchangeInfoDao extends HibernateDao<ExchangeInfo, String> {

	/**
	 * 查询质押信息
	 * @return
	 */
	public List<ApiPledge> getPledgeInfo(Integer trayId, String trendId) {
		List<ApiPledge> pledgeList = new ArrayList();
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT");
		sb.append("	* ");
		sb.append("FROM");
		sb.append("	api_pledge ");
		sb.append("WHERE");
		if(trayId != null) {
			sb.append("	trayinfo_id = " + trayId + "");
		}
		if(StringUtils.isNotBlank(trendId)) {
			sb.append("	and trend_id = '" + trendId + "'");
		}
		
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString()).addEntity(ApiPledge.class);
		List list = sqlQuery.list();
		if(null != list && list.size() > 0) {
			for(int i=0; i<list.size(); i++) {
				ApiPledge apiPledge = (ApiPledge)list.get(i);
				if(null != apiPledge) {
					pledgeList.add(apiPledge);
				}
			}
		}
		return pledgeList;
	}
	
	/**
	 * 查询所有换货信息
	 */
//	public List<Map<String,Object>> getExchangeList(){
//		List<Map<String,Object>> exchangeList = new ArrayList();
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT");
//		sb.append("	* ");
//		sb.append("FROM");
//		sb.append("	api_exchange_info");
//		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString()).addEntity(ExchangeInfo.class);
//		List list = sqlQuery.list();
//		for(int i=0; i<list.size(); i++) {
//			ExchangeInfo exchangeInfo = (ExchangeInfo)list.get(i);
//			Map<String,Object> map = new HashMap();
//			map.put("ID", exchangeInfo.getId());
//			map.put("ACCOUNT_ID", exchangeInfo.getAccountId());
//			map.put("NEW_PLEDGE_ID", exchangeInfo.getNewPledgeId());
//			map.put("PLEDGE_NUMBER", exchangeInfo.getPledgeNumber());
//			map.put("PLEDGE_WEIGHT", exchangeInfo.getPledgeWeight());
//			map.put("PLEDGE_STATUS", exchangeInfo.getPledgeStatus());
//			map.put("ORIGINAL_PLEDGE_ID", exchangeInfo.getOriginalPledgeId());
//			map.put("RELIEVE_NUMBER", exchangeInfo.getRelieveNumber());
//			map.put("RELIEVE_WEIGHT", exchangeInfo.getRelieveWeight());
//			map.put("RELIEVE_STATUS", exchangeInfo.getRelieveStatus());
//			map.put("CREATE_TIME", exchangeInfo.getCreateTime());
//			map.put("UPDATE_TIME", exchangeInfo.getUpdateTime());
//			map.put("STATUS", exchangeInfo.getStatus());
//			map.put("SOURCE_TREND_ID", exchangeInfo.getSourceTrendId());
//			map.put("RELATED_ID", exchangeInfo.getRelatedId());
//			map.put("TREND_ID", exchangeInfo.getTrendId());
//			
//			exchangeList.add(map);
//		}
//		return exchangeList;
//	}
	
	/**
	 * 修改状态
	 */
	public Integer updStatus(Integer status, String id) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE api_exchange_info ");
		sb.append("SET status = " + status + " ");
		sb.append("WHERE");
		sb.append("	id = '" + id + "'");
		
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
		int i = sqlQuery.executeUpdate();
		return i;
	}
}
