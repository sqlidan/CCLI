package com.haiersoft.ccli.bounded.dao;

import com.haiersoft.ccli.bounded.web.BondedController;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.report.entity.Stock;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 *
 */
@Repository
public class BaseBoundedDao extends HibernateDao<BaseBounded, String>{
	private static final Logger logger = LoggerFactory.getLogger(BaseBoundedDao.class);

	private static String like(String str) {
        return "%" + str + "%";
    }

	/**
	 * @param page
	 * @Description: 分页查询
	 */
	public Page<BaseBounded> searchBaseBounded(Page<BaseBounded> page, BaseBounded baseBounded) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(""
				+ " SELECT "
				+ " t.CLIENT_ID AS clientId, "
				+ " t.CLIENT_NAME AS clientName, "
				+ " t.BILL_NUM AS billNum, "
				+ " t.CD_NUM AS cdNum, "
				+ " t.CTN_NUM AS ctnNum, "
				+ " t.ITEM_NAME AS itemName, "
				+ " t.PIECE AS piece, "
				+ " t.NET_WEIGHT AS netWeight, "
				+ " t.CUSTOMER_SERVICE_NAME AS customerServiceName, "
				+ " t.HS_CODE AS hsCode, "
				+ " t.HS_ITEMNAME AS hsItemname, "
				+ " t.ACCOUNT_BOOK AS accountBook, "
				+ " t.DCL_QTY AS dclQty, "
				+ " t.DCL_UNIT AS dclUnit, "
				+ " t.HS_QTY AS hsQty, "
				+ " t.TYPE_SIZE AS typeSize, "
				+ " t.CARGO_LOCATION as cargoLocation, "
				+ " t.CARGO_AREA as cargoArea, "
				+ " t.STORAGE_DATE as storageDate, "
				+ " t.CREATED_TIME as createdTime, "
				+ " t.UPDATED_TIME as updatedTime, "
				+ " st.MAKE_TIMES as makeTimes, "
				+ " st.MAKE_TIMEE as makeTimee, "
				+ " TO_NUMBER(sysdate - st.MAKE_TIMES) as day "
				+ " FROM BASE_BOUNDED t "
				+ " LEFT JOIN BIS_ENTER_STOCK st ON T.BILL_NUM=ST.ITEM_NUM "
				+ " WHERE 1 = 1 ");//20170821 增加库存大于0的条件


		if(!StringUtils.isNull(baseBounded.getClientName())){
			sql.append(" and t.CLIENT_NAME like :clientName");
			params.put("clientName", "%"+baseBounded.getClientName()+"%");
		}
		if(!StringUtils.isNull(baseBounded.getBillNum())){
			sql.append(" and t.BILL_NUM like :billNum");
			params.put("billNum", "%"+baseBounded.getBillNum()+"%");
		}
		if(!StringUtils.isNull(baseBounded.getCdNum())){
			sql.append(" and t.CD_NUM like :cdNum");
			params.put("cdNum", "%"+baseBounded.getCdNum()+"%");
		}
		if(!StringUtils.isNull(baseBounded.getCustomerServiceName())){
			sql.append(" and t.CUSTOMER_SERVICE_NAME like :customerServiceName");
			params.put("customerServiceName", "%"+baseBounded.getCustomerServiceName()+"%");
		}
		if(!StringUtils.isNull(baseBounded.getHsCode())){
			sql.append(" and t.HS_CODE like :hsCode");
			params.put("hsCode", "%"+baseBounded.getHsCode()+"%");
		}
		if(!StringUtils.isNull(baseBounded.getHsItemname())){
			sql.append(" and t.HS_ITEMNAME like :hsItemname");
			params.put("hsItemname", "%"+baseBounded.getHsItemname()+"%");
		}
		if(!StringUtils.isNull(baseBounded.getAccountBook())){
			sql.append(" and t.ACCOUNT_BOOK like :accountBook");
			params.put("accountBook", "%"+baseBounded.getAccountBook()+"%");
		}


		//查询对象属性转换
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("clientId", String.class);
		parm.put("clientName", String.class);
		parm.put("billNum", String.class);
		parm.put("cdNum", String.class);
		parm.put("ctnNum", String.class);
		parm.put("itemName", String.class);
		parm.put("piece", Integer.class);
		parm.put("netWeight", Double.class);
		parm.put("customerServiceName", String.class);
		parm.put("hsCode", String.class);
		parm.put("hsItemname", String.class);
		parm.put("accountBook", String.class);
		parm.put("dclQty", Double.class);
		parm.put("dclUnit", String.class);
		parm.put("hsQty", Double.class);
		parm.put("typeSize", String.class);
		parm.put("cargoLocation", String.class);
		parm.put("cargoArea", String.class);
		parm.put("storageDate", Date.class);
		parm.put("createdTime", Date.class);
		parm.put("updatedTime", Date.class);
		parm.put("makeTimes", Date.class);
		parm.put("makeTimee", Date.class);
		parm.put("day", Integer.class);

		return findPageSql(page, sql.toString(), parm, params);
	}

