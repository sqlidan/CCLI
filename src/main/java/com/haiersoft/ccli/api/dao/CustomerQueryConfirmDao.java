/**
 *
 */
package com.haiersoft.ccli.api.dao;

import com.haiersoft.ccli.api.entity.ApiCustomerQueryConfirm;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 *
 */
@Repository
public class CustomerQueryConfirmDao extends HibernateDao<ApiCustomerQueryConfirm, String> {


    public List<String> findClientIdList(String recordId) {
        String hql="select con.clientId from ApiCustomerQueryConfirm con where con.applyId=?0";
        Query query= createQuery(hql, recordId);
        return query.list();
    }

    public List<Integer> checkCLients(String queryStr) {
        String hql="select DISTINCT (con.clientId) FROM ApiCustomerQueryConfirm con WHERE con.clientId in ("+queryStr+")";
        Query query= createQuery(hql);
        return query.list();
    }
}
