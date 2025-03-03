package com.haiersoft.ccli.report.web;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
import com.haiersoft.ccli.common.utils.CreatPDFUtils;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.MyFileUtils;
import com.haiersoft.ccli.common.utils.MyPDFUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.StockReportService;
import com.haiersoft.ccli.system.utils.ExcelUtils;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import com.itextpdf.text.PageSize;
/**
 * @author Connor.M
 * @ClassName: StockController
 * @Description: 库存报表
 * @date 2016年3月9日 下午2:35:31
 */
@Controller
@RequestMapping("report/stock")
public class StockReportController extends BaseController {

    @Autowired
    private StockReportService stockReportService;

    /**
     * @return
     * @throws
     * @author Connor.M
     * @Description: 库存报表页面
     * @date 2016年3月9日 下午2:36:17
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "report/stockReportList";
    }

    /**
     * @param request
     * @return
     * @throws
     * @author Connor.M
     * @Description: 库存报表查询
     * @date 2016年3月9日 下午2:38:57
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<Stock> page = getPage(request);
        Stock stock = new Stock();
        parameterReflect.reflectParameter(stock, request);//转换对应实体类参数
        page = stockReportService.searchStockReport(page, stock);
        return getEasyUIData(page);
    }

    /**
     * @return
     * @throws
     * @Description: 在库明细 导出 跳转
     * @author lzg
     */
    @RequestMapping(value = "excel", method = RequestMethod.GET)
    public String toexcel(Model model) {
        Date now = new Date();
        model.addAttribute("strTime", DateUtils.getDateStart(now));
        model.addAttribute("endTime", DateUtils.getDateEnd(now));
        return "report/stockInfoReport";
    }

