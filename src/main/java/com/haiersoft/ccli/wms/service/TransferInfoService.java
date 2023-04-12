package com.haiersoft.ccli.wms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.TransferInfoDao;
import com.haiersoft.ccli.wms.entity.BisTransferStockInfo;

/**
 * @ClassName: TransferInfoService
 * @Description: 货转明细Service
 * @date 2016年3月4日 下午4:29:46
 */
@Service
public class TransferInfoService extends BaseService<BisTransferStockInfo, String> {

	@Autowired
	private TransferInfoDao transferInfoDao;
	
	@Override
    public HibernateDao<BisTransferStockInfo, String> getEntityDao() {
	    return transferInfoDao;
    }
	
	/**
	 * 根据货转单号删除对应所有货转单明细
	 * @param asnId
	 * @return
	 */
	public void deleteTransferStockInfo(String transferId){
		transferInfoDao.deleteTransferStockInfo(transferId);
	}
	/**
	 * 根据货转单id，提单号，厢号，SKU，入库类型删除明细对象
	 * @param transferId //货转单id
	 * @param billNum  //提单号
	 * @param cnNum  //厢号
	 * @param skuNum //SKU
	 * @param isLab //入库类型
	 * @return void
	 */ 
	public void delTransferStockInfo(String transferId,String billNum,String cnNum,String skuNum,String isLab){
		transferInfoDao.delTransferStockInfo(transferId, billNum, cnNum, skuNum, isLab);
	}
	
	/**
	 * 根据货转单id，提单号，厢号，SKU，入库类型获取明细对象
	 * @param transferId //出库订单id
	 * @param billNum  //提单号
	 * @param cnNum  //厢号
	 * @param skuNum //SKU
	 * @param isLab //入库类型
	 * @return BisLoadingOrderInfo
	 */
	public BisTransferStockInfo findTransferInfoObj(String transferId,String billNum,String cnNum,String skuNum,String isLab){
		return transferInfoDao.findTransferInfoObj(transferId, billNum, cnNum, skuNum, isLab);
	}
	
	/**
	 * 根据货转单号获取货转明细信息
	 * @param transferId
	 * @return
	 */
	public List<Map<String,Object>> findInfoList(String transferId){
		return transferInfoDao.findInfoList(transferId);
	}

	//根据货转联系单获取明细表
	public List<BisTransferStockInfo> findList(String transferId) {
		return transferInfoDao.findBy("transferId", transferId);
	}

	public List<BisTransferStockInfo> findonly(Map<String, Object> params3) {
		return transferInfoDao.findBy(params3);
	}

	public List<BisTransferStockInfo> findByF(List<PropertyFilter> filters) {
		return transferInfoDao.find(filters);
	}
}
