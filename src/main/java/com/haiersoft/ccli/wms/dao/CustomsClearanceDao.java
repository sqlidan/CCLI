package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.entity.BisCustomsClearance;

/**
 * 
 * @author 
 *
 */
@Repository
public class CustomsClearanceDao extends HibernateDao<BisCustomsClearance, String>{
	
    private static String like(String str) {
        return "%" + str + "%";
    }
	/**
	 * 获取信息
	 * @param page 页面
	 * @param params 参数
	 * @return
	 */
	public Page<BisCustomsClearance> seachCustomsClearanceSql(Page<BisCustomsClearance> page, BisCustomsClearance customsClearance) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("SELECT a.*,t.* FROM BIS_CUSTOMS_CLEARANCE a");
		buffer.append(" LEFT JOIN (");
		buffer.append("SELECT sum(bcci.NUM) AS NUM,sum(bcci.NET_WEIGHT) AS NET_WEIGHT, sum(bcci.GROSS_WEIGHT) AS GROSS_WEIGHT");
		buffer.append(", sum(bcci.MONEY) AS MONEY, min(bcci.CURRENCY_VALUE) AS CURRENCY_VALUE,bcci.CUS_ID, min(bcci.WOODEN_NO) AS WOODEN_NO");
		buffer.append(" FROM BIS_CUSTOMS_CLEARANCE_INFO bcci");
		buffer.append(" GROUP BY bcci.CUS_ID");
		buffer.append(") t");
		buffer.append(" ON T.CUS_ID = a.CD_NUM");
		buffer.append(" where 1=1  ");
        Map<String, Object> queryParams = new HashMap<>();
        if (StringUtils.nonNull(customsClearance.getCdNum())) {
        	buffer.append(" and a.CD_NUM like :cdNum");
            queryParams.put("cdNum",like(customsClearance.getCdNum()));
        }
        
        if (StringUtils.nonNull(customsClearance.getServiceProject())) {
        	buffer.append(" and a.SERVICE_PROJECT = :serviceProject");
            queryParams.put("serviceProject",customsClearance.getServiceProject());
        }
        if (StringUtils.nonNull(customsClearance.getAuditingState())) {
        	buffer.append(" and a.AUDITING_STATE = :auditingState");
            queryParams.put("auditingState",customsClearance.getAuditingState());
        }
        if (StringUtils.nonNull(customsClearance.getModeTrade())) {
        	buffer.append(" and a.MODE_TRADE = :modeTrade");
            queryParams.put("modeTrade",customsClearance.getModeTrade());
        }
        if (StringUtils.nonNull(customsClearance.getBillNum())) {
        	buffer.append(" and a.BILL_NUM like :billNum");
            queryParams.put("billNum",like(customsClearance.getBillNum()));
        }
        if (StringUtils.nonNull(customsClearance.getCustomsDeclarationNumber())) {
        	buffer.append(" and a.CUSTOMS_DECLARATION_NUMBER like :customsDeclarationNumber");
            queryParams.put("customsDeclarationNumber",like(customsClearance.getCustomsDeclarationNumber()));
        }
		if (StringUtils.nonNull(customsClearance.getSearchStrTime())) {//--申报日期

			buffer.append(" and a.DECLARE_TIME>=to_date(:searchstrtime,'yyyy-mm-dd hh24:mi:ss')  ");
			queryParams.put("searchstrtime", customsClearance.getSearchStrTime());
		}
		if (StringUtils.nonNull(customsClearance.getSearchEndTime())) {//--申报日期

			buffer.append(" and a.DECLARE_TIME<to_date(:searchendtime,'yyyy-mm-dd hh24:mi:ss')");
			queryParams.put("searchendtime", customsClearance.getSearchEndTime());
		}

