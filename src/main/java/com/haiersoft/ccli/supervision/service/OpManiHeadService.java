package com.haiersoft.ccli.supervision.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.supervision.dao.ManiHeadDao;
import com.haiersoft.ccli.supervision.dao.OpManiHeadDao;
import com.haiersoft.ccli.supervision.entity.ManiHead;
import com.haiersoft.ccli.supervision.entity.OpManiHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpManiHeadService extends BaseService<OpManiHead, String>{
	
	@Autowired
	OpManiHeadDao opManiHeadDao;

	@Override
	public HibernateDao<OpManiHead, String> getEntityDao() {
		// TODO Auto-generated method stub
		return opManiHeadDao;
	}

	public void updateLocalStatusbyId(String status, String id) {
		opManiHeadDao.updateLocalStatusbyId(status,id);
		
	}

	//根据id更新记录中的maniFestId，同时更新localstauts状态
	public void updateManiFestIdById(String maniFestId, String localStatus,String id) {
		opManiHeadDao.updateManiFestIdbyId(maniFestId, localStatus,id);
		
	}
	
	
	public List<String> findAllManifestID() {
		return opManiHeadDao.findAllManifestID();
	}

	public void updateByManiId(ManiHead maniHead) {
		opManiHeadDao.updateByManiId(maniHead);
		
	}
	
	//查询所有未做核放单入库到货确认的ManiHead
	public List<OpManiHead> findMainsToConfirm() {
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();			
		filters.add(new PropertyFilter("EQS_ManiConfirmStatus", "N"));
		filters.add(new PropertyFilter("EQS_IeFlag", "I"));
		filters.add(new PropertyFilter("EQS_EmptyFlag", "N"));
		filters.add(new PropertyFilter("NEQS_PassStatus", "2"));		
		List<OpManiHead> headList = super.search(filters);
		return headList;
	}

	public List<String> findMainIdsToConfirmGoods() {
		
		return opManiHeadDao.findMainIdsToConfirmGoods();
	}

	public List<String> findMainIdsToUpdate() {
		// TODO Auto-generated method stub
		return opManiHeadDao.findMainIdsToUpdate();
	}

}
