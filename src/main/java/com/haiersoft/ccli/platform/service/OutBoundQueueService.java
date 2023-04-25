package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.platform.dao.OutBoundQueueDao;
import com.haiersoft.ccli.platform.entity.OutBoundQueueVO;
import com.haiersoft.ccli.wms.dao.CountTempleteDao;
import com.haiersoft.ccli.wms.entity.CountTemplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 */
@Service
public class OutBoundQueueService {

    @Autowired
    private OutBoundQueueDao outBoundQueueDao;

    public Page<OutBoundQueueVO> searchOutBondQueue(Page<OutBoundQueueVO> page, OutBoundQueueVO outBoundQueueVO) {
        return outBoundQueueDao.searchOutBondQueue(page, outBoundQueueVO);
    }



}
