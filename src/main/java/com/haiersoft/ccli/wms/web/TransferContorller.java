package com.haiersoft.ccli.wms.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.haiersoft.ccli.system.entity.Log;
import com.itextpdf.text.Section;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.CreatPDFUtils;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.MyFileUtils;
import com.haiersoft.ccli.common.utils.MyPDFUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.InStockReportService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.wms.entity.BisTransferStock;
import com.haiersoft.ccli.wms.entity.BisTransferStockInfo;
import com.haiersoft.ccli.wms.entity.BisTransferStockTrayInfo;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
import com.haiersoft.ccli.wms.service.OutStockInfoService;
import com.haiersoft.ccli.wms.service.OutStockService;
import com.haiersoft.ccli.wms.service.TransferInfoService;
import com.haiersoft.ccli.wms.service.TransferService;
import com.haiersoft.ccli.wms.service.TransferTrayInfoService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import com.itextpdf.text.PageSize;
/**
 * 货转controller
 * @author lzg
 * @date 2016年4月6日
 */
@Controller
@RequestMapping("bis/transfer")
public class TransferContorller extends BaseController {
	
	@Autowired
	private TransferService transferService;
	@Autowired
	private LoadingOrderService loadingOrderService;//出库订单
	@Autowired
	private TransferTrayInfoService  transferTrayInfoService;//货转托盘明细
	@Autowired
	private OutStockService outStockService;
	@Autowired
	private OutStockInfoService outStockInfoService;
	@Autowired
	private TransferInfoService transferInfoService;
	@Autowired
	private TrayInfoService trayInfoService;
	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private InStockReportService inStockReportService;

