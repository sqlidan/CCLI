package com.haiersoft.ccli.wms.dao;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import java.util.HashMap;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;

/**
 * @author Connor.M
 * @ClassName: LoadingInfofDao
 * @Description: 装车单 DAO
 * @date 2016年3月11日 上午10:42:06
 */
@Repository
public class LoadingInfoDao extends HibernateDao<BisLoadingInfo, Integer> {
    /**
     * @param loadingCode
     * @return
     * @throws
     * @author Connor.M
     * @Description: 根据装车单统计  按ASN分组
     * @date 2016年3月24日 下午4:19:15
     */
    @SuppressWarnings("unchecked")
    public List<BisLoadingInfo> getSumLoadingCode(String loadingCode) {
        StringBuffer sb = new StringBuffer(""
                + " SELECT "
                + "	t.ASN_ID AS asnId, "
                + " t.sku_id AS skuId, "
                + " t.LOADING_PLAN_NUM as loadingPlanNum, "
                + " t.OUT_LINK_ID as outLinkId, "
                + " t.STOCK_ID as stockId, "
                + " NVL(SUM(t.PIECE),0) AS piece, "
                + " NVL(sum(t.NET_WEIGHT),0) AS netWeight, "
                + " NVL(sum(t.GROSS_WEIGHT),0) AS grossWeight "
                + " FROM BIS_LOADING_INFO t "
                + " WHERE 1 = 1 ");
        sb.append(" AND t.LOADING_TRUCK_NUM = ?0 ");
        //装车单状态为已装车（2）的  状态：0 已分配 1 已拣货 2 已装车 3已置换,4待回库,5回库理货,6已回库
        sb.append(" AND t.LOADING_STATE = '2' ");
        sb.append(" GROUP BY t.ASN_ID, t.sku_id, t.LOADING_PLAN_NUM, t.OUT_LINK_ID, t.STOCK_ID ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), loadingCode);

        sqlQuery.addScalar("asnId", StandardBasicTypes.STRING);//ASN
        sqlQuery.addScalar("skuId", StandardBasicTypes.STRING);//ASN
        sqlQuery.addScalar("loadingPlanNum", StandardBasicTypes.STRING);//出库订单
        sqlQuery.addScalar("outLinkId", StandardBasicTypes.STRING);//出库联系单ID
        sqlQuery.addScalar("stockId", StandardBasicTypes.STRING);//客户ID

        sqlQuery.addScalar("piece", StandardBasicTypes.INTEGER);//数量 的总和
        sqlQuery.addScalar("netWeight", StandardBasicTypes.DOUBLE);//净重 的总和
        sqlQuery.addScalar("grossWeight", StandardBasicTypes.DOUBLE);//毛重 的总和
        sqlQuery.setResultTransformer(Transformers.aliasToBean(BisLoadingInfo.class));
        return sqlQuery.list();
    }

    /**
     * 根据订单编号删除装车单对象集合
     *
     * @param ordCode 订单编号
     */
    public void deleteLoadingInfo(String ordCode) {
        if (ordCode != null && !"".equals(ordCode)) {
            String hql = "delete BisLoadingInfo where loadingPlanNum=?0 ";
            batchExecute(hql, ordCode);
        }
    }

    /**
     * 根据订单编号获取装车单对象集合
     *
     * @param ordCode 订单编号
     * @return
     */
    public List<BisLoadingInfo> getLoadingInfoIfOrd(String ordCode) {
        List<BisLoadingInfo> getList = null;
        if (ordCode != null && !"".equals(ordCode)) {
            String sHQL = "from BisLoadingInfo where loadingPlanNum=:ordCode";
            HashMap<String, Object> parme = new HashMap<String, Object>();
            parme.put("ordCode", ordCode);
            getList = this.find(sHQL, parme);
        }
        return getList;
    }

    /**
     * 装车单号获取所有状态的出库装车单信息对象集合
     *
     * @return
     */
    public List<BisLoadingInfo> getLoadingInfoIfTruckNum(String truckNum) {
        List<BisLoadingInfo> getList = null;
        if (truckNum != null && !"".equals(truckNum)) {
            String sHQL = "from BisLoadingInfo where loadingTruckNum=:TruckNum order by roomNum,ctnNum,skuId";
            HashMap<String, Object> parme = new HashMap<String, Object>();
            parme.put("TruckNum", truckNum);
            getList = this.find(sHQL, parme);
        }
//        if (truckNum != null && !"".equals(truckNum)) {
//        	String sql="select trayId"
//        }
        return getList;
    }

    /**
     * @return
     * @throws
     * @author Connor.M
     * @Description: 根据提单号    获得出库  总数量，总净重，总毛重
     * @date 2016年4月20日 下午3:32:50
     */
    public List<BisLoadingInfo> getSumNumByBillNum(BisLoadingInfo loadingInfo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer(""
                + " SELECT "
                + " l.bill_num as billNum,"
                + " SUM(NVL(l.piece,0)) as piece,"
                + " SUM(NVL(l.net_weight,0)) as netWeight,"
                + " SUM(NVL(l.gross_weight,0)) as grossWeight"
                + " FROM bis_loading_info l "
                + " WHERE l.loading_state = '2' ");

        sql.append(" AND l.bill_num = :billNum ");
        params.put("billNum", loadingInfo.getBillNum());

        sql.append(" GROUP BY l.bill_num ");

        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("billNum", String.class);//提单号
        parm.put("piece", Integer.class);//现有 总件数
        parm.put("netWeight", Double.class);//总净重
        parm.put("grossWeight", Double.class);//总毛重

        return findSql(sql.toString(), parm, params);
    }

    /**
     * @param truckNum
     * @return
     * @throws
     * @author PYL
     * @Description: 根据装车号，按小类分类获得总净重和总毛重
     * @date 2016年5月19日
     */
    @SuppressWarnings("unchecked")
	public List<BisLoadingInfo> getQingDan(String truckNum) {
        StringBuffer sb = new StringBuffer(""
                + " SELECT "
                + " sum(a.PIECE) as piece, "
                + "	sum(a.net_weight) as netWeight, "
                + " sum(a.gross_weight) as grossWeight, "
                + " a.CTN_NUM as ctnNum, "
                + " b.PRODUCING_AREA as producingArea "
                + " from bis_loading_info a "
                + " inner join base_sku_base_info b on a.sku_id = b.sku_id ");
        sb.append(" where a.loading_truck_num = ?0 and a.LOADING_STATE = '2' ");
        sb.append(" GROUP BY a.CTN_NUM,b.PRODUCING_AREA ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), truckNum);
        sqlQuery.addScalar("piece", StandardBasicTypes.INTEGER);//
        sqlQuery.addScalar("ctnNum", StandardBasicTypes.STRING);//
        sqlQuery.addScalar("netWeight", StandardBasicTypes.DOUBLE);//
        sqlQuery.addScalar("grossWeight", StandardBasicTypes.DOUBLE);//
        sqlQuery.addScalar("producingArea", StandardBasicTypes.STRING);//
        sqlQuery.setResultTransformer(Transformers.aliasToBean(BisLoadingInfo.class));
        return sqlQuery.list();
    }

    /**
     * 根据出库联系单号获取订单标记为最后一车
     *
     * @param outLinkCode
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> isLastCar(String outLinkCode) {
        StringBuffer sb = new StringBuffer("select ord.order_num from bis_loading_order ord where ord.last_car=1  and ord.out_link_id=:outLinkCode ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("outLinkCode", outLinkCode);
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findallnum(String loadingPlanNum,
                                                String loadingTruckNum, String outLinkId, String billNum,
                                                String carNo, String operator, String stockId) {
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append("select sum(t.PIECE) as piece,sum(t.NET_WEIGHT) as net_weight,sum(t.GROSS_WEIGHT) as gross_weight from bis_loading_info t ");
        sb.append(" where 1=1 ");
        if (!StringUtils.isNull(loadingPlanNum)) {
            sb.append(" and t.LOADING_PLAN_NUM = :loadingPlanNum ");
            parme.put("loadingPlanNum", loadingPlanNum);
        }
        if (!StringUtils.isNull(loadingTruckNum)) {
            sb.append(" and t.LOADING_TRUCK_NUM = :loadingTruckNum ");
            parme.put("loadingTruckNum", loadingTruckNum);
        }
        if (!StringUtils.isNull(outLinkId)) {
            sb.append(" and t.OUT_LINK_ID = :outLinkId ");
            parme.put("outLinkId", outLinkId);
        }
        if (!StringUtils.isNull(billNum)) {
            sb.append(" and t.BILL_NUM = :billNum ");
            parme.put("billNum", billNum);
        }
        if (!StringUtils.isNull(carNo)) {
            sb.append(" and t.CAR_NO = :carNo ");
            parme.put("carNo", carNo);
        }
        if (!StringUtils.isNull(operator)) {
            sb.append(" and t.OPERATOR = :operator ");
            parme.put("operator", operator);
        }
        if (!StringUtils.isNull(stockId)) {
            sb.append(" and t.STOCK_ID = :stockId ");
            parme.put("stockId", stockId);
        }
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLoadingTime(String[] aaa, String[] bbb) throws ParseException {
        StringBuffer sb = new StringBuffer(" select distinct OUT_LINK_ID from BIS_LOADING_INFO where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (null != aaa[0] && !"".equals(aaa)) {
            String a = aaa[0] + " " + aaa[1] + ":" + aaa[2] + ":" + aaa[3];
            sb.append(" and LOADING_TIME > :aa ");
            params.put("aa", DateUtils.stringToDate2(a));
        }
        if (null != bbb[0] && !"".equals(bbb)) {
            String b = bbb[0] + " " + bbb[1] + ":" + bbb[2] + ":" + bbb[3];
            sb.append(" and LOADING_TIME < :bb ");
            params.put("bb", DateUtils.stringToDate2(b));
        }
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    //将备注加入装车单
    public void insertRemark(String ordId, String sode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ordId", ordId);
        params.put("sode", sode);
        StringBuffer sb = new StringBuffer("" +
                " update bis_loading_info l set l.REMARK = ( " +
                " select s.remark1 from bis_loading_order_info s  " +
                " where l.bill_num = s.bill_num and l.ctn_num = s.ctn_num and l.sku_id = s.sku_id " +
                " and s.LOADING_PLAN_NUM =:ordId )" +
                " where l.LOADING_TRUCK_NUM = :sode ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
        sqlQuery.executeUpdate();
    }

    /**
     * 获取装车单号对应的托盘信息
     *
     * @param truckNum 装车号
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, String>> getOrderInfo(String truckNum) {

        Map<String, Object> loadingInfoParams = new HashMap<String, Object>();
        loadingInfoParams.put("num", truckNum);

        String sql = "select b.TRAY_ID,b.LOADING_STATE from bis_loading_info b where b.LOADING_TRUCK_NUM = :num  ";

        SQLQuery sqlQuery = createSQLQuery(sql, loadingInfoParams);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    // 已回库 "6"
    private static final String LOADING_STATE_BACK = "6";

    // 已上架 "01"
    private static final String CARGO_STATE_UP = "01";

    /**
     * 通过托盘号修改装车单表和库存表信息
     *
     * @param trayId 托盘号
     */
    public void updateLoadingInfoAndTrayInfo(String trayId) {

        // 修改装车单表相关信息,将装车状态loading_state改为6(已回库)
        Map<String, Object> loadingInfoParams = new HashMap<String, Object>();
        loadingInfoParams.put("id", trayId);
        loadingInfoParams.put("state", LOADING_STATE_BACK);
        String loadingInfoSQL = "update BIS_LOADING_INFO t set t.LOADING_STATE = :state where t.TRAY_ID = :id";
        SQLQuery loadingInfoSQLQuery = createSQLQuery(loadingInfoSQL, loadingInfoParams);
        loadingInfoSQLQuery.executeUpdate();

        // 修改库存表,将库存状态改为01(已上架),锁定状态改为0(未锁定)
        Map<String, Object> trayInfoParams = new HashMap<String, Object>();
        trayInfoParams.put("id", trayId);
        trayInfoParams.put("state", CARGO_STATE_UP);
        String trayInfoSQL = "update BIS_TRAY_INFO t set t.CARGO_STATE = :state , t.ISTRUCK = '0' where t.TRAY_ID = :id";
        SQLQuery trayInfoSQLQuery = createSQLQuery(trayInfoSQL, trayInfoParams);
        trayInfoSQLQuery.executeUpdate();
    }

    /**
     * 当出库订单表(BIS_LOADING_ORDER)修改车牌号时,同时修改装车信息表(BIS_LOADING_INFO)表中的车牌号
     * @param orderNum
     * @param carNum
     */
    public void updateLoadingInfoCarNumWithLoadingOrderChanged(String orderNum, String carNum) {
        String sql = "update BIS_LOADING_INFO set CAR_NO = :carNum where LOADING_PLAN_NUM = :orderNum";

        Map<String, Object> params = new HashMap<>();
        params.put("orderNum", orderNum);
        params.put("carNum", carNum);

        SQLQuery sqlQuery = createSQLQuery(sql,params);
        sqlQuery.executeUpdate();

    }

    /*
     * 出库作业列表展示装车单
     */
    
    public Page<BisLoadingInfo> getLoadingToWork(Page<BisLoadingInfo> page, BisLoadingInfo bisLoadingInfo) {
        String sql ="select "+
        		"ta.workMan as workMan,"+
        		"ta.lhPerson as lhPerson,"+
        		"ta.ccPerson as ccPerson,"+
        		"ta.ccPerson2 as ccPerson2,"+
        		"ta.loadingTruckNum as loadingTruckNum,"+
        		"ord.rule_job_type as ruleJobType,"+
        		"ta.loadingPlanNum as loadingPlanNum,"+
        		"ta.outLinkId as outLinkId,"+
        		"ta.carNo as carNo,"+
        		"ta.piece as piece,"+
        		"tb.piece as jpiece,"+
        		"tb.netWeight as netWeight,"+
        		"tb.grossWeight as grossWeight, "+
        		"ta.loadingTime  as loadingTime "+
        		"from " +
        		"( select " +
        		"min(l.work_man) as workMan,"+
        		"min(l.lh_person) as lhPerson,"+
        		"min(l.cc_person) as ccPerson,"+
        		"min(l.cc_person2) as ccPerson2,"+
                "l.loading_truck_num as loadingTruckNum," +
        		"min(l.RULE_JOB_TYPE) as ruleJobType, "+
                "min(l.loading_plan_num) as loadingPlanNum," +
                "min(l.out_link_id) as outLinkId," +
                "min(l.car_no) as carNo," +
                "sum(l.piece) as piece, " +
                "max(l.library_ope_time) as libraryOpeTime, "+
                "max(l.loading_time) as loadingTime "+
                " from bis_loading_info l" +
                " where l.loading_state <> '3' group by l.loading_truck_num ) ta "+
                "left join " +
                "( select " +
                "sum(h.piece) as piece," +
                "h.loading_truck_num as loadingTruckNum," +
                "sum(h.net_weight) as netWeight," +
                "sum(h.gross_weight) as grossWeight" +
                " from bis_loading_info h" +
                " where h.loading_state = '2' or h.loading_state = '6' group by h.loading_truck_num ) tb on "+
                "ta.loadingTruckNum = tb.loadingTruckNum  left join bis_loading_order ord on ord.order_num=ta.loadingPlanNum where 1 = 1 ";
        
        Map<String, Object> queryParams = new HashMap<>();
        if(StringUtils.isNull(bisLoadingInfo.getLoadingTruckNum()) &&
           StringUtils.isNull(bisLoadingInfo.getLoadingPlanNum()) &&
           StringUtils.isNull(bisLoadingInfo.getOutLinkId())){
        	
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            Date date=new Date();  
            Calendar calendar = Calendar.getInstance();  
            calendar.setTime(date);  
            calendar.add(Calendar.DAY_OF_MONTH, -2);  
            date = calendar.getTime();  
            //System.out.println(sdf.format(date));
            sql += " and to_char(ta.libraryOpeTime,'yyyy-MM-dd HH:mm:SS') >= :libraryOpeTime ";
            queryParams.put("libraryOpeTime", sdf.format(date));
            sql+=" order by ta.loadingTruckNum desc";
        	
        }else{
        	if (StringUtils.nonNull(bisLoadingInfo.getLoadingTruckNum())) {
                sql += " and ta.loadingTruckNum like :loadingNum";
                queryParams.put("loadingNum",  like(bisLoadingInfo.getLoadingTruckNum()) );
            }

            if (StringUtils.nonNull(bisLoadingInfo.getLoadingPlanNum())) {
                sql += " and ta.loadingPlanNum like :orderNum";
                queryParams.put("orderNum", like(bisLoadingInfo.getLoadingPlanNum()));
            }

            if (StringUtils.nonNull(bisLoadingInfo.getOutLinkId())) {
                sql += " and ta.outLinkId like :linkId";
                queryParams.put("linkId", like(bisLoadingInfo.getOutLinkId()));
            }
            sql+=" order by ta.loadingTruckNum desc";
        }
        
        
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("workMan", String.class);
        paramType.put("lhPerson", String.class);
        paramType.put("ccPerson", String.class);
        paramType.put("ccPerson2", String.class);
        paramType.put("loadingTruckNum", String.class);
        paramType.put("ruleJobType", String.class);
        paramType.put("loadingPlanNum", String.class);
        paramType.put("outLinkId", String.class);
        paramType.put("carNo", String.class);
        paramType.put("piece", Integer.class);
        paramType.put("netWeight", Double.class);
        paramType.put("grossWeight", Double.class);
        paramType.put("jpiece", Integer.class);
        paramType.put("loadingTime", Date.class);
        return findPageSql(page, sql, paramType, queryParams);
    }
    
    /*	0821修改方法备份
     * public Page<BisLoadingInfo> getLoadingToWork0821(Page<BisLoadingInfo> page, BisLoadingInfo bisLoadingInfo) {
        String sql = "select " +
        		"min(l.work_man) as workMan,"+
        		"min(l.lh_person) as lhPerson,"+
        		"min(l.cc_person) as ccPerson,"+
        		"min(l.cc_person2) as ccPerson2,"+
                "l.loading_truck_num as loadingTruckNum," +
        		"min(l.RULE_JOB_TYPE) as ruleJobType, "+
                "min(l.loading_plan_num) as loadingPlanNum," +
                "min(l.out_link_id) as outLinkId ," +
                "min(l.car_no) as carNo," +
                "sum(l.piece) as piece," +
                "sum(l.net_weight) as netWeight," +
                "sum(l.gross_weight) as grossWeight" +
                "  from bis_loading_info l" +
                "  where l.loading_state='0'  ";

        Map<String, Object> queryParams = new HashMap<>();

        if (StringUtils.nonNull(bisLoadingInfo.getLoadingTruckNum())) {
            sql += " and l.loading_truck_num like :loadingNum";
            queryParams.put("loadingNum",  like(bisLoadingInfo.getLoadingTruckNum()) );
        }

        if (StringUtils.nonNull(bisLoadingInfo.getLoadingPlanNum())) {
            sql += " and l.loading_plan_num like :orderNum";
            queryParams.put("orderNum", like(bisLoadingInfo.getLoadingPlanNum()));
        }

        if (StringUtils.nonNull(bisLoadingInfo.getOutLinkId())) {
            sql += "and l.out_link_id like :linkId";
            queryParams.put("linkId", like(bisLoadingInfo.getOutLinkId()));
        }
        sql+= " group by l.loading_truck_num ";
        sql+=" order by l.loading_truck_num desc";
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("workMan", String.class);
        paramType.put("lhPerson", String.class);
        paramType.put("ccPerson", String.class);
        paramType.put("ccPerson2", String.class);
        paramType.put("loadingTruckNum", String.class);
        paramType.put("ruleJobType", String.class);
        paramType.put("loadingPlanNum", String.class);
        paramType.put("outLinkId", String.class);
        paramType.put("carNo", String.class);
        paramType.put("piece", Integer.class);
        paramType.put("netWeight", Double.class);
        paramType.put("grossWeight", Double.class);
        return findPageSql(page, sql, paramType, queryParams);
    }
    */
    /*
     * 重收ASN关联装车单号
     */
    
    public Page<BisLoadingInfo> getLoadingToAsn(Page<BisLoadingInfo> page, BisLoadingInfo bisLoadingInfo) {
        String sql = "select " +
                "l.loading_truck_num as loadingTruckNum," +
        		"(min(l.LOADING_TIME)+1) as loadingTime," +
                "min(l.loading_plan_num) as loadingPlanNum," +
                "min(l.out_link_id) as outLinkId ," +
                "min(l.car_no) as carNo," +
                "sum(l.piece) as piece " +
                "  from bis_loading_info l" +
                "  where l.loading_state='2'  ";

        Map<String, Object> queryParams = new HashMap<>();

        if (StringUtils.nonNull(bisLoadingInfo.getLoadingTruckNum())) {
            sql += " and l.loading_truck_num like :loadingNum";
            queryParams.put("loadingNum",  like(bisLoadingInfo.getLoadingTruckNum()) );
        }

        if (StringUtils.nonNull(bisLoadingInfo.getLoadingPlanNum())) {
            sql += " and l.loading_plan_num like :orderNum";
            queryParams.put("orderNum", like(bisLoadingInfo.getLoadingPlanNum()));
        }

        if (StringUtils.nonNull(bisLoadingInfo.getOutLinkId())) {
            sql += "and l.out_link_id like :linkId";
            queryParams.put("linkId", like(bisLoadingInfo.getOutLinkId()));
        }
        sql+= " group by l.loading_truck_num ";
        sql+=" order by l.loading_truck_num desc";
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("loadingTruckNum", String.class);
        paramType.put("loadingTime", Date.class);
        paramType.put("loadingPlanNum", String.class);
        paramType.put("outLinkId", String.class);
        paramType.put("carNo", String.class);
        paramType.put("piece", Integer.class);
        return findPageSql(page, sql, paramType, queryParams);
    }
    
    
    private static String like(String str) {
        return "%" + str + "%";
    }

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLoadingWithRemind(String userName) {
		StringBuffer sb=new StringBuffer();
		sb.append(" select remind.loading_num as LOADING_TRUCK_NUM " +
				" from (select r.link_id ,r.loading_num from " +
				"base_remind_task r " +
				"where r.user_name=?0 " +
				"and r.type='4') remind " +
				 " inner join bis_loading_order ord on ord.ORDER_NUM=remind.link_id "+
					" where ord.ORDER_STATE<'4' ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), userName);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

    public List<Map<String, Object>> listFloorTray(String billNum, String ctnNum, String skuNum) {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("  select t.floor_num floorNum,        ");
        sbSQL.append("         t.bill_num billNum,          ");
        sbSQL.append("         t.ctn_num ctnNum,            ");
        sbSQL.append("         t.sku_id skuId,              ");
        sbSQL.append("         sum(t.now_piece) allNum      ");
        sbSQL.append("    from ccli.bis_tray_info t         ");
        sbSQL.append("   where t.now_piece > 0              ");
        sbSQL.append("     and t.istruck = '0'              ");
        sbSQL.append("     and t.cargo_state = '01'         ");
        sbSQL.append("     and t.ctn_num = :ctnNum          ");
        sbSQL.append("     and t.bill_num = :billNum        ");
        sbSQL.append("     and t.sku_id = :skuId            ");
        sbSQL.append("   group by t.floor_num, t.bill_num, t.ctn_num, t.sku_id       ");
        sbSQL.append("   order by t.floor_num               ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("ctnNum", ctnNum);
        params.put("billNum", billNum);
        params.put("skuId", skuNum);

        SQLQuery sqlQuery = createSQLQuery(sbSQL.toString(), params);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