/*	public Page<PlatformUser> seachCustomsClearanceSql(Page<PlatformUser> page, PlatformUser customsClearance) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("select t.* from PLATFORM_GROUP_MANAGE t where 1=1");

        Map<String, Object> queryParams = new HashMap<>();
        if (StringUtils.nonNull(customsClearance.getPlatformNo())) {
        	buffer.append(" and t.GROUP_NAME like :groupName");
            queryParams.put("groupName",like(customsClearance.getPlatformNo()));
        }
        


		buffer.append(" order by t.CREATED_TIME desc");
//        Map<String, Object> paramType = new HashMap<>();
//        paramType.put("cdNum", String.class);
//		return findPageSql(page, buffer.toString(),paramType, queryParams);
        return findPageSql(page, buffer.toString(),queryParams);
	}*/


	public List<Map<String, Object>> getLoactionsByBillNum(BaseBounded baseBounded) {
		StringBuffer buffer = new StringBuffer();
		Map<String, Object> queryParams = new HashMap<>();

		buffer.append("select to_char(wm_concat(  locationCode)) locationCode ,to_char(wm_concat( areaNum)) areaNum from (\n" +
				"\n" +
				"select  DISTINCT locationCode,areaNum from (\n" +
				"\n" +
				"\t\tSELECT\n" +
				"\t\t\ttray.stock_in AS clientId,\n" +
				"\t\t\ttray.stock_name AS clientName,\n" +
				"\t\t\t( CASE st.IF_BONDED WHEN '1' THEN '1' ELSE '0' END ) AS isBonded,\n" +
				"\t\t\ttray.bill_num AS billCode,\n" +
				"\t\t\ttray.ctn_num AS ctnNum,\n" +
				"\t\t\ttray.STOREROOM_NUM AS storeroomNum,\n" +
				"\t\t\tnvl( info.bgdh, st.bgdh ) AS bgdh,\n" +
				"\t\t\tnvl( info.ycg, st.ycg ) AS ycg,\n" +
				"\t\t\tTO_CHAR( nvl( info.BGDHDATE, st.BGDHDATE ), 'yyyy-mm-dd' ) AS bgdhdate,\n" +
				"\t\t\ttray.sku_id AS sku,\n" +
				"\t\t\tst.CTN_TYPE_SIZE AS cz,\n" +
				"\t\t\ts.type_name AS bigName,\n" +
				"\t\t\ts.cargo_type AS bigType,\n" +
				"\t\t\ts.class_name AS simName,\n" +
				"\t\t\ts.class_type AS simType,\n" +
				"\t\t\ttray.CARGO_NAME AS cargoName,\n" +
				"\t\t\ttray.rkTime,\n" +
				"\t\t\ttray.enter_state AS state,\n" +
				"\t\t\tSUM( tray.num ) AS nowNum,\n" +
				"\t\t\tSUM( tray.net_weight ) AS allnet,\n" +
				"\t\t\tSUM( tray.gross_weight ) AS allgross,\n" +
				"\t\t\ttray.CONTACT_NUM AS contactCode,\n" +
				"\t\t\tst.OPERATOR AS createUser,\n" +
				"\t\t\ttray.asn,\n" +
				"\t\t\ttray.cargo_location AS locationCode,\n" +
				"\t\t\ttray.AREA_NUM as areaNum,\n" +
				"\t\t\tdecode( info.HS_CODE, NULL, bai.HS_CODE, info.HS_CODE ) AS hsCode,\n" +
				"\t\t\tdecode( info.ACCOUNT_BOOK, NULL, bai.ACCOUNT_BOOK, info.ACCOUNT_BOOK ) AS accountBook,\n" +
				"\t\t\tdecode( info.HS_ITEMNAME, NULL, bai.HS_ITEMNAME, info.HS_ITEMNAME ) AS hsItemname\n" +
				"\t\tFROM\n" +
				"\t\t\t(\n" +
				"\t\t\tSELECT\n" +
				"\t\t\t\ttray.stock_in,\n" +
				"\t\t\t\ttray.stock_name,\n" +
				"\t\t\t\ttray.bill_num,\n" +
				"\t\t\t\ttray.ctn_num,\n" +
				"\t\t\t\ttray.STOREROOM_NUM,\n" +
				"\t\t\t\ttray.sku_id,\n" +
				"\t\t\t\tSUM( tray.now_piece ) AS num,\n" +
				"\t\t\t\t( CASE tray.enter_state WHEN '0' THEN 'INTACT' WHEN '1' THEN 'BROKEN' WHEN '2' THEN 'COVER TORN' END ) AS enter_state,\n" +
				"\t\t\t\ttray.CARGO_NAME,\n" +
				"\t\t\t\tSUM( tray.net_weight ) AS net_weight,\n" +
				"\t\t\t\tSUM( tray.gross_weight ) AS gross_weight,\n" +
				"\t\t\t\ttray.CONTACT_NUM,\n" +
				"\t\t\t\tto_char( tray.ENTER_STOCK_TIME, 'yyyy-mm-dd' ) AS rkTime,\n" +
				"\t\t\t\ttray.asn,\n" +
				"\t\t\t\ttray.cargo_location,\n" +
				"\t\t\t\ttray.AREA_NUM\n" +
				"\t\t\tFROM\n" +
				"\t\t\t\tBIS_TRAY_INFO tray\n" +
				"\t\t\tWHERE\n" +
				"\t\t\t\t1 = 1\n" +
				"\t\t\t\tAND ( tray.cargo_state = '01' OR tray.cargo_state = '10' )\n" +
				"\t\t\t\tAND tray.now_piece != 0\n" +
				"\t\t\tGROUP BY\n" +
				"\t\t\t\ttray.stock_in,\n" +
				"\t\t\t\ttray.stock_name,\n" +
				"\t\t\t\ttray.bill_num,\n" +
				"\t\t\t\ttray.ctn_num,\n" +
				"\t\t\t\ttray.sku_id,\n" +
				"\t\t\t\ttray.enter_state,\n" +
				"\t\t\t\ttray.CARGO_NAME,\n" +
				"\t\t\t\ttray.CONTACT_NUM,\n" +
				"\t\t\t\ttray.STOREROOM_NUM,\n" +
				"\t\t\t\ttray.asn,\n" +
				"\t\t\t\ttray.enter_state,\n" +
				"\t\t\t\tto_char( tray.ENTER_STOCK_TIME, 'yyyy-mm-dd' ),\n" +
				"\t\t\t\ttray.cargo_location,\n" +
				"\t\t\t\ttray.AREA_NUM\n" +
				"\t\t\t) tray\n" +
				"\t\t\tLEFT JOIN BIS_ENTER_STOCK st ON ( tray.bill_num = st.ITEM_NUM AND tray.CONTACT_NUM = st.LINK_ID )\n" +
				"\t\t\tLEFT JOIN BIS_ENTER_STOCK_INFO info ON (\n" +
				"\t\t\t\tst.ITEM_NUM = info.ITEM_NUM\n" +
				"\t\t\t\tAND st.LINK_ID = info.LINK_ID\n" +
				"\t\t\t\tAND tray.ctn_num = info.ctn_num\n" +
				"\t\t\t\tAND tray.sku_id = info.sku\n" +
				"\t\t\t)\n" +
				"\t\t\tLEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id\n" +
				"\t\t\tLEFT JOIN BIS_ASN_INFO bai ON bai.ASN_ID = tray.asn\n" +
				"\t\t\tAND bai.SKU_ID = tray.SKU_ID\n" +
				"\t\t\tLEFT JOIN BASE_HSCODE bh ON bai.HS_CODE = bh.CODE\n" +
				"\t\tWHERE\n" +
				"\t\t\t1 = 1");


		buffer.append(" \t\t\tAND tray.bill_num =:bill_num");

		queryParams.put("bill_num", baseBounded.getBillNum());


		buffer.append(" GROUP BY\n" +
				"\t\t\ttray.stock_in,\n" +
				"\t\t\ttray.stock_name,\n" +
				"\t\t\tst.IF_BONDED,\n" +
				"\t\t\ttray.bill_num,\n" +
				"\t\t\ttray.ctn_num,\n" +
				"\t\t\ttray.STOREROOM_NUM,\n" +
				"\t\t\tinfo.bgdh,\n" +
				"\t\t\tinfo.ycg,\n" +
				"\t\t\tst.bgdh,\n" +
				"\t\t\tst.ycg,\n" +
				"\t\t\tto_char( nvl( info.BGDHDATE, st.BGDHDATE ), 'yyyy-mm-dd' ),\n" +
				"\t\t\ttray.sku_id,\n" +
				"\t\t\tst.CTN_TYPE_SIZE,\n" +
				"\t\t\ts.type_name,\n" +
				"\t\t\ts.cargo_type,\n" +
				"\t\t\ts.class_name,\n" +
				"\t\t\ts.class_type,\n" +
				"\t\t\ttray.CARGO_NAME,\n" +
				"\t\t\ttray.rkTime,\n" +
				"\t\t\ttray.enter_state,\n" +
				"\t\t\ttray.CONTACT_NUM,\n" +
				"\t\t\tst.OPERATOR,\n" +
				"\t\t\ttray.asn,\n" +
				"\t\t\ttray.cargo_location,\n" +
				"\t\t\ttray.AREA_NUM,\n" +
				"\t\t\tinfo.HS_CODE,\n" +
				"\t\t\tinfo.ACCOUNT_BOOK,\n" +
				"\t\t\tinfo.HS_ITEMNAME,\n" +
				"\t\t\tbai.HS_CODE,\n" +
				"\t\t\tbai.HS_ITEMNAME,\n" +
				"\t\t\tbai.ACCOUNT_BOOK,\n" +
				"\t\t\tbai.HS_CODE,\n" +
				"\t\t\tbai.HS_ITEMNAME,\n" +
				"\t\t\tbai.ACCOUNT_BOOK\n" +
				"\t\tORDER BY\n" +
				"\t\t\ttray.bill_num,\n" +
				"\t\t\ttray.ctn_num,\n" +
				"\t\t\ttray.rkTime) )");

		SQLQuery q = createSQLQuery(buffer.toString(), queryParams);
		q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> result = q.list();

		return result;
	}

	

	
}
