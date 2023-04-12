package com.haiersoft.ccli.wms.dao;

import java.text.ParseException;
import java.util.*;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.wms.entity.BisAsn;

@Repository
public class ASNDao extends HibernateDao<BisAsn, String> {

    //asn点击完结时计算出入库费用
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> crFee(Map<String, Object> params) {
        StringBuffer sb = new StringBuffer("select sum(tray.original_piece - tray.remove_piece) as piece,sum((tray.original_piece - tray.remove_piece)*tray.net_single) as netWeight ,sum((tray.original_piece - tray.remove_piece)*tray.gross_single) as grossWeight  ");
        sb.append(" from bis_tray_info tray  ");
        sb.append(" where tray.asn = :asn and tray.cargo_state <> '99' ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    //asn点击完结时计算分拣费用
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> fjFee(Map<String, Object> params) {
        StringBuffer sb = new StringBuffer("select sum(tray.original_piece - tray.remove_piece) as piece,sum((tray.original_piece - tray.remove_piece)*tray.net_single) as netWeight ,sum((tray.original_piece - tray.remove_piece)*tray.gross_single) as grossWeight  ");
        sb.append(" from bis_tray_info tray  ");
        sb.append(" where tray.asn = :asn and tray.cargo_state <> '99' ");
        sb.append(" group by tray.sku_id");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 根据提单号获取asn里订单号
     *
     * @param billNum
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOrderNo(String billNum) {
        if (billNum != null && !"".equals(billNum)) {
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuffer sb = new StringBuffer("select a.order_num from bis_asn a where a.bill_num=:billNum and a.order_num is not null ");
            params.put("billNum", billNum);
            SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
            return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAanT(String asnId,String state){
    	StringBuffer sb=new StringBuffer();
    	Map<String, Object> map=new HashMap<String, Object>();
    	sb.append("SELECT                                                                                       "); 
    	sb.append(" t.asn,                                                                                      ");
        sb.append(" listagg(t.CARGO_LOCATION,',') WITHIN GROUP (order by t.CARGO_LOCATION )AS cargo_location,");
    	//sb.append("	TO_CHAR(wmsys.wm_concat(DISTINCT t.CARGO_LOCATION)) AS cargo_location,                      ");
    	sb.append("	SUM(t.original_piece-NVL(t.REMOVE_PIECE,0)) AS num,                                         ");
    	sb.append("	TRUNC(SUM((t.original_piece-NVL(t.REMOVE_PIECE, 0))*t.GROSS_SINGLE),2) AS gross,            ");
    	sb.append("	TRUNC(SUM((t.original_piece-NVL(t.REMOVE_PIECE, 0))*t.NET_SINGLE),2) AS net,                ");
    	sb.append(" TRUNC(SUM((t.original_piece-NVL(t.REMOVE_PIECE, 0))*t.GROSS_SINGLE)/1000,2) AS zgross,      ");
    	sb.append("	TRUNC(SUM((t.original_piece-NVL(t.REMOVE_PIECE, 0))*t.NET_SINGLE)/1000,2) AS znet           ");
    	sb.append("FROM                                                                                         ");
    	sb.append("	bis_tray_info t                                                                             ");
    	sb.append("WHERE                                                                                        ");
    	sb.append("	t.asn=:asnId                                                                                ");
    	if(state!=null&&!"".equals(state)){
    		map.put("state",state);
    		sb.append(" and t.CARGO_STATE=:state");
    	}
    	sb.append(" AND (t.cargo_state = '01' OR t.cargo_state = '10') ");
    	sb.append(" AND t.now_piece != 0 ");
    	sb.append("GROUP BY                                                                                     ");
    	sb.append("	t.asn                                                                                       ");
    	map.put("asnId",asnId);
    	SQLQuery sqlQuery = createSQLQuery(sb.toString(),map);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 获取ASN实际入库数量
     *
     * @param asnIds
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getASNPNum(String asnIds) {
        if (asnIds != null && !"".equals(asnIds)) {
            StringBuffer sb = new StringBuffer("SELECT ASN,SUM(original_piece-REMOVE_PIECE) AS pnum,0 as pieceinfo, "
                    + " TRUNC(SUM((original_piece-REMOVE_PIECE)*GROSS_SINGLE),2) AS gross, "
                    + " TRUNC(SUM((original_piece-REMOVE_PIECE)*NET_SINGLE),2)   AS net "
                    + " FROM (SELECT t.original_piece, NVL(REMOVE_PIECE,0) AS REMOVE_PIECE, t.asn ,t.GROSS_SINGLE,  t.NET_SINGLE "
                    + " FROM bis_tray_info t   WHERE t.asn IN (");
            sb.append(asnIds).append(" ) ) group by asn ");
            SQLQuery sqlQuery = createSQLQuery(sb.toString(), new HashMap<String, Object>());
            return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRkAsnByTime(String[] aa, String[] bb) throws ParseException {
        StringBuffer sb = new StringBuffer(" select distinct LINK_ID from bis_asn where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (null != aa[0] && !"".equals(aa)) {
            String a = aa[0];
            sb.append(" and INBOUND_DATE > :aa ");
            params.put("aa", DateUtils.stringToDate(a));
        }
        if (null != bb[0] && !"".equals(bb)) {
            String b = bb[0];
            sb.append(" and INBOUND_DATE < :bb ");
            params.put("bb", DateUtils.stringToDate(b));
        }
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getinfonum(String asn) {
        StringBuffer sb = new StringBuffer(" select nvl(sum(t.piece),0) as piece from bis_asn_info t where t.ASN_ID=?0");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), asn);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public Page<BisAsn> getAllASN(Page<BisAsn> page, BisAsn bisAsn) {

        String sql = "select " +
                "t.ASN as asn," +
                "t.ASN_STATE as asnState," +
                "t.CTN_NUM as ctnNum," +
                "t.TRANSPORT_TIME as transportTime," +
                "t.ORDER_NUM as orderNum," +
                "t.LINK_ID as linkId," +
                "t.STOCK_IN as stockIn," +
                "t.BILL_NUM as billNum," +
                "t.REMARK as remark," +
                "t.MARK as mark," +
                "t.TALLY_USER as tallyUser," +
                "t.IF_SECOND_ENTER as ifSecondEnter," +
                "t.INBOUND_DATE as inboundTime," +
                "t.STOCK_NAME as stockName," +
                "t.IS_BONDED as isBonded," +
                "t.WAREHOUSE_ID as warehouseId," +
                "t.WAREHOUSE as warehouse," +
                "t.IF_PLAN_TIME as ifPlanTime,t.CREATE_TIME as createTime" +
                " from BIS_ASN t" +
                " where 1 = 1";

        Map<String, Object> queryParams = new HashMap<>();

        if (StringUtils.nonNull(bisAsn.getAsn())) {
            sql += " and t.ASN like :asn";
            queryParams.put("asn", like(bisAsn.getAsn()));
        }

        if (StringUtils.nonNull(bisAsn.getBillNum())) {
            sql += " and t.BILL_NUM like :billNum";
            queryParams.put("billNum", like(bisAsn.getBillNum()));
        }

        if (StringUtils.nonNull(bisAsn.getLinkId())) {
            sql += "and t.LINK_ID like :linkId";
            queryParams.put("linkId", like(bisAsn.getLinkId()));
        }

        if (StringUtils.nonNull(bisAsn.getCtnNum())) {
            sql += " and t.CTN_NUM like :ctnNum";
            queryParams.put("ctnNum", like(bisAsn.getCtnNum()));
        }

        if (StringUtils.nonNull(bisAsn.getStockIn())) {
            sql += " and t.STOCK_IN =:stockIn";
            queryParams.put("stockIn", bisAsn.getStockIn());
        }

        if (StringUtils.nonNull(bisAsn.getIfSecondEnter())) {
            sql += " and t.IF_SECOND_ENTER like :ifSecondEnter";
            queryParams.put("ifSecondEnter", like(bisAsn.getIfSecondEnter()));
        }

        if (StringUtils.nonNull(bisAsn.getAsnState())) {
            sql += " and t.ASN_STATE like :asnState";
            queryParams.put("asnState", like(bisAsn.getAsnState()));
        }
        sql+=" order by t.CREATE_TIME desc nulls last";
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("asn", String.class);
        paramType.put("asnState", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("transportTime", Date.class);
        paramType.put("orderNum", String.class);
        paramType.put("linkId", String.class);
        paramType.put("stockIn", String.class);
        paramType.put("stockName", String.class);
        paramType.put("billNum", String.class);
        paramType.put("remark", String.class);
        paramType.put("mark", String.class);
        paramType.put("tallyUser", String.class);
        paramType.put("ifSecondEnter", String.class);
        paramType.put("inboundTime", Date.class);
        paramType.put("isBonded", String.class);
        paramType.put("warehouseId", String.class);
        paramType.put("warehouse", String.class);
        paramType.put("ifPlanTime", Integer.class);
        paramType.put("createTime", Date.class);         return findPageSql(page, sql, paramType, queryParams);
    }
    
    
    /*
     * 入库作业列表展示ASN
     */
    
    public Page<BisAsn> getAsnToWork(Page<BisAsn> page, BisAsn bisAsn) {
        String sql = "select " +
                "t.ASN as asn," +
                "t.ASN_STATE as asnState," +
                "t.CTN_NUM as ctnNum," +
                "t.TRANSPORT_TIME as transportTime," +
                "t.ORDER_NUM as orderNum," +
                "t.LINK_ID as linkId," +
                "t.STOCK_IN as stockIn," +
                "t.BILL_NUM as billNum," +
                "t.REMARK as remark," +
                "t.MARK as mark," +
                "t.TALLY_USER as tallyUser," +
                "t.IF_SECOND_ENTER as ifSecondEnter," +
                "t.INBOUND_DATE as inboundTime," +
                "t.STOCK_NAME as stockName," +
                "t.IS_BONDED as isBonded," +
                "t.WAREHOUSE_ID as warehouseId," +
                "t.WAREHOUSE as warehouse," +
                "t.IF_PLAN_TIME as ifPlanTime," +
                "t.WORK_MAN as workMan," +
                "t.LH_PERSON as lhPerson," +
                "t.CC_PERSON as ccPerson," +
                "t.CC_PERSON2 as ccPerson2," +
                "t.REMIND_ID as remindId," +
                "t.RULE_JOB_TYPE as ruleJobType " +
                " from BIS_ASN t" +
                " where 1 = 1 ";

        Map<String, Object> queryParams = new HashMap<>();

        if (StringUtils.nonNull(bisAsn.getAsn())) {
            sql += " and t.ASN like :asn";
            queryParams.put("asn", like(bisAsn.getAsn()));
        }

        if (StringUtils.nonNull(bisAsn.getBillNum())) {
            sql += " and t.BILL_NUM like :billNum";
            queryParams.put("billNum", like(bisAsn.getBillNum()));
        }

        if (StringUtils.nonNull(bisAsn.getLinkId())) {
            sql += " and t.LINK_ID like :linkId";
            queryParams.put("linkId", like(bisAsn.getLinkId()));
        }

        if (StringUtils.nonNull(bisAsn.getCtnNum())) {
            sql += " and t.CTN_NUM like :ctnNum";
            queryParams.put("ctnNum", like(bisAsn.getCtnNum()));
        }

        if (StringUtils.nonNull(bisAsn.getStockIn())) {
            sql += " and t.STOCK_IN =:stockIn";
            queryParams.put("stockIn", bisAsn.getStockIn());
        }

        if (StringUtils.nonNull(bisAsn.getIfSecondEnter())) {
            sql += " and t.IF_SECOND_ENTER like :ifSecondEnter";
            queryParams.put("ifSecondEnter", like(bisAsn.getIfSecondEnter()));
        }

        if (StringUtils.nonNull(bisAsn.getAsnState())) {
            sql += " and t.ASN_STATE like :asnState";
            queryParams.put("asnState", like(bisAsn.getAsnState()));
        }else{
        	sql += "and t.ASN_STATE ='1'";
        }
        
        sql+=" order by t.ASN desc";
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("asn", String.class);
        paramType.put("asnState", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("transportTime", Date.class);
        paramType.put("orderNum", String.class);
        paramType.put("linkId", String.class);
        paramType.put("stockIn", String.class);
        paramType.put("stockName", String.class);
        paramType.put("billNum", String.class);
        paramType.put("remark", String.class);
        paramType.put("mark", String.class);
        paramType.put("tallyUser", String.class);
        paramType.put("ifSecondEnter", String.class);
        paramType.put("inboundTime", Date.class);
        paramType.put("isBonded", String.class);
        paramType.put("warehouseId", String.class);
        paramType.put("warehouse", String.class);
        paramType.put("ifPlanTime", Integer.class);
        paramType.put("workMan", String.class);
        paramType.put("lhPerson", String.class);
        paramType.put("ccPerson", String.class);
        paramType.put("ccPerson2", String.class);
        paramType.put("ruleJobType", String.class);

        return findPageSql(page, sql, paramType, queryParams);
    }

    @SuppressWarnings("unchecked")
	public Map<String, String> getPiece1Gross1NetCount(BisAsn bisAsn) {

//        String sql =
//                "SELECT " +
//                "SUM(original_piece - REMOVE_PIECE) AS pnum, " +
//                "0 AS pieceinfo, " +
//                "TRUNC( SUM((original_piece - REMOVE_PIECE) * GROSS_SINGLE), 2) AS gross, " +
//                "TRUNC( SUM((original_piece - REMOVE_PIECE) * NET_SINGLE), 2) AS net " +
//                "FROM" +
//                "( " +
//                "SELECT t.original_piece, " +
//                "NVL(REMOVE_PIECE, 0) AS REMOVE_PIECE," +
//                " t.asn," +
//                " t.GROSS_SINGLE, " +
//                " t.NET_SINGLE FROM bis_tray_info t where 1 = 1 ";

        String sql = "SELECT SUM(original_piece - REMOVE_PIECE) AS pnum, TRUNC( SUM((original_piece - REMOVE_PIECE) * GROSS_SINGLE), 2) AS gross, TRUNC( SUM((original_piece - REMOVE_PIECE) * NET_SINGLE), 2) AS net FROM( SELECT T .original_piece, NVL(REMOVE_PIECE, 0) AS REMOVE_PIECE, T .asn, T .GROSS_SINGLE, T .NET_SINGLE FROM bis_tray_info T WHERE T .asn IN( SELECT T .ASN AS asn FROM BIS_ASN T WHERE 1 = 1 ";

        Map<String, Object> queryParams = new HashMap<>();

        if (StringUtils.nonNull(bisAsn.getAsn())) {
            sql += " and t.ASN like :asn";
            queryParams.put("asn", like(bisAsn.getAsn()));
        }

        if (StringUtils.nonNull(bisAsn.getBillNum())) {
            sql += " and t.BILL_NUM like :billNum";
            queryParams.put("billNum", like(bisAsn.getBillNum()));
        }

        if (StringUtils.nonNull(bisAsn.getLinkId())) {
            sql += "and t.LINK_ID like :linkId";
            queryParams.put("linkId", like(bisAsn.getLinkId()));
        }

        if (StringUtils.nonNull(bisAsn.getCtnNum())) {
            sql += " and t.CTN_NUM like :ctnNum";
            queryParams.put("ctnNum", like(bisAsn.getCtnNum()));
        }

        if (StringUtils.nonNull(bisAsn.getStockIn())) {
            sql += " and t.STOCK_IN =:stockIn";
            queryParams.put("stockIn", bisAsn.getStockIn());
        }

        if (StringUtils.nonNull(bisAsn.getIfSecondEnter())) {
            sql += " and t.IF_SECOND_ENTER like :ifSecondEnter";
            queryParams.put("ifSecondEnter", like(bisAsn.getIfSecondEnter()));
        }

        if (StringUtils.nonNull(bisAsn.getAsnState())) {
            sql += " and t.ASN_STATE like :asnState";
            queryParams.put("asnState", like(bisAsn.getAsnState()));
        }

        sql += " )) ";

        SQLQuery sqlQuery = createSQLQuery(sql, queryParams);

        List<Map<String, String>> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

        return list.get(0);
    }

    private static String like(String str) {
        return "%" + str + "%";
    }

		
		//获取入库的净重合集
		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> getinfoNet(String asn) {
			StringBuffer sb=new StringBuffer(" select nvl(sum((t.original_piece - t.remove_piece)*t.net_single),0) as netWeight from bis_tray_info t where t.ASN=?0");
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), asn);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		
		//获取入库的毛重合集
		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> getinfoGross(String asn) {
			StringBuffer sb=new StringBuffer(" select nvl(sum((t.original_piece - t.remove_piece)*t.gross_single),0) as grossWeight from bis_tray_info t where t.ASN=?0");
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), asn);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}

		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> getAsnWithRemind(String userName) {
			StringBuffer sb=new StringBuffer();
			sb.append(" select t.asn," +
						"t.ctn_num as ctnNum " +
						"from (select r.link_id " +
						"from base_remind_task r " +
						"where r.user_name=?0 " +
						"and r.type='3') a "+
						"inner join bis_asn t on a.link_id=t.asn " +
						"where t.asn_state < '3'");
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), userName);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		
		
		
		public String updateASNforNewCtnNum(String ctnNumNew,String ctnNumOld,String linkId) {

	        Map<String, String> params = new HashMap<>();
	        String sql = "update BIS_ASN t set t.ctn_num =:ctnNumNew where t.ctn_num=:ctnNumOld and t.link_id=:linkId";

	        if (StringUtils.nonNull(ctnNumNew)) {
	            
	            params.put("ctnNumNew", ctnNumNew);
	        }else{
	        	params.put("ctnNumNew", "");
	        }
	        
	        if (StringUtils.nonNull(ctnNumOld)) {
	            
	            params.put("ctnNumOld", ctnNumOld);
	        }else{
	        	params.put("ctnNumOld", "");
	        }

	        
	        if (StringUtils.nonNull(linkId)) {
	            
	            params.put("linkId", linkId);
	        }else{
	        	params.put("linkId", "");
	        }
	        
	        SQLQuery sqlQuery = createSQLQuery(sql, params);
	        int rows = sqlQuery.executeUpdate();

	        return rows > 0 ? "success" : "";
	    }
		
		
}

