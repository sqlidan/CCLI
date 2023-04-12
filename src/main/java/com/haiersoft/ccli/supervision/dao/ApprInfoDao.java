package com.haiersoft.ccli.supervision.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.supervision.entity.ApprInfo;

@Repository
public class ApprInfoDao extends HibernateDao<ApprInfo, String> {
	
	public void updateArrpIdbyHeadId(String apprId, String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("apprId", apprId);
		params.put("headId", headId);
		String sql = "update FLJG_APPR_INFO set APPR_ID = :apprId where HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
	}

	public void deleteByHeadId(String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("headId", headId);
		String sql = "delete FLJG_APPR_INFO where HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
		
	}

	public ApprInfo findByOutBisInfoId(String bisInfoId) {
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("bisInfoId", bisInfoId);
        params.put("ioType", "2");
        List<ApprInfo> result = findBy(params);
        if (result.size() == 1){
            return result.get(0);
        }
        return null;
	}

	public ApprInfo findByEntBisInfoId(String bisInfoId) {
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("bisInfoId", bisInfoId);
        params.put("ioType", "1");
        List<ApprInfo> result = findBy(params);
        if (result.size() == 1){
            return result.get(0);
        }
        return null;
	}

	public ApprInfo findByEntBisInfoIdNew(String bisInfoId) {
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("bisInfoId", bisInfoId);
        params.put("ioType", "1");
        List<ApprInfo> result = findBy(params);
        if (result.size() == 1){
            return result.get(0);
        }else if (result.size() > 1){
        	Date d1 = result.get(0).getCreateTime();
        	ApprInfo resInfo = result.get(0);
        	for(int i =1;i< result.size();i++) {
        		Date currInfoDate = result.get(i).getCreateTime();
        		//如果d1比这个info的创建日期早，返回当前的info
        		if(d1.before(currInfoDate)) {
        			d1=currInfoDate;
        			resInfo = result.get(i);
        		}
        	}
        	return resInfo;
        }
        return null;
	}
}
