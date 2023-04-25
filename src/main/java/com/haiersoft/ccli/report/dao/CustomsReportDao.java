package com.haiersoft.ccli.report.dao;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.PageUtils;
import com.haiersoft.ccli.report.entity.Stock;
@Repository
public class CustomsReportDao extends HibernateDao<Stock, String> {
	
	
	/**
	 * 根据日期获取海关报表统计
	 * @param starTime
	 * @param endTime
	 * @param npage
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> getCustomsReportInfo(String starTime,String endTime,int npage,int pageSize){
		Map<String,Object> returnMap = new HashMap<String,Object>();
			StringBuffer sql=new StringBuffer();
			HashMap<String,Object> parme=new HashMap<String,Object>();
			sql.append(" select instore.item_num,instore.cargo_name,instore.declare_time,instore.cd_num,instore.bill_num,instore.net_weight,");
			sql.append(" outstore.declare_time as outDeclareTime,outstore.cd_num as outCdNum,outstore.net_weight as outNetWeight,");
			sql.append(" instore.unit_price,instore.scalar,outstore.scalar as outScalar,");
			sql.append(" nvl(instore.total_prices,0) as total_prices,outstore.total_prices as outTotalPrices");
			sql.append(" from  ");
			sql.append(" ( ");
			sql.append(" select t.cd_num as HS,t.item_num,t.cargo_name,d.declare_time,d.cd_num,d.bill_num,");
			sql.append(" sum(nvl(t.net_weight,0)) as net_weight,nvl(t.unit_price,0) as unit_price,sum(nvl(t.scalar,0)) as scalar,");
			sql.append(" sum(nvl(t.total_prices,0)*rmbtax.exchange_rate/mytax.exchange_rate) as total_prices /*货值（美元）*/  ");
			sql.append(" from bis_customs_declaration_info t ");
			sql.append(" left join base_tax_rate rmbtax on rmbtax.currency_type=t.Currency_Type ");
			sql.append(" left join base_tax_rate mytax on mytax.currency_type=1 ");
			sql.append(" left join bis_customs_declaration d on t.cus_id=d.id where d.in_out_sign=1 ");			
			if(starTime!=null && !"".equals(starTime)){
				sql.append("and trunc(d.declare_time,'mm') >= to_date(:starTime,'yyyy-mm') ");
				parme.put("starTime", starTime);
			}
			if(endTime!=null && !"".equals(endTime)){
				sql.append("and trunc(d.declare_time,'mm') <= to_date(:endTime,'yyyy-mm') ");
				parme.put("endTime", endTime);
			}
			sql.append("  group by t.cd_num,t.item_num,d.bill_num,d.declare_time,t.cargo_name,d.declare_time,d.cd_num,d.bill_num,t.unit_price ");
			sql.append("  )  instore  ");
			sql.append(" left join ");
			sql.append(" (  ");
			sql.append(" select t.cd_num as HS,t.item_num,t.cargo_name,d.declare_time,d.cd_num,d.bill_num, ");
			sql.append(" sum(nvl(t.net_weight,0)) as net_weight,sum(nvl(t.scalar,0)) as scalar,");
			sql.append(" sum(nvl(t.total_prices,0)*rmbtax.exchange_rate/mytax.exchange_rate) as total_prices /*货值（美元）*/  ");
			sql.append(" from bis_customs_declaration_info t  ");
			sql.append(" left join base_tax_rate rmbtax on rmbtax.currency_type=t.Currency_Type ");
			sql.append(" left join base_tax_rate mytax on mytax.currency_type=1 ");
			sql.append(" left join bis_customs_declaration d on t.cus_id=d.id where d.in_out_sign=2");		
			if(starTime!=null && !"".equals(starTime)){
				sql.append("and trunc(d.declare_time,'mm') >= to_date(:starTime,'yyyy-mm') ");
				parme.put("starTime", starTime);
			}
			if(endTime!=null && !"".equals(endTime)){
				sql.append(" and trunc(d.declare_time,'mm') <= to_date(:endTime,'yyyy-mm') ");
				parme.put("endTime", endTime);
			}
			sql.append("group by t.cd_num,t.item_num,d.bill_num,d.cd_num,d.declare_time,t.cargo_name) outstore ");
			sql.append(" on instore.item_num=outstore.item_num  and ");
			sql.append(" instore.HS=outstore.HS and instore.cd_num=outstore.cd_num ");
			sql.append("  order by instore.declare_time desc,outstore.declare_time desc");
			
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
			if(pageSize>0){
				long totalCount = countSqlResult(sql.toString(), parme);
				sqlQuery.setFirstResult(PageUtils.calBeginIndex(npage, pageSize,Integer.valueOf(String.valueOf(totalCount))));
				sqlQuery.setMaxResults(pageSize);
				returnMap.put("total", totalCount);
			}
			returnMap.put("rows", sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		//}
		return returnMap;
	}
}
