package com.haiersoft.ccli.supervision.web;

import java.rmi.RemoteException;
import java.util.*;
import javax.transaction.Transactional;
import javax.xml.rpc.ServiceException;

import com.haiersoft.ccli.supervision.dao.ApprGnoSequenceDao;
import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.base.entity.BaseHscode;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.HscodeService;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.service.ApprHeadService;
import com.haiersoft.ccli.supervision.service.ApprInfoService;
import com.haiersoft.ccli.supervision.service.FljgWsClient;
import com.haiersoft.ccli.supervision.service.GetKeyService;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.haiersoft.ccli.wms.service.EnterStockService;



/**
 * 分类监管 申请单controller
 * 
 * @author
 *
 */

@Controller
@RequestMapping("supervision/enterappr")
public class EnterApprController extends BaseController {

	@Autowired
	private ApprHeadService apprHeadService;
	@Autowired
	private EnterStockService enterStockService;

	@Autowired
	private ApprInfoService apprInfoService;
	@Autowired
	private EnterStockInfoService enterStockInfoService;
	@Autowired
	private ClientService clientService;
	
	@Autowired
	HscodeService hscodeService;

	@Autowired
	ApprGnoSequenceDao apprGnoSequenceDao;


	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String saveApprHead(ApprHead apprHead) throws RemoteException, ServiceException {

		// 判断是否已经存在申请单
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_linkId", apprHead.getLinkId()));
		List<ApprHead> isExist = apprHeadService.search(filters);
		if (!isExist.isEmpty()) {
			return "exist";
		}
		// 根据联系单生成申请单head
		BisEnterStock bisEnterStock = enterStockService.get(apprHead.getLinkId());
		List<ApprInfo> apprInfoList = null;
		if (bisEnterStock != null) {
			/*
			 * BaseClientInfo baseClientInfo =
			 * clientService.get(Integer.valueOf(bisEnterStock.getStockId())); if
			 * (baseClientInfo != null) {
			 * //apprHead.setOwnerCode(baseClientInfo.getClientCode());
			 * //apprHead.setOwnerName(baseClientInfo.getClientName());
			 * 
			 * }
			 */
			apprHead.setOwnerCode("3702631016");
			apprHead.setOwnerName("青岛港怡之航冷链物流有限公司");
			apprHead.setLocalStatus("1");
			apprHead.setItemNum(bisEnterStock.getItemNum());
			apprHead.setCreateTime(new Date());
			List<BisEnterStockInfo> list = enterStockInfoService.getList(apprHead.getLinkId());
			apprInfoList = copyStockInfo2ApprInfo(list);

		}
		//底账项号修改为从数据库中的sequence获取
		//底账商品编号为空时填充		
//		if(StringUtils.isEmpty(apprHead.getgNo()))
//			apprHead.setgNo(getGnoMethod());
		apprHeadService.save(apprHead);		
		
		if (apprInfoList != null) {
			for (ApprInfo info : apprInfoList) {
//				info.setgNo(apprHead.getgNo());
				info.setHeadId(apprHead.getId());
				info.setIoType("1");
				apprInfoService.save(info);
			}
		}
		return "success";
	}



	//从库存信息生成申请单行
	private List<ApprInfo> copyStockInfo2ApprInfo(List<BisEnterStockInfo> list) {
		List<ApprInfo> la = new ArrayList<ApprInfo>();
		int index = 1;
		Map<String,Integer> gnoMap = this.getGnoMap(list);
		Date date = new Date();
		for (BisEnterStockInfo info : list) {
			ApprInfo ai = new ApprInfo();
			ai.setBisInfoId(String.valueOf(info.getId()));
			ai.setLinkId(info.getLinkId());
			ai.setApprGNo(index++);
			ai.setCodeTs(info.getHsCode());
			ai.setgName(info.getHsItemname());
			//规格
			ai.setgModel(info.getTypeSize());
			
			List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQS_code", info.getHsCode()));
			List<BaseHscode> bhs = hscodeService.search(filters);
			
			if (bhs.size() != 0) {
				//System.out.println("bhs:" + bhs.get(0).getItemNum());
				ai.setgUnit(bhs.get(0).getItemNum());
			} else {
				ai.setgUnit("035");
			}
		
			ai.setgQty(String.valueOf(info.getPiece()));
			ai.setUnit1(ai.getgUnit());
			ai.setQty1(ai.getgQty());
			ai.setGrossWt(String.valueOf(info.getGrossWeight()));
			ai.setCreateTime(date);
			//申请单行生成底账项号
			ai.setgNo(gnoMap.get(info.getCtnNum()+info.getHsCode()+info.getHsItemname()));
			ai.setCtnNum(info.getCtnNum());
			la.add(ai);
		}
		return la;
	}

	/**
	 * 根据货物信息中的箱号、商品编码、商品名称生成底账项号
	 * 一票多箱，多品名，每项一个“底账项号”
	 * @param list
	 * @return
	 */
	private Map<String, Integer> getGnoMap(List<BisEnterStockInfo> list) {
		Map<String, Integer> map = new HashMap<>(16);
		for (BisEnterStockInfo info :list){
			String key = info.getCtnNum()+info.getHsCode()+info.getHsItemname();
			if(null ==map.get(key)){
				map.put(key,apprGnoSequenceDao.getNextval());
			}
		}
		return map;
	}

	@Autowired
	GetKeyService getKeyService;

	@Autowired
	FljgWsClient fljgWsClient;
	
	//获取底账项号的方法
	private String getGnoMethod() throws RemoteException, ServiceException {
		//调用非保税账册查询
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("EmsNo", "NH4230200002");
		jsonObject.put("CodeTs", null);
		jsonObject.put("GNo", null);

		// 1 获得key
		// String tickid = getKeyService.builderTest();
		String tickId = getKeyService.builder();
		System.out.println(tickId);

		String serviceName = "NonEmsGoodsQuery";
		String result = fljgWsClient.getResult(jsonObject, tickId, serviceName);
		System.out.println("result: " + result);
		
		JSONObject jsonResult = JSON.parseObject(result);
        String state = jsonResult.getString("state");
        //获取接口中最大的gno
        long gnoRemote = 0;
        if(state.equals("1")) {
        	 //JSONArray jsonArray = JSONArray.fromObject(jsonResult.get("PageList").toString());
        	 System.out.println(jsonResult.get("PageList").toString());
        	 JSONArray jsonArray= JSONArray.parseArray(jsonResult.get("PageList").toString());
        	 gnoRemote=jsonArray.size()+1;
        }else {
        	String CheckInfos = jsonObject.getString("CheckInfos");
        	return CheckInfos;
        }
        //本地数据库中的最大gno
        long  gnoLocal = 0;
        String maxGno=apprHeadService.getMaxGno();
        if(!maxGno.equals("null")) {
        	 Long.parseLong(maxGno);
        }
       

		//返回接口和数据库中最大的gno
        if (gnoRemote>gnoLocal){
           return String.valueOf(gnoRemote);
        }else{
        	 return String.valueOf(gnoLocal+1);
        }
	}

}
