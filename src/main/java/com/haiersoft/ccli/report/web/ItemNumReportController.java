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
import com.haiersoft.ccli.report.service.ItemNumReportService;

/**
 * 
 * @author Mazy
 * @ClassName: ItemNumReportController
 * @Description: 项号报表
 * @date 2016年7月1日 下午2:35:31
 */
@Controller
@RequestMapping("report/itemnum")
public class ItemNumReportController extends BaseController{
	
	@Autowired
	private ItemNumReportService itemNumReportService;
	
	
	
	@RequestMapping(value="itemnumlist", method = RequestMethod.GET)
	public String list() {
		return "report/itemNumReport";
	}
	
	/***
	 * 异步获取项号统计数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="itemNumJson",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getCustomsListJson(HttpServletRequest request) {
		Map<String,Object> returnMap =null;
		int pageNumber=Integer.valueOf(request.getParameter("pageNumber")!=null?request.getParameter("pageNumber").toString():"1");
		int pageSize=Integer.valueOf(request.getParameter("pageSize")!=null?request.getParameter("pageSize").toString():"20");
		String staTime=request.getParameter("starTime")!=null?request.getParameter("starTime").toString():"";
		String endTime=request.getParameter("endTime")!=null?request.getParameter("endTime").toString():"";
		String itemNum=request.getParameter("itemNum")!=null?request.getParameter("itemNum").toString():"";
		returnMap=itemNumReportService.getItemNumReportInfo(staTime, endTime, pageNumber, pageSize,itemNum);
		if(returnMap==null){
			returnMap = new HashMap<String,Object>();
		}
		return  returnMap;
	}
	
	/**
	 * 项号统计数据导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "itemnumexcel")
	@ResponseBody
	public void itemnumexcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String,Object> returnMap =null;
		List<Map<String,Object> > getList=null;
		String staTime=request.getParameter("starTime")!=null?request.getParameter("starTime").toString():"";
		String endTime=request.getParameter("endTime")!=null?request.getParameter("endTime").toString():"";
		String itemNum=request.getParameter("itemNum")!=null?request.getParameter("itemNum").toString():"";
		returnMap=itemNumReportService.getItemNumReportInfo(staTime, endTime, 1, 0,itemNum);
		if(returnMap!=null && returnMap.size()>0){
			getList=(List<Map<String, Object>>) returnMap.get("rows");
		}
		String formatFileName = URLEncoder.encode("项号报表" +".xls","UTF-8");
    	ExcelUtil excelUtil=new ExcelUtil();
    	String  filePath=PropertiesUtil.getPropertiesByName("filepath", "application");
    	String srcPath =null; 
    	String desPath=null;
    	if(filePath==null || "".equals(filePath)){
    		filePath = request.getSession().getServletContext().getRealPath("/");
    		srcPath = filePath+"WEB-INF\\classes\\exceltemplate\\itemNumReport.xls";
        	desPath = filePath+"WEB-INF\\classes\\excelpost\\itemNumReport.xls";
    	}else{
    		srcPath = filePath+"exceltemplate\\itemNumReport.xls";
        	desPath = filePath+"excelpost\\itemNumReport.xls";
    	}
    	//System.out.println(srcPath);
    	excelUtil.setSrcPath(srcPath);
    	excelUtil.setDesPath(desPath);
    	excelUtil.setSheetName("项号报表");
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
    				excelUtil.setCellStrValue(starRows+i, 9, getMap.get("WEIGHT")!=null?getMap.get("WEIGHT").toString():"");
    				excelUtil.setCellStrValue(starRows+i, 10, getMap.get("KCTOTALWEIGHT")!=null?getMap.get("KCTOTALWEIGHT").toString():"");
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
