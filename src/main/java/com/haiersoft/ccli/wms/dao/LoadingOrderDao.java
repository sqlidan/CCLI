package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;

@Repository
public class LoadingOrderDao extends HibernateDao<BisLoadingOrder, String> {

    /**
     * 根据出库联系单号获取联系单明细和出库订单明细出库货物件数
     *
     * @param outCode
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOutCodeAndLoadingCodeNum(String outCode) {
        if (outCode != null && !"".equals(outCode)) {
            StringBuffer sb = new StringBuffer();
            HashMap<String, Object> parme = new HashMap<String, Object>();
            sb.append(" select  a.sku_id, a.OUT_NUM as apiece,nvl(b.piece,0) as bpiece from ");
            sb.append(" (select * from BIS_OUT_STOCK_INFO  t where t.out_link_id=:outid1) a ");
            sb.append("  left join  ");
            sb.append(" (select * from bis_loading_order_info l where l.out_link_id=:outid2) b on a.sku_id=b.sku_id");
            parme.put("outid1", outCode);
            parme.put("outid2", outCode);
            SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
            return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
        return null;
    }

    //查找选择了【是最后一车】的出库订单，按照出库订单的明细，按照group by asn、sku
    @SuppressWarnings("unchecked")
	public List<BisLoadingInfo> getObjList(String outLinkId) {
        StringBuffer sb = new StringBuffer("select a.stock_id as stockId,a.asn_id as asnId,a.sku_id as skuId,sum(a.piece) as piece,sum(a.net_weight) as netWeight,sum(a.gross_weight) as grossWeight from bis_loading_info a  ");
        sb.append(" left join bis_loading_order b on a.loading_plan_num = b.order_num ");
        sb.append(" where a.out_link_id = ?0 and b.if_has_cleared ='0' and b.last_car = '1' ");
        sb.append(" group by a.stock_id,a.asn_id,a.sku_id ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), outLinkId);
        sqlQuery.addScalar("stockId", StandardBasicTypes.STRING);
        sqlQuery.addScalar("asnId", StandardBasicTypes.STRING);
        sqlQuery.addScalar("skuId", StandardBasicTypes.STRING);
        sqlQuery.addScalar("piece", StandardBasicTypes.INTEGER);
        sqlQuery.addScalar("netWeight", StandardBasicTypes.DOUBLE);
        sqlQuery.addScalar("grossWeight", StandardBasicTypes.DOUBLE);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(BisLoadingInfo.class));
        return sqlQuery.list();
    }

    /**
     * 通过出库订单号获取信息
     *
     * @param orderId
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getOrderSumNum(String orderId) {

        Map<String, Object> params = new HashMap<>();
        params.put("num", orderId);

        String sql = "select t.PIECE from BIS_LOADING_ORDER_INFO t where LOADING_PLAN_NUM = :num";

        SQLQuery sqlQuery = createSQLQuery(sql,params);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

    }

	public void updateState(String loadingPlanNum) {
		StringBuffer sb = new StringBuffer();
        //HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append(" update bis_loading_order t set t.ORDER_STATE ='4' where t.ORDER_NUM=?0 ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), loadingPlanNum);
        sqlQuery.executeUpdate();
	}

}
