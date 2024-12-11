package com.haiersoft.ccli.report.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.report.entity.AAccountBook;
import com.haiersoft.ccli.report.entity.ATray;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class AAccountBookDao extends HibernateDao<AAccountBook, String> {

    public List<Map<String,Object>> queryClearInventorySData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT a.* " +
                " from A_ACCOUNTBOOK a " +
                " where a.EMSNO = 'NH4230210001' and a.CUTQTY > 0 and nvl(FINISH,'0') = '0' and nvl(ISS,'0') = '0'");

        SQLQuery sqlQuery=createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<Map<String,Object>> queryClearInventoryHData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT a.* " +
                " from A_ACCOUNTBOOK a " +
                " where a.EMSNO = 'NH4230210001' a.CUTQTY > 0 and nvl(FINISH,'0') = '0'  and nvl(ISS,'0') = '1' and nvl(ISH,'0') = '0'");

        SQLQuery sqlQuery=createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
