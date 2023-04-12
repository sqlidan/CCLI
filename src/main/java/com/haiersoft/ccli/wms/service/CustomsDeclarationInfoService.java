package com.haiersoft.ccli.wms.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.CustomsDeclarationInfoDao;
import com.haiersoft.ccli.wms.entity.BisCustomsDeclarationInfo;
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
public class CustomsDeclarationInfoService extends BaseService<BisCustomsDeclarationInfo, Integer> {
	
	//@Autowired
	//private CustomsDeclarationDao customsDeclarationDao;
	@Autowired
	private CustomsDeclarationInfoDao customsDeclarationInfoDao;
	//@Autowired
	//private FeeCodeDao feeCodeDao;
	
	@Override
    public HibernateDao<BisCustomsDeclarationInfo, Integer> getEntityDao() {
	    return customsDeclarationInfoDao;
    }

	//根据入库报关单ID获得明细列表
	public List<BisCustomsDeclarationInfo> getByCusId(String id) {
		return customsDeclarationInfoDao.findBy("cusId", id);
		
	}
	
}
