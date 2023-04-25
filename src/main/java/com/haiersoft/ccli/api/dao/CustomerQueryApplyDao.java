/**
 *
 */
package com.haiersoft.ccli.api.dao;

import com.haiersoft.ccli.api.entity.ApiCustomerQueryApply;
import com.haiersoft.ccli.api.entity.ApiCustomerQueryVo;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 *
 */
@Repository
public class CustomerQueryApplyDao extends HibernateDao<ApiCustomerQueryApply, String> {

    public List<Map<String,String>> findQueryResults(String accountId, String taxNumber) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accountId", accountId);
        if (StringUtils.isNotBlank(taxNumber)) {
            params.put("taxNumber", taxNumber);
        }

        StringBuffer sb = new StringBuffer("select ");
        sb.append(" t.ACCOUNT_ID as accountId,t.TRACE_ID as traceId,t.CUSTOMER_NAME as customerName," +
				"t.CUSTOMER_NUMBER as customerNumber,t.CREATE_DATE ,t.TAX_NUMBER ,t.FILE_URL ,t.REMARK ,t.CONFIRM_STATUS ,t.REASON ");
        sb.append("FROM API_CUSTOMER_QUERY_APPLY t ");

        sb.append(" WHERE t.ACCOUNT_ID = :accountId");
        if (StringUtils.isNotBlank(taxNumber)) {
            sb.append(" AND t.TAX_NUMBER = :taxNumber");
        }
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
        return sqlQuery.list();
    }
}
