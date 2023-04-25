package com.haiersoft.ccli.report.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.haiersoft.ccli.report.dao.TrayReportDao; 
import com.haiersoft.ccli.base.dao.BaseTrayDao;

/**
 * @author slh
 * @ClassName: TrayReportService
 * @Description: 库位使用情况
 * @date 20160623 1019
 */
@Service
public class TrayReportService {

	@Autowired
	private TrayReportDao trayReportDao;
	@Autowired
	private BaseTrayDao baseTrayDao;

	/**
	 * 获取已使用的库房号 by slh 20160623
	 * 
	 * @param BuildingNum 楼号
	 *            
	 * @return
	 */
	public List<Map<String, Object>> FindTrayInfoByBuiNum(String BulidingNum) {
		return trayReportDao.FindTrayInfoByBuiNum(BulidingNum);
	}

	/**
	 * 空楼库位框架 by slh 20160623 
	 * @param BuildingNum 楼号 
	 * @return
	 */
	public List<Map<String, Object>> FindTrayFByBuildingNum(String BulidingNum) {
		return baseTrayDao.FindTrayFByBuildingNum(BulidingNum);
	}
	
	/**
	 * 获取已使用的库房号 by slh 20160623
	 * 
	 * @param BuildingNum 楼号  Housid 仓库id
	 *            
	 * @return
	 */
	public List<Map<String, Object>> FindTrayInfo(String BulidingNum,String Housid) {
		return trayReportDao.FindTrayInfo(BulidingNum,Housid);
	}

	/**
	 * 空楼库位框架 by slh 20160623 
	 * @param BuildingNum 楼号  Housid 仓库id
	 * @return
	 */
	public List<Map<String, Object>> FindTrayF(String BulidingNum,String Housid) {
		return baseTrayDao.FindTrayF(BulidingNum,Housid);
	} 

	/**
	 *获取空库位框架 by slh 20160623 
	 * @param BuildingNum 楼号  Housid 仓库id
	 * @return
	 */
	public List<Map<String, Object>> FindBTrayStoreF(String BulidingNum,String Housid) {
		return baseTrayDao.FindTrayStoreF(BulidingNum,Housid);
	} 
	/**
	 *获取库房货物统计 by slh 20160623 
	 * @param BuildingNum 楼号  Housid 仓库id
	 * @return
	 */
	public List<Map<String, Object>> FindTrayStoreInfo(String BulidingNum,String Housid) {
		return trayReportDao.FindTrayStoreInfo(BulidingNum,Housid);
	}
	
	/**
	 * 获取现有个类产品的库存 by slh 20160623 
	 * @param BuildingNum 楼号  Housid 仓库id
	 * @return
	 */
	public List<Map<String, Object>> FindTrayProductSum(String BulidingNum,String Housid) {
		return trayReportDao.FindTrayProductSum(BulidingNum,Housid);
	}
}
