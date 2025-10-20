package com.haiersoft.ccli.wms.web;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.service.PreEntryInvtQuery.PreEntryInvtQueryService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.api.service.ApiPledgeService;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BisExpenseSchemeScale;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeScaleService;
import com.haiersoft.ccli.base.service.FeeCodeService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.persistence.PropertyFilter.MatchType;
import com.haiersoft.ccli.common.utils.CreatPDFUtils;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.MyFileUtils;
import com.haiersoft.ccli.common.utils.MyPDFUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.InStockReportService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.wms.entity.BisTransferStock;
import com.haiersoft.ccli.wms.entity.BisTransferStockInfo;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
import com.haiersoft.ccli.wms.service.OutStockInfoService;
import com.haiersoft.ccli.wms.service.OutStockService;
import com.haiersoft.ccli.wms.service.TransferInfoService;
import com.haiersoft.ccli.wms.service.TransferService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import com.itextpdf.text.PageSize;

/**
 * 
 * @author pyl
 * @ClassName: OutStockController
 * @Description: 出库总览Control1ler
 * @date 2016年3月1日 下午5:05:33
 */
@Controller
@RequestMapping("wms/outstock")
public class OutStockController extends BaseController {

	@Autowired
	private LoadingInfoService loadingInfoService;
	@Autowired
	private OutStockService outStockService;
	@Autowired
	private OutStockInfoService outStockInfoService;
	@Autowired
	private ExpenseSchemeScaleService expenseSchemeScaleService;
	@Autowired
	private FeeCodeService feeCodeService;
	@Autowired
	private TransferService transferService;
	@Autowired
	private TrayInfoService trayInfoService;
	@Autowired
	private TransferInfoService transferInfoService;
	@Autowired
	private InStockReportService inStockReportService;
	@Autowired
	private LoadingOrderService loadingOrderService;
	@Autowired
    private StandingBookService standingBookService;
	@Autowired
    private ClientService clientService;
	@Autowired
	private PreEntryInvtQueryService preEntryInvtQueryService;

	/**
	 * 出库联系单总览默认页面
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list() {
		return "wms/outstock/outStock";
	}

	/**
	 * 出库联系单管理默认页面
	 */
	@RequestMapping(value = "manager", method = RequestMethod.GET)
	public String manager(Model model) {
		model.addAttribute("action", "create");
		return "wms/outstock/outStockManager";
	}

	/**
	 * 获取费目分摊表的回显
	 * 
	 * @return list
	 */
	@RequestMapping(value = "gethxfee/{outLinkId}", method = RequestMethod.POST)
	@ResponseBody
	public List<Object> gethxfee(@PathVariable("outLinkId") String outLinkId) {
		List<BisExpenseSchemeScale> expenseList = expenseSchemeScaleService
				.getExpense(outLinkId);
		// 分别为买方、卖方费目名称回显，买方卖方费目代码回显
		String buy = "";
		String sell = "";
		String buyBL = "";
		String sellBL = "";
		for (int i = 0; expenseList!=null&&i<expenseList.size(); i++) {
			BisExpenseSchemeScale expenseScheme=expenseList.get(i);
			if ("0".equals(expenseScheme.getBosSign())) {
				buy += feeCodeService.getFeeCode(expenseScheme.getFeeCode()).getNameC();
				buyBL += expenseScheme.getFeeCode() + ",";
				if ("0".equals(expenseScheme.getIfRatio())) {
					buy += ",";
					buyBL += "0" + ",";
				} else {
					buy += "(按比例)" + ",";
					buyBL += "1" + ",";
				}
			} else {
				sell += feeCodeService.getFeeCode(expenseScheme.getFeeCode()).getNameC();
				sellBL += expenseScheme.getFeeCode() + ",";
				if ("0".equals(expenseScheme.getIfRatio())) {
					sell += ",";
					sellBL += "0" + ",";
				} else {
					sell += "(按比例)" + ",";
					sellBL += "1" + ",";
				}
			}
		}
		List<Object> a = new ArrayList<Object>();
		// 去掉最后的逗号
		if (buy.length() != 0) {
			buy = buy.substring(0, buy.length() - 1);
		}
		a.add(buy);
		if (sell.length() != 0) {
			sell = sell.substring(0, sell.length() - 1);
		}
		a.add(sell);
		if (buyBL.length() != 0) {
			buyBL = buyBL.substring(0, buyBL.length() - 1);
		}
		a.add(buyBL);
		if (sellBL.length() != 0) {
			sellBL = sellBL.substring(0, sellBL.length() - 1);
		}
		a.add(sellBL);
		return a;
	}

	/**
	 * 获取新的出库联系单号
	 * 
	 * @return string
	 */
	@RequestMapping(value = "getnewoutlinkid", method = RequestMethod.POST)
	@ResponseBody
	public String getNewOutLinkId() {
		return outStockService.getOutLinkIdToString();
	}

