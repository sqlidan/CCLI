package com.haiersoft.ccli.wms.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.LoadingOrderInfoDao;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;
 
@Service
public class LoadingOrderInfoService extends BaseService<BisLoadingOrderInfo, String> {
	@Autowired
	private LoadingOrderInfoDao loadingOrderInfoDao;
	
	@Override
	public HibernateDao<BisLoadingOrderInfo, String> getEntityDao() {
		return loadingOrderInfoDao;
	}
	
	/**
	 * 根据orderId删除对应所有出库订单明细
	 * @param asnId
	 * @return
	 */
	public void deleteOrderInfos(String orderId){
		loadingOrderInfoDao.deleteOrderInfos(orderId);
	}
	/**
	 * 根据订单号获取订单明细信息
	 * @param orderId 出库订单号
	 * @return
	 */
	public List<Map<String,Object>> findInfoList(String orderId){
		return loadingOrderInfoDao.findInfoList(orderId);
	}
	/**
	 * 根据出库订单id，提单号，厢号，SKU，入库类型获取明细对象
	 * @param orderNum //出库订单id
	 * @param billNum  //提单号
	 * @param cnNum  //厢号
	 * @param skuNum //SKU
	 * @param isLab //入库类型
	 * @return BisLoadingOrderInfo
	 */
	public BisLoadingOrderInfo findOrderInfoObj(String orderNum,String billNum,String cnNum,String skuNum,String isLab,String asn){
		return loadingOrderInfoDao.findOrderInfoObj(orderNum, billNum, cnNum, skuNum, isLab,asn);
	}
	/**
	 * 根据出库订单id，提单号，厢号，SKU，入库类型删除明细对象
	 * @param orderNum //出库订单id
	 * @param billNum  //提单号
	 * @param cnNum  //厢号
	 * @param skuNum //SKU
	 * @param isLab //入库类型
	 * @return void
	 */
	public void delOrderInfoObj(String orderNum,String billNum,String cnNum,String skuNum,String isLab){
		 loadingOrderInfoDao.delOrderInfoObj(orderNum, billNum, cnNum, skuNum, isLab);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 根据 出库联系单  获得最大出库时间   的对象数据
	 * @date 2016年4月16日 上午10:48:30 
	 * @param linkId
	 * @return
	 * @throws
	 */
	public BisLoadingOrderInfo getOrderInfoByLinkIdForMaxDate(String linkId) {
		return loadingOrderInfoDao.getOrderInfoByLinkIdForMaxDate(linkId);
	}
	
	public List<BisLoadingOrderInfo> getInfoList(String orderNum){
		return loadingOrderInfoDao.findBy("loadingPlanNum", orderNum);
	}
}
