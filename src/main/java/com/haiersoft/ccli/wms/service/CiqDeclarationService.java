package com.haiersoft.ccli.wms.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.CiqDeclarationDao;
import com.haiersoft.ccli.wms.entity.BisCiqDeclaration;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;

/**
 * 
 * @author pyl
 * @ClassName: CustomsDeclarationService
 * @Description: 入库报关单Service
 * @date 2016年4月16日 下午3:54:37
 */
@Service
@Transactional(readOnly = true)
public class CiqDeclarationService extends BaseService<BisCiqDeclaration, String> {
	
	@Autowired
	private CiqDeclarationDao ciqDeclarationDao;
	//@Autowired
	//private CiqDeclarationInfoDao ciqDeclarationInfoDao;
	
	@Override
	public HibernateDao<BisCiqDeclaration, String> getEntityDao() {
		return ciqDeclarationDao;
	}
	/**
	 * 
	 * @author pyl
	 * @Description: 获得报检单ID
	 * @date 2016年3月5日 下午12:08:24
	 * @return
	 * @throws
	 */
	public String getNewNumber() {
 		int num = ciqDeclarationDao.getSequenceId("SEQ_CIQ");
 		String number = StringUtils.lpadInt(6, num);
		return number;
	}
	public List<BisCiqDeclaration> findList(Map<String, Object> params) {
		return ciqDeclarationDao.findBy(params);
	}
}
