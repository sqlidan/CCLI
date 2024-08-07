package com.haiersoft.ccli.bounded.dao;

import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.bounded.entity.BaseBoundedToNonBounded;
import com.haiersoft.ccli.common.persistence.HibernateDao;
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
public class BaseBoundedToNonBoundedDao extends HibernateDao<BaseBoundedToNonBounded, String>{
	private static final Logger logger = LoggerFactory.getLogger(BaseBoundedToNonBoundedDao.class);

	private static String like(String str) {
        return "%" + str + "%";
    }

}
