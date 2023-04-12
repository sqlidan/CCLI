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
public class ItemNumReportDao extends HibernateDao<Stock, String> {
	
	
	/**
	 * 根据提单号和日期获取水产出入库统计
	 * @param starTime
	 * @param endTime
	 * @param npage
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> getItemNumReportInfo(String starTime,String endTime,int npage,int pageSize,String itemNum){
		Map<String,Object> returnMap = new HashMap<String,Object>();
			StringBuffer sql=new StringBuffer();
			HashMap<String,Object> parme=new HashMap<String,Object>();
			sql.append(" select instore.item_num,instore.cargo_name,instore.declare_time,instore.cd_num,instore.bill_num,instore.net_weight,");
			sql.append(" outstore.declare_time as outDeclareTime,outstore.cd_num as outCdNum,outstore.net_weight as outNetWeight,");
			sql.append(" instore.unit_price,instore.scalar,outstore.scalar as outScalar,");
			sql.append(" instore.total_prices,outstore.total_prices as outTotalPrices,");	
			sql.append(" inweight.intotalweight,outweight.outtotalweight");
			sql.append(" from  ");
			sql.append(" ( ");
			sql.append(" select t.cd_num as HS,t.item_num,t.cargo_name,d.declare_time,d.cd_num,d.bill_num,");
			sql.append(" sum(nvl(t.net_weight,0)) as net_weight,nvl(t.unit_price,0) as unit_price,sum(nvl(t.scalar,0)) as scalar,");
			sql.append(" sum(nvl(t.total_prices,0)*rmbtax.exchange_rate/mytax.exchange_rate) as total_prices, /*货值（美元）*/  ");
			sql.append(" t.item_num||d.cd_num as idItem");
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
			sql.append(" sum(nvl(t.total_prices,0)*rmbtax.exchange_rate/mytax.exchange_rate) as total_prices, /*货值（美元）*/  ");
			sql.append(" t.item_num||d.cd_num as idItem");
			sql.append(" from bis_customs_declaration_info t  ");
			sql.append(" left join base_tax_rate rmbtax on rmbtax.currency_type=t.Currency_Type ");
			sql.append(" left join base_tax_rate mytax on mytax.currency_type=1 ");
			sql.append(" left join bis_customs_declaration d on t.cus_id=d.id where d.in_out_sign=2");
			if(starTime!=null && !"".equals(starTime)){
				sql.append("and trunc(d.declare_time,'mm') >= to_date(:starTime,'yyyy-mm') ");
				parme.put("starTime", starTime);
			}
			if(endTime!=null && !"".equals(endTime)){
				sql.append("  and trunc(d.declare_time,'mm') <= to_date(:endTime,'yyyy-mm') ");
				parme.put("endTime", endTime);
			}
			sql.append("group by t.cd_num,t.item_num,d.bill_num,d.cd_num,d.declare_time,t.cargo_name) outstore ");
			sql.append(" on instore.item_num=outstore.item_num and instore.HS=outstore.HS and instore.cd_num=outstore.cd_num ");
			
			sql.append(" left join (select a.item_num||b.cd_num as idItem,sum(nvl(a.net_weight,0)) as intotalweight from bis_customs_declaration_info a left join bis_customs_declaration b on a.cus_id=b.id where b.in_out_sign=1 group by a.item_num||b.cd_num ) inweight  ");
			sql.append(" on instore.idItem=inweight.idItem ");
			sql.append(" left join (select e.item_num||f.cd_num as idItem,sum(nvl(e.net_weight,0)) as outtotalweight from bis_customs_declaration_info e left join bis_customs_declaration f on e.cus_id=f.id where f.in_out_sign=2 group by e.item_num||f.cd_num ) outweight  ");
			sql.append(" on outstore.idItem=outweight.idItem ");
			
			if(itemNum!=null && !"".equals(itemNum)){
				sql.append("  where lower(instore.item_num) like lower(:itemNum) ");
				parme.put("itemNum", "%"+itemNum+"%");
			}
			sql.append("  order by instore.declare_time desc,outstore.declare_time desc");
			
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
			if(pageSize>0){
				long totalCount = countSqlResult(sql.toString(), parme);
				sqlQuery.setFirstResult(PageUtils.calBeginIndex(npage, pageSize,Integer.valueOf(String.valueOf(totalCount))));
				sqlQuery.setMaxResults(pageSize);
				returnMap.put("total", totalCount);
			}
			returnMap.put("rows", sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		return returnMap;
	}
}
