package com.haiersoft.ccli.remoting.hand.steve.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.base.dao.ClientDao;
import com.haiersoft.ccli.base.dao.ExpenseContractDao;
import com.haiersoft.ccli.base.dao.ExpenseSchemeDao;
import com.haiersoft.ccli.base.dao.ExpenseShareDao;
import com.haiersoft.ccli.base.entity.BaseExpenseShare;
import com.haiersoft.ccli.base.entity.ExpenseContract;
import com.haiersoft.ccli.base.entity.ExpenseScheme;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.cost.dao.EnterStevedoringDao;
import com.haiersoft.ccli.cost.dao.OutStevedoringDao;
import com.haiersoft.ccli.cost.entity.BisEnterSteveDoring;
import com.haiersoft.ccli.cost.entity.BisOutSteveDoring;
import com.haiersoft.ccli.cost.service.EnterStevedoringService;
import com.haiersoft.ccli.cost.service.OutStevedoringService;
import com.haiersoft.ccli.wms.dao.ASNDao;
import com.haiersoft.ccli.wms.dao.EnterStockDao;
import com.haiersoft.ccli.wms.dao.LoadingInfoDao;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.TrayInfo;
/**
 * 
 * @author PYL
 * @ClassName: SteveWebService
 * @Description: 作业监控webService接口
 */
@WebService
@Service
public class SteveWebService {
	@Autowired
	private TrayInfoDao trayInfoDao;
	@Autowired
	private ASNDao asnDao;
	@Autowired
	private EnterStockDao enterStockDao;
	//@Autowired
	//private OutStockDao outStockDao;
	@Autowired
	private LoadingInfoDao loadingInfoDao;
	@Autowired
	private ExpenseSchemeDao expenseSchemeDao;
	@Autowired
	private ExpenseContractDao expenseContractDao;
	@Autowired
	private ExpenseShareDao expenseShareDao;
	@Autowired
	private EnterStevedoringDao enterStevedoringDao;
	@Autowired
	private EnterStevedoringService enterStevedoringService;
	@Autowired
	private OutStevedoringDao outStevedoringDao;
	@Autowired
	private OutStevedoringService outStevedoringService;
	@Autowired
	private ClientDao clientDao;
	
