package com.haiersoft.ccli.supervision.dao;


import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BasePlatform;
import com.haiersoft.ccli.base.entity.BaseProductClass;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.supervision.entity.BaseAccount;
import com.haiersoft.ccli.supervision.entity.BaseAccountExcel;
import com.haiersoft.ccli.supervision.entity.ManiInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

;import java.math.BigDecimal;
import java.util.*;

@Repository
public class BaseAccountDao extends HibernateDao<BaseAccount, String> {

    /**
     * 获取底账明细
     * @param page 页面
     * @param params 参数
     * @return
     */
    public Page<BaseAccount> seachSql(Page<BaseAccount> page, Map<String, Object> params) {
        StringBuffer sb=new StringBuffer();


        sb.append(" select pr.* from (                                         ");
        sb.append(" SELECT                                                ");
        sb.append("   ba.id,                                      ");
        sb.append("   ba.STOCK_TYPE as stockType,                    ");
        sb.append("   ba.CREATED_TIME  as appointDate,                    ");
        sb.append("   ba.BILL_NO  as billNo,                  ");
        sb.append("   ba.CONTAINER_NO as containerNo,                  ");
        sb.append("   ba.PRODUCT_TYPE as productType,                   ");
        sb.append("   ba.PRODUCT_NAME as productName,                   ");
        sb.append("   ba.NUM  as num,                  ");
        sb.append("   ba.WEIGHT as weight,                  ");
        sb.append("   pro.STATUS as status                 ");
        sb.append("  from              ");
        sb.append("   BASE_ACCOUNT ba left join PLATFORM_RESERVATION_OUTBOUND pro on ba.STOCK_ID = pro.ID                ");
        sb.append(" where  ba.STOCK_TYPE = '1'              ");
        if(null!=params) {
            if(null!=params.get("billNo")&&!"".equals(params.get("billNo"))){
                sb.append(" and ba.BILL_NO like '%"+params.get("billNo")+"%'");
            }
            if(null!=params.get("containerNo")&&!"".equals(params.get("containerNo"))){
                sb.append(" and ba.CONTAINER_NO = "+params.get("containerNo")+"");
            }
            if (null!=params.get("appointDateStart") && !"".equals(params.get("appointDateStart"))

            && null!=params.get("appointDateEnd") && !"".equals(params.get("appointDateEnd"))
            ) {
                sb.append("	AND ba.CREATED_TIME BETWEEN to_date( '" + params.get("appointDateStart") + "', 'yyyy-mm-dd hh24:mi:ss' ) ");
                sb.append("	AND to_date( '" + params.get("appointDateEnd") + "', 'yyyy-mm-dd hh24:mi:ss' ) ");
            }

            if(null!=params.get("status")&&!"".equals(params.get("status"))){
                sb.append(" and pro.STATUS = "+params.get("status")+"");
            }
        }
        sb.append("  UNION ALL                   ");
        sb.append("  select                    ");
        sb.append("   ba.id,                        ");
        sb.append("   ba.STOCK_TYPE  as stockType,                    ");
        sb.append("   ba.CREATED_TIME as appointDate,                   ");
        sb.append("  ba.BILL_NO as billNo,                  ");
        sb.append("  ba.CONTAINER_NO as containerNo,                  ");
        sb.append("  ba.PRODUCT_TYPE  as productType,                  ");
        sb.append("  ba.PRODUCT_NAME as productName,                   ");
        sb.append("   ba.NUM      as num,                 ");
        sb.append("    ba.WEIGHT as weight,                  ");
        sb.append("    pri.STATUS as status                      ");
        sb.append("     from                          ");
        sb.append("     BASE_ACCOUNT ba left join PLATFORM_RESERVATION_inBOUND pri on ba.STOCK_ID = pri.ID ");
        sb.append("     where ba.STOCK_TYPE = 0    ");
        if(null!=params) {
            if(null!=params.get("billNo")&&!"".equals(params.get("billNo"))){
                sb.append(" and ba.BILL_NO like '%"+params.get("billNo")+"%'");
            }
            if(null!=params.get("containerNo")&&!"".equals(params.get("containerNo"))){
                sb.append(" and ba.CONTAINER_NO = "+params.get("containerNo")+"");
            }
            if (null!=params.get("appointDateStart") && !"".equals(params.get("appointDateStart"))

                    && null!=params.get("appointDateEnd") && !"".equals(params.get("appointDateEnd"))
            ) {
                sb.append("	AND ba.CREATED_TIME BETWEEN to_date( '" + params.get("appointDateStart") + "', 'yyyy-mm-dd hh24:mi:ss' ) ");
                sb.append("	AND to_date( '" + params.get("appointDateEnd") + "', 'yyyy-mm-dd hh24:mi:ss' ) ");
            }

            if(null!=params.get("status")&&!"".equals(params.get("status"))){
                sb.append(" and pri.STATUS = "+params.get("status")+"");
            }
        }
        sb.append("   ) pr                  ");
        sb.append("  ORDER BY  pr.ID ASC                 ");

        Map<String, Object> paramType = new HashMap<>();
        paramType.put("id", Integer.class);
        paramType.put("stockType", String.class);
        paramType.put("appointDate", Date.class);
        paramType.put("billNo", String.class);
        paramType.put("containerNo", String.class);
        paramType.put("productType", String.class);
        paramType.put("productName", String.class);
        paramType.put("num", String.class);
        paramType.put("weight", String.class);
        paramType.put("status", String.class);


        return findPageSql(page, sb.toString(),paramType,null);
    }


