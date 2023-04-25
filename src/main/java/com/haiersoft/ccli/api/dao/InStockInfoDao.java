package com.haiersoft.ccli.api.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.api.entity.InStockInfo;
import com.haiersoft.ccli.api.entity.InStockInfoSort;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.wms.entity.TrayInfo;

@Repository
public class InStockInfoDao extends HibernateDao<InStockInfo, String> {

	/**
	 * 根据client_code查询ids
	 */
	public String getIds(String clientCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append("	ids ");
		sb.append("FROM");
		sb.append("	base_client_info ");
		sb.append("WHERE");
		sb.append("	client_code = '" + clientCode + "'");
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
		List list = sqlQuery.list();
		String ids = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object object = list.get(i);
				if (null != object) {
					ids = object.toString();
				}
			}
		}
		return ids;
	}

	/**
	 * 根据货主账号或货主名称查询
	 */
	@SuppressWarnings("all")
	public List<BaseClientInfo> getClientInfo(Integer customerNumber, String custName) {
		List<BaseClientInfo> clientList = new ArrayList();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from base_client_info where 1=1 ");
		if (null != customerNumber) {
			sb.append("and ids = " + customerNumber);
		}
		if (StringUtils.isNotBlank(custName)) {
			sb.append("and client_name = '" + custName + "'");
		}
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString()).addEntity(BaseClientInfo.class);
		List clientInfoList = sqlQuery.list();
		if (clientInfoList != null && clientInfoList.size() > 0) {
			for (Object baseClientInfo : clientInfoList) {
				BaseClientInfo client = (BaseClientInfo) baseClientInfo;
				clientList.add(client);
			}
		}
		return clientList;
	}

	/**
	 * 查询历史库存信息
	 */
	@SuppressWarnings("all")
	public List<Map<String, Object>> getHistoryStockInfo(String customerNumber, String itemClass, String enterStockTime,
			String outStockTime, String customerName) {
		List<Map<String, Object>> historyList = new ArrayList();
		Map<String, Object> historyStockMap = new HashMap();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append("	* ");
		sb.append("FROM");
		sb.append("	(");
		sb.append("	SELECT");
		sb.append("		t.stock_in,");
		sb.append("		t.stock_name,");
		sb.append("		s.class_type,");
		sb.append("		s.class_name,");
		sb.append("		t.original_piece,");
		sb.append("		t.net_weight,");
		sb.append("		t.enter_stock_time,");
		sb.append("		t.out_stock_time ");
		sb.append("FROM");
		sb.append("	bis_tray_history_info t");
		sb.append("	LEFT JOIN base_sku_base_info s ON t.sku_id = s.sku_id ");
		sb.append("WHERE");
		if (StringUtils.isNotBlank(customerNumber)) {
			sb.append("		t.stock_in in( " + customerNumber + " )");
		}
		if (StringUtils.isBlank(customerNumber)) {
			if (StringUtils.isNotBlank(customerName)) {
				sb.append("		t.stock_name in( " + customerName + " )");
			}
		}
		sb.append("	) ");
		sb.append("WHERE");
		if (StringUtils.isNotBlank(itemClass)) {
			sb.append("	class_type in( " + itemClass + " )");
		}
		if (StringUtils.isNotBlank(enterStockTime) && StringUtils.isNotBlank(outStockTime)) {
			sb.append("	AND enter_stock_time BETWEEN to_date( '" + enterStockTime + "', 'yyyy-mm-dd hh24:mi:ss' ) ");
			sb.append("	AND to_date( '" + outStockTime + "', 'yyyy-mm-dd hh24:mi:ss' ) ");
		}

		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
		List list = sqlQuery.list();

		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);

				historyStockMap.put("customerNumber", obj[0]);
				historyStockMap.put("customerName", obj[1]);
				historyStockMap.put("itemCLass", obj[2]);
				historyStockMap.put("itemClassName", obj[3]);
				historyStockMap.put("historyAmount", obj[4]);
				historyStockMap.put("historyWeight", obj[5]);
				historyStockMap.put("enterStockTime", obj[6]);
				historyStockMap.put("outStockTime", obj[7]);

				historyList.add(historyStockMap);
			}
		}
		return historyList;
	}

	/**
	 * 查询历史库存
	 */
	public List<?> getStockInfoInPeriod(List<Integer> clientIdList, List<String> itemClass, String dateStart, String dateEnd) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"select ts.stock_in,ts.stock_name,ts.CLASS_NAME,ts.CLASS_TYPE,sum(AMOUNT),sum(WEIGHT),sum(GROSS_SINGLE),ts.INBOUND_DATE from ");
		sb.append(
				"(SELECT ts.stock_in,ts.stock_name,ts.CLASS_NAME,ts.CLASS_TYPE,ts.num AS AMOUNT,ts.num * ts.GROSS_SINGLE AS WEIGHT,ts.GROSS_SINGLE,ts.INBOUND_DATE ");		
		sb.append("FROM (");		
		sb.append(
				" SELECT tray.stock_in, tray.stock_name, tray.asn, s.CLASS_NAME, s.CLASS_TYPE, SUM( tray.original_piece - tray.remove_piece ) AS num, MAX( tray.NET_SINGLE ) AS NET_SINGLE, MAX( tray.GROSS_SINGLE ) AS GROSS_SINGLE, asn.INBOUND_DATE ");		
		sb.append(
				"FROM BIS_TRAY_INFO tray LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id LEFT JOIN BIS_ASN asn ON tray.ASN = asn.ASN ");		
		sb.append("WHERE asn.if_second_enter = '1' 	AND tray.if_transfer = '0' ");
		sb.append("GROUP BY tray.stock_in, tray.stock_name, tray.asn, s.CLASS_NAME, s.CLASS_TYPE, asn.INBOUND_DATE ");
		sb.append("	) ts ");
		sb.append("where 1=1 ");
		if (null !=clientIdList && 0!=clientIdList.size()) {
			String s = "AND ts.STOCK_IN in (";
			for(int i=0; i <clientIdList.size()-1; i++) {
				s= s+"'"+clientIdList.get(i)+"',";
			}
			s=s+"'"+clientIdList.get(clientIdList.size()-1)+"') ";
			sb.append(s);
		}
		if (StringUtils.isNotBlank(dateStart)) {
			sb.append("AND ts.INBOUND_DATE >= to_date( '" + dateStart + "', 'yyyymmdd' ) ");
		}
		if (StringUtils.isNotBlank(dateEnd)) {
			sb.append("AND ts.INBOUND_DATE < to_date( '" + dateEnd + "', 'yyyymmdd' ) +1 ");
		}
		if (null !=itemClass && 0!=itemClass.size()) {
			String s = "AND ts.CLASS_TYPE in (";
			for(int i=0; i <itemClass.size()-1; i++) {
				s= s+"'"+itemClass.get(i)+"',";
			}
			s=s+"'"+itemClass.get(itemClass.size()-1)+"') ";
			sb.append(s);
		}
		sb.append(") ts GROUP BY ts.stock_in, ts.stock_name, ts.CLASS_NAME, ts.CLASS_TYPE, ts.INBOUND_DATE");
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
		return sqlQuery.list();
	}

