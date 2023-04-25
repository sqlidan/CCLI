package com.haiersoft.ccli.cost.dao;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.haiersoft.ccli.cost.entity.BisCheckingBook;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BisExpenseSchemeScale;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.PageUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.wms.entity.AsnAction;
/**
 * @author Connor.M
 * @ClassName: StandingBookDao
 * @Description: 台账DAo
 * @date 2016年5月5日 上午10:10:18
 */
@Repository
public class StandingBookDao extends HibernateDao<BisStandingBook, Integer> {


    private final static Logger log = Logger.getLogger("StandingBookDao");
	public int insertStandingBook(BisStandingBook book){

	    StringBuffer sb=new StringBuffer();
		sb.append(" insert into BIS_STANDING_BOOK ( ");
		sb.append(" CUSTOMS_NUM,CUSTOMS_NAME,BILL_NUM,LINK_ID,CRK_SIGN,STORAGE_DTAE,FEE_CODE,FEE_NAME,");
		sb.append(" FEE_PLAN,IF_RECEIVE,NUM,PRICE,RECEIVE_AMOUNT,REAL_AMOUNT,CURRENCY,EXCHANGE_RATE,");
		sb.append(" SHOULD_RMB,BIS_TYPE,TAX_RATE,FILL_SIGN,INPUT_PERSON_ID,INPUT_PERSON,INPUT_DATE,CHARGE_DATE,");
		sb.append(" COST_DATE,EXAMINE_SIGN,SHARE_SIGN,RECONCILE_SIGN,SETTLE_SIGN,SPLIT_SIGN,CONTACT_TYPE,");
		sb.append(" CHARGE_SIGN,BILL_DATE,REMARK,STANDING_NUM,STANDING_CODE,PAY_SIGN)");
		sb.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		sqlQuery.setString(0, book.getCustomsNum());
		sqlQuery.setString(1, book.getCustomsName());
		sqlQuery.setString(2, book.getBillNum());
		sqlQuery.setString(3, book.getLinkId());
		sqlQuery.setInteger(4, book.getCrkSign());
		sqlQuery.setDate(5, book.getStorageDtae());
		sqlQuery.setString(6, book.getFeeCode());
		sqlQuery.setString(7, book.getFeeName());
		sqlQuery.setString(8, book.getFeePlan());
		sqlQuery.setInteger(9, book.getIfReceive());
		sqlQuery.setDouble(10, book.getNum());
		sqlQuery.setDouble(11, book.getPrice());
		sqlQuery.setDouble(12, book.getReceiveAmount());
		sqlQuery.setDouble(13, book.getRealAmount());
		sqlQuery.setString(14, book.getCurrency());
		sqlQuery.setDouble(15, book.getExchangeRate());
		sqlQuery.setDouble(16, book.getShouldRmb());
		sqlQuery.setString(17, book.getBisType());
		sqlQuery.setDouble(18, book.getTaxRate());
		sqlQuery.setInteger(19,book.getFillSign());
		sqlQuery.setString(20, book.getInputPersonId());
		sqlQuery.setString(21, book.getInputPerson());
		sqlQuery.setDate(22, book.getInputDate());
		sqlQuery.setDate(23, book.getChargeDate());
		sqlQuery.setDate(24, book.getCostDate());
		sqlQuery.setInteger(25, book.getExamineSign());
		sqlQuery.setInteger(26, book.getShareSign());
		sqlQuery.setInteger(27, book.getReconcileSign());
		sqlQuery.setInteger(28, book.getSettleSign());
		sqlQuery.setInteger(29, book.getSplitSign());
		sqlQuery.setInteger(30, book.getContactType());
		sqlQuery.setString(31, book.getChargeSign());
		sqlQuery.setDate(32, book.getBillDate());
		sqlQuery.setString(33, book.getRemark());
		sqlQuery.setInteger(34, book.getStandingNum());
		sqlQuery.setString(35, book.getStandingCode());
		sqlQuery.setString(36, book.getPaySign());
		return sqlQuery.executeUpdate();

	}
	@SuppressWarnings("unchecked")
	public List<BisStandingBook> getBisStandingBooks(String billNum,String[] feeCodes){
		   StringBuffer sql=new StringBuffer();
	 	   sql.append("FROM BisStandingBook book where book.billNum=? and book.ifReceive=1 and book.reconcileSign=1 and book.examineSign=1 and book.feeCode<>'cc' ");
	 	   if(null!=feeCodes&&!"".equals(feeCodes)){
	 		  sql.append(" and book.feeCode in (:list) ");
	 	   }
	 	   Query query= createQuery(sql.toString());
	 	   query.setString(0,billNum);
	 	   if(null!=feeCodes&&!"".equals(feeCodes)){
	 		  query.setParameterList("list",feeCodes);
	 	   }
		   List<BisStandingBook> books=query.list();
	 	   return books;
	}

	public BisStandingBook getBisStandingBook(String customsNum,String billNum,String linkId,String feePlan,Integer ifReceive,String feeCode){
		   StringBuffer sql=new StringBuffer();
	 	   sql.append("FROM BisStandingBook book where book.customsNum=? and book.billNum=? and book.linkId=? and book.feePlan=? and book.ifReceive=? and book.feeCode=? ");
	 	   Query query= createQuery(sql.toString());
	 	   query.setString(0,customsNum);
	 	   query.setString(1,billNum);
	 	   query.setString(2,linkId);
		   query.setString(3,feePlan);
		   query.setInteger(4,ifReceive);
		   query.setString(5,feeCode);
		   BisStandingBook book=(BisStandingBook)query.uniqueResult();
	 	   return book;
	}

