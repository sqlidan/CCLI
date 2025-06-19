package com.haiersoft.ccli.wms.web;
import java.io.*;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.haiersoft.ccli.report.web.OCRUtils;
import com.haiersoft.ccli.supervision.web.FTPUtils;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.apiEntity.ApiType;
import com.haiersoft.ccli.wms.entity.apiEntity.InvtHeadType;
import com.haiersoft.ccli.wms.entity.apiEntity.InvtHeadTypeVo;
import com.haiersoft.ccli.wms.service.PreEntryInvtQuery.PreEntryInvtQueryService;
import com.haiersoft.ccli.wms.web.preEntry.HttpUtils;
import okhttp3.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.alibaba.druid.sql.ast.statement.SQLIfStatement.Else;
import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseExpenseShare;
import com.haiersoft.ccli.base.entity.BaseHscode;
import com.haiersoft.ccli.base.entity.BaseItemname;
import com.haiersoft.ccli.base.entity.ExpenseScheme;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.ExpenseSchemeService;
import com.haiersoft.ccli.base.service.ExpenseShareService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.CreatPDFUtils;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.MyFileUtils;
import com.haiersoft.ccli.common.utils.MyPDFUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.InStockReportService;
import com.haiersoft.ccli.supervision.web.SupervisionController;
import com.haiersoft.ccli.system.entity.Dict;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.service.DictService;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.AsnActionDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.AsnActionService;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import com.itextpdf.text.PageSize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author pyl
 * @ClassName: EnterStockController
 * @Description: 入库总览Control1ler
 * @date 2016年3月1日 下午5:05:33
 */
@Controller
@RequestMapping("wms/enterStock")
public class EnterStockController extends BaseController {

    @Autowired
    private EnterStockService enterStockService;
    @Autowired
    private EnterStockInfoService enterStockInfoService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ExpenseSchemeService expenseSchemeService;
    @Autowired
    private ExpenseSchemeInfoService expenseSchemeInfoService;
    @Autowired
    private ASNService asnService;
    @Autowired
    private DictService dictService;
    @Autowired
    private ExpenseShareService expenseShareService;
    @Autowired
    private TrayInfoService trayInfoService;
    @Autowired
    private StandingBookService standingBookService;
    @Autowired
    private InStockReportService inStockReportService;
    @Autowired
	private AsnActionDao asnActionDao;
    @Autowired
	private AsnActionService asnActionService;
    
    
    /**
     * 入库联系单总览默认页面
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "wms/enterStock/enterStock";
    }
    /**
     * 财务展示入库联系单默认页面处理
     * @return
     */
    @RequestMapping(value = "fdlist", method = RequestMethod.GET)
    public String fdlist() {
        return "wms/enterStock/enterFdStock";
    }

    /**
     * 入库联系单管理默认页面
     */
    @RequestMapping(value = "manager", method = RequestMethod.GET)
    public String manager(Model model) {
        model.addAttribute("action", "create");
        return "wms/enterStock/enterStockManager";
    }
    
    
    /**
     * 入库联系单管理修改默认页面
     */
    @RequestMapping(value = "enterStockUpdate", method = RequestMethod.GET)
    public String enterStockUpdate() {
        return "wms/enterStock/enterStockUpdate";
    }
    /**
     * 入库联系单管理修改的修改页面
     */
    @RequestMapping(value = "managerUpdate", method = RequestMethod.GET)
    public String managerUpdate(HttpServletRequest request,Model model) {
    	
    	String linkId = request.getParameter("linkId");
    	if(linkId!=null && !"".equals(linkId)){
    		BisEnterStock bisEnterStock = enterStockService.get(linkId);
    		model.addAttribute("bisEnterStock", bisEnterStock);
    	}
        return "wms/enterStock/enterStockManagerUpdate";
    }
    //入库联系单管理修改 默认页面 查询
    @RequestMapping(value = "serchEnterStockUpdate", method = RequestMethod.POST)
    @ResponseBody
    public String searchEnterStock(HttpServletRequest request,Model model) {
    	String itemNum = request.getParameter("itemNum");
    	String linkId = request.getParameter("linkId");
    	
    	//todo
    	List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        if(itemNum!=null && !"".equals(itemNum)){
        	PropertyFilter filter = new PropertyFilter("EQS_itemNum", itemNum);
        	filters.add(filter);
        }
    	if(linkId!=null && !"".equals(linkId)){
    		PropertyFilter filter2 = new PropertyFilter("EQS_linkId", linkId);
            filters.add(filter2);
    	}
        List<BisEnterStock> list = enterStockService.search(filters);
        BisEnterStock bs = list.get(0);
        model.addAttribute("bisEnterStock", bs);
        return bs.getLinkId()!=null?bs.getLinkId():"";
    }
    
