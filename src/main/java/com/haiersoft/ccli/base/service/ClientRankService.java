package com.haiersoft.ccli.base.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.dao.ClientRankDao;
import com.haiersoft.ccli.base.entity.BaseClientRank;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class ClientRankService extends BaseService<BaseClientRank, String> {
	@Autowired
	private ClientRankDao clientRankDao;
	
	@Override
	public HibernateDao<BaseClientRank, String> getEntityDao() {
		return clientRankDao;
	}
	
	
		//获取rank(无视大小写)
		public List<Map<String,Object>> findRank(String param) {
			return clientRankDao.findRank(param);
		}
}
