package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.base.entity.*;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.platform.dao.BaseTrayRoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseTrayRoomService extends BaseService<BaseTrayRoom, Integer> {
    @Autowired
    private BaseTrayRoomDao baseTrayRoomDao;
    @Override
    public HibernateDao<BaseTrayRoom, Integer> getEntityDao() {
        return this.baseTrayRoomDao;
    }

}
