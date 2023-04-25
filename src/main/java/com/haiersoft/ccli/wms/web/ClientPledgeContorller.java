package com.haiersoft.ccli.wms.web;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ClientPledgeService;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import com.haiersoft.ccli.base.service.ClientService;
/**
 * ClientPledgeContorller
 * @author pyl
 */
@Controller
@RequestMapping("wms/pledge")
public class ClientPledgeContorller extends BaseController {
	
	@Autowired
	private ClientPledgeService clientPledgeService;
	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private TrayInfoService trayInfoService;
	@Autowired
	private ClientService clientService;
	
	/*跳转列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "wms/pledge/pledge";
	}
	/**
	 * 导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BaseClientPledge enterStock = new BaseClientPledge();
        parameterReflect.reflectParameter(enterStock, request);// 转换对应实体类参数
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        List<BaseClientPledge> enterStockList = clientPledgeService.search(filters);
        ExportParams params = new ExportParams("质押单", "质押单", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, BaseClientPledge.class, enterStockList);
        String formatFileNameP = "质押单" + ".xlsx";
        String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }

	/**
	 * 
	 * @author pyl
	 * @Description: 质押查询
	 * @date 2016年3月18日 下午5:00:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> getData(HttpServletRequest request) {
		String clientName=request.getParameter("filter_LIKES_clientName");//客户id
		String sku=request.getParameter("filter_LIKES_skuId");//sku
		String billNum=request.getParameter("filter_LIKES_billNum");//提单号
		String ctnNum=request.getParameter("filter_LIKES_ctnNum");//箱号
		String warehouseId = request.getParameter("filter_LIKES_warehouseId");//仓库ID
		List<Map<String, Object>> a = clientPledgeService.getTray(clientName, sku, billNum, ctnNum,warehouseId);
		return a;
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 质押管理
	 * @date 2016年3月19日 上午11:00:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="manager", method = RequestMethod.GET)
	public String pledge() {
		return "wms/pledge/pledgeManager";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 撤押管理
	 * @date 2016年3月19日 上午11:00:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="pledgeC", method = RequestMethod.GET)
	public String pledgeC() {
		return "wms/pledge/pledgeCancle";
	}
	
	
	
	
	/**
	 * @author PYL
	 * @Description: 根据SKU号获取货物信息
	 */
	@RequestMapping(value="gettraybysku",method = RequestMethod.POST)
	@ResponseBody
	public String getTrayBySku(HttpServletRequest request, String sku,String clientId,String warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuId", sku);
		params.put("stockIn", clientId);
		params.put("cargoState","01");
		params.put("warehouseId", warehouseId);
		List<TrayInfo> trayInfoList = trayInfoService.findBySku(params);
		if(!trayInfoList.isEmpty()){
			int i =0;
			int size = trayInfoList.size();
			//记录库存表现有件数、总净重
			int nowPiece =0;
			Double nowWeight =0.00;
			//记录质押表质押件数、总净重
			int peldgeNum =0;
			Double peldgeWeight =0.00;
			for(i=0;i<size;i++){
				nowPiece += trayInfoList.get(i).getNowPiece();
				nowWeight += trayInfoList.get(i).getNetWeight();
			}
			Map<String,Object> params2 = new HashMap<String,Object>();
			params2.put("skuId", sku);
			params2.put("client", clientId);
			params2.put("warehouseId", warehouseId);
//			params.put("ptype", 2);
			List<BaseClientPledge> pledgeList= clientPledgeService.findBySku(params2);
			if(!pledgeList.isEmpty()){
				size=pledgeList.size();
				for(i=0;i<size;i++){
					peldgeNum += pledgeList.get(i).getNum();
					peldgeWeight += pledgeList.get(i).getNetWeight();
				}
			}
			String piece = String.valueOf(nowPiece-peldgeNum);
			String weight = String.valueOf(nowWeight-peldgeWeight);
			Double sing= skuInfoService.getSKUInfo(sku).getNetSingle();
			String single = String.valueOf(sing);
			//返回结果用逗号隔开
			return piece+","+weight+","+single;
		}else{
			return "0,0,0";
		}
	}
	
