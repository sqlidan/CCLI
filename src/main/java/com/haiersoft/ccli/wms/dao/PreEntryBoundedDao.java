package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryBounded;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 *
 */
@Repository
public class PreEntryBoundedDao extends HibernateDao<BisPreEntryBounded, String>{

}
