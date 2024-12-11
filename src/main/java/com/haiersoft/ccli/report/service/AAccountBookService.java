package com.haiersoft.ccli.report.service;

import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.report.dao.AAccountBookDao;
import com.haiersoft.ccli.report.dao.ATrayDao;
import com.haiersoft.ccli.report.entity.AAccountBook;
import com.haiersoft.ccli.report.entity.ATray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AAccountBookService extends BaseService<AAccountBook, String> {

	@Autowired
	private AAccountBookDao aAccountBookDao;
	@Override
	public HibernateDao<AAccountBook, String> getEntityDao() {
		return aAccountBookDao;
	}

	public List<AAccountBook> queryClearInventorySData(){
		List<AAccountBook> aAccountBookList = new ArrayList<>();

		List<Map<String,Object>> mapList = aAccountBookDao.queryClearInventorySData();
		for (Map<String,Object> forMap:mapList) {
			JSONObject jsonObject = new JSONObject(forMap);
			AAccountBook aAccountBook = new AAccountBook();
			aAccountBook.setEmsNo(jsonObject.getString("EMSNO"));
			aAccountBook.setGNo(jsonObject.getString("GNO"));
			aAccountBook.setCodeTs(jsonObject.getString("CODETS"));
			aAccountBook.setGName(jsonObject.getString("GNAME"));
			aAccountBook.setGModel(jsonObject.getString("GMODEL"));
			aAccountBook.setUnit(jsonObject.getString("UNIT"));
			aAccountBook.setApprQty(jsonObject.getString("APPRQTY"));
			aAccountBook.setCutQty(jsonObject.getString("CUTQTY"));
			aAccountBook.setDeductQty(jsonObject.getString("DEDUCTQTY"));
			aAccountBook.setConfirmQty(jsonObject.getString("CONFIRMQTY"));
			aAccountBook.setSrcBillId(jsonObject.getString("SRCBILLID"));
			aAccountBook.setSrcGNo(jsonObject.getString("SRCGNO"));
			aAccountBook.setQty1(jsonObject.getString("QTY1"));
			aAccountBook.setGdsMtno(jsonObject.getString("GDSMTNO"));
			aAccountBookList.add(aAccountBook);
		}
		return aAccountBookList;

	}

	public List<AAccountBook> queryClearInventoryHData(){
		List<AAccountBook> aAccountBookList = new ArrayList<>();

		List<Map<String,Object>> mapList = aAccountBookDao.queryClearInventoryHData();
		for (Map<String,Object> forMap:mapList) {
			JSONObject jsonObject = new JSONObject(forMap);
			AAccountBook aAccountBook = new AAccountBook();
			aAccountBook.setEmsNo(jsonObject.getString("EMSNO"));
			aAccountBook.setGNo(jsonObject.getString("GNO"));
			aAccountBook.setCodeTs(jsonObject.getString("CODETS"));
			aAccountBook.setGName(jsonObject.getString("GNAME"));
			aAccountBook.setGModel(jsonObject.getString("GMODEL"));
			aAccountBook.setUnit(jsonObject.getString("UNIT"));
			aAccountBook.setApprQty(jsonObject.getString("APPRQTY"));
			aAccountBook.setCutQty(jsonObject.getString("CUTQTY"));
			aAccountBook.setDeductQty(jsonObject.getString("DEDUCTQTY"));
			aAccountBook.setConfirmQty(jsonObject.getString("CONFIRMQTY"));
			aAccountBook.setSrcBillId(jsonObject.getString("SRCBILLID"));
			aAccountBook.setSrcGNo(jsonObject.getString("SRCGNO"));
			aAccountBook.setQty1(jsonObject.getString("QTY1"));
			aAccountBook.setGdsMtno(jsonObject.getString("GDSMTNO"));
			aAccountBookList.add(aAccountBook);
		}
		return aAccountBookList;

	}

}
