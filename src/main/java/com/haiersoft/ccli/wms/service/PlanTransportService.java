package com.haiersoft.ccli.wms.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PlanTransportDAO;
import com.haiersoft.ccli.wms.entity.BisPlanTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 网上预约模块
 */
@Service
public class PlanTransportService extends BaseService<BisPlanTransport, Integer> {

    @Autowired
    private PlanTransportDAO planTransportDAO;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public HibernateDao getEntityDao() {
        return planTransportDAO;
    }

    public Page<BisPlanTransport> pagePlans(Page<BisPlanTransport> page, BisPlanTransport params) {
        return planTransportDAO.pagePlans(page, params);
    }

    public List<Map<String, Object>> listPlanTime(String params) {
        return planTransportDAO.listPlanTime(params);
    }

}
