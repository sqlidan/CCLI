package com.haiersoft.ccli.supervision.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.supervision.entity.OpApprHead;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OpApprHeadDao extends HibernateDao<OpApprHead, String> {
	
	public void updateArrpIdbyId(String apprId, String localStatus,String id) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("apprId", apprId);
		params.put("id", id);
		params.put("localStatus", localStatus);
		String sql = "update FLJG_OP_APPR_HEAD set APPR_ID = :apprId, LOCAL_STATUS = :localStatus, STATUS = :localStatus where ID = :id ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
	}

	public void updateByApprId(OpApprHead apprHead) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "update FLJG_APPR_HEAD set TRADE_CODE  = :tradeCode, "
        		+ "TRADE_NAME = :tradeName, "
         		+ "OWNER_CODE = :ownerCode, "
        		+ "OWNER_NAME = :ownerName, "
        		+ "AGENT_CODE = :agentCode, "
        		+ "AGENT_NAME = :agentName, "
        		+ "LOADING_FLAG = :loadingFlag, "
        		+ "STATUS = :status, "
        		+ "INPUTER = :inputEr, "
        		+ "INPUT_DATE = :inputDate, "
        		+ "DDATE = :dDate, "
        		+ "APPROVE_DATE = :approveDate, "
        		+ "APPROVE_NOTE = :approveNote, "
        		+ "PASS_STATUS = :passStatus, "
        		+ "DECL_TYPE = :declType "
        		+ "where APPR_ID = :apprId ";        
        params.put("tradeCode",apprHead.getTradeCode());
        params.put("tradeName",apprHead.getTradeName());
        params.put("ownerCode",apprHead.getOwnerCode());
        params.put("ownerName",apprHead.getOwnerName());
        params.put("agentCode",apprHead.getAgentCode()); 
        params.put("agentName",apprHead.getAgentName());
        params.put("loadingFlag",apprHead.getLoadingFlag());
        params.put("status",apprHead.getStatus());
        params.put("inputEr",apprHead.getInputEr()); 
        params.put("inputDate",apprHead.getInputDate());
        params.put("dDate",apprHead.getdDate());
        params.put("approveDate",apprHead.getApproveDate());
        params.put("approveNote",apprHead.getApproveNote()); 
        params.put("passStatus",apprHead.getPassStatus());
        params.put("declType",apprHead.getDeclType());
        params.put("apprId",apprHead.getApprId());
        
        SQLQuery sqlQuery = createSQLQuery(sql, params);

        sqlQuery.executeUpdate();
		
	}



	public void updateLocalStatusbyId(String status, String id) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		params.put("status", status);
		String sql = "update FLJG_APPR_HEAD set LOCAL_STATUS = :status where ID = :id ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
		
	}

	
	/**
	 * 查询所有要更新的申请单的apprID
	 * 申请单状态不等于 2（审批通过）
	 * @param 
	 * @return 结果集合
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllUpdateApprID(){
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT APPR_ID apprId FROM FLJG_APPR_HEAD where APPR_ID is not null and STATUS !='2'");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.list();
	}
	
	
	//获取最大底账项号
	public String getMaxGno() {
		StringBuffer sb=new StringBuffer ( "SELECT  MAX(TO_NUMBER(fah.GNO)) as gno  FROM FLJG_APPR_HEAD  fah WHERE  IO_TYPE = '1'");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return (String.valueOf(sqlQuery.uniqueResult()));		
	}

	//获取所有在库的联系单ID
	public List<String> getAllEnterSorckId() {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		sb.append("select LINK_ID from BIS_ENTER_STOCK be where be.ITEM_NUM in \r\n" + 
				"	(\r\n" + 
				"select DISTINCT(BILL_NUM) from BIS_TRAY_INFO tray\r\n" + 
				"WHERE\r\n" + 
				"		1 = 1 \r\n" + 
				"		AND ( tray.cargo_state = '01' OR tray.cargo_state = '10' ) \r\n" + 
				"		AND tray.now_piece != 0\r\n" + 
				"		) \r\n" + 
				"		AND be.DEL_FLAG = 0\r\n" + 
				"		AND be.IF_BONDED = 0\r\n" +
				"");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.list();
	}

	public List<String> getAllLinkIdForBuildMani() {
		StringBuffer sb=new StringBuffer();
		sb.append("select LINK_ID from (\r\n" + 
				"select infos.LINK_ID,COUNT(infos.LINK_ID) counts from (\r\n" + 
				"\r\n" + 
				"select info.LINK_ID,info.CTN_NUM,COUNT(info.CTN_NUM) counts  from BIS_ENTER_STOCK_INFO info\r\n" + 
				"where info.LINK_ID in (select LINK_ID from FLJG_APPR_HEAD t where t.IO_TYPE = 1 and t.APPR_TYPE = 3 and t.EMS_NO = 'NH4230210001' and t.appr_id is not null)\r\n" + 
				"GROUP BY info.LINK_ID,info.CTN_NUM ) infos\r\n" + 
				"\r\n" + 
				"GROUP BY infos.LINK_ID ) res\r\n" + 
				"where res.counts = 1\r\n" + 
				"");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.list();
	}

	public List<String> findAllBatchApplyApprID() {
		StringBuffer sb=new StringBuffer();
		sb.append("select id from FLJG_APPR_HEAD where APPR_ID is null and EMS_NO = 'NH4230210001'");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.list();
	}

	public void deleteById(String id) {
		// TODO Auto-generated method stub
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		String sql = "delete FLJG_APPR_HEAD where ID = :id";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
	}
	
}

