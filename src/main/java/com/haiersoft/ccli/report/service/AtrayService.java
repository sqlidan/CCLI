package com.haiersoft.ccli.report.service;

import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.report.dao.ATrayDao;
import com.haiersoft.ccli.report.dao.StockReportDao;
import com.haiersoft.ccli.report.entity.ATray;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AtrayService extends BaseService<ATray, String> {

	@Autowired
	private ATrayDao aTrayDao;
	@Override
	public HibernateDao<ATray, String> getEntityDao() {
		return aTrayDao;
	}

	public String createDataNewTemp() throws InvocationTargetException, IllegalAccessException {
		aTrayDao.deleteData();
		List<Map<String,Object>> mapList = aTrayDao.queryData();
		int count = 0;
		for (Map<String,Object> forMap:mapList) {
			JSONObject jsonObject = new JSONObject(forMap);
			ATray aTray = new ATray();
			aTray.setId(jsonObject.getInteger("XH"));
			aTray.setBillCode(jsonObject.getString("BILLCODE"));
			aTray.setNowNum(jsonObject.getBigDecimal("NOWNUM"));
			aTray.setAllnet(jsonObject.getBigDecimal("ALLNET"));
			aTray.setAllgross(jsonObject.getBigDecimal("ALLGROSS"));
			aTray.setXh(jsonObject.getBigDecimal("XH"));
			int num = aTrayDao.saveATray(aTray);
			System.out.println("num"+num);
			count++;
		}
		System.out.println("count"+count);
		return "success";
	}

	public  List<ATray> querySQDData(){
		List<ATray> aTrayList = new ArrayList<>();

		List<Map<String,Object>> mapList = aTrayDao.querySQDData();
		for (Map<String,Object> forMap:mapList) {
			JSONObject jsonObject = new JSONObject(forMap);
			ATray aTray = new ATray();
			aTray.setId(jsonObject.getInteger("ID"));
			aTray.setBillCode(jsonObject.getString("BILLCODE"));
			aTray.setNowNum(jsonObject.getBigDecimal("NOWNUM"));
			aTray.setAllnet(jsonObject.getBigDecimal("ALLNET"));
			aTray.setAllgross(jsonObject.getBigDecimal("ALLGROSS"));
			aTray.setLinkId(jsonObject.getString("LINKID"));
			aTray.setXh(jsonObject.getBigDecimal("XH"));
			aTrayList.add(aTray);
		}
		return aTrayList;
	}



}
