package com.haiersoft.ccli.wms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.CustomsDeclarationDao;
import com.haiersoft.ccli.wms.entity.BisCustomsDeclaration;
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
public class CustomsDeclarationService extends BaseService<BisCustomsDeclaration, String> {
	
	@Autowired
	private CustomsDeclarationDao customsDeclarationDao;
	//@Autowired
	//private CustomsDeclarationInfoDao customsDeclarationInfoDao;
	
	@Override
	public HibernateDao<BisCustomsDeclaration, String> getEntityDao() {
		return customsDeclarationDao;
	}
//	/**
//	 * 按条件查询入库联系单和货转联系单集合
//	 * @param page
//	 * @param billNum //提单id
//	 * @param transNum//装车单号
//	 * @param ctnNum //厢号
//	 * @return
//	 */
//	public  List<Map<String,Object>>  findList(Page page,String linkNum,String billNum,String transNum,String ctnNum){
//		return enterStockDao.findList(page,linkNum, billNum, transNum, ctnNum);
//	}
		
	
	/**
	 * 
	 * @author pyl
	 * @Description: 获得入库联系单ID
	 * @date 2016年3月5日 下午12:08:24
	 * @return
	 * @throws
	 */
	public String getNewNumber() {
 		int num = customsDeclarationDao.getSequenceId("SEQ_CUSTOMS");
 		String number = StringUtils.lpadInt(6, num);
		return number;
	}

 	//获得报关单集合（按param查询）
	public List<BisCustomsDeclaration> findList(Map<String, Object> params) {
		return customsDeclarationDao.findBy(params);
	}
	
	
}
