package com.haiersoft.ccli.wms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.ASNInfoDao;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
 
@Service
public class ASNInfoService extends BaseService<BisAsnInfo, String> {
	@Autowired
	private ASNInfoDao asnInfoDao;

	@Override
	public HibernateDao<BisAsnInfo, String> getEntityDao() {
		return asnInfoDao;
	}
	 
	/**
	 * 根据ansId删除ASN所有明细
	 * @param asnId
	 * @return
	 */
	public void deleteASNInfos(String asnId){
		 asnInfoDao.deleteASNInfos(asnId);
	} 
	/**
	 * 根据asnid和skuid获取唯一对象
	 * @param ansId
	 * @param skuId
	 * @return
	 */
	public BisAsnInfo findBisAsnInfo(String ansId,String skuId){
		BisAsnInfo retObj=null;
		if(ansId!=null && !"".equals(ansId) && skuId!=null && !"".equals(skuId)){
			String hql="from BisAsnInfo where asnId=:asnId and skuId=:skuId";
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("asnId", ansId);
			parme.put("skuId", skuId);
			retObj=asnInfoDao.findUnique(hql,parme);
		}
	    return retObj;
	}
	/**
	 * 根据ASNid获取明细对象列表
	 * @param ansId
	 * @return
	 */
	public List<BisAsnInfo> getASNInfoList(String ansId){
		List<BisAsnInfo> getList=null;
		if(ansId!=null && !"".equals(ansId)){
			getList=asnInfoDao.findBy("asnId", ansId);
		}
		return getList;
	}
	
	/**
	 * 根据SKU获取明细对象列表,gzq 20160623
	 * @param ansId
	 * @return
	 */
	public List<BisAsnInfo> getASNInfoListBySKU(String skuId){
		List<BisAsnInfo> getList=null;
		if(skuId!=null && !"".equals(skuId)){
			getList=asnInfoDao.findBy("skuId", skuId);
		}
		return getList;
	}

	/**
	 * 通过SQL语句，根据ASNid获取对象列表
	 * @author PYL
	 * @param ans
	 * @return
	 */
	public List<Map<String, Object>> getAsnInfoByAsn(String asn) {
		return asnInfoDao.getAsnInfoByAsn(asn);
		
	}

	public List<Map<String, Object>> getAsnReportByAsn(String asn) {
		return asnInfoDao.getAsnReportByAsn(asn);
	}

	public List<BisAsnInfo> findByF(List<PropertyFilter> filters) {
		return asnInfoDao.find(filters);
	}


	

	
	
}