	/**
	 * @author PYL
	 * @Description: 总量质押
	 */
	@RequestMapping(value="zy",method = RequestMethod.POST)
	@ResponseBody
	public String zy(HttpServletRequest request,Double zyPiece,String sku,String clientId,String warehouseId,String warehouse){
		User user = UserUtil.getCurrentUser();
		//生成质押单号
		String codeNum = clientPledgeService.getCodeNum();
		BaseSkuBaseInfo skuBase = skuInfoService.get(sku);
		BaseClientInfo clientInfo = clientService.get(Integer.valueOf(clientId));
		Double single = skuBase.getNetSingle();
        
		BaseClientPledge clientPledge = new BaseClientPledge();
		clientPledge.setCodeNum(codeNum);
		clientPledge.setClient(clientId);
		clientPledge.setClientName(clientInfo.getClientName());
		clientPledge.setSkuId(sku);
		clientPledge.setPname(skuBase.getCargoName());
		clientPledge.setNorms(skuBase.getTypeSize());
        clientPledge.setCtime(new Date());
        clientPledge.setCuser(user.getName());
        clientPledge.setPclass(2);
        clientPledge.setPtype(2);
        clientPledge.setEnterState("0");
        clientPledge.setNum(zyPiece);
        clientPledge.setNetWeight(zyPiece*single);
        clientPledge.setUnit("KG");
        clientPledge.setWarehouseId(warehouseId);
        clientPledge.setWarehouse(warehouse);
        clientPledge.setTrack("0");
        clientPledgeService.save(clientPledge);
        return "success";
        
	}
	
	/**
	 * @author PYL
	 * @Description: 动态质押后台校验
	 */
	@RequestMapping(value="ajaxd",method = RequestMethod.POST)
	@ResponseBody
	public String ajaxd(HttpServletRequest request, String sku,String clientId,Integer piece,String warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuId", sku);
		params.put("stockIn", clientId);
		params.put("cargoState","01");
		params.put("warehouseId", warehouseId);
		List<TrayInfo> trayInfoList = trayInfoService.findBySku(params);
		if(!trayInfoList.isEmpty()){
			int i =0;
			int size = trayInfoList.size();
			//记录库存表现有件数、总净重
			int nowPiece =0;
			//记录质押表质押件数、总净重
			int peldgeNum =0;
			for(i=0;i<size;i++){
				nowPiece += trayInfoList.get(i).getNowPiece();
			}
			Map<String,Object> params2 = new HashMap<String,Object>();
			params2.put("skuId", sku);
			params2.put("client", clientId);
			params2.put("warehouseId", warehouseId);
//			params.put("ptype", 2);
			List<BaseClientPledge> pledgeList= clientPledgeService.findBySku(params2);
			if(!pledgeList.isEmpty()){
				size=pledgeList.size();
				for(i=0;i<size;i++){
					peldgeNum += pledgeList.get(i).getNum();
				}
			}
			Integer  pieceNow = nowPiece-peldgeNum;
			if(piece != pieceNow){
				return "false";
			}else{
				return "success";
			}
		}else{
			return "false";
		}
	}
	
	
	
	/**
	 * @author PYL
	 * @Description: 根据静态条件获取货物信息
	 */
	@RequestMapping(value="gettraybystay",method = RequestMethod.POST)
	@ResponseBody
	public String getTrayByStay(HttpServletRequest request, String sku,String clientId,String billNum,String ctnNum,String warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuId", sku);
		params.put("stockIn", clientId);
		params.put("billNum", billNum);
		params.put("ctnNum", ctnNum);
		params.put("cargoState","01");
		params.put("warehouseId", warehouseId);
		List<TrayInfo> trayInfoList = trayInfoService.findBySku(params);
		if(!trayInfoList.isEmpty()){
			int i =0;
			int size = trayInfoList.size();
			//记录库存表现有件数、总净重
			int nowPiece =0;
			Double nowWeight =0.00;
			//记录质押表质押件数、总净重
			int peldgeNum =0;
			Double peldgeWeight =0.00;
			for(i=0;i<size;i++){
				nowPiece += trayInfoList.get(i).getNowPiece();
				nowWeight += trayInfoList.get(i).getNetWeight();
			}
			Map<String,Object> params2 = new HashMap<String,Object>();
			params2.put("skuId", sku);
			params2.put("client", clientId);
			params2.put("billNum", billNum);
			params2.put("ctnNum", ctnNum);
			params2.put("ptype", 1);
			params2.put("warehouseId", warehouseId);
			List<BaseClientPledge> pledgeList= clientPledgeService.findBySku(params2);
			if(!pledgeList.isEmpty()){
				size=pledgeList.size();
				for(i=0;i<size;i++){
					peldgeNum += pledgeList.get(i).getNum();
					peldgeWeight += pledgeList.get(i).getNetWeight();
				}
			}
			String piece = String.valueOf(nowPiece-peldgeNum);
			String weight = String.valueOf(nowWeight-peldgeWeight);
			Double sing= skuInfoService.getSKUInfo(sku).getNetSingle();
			String single = String.valueOf(sing);
			//返回结果用逗号隔开
			return piece+","+weight+","+single;
		}else{
			return "0,0,0";
		}
	}
	
