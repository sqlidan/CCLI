package com.haiersoft.ccli.base.service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.HscodeDao;
import com.haiersoft.ccli.base.entity.BaseHscode;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class HscodeService extends BaseService<BaseHscode, Integer> {
	@Autowired
	private HscodeDao hscodeDao;
	@Override
	public HibernateDao<BaseHscode, Integer> getEntityDao() {
		return hscodeDao;
	}
	public List<Object[]> getHsData(String hs) {
		// TODO Auto-generated method stub
		List<Object[]> reString=hscodeDao.getHsData(hs);
		return reString;
	}


}
