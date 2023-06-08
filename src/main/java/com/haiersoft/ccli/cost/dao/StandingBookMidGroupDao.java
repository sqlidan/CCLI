package com.haiersoft.ccli.cost.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BisCheckingBook;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Repository
public class StandingBookMidGroupDao extends HibernateDao<BisStandingBook, Integer> {

    private final static Logger log = Logger.getLogger("StandingBookDao");
    private final static String  STATU = "已上传";

    /**
     * 应收对账单 上传至中台
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> selectOneByCodeNum(List<String> codeNums) {
        String sql = "SELECT\n" +
                " \tBIS_CHEKING_BOOK.CODENUM AS codeNum,\n" +
                "\tBIS_CHEKING_BOOK.CUSTOMEID AS customeId,\n" +
                "\tbb.bill_num AS billNum,\n" +
                "\tbb.fee_code AS feeCode,\n" +
                "\tbb.standing_num AS standingNum,\n" +
                "\tbb.should_rmb AS shouldRmb,\n" +
                "\tbb.real_rmb AS realRmb,\n" +
                "\tbb.tax_rate AS taxRate,\n" +
                "\tbb.currency AS currEncy,\n" +
                "\tbb.price AS price,\n" +
                "\tbb.piece AS piece,\n" +
                "\tBIS_CHEKING_BOOK.CRTIME AS crTime,\n" +
                "\tBIS_CHEKING_BOOK.JSFS AS jsfs \n" +
                "FROM\n" +
                "\tBIS_CHEKING_BOOK  \n" +
                "\tLEFT JOIN BIS_STANDING_BOOK  bb ON BIS_CHEKING_BOOK.CODENUM = bb.reconcile_num \n" +
                "WHERE\n" +
                "\t1 = 1 ";
        Map<String, Object> queryParams = new HashMap<>();
        if(CollectionUtils.isEmpty(codeNums)){
            return null;
        }else{
            String sqlPing = new String();
            for (int i = 0; i < codeNums.size(); i++) {
                if(i== codeNums.size()-1){
                    sqlPing+="'"+codeNums.get(i)+"'";
                    break;
                }
                sqlPing+="'"+codeNums.get(i)+"',";
            }
            sql+=" and BIS_CHEKING_BOOK.CODENUM in ("+sqlPing+") ";
        }
        List<Map<String, Object>> sql1 = findSqlOrSelect(sql, queryParams);
        return sql1;
    }

    /*
    * 修改上传之后的状态
    * */
    public void updateStatusByCodeNum(List<String> codeNums) {
        if(!CollectionUtils.isEmpty(codeNums)){
            codeNums.forEach(x->{
                Map<String,Object> parems=new HashMap<>();
                String  sql="UPDATE BIS_CHEKING_BOOK set midGroupStatic = :midGroupStatic where codeNum =:codeNum";
                parems.put("codeNum",x);
                SQLQuery sqlQuery=createSQLQuery(sql, parems);
                log.info("sql是什么:{}"+sql);
                sqlQuery.executeUpdate();
            });

        }

    }

    /*
    * 根据所有的 客户编号 查出对应的数据
    * */
    public List<BisStandingBook> selectAllByCust(List<Integer> customIds) {
        StringBuffer sql = new StringBuffer();
        sql.append("FROM BisStandingBook where standingNum in (:customIds)");
        Query sqlQuery1 =  createQuery(sql.toString());
        sqlQuery1.setParameterList("customIds",customIds);
        log.info("sql语句是{}"+sqlQuery1);
        List<BisStandingBook> list = sqlQuery1.list();
        return list;
    }

    public Integer selectStatuTotals() {
        StringBuffer sql = new StringBuffer();
        Map<String,Object> params = new HashMap<>();
        sql.append("SELECT count(0) from BIS_CHEKING_BOOK where midGroupStatic = :midGroupStatic ");
        params.put("midGroupStatic",STATU);
        SQLQuery sqlQuery=createSQLQuery(sql.toString(), params);
        return sqlQuery.executeUpdate();
    }

    public String queryUnitsByFeeCode(String feecodes) {
        String label = null;
        String sql = "SELECT\n" +
                "\tunit.label \n" +
                "FROM\n" +
                "\tBASE_EXPENSE_CATEGORY_INFO feecode\n" +
                "\tLEFT JOIN ( SELECT unit.label, unit.value FROM dict unit WHERE unit.TYPE = 'units' ) unit ON feecode.units = unit.value \n" +
                "WHERE\n" +
                "\tCODE =:feecodes";
        Map<String,Object> params = new HashMap<>();
        params.put("feecodes",feecodes);
        List<Map<String, Object>> sqlLe   = findSqlOrSelect(sql, params);
        for (Map<String, Object> map : sqlLe) {
           Object labels =  map.get("LABEL");
           if(!StringUtils.isEmpty(labels)){
               label= labels.toString();
               return label;
           }

        }
        return label;
    }
}
