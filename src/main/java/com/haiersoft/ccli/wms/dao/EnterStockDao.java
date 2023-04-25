package com.haiersoft.ccli.wms.dao;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
/**
 * @author pyl
 * @ClassName: EnterStockDao
 * @Description: 入库联系单DAO
 * @date 2016年2月24日 下午3:52:06
 */
@Repository
public class EnterStockDao extends HibernateDao<BisEnterStock, String> {

	/**
	 * 入库联系单号
	 * @param page
	 * @param obj
	 * @return
	 */
	public Page<BisEnterStock> getEnterStocks(Page<BisEnterStock> page,BisEnterStock obj) {
		StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
		sb.append(" SELECT                                                         ");
		sb.append("   ST.LINK_ID AS linkId,                                        ");
		sb.append("   ST.ITEM_NUM AS itemNum,                                      ");
		sb.append("   info.CTN_NUM AS vesselName,                                  ");
		sb.append("   st.FEE_ID AS feeId,                                          ");
		sb.append("   ST.FEE_PLAN AS feePlan,                                      ");
		sb.append("   ST.STOCK_ID AS stockId,                                      ");
		sb.append("   ST.STOCK_IN AS stockIn,                                      ");
		sb.append("   ST.AUDITING_STATE AS auditingState,                          ");
		sb.append("   ST.REMARK AS remark,                                         ");
		sb.append("   ST.IF_BACK AS ifBack,                                        ");
		sb.append("   ST.BACKDATE AS backDate,                                     ");
		sb.append("   ST.RK_TIME AS rkTime,                                        ");
		sb.append("   ST.CTN_TYPE_SIZE AS ctnTypeSize,                             ");
		sb.append("   ST.IF_BONDED AS ifBonded,                                    ");
		sb.append("   ST.IF_SORTING AS ifSorting,                                  ");
		sb.append("   ST.IF_TO_CUSTOMS AS ifToCustoms,                             ");
		sb.append("   ST.CUSTOMS_COMPANY AS customsCompany,                        ");
		sb.append("   ST.IF_TO_CIQ AS ifToCiq,                                     ");
		sb.append("   ST.CIQ_COMPANY AS ciqCompany,                                ");
		sb.append("   ST.OPERATOR AS operator,                                     ");
		sb.append("   ST.OPERATE_TIME AS operateTime,                              ");
		sb.append("   ST.TEMPERATURE AS temperature,                               ");
		sb.append("   ST.SORTING_SPECIAL AS sortingSpecial,                        ");
		sb.append("   ST.PLAN_FEE_STATE AS planFeeState,                           ");
		sb.append("   ST.FINISH_FEE_STATE AS finishFeeState,                       ");
		sb.append("   ST.ETA_WAREHOUSE AS etaWarehouse,                            ");
		sb.append("   ST.WAREHOUSE AS warehouse,                                   ");
		sb.append("   ST.IF_WRAP AS ifWrap,                                        ");
		sb.append("   ST.IF_BAGGING AS ifBagging,                                  ");
		sb.append("   ST.IF_WITH_WOODEN AS ifWithWooden,                           ");
		sb.append("   ST.IF_MAC_ADMIT AS ifMacAdmit,INFOS.HS_CODE as hscode ,st.IS_SEND as isSend                             ");
		sb.append(" FROM                                                           ");
		sb.append(" 	BIS_ENTER_STOCK st                                         ");
		sb.append(" LEFT JOIN                                                      ");
		sb.append(" (                                                              ");
		sb.append("   SELECT                                                       ");
		sb.append("      INFO.LINK_ID,                                             ");
		sb.append("      INFO.ITEM_NUM,                                            ");
		sb.append("      LISTAGG (INFO.CTN_NUM, ',') WITHIN GROUP (ORDER BY INFO.CTN_NUM) AS CTN_NUM   ");
		sb.append("   FROM                                                         ");
		sb.append("      (SELECT DISTINCT LINK_ID,ITEM_NUM,CTN_NUM FROM BIS_ENTER_STOCK_INFO   GROUP BY  LINK_ID,ITEM_NUM,CTN_NUM) info        ");
		sb.append("   GROUP BY                                                     ");
		sb.append("      INFO.LINK_ID,                                             ");
		sb.append("      INFO.ITEM_NUM                                       ");
		sb.append(" ) info                                                         ");
		sb.append(" ON                                                             ");
		sb.append("   ST.LINK_ID=INFO.LINK_ID AND ST.ITEM_NUM=INFO.ITEM_NUM        ");
		sb.append(" LEFT JOIN                                                      ");
		sb.append(" (                                                              ");
		sb.append("   SELECT                                                       ");
		sb.append("      INFO.LINK_ID,                                             ");
		sb.append("      INFO.ITEM_NUM,                                            ");
		sb.append("      LISTAGG (INFO.HS_CODE, ',') WITHIN GROUP (ORDER BY INFO.HS_CODE) AS HS_CODE   ");
		sb.append("   FROM                                                         ");
		sb.append("      (SELECT DISTINCT LINK_ID,ITEM_NUM, HS_CODE FROM BIS_ENTER_STOCK_INFO   GROUP BY  LINK_ID,ITEM_NUM,HS_CODE) info        ");
		sb.append("   GROUP BY                                                     ");
		sb.append("      INFO.LINK_ID,                                             ");
		sb.append("      INFO.ITEM_NUM                                        ");
		sb.append(" ) INFOS                                                         ");
		sb.append(" ON                                                             ");
		sb.append("   ST.LINK_ID=INFOS.LINK_ID AND ST.ITEM_NUM=INFOS.ITEM_NUM        ");
		sb.append(" where 1=1 and ST.DEL_FLAG='0'                                  ");

		if (null!=obj.getBgdh()&& !"".equals(obj.getBgdh())) {//报关单号
        	sb.append(" and ST.BGDH like:bgdh  ");
            parme.put("bgdh","%"+obj.getBgdh()+"%");
        }
		
		if (null!=obj.getSearchItemNum()&& !"".equals(obj.getSearchItemNum())) {//提单号
        	sb.append(" and ST.ITEM_NUM like:billnum  ");
            parme.put("billnum","%"+obj.getSearchItemNum()+"%");
        }
		
        if (null!=obj.getSearchStockIn()&& !"".equals(obj.getSearchStockIn())) {//--客户ID
        	sb.append(" and ST.STOCK_ID=:sockid");
            parme.put("sockid",obj.getSearchStockIn());
        }
        
        if(null!=obj.getIfBack()&&!"".equals(obj.getIfBack())){
        	if("1".equals(obj.getIfBack())){
	        	sb.append(" AND ST.IF_BACK=:back");
	        	parme.put("back",obj.getIfBack());
        	}else{
        		sb.append(" AND (ST.IF_BACK='0' or ST.IF_BACK is NULL) ");
        	}
        }
        
        if(null!=obj.getIfBonded()&&!"".equals(obj.getIfBonded())){
        	if("1".equals(obj.getIfBonded())){
	        	sb.append(" AND ST.IF_BONDED=:bonded");
	        	parme.put("bonded",obj.getIfBonded());
        	}else{
        		sb.append(" AND (ST.IF_BONDED='0' or ST.IF_BONDED is NULL) ");
        	}
        }
        
        if(null!=obj.getIfToCustoms()&&!"".equals(obj.getIfToCustoms())){
        	if("1".equals(obj.getIfToCustoms())){
	        	sb.append(" AND ST.IF_TO_CUSTOMS=:customs");
	        	parme.put("customs",obj.getIfToCustoms());
        	}else{
        		sb.append(" AND (ST.IF_TO_CUSTOMS='0' or ST.IF_TO_CUSTOMS is NULL) ");
        	}
        }
        
        if(null!=obj.getIfToCiq()&&!"".equals(obj.getIfToCiq())){
        	if("1".equals(obj.getIfToCiq())){
	        	sb.append(" AND ST.IF_TO_CIQ=:ciq");
	        	parme.put("ciq",obj.getIfToCiq());
        	}else{
        		sb.append(" AND (ST.IF_TO_CIQ='0' or ST.IF_TO_CIQ is NULL) ");
        	}
        }
        
        if(null!=obj.getIfSorting()&&!"".equals(obj.getIfSorting())){
        	if("1".equals(obj.getIfSorting())){
	        	sb.append(" AND ST.IF_SORTING=:sorting");
	        	parme.put("sorting",obj.getIfSorting());
        	}else{
        		sb.append(" AND (ST.IF_SORTING='0' or ST.IF_SORTING is NULL) ");
        	}
        }
        
        if(null!=obj.getAuditingState()&&!"".equals(obj.getAuditingState())){
        	sb.append(" AND ST.AUDITING_STATE=:state");
        	parme.put("state",obj.getAuditingState());
        }
        
        if(null!=obj.getSearchLinkId()&&!"".equals(obj.getSearchLinkId())){
        	sb.append(" AND ST.LINK_ID=:linkId");
        	parme.put("linkId",obj.getSearchLinkId());
        }
        
        if(null!=obj.getOperator()&&!"".equals(obj.getOperator())){
        	sb.append(" AND ST.OPERATOR=:operator");
        	parme.put("operator",obj.getOperator());
        }
        
        if(null!=obj.getSearchCunNum()&&!"".equals(obj.getSearchCunNum())){
        	sb.append(" AND info.CTN_NUM LIKE:ctnNum");
        	parme.put("ctnNum","%"+obj.getSearchCunNum()+"%");
        }
        boolean flag=false;
        if (obj.getSearchStrTime()!= null && !"".equals(obj.getSearchStrTime())) {//--入库日期
        	flag=true;
        	//sb.append(" and ST.ETA_WAREHOUSE>=to_date(:searchstrtime,'yyyy-mm-dd hh24:mi:ss')  ");
            //parme.put("searchstrtime", obj.getSearchStrTime());
        }
        if (obj.getSearchEndTime()!= null && !"".equals(obj.getSearchEndTime())) {//--入库日期
        	flag=true;
        	//sb.append(" and ST.ETA_WAREHOUSE<to_date(:searchendtime,'yyyy-mm-dd hh24:mi:ss')");
            //parme.put("searchendtime", obj.getSearchEndTime());
        }
        
        if(flag){
           sb.append(" and ST.LINK_ID in (SELECT LINK_ID FROM BIS_ASN where 1=1 and INBOUND_DATE>=to_date(:searchstrtime,'yyyy-mm-dd hh24:mi:ss') and INBOUND_DATE<to_date(:searchendtime,'yyyy-mm-dd hh24:mi:ss')  GROUP BY LINK_ID) ");
           parme.put("searchstrtime", obj.getSearchStrTime());
           parme.put("searchendtime", obj.getSearchEndTime());
        }
        
        if (obj.getSearchDxStrTime()!= null && !"".equals(obj.getSearchDxStrTime())) {//--倒箱开始日期
            sb.append(" and ST.BACKDATE>=to_date(:dxstrtime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("dxstrtime", obj.getSearchDxStrTime());
        }
        if (obj.getSearchDxEndTime()!= null && !"".equals(obj.getSearchDxEndTime())) {//--倒箱结束日期
            sb.append(" and ST.BACKDATE<to_date(:dxendtime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("dxendtime", obj.getSearchDxEndTime());
        }
        sb.append(" ORDER BY ST.OPERATE_TIME DESC"                                  );       
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("linkId",String.class);//入库联系单号
        paramType.put("itemNum",String.class);//提单号
        paramType.put("vesselName",String.class);//箱号
        paramType.put("feeId",String.class);//费用方案Id
        paramType.put("feePlan",String.class);//费用方案
        paramType.put("stockId", String.class);//存货方Id
        paramType.put("stockIn", String.class);//存货方
        paramType.put("auditingState", Integer.class);//审核状态
        paramType.put("remark", String.class);//备注
        paramType.put("ifBack", String.class);//是否倒箱
        paramType.put("backDate", Date.class);//倒箱日期
        paramType.put("rkTime", String.class);//入库日期
        paramType.put("ctnTypeSize", String.class);//箱型尺寸
        paramType.put("ifBonded", String.class);//是否保税
        paramType.put("ifSorting", String.class);//是否分拣
        paramType.put("ifToCustoms", String.class);//报关
        paramType.put("customsCompany", String.class);//报关代理公司
        paramType.put("ifToCiq", String.class);//报检
        paramType.put("ciqCompany", String.class);//报检代理公司
        paramType.put("operator", String.class);//创建人
        paramType.put("operateTime", Date.class);//创建时间
        paramType.put("temperature", String.class);//温度
        paramType.put("sortingSpecial", String.class);//特殊要求
        paramType.put("planFeeState",String.class);//计划费用状态
        paramType.put("finishFeeState",String.class);//费用完成状态
        paramType.put("etaWarehouse", Date.class);//计划入库日期
        paramType.put("warehouse", String.class);//入库仓库
        paramType.put("ifWrap", String.class);//是否缠膜
        paramType.put("ifBagging", String.class);//是否套袋
        paramType.put("ifWithWooden", String.class);//是否带木托
        paramType.put("ifMacAdmit", String.class);//是否MSC认证
        paramType.put("hscode", String.class);//是否MSC认证
        paramType.put("isSend", String.class);//是否MSC认证
            
        return findPageSql(page, sb.toString(), paramType, parme);
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Map<String, Object>> findList(Page page, String value) {
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        if (value != null && !"".equals(value)) {
            sb.append("select r.link_id as linkId,0 as ntype,r.stock_in as stockIn ,r.stock_id as stockId,r.item_num as itemNum,r.operate_time as operateTime,substr(r.rk_time,0,10) as rkTime,r.IF_BONDED as ifBonded,r.STOCK_ORG_ID as stockOrgId,r.STOCK_ORG as stockOrg from bis_enter_stock r where (r.link_id=:linkNum or r.item_num=:itemNum) and r.DEL_FLAG='0'");
            parme.put("linkNum", value);
            parme.put("itemNum", value);
        }
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
    /**
     * 按条件查询入库联系单和货转联系单集合
     *
     * @param page
     * @param billNum        //提单id
     * @param transNum//装车单号
     * @param ctnNum         //厢号
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Map<String, Object>> findList(Page page, String linkNum, String billNum, String transNum, String ctnNum) {
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        if (linkNum != null && !"".equals(linkNum)) {
            sb.append("select r.link_id as codenum,0 as ntype,r.stock_in as stock ,r.stock_id as stockid,r.item_num as itemnum,r.operate_time,substr(r.rk_time,0,10) as RK_TIME,r.IF_BONDED,r.STOCK_ORG_ID,r.STOCK_ORG from bis_enter_stock r where r.link_id=:linkNum and r.DEL_FLAG='0'");
            parme.put("linkNum", linkNum);
        } else if (billNum != null && !"".equals(billNum)) {
            sb.append("select r.link_id as codenum,0 as ntype,r.stock_in as stock ,r.stock_id as stockid,r.item_num as itemnum,r.operate_time,substr(r.rk_time,0,10) as RK_TIME,r.IF_BONDED,r.STOCK_ORG_ID,r.STOCK_ORG from bis_enter_stock r where r.item_num=:ritem_num and r.DEL_FLAG='0'");
            parme.put("ritem_num", billNum);
        } else {
            if ((transNum != null && !"".equals(transNum)) || (ctnNum != null && !"".equals(ctnNum))) {
                sb.append("select r.link_id as codenum,0 as ntype,r.stock_in as stock ,r.stock_id as stockid,r.item_num as itemnum,r.operate_time,r.IF_BONDED  from bis_enter_stock r where r.item_num in (select loading_plan_num from bis_loading_info z where 1=1 ").append(transNum != null && !"".equals(transNum) ? " and z.loading_truck_num=:truckNum1" : "").append((ctnNum != null && !"".equals(ctnNum)) ? " and z.ctn_num=:ctnNum1" : "").append(")");
                sb.append(" union ");
                sb.append("select t.transfer_id as codenum,1 as ntype,t.stock_in as strock,t.stock_in_id as stockid ,t.item_num as itemnum,t.operate_time  from  bis_transfer_stock t where t.item_num in (select loading_plan_num from bis_loading_info z where 1=1 ").append(transNum != null && !"".equals(transNum) ? " and z.loading_truck_num=:truckNum2" : "").append(ctnNum != null && !"".equals(ctnNum) ? " and z.ctn_num=:ctnNum2" : "").append(")");
                if (transNum != null && !"".equals(transNum)) {
                    parme.put("truckNum1", transNum);
                    parme.put("truckNum2", transNum);
                }
                if (ctnNum != null && !"".equals(ctnNum)) {
                    parme.put("ctnNum1", ctnNum);
                    parme.put("ctnNum2", ctnNum);
                }
            }
        }
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
    /**
     * 获取列表数据
     * @param page
     * @param obj
     * @return
     */
	public Page<BisEnterStock> getStocks(Page<BisEnterStock> page,BisEnterStock obj) {
		StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        if (obj.getSearchLinkId()!= null && !"".equals(obj.getSearchLinkId())) {
            sb.append("select r.link_id as linkId,0 as ntype,r.stock_in as stockIn ,r.stock_id as stockId,r.item_num as itemNum,r.operate_time as operateTime,substr(r.rk_time,0,10) as rkTime,r.IF_BONDED as ifBonded,r.STOCK_ORG_ID as stockOrgId,r.STOCK_ORG as stockOrg from bis_enter_stock r where r.link_id=:linkNum and r.DEL_FLAG='0'");
            parme.put("linkNum",obj.getSearchLinkId());
        } else if (obj.getSearchItemNum()!= null && !"".equals(obj.getSearchItemNum())) {
            sb.append("select r.link_id as linkId,0 as ntype,r.stock_in as stockIn ,r.stock_id as stockId,r.item_num as itemNum,r.operate_time as operateTime,substr(r.rk_time,0,10) as rkTime,r.IF_BONDED as ifBonded,r.STOCK_ORG_ID as stockOrgId,r.STOCK_ORG as stockOrg from bis_enter_stock r where r.item_num=:ritem_num and r.DEL_FLAG='0'");
            parme.put("ritem_num",obj.getSearchItemNum());
        }
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("linkId",String.class);//入库联系单号
        paramType.put("ntype",Integer.class);//
        paramType.put("stockIn",String.class);//客户
        paramType.put("stockId",String.class);//客户id
        paramType.put("itemNum",String.class);//提单号
        paramType.put("operateTime",Date.class);//创建时间
        paramType.put("rkTime", String.class);//入库日期
        paramType.put("ifBonded",String.class);//是否保税
        paramType.put("stockOrgId", String.class);
        paramType.put("stockOrg", String.class);
        return findPageSql(page, sb.toString(), paramType, parme);
	}
    /**
     * 入库报告书--普通客户
     *
     * @param itemNum 提单号
     * @param cunNum  厢号
     * @param stockIn 客户id
     * @param linkId  联系单号
     * @param sku     sku
     * @param strTime 入库时间开始
     * @param endTime 入库时间结束
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findRepotPT(String itemNum, String cunNum, String stockIn, String linkId, String sku, String strTime, String endTime,String isBonded) {
        List<Map<String, Object>> getList = null;
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        String billCode = "";
        if (!StringUtils.isNull(itemNum)) {
            String[] billList = itemNum.split(",");
            for (String billNum : billList) {
                billCode += "'" + billNum + "'" + ",";
            }
            if (!billCode.equals("")) {
                billCode = billCode.substring(0, billCode.length() - 1);
            }
        }
        sb.append(" SELECT ");
        sb.append(" c.client_name,    ");
        sb.append(" aa.stock_in,      ");
        sb.append(" aa.bill_num,      ");
        sb.append(" aa.ctn_num,       ");
        sb.append(" aa.sku_id,        ");
        sb.append(" s.cargo_name,     ");
        sb.append(" aa.inbound_date,  ");
        sb.append(" aa.enter_state,   ");
        sb.append(" aa.ruku_piece_sum,");
        sb.append(" round(aa.ruku_piece_sum * s.net_single,2) AS net_weight_sum,     ");
        sb.append(" round(aa.ruku_piece_sum * s.gross_single,2) AS gross_weight_sum, ");
        sb.append(" aa.is_bonded  ");
        sb.append(" FROM(  ");
        sb.append(" SELECT  ");
        sb.append(" a.inbound_date, ");
        sb.append(" a.is_bonded,   ");
        sb.append(" t.stock_in,    ");
        sb.append(" t.bill_num,    ");
        sb.append(" t.ctn_num,     ");
        sb.append(" t.sku_id,      ");
        sb.append(" (              ");
        sb.append(" 	CASE t.enter_state ");
        sb.append(" 	WHEN '0' THEN      ");
        sb.append(" 		'INTACT'         ");
        sb.append(" 	WHEN '1' THEN      ");
        sb.append(" 		'BROKEN'         ");
        sb.append(" 	WHEN '2' THEN      ");
        sb.append(" 		'COVER TORN'     ");
        sb.append(" 	END                ");
        sb.append(" ) AS enter_state,    ");
        sb.append(" sum(            ");
        sb.append(" 	t.original_piece - t.remove_piece ");
        sb.append(" ) AS ruku_piece_sum     ");
        sb.append(" FROM                    ");
        sb.append("(                        ");
        sb.append("	SELECT                  ");
        sb.append("		ba.asn,               ");
        sb.append("		ba.inbound_date,      ");
        sb.append("		ba.ctn_num,           ");
        sb.append("		ba.bill_num,          ");
        sb.append("		ba.stock_in,          ");
        sb.append("		ba.is_bonded          ");
        sb.append("	FROM                    ");
        sb.append("		bis_asn ba            ");
        sb.append("	WHERE                   ");
        sb.append("		1 = 1                 ");
        sb.append("	AND ba.ASN_STATE >= '3' ");
        if(!StringUtils.isNull(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" and ba.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" and (ba.is_bonded='0' or ba.is_bonded is null) ");
        	}
        }
        if (!StringUtils.isNull(itemNum)) {//提单号
        	sb.append(" AND ba.bill_num in (" + billCode + ") ");
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and ba.ctn_num=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and ba.stock_in=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
        	sb.append(" and ba.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
        	sb.append(" and ba.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        sb.append(" ) a  ");
        sb.append(" INNER JOIN ( ");
        sb.append(" SELECT   ");
        sb.append(" bt.sku_id, ");
        sb.append(" bt.asn, ");
        sb.append(" bt.bill_num,  ");
        sb.append(" bt.stock_in,  ");
        sb.append(" bt.ctn_num, ");
        sb.append(" bt.enter_state, ");
        sb.append(" bt.original_piece, ");
        sb.append(" bt.remove_piece ");
        sb.append(" FROM BIS_TRAY_INFO bt  ");
        sb.append(" WHERE 1 = 1 AND bt.cargo_state != '99' AND bt.cargo_state >= '01' ");
        if (!StringUtils.isNull(itemNum)) {//提单号
            sb.append(" AND bt.bill_num in (" + billCode + ") ");
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and bt.ctn_num=:ctnnum1   ");
            parme.put("ctnnum1", cunNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and bt.stock_in=:sockid1  ");
            parme.put("sockid1", stockIn);
        }
        if (sku != null && !"".equals(sku)) {//--sku
        	sb.append(" and  bt.sku_id=:sku  ");
            parme.put("sku", sku);
        }
        sb.append(" ) t ON t.asn = a.asn     ");
        sb.append(" AND a.bill_num = t.bill_num   ");
        sb.append(" AND a.ctn_num = t.ctn_num ");
        sb.append(" GROUP BY  ");
        sb.append(" t.stock_in, ");
        sb.append(" t.bill_num, ");
        sb.append(" t.ctn_num,  ");
        sb.append(" t.sku_id,  ");
        sb.append(" t.enter_state, ");
        sb.append(" a.is_bonded, ");
        sb.append(" a.inbound_date  ");
        sb.append(" ) aa   ");
        sb.append(" LEFT JOIN base_client_info c ON c.ids = aa.stock_in                   ");
        sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id                ");
        sb.append(" ORDER BY    ");
        sb.append(" aa.bill_num,  ");
        sb.append(" aa.ctn_num,  ");
        sb.append(" aa.inbound_date ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }

    /**
     * 入库报告书--JP客户
     *
     * @param itemNum 提单号
     * @param cunNum  厢号
     * @param stockIn 客户id
     * @param linkId  联系单号
     * @param strTime 入库时间开始
     * @param endTime 入库时间结束
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findRepotJP(String itemNum, String cunNum, String stockIn, String linkId, String sku, String strTime, String endTime,String isBonded) {
        List<Map<String, Object>> getList = null;
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        String billCode = "";
        if (!StringUtils.isNull(itemNum)) {
            String[] billList = itemNum.split(",");
            for (String billNum : billList) {
                billCode += "'" + billNum + "'" + ",";
            }
            if (!billCode.equals("")) {
                billCode = billCode.substring(0, billCode.length() - 1);
            }
        }
        sb.append("SELECT   ");
        sb.append("	c.client_name,");
        sb.append("	aa.stock_in,  ");
        sb.append("	aa.rk_num,    ");
        sb.append("	aa.bill_num,  ");
        sb.append("	aa.ctn_num,   ");
        sb.append("	aa.sku_id,    ");
        sb.append("	s.cargo_name, ");
        sb.append("	aa.inbound_date,  ");
        sb.append("	aa.enter_state,   ");
        sb.append("	aa.ruku_piece_sum,");
        sb.append("	round(            ");
        sb.append("		aa.ruku_piece_sum * s.net_single,  ");
        sb.append("		2                    ");
        sb.append("	) AS net_weight_sum,   ");
        sb.append("	round(                 ");
        sb.append("		aa.ruku_piece_sum * s.gross_single,");
        sb.append("		2                                  ");
        sb.append("	) AS gross_weight_sum,  ");
        sb.append("	aa.is_bonded            ");
        sb.append("FROM      ");
        sb.append("	(        ");
        sb.append("		SELECT ");
        sb.append("			ai.rk_num,          ");
        sb.append("			a.inbound_date,     ");
        sb.append("			a.is_bonded,        ");
        sb.append("			t.stock_in,         ");
        sb.append("			t.bill_num,         ");
        sb.append("			t.ctn_num,          ");
        sb.append("			t.sku_id,           ");
        sb.append("			(                   ");
        sb.append("				CASE t.enter_state");
        sb.append("				WHEN '0' THEN     ");
        sb.append("					'INTACT'        ");
        sb.append("				WHEN '1' THEN     ");
        sb.append("					'BROKEN'        ");
        sb.append("				WHEN '2' THEN     ");
        sb.append("					'COVER TORN'    ");
        sb.append("				END               ");
        sb.append("			) AS enter_state,   ");
        sb.append("			sum(                ");
        sb.append("				t.original_piece - t.remove_piece ");
        sb.append("			) AS ruku_piece_sum  ");
        sb.append("		FROM                   ");
        sb.append("			(                    ");
        sb.append("				SELECT             ");
        sb.append("					ba.asn,          ");
        sb.append("					ba.inbound_date, ");
        sb.append("					ba.is_bonded,    ");
        sb.append("					ba.ctn_num,      ");
        sb.append("					ba.bill_num,     ");
        sb.append("					ba.stock_in      ");
        sb.append("				FROM               ");
        sb.append("					bis_asn ba       ");
        sb.append("				WHERE              ");
        sb.append("					ba.if_second_enter = '1'        ");
        sb.append("				AND ba.ASN_STATE >= '3'           ");
        if(!StringUtils.isNull(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" and ba.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" and (ba.is_bonded='0' or ba.is_bonded is null) ");
        	}
        }
        if (!StringUtils.isNull(itemNum)) {//提单号
            sb.append(" AND ba.bill_num in (" + billCode + ") ");
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and ba.ctn_num=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and ba.stock_in=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
        	sb.append(" and ba.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
        	sb.append(" and ba.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        sb.append("			) a ");
        sb.append("		LEFT JOIN bis_asn_info ai ON ai.asn_id = a.asn ");
        sb.append("		INNER JOIN (          ");
        sb.append("			SELECT              ");
        sb.append("				bt.sku_id,        ");
        sb.append("				bt.asn,           ");
        sb.append("				bt.bill_num,      ");
        sb.append("				bt.stock_in,      ");
        sb.append("				bt.ctn_num,       ");
        sb.append("				bt.enter_state,   ");
        sb.append("				bt.original_piece,");
        sb.append("				bt.remove_piece   ");
        sb.append("			FROM                ");
        sb.append("				BIS_TRAY_INFO bt  ");
        sb.append("			WHERE               ");
        sb.append("				1 = 1             ");
        sb.append("			AND bt.cargo_state != '99' ");
        sb.append("			AND bt.cargo_state >= '01' ");
        if (!StringUtils.isNull(itemNum)) {//提单号
        	sb.append(" AND bt.bill_num in (" + billCode + ") ");
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and bt.ctn_num=:ctnnum1   ");
            parme.put("ctnnum1", cunNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and bt.stock_in=:sockid1  ");
            parme.put("sockid1", stockIn);
        }
        if (sku != null && !"".equals(sku)) {//--sku
        	sb.append(" and  bt.sku_id==:sku  ");
            parme.put("sku", sku);
        }
        sb.append("		) t ON t.asn = a.asn        ");
        sb.append("		AND ai.sku_id = t.sku_id    ");
        sb.append("		AND a.bill_num = t.bill_num ");
        sb.append("		AND a.ctn_num = t.ctn_num   ");
        sb.append("		GROUP BY         ");
        sb.append("			ai.rk_num,     ");
        sb.append("			t.stock_in,    ");
        sb.append("			t.bill_num,    ");
        sb.append("			t.ctn_num,     ");
        sb.append("			t.sku_id,      ");
        sb.append("			a.inbound_date,");
        sb.append("			a.is_bonded,   ");
        sb.append("			t.enter_state  ");
        sb.append("	) aa               ");
        sb.append("LEFT JOIN base_client_info c ON c.ids = aa.stock_in    ");
        sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id ");
        sb.append("ORDER BY         ");
        sb.append("	aa.bill_num,    ");
        sb.append("	aa.ctn_num,     ");
        sb.append("	aa.rk_num,      ");
        sb.append("	aa.inbound_date ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }

    /**
     * 入库报告书--OTE客户
     *
     * @param itemNum 提单号
     * @param cunNum  厢号
     * @param stockIn 客户id
     * @param linkId  联系单号
     * @param strTime 入库时间开始
     * @param endTime 入库时间结束
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findRepotOTE(String itemNum, String cunNum, String stockIn, String linkId, String sku, String strTime, String endTime,String isBonded) {
        List<Map<String, Object>> getList = null;
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        String billCode = "";
        if (!StringUtils.isNull(itemNum)) {
            String[] billList = itemNum.split(",");
            for (String billNum : billList) {
                billCode += "'" + billNum + "'" + ",";
            }
            if (!billCode.equals("")) {
                billCode = billCode.substring(0, billCode.length() - 1);
            }
        }
        sb.append("SELECT             ");
        sb.append("	c.client_name,    ");
        sb.append("	aa.stock_in,      ");
        sb.append("	aa.bill_num,      ");
        sb.append("	aa.ctn_num,       ");
        sb.append("	aa.sku_id,        ");
        sb.append("	s.cargo_name,     ");
        sb.append("	aa.inbound_date,  ");
        sb.append("	aa.pro_time,      ");
        sb.append("	aa.enter_state,   ");
        sb.append("	aa.ruku_piece_sum,");
        sb.append("	round(            ");
        sb.append("		aa.ruku_piece_sum * s.net_single,  ");
        sb.append("		2                                  ");
        sb.append("	) AS net_weight_sum,                 ");
        sb.append("	round(                               ");
        sb.append("		aa.ruku_piece_sum * s.gross_single,");
        sb.append("		2                                  ");
        sb.append("	) AS gross_weight_sum,               ");
        sb.append("	s.type_size,       ");
        sb.append("	s.pro_num,         ");
        sb.append("	s.lot_num,         ");
        sb.append("	s.msc_num,         ");
        sb.append("	aa.is_bonded       ");
        sb.append("FROM                ");
        sb.append("	(                  ");
        sb.append("		SELECT           ");
        sb.append("			ai.pro_time,   ");
        sb.append("			t.inbound_date,");
        sb.append("			a.is_bonded,   ");
        sb.append("			t.stock_in,    ");
        sb.append("			t.bill_num,    ");
        sb.append("			t.ctn_num,     ");
        sb.append("			t.sku_id,      ");
        sb.append("			(                    ");
        sb.append("				CASE t.enter_state ");
        sb.append("				WHEN '0' THEN      ");
        sb.append("					'INTACT'         ");
        sb.append("				WHEN '1' THEN      ");
        sb.append("					'BROKEN'         ");
        sb.append("				WHEN '2' THEN      ");
        sb.append("					'COVER TORN'     ");
        sb.append("				END                ");
        sb.append("			) AS enter_state,    ");
        sb.append("			sum(                 ");
        sb.append("				t.original_piece - t.remove_piece ");
        sb.append("			) AS ruku_piece_sum  ");
        sb.append("		FROM                   ");
        sb.append("			(                    ");
        sb.append("				SELECT             ");
        sb.append("					ba.asn,          ");
        sb.append("					ba.is_bonded,    ");
        sb.append("					ba.ctn_num,      ");
        sb.append("					ba.bill_num,     ");
        sb.append("					ba.stock_in      ");
        sb.append("				FROM               ");
        sb.append("					bis_asn ba       ");
        sb.append("				WHERE              ");
        sb.append("					ba.if_second_enter = '1' ");
        sb.append("				AND ba.ASN_STATE >= '3'    ");
        if(!StringUtils.isNull(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" and ba.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" and (ba.is_bonded='0' or ba.is_bonded is null) ");
        	}
        }
        if (!StringUtils.isNull(itemNum)) {//提单号
        	sb.append(" AND ba.bill_num in (" + billCode + ") ");
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and ba.ctn_num=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and ba.stock_in=:sockid  ");
            parme.put("sockid", stockIn);
        }
        sb.append("			) a  ");
        sb.append("		LEFT JOIN bis_asn_info ai ON ai.asn_id = a.asn      ");
        sb.append("		INNER JOIN (          ");
        sb.append("			SELECT              ");
        sb.append("				bt.sku_id,        ");
        sb.append("				bt.asn,           ");
        sb.append("				bt.bill_num,      ");
        sb.append("				bt.stock_in,      ");
        sb.append("				bt.ctn_num,       ");
        sb.append("				bt.enter_state,   ");
        sb.append("				to_char(bt.ENTER_TALLY_TIME, 'yyyy-mm-dd') AS inbound_date,   ");
        sb.append("				bt.original_piece,");
        sb.append("				bt.remove_piece   ");
        sb.append("			FROM                ");
        sb.append("				BIS_TRAY_INFO bt  ");
        sb.append("			WHERE               ");
        sb.append("				1 = 1             ");
        sb.append("			AND bt.cargo_state != '99' ");
        sb.append("			AND bt.cargo_state >= '01' ");
        if (!StringUtils.isNull(itemNum)) {//提单号
        	sb.append(" AND bt.bill_num in (" + billCode + ") ");
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and bt.ctn_num=:ctnnum1   ");
            parme.put("ctnnum1", cunNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and bt.stock_in=:sockid1  ");
            parme.put("sockid1", stockIn);
        }
        if (sku != null && !"".equals(sku)) {//--sku
        	sb.append(" and  bt.sku_id==:sku  ");
            parme.put("sku", sku);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and bt.ENTER_TALLY_TIME>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and bt.ENTER_TALLY_TIME<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        sb.append("		) t ON t.asn = a.asn        ");
        sb.append("		AND ai.sku_id = t.sku_id    ");
        sb.append("		AND a.bill_num = t.bill_num ");
        sb.append("		AND a.ctn_num = t.ctn_num   ");
        sb.append("		GROUP BY         ");
        sb.append("			t.bill_num,    ");
        sb.append("			t.ctn_num,     ");
        sb.append("			t.sku_id,      ");
        sb.append("			ai.pro_time,   ");
        sb.append("			t.inbound_date,");
        sb.append("			a.is_bonded,   ");
        sb.append("			t.enter_state, ");
        sb.append("			t.stock_in     ");
        sb.append("	) aa               ");
        sb.append("LEFT JOIN base_client_info c ON c.ids = aa.stock_in    ");
        sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id ");
        sb.append("ORDER BY         ");
        sb.append("	aa.bill_num,    ");
        sb.append("	aa.ctn_num,     ");
        sb.append("	aa.pro_time,    ");
        sb.append("	aa.inbound_date ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }

    public BisEnterStock getEnterStock(String billNum) {
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("itemNum", billNum);
        params.put("delFlag", "0");
        List<BisEnterStock> result = findBy(params);
        if (result.size() == 1){
            return result.get(0);
        }
        return null;
    }

	@SuppressWarnings("unchecked")
	public List<HashMap> getlisths(String voyageNum, String chineseName) {
			   StringBuffer sql=new StringBuffer();
			   List<HashMap> getList = new ArrayList<HashMap>();
		 	  // sql.append(" SELECT cmdm,JKHC,CKHC FROM CBJH where 1=1 ");
		 	  sql.append("  SELECT cmdm,seq_no FROM hgcy_wxp_cbjh where 1=1");
		 	   if(null!=chineseName&&!"".equals(chineseName)){
		 		  sql.append(" and  cshipnam = '"+chineseName.trim()+"'");
		 	   }
		 	   if(null!=voyageNum&&!"".equals(voyageNum)){
		 		  sql.append(" and  jkhc = '"+voyageNum.trim()+"'");
		 	   }	 	   
		 	//  sql.append("  order by code   ");
				SQLQuery sqlQuery2=this.getSession().createSQLQuery(sql.toString());
				List<Object[]> datas = (List<Object[]>)sqlQuery2.list();
				for (Object[] ob:datas){
					
					HashMap data = new HashMap();
					data.put("vesselName",ob[0].toString()!= null ? ob[0].toString() : "" );
		        	
					
					getList.add(data);
				}
		 	 //  Query query= createQuery(sql.toString());
		 	 //  List<Object[]> list=query.list();
		 	   return getList;

		
		}


    /**
     * 入库报告书--普通客户(为海路通提供接口)
     *
     * @param itemNum 提单号
     * @param cunNum  厢号
     * @param realClientName 客户名称
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> enterReportInformation(String itemNum, String cunNum, String realClientName) {
        List<Map<String, Object>> getList = null;
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        String billCode = "";
        if (!StringUtils.isNull(itemNum)) {
            String[] billList = itemNum.split(",");
            for (String billNum : billList) {
                billCode += "'" + billNum + "'" + ",";
            }
            if (!billCode.equals("")) {
                billCode = billCode.substring(0, billCode.length() - 1);
            }
        }
        sb.append(" SELECT ");
        sb.append(" c.REAL_CLIENT_NAME AS client_name,    ");
        sb.append(" aa.stock_in,      ");
        sb.append(" aa.bill_num,      ");
        sb.append(" aa.ctn_num,       ");
        sb.append(" aa.sku_id,        ");
        sb.append(" s.cargo_name,     ");
        sb.append(" aa.inbound_date,  ");
        sb.append(" aa.enter_state,   ");
        sb.append(" aa.ruku_piece_sum,");
        sb.append(" round(aa.ruku_piece_sum * s.net_single,2) AS net_weight_sum,     ");
        sb.append(" round(aa.ruku_piece_sum * s.gross_single,2) AS gross_weight_sum, ");
        sb.append(" aa.is_bonded  ");
        sb.append(" FROM(  ");
        sb.append(" SELECT  ");
        sb.append(" a.inbound_date, ");
        sb.append(" a.is_bonded,   ");
        sb.append(" t.stock_in,    ");
        sb.append(" t.bill_num,    ");
        sb.append(" t.ctn_num,     ");
        sb.append(" t.sku_id,      ");
        sb.append(" (              ");
        sb.append(" 	CASE t.enter_state ");
        sb.append(" 	WHEN '0' THEN      ");
        sb.append(" 		'INTACT'         ");
        sb.append(" 	WHEN '1' THEN      ");
        sb.append(" 		'BROKEN'         ");
        sb.append(" 	WHEN '2' THEN      ");
        sb.append(" 		'COVER TORN'     ");
        sb.append(" 	END                ");
        sb.append(" ) AS enter_state,    ");
        sb.append(" sum(            ");
        sb.append(" 	t.original_piece - t.remove_piece ");
        sb.append(" ) AS ruku_piece_sum     ");
        sb.append(" FROM                    ");
        sb.append("(                        ");
        sb.append("	SELECT                  ");
        sb.append("		ba.asn,               ");
        sb.append("		ba.inbound_date,      ");
        sb.append("		ba.ctn_num,           ");
        sb.append("		ba.bill_num,          ");
        sb.append("		ba.stock_in,          ");
        sb.append("		ba.is_bonded          ");
        sb.append("	FROM                    ");
        sb.append("		bis_asn ba            ");
        sb.append("	WHERE                   ");
        sb.append("		1 = 1                 ");
        sb.append("	AND ba.ASN_STATE >= '3' ");

        if (!StringUtils.isNull(itemNum)) {//提单号
            sb.append(" AND ba.bill_num in (" + billCode + ") ");
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append(" and ba.ctn_num=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        sb.append(" ) a  ");
        sb.append(" INNER JOIN ( ");
        sb.append(" SELECT   ");
        sb.append(" bt.sku_id, ");
        sb.append(" bt.asn, ");
        sb.append(" bt.bill_num,  ");
        sb.append(" bt.stock_in,  ");
        sb.append(" bt.ctn_num, ");
        sb.append(" bt.enter_state, ");
        sb.append(" bt.original_piece, ");
        sb.append(" bt.remove_piece ");
        sb.append(" FROM BIS_TRAY_INFO bt  ");
        sb.append(" WHERE 1 = 1 AND bt.cargo_state != '99' AND bt.cargo_state >= '01' ");
        if (!StringUtils.isNull(itemNum)) {//提单号
            sb.append(" AND bt.bill_num in (" + billCode + ") ");
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append(" and bt.ctn_num=:ctnnum1   ");
            parme.put("ctnnum1", cunNum);
        }
        sb.append(" ) t ON t.asn = a.asn     ");
        sb.append(" AND a.bill_num = t.bill_num   ");
        sb.append(" AND a.ctn_num = t.ctn_num ");
        sb.append(" GROUP BY  ");
        sb.append(" t.stock_in, ");
        sb.append(" t.bill_num, ");
        sb.append(" t.ctn_num,  ");
        sb.append(" t.sku_id,  ");
        sb.append(" t.enter_state, ");
        sb.append(" a.is_bonded, ");
        sb.append(" a.inbound_date  ");
        sb.append(" ) aa   ");
        sb.append(" LEFT JOIN base_client_info c ON c.ids = aa.stock_in                   ");
        sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id                ");
        sb.append("	WHERE                   ");
        sb.append("		1 = 1                 ");
        if (realClientName != null && !"".equals(realClientName)) {//--客户名称
            sb.append(" and c.REAL_CLIENT_NAME=:sockid  ");
            parme.put("sockid", realClientName);
        }
        sb.append(" ORDER BY    ");
        sb.append(" aa.bill_num,  ");
        sb.append(" aa.ctn_num,  ");
        sb.append(" aa.inbound_date ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }


}