	/**
	 * 
	 * @author GLZ
	 * @Description: 根据ASN号获得入库件数及重量
	 * @date 2017年5月8日  17:19:16
	 * @param asn ASN号
	 * @return
	 * @throws
	 */
	public String getTrayByAsn(String asn) {
		Result<Object> result = new Result<Object>();
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_asn", asn));
//		filters.add(new PropertyFilter("NEQS_cargoState", "00"));
		List<TrayInfo> infos = trayInfoDao.find(filters);
		
		if (null != infos && infos.size() > 0) {
			BisAsn asnObj=asnDao.find(asn);
			BisEnterStock enterObj=enterStockDao.findUniqueBy("linkId", asnObj.getLinkId());
			ExpenseScheme exObj=expenseSchemeDao.find(enterObj.getFeeId());
			ExpenseContract conObj=expenseContractDao.find(exObj.getContractId());
			Double piece=0d;
			Double weight=0d;
			for(TrayInfo obj:infos){
				piece += obj.getOriginalPiece();
				weight += BigDecimalUtil.mul(obj.getNetSingle(),obj.getOriginalPiece());
			}
			String[] values = {piece.toString(),weight.toString(),conObj.getIfMan()};
//			Map<String, Object> parm = new HashMap<String, Object>();
			
//			List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
//			filters2.add(new PropertyFilter("EQS_clientSort", "2"));
//			filters2.add(new PropertyFilter("EQI_delFlag", 0));
			List<Map<String,Object>> clientList=clientDao.findZX();
			if(null==conObj.getIfMan() || conObj.getIfMan().equals("1") || (conObj.getIfMan().equals("0") && asnObj.getIfAllow()==1)){
				values[2]="1";
				result.setCode(0);
				result.setMsg("查询成功！");
				result.setList(clientList);
			}else{
				result.setCode(0);
				result.setMsg("查询成功！但需授权后才开填写装卸队！");
				result.setList(new ArrayList<Map<String,Object>>());
			}
			result.setValues(values);	
//			result.setMap(parm);
		} else {
			result.setCode(1);
			result.setMsg("无此ASN的收货记录！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
	
	
	/**
	 * 
	 * @author GLZ
	 * @Description: 根据装车号获得出库件数及重量
	 * @date 2017年5月9日 14:59:16
	 * @param loadingNum 装车号
	 * @return
	 * @throws
	 */
	public String getTrayByLoadingNum(String loadingNum) {
		Result<Object> result = new Result<Object>();
		
		//根据装车号获取已装车的出库装车信息
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("loadingTruckNum", loadingNum);
		params.put("loadingState", "2");
		List<BisLoadingInfo> infos = loadingInfoDao.findBy(params);
		
		if (null != infos && infos.size() > 0) {
//			BisOutStock outObj=outStockDao.find(infos.get(0).getOutLinkId());
//			ExpenseScheme exObj=expenseSchemeDao.find(outObj.getFeeScheme());
// 			ExpenseContract conObj=expenseContractDao.find(exObj.getContractId());
			Double piece=0d;
			Double weight=0d;
			for(BisLoadingInfo obj:infos){
				piece += obj.getPiece();
				weight += obj.getNetWeight();
			}
			String[] values = {piece.toString(),weight.toString(),"1"};
//			Map<String, Object> parm = new HashMap<String, Object>();
			
//			List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
//			filters2.add(new PropertyFilter("EQS_clientSort", "2"));
//			filters2.add(new PropertyFilter("EQI_delFlag", 0));
			List<Map<String,Object>> clientList=clientDao.findZX();
				result.setCode(0);
				result.setMsg("查询成功！");
				result.setList(clientList);
				result.setValues(values);	
		} else {
			result.setCode(1);
			result.setMsg("无此装车单号的装车记录！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
	
	
	/**
	 * 
	 * @author GLZ
	 * @Description: 根据客户ID获得费用方案列表
	 * @date 2017年5月8日  18:19:16
	 * @param clientId clientId
	 * @return
	 * @throws
	 */
	public String getFeeById(String clientId) {
		
		Result<Object> result = new Result<Object>();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
 		filters.add(new PropertyFilter("EQS_customsId", ""+clientId));
		filters.add(new PropertyFilter("EQS_isDel", "0"));
		List<ExpenseScheme> infos = expenseSchemeDao.find(filters);
		//共享合同中的费用方案
		List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
		filters2.add(new PropertyFilter("EQS_programState", "1"));
		filters2.add(new PropertyFilter("EQS_clientId", clientId));
		filters2.add(new PropertyFilter("NEQS_ifParent", "1"));
        List<BaseExpenseShare> expenseShare = expenseShareDao.find(filters2);
        if (!infos.isEmpty()) {
            for (ExpenseScheme obj : infos) {
                BaseExpenseShare expense = new BaseExpenseShare();
                expense.setSchemeNum(obj.getSchemeNum());
                expense.setSchemeName(obj.getSchemeName());
                expenseShare.add(expense);
            }
        }
        
        
		if (!expenseShare.isEmpty()) {
			result.setList(expenseShare);
			result.setCode(0);
			result.setMsg("查询成功！");
		} else {
			result.setCode(1);
			result.setMsg("未查找到对应的费用方案记录！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
	
	
	
	/**
	 * 
	 * @author GLZ
	 * @Description: 入库装卸单添加
	 * @date 2017年5月9日  16:47:16
	 * @param bisEnterSteveDoring 入库装卸实体类
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	public String addEnterSteve(BisEnterSteveDoring bisEnterSteveDoring,List<String> numList,String userName) {
		Result<Object> result = new Result<Object>();
		StringBuffer sb=new StringBuffer();
		Double netWeight=Double.valueOf(numList.get(1));
		System.out.println(netWeight);
		//后台校验 四个数量是否小于（ ASN数量 - 已添加的装卸单数量）
		List<BisEnterSteveDoring> enterList = enterStevedoringDao.findBy("asnId",bisEnterSteveDoring.getAsnId());
		//定义剩余未操作重量
		Double sortingNum = 0.00;
		Double manNum = 0.00;
		Double wrapNum = 0.00;
		Double packNum = 0.00;
		//遍历出已操作重量和
		if(!enterList.isEmpty()){
			int size = enterList.size();
			for(int i=1;i<size;i++){
				BisEnterSteveDoring enterSteveDoring = new BisEnterSteveDoring();
				enterSteveDoring = enterList.get(i);
				sortingNum += enterSteveDoring.getSortingNum();
				manNum += enterSteveDoring.getManNum();
				wrapNum += enterSteveDoring.getWrapNum();
				packNum += enterSteveDoring.getPackNum();
			}
		}//end if
		System.out.println(sortingNum);
		System.out.println(manNum);
		System.out.println(wrapNum);
		System.out.println(packNum);
		sortingNum = netWeight - sortingNum;
		manNum     = netWeight - manNum;
		wrapNum    = netWeight - wrapNum;
		packNum    = netWeight - packNum;
		if(sortingNum < Double.valueOf(numList.get(2))){
			sb.append("分拣数量,");
		}
		if(manNum < Double.valueOf(numList.get(3))){
			sb.append("人工数量,");
		}
		if(wrapNum < Double.valueOf(numList.get(4))){
			sb.append("人工数量,");
		}
		if(packNum < Double.valueOf(numList.get(5))){
			sb.append("打包数量,");
		}
		//droMR droStockInId     droEnterStockTime droStockInName 
		//状态默认为未完成
		bisEnterSteveDoring.setIfOk(0);
		try{
			if(sb.toString().equals("")){
				bisEnterSteveDoring.setIfAllMan(Integer.valueOf(numList.get(0)));
				bisEnterSteveDoring.setNetWeight(Double.valueOf(numList.get(1)));
				bisEnterSteveDoring.setSortingNum(Double.valueOf(numList.get(2)));
				bisEnterSteveDoring.setManNum(Double.valueOf(numList.get(3)));
				bisEnterSteveDoring.setWrapNum(Double.valueOf(numList.get(4)));
				bisEnterSteveDoring.setPackNum(Double.valueOf(numList.get(5)));
				bisEnterSteveDoring.setCreateUser(userName);
				bisEnterSteveDoring.setCreateTime(new Date());
				enterStevedoringService.save(bisEnterSteveDoring);
				result.setCode(0);
				result.setMsg("保存成功！");
			}else{
				result.setCode(1);
				String back=sb.toString().substring(0, sb.toString().length()-1);
				result.setMsg("保存失败！" + back + "超出此ASN下剩余未装卸数量！");
			}
		}catch (Exception e){
			result.setCode(1);
			result.setMsg("保存失败！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
	
	
	/**
	 * 
	 * @author GLZ
	 * @Description: 出库装卸单添加
	 * @date 2017年5月9日  17:21:16
	 * @param bisOutSteveDoring 出库装卸实体类
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	public String addOutSteve(BisOutSteveDoring bisOutSteveDoring,List<String> numList,String userName) {
		Result<Object> result = new Result<Object>();
		StringBuffer sb=new StringBuffer();
		Double netWeight = Double.valueOf(numList.get(1));
		//后台校验 四个数量是否小于（ 装车订单数量 - 已添加的装卸单数量）
		String loadingNum = bisOutSteveDoring.getLoadingNum();
		List<BisOutSteveDoring> outList = outStevedoringDao.findBy("loadingNum", loadingNum);;
		//定义剩余未操作重量
		Double sortingNum = 0.00;
		Double manNum = 0.00;
		Double wrapNum = 0.00;
		Double packNum = 0.00;
		//遍历出已操作重量和
		if(!outList.isEmpty()){
			int size = outList.size();
			for(int i=1;i<size;i++){
				BisOutSteveDoring outSteveDoring = new BisOutSteveDoring();
				outSteveDoring = outList.get(i);
				sortingNum += outSteveDoring.getSortingNum();
				manNum += outSteveDoring.getManNum();
				wrapNum += outSteveDoring.getWrapNum();
				packNum += outSteveDoring.getPackNum();
			}
		}//end if
		sortingNum = netWeight - sortingNum;
		manNum     = netWeight - manNum;
		wrapNum    = netWeight - wrapNum;
		packNum    = netWeight - packNum;
		if(sortingNum < Double.valueOf(numList.get(2))){
			sb.append("分拣数量,");
		}
		if(manNum < Double.valueOf(numList.get(3))){
			sb.append("人工数量,");
		}
		if(wrapNum < Double.valueOf(numList.get(4))){
			sb.append("人工数量,");
		}
		if(packNum < Double.valueOf(numList.get(5))){
			sb.append("打包数量,");
		}
		
		//状态默认为未完成
		bisOutSteveDoring.setIfOk(0);
		try{
			if(sb.toString().equals("")){
				bisOutSteveDoring.setIfAllMan(Integer.valueOf(numList.get(0)));
				bisOutSteveDoring.setNetWeight(Double.valueOf(numList.get(1)));
				bisOutSteveDoring.setSortingNum(Double.valueOf(numList.get(2)));
				bisOutSteveDoring.setManNum(Double.valueOf(numList.get(3)));
				bisOutSteveDoring.setWrapNum(Double.valueOf(numList.get(4)));
				bisOutSteveDoring.setPackNum(Double.valueOf(numList.get(5)));
				bisOutSteveDoring.setCreateUser(userName);
				bisOutSteveDoring.setCreateTime(new Date());
				outStevedoringService.save(bisOutSteveDoring);
				result.setCode(0);
				result.setMsg("保存成功！");
			}else{
				result.setCode(1);
				String back=sb.toString().substring(0, sb.toString().length()-1);
				result.setMsg("保存失败！" + back + "超出此装车单下剩余未装卸数量！");
			}
		}catch (Exception e){
			result.setCode(1);
			result.setMsg("保存失败！");
		}
		String res = JSON.toJSONString(result);
		return res;
		
		
		
	}
}