	private final static Logger log = Logger.getLogger(String.valueOf(TransferContorller.class));
	/*跳转货转列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "wms/transfer/transferList";
	}
	
	/*
	 * 列表页面table获取json
	 * */
	@RequiresPermissions("bis:transfer:view")
	@RequestMapping(value="listjson",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisTransferStock> page = getPage(request);
 		page.orderBy("operateTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = transferService.search(page, filters);
		return getEasyUIData(page);
	}
	
	/**
	 * 添加货转跳转
	 */
	@RequiresPermissions("bis:transfer:add")
	@RequestMapping(value = "create/{transferId}", method = RequestMethod.GET)
	public String createForm(@PathVariable("transferId") String transferId,Model model) {
		BisTransferStock retObj=null;
		if(transferId!=null && "000000".equals(transferId)){
			retObj=new BisTransferStock();
			retObj.setTransferId(transferService.getTransferId());
		}else{
			retObj=transferService.get(transferId);
		}
		model.addAttribute("obj", retObj);
		model.addAttribute("action", "create");
		return "wms/transfer/transferForm";
	} 
	/**
	 * 打开货转添加明细跳转
	 *  @param linkId 出库联系单id
	 */
	@RequestMapping(value = "openinfos/{clientId}/{ckid}", method = RequestMethod.GET)
	public String openInfosForm(@PathVariable("clientId") String clientId,@PathVariable("ckid") String ckid,Model model) {
		BisLoadingOrder newObj= new BisLoadingOrder();
		if(clientId!=null && !"".equals(clientId) && ckid!=null && !"".equals(ckid) ){
				newObj.setStockIn(clientId);//存货方id
				newObj.setWarehouseId(ckid);//库存id
		}  
		model.addAttribute("order", newObj);
		return "wms/loadingorder/addInfoList";
	}
	/**
	 * 校验库存明细SKU出库数量校验，出库数量>=库存量-质押量
	 * @param order
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "check", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> checkSKUNum(@Valid BisLoadingOrder order, Model model) {
		Map<String,Object> retMap=loadingOrderService.checkSKUNum(order,order.getWarehouseId(),order.getStockIn());
		return retMap;
	}
	/**
	 * 校验库存明细SKU出库数量校验，出库数量>=库存量-质押量
	 * @param order
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "usercheck", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> checkUserNum(@Valid BisLoadingOrder order, Model model) {
		return transferService.checkUserNum(order);
	}
	/**
	 * 添加货转信息
	 */
	@RequiresPermissions("bis:transfer:add")
	@RequestMapping(value ="create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> create(@Valid BisTransferStock transfer, Model model) {
		Map<String,Object> retMap=transferService.saveTransfer(transfer);
		return retMap;
	}
	
	/**
	 * 修改订单跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("bis:transfer:update")
	@RequestMapping(value = "update/{transferId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("transferId") String transferId, Model model) {
		BisTransferStock getObj=transferService.get(transferId);
		if(getObj!=null && "1".equals(getObj.getAuditingState())){
			getObj.setIsEdite(1);
		}
		List<BisTransferStockTrayInfo>  getList=transferTrayInfoService.getTransferStockTrayInfoList(transferId);
		if(getList==null || getList.size()==0){
			getObj.setIsTSpliet(1);//1 保存未拆分（可编辑）
		}else{
			getObj.setIsTSpliet(2);//2 保存已拆分完（不可编辑）
		}
		model.addAttribute("obj", getObj);
		model.addAttribute("action","update");
		return "wms/transfer/transferForm";
	}
	
	/**
	 * 添加货转信息
	 * @throws Exception 
	 */
	@RequiresPermissions("bis:transfer:update")
	@RequestMapping(value ="update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update( @ModelAttribute @RequestBody  BisTransferStock transfer, HttpServletRequest request) throws Exception {
		String operateTime = request.getParameter("operateTime");
		if(operateTime.length()>0){
			String substring = operateTime.substring(0, 19);
			SimpleDateFormat dft=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = dft.parse(substring);
			transfer.setOperateTime(date);
		}else{
			transfer.setOperateTime(new Date());
		}
		Map<String,Object> retMap=transferService.updateTransfer(transfer);
		return retMap;
	}
	
	
	/**
	 * 取消货转明细 
	 * @param transferId
	 * @return
	 */
	@RequestMapping(value ="deltruck/{transferId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deltruck(@PathVariable("transferId") String transferId) {
		Map<String,Object> retMap=new HashMap<String,Object>();
		retMap.put("endstr", "error");
		if(transferId!=null && !"".equals(transferId)){
			retMap=transferService.deltruck(transferId);
		}
		String clintId = transferService.get(transferId).getStockInId();
		if(retMap.get("endstr").toString().equals("success")){
			transferTrayInfoService.delAsnAction(transferId,clintId);
		}
		return retMap;
	}
	/**
	 * 校验库存明细SKU出库数量校验，出库数量>=库存量-质押量
	 * @param order
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "checkstate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> checkState(HttpServletRequest request) {
		String transferId=request.getParameter("transferId");
		Map<String,Object> retMap=new HashMap<String,Object>();
		retMap.put("end", 0);
		if(transferId!=null && !"".equals(transferId)){
			List<BisTransferStockTrayInfo>  getList=transferTrayInfoService.getTransferStockTrayInfoList(transferId);
			if(getList==null || getList.size()==0){
				retMap.put("end",1);//1 保存未拆分（可编辑）
			}else{
				retMap.put("end",2);//2 保存已拆分完（不可编辑）
			}
		}
		return retMap;
	}
	
	
	/**
	 * 删除出库订单
	 * @param id
	 * @return
	 */
	@RequiresPermissions("bis:loading:delete")
	@RequestMapping(value = "delete/{transferId}")
	@ResponseBody
	public String delete(@PathVariable("transferId") String transferId) {
		String retStr="success";
		List<BisTransferStockTrayInfo>  getList=transferTrayInfoService.getTransferStockTrayInfoList(transferId);
		if(getList==null || getList.size()==0){
		//创建过程中可以删除
			retStr=transferService.deleteTransfer(transferId);
		}else{
			retStr="state";
		}
		return retStr;
	}
	
	/**
	 * 制作出库联系单
	 */
	@RequestMapping(value = "createOutstock/{transferId}", method = RequestMethod.GET)
	@ResponseBody
	public String createForm(@PathVariable("transferId") String transferId) {
		User user=UserUtil.getCurrentUser();
		BisOutStock retObj = new BisOutStock();
		BisTransferStock transfer = transferService.get(transferId);
		List<BisTransferStockInfo> infoList = transferInfoService.findList(transferId);
		
		//主表保存
		retObj.setOutLinkId(outStockService.getOutLinkIdToString());
		retObj.setReceiverId(transfer.getReceiver());
		retObj.setReceiver(transfer.getReceiverName());
		retObj.setReceiverLinker(transfer.getReceiverLinker());
		retObj.setStockInId(transfer.getReceiver());
		retObj.setStockIn(transfer.getReceiverName());
		retObj.setOldOwnerId(transfer.getOldOwnerId());
		retObj.setOldOwner(transfer.getOldOwner());
		retObj.setSettleOrgId(transfer.getReceiver());
		retObj.setSettleOrg(transfer.getReceiverName());
		retObj.setSupplyCompany(transfer.getSupplyCompany());
		retObj.setIfClearStore(transfer.getIfClearStore());
		retObj.setCdNum(transfer.getCdNum());
		retObj.setIfSelfCustomsClearance(transfer.getIfSelfCustomsClearance());
		retObj.setRemark(transfer.getRemark());
		retObj.setIfBuyerPay(transfer.getIsBuyFee());
		retObj.setWarehouse(transfer.getWarehouse());
		retObj.setWarehouseId(transfer.getWarehouseId());
		retObj.setOperator(user.getName());
		retObj.setOperateTime(new Date());
		int sign=0;
		//用于最后是否可以保存后再进行保存的存储List
		List<BisOutStockInfo> outStockInfoList = new ArrayList<BisOutStockInfo>();
		//判断是否有出库货物明细
		if(!infoList.isEmpty()){
			Integer piece = 0;
			Integer tnum = 0;
			Integer ttnum = 0;
			BaseSkuBaseInfo skuObj=new BaseSkuBaseInfo();
			for(BisTransferStockInfo info:infoList){
				List<Map<String,Object>> thelist = trayInfoService.findClientTrayList(retObj.getOutLinkId(),transfer.getReceiver(), transfer.getWarehouseId(), "", info.getBillNum(), info.getSku(), info.getCtnNum());
				//找到库存
				if(!thelist.isEmpty() && thelist.size() == 1){
					piece = ((BigDecimal) thelist.get(0).get("NOW_PIECE")).intValue();
					tnum = ((BigDecimal) thelist.get(0).get("TNUM")).intValue();
					ttnum = ((BigDecimal) thelist.get(0).get("TTNUM")).intValue();
					BisOutStockInfo infoObj = new BisOutStockInfo();
					//判断库存是否发生变动
					if(info.getPiece().intValue() < piece-tnum-ttnum){
						infoObj.setOutNum(info.getPiece().intValue());
					}else{
						infoObj.setOutNum(piece-tnum-ttnum);
					}
					infoObj.setOutLinkId(retObj.getOutLinkId());
					infoObj.setBillNum(info.getBillNum());
					infoObj.setCtnNum(info.getCtnNum());
					infoObj.setSkuId(info.getSku());
					if(!StringUtils.isNull(info.getSku())){
						skuObj=skuInfoService.get(info.getSku());
						if(null!=skuObj){
							infoObj.setMscNum(skuObj.getMscNum());
							infoObj.setLotNum(skuObj.getLotNum());
						}
					}
					infoObj.setRkNum(info.getRkNum());
					infoObj.setCargoName(info.getCargoName());
					infoObj.setTypeSize(info.getTypeSize()!=null?info.getTypeSize():" ");
					infoObj.setGrossWeight(info.getGrossWeight());
					infoObj.setNetWeight(info.getNetWeight());
					infoObj.setUnits("1");
					infoObj.setOperator(user.getName());
					infoObj.setOperateTime(new Date());
					infoObj.setRemark(info.getRemark());
					infoObj.setEnterState(info.getEnterState());
					infoObj.setPiece(0);
					outStockInfoList.add(infoObj);
				} 
			}
		}
		if(sign ==0){
			outStockService.save(retObj);
			for(BisOutStockInfo outStockInfo : outStockInfoList){
				outStockInfoService.save(outStockInfo);
			}
		} 
		return retObj.getOutLinkId();
	}
	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 判断货转联系单是否已保存
	 * @date 2016年3月17日 下午7:22:02
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "ifsave/{transferId}", method = RequestMethod.GET)
	@ResponseBody
	public String ifsave(@PathVariable("transferId") String transferId) {
		BisTransferStock transfer = transferService.get(transferId);
		if (null != transfer && !"".equals(transfer)) {
			return "success";
		} else {
			return "false";
		}
	}
	
	
	/**
	 * 跳转到货转出库报告书页面
	 */
	@RequestMapping(value = "toreport", method = RequestMethod.GET)
	public String toReport(Model model) {
		Date now = new Date();
		model.addAttribute("strTime", DateUtils.getDateStart(now));
		model.addAttribute("endTime", DateUtils.getDateEnd(now));
		return "wms/transfer/transferOutReport";
	}
	
	
	/***
	 * 根据查询条件，导出货转出库报告书
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "report")
	@ResponseBody
	public void export(@Valid @ModelAttribute @RequestBody BisTransferStock obj,HttpServletRequest request, HttpServletResponse response)throws Exception {
		if (obj.getNtype() != null && obj.getNtype() > 0) {
			List<Map<String, Object>> getlist = transferService.findRepot(obj.getNtype(),obj.getIfBonded(),obj.getSearchTransferNum());
			String formatFileName="";
			if(obj.getChuru().equals("1")){
				 formatFileName = URLEncoder.encode("入库报告单" + ".xls", "UTF-8");
			}else{
				 formatFileName = URLEncoder.encode("出库报告单" + ".xls", "UTF-8");
			}	
			ExcelUtil excelUtil = new ExcelUtil();
			String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
			String srcPath = null;
			String desPath = null;
			if (filePath == null || "".equals(filePath)) {
				filePath = request.getSession().getServletContext().getRealPath("/");
				if (1 == obj.getNtype()) {
					if(obj.getChuru().equals("1")){
						srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\transferinreport.xls";
					}else{
						srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\transferoutreport.xls";
					}
					
				} else if (2 == obj.getNtype()) {
					if(obj.getChuru().equals("1")){
						srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\transferinreportjp.xls";
					}else{
						srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\transferoutreportjp.xls";
					}
					
				} else {
					if(obj.getChuru().equals("1")){
						srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\transferinreportote.xls";
					}else{
						srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\transferoutreportote.xls";
					}
					
				}
				if(obj.getChuru().equals("1")){
					desPath = filePath + "WEB-INF\\classes\\excelpost\\transferinreport.xls";
				}else{
					desPath = filePath + "WEB-INF\\classes\\excelpost\\transferoutreport.xls";
				}
				
			} else {
				if (1 == obj.getNtype()) {
					if(obj.getChuru().equals("1")){
						srcPath = filePath + "exceltemplate\\transferinreport.xls";
					}else{
						srcPath = filePath + "exceltemplate\\transferoutreport.xls";
					}
					
				} else if (2 == obj.getNtype()) {
					if(obj.getChuru().equals("1")){
						srcPath = filePath + "exceltemplate\\transferinreportjp.xls";
					}else{
						srcPath = filePath + "exceltemplate\\transferoutreportjp.xls";
					}
					
				} else {
					if(obj.getChuru().equals("1")){
						srcPath = filePath + "exceltemplate\\transferinreportote.xls";
					}else{
						srcPath = filePath + "exceltemplate\\transferoutreportote.xls";
					}
					
				}
				if(obj.getChuru().equals("1")){
					desPath = filePath + "excelpost\\transferinreport.xls";
				}else{
					desPath = filePath + "excelpost\\transferoutreport.xls";
				}
				
			}
			excelUtil.setSrcPath(srcPath);
			excelUtil.setDesPath(desPath);
			excelUtil.setSheetName("Sheet1");
			excelUtil.getSheet();
			// 加载数据
			int starRows = 10;// 数据开始填充行数
			int addlank = 0;// 列数添加数
			if (getlist != null && getlist.size() > 0) {
				Map<String, Object> getMap = null;
				Double sumPice = 0d;// 总件数
				Double sumNet = 0d;// 总净重
				Double sumGross = 0d;// 总毛重
				int sumNum = 0;// 记录合计开始列
				for (int i = 0; i < getlist.size(); i++) {
					getMap = getlist.get(i);
					if (getMap != null && getMap.size() > 0) {
						if (2 == obj.getNtype()) {
							addlank = 1;
						}
						if (getMap.get("BILL_NUM") != null && getMap.get("CTN_NUM") != null
								&& getMap.get("SKU_ID") != null) {
							excelUtil.setCellStrValue(starRows + i, 0 + addlank, getMap.get("BILL_NUM").toString());
							excelUtil.setCellStrValue(starRows + i, 1 + addlank, getMap.get("CTN_NUM").toString());
							excelUtil.setCellStrValue(starRows + i, 2 + addlank, getMap.get("SKU_ID").toString());
							excelUtil.setCellStrValue(starRows + i, 3 + addlank,
									getMap.get("CARGO_NAME") != null ? getMap.get("CARGO_NAME").toString() : "");
							/*if (0 == i) {
								BisOutStock outStock = outStockService.get(getMap.get("BILL_NUM").toString());
								if (outStock != null && outStock.getOutLinkId() != null) {
									excelUtil.setCellStrValue(5, 5, "The customer:" + outStock.getStockIn());
									excelUtil.setCellStrValue(6, 5, "The consignee:" + outStock.getReceiver());
								}
							}*/
						} else {
							continue;
						}
						if (1 == obj.getNtype()) {
							if("1".endsWith(obj.getChuru())){
								excelUtil.setCellStrValue(starRows + i, 4 + addlank,getMap.get("START_STORE_DATE")!=null?getMap.get("START_STORE_DATE").toString():(getMap.get("OPERATE_TIME") != null ? getMap.get("OPERATE_TIME").toString() : ""));
							}else{
								excelUtil.setCellStrValue(starRows + i, 4 + addlank,getMap.get("START_STORE_DATE")!=null?DateUtils.formatDateTime2(DateUtils.addDay((Date)getMap.get("START_STORE_DATE"),-1)):(getMap.get("OPERATE_TIME") != null ? DateUtils.formatDateTime2(DateUtils.addDay((Date)getMap.get("OPERATE_TIME"),-1)) : ""));
							}
							excelUtil.setCellDoubleValue(starRows + i, 5 + addlank,
									Double.parseDouble(getMap.get("PIECE") != null ? getMap.get("PIECE").toString() : "0"));
							excelUtil.setCellStrValue(starRows + i, 6 + addlank,
									getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "");
							excelUtil.setCellDoubleValue(starRows + i, 7 + addlank, Double.parseDouble(getMap.get("NET_WEIGHT") != null
									? getMap.get("NET_WEIGHT").toString() : "0"));
							excelUtil.setCellDoubleValue(starRows + i, 8 + addlank, Double.parseDouble(getMap.get("GROSS_WEIGHT") != null
									? getMap.get("GROSS_WEIGHT").toString() : "0"));
							if(obj.getChuru().equals("1")){
								excelUtil.setCellStrValue(starRows + i, 9 + addlank, getMap.get("RECEIVER_NAME") != null
										? getMap.get("RECEIVER_NAME").toString() : "");
							}else{
								excelUtil.setCellStrValue(starRows + i, 9 + addlank, getMap.get("STOCK_IN") != null
										? getMap.get("STOCK_IN").toString() : "");
							}
							sumNum = 5;
						} else if (2 == obj.getNtype()) {
							excelUtil.setCellStrValue(starRows + i, 0,
									getMap.get("RKDH") != null ? getMap.get("RKDH").toString() : "");
							if("1".endsWith(obj.getChuru())){
								excelUtil.setCellStrValue(starRows + i, 4 + addlank,getMap.get("START_STORE_DATE")!=null?getMap.get("START_STORE_DATE").toString():(getMap.get("OPERATE_TIME") != null ? getMap.get("OPERATE_TIME").toString() : ""));
							}else{
								excelUtil.setCellStrValue(starRows + i, 4 + addlank,getMap.get("START_STORE_DATE")!=null?DateUtils.addDay((Date)getMap.get("START_STORE_DATE"),-1).toString():(getMap.get("OPERATE_TIME") != null ? DateUtils.addDay((Date)getMap.get("OPERATE_TIME"),-1).toString() : ""));
							}
							excelUtil.setCellDoubleValue(starRows + i, 5 + addlank,
									Double.parseDouble(getMap.get("PIECE") != null ? getMap.get("PIECE").toString() : "0"));
							excelUtil.setCellStrValue(starRows + i, 6 + addlank,
									getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "");
							excelUtil.setCellDoubleValue(starRows + i, 7 + addlank, Double.parseDouble(getMap.get("NET_WEIGHT") != null
									? getMap.get("NET_WEIGHT").toString() : "0"));
							excelUtil.setCellDoubleValue(starRows + i, 8 + addlank, Double.parseDouble(getMap.get("GROSS_WEIGHT") != null
									? getMap.get("GROSS_WEIGHT").toString() : "0"));
							if(obj.getChuru().equals("1")){
								excelUtil.setCellStrValue(starRows + i, 9 + addlank, getMap.get("RECEIVER_NAME") != null
										? getMap.get("RECEIVER_NAME").toString() : "");
							}else{
								excelUtil.setCellStrValue(starRows + i, 9 + addlank, getMap.get("STOCK_IN") != null
										? getMap.get("STOCK_IN").toString() : "");
							}
							sumNum = 6;
						} else {
							excelUtil.setCellStrValue(starRows + i, 4 + addlank,
									getMap.get("TYPE_SIZE") != null ? getMap.get("TYPE_SIZE").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 5 + addlank,
									getMap.get("PRO_NUM") != null ? getMap.get("PRO_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 6 + addlank,
									getMap.get("LOT_NUM") != null ? getMap.get("LOT_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 7 + addlank,
									getMap.get("MSC_NUM") != null ? getMap.get("MSC_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 8 + addlank,
									getMap.get("MAKE_TIME") != null ? getMap.get("MAKE_TIME").toString() : "");
							if("1".endsWith(obj.getChuru())){
								excelUtil.setCellStrValue(starRows + i, 9 + addlank,getMap.get("START_STORE_DATE")!=null?getMap.get("START_STORE_DATE").toString():(getMap.get("OPERATE_TIME") != null ? getMap.get("OPERATE_TIME").toString() : ""));
							}else{
								excelUtil.setCellStrValue(starRows + i, 9 + addlank,getMap.get("START_STORE_DATE")!=null?DateUtils.addDay((Date)getMap.get("START_STORE_DATE"),-1).toString():(getMap.get("OPERATE_TIME") != null ? DateUtils.addDay((Date)getMap.get("OPERATE_TIME"),-1).toString() : ""));
							}
							excelUtil.setCellStrValue(starRows + i, 10 + addlank,
									getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "");
							excelUtil.setCellDoubleValue(starRows + i, 11 + addlank,Double.parseDouble(getMap.get("PIECE") != null 
									? getMap.get("PIECE").toString() : "0"));
							excelUtil.setCellDoubleValue(starRows + i, 12 + addlank, Double.parseDouble(getMap.get("NET_WEIGHT") != null
									? getMap.get("NET_WEIGHT").toString() : "0"));
							excelUtil.setCellDoubleValue(starRows + i, 13 + addlank, Double.parseDouble(getMap.get("GROSS_WEIGHT") != null
									? getMap.get("GROSS_WEIGHT").toString() : "0"));
						//	excelUtil.setCellStrValue(starRows + i, 14 + addlank,
						//			getMap.get("SALES_NUM") != null ? getMap.get("SALES_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 15 + addlank,
									getMap.get("ORDER_NUM") != null ? getMap.get("ORDER_NUM").toString() : "");
							if(obj.getChuru().equals("1")){
								excelUtil.setCellStrValue(starRows + i, 16 + addlank, getMap.get("RECEIVER_NAME") != null
										? getMap.get("RECEIVER_NAME").toString() : "");
							}else{
								excelUtil.setCellStrValue(starRows + i, 16 + addlank, getMap.get("STOCK_IN") != null
										? getMap.get("STOCK_IN").toString() : "");
							}
							sumNum = 10;
						}

						sumPice += Double.valueOf(
								getMap.get("PIECE") != null ? getMap.get("PIECE").toString() : "0");
						sumNet += Double.valueOf(
								getMap.get("NET_WEIGHT") != null ? getMap.get("NET_WEIGHT").toString() : "0");
						sumGross += Double.valueOf(getMap.get("GROSS_WEIGHT") != null
								? getMap.get("GROSS_WEIGHT").toString() : "0");
					}

				}
				// 添加合计
				if(3 == obj.getNtype()){
					excelUtil.setCellStrValue(starRows + getlist.size(),sumNum,
							"合计/Total:");
					excelUtil.setCellStrValue(starRows + getlist.size(),
							sumNum + 1, String.valueOf(sumPice.longValue()));
				}else{
					excelUtil.setCellStrValue(starRows + getlist.size(),sumNum-1,
							"合计/Total:");
					excelUtil.setCellStrValue(starRows + getlist.size(),
							sumNum , String.valueOf(sumPice.longValue()));
				}
				excelUtil.setCellStrValue(starRows + getlist.size(),
						sumNum + 2, String.valueOf(sumNet));
				excelUtil.setCellStrValue(starRows + getlist.size(),
						sumNum + 3, String.valueOf(sumGross));
			}

			excelUtil.exportToNewFile();
			FileInputStream in = new FileInputStream(new File(desPath));
			int len = 0;
			byte[] buffer = new byte[1024];
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ formatFileName + "\"");// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型
			OutputStream out = response.getOutputStream();
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}

		}
	}
	
	
	/**
	 * PDF 导出
	 * @param obj
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	 	@RequestMapping(value = "reportpdf", method = RequestMethod.POST)
	   	@ResponseBody
	   	public void exportpdf(@Valid @ModelAttribute @RequestBody BisTransferStock obj,HttpServletRequest request, HttpServletResponse response) throws Exception{
	 		if(obj.getNtype()!=null && obj.getNtype()>0){
	       		List<Map<String, Object>> getlist = transferService.findRepot(obj.getNtype(),obj.getIfBonded(),obj.getSearchTransferNum());
	       		String[] headCN={"货转单号","MR/集装箱号","SKU号","货物描述","货转日期","货物状态","件数","总净重(KGS)","总毛重(KGS)"};
	       		String[] headEN={"B/L NO.","MR/CTN NO.","SKU","Description of cargo","Outbound Date","State of cargo","Qty","Net Weight","Gross Weight"};
	       		String pdfTitle = "货转报告书";
	       		if(obj.getChuru().equals("1")){
	       			pdfTitle = "货转报告书（入）";
	       		}else{
	       			pdfTitle = "货转报告书（出）";
	       		}
	       		StringBuffer pdfCN=new StringBuffer();
	            if (1==obj.getNtype()) {
	            	pdfCN.append(""
	            			    +"货转单号"+"                                      "
	            				+"MR/集装箱号"+"                                     "
	            				+"SKU"+"                                                           "
	            				+"货物描述"+"                                                              "
	            				+"货转日期"+"                    "
	            				+"货物状态"+"                     "
	            				+"件数"+"                           "
	            				+"总净重(KGS)"+"           "
	            				+"总毛重(KGS)"+"               "
	            				);
	            }
	            if (2==obj.getNtype()) {
	            	pdfCN.append(""
	            			+"入库号"+"                       "
            			    +"货转单号"+"                                "
            				+"MR/集装箱号"+"                                 "
            				+"SKU"+"                                                    "
            				+"货物描述"+"                                                   "
            				+"货转日期"+"                   "
            				+"货物状态"+"                      "
            				+"件数"+"                     "
            				+"总净重(KGS)"+"           "
            				+"总毛重(KGS)"+"               "
	        				);
	            }
	            if (3==obj.getNtype()) {
	            	pdfCN.append(""
	            			+"订单号"+"         "
            			    +"货转单号"+"                   "
            				+"MR/集装箱号"+"                 "
            				+"SKU"+"                                    "
            				+"货物描述"+"                               "
            				+"规格"+"                 "
            				+"项目号"+"           "
            				+"船名批号"+"            "
            				+"MSC"+"           "
            				+"生产日期"+"      "
            				+"货转日期"+"       "
            				+"货物状态"+"         "
            				+"件数"+"        "
            				+"总净重(KGS)"+"    "
            				+"总毛重(KGS)"+"               "
	        				);
	            }
	       		String path=request.getSession().getServletContext().getRealPath("/");//获取web项目的路径"d://exceltemplate//syshtm.html";
	       		String pathHtml=path+"//osyshtm.html";
	       		String pathPdf=path+"//osyspdf.pdf";
	       		StringBuffer sbHtml=new StringBuffer();
	       		
	       		sbHtml.append("<div  style=\"height:5px;\"></div>");
	       		sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
	       		//填充标题头
	       		sbHtml.append("<tr style=\"height:30px; \">");
	       		if(2==obj.getNtype()){
	       			sbHtml.append("<td class=\"htd\">").append("入库号").append("</td>");
	       		}else if(3==obj.getNtype()){
	       			sbHtml.append("<td class=\"htd\">").append("订单号").append("</td>");
	       		}
	       		for(String lab:headCN){
	       			if("货物描述".equals(lab)){
	            		sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
	            	}else if("SKU号".equals(lab)||"MR/集装箱号".equals(lab)||"货转单号".equals(lab)){
	            		sbHtml.append("<td  width=\"250px\">").append(lab).append("</td>");
	       			}else{
	            		sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
	            	}
	       			if(3==obj.getNtype() && "货物描述".equals(lab)){
	       				sbHtml.append("<td class=\"htd\">").append("规格").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("项目号").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("船名批号").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("生产日期").append("</td>");
	       			}
	       		}
	       		sbHtml.append("</tr>");
	       		sbHtml.append("<tr style=\"height:30px; \">");
	       		if(2==obj.getNtype()){
	       			sbHtml.append("<td class=\"htd\">").append("Inbound No.").append("</td>");
	       		}else if(3==obj.getNtype()){
	       			sbHtml.append("<td class=\"htd\">").append("Odrer No.").append("</td>");
	       		}
	       		for(String lab:headEN){
	       			sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
	       			if(3==obj.getNtype() && "Description of cargo".equals(lab)){
	       				sbHtml.append("<td class=\"htd\">").append("Standard").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("Pro No.").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("Lot No.").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("Prod Date").append("</td>");
	       			}
	       		}
	       		sbHtml.append("</tr>");
	       		int nTJ=5;
	       		String customUser=null;
	       		String consigneeUser=null;
	       		//填充内容
	       		if(getlist!=null && getlist.size()>0){
	       			Double sumPice=0d;
	       			Double sumNet=0d;
	       			Double sumGross=0d;
	       			Map<String,Object> getMap=null;
	       			for(int i=0;i<getlist.size();i++){
	       				getMap=getlist.get(i);
	        			if(getMap!=null && getMap.size()>0){
	        				if(customUser==null || consigneeUser==null){
	        						if(!obj.getSearchTransferNum().contains(",")){
			        					customUser=getMap.get("STOCK_IN")!=null?getMap.get("STOCK_IN").toString():"";
			        					consigneeUser=getMap.get("RECEIVER_NAME")!=null?getMap.get("RECEIVER_NAME").toString():"";
	        						}else{
	        							customUser="";
	        							consigneeUser="";
	        						}
	        				}
		       				sbHtml.append("<tr>");
		       				if(2==obj.getNtype()){
		       					sbHtml.append("<td>").append(getMap.get("RKDH")!=null?getMap.get("RKDH").toString():"").append("</td>");
		    	       			nTJ=6;
		    	       		}else if(3==obj.getNtype()){
		    	       			sbHtml.append("<td>").append(getMap.get("ORDER_NUM")!=null?getMap.get("ORDER_NUM").toString():"").append("</td>");
		    	       		}
		       				sbHtml.append("<td>").append(getMap.get("BILL_NUM")!=null?getMap.get("BILL_NUM").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(getMap.get("CTN_NUM")!=null?getMap.get("CTN_NUM").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(getMap.get("SKU_ID")!=null?getMap.get("SKU_ID").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(getMap.get("CARGO_NAME")!=null?getMap.get("CARGO_NAME").toString().replaceAll("&", "&amp;"):"").append("</td>");
		       				if(3==obj.getNtype()){
		       					sbHtml.append("<td>").append(getMap.get("TYPE_SIZE")!=null?getMap.get("TYPE_SIZE").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("PRO_NUM")!=null?getMap.get("PRO_NUM").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("LOT_NUM")!=null?getMap.get("LOT_NUM").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("MSC_NUM")!=null?getMap.get("MSC_NUM").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("MAKE_TIME")!=null?getMap.get("MAKE_TIME").toString():"").append("</td>");
		       					nTJ=11;
		       				}
		       				if("1".endsWith(obj.getChuru())){
//		       					sbHtml.append("<td>").append(getMap.get("OPERATE_TIME")!=null?getMap.get("OPERATE_TIME").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("START_STORE_DATE")!=null?getMap.get("START_STORE_DATE").toString():(getMap.get("OPERATE_TIME") != null ? getMap.get("OPERATE_TIME").toString() : "")).append("</td>");
							}else{
								sbHtml.append("<td>").append(getMap.get("START_STORE_DATE")!=null?DateUtils.formatDateTime2(DateUtils.addDay((Date)getMap.get("START_STORE_DATE"),-1)):(getMap.get("OPERATE_TIME") != null ? DateUtils.formatDateTime2(DateUtils.addDay((Date)getMap.get("OPERATE_TIME"),-1)) : "")).append("</td>");
							}
		       				sbHtml.append("<td>").append(getMap.get("ENTER_STATE")!=null?getMap.get("ENTER_STATE").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("PIECE")!=null?getMap.get("PIECE").toString():"0"))).append("</td>");
		       				sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("NET_WEIGHT")!=null?getMap.get("NET_WEIGHT").toString():"0"))).append("</td>");
		       				sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("GROSS_WEIGHT")!=null?getMap.get("GROSS_WEIGHT").toString():"0"))).append("</td>");
		       				 
		       				sbHtml.append("</tr>");
		       				sumPice+=Double.valueOf(getMap.get("PIECE")!=null?getMap.get("PIECE").toString():"0");
	        	        	sumNet+=Double.valueOf(getMap.get("NET_WEIGHT")!=null?getMap.get("NET_WEIGHT").toString():"0");
		        			sumGross+=Double.valueOf(getMap.get("GROSS_WEIGHT")!=null?getMap.get("GROSS_WEIGHT").toString():"0");
	       				}
	       			}//end for
	       			 
	       			//添加合计
	       			sbHtml.append("<tr><td class=\"ftd\" colspan=\""+nTJ+"\" style=\"border:0px;height:30px; \"></td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">合计/Total：</td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumPice)).append("</td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumNet)).append("</td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumGross)).append("</td>");
	       			sbHtml.append("</tr>");
	       		}
	       		sbHtml.append("</table>");
	       		sbHtml.append("<table style=\"border-spacing:0px;text-align:center; border-collapse:collapse;font-family:宋体;font-size:12px;width:100%\"><tr>");
	       		@SuppressWarnings("unused")
				User user = UserUtil.getCurrentUser();
	       		sbHtml.append("<td style=\"text-align:right;margin-top:10px;\">").append("操作人员：").append(" PrintTime : &nbsp;").append(DateUtils.getDateTime()).append("</td>");
	       		sbHtml.append("</tr></table>");
	       		MyFileUtils html=new MyFileUtils();
	       		html.setFilePath(pathHtml);
	       		if(obj.getChuru().equals("1")){
	       			html.saveStrToFile(CreatPDFUtils.createPdfHtmlAddRight("货转报告书（入）", "Inbound Report","The consignee ：",consigneeUser, sbHtml.toString()));
	       		}else{
	       			html.saveStrToFile(CreatPDFUtils.createPdfHtmlAddRight("货转报告书（出）", "Outbound Report","The customer ：",customUser, sbHtml.toString()));
	       		}
	       		MyPDFUtils.setsDEST(pathPdf);
	       		MyPDFUtils.setsHTML(pathHtml);
	       		MyPDFUtils.createPdf(PageSize.A3,pdfCN.toString(),pdfTitle);
	       		
	       		//下载操作
	       		FileInputStream in = new FileInputStream(new File(pathPdf));
	        	int len = 0;
	        	byte[] buffer = new byte[1024];
	        	String formatFileName="";
	        	if(obj.getChuru().equals("1")){
	        		formatFileName = URLEncoder.encode("货转报告单（入）" +".pdf","UTF-8");
	        	}else{
	        		formatFileName = URLEncoder.encode("货转报告单（出）" +".pdf","UTF-8");
	        	}
	        	response.setHeader("Content-disposition", "attachment; filename=\"" +formatFileName+"\"");// 设定输出文件头
	        	response.setContentType("application/msexcel");// 定义输出类型
	        	OutputStream out = response.getOutputStream();
	        	while((len = in.read(buffer)) > 0) {
	        		out.write(buffer,0,len);
	        	}
	        	if(null != in){
	        		in.close();
	        	}
	        	if(null != out){
	        		out.close();
	        	}
	       	}
	    }
	 	
	 	/**
	     * 明细校验库存警戒线
	     *
	     * @param user
	     * @param model
	     */
	    @RequestMapping(value = "checkRank", method = RequestMethod.GET)
	    @ResponseBody
	    public String check(String transferId, String type,HttpServletRequest request) {
	    	BisTransferStock transferObj = transferService.get(transferId);
	    	BaseClientInfo client= new BaseClientInfo();
	    	if("1".equals(type)){ //进行货转
	    		client = clientService.get(Integer.valueOf(transferObj.getStockInId()));
	    	}else{   //取消货转
	    		client = clientService.get(Integer.valueOf(transferObj.getReceiverOrgId()));
	    	}
	    	List<BisTransferStockInfo> infos = transferInfoService.findList(transferId);
	    	Double weight=0d;
	    	if(!infos.isEmpty()){
		    	for(BisTransferStockInfo info:infos){
		    		weight += info.getNetWeight();
		    	}
	    	}
	    	if(transferService.checkRank(weight, client)){
	    		return "false";
	    	}else{
	    		return "success";
	    	}
	    }
	    
	    @RequestMapping(value = "jsonReport", method = RequestMethod.GET)
	    @ResponseBody
	    public Map<String, Object> getJsonData(@Valid @ModelAttribute @RequestBody BisTransferStock obj,HttpServletRequest request) {
	    	Page<Stock> page = getPage(request);
	    	page = inStockReportService.getTransferStockStocks(page, obj.getIfBonded(), obj.getSearchTransferNum());
	    	return getEasyUIData(page);
	    }
	    
}
