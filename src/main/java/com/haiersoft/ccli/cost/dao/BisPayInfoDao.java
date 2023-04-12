package com.haiersoft.ccli.cost.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BisPayInfo;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
@Repository
public class BisPayInfoDao  extends HibernateDao<BisPayInfo, Integer> {

	public List<BisPayInfo> findByPayId(String payId) {
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(""
				+ " SELECT "
				+ " c.saler as saler, "
				+ " t.bill_num AS billNum, "
                + " t.client_name as clientName,  "
                + " t.fee_name as feeName, "
                + " t.totel_price as totelPrice, "
                + " t.help_pay    as helpPay, "
                + " t.remark as remark "
                + " FROM BIS_PAY_INFO t "
                + " inner join BASE_CLIENT_INFO c on c.ids=t.client_id "
                + " WHERE t.pay_id = :payId ");
		params.put("payId", payId);
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("billNum", String.class);//提单号
		parm.put("clientName", String.class); 
		parm.put("feeName", String.class); 
		parm.put("totelPrice", Double.class); 
		parm.put("remark", String.class); 
		parm.put("saler", String.class); 
		parm.put("helpPay", Integer.class);
		return findSql(sql.toString(), parm, params);
	}
	/**
	 * 判断提单号下面关联的入库单实体中是否存在费用完成状态
	 * 如果有则提示不能审核，反之可以审核
	 * @param payId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findStock(String payId) {
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
	    sb.append("SELECT                                                  ");
		sb.append("	t.id,                                                  ");
		sb.append("	t.bill_num as billNum                                  ");
		sb.append("FROM                                                    ");
		sb.append("	BIS_PAY_INFO t                                         ");
		sb.append("LEFT JOIN BIS_ENTER_STOCK s ON s.item_num = t.bill_num  ");
		sb.append("WHERE                                                   ");
		sb.append("	t.pay_id = :payId                                      ");
		sb.append("AND s.del_flag = '0'                                    ");
		sb.append("AND s.finish_fee_state = '1'                            ");
		params.put("payId", payId);
		SQLQuery query=createSQLQuery(sb.toString(), params);
		return query.list();
	}
	
	public Integer updateStock(String payId,String state){
		Map<String,Object> params = new HashMap<String, Object>();
		User user = UserUtil.getCurrentUser();
		String userName="1".equals(state)?user.getName():"";
		StringBuffer sb=new StringBuffer();
		sb.append("UPDATE bis_standing_book       ");
		sb.append("SET examine_sign = '"+state+"', ");
		sb.append(" examine_person = '"+userName+"'");
		sb.append("WHERE                          ");
		sb.append("	pay_id IN (                   ");
		sb.append("		SELECT                    ");
		sb.append("			t.id                  ");
		sb.append("		FROM                      ");
		sb.append("			bis_pay_info t        ");
		sb.append("		WHERE                     ");
		sb.append("			pay_id =:payId        ");
		sb.append("	)                             ");
		/*sb.append("AND if_receive = '2'           ");*/
		params.put("payId", payId);
        SQLQuery query=createSQLQuery(sb.toString(), params);
		return query.executeUpdate();
	}
	
	public Integer deleteStock(Integer payInfoId){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append("DELETE FROM bis_standing_book  ");
		sb.append("WHERE                          ");
		sb.append("	pay_id IN (                   ");
		sb.append("		SELECT                    ");
		sb.append("			t.id                  ");
		sb.append("		FROM                      ");
		sb.append("			bis_pay_info t        ");
		sb.append("		WHERE                     ");
		sb.append("			id=:payInfoId         ");
		sb.append("	)                             ");
		params.put("payInfoId", payInfoId);
        SQLQuery query=createSQLQuery(sb.toString(), params);
		return query.executeUpdate();
	}
	
	public Integer updatePayInfo(String payId,String state){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append("UPDATE bis_pay_info       ");
		sb.append("SET check_sign ='"+state+"'");
		sb.append("WHERE pay_id =:payId      ");
		params.put("payId", payId);
        SQLQuery query=createSQLQuery(sb.toString(), params);
		return query.executeUpdate();
	}
	
}
