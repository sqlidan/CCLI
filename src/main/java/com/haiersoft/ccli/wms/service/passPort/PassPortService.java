package com.haiersoft.ccli.wms.service.passPort;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PassPortDao;
import com.haiersoft.ccli.wms.dao.ScmDictDao;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPort;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.web.scm.ScmTaskOneController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @ClassName: PassPortService
 * @Description: 预报单Service
 */
@Service
@Transactional(readOnly = true)
public class PassPortService extends BaseService<BisPassPort, String> {
	private static final Logger logger = LoggerFactory.getLogger(PassPortService.class);
	
	@Autowired
	private PassPortDao passPortDao;
	@Autowired
	private ScmDictDao scmDictDao;

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
				String passportTypecd = bisPassPortDataMap.get(0).get("PASSPORT_TYPECD")==null?"":bisPassPortDataMap.get(0).get("PASSPORT_TYPECD").toString();
				String areainEtpsNm = bisPassPortDataMap.get(0).get("AREAIN_ETPS_NM")==null?"":bisPassPortDataMap.get(0).get("AREAIN_ETPS_NM").toString();
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
				map.put("passportTypecd",passportTypecd);//核放单类型
				String passportTypecdName = "";
				if("1".equals(passportTypecd)){
					passportTypecdName = "先入区后报关";
				}
				if("2".equals(passportTypecd)){
					passportTypecdName = "一线一体化进出区";
				}
				if("3".equals(passportTypecd)){
					passportTypecdName = "二线进出区";
				}
				if("4".equals(passportTypecd)){
					passportTypecdName = "非报关进出区";
				}
				if("5".equals(passportTypecd)){
					passportTypecdName = "卡口登记货物";
				}
				if("6".equals(passportTypecd)){
					passportTypecdName = "空车进出区";
				}
				if("7".equals(passportTypecd)){
					passportTypecdName = "两步申报";
				}
				map.put("hfdType",passportTypecdName);//核放单类型
				map.put("declareCompany",areainEtpsNm);//申报企业
				map.put("carWeight",vehicleWeight.add(vehicleFrameWeight));//车自重+车架重
				map.put("productWeight",totalGrossWeight);//货重（货毛重）
				map.put("totalWeight",totalWeight);//总重(车自重+车架重+箱重+货毛重)

				map.put("vehicleWeight",vehicleWeight);//车自重
				map.put("vehicleFrameWeight",vehicleFrameWeight);//车架重
				map.put("containerWeight",containerWeight);//箱重
				map.put("totalGrossWeight",totalGrossWeight);//货毛重
				map.put("totalNetWeight",totalNetWeight);//货净重


				// 20250512 徐峥增加核放单编号，进出标志，货物毛重，车号
				String passPort = hfdNo;
				String io = ioTypecd.trim();
				if ("E".equals(ioTypecd.trim())){
					io = "O";
				}
				map.put("passPort",passPort);//核放单编号
				map.put("ioType",io);//进出标志 I-进区；O-出区
				map.put("productGrossWeight",totalGrossWeight);//货物毛重
				map.put("vehicleNo",vehicleNo);//车号


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
//====================================================================================================================
	//全量查询实时库存
	public Map<String, Object> queryFullInventoryData(String startTime, String endTime){
		Map<String, Object> result = new HashMap<>();

		List<Map<String,Object>> mapList = scmDictDao.queryFullInventoryData(startTime, endTime);
		if(mapList == null || mapList.size() == 0){
			result.put("code", "500");
			result.put("msg", "未找到对应的库存信息!");
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		result.put("code", "200");
		result.put("data", JSON.toJSON(mapList));
		result.put("msg", "success");
		return result;
	}
//====================================================================================================================
	//转口货物备案查询
	public Map<String, Object> queryDeclarationTransshipmentGoods(String accountBook,String emsNo){
		Map<String, Object> result = new HashMap<>();

		List<Map<String,Object>> mapList = scmDictDao.queryDeclarationTransshipmentGoods(accountBook,emsNo);
		if(mapList == null || mapList.size() == 0){
			result.put("code", "500");
			result.put("msg", "未找到对应的库存信息!");
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		result.put("code", "200");
		result.put("data", JSON.toJSON(mapList));
		result.put("msg", "success");
		return result;
	}
	//一键推送转口货物备案
	public Map<String, Object> pushDeclarationTransshipmentGoods(String accountBook,String emsNo){
		Map<String, Object> result = new HashMap<>();
		//查询
		List<Map<String,Object>> mapList = scmDictDao.queryDeclarationTransshipmentGoods(accountBook,emsNo);
		if(mapList == null || mapList.size() == 0){
			result.put("code", "500");
			result.put("msg", "暂无数据");
			return result;
		}
		//推送
		execute(mapList);

		Map<String, Object> map = new HashMap<String, Object>();
		result.put("code", "200");
		result.put("msg", "一键推送转口货物备案数据成功");
		return result;
	}
	//执行发送
	public static final String url = "https://apiplat.sdland-sea.com/scm/XXXXX";//一键推送转口货物备案地址
	public void execute(List<Map<String,Object>> mapList) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		//增量推送入库订单明细
		Map<String, Object> params1 = new HashMap<String, Object>();
		UUID uuid1 = UUID.randomUUID();
		String str1 = uuid1.toString().replace("-","");
		params1.put("requestId",str1);
		params1.put("requestDate",sdf.format(date));
		params1.put("key",UUID.randomUUID());
		params1.put("list",mapList);
		params1.put("data",mapList);
		logger.info("一键推送转口货物备案参数："+ JSON.toJSONString(params1));
		String bodys1 = HttpUtil.createPost(url)
				.body(JSON.toJSONString(params1))
				.execute()
				.body();
		JSONObject respones1 = JSONObject.parseObject(bodys1);
		logger.info("一键推送转口货物备案结果："+ respones1.toJSONString());
		if(0 != respones1.getIntValue("code")){
			logger.info("一键推送转口货物备案推送失败："+respones1.getString("msg"));
		}
	}
}
