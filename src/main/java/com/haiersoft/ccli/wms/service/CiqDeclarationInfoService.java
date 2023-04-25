package com.haiersoft.ccli.wms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.CiqDeclarationInfoDao;
import com.haiersoft.ccli.wms.entity.BisCiqDeclarationInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

/**
 * 
 * @author pyl
 * @ClassName: CustomsDeclarationInfoService
 * @Description: 入库报关单明细Service
 * @date 2016年4月16日 下午3:55:48
 */
@Service
@Transactional(readOnly = true)
public class CiqDeclarationInfoService extends BaseService<BisCiqDeclarationInfo, Integer> {
	
	//@Autowired
	//private CiqDeclarationDao ciqDeclarationDao;
	@Autowired
	private CiqDeclarationInfoDao ciqDeclarationInfoDao;
	
	@Override
    public HibernateDao<BisCiqDeclarationInfo, Integer> getEntityDao() {
	    return ciqDeclarationInfoDao;
    }

	//根据入库报检单ID获得明细列表
	public List<BisCiqDeclarationInfo> getByCiqId(String id) {
		return ciqDeclarationInfoDao.findBy("ciqId", id);
		
	}
	
}
