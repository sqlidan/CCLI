package com.haiersoft.ccli.report.web;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.service.PaymentDetailReportService;

/**
 * 
 * @author cuij
 * @ClassName: PaymentDetailReportController
 * @Description: 业务付款申请单明细表
 * @date 2016年6月24日16:07:45
 */
@Controller
@RequestMapping("report/paymentDetailReportStock")
public class PaymentDetailReportController extends BaseController{
	
	@Autowired
	private PaymentDetailReportService paymentDetailReportService;
	
	/**
	 * 
	 * @author cuij
	 * @Description:  业务付款申请单明细表页面
	 * @date 2016年6月24日16:59:35 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="paymentReportList", method = RequestMethod.GET)
	public String paymentReportList() {
		return "report/paymentReportList";
	}
	
	/**
	 * 
	 * @author cuij
	 * @Description: 业务付款申请单明细表查询
	 * @date 2016年6月24日17:01:19 
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="detail",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getData(HttpServletRequest request) {
		String payId=request.getParameter("payId")!=null?request.getParameter("payId").toString():"";//业务付款单号
		String clientName=request.getParameter("clientName")!=null?request.getParameter("clientName").toString():"";//客户名称
		String payee=request.getParameter("payee")!=null?request.getParameter("payee").toString():"";//收款人
		String billNum=request.getParameter("billNum")!=null?request.getParameter("billNum").toString():"";//提单号
		String searchStrTime=request.getParameter("searchStrTime")!=null?request.getParameter("searchStrTime").toString():"";//开始日期
		String searchEndTime=request.getParameter("searchEndTime")!=null?request.getParameter("searchEndTime").toString():"";//结束日期
		List<Map<String, Object>> listmap = paymentDetailReportService.searchStockReport(payId, clientName, payee, billNum, searchStrTime, searchEndTime);
		if(listmap==null){
			listmap=new ArrayList<Map<String, Object>>();
		}
		return listmap;
	}
	@RequestMapping(value="detailhead",method = RequestMethod.GET)
	@ResponseBody	
	public List<Map<String, Object>> getData2(HttpServletRequest request) {
		String payId=request.getParameter("payId")!=null?request.getParameter("payId").toString():"";//业务付款单号
		String clientName=request.getParameter("clientName")!=null?request.getParameter("clientName").toString():"";//客户名称
		String payee=request.getParameter("payee")!=null?request.getParameter("payee").toString():"";//收款人
		String billNum=request.getParameter("billNum")!=null?request.getParameter("billNum").toString():"";//提单号
		String searchStrTime=request.getParameter("searchStrTime")!=null?request.getParameter("searchStrTime").toString():"";//开始日期
		String searchEndTime=request.getParameter("searchEndTime")!=null?request.getParameter("searchEndTime").toString():"";//结束日期
		if((payId==null || "".equals(payId)) && (clientName==null ||"".equals(clientName)) && (payee==null|| "".equals(payee)) && (billNum==null || "".equals(billNum)) && (searchStrTime==null || "".equals(searchStrTime)) && (searchEndTime==null || "".equals(searchEndTime))){
			searchEndTime=DateUtils.getTimeMonth();
		}
		List<Map<String, Object>> listmap = paymentDetailReportService.searchStockReport(payId, clientName, payee, billNum, searchStrTime, searchEndTime);
		List<Map<String, Object>> returnlistmap=new ArrayList<Map<String, Object>>();
		returnlistmap= getExcelHead(listmap);
		return returnlistmap;
	}
	/**
	 * 
	 * @author cuij
	 * @Description:  导出业务付款申请单明细表
	 * @date 2016年6月24日17:04:00 
	 * @param paymentDrtailReportStock
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
 */	

