package com.haiersoft.ccli.wms.web;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.BaseItemname;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.wms.entity.BisTransferStockInfo;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.AsnActionService;
import com.haiersoft.ccli.wms.service.ClientPledgeService;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.wms.service.OutStockInfoService;
import com.haiersoft.ccli.wms.service.TransferInfoService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
/**
 *  库存 controller
 * @author lzg
 * @date 2016年3月1日
 */
@Controller
@RequestMapping("bis/trayinfo")
public class TrayInfoContorller extends BaseController {
	@Autowired
	private TrayInfoService trayInfoService;
	@Autowired
	private ASNService asnService;
	@Autowired
	private AsnActionService asnActionService;
	@Autowired
	private EnterStockService enterStockService;
	@Autowired
	private OutStockInfoService outStockInfoService;
	@Autowired
	private TransferInfoService transferInfoService;
	@Autowired
	private ClientPledgeService clientPledgeService;
	 
	/*
	 * 列表页面table获取json
	 * */
	@RequestMapping(value="ctrayjson",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> getJosnData(HttpServletRequest request) {
		List<Map<String,Object>> getList=null; 
		String getClientId=request.getParameter("clientId")!=null?request.getParameter("clientId").toString():"";//客户id
		String getCKId=request.getParameter("ckId")!=null?request.getParameter("ckId").toString():"";//仓库id
		String getOutCode=request.getParameter("outCode")!=null?request.getParameter("outCode").toString():"";//出库联系单号
		String getOrderCode=request.getParameter("orderCode")!=null?request.getParameter("orderCode").toString():"";//出库联系单号
		String getRk=request.getParameter("rkNum")!=null?request.getParameter("rkNum").toString():"";//入库号
		String getBill=request.getParameter("getBill");//提单号
		String getSku=request.getParameter("getSku");//sku
		String getMr=request.getParameter("getMr");//箱号
		String getName=request.getParameter("getName");//名称
		if(getClientId!=null && !"".equals(getClientId) && getCKId!=null && !"".equals(getCKId) ){
			getList = trayInfoService.findOutClientTrayList(getClientId,getCKId,getOutCode,getOrderCode,getRk,getBill,getSku,getMr,getName);
		}
		if(getList==null){
			getList=new ArrayList<Map<String,Object>>(); 
		}
		return getList;
	}
	
	/*
	 * 列表页面table获取json
	 * */
	@RequestMapping(value="ctrayjson/{sign}",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> getJosnData2(HttpServletRequest request,@PathVariable("sign") Integer sign) {
		List<Map<String,Object>> getList=null; 
		String outLinkId = request.getParameter("outLinkId");
		String getClientId=request.getParameter("clientId");//客户id
		String getCKId=request.getParameter("ckId");//仓库id
		String getRk=request.getParameter("getRk");//入库号
		String getBill=request.getParameter("getBill");//提单号
		String getSku=request.getParameter("getSku");//sku
		String getMr=request.getParameter("getMr");//箱号
		if(getClientId!=null && !"".equals(getClientId)){
			if(sign == 1){
				getList = trayInfoService.findClientTrayList2(outLinkId,getClientId,getCKId,getRk,getBill,getSku,getMr);
			}else{
				getList = trayInfoService.findClientTrayList(outLinkId,getClientId,getCKId,getRk,getBill,getSku,getMr);
			}
			
		}
		if(getList.isEmpty()){
			getList=new ArrayList<Map<String,Object>>(); 
		}
		return getList;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 简易  库存分页查询 -HQL
	 * @date 2016年3月18日 上午9:42:21 
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="jsonPage",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<TrayInfo> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = trayInfoService.search(page, filters);
		return getEasyUIData(page);
	}
	 
	/**
	 * @throws ParseException 
	 * 
	 * @author PYL
	 * @Description: 修改货物入库时间
	 * @date 2016年10月31日
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="changeInbound/{trayId}/{date}",method = RequestMethod.POST)
	@ResponseBody
	public String changeInbound(@PathVariable("trayId") String trayId,@PathVariable("date") Date theDate) throws ParseException {
		TrayInfo obj=trayInfoService.find("trayId", trayId);
		if(obj!=null){
			User user=UserUtil.getCurrentUser();
			//修改库存中的入库时间
			obj.setEnterRemark(user.getName()!=null?user.getName()+"修改了入库时间。原入库时间为："+obj.getEnterStockTime().toString()+",现入库时间为："+theDate.toString():"");
			obj.setEnterStockTime(theDate);
			trayInfoService.save(obj);
			//修改ASN和入库联系单的入库时间
			String asn=obj.getAsn();
			if(!StringUtils.isNull(asn)){
					BisAsn asnObj = asnService.get(asn);
					//获取此ASN在库存表中的所有入库时间
					List<TrayInfo> trayList=trayInfoService.getASNInfoList(asn);
					if(!trayList.isEmpty()){
						//对时间进行排序，取最小的写入ASN中
						if(trayList.size()>1){
							Collections.sort(trayList, new Comparator<TrayInfo>(){
					 			@Override  
					            public int compare(TrayInfo b1, TrayInfo b2) {  
					                if(b1.getEnterStockTime()!=null){
					                	return b1.getEnterStockTime().compareTo(b2.getEnterStockTime());
					                }else{
					                	return 1;
					                }
					            }  
					 		});
						}
						List<Date> dateList = new ArrayList<Date>();
						for(TrayInfo trayObj:trayList){
							dateList.add(trayObj.getEnterStockTime());
						}
						
						asnObj.setInboundTime(dateList.get(0));
						asnService.save(asnObj);
						//重新写入库联系单的入库时间
						BisEnterStock enterObj=enterStockService.find("linkId",asnObj.getLinkId());
						List<Date> rkTimeList=new ArrayList<Date>();
						//入库时间去重
						for(Date rkTime:dateList){
							if(rkTime != null){
								rkTime=DateUtils.dateToDate(rkTime);
								if(!rkTimeList.contains(rkTime)){
									rkTimeList.add(rkTime);
								}
							}
						}
						String finalTime="";
						//入库时间写入入库联系单中
						for(Date enterTime:rkTimeList){
							finalTime += DateUtils.formatDateTime2(enterTime)+",";
						}
						if(!finalTime.equals("")){
							finalTime=finalTime.substring(0, finalTime.length()-1);
						}
						enterObj.setRkTime(finalTime);
						enterStockService.save(enterObj);
					}
			}
			return "success";
		}else{
			return "reset";
		}
	}
	
	/**
	 * @throws ParseException 
	 * 
	 * @author PYL
	 * @Description: 判断在出库联系单中和货转联系单中是否有此货物
	 * @date 2016年11月8日
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="examOutStock",method = RequestMethod.POST)
	@ResponseBody
	public String examOutStock(HttpServletRequest request) throws ParseException {
		String sku = request.getParameter("sku");
		String billNum=request.getParameter("billNum");
		String ctnNum=request.getParameter("ctnNum");
		String trayId=request.getParameter("trayId");
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_billNum", billNum));
		filters.add(new PropertyFilter("EQS_ctnNum", ctnNum));
		filters.add(new PropertyFilter("EQS_skuId", sku));
		//判断出库联系单是否使用此条ASN的货物
		List<BisOutStockInfo> outList=outStockInfoService.findByF(filters);
		//判断货转单是否使用此条ASN的货物
		List<PropertyFilter> filters3 = new ArrayList<PropertyFilter>();
		filters3.add(new PropertyFilter("EQS_billNum", billNum));
		filters3.add(new PropertyFilter("EQS_ctnNum", ctnNum));
		filters3.add(new PropertyFilter("EQS_sku", sku));
		List<BisTransferStockInfo> transferList=transferInfoService.findByF(filters3);
		//判断是否有拆托操作
		TrayInfo tray=trayInfoService.find("trayId", trayId);
		List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
		filters2.add(new PropertyFilter("EQS_client", tray.getStockIn()));
		filters2.add(new PropertyFilter("EQS_skuId", sku));
		List<BaseClientPledge> pledgeList =clientPledgeService.findByF(filters2);
		if(!outList.isEmpty()){
			return "out";
		}else if(!transferList.isEmpty()){
			return "transfer";
		}
//		else if(tray.getRemovePiece()!=0 || !tray.getRemovePiece().equals(0)){
//			return "huozhuan";
//		}
		else if(!pledgeList.isEmpty()){
			return "pledge";
		}else{
			return "success";
		}
	}
	
	/**
	 * @throws ParseException 
	 * 
	 * @author PYL
	 * @Description: 修改货物上架件数
	 * @date 2016年11月8日
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="changePiece/{trayId}/{piece}",method = RequestMethod.POST)
	@ResponseBody
	public String changePiece(HttpServletRequest request,@PathVariable("trayId") String trayId,@PathVariable("piece") Integer piece) throws ParseException {
		TrayInfo obj=trayInfoService.find("trayId", trayId);
		if(obj!=null){
			String sku=request.getParameter("sku");
			//String ctnNum=request.getParameter("ctnNum");
			User user=UserUtil.getCurrentUser();
			//修改库存中的件数
			Integer difference = piece-obj.getNowPiece();
			obj.setEnterRemark2((user.getName()!=null?user.getName():"")+"修改了上架件数。原上架件数为："+obj.getNowPiece().toString()+",现上架件数为："+piece.toString());
			obj.setNowPiece(piece);
			obj.setOriginalPiece(obj.getOriginalPiece()+difference);
			obj.setNetWeight(piece*obj.getNetSingle());
			obj.setGrossWeight(piece*obj.getGrossSingle());
			trayInfoService.save(obj);
			//修改ASN和asn区间表的件数
 			String asn=obj.getAsn();
			//修改ASN区间表
			List<PropertyFilter> actionPf = new ArrayList<PropertyFilter>();
			if(!StringUtils.isNull(asn)){
				actionPf.add(new PropertyFilter("EQS_asn", asn));
			}else{
				actionPf.add(new PropertyFilter("NULLS_asn", asn));
			}
			actionPf.add(new PropertyFilter("EQS_sku", sku));
			actionPf.add(new PropertyFilter("EQS_clientId", obj.getStockIn()));
			actionPf.add(new PropertyFilter("EQS_billCode", obj.getBillNum()));
			List<AsnAction> actionList=asnActionService.getAsnAction(actionPf);
			if(!actionList.isEmpty()){
				AsnAction action=actionList.get(0);
				action.setNum(action.getNum()+difference);
				action.setNetWeight(action.getNum()*obj.getNetSingle());
				action.setGrossWeight(action.getNum()*obj.getGrossSingle());
				asnActionService.update(action);
			}
			return "success";
		}else{
			return "reset";
		}
	}
	/**
	 * 保税转一般贸易
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String updateForm(HttpServletRequest request,Model model) {
		String trayCodes=request.getParameter("trayCode");
		String[] trayCode = trayCodes.split(",");
		Integer js=0;
		
		for(int i=0;i<trayCode.length;i++){
			TrayInfo trayInfo=trayInfoService.find("trayId", trayCode[i]);
			if (trayInfo!=null) {
				js+=trayInfo.getNowPiece();
			}
		}
		TrayInfo trayInfo= new TrayInfo();
		trayInfo.setTrayId(trayCodes);
		trayInfo.setNowPiece(js);
	//	trayInfo.setTrayId(trayCode);
	//	List<String> list=trayInfoService.getTrayInfoByTrayId(trayCode);
		model.addAttribute("trayInfo",trayInfo );
		model.addAttribute("action", "update");
		return "report/stockChange";
	}
	/**
	 * 保税转一般贸易保存
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody TrayInfo trayInfo,Model model) {
		String trayCodes=trayInfo.getTrayId();
		String[] trayCode = trayCodes.split(",");
		TrayInfo trayInfo2 = null;
		User user=UserUtil.getCurrentUser();
		if (trayCode.length>1) {
			for(int i=0;i<trayCode.length;i++){
				trayInfo2 =trayInfoService.find("trayId", trayCode[i]);
				
				 trayInfo2.setIsBonded(trayInfo.getIsBonded()==null?"0":trayInfo.getIsBonded());
				// trayInfo2.setUpdateTime(new Date());
				 trayInfo2.setUploader(user.getName());
				 trayInfo2.setUploadDate(new Date());
				// trayInfo2.setIsAllForward("W");//完全转为一般贸易
				 trayInfoService.update(trayInfo2);
				// trayInfoService.save(rInfo);
				
			}
			
		}else{	
		trayInfo2 =trayInfoService.find("trayId", trayCodes);
		TrayInfo rInfo= new TrayInfo();
	    Double bDouble= Double.valueOf(trayInfo.getQty())/Double.valueOf(trayInfo2.getNowPiece());

		if (trayInfo.getQty()<trayInfo.getNowPiece()) {
    
		    rInfo.setTrayId(trayInfo.getTrayNewCode());
		    rInfo.setCtnNum(trayInfo2.getCtnNum()==null?"":trayInfo2.getCtnNum());;
		    rInfo.setBillNum(trayInfo2.getBillNum()==null?"":trayInfo2.getBillNum());
            rInfo.setContactType(trayInfo2.getContactType()==null?"":trayInfo2.getContactType());
            rInfo.setContactNum(trayInfo2.getContactNum()==null?"":trayInfo2.getContactNum());
            rInfo.setAsn(trayInfo2.getAsn()==null?"":trayInfo2.getAsn());
            rInfo.setSkuId(trayInfo2.getSkuId()==null?"":trayInfo2.getSkuId());
            rInfo.setStockIn(trayInfo2.getStockIn()==null?"":trayInfo2.getStockIn());
            rInfo.setStockName(trayInfo2.getStockName()==null?"":trayInfo2.getStockName());
            rInfo.setBeforeStockIn(trayInfo2.getBeforeStockIn()==null?"":trayInfo2.getBeforeStockIn());
            rInfo.setBeforeStockName(trayInfo2.getBeforeStockName()==null?"":trayInfo2.getBeforeStockName());
            rInfo.setOriginalPiece(trayInfo.getQty());
            rInfo.setNowPiece(trayInfo.getQty()==null?0:trayInfo.getQty());
            rInfo.setRemovePiece(trayInfo.getQty()==null?0:trayInfo.getQty());
            rInfo.setCargoName(trayInfo2.getCargoName()==null?"":trayInfo2.getCargoName());
            rInfo.setCargoType(trayInfo2.getCargoType()==null?"":trayInfo2.getCargoType());
            rInfo.setIfTransfer(trayInfo2.getIfTransfer()==null?"":trayInfo2.getIfTransfer());
            rInfo.setIfSecondEnter(trayInfo2.getIfSecondEnter()==null?"":trayInfo2.getIfSecondEnter());
            rInfo.setNetWeight(trayInfo2.getNetWeight()==null?0:bDouble*trayInfo2.getNetWeight());
		    rInfo.setGrossWeight(trayInfo2.getGrossWeight()==null?0:bDouble*trayInfo2.getGrossWeight());
		    rInfo.setNetSingle(trayInfo2.getNetSingle()==null?0:bDouble*trayInfo2.getNetSingle());
		    rInfo.setGrossSingle(trayInfo2.getGrossSingle()==null?0:bDouble*trayInfo2.getGrossSingle());
		    rInfo.setUnits(trayInfo2.getUnits()==null?"":trayInfo2.getUnits());
		    rInfo.setWarehouseId(trayInfo2.getWarehouseId()==null?"":trayInfo2.getWarehouseId());
		    rInfo.setWarehouse(trayInfo2.getWarehouse()==null?"":trayInfo2.getWarehouse());
		    rInfo.setCargoLocation(trayInfo2.getCargoLocation()==null?"":trayInfo2.getCargoLocation());
		    rInfo.setBuildingNum(trayInfo2.getBuildingNum()==null?"":trayInfo2.getBuildingNum());
		    rInfo.setFloorNum(trayInfo2.getFloorNum()==null?"":trayInfo2.getFloorNum());
		    rInfo.setRoomNum(trayInfo2.getRoomNum()==null?"":trayInfo2.getRoomNum());
		    rInfo.setAreaNum(trayInfo2.getAreaNum()==null?"":trayInfo2.getAreaNum());
		    rInfo.setStoreroomNum(trayInfo2.getStoreroomNum()==null?"":trayInfo2.getStoreroomNum());
		    rInfo.setEnterStockNum(trayInfo2.getEnterStockNum()==null?"":trayInfo2.getEnterStockNum());
		    rInfo.setEnterTallyTime(trayInfo2.getEnterTallyTime()==null?null:trayInfo2.getEnterTallyTime());
		    rInfo.setEnterTallyOperson(trayInfo2.getEnterTallyOperson()==null?"":trayInfo2.getEnterTallyOperson());
		    rInfo.setOutTallyOperson(trayInfo2.getOutTallyOperson()==null?"":trayInfo2.getOutTallyOperson());
		    rInfo.setOutTallyTime(trayInfo2.getOutTallyTime()==null?null:trayInfo2.getOutTallyTime());
		    rInfo.setEnterStockTime(trayInfo2.getEnterStockTime()==null?null:trayInfo2.getEnterStockTime());
		    rInfo.setEnterOperson(trayInfo2.getEnterOperson()==null?"":trayInfo2.getEnterOperson());
		    rInfo.setOutStockTime(trayInfo2.getOutStockTime()==null?null:trayInfo2.getOutStockTime());
		    rInfo.setOutOperson(trayInfo2.getOutOperson()==null?"":trayInfo2.getOutOperson());
		    rInfo.setOutOperateTime(trayInfo2.getOutOperateTime()==null?null:trayInfo2.getOutOperateTime());
		    rInfo.setEnterState(trayInfo2.getEnterState()==null?"":trayInfo2.getEnterState());
		    rInfo.setCargoState(trayInfo2.getCargoState()==null?"":trayInfo2.getCargoState());
		    rInfo.setUpdateTime(new Date());
		    rInfo.setRemark(trayInfo2.getRemark()==null?"":trayInfo2.getRemark());
		    rInfo.setIsTruck(trayInfo2.getIsTruck()==null?"":trayInfo2.getIsTruck());
		    rInfo.setEnterRemark(trayInfo2.getEnterRemark()==null?"":trayInfo2.getEnterRemark());
		    rInfo.setEnterRemark2(trayInfo2.getEnterRemark2()==null?"":trayInfo2.getEnterRemark2());
		    rInfo.setIsBonded(trayInfo.getIsBonded()==null?"0":trayInfo.getIsBonded());
		    rInfo.setUploader(user.getName());
		    rInfo.setUploadDate(new Date());
		     trayInfo2.setNowPiece(trayInfo2.getNowPiece()-trayInfo.getQty());
		     trayInfo2.setRemovePiece(trayInfo2.getRemovePiece()-trayInfo.getQty());
		     trayInfo2.setNetWeight(trayInfo2.getNetWeight()-bDouble*trayInfo2.getNetWeight());
		     trayInfo2.setGrossWeight(trayInfo2.getGrossWeight()-bDouble*trayInfo2.getGrossWeight());
		     trayInfo2.setNetSingle(trayInfo2.getNetSingle()-bDouble*trayInfo2.getNetSingle());
		     trayInfo2.setGrossSingle(trayInfo2.getGrossSingle()-bDouble*trayInfo2.getGrossSingle());
		     trayInfo2.setUploader(user.getName());
		     trayInfo2.setUploadDate(new Date());
			trayInfoService.update(trayInfo2);
			trayInfoService.save(rInfo);
		}else{

			 trayInfo2.setIsBonded(trayInfo.getIsBonded()==null?"0":trayInfo.getIsBonded());
			 trayInfo2.setUploader(user.getName());
			 trayInfo2.setUploadDate(new Date());
			// trayInfo2.setIsAllForward("W");//完全转为一般贸易
			 trayInfoService.update(trayInfo2);
			// trayInfoService.save(rInfo);
			
			
		}
		}
		 
		return "success";
	}
	
	/**
	 * 校验托盘号是否唯一
	 */
	@RequestMapping(value = "checktraynum")
	@ResponseBody
	public String checkItemNum(String trayNewCode) {
		TrayInfo trayInfo = trayInfoService.find("trayId",trayNewCode);
		if (null == trayInfo || "".equals(trayInfo)) {
			return "true";
		}else{
			return "false";
		}
	}
	
	
	
	
}
