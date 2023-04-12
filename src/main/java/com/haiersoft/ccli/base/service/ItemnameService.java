package com.haiersoft.ccli.base.service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.HscodeDao;
import com.haiersoft.ccli.base.dao.ItemnameDao;
import com.haiersoft.ccli.base.entity.BaseItemname;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class ItemnameService extends BaseService<BaseItemname, Integer> {
	@Autowired
	private ItemnameDao itemnameDao;
	@Override
	public HibernateDao<BaseItemname, Integer> getEntityDao() {
		return itemnameDao;
	}
	public List<Object[]> getHsItemname(String hs) {
		// TODO Auto-generated method stub
		List<Object[]> reString=itemnameDao.getHsItemname(hs);
		return reString;
	}



}
