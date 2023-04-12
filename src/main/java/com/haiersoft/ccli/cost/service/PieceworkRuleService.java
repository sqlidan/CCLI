package com.haiersoft.ccli.cost.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.PieceworkRuleDao;
import com.haiersoft.ccli.cost.dao.WorkReportDao;
import com.haiersoft.ccli.cost.entity.BasePieceworkRule;
import com.haiersoft.ccli.cost.entity.WorkReport;

/**
 * 
 * @author pyl
 * @ClassName: PieceworkRuleService
 * @Description: 作业计件Service
 */
@Service
@Transactional(readOnly = true)
public class PieceworkRuleService extends BaseService<BasePieceworkRule, Integer> {
	
	@Autowired
	private PieceworkRuleDao pieceworkRuleDao;
	@Autowired
	private WorkReportDao workReportDao;
	
	@Override
	public HibernateDao<BasePieceworkRule, Integer> getEntityDao() {
		return pieceworkRuleDao;
	}

	public Page<WorkReport> searchWorkReport(Page<WorkReport> page,WorkReport workReport) {
		return workReportDao.searchWorkReport(page,workReport);
	}

	public List<BasePieceworkRule> findby(String propertyName, String value) {
		return pieceworkRuleDao.findBy(propertyName, value);
	}
	
	
}