    /**
     * 获取底账明细页面
     * @param params 参数
     * @return
     */
    public List<BaseAccount> seachSql(Map<String, Object> params) {

        StringBuffer sb=new StringBuffer();


        sb.append(" select pr.* from (                                         ");
        sb.append(" SELECT                                                ");
        sb.append("   ba.id,                                      ");
        sb.append("   case when ba.STOCK_TYPE = '0' then '入库' when ba.STOCK_TYPE = '1' then '出库' END  AS stockType,                    ");
        sb.append("   ba.CREATED_TIME  as appointDate,                    ");
        sb.append("   ba.BILL_NO  as billNo,                  ");
        sb.append("   ba.CONTAINER_NO as containerNo,                  ");
        sb.append("   case when ba.PRODUCT_TYPE = '1' then '水产'  when ba.PRODUCT_TYPE = '2' then '肉类' else '其他' end  AS productType,                   ");
        sb.append("   ba.PRODUCT_NAME as productName,                   ");
        sb.append("   ba.NUM  as num,                  ");
        sb.append("   ba.WEIGHT as weight,                  ");
        sb.append("   case when pro.STATUS = '0' then '已保存' when pro.STATUS = '1' then '已入闸' when pro.STATUS = '2' then '已出闸' when pro.STATUS = '3' then '已取消' end AS status                ");
        sb.append("  from              ");
        sb.append("    BASE_ACCOUNT ba left join PLATFORM_RESERVATION_OUTBOUND pro on ba.STOCK_ID = pro.ID                ");
        sb.append(" where  ba.STOCK_TYPE = '1'              ");
        if(null!=params) {
            if(null!=params.get("billNo")&&!"".equals(params.get("billNo"))){
                sb.append(" and ba.BILL_NO like '%"+params.get("billNo")+"%'");
            }
            if(null!=params.get("containerNo")&&!"".equals(params.get("containerNo"))){
                sb.append(" and ba.CONTAINER_NO ='"+params.get("containerNo")+"'");
            }
            if (null!=params.get("appointDateStart") && !"".equals(params.get("appointDateStart"))

                    && null!=params.get("appointDateEnd") && !"".equals(params.get("appointDateEnd"))
            ) {
                sb.append("	AND ba.CREATED_TIME BETWEEN to_date( '" + params.get("appointDateStart") + "', 'yyyy-mm-dd hh24:mi:ss' ) ");
                sb.append("	AND to_date( '" + params.get("appointDateEnd") + "', 'yyyy-mm-dd hh24:mi:ss' ) ");
            }

            if(null!=params.get("status")&&!"".equals(params.get("status"))){
                sb.append(" and pro.STATUS = '"+params.get("status")+"'");
            }
        }
        sb.append("  UNION ALL                   ");
        sb.append("  select                    ");
        sb.append("   ba.id,                        ");
        sb.append("   case when ba.STOCK_TYPE = '0' then '入库' when ba.STOCK_TYPE = '1' then '出库' END  AS stockType,                    ");
        sb.append("    ba.CREATED_TIME as appointDate,                   ");
        sb.append("  ba.BILL_NO as billNo,                  ");
        sb.append("  ba.CONTAINER_NO as containerNo,                  ");
        sb.append("  case when ba.PRODUCT_TYPE = '1' then '水产'  when ba.PRODUCT_TYPE = '2' then '肉类' else '其他' end  AS productType,                ");
        sb.append("  ba.PRODUCT_NAME as productName,                   ");
        sb.append("   ba.NUM      as num,                 ");
        sb.append("    ba.WEIGHT as weight,                  ");
        sb.append("    case when pri.STATUS = '0' then '已保存' when pri.STATUS = '1' then '已入闸' when pri.STATUS = '2' then '已出闸' when pri.STATUS = '3' then '已取消' end AS status ");
        sb.append("     from                          ");
        sb.append("     BASE_ACCOUNT ba left join PLATFORM_RESERVATION_inBOUND pri on ba.STOCK_ID = pri.ID ");
        sb.append("     where ba.STOCK_TYPE = 0    ");
        if(null!=params) {
            if(null!=params.get("billNo")&&!"".equals(params.get("billNo"))){
                sb.append(" and ba.BILL_NO like '%"+params.get("billNo")+"%'");
            }
            if(null!=params.get("containerNo")&&!"".equals(params.get("containerNo"))){
                sb.append(" and ba.CONTAINER_NO ='"+params.get("containerNo")+"'");
            }
            if (null!=params.get("appointDateStart") && !"".equals(params.get("appointDateStart"))

                    && null!=params.get("appointDateEnd") && !"".equals(params.get("appointDateEnd"))
            ) {
                sb.append("	AND ba.CREATED_TIME  BETWEEN to_date( '" + params.get("appointDateStart") + "', 'yyyy-mm-dd hh24:mi:ss' ) ");
                sb.append("	AND to_date( '" + params.get("appointDateEnd") + "', 'yyyy-mm-dd hh24:mi:ss' ) ");
            }

            if(null!=params.get("status")&&!"".equals(params.get("status"))){
                sb.append(" and pri.STATUS = '"+params.get("status")+"'");
            }
        }
        sb.append("   ) pr                  ");
        sb.append("  ORDER BY  pr.ID ASC                 ");


        SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
        sqlQuery.addScalar("id", StandardBasicTypes.INTEGER);
        sqlQuery.addScalar("stockType", StandardBasicTypes.STRING);
        sqlQuery.addScalar("appointDate", StandardBasicTypes.DATE);
        sqlQuery.addScalar("billNo", StandardBasicTypes.STRING);
        sqlQuery.addScalar("containerNo", StandardBasicTypes.STRING);
        sqlQuery.addScalar("productType", StandardBasicTypes.STRING);
        sqlQuery.addScalar("productName", StandardBasicTypes.STRING);
        sqlQuery.addScalar("num", StandardBasicTypes.STRING);
        sqlQuery.addScalar("weight", StandardBasicTypes.STRING);
        sqlQuery.addScalar("status", StandardBasicTypes.STRING);
        List<BaseAccount> baseAccountList = sqlQuery.setResultTransformer(Transformers.aliasToBean(BaseAccount.class)).list();
        return baseAccountList;
    }


