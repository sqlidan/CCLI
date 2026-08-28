package com.haiersoft.ccli.bounded.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.bounded.dao.BaseBoundedDao;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class BaseBoundedService extends BaseService<BaseBounded, String> {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseBoundedService.class);
	
	@Autowired
	private BaseBoundedDao baseBoundedDao  ;

	@Autowired
	private TrayInfoService trayInfoService;

	@Autowired
	private EnterStockService enterStockService;
	
	@Override
	public HibernateDao<BaseBounded, String> getEntityDao() {
		return baseBoundedDao;
	}


	public Page<BaseBounded> searchBaseBounded(Page<BaseBounded> page, BaseBounded baseBounded){
		return baseBoundedDao.searchBaseBounded(page, baseBounded);
	}
	
/*
	public Page<PlatformUser> seachCustomsClearanceSql(Page<PlatformUser> page,PlatformUser customsClearance){
		return platformUserDao.seachCustomsClearanceSql(page, customsClearance);
	}
*/

	public List<Map<String, Object>> getLoactionsByBillNum(BaseBounded baseBounded){
		return baseBoundedDao.getLoactionsByBillNum(baseBounded);
	}

	/**
	 * 入库联系单审核通过后，创建保税货物底账基础信息。
	 */
	@Transactional(readOnly = false)
	public void createAutoBaseBounded(BisEnterStock enterStock, List<BisEnterStockInfo> enterStockInfoList) {
		if (enterStock == null || !"1".equals(enterStock.getIfBonded())) {
			return;
		}
		BisEnterStockInfo firstInfo = getFirstEnterStockInfo(enterStockInfoList);
		BaseBounded baseBounded = new BaseBounded();
		baseBounded.setClientId(enterStock.getStockId());
		baseBounded.setClientName(enterStock.getStockIn());
		baseBounded.setBillNum(enterStock.getItemNum());
		baseBounded.setCdNum(enterStock.getBgdh());
		baseBounded.setCustomerServiceName(enterStock.getExaminePerson());
		baseBounded.setHsCode(firstInfo == null ? null : firstInfo.getHsCode());
		baseBounded.setHsItemname(firstInfo == null ? null : firstInfo.getHsItemname());
		baseBounded.setAccountBook(firstInfo == null ? null : firstInfo.getAccountBook());
		baseBounded.setNetWeight(0D);
		baseBounded.setDclQty(0D);
		baseBounded.setHsQty(0D);
		baseBounded.setCreatedTime(new Date());
		baseBoundedDao.save(baseBounded);
	}

	/**
	 * 查询前一天创建的保税底账，按报关单号和提单号查询唯一的保税入库联系单，汇总托盘库存后回填保税底账。
	 * 若查询结果不是唯一一条，则跳过该底账，避免重复联系单导致库存串账。
	 */
	@Transactional(readOnly = false)
	public int syncAutoBaseBoundedStockInfo() {
		Date endTime = getTodayStartTime();
		Date startTime = getPreviousDayStartTime(endTime);
		List<BaseBounded> baseBoundedList = baseBoundedDao.getBaseBoundedListByCreatedTime(startTime, endTime);
		int updateCount = 0;
		for (BaseBounded baseBounded : baseBoundedList) {
			if (isBlank(baseBounded.getBillNum()) || isBlank(baseBounded.getCdNum())) {
				LOGGER.warn("保税底账[{}]缺少提单号或报关单号，跳过库存回填。", baseBounded.getId());
				continue;
			}
			List<BisEnterStock> enterStockList = enterStockService.getBondedEnterStockByBillNumAndBgdh(
					baseBounded.getBillNum().trim(), baseBounded.getCdNum().trim());
			if (enterStockList.size() != 1) {
				LOGGER.warn("保税底账[{}]按报关单号[{}]和提单号[{}]查询到{}条入库联系单，跳过库存回填。",
						baseBounded.getId(), baseBounded.getCdNum(), baseBounded.getBillNum(), enterStockList.size());
				continue;
			}
			List<TrayInfo> trayInfoList = trayInfoService.getBondedStockTrayList(enterStockList.get(0).getLinkId());
			double totalNetWeight = 0D;
			for (TrayInfo trayInfo : trayInfoList) {
				totalNetWeight += trayInfo.getNetWeight() == null ? 0D : trayInfo.getNetWeight();
			}
			Map<String, Object> locationInfo = getLocationInfo(baseBounded);
			baseBounded.setNetWeight(totalNetWeight);
			baseBounded.setDclQty(totalNetWeight);
			baseBounded.setHsQty(totalNetWeight);
			baseBounded.setCargoLocation(getMapValue(locationInfo, "LOCATIONCODE"));
			baseBounded.setCargoArea(getMapValue(locationInfo, "AREANUM"));
			baseBounded.setUpdatedTime(new Date());
			baseBoundedDao.merge(baseBounded);
			updateCount++;
		}
		return updateCount;
	}

	private Date getTodayStartTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	private Date getPreviousDayStartTime(Date todayStartTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(todayStartTime);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	private Map<String, Object> getLocationInfo(BaseBounded baseBounded) {
		List<Map<String, Object>> locationInfoList = getLoactionsByBillNum(baseBounded);
		return locationInfoList == null || locationInfoList.isEmpty() ? null : locationInfoList.get(0);
	}

	private String getMapValue(Map<String, Object> values, String key) {
		if (values == null || values.get(key) == null) {
			return "";
		}
		return values.get(key).toString();
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().length() == 0;
	}

	private BisEnterStockInfo getFirstEnterStockInfo(List<BisEnterStockInfo> enterStockInfoList) {
		if (enterStockInfoList == null || enterStockInfoList.isEmpty()) {
			return null;
		}
		BisEnterStockInfo firstInfo = null;
		for (BisEnterStockInfo enterStockInfo : enterStockInfoList) {
			if (firstInfo == null || (enterStockInfo.getId() != null
					&& (firstInfo.getId() == null || enterStockInfo.getId() < firstInfo.getId()))) {
				firstInfo = enterStockInfo;
			}
		}
		return firstInfo;
	}




}
