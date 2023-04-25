package com.haiersoft.ccli.api.service;

import com.haiersoft.ccli.api.dao.CustomerQueryApplyDao;
import com.haiersoft.ccli.api.entity.ApiCustomerQueryApply;
import com.haiersoft.ccli.api.entity.ApiCustomerQueryVo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerQueryApplyService extends BaseService<ApiCustomerQueryApply, String> {
    @Autowired
    private CustomerQueryApplyDao customerQueryApplyDao;

    @Override
    public HibernateDao<ApiCustomerQueryApply, String> getEntityDao() {
        return customerQueryApplyDao;
    }

    public List<Map<String, String>> findQueryResults(String accountId, String taxNumber) {
        return customerQueryApplyDao.findQueryResults(accountId, taxNumber);


    }

    public List<ApiCustomerQueryApply> listByTaxNumber(String taxNumber) {
        List<PropertyFilter> filters = new ArrayList<>();
        filters.add(new PropertyFilter("EQS_taxNumber", taxNumber));
        return this.search(filters);
    }

    public List<ApiCustomerQueryApply> findApply(String accountId, String taxNumber) {
        List<PropertyFilter> filters = new ArrayList<>();
        filters.add(new PropertyFilter("EQS_accountId", accountId));
        if (!StringUtils.isBlank(taxNumber)) {
            filters.add(new PropertyFilter("EQS_taxNumber", taxNumber));
        }
        return this.search(filters);


    }


}