	/**
	 * @throws ParseException 
	 * 
	 * @author pyl
	 * @Description: 出库联系单总览查询
	 * @date 2016年3月11日 下午5:18:10
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) throws ParseException {
		Page<BisOutStock> page = getPage(request);
		page.orderBy("operateTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest2(request);
		Integer a=10000;
		Integer b=10000;
		String[] aaa = new String[4];
		String[] bbb = new String[4];
		int size = filters.size();
		if (size == 0){
			// 计算 60 天前的日期
			LocalDate sixtyDaysAgoLocalDate = LocalDate.now().minusDays(60);
			// 转换为 Hibernate 支持的 java.util.Date（若实体字段用 Date 类型）
			Date sixtyDaysAgo = Date.from(sixtyDaysAgoLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			PropertyFilter filter = new PropertyFilter("GED_operateTime", sixtyDaysAgo);
			filters.add(filter);
		}
		for(int i=0;i<size;i++){
			PropertyFilter obj = filters.get(i);
			if("NULL".equals(obj.getMatchType().toString())){
				if(obj.getMatchValue().equals("1")){
					obj.setMatchType(MatchType.NNULL);
				}
			}
			if("IN".equals(obj.getMatchType().toString()) && "outLinkId".equals(obj.getPropertyName())){
				String[] billNum = (String[]) obj.getMatchValue();
				for(int j=0;j<billNum.length;j++){
					if(billNum[i].indexOf("wojiubuxinyourenyongzhege")!=-1){
						billNum[i]=billNum[i].replaceAll("wojiubuxinyourenyongzhege", "/");
					}
				}
				obj.setMatchValue(billNum);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("billNum", billNum[0]);
				List<BisOutStockInfo> infoList = outStockInfoService.findonly(params);
				if(!infoList.isEmpty()){
					List<String> outList = new ArrayList<String>();
					for(BisOutStockInfo info:infoList){
						outList.add(info.getOutLinkId());
					}
					String[] aa = (String[]) outList.toArray(new String[outList.size()]);
					obj.setMatchValue(aa);
				}else{
					String[] aa = new String[1];
					aa[0]="000";
					obj.setMatchValue(aa);
				}
			}
			if("IN".equals(obj.getMatchType().toString()) && "ctnNum".equals(obj.getPropertyName())){
				String[] ctnNum = (String[]) obj.getMatchValue();
				for(int j=0;j<ctnNum.length;j++){
					if(ctnNum[i].indexOf("wojiubuxinyourenyongzhege")!=-1){
						ctnNum[i]=ctnNum[i].replaceAll("wojiubuxinyourenyongzhege", "/");
					}
				}
				obj.setMatchValue(ctnNum);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ctnNum", ctnNum[0]);
				List<BisOutStockInfo> infoList = outStockInfoService.findonly(params);
				if(!infoList.isEmpty()){
					List<String> outList = new ArrayList<String>();
					for(BisOutStockInfo info:infoList){
						outList.add(info.getOutLinkId());
					}
					String[] aa = (String[]) outList.toArray(new String[outList.size()]);
					obj.setMatchValue(aa);
					obj.setPropertyName("outLinkId");
				}else{
					String[] aa = new String[1];
					aa[0]="000";
					obj.setMatchValue(aa);
					obj.setPropertyName("outLinkId");
				}
			}
			if( "IN".equals(obj.getMatchType().toString()) && !"outLinkId".equals(obj.getPropertyName())){
				if("outLinkIda".equals(obj.getPropertyName().toString())){
					a=i;
					aaa = (String[])obj.getMatchValue();
				}
				if("outLinkIdb".equals(obj.getPropertyName().toString())){
					b=i;
					bbb = (String[])obj.getMatchValue();
				}
			}
		}
		
		List<Map<String,Object>> loadingInfoList = loadingInfoService.getLoadingTime(aaa,bbb);
		List<String> linkList = new ArrayList<String>();
		for(Map<String,Object> mapObj:loadingInfoList){
			linkList.add( (String)mapObj.get("OUT_LINK_ID") );
		}
		if(linkList.size()>0){
			String[] cc = (String[]) linkList.toArray(new String[linkList.size()]);
			if(a<10000){
				filters.get(a).setMatchValue(cc);
				filters.get(a).setPropertyName("outLinkId");
			}
			if(b<10000){
				filters.get(b).setMatchValue(cc);
				filters.get(b).setPropertyName("outLinkId");
			}
		}else{
			String[] ddd = new String[]{"00"};
			if(a<10000){
				filters.get(a).setMatchValue(ddd);
				filters.get(a).setPropertyName("outLinkId");
			}
			if(b<10000){
				filters.get(b).setMatchValue(ddd);
				filters.get(b).setPropertyName("outLinkId");
			}
		}
		page = outStockService.search(page, filters);
		return getEasyUIData(page);
	}

	/**
	 * @author lv
	 * @Description: 出库联系单查询
	 */
	@RequestMapping(value = "jsonlist", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> getSearchData(HttpServletRequest request) {
		List<Map<String, Object>> getList = new ArrayList<Map<String, Object>>();
		if (request.getParameter("filter_EQS_outLinkId") != null || request.getParameter("filter_EQS_stockIn") != null || request.getParameter("filter_EQS_receiverId") != null) {
			String outNum = request.getParameter("filter_EQS_outLinkId").toString();// 联系单号
			String stockIn = request.getParameter("filter_EQS_stockIn").toString();// 存货客户id
			String receiverId = request.getParameter("filter_EQS_receiverId").toString();// 收货客户id
			getList = outStockService.findList(outNum, stockIn, receiverId);
		}
		return getList;
	}

	/**
	 * @author lv
	 * @Description: 出库联系单查询2
	 */
	@RequestMapping(value = "jsonlist2", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> getSearchData2(HttpServletRequest request,String outId,String receiverNum,String stockNum) {
		List<Map<String, Object>> getList = new ArrayList<Map<String, Object>>();
		if (outId != null || receiverNum != null || stockNum != null) {
			getList = outStockService.findList(outId, stockNum, receiverNum);
		}
		return getList;
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 判断出库联系单是否已保存
	 * @date 2016年3月17日 下午7:22:02
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "ifsave/{outLinkId}", method = RequestMethod.GET)
	@ResponseBody
	public String ifsave(@PathVariable("outLinkId") String outLinkId) {
		BisOutStock outStock = outStockService.get(outLinkId);
		if (outStock != null && !"".equals(outStock)) {
			return "success";
		} else {
			return "false";
		}

	}

	/**
	 * @author pyl
	 * @Description: 添加出库联系单页面跳转
	 * @date 2016年3月15日 下午4:42:15
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(Model model) {
		String outLinkId = outStockService.getOutLinkIdToString();// 入库联系单号
		User user = UserUtil.getCurrentUser();

		BisOutStock enterStock = new BisOutStock();
		enterStock.setOutLinkId(outLinkId);
		enterStock.setOperator(user.getName());
		enterStock.setOperateTime(new Date());

		model.addAttribute("outLinkId", outLinkId);
		model.addAttribute("action", "create");
		return "wms/outstock/outStockManager";
	}

	/**
	 * 
	 * @author pyl
	 * @Description: 出库联系单修改展示
	 * @date 2016年3月15日 下午7:11:02
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "updateoutstock/{outLinkId}", method = RequestMethod.GET)
	public String updateContractForm(Model model,
			@PathVariable("outLinkId") String outLinkId) {
		BisOutStock bisOutStock = outStockService.get(outLinkId);
		model.addAttribute("bisOutStock", bisOutStock);
		model.addAttribute("action", "update");
		return "wms/outstock/outStockManager";
	}

	/**
	 * 出库联系单费目选择回显
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "hxcode", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> hxcode(HttpServletRequest request, String sellList,
			String buyList,String linkId,String sellId,String buyId) {
		StringBuffer sell=new StringBuffer();
		StringBuffer buy=new StringBuffer();
		List<BisOutStockInfo> infoList = outStockInfoService.getList(linkId);
		List<Object> tt = new ArrayList<Object>();
		if (sellList != null && !"".equals(sellList)) {
			String[] sellLis = sellList.split(",");
			for (int i = 0; i < sellLis.length; i++) {
				sell.append(feeCodeService.getFeeCode(sellLis[i]).getNameC());
				i++;
				sell.append("1".equals(sellLis[i])?"(按比例)":"");
				if(i!=sellLis.length-1){
					sell.append(",");
				}
			}
			//保存存货方分摊信息
			List<BisStandingBook> list=standingBookService.getBisStandingBooks(infoList.get(0).getBillNum(),sellLis);
            for (int i = 0; i < list.size(); i++) {
            	BisStandingBook book=list.get(i);
            	List<BisExpenseSchemeScale> ll=expenseSchemeScaleService.getSchemeScale(linkId, book.getFeeCode(), sellId);
            	if(null!=ll&&ll.size()>0){
            		continue;
            	}
            	BisExpenseSchemeScale expenseSchemeScale=new BisExpenseSchemeScale();
            	expenseSchemeScale.setLinkId(linkId);
            	expenseSchemeScale.setCustomsId(sellId);//存货方
            	expenseSchemeScale.setCustomsName(book.getCustomsName());
				expenseSchemeScale.setSchemeName(book.getFeePlan());
				expenseSchemeScale.setEntrySign("2");//出库
				expenseSchemeScale.setFeeCode(book.getFeeCode());
				expenseSchemeScale.setFeeName(book.getFeeName());
				expenseSchemeScale.setIfRatio("1");//按比例
				expenseSchemeScale.setShareSign("0");//未分摊
				expenseSchemeScale.setBosSign("0");//存货方
				expenseSchemeScale.setStandingNum(book.getStandingNum().toString());
				expenseSchemeScale.setBillNum(infoList.get(0).getBillNum());//提单号
				expenseSchemeScale.setOperateTime(new Date());
				expenseSchemeScaleService.save(expenseSchemeScale);
			}
		}
		if (buyList != null && !"".equals(buyList)) {
			String[] buyLis = buyList.split(",");
			for (int j = 0; j < buyLis.length; j++) {
				buy.append(feeCodeService.getFeeCode(buyLis[j]).getNameC());
				j++;
				buy.append("1".equals(buyLis[j])?"(按比例)":"");
				if(j!=buyLis.length-1){
					buy.append(",");
				}
			}
			//保存收货方分摊信息
			List<BisStandingBook> list=standingBookService.getBisStandingBooks(infoList.get(0).getBillNum(),buyLis);
			BaseClientInfo client=clientService.find("ids",Integer.parseInt(buyId));
			for (int i = 0; i < list.size(); i++) {
            	BisStandingBook book=list.get(i);
            	List<BisExpenseSchemeScale> ll=expenseSchemeScaleService.getSchemeScale(linkId, book.getFeeCode(), buyId);
            	if(null!=ll&&ll.size()>0){
            		continue;
            	}
            	BisExpenseSchemeScale expenseSchemeScale=new BisExpenseSchemeScale();
            	expenseSchemeScale.setLinkId(linkId);
            	expenseSchemeScale.setCustomsId(buyId);//收货方
            	expenseSchemeScale.setCustomsName(client.getClientName());
				expenseSchemeScale.setSchemeName(book.getFeePlan());
				expenseSchemeScale.setEntrySign("2");//出库
				expenseSchemeScale.setFeeCode(book.getFeeCode());
				expenseSchemeScale.setFeeName(book.getFeeName());
				expenseSchemeScale.setIfRatio("1");//按比例
				expenseSchemeScale.setShareSign("0");//未分摊
				expenseSchemeScale.setBosSign("1");//收货方
				expenseSchemeScale.setStandingNum(book.getStandingNum().toString());
				expenseSchemeScale.setBillNum(infoList.get(0).getBillNum());//提单号
				expenseSchemeScale.setOperateTime(new Date());
				expenseSchemeScaleService.save(expenseSchemeScale);
			}
		}
		tt.add(sell.toString());
		tt.add(buy.toString());
		return tt;
	}

	/**
	 * 出库联系单保存费目到费目分摊表
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "addexpense", method = RequestMethod.GET)
	@ResponseBody
	public String addExpense(HttpServletRequest request, String sellList,
			String buyList, String linkId, String sellId, String buyId) {
		// 先删除原有的费目分摊的表
		expenseSchemeScaleService.delByLinkId(linkId);
		String[] sellL = sellList.split(",");
		String[] buyL = buyList.split(",");
		BisExpenseSchemeScale expenseSchemeScale = null;
		if (sellList != null && !"".equals(sellList)) {
			for (int i = 0; i < sellL.length; i++) {
				expenseSchemeScale = new BisExpenseSchemeScale();
				// 根据费目代码获得费目名称
				String schemeName = feeCodeService.getName(sellL[i]);
				expenseSchemeScale.setFeeCode(sellL[i]);
				expenseSchemeScale.setSchemeName(schemeName);
				i = i + 1;
				expenseSchemeScale.setIfRatio(sellL[i]);
				expenseSchemeScale.setBosSign("1");
				expenseSchemeScale.setCustomsId(sellId);
				expenseSchemeScale.setEntrySign("2");
				expenseSchemeScale.setLinkId(linkId);
				expenseSchemeScale.setShareSign("0");
				expenseSchemeScaleService.save(expenseSchemeScale);
			}
		}
		if (buyList != null && !"".equals(buyList)) {
			for (int j = 0; j < buyL.length; j++) {
				expenseSchemeScale = new BisExpenseSchemeScale();
				// 根据费目代码获得费目名称
				String schemeName = feeCodeService.getName(buyL[j]);
				expenseSchemeScale.setFeeCode(buyL[j]);
				expenseSchemeScale.setSchemeName(schemeName);
				j = j + 1;
				expenseSchemeScale.setIfRatio(buyL[j]);
				expenseSchemeScale.setBosSign("0");
				expenseSchemeScale.setCustomsId(buyId);
				expenseSchemeScale.setEntrySign("2");
				expenseSchemeScale.setLinkId(linkId);
				expenseSchemeScale.setShareSign("0");
				expenseSchemeScaleService.save(expenseSchemeScale);
			}
		}
		return "success";
	}

	/**
	 * 
	 * @author pyl
	 * @Description: 保存
	 * @date 2016年3月4日 下午5:13:25
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "createoutstock/{state}", method = RequestMethod.POST)
	@ResponseBody
	public String createoutstock(HttpServletRequest request, HttpServletResponse response, @PathVariable("state") String state) {
		BisOutStock outStock = new BisOutStock();
		parameterReflect.reflectParameter(outStock, request);//转换对应实体类参数
		User user = UserUtil.getCurrentUser();

//		//2025-01-21 徐峥 保税校验逻辑部署后，修改校验逻辑注释
//		//2024-12-09 徐峥增加
//		if (outStock.getIfBonded()!=null && "1".equals(outStock.getIfBonded()) && outStock.getCheckListNo()!=null && outStock.getCheckListNo().trim().length() > 0){
//			//核注清单号可能会有多个，用英文分号隔开
//			String[] string = null;
//			if (outStock.getCheckListNo().trim().contains(";")){
//				string = outStock.getCheckListNo().trim().split(";");
//			}else{
//				string = new String[1];
//				string[0] = outStock.getCheckListNo().trim();
//			}
//
//			for (int i = 0; i < string.length; i++) {
//				//依据核注清单号查询核注清单信息是否存在
//				List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();
//				List<PropertyFilter> filters = new ArrayList<>();
//				filters.add(new PropertyFilter("EQS_bondInvtNo", string[i]));
//				filters.add(new PropertyFilter("EQS_synchronization", "1"));
//				bisPreEntryInvtQueryList = preEntryInvtQueryService.search(filters);
//				if (bisPreEntryInvtQueryList==null || bisPreEntryInvtQueryList.size() == 0){
//					return "当前信息为保税出库联系单，需填写有效的的核注清单号。";
//				}else{
//					if (bisPreEntryInvtQueryList.get(0)!=null){
//						bisPreEntryInvtQueryList.get(0).setOutLinkId(outStock.getOutLinkId());
//						preEntryInvtQueryService.merge(bisPreEntryInvtQueryList.get(0));
//					}
//				}
//			}
//		}

		if("0".equals(state)){
			outStock.setPlanFeeState("0");
			outStock.setFinishFeeState("0");
			outStock.setOperateTime(new Date());
		}else{
			BisOutStock outS = outStockService.get(outStock.getOutLinkId());
			outStock.setUpdateTime(new Date());
			outStock.setPlanFeeState(outS.getPlanFeeState());
			outStock.setFinishFeeState(outS.getFinishFeeState());
			outStock.setOperateTime(outS.getOperateTime());
		}
		if(null == outStock.getIfRelease() || "".equals(outStock.getIfRelease())){
			outStock.setIfRelease("1");
		}
		outStock.setOperator(user.getName());
		outStock.setAuditingState("0");
		outStockService.merge(outStock);
		return "success";
	}

	/**
	 * 
	 * @author pyl
	 * @Description: 删除出库联系单 总览
	 * @date 2016年3月11日 下午16:29:50
	 * @param outLinkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "deleteoutstock/{outLinkId}")
	@ResponseBody
	public String deleteoutstock(@PathVariable("outLinkId") String outLinkId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("outLinkId", outLinkId);
		List<BisOutStockInfo> objList=outStockInfoService.findonly(params);
		if(objList.isEmpty()){
			BisOutStock outStock = outStockService.get(outLinkId);
			outStockService.delete(outStock);
		    return "success";
		}else{
			return "false";
		}
	}

	/**
	 * 
	 * @Description: 导出excel
	 * @date 2016年3月15日 下午5:22:55
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping(value = "export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BisOutStock outStock = new BisOutStock();
		parameterReflect.reflectParameter(outStock, request);// 转换对应实体类参数

		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		// PropertyFilter filter = new PropertyFilter("EQS_isDel", "0");
		// filters.add(filter);

		List<BisOutStock> outStockList = outStockService.search(filters);
 		Collections.sort(outStockList, new Comparator<BisOutStock>(){
 			@Override  
            public int compare(BisOutStock b1, BisOutStock b2) {  
                return b2.getOperateTime().compareTo(b1.getOperateTime());  
            }  

 		});
		ExportParams params = new ExportParams("出库联系单", "出库联系单Sheet",ExcelType.XSSF);

		Workbook workbook = ExcelExportUtil.exportExcel(params,BisOutStock.class, outStockList);

		// String formatFileName = URLEncoder.encode("出库联系单" +".xlsx","UTF-8");
		String formatFileNameP = "出库联系单" + ".xlsx";
		String formatFileName = new String(formatFileNameP.getBytes("GB2312"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
		OutputStream os = response.getOutputStream();
		workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}

	/**
	 * 导出OTE PDF（出库日期为计划出库日期），gzq 20160623 Add
	 * @author gzq
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	 	@RequestMapping(value = "exportotepdf")
	   	@ResponseBody
	   	public void exportOtePdf(HttpServletRequest request, HttpServletResponse response) throws Exception{
	 		String linkId=request.getParameter("filter_LIKES_outLinkId");
	 		
	 		if(linkId!=null && linkId!=""){
	       		List<Map<String, Object>> getlist = outStockService.findRepotOTEByOutlinkid(linkId);
	       		String[] headCN={"提单号","MR/集装箱号","SKU号","货物描述","出库日期","货物状态","件数","总净重(KGS)","总毛重(KGS)"};
	       		String[] headEN={"B/L NO.","MR/CTN NO.","SKU","Description of cargo","Outbound Date","State of cargo","Qty","Net Weight","Gross Weight"};
	       		String pdfTitle = "出库报告书";
	            StringBuffer pdfCN=new StringBuffer();
	            	pdfCN.append(""
            			    +"提单号"+"                        "
            				+"MR/集装箱号"+"                       "
            				+"SKU"+"                                    "
            				+"货物描述"+"                                                         "
            				+"规格"+"               "
            				+"项目号"+"                "
            				+"船名批号"+"         "
            				+"MSC"+"                   "
            				+"出库日期"+"         "
            				+"货物状态"+"             "
            				+"件数"+"               "
            				+"总净重(KGS)"+"    "
            				+"总毛重(KGS)"+"               "
	        				);
	       		
	       		String path=request.getSession().getServletContext().getRealPath("/");//获取web项目的路径"d://exceltemplate//syshtm.html";
	       		String pathHtml=path+"//osyshtm.html";
	       		String pathPdf=path+"//osyspdf.pdf";
	       		StringBuffer sbHtml=new StringBuffer();
	       		 
	       		sbHtml.append("<div  style=\"height:5px;\"></div>");
	       		sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
	       		//填充标题头
	       		sbHtml.append("<tr style=\"height:30px; \">");
	       		for(String lab:headCN){
	       			if("货物描述".equals(lab)){
	            		sbHtml.append("<td  width=\"400px\">").append(lab).append("</td>");
	            	}else if("SKU号".equals(lab)||"MR/集装箱号".equals(lab)||"提单号".equals(lab)){
	            		sbHtml.append("<td  width=\"250px\">").append(lab).append("</td>");
	       			}else{
	            		sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
	            	}
	       			if("货物描述".equals(lab)){
	       				sbHtml.append("<td class=\"htd\">").append("规格").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("项目号").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("船名批号").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
	       			}
	       		}
	       		sbHtml.append("</tr>");
	       		sbHtml.append("<tr style=\"height:30px; \">");
	       		for(String lab:headEN){
	       			sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
	       			if("Description of cargo".equals(lab)){
	       				sbHtml.append("<td class=\"htd\">").append("Standard").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("Pro No.").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("Lot No.").append("</td>");
	       				sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
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
	        					customUser=getMap.get("STOCK_NAME")!=null?getMap.get("STOCK_NAME").toString():"";
	        					consigneeUser=getMap.get("RECEIVER_NAME")!=null?getMap.get("RECEIVER_NAME").toString():"";
	        				}
		       				sbHtml.append("<tr>");
		       				sbHtml.append("<td>").append(getMap.get("BILL_NUM")!=null?getMap.get("BILL_NUM").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(getMap.get("CTN_NUM")!=null?getMap.get("CTN_NUM").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(getMap.get("SKU_ID")!=null?getMap.get("SKU_ID").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(getMap.get("CARGO_NAME")!=null?getMap.get("CARGO_NAME").toString():"").append("</td>");
		       					sbHtml.append("<td style=\"text-align:center;\">").append(getMap.get("TYPE_SIZE")!=null?getMap.get("TYPE_SIZE").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("PRO_NUM")!=null?getMap.get("PRO_NUM").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("LOT_NUM")!=null?getMap.get("LOT_NUM").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("MSC_NUM")!=null?getMap.get("MSC_NUM").toString():"").append("</td>");
		       					nTJ=9;
		       				sbHtml.append("<td>").append(getMap.get("LOADING_TIME")!=null?getMap.get("LOADING_TIME").toString():"").append("</td>");
		       				sbHtml.append("<td style=\"text-align:center;\">").append(getMap.get("ENTER_STATE")!=null?getMap.get("ENTER_STATE").toString():"").append("</td>");
		       				sbHtml.append("<td style=\"text-align:center;\">").append(Double.valueOf(getMap.get("PIECE_SUM")!=null?getMap.get("PIECE_SUM").toString():"0").longValue()).append("</td>");
		       				sbHtml.append("<td style=\"text-align:center;\">").append(String.format("%.2f", Double.valueOf(getMap.get("NET_WEIGHT_SUM")!=null?getMap.get("NET_WEIGHT_SUM").toString():"0"))).append("</td>");
		       				sbHtml.append("<td style=\"text-align:center;\">").append(String.format("%.2f", Double.valueOf(getMap.get("GROSS_WEIGHT_SUM")!=null?getMap.get("GROSS_WEIGHT_SUM").toString():"0"))).append("</td>");
		       				 
		       				sbHtml.append("</tr>");
		       				sumPice+=Double.valueOf(getMap.get("PIECE_SUM")!=null?getMap.get("PIECE_SUM").toString():"0");
	        	        	sumNet+=Double.valueOf(getMap.get("NET_WEIGHT_SUM")!=null?getMap.get("NET_WEIGHT_SUM").toString():"0");
		        			sumGross+=Double.valueOf(getMap.get("GROSS_WEIGHT_SUM")!=null?getMap.get("GROSS_WEIGHT_SUM").toString():"0");
	       				}
	       			}//end for
	       			 
	       			//添加合计
	       			sbHtml.append("<tr><td class=\"ftd\" colspan=\""+nTJ+"\" style=\"border:0px;height:30px; \"></td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">合计：</td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(String.valueOf(sumPice.longValue())).append("</td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumNet)).append("</td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumGross)).append("</td>");
	       			sbHtml.append("</tr>");
	       		}
	       		sbHtml.append("</table>");
	       		MyFileUtils html=new MyFileUtils();
	       		html.setFilePath(pathHtml);
//	       		html.saveStrToFile(CreatPDFUtils.createPdfHtmlAddRight("出库报告书", "Outbound Report","The customer ：<br/><br/>The consignee：",customUser+"<br/><br/><br/>"+consigneeUser, sbHtml.toString()));
	       		html.saveStrToFile(CreatPDFUtils.createPdfHtmlOutStock("出库报告书", "Outbound Report","The customer ："+customUser+"<br/><br/>The consignee："+consigneeUser+"", sbHtml.toString()));	       		
	       		MyPDFUtils.setsDEST(pathPdf);
	       		MyPDFUtils.setsHTML(pathHtml);
	       		MyPDFUtils.createPdf(PageSize.A3,pdfCN.toString(),pdfTitle);
	       		
	       		
	       		//下载操作
	       		FileInputStream in = new FileInputStream(new File(pathPdf));
	        	int len = 0;
	        	byte[] buffer = new byte[1024];
	        	String formatFileName = URLEncoder.encode("出库报告单" +".pdf","UTF-8");
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
	 * 
	 * @author pyl
	 * @Description: 审核通过
	 * @date 2016年3月5日 下午2:20:07
	 * @param linkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "passoutstock/{outLinkId}")
	@ResponseBody
	public String passOutStock(@PathVariable("outLinkId") String outLinkId) {
		BisOutStock outStock = outStockService.get(outLinkId);
//		//2024-04-12 黄岛怡之航仓库取消质押数量判断逻辑 需求：韩飞；修改：徐峥
//		//检查出库数量是否大于质押后的可用数量
//		if(!checkPledgeNum(outStock)) {
//			return outLinkId +" 此条记录中的出库数量大于质押后的可用数量，请确认！";
//		}

		outStock.setAuditingState("1");
		outStock.setUpdateTime(new Date());
		if(outStock.getIfBuyerPay().equals("1") && null!=outStock.getStartStoreTiem()){
			outStockService.createSellFee(outStock);
		}
		outStockService.update(outStock);
		return "success";
	}
	
	@Autowired
	ApiPledgeService apiPledgeService;
	//质押后可出库数量检查
	private boolean checkPledgeNum(BisOutStock outStock) {
		// TODO Auto-generated method stub
		//返回false表示出库数量大于可用数量
		Integer customerId = Integer.valueOf(outStock.getStockInId());

		List<Map<String, Object>> counts = outStockInfoService.countsByClasstype(outStock.getOutLinkId());
		for(Map<String, Object> map:counts) {
			Integer itemClass = Integer.valueOf(map.get("CLASS_TYPE").toString());
			Integer sumNum = Integer.valueOf(map.get("COUNTNUM").toString());
			Integer avliableNum = apiPledgeService.pledgeNumCount(customerId,itemClass);
			if(sumNum>avliableNum) {
				return false;
			}

		}
		return true;
	}

	/**
	 * 
	 * @author pyl
	 * @Description: 取消审核
	 * @date 2016年3月5日 下午2:20:07
	 * @param linkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "nopassoutstock/{outLinkId}")
	@ResponseBody
	public String nopassOutStock(@PathVariable("outLinkId") String outLinkId) {
		BisOutStock outStock = outStockService.get(outLinkId);
		outStock.setAuditingState("0");
		outStock.setUpdateTime(new Date());
		
		outStockService.update(outStock);
		return "success";
	}

	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 计划费用完成
	 * @param outLinkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "planok/{outLinkId}")
	@ResponseBody
	public String planOk(@PathVariable("outLinkId") String outLinkId) {
		BisOutStock outStock = outStockService.get(outLinkId);
		outStock.setPlanFeeState("1");
		outStock.setUpdateTime(new Date());
		outStockService.update(outStock);
		return "success";
	}

	/**
	 * 
	 * @author pyl
	 * @Description: 计划费用取消
	 * @param outLinkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "planno/{outLinkId}")
	@ResponseBody
	public String planNo(@PathVariable("outLinkId") String outLinkId) {
		BisOutStock outStock = outStockService.get(outLinkId);
		outStock.setPlanFeeState("0");
		outStock.setUpdateTime(new Date());
		outStockService.update(outStock);
		return "success";
	}

	/**
	 * 
	 * @author pyl
	 * @Description: 费用完成
	 * @param outLinkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "finishok/{outLinkId}")
	@ResponseBody
	public String finishOk(@PathVariable("outLinkId") String outLinkId) {
		BisOutStock outStock = outStockService.get(outLinkId);
		outStock.setFinishFeeState("1");
		outStock.setUpdateTime(new Date());
		outStockService.update(outStock);
		return "success";
	}

	/**
	 * 
	 * @author pyl
	 * @Description: 费用取消
	 * @param outLinkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "finishno/{outLinkId}")
	@ResponseBody
	public String finishNo(@PathVariable("outLinkId") String outLinkId) {
		BisOutStock outStock = outStockService.get(outLinkId);
		outStock.setFinishFeeState("0");
		outStock.setUpdateTime(new Date());
		outStockService.update(outStock);
		return "success";
	}

	// /**
	// *
	// * @author pyl
	// * @Description: 取消审核通过
	// * @date 2016年3月5日 下午2:20:07
	// * @param linkId
	// * @return
	// * @throws
	// */
	// @RequestMapping(value = "noPassEnterStock/{linkId}")
	// @ResponseBody
	// public String noPassEnterStock(@PathVariable("linkId") String linkId) {
	// User user = UserUtil.getCurrentUser();
	//
	// BisEnterStock enterStock = enterStockService.get(linkId);
	// enterStock.setAuditingState(1);
	// enterStock.setOperator(user.getName());
	// enterStock.setOperateTime(new Date());
	// enterStock.setExaminePerson(user.getName());
	// enterStock.setExamineTime(new Date());
	//
	// enterStockService.update(enterStock);
	// return "success";
	// }

	/**
	 * 出库联系单打印跳转
	 * 
	 * @param linkId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "print/{outLinkId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("outLinkId") String outLinkId,Model model) {
		BisOutStock outStock = outStockService.get(outLinkId);
		List<BisOutStockInfo> outStockInfoList = outStockInfoService.getList(outLinkId);
		model.addAttribute("bisOutStock", outStock);
		BisOutStockInfo outStockInfo = null;
		if (!outStockInfoList.isEmpty()) {
			int l = outStockInfoList.size();
			model.addAttribute("infoSize", l);
			for (int i = 0; i < l; i++) {
				outStockInfo = new BisOutStockInfo();
				outStockInfo = outStockInfoList.get(i);
				model.addAttribute("bisEnterStockInfo" + i, outStockInfo);
			}
		} else {
			BisOutStockInfo enterStockInfoN = new BisOutStockInfo();
			model.addAttribute("bisEnterStockInfo", enterStockInfoN);
		}
		model.addAttribute("action", "print");
		return "wms/outstock/outStockPrint";
	}

	/**
	 * 添加货转跳转
	 */
	@RequestMapping(value = "createTransfer/{outLinkId}", method = RequestMethod.GET)
	@ResponseBody
	public String createForm(@PathVariable("outLinkId") String outLinkId) {
		User user=UserUtil.getCurrentUser();
		BisTransferStock retObj = new BisTransferStock();
		BisOutStock outStock = outStockService.get(outLinkId);
		List<BisOutStockInfo> infoList = outStockInfoService.getList(outLinkId);
		
		//主表保存
		retObj.setTransferId(transferService.getTransferId());
		retObj.setReceiver(outStock.getReceiverId());
		retObj.setReceiverName(outStock.getReceiver());
		retObj.setReceiverLinker(outStock.getReceiverLinker());
		retObj.setStockInId(outStock.getStockInId());
		retObj.setStockIn(outStock.getStockIn());
		retObj.setOldOwnerId(outStock.getOldOwnerId());
		retObj.setOldOwner(outStock.getOldOwner());
		retObj.setReceiverOrgId(outStock.getSettleOrgId());
		retObj.setReceiverOrg(outStock.getSettleOrg());
		retObj.setSupplyCompany(outStock.getSupplyCompany());
		retObj.setIfClearStore(outStock.getIfClearStore());
		retObj.setCdNum(outStock.getCdNum());
		retObj.setIfSelfCustomsClearance(outStock.getIfSelfCustomsClearance());
		retObj.setRemark(outStock.getRemark());
		retObj.setIsBuyFee(outStock.getIfBuyerPay());
		retObj.setWarehouse(outStock.getWarehouse());
		retObj.setWarehouseId(outStock.getWarehouseId());
		retObj.setOperator(user.getName());
		retObj.setOperateTime(new Date());
		int sign=0;
		//用于最后是否可以保存后再进行保存的存储List
		List<BisTransferStockInfo> transferInfoList = new ArrayList<BisTransferStockInfo>();
		//判断是否有出库货物明细
		if(!infoList.isEmpty()){
			Integer piece = 0;
			Integer tnum = 0;
			Integer ttnum = 0;
			for(BisOutStockInfo info:infoList){
				List<Map<String,Object>> thelist = trayInfoService.findClientTrayList(outLinkId,outStock.getStockInId(), outStock.getWarehouseId(), "", info.getBillNum(), info.getSkuId(), info.getCtnNum());
				//找到库存
				if(!thelist.isEmpty() && thelist.size() == 1){
					piece = ((BigDecimal) thelist.get(0).get("NOW_PIECE")).intValue();
					tnum = ((BigDecimal) thelist.get(0).get("TNUM")).intValue();
					ttnum = ((BigDecimal) thelist.get(0).get("TTNUM")).intValue();
					BisTransferStockInfo infoObj = new BisTransferStockInfo();
					//判断库存是否发生变动
					if(info.getOutNum()< piece-tnum-ttnum){
						infoObj.setPiece(Double.valueOf(info.getOutNum()));
					}else{
						infoObj.setPiece(Double.valueOf( piece-tnum-ttnum));
					}
					infoObj.setTransferId(retObj.getTransferId());
					infoObj.setBillNum(info.getBillNum());
					infoObj.setCtnNum(info.getCtnNum());
					infoObj.setSku(info.getSkuId());
					infoObj.setRkNum(info.getRkNum());
					infoObj.setCargoName(info.getCargoName());
					if(null!=info.getTypeSize()){
						infoObj.setTypeSize(info.getTypeSize());
					}
					infoObj.setGrossWeight(info.getGrossWeight());
					infoObj.setNetWeight(info.getNetWeight());
					infoObj.setUntis("1");
					infoObj.setCrUser(user.getName());
					infoObj.setCrTime(new Date());
					infoObj.setRemark(info.getRemark());
					infoObj.setEnterState(info.getEnterState());
					transferInfoList.add(infoObj);
				} 
			}
		}
		if(sign ==0){
			transferService.save(retObj);
			for(BisTransferStockInfo transferInfo :transferInfoList){
				transferInfoService.save(transferInfo);
			}
		} 
		return retObj.getTransferId();
	}
    
	@RequestMapping(value = "checkBillNum", method = RequestMethod.POST)
	@ResponseBody
	public String checkBillNum(@RequestParam String outLinkId) {
		List<BisOutStockInfo> list=outStockInfoService.getList(outLinkId);
		if(null==list||list.size()==0){
			return "请先维护出库联系单明细,才能进行费用分摊！";
		}
		String bill_num=list.get(0).getBillNum();
		for (int i = 0; i <list.size(); i++) {
			BisOutStockInfo info=list.get(i);
			if(!bill_num.equals(info.getBillNum())){
				return "费用分摊只适应单提单，多提单请人工进行费用分摊！";
			}
		}
		return "success";
	}
	/**
	 * 获取费用选择
	 * 
	 */
	@RequestMapping(value = "getfee/{outLinkId}", method = RequestMethod.GET)
	public String getFee(@PathVariable("outLinkId") String outLinkId,Model model) {
		List<BisOutStockInfo> infoList = outStockInfoService.getList(outLinkId);
		List<BisStandingBook> list=standingBookService.getBisStandingBooks(infoList.get(0).getBillNum(),null);
		//List<FeeCode> buyFeeList = feeCodeService.getBuyFee();
		//List<FeeCode> sellFeeList = feeCodeService.getSellFee();
		Integer buyNum = list.size();
		Integer sellNum =list.size();
		model.addAttribute("buyFeeList", list);
		model.addAttribute("sellFeeList", list);
		model.addAttribute("buyNum", buyNum);
		model.addAttribute("sellNum", sellNum);
		return "wms/outstock/getFee";
	}

	/**
	 * 跳转到出库报告书页面
	 */
	@RequestMapping(value = "toreport", method = RequestMethod.GET)
	public String toReport(Model model) {
		Date now = new Date();
		model.addAttribute("strTime", DateUtils.getDateStart(now));
		model.addAttribute("endTime", DateUtils.getDateEnd(now));
		return "wms/outstock/outStockReport";
	}

	/***
	 * 根据查询条件，导出出库报告书
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "report")
	@ResponseBody
	public void export(@Valid @ModelAttribute @RequestBody BisOutStock obj,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (obj.getNtype() != null && obj.getNtype() > 0) {
			List<Map<String, Object>> getlist = outStockService.findRepot(obj.getNtype(),obj.getSearchBillNum(),obj.getSearchItemNum(),
					obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchStrTime(),
					obj.getSearchEndTime(),obj.getIfBonded());
			String formatFileName = URLEncoder.encode("出库报告单" + ".xls", "UTF-8");
			ExcelUtil excelUtil = new ExcelUtil();
			String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
			String srcPath = null;
			String desPath = null;
			if (filePath == null || "".equals(filePath)) {
				filePath = request.getSession().getServletContext().getRealPath("/");
				if (1 == obj.getNtype()) {
					srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\outreport.xls";
				} else if (2 == obj.getNtype()) {
					srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\outreportjp.xls";
				} else {
					srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\outreportote.xls";
				}
				desPath = filePath + "WEB-INF\\classes\\excelpost\\outreport.xls";
			} else {
				if (1 == obj.getNtype()) {
					srcPath = filePath + "exceltemplate\\outreport.xls";
				} else if (2 == obj.getNtype()) {
					srcPath = filePath + "exceltemplate\\outreportjp.xls";
				} else {
					srcPath = filePath + "exceltemplate\\outreportote.xls";
				}
				desPath = filePath + "excelpost\\outreport.xls";
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
						if (getMap.get("BILL_NUM") != null && getMap.get("CTN_NUM") != null
								&& getMap.get("SKU_ID") != null) {
							if(2!=obj.getNtype()){
								excelUtil.setCellStrValue(starRows + i, 0 + addlank, getMap.get("BILL_NUM").toString());
							}else{
								excelUtil.setCellStrValue(starRows + i, 0 + addlank,getMap.get("RK_NUM") != null ? getMap.get("RK_NUM").toString() : "");
							}
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
							excelUtil.setCellStrValue(5,6,getMap.get("STOCK_NAME") != null ? "存货方："+getMap.get("STOCK_NAME").toString() : "");
							excelUtil.setCellStrValue(6,6,getMap.get("RECEIVER_NAME") != null ? "收货方："+getMap.get("RECEIVER_NAME").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 4 + addlank,
									getMap.get("LOADING_TRUCK_NUM") != null ? getMap.get("LOADING_TRUCK_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 5 + addlank,
									getMap.get("LOADING_TIME") != null ? getMap.get("LOADING_TIME").toString() : "");
							excelUtil.setCellDoubleValue(starRows + i, 6 + addlank,
									Double.parseDouble(getMap.get("PIECE_SUM") != null ? getMap.get("PIECE_SUM").toString() : "0"));
							excelUtil.setCellStrValue(starRows + i, 7 + addlank,
									getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "");
							excelUtil.setCellDoubleValue0215(starRows + i, 8 + addlank, Double.parseDouble(getMap.get("NET_WEIGHT_SUM") != null
									? getMap.get("NET_WEIGHT_SUM").toString() : "0"));
							excelUtil.setCellDoubleValue0215(starRows + i, 9 + addlank, Double.parseDouble(getMap.get("GROSS_WEIGHT_SUM") != null
									? getMap.get("GROSS_WEIGHT_SUM").toString() : "0"));
							sumNum = 5;
						} else if (2 == obj.getNtype()) {
							excelUtil.setCellStrValue(5,6,getMap.get("STOCK_NAME") != null ? "存货方："+getMap.get("STOCK_NAME").toString() : "");
							excelUtil.setCellStrValue(6,6,getMap.get("RECEIVER_NAME") != null ? "收货方："+getMap.get("RECEIVER_NAME").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 4 + addlank,
									getMap.get("LOADING_TRUCK_NUM") != null ? getMap.get("LOADING_TRUCK_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 5 + addlank,
									getMap.get("LOADING_TIME") != null ? getMap.get("LOADING_TIME").toString() : "");
							excelUtil.setCellDoubleValue(starRows + i, 6 + addlank,
									Double.parseDouble(getMap.get("PIECE_SUM") != null ? getMap.get("PIECE_SUM").toString() : "0"));
							excelUtil.setCellStrValue(starRows + i, 7 + addlank,
									getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "");
							excelUtil.setCellDoubleValue0215(starRows + i, 8 + addlank, Double.parseDouble(getMap.get("NET_WEIGHT_SUM") != null
									? getMap.get("NET_WEIGHT_SUM").toString() : "0"));
							excelUtil.setCellDoubleValue0215(starRows + i, 9 + addlank, Double.parseDouble(getMap.get("GROSS_WEIGHT_SUM") != null
									? getMap.get("GROSS_WEIGHT_SUM").toString() : "0"));
							sumNum = 5;
						} else {
							excelUtil.setCellStrValue(5,11,getMap.get("STOCK_NAME") != null ? "存货方："+getMap.get("STOCK_NAME").toString() : "");
							excelUtil.setCellStrValue(6,11,getMap.get("RECEIVER_NAME") != null ? "收货方："+getMap.get("RECEIVER_NAME").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 4 + addlank,
									getMap.get("LOADING_TRUCK_NUM") != null ? getMap.get("LOADING_TRUCK_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 5 + addlank,
									getMap.get("TYPE_SIZE") != null ? getMap.get("TYPE_SIZE").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 6 + addlank,
									getMap.get("PRO_NUM") != null ? getMap.get("PRO_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 7 + addlank,
									getMap.get("LOT_NUM") != null ? getMap.get("LOT_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 8 + addlank,
									getMap.get("MSC_NUM") != null ? getMap.get("MSC_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 9 + addlank,
									getMap.get("PRO_TIME") != null ? getMap.get("PRO_TIME").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 10 + addlank,
									getMap.get("LOADING_TIME") != null ? getMap.get("LOADING_TIME").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 11 + addlank,
									getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "");
							excelUtil.setCellDoubleValue(starRows + i, 12 + addlank,
									Double.parseDouble(getMap.get("PIECE_SUM") != null ? getMap.get("PIECE_SUM").toString() : "0"));
							excelUtil.setCellDoubleValue0215(starRows + i, 13 + addlank, Double.parseDouble(getMap.get("NET_WEIGHT_SUM") != null
									? getMap.get("NET_WEIGHT_SUM").toString() : "0"));
							excelUtil.setCellDoubleValue0215(starRows + i, 14 + addlank, Double.parseDouble(getMap.get("GROSS_WEIGHT_SUM") != null
									? getMap.get("GROSS_WEIGHT_SUM").toString() : "0"));
							excelUtil.setCellStrValue(starRows + i, 15 + addlank,
									getMap.get("SALES_NUM") != null ? getMap.get("SALES_NUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 16 + addlank,
									getMap.get("ORDER_NUM") != null ? getMap.get("ORDER_NUM").toString() : "");
							sumNum = 10;
						}

						sumPice += Double.valueOf(
								getMap.get("PIECE_SUM") != null ? getMap.get("PIECE_SUM").toString() : "0");
						sumNet += Double.valueOf(
								getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0");
						sumGross += Double.valueOf(getMap.get("GROSS_WEIGHT_SUM") != null
								? getMap.get("GROSS_WEIGHT_SUM").toString() : "0");
					}

				}
				// 添加合计
				if(3 == obj.getNtype()){
					excelUtil.setCellStrValue(starRows + getlist.size(),sumNum+1,
							"合计/Total:");
					excelUtil.setCellStrValue(starRows + getlist.size(),
							sumNum + 2, new DecimalFormat("####.00").format(sumPice));
				}else{
					excelUtil.setCellStrValue(starRows + getlist.size(),sumNum,
							"合计/Total:");
					excelUtil.setCellStrValue(starRows + getlist.size(),
							sumNum+1, new DecimalFormat("####.00").format(sumPice.longValue()));
				}
				excelUtil.setCellStrValue(starRows + getlist.size(),
						sumNum + 3, new DecimalFormat("####.00").format(sumNet));
				excelUtil.setCellStrValue(starRows + getlist.size(),
						sumNum + 4, new DecimalFormat("####.00").format(sumGross));
			}
			excelUtil.setCellStrValue(starRows + getlist.size()+2,7,
					"PrintTime :");
			excelUtil.setCellStrValue(starRows + getlist.size()+2,
					8,DateUtils.getDateTime());

			//2025-02-21 徐峥增加
			excelUtil.setCellStrValue(starRows + getlist.size()+2+5,
					0,"1. 本凭证仅针对出具之日货物的数量、重量及有权提货人作出陈述，对于出具后货物情况发生的变化及提货人的变化不承担任何责任。");
			excelUtil.setCellStrValue(starRows + getlist.size()+2+6,
					0,"2. 该单据仅作为提供给客户的入库/出库/在库明细不得作为他用（如金融质押、抵押等）。");
//			excelUtil.setCellStrValue(starRows + getlist.size()+2+6,
//					0,"1. This document only makes representations regarding the quantity, weight, and the authorized consignee of the goods as of the date of issuance. It does not assume any responsibility for changes in the condition of the goods or changes in the consignee after the issuance.\n" +
//							"2. This document is solely intended to provide customers with details of incoming/outgoing/current stock and must not be used for other purposes (such as financial collateral, mortgage, etc.).");

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
	 	@RequestMapping(value = "reportpdf")
	   	@ResponseBody
	   	public void exportpdf(@Valid @ModelAttribute @RequestBody BisOutStock obj,HttpServletRequest request, HttpServletResponse response) throws Exception{
	 		if(obj.getNtype()!=null && obj.getNtype()>0){
	       		List<Map<String, Object>> getlist = outStockService.findRepot(obj.getNtype(),obj.getSearchBillNum(),obj.getSearchItemNum(),obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchStrTime(),obj.getSearchEndTime(),obj.getIfBonded());
	       		String[] headCN={"提单号","MR/集装箱号","SKU号","货物描述","出库日期","货物状态","件数","总净重(KGS)","总毛重(KGS)"};
	       		String[] headEN={"B/L NO.","MR/CTN NO.","SKU","Description of cargo","Outbound Date","State of cargo","Qty","Net Weight","Gross Weight"};
	       		String pdfTitle = "出库报告书";
	            StringBuffer pdfCN=new StringBuffer();
	            if (1==obj.getNtype()) {
	            	pdfCN.append(""
	            			    +"提单号"+"                         "
	            				+"MR/集装箱号"+"                     "
	            				+"SKU"+"                                      "
	            				+"货物描述"+"                                                                           "
	            				+"出库日期"+"                          "
	            				+"货物状态"+"                            "
	            				+"件数"+"                                "
	            				+"总净重(KGS)"+"                      "
	            				+"总毛重(KGS)"+"               "
	            				);
	            }
	            if (2==obj.getNtype()) {
	            	pdfCN.append(""
	            			+"入库号"+"                       "
            			    +"提单号"+"                         "
            				+"MR/集装箱号"+"                 "
            				+"SKU"+"                                   "
            				+"货物描述"+"                                                                "
            				+"出库日期"+"                      "
            				+"货物状态"+"                             "
            				+"件数"+"                               "
            				+"总净重(KGS)"+"           "
            				+"总毛重(KGS)"+"               "
	        				);
	            }
	            if (3==obj.getNtype()) {
	            	pdfCN.append(""
	            			+"订单号"+"             "
            			    +"提单号"+"                       "
            				+"MR/集装箱号"+"                  "
            				+"SKU"+"                              "
            				+"货物描述"+"                                 "
            				+"规格"+"                 "
            				+"项目号"+"           "
            				+"船名批号"+"        "
            				+"MSC"+"                "
            				+"生产日期"+"        "
            				+"出库日期"+"       "
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
	            	}else if(3==obj.getNtype() && ( "SKU号".equals(lab)||"MR/集装箱号".equals(lab)||"提单号".equals(lab))){
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
	        					customUser=getMap.get("STOCK_NAME")!=null?getMap.get("STOCK_NAME").toString():"";
	        					consigneeUser=getMap.get("RECEIVER_NAME")!=null?getMap.get("RECEIVER_NAME").toString():"";
	        				}
		       				sbHtml.append("<tr>");
		       				if(2==obj.getNtype()){
		       					sbHtml.append("<td>").append(getMap.get("RK_NUM")!=null?getMap.get("RK_NUM").toString():"").append("</td>");
		    	       			nTJ=6;
		    	       		}else if(3==obj.getNtype()){
		    	       			String regEx="[`~!@#$%^&*=|':;'/?~！@#￥%……&*‘”“’。，、？]";     
		    	       	        Pattern   p   =   Pattern.compile(regEx); 
		    	       	        Matcher   m   =   p.matcher(getMap.get("ORDER_NUM")!=null?getMap.get("ORDER_NUM").toString():"");        
		    	       		  	sbHtml.append("<td>").append(m.replaceAll("").trim()).append("</td>");
		    	       		}
		       				sbHtml.append("<td>").append(getMap.get("BILL_NUM")!=null?getMap.get("BILL_NUM").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(getMap.get("CTN_NUM")!=null?getMap.get("CTN_NUM").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(getMap.get("SKU_ID")!=null?getMap.get("SKU_ID").toString():"").append("</td>");
		       				
// 		       				String regEx2="[<>]";     
// 	    	       	        Pattern   pp   =   Pattern.compile(regEx2); 
// 	    	       	        Matcher   mm   =   pp.matcher(getMap.get("CARGO_NAME")!=null?getMap.get("CARGO_NAME").toString():"");
// 	   	       	       	 sbHtml.append("<td>").append(mm.replaceAll("").trim()).append("</td>");
 	    	       	     	sbHtml.append("<td>").append(getMap.get("CARGO_NAME")!=null?getMap.get("CARGO_NAME").toString().replaceAll(">", "》").replaceAll("<", "《").replaceAll("&", "&amp;"):"").append("</td>");
		       				if(3==obj.getNtype()){
		       					sbHtml.append("<td>").append(getMap.get("TYPE_SIZE")!=null?getMap.get("TYPE_SIZE").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("PRO_NUM")!=null?getMap.get("PRO_NUM").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("LOT_NUM")!=null?getMap.get("LOT_NUM").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("MSC_NUM")!=null?getMap.get("MSC_NUM").toString():"").append("</td>");
		       					sbHtml.append("<td>").append(getMap.get("PRO_TIME")!=null?getMap.get("PRO_TIME").toString():"").append("</td>");
		       					nTJ=11;
		       				}
		       				sbHtml.append("<td>").append(getMap.get("LOADING_TIME")!=null?getMap.get("LOADING_TIME").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(getMap.get("ENTER_STATE")!=null?getMap.get("ENTER_STATE").toString():"").append("</td>");
		       				sbHtml.append("<td>").append(String.format("%.2f", Double.valueOf(getMap.get("PIECE_SUM")!=null?getMap.get("PIECE_SUM").toString():"0"))).append("</td>");
		       				sbHtml.append("<td>").append(String.format("%.2f", Double.valueOf(getMap.get("NET_WEIGHT_SUM")!=null?getMap.get("NET_WEIGHT_SUM").toString():"0"))).append("</td>");
		       				sbHtml.append("<td>").append(String.format("%.2f", Double.valueOf(getMap.get("GROSS_WEIGHT_SUM")!=null?getMap.get("GROSS_WEIGHT_SUM").toString():"0"))).append("</td>");
		       				 
		       				sbHtml.append("</tr>");
		       				sumPice+=Double.valueOf(getMap.get("PIECE_SUM")!=null?getMap.get("PIECE_SUM").toString():"0");
	        	        	sumNet+=Double.valueOf(getMap.get("NET_WEIGHT_SUM")!=null?getMap.get("NET_WEIGHT_SUM").toString():"0");
		        			sumGross+=Double.valueOf(getMap.get("GROSS_WEIGHT_SUM")!=null?getMap.get("GROSS_WEIGHT_SUM").toString():"0");
	       				}
	       			}//end for
	       			 
	       			//添加合计
	       			sbHtml.append("<tr><td class=\"ftd\" colspan=\""+nTJ+"\" style=\"border:0px;height:30px; \"></td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">合计/Total：</td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####").format(sumPice)).append("</td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumNet)).append("</td>");
	       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumGross)).append("</td>");
	       			sbHtml.append("</tr>");
	       		}
	       		sbHtml.append("</table>");
	       		sbHtml.append("<table style=\"border-spacing:0px;text-align:center; border-collapse:collapse;font-family:宋体;font-size:12px;width:100%\"><tr>");
	       		@SuppressWarnings("unused")
				User user = UserUtil.getCurrentUser();
	       		sbHtml.append("<td style=\"text-align:right;margin-top:10px;\">").append("PrintTime : &nbsp;").append(DateUtils.getDateTime()).append("</td>");
	       		sbHtml.append("</tr></table>");

				//2025-02-21 徐峥增加
				sbHtml.append("<table><tr><td></td></tr></table>");
				sbHtml.append("<table><tr><td></td></tr></table>");
				sbHtml.append("<table><tr><td></td></tr></table>");
				sbHtml.append("<table><tr><td></td></tr></table>");
				sbHtml.append("<table><tr><td></td></tr></table>");
				sbHtml.append("<table><tr>");
				sbHtml.append("<td style=\"width:50%;font-size:16px; font-family:宋体;\">1. 本凭证仅针对出具之日货物的数量、重量及有权提货人作出陈述，对于出具后货物情况发生的变化及提货人的变化不承担任何责任。</td>");
				sbHtml.append("</tr></table>");
				sbHtml.append("<table><tr>");
				sbHtml.append("<td style=\"width:50%;font-size:16px; font-family:宋体;\">2. 该单据仅作为提供给客户的入库/出库/在库明细不得作为他用（如金融质押、抵押等）。</td>");
				sbHtml.append("</tr></table>");
//				sbHtml.append("<table><tr>");
//				sbHtml.append("<td>").append("1. This document only makes representations regarding the quantity, weight, and the authorized consignee of the goods as of the date of issuance. It does not assume any responsibility for changes in the condition of the goods or changes in the consignee after the issuance.\n" +
//						"2. This document is solely intended to provide customers with details of incoming/outgoing/current stock and must not be used for other purposes (such as financial collateral, mortgage, etc.).").append("</td>");
//				sbHtml.append("</tr></table>");

	       		MyFileUtils html=new MyFileUtils();
	       		html.setFilePath(pathHtml);
	       		html.saveStrToFile(CreatPDFUtils.createPdfHtmlOutStock("出库报告书", "Outbound Report","The customer ："+customUser+"<br/><br/>The consignee："+consigneeUser+"", sbHtml.toString()));
	       		MyPDFUtils.setsDEST(pathPdf);
	       		MyPDFUtils.setsHTML(pathHtml);
	       		MyPDFUtils.createPdf(PageSize.A3,pdfCN.toString(),pdfTitle);
	       		
	       		//下载操作
	       		FileInputStream in = new FileInputStream(new File(pathPdf));
	        	int len = 0;
	        	byte[] buffer = new byte[1024];
	        	String formatFileName = URLEncoder.encode("出库报告单" +".pdf","UTF-8");
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
	 * 判断出库联系单是否是放行状态
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "iffx/{outLinkId}/{loadingPlanNum}", method = RequestMethod.POST)
	@ResponseBody
	public String iffx(@PathVariable("outLinkId") String outLinkId,@PathVariable("loadingPlanNum") String loadingPlanNum) {
		BisOutStock outStock = outStockService.get(outLinkId);
		BisLoadingOrder bisLoadingOrder=loadingOrderService.find("orderNum", loadingPlanNum);
		if("0".equals(outStock.getIfRelease())){
			return "该车辆不允许放行！";
		}else if("1".equals(bisLoadingOrder.getIslock())){
			return "该车辆已经被控车！";
		}else{
			return "success";
		}
	}
	
	/**
	 * 放行
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "fx/{outLinkId}", method = RequestMethod.GET)
	@ResponseBody
	public String fx(@PathVariable("outLinkId") String outLinkId) {
		BisOutStock outStock = outStockService.get(outLinkId);
		outStock.setIfRelease("1");
		User user = UserUtil.getCurrentUser();
		outStock.setClearPerson(user.getName());
		outStock.setClearDate(new Date());
		outStockService.update(outStock);
		return "success";
	}
	
    @RequestMapping(value = "jsonOutReport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getJsonData(@Valid @ModelAttribute @RequestBody BisOutStock obj,HttpServletRequest request) {
    	Page<Stock> page = getPage(request);
    	page = inStockReportService.getOutStockStocks(page, obj.getNtype(),obj.getSearchBillNum(), obj.getSearchItemNum(),
				obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchStrTime(),
				obj.getSearchEndTime(),obj.getIfBonded());
    	return getEasyUIData(page);
    }
    
    
    
    /**
     * 分类监管 申请单 出库
     */
    @RequestMapping(value = "appr/{outLinkId}", method = RequestMethod.GET)
    public String apprForm(HttpServletRequest request,Model model, @PathVariable("outLinkId") String outLinkId) {
        BisOutStock bisOutStock = outStockService.get(outLinkId);
        model.addAttribute("bisOutStock", bisOutStock);       
        List<BisOutStockInfo> oldList = outStockInfoService.getList(outLinkId);
        BigDecimal pieces = new BigDecimal(0);
        BigDecimal net = new BigDecimal(0);
        BigDecimal gross = new BigDecimal(0);
		for (BisOutStockInfo forBisOutStockInfo:oldList) {
			if (forBisOutStockInfo.getOutNum()!=null && forBisOutStockInfo.getOutNum().toString().trim().length() > 0){
				pieces = pieces.add(new BigDecimal(forBisOutStockInfo.getOutNum().toString()));
			}
			if (forBisOutStockInfo.getGrossWeight()!=null && forBisOutStockInfo.getGrossWeight().toString().trim().length() > 0){
				net = net.add(new BigDecimal(forBisOutStockInfo.getGrossWeight().toString()));
			}
			if (forBisOutStockInfo.getGrossWeight()!=null && forBisOutStockInfo.getGrossWeight().toString().trim().length() > 0){
				gross = gross.add(new BigDecimal(forBisOutStockInfo.getGrossWeight().toString()));
			}
		}
        model.addAttribute("pieces", pieces);
        model.addAttribute("net", net);
        model.addAttribute("gross", gross.setScale(2, BigDecimal.ROUND_HALF_UP));
        return "wms/outstock/apprForm";

    }
}
