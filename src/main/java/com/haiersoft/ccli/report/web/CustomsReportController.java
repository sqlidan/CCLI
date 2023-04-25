package com.haiersoft.ccli.report.web;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
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
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.service.CustomsReportService;
/**
 * 
 * @author Mazy
 * @ClassName: CustomsReportController
 * @Description: 海关报表
 * @date 2016年7月1日 下午2:35:31
 */
@Controller
@RequestMapping("report/customs")
public class CustomsReportController extends BaseController{
	
	@Autowired
	private CustomsReportService customsReportService;
	
	
	
	@RequestMapping(value="customslist", method = RequestMethod.GET)
	public String list() {
		return "report/customsReportList";
	}
	
	/***
	 * 海关报表统计
	 * @param request
	 * @return
	 */
	@RequestMapping(value="customsJson",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getCustomsListJson(HttpServletRequest request) {
		Map<String,Object> returnMap =null;
		int pageNumber=Integer.valueOf(request.getParameter("pageNumber")!=null?request.getParameter("pageNumber").toString():"1");
		int pageSize=Integer.valueOf(request.getParameter("pageSize")!=null?request.getParameter("pageSize").toString():"20");
		String staTime=request.getParameter("starTime")!=null?request.getParameter("starTime").toString():"";
		String endTime=request.getParameter("endTime")!=null?request.getParameter("endTime").toString():"";
		returnMap=customsReportService.getCustomsReportInfo(staTime, endTime, pageNumber, pageSize);
		if(returnMap==null){
			returnMap = new HashMap<String,Object>();
		}
		return  returnMap;
	}
	
	/**
	 * 海关报表统计
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "customsexcel")
	@ResponseBody
	public void customsexcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String,Object> returnMap =null;
		List<Map<String,Object> > getList=null;
		String staTime=request.getParameter("starTime")!=null?request.getParameter("starTime").toString():"";
		String endTime=request.getParameter("endTime")!=null?request.getParameter("endTime").toString():"";
		returnMap=customsReportService.getCustomsReportInfo(staTime, endTime, 1, 0);
		if(returnMap!=null && returnMap.size()>0){
			getList=(List<Map<String, Object>>) returnMap.get("rows");
		}
		String formatFileName = URLEncoder.encode("海关报关报表" +".xls","UTF-8");
    	ExcelUtil excelUtil=new ExcelUtil();
    	String  filePath=PropertiesUtil.getPropertiesByName("filepath", "application");
    	String srcPath =null; 
    	String desPath=null;
    	if(filePath==null || "".equals(filePath)){
    		filePath = request.getSession().getServletContext().getRealPath("/");
    		srcPath = filePath+"WEB-INF\\classes\\exceltemplate\\customsReport.xls";
        	desPath = filePath+"WEB-INF\\classes\\excelpost\\customsReport.xls";
    	}else{
    		srcPath = filePath+"exceltemplate\\customsReport.xls";
        	desPath = filePath+"excelpost\\customsReport.xls";
    	}
    	//System.out.println(srcPath);
    	excelUtil.setSrcPath(srcPath);
    	excelUtil.setDesPath(desPath);
    	excelUtil.setSheetName("海关报关报表");
    	excelUtil.getSheet();
    	int starRows=9;
    	if(getList!=null && getList.size()>0){
    		Map<String,Object> getMap=null;
    		for(int i=0;i<getList.size();i++){
    			getMap=getList.get(i);
    			if(getMap!=null && getMap.size()>0){
    				excelUtil.setCellStrValue(starRows+i, 0, getMap.get("ITEM_NUM")!=null?getMap.get("ITEM_NUM").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 1, getMap.get("CARGO_NAME")!=null?getMap.get("CARGO_NAME").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 2, getMap.get("DECLARE_TIME")!=null?getMap.get("DECLARE_TIME").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 3, getMap.get("CD_NUM")!=null?getMap.get("CD_NUM").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 4, getMap.get("BILL_NUM")!=null?getMap.get("BILL_NUM").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 5, getMap.get("NET_WEIGHT")!=null?getMap.get("NET_WEIGHT").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 6, getMap.get("OUTDECLARETIME")!=null?getMap.get("OUTDECLARETIME").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 7, getMap.get("OUTCDNUM")!=null?getMap.get("OUTCDNUM").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 8, getMap.get("OUTNETWEIGHT")!=null?getMap.get("OUTNETWEIGHT").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 9, getMap.get("UNIT_PRICE")!=null?getMap.get("UNIT_PRICE").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 10, getMap.get("WEIGHT")!=null?getMap.get("WEIGHT").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 11, getMap.get("NUM")!=null?getMap.get("NUM").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 12, getMap.get("PRICES")!=null?getMap.get("PRICES").toString():"");
    			}
    		}
		}
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
	
}
