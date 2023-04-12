package com.haiersoft.ccli.api.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.api.dao.InStockInfoDao;
import com.haiersoft.ccli.api.entity.InStockInfo;
import com.haiersoft.ccli.api.entity.InStockInfoSort;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class InStockInfoService extends BaseService<InStockInfo, String> {

	@Autowired
	private InStockInfoDao inStockInfoDao;
	
	@Override
	public HibernateDao<InStockInfo, String> getEntityDao() {
		return inStockInfoDao;
	}
	
	/**
	 * 查询历史库存信息
	 */
	public List<Map<String,Object>> getStockInPeriod(List<Integer> clientIdList, List<String> itemClass, String dateStart, String dateEnd) {
		
		List<?> list= inStockInfoDao.getStockInfoInPeriod(clientIdList,itemClass, dateStart, dateEnd);
		List<Map<String,Object>> historyList = new ArrayList();
		
		if(list != null && list.size() > 0) {
			
			for(int i=0; i<list.size(); i++) {
				Map<String,Object> historyStockMap = new HashMap();
				Object[] obj = (Object[])list.get(i);				
				historyStockMap.put("customerNumber", obj[0]);
				historyStockMap.put("customerName", obj[1]);
				historyStockMap.put("itemClassName", obj[2]);
				historyStockMap.put("itemCLass", obj[3]);	
				historyStockMap.put("historyAmount", obj[4]);
				historyStockMap.put("historyWeight", String.format("%.2f",obj[5]));
				historyStockMap.put("instorageDate", obj[7]);			
				historyList.add(historyStockMap);
			}
		}
		return historyList;
		
	}
	
	/**
	 * 查询在库信息
	 * @throws ParseException 
	 */
	public List<InStockInfo> getInStockInfo(List<String> itemClass, List<Integer> stockIn, String id,InStockInfoSort inStockInfoSort){
		List<InStockInfo> inStockInfoList = inStockInfoDao.getInStockInfo(itemClass, stockIn, id,inStockInfoSort);
		return inStockInfoList;
	}
	
	/**
	 * 查询在库信息总条数
	 * @throws ParseException 
	 */
	public Integer getTotal(List<String> itemClass, List<Integer> stockIn, String id,InStockInfoSort inStockInfoSort){
		return inStockInfoDao.getTotal(itemClass, stockIn, id,inStockInfoSort);
	}
	
	/**
	 * 查询动态质押汇总数据
	 */
	public List<Map<String, Object>> dynamicQuery(Integer customerId, String itemClasses){
		
		List<Map<String, Object>> list = inStockInfoDao.dynamicQuery(customerId,itemClasses);
	
		return list;
		
	}

}
