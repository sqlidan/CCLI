package com.haiersoft.ccli.wms.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.EnterStockInfoDao;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

/**
 * 
 * @author pyl
 * @ClassName: linkId
 * @Description: 入库联系单明细Service
 * @date 2016年2月25日 下午4:55:48
 */
@Service
@Transactional(readOnly = true)
public class EnterStockInfoService extends BaseService<BisEnterStockInfo, Integer> {
	
	@Autowired
	private EnterStockInfoDao enterStockInfoDao;
	
	@Override
    public HibernateDao<BisEnterStockInfo, Integer> getEntityDao() {
	    return enterStockInfoDao;
    }
	
	public List<BisEnterStockInfo> getEnterInfo(String linkId,String billNum,String ctnNum){
		return enterStockInfoDao.getEnterInfo(linkId, billNum, ctnNum);
	}
	
	//修改SKU信息时，修改相对应的入库联系单明细中的SKU信息
	public void updateSku(BaseSkuBaseInfo sku){
		 String skuId = sku.getSkuId();
		 List<BisEnterStockInfo> tt = enterStockInfoDao.findBy("sku", skuId);
		 BisEnterStockInfo enterStockInfo = new BisEnterStockInfo();
		 for(int i=0;i<tt.size();i++){
			 enterStockInfo = tt.get(i);
			 enterStockInfo.setCargoName(sku.getCargoName());
			 enterStockInfo.setTypeSize(sku.getTypeSize());
			 enterStockInfo.setPiece(sku.getPiece());
			 enterStockInfo.setNetWeight(sku.getNetWeight());
			 enterStockInfo.setGrossWeight(sku.getGrossWeight());
			 enterStockInfo.setUnits(sku.getUnits());
			 update(enterStockInfo);
		 }
	}
	
	//根据入库联系单ID获得入库联系单明细
	public List<BisEnterStockInfo> getList(String linkId){
		return enterStockInfoDao.findBy("linkId", linkId);
	}

	//根据多个条件获得入库联系单明细
	public List<BisEnterStockInfo> getListByMap(Map<String,Object> params){
		return enterStockInfoDao.findBy(params);
	}
	
	//批量删除明细
	public void deleteInfo(List<Integer> id) {
		enterStockInfoDao.deleteInfo(id);
	}

	public void updateBillNum(String linkId, String itemNum) {
		enterStockInfoDao.updateBillNum(linkId,itemNum);
	}

	public List<Map<String, Object>> getMrList(String linkId) {
		return enterStockInfoDao.getMrList(linkId);
	}

	/**
	 * 
	 * @author PYL
	 * @Description: 入库联系单明细导出
	 * @date 2016年6月21日 下午2:50:18 
	 * @param linkId
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchInfo(String linkId) {
		return enterStockInfoDao.searchInfo(linkId);
	}

	public List<Map<String, Object>> getctNum(String linkId) {
		return enterStockInfoDao.getctNum(linkId);
	}

}
