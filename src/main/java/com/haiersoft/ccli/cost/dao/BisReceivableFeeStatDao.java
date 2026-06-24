package com.haiersoft.ccli.cost.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BisReceivableFeeStat;

@Repository
public class BisReceivableFeeStatDao extends HibernateDao<BisReceivableFeeStat, Long> {

    public int rebuildYesterdayReceivableFeeStat() {
        Date todayStart = truncate(new Date());
        Date statDate = addDays(todayStart, -1);
        return rebuildReceivableFeeStat(statDate, todayStart);
    }

    public int rebuildTodayReceivableFeeStat() {
        return rebuildYesterdayReceivableFeeStat();
    }

    private int rebuildReceivableFeeStat(Date statDate, Date nextDate) {
        int deleted = deleteByStatDate(statDate);
        logger.info("Deleted {} receivable fee statistic records for statDate {}.",
                deleted, formatDate(statDate));

        List<BisReceivableFeeStat> nonStorageStats = queryNonStorageFeeStats(statDate, nextDate);
        List<BisReceivableFeeStat> storageStats = queryStorageFeeStats(statDate, nextDate);

        int inserted = saveStats(nonStorageStats) + saveStats(storageStats);
        logger.info("Inserted {} receivable fee statistic records for statDate {}, nonStorage={}, storage={}.",
                inserted, formatDate(statDate), nonStorageStats.size(), storageStats.size());
        return inserted;
    }

    private int deleteByStatDate(Date statDate) {
        SQLQuery query = createSQLQuery("delete from BIS_RECEIVABLE_FEE_STAT where STAT_DATE = :statDate");
        query.setDate("statDate", statDate);
        return query.executeUpdate();
    }

