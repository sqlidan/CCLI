package com.haiersoft.ccli.supervision.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.supervision.entity.ManiHead;

@Repository
public class ManiHeadDao extends HibernateDao<ManiHead, String> {

	public void updateLocalStatusbyId(String status, String id) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		params.put("status", status);
		String sql = "update FLJG_MANI_HEAD set LOCAL_STATUS = :status where ID = :id ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();		
	}

	public void updateManiFestIdbyId(String maniFestId, String localStatus, String id) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("maniFestId", maniFestId);
		params.put("id", id);
		params.put("localStatus", localStatus);
		String sql = "update FLJG_MANI_HEAD set MANIFEST_ID = :maniFestId, LOCAL_STATUS = :localStatus where ID = :id ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
		
	}
	
	
	/**
	 * 查询所有的Manifest
	 * @param 
	 * @return 结果集合
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllManifestID(){
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT MANIFEST_ID FROM FLJG_MANI_HEAD where MANIFEST_ID is not null");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.list();
	}

	public void updateByManiId(ManiHead maniHead) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "update FLJG_MANI_HEAD set "
        		+ "TRADE_CODE = :tradeCode, "
         		+ "TRADE_NAME = :tradeName, "
        		+ "CUSTOMS_CODE = :customsCode, "
        		+ "STATUS = :status, "
        		+ "PASS_STATUS = :passStatus, "
        		+ "INPUTER = :inputEr, "
        		+ "INPUT_DATE = :inputDate, "
        		+ "D_DATE = :dDate, "
        		+ "APPR_DATE = :apprDate, "
        		+ "APPR_NOTE = :apprNote, "
        		+ "ORG_CODE = :orgCode, "
        		+ "PASSPORT_DATE = :passportDate, "
        		+ "DECL_TYPE = :declType, "
        		+ "SEQ_NO = :seqNo, "
        		+ "CUS_STATUS = :cusStatus, "
        		+ "CUSR_MK = :cusRmk "
        		+ "where MANIFEST_ID = :manifestId ";        
        params.put("tradeCode",maniHead.getTradeCode());
        params.put("tradeName",maniHead.getTradeName());
        params.put("customsCode",maniHead.getCustomsCode());
        params.put("status",maniHead.getStatus());
        params.put("passStatus",maniHead.getPassStatus()); 
        params.put("inputEr",maniHead.getInputEr());
        params.put("inputDate",maniHead.getInputDate());
        params.put("dDate",maniHead.getDDate());
        params.put("apprDate",maniHead.getApprDate()); 
        params.put("apprNote",maniHead.getApprNote());
        params.put("orgCode",maniHead.getOrgCode());
        params.put("passportDate",maniHead.getPassportDate());
        params.put("declType",maniHead.getDeclType()); 
        params.put("seqNo",maniHead.getSeqNo());
        params.put("cusStatus",maniHead.getCusStatus());
        params.put("cusRmk",maniHead.getCusRmk());
        params.put("manifestId",maniHead.getManifestId());
        
        SQLQuery sqlQuery = createSQLQuery(sql, params);

        sqlQuery.executeUpdate();
		
	}

	public List<String> findMainIdsToConfirmGoods() {
		StringBuffer sb=new StringBuffer();
		sb.append("select mh.id from FLJG_MANI_HEAD mh \r\n" + 
				"right join (select * from  FLJG_MANI_INFO mi where mi.CONFIRM_QTY is not null ) mi2\r\n" + 
				"on mh.id = mi2.HEAD_ID\r\n" + 
				"where mh.MANI_CONFIRM_STATUS = 'N' and mh.IE_FLAG = 'I' and mh.EMPTY_FLAG = 'N' and mh.PASS_STATUS != 2\r\n" + 
				"");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.list();
	}

	public List<String> findMainIdsToUpdate() {
		StringBuffer sb=new StringBuffer();
		sb.append("select id from FLJG_MANI_HEAD mh where mh.MANI_CONFIRM_STATUS = 'N' and mh.IE_FLAG = 'I' and mh.EMPTY_FLAG = 'N' and mh.MANIFEST_ID is null");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.list();
	}

}
