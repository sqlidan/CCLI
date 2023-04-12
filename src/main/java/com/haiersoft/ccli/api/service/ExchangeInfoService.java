package com.haiersoft.ccli.api.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.api.dao.ExchangeInfoDao;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.entity.ExchangeInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class ExchangeInfoService extends BaseService<ExchangeInfo, String> {

	@Autowired
	private ExchangeInfoDao exchangeInfoDao;

	@Override
	public HibernateDao<ExchangeInfo, String> getEntityDao() {
		return exchangeInfoDao;
	}
	
	/**
	 * 查询质押状态
	 * @param trayInfoId
	 * @return
	 */
	public List<ApiPledge> getPledgeInfo(String trayInfoId, String trendId) {
		int trayInfo = 0;
		if(StringUtils.isNotEmpty(trayInfoId)) {
			if(StringUtils.isNumeric(trayInfoId)) {
				trayInfo = Integer.parseInt(trayInfoId);
			}
		}
		return exchangeInfoDao.getPledgeInfo(trayInfo,trendId);
	}
	
	/**
	 * 查询所有换货记录
	 */
//	public List<Map<String,Object>> getExchangeList(){
//		return exchangeInfoDao.getExchangeList();
//	}
	
	/**
	 * 修改换货状态
	 * @param status
	 * @param id
	 * @return
	 */
//	public Integer updStatus(Integer status, String id) {
//		return exchangeInfoDao.updStatus(status, id);
//	}
}
