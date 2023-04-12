package com.haiersoft.ccli.supervision.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.supervision.dao.ApprHeadDao;
import com.haiersoft.ccli.supervision.dao.OutApprHeadDao;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.OutApprHead;

@Service
public class OutApprHeadService extends BaseService<OutApprHead, String>{
	
	@Autowired
	OutApprHeadDao outApprHeadDao;

	@Override
	public HibernateDao<OutApprHead, String> getEntityDao() {
		// TODO Auto-generated method stub
		return outApprHeadDao;
	}

}
