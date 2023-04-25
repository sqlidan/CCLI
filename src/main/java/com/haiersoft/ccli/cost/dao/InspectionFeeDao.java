package com.haiersoft.ccli.cost.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BisInspectionFee;

@Repository
public class InspectionFeeDao  extends HibernateDao<BisInspectionFee, String> {
	

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findReportList(String clientId,Date startTime, Date endTime,String ifLx,String sort,String order) {
		HashMap<String,Object> parme=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT                                                                                                                                                        ");                  
		sb.append("	t.CLIENT_ID,                                                                                                                                                 ");
		sb.append("	t.client_name AS client_name,                                                                                                                           ");
		sb.append("	sum(t.shuichanCY) AS shuichanCY,                                                                                                                             ");
		sb.append("	sum(t.dongrouCY) AS dongrouCY,                                                                                                                               ");
		sb.append("	sum(t.shuiguoCY) AS shuiguoCY,                                                                                                                               ");
		sb.append("	sum(t.qitaCY) AS qitaCY,                                                                                                                                     ");
		sb.append("	sum(t.PLUG_PRICE) AS chadian,                                                                                                                                ");
		sb.append("	sum(t.HANG_PRICE) AS diaoxiang,                                                                                                                              ");
		sb.append("	sum(t.FIELD_PRICE) AS changdi,                                                                                                                               ");
		sb.append("	sum(t.HANDING_PRICE) AS bandao,                                                                                                                              ");
		sb.append("	max(                                                                                                                                                         ");
		sb.append("		Extract(YEAR FROM t.CHECK_DATE)                                                                                                                          ");
		sb.append("	) AS YEAR,                                                                                                                                                   ");
		sb.append("	max(                                                                                                                                                         ");
		sb.append("		Extract(MONTH FROM t.CHECK_DATE)                                                                                                                         ");
		sb.append("	) AS MONTH,                                                                                                                                                  ");
		sb.append("	(                                                                                                                                                            ");
		sb.append("		sum(t.shuichanCY) + sum(t.dongrouCY) + sum(t.shuiguoCY) + sum(t.qitaCY) + sum(t.PLUG_PRICE) + sum(t.HANG_PRICE) + sum(t.FIELD_PRICE) + sum(t.HANDING_PRICE)");
		sb.append("	) AS COST                                                                                                                                                    ");
		sb.append("FROM                                                                                                                                                          ");
		sb.append("	(                                                                                                                                                            ");
		sb.append("		SELECT                                                                                                                                                   ");
		sb.append("			fee.CLIENT_ID,                                                                                                                                       ");
		sb.append("			fee.client_name,                                                                                                                                     ");
		sb.append("			fee.CHECK_DATE,                                                                                                                                      ");
		sb.append("			decode(                                                                                                                                              ");
		sb.append("				fee.CHECK_TYPE,                                                                                                                                  ");
		sb.append("				'1',                                                                                                                                             ");
		sb.append("				nvl(feeinf.CHECK_STANDARD,0),                                                                                                                           ");
		sb.append("				0                                                                                                                                                ");
		sb.append("			) AS shuichanCY,                                                                                                                                     ");
		sb.append("			decode(                                                                                                                                              ");
		sb.append("				fee.CHECK_TYPE,                                                                                                                                  ");
		sb.append("				'2',                                                                                                                                             ");
		sb.append("				nvl(feeinf.CHECK_STANDARD,0),                                                                                                                           ");
		sb.append("				0                                                                                                                                                ");
		sb.append("			) AS dongrouCY,                                                                                                                                      ");
		sb.append("			decode(                                                                                                                                              ");
		sb.append("				fee.CHECK_TYPE,                                                                                                                                  ");
		sb.append("				'3',                                                                                                                                             ");
		sb.append("				nvl(feeinf.CHECK_STANDARD,0),                                                                                                                           ");
		sb.append("				0                                                                                                                                                ");
		sb.append("			) AS shuiguoCY,                                                                                                                                      ");
		sb.append("			decode(                                                                                                                                              ");
		sb.append("				fee.CHECK_TYPE,                                                                                                                                  ");
		sb.append("				'4',                                                                                                                                             ");
		sb.append("				nvl(feeinf.CHECK_STANDARD,0),                                                                                                                           ");
		sb.append("				0                                                                                                                                                ");
		sb.append("			) AS qitaCY,                                                                                                                                         ");
		sb.append("			nvl(PLUG_PRICE,0) AS PLUG_PRICE,                                                                                                                                          ");
		sb.append("			nvl(HANG_PRICE,0) AS HANG_PRICE,                                                                                                                                          ");
		sb.append("			nvl(FIELD_PRICE,0) AS FIELD_PRICE,                                                                                                                                         ");
		sb.append("			nvl(HANDING_PRICE,0) AS HANDING_PRICE                                                                                                                                        ");
		sb.append("		FROM                                                                                                                                                     ");
		sb.append("			bis_inspection_fee fee                                                                                                                               ");
		sb.append("		LEFT JOIN bis_inspection_fee_info feeinf ON fee.fee_id = feeinf.fee_id                                                                                   ");
		sb.append("		WHERE                                                                                                                                                    ");
		sb.append("			1 = 1                                                                                                                                                ");
		if(!"".equals(clientId) && null != clientId){
			sb.append(" AND fee.client_name like '%"+clientId+"%'");
		}
		if(!"".equals(ifLx) && null != ifLx){
			sb.append(" AND fee.IF_LX=:ifLx");
			parme.put("ifLx",ifLx);
		}
		if(null !=startTime && !"".equals(startTime)){
			sb.append(" AND fee.check_date >= :startTime ");
			parme.put("startTime", startTime);
		}
		if(null !=endTime && !"".equals(endTime)){
			sb.append(" AND fee.check_date <= :endTime ");
			parme.put("endTime", endTime);
		}
		sb.append("	) t                                                                                                                                                          ");
		sb.append("GROUP BY                                                                                                                                                      ");
		sb.append("	t.CLIENT_ID,                                                                                                                                                 ");
		sb.append(" t.CLIENT_NAME,                                                                                                                                               ");
		sb.append("	trunc (t.CHECK_DATE, 'mm')                                                                                                                                   ");
		if(null!=sort&&!"".equals(sort)){
			sb.append(" ORDER BY "+sort+" "+order);
		}
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
}
