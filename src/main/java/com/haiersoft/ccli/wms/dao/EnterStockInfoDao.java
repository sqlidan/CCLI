package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;

/**
 * 
 * @author pyl
 * @ClassName: EnterStockInfoDao
 * @Description: 入库联系单明细DAO
 * @date 2016年2月25日 下午4:57:34
 */
@Repository
public class EnterStockInfoDao extends HibernateDao<BisEnterStockInfo, Integer>{
	
	
	//返回对象
	public List<BisEnterStockInfo> getEnterInfo(String linkId,String billNum,String ctnNum){
	 	return find(Restrictions.and(Restrictions.eq("linkId", linkId),Restrictions.eq("itemNum", billNum),Restrictions.eq("ctnNum", ctnNum)));
	}
	//批量删除明细
	public void deleteInfo(List<Integer> id) {
		Map<String,Object> params = new HashMap<String,Object>();
		String ids = "(";
		for(int i=0;i<id.size();i++){
			ids += id.get(i).toString()+",";
		}
		ids=ids.substring(0, ids.length()-1);
		ids += ")";
		String sql = "delete from bis_enter_stock_info where id in "+ids;
        SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		
	}

	public void updateBillNum(String linkId, String itemNum) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("itemNum", itemNum);
		params.put("linkId", linkId);
		String sql = "update bis_enter_stock_info set ITEM_NUM = :itemNum where LINK_ID = :linkId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMrList(String linkId) {
		StringBuffer sb=new StringBuffer("select ctn_num,order_num from bis_enter_stock_info where link_id = :linkId group by ctn_num,order_num");
		HashMap<String,Object> param=new HashMap<String,Object>();
		param.put("linkId", linkId);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), param);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getCtnNumList(String linkId,String type){
		StringBuffer sb=new StringBuffer();
		HashMap<String,Object> param=new HashMap<String,Object>();
		sb.append(" SELECT                           ");
		sb.append("   T.LINK_ID,                     ");
		sb.append("   T .CTN_NUM                     ");
		sb.append(" FROM                             ");
		sb.append(" 	BIS_ENTER_STOCK_INFO T       ");
		sb.append(" WHERE                            ");
		sb.append(" 	LINK_ID=:linkId              ");
		if (null != type && !"".equals(type)) {
			switch (type) {
				case "cyzy":// 查验作业费
					sb.append(" AND T .IF_CHECK ='1'     ");
					break;
				case "cm":// 缠膜
					sb.append(" AND T .IF_WRAP ='1'     ");
					break;
				case "td":// 套袋
					sb.append(" AND T .IF_BAGGING ='1'     ");
					break;
				case "daox":// 是否倒箱
					sb.append(" AND T .IF_BACK ='1'     ");
					break;
				case "dx":// 是否倒箱
					sb.append(" AND T .IF_BACK ='1'     ");
					break;
				default:// 其他比如派车
					sb.append(" AND T .FEECODE =:type        ");
					param.put("type", type);
					break;
			}
		}
		sb.append(" GROUP BY                         ");
		sb.append("    T.LINK_ID,                    ");
		sb.append("    T .CTN_NUM                    ");
		param.put("linkId", linkId);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), param);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	
	/**
	 * 
	 * @author PYL
	 * @Description: 人库联系单明细导出
	 * @date 2016年6月21日 下午2:50:18 
	 * @param linkId
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> searchInfo(String linkId) {
		StringBuffer sql = new StringBuffer(""
				+ " SELECT "
				+ " t.CARGO_NAME, "
				+ " t.CTN_NUM, "
				+ " t.TYPE_SIZE, "
				+ " t.PIECE, "
				+ " t.NET_WEIGHT, "
				+ " t.GROSS_WEIGHT "
				+ " FROM "
				+ " BIS_ENTER_STOCK_INFO t "
                + " WHERE t.LINK_ID = ?0 " 
                + " ORDER BY t.ID DESC ");
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), linkId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getctNum(String linkId) {
		HashMap<String,Object> param=new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer(""
				+ " SELECT  DISTINCT"
				+ " t.CTN_NUM  as ctNum"
				+ " FROM "
				+ " BIS_ENTER_STOCK_INFO t where T.LINK_ID=:linkId ");
		param.put("linkId", linkId);
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), param);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
	
}