	/**
	 * @author PYL
	 * @Description: 静态质押后台校验
	 */
	@RequestMapping(value="ajaxj",method = RequestMethod.POST)
	@ResponseBody
	public String ajaxj(HttpServletRequest request, String sku,String clientId,String billNum,String ctnNum,Integer piece,String warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuId", sku);
		params.put("stockIn", clientId);
		params.put("billNum", billNum);
		params.put("ctnNum", ctnNum);
		params.put("cargoState","01");
		params.put("warehouseId", warehouseId);
		List<TrayInfo> trayInfoList = trayInfoService.findBySku(params);
		if(!trayInfoList.isEmpty()){
			int i =0;
			int size = trayInfoList.size();
			//记录库存表现有件数、总净重
			int nowPiece =0;
			//Double nowWeight = 0.00;
			//记录质押表质押件数、总净重
			int peldgeNum =0;
			//Double peldgeWeight = 0.00;
			for(i=0;i<size;i++){
				nowPiece += trayInfoList.get(i).getNowPiece();
			}
			Map<String,Object> params2 = new HashMap<String,Object>();
			params2.put("skuId", sku);
			params2.put("client", clientId);
			params2.put("billNum", billNum);
			params2.put("ctnNum", ctnNum);
			params2.put("ptype", 1);
			params2.put("warehouseId", warehouseId);
			List<BaseClientPledge> pledgeList= clientPledgeService.findBySku(params2);
			if(!pledgeList.isEmpty()){
				size=pledgeList.size();
				for(i=0;i<size;i++){
					peldgeNum += pledgeList.get(i).getNum();
				}
			}
			Integer pieceNow = nowPiece-peldgeNum;
			if(piece != pieceNow){
				return "false";
			}else{
				return "success";
			}
		}else{
			return "false";
		}
	}
	/**
	 * @author PYL
	 * @Description: 静态质押
	 */
	@RequestMapping(value="zyj",method = RequestMethod.POST)
	@ResponseBody
	public String zyj(HttpServletRequest request,Double zyPiece,String sku,String clientId,String billNum,String ctnNum,String warehouseId,String warehouse){
		User user = UserUtil.getCurrentUser();
		//生成质押单号
		String codeNum = clientPledgeService.getCodeNum();
		BaseSkuBaseInfo skuBase = skuInfoService.get(sku);
		BaseClientInfo clientInfo = clientService.get(Integer.valueOf(clientId));
		Double single = skuBase.getNetSingle();
        
		BaseClientPledge clientPledge = new BaseClientPledge();
		clientPledge.setCodeNum(codeNum);
		clientPledge.setClient(clientId);
		clientPledge.setClientName(clientInfo.getClientName());
		clientPledge.setBillNum(billNum);
		clientPledge.setCtnNum(ctnNum);
		clientPledge.setSkuId(sku);
		clientPledge.setPname(skuBase.getCargoName());
		clientPledge.setNorms(skuBase.getTypeSize());
        clientPledge.setCtime(new Date());
        clientPledge.setCuser(user.getName());
        clientPledge.setPclass(2);
        clientPledge.setPtype(1);
        clientPledge.setEnterState("0");
        clientPledge.setNetWeight(zyPiece*single);
        clientPledge.setUnit("KG");
        clientPledge.setNum(zyPiece);
        clientPledge.setWarehouseId(warehouseId);
        clientPledge.setWarehouse(warehouse);
        clientPledge.setTrack("0");
        clientPledgeService.save(clientPledge);
        return "success";
	}
	
   
	/**
	 * @author PYL
	 * @Description: 做静态质押时先判断是否有过动态质押
	 */
	@RequestMapping(value="ifdong",method = RequestMethod.POST)
	@ResponseBody
	public String ifdong(HttpServletRequest request,String sku,String clientId,String warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuId", sku);
		params.put("client", clientId);
		params.put("ptype", 2);
		params.put("warehouseId", warehouseId);
		List<BaseClientPledge> pledgeList= clientPledgeService.findBySku(params);
		if(!pledgeList.isEmpty()){
			Double piece = 0.0;
			for(int i=0;i<pledgeList.size();i++){
				piece += pledgeList.get(i).getNum();
			}
			if(piece != 0){
				return "success";
			}else{
				return "false";
			}
		}else{
			return "false";
		}
	}
//撤押
	
	
	/**
	 * @author PYL
	 * @Description: 根据SKU号获取货物信息(撤押)
	 */
	@RequestMapping(value="gettraybyskuc",method = RequestMethod.POST)
	@ResponseBody
	public String getTrayBySkuC(HttpServletRequest request, String sku,String clientId,String warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuId", sku);
		params.put("client", clientId);
		params.put("ptype", 2);
		params.put("warehouseId", warehouseId);
		List<BaseClientPledge> pledgeList= clientPledgeService.findBySku(params);
		if(!pledgeList.isEmpty()){
			int i =0;
			int size = pledgeList.size();
			//记录质押表质押件数、总净重
			int peldgeNum =0;
			Double peldgeWeight =0.00;
			for(i=0;i<size;i++){
				peldgeNum += pledgeList.get(i).getNum();
				peldgeWeight += pledgeList.get(i).getNetWeight();
			}
			Double sing= skuInfoService.getSKUInfo(sku).getNetSingle();
			String single = String.valueOf(sing);
			//返回结果用逗号隔开
			return peldgeNum+","+peldgeWeight+","+single ;
		}else{
			return "0,0,0";
		}
	}
	
