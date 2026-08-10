package com.haiersoft.ccli.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.base.dao.CustomerExpenseSchemeDao;
import com.haiersoft.ccli.base.entity.CustomerExpenseSchemeInfoView;
import com.haiersoft.ccli.base.entity.CustomerExpenseSchemeQuery;
import com.haiersoft.ccli.base.entity.CustomerExpenseSchemeView;
import com.haiersoft.ccli.common.persistence.Page;

/**
 * 客户费用方案明细查询服务。
 */
@Service
@Transactional(readOnly = true)
public class CustomerExpenseSchemeService {

    @Autowired
    private CustomerExpenseSchemeDao customerExpenseSchemeDao;

    /**
     * 分页查询客户费用方案明细列表。
     */
    public Page<CustomerExpenseSchemeView> findPage(Page<CustomerExpenseSchemeView> page,
            CustomerExpenseSchemeQuery query) {
        return customerExpenseSchemeDao.findPage(page, query);
    }

    /**
     * 查询客户费用方案主表导出数据。
     */
    public List<CustomerExpenseSchemeView> findList(CustomerExpenseSchemeQuery query) {
        return customerExpenseSchemeDao.findList(query);
    }

    /**
     * 查询客户费用方案详情页的主表数据。
     */
    public CustomerExpenseSchemeView findBySchemeNum(String schemeNum) {
        return customerExpenseSchemeDao.findBySchemeNum(schemeNum);
    }

    /**
     * 查询客户费用方案子表导出数据。
     */
    public List<CustomerExpenseSchemeInfoView> findInfoList(CustomerExpenseSchemeQuery query) {
        return customerExpenseSchemeDao.findInfoList(query);
    }
}
