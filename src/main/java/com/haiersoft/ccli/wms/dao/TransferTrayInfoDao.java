package com.haiersoft.ccli.wms.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.BisTransferStockTrayInfo;
/**
 * 货转托盘明细
 * @author LZG
 *
 */
@Repository
public class TransferTrayInfoDao  extends HibernateDao<BisTransferStockTrayInfo, String> {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLastTransfer(String trayCode) {
		StringBuffer sb=new StringBuffer();
		sb.append("select tr.* ");
		sb.append("from bis_transfer_stock tr ");
		sb.append("where tr.transfer_id in ");
		sb.append("(select tt.transfer_link_id ");
		sb.append("from bis_transfer_stock_trayinfo tt ");
		sb.append("where tt.trayinfoid in ");
		sb.append("(select t.id from bis_tray_info t where t.tray_id = ?0)) ");
		sb.append("order by tr.operate_time desc ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), trayCode);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	 
}
