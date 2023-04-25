package com.haiersoft.ccli.base.service;

import java.util.Date;

import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.dao.SkuInfoDao;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
 

@Service
public class SkuInfoService extends BaseService<BaseSkuBaseInfo, String> {

	@Autowired
	private SkuInfoDao skuInfoDao;
	@Override
	public HibernateDao<BaseSkuBaseInfo, String> getEntityDao() {
		return skuInfoDao;
	}

	/**
	 * 生成SKUID：S+货物类型(三位，自动补齐)+ YYMMDDHHMMSS+SEQ(2位循环) 
	 * @param gClass 货物类型
	 * @return
	 */
	public String  getSKUId(int gClass) {
		StringBuffer retSKU=new StringBuffer("S");
		retSKU.append(StringUtils.lpadInt(3,gClass )).append(DateUtils.getDateStr(new Date(),"YYMMddHHmmss"));
		int getNum=skuInfoDao.getSequenceId("SEQ_SKU");
		retSKU.append(StringUtils.lpadInt(2,getNum ));
		return retSKU.toString();
	}
	
	/**
	 * 按唯字段SKU_ID查询对象信息
	 */
	public BaseSkuBaseInfo getSKUInfo(String skuId) {
		BaseSkuBaseInfo getObj=null;
		if(!"".equals(skuId) && !"".equals(skuId)){
			getObj=skuInfoDao.findUniqueBy("skuId", skuId);
		}
		return getObj;
	}

	public String updateSKUInfo(BisEnterStockInfo bisEnterStockInfo){
		return skuInfoDao.updateSKUInfo(bisEnterStockInfo);
	}

}