    /**
     * 获取底账汇总统计
     * @param page 页面
     * @param params 参数
     * @return
     */
    public Page<BaseAccount> seachStatisticsSql(Page<BaseAccount> page, Map<String, Object> params) {
        StringBuffer sb=new StringBuffer();


        sb.append(" SELECT                  ");
        sb.append(" ba.BILL_NO as billNo,                                            ");
        sb.append(" ba.CONTAINER_NO as containerNo,                                     ");
        sb.append("  ba.PRODUCT_TYPE as productType,                    ");
        sb.append("  ba.PRODUCT_NAME as productName,                    ");
        sb.append("  ba.R_SUMNUM  as rSumnum,                   ");
        sb.append("  ba.R_SUMWEIGHT as rSumweight,                  ");
        sb.append("  ba.R_SUMNUM-c.C_SUMNUM as surplusNum,                   ");
        sb.append("  ba.R_SUMWEIGHT-c.C_SUMWEIGHT as surplusWeight                  ");
        sb.append("   from (                 ");
        sb.append("  SELECT               ");
        sb.append("   ba.BILL_NO ,                 ");
        sb.append("   ba.CONTAINER_NO ,          ");
        sb.append("   ba.PRODUCT_TYPE,     ");
        sb.append("   ba.PRODUCT_NAME,            ");
        sb.append("  sum(ba.NUM) AS r_sumnum, ");
        sb.append("  sum(ba.WEIGHT) AS R_SUMWEIGHT            ");
        sb.append("  FROM           ");
        sb.append("  BASE_ACCOUNT ba  where          ");
        sb.append("  ba.STOCK_TYPE = '0'           ");
        if(null!=params) {
            if(null!=params.get("billNo")&&!"".equals(params.get("billNo"))) {
                sb.append(" and ba.BILL_NO like '%"+params.get("billNo")+"%'");
            }
            if(null!=params.get("containerNo")&&!"".equals(params.get("containerNo"))) {
                sb.append(" and ba.CONTAINER_NO = "+params.get("containerNo")+"");
            }
        }
        sb.append(" GROUP BY         ");
        sb.append(" ba.BILL_NO ,          ");
        sb.append("  ba.CONTAINER_NO ,          ");

        sb.append("  ba.PRODUCT_TYPE,                  ");
        sb.append("  ba.PRODUCT_NAME ) ba                    ");
        sb.append("   left join                         ");
        sb.append("  (SELECT                    ");
        sb.append("  ba.STOCK_TYPE,                   ");
        sb.append("  ba.BILL_NO ,                  ");
        sb.append("  ba.CONTAINER_NO ,                  ");
        sb.append("  ba.PRODUCT_TYPE ,                  ");
        sb.append("  ba.PRODUCT_NAME ,                   ");
        sb.append("  sum(ba.NUM) AS C_SUMNUM,                 ");
        sb.append("  sum(ba.WEIGHT) AS C_SUMWEIGHT                ");
        sb.append("  FROM                     ");
        sb.append("  BASE_ACCOUNT ba  where           ");
        sb.append("    ba.STOCK_TYPE = '1'                        ");
        if(null!=params) {
            if(null!=params.get("billNo")&&!"".equals(params.get("billNo"))){
                sb.append(" and ba.BILL_NO like '%"+params.get("billNo")+"%'");
            }
            if(null!=params.get("containerNo")&&!"".equals(params.get("containerNo"))){
                sb.append(" and ba.CONTAINER_NO = "+params.get("containerNo")+"");
            }
        }
        sb.append("   GROUP BY ");
        sb.append("  ba.STOCK_TYPE,  ");
        sb.append("  ba.BILL_NO ,    ");
        sb.append("  ba.CONTAINER_NO ,             ");
        sb.append("  ba.PRODUCT_TYPE,                 ");
        sb.append("  ba.PRODUCT_NAME) c                ");
        sb.append("  on ba.BILL_NO = c.BILL_NO              ");
        sb.append("  and ba.CONTAINER_NO = c.CONTAINER_NO            ");
        sb.append("  and  ba.PRODUCT_TYPE = c.PRODUCT_TYPE               ");
        sb.append("  and ba.PRODUCT_NAME = c.PRODUCT_NAME               ");
        Map<String, Object> paramType = new HashMap<>();

        paramType.put("billNo", String.class);
        paramType.put("containerNo", String.class);
        paramType.put("productType", String.class);
        paramType.put("productName", String.class);
        paramType.put("rSumnum", String.class);
        paramType.put("rSumweight", String.class);
        paramType.put("surplusNum", String.class);
        paramType.put("surplusWeight", String.class);

        return findPageSql(page, sb.toString(),paramType,null);
    }


