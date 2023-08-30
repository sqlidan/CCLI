package com.haiersoft.ccli.wms.service.passPort;

import com.haiersoft.ccli.bounded.dao.BaseBoundedDao;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PassPortInfoDao;
import com.haiersoft.ccli.wms.dao.PreEntryInfoDao;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: PassPortInfoService
 */
@Service
@Transactional(readOnly = true)
public class PassPortInfoService extends BaseService<BisPassPortInfo, Integer> {

	@Autowired
	private PassPortInfoDao passPortInfoDao;
	@Autowired
	private BaseBoundedDao baseBoundedDao;

	@Override
    public HibernateDao<BisPassPortInfo, Integer> getEntityDao() {
	    return passPortInfoDao;
    }
	public List<BisPassPortInfo> getList(String id){
		return passPortInfoDao.findBy("passPortId", id);
	}
	public List<BaseBounded> getGdsInfo(String rltGdsSeqno){
		return baseBoundedDao.findBy("accountBook", rltGdsSeqno);
	}

	public List<BisPreEntryDictData> getDictDataByCode(String code){
		List<BisPreEntryDictData> bisPreEntryDictDataList = new ArrayList<>();
		if("lockage".equals(code)){
			BisPreEntryDictData bisPreEntryDictData0 = new BisPreEntryDictData();
			bisPreEntryDictData0.setValue("0");bisPreEntryDictData0.setLabel("已申请");
			bisPreEntryDictDataList.add(bisPreEntryDictData0);
			BisPreEntryDictData bisPreEntryDictData1 = new BisPreEntryDictData();
			bisPreEntryDictData1.setValue("1");bisPreEntryDictData1.setLabel("已审批(卡口放行)");
			bisPreEntryDictDataList.add(bisPreEntryDictData1);
			BisPreEntryDictData bisPreEntryDictData2 = new BisPreEntryDictData();
			bisPreEntryDictData2.setValue("2");bisPreEntryDictData2.setLabel("已过卡");
			bisPreEntryDictDataList.add(bisPreEntryDictData2);
			BisPreEntryDictData bisPreEntryDictData3 = new BisPreEntryDictData();
			bisPreEntryDictData3.setValue("3");bisPreEntryDictData3.setLabel("已过一卡");
			bisPreEntryDictDataList.add(bisPreEntryDictData3);
			BisPreEntryDictData bisPreEntryDictData4 = new BisPreEntryDictData();
			bisPreEntryDictData4.setValue("4");bisPreEntryDictData4.setLabel("已过二卡");
			bisPreEntryDictDataList.add(bisPreEntryDictData4);
			BisPreEntryDictData bisPreEntryDictData5 = new BisPreEntryDictData();
			bisPreEntryDictData5.setValue("5");bisPreEntryDictData5.setLabel("已删除");
			bisPreEntryDictDataList.add(bisPreEntryDictData5);
			BisPreEntryDictData bisPreEntryDictData6 = new BisPreEntryDictData();
			bisPreEntryDictData6.setValue("6");bisPreEntryDictData6.setLabel("已作废(拒绝过卡)");
			bisPreEntryDictDataList.add(bisPreEntryDictData6);
		}
		if("col1".equals(code)){
			BisPreEntryDictData bisPreEntryDictData0= new BisPreEntryDictData();
			bisPreEntryDictData0.setValue("0");bisPreEntryDictData0.setLabel("否");
			bisPreEntryDictDataList.add(bisPreEntryDictData0);
			BisPreEntryDictData bisPreEntryDictData1= new BisPreEntryDictData();
			bisPreEntryDictData1.setValue("1");bisPreEntryDictData1.setLabel("是");
			bisPreEntryDictDataList.add(bisPreEntryDictData1);
		}

		return bisPreEntryDictDataList;
	}
}
