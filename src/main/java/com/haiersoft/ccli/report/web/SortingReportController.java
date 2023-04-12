package com.haiersoft.ccli.report.web;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.CreatPDFUtils;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.MyFileUtils;
import com.haiersoft.ccli.common.utils.MyPDFUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.SortingReportService;
import com.itextpdf.text.PageSize;

/**
 * 
 * @author Connor.M
 * @ClassName: SortingReportController
 * @Description: 分拣报告书
 * @date 2016年4月25日 上午11:23:05
 */
@Controller
@RequestMapping("report/sorting")
public class SortingReportController extends BaseController{
	
	@Autowired
	private SortingReportService sortingReportService;
	
	/**
	 * 默认页面
	 */
	@RequestMapping(value="sortingList", method = RequestMethod.GET)
	public String sortingList() {
		return "report/sorting/sortingReportList";
	}

	/**
	 *
	 * @author Connor.M
	 * @Description: 导出分拣报告书
	 * @date 2016年4月25日 下午1:35:18 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping("exportSortingExcel")
	@ResponseBody
	public void exportSortingExcel(@Valid @ModelAttribute @RequestBody Stock stock, HttpServletRequest request, HttpServletResponse response) throws Exception{	
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		TemplateExportParams params = new TemplateExportParams("exceltemplate/sortingReport.xls");
		String excelName = "分拣报告书.xls";
		map.put("customer", "");
		if("1".equals(stock.getReportType())){//普通
			excelName =  "分拣报告书.xls";
			params = new TemplateExportParams("exceltemplate/sortingReport.xls");
			listMap = sortingReportService.searchSortingReport(stock);

		}else if("2".equals(stock.getReportType())){//日本

			excelName =  "分拣报告书-日本.xls";
			params = new TemplateExportParams("exceltemplate/sortingReportRB.xls");
			listMap = sortingReportService.searchSortingReportRB(stock);
			
		}else if("3".equals(stock.getReportType())){//OTE
			
			excelName =  "分拣报告书-OTE.xls";
			params = new TemplateExportParams("exceltemplate/sortingReportOTE.xls");
			listMap = sortingReportService.searchSortingReportOTE(stock);
		}
		
		if(null != listMap && listMap.size() > 0){
			String customer = listMap.get(0).get("CLIENT_NAME").toString();
			map.put("customer", customer);
			map.put("maplist", listMap);
		}
        
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.getSheetAt(0).setForceFormulaRecalculation(true);//强制执行公式
        
        String formatFileName = new String(excelName.getBytes("GB2312"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName +"\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}
	
	/**
	 * @Description:  导出分拣报告书 PDF
	 * @param stock
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping("exportSortingPDF")
	@ResponseBody
	public void exportSortingPDF(@Valid @ModelAttribute @RequestBody Stock stock, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(stock.getReportType()!=null && !"".equals(stock.getReportType())){
			List<Map<String, Object>>  getlist = null;
       		String[] headCN={"货物描述","SKU","提单号","入库号","MR/集装箱号","货物状态","入库日期","件数","总净重(KGS)","总毛重(KGS)"};
       		String[] headEN={"Description of cargo","SKU","B/L NO.","Inbound NO.","MR/CTN NO.","State of cargo","Inbound Date","Qty","Net Weight","Gross Weight"};
       		String pdfTitle = "分拣报告书";
       		StringBuffer pdfCN=new StringBuffer();
       		if ("1".equals(stock.getReportType())) {
            	pdfCN.append( 
            				 ""
            				+ "货物描述"+"                                                    "
            				+"SKU"+"                                                           "
            				+"提单号"+"                                                "
            				+"MR/集装箱号"+"                                        "
            				+"货物状态"+"                        "
            				+"入库日期"+"                   "
            				+"件数"+"                          "
            				+"总净重(KGS)"+"           "
            				+"总毛重(KGS)"+"               "
            				);
            }
            if ("2".equals(stock.getReportType())) {
            	pdfCN.append( ""
        				+ "货物描述"+"                                             "
        				+"SKU"+"                                                       "
        				+"提单号"+"            		              		               "
        				+"入库号"+"                        "
        				+"MR/集装箱号"+"                               "
        				+"货物状态"+"                  "
        				+"入库日期"+"                   "
        				+"件数"+"                             "
        				+"总净重(KGS)"+"    	          				"
        				+"总毛重(KGS)"+"               "
        				);
            }
            if ("3".equals(stock.getReportType())) {
            	pdfCN.append( ""
        				+"货物描述"+"                           "
        				+"SKU"+"                                       "
        				+"提单号"+"                        "
        				+"入库号"+"          "
        				+"MR/集装箱号"+"                  "
        				+"货物状态"+"       "
        				+"入库日期"+"      "
        				+"件数"+"         "
        				+"总净重(KGS)"+"  "
        				+"总毛重(KGS)"+"       "
        				+"生产日期"+"         "
        				+"规格"+"              "
        				+"项目号"+"          "
        				+"船名批次"+"            "
        				+"MSC"+"          "
        				);
            }
       		String path=request.getSession().getServletContext().getRealPath("/");//获取web项目的路径"d://exceltemplate//syshtm.html";
       		String pathHtml=path+"//sortsyshtm.html";
       		String pathPdf=path+"//sortsyspdf.pdf";
       		StringBuffer sbHtml=new StringBuffer();
       		 
       		sbHtml.append("<div  style=\"height:5px;\"></div>");
       		sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
       		if("1".equals(stock.getReportType())){
       			getlist = sortingReportService.searchSortingReport(stock);
       		}
       		if("2".equals(stock.getReportType())){
       			getlist = sortingReportService.searchSortingReportRB(stock);
       		}
       		if("3".equals(stock.getReportType())){
       			getlist = sortingReportService.searchSortingReportOTE(stock);
       		}
       		String customer =null;
       		//填充标题头
       		sbHtml.append("<tr style=\"height:30px; \">");
       		for(String lab:headCN){
       			if("1".equals(stock.getReportType()) && "入库号".equals(lab)){
     				 
       			}else{
       				if("货物描述".equals(lab)){
                		sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
                	}else if("SKU".equals(lab)||"提单号".equals(lab)||"MR/集装箱号".equals(lab)){
                		sbHtml.append("<td  width=\"250px\">").append(lab).append("</td>");
                	}else{
                		sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                	}
       			} 
       		}
       		if("3".equals(stock.getReportType()) ){
   				sbHtml.append("<td class=\"htd\">").append("生产日期").append("</td>");
   				sbHtml.append("<td class=\"htd\">").append("规格").append("</td>");
   				sbHtml.append("<td class=\"htd\">").append("项目号").append("</td>");
   				sbHtml.append("<td class=\"htd\">").append("船名批号").append("</td>");
   				sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
   			}
       		sbHtml.append("</tr>");
       		sbHtml.append("<tr style=\"height:30px; \">");
       		for(String lab:headEN){
       			if("1".equals(stock.getReportType()) && "Inbound NO.".equals(lab)){
      				 
       			}else{
       				sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
       			}
       		}
       		if("3".equals(stock.getReportType())){
   				sbHtml.append("<td class=\"htd\">").append("Produce Date").append("</td>");
   				sbHtml.append("<td class=\"htd\">").append("Standard").append("</td>");
   				sbHtml.append("<td class=\"htd\">").append("Pro No.").append("</td>");
   				sbHtml.append("<td class=\"htd\">").append("Lot No.").append("</td>");
   				sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
   			}
       		sbHtml.append("</tr>");
       		int nTJ=5;
       		//填充内容
       		if(getlist!=null && getlist.size()>0){
       			Double sumPice=0d;
        		Double sumNet=0d;
        		Double sumGross=0d;
       			Map<String,Object> getMap=null;
       			for(int i=0;i<getlist.size();i++){
       				getMap=getlist.get(i);
       				if(null==customer){
       					customer = getMap.get("CLIENT_NAME").toString();
       				}
        			if(getMap!=null && getMap.size()>0){
	       				sbHtml.append("<tr>");
	       				sbHtml.append("<td>").append(getMap.get("CARGO_NAME")!=null?getMap.get("CARGO_NAME").toString():"").append("</td>"); 
	       				sbHtml.append("<td>").append(getMap.get("SKU_ID")!=null?getMap.get("SKU_ID").toString():"").append("</td>");
	       				sbHtml.append("<td>").append(getMap.get("BILL_NUM")!=null?getMap.get("BILL_NUM").toString():"").append("</td>");
	       				if(!"1".equals(stock.getReportType())){
	       					sbHtml.append("<td>").append(getMap.get("RK_NUM")!=null?getMap.get("RK_NUM").toString():"").append("</td>");	
	       					nTJ=6;
	       				}
	       				sbHtml.append("<td>").append(getMap.get("CTN_NUM")!=null?getMap.get("CTN_NUM").toString():"").append("</td>");
	       				sbHtml.append("<td>").append(getMap.get("ENTER_STATE")!=null?getMap.get("ENTER_STATE").toString():"").append("</td>");
	       				sbHtml.append("<td>").append(getMap.get("INBOUND_DATE")!=null?getMap.get("INBOUND_DATE"):"").append("</td>");
	       				sbHtml.append("<td>").append(Double.parseDouble(getMap.get("ORIGINAL_PIECE_SUM")!=null?getMap.get("ORIGINAL_PIECE_SUM").toString():"0")).append("</td>");
	       				sbHtml.append("<td>").append(Double.parseDouble(getMap.get("NET_WEIGHT_SUM")!=null?getMap.get("NET_WEIGHT_SUM").toString():"0")).append("</td>");
	       				sbHtml.append("<td>").append(Double.parseDouble(getMap.get("GROSS_WEIGHT_SUM")!=null?getMap.get("GROSS_WEIGHT_SUM").toString():"0")).append("</td>");
	       				if("3".equals(stock.getReportType())){
	       					sbHtml.append("<td>").append(getMap.get("PRO_TIME")!=null?getMap.get("PRO_TIME").toString():"").append("</td>");
	       					sbHtml.append("<td>").append(getMap.get("TYPE_SIZE")!=null?getMap.get("TYPE_SIZE").toString():"").append("</td>");
	       					sbHtml.append("<td>").append(getMap.get("PRO_NUM")!=null?getMap.get("PRO_NUM").toString():"").append("</td>");
	       					sbHtml.append("<td>").append(getMap.get("LOT_NUM")!=null?getMap.get("LOT_NUM").toString():"").append("</td>");
	       					sbHtml.append("<td>").append(getMap.get("MSC_NUM")!=null?getMap.get("MSC_NUM").toString():"").append("</td>");
	       				}
	       				sbHtml.append("</tr>");
	       				sumPice+=Double.valueOf(getMap.get("ORIGINAL_PIECE_SUM")!=null?getMap.get("ORIGINAL_PIECE_SUM").toString():"0");
        	        	sumNet+=Double.valueOf(getMap.get("NET_WEIGHT_SUM")!=null?getMap.get("NET_WEIGHT_SUM").toString():"0");
	        			sumGross+=Double.valueOf(getMap.get("GROSS_WEIGHT_SUM")!=null?getMap.get("GROSS_WEIGHT_SUM").toString():"0");
       				}
       			}//end for
       			 
       			//添加合计
       			sbHtml.append("<tr><td class=\"ftd\" colspan=\""+nTJ+"\" style=\"border:0px;height:30px; \"></td>");
       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">合计：</td>");
       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(String.valueOf(sumPice.longValue())).append("</td>");
       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(String.valueOf(sumNet)).append("</td>");
       			sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(String.valueOf(sumGross)).append("</td>");
       			sbHtml.append("</tr>");
       		}
       		sbHtml.append("</table>");
       		sbHtml.append("<table style=\"border-spacing:0px;text-align:center; border-collapse:collapse;font-family:宋体;font-size:12px;width:100%\"><tr>");
       		//User user = UserUtil.getCurrentUser();
       		sbHtml.append("<td style=\"text-align:right;margin-top:10px;\">").append("PrintTime : &nbsp;").append(DateUtils.getDateTime()).append("</td>");
       		sbHtml.append("</tr></table>");
       		MyFileUtils html=new MyFileUtils();
       		html.setFilePath(pathHtml);
       		html.saveStrToFile(CreatPDFUtils.createPdfHtmlAddRight("分拣报告书", "Sorting Report","The Customer:",customer, sbHtml.toString()));
       		MyPDFUtils.setsDEST(pathPdf);
       		MyPDFUtils.setsHTML(pathHtml);
       		MyPDFUtils.createPdf(PageSize.A3,pdfCN.toString(),pdfTitle);
       		
       		
       		//下载操作
       		FileInputStream in = new FileInputStream(new File(pathPdf));
        	int len = 0;
        	byte[] buffer = new byte[1024];
        	String formatFileName = URLEncoder.encode("分拣报告书" +".pdf","UTF-8");
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
	
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
    	Page<Stock> page = getPage(request);
    	Stock stock = new Stock();
    	parameterReflect.reflectParameter(stock, request);
    	page = sortingReportService.getStocks(page, stock);
    	return getEasyUIData(page);
    }
}