//	/**
//	 * 查询在库信息
//	 * @throws ParseException 
//	 */
//	@SuppressWarnings("all")
//	public List<InStockInfo> getInStockInfo(Integer id, String[] itemClass, Integer accountId, String[] stockIns, String[] stockNames) throws ParseException{
//		List<InStockInfo> inStockInfoList = new ArrayList();
//		StringBuilder sb = new StringBuilder(); 
//		sb.append("SELECT");
//		sb.append("	b.id,");
//		sb.append("	b.bill_num,");
//		sb.append("	b.ctn_num,");
//		sb.append("	s.class_type,");
//		sb.append("	b.cargo_name,");
//		sb.append("	b.area_num,");
//		sb.append("	b.cargo_state,");
//		sb.append("	b.enter_stock_time,");
//		sb.append("	b.now_piece,");
//		sb.append("	b.net_single,");
//		sb.append("	b.gross_single,");
//		sb.append("	b.net_weight,");
//		sb.append("	b.gross_weight,");
//		sb.append("	a.state,");
//		sb.append("	a.pledge_weight,");
//		sb.append("	a.pledge_number ");
//		sb.append("FROM");
//		sb.append("	bis_tray_info b");
//		sb.append("	LEFT JOIN base_sku_base_info s ON b.sku_id = s.sku_id");
//		sb.append("	LEFT JOIN api_pledge a ON b.id = a.trayinfo_id ");
//		sb.append("WHERE");
//		sb.append("	1 = 1 ");
////		if(null!= accountId) {
////			sb.append("	AND a.account_id = '" + accountId + "'");
////		}
//		if(null != stockIns) {
//			for(String ids : stockIns) {
//				if(StringUtils.isNotBlank(ids)) {
//					sb.append("	AND b.stock_in = '" + ids + "'");
//				}
//			}
//		}
//		if(null != stockNames) {
//			for(String stockName : stockNames) {
//				if(StringUtils.isNotBlank(stockName)) {
//					sb.append("	AND b.stock_name = '" + stockName + "'");
//				}
//			}
//		}
//		if(null != itemClass) {
//			for(String item : itemClass) {
//				if(StringUtils.isNotBlank(item)) {
//					sb.append("	AND s.class_type = '" + item + "'");
//				}
//			}
//		}
//		if(null != id) {
//			sb.append("	AND b.id = '" + id + "' ");
//		}
//		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
//		List list = sqlQuery.list();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		if(list != null && list.size() > 0) {
//			for(int i=0; i<list.size(); i++) {
//				InStockInfo inStockInfo = new InStockInfo();
//				Object[] obj = (Object[])list.get(i);
//				if(null != obj) {
//					if(obj[0] != null) {
//						inStockInfo.setId(Integer.parseInt(obj[0].toString())); 	
//					}
//					if(obj[1] != null) {
//						inStockInfo.setTdh(obj[1].toString());
//					}
//					if(obj[2] != null) {
//						inStockInfo.setXh(obj[2].toString());
//					}
//					if(obj[3] != null) {
//						inStockInfo.setItem(obj[3].toString());
//					}
//					if(obj[4] != null) {
//						inStockInfo.setItemName(obj[4].toString());
//					}
//					if(obj[5] != null) {
//						inStockInfo.setLocation(obj[5].toString());
//					}
//					if(obj[6] != null) {
//						inStockInfo.setItemStatus(obj[6].toString());
//					}
//					if(obj[7] != null) {
//						String strDate = obj[7].toString();
//						inStockInfo.setInstorageDate(sdf.parse(strDate));
//					}
//					if(obj[8] != null) {
//						inStockInfo.setQty(Integer.parseInt(obj[8].toString()));
//					}
//					if(obj[9] != null) {
//						inStockInfo.setNetWeight(Double.parseDouble(obj[9].toString()));
//					}
//					if(obj[10] != null) {
//						inStockInfo.setGrossWeight(Double.parseDouble(obj[10].toString()));
//					}
//					if(obj[11] != null) {
//						inStockInfo.setTotalNetWeight(Double.parseDouble(obj[11].toString()));
//					}
//					if(obj[12] != null) {
//						inStockInfo.setTotalGrossWeight(Double.parseDouble(obj[12].toString()));
//					}
//					if(obj[13] != null) {
//						inStockInfo.setPledge(obj[13].toString());
//					}
//					if(obj[13] == null) {
//						inStockInfo.setPledge("4");
//					}
//					if(obj[14] != null) {
//						inStockInfo.setPledgeWeight(Double.parseDouble(obj[14].toString()));
//					}
//					if(obj[14] == null) {
//						inStockInfo.setPledgeWeight(0.0);
//					}
//					if(obj[15] != null) {
//						inStockInfo.setPledgeAmount(Double.parseDouble(obj[15].toString()));
//					}
//					if(obj[15] == null) {
//						inStockInfo.setPledgeAmount(0.0);
//					}
//				}
//				
//				inStockInfoList.add(inStockInfo);
//			}
//		}
//		return inStockInfoList;
//	}

	/**
	 * 根据货品小类代码查询
	 */
	@SuppressWarnings("all")
	public List<Map<String, Object>> getItemClassInfo(String[] ids) {
		List<Integer> idList = new ArrayList();
		List<String> nameList = new ArrayList();
		if (null != ids) {
			for (String strId : ids) {
				if (StringUtils.isNotBlank(strId)) {
					if (StringUtils.isNumeric(strId)) {
						int id = Integer.parseInt(strId);
						idList.add(id);
					} else if (!StringUtils.isNumeric(strId)) {
						nameList.add(strId);
					}
				}
			}
		}
		List<Map<String, Object>> itemClassList = new ArrayList();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append("	id,");
		sb.append("	pname ");
		sb.append("FROM");
		sb.append("	base_product_calss ");
		sb.append("WHERE");
		if (idList.size() == 1) {
			sb.append("	id = " + idList.get(0) + "");
		}
		if (idList.size() > 1) {
			sb.append("	id = " + idList.get(0) + "");
			for (int i = 1; i < idList.size(); i++) {
				sb.append("	OR id = " + idList.get(i) + " ");
			}
		}
		if (null != nameList) {
			if (nameList.size() == 1) {
				sb.append("	pname = '" + nameList.get(0) + "'");
			}
			if (nameList.size() > 1) {
				sb.append("	pname = '" + nameList.get(0) + "'");
				for (int i = 1; i < idList.size(); i++) {
					sb.append("	OR pname = '" + nameList.get(i) + "' ");
				}
			}
		}
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
		List list = sqlQuery.list();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				Map<String, Object> map = new HashMap();
				map.put("item", obj[0]);
				map.put("itemName", obj[1]);
				itemClassList.add(map);
			}
		}
		return itemClassList;
	}

	/**
	 * 动态质押查询
	 */
	@SuppressWarnings("all")
	public List<Map<String, Object>> getDynamicPledge(String customerNumber, String itemClass,
			Map<String, Object> params) {
		List<Map<String, Object>> dynamicPledgeList = new ArrayList();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT");
		sb.append("	p.item_class,");
		sb.append("	p.customer_code,");
		sb.append("	c.client_name ");
		sb.append("FROM");
		sb.append("	api_pledge p");
		sb.append("	LEFT JOIN base_client_info c ON p.customer_code = c.client_code ");
		sb.append("WHERE");
		sb.append("	p.customer_code = '" + customerNumber + "' ");
		sb.append("	AND p.item_class in( " + itemClass + ")");
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				Map<String, Object> dynamicPledgeMap = new HashMap();
				if (obj[1] != null) {
					if (params.get("itemClass").equals(obj[0].toString())) {
						dynamicPledgeMap.put("id", params.get("id"));
						dynamicPledgeMap.put("item", obj[0]);
						dynamicPledgeMap.put("customerNumber", obj[1]);
						dynamicPledgeMap.put("itemName", params.get("itemName"));
						dynamicPledgeMap.put("lessAmount", params.get("lessAmount"));
						dynamicPledgeMap.put("lessWeight", params.get("lessWeight"));
						dynamicPledgeMap.put("pledge", obj[2]);
					}
					dynamicPledgeList.add(dynamicPledgeMap);
				}
			}
		}

		return dynamicPledgeList;
	}

	/**
	 * 查询最小id
	 */
	public Map<String, Object> getSmallId(String customerNumber, String itemClass) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> idMap = new HashMap();

		sb.append("SELECT");
		sb.append("	id ");
		sb.append("FROM");
		sb.append("	api_pledge ");
		sb.append("WHERE");
		sb.append("	customer_code = '" + customerNumber + "' ");
		sb.append("	AND item_class = " + itemClass + " ");
		sb.append("ORDER BY");
		sb.append("	id ASC");
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
		List list = sqlQuery.list();
		for (int i = 0; i < list.size(); i++) {
			Object object = list.get(0);
			if (object != null) {
				idMap.put("id", object);
				break;
			}
		}

		return idMap;
	}

	/**
	 * 拼接in参数
	 * 
	 * @param param
	 * @return
	 */
	public String getInParam(List<String> param) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < param.size(); i++) {
			if (null != param.get(i)) {
				if (i == param.size() - 1) {
					sb.append("'" + param.get(i) + "'");
				} else {
					sb.append("'" + param.get(i) + "'" + " , ");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 查询在库信息
	 * 
	 * @throws ParseException
	 */
	@SuppressWarnings("all")
	public List<InStockInfo> getInStockInfo(List<String> itemClass, List<Integer> stockIns, String id,InStockInfoSort inStockInfoSort) {
		List<InStockInfo> inStockInfoList = new ArrayList();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append("	t.id,");
		sb.append("	t.bill_num,");
		sb.append("	t.ctn_num,");
		sb.append("	s.class_type,");
		sb.append("	t.cargo_name,");
		sb.append("	t.area_num,");
		sb.append("	t.cargo_state,");
		sb.append("	t.enter_stock_time,");
		sb.append("	t.now_piece,");
		sb.append("	t.net_single,");
		sb.append("	t.gross_single,");
		sb.append("	t.net_weight,");
		sb.append("	t.gross_weight, ");
		sb.append("	t.pledge_piece,");
		sb.append("	t.pledge_gross_weight ");
		sb.append("FROM");
		sb.append("	bis_tray_info t");
		sb.append("	LEFT JOIN base_sku_base_info s ON t.sku_id = s.sku_id ");
		sb.append("WHERE");
		sb.append("	1 = 1 ");
		if (null !=stockIns && 0!= stockIns.size()) {
			String s = "AND t.STOCK_IN in (";
			for(int i=0; i <stockIns.size()-1; i++) {
				s= s+"'"+stockIns.get(i)+"',";
			}
			s=s+"'"+stockIns.get(stockIns.size()-1)+"') ";
			sb.append(s);
		}
		sb.append("	AND t.now_piece != 0 ");
		if (StringUtils.isNotBlank(id)) {
			sb.append("	AND t.id = ( " + id + " ) ");
		}
		if (null !=itemClass && 0!= itemClass.size()) {
			String s = "AND s.class_type IN (";
			for(int i=0; i <itemClass.size()-1; i++) {
				s= s+"'"+itemClass.get(i)+"',";
			}
			s=s+"'"+itemClass.get(itemClass.size()-1)+"') ";
			sb.append(s);
		}
		sb.append("	AND t.if_transfer in( '0' , '3' )");
		if(StringUtils.isNotBlank(inStockInfoSort.getSortField())) {
			sb.append("	ORDER BY " +inStockInfoSort.getSortField()+" ");
		}
		if(StringUtils.isNotBlank(inStockInfoSort.getSortOrder())) {
			sb.append(inStockInfoSort.getSortOrder());
		}
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append("SELECT * ");
		sb2.append(" FROM ( SELECT e.*,ROWNUM r FROM ( ");
		sb2.append(sb);
		sb2.append(" )e WHERE ROWNUM <= "+inStockInfoSort.getPageSize()+" * "+inStockInfoSort.getPageNum()+" ) t ");
		sb2.append(" WHERE ");
		sb2.append(" r > "+inStockInfoSort.getPageSize()+ " * "+inStockInfoSort.getPageNum()+" - "+inStockInfoSort.getPageSize());
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb2.toString());
		List list = sqlQuery.list();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (list != null && list.size() > 0) {

			for (int i = 0; i < list.size(); i++) {
				Integer nowPiece = null;
				//Integer freePiece = null; // 剩余数量
				Integer pledgedPiece = null; // 已质押数量
				InStockInfo inStockInfo = new InStockInfo();
				Object[] obj = (Object[]) list.get(i);
				if (null != obj) {
					// String iii = obj[0].toString();
					if (obj[0] != null) {
						inStockInfo.setId(obj[0].toString());
					}
					if (obj[1] != null) {
						inStockInfo.setTdh(obj[1].toString());
					}
					if (obj[2] != null) {
						inStockInfo.setXh(obj[2].toString());
					}
					if (obj[3] != null) {
						inStockInfo.setItem(obj[3].toString());
					}
					if (obj[4] != null) {
						inStockInfo.setItemName(obj[4].toString());
					}
					if (obj[5] != null) {
						inStockInfo.setLocation(obj[5].toString());
					}
					if (obj[6] != null) {
						inStockInfo.setItemStatus(obj[6].toString());
					}
					if (obj[7] != null) {
						inStockInfo.setInstorageDate(obj[7].toString());
					}

					if (obj[8] != null) {
						nowPiece = Integer.parseInt(obj[8].toString());
						inStockInfo.setQty(nowPiece);
					}
					if (obj[9] != null) {
						inStockInfo.setNetWeight(Double.parseDouble(obj[9].toString()));
					}
					if (obj[10] != null) {
						inStockInfo.setGrossWeight(Double.parseDouble(obj[10].toString()));
					}
					if (obj[11] != null) {
						inStockInfo.setTotalNetWeight(Double.parseDouble(obj[11].toString()));
					}
					if (obj[12] != null) {
						inStockInfo.setTotalGrossWeight(Double.parseDouble(obj[12].toString()));
					}
//					if(obj[13] != null) {
//						inStockInfo.setPledge(obj[13].toString());
//					}
//					if(obj[13] == null) {
//						inStockInfo.setPledge("4");
//					}

					if (obj[13] != null) {
						//freePiece = Integer.parseInt(obj[13].toString());
						pledgedPiece = Integer.parseInt(obj[13].toString());
						inStockInfo.setPledgeAmount(Integer.parseInt(obj[13].toString()));
					}
					if (obj[13] == null) {
						inStockInfo.setPledgeAmount(0);
					}
					if (obj[14] != null) {
						inStockInfo.setPledgeWeight(Double.parseDouble(obj[14].toString()));
					}
					if (obj[14] == null) {
						inStockInfo.setPledgeWeight(0.0);
					}
					if (null != nowPiece && null != pledgedPiece) {
						// 如果库存件数 大于 质押件数
						if (nowPiece > pledgedPiece) {
							inStockInfo.setPledge("静态部分质押");
						} 
						if(pledgedPiece == 0 || pledgedPiece < 0 ) {
							inStockInfo.setPledge("无质押");
						} 
						if (nowPiece <= pledgedPiece) {
							inStockInfo.setPledge("静态全部质押");
						}				
					} else {
						inStockInfo.setPledge("无质押");
					}

				}

				inStockInfoList.add(inStockInfo);
			}
		}
		return inStockInfoList;
	}

	/**
	 * 查询动态质押汇总数据
	 */
	public List<Map<String, Object>> getDynamicStatus(String clientCode, String itemClass, boolean flag) {
		List<Map<String, Object>> status2List = new ArrayList();
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT");
		sb.append("	a.item_class,");
		sb.append("	sum( a.pledge_number ) AS pledge_number,");
		sb.append("	sum( a.pledge_weight ) AS pledge_weight ");
		sb.append("FROM");
		sb.append("	api_pledge a ");
		sb.append("WHERE");
		sb.append("	a.customer_code = '" + clientCode + "' ");
		sb.append("	AND a.item_class IN ( " + itemClass + " ) ");
		sb.append("	AND a.confirm_status = 1 ");
		// flag为true查询状态为2数据
		if (flag) {
			sb.append("	AND a.state = 2 ");
		}
		// flag状态为false查询状态为3数据
		if (!flag) {
			sb.append("	AND a.state = 3 ");
		}
		sb.append("GROUP BY ");
		sb.append("	a.item_class");

		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = new HashMap();
				Object[] obj = (Object[]) list.get(i);
				if (obj[0] != null) {
					String itemClass_ = obj[0].toString();
					map.put("itemClass", itemClass_);
				}
				if (obj[1] != null) {
					String totalPledgeNumber = obj[1].toString();
					map.put("pledgeNumber", totalPledgeNumber);
				}
				if (obj[2] != null) {
					String totalPledgeWeight = obj[2].toString();
					map.put("pledgeWeight", totalPledgeWeight);
				}
				status2List.add(map);
			}
		}
		return status2List;
	}
	
	
	public List<Map<String, Object>> dynamicQuery(Integer customerId, String itemClasses){
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("select rn,TREND_ID,CUSTOMER_CODE,ITEM_CLASS,PNAME,PLEDGE_NUMBER,PLEDGE_WEIGHT,CUSTOMER_NAME,RELATED_TREND_ID,COMFIRM_DATE,state from(");
		sb.append(" select ROW_NUMBER() OVER (PARTITION BY RELATED_TREND_ID ORDER BY COMFIRM_DATE DESC)");
		sb.append(" rn,TREND_ID,CUSTOMER_CODE,ITEM_CLASS,PNAME,PLEDGE_NUMBER,PLEDGE_WEIGHT,CUSTOMER_NAME,RELATED_TREND_ID,COMFIRM_DATE,state");
		sb.append(" from");
		sb.append(" API_PLEDGE where");
		sb.append(" state in (2,3) and CONFIRM_STATUS = 1 ");
		if (null !=customerId) {
			sb.append(" and CUSTOMER_ID = "+customerId+"");
		}
		if (StringUtils.isNotBlank(itemClasses)) {
			sb.append("	and ITEM_CLASS in (" + itemClasses + ") ");
		}
		sb.append(" ) where rn = 1");
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
		
		List list = sqlQuery.list();
		List<Map<String, Object>> returnList = new ArrayList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = new HashMap();
				Object[] obj = (Object[]) list.get(i);
				if (obj[1] != null) {
					map.put("id", obj[1].toString());
				}
				if (obj[2] != null) {
					String itemClass_ = obj[1].toString();
					map.put("customerNumber", obj[2].toString());
				}
				if (obj[3] != null) {
					map.put("item", obj[3].toString());
				}
				if (obj[4] != null) {
					
					map.put("itemName", obj[4].toString());
				}
				if (obj[5] != null) {
					
					map.put("lessAmount", obj[5].toString());
				}
				if (obj[6] != null) {
					
					map.put("lessWeight", obj[6].toString());
				}
				if (obj[7] != null) {					
					map.put("pledge", obj[7].toString());
				}
				returnList.add(map);
			}
		}
		return returnList;
		
	}

	public Integer getTotal(List<String> itemClass, List<Integer> stockIns, String id, InStockInfoSort inStockInfoSort) {
		List<InStockInfo> inStockInfoList = new ArrayList();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append("	count(*)");
		sb.append("FROM");
		sb.append("	bis_tray_info t");
		sb.append("	LEFT JOIN base_sku_base_info s ON t.sku_id = s.sku_id ");
		sb.append("WHERE");
		sb.append("	1 = 1 ");
		if (null !=stockIns && 0!= stockIns.size()) {
			String s = "AND t.STOCK_IN in (";
			for(int i=0; i <stockIns.size()-1; i++) {
				s= s+"'"+stockIns.get(i)+"',";
			}
			s=s+"'"+stockIns.get(stockIns.size()-1)+"') ";
			sb.append(s);
		}
		sb.append("	AND t.now_piece != 0 ");
		if (StringUtils.isNotBlank(id)) {
			sb.append("	AND t.id = ( " + id + " ) ");
		}
		if (null !=itemClass && 0!= itemClass.size()) {
			String s = "AND s.class_type IN (";
			for(int i=0; i <itemClass.size()-1; i++) {
				s= s+"'"+itemClass.get(i)+"',";
			}
			s=s+"'"+itemClass.get(itemClass.size()-1)+"') ";
			sb.append(s);
		}
		sb.append("	AND t.if_transfer in( '0' , '3' )");
		
		
		SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
		Integer count = ((BigDecimal) sqlQuery.uniqueResult()).intValue();
		return count;
	}
}
