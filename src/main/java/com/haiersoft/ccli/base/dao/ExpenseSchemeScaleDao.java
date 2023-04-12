package com.haiersoft.ccli.base.dao;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BisExpenseSchemeScale;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
/**
 * 
 * @author PYl
 * @ClassName: ExpenseSchemeScaleDao
 * @Description: 费用分摊比例DAO
 * @date 2016年2月28日 下午3:28:52
 */
@Repository
public class ExpenseSchemeScaleDao extends HibernateDao<BisExpenseSchemeScale, Integer>{
	
	
	
	@SuppressWarnings("unchecked")
	public List<BisExpenseSchemeScale> getSchemeScale(String linkId,String feeCode,String custId){
		   StringBuffer sql=new StringBuffer();
	 	   sql.append("FROM BisExpenseSchemeScale book where book.linkId=? and book.feeCode=? and book.customsId=? ");
	 	   Query query= createQuery(sql.toString());
	 	   query.setString(0,linkId);
	 	   query.setString(1,feeCode);
	 	   query.setString(2,custId);
		   List<BisExpenseSchemeScale> books=query.list();
	 	   return books;
	}
	/**
	 * 
	 * @param linkId
	 * @param feeCode
	 * @param bosSign
	 * @param standingNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BisExpenseSchemeScale> getSchemeScale(String linkId,String feeCode,String bosSign,String standingNum){
		   StringBuffer sql=new StringBuffer();
	 	   sql.append("FROM BisExpenseSchemeScale book where book.linkId=? and book.feeCode=? and book.bosSign=? and book.standingNum=? ");
	 	   Query query= createQuery(sql.toString());
	 	   query.setString(0,linkId);
	 	   query.setString(1,feeCode);
	 	   query.setString(2,bosSign);
	 	   query.setString(3,standingNum);
		   List<BisExpenseSchemeScale> books=query.list();
	 	   return books;
	}
	/**
	 * 根据联系单号删除所有明细
	 * @param asnId
	 * @return
	 */
	public void deleteExpenseSchemeScaleInfos(String linkId){
		if(linkId!=null && !"".equals(linkId)){
			String hql="delete BisExpenseSchemeScale a where a.linkId=?0";
			batchExecute(hql, linkId);
		}
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description:  费用分摊  发货方  分页查询
	 * @date 2016年4月18日 下午4:41:52 
	 * @param page
	 * @param scale
	 * @return
	 * @throws
	 */
	public Page<BisExpenseSchemeScale> searchToCastShare(Page<BisExpenseSchemeScale> page, BisExpenseSchemeScale scale){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sql = this.toCostSql();
		sql.append(" AND ES.BOS_SIGN='0' AND ES.LINK_ID=:linkId ");
		params.put("linkId",scale.getLinkId());
		return findPageSql(page, sql.toString(),this.toCostParm(), params);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 费用分摊  发货方  list集合查询
	 * @date 2016年4月19日 下午4:05:01 
	 * @param scale
	 * @return
	 * @throws
	 */
	public List<BisExpenseSchemeScale> searchToCastShare(BisExpenseSchemeScale scale){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sql = this.toCostSql();
		sql.append(" AND ES.BOS_SIGN='0' AND ES.LINK_ID=:linkId ");
		params.put("linkId", scale.getLinkId());
		return findSql(sql.toString(), this.toCostParm(), params);
	}
	
	/**
	 *  SQL 发货方查询
	 */
	public StringBuffer toCostSql(){
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT ");  
		sb.append("   ES.ID, ");
		sb.append("   ES.fentan, ");
		sb.append("   ES.STANDING_NUM AS standingNum, ");
		sb.append("   ES.SHARE_SIGN AS shareSign, ");
		sb.append("   sb.Examine_Sign AS examineSign,");
		sb.append("   es.bos_sign AS bosSign,");
		sb.append("   es.if_ratio AS ifRatio, ");
		sb.append("   sb.asn AS asn,  ");
		sb.append("   es.fee_code AS feeCode, ");
		sb.append("   es.fee_name AS feeName, ");
		sb.append("   ES.CUSTOMS_ID AS customsId, ");
		sb.append("   ES.CUSTOMS_NAME AS customsName, ");
		sb.append("   sb.num AS num, ");
		sb.append("   sb.price AS price, ");
		sb.append("   sb.Receive_Amount AS receiveAmount,");
		sb.append("   ES.FENTAN_AMOUNT AS fentanAmount,");
		sb.append("   sb.Currency AS currency, ");
		sb.append("   sb.Exchange_Rate AS exchangeRate,");
		sb.append("   sb.bill_date AS billDate, ");
		sb.append("   sb.fill_sign AS fillSign, ");
		sb.append("   (CASE ES.BOS_SIGN WHEN '0' THEN  ");
		sb.append(" 	SB.LINK_ID  ");
		sb.append("    ELSE  ");
		sb.append(" 	ES.LINK_ID  ");
		sb.append("   END) AS linkId, ");
		sb.append("   ES.bill_num AS billNum ");
		sb.append(" FROM  ");
		sb.append(" 	bis_expense_scheme_scale es ");
		sb.append(" LEFT JOIN  ");
		sb.append("   bis_standing_book sb ON ES.STANDING_NUM=sb.STANDING_NUM      ");
		sb.append(" WHERE ");
		sb.append("   1=1  ");
		
		/*StringBuffer sql = new StringBuffer(""
				+ " SELECT "
				+ " es.id AS id, "
				+ " sb.standing_num AS standingNum, "
				+ " (CASE "
				+ "	      WHEN instr(sb.share_link, :linkId) > 0 THEN 1 "
				+ "	      ELSE 0 "
				+ "	END) AS shareSign, "
                + "	sb.Examine_Sign AS examineSign,"
                + " es.bos_sign AS bosSign, "
                + " es.if_ratio AS ifRatio, "
                + " sb.asn AS asn, "
                + " sb.fee_code AS feeCode, "
                + " sb.fee_name AS feeName, "
                + " sb.customs_num AS customsId, "
                + " sb.customs_name AS customsName, "
                + " sb.num AS num, "
                + " sb.price AS price, "
                + " sb.Receive_Amount AS receiveAmount, "
                + " sb.Currency AS currency, "
                + " sb.Exchange_Rate AS exchangeRate, "
                + " sb.bill_date AS billDate, "
                + " sb.fill_sign AS fillSign, "
                + " e.link_id AS linkId, "
                + " oo.bill_num AS billNum "
                + " FROM bis_expense_scheme_scale es "
                + " LEFT JOIN ( "
                + "    SELECT o.bill_num, o.out_link_id FROM bis_out_stock_info o "
                + "    GROUP BY o.bill_num, o.out_link_id "
                + " ) oo  ON oo.out_link_id = es.link_id "
                + " LEFT JOIN bis_enter_stock e ON e.item_num = oo.bill_num "
                + " LEFT JOIN bis_standing_book sb ON sb.link_id = e.link_id "
                + " WHERE e.DEL_FLAG = '0' AND sb.if_receive = '1' AND es.fee_code = sb.fee_code ");*/
		return sb;
	}
	
	/**
	 *  MAP 发货方
	 */
	public Map<String, Object> toCostParm(){
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("id", Integer.class);//主键Id
		parm.put("standingNum",String.class);//台账Id
		parm.put("fentan",Double.class);//分摊比例
		parm.put("shareSign",String.class);//分摊标记
		parm.put("examineSign",String.class);//审核标记
		parm.put("bosSign",String.class);//买卖方 0：买方，1卖方
		parm.put("ifRatio",String.class);//是否按比例   （0：不按比例，1：按比例)
		parm.put("asn", String.class);//asn
		parm.put("feeCode", String.class);//费用代码
		parm.put("feeName", String.class);//费用名称
		parm.put("customsId", String.class);//客户Id
		parm.put("customsName", String.class);//客户名称
		parm.put("num", Double.class);//数量
		parm.put("price", Double.class);//单价
		parm.put("receiveAmount", Double.class);//金额
		parm.put("fentanAmount", Double.class);//分摊后金额
		parm.put("currency", String.class);//币种
		parm.put("exchangeRate", Double.class);//汇率
		parm.put("billDate", Date.class);//账单年月
		parm.put("fillSign", String.class);//冲补标记
		parm.put("linkId", String.class);//联系单
		parm.put("billNum", String.class);//提单号
		return parm;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description:  费用分摊  收货方  分页查询
	 * @date 2016年4月19日 上午11:14:49 
	 * @param page
	 * @param scale
	 * @return
	 * @throws
	 */
	public Page<BisExpenseSchemeScale> searchFromCastShare(Page<BisExpenseSchemeScale> page, BisExpenseSchemeScale scale){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sql = this.fromCostSql();
		sql.append(" AND ES.BOS_SIGN='1' AND ES.LINK_ID=:linkId ");
		params.put("linkId", scale.getLinkId());
		return findPageSql(page, sql.toString(), this.fromCostParm(), params);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 费用分摊  收货方 数据集合
	 * @date 2016年4月19日 下午3:41:25 
	 * @param scale
	 * @return
	 * @throws
	 */
	public List<BisExpenseSchemeScale> searchFromCastShare(BisExpenseSchemeScale scale){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sql = this.fromCostSql();
		sql.append(" AND ES.BOS_SIGN='1' AND ES.LINK_ID=:linkId ");
		params.put("linkId", scale.getLinkId());
		return findSql(sql.toString(), this.fromCostParm(), params);
	}

	/**
	 * SQL 收货方查询
	 */
	public StringBuffer fromCostSql(){
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT ");  
		sb.append("   ES.ID, ");
		sb.append("   ES.fentan, ");
		sb.append("   ES.STANDING_NUM AS standingNum, ");
		sb.append("   ES.SHARE_SIGN AS shareSign, ");
		sb.append("   sb.Examine_Sign AS examineSign,");
		sb.append("   es.bos_sign AS bosSign,");
		sb.append("   es.if_ratio AS ifRatio, ");
		sb.append("   sb.asn AS asn,  ");
		sb.append("   es.fee_code AS feeCode, ");
		sb.append("   es.fee_name AS feeName, ");
		sb.append("   ES.CUSTOMS_ID AS customsId, ");
		sb.append("   ES.CUSTOMS_NAME AS customsName, ");
		sb.append("   sb.num AS num, ");
		sb.append("   sb.price AS price, ");
		sb.append("   sb.Receive_Amount AS receiveAmount,");
		sb.append("   ES.FENTAN_AMOUNT AS fentanAmount,");
		sb.append("   sb.Currency AS currency, ");
		sb.append("   sb.Exchange_Rate AS exchangeRate,");
		sb.append("   sb.bill_date AS billDate, ");
		sb.append("   sb.fill_sign AS fillSign, ");
		sb.append("   (CASE ES.BOS_SIGN WHEN '0' THEN  ");
		sb.append(" 	SB.LINK_ID  ");
		sb.append("    ELSE  ");
		sb.append(" 	ES.LINK_ID  ");
		sb.append("   END) AS linkId, ");
		sb.append("   ES.bill_num AS billNum ");
		sb.append(" FROM  ");
		sb.append(" 	bis_expense_scheme_scale es ");
		sb.append(" LEFT JOIN  ");
		sb.append("   bis_standing_book sb ON ES.STANDING_NUM=sb.STANDING_NUM      ");
		sb.append(" WHERE ");
		sb.append("   1=1  ");
/*		StringBuffer sql = new StringBuffer(""
		   + " SELECT "
		   + " es.id AS id, "
		   + " es.share_sign AS shareSign, "
           + " es.bos_sign AS bosSign, "
           + " es.if_ratio AS ifRatio, "
           + " sb.asn AS asn, "
           + " sb.fee_code AS feeCode, "
           + " sb.fee_name AS feeName, "
           + " sb.customs_num AS customsId, "
           + " sb.customs_name AS customsName, "
           + " sb.num AS num, "
           + " sb.price AS price, "
           + " sb.Receive_Amount AS receiveAmount, "
           + " sb.Currency AS currency, "
           + " sb.Exchange_Rate AS exchangeRate, "
           + " sb.bill_date AS billDate, "
           + " sb.fill_sign AS fillSign, " 
           + " es.link_id AS linkId, "
           + " sb.bill_num AS billNum "
           + " FROM bis_expense_scheme_scale es "
           + " LEFT JOIN bis_standing_book sb ON sb.link_id = es.link_id "
           + " WHERE sb.if_receive = '1' AND es.share_sign = '1' "
           + " AND es.fee_code = sb.fee_code ");*/
		return sb;
	}
	
	/**
	 * map  收货方
	 */
	public Map<String, Object> fromCostParm(){
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("id", Integer.class);//主键Id
		parm.put("standingNum",String.class);//台账Id
		parm.put("fentan",Double.class);//分摊比例
		parm.put("shareSign",String.class);//分摊标记
		parm.put("examineSign",String.class);//审核标记
		parm.put("bosSign",String.class);//买卖方 0：买方，1卖方
		parm.put("ifRatio",String.class);//是否按比例   （0：不按比例，1：按比例)
		parm.put("asn", String.class);//asn
		parm.put("feeCode", String.class);//费用代码
		parm.put("feeName", String.class);//费用名称
		parm.put("customsId", String.class);//客户Id
		parm.put("customsName", String.class);//客户名称
		parm.put("num", Double.class);//数量
		parm.put("price", Double.class);//单价
		parm.put("receiveAmount", Double.class);//金额
		parm.put("fentanAmount", Double.class);//分摊后金额
		parm.put("currency", String.class);//币种
		parm.put("exchangeRate", Double.class);//汇率
		parm.put("billDate", Date.class);//账单年月
		parm.put("fillSign", String.class);//冲补标记
		parm.put("linkId", String.class);//联系单
		parm.put("billNum", String.class);//提单号
		return parm;
	}
}
