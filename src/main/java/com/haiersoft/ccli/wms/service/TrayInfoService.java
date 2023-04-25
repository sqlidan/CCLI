package com.haiersoft.ccli.wms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.TrayInfo;

/**
 * 
 * @author Connor.M
 * @ClassName: TrayInfoService
 * @Description: 库存Service
 * @date 2016年3月4日 下午2:09:38
 */
@Service
public class TrayInfoService extends BaseService<TrayInfo, Integer> {

	@Autowired
	private TrayInfoDao trayInfoDao;
	
	@Override
    public HibernateDao<TrayInfo, Integer> getEntityDao() {
	    return trayInfoDao;
    }
	
	/**
	 * 按客户，提单，厢号，sKU 分组获取库存数量
	 * @param clientId //客户
	 * @param getMr 
	 * @param getSku 
	 * @param getBill 
	 * @param getRk 
	 * @return
	 */
	public List<Map<String,Object>> findClientTrayList(String outLinkId,String clientId,String ckId, String getRk, String getBill, String getSku, String getMr){
		return trayInfoDao.findClientTrayList(outLinkId,clientId,ckId,getRk,getBill,getSku,getMr);
	}
	/**
	 * 按客户，提单，厢号，sKU 分组获取库存数量(原货主和现货主一致)
	 * @param clientId //客户
	 * @param getMr 
	 * @param getSku 
	 * @param getBill 
	 * @param getRk 
	 * @return
	 */
	public List<Map<String,Object>> findClientTrayList2(String outLinkId,String clientId,String ckId, String getRk, String getBill, String getSku, String getMr){
		return trayInfoDao.findClientTrayList2(outLinkId,clientId,ckId,getRk,getBill,getSku,getMr);
	}
	
	/**
	 * 按出库联系单号出库订单明细，客户，提单，厢号，sKU 分组获取库存数量
	 * @param clientId //客户
	 * @param getRk 
	 * @param getName 
	 * @return
	 */
	public List<Map<String,Object>> findOutClientTrayList(String clientId,String ckId,String outCode,String ordCode, String getRk, String getBill, String getSku, String getMr, String getName){
		return trayInfoDao.findOutOrderClientTrayList(clientId,ckId,outCode,ordCode,getRk,getBill,getSku,getMr,getName);
	}
	
	
	/**
	 * 按客户，sKU 分组获取库存数量(成品) 入库类型货损不统计
	 * @param clientId //客户
	 * @return
	 */
	public List<Map<String,Object>> findClientSKUTrayList(String clientId,String ckId){
		return trayInfoDao.findClientSKUTrayList(clientId,ckId);
	}
	
	/*
	 *按sku+客户号/sku+客户号+箱号+提单号获得库存信息 (按多条件查询通用)
	 * 
	 */
	public List<TrayInfo> findBySku(Map<String,Object> params){
		List<TrayInfo> trayInfoList = trayInfoDao.findBy(params);
		return trayInfoList;
	}
	/**
	 * 获取托盘信息
	 * @param userid 客户id
	 * @param billNum 提单号
	 * @param ctnNum 厢号
	 * @param sku 
	 * @param state 入库类型
	 * @param sqlIf 策略sql条件
	 * @param sqlOrd  策略排序
	 * @param pickNum  拣货数量
	 * @return
	 */
	public List<Map<String, Object>> findTrayList(String ckId, String userid, String billNum, String ctnNum, String asn, String sku, String state, String sqlIf, String sqlOrd, Integer pickNum, String chooseFloor) {
		return trayInfoDao.findTrayList(ckId, userid, billNum, ctnNum, asn, sku, state, sqlIf, sqlOrd, pickNum, chooseFloor);
	}
	
	
	public List<Map<String,Object>> findTrayListForSample(String stockName,String billNum,String ctnNum,String asn,String skuId,String trayId){
		return trayInfoDao.findTrayListForSample(stockName, billNum, ctnNum, asn, skuId, trayId);
	}
	
	public List<Map<String,Object>> findTrayListForCheck(String contactNum){
		return trayInfoDao.findTrayListForCheck(contactNum);
	}
	/**
	 * 排除策略拣货获取托盘信息
	 * @param userid 客户id
	 * @param billNum 提单号
	 * @param ctnNum 厢号
	 * @param sku 
	 * @param state 入库类型
	 * @param sqlOrd  策略排序
	 * @param notIds  要排除的主键集合
	 * @return
	 */
	public List<Map<String,Object>> findNoIfTrayList(String ckId,String userid,String billNum,String ctnNum,String sku,String state,String sqlOrd,String notIds){
		return trayInfoDao.findNoIfTrayList(ckId,userid, billNum, ctnNum, sku, state, sqlOrd,notIds);
	}
	
	/**
	 * 根据id集合批量锁定库存明细
	 * @param ids 最后一位为,
	 */
	public void lockTrayList(String ids){
		 trayInfoDao.lockTrayList(ids);
	}
	
	/**
	 * 根据id集合批量锁定库存明细--货转
	 * @param ids 最后一位为,
	 * @param userId 存货方id
	 * @param userName 存放方
	 */
	public void UsrTrayList(String ids,String userId,String userName){
		trayInfoDao.UsrTrayList(ids, userId, userName);
	}
	/**
	 * 根据订单id解锁锁定库存明细
	 * @param ordCode 订单id
	 */
	public void unlockTrayList(String ordCode){
		trayInfoDao.unlockTrayList(ordCode);
	}
	
	/**
	 * @author GLZ
	 * 通过SQL语句，根据ASNid获取库存对象列表
	 * @author PYL
	 * @param ans
	 * @return
	 */
	public List<Map<String, Object>> getAsnInfoByAsn(String asn) {
		return trayInfoDao.getAsnInfoByAsn(asn);
		
	}
	
	
	/**
	 * 根据ASNid获取
	 * @param ansId
	 * @return
	 */
	public List<TrayInfo> getASNInfoList(String ansId){
		List<TrayInfo> getList=null;
		if(ansId!=null && !"".equals(ansId)){
			getList=trayInfoDao.findBy("asn", ansId);
		}
		return getList;
	}

	 //根据SKU获取库存
	 public List<Map<String, Object>> GetTrayInfoBySku(String skuid) {
		 return trayInfoDao.GetTrayInfoBySku(skuid); 
	 }

	public List<Map<String, Object>> updateAsnAction(BisLoadingInfo loading) {
		return trayInfoDao.updateAsnAction(loading); 
	}

	public List<TrayInfo> findByF(List<PropertyFilter> filters) {
		return trayInfoDao.find(filters);
	}
	
	public String updateTrayInfoforNewCtnNum(String ctnNumNew,String ctnNumOld,String linkId){
		return trayInfoDao.updateTrayInfoforNewCtnNum(ctnNumNew,ctnNumOld,linkId); 
	}

}
