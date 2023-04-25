package com.haiersoft.ccli.base.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.base.dao.ExpenseSchemeDao;
import com.haiersoft.ccli.base.dao.ExpenseSchemeInfoDao;
import com.haiersoft.ccli.base.entity.ExpenseScheme;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseSchemeService
 * @Description: 费用方案Service
 * @date 2016年2月24日 下午5:23:59
 */
@Service
@Transactional(readOnly = true)
public class ExpenseSchemeService extends BaseService<ExpenseScheme, String> {
	
	@Autowired
	private ExpenseSchemeDao expenseSchemeDao;
	@Autowired
	private ExpenseSchemeInfoDao expenseSchemeInfoDao;
	
	@Override
    public HibernateDao<ExpenseScheme, String> getEntityDao() {
	    return expenseSchemeDao;
    }
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得十位主键Id
	 * @date 2016年2月29日 下午1:57:16 
	 * @return
	 * @throws
	 */
	public String getSchemeIdToString() {
		int num = expenseSchemeDao.getSequenceId("SEQ_SCHEME");
		return StringUtils.lpadInt(10, num);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 复制方案
	 * @date 2016年2月29日 下午7:16:43 
	 * @param id
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public ExpenseScheme copyScheme(String id) {
		User user = UserUtil.getCurrentUser();
		
		ExpenseScheme scheme = expenseSchemeDao.find(id);
		ExpenseScheme newScheme = new ExpenseScheme();
		
		BeanUtils.copyProperties(scheme, newScheme);//复制对象属性
		String num = this.getSchemeIdToString();
		newScheme.setSchemeNum(num);
		newScheme.setProgramState("0");
		newScheme.setOperatorPerson(user.getName());
		newScheme.setOperateTime(new Date());
		expenseSchemeDao.save(newScheme);
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>(); 
		PropertyFilter filter = new PropertyFilter("EQS_schemeNum", id);
		filters.add(filter);
		
		List<ExpenseSchemeInfo> lists = expenseSchemeInfoDao.find(filters);
		for (ExpenseSchemeInfo info : lists){
			ExpenseSchemeInfo schemeInfo = new ExpenseSchemeInfo();
			BeanUtils.copyProperties(info, schemeInfo);//复制对象属性
			
			int ids = expenseSchemeInfoDao.getSequenceId("SEQ_SCHEME_INFO");
			schemeInfo.setId(StringUtils.lpadInt(8, ids));
			schemeInfo.setSchemeNum(num);
			expenseSchemeInfoDao.save(schemeInfo);
		}
		return newScheme;
	}

	public List<Map<String,Object>> getShare(String contractId,String clientId, String ifGet ) {
		return expenseSchemeDao.getShare(contractId,clientId,ifGet);
	}

}