	public BisStandingBook getBisStandingBook(String customsNum,String billNum,String linkId,String feePlan,Integer ifReceive,String feeCode,String asn){
	   StringBuffer sql=new StringBuffer();
 	   sql.append("FROM BisStandingBook book where book.customsNum=? and book.billNum=? and book.linkId=? and book.feePlan=? and book.ifReceive=? and book.feeCode=? and book.asn=? ");
 	   Query query= createQuery(sql.toString());
 	   query.setString(0,customsNum);
 	   query.setString(1,billNum);
 	   query.setString(2,linkId);
	   query.setString(3,feePlan);
	   query.setInteger(4,ifReceive);
	   query.setString(5,feeCode);
	   query.setString(6,asn);
	   BisStandingBook book=(BisStandingBook)query.uniqueResult();
 	   return book;
	}
    /**
     * @param linkId
     * @return
     * @throws
     * @author Connor.M
     * @Description: 根据联系单Id统计  应收应付费用
     * @date 2016年4月9日 下午1:47:37
     */
    @SuppressWarnings("unchecked")
    public List<BisStandingBook> sumStandingBookByLinkId(String linkId) {
        StringBuffer sb = new StringBuffer(""
                + " SELECT "
                + " sb.if_receive AS ifReceive, "
                + " NVL(SUM(sb.receive_amount),0) as receiveAmount, "
                + " NVL(sum(sb.should_rmb),0) as shouldRmb "
                + " FROM bis_standing_book sb "
                + " WHERE 1 = 1 ");
        sb.append(" AND sb.link_id = ?0 ");
        sb.append(" GROUP BY sb.if_receive ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), linkId);

        sqlQuery.addScalar("ifReceive", StandardBasicTypes.INTEGER);//收付类型
        sqlQuery.addScalar("receiveAmount", StandardBasicTypes.DOUBLE);//应收
        sqlQuery.addScalar("shouldRmb", StandardBasicTypes.DOUBLE);//应收金额*汇率
        sqlQuery.setResultTransformer(Transformers.aliasToBean(BisStandingBook.class));
        return sqlQuery.list();
    }

    /**
     * @param schemeScale
     * @throws
     * @author Connor.M
     * @Description: 取消分摊
     * @date 2016年5月5日 上午10:09:49
     */
    public void cancelCostShare(BisExpenseSchemeScale schemeScale) {
        StringBuffer deleteSql = new StringBuffer("DELETE FROM bis_standing_book s WHERE ");
        deleteSql.append(" (s.customs_num = :stockId and instr(s.share_link, :linkId) > 0 ) OR (s.customs_num != :stockId and instr(s.share_link, :linkId) > 0 AND s.receive_amount <= 0) ");
        HashMap<String, Object> deleteParmes = new HashMap<String, Object>();
        deleteParmes.put("linkId", schemeScale.getLinkId());
        deleteParmes.put("stockId", schemeScale.getCustomsId());
        SQLQuery deleteQuery = createSQLQuery(deleteSql.toString(), deleteParmes);
        deleteQuery.executeUpdate();

        String remark = "[系统自动分摊由卖方承担,出库联系单：" + schemeScale.getLinkId() + "]";//取消备注
        StringBuffer updateSql = new StringBuffer("UPDATE bis_standing_book s SET s.remark = REPLACE(s.remark, :remark, ''), s.share_link = REPLACE(s.share_link, :linkId, ''), s.share_sign = '0', s.fill_sign='0' WHERE INSTR(s.share_link, :linkId) > 0 ");
        HashMap<String, Object> updateParmes = new HashMap<String, Object>();
        updateParmes.put("linkId", schemeScale.getLinkId() + ",");
        updateParmes.put("remark", remark);
        SQLQuery updateQuery = createSQLQuery(updateSql.toString(), updateParmes);
        updateQuery.executeUpdate();
    }

    /**
     * 分组获取台账明细
     *
     * @param sDZ      对账单号
     * @param nType    1入，2出、3在库
     * @param linkNum  联系单
     * @param ibillNum 入提单
     * @param obillNum 出提单
     * @param sBaoGuan 报关单
     * @param custom   客户
     * @param inCarNUm 费目
     * @param strTime  开始时间
     * @param endTime  结束时间
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> getStandingBookGroupList(String sDZ, int nType, String linkNum, String ibillNum, String obillNum, String sBaoGuan, String custom, String inCarNUm, String strTime, String endTime) {
        List list = new ArrayList<>();
        if (nType > 0 && sDZ != null && !"".equals(sDZ)) {
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder("select * from( ");
            sb.append(" select sum(nvl(SHOULD_RMB,0))as RMB, " +
                    " t.link_id,t.bill_num, " +
                    " t.FEE_CODE,min(t.fee_name)as fee_name, " +
                    " min(CUSTOMS_NAME) as CUSTOMS_NAME, " +
                    " min(CURRENCY) as CURRENCY, " +
                    " BILL_DATE,EXCHANGE_RATE, " +
                    " PAY_SIGN, " +
            		" listagg(t.standing_num,',') WITHIN GROUP (order by t.standing_num ) as ids ");
                   // " to_char(wmsys.wm_concat(t.standing_num)) as ids ");
            sb.append(" from BIS_STANDING_BOOK t " +
                    " where t.EXAMINE_SIGN = 1 " +
                    " and t.IF_RECEIVE = '1' " +
                    " and CRK_SIGN = :nType " +
                    " and (RECONCILE_SIGN != 1 or RECONCILE_SIGN is null) " +
                    " and (RECONCILE_NUM != :dz or RECONCILE_NUM is null) ");
            params.put("nType", nType);
            params.put("dz", sDZ);
            if (linkNum != null && !"".equals(linkNum)) {
                sb.append(" AND lower(t.link_id) like lower(:linkNum) ");
                params.put("linkNum", "%"+linkNum+"%");
            }
            if (ibillNum != null && !"".equals(ibillNum)) {
                sb.append(" AND  lower(t.bill_num) like lower(:ibillNum) ");
                params.put("ibillNum", "%"+ibillNum+"%");
            }
            if (obillNum != null && !"".equals(obillNum)) {
                sb.append(" AND  lower(t.bill_num) like lower(:obillNum) ");
                params.put("obillNum", "%"+obillNum+"%");
            }
            if (custom != null && !"".equals(custom)) {
                sb.append(" AND t.CUSTOMS_NUM=:custom ");
                params.put("custom", custom);
            }
            if (inCarNUm != null && !"".equals(inCarNUm)) {
                sb.append(" AND  t.fee_name like :feename");
                params.put("feename", "%" + inCarNUm + "%");
            }
            if (strTime != null && !"".equals(strTime)) {
                sb.append(" AND t.BILL_DATE >= to_date(:strTime, 'yyyy-mm') ");
                params.put("strTime", strTime);
            }
            if (endTime != null && !"".equals(endTime)) {
                sb.append(" AND t.BILL_DATE <=to_date(:endTime, 'yyyy-mm') ");
                params.put("endTime", endTime);
            }
            sb.append(" group by t.link_id,t.bill_num,t.FEE_CODE,PRICE,BILL_DATE,EXCHANGE_RATE,PAY_SIGN) a where a.RMB>0");
            SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
            list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
        return list;
    }

    /**
     * 根据对账单号获取对账单明细
     *
     * @param ntype
     * @param checkingBookCode
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCheckingBookInfoList(int ntype, String checkingBookCode) {
        if (ntype > 0) {
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuffer sb = new StringBuffer("select * from( ");
            sb.append(" select   sum(nvl(SHOULD_RMB,0))as RMB, t.link_id,t.bill_num,t.FEE_CODE,min(t.fee_name)as fee_name,min(CUSTOMS_NAME) as CUSTOMS_NAME,min(CURRENCY) as CURRENCY," +
                    "BILL_DATE,EXCHANGE_RATE,PAY_SIGN, listagg(t.standing_num,',') WITHIN GROUP (order by t.standing_num )as ids  ");

            // sb.append(" select   sum(nvl(SHOULD_RMB,0))as RMB, t.link_id,t.bill_num,t.FEE_CODE,min(t.fee_name)as fee_name,min(CUSTOMS_NAME) as CUSTOMS_NAME,min(CURRENCY) as CURRENCY,BILL_DATE,EXCHANGE_RATE,PAY_SIGN, to_char(wmsys.wm_concat(t.standing_num)) as ids  ");
            sb.append(" from BIS_STANDING_BOOK t where t.EXAMINE_SIGN=1 and CRK_SIGN=:ntype and RECONCILE_NUM=:code ");
            params.put("ntype", ntype);
            params.put("code", checkingBookCode);
            sb.append(" group by t.link_id,t.bill_num,t.FEE_CODE,PRICE,BILL_DATE,EXCHANGE_RATE,PAY_SIGN) a where a.RMB is not null");
            sb.append(" order by bill_num,link_id");
            SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
            return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
        return null;
    }

    /**
     * 根据主键集合，更新对账单号
     *
     * @param sCode 对账id
     * @param ids   账单集合ids
     */
    public void upStingBookCodeList(String sCode, String ids) {
        if (sCode != null && !"".equals(sCode) && ids != null && !"".equals(ids)) {
        	StringBuffer sb = new StringBuffer();
            sb.append(" update bis_standing_book b set b.reconcile_num=:code,b.RECONCILE_SIGN='1' where ");
            String sql=this.getSqlStrByList(ids.split(","),900,"b.standing_num");
            HashMap<String, Object> parme = new HashMap<String, Object>();
            parme.put("code", sCode);
            SQLQuery sqlQuery = createSQLQuery(sb.toString()+sql, parme);
            sqlQuery.executeUpdate();
        }
    }

    /**
     * 根据主键集合，对账单号，删除对账单号
     *
     * @param sCode 对账id
     * @param ids   账单集合ids
     */
    public void delStingBookCodeList(String sCode, String ids) {
        if (sCode != null && !"".equals(sCode) && ids != null && !"".equals(ids)) {
        	StringBuffer sb = new StringBuffer();
            String id = ids.replace("undefined", "0");
            sb.append("update bis_standing_book b set b.reconcile_num=null,RECONCILE_SIGN='0' where b.reconcile_num=:code and ");
            String sql=this.getSqlStrByList(id.split(","),900,"b.standing_num");
            HashMap<String, Object> parme = new HashMap<String, Object>();
            parme.put("code", sCode);
            SQLQuery sqlQuery = createSQLQuery(sb.toString()+sql, parme);
            sqlQuery.executeUpdate();
        }
    }

    /**
     * @param params
     * @return
     * @throws
     * @author PYL
     */
    @SuppressWarnings("unchecked")
	public List<AsnAction> getTray(Map<String, Object> params) {
        StringBuffer sb = new StringBuffer(""
                + " SELECT "
                + ""
                + " sb.if_receive AS ifReceive, "
                + " NVL(SUM(sb.receive_amount),0) as receiveAmount, "
                + " NVL(sum(sb.should_rmb),0) as shouldRmb "
                + " FROM bis_standing_book sb "
                + " WHERE 1 = 1 ");
        sb.append(" AND sb.link_id = ?0 ");
        sb.append(" GROUP BY sb.if_receive ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);

        sqlQuery.addScalar("ifReceive", StandardBasicTypes.INTEGER);//收付类型
        sqlQuery.addScalar("receiveAmount", StandardBasicTypes.DOUBLE);//应收
        sqlQuery.addScalar("shouldRmb", StandardBasicTypes.DOUBLE);//应收金额*汇率
        sqlQuery.setResultTransformer(Transformers.aliasToBean(BisStandingBook.class));
        return sqlQuery.list();
    }

    /**
     * 核销台账总览
     *
     * @param customsId 客户id
     * @param strTime   开始时间
     * @param endTime   结束时间 台账期
     * @param ntype     应收应付
     * @param ncx       查询类型
     * @return Map<String, Object>
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getCancelParameterList(int customsId, int ntype, String strTime, String endTime, int ncx, int nPage, int nPageSize) {
        if (ntype > 0) {
            Map<String, Object> returnMap = new HashMap<String, Object>();
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuffer sb = new StringBuffer("select * from ( ");
            StringBuffer sql = new StringBuffer("");
            sb.append("select sum(SHOULD_RMB) as ysz,sum(nvl(REAL_RMB,0)) as ys,sum(SHOULD_RMB)-sum(nvl(REAL_RMB,0)) as ws,min(CUSTOMS_NAME) as CUSTOMS_NAME, customs_num,BILL_DATE,IF_RECEIVE,fun_concat_clob(STANDING_NUM) as ids   ");
            sb.append(" from (select SHOULD_RMB,REAL_RMB,CUSTOMS_NAME,customs_num,trunc(s.BILL_DATE,'mm') BILL_DATE,IF_RECEIVE,STANDING_NUM ");
            sb.append(" from bis_standing_book s where  s.EXAMINE_SIGN=1 ");//SHOULD_RMB!=nvl( REAL_RMB,0) and
            sql.append("select  fun_concat_clob(nvl((STANDING_NUM),'0')) as ids from bis_standing_book s where  s.EXAMINE_SIGN=1 ");//SHOULD_RMB!=nvl( REAL_RMB,0) and
            if (ncx > 1) {
                sb.append(" and s.HXKEY is null  ");
                sql.append(" and s.HXKEY is null  ");
            }
            if (customsId > 0) {
                sb.append(" and s.customs_num=:customsId  ");
                sql.append(" and s.customs_num=:customsId  ");
                params.put("customsId", customsId);
            }
            if (ntype > 0) {
                sb.append(" and s.IF_RECEIVE=:ntype  ");
                sql.append(" and s.IF_RECEIVE=:ntype  ");
                params.put("ntype", ntype);
            }
            if (strTime != null && !"".equals(strTime)) {
                sb.append(" and trunc(s.BILL_DATE,'mm')>= to_date(:strTime, 'yyyy-mm') ");
                sql.append(" and trunc(s.BILL_DATE,'mm')>= to_date(:strTime, 'yyyy-mm') ");
                params.put("strTime", strTime);
            }
            if (endTime != null && !"".equals(endTime)) {
                sb.append("  and trunc(s.BILL_DATE,'mm')<= to_date(:endTime, 'yyyy-mm')  ");
                sql.append("  and trunc(s.BILL_DATE,'mm')<= to_date(:endTime, 'yyyy-mm')  ");
                params.put("endTime", endTime);
            }
            sb.append(" ) group by customs_num,BILL_DATE,IF_RECEIVE ");
            sb.append("  ) where ysz!=ys order by BILL_DATE  ");
            long totalCount = countSqlResult(sb.toString(), params);
            SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
            sqlQuery.setFirstResult(PageUtils.calBeginIndex(nPage, nPageSize, Integer.valueOf(String.valueOf(totalCount))));
            sqlQuery.setMaxResults(nPageSize);
            List<Map<String, Object>> groupList = null;
            groupList=sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            int size=groupList.size();
            if(size>0){
            	String groupId="";
                for(int i=0;i<size;i++){
                	Clob clob = (Clob)groupList.get(i).get("IDS");
                	try {
						groupId=clob.getSubString((long)1,(int)clob.length());
					} catch (SQLException e) {
						e.printStackTrace();
					}
                	groupList.get(i).put("IDS", groupId);
                }
            }
            returnMap.put("total", totalCount);
            returnMap.put("rows", groupList);
            String getIds = getSeachIds(sql.toString(), params);
            returnMap.put("ids", getIds);
            return returnMap;
        }
        return null;
    }

    /**
     * 获取查询的账单ids集合
     *
     * @param sql
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
	private String getSeachIds(String sql, Map<String, Object> params) {
        String rerStr = "";
        List<Map<String, Object>> getList = null;
        SQLQuery sqlQuery = createSQLQuery(sql, params);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (getList != null && getList.size() > 0) {
            Map<String, Object> getMap = getList.get(0);
//            可用来将clob转为string类型
            Clob clob = (Clob) getMap.get("IDS");
            try {
            	rerStr=clob.getSubString((long)1,(int)clob.length());
			} catch (SQLException e) {
				e.printStackTrace();
			}
            if ("0".equals(rerStr)) {
                rerStr = "";
            }
        }
        return rerStr;
    }

    /**
     * 核销台账明细
     *
     * @param customsId
     * @param ymTime
     * @param ntype
     * @return
     */
    public Map<String, Object> getCancelParameterInfoList(int customsId, String ymTime, int ntype, int nPage, int nPageSize) {
        if (ntype > 0) {
            Map<String, Object> returnMap = new HashMap<String, Object>();
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuffer sb = new StringBuffer();
            sb.append("select sum(SHOULD_RMB) as ysz,sum(nvl(REAL_RMB,0)) as ys,sum(SHOULD_RMB)-sum(nvl(REAL_RMB,0)) as ws,min(CUSTOMS_NAME) as CUSTOMS_NAME, customs_num,BILL_DATE,IF_RECEIVE,BILL_NUM,LINK_ID,FEE_CODE,PRICE ,min(FEE_NAME) as FEE_NAME ");
            sb.append(" from (select SHOULD_RMB,REAL_RMB,CUSTOMS_NAME,customs_num,trunc(s.BILL_DATE,'mm') BILL_DATE,IF_RECEIVE,BILL_NUM,LINK_ID,FEE_CODE,PRICE ,FEE_NAME  ");
            sb.append("  from bis_standing_book s  where  s.EXAMINE_SIGN=1 ");//SHOULD_RMB!=nvl( REAL_RMB,0) and
            if (customsId > 0) {
                sb.append(" and s.customs_num=:customsId  ");
                params.put("customsId", customsId);
            }
            if (ntype > 0) {
                sb.append(" and s.IF_RECEIVE=:ntype  ");
                params.put("ntype", ntype);
            }
            if (ymTime != null && !"".equals(ymTime)) {
                if (ymTime.split("-").length == 3) {
                    ymTime = ymTime.substring(0, ymTime.lastIndexOf("-"));
                }
                sb.append(" and trunc(s.BILL_DATE,'mm')>= to_date(:strTime, 'yyyy-mm') ");
                params.put("strTime", ymTime);
                sb.append(" and trunc(s.BILL_DATE,'mm')<= to_date(:endTime, 'yyyy-mm')  ");
                params.put("endTime", ymTime);
            }
            sb.append(" ) ");
            sb.append(" group by customs_num,BILL_DATE,IF_RECEIVE,BILL_NUM,LINK_ID,FEE_CODE,PRICE");
            long totalCount = countSqlResult(sb.toString(), params);
            SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
            sqlQuery.setFirstResult(PageUtils.calBeginIndex(nPage, nPageSize, Integer.valueOf(String.valueOf(totalCount))));
            sqlQuery.setMaxResults(nPageSize);
            returnMap.put("total", totalCount);
            returnMap.put("rows", sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
            return returnMap;
        }
        return null;
    }

    /**
     * 统计选中的核销金额
     *
     * @param ids
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> countSumMoney(String ids) {
        if (ids != null) {
            String sql = "select sum( SHOULD_RMB-nvl(REAL_RMB,0) )as SUMMONEY  from bis_standing_book s  where SHOULD_RMB!=nvl( REAL_RMB,0)  ";
            if (!"".equals(ids.trim())) {
                sql += " and  s.standing_num in (" + ids + ")";
            }
            Map<String, Object> params = new HashMap<String, Object>();
            SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
            return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
        return null;
    }


    /* * Example: List sqhlist=[aa,bb,cc,dd,ee,ff,gg] ;
    * Test.getSqlStrByList(sqhList,3,"SHENQINGH")= "SHENQING IN
    * ('aa','bb','cc') OR SHENQINGH IN ('dd','ee','ff') OR SHENQINGH IN ('g
    *
    *  集合。
    *
    * @param sqhList把超过1000的in条件集合拆分成数量splitNum的多组sql的in
    *            in条件的List
    * @param splitNum
    *            拆分的间隔数目,例如： 1000
    * @param columnName
    *            SQL中引用的字段名例如： Z.SHENQINGH
    * @return
    */
   private String getSqlStrByList(String[] sqhList, int splitNum,
			String columnName) {
		if (splitNum > 1000) // 因为数据库的列表sql限制，不能超过1000.
			return null;
		StringBuffer sql = new StringBuffer("");
		if (sqhList != null) {
			sql.append(" ").append(columnName).append(" IN ( ");
			for (int i = 0; i < sqhList.length; i++) {
				sql.append("'").append(sqhList[i] + "',");
				if ((i + 1) % splitNum == 0 && (i + 1) < sqhList.length) {
					sql.deleteCharAt(sql.length() - 1);
					sql.append(" ) OR ").append(columnName).append(" IN (");
				}
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" )");
		}
		return sql.toString();
	}

	public List<BisStandingBook> checkIfJf(BisStandingBook obj){
		Map<String, Object> params = new HashMap<>();
		String sql = "select standing_num from bis_standing_book where 1=1 ";
        if (isNotNull(obj.getBillNum())) {
            sql +=" and bill_num = :billNum ";
            params.put("billNum", obj.getBillNum());
        }
        if (isNotNull(obj.getRemark())) {
            sql +=" and remark like :remark ";
            params.put("remark", obj.getRemark());
        }
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("billNum", String.class);
        paramType.put("remark", String.class);
        return findSql(sql, paramType, params);
	}

	/*
	 * yhn20171027
	 *
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String,Object>> findStandingBookListForCheck(String linkId){
		if(linkId!=null && !"".equals(linkId)){
			StringBuffer sbSQL=new StringBuffer("select t.standing_num from bis_standing_book t where t.link_id=:linkId ");
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("linkId", linkId);
			SQLQuery sqlQuery=createSQLQuery(sbSQL.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}

    public Page<BisStandingBook> getStocks(Page<BisStandingBook> page, BisStandingBook stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillNum())){
			String[] billList=stock.getBillNum().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                                                           ");
		sb.append(" 	temp.bill_num AS billNum,                                    ");
		sb.append(" 	temp.fee_name AS feeName,                                    ");
		sb.append(" 	temp.fee_code,                                               ");
		sb.append(" 	max(temp.sl) AS sL,                                          ");
		sb.append(" 	max(temp.price) AS dJ,                                       ");
		sb.append(" 	max(temp.SHOULD_RMB) AS jE,                                  ");
		sb.append(" 	max(temp.ys) AS yS,                                          ");
		sb.append(" 	max(temp.yf) AS yF,                                          ");
		sb.append(" 	sum(temp.ys - temp.yf) AS lR,                                ");
		sb.append(" 	temp.input_date AS straTime                                  ");
		sb.append(" FROM                                                             ");
		sb.append(" 	(                                                            ");
		sb.append(" 		SELECT                                                   ");
		sb.append(" 			book.bill_num,                                       ");
		sb.append(" 			sum(book.num) AS sl,                                 ");
		sb.append(" 			max(book.price) AS price,                            ");
		sb.append(" 			round(sum(book.SHOULD_RMB), 2) AS SHOULD_RMB,        ");
		sb.append(" 			book.fee_name,                                       ");
		sb.append(" 			book.fee_code,                                       ");
		sb.append(" 			book.if_receive,                                     ");
		sb.append(" 			CASE                                                 ");
		sb.append(" 		WHEN book.if_receive = 1 THEN                            ");
		sb.append(" 			round(sum(book.SHOULD_RMB), 2)                       ");
		sb.append(" 		ELSE                                                     ");
		sb.append(" 			0                                                    ");
		sb.append(" 		END AS ys,                                               ");
		sb.append(" 		CASE                                                     ");
		sb.append(" 	WHEN book.if_receive = 2 THEN                                ");
		sb.append(" 		round(sum(book.SHOULD_RMB), 2)                           ");
		sb.append(" 	ELSE                                                         ");
		sb.append(" 		0                                                        ");
		sb.append(" 	END AS yf,                                                   ");
		sb.append(" 	to_char (                                                    ");
		sb.append(" 		book.input_date,                                         ");
		sb.append(" 		'yyyy-mm-dd'                                             ");
		sb.append(" 	) AS input_date                                              ");
		sb.append(" FROM                                                             ");
		sb.append(" 	bis_standing_book book                                       ");
		sb.append(" WHERE 1=1                                                        ");
		if(!StringUtils.isNull(stock.getBillNum())){
			sb.append(" AND book.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getFeeName())){
			sb.append(" AND book.fee_name like '%"+stock.getFeeName()+"%'");
		}
		if(!StringUtils.isNull(stock.getStraTime())){
			sb.append(" AND book.input_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStraTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND book.input_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" AND EXAMINE_SIGN = 1                                           ");
		sb.append(" GROUP BY                                                       ");
		sb.append(" 	book.bill_num,                                               ");
		sb.append(" 	book.fee_name,                                               ");
		sb.append(" 	book.fee_code,                                               ");
		sb.append(" 	book.if_receive,                                             ");
		sb.append(" 	to_char (input_date, 'yyyy-mm-dd')                           ");
		sb.append(" 	) temp                                                       ");
		sb.append(" GROUP BY                                                         ");
		sb.append(" 	temp.bill_num,                                               ");
		sb.append(" 	temp.fee_name,                                               ");
		sb.append(" 	temp.fee_code,                                               ");
		sb.append(" 	temp.input_date                                              ");
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("billNum", String.class);
        paramType.put("feeName", String.class);
        paramType.put("straTime", String.class);
        paramType.put("sL", Double.class);
        paramType.put("yS", Double.class);
        paramType.put("yF", Double.class);
        paramType.put("lR", Double.class);
        return findPageSql(page, sb.toString(), paramType, params);
    }


    public Page<BisStandingBook> getCustomerStocks(Page<BisStandingBook> page, BisStandingBook stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillNum())){
			String[] billList=stock.getBillNum().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                                                           ");
		sb.append(" 	temp.bill_num AS billNum,                                    ");
		sb.append(" 	temp.customs_num AS customsNum,                              ");
		sb.append(" 	temp.customs_name AS customsName,                            ");
		sb.append(" 	temp.fee_name AS feeName,                                    ");
		sb.append(" 	temp.fee_code AS feeCode,                                    ");
		sb.append(" 	max(temp.sl) AS sl,                                          ");
		sb.append(" 	max(temp.SHOULD_RMB) AS je,                                  ");
		sb.append(" 	max(temp.ysdj) AS ysdj,                                      ");
		sb.append(" 	max(temp.yfdj) AS yfdj,                                      ");
		sb.append(" 	max(temp.ys) AS ys,                                          ");
		sb.append(" 	max(temp.yf) AS yf,                                          ");
		sb.append(" 	sum(temp.ys - temp.yf) AS lr,                                ");
		sb.append(" 	temp.input_date AS straTime                                  ");
		sb.append(" FROM                                                             ");
		sb.append(" 	(                                                            ");
		sb.append(" 		SELECT                                                   ");
		sb.append(" 			book.bill_num,                                       ");
		sb.append(" 			book.customs_num,                                    ");
		sb.append(" 			book.customs_name,                                       ");
		sb.append(" 			sum(book.num) AS sl,                                     ");
		sb.append(" 			round(sum(book.SHOULD_RMB), 2) AS SHOULD_RMB,            ");
		sb.append(" 			book.fee_name,                                           ");
		sb.append(" 			book.fee_code,                                           ");
		sb.append(" 			book.if_receive,                                         ");
		sb.append(" 			CASE                                                     ");
		sb.append(" 		WHEN book.if_receive = 1 THEN                              ");
		sb.append(" 			max(book.price)                                          ");
		sb.append(" 		ELSE                                                       ");
		sb.append(" 			0                                                        ");
		sb.append(" 		END AS ysdj,                                               ");
		sb.append(" 		CASE                                                       ");
		sb.append(" 	WHEN book.if_receive = 2 THEN                                ");
		sb.append(" 		max(book.price)                                            ");
		sb.append(" 	ELSE                                                         ");
		sb.append(" 		0                                                          ");
		sb.append(" 	END AS yfdj,                                                 ");
		sb.append(" 	CASE                                                         ");
		sb.append(" WHEN book.if_receive = 1 THEN                                  ");
		sb.append(" 	round(sum(book.SHOULD_RMB), 2)                               ");
		sb.append(" ELSE                                                           ");
		sb.append(" 	0                                                            ");
		sb.append(" END AS ys,                                                     ");
		sb.append("  CASE                                                          ");
		sb.append(" WHEN book.if_receive = 2 THEN                                  ");
		sb.append(" 	round(sum(book.SHOULD_RMB), 2)                               ");
		sb.append(" ELSE                                                           ");
		sb.append(" 	0                                                            ");
		sb.append(" END AS yf,                                                     ");
		sb.append("  to_char (                                                     ");
		sb.append(" 	book.input_date,                                             ");
		sb.append(" 	'yyyy-mm-dd'                                                 ");
		sb.append(" ) AS input_date                                                ");
		sb.append(" FROM                                                           ");
		sb.append(" 	bis_standing_book book                                       ");
		sb.append(" WHERE  1=1                                                        ");
		if(!StringUtils.isNull(stock.getBillNum())){
			sb.append(" AND book.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getFeeName())){
			sb.append(" AND book.fee_name like '%"+stock.getFeeName()+"%'");
		}
		if(!StringUtils.isNull(stock.getCustomsNum())){
			sb.append(" AND book.customs_num=:customsnum  ");
			params.put("customsnum",stock.getCustomsNum());
		}
		if(!StringUtils.isNull(stock.getStraTime())){
			sb.append(" AND book.input_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStraTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND book.input_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" AND EXAMINE_SIGN = 1                                           ");
		sb.append(" GROUP BY                                                       ");
		sb.append(" 	book.bill_num,                                               ");
		sb.append(" 	book.customs_num,                                            ");
		sb.append(" 	book.customs_name,                                           ");
		sb.append(" 	book.fee_name,                                               ");
		sb.append(" 	book.fee_code,                                               ");
		sb.append(" 	book.if_receive,                                             ");
		sb.append(" 	to_char (input_date, 'yyyy-mm-dd')                           ");
		sb.append(" 	) temp                                                       ");
		sb.append(" GROUP BY                                                         ");
		sb.append(" 	temp.bill_num,                                               ");
		sb.append(" 	temp.customs_num,                                            ");
		sb.append(" 	temp.customs_name,                                           ");
		sb.append(" 	temp.fee_name,                                               ");
		sb.append(" 	temp.fee_code,                                               ");
		sb.append(" 	temp.input_date                                              ");
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("customsNum", String.class);
        paramType.put("customsName", String.class);
        paramType.put("billNum", String.class);
        paramType.put("feeName", String.class);
        paramType.put("feeCode", String.class);
        paramType.put("straTime", String.class);
        paramType.put("sL", Double.class);
        paramType.put("ysdj", Double.class);
        paramType.put("yfdj", Double.class);
        paramType.put("yS", Double.class);
        paramType.put("yF", Double.class);
        paramType.put("lR", Double.class);
        return findPageSql(page, sb.toString(), paramType, params);
    }


    public Page<BisStandingBook> getSupplierStocks(Page<BisStandingBook> page, BisStandingBook stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillNum())){
			String[] billList=stock.getBillNum().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                                                                                                      ");
		sb.append(" 	CASE ystemp.CRK_SIGN                                                                                    ");
		sb.append(" 		WHEN '1' THEN                                                                                       ");
		sb.append(" 			'入库'                                                                                            ");
		sb.append(" 		WHEN '2' THEN                                                                                        ");
		sb.append(" 			'出库'                                                                                            ");
		sb.append(" 		WHEN '3' THEN                                                                                        ");
		sb.append(" 			'货转'                                                                                            ");
		sb.append(" 		ELSE                                                                                                 ");
		sb.append(" 			'其他'                                                                                            ");
		sb.append(" 	END AS crkType,                                                                                          ");
		sb.append(" 	ystemp.input_date AS straTime,                                                                           ");
		sb.append(" 	ystemp.customs_name AS customsName,                                                                     ");
		sb.append(" 	yftemp.gysname AS gysName,                                                                              ");
		sb.append(" 	ystemp.bill_num AS billNum,                                                                             ");
		sb.append(" 	CASE ystemp.RECONCILE_SIGN WHEN '1' THEN '是' ELSE '否' END AS reconcileType,                            ");
		sb.append(" 	ystemp.RECONCILE_NUM AS reconcileNum,                                                                   ");
		sb.append(" 	sum(ystemp.bg) AS bg,                                                                                   ");
		sb.append(" 	sum(ystemp.bj) AS bj,                                                                                   ");
		sb.append(" 	sum(ystemp.sp)  AS sp,                                                                                  ");
		sb.append(" 	sum(ystemp.sjyh) AS sjyh,                                                                               ");
		sb.append(" 	sum(ystemp.hgyh) AS hgyh,                                                                               ");
		sb.append(" 	(sum(ystemp.bg) + sum(ystemp.bj) + sum(ystemp.sp) + sum(ystemp.sjyh) + sum(ystemp.hgyh)) AS lR,         ");
		sb.append(" 	ystemp.INPUT_PERSON AS inputPerson,                                                                     ");
		sb.append(" 	yftemp.gys AS gys,                                                                                      ");
		sb.append(" 	ystemp.customs_num,                                                                                     ");
		sb.append(" 	ystemp.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 	ystemp.CRK_SIGN                                                                                         ");
		sb.append(" FROM                                                                                                        ");
		sb.append(" 	(                                                                                                       ");
		sb.append(" 		SELECT                                                                                              ");
		sb.append(" 			book.bill_num,                                                                                  ");
		sb.append(" 			to_char(wm_concat (DISTINCT book.customs_num)) AS gys,                                                   ");
		sb.append("             to_char(wm_concat (DISTINCT book.customs_name)) AS gysname,                                              ");
		sb.append("             book.FEE_CODE,                                                                                  ");
		sb.append(" 			MAX (                                                                                           ");
		sb.append(" 				CASE book.fee_code                                                                          ");
		sb.append(" 				WHEN 'bgf' THEN                                                                             ");
		sb.append(" 					book.SHOULD_RMB                                                                         ");
		sb.append(" 				ELSE                                                                                        ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfbg,                                                                                          ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'bj' THEN                                                                                    ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfbj,                                                                                          ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'sp' THEN                                                                                    ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfsp,                                                                                          ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'sjyh' THEN                                                                                  ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfsjyh,                                                                                        ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'hgyh' THEN                                                                                  ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfhgyh,                                                                                        ");
		sb.append(" 			book.if_receive,                                                                                    ");
		sb.append(" 			TO_CHAR (                                                                                           ");
		sb.append(" 				book.input_date,                                                                                  ");
		sb.append(" 				'yyyy-mm'                                                                                         ");
		sb.append(" 			) AS input_date                                                                                     ");
		sb.append(" 		FROM                                                                                                  ");
		sb.append(" 			bis_standing_book book                                                                              ");
		sb.append(" 		WHERE                                                                                                 ");
		sb.append(" 			1 = 1                                                                                               ");
		sb.append(" 		AND EXAMINE_SIGN = 1                                                                                  ");
		sb.append(" 		AND book.if_receive = 2                                                                               ");
		sb.append(" 		AND book.FEE_CODE IN (                                                                                ");
		sb.append(" 			'bgf',                                                                                              ");
		sb.append(" 			'bj',                                                                                               ");
		sb.append(" 			'sp',                                                                                               ");
		sb.append(" 			'sjyh',                                                                                             ");
		sb.append(" 			'hgyh'                                                                                              ");
		sb.append(" 		)                                                                                                     ");
		sb.append(" 		GROUP BY                                                                                              ");
		sb.append(" 			book.bill_num,                                                                                      ");
		sb.append(" 			book.if_receive,                                                                                    ");
		sb.append("             book.FEE_CODE,                                                                                      ");
		sb.append(" 			TO_CHAR (                                                                                           ");
		sb.append(" 				book.input_date,                                                                                  ");
		sb.append(" 				'yyyy-mm'                                                                                         ");
		sb.append(" 			)                                                                                                   ");
		sb.append(" 	) yftemp                                                                                                ");
		sb.append(" LEFT JOIN(                                                                                                ");
		sb.append(" 	SELECT                                                                                                  ");
		sb.append(" 		book.bill_num,                                                                                        ");
		sb.append(" 		book.customs_num,                                                                                     ");
		sb.append(" 		book.customs_name,                                                                                    ");
		sb.append("         book.FEE_CODE,                                                                                        ");
		sb.append(" 		to_char(wm_concat (DISTINCT book.RECONCILE_NUM)) AS RECONCILE_NUM,                                             ");
		sb.append(" 		book.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'bgf' THEN                                                                                     ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS bg,                                                                                              ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'bj' THEN                                                                                      ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS bj,                                                                                              ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'sp' THEN                                                                                      ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS sp,                                                                                              ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'sjyh' THEN                                                                                    ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS sjyh,                                                                                            ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'hgyh' THEN                                                                                    ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS hgyh,                                                                                            ");
		sb.append(" 		book.if_receive,                                                                                      ");
		sb.append(" 		book.CRK_SIGN,                                                                                        ");
		sb.append(" 		TO_CHAR (                                                                                             ");
		sb.append(" 			book.input_date,                                                                                    ");
		sb.append(" 			'yyyy-mm'                                                                                           ");
		sb.append(" 		) AS input_date,                                                                                      ");
		sb.append(" 		to_char(wm_concat (DISTINCT book.INPUT_PERSON)) AS INPUT_PERSON                                                ");
		sb.append(" 	FROM                                                                                                    ");
		sb.append(" 		bis_standing_book book                                                                                ");
		sb.append(" 	WHERE                                                                                                   ");
		sb.append(" 		1 = 1                                                                                                 ");
		sb.append(" 	AND EXAMINE_SIGN = 1                                                                                    ");
		sb.append(" 	AND book.if_receive = 1                                                                                 ");
		sb.append(" 	AND book.FEE_CODE IN (                                                                                  ");
		sb.append(" 		'bgf',                                                                                                ");
		sb.append(" 		'bj',                                                                                                 ");
		sb.append(" 		'sp',                                                                                                 ");
		sb.append(" 		'sjyh',                                                                                               ");
		sb.append(" 		'hgyh'                                                                                                ");
		sb.append(" 	)                                                                                                       ");
		sb.append(" 	GROUP BY                                                                                                ");
		sb.append(" 		book.bill_num,                                                                                        ");
		sb.append(" 		book.customs_num,                                                                                     ");
		sb.append("     book.FEE_CODE,                                                                                        ");
		sb.append(" 		book.customs_name,                                                                                    ");
		sb.append(" 		TO_CHAR (                                                                                             ");
		sb.append(" 			book.input_date,                                                                                    ");
		sb.append(" 			'yyyy-mm'                                                                                           ");
		sb.append(" 		),                                                                                                    ");
		sb.append(" 		book.if_receive,                                                                                      ");
		sb.append(" 		book.CRK_SIGN,                                                                                        ");
		sb.append(" 		book.RECONCILE_SIGN                                                                                   ");
		sb.append(" ) ystemp                                                                                                  ");
		sb.append(" ON (yftemp.bill_num = ystemp.bill_num AND yftemp.FEE_CODE=ystemp.FEE_CODE)                                ");
		sb.append(" WHERE                                                                                                     ");
		sb.append(" 	1 = 1                                                                                                   ");
		if(!StringUtils.isNull(stock.getBillNum())){
			sb.append(" AND yftemp.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getsCustom())){
			sb.append(" AND yftemp.gys=:gys                   ");
			params.put("gys",stock.getsCustom());
		}
		if(!StringUtils.isNull(stock.getCustomsNum())){
			sb.append(" AND ystemp.customs_num=:customsnum    ");
			params.put("customsnum",stock.getCustomsNum());
		}
		if(null!=stock.getReconcileSign()&&!"".equals(stock.getReconcileSign())&&2!=stock.getReconcileSign()){
			sb.append(" AND ystemp.reconcile_sign=:reconcilesign    ");
			params.put("reconcilesign",stock.getReconcileSign());
		}
		if(!StringUtils.isNull(stock.getStraTime())){
			sb.append(" AND to_date(ystemp.input_date,'yyyy-mm') >= to_date(:strartTime, 'yyyy-mm') ");
			params.put("strartTime", stock.getStraTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND to_date(ystemp.input_date,'yyyy-mm') < to_date(:endTime, 'yyyy-mm')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" GROUP BY                                                                                                  ");
		sb.append(" 	ystemp.input_date,                                                                                      ");
		sb.append(" 	ystemp.customs_name,                                                                                    ");
		sb.append(" 	yftemp.gysname,                                                                                         ");
		sb.append(" 	ystemp.bill_num,                                                                                        ");
		sb.append(" 	ystemp.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 	ystemp.RECONCILE_NUM,                                                                                   ");
		sb.append(" 	ystemp.INPUT_PERSON,                                                                                    ");
		sb.append(" 	yftemp.gys,                                                                                             ");
		sb.append(" 	ystemp.customs_num,                                                                                     ");
		sb.append(" 	ystemp.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 	ystemp.CRK_SIGN                                                                                         ");
		sb.append(" ORDER BY                                                                                                  ");
		sb.append(" 	ystemp.customs_name,                                                                                    ");
		sb.append(" 	ystemp.bill_num                                                                                         ");

        Map<String, Object> paramType = new HashMap<>();
        paramType.put("crkType", String.class);
        paramType.put("straTime", String.class);
        paramType.put("customsName", String.class);
        paramType.put("gysName", String.class);
        paramType.put("billNum", String.class);
        paramType.put("reconcileType", String.class);
        paramType.put("reconcileNum", String.class);
        paramType.put("bg", Double.class);
        paramType.put("bj", Double.class);
        paramType.put("sp", Double.class);
        paramType.put("sjyh", Double.class);
        paramType.put("hgyh", Double.class);
        paramType.put("lR", Double.class);
        paramType.put("inputPerson", String.class);
        return findPageSql(page, sb.toString(), paramType, params);
    }

    /**
     * 导出excel
     * @param page
     * @param stock
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findRepot(BisStandingBook stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillNum())){
			String[] billList=stock.getBillNum().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                                                ");
		sb.append(" 	temp.bill_num,                                    ");
		sb.append(" 	temp.fee_name,                                    ");
		sb.append(" 	temp.fee_code,                                    ");
		sb.append(" 	max(temp.sl) AS sl,                               ");
		sb.append(" 	max(temp.price) AS dj,                            ");
		sb.append(" 	max(temp.SHOULD_RMB) AS je,                       ");
		sb.append(" 	max(temp.ys) AS ys,                               ");
		sb.append(" 	max(temp.yf) AS yf,                               ");
		sb.append(" 	sum(temp.ys - temp.yf) AS lr,                     ");
		sb.append(" 	temp.input_date                                   ");
		sb.append(" FROM                                                             ");
		sb.append(" 	(                                                            ");
		sb.append(" 		SELECT                                                   ");
		sb.append(" 			book.bill_num,                                       ");
		sb.append(" 			sum(book.num) AS sl,                                 ");
		sb.append(" 			max(book.price) AS price,                            ");
		sb.append(" 			round(sum(book.SHOULD_RMB), 2) AS SHOULD_RMB,        ");
		sb.append(" 			book.fee_name,                                       ");
		sb.append(" 			book.fee_code,                                       ");
		sb.append(" 			book.if_receive,                                     ");
		sb.append(" 			CASE                                                 ");
		sb.append(" 		WHEN book.if_receive = 1 THEN                            ");
		sb.append(" 			round(sum(book.SHOULD_RMB), 2)                       ");
		sb.append(" 		ELSE                                                     ");
		sb.append(" 			0                                                    ");
		sb.append(" 		END AS ys,                                               ");
		sb.append(" 		CASE                                                     ");
		sb.append(" 	WHEN book.if_receive = 2 THEN                                ");
		sb.append(" 		round(sum(book.SHOULD_RMB), 2)                           ");
		sb.append(" 	ELSE                                                         ");
		sb.append(" 		0                                                        ");
		sb.append(" 	END AS yf,                                                   ");
		sb.append(" 	to_char (                                                    ");
		sb.append(" 		book.input_date,                                         ");
		sb.append(" 		'yyyy-mm-dd'                                             ");
		sb.append(" 	) AS input_date                                              ");
		sb.append(" FROM                                                             ");
		sb.append(" 	bis_standing_book book                                       ");
		sb.append(" WHERE 1=1                                                        ");
		if(!StringUtils.isNull(stock.getBillNum())){
			sb.append(" AND book.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getFeeName())){
			sb.append(" AND book.fee_name like '%"+stock.getFeeName()+"%'");
		}
		if(!StringUtils.isNull(stock.getStraTime())){
			sb.append(" AND book.input_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStraTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND book.input_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" AND EXAMINE_SIGN = 1                                           ");
		sb.append(" GROUP BY                                                       ");
		sb.append(" 	book.bill_num,                                               ");
		sb.append(" 	book.fee_name,                                               ");
		sb.append(" 	book.fee_code,                                               ");
		sb.append(" 	book.if_receive,                                             ");
		sb.append(" 	to_char (input_date, 'yyyy-mm-dd')                           ");
		sb.append(" 	) temp                                                       ");
		sb.append(" GROUP BY                                                         ");
		sb.append(" 	temp.bill_num,                                               ");
		sb.append(" 	temp.fee_name,                                               ");
		sb.append(" 	temp.fee_code,                                               ");
		sb.append(" 	temp.input_date                                              ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		sqlQuery.list();
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> sum(BisStandingBook stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillNum())){
			String[] billList=stock.getBillNum().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                                                ");
		sb.append(" 	'' AS bill_num,                                   ");
		sb.append(" 	'合计：' AS fee_name,                                   ");
		sb.append(" 	'' AS fee_code,                                   ");
		sb.append(" 	'' AS sl,                                         ");
		sb.append(" 	'' AS dj,                                         ");
		sb.append(" 	'' AS je,                                         ");
		sb.append(" 	sum(temp.ys) AS ys,                               ");
		sb.append(" 	sum(temp.yf) AS yf,                               ");
		sb.append(" 	sum(temp.ys - temp.yf) AS lr,                     ");
		sb.append(" 	'' AS input_date                                  ");
		sb.append(" FROM                                                  ");
		sb.append(" 	(                                                 ");
		sb.append(" 		SELECT                                        ");
		sb.append(" 			book.bill_num,                                       ");
		sb.append(" 			sum(book.num) AS sl,                                 ");
		sb.append(" 			max(book.price) AS price,                            ");
		sb.append(" 			round(sum(book.SHOULD_RMB), 2) AS SHOULD_RMB,        ");
		sb.append(" 			book.fee_name,                                       ");
		sb.append(" 			book.fee_code,                                       ");
		sb.append(" 			book.if_receive,                                     ");
		sb.append(" 			CASE                                                 ");
		sb.append(" 		WHEN book.if_receive = 1 THEN                            ");
		sb.append(" 			round(sum(book.SHOULD_RMB), 2)                       ");
		sb.append(" 		ELSE                                                     ");
		sb.append(" 			0                                                    ");
		sb.append(" 		END AS ys,                                               ");
		sb.append(" 		CASE                                                     ");
		sb.append(" 	WHEN book.if_receive = 2 THEN                                ");
		sb.append(" 		round(sum(book.SHOULD_RMB), 2)                           ");
		sb.append(" 	ELSE                                                         ");
		sb.append(" 		0                                                        ");
		sb.append(" 	END AS yf,                                                   ");
		sb.append(" 	to_char (                                                    ");
		sb.append(" 		book.input_date,                                         ");
		sb.append(" 		'yyyy-mm-dd'                                             ");
		sb.append(" 	) AS input_date                                              ");
		sb.append(" FROM                                                             ");
		sb.append(" 	bis_standing_book book                                       ");
		sb.append(" WHERE 1=1                                                        ");
		if(!StringUtils.isNull(stock.getBillNum())){
			sb.append(" AND book.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getFeeName())){
			sb.append(" AND book.fee_name like '%"+stock.getFeeName()+"%'");
		}
		if(!StringUtils.isNull(stock.getStraTime())){
			sb.append(" AND book.input_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStraTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND book.input_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" AND EXAMINE_SIGN = 1                                           ");
		sb.append(" GROUP BY                                                       ");
		sb.append(" 	book.bill_num,                                               ");
		sb.append(" 	book.fee_name,                                               ");
		sb.append(" 	book.fee_code,                                               ");
		sb.append(" 	book.if_receive,                                             ");
		sb.append(" 	to_char (input_date, 'yyyy-mm-dd')                           ");
		sb.append(" 	) temp                                                       ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 导出月代理费用excel
     * @param page
     * @param stock
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findSupplierRepot(BisStandingBook stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillNum())){
			String[] billList=stock.getBillNum().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                                                                                                      ");
		sb.append(" 	CASE ystemp.CRK_SIGN                                                                                    ");
		sb.append(" 		WHEN '1' THEN                                                                                       ");
		sb.append(" 			'入库'                                                                                            ");
		sb.append(" 		WHEN '2' THEN                                                                                        ");
		sb.append(" 			'出库'                                                                                            ");
		sb.append(" 		WHEN '3' THEN                                                                                        ");
		sb.append(" 			'货转'                                                                                            ");
		sb.append(" 		ELSE                                                                                                 ");
		sb.append(" 			'其他'                                                                                            ");
		sb.append(" 	END AS crkType,                                                                                          ");
		sb.append(" 	ystemp.input_date AS straTime,                                                                           ");
		sb.append(" 	ystemp.customs_name AS customsName,                                                                     ");
		sb.append(" 	yftemp.gysname AS gysName,                                                                              ");
		sb.append(" 	ystemp.bill_num AS billNum,                                                                             ");
		sb.append(" 	CASE ystemp.RECONCILE_SIGN WHEN '1' THEN '是' ELSE '否' END AS reconcileType,                            ");
		sb.append(" 	ystemp.RECONCILE_NUM AS reconcileNum,                                                                   ");
		sb.append(" 	sum(ystemp.bg) AS bg,                                                                                   ");
		sb.append(" 	sum(ystemp.bj) AS bj,                                                                                   ");
		sb.append(" 	sum(ystemp.sp)  AS sp,                                                                                  ");
		sb.append(" 	sum(ystemp.sjyh) AS sjyh,                                                                               ");
		sb.append(" 	sum(ystemp.hgyh) AS hgyh,                                                                               ");
		sb.append(" 	(sum(ystemp.bg) + sum(ystemp.bj) + sum(ystemp.sp) + sum(ystemp.sjyh) + sum(ystemp.hgyh)) AS lR,         ");
		sb.append(" 	ystemp.INPUT_PERSON AS inputPerson,                                                                     ");
		sb.append(" 	yftemp.gys AS gys,                                                                                      ");
		sb.append(" 	ystemp.customs_num,                                                                                     ");
		sb.append(" 	ystemp.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 	ystemp.CRK_SIGN                                                                                         ");
		sb.append(" FROM                                                                                                        ");
		sb.append(" 	(                                                                                                       ");
		sb.append(" 		SELECT                                                                                              ");
		sb.append(" 			book.bill_num,                                                                                  ");
		sb.append(" 			to_char(wm_concat (DISTINCT book.customs_num)) AS gys,                                                   ");
		sb.append("             to_char(wm_concat (DISTINCT book.customs_name)) AS gysname,                                              ");
		sb.append("             book.FEE_CODE,                                                                                  ");
		sb.append(" 			MAX (                                                                                           ");
		sb.append(" 				CASE book.fee_code                                                                          ");
		sb.append(" 				WHEN 'bgf' THEN                                                                             ");
		sb.append(" 					book.SHOULD_RMB                                                                         ");
		sb.append(" 				ELSE                                                                                        ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfbg,                                                                                          ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'bj' THEN                                                                                    ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfbj,                                                                                          ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'sp' THEN                                                                                    ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfsp,                                                                                          ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'sjyh' THEN                                                                                  ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfsjyh,                                                                                        ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'hgyh' THEN                                                                                  ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfhgyh,                                                                                        ");
		sb.append(" 			book.if_receive,                                                                                    ");
		sb.append(" 			TO_CHAR (                                                                                           ");
		sb.append(" 				book.input_date,                                                                                  ");
		sb.append(" 				'yyyy-mm'                                                                                         ");
		sb.append(" 			) AS input_date                                                                                     ");
		sb.append(" 		FROM                                                                                                  ");
		sb.append(" 			bis_standing_book book                                                                              ");
		sb.append(" 		WHERE                                                                                                 ");
		sb.append(" 			1 = 1                                                                                               ");
		sb.append(" 		AND EXAMINE_SIGN = 1                                                                                  ");
		sb.append(" 		AND book.if_receive = 2                                                                               ");
		sb.append(" 		AND book.FEE_CODE IN (                                                                                ");
		sb.append(" 			'bgf',                                                                                              ");
		sb.append(" 			'bj',                                                                                               ");
		sb.append(" 			'sp',                                                                                               ");
		sb.append(" 			'sjyh',                                                                                             ");
		sb.append(" 			'hgyh'                                                                                              ");
		sb.append(" 		)                                                                                                     ");
		sb.append(" 		GROUP BY                                                                                              ");
		sb.append(" 			book.bill_num,                                                                                      ");
		sb.append(" 			book.if_receive,                                                                                    ");
		sb.append("             book.FEE_CODE,                                                                                      ");
		sb.append(" 			TO_CHAR (                                                                                           ");
		sb.append(" 				book.input_date,                                                                                  ");
		sb.append(" 				'yyyy-mm'                                                                                         ");
		sb.append(" 			)                                                                                                   ");
		sb.append(" 	) yftemp                                                                                                ");
		sb.append(" LEFT JOIN(                                                                                                ");
		sb.append(" 	SELECT                                                                                                  ");
		sb.append(" 		book.bill_num,                                                                                        ");
		sb.append(" 		book.customs_num,                                                                                     ");
		sb.append(" 		book.customs_name,                                                                                    ");
		sb.append("         book.FEE_CODE,                                                                                        ");
		sb.append(" 		to_char(wm_concat (DISTINCT book.RECONCILE_NUM)) AS RECONCILE_NUM,                                             ");
		sb.append(" 		book.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'bgf' THEN                                                                                     ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS bg,                                                                                              ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'bj' THEN                                                                                      ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS bj,                                                                                              ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'sp' THEN                                                                                      ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS sp,                                                                                              ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'sjyh' THEN                                                                                    ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS sjyh,                                                                                            ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'hgyh' THEN                                                                                    ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS hgyh,                                                                                            ");
		sb.append(" 		book.if_receive,                                                                                      ");
		sb.append(" 		book.CRK_SIGN,                                                                                        ");
		sb.append(" 		TO_CHAR (                                                                                             ");
		sb.append(" 			book.input_date,                                                                                    ");
		sb.append(" 			'yyyy-mm'                                                                                           ");
		sb.append(" 		) AS input_date,                                                                                      ");
		sb.append(" 		to_char(wm_concat (DISTINCT book.INPUT_PERSON)) AS INPUT_PERSON                                                ");
		sb.append(" 	FROM                                                                                                    ");
		sb.append(" 		bis_standing_book book                                                                                ");
		sb.append(" 	WHERE                                                                                                   ");
		sb.append(" 		1 = 1                                                                                                 ");
		sb.append(" 	AND EXAMINE_SIGN = 1                                                                                    ");
		sb.append(" 	AND book.if_receive = 1                                                                                 ");
		sb.append(" 	AND book.FEE_CODE IN (                                                                                  ");
		sb.append(" 		'bgf',                                                                                                ");
		sb.append(" 		'bj',                                                                                                 ");
		sb.append(" 		'sp',                                                                                                 ");
		sb.append(" 		'sjyh',                                                                                               ");
		sb.append(" 		'hgyh'                                                                                                ");
		sb.append(" 	)                                                                                                       ");
		sb.append(" 	GROUP BY                                                                                                ");
		sb.append(" 		book.bill_num,                                                                                        ");
		sb.append(" 		book.customs_num,                                                                                     ");
		sb.append("     book.FEE_CODE,                                                                                        ");
		sb.append(" 		book.customs_name,                                                                                    ");
		sb.append(" 		TO_CHAR (                                                                                             ");
		sb.append(" 			book.input_date,                                                                                    ");
		sb.append(" 			'yyyy-mm'                                                                                           ");
		sb.append(" 		),                                                                                                    ");
		sb.append(" 		book.if_receive,                                                                                      ");
		sb.append(" 		book.CRK_SIGN,                                                                                        ");
		sb.append(" 		book.RECONCILE_SIGN                                                                                   ");
		sb.append(" ) ystemp                                                                                                  ");
		sb.append(" ON (yftemp.bill_num = ystemp.bill_num AND yftemp.FEE_CODE=ystemp.FEE_CODE)                                ");
		sb.append(" WHERE                                                                                                     ");
		sb.append(" 	1 = 1                                                                                                   ");
		if(!StringUtils.isNull(stock.getBillNum())){
			sb.append(" AND yftemp.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getsCustom())){
			sb.append(" AND yftemp.gys=:gys                   ");
			params.put("gys",stock.getsCustom());
		}
		if(!StringUtils.isNull(stock.getCustomsNum())){
			sb.append(" AND ystemp.customs_num=:customsnum    ");
			params.put("customsnum",stock.getCustomsNum());
		}
		if(null!=stock.getReconcileSign()&&!"".equals(stock.getReconcileSign())&&2!=stock.getReconcileSign()){
			sb.append(" AND ystemp.reconcile_sign=:reconcilesign    ");
			params.put("reconcilesign",stock.getReconcileSign());
		}
		if(!StringUtils.isNull(stock.getStraTime())){
			sb.append(" AND to_date(ystemp.input_date,'yyyy-mm') >= to_date(:strartTime,'yyyy-mm') ");
			params.put("strartTime", stock.getStraTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND to_date(ystemp.input_date,'yyyy-mm')< to_date(:endTime, 'yyyy-mm')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" GROUP BY                                                                                                  ");
		sb.append(" 	ystemp.input_date,                                                                                      ");
		sb.append(" 	ystemp.customs_name,                                                                                    ");
		sb.append(" 	yftemp.gysname,                                                                                         ");
		sb.append(" 	ystemp.bill_num,                                                                                        ");
		sb.append(" 	ystemp.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 	ystemp.RECONCILE_NUM,                                                                                   ");
		sb.append(" 	ystemp.INPUT_PERSON,                                                                                    ");
		sb.append(" 	yftemp.gys,                                                                                             ");
		sb.append(" 	ystemp.customs_num,                                                                                     ");
		sb.append(" 	ystemp.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 	ystemp.CRK_SIGN                                                                                         ");
		sb.append(" ORDER BY                                                                                                  ");
		sb.append(" 	ystemp.customs_name,                                                                                    ");
		sb.append(" 	ystemp.bill_num                                                                                         ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 导出excel
     * @param page
     * @param stock
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findCustomerRepot(BisStandingBook stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillNum())){
			String[] billList=stock.getBillNum().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                                                         ");
		sb.append(" 	temp.bill_num,                                               ");
		sb.append(" 	temp.customs_num,                                            ");
		sb.append(" 	temp.customs_name,                                           ");
		sb.append(" 	temp.fee_name,                                               ");
		sb.append(" 	temp.fee_code,                                               ");
		sb.append(" 	max(temp.sl) AS sl,                                          ");
		sb.append(" 	max(temp.SHOULD_RMB) AS je,                                  ");
		sb.append(" 	max(temp.ysdj) AS ysdj,                                      ");
		sb.append(" 	max(temp.yfdj) AS yfdj,                                      ");
		sb.append(" 	max(temp.ys) AS ys,                                          ");
		sb.append(" 	max(temp.yf) AS yf,                                          ");
		sb.append(" 	sum(temp.ys - temp.yf) AS lr,                                ");
		sb.append(" 	temp.input_date                                              ");
		sb.append(" FROM                                                           ");
		sb.append(" 	(                                                            ");
		sb.append(" 		SELECT                                                     ");
		sb.append(" 			book.bill_num,                                           ");
		sb.append(" 			book.customs_num,                                        ");
		sb.append(" 			book.customs_name,                                       ");
		sb.append(" 			sum(book.num) AS sl,                                     ");
		sb.append(" 			round(sum(book.SHOULD_RMB), 2) AS SHOULD_RMB,            ");
		sb.append(" 			book.fee_name,                                           ");
		sb.append(" 			book.fee_code,                                           ");
		sb.append(" 			book.if_receive,                                         ");
		sb.append(" 			CASE                                                     ");
		sb.append(" 		WHEN book.if_receive = 1 THEN                              ");
		sb.append(" 			max(book.price)                                          ");
		sb.append(" 		ELSE                                                       ");
		sb.append(" 			0                                                        ");
		sb.append(" 		END AS ysdj,                                               ");
		sb.append(" 		CASE                                                       ");
		sb.append(" 	WHEN book.if_receive = 2 THEN                                ");
		sb.append(" 		max(book.price)                                            ");
		sb.append(" 	ELSE                                                         ");
		sb.append(" 		0                                                          ");
		sb.append(" 	END AS yfdj,                                                 ");
		sb.append(" 	CASE                                                         ");
		sb.append(" WHEN book.if_receive = 1 THEN                                  ");
		sb.append(" 	round(sum(book.SHOULD_RMB), 2)                               ");
		sb.append(" ELSE                                                           ");
		sb.append(" 	0                                                            ");
		sb.append(" END AS ys,                                                     ");
		sb.append("  CASE                                                          ");
		sb.append(" WHEN book.if_receive = 2 THEN                                  ");
		sb.append(" 	round(sum(book.SHOULD_RMB), 2)                               ");
		sb.append(" ELSE                                                           ");
		sb.append(" 	0                                                            ");
		sb.append(" END AS yf,                                                     ");
		sb.append("  to_char (                                                     ");
		sb.append(" 	book.input_date,                                             ");
		sb.append(" 	'yyyy-mm-dd'                                                 ");
		sb.append(" ) AS input_date                                                ");
		sb.append(" FROM                                                           ");
		sb.append(" 	bis_standing_book book                                       ");
		sb.append(" WHERE 1=1                                                         ");
		if(!StringUtils.isNull(stock.getBillNum())){
			sb.append(" AND book.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getFeeName())){
			sb.append(" AND book.fee_name like '%"+stock.getFeeName()+"%'");
		}
		if(!StringUtils.isNull(stock.getCustomsNum())){
			sb.append(" AND book.customs_num=:customsnum");
			params.put("customsnum",stock.getCustomsNum());
		}
		if(!StringUtils.isNull(stock.getStraTime())){
			sb.append(" AND book.input_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStraTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND book.input_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" AND EXAMINE_SIGN = 1                                           ");
		sb.append(" GROUP BY                                                       ");
		sb.append(" 	book.bill_num,                                               ");
		sb.append(" 	book.customs_num,                                            ");
		sb.append(" 	book.customs_name,                                           ");
		sb.append(" 	book.fee_name,                                               ");
		sb.append(" 	book.fee_code,                                               ");
		sb.append(" 	book.if_receive,                                             ");
		sb.append(" 	to_char (input_date, 'yyyy-mm-dd')                           ");
		sb.append(" 	) temp                                                       ");
		sb.append(" GROUP BY                                                       ");
		sb.append(" 	temp.bill_num,                                               ");
		sb.append(" 	temp.customs_num,                                            ");
		sb.append(" 	temp.customs_name,                                           ");
		sb.append(" 	temp.fee_name,                                               ");
		sb.append(" 	temp.fee_code,                                               ");
		sb.append(" 	temp.input_date                                              ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> customersum(BisStandingBook stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillNum())){
			String[] billList=stock.getBillNum().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                                                            ");
		sb.append(" 	'' AS bill_num,                                               ");
		sb.append(" 	'' AS customs_num,                                            ");
		sb.append(" 	'合计：' AS customs_name,                                       ");
		sb.append(" 	'' AS fee_name,                                               ");
		sb.append(" 	'' AS fee_code,                                               ");
		sb.append(" 	'' AS sl,                                                     ");
		sb.append(" 	'' AS je,                                                     ");
		sb.append(" 	'' AS ysdj,                                                   ");
		sb.append(" 	'' AS yfdj,                                                   ");
		sb.append(" 	SUM(temp.ys) AS ys,                                           ");
		sb.append(" 	SUM(temp.yf) AS yf,                                           ");
		sb.append(" 	sum(temp.ys - temp.yf) AS lr,                                 ");
		sb.append(" 	'' AS input_date                                              ");
		sb.append(" FROM                                                              ");
		sb.append(" 	(                                                             ");
		sb.append(" 		SELECT                                                    ");
		sb.append(" 			book.bill_num,                                        ");
		sb.append(" 			book.customs_num,                                     ");
		sb.append(" 			book.customs_name,                                    ");
		sb.append(" 			sum(book.num) AS sl,                                  ");
		sb.append(" 			round(sum(book.SHOULD_RMB), 2) AS SHOULD_RMB,         ");
		sb.append(" 			book.fee_name,                                        ");
		sb.append(" 			book.fee_code,                                        ");
		sb.append(" 			book.if_receive,                                      ");
		sb.append(" 			CASE                                                  ");
		sb.append(" 		WHEN book.if_receive = 1 THEN                             ");
		sb.append(" 			max(book.price)                                       ");
		sb.append(" 		ELSE                                                      ");
		sb.append(" 			0                                                     ");
		sb.append(" 		END AS ysdj,                                              ");
		sb.append(" 		CASE                                                      ");
		sb.append(" 	WHEN book.if_receive = 2 THEN                                ");
		sb.append(" 		max(book.price)                                            ");
		sb.append(" 	ELSE                                                         ");
		sb.append(" 		0                                                          ");
		sb.append(" 	END AS yfdj,                                                 ");
		sb.append(" 	CASE                                                         ");
		sb.append(" WHEN book.if_receive = 1 THEN                                  ");
		sb.append(" 	round(sum(book.SHOULD_RMB), 2)                               ");
		sb.append(" ELSE                                                           ");
		sb.append(" 	0                                                            ");
		sb.append(" END AS ys,                                                     ");
		sb.append("  CASE                                                          ");
		sb.append(" WHEN book.if_receive = 2 THEN                                  ");
		sb.append(" 	round(sum(book.SHOULD_RMB), 2)                               ");
		sb.append(" ELSE                                                           ");
		sb.append(" 	0                                                            ");
		sb.append(" END AS yf,                                                     ");
		sb.append("  to_char (                                                     ");
		sb.append(" 	book.input_date,                                             ");
		sb.append(" 	'yyyy-mm-dd'                                                 ");
		sb.append(" ) AS input_date                                                ");
		sb.append(" FROM                                                           ");
		sb.append(" 	bis_standing_book book                                       ");
		sb.append(" WHERE 1=1                                                         ");
		if(!StringUtils.isNull(stock.getBillNum())){
			sb.append(" AND book.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getFeeName())){
			sb.append(" AND book.fee_name like '%"+stock.getFeeName()+"%'");
		}
		if(!StringUtils.isNull(stock.getCustomsNum())){
			sb.append(" AND book.customs_num=:customsnum");
			params.put("customsnum",stock.getCustomsNum());
		}
		if(!StringUtils.isNull(stock.getStraTime())){
			sb.append(" AND book.input_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStraTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND book.input_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" AND EXAMINE_SIGN = 1                                           ");
		sb.append(" GROUP BY                                                       ");
		sb.append(" 	book.bill_num,                                               ");
		sb.append(" 	book.customs_num,                                            ");
		sb.append(" 	book.customs_name,                                           ");
		sb.append(" 	book.fee_name,                                               ");
		sb.append(" 	book.fee_code,                                               ");
		sb.append(" 	book.if_receive,                                             ");
		sb.append(" 	to_char (input_date, 'yyyy-mm-dd')                           ");
		sb.append(" 	) temp                                                       ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> suppliersum(BisStandingBook stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillNum())){
			String[] billList=stock.getBillNum().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                             ");
		sb.append(" 	'合计:' AS crkType,             ");
		sb.append("     '' AS straTime,                ");
		sb.append("     '' AS customsName,             ");
		sb.append(" 	'' AS gysName,                 ");
		sb.append(" 	'' AS billNum,                 ");
		sb.append("     '' AS reconcileNum,            ");
		sb.append("     SUM (bg) AS bg,                ");
		sb.append(" 	SUM (bj) AS bj,                ");
		sb.append(" 	SUM (sp) AS sp,                ");
		sb.append(" 	SUM (sjyh) AS sjyh,            ");
		sb.append(" 	SUM (hgyh) AS hgyh,            ");
		sb.append("     SUM (lR) AS lR,                ");
		sb.append("     '' AS inputPerson,             ");
		sb.append(" 	'' AS gys,                     ");
		sb.append(" 	'' AS customs_num,             ");
		sb.append(" 	'' AS RECONCILE_SIGN,          ");
		sb.append(" 	'' AS CRK_SIGN                 ");
		sb.append(" FROM                               ");
		sb.append(" 	(                              ");
		sb.append(" SELECT                                                                                                      ");
		sb.append(" 	CASE ystemp.CRK_SIGN                                                                                    ");
		sb.append(" 		WHEN '1' THEN                                                                                       ");
		sb.append(" 			'入库'                                                                                            ");
		sb.append(" 		WHEN '2' THEN                                                                                        ");
		sb.append(" 			'出库'                                                                                            ");
		sb.append(" 		WHEN '3' THEN                                                                                        ");
		sb.append(" 			'货转'                                                                                            ");
		sb.append(" 		ELSE                                                                                                 ");
		sb.append(" 			'其他'                                                                                            ");
		sb.append(" 	END AS crkType,                                                                                          ");
		sb.append(" 	ystemp.input_date AS straTime,                                                                           ");
		sb.append(" 	ystemp.customs_name AS customsName,                                                                     ");
		sb.append(" 	yftemp.gysname AS gysName,                                                                              ");
		sb.append(" 	ystemp.bill_num AS billNum,                                                                             ");
		sb.append(" 	CASE ystemp.RECONCILE_SIGN WHEN '1' THEN '是' ELSE '否' END AS reconcileType,                            ");
		sb.append(" 	ystemp.RECONCILE_NUM AS reconcileNum,                                                                   ");
		sb.append(" 	sum(ystemp.bg) AS bg,                                                                                   ");
		sb.append(" 	sum(ystemp.bj) AS bj,                                                                                   ");
		sb.append(" 	sum(ystemp.sp)  AS sp,                                                                                  ");
		sb.append(" 	sum(ystemp.sjyh) AS sjyh,                                                                               ");
		sb.append(" 	sum(ystemp.hgyh) AS hgyh,                                                                               ");
		sb.append(" 	(sum(ystemp.bg) + sum(ystemp.bj) + sum(ystemp.sp) + sum(ystemp.sjyh) + sum(ystemp.hgyh)) AS lR,         ");
		sb.append(" 	ystemp.INPUT_PERSON AS inputPerson,                                                                     ");
		sb.append(" 	yftemp.gys AS gys,                                                                                      ");
		sb.append(" 	ystemp.customs_num,                                                                                     ");
		sb.append(" 	ystemp.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 	ystemp.CRK_SIGN                                                                                         ");
		sb.append(" FROM                                                                                                        ");
		sb.append(" 	(                                                                                                       ");
		sb.append(" 		SELECT                                                                                              ");
		sb.append(" 			book.bill_num,                                                                                  ");
		sb.append(" 			to_char(wm_concat (DISTINCT book.customs_num)) AS gys,                                                   ");
		sb.append("             to_char(wm_concat (DISTINCT book.customs_name)) AS gysname,                                              ");
		sb.append("             book.FEE_CODE,                                                                                  ");
		sb.append(" 			MAX (                                                                                           ");
		sb.append(" 				CASE book.fee_code                                                                          ");
		sb.append(" 				WHEN 'bgf' THEN                                                                             ");
		sb.append(" 					book.SHOULD_RMB                                                                         ");
		sb.append(" 				ELSE                                                                                        ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfbg,                                                                                          ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'bj' THEN                                                                                    ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfbj,                                                                                          ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'sp' THEN                                                                                    ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfsp,                                                                                          ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'sjyh' THEN                                                                                  ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfsjyh,                                                                                        ");
		sb.append(" 			MAX (                                                                                               ");
		sb.append(" 				CASE book.fee_code                                                                                ");
		sb.append(" 				WHEN 'hgyh' THEN                                                                                  ");
		sb.append(" 					book.SHOULD_RMB                                                                                 ");
		sb.append(" 				ELSE                                                                                              ");
		sb.append(" 					0                                                                                               ");
		sb.append(" 				END                                                                                               ");
		sb.append(" 			) AS yfhgyh,                                                                                        ");
		sb.append(" 			book.if_receive,                                                                                    ");
		sb.append(" 			TO_CHAR (                                                                                           ");
		sb.append(" 				book.input_date,                                                                                  ");
		sb.append(" 				'yyyy-mm'                                                                                         ");
		sb.append(" 			) AS input_date                                                                                     ");
		sb.append(" 		FROM                                                                                                  ");
		sb.append(" 			bis_standing_book book                                                                              ");
		sb.append(" 		WHERE                                                                                                 ");
		sb.append(" 			1 = 1                                                                                               ");
		sb.append(" 		AND EXAMINE_SIGN = 1                                                                                  ");
		sb.append(" 		AND book.if_receive = 2                                                                               ");
		sb.append(" 		AND book.FEE_CODE IN (                                                                                ");
		sb.append(" 			'bgf',                                                                                              ");
		sb.append(" 			'bj',                                                                                               ");
		sb.append(" 			'sp',                                                                                               ");
		sb.append(" 			'sjyh',                                                                                             ");
		sb.append(" 			'hgyh'                                                                                              ");
		sb.append(" 		)                                                                                                     ");
		sb.append(" 		GROUP BY                                                                                              ");
		sb.append(" 			book.bill_num,                                                                                      ");
		sb.append(" 			book.if_receive,                                                                                    ");
		sb.append("             book.FEE_CODE,                                                                                      ");
		sb.append(" 			TO_CHAR (                                                                                           ");
		sb.append(" 				book.input_date,                                                                                  ");
		sb.append(" 				'yyyy-mm'                                                                                         ");
		sb.append(" 			)                                                                                                   ");
		sb.append(" 	) yftemp                                                                                                ");
		sb.append(" LEFT JOIN(                                                                                                ");
		sb.append(" 	SELECT                                                                                                  ");
		sb.append(" 		book.bill_num,                                                                                        ");
		sb.append(" 		book.customs_num,                                                                                     ");
		sb.append(" 		book.customs_name,                                                                                    ");
		sb.append("         book.FEE_CODE,                                                                                        ");
		sb.append(" 		to_char(wm_concat (DISTINCT book.RECONCILE_NUM)) AS RECONCILE_NUM,                                             ");
		sb.append(" 		book.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'bgf' THEN                                                                                     ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS bg,                                                                                              ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'bj' THEN                                                                                      ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS bj,                                                                                              ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'sp' THEN                                                                                      ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS sp,                                                                                              ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'sjyh' THEN                                                                                    ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS sjyh,                                                                                            ");
		sb.append(" 		MAX (                                                                                                 ");
		sb.append(" 			CASE book.fee_code                                                                                  ");
		sb.append(" 			WHEN 'hgyh' THEN                                                                                    ");
		sb.append(" 				book.SHOULD_RMB                                                                                   ");
		sb.append(" 			ELSE                                                                                                ");
		sb.append(" 				0                                                                                                 ");
		sb.append(" 			END                                                                                                 ");
		sb.append(" 		) AS hgyh,                                                                                            ");
		sb.append(" 		book.if_receive,                                                                                      ");
		sb.append(" 		book.CRK_SIGN,                                                                                        ");
		sb.append(" 		TO_CHAR (                                                                                             ");
		sb.append(" 			book.input_date,                                                                                    ");
		sb.append(" 			'yyyy-mm'                                                                                           ");
		sb.append(" 		) AS input_date,                                                                                      ");
		sb.append(" 		to_char(wm_concat (DISTINCT book.INPUT_PERSON)) AS INPUT_PERSON                                                ");
		sb.append(" 	FROM                                                                                                    ");
		sb.append(" 		bis_standing_book book                                                                                ");
		sb.append(" 	WHERE                                                                                                   ");
		sb.append(" 		1 = 1                                                                                                 ");
		sb.append(" 	AND EXAMINE_SIGN = 1                                                                                    ");
		sb.append(" 	AND book.if_receive = 1                                                                                 ");
		sb.append(" 	AND book.FEE_CODE IN (                                                                                  ");
		sb.append(" 		'bgf',                                                                                                ");
		sb.append(" 		'bj',                                                                                                 ");
		sb.append(" 		'sp',                                                                                                 ");
		sb.append(" 		'sjyh',                                                                                               ");
		sb.append(" 		'hgyh'                                                                                                ");
		sb.append(" 	)                                                                                                       ");
		sb.append(" 	GROUP BY                                                                                                ");
		sb.append(" 		book.bill_num,                                                                                        ");
		sb.append(" 		book.customs_num,                                                                                     ");
		sb.append("     book.FEE_CODE,                                                                                        ");
		sb.append(" 		book.customs_name,                                                                                    ");
		sb.append(" 		TO_CHAR (                                                                                             ");
		sb.append(" 			book.input_date,                                                                                    ");
		sb.append(" 			'yyyy-mm'                                                                                           ");
		sb.append(" 		),                                                                                                    ");
		sb.append(" 		book.if_receive,                                                                                      ");
		sb.append(" 		book.CRK_SIGN,                                                                                        ");
		sb.append(" 		book.RECONCILE_SIGN                                                                                   ");
		sb.append(" ) ystemp                                                                                                  ");
		sb.append(" ON (yftemp.bill_num = ystemp.bill_num AND yftemp.FEE_CODE=ystemp.FEE_CODE)                                ");
		sb.append(" WHERE                                                                                                     ");
		sb.append(" 	1 = 1                                                                                                   ");
		if(!StringUtils.isNull(stock.getBillNum())){
			sb.append(" AND yftemp.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getsCustom())){
			sb.append(" AND yftemp.gys=:gys                   ");
			params.put("gys",stock.getsCustom());
		}
		if(!StringUtils.isNull(stock.getCustomsNum())){
			sb.append(" AND ystemp.customs_num=:customsnum    ");
			params.put("customsnum",stock.getCustomsNum());
		}
		if(null!=stock.getReconcileSign()&&!"".equals(stock.getReconcileSign())&&2!=stock.getReconcileSign()){
			sb.append(" AND ystemp.reconcile_sign=:reconcilesign    ");
			params.put("reconcilesign",stock.getReconcileSign());
		}
		if(!StringUtils.isNull(stock.getStraTime())){
			sb.append(" AND to_date(ystemp.input_date,'yyyy-mm') >= to_date(:strartTime, 'yyyy-mm') ");
			params.put("strartTime", stock.getStraTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND to_date(ystemp.input_date,'yyyy-mm') < to_date(:endTime, 'yyyy-mm')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" GROUP BY                                                                                                  ");
		sb.append(" 	ystemp.input_date,                                                                                      ");
		sb.append(" 	ystemp.customs_name,                                                                                    ");
		sb.append(" 	yftemp.gysname,                                                                                         ");
		sb.append(" 	ystemp.bill_num,                                                                                        ");
		sb.append(" 	ystemp.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 	ystemp.RECONCILE_NUM,                                                                                   ");
		sb.append(" 	ystemp.INPUT_PERSON,                                                                                    ");
		sb.append(" 	yftemp.gys,                                                                                             ");
		sb.append(" 	ystemp.customs_num,                                                                                     ");
		sb.append(" 	ystemp.RECONCILE_SIGN,                                                                                  ");
		sb.append(" 	ystemp.CRK_SIGN                                                                                         ");
		sb.append(" )                                                                                                           ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