		buffer.append(" order by a.OPERATE_TIME desc");
//        Map<String, Object> paramType = new HashMap<>();
//        paramType.put("cdNum", String.class);
//		return findPageSql(page, buffer.toString(),paramType, queryParams);
        return findPageSql(page, buffer.toString(),queryParams);
	}
	

	public List<Map<String, String> >seachCustomsClearanceSql(BisCustomsClearance customsClearance) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("SELECT a.*,t.* FROM BIS_CUSTOMS_CLEARANCE a");
		buffer.append(" LEFT JOIN (");
		buffer.append("SELECT sum(bcci.NUM) AS NUM,sum(bcci.NET_WEIGHT) AS NET_WEIGHT, sum(bcci.GROSS_WEIGHT) AS GROSS_WEIGHT");
		buffer.append(", sum(bcci.MONEY) AS MONEY, min(bcci.CURRENCY_VALUE) AS CURRENCY_VALUE,bcci.CUS_ID, min(bcci.WOODEN_NO) AS WOODEN_NO");
		buffer.append(" FROM BIS_CUSTOMS_CLEARANCE_INFO bcci");
		buffer.append(" GROUP BY bcci.CUS_ID");
		buffer.append(") t");
		buffer.append(" ON T.CUS_ID = a.CD_NUM");
		buffer.append(" where 1=1  ");
		Map<String, Object> queryParams = new HashMap<>();
		if (StringUtils.nonNull(customsClearance.getCdNum())) {
			buffer.append(" and a.CD_NUM like :cdNum");
			queryParams.put("cdNum",like(customsClearance.getCdNum()));
		}

		if (StringUtils.nonNull(customsClearance.getServiceProject())) {
			buffer.append(" and a.SERVICE_PROJECT = :serviceProject");
			queryParams.put("serviceProject",customsClearance.getServiceProject());
		}
		if (StringUtils.nonNull(customsClearance.getAuditingState())) {
			buffer.append(" and a.AUDITING_STATE = :auditingState");
			queryParams.put("auditingState",customsClearance.getAuditingState());
		}
		if (StringUtils.nonNull(customsClearance.getModeTrade())) {
			buffer.append(" and a.MODE_TRADE = :modeTrade");
			queryParams.put("modeTrade",customsClearance.getModeTrade());
		}
		if (StringUtils.nonNull(customsClearance.getBillNum())) {
			buffer.append(" and a.BILL_NUM like :billNum");
			queryParams.put("billNum",like(customsClearance.getBillNum()));
		}
		if (StringUtils.nonNull(customsClearance.getCustomsDeclarationNumber())) {
			buffer.append(" and a.CUSTOMS_DECLARATION_NUMBER like :customsDeclarationNumber");
			queryParams.put("customsDeclarationNumber",like(customsClearance.getCustomsDeclarationNumber()));
		}
		if (StringUtils.nonNull(customsClearance.getSearchStrTime())) {//--申报日期

			buffer.append(" and a.DECLARE_TIME>=to_date(:searchstrtime,'yyyy-mm-dd hh24:mi:ss')  ");
			queryParams.put("searchstrtime", customsClearance.getSearchStrTime());
		}
		if (StringUtils.nonNull(customsClearance.getSearchEndTime())) {//--申报日期

			buffer.append(" and a.DECLARE_TIME<to_date(:searchendtime,'yyyy-mm-dd hh24:mi:ss')");
			queryParams.put("searchendtime", customsClearance.getSearchEndTime());
		}

		buffer.append(" order by a.OPERATE_TIME desc");
//        Map<String, Object> paramType = new HashMap<>();
//        paramType.put("cdNum", String.class);
//		return findPageSql(page, buffer.toString(),paramType, queryParams);
		return findSql(buffer.toString(),queryParams);
	}

	public String countNum(String cdNum) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("SELECT sum(info.num) FROM BIS_CUSTOMS_CLEARANCE bis, BIS_CUSTOMS_CLEARANCE_INFO info ");
		buffer.append("WHERE bis.CD_NUM  = info.cus_id "); 
		buffer.append("AND bis.SERVICE_PROJECT = 0 "); //0为“报进”
		buffer.append("AND bis.AUDITING_STATE = 3 ");	//3为审核通过
		buffer.append("AND bis.CUSTOMS_DECLARATION_NUMBER ='"+cdNum+"'");

		SQLQuery sqlQuery = createSQLQuery(buffer.toString());
		Object result = sqlQuery.uniqueResult();
		if(null ==result) {
			return null;
		}
		else {
			return result.toString();
		}
		
	}
	
	//报进数量-报出数量
	public String countMaxNum(String cdNum) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("SELECT (( nvl(inRecord.num,0) - nvl(outRecord.num,0) ) ) FROM ");
		buffer.append("(SELECT sum(info.num) num ");
		buffer.append("FROM BIS_CUSTOMS_CLEARANCE bis, BIS_CUSTOMS_CLEARANCE_INFO info ");
		buffer.append("WHERE bis.CD_NUM  = info.cus_id "); 
		buffer.append("AND bis.SERVICE_PROJECT = 0 "); //0为“报进”
		buffer.append("AND bis.AUDITING_STATE = 1 ");	//1为审核通过
		buffer.append("AND bis.CUSTOMS_DECLARATION_NUMBER ='"+cdNum+"'");
		buffer.append(") inRecord,");
		buffer.append("(SELECT sum(info.num) num ");		
		buffer.append("FROM BIS_CUSTOMS_CLEARANCE bis, BIS_CUSTOMS_CLEARANCE_INFO info ");
		buffer.append("WHERE bis.CD_NUM  = info.cus_id "); 
		buffer.append("AND bis.SERVICE_PROJECT = 1 "); //0为“报出”
	//	buffer.append("AND bis.AUDITING_STATE = 1 ");	//1为审核通过
		buffer.append("AND bis.CUSTOMS_DECLARATION_NUMBER ='"+cdNum+"'");
		buffer.append(") outRecord");
		SQLQuery sqlQuery = createSQLQuery(buffer.toString());
		Object result = sqlQuery.uniqueResult();
		if(null ==result) {
			return null;
		}
		else {
			return result.toString();
		}
		
	}
	
}