	/**
	 * @author PYL
	 * @Description: 动态撤押后台校验
	 */
	@RequestMapping(value="ajaxcd",method = RequestMethod.POST)
	@ResponseBody
	public String ajaxcd(HttpServletRequest request, String sku,String clientId,Integer piece,String warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuId", sku);
		params.put("client", clientId);
		params.put("ptype", 2);
		params.put("warehouseId", warehouseId);
		List<BaseClientPledge> pledgeList= clientPledgeService.findBySku(params);
		if(!pledgeList.isEmpty()){
			int i =0;
			int size = pledgeList.size();
			//记录质押表质押件数、总净重
			int peldgeNum =0;
			for(i=0;i<size;i++){
				peldgeNum += pledgeList.get(i).getNum();
			}
			if(piece != peldgeNum){
				return "false";
			}else{
				return "success";
			}
		}else{
			return "false";
		}
	}
	/**
	 * @author PYL
	 * @Description: 总量撤押
	 */
	@RequestMapping(value="zyc",method = RequestMethod.POST)
	@ResponseBody
	public String zyC(HttpServletRequest request,Double zyPiece,String sku,String clientId,String warehouseId,String warehouse){
		User user = UserUtil.getCurrentUser();
		//生成质押单号
		String codeNum = clientPledgeService.getCodeNum();
		BaseSkuBaseInfo skuBase = skuInfoService.get(sku);
		BaseClientInfo clientInfo = clientService.get(Integer.valueOf(clientId));
		Double single = skuBase.getNetSingle();
        
		BaseClientPledge clientPledge = new BaseClientPledge();
		clientPledge.setCodeNum(codeNum);
		clientPledge.setClient(clientId);
		clientPledge.setClientName(clientInfo.getClientName());
		clientPledge.setSkuId(sku);
		clientPledge.setPname(skuBase.getCargoName());
		clientPledge.setNorms(skuBase.getTypeSize());
        clientPledge.setCtime(new Date());
        clientPledge.setCuser(user.getName());
        clientPledge.setPclass(2);
        clientPledge.setPtype(2);
        clientPledge.setEnterState("0");
        clientPledge.setNetWeight(0.00-zyPiece*single);
        clientPledge.setUnit("KG");
        clientPledge.setNum(0-zyPiece);
        clientPledge.setWarehouseId(warehouseId);
        clientPledge.setWarehouse(warehouse);
        clientPledge.setTrack("0");
        clientPledgeService.save(clientPledge);
        return "success";
        
	}
	
	
	/**
	 * @author PYL
	 * @Description: 根据静态条件获取货物信息（撤押）
	 */
	@RequestMapping(value="gettraybystayc",method = RequestMethod.POST)
	@ResponseBody
	public String getTrayByStayC(HttpServletRequest request, String sku,String clientId,String billNum,String ctnNum,String warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuId", sku);
		params.put("client", clientId);
		params.put("billNum", billNum);
		params.put("ctnNum", ctnNum);
		params.put("ptype", 1);
		params.put("warehouseId", warehouseId);
		params.put("track", "0");
		List<BaseClientPledge> pledgeList= clientPledgeService.findBySku(params);
		if(!pledgeList.isEmpty()){
			int i =0;
			int size = pledgeList.size();
			//记录质押表质押件数、总净重
			int peldgeNum =0;
			Double peldgeWeight =0.00;
			for(i=0;i<size;i++){
				peldgeNum += pledgeList.get(i).getNum();
				peldgeWeight += pledgeList.get(i).getNetWeight();
			}
			Double sing= skuInfoService.getSKUInfo(sku).getNetSingle();
			String single = String.valueOf(sing);
			//返回结果用逗号隔开
			return peldgeNum+","+peldgeWeight+","+single ;
		}else{
			return "0,0,0";
		}
	}
	
