package com.haiersoft.ccli.cost.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BisInspectionFeeInfo;

@Repository
public class InspectionFeeInfoDao  extends HibernateDao<BisInspectionFeeInfo, Integer> {
	
	//根据ASN获取入库装卸单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEnterStevedoring(String asn) {
		StringBuffer sb=new StringBuffer("select * ");
		sb.append(" from bis_enter_stevedoring where 1 = 1 ");
		sb.append("and  asn_id = :asn ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("asn", asn);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/*
	 * 更新 是否完成 状态
	 */
	public void updateState(String asn) {
		if(asn!=null && !"".equals(asn)){
			String sql="update bis_enter_stevedoring  set if_ok = '1' where asn_id = :asn";
			Map<String,Object> parme=new HashMap<String,Object>();
			parme.put("asn", asn);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	}
	
	//更新明细的提单号
	public void updateBillNum(String feeId, String billNum) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("feeId", feeId);
		params.put("billNum", billNum);
		String sql = "update bis_inspection_fee_info set BILL_NUM = :billNum where FEE_ID = :feeId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
	}
}
