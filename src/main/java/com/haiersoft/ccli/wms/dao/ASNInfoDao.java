package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
@Repository
public class ASNInfoDao extends HibernateDao<BisAsnInfo, String> {
	/**
	 * 根据ansId删除ASN所有明细
	 * @param asnId
	 * @return
	 */
	public void deleteASNInfos(String asnId){
		if(asnId!=null && !"".equals(asnId)){
			String hql="delete BisAsnInfo a where a.asnId=?0";
			batchExecute(hql, asnId);
		}
	}

	
	/**
	 * 通过SQL语句，根据ASNid获取明细对象列表
	 * @author PYL
	 * @param ans
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAsnInfoByAsn(String asn) {
		StringBuffer sb=new StringBuffer("select a.*,b.INBOUND_DATE as inboundTime ");
		sb.append(" from bis_asn b , bis_tray_info a  where 1 = 1 ");
		sb.append(" and b.asn = :asn ");
		sb.append(" and a.asn = b.asn ");
//		sb.append("GROUP BY client,client_name,bill_num,ctn_num,sku_id,pname,ptype");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("asn", asn);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAsnReportByAsn(String asn) {
			StringBuffer sb=new StringBuffer("select o.sku_id,"
											+"o.cargo_name,"
											+"o.piece,"
											+"a.ctn_num,"
											+"a.bill_num,"
											+"t.tray_id,"
											+"t.cargo_location ");
			sb.append(" from bis_asn_info o ");
			sb.append(" inner join bis_asn a on a.asn = o.asn_id ");
			sb.append(" inner join bis_tray_info t on t.asn = a.asn and t.bill_num = a.bill_num and t.sku_id = o.sku_id and t.ctn_num = a.ctn_num ");
			sb.append(" where o.asn_id =?0");
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), asn);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}


	
}