    /**
     * 获取底账汇总统计
     * @param params 参数
     * @return
     */
    public List<BaseAccountExcel> seachStatisticsSql( Map<String, Object> params) {
        StringBuffer sb=new StringBuffer();


        sb.append(" SELECT                  ");
        sb.append(" ba.BILL_NO as billNo,                                            ");
        sb.append(" ba.CONTAINER_NO as containerNo,                                     ");
        sb.append("  ba.PRODUCT_TYPE as productType,                    ");
        sb.append("  ba.PRODUCT_NAME as productName,                    ");
        sb.append("  ba.R_SUMNUM  as rSumnum,                   ");
        sb.append("  ba.R_SUMWEIGHT as rSumweight,                  ");
        sb.append("  ba.R_SUMNUM-c.C_SUMNUM as surplusNum,                   ");
        sb.append("  ba.R_SUMWEIGHT-c.C_SUMWEIGHT as surplusWeight                  ");
        sb.append("   from (                 ");
        sb.append("  SELECT               ");
        sb.append("   ba.BILL_NO ,                 ");
        sb.append("   ba.CONTAINER_NO ,          ");
        sb.append("   ba.PRODUCT_TYPE,     ");
        sb.append("   ba.PRODUCT_NAME,            ");
        sb.append("  sum(ba.NUM) AS r_sumnum, ");
        sb.append("  sum(ba.WEIGHT) AS R_SUMWEIGHT            ");
        sb.append("  FROM           ");
        sb.append("  BASE_ACCOUNT ba  where          ");
        sb.append("  ba.STOCK_TYPE = '0'           ");
        if(null!=params) {
            if(null!=params.get("billNo")&&!"".equals(params.get("billNo"))) {
                sb.append(" and ba.BILL_NO like '%"+params.get("billNo")+"%'");
            }
            if(null!=params.get("containerNo")&&!"".equals(params.get("containerNo"))) {
                sb.append(" and ba.CONTAINER_NO = '"+params.get("containerNo")+"'");
            }
        }
        sb.append(" GROUP BY         ");
        sb.append(" ba.BILL_NO ,          ");
        sb.append("  ba.CONTAINER_NO ,          ");

        sb.append("  ba.PRODUCT_TYPE,                  ");
        sb.append("  ba.PRODUCT_NAME ) ba                    ");
        sb.append("   left join                         ");
        sb.append("  (SELECT                    ");
        sb.append("  ba.STOCK_TYPE,                   ");
        sb.append("  ba.BILL_NO ,                  ");
        sb.append("  ba.CONTAINER_NO ,                  ");
        sb.append("  ba.PRODUCT_TYPE ,                  ");
        sb.append("  ba.PRODUCT_NAME ,                   ");
        sb.append("  sum(ba.NUM) AS C_SUMNUM,                 ");
        sb.append("  sum(ba.WEIGHT) AS C_SUMWEIGHT                ");
        sb.append("  FROM                     ");
        sb.append("  BASE_ACCOUNT ba  where           ");
        sb.append("    ba.STOCK_TYPE = '1'                        ");
        if(null!=params) {
            if(null!=params.get("billNo")&&!"".equals(params.get("billNo"))){
                sb.append(" and ba.BILL_NO like '%"+params.get("billNo")+"%'");
            }
            if(null!=params.get("containerNo")&&!"".equals(params.get("containerNo"))){
                sb.append(" and ba.CONTAINER_NO = '"+params.get("containerNo")+"'");
            }
        }
        sb.append("   GROUP BY ");
        sb.append("  ba.STOCK_TYPE,  ");
        sb.append("  ba.BILL_NO ,    ");
        sb.append("  ba.CONTAINER_NO ,             ");
        sb.append("  ba.PRODUCT_TYPE,                 ");
        sb.append("  ba.PRODUCT_NAME) c                ");
        sb.append("  on ba.BILL_NO = c.BILL_NO              ");
        sb.append("  and ba.CONTAINER_NO = c.CONTAINER_NO            ");
        sb.append("  and  ba.PRODUCT_TYPE = c.PRODUCT_TYPE               ");
        sb.append("  and ba.PRODUCT_NAME = c.PRODUCT_NAME               ");
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());

