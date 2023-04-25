package com.haiersoft.ccli.wms.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.wms.entity.BisCustomsClearanceInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;

/**
 * 
 * @author 
 * @ClassName: 
 * @Description: 
 * @date 
 */
@Repository
public class CustomsClearanceInfoDao extends HibernateDao<BisCustomsClearanceInfo, Integer>{
	
	/*
	 * 获取报出单的业务单号
	 * @author:
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getByAllWeight(String customsDeclarationNumber){
		StringBuffer buffer=new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("api.CD_NUM ");
		buffer.append("FROM ");
		buffer.append("BIS_CUSTOMS_CLEARANCE api ");
		buffer.append("WHERE 1=1 ");
		if(customsDeclarationNumber != null && !"".equals(customsDeclarationNumber)) {
			buffer.append(" AND api.CUSTOMS_DECLARATION_NUMBER = "+customsDeclarationNumber);
		}
		SQLQuery sqlQuery=createSQLQuery(buffer.toString());
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	public List<Map<String, Object>> getRecordByDNumber(String customsDeclarationNumber) {
		if(StringUtils.isNotEmpty(customsDeclarationNumber)) {
			StringBuffer buffer=new StringBuffer();
			buffer.append("SELECT info.id as id,info.CUS_ID as cusId,");
			buffer.append("info.COMMODITY_CODE as commodityCode,");
			buffer.append("info.COMMODITY_NAME as commodityName,");
			buffer.append("info.LATIN_NAME as latinName,");
			buffer.append("info.SPECIFICATION as specification,");
			buffer.append("info.NUM as num,");
			buffer.append("info.NET_WEIGHT as netWeight,");
			buffer.append("info.GROSS_WEIGHT as grossWeight,");
			buffer.append("info.MONEY as money,");
			buffer.append("info.CURRENCY_VALUE as currencyValue,");	
			buffer.append("info.FIRM_NAME as firmName,");		
			buffer.append("info.PACKAGED_FORM as packagedForm,");	
			buffer.append("info.IF_WOODEN_PACKING as ifWoodenPacking, ");
			buffer.append("info.WOODEN_NO as woodenNo ");
			buffer.append("FROM ");
			buffer.append("BIS_CUSTOMS_CLEARANCE_INFO info, ");
			buffer.append("(SELECT CD_NUM FROM BIS_CUSTOMS_CLEARANCE bis ");
			buffer.append("WHERE bis.CUSTOMS_DECLARATION_NUMBER = '"+customsDeclarationNumber+"' ");
			buffer.append("AND bis.AUDITING_STATE = 3 ) bis ");
			buffer.append("WHERE ");
			buffer.append("bis.CD_NUM = info.CUS_ID ");
			SQLQuery sqlQuery=createSQLQuery(buffer.toString());
			sqlQuery.addScalar("id",StandardBasicTypes.INTEGER);
			sqlQuery.addScalar("cusId",StandardBasicTypes.STRING);
			sqlQuery.addScalar("commodityCode",StandardBasicTypes.STRING);
			sqlQuery.addScalar("commodityName",StandardBasicTypes.STRING);
			sqlQuery.addScalar("latinName",StandardBasicTypes.STRING);
			sqlQuery.addScalar("specification",StandardBasicTypes.STRING);
			sqlQuery.addScalar("num",StandardBasicTypes.BIG_DECIMAL);
			sqlQuery.addScalar("netWeight",StandardBasicTypes.BIG_DECIMAL);
			sqlQuery.addScalar("grossWeight",StandardBasicTypes.BIG_DECIMAL);
			sqlQuery.addScalar("money",StandardBasicTypes.BIG_DECIMAL);
			sqlQuery.addScalar("currencyValue",StandardBasicTypes.STRING);
			sqlQuery.addScalar("firmName",StandardBasicTypes.STRING);
			sqlQuery.addScalar("packagedForm",StandardBasicTypes.STRING);
			sqlQuery.addScalar("ifWoodenPacking",StandardBasicTypes.STRING);
			sqlQuery.addScalar("woodenNo",StandardBasicTypes.STRING);

			return sqlQuery.setResultTransformer(Transformers.aliasToBean(BisCustomsClearanceInfo.class)).list();
			
		}
		// TODO Auto-generated method stub
		
		return null;
	}	
	
}
