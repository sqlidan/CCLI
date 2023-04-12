package com.haiersoft.ccli.cost.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BisPay;
import com.haiersoft.ccli.cost.entity.vo.MidGroupVo;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class BisPayMidGroupDao extends HibernateDao<BisPay, String> {

    private final static String  STATU = "未上传";
    private final static String  NOSTATU = "已上传";

    /**
     *
     *业务付款单总览 发送到 中台
     */
    public List<Map<String, Object>> selectOneByCodeNum(List<String> payIds) {

//            ,bp.UNIT_PRICE as unitPrice      "bp.TAX_RATE as taxRate " +
            String sql ="select" +
                    " b.pay_id as payId," +
                    "b.client_id as clientId," +
                    "b.client_name as clientName," +
                    "b.pay_way as payWay," +
                    "b.pay_cycle as payCycle," +
                    "b.piece as piece," +
                    "b.price as price," +
                    "b.state as state," +
                    "b.midGroupStatic as midGroupStatic,"+
                    "bp.id as ID,"+
                    "bp.fee_code as feeCode," +
                    "bp.unit_price as unitPrice," +
                    "b.ask_date as askDate,"+
                    "bp.tax_rate as taxRate," +
                    "bp.num as num," +
                    "bs.link_id as linkId," +
                    "bs.customs_num as customsNum," +
                    "bs.currency as currEncy," +
                    "bs.RECONCILE_SIGN as reconcileSign," +
                    "bs.should_rmb as shouldRmb," +
                    "bs.hxbz as hxbz," +
                    "bs.bill_num as billNum," +
                    "bs.exchange_rate as exchangeRate," +
                    "bs.SETTLE_DATE as settleDate," +
                    "bs.input_date as inputDate," +
                    "bs.reconcile_sign as reconCilEnsign," +
                    "bs.real_rmb as realRmb " +
                    " from BIS_PAY  b  " +
                    " inner join BIS_PAY_INFO  bp ON  b.pay_id = bp.pay_id " +
                    " left join BIS_STANDING_BOOK  bs ON  bp.standing_num = bs.standing_num " +
                    " where 1=1 ";

            Map<String, Object> queryParams = new HashMap<>();
            if(CollectionUtils.isEmpty(payIds)){
                return null;
            }else{
                String sqlPing = new String();
                for (int i = 0; i < payIds.size(); i++) {
                  if(i== payIds.size()-1){
                      sqlPing+="'"+payIds.get(i)+"'";
                      break;
                  }
                  sqlPing+="'"+payIds.get(i)+"',";
                }

              sql+=" and b.pay_id in ("+sqlPing+") ";
            }
        List<Map<String, Object>> sql1 = findSqlOrSelect(sql, queryParams);

    return sql1;
    }
    /*
    * 修改上传的壮态
    * */
    public void updateStatusByPayId(String payId) {
        if(!StringUtils.isEmpty(payId)){
            StringBuffer sql = new StringBuffer();
            Map<String,Object> parems=new HashMap<>();
            sql.append("UPDATE BIS_PAY set midGroupStatic = :midGroupStatic where 1 =1 and pay_id = :payId");
            parems.put("midGroupStatic",NOSTATU);
            parems.put("payId",payId);
            SQLQuery sqlQuery=createSQLQuery(sql.toString(), parems);
            sqlQuery.executeUpdate();
        }

    }
    /*
     * 修改撤回的壮态
     * */
    public void updateNoStatusByPayId(String payId) {
        if(!StringUtils.isEmpty(payId)){
            StringBuffer sql = new StringBuffer();
            Map<String,Object> parems=new HashMap<>();
            sql.append("UPDATE BIS_PAY set midGroupStatic = :midGroupStatic where 1 =1 and pay_id = :payId");
            parems.put("midGroupStatic",STATU);
            parems.put("payId",payId);
            SQLQuery sqlQuery=createSQLQuery(sql.toString(), parems);
            sqlQuery.executeUpdate();
        }

    }
    /*
    * 根据bisPay_id 查询出所有数据
    * static ?
    *
    * */
    public List<Map<String, Object>> selectAll(List<String> ids) {
        Map<String, Object> queryParams = new HashMap<>();
        String sql = new String();
        if(!CollectionUtils.isEmpty(ids)){
            sql="SELECT PAY_ID,MIDGROUPSTATIC,CLIENT_ID from BIS_PAY where 1 =1   ";
            String sqlPing = new String();
            for (int i = 0; i < ids.size(); i++) {
                if(i== ids.size()-1){
                    sqlPing+="'"+ids.get(i)+"'";
                    break;
                }
                sqlPing+="'"+ids.get(i)+"',";
            }

                sql+=" and pay_id in ("+sqlPing+") ";
        }
        List<Map<String, Object>> sqlLe = findSqlOrSelect(sql, queryParams);

        return sqlLe;
    }


    public List<Map<String, Object>> selectBisAllByCodeNumSendOrder(List<String> payIds) {
        String sql ="select" +
                " b.pay_id as payId," +
                "b.client_name as clientName," +
                "b.client_id as clientId," +
                "b.ask_man as askMan," +
                "b.midGroupStatic as midGroupStatic,"+
                "b.ask_date as askDate "+
                " from BIS_PAY  b  " +
                " where 1=1 ";
        Map<String, Object> queryParams = new HashMap<>();
        if(CollectionUtils.isEmpty(payIds)){
            return null;
        }else{
            String sqlPing = new String();
            for (int i = 0; i < payIds.size(); i++) {
                if(i== payIds.size()-1){
                    sqlPing+="'"+payIds.get(i)+"'";
                    break;
                }
                sqlPing+="'"+payIds.get(i)+"',";
            }

            sql+=" and b.pay_id in ("+sqlPing+") ";
        }
        List<Map<String, Object>> sql1 = findSqlOrSelect(sql, queryParams);
        return sql1;
    }

    public List<String> getBillNum(String payId) {
        Map<String, Object> queryParams = new HashMap<>();
        List<String> billNums = new ArrayList<>();
        String sql = new String();
        sql ="SELECT " +
                " bp.id as ID" +
                " from BIS_PAY b" +
                " inner join BIS_PAY_INFO  bp ON  b.pay_id = bp.pay_id " +
                " left join BIS_STANDING_BOOK  bs ON  bp.standing_num = bs.standing_num " +
                " where 1=1 ";
        if(payId != null){
            sql+=" and b.pay_id = "+"'"+payId+"'";
        }
        List<Map<String, Object>> sqlLe = findSqlOrSelect(sql, queryParams);

        System.out.printf(""+sqlLe);
        for (Map<String, Object> stringObjectMap : sqlLe) {
            Object billnum = stringObjectMap.get("ID");
            String billNum = billnum.toString();
            billNums.add(billNum);
        }
        return billNums;
    }

    public void addStatementNoAndCostId(String origBizId, String statementNo) {

        Map<String, Object> queryParams = new HashMap<>();
        String arr = new String();
        String sql = new String();
        sql ="UPDATE BIS_PAY set BIS_PAY.STATEMENT_NO =:statementNo WHERE pay_id = :id  ";
        queryParams.put("statementNo",statementNo);
        queryParams.put("id",origBizId);
        SQLQuery sqlQuery=createSQLQuery(sql, queryParams);
        sqlQuery.executeUpdate();

    }

    public String selectStatementNo(String id) {
        String statementNo = "";
        Map<String, Object> queryParams = new HashMap<>();
        String sql = new String();
        sql ="SELECT BIS_PAY.STATEMENT_NO FROM BIS_PAY  WHERE pay_id = :id  ";

        queryParams.put("id",id);
        List<Map<String, Object>> sqlLe = findSqlOrSelect(sql, queryParams);
        for (Map<String, Object> stringObjectMap : sqlLe) {
           Object statementNoObj = stringObjectMap.get("STATEMENT_NO");
            if(StringUtils.isEmpty(statementNoObj)){
                return "结算单号为空";
            }
            statementNo=statementNoObj.toString();
        }
        return  statementNo;
    }
}
