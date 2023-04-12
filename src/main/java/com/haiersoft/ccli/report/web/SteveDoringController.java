package com.haiersoft.ccli.report.web;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.service.SteveDroingReportService;
import com.haiersoft.ccli.system.utils.ExcelUtils;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * 
 * @author  slh 
 * @Description: 装卸队统计报表
 * @date 2016年6月28日  
 */
@Controller
@RequestMapping("report/stevedoring")
public class SteveDoringController extends BaseController{
	
	@Autowired
	private SteveDroingReportService steveDroingReportService;
	
	/**
	 * 入库默认页面
	 */
	@RequestMapping(value="stevedro", method = RequestMethod.GET)
	public String enterstevedro() {
		return "report/SteveDroing/SteveDoringReportList";
	} 
	
	/***
	 * 入库装卸队 装卸数量统计数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="Json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getEnterSteveListJson(HttpServletRequest request) {
		int pageNumber=Integer.valueOf(request.getParameter("pageNumber")!=null?request.getParameter("pageNumber").toString():"1");
		int pageSize=Integer.valueOf(request.getParameter("pageSize")!=null?request.getParameter("pageSize").toString():"20");
		String clientId = request.getParameter("clientId")!=null?request.getParameter("clientId").toString():"";
		String statTime=request.getParameter("startTime")!=null?request.getParameter("startTime").toString():"";
		String endTime=request.getParameter("endTime")!=null?request.getParameter("endTime").toString():"";
        String reportType =request.getParameter("reportm")!=null?request.getParameter("reportm").toString():""; //报表类型
		return steveDroingReportService.getSteveReportInfo(reportType,clientId,statTime, endTime, pageNumber, pageSize);
	} 
	
	/**
	 * 入库装卸导出execl
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "toexcel")
	@ResponseBody
	public String customsexcel(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
		String reportType = request.getParameter("reporttype")!=null?request.getParameter("reporttype").toString():""; //获取报表类型
		String clientId = request.getParameter("clientId")!=null?request.getParameter("clientId").toString():""; 
		String statTime=request.getParameter("startTime")!=null?request.getParameter("startTime").toString():"";
		String endTime=request.getParameter("endTime")!=null?request.getParameter("endTime").toString():"";   
		Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> returnMap = new ArrayList<Map<String,Object>>();
		TemplateExportParams params = new TemplateExportParams("exceltemplate/SteveDroingReport.xls"); 
		reportType=reportType.trim();
		Map<String, Object> mapa= steveDroingReportService.getSteveReportInfo(reportType,clientId,statTime, endTime, 1, 0); 
		//reportType =reportType.length()<=0?"":(reportType.equals("1")?"入库":(reportType.equals("2")?"出库":(reportType.equals("3")?"倒箱":(reportType.equals("4")?"日工":""))));
		
		String excelName = "装卸队装卸数量统计.xls";
		if(mapa!=null && mapa.size()>0)
		   returnMap=(List<Map<String, Object>>) mapa.get("rows");
		map.put("datefw", statTime+"~"+endTime);  
		if(null != returnMap && returnMap.size() > 0)  
			map.put("maplist", returnMap); 

		Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.getSheetAt(0).setForceFormulaRecalculation(true);//强制执行公式
	    
		String formatFileName = new String(excelName.getBytes("GB2312"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName +"\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
		os.close(); // 关闭流
		return null;
    }
	
	/**
	 * 入库装卸汇总导出execl
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "tosumexcel")
	@ResponseBody
	public String customssumexcel(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
		String reportType = request.getParameter("reporttype")!=null?request.getParameter("reporttype").toString():""; //获取报表类型
		String clientId = request.getParameter("clientId")!=null?request.getParameter("clientId").toString():""; 
		String statTime=request.getParameter("startTime")!=null?request.getParameter("startTime").toString():"";
		String endTime=request.getParameter("endTime")!=null?request.getParameter("endTime").toString():"";   
        String path = request.getSession().getServletContext().getRealPath("/");//获取web项目的路径
        long nowTime = System.currentTimeMillis();
        //获取客户信息
        List<String> custlist=steveDroingReportService.findCustList(reportType, clientId, statTime, endTime);
        //获取类型list的小计
        List<Object[]> lxList=steveDroingReportService.findLxList(custlist, reportType, clientId, statTime, endTime);
        //获取总统计数
        List<Object[]> sumList=steveDroingReportService.findSumList(custlist, reportType, clientId, statTime, endTime);
        
        Map lxmap=new HashMap();
        for (int i = 0; i < lxList.size(); i++) {
        	Object[] obj=lxList.get(i);
            List<Object[]> getlist = steveDroingReportService.findSteveReport(obj[0].toString(),obj[1].toString(),custlist,reportType,clientId,statTime,endTime);
            lxmap.put(obj[3], getlist);
		}
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        if(endTime!=null&&!"".equals(endTime)){
           date=formatter.parse(endTime);
        }else{
           date=new Date();
        }
        List<String> numlist=new ArrayList<String>();
        for (int i = 0; i < custlist.size()*2; i++) {
        	numlist.add((4+i)+"");
		}
        String formatFileName = URLEncoder.encode("装卸队装卸汇总数量统计" + ".xls", "UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat ftter = new SimpleDateFormat("yyyy年MM月");
        map.put("month",ftter.format(date));
        map.put("custlist",custlist);
        map.put("numlist", numlist);
        map.put("sumList",sumList);
        map.put("user",UserUtil.getCurrentUser().getName());
        map.put("lxList",lxList);
        map.put("lxmap", lxmap);
		map.put("date", statTime+"~"+endTime);
		File file=ExcelUtils.createExcel(path,map,"myExcel","stevedoring",nowTime);
		InputStream in = null;
		try {
		      response.reset();
		      response.setCharacterEncoding("utf-8");
		      response.setContentType("application/msexcel");// 定义输出类型
		      response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName +"\"");// 设定输出文件头
		      in = new FileInputStream(file);
		      byte[] b = new byte[1024];
		      int c;
		      while ((c = in.read(b)) != -1){
		        response.getOutputStream().write(b, 0, c);
		      }
		      in.close();
		    }catch (Exception e) {
		      e.printStackTrace();
		      if (in != null)
		        try {
		          in.close();
		        }catch (IOException localIOException){
		        }
		    }
			finally {
				if (in != null) {
					try {
						in.close();
					}catch (IOException localIOException1) {
					}
				}
			}
		return null;
    }
	 
}
