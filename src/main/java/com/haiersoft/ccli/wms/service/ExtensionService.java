package com.haiersoft.ccli.wms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.wms.dao.ExtensionDao;
import com.haiersoft.ccli.wms.entity.Extension;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

/**
 * 
 * @author pyl
 * @ClassName: ExtensionService
 * @Description: 展期Service
 * @date 2016年5月31日
 */
@Service
@Transactional(readOnly = true)
public class ExtensionService extends BaseService<Extension, Integer>{
	
	@Autowired
	private ExtensionDao extensionDao;
	@Override
	public HibernateDao<Extension, Integer> getEntityDao() {
		return extensionDao;
	}
	
	//获取报关报检单数据
	public List<Map<String, Object>> getList(String billNum) {
		return extensionDao.getList(billNum);
	}
	
	//获取超过弹出报警时间的数据
	public List<Map<String, Object>> getWarningList() {
		return extensionDao.getWarningList();
	}
	
}
