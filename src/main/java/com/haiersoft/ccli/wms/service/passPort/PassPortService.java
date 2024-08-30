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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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

	public Map<String, Object> getDataByVehicleNo(String vehicleNo,String ioTypecd){
		Map<String, Object> result = new HashMap<>();
		List<Map<String,Object>> bisPassPortDataMap = passPortDao.getDataByVehicleNo(vehicleNo,ioTypecd);
		if(bisPassPortDataMap == null || bisPassPortDataMap.size() == 0){
			result.put("code", "500");
			result.put("msg", "未找到核放单信息!");
			return result;
		}else{
			if(bisPassPortDataMap.get(0) != null && "0".equals(bisPassPortDataMap.get(0).get("LOCKAGE").toString().trim()) ){
				String totalWt = bisPassPortDataMap.get(0).get("TOTAL_WT")==null?"0":bisPassPortDataMap.get(0).get("TOTAL_WT").toString();
				String flag = bisPassPortDataMap.get(0).get("IO_TYPECD")==null?"":bisPassPortDataMap.get(0).get("IO_TYPECD").toString();
				String hfdNo = "";
				if(bisPassPortDataMap.get(0).get("PASSPORT_NO")!=null && bisPassPortDataMap.get(0).get("PASSPORT_NO").toString().trim().length() > 0){
					hfdNo = bisPassPortDataMap.get(0).get("PASSPORT_NO").toString().trim();
				}else{
					if(bisPassPortDataMap.get(0).get("SEQ_NO")!=null && bisPassPortDataMap.get(0).get("SEQ_NO").toString().trim().length() > 0){
						hfdNo = bisPassPortDataMap.get(0).get("SEQ_NO").toString().trim();
					}
				}

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("totalWt",totalWt);//总重量
				map.put("flag",flag);//I-进区;E-出区
				map.put("hfdNo",hfdNo);//核放单号或预录入统一编号

				result.put("code", "200");
				result.put("data", map);
				result.put("msg", "success");
				return result;
			}else{
				result.put("code", "500");
				result.put("msg", "未找到核放单信息!");
				return result;
			}
		}
	}

	public Map<String, Object> passButNotPassGate(String vehicleNo,String ioTypecd){
		Map<String, Object> result = new HashMap<>();
		List<Map<String,Object>> bisPassPortDataMap = passPortDao.passButNotPassGate(vehicleNo,ioTypecd);
		if(bisPassPortDataMap == null || bisPassPortDataMap.size() == 0){
			result.put("code", "500");
			result.put("msg", "未找到核放单信息!");
			return result;
		}else{
			if(bisPassPortDataMap.get(0) != null){
				String inAndOut = bisPassPortDataMap.get(0).get("IO_TYPECD")==null?"":bisPassPortDataMap.get(0).get("IO_TYPECD").toString();
				BigDecimal vehicleWeight = bisPassPortDataMap.get(0).get("VEHICLE_WT")==null?new BigDecimal(0):new BigDecimal(bisPassPortDataMap.get(0).get("VEHICLE_WT").toString());//车自重
				BigDecimal vehicleFrameWeight = bisPassPortDataMap.get(0).get("VEHICLE_FRAME_WT")==null?new BigDecimal(0):new BigDecimal(bisPassPortDataMap.get(0).get("VEHICLE_FRAME_WT").toString());//车架重
				BigDecimal containerWeight = bisPassPortDataMap.get(0).get("CONTAINER_WT")==null?new BigDecimal(0):new BigDecimal(bisPassPortDataMap.get(0).get("CONTAINER_WT").toString());//箱重
				BigDecimal totalGrossWeight = bisPassPortDataMap.get(0).get("TOTAL_GROSS_WT")==null?new BigDecimal(0):new BigDecimal(bisPassPortDataMap.get(0).get("TOTAL_GROSS_WT").toString());//货毛重
				BigDecimal totalNetWeight = bisPassPortDataMap.get(0).get("TOTAL_NET_WT")==null?new BigDecimal(0):new BigDecimal(bisPassPortDataMap.get(0).get("TOTAL_NET_WT").toString());//货净重
				BigDecimal totalWeight = bisPassPortDataMap.get(0).get("TOTAL_WT")==null?new BigDecimal(0):new BigDecimal(bisPassPortDataMap.get(0).get("TOTAL_WT").toString());//总重
				String hfdNo = "";
				if(bisPassPortDataMap.get(0).get("PASSPORT_NO")!=null && bisPassPortDataMap.get(0).get("PASSPORT_NO").toString().trim().length() > 0){
					hfdNo = bisPassPortDataMap.get(0).get("PASSPORT_NO").toString().trim();
				}else{
					if(bisPassPortDataMap.get(0).get("SEQ_NO")!=null && bisPassPortDataMap.get(0).get("SEQ_NO").toString().trim().length() > 0){
						hfdNo = bisPassPortDataMap.get(0).get("SEQ_NO").toString().trim();
					}
				}

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("cph",vehicleNo);//车牌号
				map.put("inAndOut",inAndOut);//I-进区;E-出区
				if ("I".equals(inAndOut)){
					map.put("inAndOutStr","进区");
				}else if ("E".equals(inAndOut)){
					map.put("inAndOutStr","出区");
				}else{
					map.put("inAndOutStr","");
				}
				map.put("hfdNo",hfdNo);//核放单号或预录入统一编号
				map.put("carWeight",vehicleWeight.add(vehicleFrameWeight));//车自重+车架重
				map.put("productWeight",totalGrossWeight);//货重(货毛重)
				map.put("totalWeight",totalWeight);//总重(车自重+车架重+箱重+货毛重)

				map.put("vehicleWeight",vehicleWeight);//车自重
				map.put("vehicleFrameWeight",vehicleFrameWeight);//车架重
				map.put("containerWeight",containerWeight);//箱重
				map.put("totalGrossWeight",totalGrossWeight);//货毛重
				map.put("totalNetWeight",totalNetWeight);//货净重

				result.put("code", "200");
				result.put("data", map);
				result.put("msg", "success");
				return result;
			}else{
				result.put("code", "500");
				result.put("msg", "未找到核放单信息!");
				return result;
			}
		}
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