    /**
     * 保留原非堆存费查询口径：BIS_STANDING_BOOK 关联费目表，仅把查询结果交给 Java 插入。
     */
    @SuppressWarnings("unchecked")
    private List<BisReceivableFeeStat> queryNonStorageFeeStats(Date statDate, Date nextDate) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" nvl(book.CUSTOMS_NAME, 'UNKNOWN') as customerName, ");
        sql.append(" :accountPeriod as accountPeriod, ");
        sql.append(" :statDate as statDate, ");
        sql.append(" book.FEE_CODE as feeCode, ");
        sql.append(" nvl(fc.NAME_C, book.FEE_NAME) as feeName, ");
        sql.append(" nvl(book.SHOULD_RMB, 0) as amount ");
        sql.append(" from BIS_STANDING_BOOK book ");
        sql.append(" left join BASE_EXPENSE_CATEGORY_INFO fc on fc.CODE = book.FEE_CODE ");
        sql.append(" where book.IF_RECEIVE = '1' ");
        sql.append(" and book.FEE_CODE is not null ");
        sql.append(" and nvl(fc.FEE_TYPE, 0) <> 2 ");
        sql.append(" and book.INPUT_DATE >= :statDate ");
        sql.append(" and book.INPUT_DATE < :nextDate ");
        sql.append(" and nvl(book.SHOULD_RMB, 0) <> 0 ");

        SQLQuery query = createReceivableFeeStatQuery(sql.toString());
        query.setString("accountPeriod", buildAccountPeriod(statDate));
        query.setDate("statDate", statDate);
        query.setTimestamp("nextDate", nextDate);
        List<BisReceivableFeeStat> result = query.list();
        logger.info("Queried {} non-storage receivable fee records for statDate {}.",
                result.size(), formatDate(statDate));
        return result;
    }

    /**
     * 保留原堆存费查询口径：BIS_ASN_ACTION 关联费用方案、费目、汇率、客户表，仅把查询结果交给 Java 插入。
     */
    @SuppressWarnings("unchecked")
    private List<BisReceivableFeeStat> queryStorageFeeStats(Date statDate, Date nextDate) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" nvl(client.CLIENT_NAME, 'UNKNOWN') as customerName, ");
        sql.append(" :accountPeriod as accountPeriod, ");
        sql.append(" :statDate as statDate, ");
        sql.append(" scheme.FEE_CODE as feeCode, ");
        sql.append(" nvl(fc.NAME_C, scheme.FEE_NAME) as feeName, ");
        sql.append(" ( ");
        sql.append("   case ");
        sql.append("     when scheme.BILLING = '1' then nvl(scheme.UNIT, 0) * nvl(action.NUM, 0) ");
        sql.append("     when scheme.BILLING = '2' then nvl(scheme.UNIT, 0) * nvl(action.GROSS_WEIGHT, 0) / 1000 ");
        sql.append("     when scheme.BILLING = '3' then nvl(scheme.UNIT, 0) * nvl(action.NET_WEIGHT, 0) / 1000 ");
        sql.append("     else 0 ");
        sql.append("   end * nvl(tax.EXCHANGE_RATE, 1) ");
        sql.append(" ) as amount ");
        sql.append(" from BIS_ASN_ACTION action ");
        sql.append(" inner join BASE_EXPENSE_SCHEME_INFO scheme on scheme.SCHEME_NUM = action.FEE_PLAN_ID ");
        sql.append(" left join BASE_EXPENSE_CATEGORY_INFO fc on fc.CODE = scheme.FEE_CODE ");
        sql.append(" left join BASE_TAX_RATE tax on tax.CURRENCY_TYPE = scheme.CURRENCY ");
        sql.append(" left join BASE_CLIENT_INFO client on to_char(client.IDS) = action.JFCLIENT_ID ");
        sql.append(" where action.STATUS = '1' ");
        sql.append(" and action.CLEAN_SIGN = '0' ");
        sql.append(" and action.NUM is not null ");
        sql.append(" and action.CHARGE_STA_DATE is not null ");
        sql.append(" and scheme.FEE_TYPE = '2' ");
        sql.append(" and scheme.FEE_CODE is not null ");
        sql.append(" and action.CHARGE_STA_DATE < :nextDate ");
        sql.append(" and (action.CHARGE_END_DATE is null or action.CHARGE_END_DATE >= :statDate) ");
        sql.append(" and ( ");
        sql.append("   case ");
        sql.append("     when scheme.BILLING = '1' then nvl(scheme.UNIT, 0) * nvl(action.NUM, 0) ");
        sql.append("     when scheme.BILLING = '2' then nvl(scheme.UNIT, 0) * nvl(action.GROSS_WEIGHT, 0) / 1000 ");
        sql.append("     when scheme.BILLING = '3' then nvl(scheme.UNIT, 0) * nvl(action.NET_WEIGHT, 0) / 1000 ");
        sql.append("     else 0 ");
        sql.append("   end * nvl(tax.EXCHANGE_RATE, 1) ");
        sql.append(" ) <> 0 ");

        SQLQuery query = createReceivableFeeStatQuery(sql.toString());
        query.setString("accountPeriod", buildAccountPeriod(statDate));
        query.setDate("statDate", statDate);
        query.setTimestamp("nextDate", nextDate);
        List<BisReceivableFeeStat> result = query.list();
        logger.info("Queried {} storage receivable fee records for statDate {}.",
                result.size(), formatDate(statDate));
        return result;
    }

    private SQLQuery createReceivableFeeStatQuery(String sql) {
        SQLQuery query = createSQLQuery(sql);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("accountPeriod", StandardBasicTypes.STRING);
        query.addScalar("statDate", StandardBasicTypes.DATE);
        query.addScalar("feeCode", StandardBasicTypes.STRING);
        query.addScalar("feeName", StandardBasicTypes.STRING);
        query.addScalar("amount", StandardBasicTypes.BIG_DECIMAL);
        query.setResultTransformer(Transformers.aliasToBean(BisReceivableFeeStat.class));
        return query;
    }

    private int saveStats(List<BisReceivableFeeStat> stats) {
        int count = 0;
        for (BisReceivableFeeStat stat : filterValidStats(stats)) {
            save(stat);
            count++;
            if (count % 100 == 0) {
                getSession().flush();
                getSession().clear();
            }
        }
        return count;
    }

    private List<BisReceivableFeeStat> filterValidStats(List<BisReceivableFeeStat> stats) {
        List<BisReceivableFeeStat> result = new ArrayList<BisReceivableFeeStat>();
        for (BisReceivableFeeStat stat : stats) {
            if (stat != null && stat.getAmount() != null && BigDecimal.ZERO.compareTo(stat.getAmount()) != 0) {
                stat.setAmount(stat.getAmount().setScale(4, RoundingMode.HALF_UP));
                result.add(stat);
            }
        }
        return result;
    }

    private String buildAccountPeriod(Date statDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(statDate);
        if (calendar.get(Calendar.DAY_OF_MONTH) >= 26) {
            calendar.add(Calendar.MONTH, 1);
        }
        return new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
    }

    private Date truncate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
