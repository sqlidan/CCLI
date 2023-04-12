package com.haiersoft.ccli.base.dao;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import java.util.HashMap;
import java.util.Map;
@Repository
public class SkuInfoDao extends HibernateDao<BaseSkuBaseInfo, String> {

    public String updateSKUInfo(BisEnterStockInfo bisEnterStockInfo) {

        Map<String, String> params = new HashMap<>();
        String sql = "UPDATE base_sku_base_info t SET t.SKU_ID = :sku_id ";

        if (StringUtils.nonNull(bisEnterStockInfo.getLotNum())) {
            sql += " ,t.LOT_NUM = :lot_num ";
            params.put("lot_num", bisEnterStockInfo.getLotNum());
        }

        if (StringUtils.nonNull(bisEnterStockInfo.getRkNum())) {
            sql += " ,t.RKDH = :rkdh ";
            params.put("rkdh", bisEnterStockInfo.getRkNum());
        }

        if (StringUtils.nonNull(bisEnterStockInfo.getMscNum())) {
            sql += " ,t.MSC_NUM = :msc_num ";
            params.put("msc_num", bisEnterStockInfo.getMscNum());
        }

        sql += " WHERE t.SKU_ID = :sku_id ";
        params.put("sku_id", bisEnterStockInfo.getSku());

        SQLQuery sqlQuery = createSQLQuery(sql, params);
        int rows = sqlQuery.executeUpdate();

        return rows > 0 ? "success" : "";
    }

}
