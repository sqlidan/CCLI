package com.haiersoft.ccli.bounded.dao;

import com.haiersoft.ccli.bounded.web.BondedController;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

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
