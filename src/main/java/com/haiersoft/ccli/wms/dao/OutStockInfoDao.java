package com.haiersoft.ccli.wms.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;

/**
 * 
 * @author pyl
 * @ClassName: OutStockInfoDao
 * @Description: 出库联系单明细DAO
 * @date 2016年3月11日 下午4:15:34
 */
@Repository
public class OutStockInfoDao extends HibernateDao<BisOutStockInfo, Integer>{
	
	
	public Integer updateByLinkId(BisOutStockInfo outStockInfo){
 	        Map<String,Object> params = new HashMap<String,Object>();
		    StringBuffer sql=new StringBuffer();
	 	    sql.append("update BIS_OUT_STOCK_INFO set  ");
			if(null!=outStockInfo.getOutNum()){
				sql.append(" OUT_NUM=:outNum ");
				params.put("outNum",outStockInfo.getOutNum());
			}
			if(null!=outStockInfo.getNetWeight()){
				sql.append(" ,NET_WEIGHT=:netWeight ");
				params.put("netWeight",outStockInfo.getNetWeight());
			}
			if(null!=outStockInfo.getGrossWeight()){
				sql.append(" ,GROSS_WEIGHT=:grossWeight ");
				params.put("grossWeight",outStockInfo.getGrossWeight());
			}
			if(null!=outStockInfo.getSalesNum()){
				sql.append(" ,SALES_NUM=:salesNum       ");
				params.put("salesNum",outStockInfo.getSalesNum());
			}
			if(null!=outStockInfo.getCodeNum()){
				sql.append(" ,CODE_NUM=:codeNum  ");
				params.put("codeNum",outStockInfo.getCodeNum());
			}
			sql.append(" where 1=1 "); 
			if(null!=outStockInfo.getId()){
				sql.append(" and ID=:id "); 
				params.put("id",outStockInfo.getId());
			}
			if(null!=outStockInfo.getOutLinkId()){
				sql.append(" and OUT_LINK_ID=:outLinkId "); 
				params.put("outLinkId",outStockInfo.getOutLinkId());
			}
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), params);
	 	    return sqlQuery.executeUpdate();
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得出库单明细实体
	 * @date 2018年11月14日 下午9:34 
	 * @param outStockInfo
	 * @return
	 * @throws
	 */
	public BisOutStockInfo getByLinkId(Integer id,String outLinkId){
	   StringBuffer sql=new StringBuffer();
 	   sql.append("FROM BisOutStockInfo outStock where outStock.id=? and outStock.outLinkId=?");
 	   Query query= createQuery(sql.toString());
 	   query.setInteger(0,id);
 	   query.setString(1,outLinkId);
 	   BisOutStockInfo outStockInfo=(BisOutStockInfo)query.uniqueResult();
	   return outStockInfo;
	}
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得提单数据
	 * @date 2016年5月4日 下午4:50:18 
	 * @param outStockInfo
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
    public List<BisOutStockInfo> searchBillCodeByLinkId(BisOutStockInfo outStockInfo){
		StringBuffer sb = new StringBuffer("SELECT o.bill_num AS billNum FROM bis_out_stock_info o WHERE o.out_link_id = ?0 GROUP BY o.bill_num");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), outStockInfo.getOutLinkId());
		sqlQuery.addScalar("billNum", StandardBasicTypes.STRING);//提单号
		sqlQuery.setResultTransformer(Transformers.aliasToBean(BisOutStockInfo.class));
		return sqlQuery.list();
	}

	
	/**
	 * 
	 * @author PYL
	 * @Description: 出库联系单明细导出
	 * @date 2016年6月20日 下午1:50:18 
	 * @param outLinkId
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> searchInfo(String outLinkId) {
		StringBuffer sql = new StringBuffer(""
				+ " SELECT "
				+ " t.BILL_NUM, "
				+ " t.CTN_NUM, "
				+ " t.CARGO_NAME, "
				+ " t.SKU_ID, "
				+ " t.TYPE_SIZE, "
				+ " t.OUT_NUM, "
				+ " t.NET_WEIGHT, "
				+ " t.GROSS_WEIGHT, "
				+ " st.REMARK, "
				+ " sku.PRO_NUM, "
				+ " sku.SHIPNUM, "
				+ " sku.RKDH, "
				+ " t.MSC_NUM,"
				+ " t.LOT_NUM "
				+ " FROM "
				+ " BIS_OUT_STOCK_INFO t "
				+ " LEFT JOIN BIS_OUT_STOCK st "
				+ " on t.OUT_LINK_ID=st.OUT_LINK_ID "
				+ " LEFT JOIN BASE_SKU_BASE_INFO sku "
				+ " on t.SKU_ID = sku.SKU_ID "
                + " WHERE t.OUT_LINK_ID = ?0 " 
                + " ORDER BY t.ID ASC ");
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), outLinkId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}


//	/**
//	 * 
//	 * @author PYL
//	 * @Description: 出库联系单明细导出(带船号项目号)
//	 * @date 2016年6月20日 下午1:50:18 
//	 * @param outLinkId
//	 * @return
//	 * @throws
//	 */
//	public List<Map<String, Object>> searchInfoWith(String outLinkId) {
//		Map<String,Object> params = new HashMap<String, Object>();
//		StringBuffer sql = new StringBuffer(""
//				+ " SELECT "
//				+ " t.BILL_NUM, "
//				+ " t.CTN_NUM, "
//				+ " t.CARGO_NAME, "
//				+ " t.SKU_ID, "
//				+ " t.TYPE_SIZE, "
//				+ " t.OUT_NUM, "
//				+ " t.NET_WEIGHT, "
//				+ " t.GROSS_WEIGHT, "
//				+ " t.REMARK1 "
//				+ " sku.PRO_NUM "
//				+ " sku.SHIPNUM "
//				+ " sku.RKDH "
//				+ " FROM "
//				+ " BIS_OUT_STOCK_INFO t "
//				+ " LEFT JOIN BASE_SKU_BASE_INFO sku "
//				+ " on t.SKU_ID = sku.SKU_ID "
//                + " WHERE t.OUT_LINK_ID = ?0 " 
//                + " ORDER BY t.ID DESC ");
//		SQLQuery sqlQuery = createSQLQuery(sql.toString(), outLinkId);
//		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//		return sqlQuery.list();
//	}
	public Boolean ifConfirm(String getTrayId) {
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select * from BIS_TRAY_INFO where TRAY_ID=:getTrayId");
		
		params.put("getTrayId", getTrayId);
		SQLQuery query = createSQLQuery(sql.toString(), params);
        return query.list().size() <= 0;
	}
	
	
	//根据出库联系单号统计本次出库的各货种小类的件数
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> countsByClasstype(String outLinkId){
		StringBuffer buffer=new StringBuffer();
		buffer.append("select sku.CLASS_TYPE,sum(oif.OUT_NUM) countNum from bis_out_stock_info oif  ");
		buffer.append("left join BASE_SKU_BASE_INFO sku on oif.SKU_ID = sku.SKU_ID ");
		buffer.append("where oif.out_link_id = '"+outLinkId+"'");
		buffer.append("GROUP BY sku.CLASS_TYPE");
		SQLQuery sqlQuery=createSQLQuery(buffer.toString());
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
}