    /**
     * 检查是否可以修改
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "checkEnterStockUpdate", method = RequestMethod.POST)
    @ResponseBody
    public String checkEnterStockUpdate(HttpServletRequest request,Model model) {
    	String linkId = request.getParameter("linkId");
    	List<Map<String,Object>> list = new ArrayList<>();
    	//根据llinkId取库存表不符合条件数量
    	list = trayInfoService.findTrayListForCheck(linkId);
    	
    	if(list!=null && list.size()>0){
    		return "error0";//货物已出库或货转，不能修改当前入库联系单
    	}else{
    		list = standingBookService.findStandingBookListForCheck(linkId);
    		if(list!=null && list.size()>0){
    			return "error1";//联系单已生成费用，不能修改当前入库联系单
    		}else{
    			return "success";//可以修改
    		}
    	}
    }
    
    
    
    @RequestMapping(value = "getNewLinkId", method = RequestMethod.POST)
    @ResponseBody
    public String getNewLinkId() {
        return enterStockService.getLinkIdToString();
    }

    /**
     * @return
     * @throws ParseException
     * @throws
     * @author pyl
     * @Description: 入库联系单查询
     * @date 2016年3月2日 下午2:42:10
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(@Valid @ModelAttribute @RequestBody BisEnterStock obj,HttpServletRequest request) throws ParseException {
        Page<BisEnterStock> page = getPage(request);
        page = enterStockService.getEnterStocks(page, obj);
        return getEasyUIData(page);
    }

    /**
     * @author lv
     * @Description: 入库计划单查询
     */
    @RequestMapping(value = "jsonlist", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getSearchData(HttpServletRequest request) {
    	Page<Object> page = getPage(request);
        List<Map<String, Object>> getList = new ArrayList<Map<String, Object>>();
        if (request.getParameter("filter_EQS_linkId") != null || request.getParameter("filter_EQS_billNum") != null || request.getParameter("filter_EQS_transNum") != null || request.getParameter("filter_EQS_ctnNum") != null) {
            String linkNum = request.getParameter("filter_EQS_linkId").toString();//提单号
            String billNum = request.getParameter("filter_EQS_billNum").toString();//提单号
            String transNum = request.getParameter("filter_EQS_transNum").toString();//装车单号
            String ctnNum = request.getParameter("filter_EQS_ctnNum").toString();//厢号
            getList = enterStockService.findList(page, linkNum, billNum, transNum, ctnNum);
        }
        return getList;
    }
    /**
     * 检索查询
     * @param request
     * @return
     */
    @RequestMapping(value = "searchlist", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> searchData(HttpServletRequest request) {
    	Page<BisEnterStock> page = getPage(request);
        String linkNum = request.getParameter("searchLinkId");
        String billNum = request.getParameter("searchItemNum");//提单号
    	if((null!=linkNum&&!"".equals(linkNum))||(null!=billNum&&!"".equals(billNum))){
    		BisEnterStock obj=new BisEnterStock();
    		obj.setSearchLinkId(linkNum);
    		obj.setSearchItemNum(billNum);
    		page = enterStockService.getStocks(page, obj);
    	}
        return getEasyUIData(page);
    }
    
    @RequestMapping(value = "getlist", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getData(HttpServletRequest request) {
    	Page<BisEnterStock> page = getPage(request);
        String value = request.getParameter("value");
        List<Map<String, Object>> getList = new ArrayList<Map<String, Object>>();
    	if(null!=value&&!"".equals(value)){
            getList = enterStockService.findList(page,value);
    	}
        return getList;
    }
    /**
     * @param
     * @return
     * @throws
     * @author pyl
     * @Description: 判断入库联系单是否已保存
     * @date 2016年3月17日 下午7:22:02
     */
    @RequestMapping(value = "ifsave/{linkId}", method = RequestMethod.GET)
    @ResponseBody
    public String ifsave(@PathVariable("linkId") String linkId) {
        BisEnterStock enterStock = enterStockService.get(linkId);
        if (enterStock != null && !"".equals(enterStock)) {
            return "success";
        } else {
            return "false";
        }
    }

    /**
     * @param model
     * @return
     * @throws
     * @author pyl
     * @Description: 添加入库联系单页面跳转
     * @date 2016年3月2日 下午2:42:15
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String createContractForm(Model model) {
        String linkId = enterStockService.getLinkIdToString();//入库联系单号
        User user = UserUtil.getCurrentUser();
        BisEnterStock enterStock = new BisEnterStock();
        enterStock.setLinkId(linkId);
        enterStock.setOperator(user.getName());
        enterStock.setOperateTime(new Date());
        model.addAttribute("action", "create");
        return "wms/enterStock/enterStockManager";
    }

    /**
     * 获取费用方案字典
     */
    @RequestMapping(value = "selFeePlan", method = RequestMethod.GET)
    @ResponseBody
    public List<BaseExpenseShare> selFeePlan(HttpServletRequest request, String clientId) {
        List<BaseExpenseShare> tt = new ArrayList<BaseExpenseShare>();
        if (!NumberUtils.isDigits(clientId)) {
            return tt;
        }
        List<BaseClientInfo> clientInfoList = clientService.judgeById(Integer.valueOf(clientId));
        if (clientInfoList.isEmpty()) {
            return tt;
        }
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQS_programState", "1");
        PropertyFilter filter2 = new PropertyFilter("EQS_clientId", clientId);
        PropertyFilter filter0 = new PropertyFilter("NEQS_ifParent", "1");
        filters.add(filter);
        filters.add(filter2);
        filters.add(filter0);
        List<BaseExpenseShare> expenseShare = expenseShareService.search(filters);
        List<PropertyFilter> filterOld = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter3 = new PropertyFilter("EQS_customsId", clientId);
        PropertyFilter filter4 = new PropertyFilter("EQS_isDel", "0");
        filterOld.add(filter3);
        filterOld.add(filter);
        filterOld.add(filter4);
        List<ExpenseScheme> oldObj = expenseSchemeService.search(filterOld,"operateTime",false);
        if (!oldObj.isEmpty()) {
            for (ExpenseScheme obj : oldObj) {
                BaseExpenseShare expense = new BaseExpenseShare();
                expense.setSchemeNum(obj.getSchemeNum());
                expense.setSchemeName(obj.getSchemeName());
                expenseShare.add(expense);
            }
        }
        return expenseShare;
    }

    /**
     * 获取客户字典
     */
    @RequestMapping(value = "selClient", method = RequestMethod.GET)
    @ResponseBody
    public List<BaseClientInfo> selClient(HttpServletRequest request) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQI_delFlag", String.valueOf(0));
        filters.add(filter);
        PropertyFilter filter2 = new PropertyFilter("EQS_clientSort", String.valueOf(0));
        filters.add(filter2);
        List<BaseClientInfo> clientInfo = clientService.search(filters);
        return clientInfo;
    }

    /**
     * @param model
     * @return
     * @throws
     * @author pyl
     * @Description: 入库联系单修改展示
     * @date 2016年3月5日 上午10:35:02
     */
    @RequestMapping(value = "updateEnterStock/{linkId}", method = RequestMethod.GET)
    public String updateContractForm(Model model, @PathVariable("linkId") String linkId) {
        BisEnterStock bisEnterStock = enterStockService.get(linkId);
        model.addAttribute("bisEnterStock", bisEnterStock);
        model.addAttribute("checkListNo", bisEnterStock.getCheckListNo());
        model.addAttribute("emsNo", "");
        model.addAttribute("action", "update");
        return "wms/enterStock/enterStockManager";
    }

    /**
     * @param state
     * @return
     * @throws
     * @author pyl
     * @Description: 保存
     * @date 2016年3月4日 下午5:13:25
     */
    @RequestMapping(value = "createEnterStock/{state}", method = RequestMethod.POST)
    @ResponseBody
    public String createContract(HttpServletRequest request, HttpServletResponse response, @PathVariable("state") String state) {
        BisEnterStock enterStock = new BisEnterStock();
        parameterReflect.reflectParameter(enterStock, request);//转换对应实体类参数
        User user = UserUtil.getCurrentUser();

        //2024-12-09 徐峥增加
        if (enterStock.getIfBonded()!=null && "1".equals(enterStock.getIfBonded()) && enterStock.getCheckListNo()!=null && enterStock.getCheckListNo().trim().length() > 0){
            //依据核注清单号查询核注清单信息是否存在
            List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();
            List<PropertyFilter> filters = new ArrayList<>();
            filters.add(new PropertyFilter("EQS_bondInvtNo", enterStock.getCheckListNo().trim()));
            filters.add(new PropertyFilter("EQS_synchronization", "1"));
            bisPreEntryInvtQueryList = preEntryInvtQueryService.search(filters);
            if (bisPreEntryInvtQueryList==null || bisPreEntryInvtQueryList.size() == 0){
                return "当前信息为保税入库联系单，需填写有效的的核注清单号。";
            }else{
                if (bisPreEntryInvtQueryList.get(0)!=null) {
                    bisPreEntryInvtQueryList.get(0).setInLinkId(enterStock.getLinkId());
                    preEntryInvtQueryService.merge(bisPreEntryInvtQueryList.get(0));
                }
            }
        }

        enterStock.setDelFlag("0"); //删除标志，正常
        if ("0".equals(state)) {
            enterStock.setPlanFeeState("0");
            enterStock.setFinishFeeState("0");
            enterStock.setAuditingState(0);
            enterStock.setOperateTime(new Date());
        } else {
            BisEnterStock enterS = enterStockService.get(enterStock.getLinkId());
            enterStock.setPlanFeeState(enterS.getPlanFeeState());
            enterStock.setFinishFeeState(enterS.getFinishFeeState());
            enterStock.setOperateTime(enterS.getOperateTime());
            enterStock.setUpdateTime(new Date());
        }

        enterStock.setOperator(user.getName());
        enterStock.setItemNum(enterStock.getItemNum().trim());

        enterStockService.merge(enterStock);
        enterStockInfoService.updateBillNum(enterStock.getLinkId(), enterStock.getItemNum());

// 	    asnService.changeAsn(enterStock);
        //修改入库订单时，增加对asn计费区间的费用方案同步处理 yhn20170729
        List<PropertyFilter> plist = new ArrayList<PropertyFilter>();
        plist.add(new PropertyFilter("EQS_enterId", enterStock.getLinkId()));
        plist.add(new PropertyFilter("NULLD_chargeEndDate","2017-07-29 00:00:00"));//??
        plist.add(new PropertyFilter("NULLS_linkTransferId", ""));
        plist.add(new PropertyFilter("NULLS_transferSign",""));
        List<AsnAction> asnActionList = asnActionService.getAsnAction(plist);
        if (!asnActionList.isEmpty()) {
            for (AsnAction asnac : asnActionList) {
            	asnac.setFeePlanId(enterStock.getFeeId());
            	asnActionService.merge(asnac);
            }
        }
        return "success";
    }
    
    
    /**
     * 入库联系单修改 页面的保存功能
     * @param request
     * @param response
     * @param state
     * @return
     */
    @RequestMapping(value = "createEnterStockUpdate/{state}", method = RequestMethod.POST)
    @ResponseBody
    public String createContractUpdate(HttpServletRequest request, HttpServletResponse response, @PathVariable("state") String state) {
    	String result = "success";
        BisEnterStock enterStock = new BisEnterStock();
        parameterReflect.reflectParameter(enterStock, request);//转换对应实体类参数

        User user = UserUtil.getCurrentUser();
        enterStock.setDelFlag("0"); //删除标志，正常ss
//		enterStock.setStockIn(getClientName(Integer.valueOf(enterStock.getStockId())));
        String stockIdOld = request.getParameter("stockS");//老存货方id
        String stockInOld = request.getParameter("stockSName");//老存货方名称
        //String feeIdOld = request.getParameter("feeS");//老费用方案Id
        //String feePlanOld = request.getParameter("feeSName");//老费用方案名称
        //String itemNumOld = request.getParameter("itemNumS");//老提单号
        //String stockOrgIdOld = request.getParameter("stockOrgIdS");//老结算单位id
        //String stockOrgNameOld = request.getParameter("stockOrgIdSName");//老结算单位名称
        
        if (state.equals("0")) {
            enterStock.setPlanFeeState("0");
            enterStock.setFinishFeeState("0");
            enterStock.setAuditingState(0);
            enterStock.setOperateTime(new Date());
        } else {
            BisEnterStock enterS = enterStockService.get(enterStock.getLinkId());
            enterStock.setPlanFeeState(enterS.getPlanFeeState());
            enterStock.setFinishFeeState(enterS.getFinishFeeState());
            enterStock.setOperateTime(enterS.getOperateTime());
            enterStock.setUpdateTime(new Date());
        }
        enterStock.setOperator(user.getName());
        enterStock.setItemNum(enterStock.getItemNum().trim());
        enterStockService.merge(enterStock);
        enterStockInfoService.updateBillNum(enterStock.getLinkId(), enterStock.getItemNum());
        //调用存储过程 同步asn 库存表 asn_action
        String linkId = enterStock.getLinkId();
        String stockId = enterStock.getStockId();
        String stockIn = enterStock.getStockIn();
        String feeId = enterStock.getFeeId();
        String itemNum = enterStock.getItemNum();
        String stockOrgId = enterStock.getStockOrgId();
        try{
        	result = UpdEnterStockCascadeASNTrayInfoASNActionBylinkId(linkId,stockIdOld,stockInOld,stockId,stockIn,feeId,itemNum,stockOrgId,(user!=null?user.getName():""));
            result = result.contains("1")?"success":"nosuccess";
            return result;
        }catch(Exception e){
        	e.printStackTrace();
        	return "nosuccess";
        }
// 	    asnService.changeAsn(enterStock);
    }
	///存储过程 根据
    public String UpdEnterStockCascadeASNTrayInfoASNActionBylinkId(String linkId,String stockIdOld,String stockInOld,String stockId,String stockIn,String feeId,String itemNum,String stockOrgId,String username){    
 		 String driver = PropertiesUtil.getPropertiesByName("jdbc.driver", "application"); 
 		  Statement stmt = null;  
 		  ResultSet rs = null;  
 		  Connection conn = null; 
 		  String restate="0";//0失败 1成功
 		  try {  
 		      Class.forName(driver);   
 		      conn = enterStockService.getConnect();
 		      CallableStatement proc = null;  
 		      proc = conn.prepareCall("{ call Pro_EnterStockCascade(?,?,?,?,?,?,?,?,?,?) }");  
 		      proc.setString(1, linkId);  
 		      proc.setString(2, stockIdOld);
 		      proc.setString(3, stockInOld);
 		      proc.setString(4, stockId); 
 		      proc.setString(5, stockIn); 
 		      proc.setString(6, feeId);
 		      proc.setString(7, itemNum);
 		      proc.setString(8, stockOrgId);
 		      proc.setString(9, username);
 		      proc.registerOutParameter(10, Types.INTEGER);  
 		      proc.execute();  
 		      restate = proc.getString(10);    
 		   }  
 		   catch (SQLException ex2) {
 			   //ex2.printStackTrace();  
 		   }  
 		   catch (Exception ex2) {  
 			  // ex2.printStackTrace();  
 		   }  
 		   finally{  
 		      try {  
 		        if(rs != null){  
 		          rs.close();  
 		          if(stmt!=null){  
 		            stmt.close();  
 		          }  
 		          if(conn!=null){  
 		            conn.close();  
 		          }  
 		        }  
 		      }  
 		      catch (SQLException ex1) {  
 		      }  
 		  }
 		return restate;
 	} 
    
    
    
    /**
     * @return
     * @throws
     * @author pyl
     * @Description: 保存并新建
     * @date 2016年7月15日 上午9:20
     */
    @RequestMapping(value = "saveAndAdd/{linkId}", method = RequestMethod.GET)
    @ResponseBody
    public String saveAndAdd(@PathVariable("linkId") String linkId) {
        BisEnterStock obj = enterStockService.get(linkId);
        BisEnterStock newObj = new BisEnterStock();
        BeanUtils.copyProperties(obj, newObj);
        newObj.setLinkId(enterStockService.getLinkIdToString());
        newObj.setAuditingState(0);
        newObj.setItemNum("00");
        enterStockService.merge(newObj);
        return newObj.getLinkId();
    }

    /**
     * @return
     * @throws
     * @author pyl
     * @Description: 复制（带明细）
     * @date 2016年7月15日 上午9:20
     */
    @RequestMapping(value = "copyit/{linkId}", method = RequestMethod.GET)
    @ResponseBody
    public String copyit(@PathVariable("linkId") String linkId) {
        BisEnterStock obj = enterStockService.get(linkId);
        BisEnterStock newObj = new BisEnterStock();
        BeanUtils.copyProperties(obj, newObj);
        newObj.setLinkId(enterStockService.getLinkIdToString());
        newObj.setAuditingState(0);
        newObj.setItemNum("00");
        enterStockService.merge(newObj);
        List<BisEnterStockInfo> oldList = enterStockInfoService.getList(linkId);

        for (BisEnterStockInfo oldInfo : oldList) {
            BisEnterStockInfo newInfo = new BisEnterStockInfo();
            BeanUtils.copyProperties(oldInfo, newInfo);
            newInfo.setId(null);
            newInfo.setItemNum("");
            newInfo.setLinkId(newObj.getLinkId());
            enterStockInfoService.merge(newInfo);
        }
        return newObj.getLinkId();
    }


    /**
     * @param linkId
     * @return
     * @throws
     * @author pyl
     * @Description: 删除
     * @date 2016年3月3日 上午11:29:50
     */
    @RequestMapping(value = "deleteEnterStock/{linkId}")
    @ResponseBody
    public String deleteEnterStock(@PathVariable("linkId") String linkId) {
        User user = UserUtil.getCurrentUser();
        BisEnterStock enterStock = enterStockService.get(linkId);
        if (enterStock != null && !"".equals(enterStock)) {
            enterStock.setDelFlag("1");//删除
            enterStock.setOperator(user.getName());
            enterStock.setUpdateTime(new Date());
            enterStockService.update(enterStock);
            return "success";
        } else {
            return "此入库联系单未保存，无法删除";
        }

    }

    /**
     * @param linkId
     * @return
     * @throws
     * @author pyl
     * @Description: 提交通过
     * @date 2016年3月5日 下午2:20:07
     */
    @RequestMapping(value = "submitEnterStock/{linkId}")
    @ResponseBody
    public String submitEnterStock(@PathVariable("linkId") String linkId) {
        //User user = UserUtil.getCurrentUser();

        BisEnterStock enterStock = enterStockService.get(linkId);
        enterStock.setAuditingState(1);
        enterStock.setUpdateTime(new Date());

        enterStockService.update(enterStock);
        return "success";
    }
    
    /**
     * @param linkId
     * @return
     * @throws
     * @author yhn
     * @Description: 审核前验证相关ASNAction 是否要修改计费客户和费用方案
     * @date 2018年5月29日 03:20:07
     */
    @RequestMapping(value = "checkPassEnterStock/{linkId}")
    @ResponseBody
    public String checkPassEnterStock(@PathVariable("linkId") String linkId) {
       
        BisEnterStock enterStock = enterStockService.get(linkId);
        
        List<PropertyFilter> asnActionFilters = new ArrayList<PropertyFilter>();
		asnActionFilters.add(new PropertyFilter("EQS_enterId", linkId));
		List<AsnAction> asnActions = asnActionDao.find(asnActionFilters);//查询ASN区间表
		if(asnActions.size()>0){
			for(AsnAction aa :asnActions){
				if(!aa.getJfClientId().equals(enterStock.getStockOrgId()) || !aa.getFeePlanId().equals(enterStock.getFeeId())){
					return "false";
				}
			}
		}
        return "success";
    }
    
    /**
     * @param linkId
     * @return
     * @throws
     * @author yhn
     * @Description: 审核前验证相关ASNAction 是否要修改计费客户和费用方案
     * @date 2018年5月29日 03:20:07
     */
    @RequestMapping(value = "passEnterStockAndUpdateASNActions/{linkId}")
    @ResponseBody
    public String passEnterStockAndUpdateASNActions(@PathVariable("linkId") String linkId) {
       
        BisEnterStock enterStock = enterStockService.get(linkId);
        
        List<PropertyFilter> asnActionFilters = new ArrayList<PropertyFilter>();
		asnActionFilters.add(new PropertyFilter("EQS_enterId", linkId));
		List<AsnAction> asnActions = asnActionDao.find(asnActionFilters);//查询ASN区间表
		if(asnActions.size()>0){
			for(AsnAction aa :asnActions){
				
				aa.setJfClientId(enterStock.getStockOrgId());
				aa.setFeePlanId(enterStock.getFeeId());
				asnActionService.update(aa);
			}
		}
        return "success";
    }
    
    
    /**
     * @param linkId
     * @return
     * @throws
     * @author pyl
     * @Description: 审核通过
     * @date 2016年3月5日 下午2:20:07
     */
    @RequestMapping(value = "passEnterStock/{linkId}")
    @ResponseBody
    public String passEnterStock(@PathVariable("linkId") String linkId) {
        User user = UserUtil.getCurrentUser();

        BisEnterStock enterStock = enterStockService.get(linkId);
        enterStock.setAuditingState(2);
        enterStock.setUpdateTime(new Date());
        enterStock.setExaminePerson(user.getName());
        enterStock.setExamineTime(new Date());

        enterStockService.update(enterStock);
        return "success";
    }

    /**
     * @param linkId
     * @return
     * @throws
     * @author pyl
     * @Description: 驳回
     * @date 2016年3月5日 下午2:20:07
     */
    @RequestMapping(value = "noPassEnterStock/{linkId}")
    @ResponseBody
    public String noPassEnterStock(@PathVariable("linkId") String linkId) {
        User user = UserUtil.getCurrentUser();

        BisEnterStock enterStock = enterStockService.get(linkId);
        enterStock.setAuditingState(0);
        enterStock.setUpdateTime(new Date());
        enterStock.setExaminePerson(user.getName());
        enterStock.setExamineTime(new Date());

        enterStockService.update(enterStock);
        return "success";
    }

    /**
     * @param linkId
     * @return
     * @throws
     * @author pyl
     * @Description: 取消审核
     * @date 2016年3月5日 下午2:20:07
     */
    @RequestMapping(value = "backPassEnterStock/{linkId}")
    @ResponseBody
    public String backPassEnterStock(@PathVariable("linkId") String linkId) {
        User user = UserUtil.getCurrentUser();
        List<BisAsn> asnList = asnService.ifasn(linkId);
        if (asnList.isEmpty()) {
            BisEnterStock enterStock = enterStockService.get(linkId);
            enterStock.setAuditingState(1);
            enterStock.setUpdateTime(new Date());
            enterStock.setExaminePerson(user.getName());
            enterStock.setExamineTime(new Date());
            enterStockService.update(enterStock);
            return "success";
        } else {
        	BisEnterStock enterStock = enterStockService.get(linkId);
            enterStock.setAuditingState(1);
            enterStock.setUpdateTime(new Date());
            enterStock.setExaminePerson(user.getName());
            enterStock.setExamineTime(new Date());
            enterStockService.update(enterStock);
            return "false";
        }

    }

    /**
     * @param linkId
     * @return
     * @throws
     * @author pyl
     * @Description: 计划费用完成
     */
    @RequestMapping(value = "planOk/{linkId}")
    @ResponseBody
    public String planOk(@PathVariable("linkId") String linkId) {
        BisEnterStock enterStock = enterStockService.get(linkId);
        enterStock.setPlanFeeState("1");
        enterStock.setUpdateTime(new Date());
        enterStockService.update(enterStock);
        return "success";
    }

    /**
     * @param linkId
     * @return
     * @throws
     * @author pyl
     * @Description: 计划费用取消
     */
    @RequestMapping(value = "planNo/{linkId}")
    @ResponseBody
    public String planNo(@PathVariable("linkId") String linkId) {
        BisEnterStock enterStock = enterStockService.get(linkId);
        enterStock.setPlanFeeState("0");
        enterStock.setUpdateTime(new Date());
        enterStockService.update(enterStock);
        return "success";
    }

    /**
     * @param linkId
     * @return
     * @throws
     * @author pyl
     * @Description: 费用完成
     */
    @RequestMapping(value = "finishOk/{linkId}")
    @ResponseBody
    public String finishOk(@PathVariable("linkId") String linkId) {
        BisEnterStock enterStock = enterStockService.get(linkId);
        enterStock.setFinishFeeState("1");
        enterStock.setUpdateTime(new Date());
        enterStockService.update(enterStock);
        return "success";
    }

    /**
     * @param linkId
     * @return
     * @throws
     * @author pyl
     * @Description: 费用取消
     */
    @RequestMapping(value = "finishNo/{linkId}")
    @ResponseBody
    public String finishNo(@PathVariable("linkId") String linkId) {
        BisEnterStock enterStock = enterStockService.get(linkId);
        enterStock.setFinishFeeState("0");
        enterStock.setUpdateTime(new Date());
        enterStockService.update(enterStock);
        return "success";
    }


    /**
     * Ajax请求校验提单号是否唯一。
     */
    @RequestMapping(value = "checkItemNum/{linkId}")
    @ResponseBody
    public String checkItemNum(@PathVariable("linkId") String linkId, String itemNum) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("itemNum", itemNum);
        params.put("delFlag", "0");
        List<BisEnterStock> enterStock = enterStockService.findEnterList(params);
        if (itemNum.contains(",") || itemNum.contains("，")) {
            return "false";
        } else {
            if (enterStock.isEmpty()) {
                return "true";
            } else {
                if (enterStock.get(0).getLinkId().equals(linkId)) {
                    return "true";
                } else {
                    return "false";
                }
            }
        }
    }

    /**
     * @Description: 通过费用ID获得费用方案名称
     */
    public String getFeeName(String id) {
        ExpenseSchemeInfo expenseSchemeInfo = expenseSchemeInfoService.get(id);
        String schemeName = expenseSchemeInfo.getSchemeNum();
        return schemeName;
    }

    /**
     * 入库联系单打印跳转
     *
     * @param linkId
     * @param model
     * @return
     */
    @RequestMapping(value = "print/{linkId}", method = RequestMethod.GET)
    public String print(@PathVariable("linkId") String linkId, Model model) {
        BisEnterStock enterStock = enterStockService.get(linkId);
        List<BisEnterStockInfo> enterStockInfoList = enterStockInfoService.getList(linkId);
        List<Dict> dict = dictService.getReceptacle("receptacle");
        model.addAttribute("bisEnterStock", enterStock);
        model.addAttribute("dict", dict);
//		BisEnterStockInfo enterStockInfo = null;
        if (!enterStockInfoList.isEmpty()) {
            int l = enterStockInfoList.size();
            model.addAttribute("infoSize", l);
            model.addAttribute("enterStockInfoList", enterStockInfoList);
            Integer allPiece = 0;
            Double allNet = 0.00;
            Double allGross = 0.00;
            for (int i = 0; i < enterStockInfoList.size(); i++) {
                allPiece += enterStockInfoList.get(i).getPiece();
                allNet += enterStockInfoList.get(i).getNetWeight();
                allGross += enterStockInfoList.get(i).getGrossWeight();
            }
            model.addAttribute("allPiece", allPiece);
            model.addAttribute("allNet", allNet);
            model.addAttribute("allGross", allGross);
        }
//		else{
//			BisEnterStockInfo enterStockInfoN = new BisEnterStockInfo();
//			model.addAttribute("bisEnterStockInfo", enterStockInfoN);
//		}
        model.addAttribute("action", "print");
        return "wms/enterStock/enterStockPrint";
    }


    /**
     * 判断是否制作了ASN
     *
     * @param linkId
     * @return
     */
    @RequestMapping(value = "ifasn/{linkId}", method = RequestMethod.GET)
    public String ifasn(@PathVariable("linkId") String linkId, Model model) {
        List<BisAsn> asnList = asnService.ifasn(linkId);
        if (!asnList.isEmpty()) {
            return "success";
        } else {
            return "false";
        }
    }

    /**
     * @param request
     * @param response
     * @throws Exception
     * @throws
     * @Description: 导出excel
     * @date 2016年3月15日 下午5:22:55
     */
    @RequestMapping(value = "export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BisEnterStock enterStock = new BisEnterStock();
        parameterReflect.reflectParameter(enterStock, request);// 转换对应实体类参数

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        
        List<BisEnterStock> enterStockList = enterStockService.search(filters);

        ExportParams params = new ExportParams("入库联系单", "入库联系单Sheet", ExcelType.XSSF);

        Workbook workbook = ExcelExportUtil.exportExcel(params, BisEnterStock.class, enterStockList);

        String formatFileNameP = "入库联系单" + ".xlsx";
        String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }


    /**
     * 跳转到入库报告书页面
     */
    @RequestMapping(value = "toreport", method = RequestMethod.GET)
    public String toReport(Model model) {
        Date now = new Date();
        model.addAttribute("strTime", DateUtils.getDateStart(now));
        model.addAttribute("endTime", DateUtils.getDateEnd(now));
        return "wms/enterStock/enterStockReport";
    }

    /***
     * 根据查询条件，导出入库报告书
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "report")
    @ResponseBody
    public void export(@Valid @ModelAttribute @RequestBody BisEnterStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (obj.getNtype() != null && obj.getNtype() > 0) {
            List<Map<String, Object>> getlist = enterStockService.findRepot(obj.getNtype(), obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchSKUNum(), obj.getSearchStrTime(), obj.getSearchEndTime(),obj.getIfBonded());
            String formatFileName = URLEncoder.encode("入库报告单" + ".xls", "UTF-8");
            ExcelUtil excelUtil = new ExcelUtil();
            String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
            String srcPath = null;
            String desPath = null;
            if (filePath == null || "".equals(filePath)) {
                filePath = request.getSession().getServletContext().getRealPath("/");
                if (1 == obj.getNtype()) {
                    srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\inreport.xls";
                } else if (2 == obj.getNtype()) {
                    srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\inreportjp.xls";
                } else {
                    srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\inreportote.xls";
                }
                desPath = filePath + "WEB-INF\\classes\\excelpost\\inreport.xls";
            } else {
                if (1 == obj.getNtype()) {
                    srcPath = filePath + "exceltemplate\\inreport.xls";
                } else if (2 == obj.getNtype()) {
                    srcPath = filePath + "exceltemplate\\inreportjp.xls";
                } else {
                    srcPath = filePath + "exceltemplate\\inreportote.xls";
                }
                desPath = filePath + "excelpost\\inreport.xls";
            }
            excelUtil.setSrcPath(srcPath);
            excelUtil.setDesPath(desPath);
            excelUtil.setSheetName("Sheet1");
            excelUtil.getSheet();
            //加载数据
            int starRows = 10;
            if (getlist != null && getlist.size() > 0) {
                Map<String, Object> getMap = null;
                Double sumPice = 0d;
                Double sumNet = 0d;
                Double sumGross = 0d;
                int sumNum = 0;
                String getOrderNum = null;
                for (int i = 0; i < getlist.size(); i++) {
                    getMap = getlist.get(i);
                    if (getMap != null && getMap.size() > 0) {
                        if (getMap.get("BILL_NUM") != null && getMap.get("CTN_NUM") != null && getMap.get("SKU_ID") != null) {
                            excelUtil.setCellStrValue(starRows + i, 0, getMap.get("BILL_NUM").toString());
                            excelUtil.setCellStrValue(starRows + i, 1, getMap.get("CTN_NUM").toString());
                            excelUtil.setCellStrValue(starRows + i, 2, getMap.get("SKU_ID").toString());
                            //处理转义字符串 yaohn 20180919
                            String str = getMap.get("CARGO_NAME").toString();
                            if(str!=null){
                            	String sss = transferHtml(str,str);
                            	excelUtil.setCellStrValue(starRows + i, 3, sss);
                            }else{
                            	excelUtil.setCellStrValue(starRows + i, 3, "");	
                            }
                            //excelUtil.setCellStrValue(starRows + i, 3, getMap.get("CARGO_NAME") != null ? getMap.get("CARGO_NAME").toString() : "");
//            	        	if(0==i){
//            	        		excelUtil.setCellStrValue(5,6,"The customer:"+ getMap.get("CLIENT_NAME").toString());
//            	        	}
                        } else {
                            continue;
                        }
                        if (1 == obj.getNtype()) {
                            excelUtil.setCellStrValue(starRows + i, 4, getMap.get("INBOUND_DATE") != null ? getMap.get("INBOUND_DATE").toString() : "");
                            excelUtil.setCellStrValue(starRows + i, 5, getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "");
                            excelUtil.setCellDoubleValue(starRows + i, 6, Double.parseDouble(getMap.get("RUKU_PIECE_SUM") != null ? getMap.get("RUKU_PIECE_SUM").toString() : "0"));
                            excelUtil.setCellDoubleValue(starRows + i, 7, Double.parseDouble(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0"));
                            excelUtil.setCellDoubleValue(starRows + i, 8, Double.parseDouble(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0"));
                            excelUtil.setCellStrValue(starRows + i, 9, getMap.get("CLIENT_NAME") != null ? getMap.get("CLIENT_NAME").toString() : "");
                            sumNum = 5;
                        } else if (2 == obj.getNtype()) {
                            excelUtil.setCellStrValue(starRows + i, 4, getMap.get("RK_NUM") != null ? getMap.get("RK_NUM").toString() : "");
                            excelUtil.setCellStrValue(starRows + i, 5, getMap.get("INBOUND_DATE") != null ? getMap.get("INBOUND_DATE").toString() : "");
                            excelUtil.setCellDoubleValue(starRows + i, 6, Double.parseDouble(getMap.get("RUKU_PIECE_SUM") != null ? getMap.get("RUKU_PIECE_SUM").toString() : "0"));
                            excelUtil.setCellStrValue(starRows + i, 7, getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "");
                            excelUtil.setCellDoubleValue(starRows + i, 8, Double.parseDouble(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0"));
                            excelUtil.setCellDoubleValue(starRows + i, 9, Double.parseDouble(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0"));
                            excelUtil.setCellStrValue(starRows + i, 10, getMap.get("CLIENT_NAME") != null ? getMap.get("CLIENT_NAME").toString() : "");
                            sumNum = 6;
                        } else {
                            //if(0==i){
                            getOrderNum = asnService.getOrderNo(getMap.get("BILL_NUM").toString());
//        	        			if(getOrderNum!=null){
//        	        				//根据提单号获取asn里订单号
//                	        		excelUtil.setCellStrValue(6,5,"Order_no:"+ getOrderNum);
//        	        			}
                            //}
                            excelUtil.setCellStrValue(starRows + i, 4, getMap.get("TYPE_SIZE") != null ? getMap.get("TYPE_SIZE").toString() : "");
                            excelUtil.setCellStrValue(starRows + i, 5, getMap.get("PRO_NUM") != null ? getMap.get("PRO_NUM").toString() : "");
                            excelUtil.setCellStrValue(starRows + i, 6, getMap.get("LOT_NUM") != null ? getMap.get("LOT_NUM").toString() : "");
                            excelUtil.setCellStrValue(starRows + i, 7, getMap.get("MSC_NUM") != null ? getMap.get("MSC_NUM").toString() : "");
                            excelUtil.setCellStrValue(starRows + i, 8, getMap.get("PRO_TIME") != null ? getMap.get("PRO_TIME").toString() : "");        //生产日期
                            excelUtil.setCellStrValue(starRows + i, 9, getMap.get("INBOUND_DATE") != null ? getMap.get("INBOUND_DATE").toString() : "");
                            excelUtil.setCellStrValue(starRows + i, 10, getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "");
                            excelUtil.setCellDoubleValue(starRows + i, 11, Double.parseDouble(getMap.get("RUKU_PIECE_SUM") != null ? getMap.get("RUKU_PIECE_SUM").toString() : "0"));
                            excelUtil.setCellDoubleValue(starRows + i, 12, Double.parseDouble(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0"));
                            excelUtil.setCellDoubleValue(starRows + i, 13, Double.parseDouble(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0"));
                            excelUtil.setCellStrValue(starRows + i, 14, getMap.get("CLIENT_NAME") != null ? getMap.get("CLIENT_NAME").toString() : "");
                            excelUtil.setCellStrValue(starRows + i, 15, getOrderNum != null ? getOrderNum : "");
                            sumNum = 10;
                        }

                        sumPice += Double.valueOf(getMap.get("RUKU_PIECE_SUM") != null ? getMap.get("RUKU_PIECE_SUM").toString() : "0");
                        sumNet += Double.valueOf(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0");
                        sumGross += Double.valueOf(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0");
                    }

                }
                //添加合计
                excelUtil.setCellStrValue(starRows + getlist.size(), sumNum, "合计/Total:");
                excelUtil.setCellStrValue(starRows + getlist.size(), sumNum + 1, String.valueOf(sumPice.longValue()));
                excelUtil.setCellStrValue(starRows + getlist.size(), sumNum + 2, String.valueOf(sumNet));
                excelUtil.setCellStrValue(starRows + getlist.size(), sumNum + 3, String.valueOf(sumGross));
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
//            excelUtil.setCellStrValue(starRows + getlist.size()+2+6,
//                    0,"1. This document only makes representations regarding the quantity, weight, and the authorized consignee of the goods as of the date of issuance. It does not assume any responsibility for changes in the condition of the goods or changes in the consignee after the issuance.\n" +
//                            "2. This document is solely intended to provide customers with details of incoming/outgoing/current stock and must not be used for other purposes (such as financial collateral, mortgage, etc.).");

            excelUtil.exportToNewFile();
            FileInputStream in = new FileInputStream(new File(desPath));
            int len = 0;
            byte[] buffer = new byte[1024];
            response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
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

    @RequestMapping(value = "reportpdf")
    @ResponseBody
    public void exportpdf(@Valid @ModelAttribute @RequestBody BisEnterStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (obj.getNtype() != null && obj.getNtype() > 0) {
            List<Map<String, Object>> getlist = enterStockService.findRepot(obj.getNtype(), obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchSKUNum(), obj.getSearchStrTime(), obj.getSearchEndTime(),obj.getIfBonded());
            String[] headCN = {"提单号", "MR/集装箱号", "SKU号", "货物描述", "入库日期", "货物状态", "件数", "总净重(KGS)", "总毛重(KGS)"};//,"存货方"
            String[] headEN = {"B/L NO.", "MR/CTN NO.", "SKU", "Description of cargo", "Inbound Date", "State of cargo", "Qty", "Net Weight", "Gross Weight"};//,"The customer"
            String pdfTitle = "入库报告书";
            StringBuffer pdfCN = new StringBuffer();
            if (1 == obj.getNtype()) {
                pdfCN.append(""
                        + "提单号"
                        + "                                               "
                        + "MR/集装箱号" + "                                     "
                        + "SKU" + "                                                    "
                        + "货物描述" + "                                                              "
                        + "入库日期" + "                    "
                        + "货物状态" + "                     "
                        + "件数" + "                            "
                        + "总净重(KGS)" + "                "
                        + "总毛重(KGS)" + "               "
                );
            }
            if (2 == obj.getNtype()) {
                pdfCN.append(""
                        + "提单号"
                        + "                                         "
                        + "MR/集装箱号" + "                               "
                        + "SKU" + "                                                  "
                        + "货物描述" + "                                                       "
                        + "入库号" + "                    "
                        + "入库日期" + "                    "
                        + "货物状态" + "                 "
                        + "件数" + "                         "
                        + "总净重(KGS)" + "           "
                        + "总毛重(KGS)" + "               "
                );
            }
            if (3 == obj.getNtype()) {
                pdfCN.append(""
                        + "提单号"
                        + "                          "
                        + "MR/集装箱号" + "                  "
                        + "SKU" + "                                       "
                        + "货物描述" + "                                  "
                        + "规格" + "                 "
                        + "项目号" + "            "
                        + "船名批名" + "          "
                        + "MSC" + "                   "
                        + "生产日期" + "          "
                        + "入库日期" + "          "
                        + "货物状态" + "         "
                        + "件数" + "         "
                        + "总净重(KGS)" + "    "
                        + "总毛重(KGS)" + "    "
                        + "订单号" + "        "
                );
            }
            String path = request.getSession().getServletContext().getRealPath("/");//获取web项目的路径"d://exceltemplate//syshtm.html";
            String pathHtml = path + "//isyshtm.html";
            String pathPdf = path + "//isyspdf.pdf";
            StringBuffer sbHtml = new StringBuffer();

            sbHtml.append("<div  style=\"height:5px;\"></div>");
            sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
            //填充标题头
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headCN) {
                if ("货物描述".equals(lab)) {
                    sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
                } else if ("SKU号".equals(lab) || "MR/集装箱号".equals(lab) || "提单号".equals(lab)) {
                    sbHtml.append("<td  width=\"250px\">").append(lab).append("</td>");
                } else {
                    sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                }
                if (2 == obj.getNtype() && "货物描述".equals(lab)) {
                    sbHtml.append("<td class=\"htd\">").append("入库号").append("</td>");
                }
                if (3 == obj.getNtype() && "货物描述".equals(lab)) {
                    sbHtml.append("<td class=\"htd\">").append("规格").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("项目号").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("船名批号").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("生产日期").append("</td>");
                }
            }
            if (3 == obj.getNtype()) {
                sbHtml.append("<td class=\"htd\">").append("订单号").append("</td>");
            }
            sbHtml.append("</tr>");
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headEN) {
                sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                if (2 == obj.getNtype() && "Description of cargo".equals(lab)) {
                    sbHtml.append("<td class=\"htd\">").append("Inbound NO.").append("</td>");
                }
                if (3 == obj.getNtype() && "Description of cargo".equals(lab)) {
                    sbHtml.append("<td class=\"htd\">").append("Standard").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("Pro No.").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("Lot No.").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("Pro Date").append("</td>");
                }
            }
            if (3 == obj.getNtype()) {
                sbHtml.append("<td class=\"htd\">").append("Order No").append("</td>");
            }
            sbHtml.append("</tr>");
            int nTJ = 5;
            String custNmae = null;
            //填充内容
            if (getlist != null && getlist.size() > 0) {
                String getOrderNum = null;//订单号
                Double sumPice = 0d;
                Double sumNet = 0d;
                Double sumGross = 0d;
                Map<String, Object> getMap = null;
                for (int i = 0; i < getlist.size(); i++) {
                    getMap = getlist.get(i);
                    if (getMap != null && getMap.size() > 0) {
                        sbHtml.append("<tr>");
                        sbHtml.append("<td>").append(getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CTN_NUM") != null ? getMap.get("CTN_NUM").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("SKU_ID") != null ? getMap.get("SKU_ID").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CARGO_NAME") != null ? getMap.get("CARGO_NAME").toString().replaceAll("&", "&amp;") : "").append("</td>");
                        if (2 == obj.getNtype()) {
                            sbHtml.append("<td>").append(getMap.get("RK_NUM") != null ? getMap.get("RK_NUM").toString() : "").append("</td>");
                            nTJ = 6;
                        }
                        if (3 == obj.getNtype()) {
                            sbHtml.append("<td>").append(getMap.get("TYPE_SIZE") != null ? getMap.get("TYPE_SIZE").toString() : "").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("PRO_NUM") != null ? getMap.get("PRO_NUM").toString() : "").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("LOT_NUM") != null ? getMap.get("LOT_NUM").toString() : "").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("MSC_NUM") != null ? getMap.get("MSC_NUM").toString() : "").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("PRO_TIME") != null ? getMap.get("PRO_TIME").toString() : "").append("</td>");
                            nTJ = 10;
                        }
                        sbHtml.append("<td>").append(getMap.get("INBOUND_DATE") != null ? getMap.get("INBOUND_DATE").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####").format(Double.parseDouble(getMap.get("RUKU_PIECE_SUM") != null ? getMap.get("RUKU_PIECE_SUM").toString() : "0"))).append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.parseDouble(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0"))).append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.parseDouble(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0"))).append("</td>");
                        //sbHtml.append("<td>").append(getMap.get("CLIENT_NAME")!=null?getMap.get("CLIENT_NAME").toString():"").append("</td>");
                        if (3 == obj.getNtype()) {
                            getOrderNum = asnService.getOrderNo(getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "");
                            sbHtml.append("<td>").append(getOrderNum != null ? getOrderNum : "").append("</td>");
                        }
                        sbHtml.append("</tr>");
                        sumPice += Double.valueOf(getMap.get("RUKU_PIECE_SUM") != null ? getMap.get("RUKU_PIECE_SUM").toString() : "0");
                        sumNet += Double.valueOf(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0");
                        sumGross += Double.valueOf(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0");
                        if (custNmae == null) {
                            custNmae = getMap.get("CLIENT_NAME") != null ? getMap.get("CLIENT_NAME").toString() : "";
                        }
                    }
                }//end for

                //添加合计
                sbHtml.append("<tr><td class=\"ftd\" colspan=\"" + nTJ + "\" style=\"border:0px;height:30px; \"></td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">合计：</td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####").format(sumPice)).append("</td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumNet)).append("</td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumGross)).append("</td>");
                sbHtml.append("</tr>");
            }
            sbHtml.append("</table>");
            sbHtml.append("<table style=\"border-spacing:0px;text-align:center; border-collapse:collapse;font-family:宋体;font-size:12px;width:100%\"><tr>");
            //User user = UserUtil.getCurrentUser();
            sbHtml.append("<td style=\"text-align:right;margin-top:10px;\">").append("操作人员：").append(" PrintTime : &nbsp;").append(DateUtils.getDateTime()).append("</td>");
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
//            sbHtml.append("<table><tr>");
//            sbHtml.append("<td>").append("1. This document only makes representations regarding the quantity, weight, and the authorized consignee of the goods as of the date of issuance. It does not assume any responsibility for changes in the condition of the goods or changes in the consignee after the issuance.\n" +
//                    "2. This document is solely intended to provide customers with details of incoming/outgoing/current stock and must not be used for other purposes (such as financial collateral, mortgage, etc.).").append("</td>");
//            sbHtml.append("</tr></table>");

            MyFileUtils html = new MyFileUtils();
            html.setFilePath(pathHtml);
            html.saveStrToFile(CreatPDFUtils.createPdfHtmlEnterStock("入库报告书", "Inbound Report（IN）", "The Customer：", custNmae, sbHtml.toString()));
            MyPDFUtils.setsDEST(pathPdf);
            MyPDFUtils.setsHTML(pathHtml);
            MyPDFUtils.createPdf(PageSize.A3, pdfCN.toString(), pdfTitle);


            //下载操作
            FileInputStream in = new FileInputStream(new File(pathPdf));
            int len = 0;
            byte[] buffer = new byte[1024];
            String formatFileName = URLEncoder.encode("入库报告单" + ".pdf", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
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

    
    @RequestMapping(value = "jsonEnterReport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getJsonData(@Valid @ModelAttribute @RequestBody BisEnterStock obj,HttpServletRequest request) {
    	Page<Stock> page = getPage(request);
    	page = inStockReportService.getEnterStockStocks(page, obj.getNtype(), obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchSKUNum(), obj.getSearchStrTime(), obj.getSearchEndTime(),obj.getIfBonded());
    	return getEasyUIData(page);
    }
    
    /**
    *转换html特殊符号。
    * @param content 需要转换的html特殊符号
    * @param defaultName 默认返回值
    * @return 转化后实际的符号
    */
    public static String transferHtml(String content, String defaultName) {
    if(content==null) return defaultName; 
    String html = content;
    html = StringUtils.replace(html, "&quot;", "\"");
    html = StringUtils.replace(html, "&lt;", "<");
    html = StringUtils.replace(html, "&gt;", ">");
    html = StringUtils.replace(html, "&gt;", ">");
    html = StringUtils.replace(html, "&sim;", "~");
    html = StringUtils.replace(html, "&and;", "^");
    html = StringUtils.replace(html, "&hellip;", "...");
    return html;
    }
	/**
	 * 添加跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "sendbsl", method = RequestMethod.GET)
	public String sendbsl(HttpServletRequest request,Model model) {
	   String linkId = request.getParameter("linkId");
	    if(linkId!=null && !"".equals(linkId)){
	    BisEnterStock bisEnterStock = enterStockService.get(linkId);
		model.addAttribute("bisEnterStock", bisEnterStock);
		model.addAttribute("action", "sendbsl");
	    }
		return "wms/enterStock/sendbslinfo";
	}
		
	public static String formatString(String str, int length, String slot){
		StringBuffer sb = new StringBuffer();
		sb.append(str);

		int count = length - str.length();

		while(count > 0){
			sb.append(slot);
			count --;
		}
		return sb.toString();
	}
	@RequestMapping(value="lisths", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getlisths(String linkId,String voyageNum,String chineseName) throws UnsupportedEncodingException {
	//	Page<BisEnterStock> page = getPage(request);
	//	String voyageNum = request.getParameter("voyageNum");// 搜索值
	//	String chineseName=request.getParameter("voyageNum");
		BisEnterStock bisEnterStock = enterStockService.get(linkId);
		chineseName=URLDecoder.decode(chineseName, "UTF-8");
		List<HashMap>  res=enterStockService.getlisths(chineseName,voyageNum);
        if (res.size()<=0) {
        	return "fail";
		}else {
			//bisEnterStock.setVesselName(res.get(0).get("vesselName").toString());
			//  enterStockService.update(bisEnterStock);
			
			  return res.get(0).get("vesselName").toString();
		}      
		
	}
	
	/**
	 * 修改入库联系单明细
	 *
	 * @param bisEnterStock
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "sendbsl", method = RequestMethod.POST)
	@ResponseBody
	public String sendbslForm(@Valid @ModelAttribute @RequestBody BisEnterStock bisEnterStock,Model model) throws IOException {
		BisEnterStock bisEnterStock2=enterStockService.getEnterStock(bisEnterStock.getItemNum());
		List<Map<String, Object>> list = new ArrayList<>();
		list =enterStockInfoService.getctNum(bisEnterStock.getLinkId());
		    String port=bisEnterStock.getDock();
			String re="success";
			StringBuffer  buffData = new StringBuffer();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String nowdate = sdf.format(date);
			//buffData.append("\r\n");
			String title ="LL_BSL"+nowdate+".bsl";
			String  filePath = "D:/supervision/" + "BSL"+nowdate+  ".bsl";

		    
			for (Map<String, Object> hashMap : list) {

//	           	 CargoInfo cargo = this.get(CargoInfo.class, Restrictions.eq("containerNum", site.getContainerNum()));
				//海关英文船名
				if (bisEnterStock.getEnglishName()==null){
					return "error";
				}else{
					if (bisEnterStock.getEnglishName().length()>20){
						buffData.append(formatString(bisEnterStock.getEnglishName().substring(0,20),20," "));//01
						bisEnterStock2.setEnglishName(formatString(bisEnterStock.getEnglishName().substring(0,20),20," "));
					}else{
						buffData.append(formatString(bisEnterStock.getEnglishName(),20," "));//01
						bisEnterStock2.setEnglishName(formatString(bisEnterStock.getEnglishName(),20," "));
					}
					//buffData.append("     ");//01
				}
				//船名代码
				if (bisEnterStock.getVesselName()==null){
					return "error";
				}else{
					buffData.append(formatString(bisEnterStock.getVesselName(),6," "));
					bisEnterStock2.setVesselName(formatString(bisEnterStock.getVesselName(),6," "));
				//	buffData.append("     ");//01
				}

				//进口航次
				if (bisEnterStock.getVoyageNum()==null){
					return "error";
				}else{
					buffData.append(formatString(bisEnterStock.getVoyageNum(),6," "));
					bisEnterStock2.setVoyageNum(formatString(bisEnterStock.getVoyageNum(),6," "));
				//	buffData.append("   ");
				}
				//箱号
				String ctNum=hashMap.get("CTNUM").toString();
				if (ctNum==null){
					return "error";
				}else{
					if (ctNum.length()>12){
						buffData.append(formatString(ctNum.substring(0,12),12," "));//01

					}else{

						buffData.append(formatString(ctNum,12," "));		
					}
				}
				//提单号
				buffData.append(formatString(bisEnterStock.getItemNum(),20," "));
			//	buffData.append("   ");

				if (port.equals("3")){
					bisEnterStock2.setDock("3");
					//箱站代码
					buffData.append("BSL");
					//提单号
				//	buffData.append(instoragePlan.getMasterBillNum());
					buffData.append("\r\n");
				}else if(port.equals("4")||port.equals("5")){
					bisEnterStock2.setDock(bisEnterStock.getDock());
					//箱站代码
					buffData.append("BSL ");
					buffData.append("\r\n");
				}

				//     site.setMessageId(report.getMessageId());
				//   site.setReportCode("");
				// site.setReportStatus("I0");


			}
			
			bisEnterStock2.setIsSend(("1"));
			bisEnterStock2.setChineseName(bisEnterStock.getChineseName());
			enterStockService.update(bisEnterStock2);
			//将文件写到磁盘
			String data = buffData.toString();
			File file = new File("D:/supervision");
			if (!file.isDirectory()) {// 路径文件夹是否存在，不存在则创建
				file.mkdirs();
			}
			//String tempFileDir =filePath+"/"+title;
			File f = new File("D:/supervision/"+title);
			if (!f.exists()) {
				f.createNewFile();// 不存在则创建  F:/4yundi/20160604/4218718062497_201606041519010.txt
			}
			BufferedReader input = new BufferedReader(new FileReader(f));

			input.close();

			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(data);
			output.close();

			///文件上传FTP
			System.out.println("生成.FLJGRX成功");
			String ftpHost="10.135.252.41";
			String ftpUserName = "bsl";
			String ftpPassword = "Bsl82987792";
			int ftpPort = 21;

			String ftpPath = "";
			if (port.equals("3")){
				ftpPath="/qqct/";
			}else if(port.equals("4")){
				ftpPath="/qqctu/";
			}else if(port.equals("5")){
				ftpPath="/qqctn/";
			}

			FileInputStream in=new FileInputStream(f);
			boolean flag = uploadFile(ftpPath, title, in);
            System.out.println("BSLflag="+flag);

		//	SupervisionController task=new SupervisionController();
//		    uploadFile(ftpHost,ftpUserName, ftpPassword, ftpPort, ftpPath, title, in);
		  
		return "success";
	}

    public boolean uploadFile(String path, String filename, InputStream input) {
        FTPClient ftpClient = new FTPClient();
        boolean success = false;
        System.out.println("111111");
        try {
            int reply;
            ftpClient.connect("10.135.252.41", 21);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftpClient.login("bsl", "Bsl82987792");// 登录
            reply = ftpClient.getReplyCode();
            System.out.println("222222"+reply);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();//TODO 被动模式 看现场是否需要这个
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("333333");
                ftpClient.disconnect();
                return success;
            }
            System.out.println("444444");
            Boolean result1 = ftpClient.changeWorkingDirectory(path);
            System.out.println("555555"+result1);
            Boolean result = ftpClient.storeFile(filename, input);
            System.out.println("666666"+result);
            input.close();
        } catch (IOException e) {
            throw new RuntimeException("向FTP服务器上传文件异常" , e);
        } finally {
            try {
                // 登出服务器
                ftpClient.logout();
            } catch (IOException e) {
                throw new RuntimeException("登录FTP服务器异常" , e);
            } finally {
                // 判断连接是否存在
                if (ftpClient.isConnected()) {
                    try {
                        // 断开连接
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        throw new RuntimeException("关闭FTP服务器异常" , e);
                    }
                }
            }
        }
        return true;
    }


	/**
	 * Description:             向FTP服务器上传文件
	 * @param ftpHost              FTP服务器hostname
	 * @param ftpUserName          FTP登录账号
     * @param ftpPassword          FTP登录密码
     * @param ftpPort              FTP服务器端口
     * @param ftpPath          FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
	 * @param filename          上传到FTP服务器上的文件名
	 * @param input             输入流
	 * @return                  成功返回true，否则返回false
	 */
	public static boolean uploadFile(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort, String ftpPath, String filename, InputStream input) {
		boolean result = false;
		FTPClient ftpClient = new FTPClient();
		try {
			int reply;
			ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
			ftpClient.changeWorkingDirectory(ftpPath);

			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return result;
			}

			filename = new String(filename.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING);//编码文件名，支持中文文件名
			//上传文件
			if (!ftpClient.storeFile(filename, input)) {

				return result;

			}
			System.out.println("上传成功");
			input.close();
			ftpClient.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

	/**
	 * 获取FTPClient对象
	 * @param ftpHost       FTP主机服务器
	 * @param ftpPassword   FTP 登录密码
	 * @param ftpUserName   FTP登录用户名
	 * @param ftpPort       FTP端口 默认为21
	 * @return
	 */
	public static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ftpHost, ftpPort);              // 连接FTP服务器
			ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
			ftpClient.setControlEncoding("UTF-8"); // 中文支持
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				System.out.println("未连接到FTP，用户名或密码错误。");
				//logger.info();
				ftpClient.disconnect();
			} else {
				System.out.println("FTP连接成功。");
				// logger.info("");
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("FTP的IP地址可能错误，请正确配置。");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FTP的端口错误,请正确配置。");
		}
		return ftpClient;
	}
	
	/**
	 * 分类监管 申请单 申请
	 */
    @RequestMapping(value = "appr/{linkId}", method = RequestMethod.GET)
    public String apprForm(HttpServletRequest request,Model model, @PathVariable("linkId") String linkId) {
        BisEnterStock bisEnterStock = enterStockService.get(linkId);
        model.addAttribute("bisEnterStock", bisEnterStock);       
        List<BisEnterStockInfo> oldList = enterStockInfoService.getList(linkId);
       
        model.addAttribute("bisEnterStockInfo", JSON.toJSONString(oldList));
        return "wms/enterStock/apprForm";
    }
