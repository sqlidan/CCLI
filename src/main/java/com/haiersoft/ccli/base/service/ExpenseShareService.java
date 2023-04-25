package com.haiersoft.ccli.base.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.ExpenseShareDao;
import com.haiersoft.ccli.base.entity.BaseExpenseShare;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class ExpenseShareService extends BaseService<BaseExpenseShare, Integer> {
	@Autowired
	private ExpenseShareDao expenseShareDao;
	@Override
	public HibernateDao<BaseExpenseShare, Integer> getEntityDao() {
		return expenseShareDao;
	}
	public List<BaseExpenseShare> getList(Map<String,Object> params) {
		return expenseShareDao.findBy(params);
	}
	
	//根据费用方案号删除
	public void delSchemeNum(String schemeNum) {
		expenseShareDao.delSchemeNum(schemeNum);
	}
	
	//费用方案审核
	public void okpass(String id) {
		expenseShareDao.okpass(id);
	}
	
	//费用方案取消审核
	public void nopass(String id) {
		expenseShareDao.nopass(id);
	}

}
