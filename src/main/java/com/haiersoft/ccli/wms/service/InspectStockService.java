package com.haiersoft.ccli.wms.service;
import java.util.ArrayList;
import java.util.List;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.wms.dao.InspectStockDAO;
import com.haiersoft.ccli.wms.entity.InspectStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InspectStockService extends BaseService<InspectStock, String> {

    @Autowired
    private InspectStockDAO inspectStockDAO;

    @Override
    public HibernateDao<InspectStock, String> getEntityDao() {
        return inspectStockDAO;
    }

    public Page<InspectStock> findInspectPlans(Page<InspectStock> page, InspectStock params) {
        return inspectStockDAO.findInspectPlans(page, params);
    }
    
    public Page<InspectStock> findInspectPlansInfo(Page<InspectStock> page, InspectStock params) {
        return inspectStockDAO.findInspectPlansInfo(page, params);
    }
    
    public String auditInspectPlans(User user, String inspectId) {
        return inspectStockDAO.auditInspectPlans(user, inspectId);
    }
    
    public List<InspectStock> findInspectStocksByMasterId(String inspectMasterId){
    	List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    	PropertyFilter filter = new PropertyFilter("EQS_inspectMasterId", inspectMasterId);
    	filters.add(filter);
    	return inspectStockDAO.find(filters);
    }
}