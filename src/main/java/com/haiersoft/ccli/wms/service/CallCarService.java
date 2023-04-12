package com.haiersoft.ccli.wms.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.CallCarDao;
import com.haiersoft.ccli.wms.entity.CallCar;
/**
 * 
 * @ClassName: CallCarService
 */
@Service
public class CallCarService extends BaseService<CallCar, String> {

	@Autowired
	private CallCarDao callCarDao;
	
	@Override
    public HibernateDao<CallCar, String> getEntityDao() {
	    return callCarDao;
    }

	public void deleteZero() {
		callCarDao.deleteZero();
		
	}
	
}
