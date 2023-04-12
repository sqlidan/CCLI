package com.haiersoft.ccli.api.service;

import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.api.dao.ApiPledgeDao;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class ApiPledgeService extends BaseService<ApiPledge, String> {
	@Autowired
	private ApiPledgeDao apiPledgeDao;
	@Override
	public HibernateDao<ApiPledge, String> getEntityDao() {
		return apiPledgeDao;
	}
	
//	@Autowired
//	private TrayInfoService trayInfoService;

	
//	public HashMap<String, Object> activeQuery(Integer itemClass,String id){
//		return apiPledgeDao.activeQuery(itemClass,id);
//	}
	
//	public HashMap<String, Object> activeReleaseSumQuery(String customerNumber,Integer itemClass){
//		return apiPledgeDao.activeReleaseSumQuery(customerNumber,itemClass);
//	}
//	
//	public List<ApiPledge>activeNowQuery(String customerNum){
//		return apiPledgeDao.activeNowQuery(customerNum);
//	}

	// 动态质押
//	@Transactional
//	public ResponseVo activeSave(PledgeRequestVo requestVo) {

		// 查询已动态质押的数量
//			HashMap<String, BigDecimal>alreadyResult =  apiPledgeDao.activeSaveSumQuery(requestVo.getCustomerNumber(),requestVo.getItemClass());
//			BigDecimal alreadyNum = alreadyResult.get("alreadyNum");//已质押数量
//			BigDecimal alreadyWeight = alreadyResult.get("alreadyWeight");//已质押重量

//		ApiPledge api = new ApiPledge();
//		api.setAccountId(requestVo.getAccountId());
//		api.setCustomerNumber(requestVo.getCustomerNumber());
//		api.setItemClass(requestVo.getItemClass());
//		api.setPledgeNumber(requestVo.getPledgeNumber());
//		api.setPledgeWeight(requestVo.getPledgeWeight());
//		api.setState(2);
//		api.setTrendId(requestVo.getTrendId());
//		api.setCreateDate(new Date());
//		apiPledgeDao.save(api);
//		return ResponseVo.pledgeOk();

//	}
	
	public List<ApiPledge> findPledgedRecordByTrendId(String trendId) {
		return apiPledgeDao.find(Restrictions.eq("trendId",trendId),Restrictions.eq("confirmStatus",1),Restrictions.eq("state",1));
	}
	
	public HashMap<String, Object> countStaticPledgedNumByTrendId(String trendId){
		return apiPledgeDao.countStaticPledgedNumByTrendId(trendId);
		
	}
	  
	//根据库存主键查询当前静态质押的数量，即全部静态质押的数量 - 全部解押的数量
	public HashMap<String, Object> countTotalPledgedByTrayInfoId(Integer trayInfoId){
		return apiPledgeDao.countTotalPledgedByTrayInfoId(trayInfoId);
		
	}
	//根据RelatedTrendId查询当前静态质押的数量
	public HashMap<String, Object> countRelatedTrendIdByRelatedTrendId(String relatedTrendId){
		return apiPledgeDao.countRelatedTrendIdByRelatedTrendId(relatedTrendId);
		
	}
	  //动态解压
//	@Transactional
//	  public ResponseVo activeRelease(PledgeRequestVo requestVo) {
//		//判断动态质押解押货主账号是否存在
//			//先根据货主账号查询ids
//			List<BaseClientInfo> list = apiPledgeDao.QueryIdsByCustomerNumber(requestVo.getCustomerNumber());
//			if(list.size()==0) {
//				return ResponseVo.error("货主账号不存在");
//			}
//		//先查询要解押的数据的质押数量和重量
//		HashMap<String, Object>result =  apiPledgeDao.activeSumQueryBytrendId(requestVo.getRelatedTrendId());
//		Integer saveNum = Integer.valueOf(result.get("saveNum").toString());//质押总数量
//		Double saveWeight = Double.valueOf(result.get("saveWeight").toString());//质押总重量
//		if(saveNum==0 ||saveWeight==0.0) {
//			return ResponseVo.error("质押唯一标识为："+requestVo.getRelatedTrendId()+"的质押记录未找到");
//		}else {
//			HashMap<String, Object>releaseResult =  apiPledgeDao.activeSumReleaseQueryBytrendId(requestVo.getRelatedTrendId());
//			Integer releaseNum = Integer.valueOf(releaseResult.get("releaseNum").toString());//解押总数量
//			Double releaseWeight = Double.valueOf(releaseResult.get("releaseWeight").toString());//解押总重量
//		
//			Integer pledgeNum = Integer.valueOf(requestVo.getPledgeNumber().toString());//最低质押数量
//			Double pledgeWeight = Double.valueOf(requestVo.getPledgeWeight().toString());//最低质押重量
//			//判断当前是否进行过解押
//			if(releaseNum == null || releaseWeight == null ) {//没解押过
//				if(pledgeNum >= saveNum ) {
//					return ResponseVo.error("解除质押的重量或数量超出当前质押重量或数量！");
//				}else {
//					ApiPledge api = new ApiPledge();
//					api.setAccountId(requestVo.getAccountId());
//					api.setCustomerCode(requestVo.getCustomerNumber());
//					api.setItemClass(requestVo.getItemClass());
//					api.setPledgeNumber(requestVo.getPledgeNumber());
//					api.setPledgeWeight(requestVo.getPledgeWeight());
//					api.setState(3);
//					api.setCreateDate(new Date());
//					api.setTrendId(requestVo.getTrendId());
//					api.setRelatedTrendId(requestVo.getRelatedTrendId());
//					apiPledgeDao.save(api);
//					return ResponseVo.pledgeOk();
//				}	
//			}else {
//				//判断解押数量或重量是否超出
//				if(pledgeNum.compareTo(releaseNum)==1||pledgeWeight.compareTo(releaseWeight)==1) {
//					return ResponseVo.error("解除质押的重量或数量超出当前质押重量或数量！");
//				}else {
//					ApiPledge api = new ApiPledge();
//					api.setAccountId(requestVo.getAccountId());
//					api.setCustomerCode(requestVo.getCustomerNumber());
//					api.setItemClass(requestVo.getItemClass());
//					api.setPledgeNumber(requestVo.getPledgeNumber());
//					api.setPledgeWeight(requestVo.getPledgeWeight());
//					api.setState(3);
//					api.setCreateDate(new Date());
//					api.setTrendId(requestVo.getTrendId());
//					api.setRelatedTrendId(requestVo.getRelatedTrendId());
//					apiPledgeDao.save(api);
//					return ResponseVo.pledgeOk();
//				}	
//			}
//			
//		}
//		
//	  }
	/*
	 * 查询唯一质押ID
	 * @author:wangxiang
	 * @param params
	 */
	public List<ApiPledge> checkTrendId(String trendId){
		return apiPledgeDao.checkTrendId(trendId);
	}
	/*
	 * 查询唯一质押ID(动态）
	 * @author:wangxiang
	 * @param params
	 */
	public List<ApiPledge> checkTrendIdActive(String trendId){
		return apiPledgeDao.checkTrendIdActive(trendId);
	}

	public ApiPledge findUniqueByTrendId(String relatedTrendId) {
		
		return apiPledgeDao.findUniqueBy("trendId", relatedTrendId);
	}

//	public HashMap<String, Object> activeSumQueryBytrendId(String relatedTrendId) {
//		
//		return apiPledgeDao.activeSumQueryBytrendId(relatedTrendId);
//	}

//	public HashMap<String, Object> activeSumReleaseQueryBytrendId(String relatedTrendId) {
//		
//		return apiPledgeDao.activeSumReleaseQueryBytrendId(relatedTrendId);
//	}

	public List<ApiPledge> findOrderedListByRelatedTrendId(String relatedTrendId){
		return apiPledgeDao.findOrderedListByRelatedTrendId(relatedTrendId);
	}
	
	//根据客户号小类代码计算当前动态质押数量
	public HashMap<String, Object> countActivePledgedNum(Integer customerId, Integer itemClass) {
		
		//先查出客户号小类代码下边所有的  relatedTrendId
		List<String> relatedTrendIdList = apiPledgeDao.findRelatedTrendIds(customerId,itemClass);
		//List<String> relatedTrendIdList = apiPledgeDao.findRelatedTrendIds(9998,20);
		
		Integer sumNum = 0;
		Double sumWeight = 0.0;
		for(String relatedTrendId :relatedTrendIdList) {
			
			//Integer num = 0;
			//Double weight = 0.0;
			// 根据relatedTrendId查询此组质押/解押记录 按日期排序
			List<ApiPledge> records = apiPledgeDao.findOrderedListByRelatedTrendId(relatedTrendId);
//			if (records.size() == 1) // 只有质押没有解押过的情况,质押数量/重量为原质押记录的质押数量/重量
//			{
//				num = records.get(0).getPledgeNumber();
//				weight = records.get(0).getPledgeWeight();
//			}
//			if (records.size() > 1) // 有解押过的情况 质押数量/重量 等于 最新一条解押记录的质押数量/重量
//			{
//				num =  records.get(records.size() - 1).getPledgeNumber();
//				weight =  records.get(records.size() - 1).getPledgeWeight();
//			}
			
			// 根据日期排序查询动态质押记录后，当前动态质押数量即最新的一条 质押/解押记录的数量
			Integer num = records.get(0).getPledgeNumber();
			Double weight = records.get(0).getPledgeWeight();
			sumNum = sumNum + num;
			sumWeight = sumWeight + weight;		
			
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("sumNum", sumNum);
		map.put("sumWeight", sumWeight);

		return map;
	}
	

	//根据客户号小类代码计算当是否可质押（总量）
	//可质押数量 = 库存数量-静态质押-动态质押
	public Boolean pledgeCountCheck(ApiPledge pledge) {
		// 查询库存
		HashMap<String, Object> tray = apiPledgeDao.trayQuery(pledge.getCustomerId(), pledge.getItemClass());

		//查询静态质押
		HashMap<String, Object> staticPledged = apiPledgeDao.countStaticPledgedNum(pledge.getCustomerId(),
				pledge.getItemClass());

		//查询动态质押
		HashMap<String, Object> activePledged = this.countActivePledgedNum(pledge.getCustomerId(),
				pledge.getItemClass());

		Integer trayNum = Integer.valueOf(tray.get("sumNum").toString());
		Integer staticPledgedNum = Integer.valueOf(staticPledged.get("sumNum").toString());
		Integer activePledgedNum = Integer.valueOf(activePledged.get("sumNum").toString());

		// 如果可质押数量小于本次质押数量,即不可质押
		if (trayNum - staticPledgedNum - activePledgedNum < pledge.getPledgeNumber()) {
			return false;
		}
		return true;

	}
	
	//根据客户号小类代码统计当前可出库数量
	//可出库数量 = 库存数量-静态质押-动态质押
	public Integer pledgeNumCount(Integer customerId,Integer itemClass) {
		// 查询库存
		HashMap<String, Object> tray = apiPledgeDao.trayQuery(customerId, itemClass);

		//查询静态质押
		HashMap<String, Object> staticPledged = apiPledgeDao.countStaticPledgedNum(customerId,itemClass);

		//查询动态质押
		HashMap<String, Object> activePledged = this.countActivePledgedNum(customerId,itemClass);

		Integer trayNum = Integer.valueOf(tray.get("sumNum").toString());
		Integer staticPledgedNum = Integer.valueOf(staticPledged.get("sumNum").toString());
		Integer activePledgedNum = Integer.valueOf(activePledged.get("sumNum").toString());

		return trayNum - staticPledgedNum - activePledgedNum;

	}

	public HashMap<String, Object> countPledgedByTrayInfoId(Integer trayInfoId) {
		
		return apiPledgeDao.countPledgedByTrayInfoId(trayInfoId);
	}
	  
	public HashMap<String, String> getSKUandWarehouse(ApiPledge apiPledge){
		return apiPledgeDao.getSKUandWarehouse(apiPledge);
	}
	public HashMap<String, Object> sumStaticPledgedNum(Integer trayInfoId){
		return apiPledgeDao.sumStaticPledgedNum(trayInfoId);
	}
}
