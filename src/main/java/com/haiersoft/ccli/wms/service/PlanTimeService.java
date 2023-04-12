package com.haiersoft.ccli.wms.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PlanTimeDAO;
import com.haiersoft.ccli.wms.entity.BasePlanTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PlanTimeService extends BaseService<BasePlanTime, Integer> {

    @Autowired
    private PlanTimeDAO planTimeDAO;

    @Override
    public HibernateDao<BasePlanTime, Integer> getEntityDao() {
        return planTimeDAO;
    }

    public Page<BasePlanTime> pageTime(Page<BasePlanTime> page, BasePlanTime obj) {
        return planTimeDAO.pageTime(page, obj);
    }

    public List<Map<String, Object>> listTime() {
        return planTimeDAO.listTime();
    }

    public int getSeqId() {
        return planTimeDAO.getSequenceId("SEQ_SCHEME");
    }

}
