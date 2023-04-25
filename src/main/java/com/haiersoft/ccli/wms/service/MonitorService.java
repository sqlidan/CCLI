package com.haiersoft.ccli.wms.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.MonitorDao;
import com.haiersoft.ccli.wms.entity.Monitor;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
/**
 * 
 * @author pyl
 * @ClassName: MonitorService
 * @Description: 入库联系单Service
 * @date 2016年2月24日 下午3:52:37
 */
@Service
@Transactional(readOnly = true)
public class MonitorService extends BaseService<Monitor, String> {
	
	@Autowired
	private MonitorDao monitorDao;
	
	@Override
	public HibernateDao<Monitor, String> getEntityDao() {
		return monitorDao;
	}
	
	
	public Page<Monitor> searchMonitor(Page<Monitor> page, Monitor monitor){
		return monitorDao.searchMonitor(page, monitor);
	}
	
	public Page<Monitor> searchOutMonitor(Page<Monitor> page, Monitor monitor){
		return monitorDao.searchOutMonitor(page, monitor);
	}
}