	//excel导出对账单明细
			@RequestMapping(value = "exportPaymentDetailReportStockExcel")
			@ResponseBody
			public void export(HttpServletRequest request, HttpServletResponse response) throws Exception{
				String payId=request.getParameter("payId")!=null?request.getParameter("payId").toString():"";//业务付款单号
				String clientName=request.getParameter("clientName")!=null?request.getParameter("clientName").toString():"";//客户名称
				String payee=request.getParameter("payee")!=null?request.getParameter("payee").toString():"";//收款人
				String billNum=request.getParameter("billNum")!=null?request.getParameter("billNum").toString():"";//提单号
				String searchStrTime=request.getParameter("searchStrTime")!=null?request.getParameter("searchStrTime").toString():"";//开始日期
				String searchEndTime=request.getParameter("searchEndTime")!=null?request.getParameter("searchEndTime").toString():"";//结束日期
				if((payId==null || "".equals(payId)) && (clientName==null ||"".equals(clientName)) && (payee==null|| "".equals(payee)) && (billNum==null || "".equals(billNum)) && (searchStrTime==null || "".equals(searchStrTime)) && (searchEndTime==null || "".equals(searchEndTime))){
					searchEndTime=DateUtils.getTimeMonth();
				}
		    		List<Map<String, Object>> listmap = paymentDetailReportService.searchStockReport(payId, clientName, payee, billNum, searchStrTime, searchEndTime);
		    		List<Map<String, Object>> headListMap=new ArrayList<Map<String, Object>>();
		    		if(listmap !=null && listmap.size()>0){
		    			headListMap= getExcelHead(listmap);
		    		}
		    		String formatFileName = URLEncoder.encode("业务付款申请单明细表" +".xls","UTF-8");
		        	ExcelUtil excelUtil=new ExcelUtil();
		        	String  filePath=PropertiesUtil.getPropertiesByName("filepath", "application");
		        	String srcPath =""; 
		        	String desPath=null;
		        	if(filePath==null || "".equals(filePath)){
		        		filePath = request.getSession().getServletContext().getRealPath("/");
		            		srcPath = filePath+"WEB-INF\\classes\\exceltemplate\\paymentDetailReport.xls";
		            	    desPath = filePath+"WEB-INF\\classes\\excelpost\\paymentDetailReport.xls";
		        	}else{
		            		srcPath = filePath+"exceltemplate\\paymentDetailReport.xls";
		            	    desPath =  filePath+"excelpost\\paymentDetailReport.xls";
		        	}
		        	excelUtil.setSrcPath(srcPath);
		        	excelUtil.setDesPath(desPath);
		        	excelUtil.setSheetName("Sheet1");
		        	excelUtil.getSheet();
		        	
		        	
		        	int nLrow=0;
		        	if(headListMap!=null && headListMap.size()>0){
		        		for(Map<String,Object> getMap:headListMap){
		        			excelUtil.setCellStrValue(8,nLrow, getMap.get("lab")!=null?getMap.get("lab").toString():"费目名称");
		        			getMap.put("nlrow",nLrow);//添加该费目类在excel中是第几列
		        			nLrow++;
		        		}
		        	}
		        	if(listmap!=null && listmap.size()>0){
		        		Map<String,Object> map=null;
		        		//Map<String ,Object> keyMap=new HashMap<String ,Object>();//保存分组唯一表示和行数
		        		String getVal=null;
		        		Integer getNL=0;
		        		for(int i=0;i<listmap.size();i++){
		        			map=listmap.get(i);
		        			for (String key : map.keySet()) {
		        				getVal=map.get(key)!=null?map.get(key).toString():"";
		        				if(!"".equals(getVal)){
		        					getNL=getNum(headListMap,key);
			        				if(getNL!=null){
			        					excelUtil.setCellStrValue(9+i,getNL, map.get(key)!=null?map.get(key).toString():"");
			        				}
		        				}
		        				
		   					 // System.out.println("key= "+ key + " and value= " + map.get(key));
		   				    }
		        		}//end for
		        	}
		        	/** 
		        	excelUtil.setCellStrValue(myNum+3+addRow+1,6, "如无问题，请尽快安排付款。谢谢！");
		        	excelUtil.setCellStrValue(myNum+3+addRow+2,6, "公司名称：青岛港怡之航冷链物流有限公司");
		        	excelUtil.setCellStrValue(myNum+3+addRow+3,6, "人民币账户：802790200002726");
		        	excelUtil.setCellStrValue(myNum+3+addRow+4,6, "开户行名称：青岛银行");
		        	excelUtil.setCellStrValue(myNum+3+addRow+5,6, "纳税人识别号：91370220395949850B");
		        	excelUtil.setCellStrValue(myNum+3+addRow+6,6, "地  址：山东省青岛市保税区北京路41号203房间");
		        	excelUtil.setCellStrValue(myNum+3+addRow+7,6, "电  话：0532-82987866");
		        	*/
		        	excelUtil.exportToNewFile();
		        	FileInputStream in = new FileInputStream(new File(desPath));
		        	int len = 0;
		        	byte[] buffer = new byte[1024];
		        	response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName+"\"");// 设定输出文件头
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
			
