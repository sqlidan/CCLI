package com.haiersoft.ccli.wms.service.passPort;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PassPortDao;
import com.haiersoft.ccli.wms.dao.PreEntryDao;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPort;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: PassPortService
 * @Description: 预报单Service
 */
@Service
@Transactional(readOnly = true)
public class PassPortService extends BaseService<BisPassPort, String> {
	
	@Autowired
	private PassPortDao passPortDao;

	@Override
	public HibernateDao<BisPassPort, String> getEntityDao() {
		return passPortDao;
	}

	public List<BisPassPort> getList(String id){
		return passPortDao.findBy("id", id);
	}

	public List<BisPreEntryDictData> getDictDataByCode(String code){
		List<Map<String,Object>> bisPreEntryDictDataMap = passPortDao.getDictDataByCode(code);
		List<BisPreEntryDictData> bisPreEntryDictDataList = new ArrayList<>();
		for (Map<String,Object> map:bisPreEntryDictDataMap) {
			BisPreEntryDictData bisPreEntryDictData = new BisPreEntryDictData();
			bisPreEntryDictData.setId(map.get("ID")==null?"":map.get("ID").toString());
			bisPreEntryDictData.setParentId(map.get("PARENT_ID")==null?"":map.get("PARENT_ID").toString());
			bisPreEntryDictData.setCode(map.get("CODE")==null?"":map.get("CODE").toString());
			bisPreEntryDictData.setDictKey(map.get("DICT_KEY")==null?"":map.get("DICT_KEY").toString());
			bisPreEntryDictData.setDictValue(map.get("DICT_VALUE")==null?"":map.get("DICT_VALUE").toString());
			bisPreEntryDictData.setSort(map.get("SORT")==null?0:Integer.parseInt(map.get("SORT").toString()));
			bisPreEntryDictData.setLabel(bisPreEntryDictData.getDictKey());
			bisPreEntryDictData.setValue(bisPreEntryDictData.getDictValue());
			bisPreEntryDictDataList.add(bisPreEntryDictData);
		}
		return bisPreEntryDictDataList;
	}

}