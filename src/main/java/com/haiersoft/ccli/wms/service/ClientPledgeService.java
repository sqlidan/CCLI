package com.haiersoft.ccli.wms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.wms.dao.ClientPledgeDao;
import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
 

@Service
public class ClientPledgeService extends BaseService<BaseClientPledge, Integer> {
	@Autowired
	private ClientPledgeDao clientPledgeDao;
	@Override
	public HibernateDao<BaseClientPledge, Integer> getEntityDao() {
		return clientPledgeDao;
	}
	
	/*
	 * @description 根据对应条件（SKU+客户 /SKU+客户+提单号+箱号）拿到质押表中质押的件数
	 * 
	 * 
	 */
	public List<BaseClientPledge> findBySku(Map<String,Object> params){
		List<BaseClientPledge> pledgeList = clientPledgeDao.findBy(params);
		return pledgeList;
	}
	
	/*
	 * 生成质押单号
	 */
	public String getCodeNum(){
		int codeN = clientPledgeDao.getSequenceId("SEQUENCE_CLIENT_PLEDGE");
		String dateS = StringUtils.dateToString();
		String codeNum = String.valueOf(codeN) + dateS ;
		return codeNum;
	}
	

	/*
	 * 静态质押分组查询
	 * @author:PYL
	 * @param params
	 */
	public List<Map<String,Object>> getTray(String clientName,String sku,String billNum,String ctnNum, String warehouseId){
		return clientPledgeDao.getTray(clientName,sku,billNum,ctnNum,warehouseId);
	}

	public List<BaseClientPledge> findByF(List<PropertyFilter> filters2) {
		return clientPledgeDao.find(filters2);
	}
}
