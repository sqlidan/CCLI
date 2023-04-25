package com.haiersoft.ccli.cost.service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.dao.BisCheckingBookDao;
import com.haiersoft.ccli.cost.entity.BisCheckingBook;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class BisCheckingBookService extends BaseService<BisCheckingBook, String> {

    @Autowired
    private BisCheckingBookDao bisCheckingBookDao;
    
    

    @Override
    public HibernateDao<BisCheckingBook, String> getEntityDao() {
        return bisCheckingBookDao;
    }
    /**
     * 撤销发票信息
     * @param codeNum
     * @return
     */
	public String delData(String codeNum){
    	//登录的应用系统名称和系统编码
    	Map<String, String> params = new HashMap<String, String>();
        JSONObject appInfo = new JSONObject();
        appInfo.put("user", "20017011");
		appInfo.put("source", "YW-711");
        Object[] objects = new Object[] {codeNum};
        JSONArray dataArray = JSONArray.fromObject(objects);
        params.put("appInfo",appInfo.toString());
		params.put("method","deleteData");
		params.put("queryData",dataArray.toString());
    	HttpGo httpGo = new HttpGo();
    	Properties properties=PropertiesUtil.getProperties();//上传地址处理
        String json=httpGo.sendRequest(properties.getProperty("test.delete.url"), params);
        JSONObject jsonObject = JSONObject.fromObject(json);
        //第一层接口通
        if("0000".equals(jsonObject.getString("result"))){
        	JSONObject data=JSONObject.fromObject(jsonObject.getString("data"));
        	if("0000".equals(data.getString("result"))){
        		//回写数据库里面发票信息
        		BisCheckingBook bisCheckingBook=bisCheckingBookDao.find(codeNum);
        		if(null==bisCheckingBook){
        			return "对账单不存在！";
        		}
        		bisCheckingBook.setCodeNum(codeNum);
        		bisCheckingBook.setJsfs("Y");
        		bisCheckingBook.setResult("remove");
        		bisCheckingBookDao.updateBisCheckBook(bisCheckingBook);
        		return "success";
        	}
        	return data.getString("data");
        }
        return "error";
    }
    /**
     * 编辑发票
     * @param request
     * @return
     */
	public String editUpload(HttpServletRequest request){
    	String codeNum=request.getParameter("codeNum");
    	//先撤销然后在上传
    	String flag=delData(codeNum);
    	if(!"success".equals(flag)){
    		return flag;
    	}
    	return upload(request);
    }
    /**
     * 上传增值税发票处理
     * @param request
     * @return
     */
	public String upload(HttpServletRequest request){
    	String operator=request.getParameter("operator");
    	String codeNum=request.getParameter("codeNum");
    	String customID=request.getParameter("customID");
    	String custom=request.getParameter("custom");//调整后的客户名称
    	String jsfs=request.getParameter("jsfs");//结算方式
    	Properties properties=PropertiesUtil.getProperties();//上传地址处理
    	HttpGo httpGo = new HttpGo();
    	JSONObject appInfo = new JSONObject();
		appInfo.put("user", "20017011");
		appInfo.put("source", "YW-711");
		Map<String, String> params = new HashMap<String, String>();
		params.put("appInfo",appInfo.toString());
		params.put("method","importData");
    	List<Map<String, Object>> list=bisCheckingBookDao.getFeeRmb(codeNum);
    	JSONArray jsonArray = new JSONArray();
    	for (int i = 0; i < list.size(); i++) {
    		String feeCode=request.getParameter("FEECODE"+i);//集团费目代码
    		String feeName=request.getParameter("FEENAME"+i);//集团费目名称
    		String label=request.getParameter("LABEL"+i);//币种
    		String rate=request.getParameter("RATE"+i);//汇率
    		String rmb=request.getParameter("RMB"+i);//含税金额
    		//上传发票信息
    		Map<String, Object> map=StringUtils.getMap(codeNum, customID, custom, label, rate, feeCode, feeName, rmb, operator,jsfs);
            JSONObject jsonObject = JSONObject.fromObject(map);
            jsonArray.add(jsonObject);
		}
    	Object[] objects = new Object[] {jsonArray};
    	JSONArray dataArray = JSONArray.fromObject(objects);
    	params.put("queryData",dataArray.toString());
    	String json=httpGo.sendRequest(properties.getProperty("test.upload.url"), params);
        JSONObject object= JSONObject.fromObject(json);
        //第一层接口通
        if("0000".equals(object.getString("result"))){
        	JSONObject data=JSONObject.fromObject(object.getString("data"));
        	if("0000".equals(data.getString("result"))){
        		//回写数据库里面发票信息
        		BisCheckingBook bisCheckingBook=bisCheckingBookDao.find(codeNum);
        		if(null==bisCheckingBook){
        			return "对账单不存在！";
        		}
        		bisCheckingBook.setCodeNum(codeNum);
        		bisCheckingBook.setSrcCustName(custom);
        		bisCheckingBook.setJsfs("Y");
        		bisCheckingBook.setResult("upload");
        		bisCheckingBookDao.updateBisCheckBook(bisCheckingBook);
        		return "success";
        	}
        	return data.getString("data");
        }
    	return "error";
    }
	
    /**
     * 开票增值税发票处理
     * @param request
     * @return
     */
	public String open(HttpServletRequest request) {
		String operator = request.getParameter("operator");
		String codeNum = request.getParameter("codeNum");
		String customID = request.getParameter("customID");
		String custom = request.getParameter("custom");// 调整后的客户名称
		String jsfs = request.getParameter("jsfs");// 结算方式
		String invoiceType = request.getParameter("invoiceType");// 发票类型
		String taxType = request.getParameter("taxType");// 税盘类型
		String currentAccountType = request.getParameter("currentAccountType");// 往来户类型
		String currencynum = request.getParameter("currencynum");// 币种
		String remark = request.getParameter("remark");// 备注
		String drawer = request.getParameter("drawer");// 开票人
		String payer = request.getParameter("payer");// 收票人
		String checker = request.getParameter("checker");// 复核人
		Properties properties = PropertiesUtil.getProperties();// 上传地址处理
		HttpGo httpGo = new HttpGo();
		Map<String, String> params = openGold(taxType, properties, httpGo);
		if (null != params && null != params.get("msg")) {
			return params.get("msg");
		}
		Map<String, Object> invoiceMap = StringUtils.getOpenMap(codeNum, drawer, payer, checker, invoiceType,
				currentAccountType, custom, currencynum, 0d, remark, taxType);
		JSONObject invoice = JSONObject.fromObject(invoiceMap);
		JSONArray makeArray = new JSONArray();
		makeArray.add("QDG065");
		makeArray.add("qdg_makeInvoice");
		List<Map<String, Object>> list = bisCheckingBookDao.getFeeRmb(codeNum);
		JSONArray jsonArray = new JSONArray();
		Double totale = 0d;
		for (int i = 0; i < list.size(); i++) {
			String feeCode = request.getParameter("FEECODE" + i);// 集团费目代码
			String feeName = request.getParameter("FEENAME" + i);// 集团费目名称
			String label = request.getParameter("LABEL" + i);// 币种
			String rate = request.getParameter("RATE" + i);// 汇率
			String rmb = request.getParameter("RMB" + i);// 含税金额
			// 上传发票信息
			Map<String, Object> map = StringUtils.getEntryMap(codeNum, customID, custom, label, rate, feeCode, feeName,
					rmb, operator, jsfs);
			JSONObject jsonObject = JSONObject.fromObject(map);
			jsonArray.add(jsonObject);
			totale = BigDecimalUtil.add(rmb != null && !"".equals(rmb) ? Double.valueOf(rmb) : 0, totale);
		}
		invoice.put("taxAmtTotal", BigDecimalUtil.getRate(BigDecimalUtil.getAmt(totale.toString())));
		invoice.put("noTaxAmtTotal", BigDecimalUtil.getAmt(totale.toString()));
		invoice.put("priceTaxTotal", totale);
		invoice.put("entry", jsonArray);
		makeArray.add(invoice.toString());
		params.put("queryData", makeArray.toString());
		String invoiceJson = httpGo.sendRequest(properties.getProperty("test.open.url"), params);
		JSONObject invoiceOb = JSONObject.fromObject(invoiceJson);
		if ("0000".equals(invoiceOb.getString("result"))) {
			JSONObject invoiceData = JSONObject.fromObject(invoiceOb.getString("data"));
			if ("0".equals(invoiceData.getString("result"))) {
				// 回写数据库里面发票信息
				BisCheckingBook bisCheckingBook = bisCheckingBookDao.find(codeNum);
				if (null == bisCheckingBook) {
					return "对账单不存在！";
				}
				JSONObject resutData = JSONObject.fromObject(invoiceData.getString("data"));
				bisCheckingBook.setCodeNum(codeNum);
				bisCheckingBook.setSrcCustName(custom);
				bisCheckingBook.setInvoiceCode(resutData.getString("invoiceCode"));
				bisCheckingBook.setInvoiceNum(resutData.getString("invoiceNum"));
				bisCheckingBook.setJsfs("X");
				bisCheckingBook.setResult("success");
				bisCheckingBookDao.updateBisCheckBook(bisCheckingBook);
				return "success";
			} else {
				JSONObject msgData = JSONObject.fromObject(invoiceData.getString("data"));
				return msgData.getString("msg");
			}
		}
		closeGold(taxType, properties, httpGo);

		return "现结开票失败！";
	}
	/**
	 * 发票打印处理
	 * @param codeNum
	 * @return
	 */
	public String printDate(HttpServletRequest request) {
		String codeNum = request.getParameter("codeNum");// 对账单号
		String taxType = request.getParameter("taxType");// 税盘类型
		String invoiceNo = request.getParameter("invoiceNo");// 发票号码
		String invoiceCode = request.getParameter("invoiceCode");// 发票代码
		Properties properties = PropertiesUtil.getProperties();// 上传地址处理
		HttpGo httpGo = new HttpGo();
		Map<String, String> params = openGold(taxType, properties, httpGo);
		if (null != params && null != params.get("msg")) {
			return params.get("msg");
		}
		// 打印发票
		JSONArray printArray = new JSONArray();
		printArray.add("QDG065");
		printArray.add("qdg_invoicePrint");
		JSONObject printObject = new JSONObject();
		printObject.put("companyNum", "QDG065");
		printObject.put("goldTaxNum", taxType);
		printObject.put("invoiceCode", invoiceCode);
		printObject.put("invoiceNum", invoiceNo);
		printArray.add(printObject);
		params.put("queryData", printArray.toString());
		String json = httpGo.sendRequest(properties.getProperty("test.open.url"), params);
		JSONObject closeOb = JSONObject.fromObject(json);
		if ("0000".equals(closeOb.getString("result"))) {
			JSONObject data = JSONObject.fromObject(closeOb.getString("data"));
			if ("0".equals(data.getString("result"))) {
				// 回写数据库里面发票信息
				BisCheckingBook bisCheckingBook = bisCheckingBookDao.find(codeNum);
				if (null == bisCheckingBook) {
					return "对账单不存在！";
				}
				bisCheckingBook.setCodeNum(codeNum);
				bisCheckingBook.setResult("print");
				bisCheckingBookDao.updateBisCheckBook(bisCheckingBook);
				return "success";
			} else {
				JSONObject msgData = JSONObject.fromObject(data.getString("data"));
				return msgData.getString("msg");
			}
		}
		closeGold(taxType, properties, httpGo);
		return "error";
	}
	/**
	 * 发票作废接口处理
	 * @param codeNum
	 * @return
	 */
	public String cancelData(HttpServletRequest request) {
		String codeNum = request.getParameter("codeNum");// 对账单号
		String taxType = request.getParameter("taxType");// 税盘类型
		String invoiceNo = request.getParameter("invoiceNo");// 发票号码
		String invoiceCode = request.getParameter("invoiceCode");// 发票代码
		Properties properties = PropertiesUtil.getProperties();// 上传地址处理
		HttpGo httpGo = new HttpGo();
		Map<String, String> params = openGold(taxType, properties, httpGo);
		if (null != params && null != params.get("msg")) {
			return params.get("msg");
		}
		// 发票作废
		JSONArray printArray = new JSONArray();
		printArray.add("QDG065");
		printArray.add("qdg_invoiceCancel");
		JSONObject printObject = new JSONObject();
		printObject.put("companyNum", "QDG065");
		printObject.put("goldTaxNum", taxType);
		printObject.put("invoiceCode", invoiceCode);
		printObject.put("invoiceNum", invoiceNo);
		printArray.add(printObject);
		params.put("queryData", printArray.toString());
		String json = httpGo.sendRequest(properties.getProperty("test.open.url"), params);
		JSONObject closeOb = JSONObject.fromObject(json);
		if ("0000".equals(closeOb.getString("result"))) {
			JSONObject data = JSONObject.fromObject(closeOb.getString("data"));
			if ("0".equals(data.getString("result"))) {
				// 回写数据库里面发票信息
				BisCheckingBook bisCheckingBook = bisCheckingBookDao.find(codeNum);
				if (null == bisCheckingBook) {
					return "对账单不存在！";
				}
				bisCheckingBook.setCodeNum(codeNum);
				bisCheckingBook.setResult("cancel");
				bisCheckingBookDao.updateBisCheckBook(bisCheckingBook);
				return "success";
			} else {
				JSONObject msgData = JSONObject.fromObject(data.getString("data"));
				return msgData.getString("msg");
			}
		}
		closeGold(taxType, properties, httpGo);
		return "发票作废失败！";
	}
	
	public Map<String, String> openGold(String taxType,Properties properties,HttpGo httpGo){
    	JSONObject appInfo = new JSONObject();
		appInfo.put("user","20017011");
		appInfo.put("source","YW-711");
		Map<String, String> params = new HashMap<String, String>();
		params.put("appInfo",appInfo.toString());
		params.put("method","invokeFacade");
		//开通税盘
		JSONArray openArray = new JSONArray();
		openArray.add("QDG065");
		openArray.add("qdg_openGold");
		JSONObject openObject=new JSONObject();
		openObject.put("companyNum","QDG065");
		openObject.put("goldTaxNum",taxType);
		openArray.add(openObject);
		params.put("queryData",openArray.toString());
		String json = httpGo.sendRequest(properties.getProperty("test.open.url"), params);
		JSONObject openOb= JSONObject.fromObject(json);
        //打开税盘接口
		if(null==openOb.getString("data")){
			return null;
		}
    	JSONObject data=JSONObject.fromObject(openOb.getString("data"));
    	if("0".equals(data.getString("result"))){
    		return params;
    	}else{
    		JSONObject result=JSONObject.fromObject(data.getString("data"));
    		Map<String, String> resultMap= new HashMap<String, String>();
    		resultMap.put("msg",result.getString("msg"));
    		return resultMap;
    	}
	}
	/**
	 * 关闭税盘
	 * @return
	 */
	public String closeGold(String taxType,Properties properties,HttpGo httpGo){
    	JSONObject appInfo = new JSONObject();
		appInfo.put("user", "20017011");
		appInfo.put("source", "YW-711");
		Map<String, String> params = new HashMap<String, String>();
		params.put("appInfo",appInfo.toString());
		params.put("method","invokeFacade");
		//开通税盘
		JSONArray closeArray = new JSONArray();
		closeArray.add("QDG065");
		closeArray.add("qdg_closeGold");
		JSONObject closeObject=new JSONObject();
		closeObject.put("companyNum","QDG065");
		closeObject.put("goldTaxNum",taxType);
		closeArray.add(closeObject);
		params.put("queryData",closeArray.toString());
		String json = httpGo.sendRequest(properties.getProperty("test.open.url"), params);
		JSONObject closeOb= JSONObject.fromObject(json);
		if(null==closeOb.getString("result")){
			return "关闭税盘失败！";
		}
		if("0000".equals(closeOb.getString("result"))){
        	JSONObject data=JSONObject.fromObject(closeOb.getString("data"));
        	if("0".equals(data.getString("result"))){
        		return "success";
        	}
		}
		return "关闭税盘失败！";
	}
    /**
     * 根据账单获取应收的总费目
     * @param codeNum
     * @return
     */
    public List<Map<String, Object>> getFeeRmb(String codeNum){
    	return bisCheckingBookDao.getFeeRmb(codeNum);
    }
    
    public String getKeyCode() {
        StringBuilder retKey = new StringBuilder("DZ-");
        User user = UserUtil.getCurrentUser();
        if (user != null) {
            retKey.append(user.getUserCode());
        } else {
            retKey.append("YZH");
        }
        retKey.append("-").append(DateUtils.getDateStr(new Date(), "YYMMdd"));
        int getNum = bisCheckingBookDao.getSequenceId("SEQ_CHEKING_BOOK");
        retKey.append(StringUtils.lpadInt(4, getNum));
        return retKey.toString();
    }

    /**
     * 根据账单号 确认对账信息
     *
     * @param sCode 对账id
     */
    public String addStingBookCodeList(String sCode) {
        String endStr = "error";
        if (sCode != null && !"".equals(sCode)) {
            bisCheckingBookDao.addStingBookCodeList(sCode);
            endStr = "success";
        }
        return endStr;
    }

    /**
     * 根据账单号删除对账信息
     *
     * @param sCode 对账id
     */
    public String delStingBookCodeList(String sCode) {
        String endStr = "error";
        if (sCode != null && !"".equals(sCode)) {
            bisCheckingBookDao.delStingBookCodeList(sCode);
            endStr = "success";
        }
        return endStr;
    }
    /**
     * 关于返回提单号对应的行数小计处理
     * @param checkingBookCode
     * @param feeList
     * @param ntype
     * @param type
     * @return
     */
    public Map<String, Object> getBookRows(String checkingBookCode,List<Map<String,Object>> feeList,String ntype,String type){
    	return bisCheckingBookDao.getBookRows(checkingBookCode, feeList, ntype, type);
    }
    /**
     * 对账单明细导出
     */
    public List<Map<String, Object>> getCheckingBookInfos(String checkingBookCode,List<Map<String,Object>> feeList,String ntype,String type){
    	return bisCheckingBookDao.getCheckingBookInfos(checkingBookCode, feeList, ntype, type);
    }

    /**
     * 对账单明细导出
     */
    public List<Map<String, Object>> getRepCheckingBookInfo(String checkingBookCode, String type) {
        return bisCheckingBookDao.getRepCheckingBookInfo(checkingBookCode, type);
    }
   
    public List<Map<String, Object>> getRepCheckingBookInfo2(String codeNum) {
        return bisCheckingBookDao.getRepCheckingBookInfo2(codeNum);
    }
    
    public List<Map<String, Object>> getRepCheckingBookInfo11(String codeNum) {
        return bisCheckingBookDao.getRepCheckingBookInfo11(codeNum);
    }
    /**
     * 获取excel导出列---费目明细
     */
    public List<Map<String, Object>> getRepCheckingBookHead(String checkingBookCode) {
        return bisCheckingBookDao.getRepCheckingBookHead(checkingBookCode);
    }
    
    public List<Map<String, Object>> getRepCheckingBookHead11(String checkingBookCode) {
        return bisCheckingBookDao.getRepCheckingBookHead11(checkingBookCode);
    }

    public List<Map<String, Object>> getTotal(String codeNum) {
        return bisCheckingBookDao.getTotal(codeNum);
    }

    public List<Map<String, Object>> connectBillNum(String linkId) {
        return bisCheckingBookDao.connectBillNum(linkId);
    }

}