			/**
			 * 从费目头部中获取费目所在行，和在list中的index
			 * @param list 
			 * @param lab 费目唯一标记
			 * @return
			 */
			private Integer getNum(List<Map<String,Object>> list,String lab){
				Integer retNum=null;
				if(list!=null && list.size()>0 && lab!=null){
					Map<String,Object> map=null;
					for(int i=0;i<list.size();i++){
						map=list.get(i);
						if(lab.equals(map.get("lab").toString())){
							retNum= Integer.valueOf(map.get("nlrow")!=null?map.get("nlrow").toString():"0");
							break;
						}
					}
					if(null==retNum){
						for(int k=0;k<list.size();k++){
							map=list.get(k);
							if(lab.equals(map.get("labe").toString())){
								retNum= Integer.valueOf(map.get("nlrow")!=null?map.get("nlrow").toString():"0");
								break;
							}
						}
					}
				}
				return retNum;
			}
	
//取excel表头方法
	private List<Map<String, Object>> getExcelHead(List<Map<String, Object>> listmap) {
		//return listmap;
		List<Map<String, Object>> returnlistmap=new ArrayList<Map<String, Object>>();
		if(listmap!=null && !listmap.isEmpty()){
			 Map<String, Object> checkmap = new HashMap<String, Object>();
			 checkmap.put("PAY_ID",1);
			 checkmap.put("CLIENT_NAME", 2);
			 checkmap.put("SELL_MAN", 3);
			 checkmap.put("BILL_NUM", 4);
			 checkmap.put("DL_CLIENT_NAME", 5);
			 checkmap.put("CTN_AMOUNT", 6);
			 checkmap.put("ASK_MAN", 7);
			 checkmap.put("ASK_DATE", 8);
		Map<String, Object> map=listmap.get(0);
			if(map!=null && !map.isEmpty()){
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("lab", "业务付款单号");
				map1.put("labe", "PAY_ID");
				returnlistmap.add(map1);
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("lab", "客户名称");
				map2.put("labe", "CLIENT_NAME");
				returnlistmap.add(map2);
				Map<String, Object> map3 = new HashMap<String, Object>();
				map3.put("lab", "销售人员");
				map3.put("labe", "SELL_MAN");
				returnlistmap.add(map3);
				Map<String, Object> map4 = new HashMap<String, Object>();
				map4.put("lab", "提单号");
				map4.put("labe", "BILL_NUM");
				returnlistmap.add(map4);
				Map<String, Object> map5 = new HashMap<String, Object>();
				map5.put("lab", "收款人");
				map5.put("labe", "DL_CLIENT_NAME");
				returnlistmap.add(map5);
				Map<String, Object> map6 = new HashMap<String, Object>();
				map6.put("lab", "箱量");
				map6.put("labe", "CTN_AMOUNT");
				returnlistmap.add(map6);
				Map<String, Object> map7 = new HashMap<String, Object>();
				map7.put("lab", "申请人");
				map7.put("labe", "ASK_MAN");
				returnlistmap.add(map7);
				Map<String, Object> map8 = new HashMap<String, Object>();
				map8.put("lab", "申请日期");
				map8.put("labe", "ASK_DATE");
				returnlistmap.add(map8);
				
				Map<String, Object> addmap=null;
				  for (String key : map.keySet()) {
					  if(checkmap.get(key)==null){
						  addmap=new HashMap<String, Object>();
						  addmap.put("lab", key);
						  returnlistmap.add(addmap);
					  }
				
					  System.out.println("key= "+ key + " and value= " + map.get(key));
				  }
			}
			
		}
		return returnlistmap;
	}
    
//存储过程
	@RequestMapping(value="cunchu",method = RequestMethod.POST)
	@ResponseBody
	 public String cunchu(){  
//		    String driver = "oracle.jdbc.driver.OracleDriver";  
//		    String strUrl = "jdbc:oracle:thin:@192.168.132.145:1521:orcl";  
		 	String driver = PropertiesUtil.getPropertiesByName("jdbc.driver", "application");
		    String strUrl = PropertiesUtil.getPropertiesByName("jdbc.url", "application");
		    String user = PropertiesUtil.getPropertiesByName("jdbc.username", "application");
		    String password = PropertiesUtil.getPropertiesByName("jdbc.password", "application");
		    Statement stmt = null;  
		    ResultSet rs = null;  
		    Connection conn = null;  
		    String testPrint="0";
		    try {  
		      Class.forName(driver);  
		      conn =  DriverManager.getConnection(strUrl, user, password);  
		      CallableStatement proc = null;  
		      proc = conn.prepareCall("{ call ROW_TO_COL(?,?,?,?,?,?,?,?,?,?) }");  
		      proc.setString(1, "select py.pay_id,pyin.client_name,pyin.sell_man,pyin.bill_num,py.client_name as dl_client_name,pyin.CTN_AMOUNT,py.ASK_MAN,py.ASK_DATE,pyin.FEE_CODE,pyin.fee_name,pyin.TOTEL_PRICE from bis_pay py left join bis_pay_info pyin on py.pay_id=pyin.pay_id" );  
		      proc.setString(2, "pay_id,client_name,max(sell_man) sell_man, bill_num,dl_client_name, ctn_amount,max(ask_man) ask_man ,max(ask_date) ask_date");  
		      proc.setString(3, "pay_id,client_name,bill_num,dl_client_name,ctn_amount");  
		      proc.setString(4, "fee_name" );  
		      proc.setString(5, "TOTEL_PRICE" );  
		      proc.setString(6, "sum");  
		      proc.setString(7, "fee_name" );  
		      proc.setString(8, "pay_id,bill_num" );  
		      proc.setString(9, "0" );  
		      proc.setString(10,"PaymentDetailReport" );  
		      
		      proc.execute();  
//		      testPrint = proc.getString(2);  
//		      System.out.println("存储过程返回的值是："+testPrint);  
		    }  
		    catch (SQLException ex2) {  
		      ex2.printStackTrace();  
		    }  
		    catch (Exception ex2) {  
		      ex2.printStackTrace();  
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
		    return testPrint;
		  }  
}
    

