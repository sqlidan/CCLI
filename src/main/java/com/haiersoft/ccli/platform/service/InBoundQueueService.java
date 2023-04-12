package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.platform.dao.InBoundQueueDao;
import com.haiersoft.ccli.platform.dao.OutBoundQueueDao;
import com.haiersoft.ccli.platform.entity.InBoundQueueVO;
import com.haiersoft.ccli.platform.entity.OutBoundQueueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class InBoundQueueService {

    @Autowired
    private InBoundQueueDao inBoundQueueDao;

    public Page<InBoundQueueVO> searchInBondQueue(Page<InBoundQueueVO> page, InBoundQueueVO inBoundQueueVO) {
        return inBoundQueueDao.searchInBondQueue(page, inBoundQueueVO);
    }



}