    /***
     * 根据查询条件，导出在库明细
     * @param request
     * @param response
     * @throws Exception
     * @author lzg
     */
    @RequestMapping(value = "report")
    @ResponseBody
    public void export(@Valid @ModelAttribute @RequestBody BisOutStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (obj.getNtype() != null && obj.getNtype() > 0) {
            List<Map<String, Object>> getlist = stockReportService.findRepot(obj.getNtype(),obj.getIfBonded(),obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchStrTime(), obj.getSearchEndTime(), obj.getLocationType());
            ExcelUtil excelUtil = new ExcelUtil();
            String formatFileName = URLEncoder.encode("在库明细" + ".xls", "UTF-8");
            String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
            String srcPath = null;
            String desPath = null;
            if (filePath == null || "".equals(filePath)) {
                filePath = request.getSession().getServletContext().getRealPath("/");
                srcPath = filePath + "WEB-INF" + File.separator + "classes" + File.separator + "exceltemplate" + File.separator + "stocklist.xls";
                desPath = filePath + "WEB-INF" + File.separator + "classes" + File.separator + "excelpost" + File.separator + "stocklist.xls";
            } else {
                srcPath = filePath + "exceltemplate" + File.separator + "stocklist.xls";
            	desPath = filePath + "excelpost" + File.separator + "stocklist.xls";
            }
            excelUtil.setSrcPath(srcPath);
            excelUtil.setDesPath(desPath);
            excelUtil.setSheetName("Sheet1");
            excelUtil.getSheet();
            //加载数据
            int starRows = 10;//数据开始填充行数
            int addlank = 0;//列数添加数
            if (getlist != null && getlist.size() > 0) {
                Double sumPice = 0d;//总件数
                Double sumNet = 0d;//总净重
                Double sumGross = 0d;//总毛重
                int sumNum = 0;//记录合计开始列
                for (int i = 0; i < getlist.size(); i++) {
                	Map<String, Object> getMap = getlist.get(i);
                	excelUtil.setCellStrValue(starRows + i, 0 + addlank, getMap.get("CLIENTNAME") != null ? getMap.get("CLIENTNAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 1 + addlank, getMap.get("ISBONDED") != null ? getMap.get("ISBONDED").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 2 + addlank, getMap.get("BILLCODE") != null ? getMap.get("BILLCODE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 3 + addlank, getMap.get("BGDH") != null ? getMap.get("BGDH").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 4 + addlank, getMap.get("YCG") != null ? getMap.get("YCG").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 5 + addlank, getMap.get("BGDHDATE") != null ? getMap.get("BGDHDATE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 6 + addlank, getMap.get("CTNNUM") != null ? getMap.get("CTNNUM").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 7 + addlank, getMap.get("CZ") != null ? getMap.get("CZ").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 8 + addlank, getMap.get("BIGNAME") != null ? getMap.get("BIGNAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 9 + addlank, getMap.get("SIMNAME") != null ? getMap.get("SIMNAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 10 + addlank, getMap.get("SKU") != null ? getMap.get("SKU").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 11 + addlank, getMap.get("CARGONAME") != null ? getMap.get("CARGONAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 12 + addlank, getMap.get("STATE") != null ? getMap.get("STATE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 13 + addlank, getMap.get("RKTIME") != null ? getMap.get("RKTIME").toString() : "");
                    excelUtil.setCellDoubleValue(starRows + i,14+ addlank, Double.parseDouble(getMap.get("NOWNUM") != null ? getMap.get("NOWNUM").toString() : "0"));
                    excelUtil.setCellDoubleValue(starRows + i,15+ addlank, Double.parseDouble(getMap.get("ALLNET") != null ? getMap.get("ALLNET").toString() : "0"));
                    excelUtil.setCellDoubleValue(starRows + i,16+ addlank, Double.parseDouble(getMap.get("ALLGROSS") != null ? getMap.get("ALLGROSS").toString() : "0"));
                    excelUtil.setCellStrValue(starRows + i, 17 + addlank, getMap.get("CONTACTCODE") != null ? getMap.get("CONTACTCODE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 18 + addlank, getMap.get("CREATEUSER") != null ? getMap.get("CREATEUSER").toString() : "");

               
                    if ("1".equals(obj.getLocationType())) {
                       excelUtil.setTitleCellStrValue(8, 19 + addlank,"区位");
                       excelUtil.setTitleCellStrValue(9, 19 + addlank,"Cargo Location");
                       excelUtil.setCellStrValue(starRows + i, 19 + addlank, getMap.get("LOCATIONCODE") != null ? getMap.get("LOCATIONCODE").toString() : "");

                       switch(obj.getNtype()) {
                          case 2:
                           	excelUtil.setTitleCellStrValue(8, 20 + addlank,"入库号");
                           	excelUtil.setTitleCellStrValue(9, 20 + addlank,"Inbound No");
                         	excelUtil.setCellStrValue(starRows + i, 20 + addlank, getMap.get("RKNUM") != null ? getMap.get("RKNUM").toString() : "");
                            ////liuyu
                            excelUtil.setTitleCellStrValue(8, 21 + addlank,"hs编码");
                            excelUtil.setTitleCellStrValue(9, 21 + addlank,"Hs Code");
                            excelUtil.setCellStrValue(starRows + i, 21 + addlank, getMap.get("HSCODE") != null ? getMap.get("HSCODE").toString() : "");

                            excelUtil.setTitleCellStrValue(8, 22 + addlank,"账册商品序号");
                            excelUtil.setTitleCellStrValue(9, 22 + addlank,"Account Book");
                            excelUtil.setCellStrValue(starRows + i, 22 + addlank, getMap.get("ACCOUNTBOOK") != null ? getMap.get("ACCOUNTBOOK").toString() : "");

                            excelUtil.setTitleCellStrValue(8, 23 + addlank,"海关品名");
                            excelUtil.setTitleCellStrValue(9, 23 + addlank,"Hs Itemname");
                            excelUtil.setCellStrValue(starRows + i, 23 + addlank, getMap.get("HSITEMNAME") != null ? getMap.get("HSITEMNAME").toString() : ""); 
                            //////liu yu
                         	break;
                          case 3:
                        	excelUtil.setTitleCellStrValue(8, 20 + addlank,"规格");
                            excelUtil.setTitleCellStrValue(9, 20 + addlank,"Standard");
                            excelUtil.setTitleCellStrValue(8, 21 + addlank,"项目号");
                            excelUtil.setTitleCellStrValue(9, 21 + addlank,"Pro No.");
                            excelUtil.setTitleCellStrValue(8, 22 + addlank,"船名批号");
                            excelUtil.setTitleCellStrValue(9, 22 + addlank,"Ship No.");
                            excelUtil.setTitleCellStrValue(8, 23 + addlank,"MSC");
                            excelUtil.setTitleCellStrValue(9, 23 + addlank,"MSC No.");
                            excelUtil.setTitleCellStrValue(8, 24 + addlank,"order号");
                            excelUtil.setTitleCellStrValue(9, 24 + addlank,"Order Num");
                            excelUtil.setTitleCellStrValue(8, 25 + addlank,"生产日期");
                            excelUtil.setTitleCellStrValue(9, 25 + addlank,"Produce Date");
                            excelUtil.setTitleCellStrValue(8, 26 + addlank,"Lot");
                            excelUtil.setTitleCellStrValue(9, 26 + addlank,"Lot Num");
                            excelUtil.setCellStrValue(starRows + i, 20 + addlank, getMap.get("TYPESIZE") != null ? getMap.get("TYPESIZE").toString() : "");
                       	    excelUtil.setCellStrValue(starRows + i, 21 + addlank, getMap.get("PROJECTNUM") != null ? getMap.get("PROJECTNUM").toString() : "");
                       	    excelUtil.setCellStrValue(starRows + i, 22 + addlank, getMap.get("SHIPNUM") != null ? getMap.get("SHIPNUM").toString() : "");
                       	    excelUtil.setCellStrValue(starRows + i, 23 + addlank, getMap.get("MSCNUM") != null ? getMap.get("MSCNUM").toString() : "");
                       	    excelUtil.setCellStrValue(starRows + i, 24 + addlank, getMap.get("ORDERNUM") != null ? getMap.get("ORDERNUM").toString() : "");
                       	    excelUtil.setCellStrValue(starRows + i, 25 + addlank, getMap.get("MAKETIME") != null ? getMap.get("MAKETIME").toString() : "");
                       	    excelUtil.setCellStrValue(starRows + i, 26 + addlank, getMap.get("LOTNUM") != null ? getMap.get("LOTNUM").toString() : "");

                            ////liuyu
                            excelUtil.setTitleCellStrValue(8, 27 + addlank,"hs编码");
                            excelUtil.setTitleCellStrValue(9, 27 + addlank,"Hs Code");
                            excelUtil.setCellStrValue(starRows + i, 27 + addlank, getMap.get("HSCODE") != null ? getMap.get("HSCODE").toString() : "");

                            excelUtil.setTitleCellStrValue(8, 28 + addlank,"账册商品序号");
                            excelUtil.setTitleCellStrValue(9, 28 + addlank,"Account Book");
                            excelUtil.setCellStrValue(starRows + i, 28 + addlank, getMap.get("ACCOUNTBOOK") != null ? getMap.get("ACCOUNTBOOK").toString() : "");

                            excelUtil.setTitleCellStrValue(8, 29 + addlank,"海关品名");
                            excelUtil.setTitleCellStrValue(9, 29 + addlank,"Hs Itemname");
                            excelUtil.setCellStrValue(starRows + i, 29 + addlank, getMap.get("HSITEMNAME") != null ? getMap.get("HSITEMNAME").toString() : ""); 
                            //////liu yu
                       	    break;
                       }
                       ////liuyu
                       excelUtil.setTitleCellStrValue(8, 20 + addlank,"hs编码");
                       excelUtil.setTitleCellStrValue(9, 20 + addlank,"Hs Code");
                       excelUtil.setCellStrValue(starRows + i, 20 + addlank, getMap.get("HSCODE") != null ? getMap.get("HSCODE").toString() : "");

                       excelUtil.setTitleCellStrValue(8, 21 + addlank,"账册商品序号");
                       excelUtil.setTitleCellStrValue(9, 21 + addlank,"Account Book");
                       excelUtil.setCellStrValue(starRows + i, 21 + addlank, getMap.get("ACCOUNTBOOK") != null ? getMap.get("ACCOUNTBOOK").toString() : "");

                       excelUtil.setTitleCellStrValue(8, 22 + addlank,"海关品名");
                       excelUtil.setTitleCellStrValue(9, 22 + addlank,"Hs Itemname");
                       excelUtil.setCellStrValue(starRows + i, 22 + addlank, getMap.get("HSITEMNAME") != null ? getMap.get("HSITEMNAME").toString() : ""); 
                       //////liu yu
					} else {
						switch (obj.getNtype()) {
						case 2:
							excelUtil.setTitleCellStrValue(8, 19 + addlank, "入库号");
							excelUtil.setTitleCellStrValue(9, 19 + addlank, "Inbound No");
							excelUtil.setCellStrValue(starRows + i, 19 + addlank,getMap.get("RKNUM") != null ? getMap.get("RKNUM").toString() : "");
                            ////liuyu
                            excelUtil.setTitleCellStrValue(8, 20 + addlank,"hs编码");
                            excelUtil.setTitleCellStrValue(9, 20 + addlank,"Hs Code");
                            excelUtil.setCellStrValue(starRows + i, 20 + addlank, getMap.get("HSCODE") != null ? getMap.get("HSCODE").toString() : "");

                            excelUtil.setTitleCellStrValue(8, 21 + addlank,"账册商品序号");
                            excelUtil.setTitleCellStrValue(9, 21 + addlank,"Account Book");
                            excelUtil.setCellStrValue(starRows + i, 21 + addlank, getMap.get("ACCOUNTBOOK") != null ? getMap.get("ACCOUNTBOOK").toString() : "");

                            excelUtil.setTitleCellStrValue(8, 22 + addlank,"海关品名");
                            excelUtil.setTitleCellStrValue(9, 22 + addlank,"Hs Itemname");
                            excelUtil.setCellStrValue(starRows + i, 22 + addlank, getMap.get("HSITEMNAME") != null ? getMap.get("HSITEMNAME").toString() : ""); 
                            //////liu yu
							break;
						case 3:
							excelUtil.setTitleCellStrValue(8, 19 + addlank, "规格");
							excelUtil.setTitleCellStrValue(9, 19 + addlank, "Standard");
							excelUtil.setTitleCellStrValue(8, 20 + addlank, "项目号");
							excelUtil.setTitleCellStrValue(9, 20 + addlank, "Pro No.");
							excelUtil.setTitleCellStrValue(8, 21 + addlank, "船名批号");
							excelUtil.setTitleCellStrValue(9, 21 + addlank, "Ship No.");
							excelUtil.setTitleCellStrValue(8, 22 + addlank, "MSC");
							excelUtil.setTitleCellStrValue(9, 22 + addlank, "MSC No.");
							excelUtil.setTitleCellStrValue(8, 23 + addlank, "order号");
							excelUtil.setTitleCellStrValue(9, 23 + addlank, "Order Num");
							excelUtil.setTitleCellStrValue(8, 24 + addlank, "生产日期");
							excelUtil.setTitleCellStrValue(9, 24 + addlank, "Produce Date");
							excelUtil.setTitleCellStrValue(8, 25 + addlank,"Lot");
	                        excelUtil.setTitleCellStrValue(9, 25 + addlank,"Lot Num");
							excelUtil.setCellStrValue(starRows + i, 19 + addlank,
									getMap.get("TYPESIZE") != null ? getMap.get("TYPESIZE").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 20 + addlank,
									getMap.get("PROJECTNUM") != null ? getMap.get("PROJECTNUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 21 + addlank,
									getMap.get("SHIPNUM") != null ? getMap.get("SHIPNUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 22 + addlank,
									getMap.get("MSCNUM") != null ? getMap.get("MSCNUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 23 + addlank,
									getMap.get("ORDERNUM") != null ? getMap.get("ORDERNUM").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 24 + addlank,
									getMap.get("MAKETIME") != null ? getMap.get("MAKETIME").toString() : "");
							excelUtil.setCellStrValue(starRows + i, 25 + addlank,
									getMap.get("LOTNUM") != null ? getMap.get("LOTNUM").toString() : "");

                            ////liuyu
                            excelUtil.setTitleCellStrValue(8, 26 + addlank,"hs编码");
                            excelUtil.setTitleCellStrValue(9, 26 + addlank,"Hs Code");
                            excelUtil.setCellStrValue(starRows + i, 26 + addlank, getMap.get("HSCODE") != null ? getMap.get("HSCODE").toString() : "");

                            excelUtil.setTitleCellStrValue(8, 27 + addlank,"账册商品序号");
                            excelUtil.setTitleCellStrValue(9, 27 + addlank,"Account Book");
                            excelUtil.setCellStrValue(starRows + i, 27 + addlank, getMap.get("ACCOUNTBOOK") != null ? getMap.get("ACCOUNTBOOK").toString() : "");

                            excelUtil.setTitleCellStrValue(8, 28 + addlank,"海关品名");
                            excelUtil.setTitleCellStrValue(9, 28 + addlank,"Hs Itemname");
                            excelUtil.setCellStrValue(starRows + i, 28 + addlank, getMap.get("HSITEMNAME") != null ? getMap.get("HSITEMNAME").toString() : ""); 
                            //////liu yu
							break;
						}
                        ////liuyu
                        excelUtil.setTitleCellStrValue(8, 19 + addlank,"hs编码");
                        excelUtil.setTitleCellStrValue(9, 19 + addlank,"Hs Code");
                        excelUtil.setCellStrValue(starRows + i, 19 + addlank, getMap.get("HSCODE") != null ? getMap.get("HSCODE").toString() : "");

                        excelUtil.setTitleCellStrValue(8, 20 + addlank,"账册商品序号");
                        excelUtil.setTitleCellStrValue(9, 20 + addlank,"Account Book");
                        excelUtil.setCellStrValue(starRows + i, 20 + addlank, getMap.get("ACCOUNTBOOK") != null ? getMap.get("ACCOUNTBOOK").toString() : "");

                        excelUtil.setTitleCellStrValue(8, 21 + addlank,"海关品名");
                        excelUtil.setTitleCellStrValue(9, 21 + addlank,"Hs Itemname");
                        excelUtil.setCellStrValue(starRows + i, 21 + addlank, getMap.get("HSITEMNAME") != null ? getMap.get("HSITEMNAME").toString() : ""); 
                        //////liu yu
						
					}
                    sumPice+=Double.parseDouble(getMap.get("NOWNUM") != null ? getMap.get("NOWNUM").toString() : "0");
                    sumNet+=Double.parseDouble(getMap.get("ALLNET") != null ? getMap.get("ALLNET").toString() : "0");
                    sumGross+=Double.parseDouble(getMap.get("ALLGROSS") != null ? getMap.get("ALLGROSS").toString() : "0");
                    sumNum = 13;
                    addlank = 0;
                }
                NumberFormat nf = NumberFormat.getInstance();
                //设置保留多少位小数
                nf.setMaximumFractionDigits(2);
                 // 取消科学计数法
                nf.setGroupingUsed(false);
                //添加合计
                excelUtil.setCellStrValue(starRows + getlist.size(), sumNum, "合计/Total:");
                excelUtil.setCellStrValue(starRows + getlist.size(), sumNum + 1, String.valueOf(sumPice.longValue()));
                excelUtil.setCellStrValue(starRows + getlist.size(), sumNum + 2, String.valueOf(nf.format(sumNet)));
                excelUtil.setCellStrValue(starRows + getlist.size(), sumNum + 3, String.valueOf(nf.format(sumGross)));
            }

//            //2025-02-21 徐峥增加
//            excelUtil.setCellStrValue(starRows + getlist.size()+5,
//                    0,"1. 本凭证仅针对出具之日货物的数量、重量及有权提货人作出陈述，对于出具后货物情况发生的变化及提货人的变化不承担任何责任。");
//            excelUtil.setCellStrValue(starRows + getlist.size()+6,
//                    0,"2. 该单据仅作为提供给客户的入库/出库/在库明细不得作为他用（如金融质押、抵押等）。");
//            excelUtil.setCellStrValue(starRows + getlist.size()+6,
//                    0,"1. This document only makes representations regarding the quantity, weight, and the authorized consignee of the goods as of the date of issuance. It does not assume any responsibility for changes in the condition of the goods or changes in the consignee after the issuance.\n" +
//                            "2. This document is solely intended to provide customers with details of incoming/outgoing/current stock and must not be used for other purposes (such as financial collateral, mortgage, etc.).");

            excelUtil.exportToNewFile();
            FileInputStream in = new FileInputStream(new File(desPath));
            int len = 0;
            byte[] buffer = new byte[1024];
            response.reset();
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
            	out.flush();
                out.close();
            }
        }
    }

    /***
     * 根据查询条件，导出在库明细 PDF
     * @param request
     * @param response
     * @throws Exception
     * @author lzg
     */
    @RequestMapping(value = "reportpdf")
    @ResponseBody
    public void exportPDF(@Valid @ModelAttribute @RequestBody BisOutStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (obj.getNtype() != null && obj.getNtype() > 0) {
            List<Map<String, Object>> getlist = stockReportService.findRepot(obj.getNtype(),obj.getIfBonded(),obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchStrTime(), obj.getSearchEndTime(), obj.getLocationType());
            String[] headCN = {"客户名称","货物类别","提单号", "报关单号", "原产国","报关审报时间","MR/集装箱号","箱型尺寸","大类","小类","SKU号","货物描述","货物状态","入库日期","件数","总净重(KGS)", "总毛重(KGS)","联系单号","所属客服","Hs编码","账册商品序号","海关品名"};
            String[] headEN = {"Customer","Cargo Type","B/L NO.","Declare NO.","Country of origin","Declare Date","MR/CTN NO.","Size","Class","Sub Class","SKU", "Description of cargo","State of cargo","Inbound Date","Qty","Net Weight","Gross Weight","Contact NO.","Customer service","Hs Code","Account Book","Hs Itemname"};
            String pdfTitle = "在库明细";
            StringBuffer pdfCN = new StringBuffer();
            pdfCN.append("客户名称");
            pdfCN.append("            ");
            pdfCN.append("货物类别");
            pdfCN.append("            ");
            pdfCN.append("提单号");
            pdfCN.append("                ");
            pdfCN.append("报关单号");
            pdfCN.append("                ");
            pdfCN.append("原产国");
            pdfCN.append("        ");
            pdfCN.append("报关审报时间");
            pdfCN.append("              ");
            pdfCN.append("MR/集装箱号");
            pdfCN.append("              ");
            pdfCN.append("箱型尺寸");
            pdfCN.append("      ");
            pdfCN.append("大类");
            pdfCN.append("      ");
            pdfCN.append("小类");
            pdfCN.append("      ");
            pdfCN.append("SKU号");
            pdfCN.append("                ");
            pdfCN.append("货物描述");
            pdfCN.append("                           ");
            pdfCN.append("货物状态");
            pdfCN.append("            ");
            pdfCN.append("入库日期");
            pdfCN.append("              ");
            pdfCN.append("件数");
            pdfCN.append("      ");
            pdfCN.append("总净重(KGS)");
            pdfCN.append("            ");
            pdfCN.append("总毛重(KGS)");
            pdfCN.append("            ");
            pdfCN.append("联系单号");
            pdfCN.append("                ");
            pdfCN.append("所属客服");
            pdfCN.append("            ");
            pdfCN.append("Hs编码");
            pdfCN.append("            ");
            pdfCN.append("账册商品序号");
            pdfCN.append("            ");
            pdfCN.append("海关品名");
            pdfCN.append("            ");
            if ("1".equals(obj.getLocationType())) {
            	pdfCN.append("区位");
                pdfCN.append("            ");
            }
            switch(obj.getNtype()) {
               case 2:
            	   pdfCN.append("入库号");
                   pdfCN.append("          ");
            	   break;
               case 3:
            	   pdfCN.append("规格");
                   pdfCN.append("       ");
                   pdfCN.append("项目号");
                   pdfCN.append("          ");
                   pdfCN.append("船名批号");
                   pdfCN.append("          ");
                   pdfCN.append("MSC");
                   pdfCN.append("          ");
                   pdfCN.append("order号");
                   pdfCN.append("               ");
                   pdfCN.append("生产日期");
                   pdfCN.append("               ");
                   pdfCN.append("Lot");
                   pdfCN.append("              ");
            	   break;
            }
            String path = request.getSession().getServletContext().getRealPath("/");//获取web项目的路径"d://exceltemplate//syshtm.html";
            String pathHtml = path + "//infosyshtm.html";
            String pathPdf = path + "//infosyspdf.pdf";
            StringBuffer sbHtml = new StringBuffer();

            sbHtml.append("<div  style=\"height:5px;\"></div>");
            sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
            //填充标题头
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headCN) {
                if ("货物描述".equals(lab)) {
                    sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
                } else if ("SKU号".equals(lab) || "MR/集装箱号".equals(lab)) {
                    sbHtml.append("<td  width=\"250px\">").append(lab).append("</td>");
                } else {
                    sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                }
            }
            if ("1".equals(obj.getLocationType())) {
        		sbHtml.append("<td class=\"htd\">").append("区位").append("</td>");
            }
			switch (obj.getNtype()) {
			case 2:
				sbHtml.append("<td class=\"htd\">").append("入库号").append("</td>");
				break;
			case 3:
				sbHtml.append("<td class=\"htd\">").append("规格").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("项目号").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("船名批号").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("order号").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("生产日期").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("Lot").append("</td>");
				break;
			}
            sbHtml.append("</tr>");
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headEN) {
                sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
            }
            if ("1".equals(obj.getLocationType())) {
        		sbHtml.append("<td class=\"htd\">").append("Cargo Location").append("</td>");
            }
			switch (obj.getNtype()) {
				case 2:
					sbHtml.append("<td class=\"htd\">").append("Inbound No").append("</td>");
					break;
				case 3:
					sbHtml.append("<td class=\"htd\">").append("Standard").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("Pro No.").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("Ship No.").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("MSC No.").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("Order Num").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("Produce Date").append("</td>");
                    sbHtml.append("<td class=\"htd\">").append("Lot No.").append("</td>");
					break;
			}
            sbHtml.append("</tr>");
            //填充内容
            if (getlist != null && getlist.size() > 0) {
                Double sumPice = 0d;
                Double sumNet = 0d;
                Double sumGross = 0d;
                for (int i = 0; i < getlist.size(); i++) {
                	Map<String, Object> getMap=getlist.get(i);
                    if (getMap != null && getMap.size() > 0) {
                        sbHtml.append("<tr>");
                        sbHtml.append("<td>").append(getMap.get("CLIENTNAME") != null ? getMap.get("CLIENTNAME").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("ISBONDED") != null ? getMap.get("ISBONDED").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("BILLCODE") != null ? getMap.get("BILLCODE").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("BGDH") != null ? getMap.get("BGDH").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("YCG") != null ? getMap.get("YCG").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("BGDHDATE") != null ? getMap.get("BGDHDATE").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CTNNUM") != null ? getMap.get("CTNNUM").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CZ") != null ? getMap.get("CZ").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("BIGNAME") != null ? getMap.get("BIGNAME").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("SIMNAME") != null ? getMap.get("SIMNAME").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("SKU") != null ? getMap.get("SKU").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CARGONAME") != null ? getMap.get("CARGONAME").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("STATE") != null ? getMap.get("STATE").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("RKTIME") != null ? getMap.get("RKTIME").toString():"").append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("NOWNUM") != null ? getMap.get("NOWNUM").toString() : "0"))).append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("ALLNET") != null ? getMap.get("ALLNET").toString() : "0"))).append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("ALLGROSS") != null ? getMap.get("ALLGROSS").toString() : "0"))).append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CONTACTCODE") != null ? getMap.get("CONTACTCODE").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CREATEUSER") != null ? getMap.get("CREATEUSER").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("HSCODE") != null ? getMap.get("HSCODE").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("ACCOUNTBOOK") != null ? getMap.get("ACCOUNTBOOK").toString():"").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("HSITEMNAME") != null ? getMap.get("HSITEMNAME").toString():"").append("</td>");
 
                        if ("1".equals(obj.getLocationType())) {
                            sbHtml.append("<td>").append(getMap.get("LOCATIONCODE")!=null?getMap.get("LOCATIONCODE").toString():"").append("</td>");
                        }
                        switch (obj.getNtype()) {
						case 2:
                            sbHtml.append("<td>").append(getMap.get("RKNUM")!=null?getMap.get("RKNUM").toString():"").append("</td>");
							break;
						case 3:
                            sbHtml.append("<td>").append(getMap.get("TYPESIZE")!=null?getMap.get("TYPESIZE").toString():"").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("PROJECTNUM")!=null?getMap.get("PROJECTNUM").toString():"").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("SHIPNUM")!=null?getMap.get("SHIPNUM").toString():"").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("MSCNUM")!=null?getMap.get("MSCNUM").toString():"").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("ORDERNUM")!=null?getMap.get("ORDERNUM").toString():"").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("MAKETIME")!=null?getMap.get("MAKETIME").toString():"").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("LOTNUM")!=null?getMap.get("LOTNUM").toString():"").append("</td>");
							break;
						}
                        sbHtml.append("</tr>");
                        sumPice += Double.valueOf(getMap.get("NOWNUM") != null ? getMap.get("NOWNUM").toString() : "0");
                        sumNet += Double.valueOf(getMap.get("ALLNET") != null ? getMap.get("ALLNET").toString() : "0");
                        sumGross += Double.valueOf(getMap.get("ALLGROSS") != null ? getMap.get("ALLGROSS").toString() : "0");
                    }
                }//end for

                //添加合计
                sbHtml.append("<tr><td class=\"ftd\" colspan=\"13\" style=\"border:0px;height:30px; \"></td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">合计：</td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumPice)).append("</td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumNet)).append("</td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumGross)).append("</td>");
                sbHtml.append("</tr>");
            }
            sbHtml.append("</table>");

//            //2025-02-21 徐峥增加
//            sbHtml.append("<table><tr><td></td></tr></table>");
//            sbHtml.append("<table><tr><td></td></tr></table>");
//            sbHtml.append("<table><tr><td></td></tr></table>");
//            sbHtml.append("<table><tr><td></td></tr></table>");
//            sbHtml.append("<table><tr><td></td></tr></table>");
//            sbHtml.append("<table><tr>");
//            sbHtml.append("<td style=\"width:50%;font-size:16px; font-family:宋体;\">1. 本凭证仅针对出具之日货物的数量、重量及有权提货人作出陈述，对于出具后货物情况发生的变化及提货人的变化不承担任何责任。</td>");
//            sbHtml.append("</tr></table>");
//            sbHtml.append("<table><tr>");
//            sbHtml.append("<td style=\"width:50%;font-size:16px; font-family:宋体;\">2. 该单据仅作为提供给客户的入库/出库/在库明细不得作为他用（如金融质押、抵押等）。</td>");
//            sbHtml.append("</tr></table>");
//			sbHtml.append("<table><tr>");
//			sbHtml.append("<td>").append("1. This document only makes representations regarding the quantity, weight, and the authorized consignee of the goods as of the date of issuance. It does not assume any responsibility for changes in the condition of the goods or changes in the consignee after the issuance.\n" +
//					"2. This document is solely intended to provide customers with details of incoming/outgoing/current stock and must not be used for other purposes (such as financial collateral, mortgage, etc.).").append("</td>");
//			sbHtml.append("</tr></table>");

