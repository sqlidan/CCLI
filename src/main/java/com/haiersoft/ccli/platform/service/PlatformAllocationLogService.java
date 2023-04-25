package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.platform.entity.PlatformAllocationLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author A0047794
 * @日期 2021/12/9
 * @描述
 */
@Service
@Transactional(readOnly = true)
public class PlatformAllocationLogService extends BaseService<PlatformAllocationLog,Integer> {
    @Override
    public HibernateDao<PlatformAllocationLog, Integer> getEntityDao() {
        return null;
    }
}
