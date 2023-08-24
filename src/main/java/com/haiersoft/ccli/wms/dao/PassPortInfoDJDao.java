package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfoDJ;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfoDJ;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Repository
public class PassPortInfoDJDao extends HibernateDao<BisPassPortInfoDJ, Integer> {

}
