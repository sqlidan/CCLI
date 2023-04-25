package com.haiersoft.ccli.cost.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.PlatformWorkTicketDao;
import com.haiersoft.ccli.cost.entity.PlatformWorkTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Stevedore
 *
 * @author test
 * @date 2022
 */
@Service
@Transactional(readOnly = true)
public class PlatformWorkTicketService extends BaseService<PlatformWorkTicket, String> {

    @Autowired
    private PlatformWorkTicketDao dao;

    @Override
    public HibernateDao<PlatformWorkTicket, String> getEntityDao() {
        return dao;
    }

    public Page<PlatformWorkTicket> page(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, PlatformWorkTicketDao.QueryType queryType, String orderBy) {
        return dao.pageWorkTicket(page, filters, queryType, orderBy);
    }

    public List<PlatformWorkTicket> getWorkTicket(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, PlatformWorkTicketDao.QueryType queryType, String orderBy) {
        return dao.getWorkTicket(page, filters, queryType, orderBy);
    }

    public List<Map<String, Object>> findTally(String query) {
        return dao.findTally(query);
    }

    public List<Map<String, Object>> findForklift(String query) {
        return dao.findForklift(query);
    }

    public List<Map<String, Object>> findZxd(String query) {
        return dao.findZxd(query);
    }

    public List<Map<String, Object>> findZxg(String zxd) {
        return dao.findZxg(zxd);
    }

    public void updateWorkTicket(PlatformWorkTicket platformWorkTicket) {

        dao.updateWorkTicket(platformWorkTicket);

    }

    public BigDecimal getEnterWeight(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, PlatformWorkTicketDao.QueryType queryType, String orderBy) {
        return dao.getEnterWeight(page, filters, queryType, orderBy);
    }

    public BigDecimal getOutWeight(Page<PlatformWorkTicket> page, List<PropertyFilter> filters, PlatformWorkTicketDao.QueryType queryType, String orderBy) {
        return dao.getOutWeight(page, filters, queryType, orderBy);
    }

    public Page<PlatformWorkTicket> workTicketPage(Page<PlatformWorkTicket> page, Map<String, Object> params) {
        return dao.workTicketPage(page, params);
    }

    public List<Map<String,String>> workTicketOfExport( Map<String, Object> params) {
        return dao.workTicketOfExport(params);
    }

}