            MyFileUtils html = new MyFileUtils();
            html.setFilePath(pathHtml);
            html.saveStrToFile(CreatPDFUtils.createPdfHtml("在库明细", "Inbound Details", sbHtml.toString()));
            MyPDFUtils.setsDEST(pathPdf);
            MyPDFUtils.setsHTML(pathHtml);
            MyPDFUtils.createPdf(PageSize.A4, pdfCN.toString(), pdfTitle);
            //下载操作
            FileInputStream in = new FileInputStream(new File(pathPdf));
            int len = 0;
            byte[] buffer = new byte[1024];
            String formatFileName = URLEncoder.encode("在库明细" + ".pdf", "UTF-8");
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

    /**
     * @return
     * @throws
     * @Description: 出入库明细 导出 跳转
     * @author lzg
     */
    @RequestMapping(value = "inout", method = RequestMethod.GET)
    public String toinout(Model model) {
        Date now = new Date();
        model.addAttribute("strTime", DateUtils.getDateStart(now));
        model.addAttribute("endTime", DateUtils.getDateEnd(now));
        return "report/stockInOutReport";
    }

    /***
     * 根据查询条件，导出出入库明细
     * @param request
     * @param response
     * @throws Exception
     * @author lzg
     */
    @RequestMapping(value = "inoutport")
    @ResponseBody
    public void inoutport(@Valid @ModelAttribute @RequestBody BisOutStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (obj.getNtype() != null && obj.getNtype() > 0) {
        	String path = request.getSession().getServletContext().getRealPath("/");//获取web项目的路径
        	long nowTime = System.currentTimeMillis();
        	List<Object[]> getlist = stockReportService.findRepotInAndOutObject(obj.getNtype(),obj.getIfBonded(),obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchStrTime(), obj.getSearchEndTime());
            String formatFileName = URLEncoder.encode("出入库明细" + ".xls", "UTF-8");
            Map<String, Object> map = new HashMap<String, Object>();
			if(null != getlist && getlist.size() > 0){
				map.put("list",getlist);
			}else{
				return;
			}
			File file=ExcelUtils.createExcel(path,map,"myExcel",1==obj.getNtype()?"inoutstock":"inoutstockq",nowTime);
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
		    finally{
		      if (in != null)
		        try {
		          in.close();
		        } catch (IOException localIOException1) {
		        }
		    }
        }
    }

