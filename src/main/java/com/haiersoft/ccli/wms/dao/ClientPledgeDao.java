package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.common.persistence.HibernateDao;
@Repository
public class ClientPledgeDao  extends HibernateDao<BaseClientPledge, Integer> {
	
	/*
	 * 静态质押分组查询
	 * @author:PYL
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getTray(String clientName,String sku,String billNum,String ctnNum, String warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select client,client_name as clientName,bill_num as billNum,ctn_num as ctnNum,sku_id as sku,pname,sum(num) as NUM,ptype,sum(net_weight) as netWeight,warehouse ");
		sb.append(" from base_client_pledge where 1 = 1 ");
		if(clientName != null && !"".equals(clientName)){
			sb.append("and lower(client_name) like lower(:clientName) ");
			params.put("clientName","%"+clientName+"%");
		}
		if(sku != null && !"".equals(sku)){
			sb.append("and lower(sku_id) like lower(:sku)");
			params.put("sku", "%"+sku+"%");
		}
		if(billNum != null && !"".equals(billNum)){
			sb.append(" and lower(bill_num) like lower(:billNum) ");
			params.put("billNum","%"+billNum+"%");
		}
		if(ctnNum != null && !"".equals(ctnNum)){
			sb.append(" and lower(ctn_num) like lower(:ctnNum) " );
			params.put("ctnNum", "%"+ctnNum+"%");
		}
		if(warehouseId != null && !"".equals(warehouseId)){
			sb.append(" and warehouse_id = :warehouseId " );
			params.put("warehouseId", warehouseId);
		}
		sb.append(" GROUP BY client,client_name,bill_num,ctn_num,sku_id,pname,ptype,warehouse");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
}
