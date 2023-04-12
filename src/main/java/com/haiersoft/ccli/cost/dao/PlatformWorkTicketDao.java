package com.haiersoft.ccli.cost.dao;

import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.common.entity.SqlAndParamVO;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.FrameworkUtil;
import com.haiersoft.ccli.cost.entity.PlatformWorkTicket;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PlatformWorkTicketDao extends HibernateDao<PlatformWorkTicket, String> {
    //query type
    public enum QueryType {
        NONE,
        PLATFORM_WORK_TICKET,
        ENTER_STEVEDORING_NO_FEE_PAID,//入库没缴费
        OUT_STEVEDORING_NO_FEE_PAID//出库没缴费
    }

    public SqlAndParamVO pageSql(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, QueryType queryType ,String orderBy){
        String sql =
                "SELECT\n" +
                        "t1.ID as id, \n" +
                        "t1.YYID as yyid, \n" +
                        "t1.TALLY_ID as tallyId, \n" +
                        "t1.FORKLIFT_UP_ID as forkliftUpId, \n" +
                        "t1.FORKLIFT_SCENE_ID as forkliftSceneId, \n" +
                        "t1.CLIENT_ID as clientId, \n" +
                        "t1.UPDATED_TIME as updatedTime, \n" +
                        "t1.DELETED_FLAG as deletedFlag, \n" +
                        "t1.STEVEDORE_ID as stevedoreId, \n" +
                        "t2.CONTAINER_NO as containerNo, \n" +
                        "t2.PLAT_NO as platNo, \n" +
                        "t1.INOUT_BOUND_FLAG as inOutBoundFlag, \n" +
                        "t1.STEVEDORE_NAME as stevedoreName, \n" +
                        "t1.ASN_TRANS_NUM as asnTransNum, \n" +
                        "t3.NAME as tallyName,  \n" +
                        "t4.NAME as forkliftUpName, \n" +
                        "t5.NAME as forkliftSceneName, \n" +
                        "t6.CLIENT_NAME as clientName, \n" +
                        "t1.CREATED_TIME as createdTime, \n" +
                        "t1.NUM_PLUS as numPlus, \n"+
                        "t1.if_All_Man as ifAllMan, \n"+
                         "( SELECT PRODUCT_NAME FROM PLATFORM_RESERVATION_INBOUND t WHERE t.yyid = t1.yyid ) inProductName ,\n" +
                        "( SELECT PRODUCT_NAME FROM PLATFORM_RESERVATION_OUTBOUND t WHERE t.yyid = t1.yyid ) outProductName, ";

                        if(QueryType.ENTER_STEVEDORING_NO_FEE_PAID==queryType){
                            String str=
                                    "nvl((SELECT TRUNC(SUM((t.original_piece-NVL(t.REMOVE_PIECE,0))*t.NET_SINGLE)/1000,1) FROM bis_tray_info t WHERE t.asn=t1.ASN_TRANS_NUM ),0) as inWeight,\n" +
                                    "0 as outWeight\n";
                            sql+=str;
                        }
                        else if(QueryType.OUT_STEVEDORING_NO_FEE_PAID==queryType){
                            String str=
                                    "0 as inWeight,\n" +
                                    "nvl((SELECT TRUNC(sum(t.NET_WEIGHT)/1000,1) as net_weight from bis_loading_info t  WHERE t.LOADING_TRUCK_NUM = t1.ASN_TRANS_NUM),0) as outWeight\n";
                            sql+=str;
                        }else if(QueryType.PLATFORM_WORK_TICKET==queryType){
                            String str=
                                    "nvl((SELECT TRUNC(SUM((t.original_piece-NVL(t.REMOVE_PIECE,0))*t.NET_SINGLE)/1000,1) FROM bis_tray_info t WHERE t.asn=t1.ASN_TRANS_NUM ),0) as inWeight,\n" +
                                    "nvl((SELECT TRUNC(sum(t.NET_WEIGHT)/1000,1) as net_weight from bis_loading_info t  WHERE t.LOADING_TRUCK_NUM = t1.ASN_TRANS_NUM),0) as outWeight\n";
                            sql+=str;
                        }
                String str2=


                        "FROM PLATFORM_WORK_TICKET t1 \n" +
                        "LEFT JOIN PLATFORM_QUEUE t2 \n" +
                        "on t1.YYID=t2.YYID \n" +

                        "LEFT JOIN USERS t3 \n" +
                        "on t1.TALLY_ID=t3.ID \n" +

                        "LEFT JOIN USERS t4 \n" +
                        "on t1.FORKLIFT_UP_ID=t4.ID \n" +

                        "LEFT JOIN USERS t5 \n" +
                        "on t1.FORKLIFT_SCENE_ID=t5.ID \n" +

                        "LEFT JOIN BASE_CLIENT_INFO t6 \n" +
                        "on t1.CLIENT_ID=t6.ids\n";
                sql+=str2;
        if(QueryType.ENTER_STEVEDORING_NO_FEE_PAID==queryType){
            String str=
                    "LEFT JOIN BIS_ENTER_STEVEDORING t7\n" +
                            "on t1.ASN_TRANS_NUM=t7.ASN_ID\n" ;
            sql+=str;
        }

        else if(QueryType.OUT_STEVEDORING_NO_FEE_PAID==queryType){
            String str=
                    "LEFT JOIN BIS_OUT_STEVEDORING t8\n" +
                            "on t1.ASN_TRANS_NUM=t8.LOADING_NUM\n" ;
            sql+=str;
        }
        String str1=
                "where (t1.DELETED_FLAG is NULL OR '0'=t1.DELETED_FLAG)\n";
        sql+=str1;

        if(QueryType.ENTER_STEVEDORING_NO_FEE_PAID==queryType){
            String str="AND t1.INOUT_BOUND_FLAG=1 AND t7.ID IS NULL and t1.if_All_Man=1 \n";
            sql+=str;
        }
        else if(QueryType.OUT_STEVEDORING_NO_FEE_PAID==queryType){
            String str="AND t1.INOUT_BOUND_FLAG=2 AND t8.ID IS NULL and t1.if_All_Man=1 \n";
            sql+=str;
        }



//        for (int i = 0; i < filters.size(); i++) {
//            PropertyFilter filter = filters.get(i);
//            String propertyName = filter.getPropertyName();
//            PropertyFilter.MatchType matchType = filter.getMatchType();
//            Object value = filter.getMatchValue();
//
//            if (matchType == PropertyFilter.MatchType.LIKE) {
//                sql += " AND " + propertyName + " LIKE :" + propertyName;
//                params.put(propertyName, "%" + value + "%");
//            } else if (matchType == PropertyFilter.MatchType.GT) {
//                sql += " AND  t1.CREATED_TIME > :createDateGt";
//                params.put("createDateGt", value);
//            } else if (matchType == PropertyFilter.MatchType.LT) {
//                sql += " AND t1.CREATED_TIME < :createDateLt";
//                params.put("createDateLt", value);
//            }
//        }
//        Map<String, Object> paramType = new HashMap<>();
//
//        paramType.put("id", String.class);
//        paramType.put("yyid", String.class);
//        paramType.put("tallyId", String.class);
//        paramType.put("forkliftUpId", String.class);
//        paramType.put("forkliftSceneId", String.class);
//        paramType.put("clientId", String.class);
//        paramType.put("updatedTime", Date.class);
//        paramType.put("deletedFlag", String.class);
//        paramType.put("stevedoreId", String.class);
//        paramType.put("containerNo", String.class);
//        paramType.put("platNo", String.class);
//        paramType.put("inOutBoundFlag", String.class);
//        paramType.put("stevedoreName", String.class);
//        paramType.put("asnTransNum", String.class);
//        paramType.put("tallyName", String.class);
//        paramType.put("forkliftUpName", String.class);
//        paramType.put("forkliftSceneName", String.class);
//        paramType.put("clientName", String.class);
//        paramType.put("createdTime", Date.class);
//        paramType.put("numPlus", String.class);

        SqlAndParamVO sqlAndParamVO=FrameworkUtil.filtersToParamsAndSQL(true,filters);
        sql+=sqlAndParamVO.strSql;
        sql+=orderBy;
        sqlAndParamVO.strSql=sql;
        return sqlAndParamVO;
    }

    public Page<PlatformWorkTicket> pageWorkTicket(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, QueryType queryType ,String orderBy) {
        SqlAndParamVO sqlAndParamVO=this.pageSql(page, filters,queryType,orderBy);
        Map<String, Object> paramType= FrameworkUtil.getPropertiesTypeMap(PlatformWorkTicket.class);

        return findPageSql(page, sqlAndParamVO.strSql, paramType, sqlAndParamVO.params);
    }

    public List<PlatformWorkTicket> getWorkTicket(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, QueryType queryType ,String orderBy) {
        SqlAndParamVO sqlAndParamVO=this.pageSql(page, filters,queryType,orderBy);
        Map<String, Object> paramType= FrameworkUtil.getPropertiesTypeMap(PlatformWorkTicket.class);
        findSql(sqlAndParamVO.strSql,paramType,sqlAndParamVO.params);
        return  findSql(sqlAndParamVO.strSql,paramType,sqlAndParamVO.params);
    }

    //理货
    public List<Map<String,Object>> findTally(String param) {

        StringBuffer sb=new StringBuffer("SELECT u.id,u.NAME FROM USERS u,USER_ROLE ur WHERE u.id=ur.USER_ID  AND ur.ROLE_ID =14   ");

        HashMap<String,Object> parme=new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(param)){
            sb.append(" and lower(name) like lower(:name)");

            parme.put("name", "%"+param+"%");

        }





        SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    //叉车
    public List<Map<String,Object>> findForklift(String param) {

        StringBuffer sb=new StringBuffer("SELECT u.id,u.NAME FROM USERS u,USER_ROLE ur WHERE u.id=ur.USER_ID  AND ur.ROLE_ID =15   ");

        HashMap<String,Object> parme=new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(param)){
            sb.append(" and lower(name) like lower(:name)");

            parme.put("name", "%"+param+"%");

        }





        SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }


    //装卸队

    public List<Map<String,Object>> findZxd(String param) {

        StringBuffer sb=new StringBuffer("SELECT bci.CLIENT_NAME as NAME ,bci.IDS as id FROM BASE_CLIENT_INFO bci WHERE bci.CLIENT_SORT =2  ");

        HashMap<String,Object> parme=new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(param)){
            sb.append(" and lower(CLIENT_NAME) like lower(:name)");

            parme.put("name", "%"+param+"%");

        }





        SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<Map<String,Object>> findZxg(String param) {

        StringBuffer sb=new StringBuffer("SELECT s.ID ,s.NAME FROM STEVEDORE s WHERE COMPANY_ID = :name ");

        HashMap<String,Object> parme=new HashMap<String,Object>();


        parme.put("name", param);




        SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public void updateWorkTicket(PlatformWorkTicket platformWorkTicket){
        String sql = "update PLATFORM_WORK_TICKET set "
                + "tally_Id = :tallyId, "
                + "forklift_Scene_Id = :forkliftSceneId, "
                + "forklift_Up_Id = :forkliftUpId, "
                + "client_Id = :clientId, "
                + "stevedore_Id = :stevedoreId, "
                + "stevedore_Name = :stevedoreName, "
                + "updated_Time = :updatedTime "

                + "where ID = :id ";
        HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("tallyId", platformWorkTicket.getTallyId());
		parme.put("forkliftSceneId", platformWorkTicket.getForkliftSceneId());
        parme.put("forkliftUpId", platformWorkTicket.getForkliftUpId());
        parme.put("clientId", platformWorkTicket.getClientId());

        parme.put("stevedoreId", platformWorkTicket.getStevedoreId());
        parme.put("stevedoreName", platformWorkTicket.getStevedoreName());
        parme.put("updatedTime", new Date());

        parme.put("id", platformWorkTicket.getId());

        SQLQuery sqlQuery=createSQLQuery(sql, parme);
		sqlQuery.executeUpdate();


    }
    private SqlAndParamVO enterWeightSql(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, QueryType queryType,String orderBy){
        String sqlFormat=
                "SELECT  TRUNC(SUM((original_piece-REMOVE_PIECE)*NET_SINGLE),2)   AS net  " +
                        "FROM (SELECT t.original_piece, NVL(REMOVE_PIECE,0) AS REMOVE_PIECE, t.asn ,t.GROSS_SINGLE,  t.NET_SINGLE  FROM bis_tray_info t   " +
                        "WHERE t.asn IN (SELECT asnTransNum from(%s) ) ) group by asn";
        SqlAndParamVO sqlAndParam=pageSql(page,filters,queryType,orderBy);
        String resultSQL=String.format(sqlFormat,sqlAndParam.strSql);
        sqlAndParam.strSql=resultSQL;
        return sqlAndParam;
    }

    public BigDecimal getEnterWeight(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, QueryType queryType,String orderBy){
        SqlAndParamVO sqlAndParamVO=enterWeightSql( page, filters,queryType,orderBy);
        SQLQuery sqlQuery = createSQLQuery(sqlAndParamVO.strSql, sqlAndParamVO.params);
        List<Map<String, Object>> mapList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if(0==mapList.size()||null==mapList.get(0).get("NET")){
            return  new BigDecimal(0);
        }else{
            return (BigDecimal)mapList.get(0).get("NET");
        }
    }

    private SqlAndParamVO outWeightSql(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, QueryType queryType,String orderBy){
        String sqlFormat=
        "select sum(t.NET_WEIGHT) as net_weight from bis_loading_info t  " +
                "where 1=1  and t.LOADING_TRUCK_NUM IN (SELECT asnTransNum from(%s) )";
        SqlAndParamVO sqlAndParam=pageSql(page,filters,queryType,orderBy);
        String resultSQL=String.format(sqlFormat,sqlAndParam.strSql);
        sqlAndParam.strSql=resultSQL;
        return sqlAndParam;
    }

    public BigDecimal getOutWeight(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, QueryType queryType,String orderBy){
        SqlAndParamVO sqlAndParamVO=outWeightSql( page, filters,queryType,orderBy);
        SQLQuery sqlQuery = createSQLQuery(sqlAndParamVO.strSql, sqlAndParamVO.params);
        List<Map<String, Object>> mapList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if(0==mapList.size()||null==mapList.get(0).get("NET_WEIGHT")){
            return  new BigDecimal(0);
        }else{
            return (BigDecimal)mapList.get(0).get("NET_WEIGHT");
        }
    }



    /**
     * 工票管理页面
     * @param page 页面
     * @param params 参数
     * @return
     */
    public Page<PlatformWorkTicket> workTicketPage(Page<PlatformWorkTicket> page, Map<String, Object> params) {

        StringBuffer buffer=new StringBuffer();
        buffer.append("SELECT ");
        buffer.append("t1.ID as id,t1.ASN_TRANS_NUM as asnTransNum,t1.STEVEDORE_NAME as stevedoreName,t1.if_All_Man as ifAllMan, t1.NUM_PLUS as numPlus,t1.INOUT_BOUND_FLAG as inOutBoundFlag, t1.CREATED_TIME as createdTime,   ");
        buffer.append("t2.CONTAINER_NO as containerNo, t2.PLAT_NO as platNo,");
        buffer.append("nvl(tInWeight.num,0) as inWeight,");
        buffer.append("nvl(tOutWeight.num,0) AS outWeight, ");
        buffer.append("t3.NAME as tallyName,t4.NAME as forkliftUpName,t5.NAME as forkliftSceneName,t6.CLIENT_NAME as clientName   ");
        buffer.append("FROM PLATFORM_WORK_TICKET t1  ");

        buffer.append("LEFT JOIN PLATFORM_QUEUE t2 on t1.YYID=t2.YYID  ");
        buffer.append("LEFT JOIN USERS t3 on t1.TALLY_ID=t3.ID  ");
        buffer.append("LEFT JOIN USERS t4 on t1.FORKLIFT_UP_ID=t4.ID  ");
        buffer.append("LEFT JOIN USERS t5 on t1.FORKLIFT_SCENE_ID=t5.ID ");
        buffer.append("LEFT JOIN BASE_CLIENT_INFO t6 on t1.CLIENT_ID=t6.ids ");
        buffer.append("LEFT JOIN (SELECT t.asn ,SUM ((t.original_piece- NVL(t.REMOVE_PIECE,0)) * t.NET_SINGLE) AS num FROM bis_tray_info t GROUP BY t.asn) tInWeight ON tInWeight.asn = t1.ASN_TRANS_NUM ");
        buffer.append("LEFT JOIN (SELECT t.LOADING_TRUCK_NUM,sum(t.NET_WEIGHT) AS num FROM bis_loading_info t GROUP BY t.LOADING_TRUCK_NUM ) tOutWeight ON tOutWeight.LOADING_TRUCK_NUM = t1.ASN_TRANS_NUM ");

        buffer.append("WHERE (t1.DELETED_FLAG is NULL OR '0'=t1.DELETED_FLAG) ");
        if(params.get("LIKES_CONTAINER_NO") != null && !"".equals(params.get("LIKES_CONTAINER_NO"))) {
            buffer.append(" AND t2.CONTAINER_NO like '%"+params.get("LIKES_CONTAINER_NO")+"%'");
        }
        if(params.get("LIKES_PLAT_NO") != null && !"".equals(params.get("LIKES_PLAT_NO"))) {
            buffer.append(" AND t2.PLAT_NO like '%"+params.get("LIKES_PLAT_NO")+"%'");
        }
        if(params.get("EQS_t1.inOut_Bound_Flag") != null && !"".equals(params.get("EQS_t1.inOut_Bound_Flag"))) {
            buffer.append(" AND t1.INOUT_BOUND_FLAG = '"+params.get("EQS_t1.inOut_Bound_Flag")+"'");
        }

        if(params.get("LIKES_T4.NAME") != null && !"".equals(params.get("LIKES_T4.NAME"))) {
            buffer.append(" AND t4.NAME like '%"+params.get("LIKES_T4.NAME")+"%'");
        }
        if(params.get("LIKES_T5.NAME") != null && !"".equals(params.get("LIKES_T5.NAME"))) {
            buffer.append(" AND t5.NAME like '%"+params.get("LIKES_T5.NAME")+"%'");
        }
        if(params.get("LIKES_STEVEDORE_NAME") != null && !"".equals(params.get("LIKES_STEVEDORE_NAME"))) {
            buffer.append(" AND t1.STEVEDORE_NAME like '%"+params.get("LIKES_STEVEDORE_NAME")+"%'");
        }
        if(params.get("EQS_t1.inOut_Bound_Flag") != null && !"".equals(params.get("EQS_t1.inOut_Bound_Flag"))) {
            buffer.append(" AND t1.INOUT_BOUND_FLAG = '"+params.get("EQS_t1.inOut_Bound_Flag")+"'");
        }
        if(params.get("LIKES_ASN_TRANS_NUM") != null && !"".equals(params.get("LIKES_ASN_TRANS_NUM"))) {
            buffer.append(" AND t1.ASN_TRANS_NUM like '%"+params.get("LIKES_ASN_TRANS_NUM")+"'");
        }
        if(params.get("LIKES_CLIENT_NAME") != null && !"".equals(params.get("LIKES_CLIENT_NAME"))) {
            buffer.append(" AND t6.CLIENT_NAME like '%"+params.get("LIKES_CLIENT_NAME")+"%'");
        }

        //筛选条件没有日期  默认为今天
        if(params.get("GTD_T1.CREATED_TIME") == null || "".equals(params.get("GTD_T1.CREATED_TIME"))) {
            if(params.get("LTD_T1.CREATED_TIME") == null || "".equals(params.get("LTD_T1.CREATED_TIME"))) {
                buffer.append(" AND TO_CHAR( t1.CREATED_TIME,'yyyy-mm-dd') = TO_CHAR( sysdate,'yyyy-mm-dd')");
            }
        }

        if(params.get("GTD_T1.CREATED_TIME") != null && !"".equals(params.get("GTD_T1.CREATED_TIME"))) {
            buffer.append(" AND TO_CHAR( t1.CREATED_TIME,'yyyy-mm-dd') >= '"+params.get("GTD_T1.CREATED_TIME")+"'");
        }

        if(params.get("LTD_T1.CREATED_TIME") != null && !"".equals(params.get("LTD_T1.CREATED_TIME"))) {
            buffer.append(" AND TO_CHAR( t1.CREATED_TIME,'yyyy-mm-dd') <= '"+params.get("LTD_T1.CREATED_TIME")+"'");
        }

        buffer.append("ORDER BY t1.INOUT_BOUND_FLAG ASC , t1.CREATED_TIME DESC  ");
        
        return findPageSql(page, buffer.toString(),null);
    }


    public List<Map<String,String>> workTicketOfExport(Map<String, Object> params) {

        StringBuffer buffer=new StringBuffer();
        buffer.append("SELECT ");
        buffer.append("t1.ID as id,t1.ASN_TRANS_NUM as asnTransNum,t1.STEVEDORE_NAME as stevedoreName,t1.if_All_Man as ifAllMan, t1.NUM_PLUS as numPlus,t1.INOUT_BOUND_FLAG as inOutBoundFlag, t1.CREATED_TIME as createdTime,   ");
        buffer.append("t2.CONTAINER_NO as containerNo, t2.PLAT_NO as platNo,");
        buffer.append("nvl(tInWeight.num,0) as inWeight,");
        buffer.append("nvl(tOutWeight.num,0) AS outWeight, ");
        buffer.append("t3.NAME as tallyName,t4.NAME as forkliftUpName,t5.NAME as forkliftSceneName,t6.CLIENT_NAME as clientName ,  ");

        buffer.append("inbound.PRODUCT_NAME inProductName,outbound.PRODUCT_NAME outProductName ");

        buffer.append("FROM PLATFORM_WORK_TICKET t1  ");

        buffer.append("LEFT JOIN PLATFORM_QUEUE t2 on t1.YYID=t2.YYID  ");
        buffer.append("LEFT JOIN USERS t3 on t1.TALLY_ID=t3.ID  ");
        buffer.append("LEFT JOIN USERS t4 on t1.FORKLIFT_UP_ID=t4.ID  ");
        buffer.append("LEFT JOIN USERS t5 on t1.FORKLIFT_SCENE_ID=t5.ID ");
        buffer.append("LEFT JOIN BASE_CLIENT_INFO t6 on t1.CLIENT_ID=t6.ids ");
        buffer.append("LEFT JOIN (SELECT t.asn ,nvl(TRUNC(SUM((t.original_piece-NVL(t.REMOVE_PIECE,0))*t.NET_SINGLE)/1000,1),0) AS num FROM bis_tray_info t GROUP BY t.asn) tInWeight ON tInWeight.asn = t1.ASN_TRANS_NUM ");
        buffer.append("LEFT JOIN (SELECT t.LOADING_TRUCK_NUM,nvl(TRUNC(sum(t.NET_WEIGHT)/1000,1),0) AS num FROM bis_loading_info t GROUP BY t.LOADING_TRUCK_NUM ) tOutWeight ON tOutWeight.LOADING_TRUCK_NUM = t1.ASN_TRANS_NUM ");
        buffer.append("LEFT JOIN PLATFORM_RESERVATION_INBOUND inbound  on inbound.yyid = t1.yyid ");
        buffer.append("LEFT JOIN PLATFORM_RESERVATION_OUTBOUND outbound on outbound.yyid = t1.yyid ");


        buffer.append("WHERE (t1.DELETED_FLAG is NULL OR '0'=t1.DELETED_FLAG) ");
        if(params.get("LIKES_CONTAINER_NO") != null && !"".equals(params.get("LIKES_CONTAINER_NO"))) {
            buffer.append(" AND t2.CONTAINER_NO like '%"+params.get("LIKES_CONTAINER_NO")+"%'");
        }
        if(params.get("LIKES_PLAT_NO") != null && !"".equals(params.get("LIKES_PLAT_NO"))) {
            buffer.append(" AND t2.PLAT_NO like '%"+params.get("LIKES_PLAT_NO")+"%'");
        }
        if(params.get("EQS_t1.inOut_Bound_Flag") != null && !"".equals(params.get("EQS_t1.inOut_Bound_Flag"))) {
            buffer.append(" AND t1.INOUT_BOUND_FLAG = '"+params.get("EQS_t1.inOut_Bound_Flag")+"'");
        }

        if(params.get("LIKES_T4.NAME") != null && !"".equals(params.get("LIKES_T4.NAME"))) {
            buffer.append(" AND t4.NAME like '%"+params.get("LIKES_T4.NAME")+"%'");
        }
        if(params.get("LIKES_T5.NAME") != null && !"".equals(params.get("LIKES_T5.NAME"))) {
            buffer.append(" AND t5.NAME like '%"+params.get("LIKES_T5.NAME")+"%'");
        }
        if(params.get("LIKES_STEVEDORE_NAME") != null && !"".equals(params.get("LIKES_STEVEDORE_NAME"))) {
            buffer.append(" AND t1.STEVEDORE_NAME like '%"+params.get("LIKES_STEVEDORE_NAME")+"%'");
        }
        if(params.get("EQS_t1.inOut_Bound_Flag") != null && !"".equals(params.get("EQS_t1.inOut_Bound_Flag"))) {
            buffer.append(" AND t1.INOUT_BOUND_FLAG = '"+params.get("EQS_t1.inOut_Bound_Flag")+"'");
        }
        if(params.get("LIKES_ASN_TRANS_NUM") != null && !"".equals(params.get("LIKES_ASN_TRANS_NUM"))) {
            buffer.append(" AND t1.ASN_TRANS_NUM like '%"+params.get("LIKES_ASN_TRANS_NUM")+"'");
        }
        if(params.get("LIKES_CLIENT_NAME") != null && !"".equals(params.get("LIKES_CLIENT_NAME"))) {
            buffer.append(" AND t6.CLIENT_NAME like '%"+params.get("LIKES_CLIENT_NAME")+"%'");
        }

        //筛选条件没有日期  默认为今天
        if(params.get("GTD_T1.CREATED_TIME") == null || "".equals(params.get("GTD_T1.CREATED_TIME"))) {
            if(params.get("LTD_T1.CREATED_TIME") == null || "".equals(params.get("LTD_T1.CREATED_TIME"))) {
                buffer.append(" AND TO_CHAR( t1.CREATED_TIME,'yyyy-mm-dd') = TO_CHAR( sysdate,'yyyy-mm-dd')");
            }
        }

        if(params.get("GTD_T1.CREATED_TIME") != null && !"".equals(params.get("GTD_T1.CREATED_TIME"))) {
            buffer.append(" AND TO_CHAR( t1.CREATED_TIME,'yyyy-mm-dd') >= '"+params.get("GTD_T1.CREATED_TIME")+"'");
        }

        if(params.get("LTD_T1.CREATED_TIME") != null && !"".equals(params.get("LTD_T1.CREATED_TIME"))) {
            buffer.append(" AND TO_CHAR( t1.CREATED_TIME,'yyyy-mm-dd') <= '"+params.get("LTD_T1.CREATED_TIME")+"'");
        }

        buffer.append("ORDER BY t1.INOUT_BOUND_FLAG ASC , t1.CREATED_TIME DESC  ");

        return findSql( buffer.toString(),null);
    }
}