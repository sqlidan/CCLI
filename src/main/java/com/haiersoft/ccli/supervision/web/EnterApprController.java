package com.haiersoft.ccli.supervision.web;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.*;
import javax.transaction.Transactional;
import javax.xml.rpc.ServiceException;

import com.haiersoft.ccli.supervision.dao.ApprGno2SequenceDao;
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

	@Autowired
	ApprGno2SequenceDao apprGno2SequenceDao;


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

		//2024-11-19 徐峥
		//生成申请单时，校验HS编码是否不一样
		List<BisEnterStockInfo> bisEnterStockInfoList = enterStockInfoService.getList(apprHead.getLinkId());
		if (bisEnterStockInfoList!=null && bisEnterStockInfoList.size() > 0){
			List<String> stringList = new ArrayList<>();
			for (BisEnterStockInfo forBisEnterStockInfo:bisEnterStockInfoList) {
				if (!stringList.contains(forBisEnterStockInfo.getHsCode())){
					stringList.add(forBisEnterStockInfo.getHsCode());
				}
			}
			if (stringList.size()!=1){
				return "入库联系单"+apprHead.getLinkId()+" 明细中的HS编码不存在或不一致";
			}
		}

		// 根据联系单生成申请单head
		BisEnterStock bisEnterStock = enterStockService.get(apprHead.getLinkId());
		List<ApprInfo> apprInfoList = null;
		if (bisEnterStock != null) {
			//判断当前入库联系单生成申请单是否为非保税数据
			if ("1".equals(bisEnterStock.getIfBonded())){
				//保税
				return "当前入库联系单为保税数据，不可申请申请单";
			}

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
//			apprInfoList = copyStockInfo2ApprInfo(bisEnterStockInfoList);//2024-11-19 徐峥注释
			apprInfoList = copyStockInfo2ApprInfo2(bisEnterStockInfoList);//2024-11-19 徐峥编写
			if (apprInfoList != null && apprInfoList.size() > 0) {
				if (apprInfoList.get(0)!=null && apprInfoList.get(0).getgNo()!=null){
					apprHead.setgNo(apprInfoList.get(0).getgNo());
				}
			}
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
			DecimalFormat df = new DecimalFormat("0.00000");//保留小数点后3位，四舍五入
			ai.setGrossWt(df.format(info.getGrossWeight()));
			ai.setCreateTime(date);
			//申请单行生成底账项号
			ai.setgNo(gnoMap.get(info.getCtnNum()+info.getHsCode()+info.getHsItemname()));
			ai.setCtnNum(info.getCtnNum());
			la.add(ai);
		}
		return la;
	}

	//2024-11-19 徐峥 启用
	private List<ApprInfo> copyStockInfo2ApprInfo2(List<BisEnterStockInfo> list) {
		List<ApprInfo> la = new ArrayList<ApprInfo>();
		Map<String,Integer> gnoMap = this.getGnoMap2(list);
		Date date = new Date();
		ApprInfo ai = new ApprInfo();
		String bisInfoId = "";
		for (BisEnterStockInfo forBisEnterStockInfoTemp:list) {
			bisInfoId += String.valueOf(forBisEnterStockInfoTemp.getId())+",";
		}
		if (bisInfoId.length() > 0 && bisInfoId.contains(",")){
			bisInfoId = bisInfoId.substring(0,bisInfoId.length()-1);
		}
		ai.setBisInfoId(bisInfoId);
		ai.setLinkId(list.get(0).getLinkId());
		ai.setApprGNo(1);
		ai.setCodeTs(list.get(0).getHsCode());
		ai.setgName(list.get(0).getHsItemname());
		//规格
		ai.setgModel(list.get(0).getTypeSize());

		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_code", list.get(0).getHsCode()));
		List<BaseHscode> bhs = hscodeService.search(filters);

		if (bhs.size() != 0) {
			ai.setgUnit(bhs.get(0).getItemNum());
		} else {
			ai.setgUnit("035");
		}
		ai.setUnit1(ai.getgUnit());

		Integer piece = 0;
		Double grossWeight = 0.00;
		for (BisEnterStockInfo info : list) {
			piece += (info.getPiece()==null?0:info.getPiece());
			grossWeight += (info.getGrossWeight()==null?0.00:info.getGrossWeight());
		}
		ai.setgQty(String.valueOf(piece));
		DecimalFormat df = new DecimalFormat("0.00000");//保留小数点后3位，四舍五入
		ai.setGrossWt(df.format(grossWeight));
		ai.setQty1(ai.getgQty());
		ai.setCreateTime(date);
		//申请单行生成底账项号
		ai.setgNo(gnoMap.get(list.get(0).getItemNum()));
		ai.setCtnNum(list.get(0).getCtnNum());
		la.add(ai);
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

	/**
	 * 徐峥
	 * 2024-11-19
	 * 根据提单号生成底账项号
	 * 一个提单号对应一个“底账项号”
	 * @param list
	 * @return
	 */
	private Map<String, Integer> getGnoMap2(List<BisEnterStockInfo> list) {
		Map<String, Integer> map = new HashMap<>(16);
		for (BisEnterStockInfo info :list){
			String key = info.getItemNum();
			if(null ==map.get(key)){
				map.put(key,apprGno2SequenceDao.getNextval());
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
