package com.haiersoft.ccli.wms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.wms.dao.OutStockInfoDao;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;

/**
 * 
 * @author pyl
 * @ClassName: OutStockInfoService
 * @Description: 出库联系单明细Service
 * @date 2016年3月11日 下午4:19:48
 */
@Service
@Transactional(readOnly = true)
public class OutStockInfoService extends BaseService<BisOutStockInfo, Integer> {
	
	@Autowired
	private OutStockInfoDao outStockInfoDao;
	
	@Override
    public HibernateDao<BisOutStockInfo, Integer> getEntityDao() {
	    return outStockInfoDao;
    }
	/**
	 * 
	 * @author jxk
	 * @Description: 更新出库明细表
	 * @date 2018年11月14日 下午10.13
	 * @param outStockInfo
	 * @return Integer
	 * @throws
	 */
	public Integer updateByLinkId(BisOutStockInfo outStockInfo){
		if (outStockInfo.getGrossSingle() != null) {
            outStockInfo.setGrossWeight(outStockInfo.getOutNum()*outStockInfo.getGrossSingle());
        } else {
            outStockInfo.setGrossSingle(0.00);
            outStockInfo.setGrossWeight(0.00);
        }
		if (outStockInfo.getNetSingle() != null) {
            outStockInfo.setNetWeight(outStockInfo.getOutNum()*outStockInfo.getNetSingle());
        } else {
            outStockInfo.setNetSingle(0.00);
            outStockInfo.setNetWeight(0.00);
        }
		return outStockInfoDao.updateByLinkId(outStockInfo);
	}
	/**
	 * 
	 * @author jxk
	 * @Description: 获得出库单明细实体
	 * @date 2018年11月14日 下午9:34 
	 * @param outStockInfo
	 * @return
	 * @throws
	 */
	public BisOutStockInfo getByLinkId(Integer id,String outLinkId){
		return outStockInfoDao.getByLinkId(id, outLinkId);
	}
	
//	//修改SKU信息时，修改相对应的入库联系单明细中的SKU信息
//	public void updateSku(BaseSkuBaseInfo sku){
//		 String skuId = sku.getSkuId();
//		 List<BisEnterStockInfo> tt = enterStockInfoDao.findBy("sku", skuId);
//		 BisEnterStockInfo enterStockInfo = new BisEnterStockInfo();
//		 for(int i=0;i<tt.size();i++){
//			 enterStockInfo = tt.get(i);
//			 enterStockInfo.setCargoName(sku.getCargoName());
//			 enterStockInfo.setTypeSize(sku.getTypeSize());
//			 enterStockInfo.setPiece(sku.getPiece());
//			 enterStockInfo.setNetWeight(sku.getNetWeight());
//			 enterStockInfo.setGrossWeight(sku.getGrossWeight());
//			 enterStockInfo.setUnits(sku.getUnits());
//			 update(enterStockInfo);
//		 }
//	}
//	
	//根据出库联系单ID获得出库联系单明细
	public List<BisOutStockInfo> getList(String outLinkId){
		return outStockInfoDao.findBy("outLinkId", outLinkId);
	}

	public List<BisOutStockInfo> findonly(Map<String, Object> params) {
		return outStockInfoDao.findBy(params);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得提单数据
	 * @date 2016年5月4日 下午4:49:04 
	 * @param outStockInfo
	 * @return
	 * @throws
	 */
	public List<BisOutStockInfo> searchBillCodeByLinkId(BisOutStockInfo outStockInfo){
		return outStockInfoDao.searchBillCodeByLinkId(outStockInfo);
	}

	/**
	 * 
	 * @author PYL
	 * @Description: 出库联系单明细导出
	 * @date 2016年6月20日 下午1:50:18 
	 * @param outLinkId
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchInfo(String outLinkId) {
		return outStockInfoDao.searchInfo(outLinkId);
	}
	/**
	 * 
	 * @author PH
	 * @Description: 判断托盘号是否重复
	 * @date 2016年6月20日 下午1:50:18 
	 * @param outLinkId
	 * @return
	 * @throws
	 */
	public Boolean ifConfirm(String getTrayId) {
		return outStockInfoDao.ifConfirm(getTrayId);
	}
//	/**
//	 * 
//	 * @author PYL
//	 * @Description: 出库联系单明细导出(带船号项目号)
//	 * @date 2016年6月20日 下午1:50:18 
//	 * @param outLinkId
//	 * @return
//	 * @throws
//	 */
//	public List<Map<String, Object>> searchInfoWith(String outLinkId) {
//		return outStockInfoDao.searchInfoWith(outLinkId);
//	}

	public List<BisOutStockInfo> findByF(List<PropertyFilter> filters) {
		return outStockInfoDao.find(filters);
	}
	
	
	public List<Map<String, Object>> countsByClasstype(String outLinkId){
		return outStockInfoDao.countsByClasstype(outLinkId);
	}
}