//==========================识别报关单渲染录入联系单===========================================================================================
    @Autowired
    private PreEntryInvtQueryService preEntryInvtQueryService;

    /**
     * 上传报关单PDF
     */
    @RequestMapping(value = "BGDOCR", method = RequestMethod.GET)
    public String BGDOCR(HttpServletRequest request,Model model) {
        return "wms/enterStock/enterStockOCRInfoInto";
    }
    /**
     * 报关单PDF识别
     */
    @RequestMapping(value = "OCRPDF", method = RequestMethod.POST)
    @ResponseBody
    public String OCRPdf(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String API_KEY = "3HHaLDGWrn7aim103NDugde6";
        String SECRET_KEY = "cfURiYfdOrA1oXf6Hh4ZJ8XGnerKIU2R";

        List<Dict> dictList = new ArrayList<>();
        List<PropertyFilter> filters = new ArrayList<>();
        filters.add(new PropertyFilter("EQS_type", "ocr_key"));
        filters.add(new PropertyFilter("EQS_delFlag", "0"));
        dictList = dictService.search(filters);
        if (dictList!=null && dictList.size() > 0){
            API_KEY = dictList.get(0).getLabel();
            SECRET_KEY = dictList.get(0).getValue();
        }
        try {
            //方案一，开通外网识别域名权限，直接访问识别API
//            return OCRUtils.OCRPdf(bytes,API_KEY,SECRET_KEY);
            //方案二，访问10.135.200.5上的8994端口服务，访问跳转接口，获取识别结果数据
            String url = "http://10.135.200.5:8994/ccli/ocr/OCRPdf";
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/octet-stream");
            okhttp3.RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("API_KEY", API_KEY)
                    .addFormDataPart("SECRET_KEY", SECRET_KEY)
                    .addFormDataPart("file","ocrPdfFile.pdf", okhttp3.RequestBody.create(mediaType,bytes))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                String result = response.body().string();
                System.out.println(result);
                return result;
            }
        }catch(Exception e){
            return "识别异常";
        }
    }
    @RequestMapping(value = "addData/{json}", method = RequestMethod.GET)
    public String addData(Model model, @PathVariable("json") String json) {
        String linkId = enterStockService.getLinkIdToString();//入库联系单号
        User user = UserUtil.getCurrentUser();
        BisEnterStock enterStock = new BisEnterStock();
        enterStock.setLinkId(linkId);
        enterStock.setOperator(user.getName());
        enterStock.setOperateTime(new Date());

        String bah = "";//备案号
        String tydh = "";//提运单号
        String hzqdh = "";//核注清单号
        String bgdh = "";//报关单号
        if (json!=null && json.trim().length() > 0){
            String[] strings = json.split(",");
            bah = strings[1];
            tydh = strings[2];
            hzqdh = strings[3];
            bgdh = strings[5];
        }
        enterStock.setItemNum(tydh);//入库提单号
        //如果备案号等于T4230W000036，且存在核注清单号标识为此报关单为保税单据
        if (hzqdh !=null && hzqdh.trim().length() > 0 && ("T4230W000036".equals(bah) || "T4230W000042".equals(bah) || "T4230W000044".equals(bah))){
            enterStock.setIfBonded("1");//默认渲染为勾选保税，且不可修改
        }
        if (hzqdh.trim().length() > 0){
            //依据核注清单号查询核注清单信息是否存在
            List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();
            List<PropertyFilter> filters = new ArrayList<>();
            filters.add(new PropertyFilter("EQS_bondInvtNo", hzqdh));
            filters.add(new PropertyFilter("EQS_synchronization", "1"));
            bisPreEntryInvtQueryList = preEntryInvtQueryService.search(filters);
            if (bisPreEntryInvtQueryList!=null && bisPreEntryInvtQueryList.size() == 1){
                if (!"1".equals(enterStock.getIfBonded())){
                    enterStock.setIfBonded("1");//默认渲染为勾选保税，且不可修改
                }
                //渲染数据
                for (BisPreEntryInvtQuery forBisPreEntryInvtQuery:bisPreEntryInvtQueryList) {
                    enterStock.setItemNum(forBisPreEntryInvtQuery.getTdNo());//入库提单号
                }
            }
        }
        enterStock.setBgdh(bgdh);
        model.addAttribute("action", "create");
        model.addAttribute("bisEnterStock", enterStock);
        model.addAttribute("checkListNo", hzqdh);
        model.addAttribute("emsNo", bah);
        return "wms/enterStock/enterStockManager";
    }

//==========================================================================================================================
    public static Object ByteAryToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        if(bytes == null){
            return null;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn = null;
        Object obj = null;
        sIn = new ObjectInputStream(in);
        obj = sIn.readObject();
        return obj;
    }
}
