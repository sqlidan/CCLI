package com.haiersoft.ccli.cost.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.BackStevedoringDao;
import com.haiersoft.ccli.cost.entity.BisBackSteveDoring;

@Service
public class BackStevedoringService extends BaseService<BisBackSteveDoring, Integer> {
	@Autowired
	private BackStevedoringDao backStevedoringDao;
	//@Autowired
	//private ClientDao clientDao;
	//@Autowired
	//private SkuInfoService skuInfoService;//sku
	//@Autowired
	//private ClientService clientService;//客户
	
	@Override
	public HibernateDao<BisBackSteveDoring, Integer> getEntityDao() {
		return backStevedoringDao;
    }
	
	
	
	/*
	 * 根据装车号获取出库装车单信息
	 */
	public List<Map<String, Object>> getLoadingInfoByNum(String loadingNum) {
		return backStevedoringDao.getLoadingInfoByNum(loadingNum);
	}

	/*
	 *  根据装车号获取出装卸单信息
	 */
	public List<Map<String, Object>> getBackStevedoring(String billNum) {
		return backStevedoringDao.getBackStevedoring(billNum);
	}
	

	/*
	 *  根据ASN获取入库装卸单信息
	 */
	public List<BisBackSteveDoring> getSteveByBill(String billNum) {
		return backStevedoringDao.findBy("billNum", billNum);
	}

	
	/*
	 * 更新 是否完成 状态
	 */
	public void updateState(String billNum) {
		backStevedoringDao.updateState(billNum);
	} 
	
	/*
	 * 更新 是否完成 状态
	 */
	public void updateState2(String billNum) {
		backStevedoringDao.updateState2(billNum);
	}

//	/*
//	 * 获得sequence
//	 */
//	public Integer getSequenceId() {
//		return outStevedoringDao.getSequenceId("SEQ_STANDING_BOOK");
//		
//	}
}