        sqlQuery.addScalar("billNo", StandardBasicTypes.STRING);
        sqlQuery.addScalar("containerNo", StandardBasicTypes.STRING);
        sqlQuery.addScalar("productType", StandardBasicTypes.STRING);
        sqlQuery.addScalar("productName", StandardBasicTypes.STRING);
        sqlQuery.addScalar("rSumnum", StandardBasicTypes.STRING);
        sqlQuery.addScalar("rSumweight", StandardBasicTypes.STRING);
        sqlQuery.addScalar("surplusNum", StandardBasicTypes.STRING);
        sqlQuery.addScalar("surplusWeight", StandardBasicTypes.STRING);

        List<BaseAccountExcel> baseAccountList = sqlQuery.setResultTransformer(Transformers.aliasToBean(BaseAccountExcel.class)).list();
        return baseAccountList;
    }


    /**
     * 通过id获取底账明细
     * @param id
     * @return
     */
    public  BaseAccount queryBaseAccountInfo(Integer id) {
        BaseAccount  baseAccount= new BaseAccount();
        StringBuffer sql =new StringBuffer();
        sql.append("SELECT ba.ID,ba.NUM ,ba.WEIGHT FROM BASE_ACCOUNT ba WHERE ");
        sql.append("ba.ID = " + id );
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql.toString());
        Object[] aa = (Object[]) sqlQuery.uniqueResult();
        if(aa[0]!=null||aa[1]!=null) {
            baseAccount.setId(Integer.valueOf(String.valueOf(aa[0])));
            baseAccount.setNum(aa[1].toString());
            baseAccount.setWeight( aa[2].toString());
        }
        return baseAccount;
    }


    /**
     * 通过id修改底账明细
     * @param baseAccount
     * @return
     */
    public  void updateById(BaseAccount baseAccount) {
        StringBuffer sql =new StringBuffer();
        sql.append("update BASE_ACCOUNT  ");
        sql.append("set NUM = " + baseAccount.getNum() + ", ");
        sql.append(" WEIGHT = " + baseAccount.getWeight() + " ");
        sql.append("WHERE");
        sql.append("	id = " + baseAccount.getId() + "");
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql.toString());
        sqlQuery.executeUpdate();
    }

    /**
     * 通过id获取底账明细
     * @param billNo
     * @param containerNo
     * @return
     */
    public  BaseAccount selectByBillNoAndCno(String billNo, String containerNo) {
        BaseAccount  baseAccount= new BaseAccount();
        StringBuffer sql =new StringBuffer();
        sql.append("select    ");
        sql.append("  ba.id ,    ");
        sql.append("  ba.BILL_NO ,            ");
        sql.append("  ba.CONTAINER_NO ,       ");
        sql.append("  ba.STOCK_ID,          ");
        sql.append("  ba.STOCK_TYPE,          ");
        sql.append("  ba.PRODUCT_TYPE ,       ");
        sql.append("  ba.PRODUCT_NAME ,       ");
        sql.append("  ba.NUM ,                ");
        sql.append("  ba.WEIGHT ,             ");
        sql.append("  ba.CREATED_TIME ,       ");
        sql.append("  ba.UPDATED_TIME ,       ");
        sql.append("  ba.REMARK      ");
        sql.append("  from BASE_ACCOUNT  ba   ");
        sql.append("WHERE");
        sql.append("  ba.BILL_NO = '" + billNo + "' ");

        sql.append("  and ba.CONTAINER_NO =  '" +containerNo+" ' ");
        sql.append("  and ba.STOCK_TYPE = '1' ");

        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql.toString());
        Object[] aa = (Object[]) sqlQuery.uniqueResult();
        if (aa!=null) {
            baseAccount.setId(Integer.valueOf(String.valueOf(aa[0])));
            baseAccount.setBillNo(aa[1].toString());
            baseAccount.setContainerNo(aa[2].toString());
            baseAccount.setStockId(aa[3].toString());
            baseAccount.setStockType(aa[4].toString());
            baseAccount.setProductType(aa[5].toString());
            baseAccount.setProductName(aa[6].toString());
            baseAccount.setNum(aa[7].toString());
            baseAccount.setWeight( aa[8].toString());
        }
        return baseAccount;
    }

    /**
     * 通过id修改预约出库字段 是否已拆分
     * @param id
     * @return
     */
    public  void updateOutBoundIsSplit(String id) {
        StringBuffer sql =new StringBuffer();
        sql.append("update PLATFORM_RESERVATION_OUTBOUND  ");
        sql.append("set IS_SPLIT = '1' ");
        sql.append("WHERE");
        sql.append("	id = " + id + "");
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql.toString());
        sqlQuery.executeUpdate();
    }


    /**
     * 获取底账明细
     * @param page 页面
     * @param params 参数
     * @return
     */
    public Page<BaseAccount> seachData(Page<BaseAccount> page, Map<String, Object> params) {
        StringBuffer sb=new StringBuffer();
        sb.append("  select                    ");
        sb.append("   ba.id,                        ");
        sb.append("   ba.STOCK_TYPE  as stockType,                    ");
        sb.append("   ba.CREATED_TIME as appointDate,                   ");
        sb.append("  ba.BILL_NO as billNo,                  ");
        sb.append("  ba.CONTAINER_NO as containerNo,                  ");
        sb.append("  ba.PRODUCT_TYPE  as productType,                  ");
        sb.append("  ba.PRODUCT_NAME as productName,                   ");
        sb.append("  ba.NUM      as num,                 ");
        sb.append("  ba.WEIGHT as weight,                  ");
        sb.append("  pri.STATUS as status                      ");
        sb.append("     from                          ");
        sb.append("  BASE_ACCOUNT ba left join PLATFORM_RESERVATION_inBOUND pri on ba.STOCK_ID = pri.ID ");
        sb.append("  where ba.STOCK_TYPE = '0'    ");
        if(null!=params) {
            if(null!=params.get("billNo")&&!"".equals(params.get("billNo"))){
                sb.append(" and ba.BILL_NO like '%"+params.get("billNo")+"%'");
            }
            if(null!=params.get("containerNo")&&!"".equals(params.get("containerNo"))){
                sb.append(" and ba.CONTAINER_NO = '"+params.get("containerNo")+"'");
            }
        }
        sb.append("  ORDER BY  ba.ID ASC                 ");
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("id", Integer.class);
        paramType.put("stockType", String.class);
        paramType.put("appointDate", Date.class);
        paramType.put("billNo", String.class);
        paramType.put("containerNo", String.class);
        paramType.put("productType", String.class);
        paramType.put("productName", String.class);
        paramType.put("num", String.class);
        paramType.put("weight", String.class);
        paramType.put("status", String.class);

        return findPageSql(page, sb.toString(),paramType,null);
    }
	
}

