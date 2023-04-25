package com.haiersoft.ccli.cost.dao;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BisAsnActionLog;

@Repository
public class AsnActionLogDao  extends HibernateDao<BisAsnActionLog, Integer> {

	public void saveTheLog(BisAsnActionLog logObj) {
		 Map<String, Object> params = new HashMap<String, Object>();
		 params.put("id", logObj.getId());
		 params.put("asnId", logObj.getAsnActionId());
		 params.put("operateTime", logObj.getOperateTime());
		 params.put("operator", logObj.getOperator());
		 params.put("type", logObj.getType());
		 params.put("linkId", logObj.getLinkId());
		 params.put("linkType", logObj.getLinkType());
		 params.put("asn", logObj.getAsn());
		 params.put("oldPiece", logObj.getOldPiece());
		 params.put("changePiece", logObj.getChangePiece());
		 params.put("nowPiece", logObj.getNowPiece());
		 params.put("netWeight", logObj.getNetWeight());
		 params.put("grossWeight", logObj.getGrossWeight());
		 params.put("remark", logObj.getRemark());
	     String sql = "insert into bis_asn_action_log (id,asn_action_id,type,link_id,link_type,asn,old_piece,change_piece,now_piece,net_weight,gross_weight,operate_time,operator,remark) " +
	     		" values (:id,:asnId,:type,:linkId,:linkType,:asn,:oldPiece,:changePiece,:nowPiece,:netWeight,:grossWeight,:operateTime,:operator,:remark)";
	     SQLQuery logSQLQuery = createSQLQuery(sql, params);
	     logSQLQuery.executeUpdate();
		
	}
	
}
