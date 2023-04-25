package com.haiersoft.ccli.supervision.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.supervision.dao.ManiHeadDao;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.entity.ManiHead;

@Service
public class ManiHeadService extends BaseService<ManiHead, String>{
	
	@Autowired
	ManiHeadDao maniHeadDao;

	@Override
	public HibernateDao<ManiHead, String> getEntityDao() {
		// TODO Auto-generated method stub
		return maniHeadDao;
	}

	public void updateLocalStatusbyId(String status, String id) {
		maniHeadDao.updateLocalStatusbyId(status,id);
		
	}

	//根据id更新记录中的maniFestId，同时更新localstauts状态
	public void updateManiFestIdById(String maniFestId, String localStatus,String id) {
		maniHeadDao.updateManiFestIdbyId(maniFestId, localStatus,id);
		
	}
	
	
	public List<String> findAllManifestID() {
		return maniHeadDao.findAllManifestID();		
	}

	public void updateByManiId(ManiHead maniHead) {
		maniHeadDao.updateByManiId(maniHead);	
		
	}
	
	//查询所有未做核放单入库到货确认的ManiHead
	public List<ManiHead> findMainsToConfirm() {
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();			
		filters.add(new PropertyFilter("EQS_ManiConfirmStatus", "N"));
		filters.add(new PropertyFilter("EQS_IeFlag", "I"));
		filters.add(new PropertyFilter("EQS_EmptyFlag", "N"));
		filters.add(new PropertyFilter("NEQS_PassStatus", "2"));		
		List<ManiHead> headList = super.search(filters);
		return headList;
	}

	public List<String> findMainIdsToConfirmGoods() {
		
		return maniHeadDao.findMainIdsToConfirmGoods();
	}

	public List<String> findMainIdsToUpdate() {
		// TODO Auto-generated method stub
		return maniHeadDao.findMainIdsToUpdate();
	}

}
