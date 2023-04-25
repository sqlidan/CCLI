package com.haiersoft.ccli.cost.service;


import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.BaseExpenseCategoryDetailDao;
import com.haiersoft.ccli.cost.entity.BaseExpenseCategoryDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class BaseExpenseCategoryDetailService extends BaseService<BaseExpenseCategoryDetail, Integer> {

    @Autowired
    private BaseExpenseCategoryDetailDao baseExpenseCategoryDetailDao;

    @Override
    public HibernateDao<BaseExpenseCategoryDetail, Integer> getEntityDao() {

        return baseExpenseCategoryDetailDao;
    }

    public Map<String, Object> getCodeByFeeCode(String feecode) {

        return baseExpenseCategoryDetailDao.getCodeByFeeCode(feecode);
    }
}
