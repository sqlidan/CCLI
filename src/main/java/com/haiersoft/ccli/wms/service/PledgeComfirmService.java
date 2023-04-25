package com.haiersoft.ccli.wms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.api.dao.ApiPledgeDao;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.service.ApiPledgeService;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
 

@Service
public class PledgeComfirmService extends BaseService<ApiPledge, String> {
	@Autowired
	private ApiPledgeDao apiPledgeDao;
	@Autowired
	private TrayInfoDao trayInfoDao;
	
	public Page<ApiPledge> seachStaticApiPledgeSql(Page<ApiPledge> page,Map<String, Object> params){
		return apiPledgeDao.seachStaticApiPledgeSql(page, params);
	}
	
	
	public Page<ApiPledge> seachActiveApiPledgeSql(Page<ApiPledge> page,Map<String, Object> params){
		return apiPledgeDao.seachActiveApiPledgeSql(page, params);
	}

	@Override
	public HibernateDao<ApiPledge, String> getEntityDao() {
		return apiPledgeDao;
	}
	/*
	 * 静态质押分组查询
	 * @author:wangxiang
	 * @param params
	 */
	public List<Map<String, Object>> getStaticApiPledge(String status,String state,String accountId){
		return apiPledgeDao.getStaticApiPledge(status,state,accountId);
	}
	
	
	
	/*
	 * 动态质押分组查询
	 * @author:wangxiang
	 * @param params
	 */
	public List<Map<String, Object>> getActiveApiPledge(String status,String state,String accountId){
		return apiPledgeDao.getActiveApiPledge(status,state,accountId);
	}

	

	
	
	/*
	 * 动态质押分组查询
	 * @author:wangxiang
	 * @param params
	 */
//	public List<ApiPledge> staticPledgeCheck(Integer trayInfoId){
//		return apiPledgeDao.staticPledgeCheck(trayInfoId);
//	}
}
