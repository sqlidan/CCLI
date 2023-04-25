package com.haiersoft.ccli.cost.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.InspectionFeeInfoDao;
import com.haiersoft.ccli.cost.entity.BisInspectionFeeInfo;
 
@Service
public class InspectionFeeInfoService extends BaseService<BisInspectionFeeInfo, Integer> {
	@Autowired
	private InspectionFeeInfoDao inspectionFeeInfoDao;
	//@Autowired
	//private ClientDao clientDao;
	//@Autowired
	//private AsnActionDao asnActionDao;
	//@Autowired
	//private ASNInfoService asnInfoService;//ans明细
	//@Autowired
	//private SkuInfoService skuInfoService;//sku
	//@Autowired
	//private AsnActionService asnActionService;//ASN计费区间
	//@Autowired
	//private EnterStockService enterStockService;//入库联系单
	//@Autowired
	//private ClientService clientService;//客户
	
	@Override
	public HibernateDao<BisInspectionFeeInfo, Integer> getEntityDao() {
		return inspectionFeeInfoDao;
	}

	//更新明细的提单号
	public void updateBillNum(String feeId, String billNum) {
		inspectionFeeInfoDao.updateBillNum(feeId,billNum);
	}

	public List<BisInspectionFeeInfo> findByFeeId(String feeId) {
		return inspectionFeeInfoDao.findBy("feeId", feeId);
	}
	
}