	/**
	 * @author PYL
	 * @Description: 静态撤押后台校验
	 */
	@RequestMapping(value="ajaxcj",method = RequestMethod.POST)
	@ResponseBody
	public String ajaxcj(HttpServletRequest request, String sku,String clientId,String billNum,String ctnNum,Integer piece,String warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuId", sku);
		params.put("client", clientId);
		params.put("billNum", billNum);
		params.put("ctnNum", ctnNum);
		params.put("ptype", 1);
		params.put("warehouseId", warehouseId);
		List<BaseClientPledge> pledgeList= clientPledgeService.findBySku(params);
		if(!pledgeList.isEmpty()){
			int i =0;
			int size = pledgeList.size();
			//记录质押表质押件数、总净重
			int peldgeNum =0;
			for(i=0;i<size;i++){
				peldgeNum += pledgeList.get(i).getNum();
			}
			if(piece != peldgeNum){
				return "false";
			}else{
				return "success";
			}
		}else{
			return "false";
		}
	}
	
	/**
	 * @author PYL
	 * @Description: 静态撤押
	 */
	@RequestMapping(value="zyjc",method = RequestMethod.POST)
	@ResponseBody
	public String zyjC(HttpServletRequest request,Double zyPiece,String sku,String clientId,String billNum,String ctnNum,String warehouseId,String warehouse){
		User user = UserUtil.getCurrentUser();
		//生成质押单号
		String codeNum = clientPledgeService.getCodeNum();
		BaseSkuBaseInfo skuBase = skuInfoService.get(sku);
		BaseClientInfo clientInfo = clientService.get(Integer.valueOf(clientId));
		Double single = skuBase.getNetSingle();
        
		BaseClientPledge clientPledge = new BaseClientPledge();
		clientPledge.setCodeNum(codeNum);
		clientPledge.setClient(clientId);
		clientPledge.setClientName(clientInfo.getClientName());
		clientPledge.setBillNum(billNum);
		clientPledge.setCtnNum(ctnNum);
		clientPledge.setSkuId(sku);
		clientPledge.setPname(skuBase.getCargoName());
		clientPledge.setNorms(skuBase.getTypeSize());
        clientPledge.setCtime(new Date());
        clientPledge.setCuser(user.getName());
        clientPledge.setPclass(2);
        clientPledge.setPtype(1);
        clientPledge.setEnterState("0");
        clientPledge.setNetWeight(0.00-zyPiece*single);
        clientPledge.setUnit("KG");
        clientPledge.setNum(0-zyPiece);
        clientPledge.setWarehouseId(warehouseId);
        clientPledge.setWarehouse(warehouse);
        clientPledge.setTrack("0");
        clientPledgeService.save(clientPledge);
        return "success";
	}
}