    /***
     * 根据查询条件，导出出入库明细 PDF
     * @param request
     * @param response
     * @throws Exception
     * @author lzg
     */
    @RequestMapping(value = "ioreportpdf")
    @ResponseBody
    public String inoutportPDF(@Valid @ModelAttribute @RequestBody BisOutStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (obj.getNtype() != null && obj.getNtype() > 0) {
        	List<Object[]> getlist = stockReportService.findRepotInAndOutObject(obj.getNtype(),obj.getIfBonded(),obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchStrTime(), obj.getSearchEndTime());
        	String[] headCN = {"存货方","货物类别","作业类型","收货方","提单号","MR/集装箱号","报关单号","原产国","报关审报时间","SKU","箱型尺寸","大类","小类","件数","出入库日期","货物状态","货物描述","总净重(KGS)","总毛重(KGS)","联系单号","所属客服"};
            String[] headEN={"Customer","Cargo Type","Type","Receiving Party","B/L NO.", "MR/CTN NO.","Declare NO.","Country of origin","Declare Date","SKU","Size","Class", "Sub Class","Qty","Inbound Date","State of cargo","Description of cargo","Net Weight", "Gross Weight","Contact NO.","Customer service"};
        	String pdfTitle = "出入库明细";
            StringBuffer pdfCN = new StringBuffer();
            if (1 == obj.getNtype()) {
                pdfCN.append(""
                        + "存货方" + "       "
                        + "货物类别" + "         "
                        + "作业类型" + "        "
                        + "收货方" + "            "
                        + "提单号" + "                           "
                        + "MR/集装箱号" + "                      "
                        + "报关单号"+"                       "
                        + "原产国"+"         "
                        + "报关审报时间"+"             "
                        + "SKU" + "                                    "
                        + "箱型尺寸"+"       "
                        + "大类" + "                 "
                        + "小类" + "                  "
                        + "件数" + "             "
                        + "出入库日期" + "       "
                        + "货物状态" + "         "
                        + "货物描述" + "                                 "
                        + "总净重(KGS)" + "     "
                        + "总毛重(KGS)" + "               "
                        + "联系单号"+"                "
                        + "所属客服"+"           "
                );
            }
            if (2 == obj.getNtype()) {
                pdfCN.append(""
                		+ "存货方" + "       "
                        + "货物类别" + "         "
                        + "作业类型" + "        "
                        + "收货方" + "            "
                        + "提单号" + "                           "
                        + "MR/集装箱号" + "                      "
                        + "报关单号"+"                       "
                        + "原产国"+"         "
                        + "报关审报时间"+"             "
                        + "SKU" + "                                    "
                        + "箱型尺寸"+"       "
                        + "大类" + "                 "
                        + "小类" + "                  "
                        + "件数" + "             "
                        + "出入库日期" + "       "
                        + "货物状态" + "         "
                        + "货物描述" + "                                 "
                        + "总净重(KGS)" + "     "
                        + "总毛重(KGS)" + "               "
                        + "联系单号"+"                "
                        + "所属客服"+"           "
                        + "区位"+"         "
                );
            }
            String path = request.getSession().getServletContext().getRealPath("/");//获取web项目的路径"d://exceltemplate//syshtm.html";
            String pathHtml = path + "//inoutsyshtm.html";
            String pathPdf = path + "//inoutsyspdf.pdf";
            StringBuffer sbHtml = new StringBuffer();
            sbHtml.append("<div  style=\"height:5px;\"></div>");
            sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
            //填充标题头
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headCN) {
                if ("货物描述".equals(lab)) {
                    sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
                } else if ("提单号".equals(lab) || "MR/集装箱号".equals(lab) || "SKU".equals(lab)) {
                    sbHtml.append("<td  width=\"250px\">").append(lab).append("</td>");
                } else {
                    sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                }
                if(2 == obj.getNtype()&&"所属客服".equals(lab)){
                	sbHtml.append("<td class=\"htd\">").append("区位").append("</td>");
                }
            }
            sbHtml.append("</tr>");
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headEN) {
                sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                if (2 == obj.getNtype()&& "Customer service".equals(lab)) {
                    sbHtml.append("<td class=\"htd\">").append("Cargo Location").append("</td>");
                }
            }
            sbHtml.append("</tr>");
            //填充内容
            if (getlist != null && getlist.size() > 0) {
                for (int i = 0; i < getlist.size(); i++) {
                	    Object[] object= getlist.get(i);
                        sbHtml.append("<tr>");
	                        sbHtml.append("<td>").append(object[1]!= null ?object[1].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[2]!= null ?object[2].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[3]!= null ?object[3].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[4]!= null ?object[4].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[6]!= null ?object[6].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[7]!= null ?object[7].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[8]!= null ?object[8].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[9]!= null ?object[9].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[10]!= null ?object[10].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[11]!= null ?object[11].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[12]!= null ?object[12].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[13]!= null ?object[13].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[15]!= null ?object[15].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(object[17]!= null ?object[17].toString() : "0"))).append("</td>");
	                        sbHtml.append("<td>").append(object[18]!= null ?object[18].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[19]!= null ?object[19].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[20]!= null ?object[20].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(object[21]!= null ?object[21].toString() : "0"))).append("</td>");
	                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(object[22]!= null ?object[22].toString() : "0"))).append("</td>");
	                        sbHtml.append("<td>").append(object[23]!= null ?object[23].toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(object[24]!= null ?object[24].toString() : "").append("</td>");
	                        if (2 == obj.getNtype()) {
	                          sbHtml.append("<td>").append(object[26]!= null ?object[26].toString() : "").append("</td>");
	                        }
                        sbHtml.append("</tr>");
                }//end for
                //添加合计
            }
            sbHtml.append("</table>");
            MyFileUtils html = new MyFileUtils();
            html.setFilePath(pathHtml);
            html.saveStrToFile(CreatPDFUtils.createPdfHtml("出入库明细", "InOutbound Details", sbHtml.toString()));
            MyPDFUtils.setsDEST(pathPdf);
            MyPDFUtils.setsHTML(pathHtml);
            MyPDFUtils.createPdf(PageSize.A4, pdfCN.toString(), pdfTitle);
            //下载操作
            FileInputStream in = new FileInputStream(new File(pathPdf));
            int len = 0;
            byte[] buffer = new byte[1024];
            String formatFileName = URLEncoder.encode("出入库明细" + ".pdf", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
            response.setContentType("application/msexcel");// 定义输出类型
            OutputStream out =null;
            try{
	            out=response.getOutputStream();
	            while ((len = in.read(buffer)) > 0) {
	                out.write(buffer, 0, len);
	            }
	            if (null != in) {
	                in.close();
	            }
	            if (null != out) {
	                out.close();
	            }
            }catch(Exception e){
            	e.printStackTrace();
            }finally { 
	            if (null != in) {
	                in.close();
	            }
	            if (null != out) {
	                out.close();
	            }
            }
        }
        return null;
    }

    /**
     * 跳转到冻肉出入库统计报表
     *
     * @return
     */
    @RequestMapping(value = "meatlist", method = RequestMethod.GET)
    public String toMeatlist() {
        return "report/meatList";
    }

    /***
     * 异步获取肉类出入库统计
     * @param request
     * @return
     */
    @RequestMapping(value = "meatjson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMeatListJson(HttpServletRequest request) {
        Map<String, Object> returnMap = null;
        int pageNumber = Integer.valueOf(request.getParameter("pageNumber") != null ? request.getParameter("pageNumber").toString() : "1");
        int pageSize = Integer.valueOf(request.getParameter("pageSize") != null ? request.getParameter("pageSize").toString() : "20");
        String billNum = request.getParameter("billNum") != null ? request.getParameter("billNum").toString() : "";
        String staTime = request.getParameter("staTime") != null ? request.getParameter("staTime").toString() : "";
        String endTime = request.getParameter("endTime") != null ? request.getParameter("endTime").toString() : "";
        returnMap = stockReportService.getMeatBillNumTrayInfo(billNum, staTime, endTime, pageNumber, pageSize);
        if (returnMap == null) {
            returnMap = new HashMap<String, Object>();
        }
        return returnMap;
    }

    /**
     * 冻肉出入库统计
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "meatexcel")
    @ResponseBody
    public void meatexcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> returnMap = null;
        List<Map<String, Object>> getList = null;
        String billNum = request.getParameter("billNum") != null ? request.getParameter("billNum").toString() : "";
        String staTime = request.getParameter("staTime") != null ? request.getParameter("staTime").toString() : "";
        String endTime = request.getParameter("endTime") != null ? request.getParameter("endTime").toString() : "";
        returnMap = stockReportService.getMeatBillNumTrayInfo(billNum, staTime, endTime, 1, 0);
        if (returnMap != null && returnMap.size() > 0) {
            getList = (List<Map<String, Object>>) returnMap.get("rows");
        }
        String formatFileName = URLEncoder.encode("商检冻肉统计报表" + ".xls", "UTF-8");
        ExcelUtil excelUtil = new ExcelUtil();
        String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
        String srcPath = null;
        String desPath = null;
        if (filePath == null || "".equals(filePath)) {
            filePath = request.getSession().getServletContext().getRealPath("/");
            srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\meatReportC.xls";
            desPath = filePath + "WEB-INF\\classes\\excelpost\\meatReportC.xls";
        } else {
            srcPath = filePath + "exceltemplate\\meatReportC.xls";
            desPath = filePath + "excelpost\\meatReportC.xls";
        }
        //System.out.println(srcPath);
        excelUtil.setSrcPath(srcPath);
        excelUtil.setDesPath(desPath);
        excelUtil.setSheetName("Sheet1");
        excelUtil.getSheet();
        int starRows = 9;
        int size = getList.size();
        if (getList != null && size > 0) {
            Map<String, Object> getMap = null;
            int outSign = 1;
            for (int i = 0; i < size; i++) {
                getMap = getList.get(i);
                if (getMap != null && getMap.size() > 0) {
                    excelUtil.setCellStrValue(starRows + i, 0, "1".equals(getMap.get("TYPE") != null ? getMap.get("TYPE").toString() : "") ? "入库" : "出库");
                    if ("2".equals(getMap.get("TYPE") != null ? getMap.get("TYPE").toString() : "")) {
                        outSign = 0;
                    }
                    excelUtil.setCellStrValue(starRows + i, 1, getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 2, getMap.get("STOCK_TIME") != null ? getMap.get("STOCK_TIME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 3, getMap.get("CARGO_NAME") != null ? getMap.get("CARGO_NAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 4, String.valueOf(Double.valueOf(getMap.get("PIECE") != null ? getMap.get("PIECE").toString() : "0.00").longValue()));
                    excelUtil.setCellStrValue(starRows + i, 5, getMap.get("SUM_NET_WEIGHT") != null ? getMap.get("SUM_NET_WEIGHT").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 6, "");
                    excelUtil.setCellStrValue(starRows + i, 7, getMap.get("CTN_NUM") != null ? getMap.get("CTN_NUM").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 8, getMap.get("STOCK_NAME") != null ? getMap.get("STOCK_NAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 9, getMap.get("JY") != null ? getMap.get("JY").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 10, getMap.get("LAB") != null ? getMap.get("LAB").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 11, "");
                }
            }
            if (outSign == 1) {
                excelUtil.setCellStrValue(starRows + size, 0, "出库");
                excelUtil.setCellStrValue(starRows + size, 1, billNum);
                excelUtil.setCellStrValue(starRows + size, 2, "");
                excelUtil.setCellStrValue(starRows + size, 3, "");
                excelUtil.setCellStrValue(starRows + size, 4, "");
                excelUtil.setCellStrValue(starRows + size, 5, "");
                excelUtil.setCellStrValue(starRows + size, 6, "");
                excelUtil.setCellStrValue(starRows + size, 7, "");
                excelUtil.setCellStrValue(starRows + size, 8, "");
                excelUtil.setCellStrValue(starRows + size, 9, "");
                excelUtil.setCellStrValue(starRows + size, 10, "");
                excelUtil.setCellStrValue(starRows + size, 11, "");
            }
        }
//    	else{
//			List<Map<String,Object>> stockList = stockReportService.getStockNameByBill(billNum);
//			excelUtil.setCellStrValue(starRows, 0, "出库");
//			excelUtil.setCellStrValue(starRows, 1, billNum);
//			excelUtil.setCellStrValue(starRows, 2, "");
//			excelUtil.setCellStrValue(starRows, 3, "");
//			excelUtil.setCellStrValue(starRows, 4, "");
//			excelUtil.setCellStrValue(starRows, 5, "");
//			excelUtil.setCellStrValue(starRows, 6, "");
//			excelUtil.setCellStrValue(starRows, 7, "");
//			excelUtil.setCellStrValue(starRows, 8, !stockList.isEmpty()?(String)stockList.get(0).get("STOCKNAME"):"");
//			excelUtil.setCellStrValue(starRows, 9, "");
//			excelUtil.setCellStrValue(starRows, 10, "");
//			excelUtil.setCellStrValue(starRows, 11, "");
//		}
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

    @RequestMapping(value = "fisherlist", method = RequestMethod.GET)
    public String toFisherieslist() {
        return "report/fisherList";
    }

    /***
     * 异步获取水产出入库统计
     * @param request
     * @return
     */
    @RequestMapping(value = "fisherjson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getFisherListJson(HttpServletRequest request) {
        Map<String, Object> returnMap = null;
        int pageNumber = Integer.valueOf(request.getParameter("pageNumber") != null ? request.getParameter("pageNumber").toString() : "1");
        int pageSize = Integer.valueOf(request.getParameter("pageSize") != null ? request.getParameter("pageSize").toString() : "20");
        String staTime = request.getParameter("starTime") != null ? request.getParameter("starTime").toString() : "";
        String endTime = request.getParameter("endTime") != null ? request.getParameter("endTime").toString() : "";
        returnMap = stockReportService.getFisheriesBillNumTrayInfo(staTime, endTime, pageNumber, pageSize);
        if (returnMap == null) {
            returnMap = new HashMap<String, Object>();
        }
        return returnMap;
    }

    /**
     * 冻肉出入库统计
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "fisherexcel")
    @ResponseBody
    public void fisherexcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> returnMap = null;
        List<Map<String, Object>> getList = null;
        String staTime = request.getParameter("starTime") != null ? request.getParameter("starTime").toString() : "";
        String endTime = request.getParameter("endTime") != null ? request.getParameter("endTime").toString() : "";
        returnMap = stockReportService.getFisheriesBillNumTrayInfo(staTime, endTime, 1, 0);
        if (returnMap != null && returnMap.size() > 0) {
            getList = (List<Map<String, Object>>) returnMap.get("rows");
        }
        String formatFileName = URLEncoder.encode("商检水产统计报表" + ".xls", "UTF-8");
        ExcelUtil excelUtil = new ExcelUtil();
        String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
        String srcPath = null;
        String desPath = null;
        if (filePath == null || "".equals(filePath)) {
            filePath = request.getSession().getServletContext().getRealPath("/");
            srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\fisherReportC.xls";
            desPath = filePath + "WEB-INF\\classes\\excelpost\\fisherReportC.xls";
        } else {
            srcPath = filePath + "exceltemplate\\fisherReportC.xls";
            desPath = filePath + "excelpost\\fisherReportC.xls";
        }
        //System.out.println(srcPath);
        excelUtil.setSrcPath(srcPath);
        excelUtil.setDesPath(desPath);
        excelUtil.setSheetName("Sheet1");
        excelUtil.getSheet();
        int starRows = 10;
        if (getList != null && getList.size() > 0) {
            Map<String, Object> getMap = null;
            for (int i = 0; i < getList.size(); i++) {
                getMap = getList.get(i);
                if (getMap != null && getMap.size() > 0) {
                    excelUtil.setCellStrValue(starRows + i, 0, getMap.get("TDATE") != null ? getMap.get("TDATE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 1, getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 2, getMap.get("CIQ_CODE") != null ? getMap.get("CIQ_CODE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 3, getMap.get("CARGO_NAME") != null ? getMap.get("CARGO_NAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 4, getMap.get("ISUM_NET_WEIGHT") != null ? getMap.get("ISUM_NET_WEIGHT").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 5, getMap.get("TRADE_TYPE") != null ? getMap.get("TRADE_TYPE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 6, getMap.get("LAB") != null ? getMap.get("LAB").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 7, getMap.get("STOCK_TIME") != null ? getMap.get("STOCK_TIME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 8, getMap.get("CERTIFICATE_TIME") != null ? getMap.get("CERTIFICATE_TIME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 9, String.valueOf(Double.valueOf(getMap.get("PIECE") != null ? getMap.get("PIECE").toString() : "0").longValue()));
                    excelUtil.setCellStrValue(starRows + i, 10, getMap.get("STOCK_NAME") != null ? getMap.get("STOCK_NAME").toString() : "");
                    excelUtil.setCellDoubleValue(starRows + i, 11, getMap.get("NUM") != null ? Double.valueOf(getMap.get("NUM").toString()) : 0d);
                }
            }
        }
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
    
   /**
    * 在库明细查询请求接收
    * @param obj
    * @param request
    * @return
    */
    @RequestMapping(value = "jsonReport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getJsonData(@Valid @ModelAttribute @RequestBody BisOutStock obj,HttpServletRequest request) {
    	Page<Stock> page = getPage(request);
    	page = stockReportService.getStockStocks(page, obj.getNtype(),obj.getIfBonded(), obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchStrTime(), obj.getSearchEndTime(), obj.getLocationType());
    	return getEasyUIData(page);
    }
    
    
    @RequestMapping(value = "companylist", method = RequestMethod.GET)
    public String companylist() {
        return "report/stockCompanyReport";
    }
    
    @RequestMapping(value = "companyReport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getCompanyData(@Valid @ModelAttribute @RequestBody BisOutStock obj,HttpServletRequest request) {
    	Page<Stock> page = getPage(request);
    	page = stockReportService.getCompanyStocks(page,obj.getLocationType(),obj.getSearchItemNum(),obj.getSearchStockIn(),obj.getSearchStrTime(), obj.getSearchEndTime());
    	return getEasyUIData(page);
    }
    
    @RequestMapping(value = "companyExcel")
    @ResponseBody
    public void companyExcel(@Valid @ModelAttribute @RequestBody BisOutStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
            List<Map<String, Object>> getlist = stockReportService.companyExcel(obj.getLocationType(),obj.getSearchItemNum(),obj.getSearchStockIn(),obj.getSearchStrTime(), obj.getSearchEndTime());
            if(null==getlist||getlist.size()==0){
            	return;
            }
            ExcelUtil excelUtil = new ExcelUtil();
            String formatFileName = URLEncoder.encode("报关报检明细" + ".xls", "UTF-8");
            String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
            String srcPath = null;
            String desPath = null;
            if (filePath == null || "".equals(filePath)) {
                filePath = request.getSession().getServletContext().getRealPath("/");
                srcPath = filePath + "WEB-INF" + File.separator + "classes" + File.separator + "exceltemplate" + File.separator + "companylist.xls";
                desPath = filePath + "WEB-INF" + File.separator + "classes" + File.separator + "excelpost" + File.separator + "companylist.xls";
            } else {
                srcPath = filePath + "exceltemplate" + File.separator + "companylist.xls";
            	desPath = filePath + "excelpost" + File.separator + "companylist.xls";
            }
            excelUtil.setSrcPath(srcPath);
            excelUtil.setDesPath(desPath);
            excelUtil.setSheetName("Sheet1");
            excelUtil.getSheet();
            //加载数据
            int starRows = 10;//数据开始填充行数
            int addlank = 0;//列数添加数
            if (getlist != null && getlist.size() > 0) {
                for (int i = 0; i < getlist.size(); i++) {
                	Map<String, Object> getMap = getlist.get(i);
                	excelUtil.setCellStrValue(starRows + i, 0 + addlank, getMap.get("BGDHDATE") != null ? getMap.get("BGDHDATE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 1 + addlank, getMap.get("REPORTTYPE") != null ? getMap.get("REPORTTYPE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 2 + addlank, getMap.get("CONTACTCODE") != null ? getMap.get("CONTACTCODE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 3 + addlank, getMap.get("CLIENTNAME") != null ? getMap.get("CLIENTNAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 4 + addlank, getMap.get("SHF") != null ? getMap.get("SHF").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 5 + addlank, getMap.get("BILLCODE") != null ? getMap.get("BILLCODE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 6 + addlank, getMap.get("COMPANYNAME") != null ? getMap.get("COMPANYNAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 7 + addlank, getMap.get("COMPANYNUM") != null ? getMap.get("COMPANYNUM").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 8 + addlank, getMap.get("CIQNAME") != null ? getMap.get("CIQNAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 9 + addlank, getMap.get("CIQNUM") != null ? getMap.get("CIQNUM").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 10 + addlank, getMap.get("ORGNAME") != null ? getMap.get("ORGNAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 11 + addlank, getMap.get("SPNUM") != null ? getMap.get("SPNUM").toString() : ""); 
                    excelUtil.setCellStrValue(starRows + i, 12 + addlank, getMap.get("REMARK") != null ? getMap.get("REMARK").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 13 + addlank, getMap.get("CREATEUSER") != null ? getMap.get("CREATEUSER").toString() : "");
                }
            }
            excelUtil.exportToNewFile();
            FileInputStream in = new FileInputStream(new File(desPath));
            int len = 0;
            byte[] buffer = new byte[1024];
            response.reset();
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
            	out.flush();
                out.close();
            }
    }
    
    @RequestMapping(value = "companyPdf")
    @ResponseBody
    public void companyPdf(@Valid @ModelAttribute @RequestBody BisOutStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> getlist = stockReportService.companyExcel(obj.getLocationType(),obj.getSearchItemNum(),obj.getSearchStockIn(),obj.getSearchStrTime(), obj.getSearchEndTime());
        if(null==getlist||getlist.size()==0){
        	return;
        }
        String[] headCN = {"日期","类别","联系单号","存货方","收货方","提单号", "报关企业","报关数量","报检企业", "报检数量","费用结算客户","审批数量","备注","所属客服"};
        String[] headEN = {"Date","Cargo Type","Contact NO.","Stock","Receiving party","B/L NO.","Company","Company Qty","Ciq Company","Ciq Company Qty","Settlement Customer","Approve Qty","Remark","Customer service"};
            String pdfTitle = "报关报检明细";
            StringBuffer pdfCN = new StringBuffer();
            pdfCN.append("日期");
            pdfCN.append("            ");
            pdfCN.append("类别");
            pdfCN.append("    ");
            pdfCN.append("联系单号");
            pdfCN.append("                ");
            pdfCN.append("存货方");
            pdfCN.append("                           ");
            pdfCN.append("收货方");
            pdfCN.append("                           ");
            pdfCN.append("提单号");
            pdfCN.append("            ");
            pdfCN.append("报关企业");
            pdfCN.append("                           ");
            pdfCN.append("报关数量");
            pdfCN.append("     ");
            pdfCN.append("报检企业");
            pdfCN.append("                           ");
            pdfCN.append("报检数量");
            pdfCN.append("     ");
            pdfCN.append("费用结算客户");
            pdfCN.append("                           ");
            pdfCN.append("审批数量");
            pdfCN.append("     ");
            pdfCN.append("备注");
            pdfCN.append("                    ");
            pdfCN.append("所属客服");
            pdfCN.append("            ");
            String path = request.getSession().getServletContext().getRealPath("/");//获取web项目的路径"d://exceltemplate//syshtm.html";
            String pathHtml = path + "//infosyshtm.html";
            String pathPdf = path + "//infosyspdf.pdf";
            StringBuffer sbHtml = new StringBuffer();

            sbHtml.append("<div  style=\"height:5px;\"></div>");
            sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
            //填充标题头
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headCN) {
                if ("备注".equals(lab)||"存货方".equals(lab)||"收货方".equals(lab)||"报关企业".equals(lab)||"报检企业".equals(lab)||"费用结算客户".equals(lab)) {
                    sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
                }else {
                    sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                }
            }
            sbHtml.append("</tr>");
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headEN) {
            	if ("Remark".equals(lab)||"Stock".equals(lab)||"Receiving party".equals(lab)||"Company".equals(lab)||"Ciq Company".equals(lab)||"Settlement Customer".equals(lab)) {
                    sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
                }else {
                    sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                }
            }
            sbHtml.append("</tr>");
            //填充内容
            if (getlist != null && getlist.size() > 0) {
                for (int i = 0; i < getlist.size(); i++) {
                	Map<String, Object> getMap=getlist.get(i);
                    if (getMap != null && getMap.size() > 0) {
                        sbHtml.append("<tr>");
	                        sbHtml.append("<td>").append(getMap.get("BGDHDATE") != null ? getMap.get("BGDHDATE").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("REPORTTYPE") != null ? getMap.get("REPORTTYPE").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("CONTACTCODE") != null ? getMap.get("CONTACTCODE").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("CLIENTNAME") != null ? getMap.get("CLIENTNAME").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("SHF") != null ? getMap.get("SHF").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("BILLCODE") != null ? getMap.get("BILLCODE").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("COMPANYNAME") != null ? getMap.get("COMPANYNAME").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("COMPANYNUM") != null ? getMap.get("COMPANYNUM").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("CIQNAME") != null ? getMap.get("CIQNAME").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("CIQNUM") != null ? getMap.get("CIQNUM").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("ORGNAME") != null ? getMap.get("ORGNAME").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("SPNUM") != null ? getMap.get("SPNUM").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("REMARK") != null ? getMap.get("REMARK").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("CREATEUSER") != null ? getMap.get("CREATEUSER").toString():"").append("</td>");
                        sbHtml.append("</tr>");
                    }
                }//end for
            }
            sbHtml.append("</table>");
            MyFileUtils html = new MyFileUtils();
            html.setFilePath(pathHtml);
            html.saveStrToFile(CreatPDFUtils.createPdfHtml("报关报检明细", "Inverted box inspection Details", sbHtml.toString()));
            MyPDFUtils.setsDEST(pathPdf);
            MyPDFUtils.setsHTML(pathHtml);
            MyPDFUtils.createPdf(PageSize.A4, pdfCN.toString(), pdfTitle);
            //下载操作
            FileInputStream in = new FileInputStream(new File(pathPdf));
            int len = 0;
            byte[] buffer = new byte[1024];
            String formatFileName = URLEncoder.encode("报关报检明细" + ".pdf", "UTF-8");
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
    
    @RequestMapping(value = "boxCheckList", method = RequestMethod.GET)
    public String boxChecklist() {
        return "report/stockBoxCheckReport";
    }
    
    @RequestMapping(value = "boxCheckReport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getBoxCheckData(@Valid @ModelAttribute @RequestBody BisOutStock obj,HttpServletRequest request) {
    	Page<Stock> page = getPage(request);
    	page = stockReportService.getBoxCheckStocks(page,obj.getIfBonded(), obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(),obj.getSearchStrTime(), obj.getSearchEndTime());
    	return getEasyUIData(page);
    }
    
    @RequestMapping(value = "boxCheckExcel")
    @ResponseBody
    public void boxCheckExcel(@Valid @ModelAttribute @RequestBody BisOutStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
            List<Map<String, Object>> getlist = stockReportService.boxCheckExcel(obj.getIfBonded(),obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(),obj.getSearchStrTime(), obj.getSearchEndTime());
            if(null==getlist||getlist.size()==0){
            	return;
            }
            ExcelUtil excelUtil = new ExcelUtil();
            String formatFileName = URLEncoder.encode("倒箱查验明细" + ".xls", "UTF-8");
            String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
            String srcPath = null;
            String desPath = null;
            if (filePath == null || "".equals(filePath)) {
                filePath = request.getSession().getServletContext().getRealPath("/");
                srcPath = filePath + "WEB-INF" + File.separator + "classes" + File.separator + "exceltemplate" + File.separator + "boxchecklist.xls";
                desPath = filePath + "WEB-INF" + File.separator + "classes" + File.separator + "excelpost" + File.separator + "boxchecklist.xls";
            } else {
                srcPath = filePath + "exceltemplate" + File.separator + "boxchecklist.xls";
            	desPath = filePath + "excelpost" + File.separator + "boxchecklist.xls";
            }
            excelUtil.setSrcPath(srcPath);
            excelUtil.setDesPath(desPath);
            excelUtil.setSheetName("Sheet1");
            excelUtil.getSheet();
            //加载数据
            int starRows = 10;//数据开始填充行数
            int addlank = 0;//列数添加数
            if (getlist != null && getlist.size() > 0) {
                for (int i = 0; i < getlist.size(); i++) {
                	Map<String, Object> getMap = getlist.get(i);
                	excelUtil.setCellStrValue(starRows + i, 0 + addlank, getMap.get("BGDHDATE") != null ? getMap.get("BGDHDATE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 1 + addlank, getMap.get("ISBONDED") != null ? getMap.get("ISBONDED").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 2 + addlank, getMap.get("CLIENTNAME") != null ? getMap.get("CLIENTNAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 3 + addlank, getMap.get("BILLCODE") != null ? getMap.get("BILLCODE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 4 + addlank, getMap.get("CTNNUM") != null ? getMap.get("CTNNUM").toString() : "");
                    excelUtil.setCellDoubleValue(starRows + i,5+ addlank, Double.parseDouble(getMap.get("NOWNUM") != null ? getMap.get("NOWNUM").toString() : "0"));
                    excelUtil.setCellDoubleValue(starRows + i,6+ addlank, Double.parseDouble(getMap.get("ALLNET") != null ? getMap.get("ALLNET").toString() : "0"));
                    excelUtil.setCellDoubleValue(starRows + i,7+ addlank, Double.parseDouble(getMap.get("ALLGROSS") != null ? getMap.get("ALLGROSS").toString() : "0"));
                    excelUtil.setCellStrValue(starRows + i, 8 + addlank, getMap.get("BIGNAME") != null ? getMap.get("BIGNAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 9 + addlank, getMap.get("SIMNAME") != null ? getMap.get("SIMNAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 10 + addlank, getMap.get("CARGONAME") != null ? getMap.get("CARGONAME").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 11 + addlank, getMap.get("CONTACTCODE") != null ? getMap.get("CONTACTCODE").toString() : "");
                    excelUtil.setCellStrValue(starRows + i, 12 + addlank, getMap.get("CREATEUSER") != null ? getMap.get("CREATEUSER").toString() : "");                   
                }
            }
            excelUtil.exportToNewFile();
            FileInputStream in = new FileInputStream(new File(desPath));
            int len = 0;
            byte[] buffer = new byte[1024];
            response.reset();
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
            	out.flush();
                out.close();
            }
    }
    
    @RequestMapping(value = "boxCheckPdf")
    @ResponseBody
    public void boxCheckPdf(@Valid @ModelAttribute @RequestBody BisOutStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	List<Map<String, Object>> getlist = stockReportService.boxCheckExcel(obj.getIfBonded(),obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(),obj.getSearchStrTime(), obj.getSearchEndTime());
        if(null==getlist||getlist.size()==0){
        	return;
        }
        String[] headCN = {"日期","类别","客户名称","提单号", "MR/集装箱号","件数","总净重(KGS)", "总毛重(KGS)","大类","小类","货物描述","联系单号","所属客服"};
        String[] headEN = {"Date","Cargo Type","Customer","B/L NO.","MR/CTN NO.","Qty","Net Weight","Gross Weight","Class","Sub Class","Description of cargo","Contact NO.","Customer service"};
            String pdfTitle = "在库明细";
            StringBuffer pdfCN = new StringBuffer();
            pdfCN.append("日期");
            pdfCN.append("            ");
            pdfCN.append("类别");
            pdfCN.append("    ");
            pdfCN.append("客户名称");
            pdfCN.append("                ");
            pdfCN.append("提单号");
            pdfCN.append("            ");
            pdfCN.append("MR/集装箱号");
            pdfCN.append("            ");
            pdfCN.append("件数");
            pdfCN.append("      ");
            pdfCN.append("总净重(KGS)");
            pdfCN.append("            ");
            pdfCN.append("总毛重(KGS)");
            pdfCN.append("            ");
            pdfCN.append("大类");
            pdfCN.append("      ");
            pdfCN.append("小类");
            pdfCN.append("      ");
            pdfCN.append("货物描述");
            pdfCN.append("                           ");
            pdfCN.append("联系单号");
            pdfCN.append("                ");
            pdfCN.append("所属客服");
            pdfCN.append("            ");
            String path = request.getSession().getServletContext().getRealPath("/");//获取web项目的路径"d://exceltemplate//syshtm.html";
            String pathHtml = path + "//infosyshtm.html";
            String pathPdf = path + "//infosyspdf.pdf";
            StringBuffer sbHtml = new StringBuffer();

            sbHtml.append("<div  style=\"height:5px;\"></div>");
            sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
            //填充标题头
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headCN) {
                if ("货物描述".equals(lab)||"客户名称".equals(lab)||"提单号".equals(lab)||"MR/集装箱号".equals(lab)||"联系单号".equals(lab)) {
                    sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
                }else {
                    sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                }
            }
            sbHtml.append("</tr>");
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headEN) {
                sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
            }
            sbHtml.append("</tr>");
            //填充内容
            if (getlist != null && getlist.size() > 0) {
                for (int i = 0; i < getlist.size(); i++) {
                	Map<String, Object> getMap=getlist.get(i);
                    if (getMap != null && getMap.size() > 0) {
                        sbHtml.append("<tr>");
	                        sbHtml.append("<td>").append(getMap.get("BGDHDATE") != null ? getMap.get("BGDHDATE").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("ISBONDED") != null ? getMap.get("ISBONDED").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("CLIENTNAME") != null ? getMap.get("CLIENTNAME").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("BILLCODE") != null ? getMap.get("BILLCODE").toString() : "").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("CTNNUM") != null ? getMap.get("CTNNUM").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("NOWNUM") != null ? getMap.get("NOWNUM").toString() : "0"))).append("</td>");
	                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("ALLNET") != null ? getMap.get("ALLNET").toString() : "0"))).append("</td>");
	                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("ALLGROSS") != null ? getMap.get("ALLGROSS").toString() : "0"))).append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("BIGNAME") != null ? getMap.get("BIGNAME").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("SIMNAME") != null ? getMap.get("SIMNAME").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("CARGONAME") != null ? getMap.get("CARGONAME").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("CONTACTCODE") != null ? getMap.get("CONTACTCODE").toString():"").append("</td>");
	                        sbHtml.append("<td>").append(getMap.get("CREATEUSER") != null ? getMap.get("CREATEUSER").toString():"").append("</td>");
                        sbHtml.append("</tr>");
                    }
                }//end for
            }
            sbHtml.append("</table>");
            MyFileUtils html = new MyFileUtils();
            html.setFilePath(pathHtml);
            html.saveStrToFile(CreatPDFUtils.createPdfHtml("倒箱查验明细", "Inverted box inspection Details", sbHtml.toString()));
            MyPDFUtils.setsDEST(pathPdf);
            MyPDFUtils.setsHTML(pathHtml);
            MyPDFUtils.createPdf(PageSize.A4, pdfCN.toString(), pdfTitle);
            //下载操作
            FileInputStream in = new FileInputStream(new File(pathPdf));
            int len = 0;
            byte[] buffer = new byte[1024];
            String formatFileName = URLEncoder.encode("倒箱查验明细" + ".pdf", "UTF-8");
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

    
    
    /**
     * 出入库明细查询请求接收
     * @param obj
     * @param request
     * @return
     */
    @RequestMapping(value = "inoutJson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getInOutJsonData(@Valid @ModelAttribute @RequestBody BisOutStock obj,HttpServletRequest request) {
    	Page<Stock> page = getPage(request);
    	page = stockReportService.getInOutStockStocks(page,obj.getIfBonded(),obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchStrTime(), obj.getSearchEndTime());
    	return getEasyUIData(page);
    }

	

    
    
}
