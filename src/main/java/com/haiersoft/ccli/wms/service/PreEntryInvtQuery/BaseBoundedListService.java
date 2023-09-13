package com.haiersoft.ccli.wms.service.PreEntryInvtQuery;


import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.bounded.dao.BaseBoundedDao;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.wms.dao.BaseBoundedListDao;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BaseBoundedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *
 * @author
 */
@Service
@Transactional(readOnly = true)
public class BaseBoundedListService extends BaseService<BaseBoundedList, String> {

    @Autowired
    private BaseBoundedListDao baseBoundedListDao  ;

    @Override
    public HibernateDao<BaseBoundedList, String> getEntityDao() {
        return baseBoundedListDao;
    }

}
